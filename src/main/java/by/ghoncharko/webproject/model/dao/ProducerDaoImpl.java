package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.Producer;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProducerDaoImpl implements ProducerDao{
    private static final Logger LOG = LogManager.getLogger(ProducerDaoImpl.class);
    private static final String SQL_CREATE_PRODUCER = "INSERT INTO producer (producer_name)  VALUES (?)";
    private static final String SQL_FIND_ALL_PRODUCERS = "SELECT  id, producer_name FROM producer";
    private static final String SQL_FIND_PRODUCER_BY_ID = "SELECT producer_name FROM producer" +
            " WHERE id = ?";
    private static final String SQL_FIND_PRODUCER_BY_PRODUCER_NAME = "SELECT id FROM producer" +
            " WHERE producer_name = ?";
    private static final String SQL_UPDATE_PRODUCER_BY_ID = "UPDATE producer SET producer_name = ?" +
            " WHERE id = ?";
    private static final String SQL_DELETE_PRODUCER_BY_ID = "DELETE FROM producer WHERE id = ?";
    private final Connection connection;

    public ProducerDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Producer create(Producer entity) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_PRODUCER, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1,entity.getName());
            final int countUpdatedRows = preparedStatement.executeUpdate();
            if(countUpdatedRows>0){
                final ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()){
                    return new Producer.Builder().
                            withId(resultSet.getInt(1)).
                            withName(entity.getName()).
                            build();
                }
            }
        }catch (SQLException e){
            LOG.error("Cannot create producer",e);
            throw new DaoException("Cannot create producer",e);
        }
        LOG.error("Cannot create producer");
        throw new DaoException("Cannot create producer");
    }

    @Override
    public List<Producer> findAll() throws DaoException {
        final List<Producer> producerList = new ArrayList<>();
        try(final Statement statement = connection.createStatement()){
           final ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_PRODUCERS);
           while (resultSet.next()){
               final Producer producer = new Producer.Builder().
                       withId(resultSet.getInt(1)).
                       withName(resultSet.getString(2)).
                       build();
               producerList.add(producer);
           }
        }catch (SQLException e){
            LOG.error("Cannot find all producers",e);
            throw new DaoException("Cannot find all producers",e);
        }
        return producerList;
    }

    @Override
    public Optional<Producer> findProducerByProducerName(String producerName) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_PRODUCER_BY_PRODUCER_NAME)){
            preparedStatement.setString(1,producerName);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return Optional.of(new Producer.Builder().
                        withId(resultSet.getInt(1)).
                        withName(producerName).
                        build());
            }
        }catch (SQLException e){
            LOG.error("Cannot find producer by producer name",e);
            throw new DaoException("Cannot find producer by producer name",e);
        }
        LOG.info("Cannot find producer by producer name");
        return Optional.empty();
    }

    @Override
    public Optional<Producer> findEntityById(Integer id) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_PRODUCER_BY_ID)){
            preparedStatement.setInt(1,id);
           final ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return Optional.of(new Producer.Builder().
                        withId(id).
                        withName(resultSet.getString(1)).
                        build());
            }
        }catch (SQLException e){
            LOG.error("Cannot find producer by id",e);
            throw new DaoException("Cannot find producer by id",e);
        }
        LOG.info("Cannot find producer by id");
        return Optional.empty();
    }

    @Override
    public Producer update(Producer entity) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_PRODUCER_BY_ID)){
            preparedStatement.setString(1,entity.getName());
            preparedStatement.setInt(2,entity.getId());
            final int countUpdatedRows = preparedStatement.executeUpdate();
            if(countUpdatedRows>0){
                return entity;
            }
        }catch (SQLException e){
            LOG.error("Cannot update producer by id",e);
            throw new DaoException("Cannot update producer by id",e);
        }
        LOG.error("Cannot update producer by id");
        throw new DaoException("Cannot update producer by id");
    }

    @Override
    public boolean delete(Producer entity) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_PRODUCER_BY_ID)){
            preparedStatement.setInt(1,entity.getId());
            final int countUpdatedRows = preparedStatement.executeUpdate();
            return countUpdatedRows>0;
        }catch (SQLException e){
            LOG.error("Cannot delete producer by id",e);
            throw new DaoException("Cannot delete producer by id",e);
        }
    }
}
