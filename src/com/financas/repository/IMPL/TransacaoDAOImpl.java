package com.financas.repository.IMPL;

import com.financas.enums.TipoTransacao;
import com.financas.exception.ConexaoException;
import com.financas.model.Conta;
import com.financas.model.Transacao;
import com.financas.repository.TransacaoDao;
import com.financas.util.ConexaoDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransacaoDAOImpl implements TransacaoDao {

    private Connection connection;

    public TransacaoDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Transacao salvar(Transacao transacao) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO transacao"
                            + "(conta_id,tipo,categoria,valor,data,descricao)"
                            + "VALUES "
                            + "(?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, transacao.getConta().getId());
            preparedStatement.setString(2, transacao.getTipo().name());
            preparedStatement.setString(3, transacao.getCategoria());
            preparedStatement.setDouble(4, transacao.getValor());
            preparedStatement.setDate(5, Date.valueOf(transacao.getData()));
            preparedStatement.setString(6, transacao.getDescricao());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    transacao = new Transacao(id, transacao.getConta(), transacao.getTipo(), transacao.getCategoria(), transacao.getValor(), transacao.getData(), transacao.getDescricao());
                }
                ConexaoDB.closeResultSet(resultSet);
            } else {
                throw new ConexaoException("Não foi possivel salvar a transacao.");
            }
        } catch (SQLException exception) {
            throw new ConexaoException("Erro ao salvar transacao: " + exception.getMessage());
        } finally {
            ConexaoDB.closeStatment(preparedStatement);
        }
        return transacao;
    }

    @Override
    public Transacao buscarPorId(int id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM transacao WHERE id = ?");

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int contaId = resultSet.getInt("conta_Id");
                ContaDAOImpl contaDAOImpl = new ContaDAOImpl(this.connection);
                Conta contaEncontrada = contaDAOImpl.buscarPorId(contaId);

                String tipoTexto = resultSet.getString("tipo");
                TipoTransacao tipoTransacao = TipoTransacao.valueOf(tipoTexto);
                String categoria = resultSet.getString("categoria");
                double valor = resultSet.getDouble("valor");
                LocalDate data = resultSet.getDate("data").toLocalDate();
                String descricao = resultSet.getString("descricao");

                Transacao transacao = new Transacao(id, contaEncontrada, tipoTransacao, categoria, valor, data, descricao);

                return transacao;
            }
            return null;
        } catch (SQLException exception) {
            throw new ConexaoException("Erro ao buscar transacao por id: " + exception.getMessage());
        } finally {
            ConexaoDB.closeStatment(preparedStatement);
            ConexaoDB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Transacao> listarPorConta(int contaId) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM transacao WHERE conta_Id = ?");
            preparedStatement.setInt(1, contaId);

            ContaDAOImpl contaDAOImpl = new ContaDAOImpl(this.connection);
            Conta buscandoConta = contaDAOImpl.buscarPorId(contaId);
            resultSet = preparedStatement.executeQuery();
            List<Transacao> transacaos = new ArrayList<>();

            while (resultSet.next()) {

                int idTransacao = resultSet.getInt("id");
                String tipoTexto = resultSet.getString("tipo");
                TipoTransacao tipoTransacao = TipoTransacao.valueOf(tipoTexto);
                String categoria = resultSet.getString("categoria");
                double valor = resultSet.getDouble("valor");
                LocalDate data = resultSet.getDate("data").toLocalDate();
                String descricao = resultSet.getString("descricao");

                Transacao transacao = new Transacao(idTransacao, buscandoConta, tipoTransacao, categoria, valor, data, descricao);
                transacaos.add(transacao);
            }
            return transacaos;
        } catch (SQLException exception) {
            throw new ConexaoException("Erro ao listar transacoes da conta: " + exception.getMessage());
        } finally {
            ConexaoDB.closeStatment(preparedStatement);
            ConexaoDB.closeResultSet(resultSet);
        }
    }

    @Override
    public Transacao atualizar(Transacao transacao) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("UPDATE transacao SET categoria = ? , valor = ?, descricao = ? WHERE id = ?");
            preparedStatement.setString(1, transacao.getCategoria());
            preparedStatement.setDouble(2, transacao.getValor());
            preparedStatement.setString(3, transacao.getDescricao());
            preparedStatement.setInt(4, transacao.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new ConexaoException("Erro ao atualizar Transação: " + exception.getMessage());
        } finally {
            ConexaoDB.closeStatment(preparedStatement);
        }
        return transacao;
    }

    @Override
    public void deletar(int id) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM transacao WHERE id = ?");

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new ConexaoException("Erro ao deletar Transação: " + exception.getMessage());
        } finally {
            ConexaoDB.closeStatment(preparedStatement);
        }
    }
}
