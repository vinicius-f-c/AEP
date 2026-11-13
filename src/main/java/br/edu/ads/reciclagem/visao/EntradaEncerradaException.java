package br.edu.ads.reciclagem.visao;

/** Lançada quando a entrada do teclado é encerrada (Ctrl+D / Ctrl+Z) no meio de uma leitura. */
public class EntradaEncerradaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntradaEncerradaException() {
        super("Entrada encerrada.");
    }
}
