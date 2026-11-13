package br.edu.ads.reciclagem.modelo;

import java.time.LocalDateTime;

public class ItemEletronico extends EntidadeBase {

    private Long doadorId;
    private String tipoEquipamento;
    private String marca;
    private String modelo;
    private EstadoConservacao estadoConservacao;
    private StatusRastreamento statusRastreamento = StatusRastreamento.AGUARDANDO_COLETA;
    private LocalDateTime dataAtualizacao;

    public ItemEletronico() {
    }

    public ItemEletronico(Long doadorId, String tipoEquipamento, String marca,
                          String modelo, EstadoConservacao estadoConservacao) {
        this.doadorId = doadorId;
        this.tipoEquipamento = tipoEquipamento;
        this.marca = marca;
        this.modelo = modelo;
        this.estadoConservacao = estadoConservacao;
    }

    public Long getDoadorId() {
        return doadorId;
    }

    public void setDoadorId(Long doadorId) {
        this.doadorId = doadorId;
    }

    public String getTipoEquipamento() {
        return tipoEquipamento;
    }

    public void setTipoEquipamento(String tipoEquipamento) {
        this.tipoEquipamento = tipoEquipamento;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public EstadoConservacao getEstadoConservacao() {
        return estadoConservacao;
    }

    public void setEstadoConservacao(EstadoConservacao estadoConservacao) {
        this.estadoConservacao = estadoConservacao;
    }

    public StatusRastreamento getStatusRastreamento() {
        return statusRastreamento;
    }

    public void setStatusRastreamento(StatusRastreamento statusRastreamento) {
        this.statusRastreamento = statusRastreamento;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public boolean podeSerExcluido() {
        return statusRastreamento != null && statusRastreamento.permiteExclusao();
    }

    /** Os dados cadastrais só podem ser corrigidos enquanto o item não foi coletado. */
    public boolean podeSerEditado() {
        return podeSerExcluido();
    }

    @Override
    public void validar() {
        if (doadorId == null) {
            throw new IllegalArgumentException("O item precisa estar vinculado a um doador.");
        }
        Validacao.exigir(tipoEquipamento, "Tipo de equipamento", 60);
        Validacao.exigir(marca, "Marca", 60);
        Validacao.exigir(modelo, "Modelo", 80);
        if (estadoConservacao == null) {
            throw new IllegalArgumentException("Estado de conservação é obrigatório.");
        }
        if (statusRastreamento == null) {
            throw new IllegalArgumentException("Status de rastreamento é obrigatório.");
        }
    }

    @Override
    public String descricaoResumida() {
        return "#" + getId() + " " + tipoEquipamento + " " + marca + " " + modelo
                + " [" + statusRastreamento + "]";
    }
}
