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
import br.edu.ads.reciclagem.visao.EntradaEncerradaException;

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
            try {
                view.exibirMenu();
                int opcao = converterOpcao(view.lerLong("Escolha"));
                switch (opcao) {
                    case 1 -> cadastrarDoador();
                    case 2 -> listarDoadores();
                    case 3 -> buscarDoador();
                    case 4 -> atualizarDoador();
                    case 5 -> excluirDoador();
                    case 6 -> registrarItem();
                    case 7 -> listarItens();
                    case 8 -> buscarItem();
                    case 9 -> corrigirItem();
                    case 10 -> atualizarStatus();
                    case 11 -> consultarHistorico();
                    case 12 -> excluirItem();
                    case 0 -> ativo = false;
                    default -> view.erro("Opção inválida.");
                }
            } catch (EntradaEncerradaException e) {
                view.mensagem("\nEntrada encerrada.");
                ativo = false;
            } catch (IllegalArgumentException | RegraNegocioException | PersistenciaException e) {
                view.erro(e.getMessage());
            } catch (RuntimeException e) {
                view.erro("Erro inesperado: " + e.getMessage());
            }
        }
        view.mensagem("Encerrando o EcoTech. Até logo!");
    }

    /** Evita que números muito grandes sejam truncados para uma opção válida ao converter long em int. */
    private static int converterOpcao(long valor) {
        return (valor >= 0 && valor <= Integer.MAX_VALUE) ? (int) valor : -1;
    }

    // ---------------------------- Doador ----------------------------

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

    private void buscarDoador() throws PersistenciaException {
        view.titulo("Buscar doador");
        view.exibirDoador(doadorService.buscarPorId(view.lerLong("ID do doador")));
    }

    private void atualizarDoador() throws PersistenciaException {
        view.titulo("Atualização de doador");
        Doador d = doadorService.buscarPorId(view.lerLong("ID do doador"));
        view.exibirDoador(d);
        view.mensagem("(Pressione Enter para manter o valor atual)");

        String nome = view.lerTexto("Nome completo [" + d.getNomeCompleto() + "]");
        if (!nome.isEmpty()) d.setNomeCompleto(nome);

        String email = view.lerTexto("E-mail [" + d.getEmail() + "]");
        if (!email.isEmpty()) d.setEmail(email);

        String telefone = view.lerTexto("Telefone [" + d.getTelefone() + "]");
        if (!telefone.isEmpty()) d.setTelefone(telefone);

        Endereco e = d.getEndereco();
        String cep = view.lerTexto("CEP [" + e.getCep() + "]");
        if (!cep.isEmpty()) e.setCep(cep);

        String logradouro = view.lerTexto("Logradouro [" + e.getLogradouro() + "]");
        if (!logradouro.isEmpty()) e.setLogradouro(logradouro);

        String numero = view.lerTexto("Número [" + e.getNumero() + "]");
        if (!numero.isEmpty()) e.setNumero(numero);

        String complemento = view.lerTexto("Complemento [" + (e.getComplemento() == null ? "" : e.getComplemento()) + "]");
        if (!complemento.isEmpty()) e.setComplemento(complemento);

        String bairro = view.lerTexto("Bairro [" + e.getBairro() + "]");
        if (!bairro.isEmpty()) e.setBairro(bairro);

        String cidade = view.lerTexto("Cidade [" + e.getCidade() + "]");
        if (!cidade.isEmpty()) e.setCidade(cidade);

        String uf = view.lerTexto("UF [" + e.getUf() + "]");
        if (!uf.isEmpty()) e.setUf(uf);

        doadorService.atualizar(d);
        view.mensagem("Doador atualizado com sucesso.");
    }

    private void excluirDoador() throws PersistenciaException {
        view.titulo("Exclusão de doador");
        Long id = view.lerLong("ID do doador");
        Doador d = doadorService.buscarPorId(id);
        view.exibirDoador(d);
        if (view.lerTexto("Confirma a exclusão? (S/N)").equalsIgnoreCase("S")) {
            doadorService.excluir(id);
            view.mensagem("Doador excluído com sucesso.");
        } else {
            view.mensagem("Exclusão cancelada.");
        }
    }

    // ------------------------- Item eletrônico -------------------------

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

    private void listarItens() throws PersistenciaException {
        view.titulo("Itens eletrônicos cadastrados");
        view.exibirItens(itemService.listar());
    }

    private void buscarItem() throws PersistenciaException {
        view.titulo("Buscar item");
        ItemEletronico item = itemService.buscarPorId(view.lerLong("ID do item"));
        view.exibirItens(java.util.List.of(item));
    }

    private void corrigirItem() throws PersistenciaException {
        view.titulo("Correção de dados do item");
        ItemEletronico item = itemService.buscarPorId(view.lerLong("ID do item"));
        view.mensagem("Item: " + item.descricaoResumida());
        if (!item.podeSerEditado()) {
            throw new RegraNegocioException("Item já coletado (status: " + item.getStatusRastreamento()
                    + "). Só é possível corrigir os dados de itens aguardando coleta.");
        }
        view.mensagem("(Pressione Enter para manter o valor atual)");

        String tipo = view.lerTexto("Tipo de equipamento [" + item.getTipoEquipamento() + "]");
        String marca = view.lerTexto("Marca [" + item.getMarca() + "]");
        String modelo = view.lerTexto("Modelo [" + item.getModelo() + "]");
        EstadoConservacao estado = view.escolherOpcaoOuManter(
                "Estado de conservação:", EstadoConservacao.values(), item.getEstadoConservacao());

        itemService.atualizarDados(item.getId(),
                tipo.isEmpty() ? item.getTipoEquipamento() : tipo,
                marca.isEmpty() ? item.getMarca() : marca,
                modelo.isEmpty() ? item.getModelo() : modelo,
                estado);
        view.mensagem("Dados do item atualizados com sucesso.");
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
        if (view.lerTexto("Confirma a exclusão? (S/N)").equalsIgnoreCase("S")) {
            itemService.excluirNaoColetado(itemId);
            view.mensagem("Item excluído com sucesso.");
        } else {
            view.mensagem("Exclusão cancelada.");
        }
    }
}
