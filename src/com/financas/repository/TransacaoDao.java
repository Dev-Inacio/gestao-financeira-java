package com.financas.repository;

import com.financas.model.Transacao;

import java.util.List;

public interface TransacaoDao {

    Transacao salvar(Transacao transacao);

    Transacao buscarPorId(int id);

    List<Transacao> listarPorConta(int contaId);

    Transacao atualizar(Transacao transacao);

    void deletar(int id);
}
