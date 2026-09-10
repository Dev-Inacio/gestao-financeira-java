package com.financas.service;

import com.financas.enums.TipoTransacao;
import com.financas.exception.ContaNaoEncontradaException;
import com.financas.exception.DadoInvalidoException;
import com.financas.exception.TransacaoNaoEncontradaException;
import com.financas.model.Conta;
import com.financas.model.Transacao;
import com.financas.repository.ContaDao;
import com.financas.repository.TransacaoDao;

import java.time.LocalDate;
import java.util.List;

public class TransacaoService {

    private TransacaoDao transacaoDao;
    private ContaDao contaDao;


    public TransacaoService(TransacaoDao transacaoDao, ContaDao contaDao) {
        this.transacaoDao = transacaoDao;
        this.contaDao = contaDao;
    }

    public Transacao registrarTransacao(Conta conta, TipoTransacao tipoTransacao, String categoria, double valor, String descricao) {

        if (valor <= 0) {
            throw new DadoInvalidoException("O valor da transacao deve ser maior que zero.");
        }
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new DadoInvalidoException("A categoria nao pode ser vazia.");
        }
        if (conta == null) {
            throw new ContaNaoEncontradaException("A Conta Não Foi Encontrada.");
        }

        Transacao transacao = new Transacao(conta, tipoTransacao, categoria, valor, LocalDate.now(), descricao);
        Transacao transacaoSalva = transacaoDao.salvar(transacao);

        if (tipoTransacao == TipoTransacao.RECEITA) {
            conta.setSaldo(conta.getSaldo() + valor);
        } else {
            conta.setSaldo(conta.getSaldo() - valor);
        }
        contaDao.atualizar(conta);
        return transacaoSalva;
    }

    public List<Transacao> listarPorConta(int contaId) {
        return transacaoDao.listarPorConta(contaId);
    }

    public Transacao atualizar(Transacao transacao) {
        if (transacao == null) {
            throw new TransacaoNaoEncontradaException("Transação Não Encontrada.");
        }
        if (transacao.getValor() <= 0) {
            throw new DadoInvalidoException("O valor da transacao deve ser maior que zero.");
        }
        if (transacao.getCategoria() == null || transacao.getCategoria().trim().isEmpty()) {
            throw new DadoInvalidoException("A categoria nao pode ser vazia.");
        }
        return transacaoDao.atualizar(transacao);
    }

    public void deletar(int transacaoId) {
        Transacao transacaoEncontrada = transacaoDao.buscarPorId(transacaoId);
        if (transacaoEncontrada == null) {
            throw new TransacaoNaoEncontradaException("Transacao nao encontrada.");
        }

        if (transacaoEncontrada.getTipo() == TipoTransacao.RECEITA){
            transacaoEncontrada.getConta().setSaldo(transacaoEncontrada.getConta().getSaldo() - transacaoEncontrada.getValor());
        }
        else {
            transacaoEncontrada.getConta().setSaldo(transacaoEncontrada.getConta().getSaldo() + transacaoEncontrada.getValor());
        }
        contaDao.atualizar(transacaoEncontrada.getConta());
        transacaoDao.deletar(transacaoId);
    }

    public Transacao buscarPorId (int id){
        return transacaoDao.buscarPorId(id);
    }
}
