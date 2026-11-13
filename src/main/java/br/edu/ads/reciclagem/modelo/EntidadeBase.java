package br.edu.ads.reciclagem.modelo;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class EntidadeBase implements Validavel {

    private Long id;
    private LocalDateTime dataCadastro;

    protected EntidadeBase() {
    }

    protected EntidadeBase(Long id, LocalDateTime dataCadastro) {
        this.id = id;
        this.dataCadastro = dataCadastro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public boolean isNova() {
        return id == null;
    }

    public abstract String descricaoResumida();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntidadeBase outra = (EntidadeBase) o;
        return id != null && id.equals(outra.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), id);
    }
}
