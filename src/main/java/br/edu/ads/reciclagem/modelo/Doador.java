package br.edu.ads.reciclagem.modelo;

import java.time.LocalDateTime;

public class Doador extends Pessoa {

    private Endereco endereco;

    public Doador() {
    }

  

    public Doador(String nomeCompleto, String email, String telefone, Endereco endereco) {
        super(null, null, nomeCompleto, email, telefone);
        this.endereco = endereco;
    }

    public Doador(Long id, LocalDateTime dataCadastro, String nomeCompleto,
                  String email, String telefone, Endereco endereco) {
        super(id, dataCadastro, nomeCompleto, email, telefone);
        this.endereco = endereco;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    @Override
    public void validar() {
        super.validar();
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço de coleta é obrigatório.");
        }
        endereco.validar();
    }

    @Override
    public String descricaoResumida() {
        return "#" + getId() + " " + getNomeCompleto() + " (" + getEmail() + ")";
    }
}
