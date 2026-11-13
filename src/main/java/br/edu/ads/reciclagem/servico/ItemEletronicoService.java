package br.edu.ads.reciclagem.servico;

import br.edu.ads.reciclagem.modelo.EstadoConservacao;
import br.edu.ads.reciclagem.modelo.ItemEletronico;
import br.edu.ads.reciclagem.modelo.StatusRastreamento;
import br.edu.ads.reciclagem.repositorio.DoadorDAO;
import br.edu.ads.reciclagem.repositorio.ItemEletronicoDAO;
import br.edu.ads.reciclagem.repositorio.PersistenciaException;

import java.util.List;

public class ItemEletronicoService {

    private final ItemEletronicoDAO itemDAO;
    private final DoadorDAO doadorDAO;

    public ItemEletronicoService(ItemEletronicoDAO itemDAO, DoadorDAO doadorDAO) {
        this.itemDAO = itemDAO;
        this.doadorDAO = doadorDAO;
    }

    public ItemEletronico registrar(ItemEletronico item) throws PersistenciaException {
        if (item == null) {
            throw new IllegalArgumentException("Item não informado.");
        }
        item.setStatusRastreamento(StatusRastreamento.AGUARDANDO_COLETA);
        item.validar();
        if (doadorDAO.buscarPorId(item.getDoadorId()).isEmpty()) {
            throw new RegraNegocioException("Doador não encontrado (id " + item.getDoadorId() + ").");
        }
        return itemDAO.salvar(item);
    }

    public ItemEletronico atualizarDados(Long itemId, String tipo, String marca, String modelo,
                                         EstadoConservacao estado) throws PersistenciaException {
        ItemEletronico item = buscarPorId(itemId);
        if (!item.podeSerEditado()) {
            throw new RegraNegocioException("Item já coletado (status: " + item.getStatusRastreamento()
                    + "). Só é possível corrigir os dados de itens aguardando coleta.");
        }
        item.setTipoEquipamento(tipo);
        item.setMarca(marca);
        item.setModelo(modelo);
        item.setEstadoConservacao(estado);
        item.validar();
        if (!itemDAO.atualizarDadosSeNaoColetado(item)) {
            throw new RegraNegocioException("O item não pôde ser atualizado porque o status mudou.");
        }
        return item;
    }

    public List<ItemEletronico> listar() throws PersistenciaException {
        return itemDAO.listarTodos();
    }

    public ItemEletronico atualizarStatus(Long itemId, StatusRastreamento novoStatus)
            throws PersistenciaException {
        ItemEletronico item = buscarPorId(itemId);
        if (!item.getStatusRastreamento().podeAvancarPara(novoStatus)) {
            throw new RegraNegocioException("O status só pode avançar. Status atual: "
                    + item.getStatusRastreamento() + ".");
        }
        if (!itemDAO.atualizarStatus(itemId, novoStatus)) {
            throw new RegraNegocioException("Não foi possível atualizar o item " + itemId + ".");
        }
        item.setStatusRastreamento(novoStatus);
        return item;
    }

    public List<ItemEletronico> consultarHistorico(Long doadorId) throws PersistenciaException {
        if (doadorDAO.buscarPorId(doadorId).isEmpty()) {
            throw new RegraNegocioException("Doador não encontrado (id " + doadorId + ").");
        }
        return itemDAO.listarPorDoador(doadorId);
    }

    public void excluirNaoColetado(Long itemId) throws PersistenciaException {
        ItemEletronico item = buscarPorId(itemId);
        if (!item.podeSerExcluido()) {
            throw new RegraNegocioException("Item já coletado (status: "
                    + item.getStatusRastreamento() + "). Só é possível excluir itens aguardando coleta.");
        }
        if (!itemDAO.excluirSeNaoColetado(itemId)) {
            throw new RegraNegocioException("O item não pôde ser excluído porque o status mudou.");
        }
    }

    public ItemEletronico buscarPorId(Long itemId) throws PersistenciaException {
        return itemDAO.buscarPorId(itemId)
                .orElseThrow(() -> new RegraNegocioException("Item não encontrado (id " + itemId + ")."));
    }
}
