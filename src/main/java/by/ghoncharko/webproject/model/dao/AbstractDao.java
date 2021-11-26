package by.ghoncharko.webproject.model.dao;


import by.ghoncharko.webproject.entity.Entity;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * Abstract class AbstractDao for all Dao
 *
 * @param <T> any type who implements Entity
 * @see Entity - Interface-marker
 */
public abstract class AbstractDao<T extends Entity> {
    private static final Logger LOG = LogManager.getLogger(AbstractDao.class);
    protected Connection connection;

    public AbstractDao(Connection connection) {
        this.connection = connection;
    }

    public abstract T create(T entity) throws DaoException;

    public abstract List<T> findAll() throws DaoException;

    public abstract Optional<T> findEntityById(Integer id) throws DaoException;

    public abstract T update(T entity) throws DaoException;

    public abstract boolean delete(T entity) throws DaoException;

    public void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOG.error("SQLException when we tried close connection" + e);
            }
        }
    }
}
