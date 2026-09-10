package com.financas.repository.IMPL;

import com.financas.exception.ConexaoException;
import com.financas.model.Usuario;
import com.financas.repository.UsuarioDao;
import com.financas.util.ConexaoDB;

import java.sql.*;

public class UsuarioDAOImpl implements UsuarioDao {

    private Connection connection;

    public UsuarioDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Usuario salvar(Usuario usuario) {

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO usuario "
                            + "(nome, email, senha )"
                            + "VALUES "
                            + "(?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, usuario.getNome());
            preparedStatement.setString(2, usuario.getEmail());
            preparedStatement.setString(3, usuario.getSenha());

            int rowsnAffected = preparedStatement.executeUpdate();

            if (rowsnAffected > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    usuario = new Usuario(id, usuario.getNome(), usuario.getEmail(), usuario.getSenha());
                }
                ConexaoDB.closeResultSet(resultSet);
            } else {
                throw new ConexaoException("Não foi possivel salvar o usuario.");
            }
        } catch (SQLException exception) {
            throw new ConexaoException("Erro ao salvar usuario: " + exception.getMessage());
        } finally {
            ConexaoDB.closeStatment(preparedStatement);
        }
        return usuario;
    }


    @Override
    public Usuario buscarPorEmail(String usuarioEmail) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM usuario where email = ?");

            preparedStatement.setString(1, usuarioEmail);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Usuario usuario = new Usuario(resultSet.getInt("id"), resultSet.getString("nome"), resultSet.getString("email"), resultSet.getString("senha"));
                return usuario;
            } else {
                return null;
            }
        } catch (SQLException exception) {
            throw new ConexaoException("Erro ao buscar usuario por email: " + exception.getMessage());
        } finally {
            ConexaoDB.closeStatment(preparedStatement);
            ConexaoDB.closeResultSet(resultSet);
        }
    }

    @Override
    public Usuario buscarPorId(int usuarioId) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM usuario where id = ?");

            preparedStatement.setInt(1, usuarioId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Usuario usuario = new Usuario(resultSet.getInt("id"), resultSet.getString("nome"), resultSet.getString("email"), resultSet.getString("senha"));
                return usuario;
            } else {
                return null;
            }
        } catch (SQLException exception) {
            throw new ConexaoException("Erro ao buscar usuario por id: " + exception.getMessage());
        } finally {
            ConexaoDB.closeStatment(preparedStatement);
            ConexaoDB.closeResultSet(resultSet);
        }
    }
}

