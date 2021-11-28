package by.ghoncharko.webproject.model.dao;


import by.ghoncharko.webproject.entity.Producer;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ProducerDaoImpl implements Dao<Producer> {
    private static final Logger LOG = LogManager.getLogger(ProducerDaoImpl.class);

    private static final String SQL_CREATE_PRODUCER = "INSERT INTO producer (producer_name) VALUES(?)";
    private static final String SQL_FIND_ALL_PRODUCERS = "SELECT id, producer_name FROM producer";
    private static final String SQL_FIND_PRODUCER_BY_ID = "SELECT id, producer_name FROM producer " +
            " WHERE id = ?";
    private static final String SQL_UPDATE_PRODUCER = "UPDATE producer " +
            " SET producer_name = ?" +
            " WHERE id = ?";
    private static final String SQL_DELETE_PRODUCER = "DELETE FROM producer WHERE id = ?";
    private final Connection connection;

    private ProducerDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Producer create(Producer entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_CREATE_PRODUCER, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getName());
            int countRows = preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (countRows > 0 & resultSet.next()) {
                return new Producer.Builder().
                        withId(resultSet.getInt(1)).
                        withName(entity.getName()).
                        build();
            }
        } catch (SQLException e) {
            LOG.error("cannot create producer", e);
            throw new DaoException("cannot create producer", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        LOG.error("cannot create producer");
        throw new DaoException("cannot create producer");
    }

    @Override
    public List<Producer> findAll() throws DaoException {
        List<Producer> producerList = new ArrayList<>();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_PRODUCERS);
            while (resultSet.next()) {
                Producer producer = new Producer.Builder().
                        withId(resultSet.getInt(1)).
                        withName(resultSet.getString(2)).
                        build();
                producerList.add(producer);
            }
        } catch (SQLException e) {
            LOG.error("cannot find all producers", e);
            throw new DaoException("cannot find all producers", e);
        } finally {
            Dao.closeStatement(statement);
        }
        return producerList;
    }

    @Override
    public Optional<Producer> findEntityById(Integer id) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_PRODUCER_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Producer.Builder().
                        withId(resultSet.getInt(1)).
                        withName(resultSet.getString(2)).
                        build());
            }
        } catch (SQLException e) {
            LOG.error("cannot find producer by id", e);
            throw new DaoException("cannot find producer by id", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        return Optional.empty();
    }

    @Override
    public Producer update(Producer entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_PRODUCER);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return entity;
            }
        } catch (SQLException e) {
            LOG.error("cannot update producer", e);
            throw new DaoException("cannot update producer", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        LOG.error("cannot update producer");
        throw new DaoException("cannot update producer");
    }

    @Override
    public boolean delete(Producer entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_DELETE_PRODUCER);
            preparedStatement.setInt(1, entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            LOG.error("cannot delete producer", e);
            throw new DaoException("cannot delete producer", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        return false;
    }
}
