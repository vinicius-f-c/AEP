package br.edu.ads.reciclagem.modelo;

/** Regras de validação reutilizadas pelas entidades (obrigatoriedade e tamanho das colunas). */
final class Validacao {

    private Validacao() {
    }

    static void exigir(String valor, String campo, int max) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " é obrigatório.");
        }
        if (valor.trim().length() > max) {
            throw new IllegalArgumentException(campo + " deve ter no máximo " + max + " caracteres.");
        }
    }

    static void opcional(String valor, String campo, int max) {
        if (valor != null && valor.trim().length() > max) {
            throw new IllegalArgumentException(campo + " deve ter no máximo " + max + " caracteres.");
        }
    }
}
