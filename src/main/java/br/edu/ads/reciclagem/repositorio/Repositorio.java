package br.edu.ads.reciclagem.repositorio;

import br.edu.ads.reciclagem.modelo.EntidadeBase;

import java.util.List;
import java.util.Optional;

public interface Repositorio<T extends EntidadeBase> {

    T salvar(T entidade) throws PersistenciaException;

    void atualizar(T entidade) throws PersistenciaException;

    boolean excluir(Long id) throws PersistenciaException;

    Optional<T> buscarPorId(Long id) throws PersistenciaException;

    List<T> listarTodos() throws PersistenciaException;
}
