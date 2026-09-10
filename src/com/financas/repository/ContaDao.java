package com.financas.repository;

import com.financas.model.Conta;

public interface ContaDao {

    Conta salvar(Conta conta);

    Conta buscarPorUsuario(int usuarioId);

    Conta atualizar(Conta conta);

    void deletar(int id);

    Conta buscarPorId (int id);
}
