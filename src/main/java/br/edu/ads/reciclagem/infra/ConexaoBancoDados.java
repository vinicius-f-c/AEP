package br.edu.ads.reciclagem.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public final class ConexaoBancoDados {

    private static final String URL_PADRAO =
            "jdbc:mysql://localhost:3306/reciclagem_eletronicos"
                    + "?useSSL=false&allowPublicKeyRetrieval=true"
                    + "&serverTimezone=America/Sao_Paulo&characterEncoding=UTF-8";
    private static final String USUARIO_PADRAO = "root";
    private static final String SENHA_PADRAO = "";

    private final String url;
    private final String usuario;
    private final String senha;

    public ConexaoBancoDados() {
        this(
                lerAmbiente("DB_URL", URL_PADRAO),
                lerAmbiente("DB_USER", USUARIO_PADRAO),
                lerAmbiente("DB_PASS", SENHA_PADRAO)
        );
    }

    public ConexaoBancoDados(String url, String usuario, String senha) {
        this.url = Objects.requireNonNull(url, "url");
        this.usuario = Objects.requireNonNull(usuario, "usuario");
        this.senha = Objects.requireNonNull(senha, "senha");
    }

    public Connection obterConexao() throws SQLException {
        return DriverManager.getConnection(url, usuario, senha);
    }

    public boolean testarConexao() {
        try (Connection conexao = obterConexao()) {
            return conexao.isValid(3);
        } catch (SQLException e) {
            return false;
        }
    }

    private static String lerAmbiente(String chave, String padrao) {
        String valor = System.getenv(chave);
        return (valor == null || valor.isBlank()) ? padrao : valor;
    }
}
