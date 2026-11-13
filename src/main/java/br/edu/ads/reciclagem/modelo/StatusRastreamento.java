package br.edu.ads.reciclagem.modelo;

public enum StatusRastreamento {

    AGUARDANDO_COLETA("Aguardando coleta"),
    COLETADO("Coletado"),
    EM_TRIAGEM("Em triagem"),
    EM_RECICLAGEM("Em reciclagem"),
    RECICLADO("Reciclado");

    private final String rotulo;

    StatusRastreamento(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }

    public boolean podeAvancarPara(StatusRastreamento destino) {
        return destino != null && destino.ordinal() > this.ordinal();
    }

    public boolean permiteExclusao() {
        return this == AGUARDANDO_COLETA;
    }

    @Override
    public String toString() {
        return rotulo;
    }
}
