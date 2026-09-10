package com.financas.model;

public class Conta {
    private int id;
    private Usuario usuario;
    private double saldo;

    public Conta(int id, Usuario usuario, double saldo) {
        this.id = id;
        this.usuario = usuario;
        this.saldo = saldo;
    }

    public Conta(Usuario usuario, double saldo) {
        this.usuario = usuario;
        this.saldo = saldo;
    }

    public int getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "-------------------------------------" +
                "\nConta\n" +
                "\nid = " + id +
                "\nusuario = " + usuario.getNome() +
                "\nsaldo = " + String.format("%.2f", saldo) +
                "\n-------------------------------------";
    }
}
