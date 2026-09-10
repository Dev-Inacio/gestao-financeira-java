package com.financas.application;

import com.financas.enums.TipoTransacao;
import com.financas.model.Conta;
import com.financas.model.Transacao;
import com.financas.model.Usuario;
import com.financas.repository.IMPL.ContaDAOImpl;
import com.financas.repository.IMPL.TransacaoDAOImpl;
import com.financas.repository.IMPL.UsuarioDAOImpl;

import com.financas.service.ContaService;
import com.financas.service.TransacaoService;
import com.financas.service.UsuarioService;
import com.financas.util.ConexaoDB;

import java.sql.Connection;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static void exibirMenuPrincipal(Scanner scanner, UsuarioService usuarioService, ContaService contaService, TransacaoService transacaoService) {
        String opcao;
        do {
            System.out.print("\n=== Menu Gestão Financeiro ===\n");
            System.out.print("\n[1] Registrar-se: ");
            System.out.print("\n[2] Login: ");
            System.out.print("\n[0] Sair: ");
            opcao = scanner.nextLine();
            try {
                switch (opcao) {
                    case "1":
                        System.out.print("\nCadastro: ");
                        System.out.print("\nNome: ");
                        String nome = scanner.nextLine();
                        System.out.print("\nEmail: ");
                        String email = scanner.nextLine();
                        System.out.print("\nSenha: ");
                        String senha = scanner.nextLine();
                        usuarioService.cadastrarUsuario(nome, email, senha);
                        break;

                    case "2":
                        System.out.print("\nEmail: ");
                        email = scanner.nextLine();
                        System.out.print("\nSenha: ");
                        senha = scanner.nextLine();
                        Usuario usuarioLogado = usuarioService.autenticar(email, senha);
                        Conta contaLogada = contaService.buscarPorUsuario(usuarioLogado.getId());
                        exibirMenuConta(scanner, transacaoService, contaLogada);
                        break;

                    case "0":
                        break;
                    default:
                        System.out.print("Opção inválida.");
                        break;
                }
            } catch (RuntimeException exception) {
                System.out.print("Error: " + exception.getMessage());
            }
        } while (!opcao.equals("0"));
    }

    private static void exibirMenuConta(Scanner scanner, TransacaoService transacaoService, Conta conta) {
        String opcao;
        do {
            System.out.print("\nBem-vindo, " + conta.getUsuario().getNome());
            System.out.print("\nSaldo: " + conta.getSaldo());
            System.out.print("\n[1] Nova Receita: ");
            System.out.print("\n[2] Nova Despesa: ");
            System.out.print("\n[3] Ver Extrato (Listar Transações): ");
            System.out.print("\n[4] Editar Transação: ");
            System.out.print("\n[5] Deletar Transação: ");
            System.out.print("\n[0] Voltar / Sair: ");
            opcao = scanner.nextLine();
            try {
                switch (opcao) {
                    case "1":
                        System.out.print("Categoria: ");
                        String categoria = scanner.nextLine();
                        System.out.print("Valor: ");
                        double valor = Double.parseDouble(scanner.nextLine());
                        System.out.print("Descrição: ");
                        String descricao = scanner.nextLine();
                        transacaoService.registrarTransacao(conta, TipoTransacao.RECEITA, categoria, valor, descricao);
                        break;
                    case "2":

                        System.out.print("Categoria: ");
                        categoria = scanner.nextLine();
                        System.out.print("Valor: ");
                        valor = Double.parseDouble(scanner.nextLine());
                        System.out.print("Descrição: ");
                        descricao = scanner.nextLine();
                        transacaoService.registrarTransacao(conta, TipoTransacao.DESPESA, categoria, valor, descricao);
                        break;

                    case "3":
                        List<Transacao> listaDeTransacoes = transacaoService.listarPorConta(conta.getId());
                        if (listaDeTransacoes.isEmpty()) {
                            System.out.print("Nenhuma transação encontrada.");
                        } else {
                            for (Transacao transacao : listaDeTransacoes) {
                                System.out.println(transacao);
                            }
                        }
                        break;

                    case "4":
                        System.out.print("Digite O Id Da transação: ");
                        int idTransacao = Integer.parseInt(scanner.nextLine());
                        Transacao transacaoEcontrada = transacaoService.buscarPorId(idTransacao);
                        if (transacaoEcontrada == null) {
                            System.out.print("Id Nao Existe: ");
                            break;
                        }
                        System.out.print("Nova Categoria: ");
                        categoria = scanner.nextLine();
                        System.out.print("Novo Valor: ");
                        valor = Double.parseDouble(scanner.nextLine());
                        System.out.print("Nova Descrição: ");
                        descricao = scanner.nextLine();

                        transacaoEcontrada.setCategoria(categoria);
                        transacaoEcontrada.setValor(valor);
                        transacaoEcontrada.setDescricao(descricao);

                        transacaoService.atualizar(transacaoEcontrada, conta);
                        break;

                    case "5":
                        System.out.print("Digite O Id Da transação: ");
                        int idTransacaoDeletar = Integer.parseInt(scanner.nextLine());
                        transacaoService.deletar(idTransacaoDeletar, conta);
                        break;
                    case "0":
                        break;
                    default:
                        System.out.print("Opção inválida.");
                        break;
                }
            } catch (RuntimeException exception) {
                System.out.print("Error: " + exception.getMessage());
            }
        } while (!opcao.equals("0"));
    }

    public static void main(String[] args) {

        Locale.setDefault(Locale.US);
        Scanner scanner = new Scanner(System.in);
        Connection connection = ConexaoDB.getConnection();

        UsuarioDAOImpl usuarioDAOImpl = new UsuarioDAOImpl(connection);
        ContaDAOImpl contaDAOImpl = new ContaDAOImpl(connection);
        TransacaoDAOImpl transacaoDaoImpl = new TransacaoDAOImpl(connection);

        ContaService contaService = new ContaService(contaDAOImpl);
        UsuarioService usuarioService = new UsuarioService(usuarioDAOImpl, contaService);
        TransacaoService transacaoService = new TransacaoService(transacaoDaoImpl, contaDAOImpl);

        exibirMenuPrincipal(scanner, usuarioService, contaService, transacaoService);
    }
}