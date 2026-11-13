package br.edu.ads.reciclagem.modelo;

public final class Endereco implements Validavel {

    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;

    public Endereco() {
    }

    public Endereco(String cep, String logradouro, String numero, String complemento,
                    String bairro, String cidade, String uf) {
        setCep(cep);
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        setUf(uf);
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep == null ? null : cep.replaceAll("\\D", "");
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf == null ? null : uf.trim().toUpperCase();
    }

    @Override
    public void validar() {
        if (cep == null || !cep.matches("\\d{8}")) {
            throw new IllegalArgumentException("CEP inválido (use 8 dígitos).");
        }
        exigir(logradouro, "Logradouro");
        exigir(numero, "Número");
        exigir(bairro, "Bairro");
        exigir(cidade, "Cidade");
        if (uf == null || !uf.matches("[A-Z]{2}")) {
            throw new IllegalArgumentException("UF inválida (use 2 letras).");
        }
    }

    public String descricaoCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append(logradouro).append(", ").append(numero);
        if (complemento != null && !complemento.isBlank()) {
            sb.append(" (").append(complemento).append(")");
        }
        sb.append(" - ").append(bairro).append(", ").append(cidade).append("/").append(uf);
        sb.append(" - CEP ").append(cep);
        return sb.toString();
    }

    private static void exigir(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " é obrigatório.");
        }
    }
}
