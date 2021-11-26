package by.ghoncharko.webproject.model.service;



import by.ghoncharko.webproject.entity.Entity;
import by.ghoncharko.webproject.exception.DaoException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface Service <T extends Entity> {
    List<T> findAll() throws DaoException;
    static void connectionClose(Connection connection){
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    static boolean autoCommitFalse(Connection connection){
        try {
            connection.setAutoCommit(false);
            return true;
        } catch (SQLException throwables) {
            return false;
        }
    }
    static void rollbackConnection(Connection connection){
        try {
            connection.rollback();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
