package br.edu.ads.reciclagem.repositorio;

import br.edu.ads.reciclagem.infra.ConexaoBancoDados;
import br.edu.ads.reciclagem.modelo.EstadoConservacao;
import br.edu.ads.reciclagem.modelo.ItemEletronico;
import br.edu.ads.reciclagem.modelo.StatusRastreamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemEletronicoDAO implements Repositorio<ItemEletronico> {

    private static final String SQL_INSERIR =
            "INSERT INTO item_eletronico (doador_id, tipo_equipamento, marca, modelo, "
                    + "estado_conservacao, status_rastreamento) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_ATUALIZAR =
            "UPDATE item_eletronico SET tipo_equipamento = ?, marca = ?, modelo = ?, "
                    + "estado_conservacao = ?, status_rastreamento = ? WHERE id = ?";

    private static final String SQL_ATUALIZAR_DADOS_NAO_COLETADO =
            "UPDATE item_eletronico SET tipo_equipamento = ?, marca = ?, modelo = ?, "
                    + "estado_conservacao = ? WHERE id = ? AND status_rastreamento = 'AGUARDANDO_COLETA'";

    private static final String SQL_ATUALIZAR_STATUS =
            "UPDATE item_eletronico SET status_rastreamento = ? WHERE id = ?";

    private static final String SQL_EXCLUIR = "DELETE FROM item_eletronico WHERE id = ?";

    private static final String SQL_EXCLUIR_NAO_COLETADO =
            "DELETE FROM item_eletronico WHERE id = ? AND status_rastreamento = 'AGUARDANDO_COLETA'";

    private static final String SQL_BUSCAR_ID = "SELECT * FROM item_eletronico WHERE id = ?";

    private static final String SQL_LISTAR = "SELECT * FROM item_eletronico ORDER BY id";

    private static final String SQL_LISTAR_POR_DOADOR =
            "SELECT * FROM item_eletronico WHERE doador_id = ? ORDER BY data_cadastro DESC, id DESC";

    private final ConexaoBancoDados conexao;

    public ItemEletronicoDAO(ConexaoBancoDados conexao) {
        this.conexao = conexao;
    }

    @Override
    public ItemEletronico salvar(ItemEletronico item) throws PersistenciaException {
        try (Connection c = conexao.obterConexao();
             PreparedStatement ps = c.prepareStatement(SQL_INSERIR, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, item.getDoadorId());
            ps.setString(2, item.getTipoEquipamento());
            ps.setString(3, item.getMarca());
            ps.setString(4, item.getModelo());
            ps.setString(5, item.getEstadoConservacao().name());
            ps.setString(6, item.getStatusRastreamento().name());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    item.setId(chaves.getLong(1));
                }
            }
            return item;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao salvar item eletrônico: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizar(ItemEletronico item) throws PersistenciaException {
        try (Connection c = conexao.obterConexao();
             PreparedStatement ps = c.prepareStatement(SQL_ATUALIZAR)) {
            ps.setString(1, item.getTipoEquipamento());
            ps.setString(2, item.getMarca());
            ps.setString(3, item.getModelo());
            ps.setString(4, item.getEstadoConservacao().name());
            ps.setString(5, item.getStatusRastreamento().name());
            ps.setLong(6, item.getId());
            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("Item não encontrado para atualização.");
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar item eletrônico: " + e.getMessage(), e);
        }
    }

    public boolean atualizarDadosSeNaoColetado(ItemEletronico item) throws PersistenciaException {
        try (Connection c = conexao.obterConexao();
             PreparedStatement ps = c.prepareStatement(SQL_ATUALIZAR_DADOS_NAO_COLETADO)) {
            ps.setString(1, item.getTipoEquipamento());
            ps.setString(2, item.getMarca());
            ps.setString(3, item.getModelo());
            ps.setString(4, item.getEstadoConservacao().name());
            ps.setLong(5, item.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar dados do item: " + e.getMessage(), e);
        }
    }

    public boolean atualizarStatus(Long id, StatusRastreamento novoStatus) throws PersistenciaException {
        try (Connection c = conexao.obterConexao();
             PreparedStatement ps = c.prepareStatement(SQL_ATUALIZAR_STATUS)) {
            ps.setString(1, novoStatus.name());
            ps.setLong(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar status do item: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean excluir(Long id) throws PersistenciaException {
        try (Connection c = conexao.obterConexao();
             PreparedStatement ps = c.prepareStatement(SQL_EXCLUIR)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao excluir item eletrônico: " + e.getMessage(), e);
        }
    }

    public boolean excluirSeNaoColetado(Long id) throws PersistenciaException {
        try (Connection c = conexao.obterConexao();
             PreparedStatement ps = c.prepareStatement(SQL_EXCLUIR_NAO_COLETADO)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao excluir item eletrônico: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<ItemEletronico> buscarPorId(Long id) throws PersistenciaException {
        try (Connection c = conexao.obterConexao();
             PreparedStatement ps = c.prepareStatement(SQL_BUSCAR_ID)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar item eletrônico: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ItemEletronico> listarTodos() throws PersistenciaException {
        List<ItemEletronico> itens = new ArrayList<>();
        try (Connection c = conexao.obterConexao();
             PreparedStatement ps = c.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                itens.add(mapear(rs));
            }
            return itens;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar itens eletrônicos: " + e.getMessage(), e);
        }
    }

    public List<ItemEletronico> listarPorDoador(Long doadorId) throws PersistenciaException {
        List<ItemEletronico> itens = new ArrayList<>();
        try (Connection c = conexao.obterConexao();
             PreparedStatement ps = c.prepareStatement(SQL_LISTAR_POR_DOADOR)) {
            ps.setLong(1, doadorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    itens.add(mapear(rs));
                }
            }
            return itens;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao consultar histórico do doador: " + e.getMessage(), e);
        }
    }

    private ItemEletronico mapear(ResultSet rs) throws SQLException {
        ItemEletronico item = new ItemEletronico(
                rs.getLong("doador_id"),
                rs.getString("tipo_equipamento"),
                rs.getString("marca"),
                rs.getString("modelo"),
                EstadoConservacao.valueOf(rs.getString("estado_conservacao"))
        );
        item.setId(rs.getLong("id"));
        item.setStatusRastreamento(StatusRastreamento.valueOf(rs.getString("status_rastreamento")));
        Timestamp cadastro = rs.getTimestamp("data_cadastro");
        Timestamp atualizacao = rs.getTimestamp("data_atualizacao");
        item.setDataCadastro(cadastro == null ? null : cadastro.toLocalDateTime());
        item.setDataAtualizacao(atualizacao == null ? null : atualizacao.toLocalDateTime());
        return item;
    }
}
