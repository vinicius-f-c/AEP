package br.edu.ads.reciclagem.modelo;

public enum EstadoConservacao {

    OTIMO("Ótimo"),
    BOM("Bom"),
    REGULAR("Regular"),
    RUIM("Ruim"),
    SUCATA("Sucata");

    private final String rotulo;

    EstadoConservacao(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }

    @Override
    public String toString() {
        return rotulo;
    }
}
