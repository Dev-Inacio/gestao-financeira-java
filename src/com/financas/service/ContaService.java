package com.financas.service;

import com.financas.exception.ContaNaoEncontradaException;
import com.financas.exception.DadoInvalidoException;
import com.financas.model.Conta;
import com.financas.model.Usuario;
import com.financas.repository.ContaDao;

public class ContaService {

    private ContaDao contaDao;

    public ContaService(ContaDao contaDao) {
        this.contaDao = contaDao;
    }

    public Conta criarConta(Usuario usuario, double saldoInicial) {
        if (saldoInicial < 0) {
            throw new DadoInvalidoException("O saldo inicial não pode ser negativo.");
        }
        Conta conta = new Conta(usuario, saldoInicial);
        return contaDao.salvar(conta);
    }

    public Conta buscarPorUsuario(int usuarioId) {
        return contaDao.buscarPorUsuario(usuarioId);
    }

    public void deletar(int contaId) {
        Conta conta = contaDao.buscarPorId(contaId);
        if (conta == null) {
            throw new ContaNaoEncontradaException("Conta não encontrada.");
        }
        contaDao.deletar(contaId);
    }
}