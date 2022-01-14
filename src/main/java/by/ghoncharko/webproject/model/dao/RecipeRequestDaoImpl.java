package by.ghoncharko.webproject.model.dao;


import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.Producer;
import by.ghoncharko.webproject.entity.RecipeRequest;
import by.ghoncharko.webproject.entity.Role;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class RecipeRequestDaoImpl implements RecipeRequestDao {
    private static final Logger LOG = LogManager.getLogger(RecipeRequestDaoImpl.class);
    private static final String SQL_CREATE_RECIPE_REQUEST = "INSERT INTO recipe_request" +
            " (user_id, drug_id, date_start, date_end) VALUES (?,?,?,?)";
    private static final String SQL_GET_ALL_RECIPE_REQUESTS = "SELECT" +
            " recipe_request.id,recipe_request.date_start,recipe_request.date_end, u.id,login,password,first_name," +
            " last_name,role_id,role_name, d.id,d.name,d.price,d.drug_count,d.description,d.need_receip,d.producer_id,p.producer_name" +
            " FROM recipe_request " +
            " INNER JOIN user u ON recipe_request.user_id = u.id" +
            " INNER JOIN role r ON u.role_id = r.id" +
            " INNER JOIN drug d ON recipe_request.drug_id = d.id" +
            " INNER JOIN producer p ON d.producer_id = p.id";
    private static final String SQL_GET_RECIPE_REQUEST_BY_ID = "SELECT" +
            " recipe_request.id,recipe_request.date_start,recipe_request.date_end, u.id,login,password,first_name," +
            " last_name,role_id,role_name, d.id,d.name,d.price,d.drug_count,d.description,d.need_receip,d.producer_id,p.producer_name" +
            " FROM recipe_request " +
            " INNER JOIN user u ON recipe_request.user_id = u.id" +
            " INNER JOIN role r ON u.role_id = r.id" +
            " INNER JOIN drug d ON recipe_request.drug_id = d.id" +
            " INNER JOIN producer p ON d.producer_id = p.id" +
            " WHERE recipe_request.id = ?";
    private static final String SQL_GET_RECIPE_REQUEST_BY_USER_ID_AND_DRUG_ID = "SELECT" +
            " recipe_request.id,recipe_request.date_start,recipe_request.date_end, u.id,login,password,first_name," +
            " last_name,role_id,role_name, d.id,d.name,d.price,d.drug_count,d.description,d.need_receip,d.producer_id,p.producer_name" +
            " FROM recipe_request " +
            " INNER JOIN user u ON recipe_request.user_id = u.id" +
            " INNER JOIN role r ON u.role_id = r.id" +
            " INNER JOIN drug d ON recipe_request.drug_id = d.id" +
            " INNER JOIN producer p ON d.producer_id = p.id" +
            " WHERE recipe_request.user_id = ? AND drug_id = ?";
    private static final String SQL_UPDATE_RECIPE_REQUEST = "UPDATE recipe_request" +
            " SET user_id = ? , drug_id = ? , date_start = ?, date_end = ?" +
            " WHERE id = ?";
    private static final String SQL_DELETE_RECIPE_REQUEST = "DELETE FROM recipe_request WHERE id = ?";
    private final Connection connection;
    public RecipeRequestDaoImpl(Connection connection) {
        this.connection = connection;
    }



    @Override
    public RecipeRequest create(RecipeRequest entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_CREATE_RECIPE_REQUEST, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, entity.getUser().getId());
            preparedStatement.setInt(2, entity.getDrug().getId());
            preparedStatement.setDate(3, entity.getDateStart());
            preparedStatement.setDate(4, entity.getDateEnd());
            int countRows = preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (countRows > 0 && resultSet.next()) {
                return new RecipeRequest.Builder().
                        withId(resultSet.getInt(1)).
                        withUser(entity.getUser()).
                        withDrug(entity.getDrug()).
                        withDateStart(entity.getDateStart()).
                        withDateEnd(entity.getDateEnd()).
                        build();
            }
        } catch (SQLException e) {
            LOG.error("cannot create recipe request",e);
            throw new DaoException("cannot create recipe request",e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        LOG.error("cannot create recipe request");
        throw new DaoException();
    }

    @Override
    public boolean createRecipeRequestByUserIdAndDrugIdWithDateStartAndDateEnd(Integer userId, Integer drugId, Date dateStart, Date dateEnd) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_CREATE_RECIPE_REQUEST);
            preparedStatement.setInt(1,userId);
            preparedStatement.setInt(2,drugId);
            preparedStatement.setDate(3,dateStart);
            preparedStatement.setDate(4,dateEnd);
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            LOG.error("cannot create recipe request",e);
            throw new DaoException("cannot create recipe request",e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        LOG.error("cannot create recipe request");
        throw new DaoException();
    }

    @Override
    public Optional<RecipeRequest> findRecipeRequestByUserIdAndDrugId(Integer userId, Integer drugId) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_GET_RECIPE_REQUEST_BY_USER_ID_AND_DRUG_ID);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2,drugId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                RecipeRequest recipeRequest = new RecipeRequest.Builder().
                        withId(resultSet.getInt(1)).
                        withDateStart(resultSet.getDate(2)).
                        withDateEnd(resultSet.getDate(3)).
                        withUser(new User.Builder().
                                withId(resultSet.getInt(4)).
                                withLogin(resultSet.getString(5)).
                                withPassword(resultSet.getString(6)).
                                withFirstName(resultSet.getString(7)).
                                withLastName(resultSet.getString(8)).
                                withRole(new Role.Builder().
                                        withId(resultSet.getInt(9)).
                                        withRoleName(resultSet.getString(10)).build()).build()
                        ).
                        withDrug(new Drug.Builder().
                                withId(resultSet.getInt(11)).
                                withName(resultSet.getString(12)).
                                withPrice(resultSet.getDouble(13)).
                                withCount(resultSet.getInt(14)).
                                withDescription(resultSet.getString(15)).
                                withNeedReceip(resultSet.getBoolean(16)).
                                withProducer(new Producer.Builder().
                                        withId(resultSet.getInt(17)).
                                        withName(resultSet.getString(18)).build()).build()
                        ).build();
                return Optional.of(recipeRequest);
            }
        } catch (SQLException e) {
            LOG.error("cannot find recipe request by id",e);
            throw new DaoException("cannot find recipe request by id",e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        return Optional.empty();
    }

    @Override
    public List<RecipeRequest> findAll() throws DaoException {
        List<RecipeRequest> recipeRequestList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_RECIPE_REQUESTS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                RecipeRequest recipeRequest = new RecipeRequest.Builder().
                        withId(resultSet.getInt(1)).
                        withDateStart(resultSet.getDate(2)).
                        withDateEnd(resultSet.getDate(3)).
                        withUser(new User.Builder().
                                withId(resultSet.getInt(4)).
                                withLogin(resultSet.getString(5)).
                                withPassword(resultSet.getString(6)).
                                withFirstName(resultSet.getString(7)).
                                withLastName(resultSet.getString(8)).
                                withRole(new Role.Builder().
                                        withId(resultSet.getInt(9)).
                                        withRoleName(resultSet.getString(10)).build()).build()
                        ).
                        withDrug(new Drug.Builder().
                                withId(resultSet.getInt(11)).
                                withName(resultSet.getString(12)).
                                withPrice(resultSet.getDouble(13)).
                                withCount(resultSet.getInt(14)).
                                withDescription(resultSet.getString(15)).
                                withNeedReceip(resultSet.getBoolean(16)).
                                withProducer(new Producer.Builder().
                                        withId(resultSet.getInt(17)).
                                        withName(resultSet.getString(18)).build()).build()
                        ).build();
                recipeRequestList.add(recipeRequest);

            }

        } catch (SQLException e) {
            LOG.error("cannot find all recipe request",e);
            throw new DaoException("cannot find all recipe request",e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        return recipeRequestList;
    }

    @Override
    public Optional<RecipeRequest> findEntityById(Integer id) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_GET_RECIPE_REQUEST_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                RecipeRequest recipeRequest = new RecipeRequest.Builder().
                        withId(resultSet.getInt(1)).
                        withDateStart(resultSet.getDate(2)).
                        withDateEnd(resultSet.getDate(3)).
                        withUser(new User.Builder().
                                withId(resultSet.getInt(4)).
                                withLogin(resultSet.getString(5)).
                                withPassword(resultSet.getString(6)).
                                withFirstName(resultSet.getString(7)).
                                withLastName(resultSet.getString(8)).
                                withRole(new Role.Builder().
                                        withId(resultSet.getInt(9)).
                                        withRoleName(resultSet.getString(10)).build()).build()
                        ).
                        withDrug(new Drug.Builder().
                                withId(resultSet.getInt(11)).
                                withName(resultSet.getString(12)).
                                withPrice(resultSet.getDouble(13)).
                                withCount(resultSet.getInt(14)).
                                withDescription(resultSet.getString(15)).
                                withNeedReceip(resultSet.getBoolean(16)).
                                withProducer(new Producer.Builder().
                                        withId(resultSet.getInt(17)).
                                        withName(resultSet.getString(18)).build()).build()
                        ).build();
                return Optional.of(recipeRequest);
            }
        } catch (SQLException e) {
            LOG.error("cannot find recipe request by id",e);
            throw new DaoException("cannot find recipe request by id",e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        return Optional.empty();
    }

    @Override
    public RecipeRequest update(RecipeRequest entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_RECIPE_REQUEST);
            preparedStatement.setInt(1, entity.getUser().getId());
            preparedStatement.setInt(2, entity.getDrug().getId());
            preparedStatement.setDate(3, entity.getDateStart());
            preparedStatement.setDate(4, entity.getDateEnd());
            preparedStatement.setInt(5, entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return entity;
            }
        } catch (SQLException e) {
            LOG.error("cannot update recipe request",e);
            throw new DaoException("cannot update recipe request",e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        LOG.error("cannot update recipe request");
        throw new DaoException();
    }

    @Override
    public boolean delete(RecipeRequest entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_DELETE_RECIPE_REQUEST);
            preparedStatement.setInt(1, entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            LOG.error("cannot delete recipe request",e);
            throw new DaoException("cannot update recipe request",e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        return false;
    }
    @Override
    public boolean deleteById(Integer recipeRequestId) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_DELETE_RECIPE_REQUEST);
            preparedStatement.setInt(1, recipeRequestId);
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            LOG.error("cannot delete recipe request",e);
            throw new DaoException("cannot update recipe request",e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        return false;
    }
}
