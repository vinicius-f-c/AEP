package br.edu.ads.reciclagem.visao;

import br.edu.ads.reciclagem.modelo.Doador;
import br.edu.ads.reciclagem.modelo.ItemEletronico;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleView {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Scanner scanner = new Scanner(System.in);

    public void exibirMenu() {
        System.out.println();
        System.out.println("===== EcoTech - Gestão de Resíduos Eletrônicos =====");
        System.out.println("--- Doadores ---");
        System.out.println(" 1 - Cadastrar doador");
        System.out.println(" 2 - Listar doadores");
        System.out.println(" 3 - Buscar doador por ID");
        System.out.println(" 4 - Atualizar doador");
        System.out.println(" 5 - Excluir doador");
        System.out.println("--- Itens eletrônicos ---");
        System.out.println(" 6 - Registrar item para descarte");
        System.out.println(" 7 - Listar todos os itens");
        System.out.println(" 8 - Buscar item por ID");
        System.out.println(" 9 - Corrigir dados de um item");
        System.out.println("10 - Atualizar status de rastreamento");
        System.out.println("11 - Consultar histórico de descartes de um doador");
        System.out.println("12 - Excluir item cadastrado incorretamente");
        System.out.println(" 0 - Sair");
    }

    public void titulo(String texto) {
        System.out.println();
        System.out.println("--- " + texto + " ---");
    }

    public void mensagem(String texto) {
        System.out.println(texto);
    }

    public void erro(String texto) {
        System.out.println("[ERRO] " + texto);
    }

    public String lerTexto(String rotulo) {
        System.out.print(rotulo + ": ");
        try {
            return scanner.nextLine().trim();
        } catch (NoSuchElementException e) {
            throw new EntradaEncerradaException();
        }
    }

    public Long lerLong(String rotulo) {
        while (true) {
            String texto = lerTexto(rotulo);
            try {
                return Long.parseLong(texto);
            } catch (NumberFormatException e) {
                erro("Digite um número válido.");
            }
        }
    }

    public <E extends Enum<E>> E escolherOpcao(String titulo, E[] opcoes) {
        System.out.println(titulo);
        for (int i = 0; i < opcoes.length; i++) {
            System.out.println("  " + (i + 1) + " - " + opcoes[i]);
        }
        while (true) {
            long escolha = lerLong("Opção");
            if (escolha >= 1 && escolha <= opcoes.length) {
                return opcoes[(int) escolha - 1];
            }
            erro("Escolha um número entre 1 e " + opcoes.length + ".");
        }
    }

    public <E extends Enum<E>> E escolherOpcaoOuManter(String titulo, E[] opcoes, E atual) {
        System.out.println(titulo + " (Enter mantém: " + atual + ")");
        for (int i = 0; i < opcoes.length; i++) {
            System.out.println("  " + (i + 1) + " - " + opcoes[i]);
        }
        while (true) {
            String texto = lerTexto("Opção");
            if (texto.isEmpty()) {
                return atual;
            }
            try {
                long escolha = Long.parseLong(texto);
                if (escolha >= 1 && escolha <= opcoes.length) {
                    return opcoes[(int) escolha - 1];
                }
                erro("Escolha um número entre 1 e " + opcoes.length + ".");
            } catch (NumberFormatException e) {
                erro("Digite um número válido ou Enter para manter.");
            }
        }
    }

    public void exibirDoador(Doador d) {
        System.out.println("#" + d.getId() + " | " + d.getNomeCompleto()
                + " | " + d.getEmail() + " | " + d.getTelefone());
        System.out.println("     Coleta: " + d.getEndereco().descricaoCompleta());
    }

    public void exibirDoadores(List<Doador> doadores) {
        if (doadores.isEmpty()) {
            System.out.println("Nenhum doador cadastrado.");
            return;
        }
        doadores.forEach(this::exibirDoador);
    }

    public void exibirItens(List<ItemEletronico> itens) {
        if (itens.isEmpty()) {
            System.out.println("Nenhum item encontrado.");
            return;
        }
        System.out.printf("%-5s %-8s %-16s %-14s %-18s %-10s %-18s %s%n",
                "ID", "Doador", "Tipo", "Marca", "Modelo", "Estado", "Status", "Cadastro");
        for (ItemEletronico i : itens) {
            String cadastro = i.getDataCadastro() == null ? "-" : i.getDataCadastro().format(FORMATO_DATA);
            System.out.printf("%-5d %-8d %-16s %-14s %-18s %-10s %-18s %s%n",
                    i.getId(), i.getDoadorId(), i.getTipoEquipamento(), i.getMarca(), i.getModelo(),
                    i.getEstadoConservacao(), i.getStatusRastreamento(), cadastro);
        }
    }
}
