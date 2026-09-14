# 💰 Gestão Financeira

CRUD em Java puro (sem frameworks) para controle financeiro pessoal, via console, com persistência em MySQL.

## Funcionalidades
- Cadastro e login de usuário (cada usuário já nasce com uma conta)
- Registro de receitas e despesas
- Extrato de transações
- Edição e exclusão de transações

## Tecnologias
Java • JDBC • MySQL

## Como rodar
1. Crie o banco `gestao_financeira`
2. Renomeie `db.properties.example` para `db.properties` e preencha suas credenciais
3. Execute `Main.java`

## Arquitetura
Model → DAO (interface + implementação) → Service → Main, com tratamento de exceções customizadas.
