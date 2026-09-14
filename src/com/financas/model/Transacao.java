package com.financas.model;

import com.financas.enums.TipoTransacao;

import java.time.LocalDate;

public class Transacao {
    private int id;
    private Conta conta;
    private TipoTransacao tipo;
    private String categoria;
    private double valor;
    private LocalDate data;
    private String descricao;

    public Transacao(Conta conta, TipoTransacao tipo, String categoria, double valor, LocalDate data, String descricao) {
        this.conta = conta;
        this.tipo = tipo;
        this.categoria = categoria;
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
    }

    public Transacao(int id, Conta conta, TipoTransacao tipo, String categoria, double valor, LocalDate data, String descricao) {
        this.id = id;
        this.conta = conta;
        this.tipo = tipo;
        this.categoria = categoria;
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public Conta getConta() {
        return conta;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return String.format("%-5d %-10s %-20s R$%10.2f   %s",id, tipo, categoria, valor, data);
    }
}
