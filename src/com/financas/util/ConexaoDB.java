package com.financas.util;

import com.financas.exception.ConexaoException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class ConexaoDB {

    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Properties properties = loadProperties();
                String url = properties.getProperty("dburl");
                connection = DriverManager.getConnection(url, properties);
            } catch (SQLException exception) {
                throw new ConexaoException(exception.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException exception) {
                throw new ConexaoException(exception.getMessage());
            }
        }
    }

    private static Properties loadProperties() {
        try (FileInputStream fileInputStream = new FileInputStream("db.properties")) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return properties;
        } catch (IOException exception) {
            throw new ConexaoException(exception.getMessage());
        }
    }

    public static void closeStatment(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException exception) {
                throw new ConexaoException(exception.getMessage());
            }
        }
    }
    public static void closeResultSet(ResultSet resultSet){
        if (resultSet != null) {
            try{
                resultSet.close();
            } catch (SQLException exception) {
                throw new ConexaoException(exception.getMessage());
            }
        }
    }
}