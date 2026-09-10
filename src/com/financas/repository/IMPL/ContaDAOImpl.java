package com.financas.repository.IMPL;

import com.financas.exception.ConexaoException;
import com.financas.model.Conta;
import com.financas.model.Usuario;
import com.financas.repository.ContaDao;
import com.financas.util.ConexaoDB;

import java.sql.*;

public class ContaDAOImpl implements ContaDao {

    private Connection connection;

    public ContaDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Conta salvar(Conta conta) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO conta "
                            + "(usuario_id,saldo) "
                            + "VALUES "
                            + "(?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, conta.getUsuario().getId());
            preparedStatement.setDouble(2, conta.getSaldo());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    conta = new Conta(id, conta.getUsuario(), conta.getSaldo());
                }
                ConexaoDB.closeResultSet(resultSet);
            } else {
                throw new ConexaoException("Não foi possivel salvar a conta.");
            }
        } catch (SQLException exception) {
            throw new ConexaoException("Erro ao salvar conta: " + exception.getMessage());
        } finally {
            ConexaoDB.closeStatment(preparedStatement);
        }
        return conta;
    }

    @Override
    public Conta buscarPorUsuario(int usuarioId) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM conta WHERE usuario_id = ?");
            preparedStatement.setInt(1, usuarioId);

            UsuarioDAOImpl usuarioDAOImpl = new UsuarioDAOImpl(this.connection);
            Usuario buscando = usuarioDAOImpl.buscarPorId(usuarioId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int idConta = resultSet.getInt("id");
                double saldoConta = resultSet.getDouble("saldo");
                return new Conta(idConta, buscando, saldoConta);
            }
            return null;
        } catch (SQLException exception) {
            throw new ConexaoException("Erro ao buscar conta por usuario: " + exception.getMessage());
        } finally {
            ConexaoDB.closeStatment(preparedStatement);
            ConexaoDB.closeResultSet(resultSet);
        }
    }

    @Override
    public Conta atualizar(Conta conta) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "UPDATE conta SET saldo = ? WHERE id = ?");

            preparedStatement.setDouble(1, conta.getSaldo());
            preparedStatement.setInt(2, conta.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new ConexaoException("Erro ao atualizar conta: " + exception.getMessage());
        } finally {
            ConexaoDB.closeStatment(preparedStatement);
        }
        return conta;
    }

    @Override
    public void deletar(int id) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM conta WHERE id = ?");

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new ConexaoException("Erro ao deletar conta: " + exception.getMessage());
        } finally {
            ConexaoDB.closeStatment(preparedStatement);
        }
    }

    @Override
    public Conta buscarPorId(int id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM conta WHERE id = ?");

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int usuarioId = resultSet.getInt("usuario_id");
                UsuarioDAOImpl usuarioDAOImpl = new UsuarioDAOImpl(this.connection);
                Usuario usuarioEncontrado = usuarioDAOImpl.buscarPorId(usuarioId);

                int idConta = resultSet.getInt("id");
                double saldoConta = resultSet.getDouble("saldo");

                Conta conta = new Conta(idConta, usuarioEncontrado, saldoConta);

                return conta;
            }
            return null;
        } catch (SQLException exception) {
            throw new ConexaoException("Erro ao buscar conta por id: " + exception.getMessage());
        } finally {
            ConexaoDB.closeStatment(preparedStatement);
            ConexaoDB.closeResultSet(resultSet);
        }
    }
}
