package br.edu.ads.reciclagem.repositorio;

import br.edu.ads.reciclagem.infra.ConexaoBancoDados;
import br.edu.ads.reciclagem.modelo.Doador;
import br.edu.ads.reciclagem.modelo.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoadorDAO implements Repositorio<Doador> {

    private static final String SQL_INSERIR =
            "INSERT INTO doador (nome_completo, email, telefone, cep, logradouro, numero, "
                    + "complemento, bairro, cidade, uf) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_ATUALIZAR =
            "UPDATE doador SET nome_completo = ?, email = ?, telefone = ?, cep = ?, "
                    + "logradouro = ?, numero = ?, complemento = ?, bairro = ?, cidade = ?, uf = ? "
                    + "WHERE id = ?";

    private static final String SQL_EXCLUIR = "DELETE FROM doador WHERE id = ?";

    private static final String SQL_BUSCAR_ID = "SELECT * FROM doador WHERE id = ?";

    private static final String SQL_BUSCAR_EMAIL = "SELECT * FROM doador WHERE email = ?";

    private static final String SQL_LISTAR = "SELECT * FROM doador ORDER BY nome_completo";

    private final ConexaoBancoDados conexao;

    public DoadorDAO(ConexaoBancoDados conexao) {
        this.conexao = conexao;
    }

    @Override
    public Doador salvar(Doador doador) throws PersistenciaException {
        try (Connection c = conexao.obterConexao();
             PreparedStatement ps = c.prepareStatement(SQL_INSERIR, Statement.RETURN_GENERATED_KEYS)) {
            preencher(ps, doador);
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    doador.setId(chaves.getLong(1));
                }
            }
            return doador;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new PersistenciaException("Já existe um doador cadastrado com este e-mail.", e);
            }
            throw new PersistenciaException("Erro ao salvar doador: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizar(Doador doador) throws PersistenciaException {
        try (Connection c = conexao.obterConexao();
             PreparedStatement ps = c.prepareStatement(SQL_ATUALIZAR)) {
            preencher(ps, doador);
            ps.setLong(11, doador.getId());
            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("Doador não encontrado para atualização.");
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new PersistenciaException("Já existe outro doador cadastrado com este e-mail.", e);
            }
            throw new PersistenciaException("Erro ao atualizar doador: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean excluir(Long id) throws PersistenciaException {
        try (Connection c = conexao.obterConexao();
             PreparedStatement ps = c.prepareStatement(SQL_EXCLUIR)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                throw new PersistenciaException("Não é possível excluir: o doador possui itens "
                        + "eletrônicos registrados (o histórico de descartes é preservado).", e);
            }
            throw new PersistenciaException("Erro ao excluir doador: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Doador> buscarPorId(Long id) throws PersistenciaException {
        try (Connection c = conexao.obterConexao();
             PreparedStatement ps = c.prepareStatement(SQL_BUSCAR_ID)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar doador: " + e.getMessage(), e);
        }
    }

    public Optional<Doador> buscarPorEmail(String email) throws PersistenciaException {
        try (Connection c = conexao.obterConexao();
             PreparedStatement ps = c.prepareStatement(SQL_BUSCAR_EMAIL)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar doador por e-mail: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Doador> listarTodos() throws PersistenciaException {
        List<Doador> doadores = new ArrayList<>();
        try (Connection c = conexao.obterConexao();
             PreparedStatement ps = c.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                doadores.add(mapear(rs));
            }
            return doadores;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar doadores: " + e.getMessage(), e);
        }
    }

    private void preencher(PreparedStatement ps, Doador d) throws SQLException {
        Endereco e = d.getEndereco();
        ps.setString(1, d.getNomeCompleto());
        ps.setString(2, d.getEmail());
        ps.setString(3, d.getTelefone());
        ps.setString(4, e.getCep());
        ps.setString(5, e.getLogradouro());
        ps.setString(6, e.getNumero());
        ps.setString(7, e.getComplemento());
        ps.setString(8, e.getBairro());
        ps.setString(9, e.getCidade());
        ps.setString(10, e.getUf());
    }

    private Doador mapear(ResultSet rs) throws SQLException {
        Endereco endereco = new Endereco(
                rs.getString("cep"),
                rs.getString("logradouro"),
                rs.getString("numero"),
                rs.getString("complemento"),
                rs.getString("bairro"),
                rs.getString("cidade"),
                rs.getString("uf")
        );
        Timestamp cadastro = rs.getTimestamp("data_cadastro");
        return new Doador(
                rs.getLong("id"),
                cadastro == null ? null : cadastro.toLocalDateTime(),
                rs.getString("nome_completo"),
                rs.getString("email"),
                rs.getString("telefone"),
                endereco
        );
    }
}
