package by.ghoncharko.webproject.model.dao;


import by.ghoncharko.webproject.entity.StatusRecipeRequest;
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


public class StatusRecipeRequestDaoImpl implements Dao<StatusRecipeRequest> {
    private static final Logger LOG = LogManager.getLogger(StatusRecipeRequest.class);

    private static final String SQL_CREATE_STATUS_RECIPE_REQUEST = "INSERT INTO status_recipe_request" +
            " (name) VALUES (?)";
    private static final String SQL_FIND_ALL_STATUS_RECIPE_REQUESTS = "SELECT id, name" +
            " FROM status_recipe_request";
    private static final String SQL_FIND_STATUS_RECIPE_REQUEST_BY_ID ="SELECT name" +
            " FROM status_recipe_request WHERE id = ?";
    private static final String SQL_UPDATE_STATUS_RECIPE_REQUEST = "UPDATE status_recipe_request SET name = ? WHERE id = ?";
    private static final String SQL_DELETE_STATUS_RECIPE_REQUEST = "DELETE FROM status_recipe_request" +
            " WHERE id = ?";
    private final Connection connection;
    private StatusRecipeRequestDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public StatusRecipeRequest create(StatusRecipeRequest entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(SQL_CREATE_STATUS_RECIPE_REQUEST, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,entity.getName());
            int countRows = preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(countRows>0 && resultSet.next()){
                return new StatusRecipeRequest.Builder().
                        withId(resultSet.getInt(1)).
                        withName(entity.getName()).
                        build();
            }
        }catch (SQLException e){
            LOG.error("cannot creaate status recipe request",e);
            throw new DaoException("cannot creaate status recipe request",e);
        }finally {
            Dao.closeStatement(preparedStatement);
        }
        LOG.error("cannot creaate status recipe request");
        throw new DaoException("cannot creaate status recipe request");
    }

    @Override
    public List<StatusRecipeRequest> findAll() throws DaoException {
        List<StatusRecipeRequest> statusRecipeRequestList = new ArrayList<>();
        Statement statement = null;
        try{
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_STATUS_RECIPE_REQUESTS);
            while (resultSet.next()){
                StatusRecipeRequest statusRecipeRequest = new StatusRecipeRequest.Builder().
                        withId(resultSet.getInt(1)).
                        withName(resultSet.getString(2)).
                        build();
                statusRecipeRequestList.add(statusRecipeRequest);
            }
        }catch (SQLException e){
            LOG.error("cannot find all status recipe request",e);
            throw new DaoException("cannot find all status recipe request",e);
        }finally {
            Dao.closeStatement(statement);
        }
        return statusRecipeRequestList;
    }

    @Override
    public Optional<StatusRecipeRequest> findEntityById(Integer id) throws DaoException {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(SQL_FIND_STATUS_RECIPE_REQUEST_BY_ID);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                StatusRecipeRequest statusRecipeRequest = new StatusRecipeRequest.Builder().
                        withId(id).
                        withName(resultSet.getString(1)).
                        build();
                return Optional.of(statusRecipeRequest);
            }
        }catch (SQLException e){
            LOG.error("cannot find status recipe request by id",e);
            throw new DaoException("cannot find status recipe request by id",e);
        }finally {
            Dao.closeStatement(preparedStatement);
        }
        return Optional.empty();
    }

    @Override
    public StatusRecipeRequest update(StatusRecipeRequest entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(SQL_UPDATE_STATUS_RECIPE_REQUEST);
            preparedStatement.setString(1,entity.getName());
            preparedStatement.setInt(2,entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if(countRows>0){
                return entity;
            }
        }catch (SQLException e){
            LOG.error("Cannot update status recipe request",e);
            throw new DaoException("Cannot update status recipe request",e);
        }finally {
            Dao.closeStatement(preparedStatement);
        }
        LOG.error("Cannot update status recipe request");
        throw new DaoException();
    }

    @Override
    public boolean delete(StatusRecipeRequest entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(SQL_DELETE_STATUS_RECIPE_REQUEST);
            preparedStatement.setInt(1,entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if(countRows>0){
                return true;
            }
        }catch (SQLException e){
            LOG.error("cannot delete status recipe request",e);
            throw new DaoException("cannot delete status recipe request",e);
        }finally {
            Dao.closeStatement(preparedStatement);
        }
        return false;
    }
}
