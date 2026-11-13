package br.edu.ads.reciclagem.modelo;

import java.time.LocalDateTime;

public abstract class Pessoa extends EntidadeBase {

    private String nomeCompleto;
    private String email;
    private String telefone;

    protected Pessoa() {
    }

    protected Pessoa(Long id, LocalDateTime dataCadastro,
                     String nomeCompleto, String email, String telefone) {
        super(id, dataCadastro);
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.telefone = telefone;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public void validar() {
        if (nomeCompleto == null || nomeCompleto.isBlank()) {
            throw new IllegalArgumentException("Nome completo é obrigatório.");
        }
        if (email == null || !email.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        if (telefone == null || !telefone.replaceAll("\\D", "").matches("\\d{10,11}")) {
            throw new IllegalArgumentException("Telefone celular inválido (use DDD + número).");
        }
    }

    @Override
    public String descricaoResumida() {
        return nomeCompleto + " <" + email + ">";
    }
}
