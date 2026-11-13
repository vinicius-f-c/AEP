package br.edu.ads.reciclagem.controlador;

import br.edu.ads.reciclagem.modelo.Doador;
import br.edu.ads.reciclagem.modelo.Endereco;
import br.edu.ads.reciclagem.modelo.EstadoConservacao;
import br.edu.ads.reciclagem.modelo.ItemEletronico;
import br.edu.ads.reciclagem.modelo.StatusRastreamento;
import br.edu.ads.reciclagem.repositorio.PersistenciaException;
import br.edu.ads.reciclagem.servico.DoadorService;
import br.edu.ads.reciclagem.servico.ItemEletronicoService;
import br.edu.ads.reciclagem.servico.RegraNegocioException;
import br.edu.ads.reciclagem.visao.ConsoleView;

public class EcoTechController {

    private final ConsoleView view;
    private final DoadorService doadorService;
    private final ItemEletronicoService itemService;

    public EcoTechController(ConsoleView view, DoadorService doadorService,
                             ItemEletronicoService itemService) {
        this.view = view;
        this.doadorService = doadorService;
        this.itemService = itemService;
    }

    public void executar() {
        boolean ativo = true;
        while (ativo) {
            view.exibirMenu();
            long opcao = view.lerLong("Escolha");
            try {
                switch ((int) opcao) {
                    case 1 -> cadastrarDoador();
                    case 2 -> listarDoadores();
                    case 3 -> registrarItem();
                    case 4 -> atualizarStatus();
                    case 5 -> consultarHistorico();
                    case 6 -> excluirItem();
                    case 0 -> ativo = false;
                    default -> view.erro("Opção inválida.");
                }
            } catch (IllegalArgumentException | RegraNegocioException e) {
                view.erro(e.getMessage());
            } catch (PersistenciaException e) {
                view.erro(e.getMessage());
            }
        }
        view.mensagem("Encerrando o EcoTech. Até logo!");
    }

    private void cadastrarDoador() throws PersistenciaException {
        view.titulo("Cadastro de doador");
        String nome = view.lerTexto("Nome completo");
        String email = view.lerTexto("E-mail");
        String telefone = view.lerTexto("Telefone celular (com DDD)");
        view.mensagem("Endereço completo para coleta:");
        String cep = view.lerTexto("CEP");
        String logradouro = view.lerTexto("Logradouro");
        String numero = view.lerTexto("Número");
        String complemento = view.lerTexto("Complemento (opcional)");
        String bairro = view.lerTexto("Bairro");
        String cidade = view.lerTexto("Cidade");
        String uf = view.lerTexto("UF");

        Endereco endereco = new Endereco(cep, logradouro, numero,
                complemento.isBlank() ? null : complemento, bairro, cidade, uf);
        Doador doador = doadorService.cadastrar(new Doador(nome, email, telefone, endereco));
        view.mensagem("Doador cadastrado com sucesso. ID: " + doador.getId());
    }

    private void listarDoadores() throws PersistenciaException {
        view.titulo("Doadores cadastrados");
        view.exibirDoadores(doadorService.listar());
    }

    private void registrarItem() throws PersistenciaException {
        view.titulo("Registro de item eletrônico");
        Long doadorId = view.lerLong("ID do doador");
        String tipo = view.lerTexto("Tipo de equipamento (ex.: notebook, celular)");
        String marca = view.lerTexto("Marca");
        String modelo = view.lerTexto("Modelo");
        EstadoConservacao estado = view.escolherOpcao("Estado de conservação:", EstadoConservacao.values());

        ItemEletronico item = itemService.registrar(new ItemEletronico(doadorId, tipo, marca, modelo, estado));
        view.mensagem("Item registrado com sucesso. ID: " + item.getId()
                + " | Status: " + item.getStatusRastreamento());
    }

    private void atualizarStatus() throws PersistenciaException {
        view.titulo("Atualização de status de rastreamento");
        Long itemId = view.lerLong("ID do item");
        ItemEletronico atual = itemService.buscarPorId(itemId);
        view.mensagem("Item: " + atual.descricaoResumida());
        StatusRastreamento novo = view.escolherOpcao("Novo status:", StatusRastreamento.values());
        ItemEletronico atualizado = itemService.atualizarStatus(itemId, novo);
        view.mensagem("Status atualizado para: " + atualizado.getStatusRastreamento());
    }

    private void consultarHistorico() throws PersistenciaException {
        view.titulo("Histórico de descartes do doador");
        Long doadorId = view.lerLong("ID do doador");
        Doador doador = doadorService.buscarPorId(doadorId);
        view.mensagem("Doador: " + doador.descricaoResumida());
        view.exibirItens(itemService.consultarHistorico(doadorId));
    }

    private void excluirItem() throws PersistenciaException {
        view.titulo("Exclusão de item cadastrado incorretamente");
        Long itemId = view.lerLong("ID do item");
        ItemEletronico item = itemService.buscarPorId(itemId);
        view.mensagem("Item: " + item.descricaoResumida());
        String confirmacao = view.lerTexto("Confirma a exclusão? (S/N)");
        if (confirmacao.equalsIgnoreCase("S")) {
            itemService.excluirNaoColetado(itemId);
            view.mensagem("Item excluído com sucesso.");
        } else {
            view.mensagem("Exclusão cancelada.");
        }
    }
}
