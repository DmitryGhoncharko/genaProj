package by.ghoncharko.webproject.model.dao;


import by.ghoncharko.webproject.entity.Status;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class StatusDaoImpl extends AbstractDao<Status> {
    private static final Logger LOG = LogManager.getLogger(StatusDaoImpl.class);

    private static final String SQL_CREATE_STATUS = "INSERT INTO status (status_name) VALUES (?)";
    private static final String SQL_FIND_ALL_STATUS = "SELECT id,status_name FROM status";
    private static final String SQL_FIND_STATUS_BY_ID = "SELECT id,status_name FROM status" +
            " WHERE id = ?";
    private static final String SQL_UPDATE_STATUS = "UPDATE status SET status_name = ? WHERE  id = ?";
    private static final String SQL_DELETE_STATUS = "DELETE FROM status WHERE id = ?";
    private StatusDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Status create(Status entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(SQL_CREATE_STATUS, Statement.RETURN_GENERATED_KEYS);
            int countRows = preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.executeQuery();
            if(countRows>0 && resultSet.next()){
                return new Status.Builder().
                        withId(resultSet.getInt(1)).
                        withName(entity.getName()).build();
            }
        }catch (SQLException e){
            LOG.error("cannot create status",e);
            throw new DaoException("cannot create status",e);
        }finally {
            close(preparedStatement);
        }
        LOG.error("cannot create status");
        throw new DaoException();
    }

    @Override
    public List<Status> findAll() throws DaoException {
        List<Status> statusList = new ArrayList<>();
        Statement statement = null;
        try{
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_STATUS);
            while (resultSet.next()){
                Status status = new Status.Builder().
                        withId(resultSet.getInt(1)).
                        withName(resultSet.getString(2)).
                        build();
                statusList.add(status);
            }
        }catch (SQLException e){
            LOG.error("cannot find all statuses",e);
            throw new DaoException("cannot find all statuses",e);
        }finally {
            close(statement);
        }
        return statusList;
    }

    @Override
    public Optional<Status> findEntityById(Integer id) throws DaoException {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(SQL_FIND_STATUS_BY_ID);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Status status = new Status.Builder().
                        withId(resultSet.getInt(1)).
                        withName(resultSet.getString(2)).
                        build();
                return Optional.of(status);
            }
        }catch (SQLException e){
            LOG.error("cannot find status by id",e);
            throw new DaoException("cannot find status by id",e);
        }finally {
            close(preparedStatement);
        }
        return Optional.empty();
    }

    @Override
    public Status update(Status entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(SQL_UPDATE_STATUS);
            preparedStatement.setString(1,entity.getName());
            preparedStatement.setInt(2,entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if(countRows>0){
                return entity;
            }
        }catch (SQLException e){
            LOG.error("cannot update status",e);
            throw  new DaoException("cannot update status",e);
        }finally {
            close(preparedStatement);
        }
        LOG.error("cannot update status");
        throw new DaoException();
    }

    @Override
    public boolean delete(Status entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(SQL_DELETE_STATUS);
            preparedStatement.setInt(1,entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if(countRows>0){
                return true;
            }
        }catch (SQLException e){
            LOG.error("cannot delete status",e);
            throw new DaoException("cannot delete status",e);
        }finally {
            close(preparedStatement);
        }
        return false;
    }
}
