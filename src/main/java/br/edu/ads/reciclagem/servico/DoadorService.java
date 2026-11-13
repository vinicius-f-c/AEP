package br.edu.ads.reciclagem.servico;

import br.edu.ads.reciclagem.modelo.Doador;
import br.edu.ads.reciclagem.repositorio.DoadorDAO;
import br.edu.ads.reciclagem.repositorio.PersistenciaException;

import java.util.List;

public class DoadorService {

    private final DoadorDAO doadorDAO;

    public DoadorService(DoadorDAO doadorDAO) {
        this.doadorDAO = doadorDAO;
    }

    public Doador cadastrar(Doador doador) throws PersistenciaException {
        if (doador == null) {
            throw new IllegalArgumentException("Doador não informado.");
        }
        doador.setNomeCompleto(doador.getNomeCompleto() == null ? null : doador.getNomeCompleto().trim());
        doador.setEmail(doador.getEmail() == null ? null : doador.getEmail().trim().toLowerCase());
        doador.setTelefone(doador.getTelefone() == null ? null : doador.getTelefone().replaceAll("\\D", ""));
        doador.validar();
        if (doadorDAO.buscarPorEmail(doador.getEmail()).isPresent()) {
            throw new RegraNegocioException("Já existe um doador cadastrado com este e-mail.");
        }
        return doadorDAO.salvar(doador);
    }

    public List<Doador> listar() throws PersistenciaException {
        return doadorDAO.listarTodos();
    }

    public Doador buscarPorId(Long id) throws PersistenciaException {
        return doadorDAO.buscarPorId(id)
                .orElseThrow(() -> new RegraNegocioException("Doador não encontrado (id " + id + ")."));
    }
}
