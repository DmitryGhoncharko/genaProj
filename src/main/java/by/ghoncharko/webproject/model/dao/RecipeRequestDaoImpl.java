package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.Producer;
import by.ghoncharko.webproject.entity.Recipe;
import by.ghoncharko.webproject.entity.RecipeRequest;
import by.ghoncharko.webproject.entity.Role;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecipeRequestDaoImpl extends AbstractDao<RecipeRequest> implements RecipeRequestDao {
    private static final Logger LOG = LogManager.getLogger(RecipeRequestDaoImpl.class);
    private static final String SQL_CREATE_RECIPE_REQUEST = "INSERT INTO recipe_request(recipe_id) VALUES (?)";
    private static final String SQL_FIND_ALL_RECIPE_REQUESTS = "SELECT" +
            " recipe_request.id, r.id, u.id, u.login, u.password, r2.role_name, u.first_name, u.last_name , u.is_banned, d.id, d.name," +
            " d.price, d.drug_count, d.description, p.id, p.producer_name, d.need_recipe, d.is_deleted, r.date_start, r.date_end" +
            " FROM recipe_request" +
            " INNER JOIN recipe r on recipe_request.recipe_id = r.id" +
            " INNER JOIN user u on r.user_id = u.id" +
            " INNER JOIN role r2 on u.role_id = r2.id" +
            " INNER JOIN drug d on r.drug_id = d.id" +
            " INNER JOIN producer p on d.producer_id = p.id";
    private static final String SQL_FIND_RECIPE_REQUEST_BY_ID = "SELECT" +
            " recipe_request.id, r.id, u.id, u.login, u.password, r2.role_name, u.first_name, u.last_name , u.is_banned, d.id, d.name," +
            " d.price, d.drug_count, d.description, p.id, p.producer_name, d.need_recipe, d.is_deleted, r.date_start, r.date_end" +
            " FROM recipe_request" +
            " INNER JOIN recipe r on recipe_request.recipe_id = r.id" +
            " INNER JOIN user u on r.user_id = u.id" +
            " INNER JOIN role r2 on u.role_id = r2.id" +
            " INNER JOIN drug d on r.drug_id = d.id" +
            " INNER JOIN producer p on d.producer_id = p.id" +
            " WHERE recipe_request.id = ?";
    private static final String SQL_FIND_RECIPE_REQUEST_IS_EXIST_OR_REJECTED_BY_RECIPE_AND_USER_ID = "SELECT  recipe_request.id FROM  recipe_request" +
            " INNER JOIN recipe r on recipe_request.recipe_id = r.id" +
            " LEFT JOIN recipe_request_decision rrd on recipe_request.id = rrd.recipe_request_id" +
            " WHERE recipe_request.recipe_id = ? and r.user_id = ? and r.date_end>=current_date and (rrd.id is null or rrd.is_extended = false)";
    private static final String SQL_UPDATE_RECIPE_REQUEST_BY_ID = "UPDATE recipe_request SET recipe_id = ? " +
            " WHERE id = ?";
    private static final String SQL_DELETE_RECIPE_REQUEST_BY_ID = "DELETE FROM recipe_request WHERE id = ?";


    public RecipeRequestDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public RecipeRequest create(RecipeRequest entity) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_RECIPE_REQUEST, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, entity.getRecipe().getId());
            final int countUpdatedRows = preparedStatement.executeUpdate();
            if (countUpdatedRows > 0) {
                final ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    return new RecipeRequest.Builder().
                            withId(resultSet.getInt(1)).
                            withRecipe(entity.getRecipe()).
                            build();
                }
            }
        } catch (SQLException e) {
            LOG.error("Cannot create recipeRequest", e);
            throw new DaoException("Cannot create recipeRequest", e);
        }
        LOG.error("Cannot create recipeRequest");
        throw new DaoException("Cannot create recipeRequest");
    }

    @Override
    public boolean findRecipeRequestIsExistOrRejected(Integer recipeId, Integer userId) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_RECIPE_REQUEST_IS_EXIST_OR_REJECTED_BY_RECIPE_AND_USER_ID)) {
            preparedStatement.setInt(1, recipeId);
            preparedStatement.setInt(2, userId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            LOG.error("Cannot find recipe request is exsist or rejected", e);
            throw new DaoException("Cannot find recipe request is exsist or rejected", e);
        }
        LOG.info("Recipe request is not exist");
        return false;
    }

    @Override
    public List<RecipeRequest> findAll() throws DaoException {
        final List<RecipeRequest> recipeRequestList = new ArrayList<>();
        try (final Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_RECIPE_REQUESTS);
            while (resultSet.next()) {
                final RecipeRequest recipeRequest = extractEntity(resultSet);
                recipeRequestList.add(recipeRequest);
            }
        } catch (SQLException e) {
            LOG.error("Cannot find all recipe requests", e);
            throw new DaoException("Cannot find all recipe requests", e);
        }
        return recipeRequestList;
    }

    @Override
    public Optional<RecipeRequest> findEntityById(Integer id) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_RECIPE_REQUEST_BY_ID)) {
            preparedStatement.setInt(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            return Optional.of(extractEntity(resultSet));
        } catch (SQLException e) {
            LOG.error("Cannot find recipe request by id", e);
            throw new DaoException("Cannot find recipe request by id", e);
        }
    }

    @Override
    public RecipeRequest update(RecipeRequest entity) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_RECIPE_REQUEST_BY_ID)) {
            preparedStatement.setInt(1, entity.getRecipe().getId());
            preparedStatement.setInt(2, entity.getId());
            final int countUpdatedRows = preparedStatement.executeUpdate();
            if (countUpdatedRows > 0) {
                return entity;
            }
        } catch (SQLException e) {
            LOG.error("Cannot update recipe request by id", e);
            throw new DaoException("Cannot update recipe request by id", e);
        }
        LOG.error("Cannot update recipe request by id");
        throw new DaoException("Cannot update recipe request by id");
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_RECIPE_REQUEST_BY_ID)) {
            return deleteBillet(preparedStatement, id);
        } catch (SQLException e) {
            LOG.error("Cannot delete recipeRequest by id", e);
            throw new DaoException("Cannot delete recipeRequest by id", e);
        }
    }

    @Override
    protected RecipeRequest extractEntity(ResultSet resultSet) throws SQLException {
        return new RecipeRequest.Builder().
                withId(resultSet.getInt(1)).
                withRecipe(new Recipe.Builder().
                        withId(resultSet.getInt(2)).
                        withUser(new User.Builder().
                                withId(resultSet.getInt(3)).
                                withLogin(resultSet.getString(4)).
                                withPassword(resultSet.getString(5)).
                                withRole(Role.valueOf(resultSet.getString(6))).
                                withFirstName(resultSet.getString(7)).
                                withLastName(resultSet.getString(8)).
                                withBannedStatus(resultSet.getBoolean(9)).
                                build()).
                        withDrug(new Drug.Builder().
                                withId(resultSet.getInt(10)).
                                withName(resultSet.getString(11)).
                                withPrice(BigDecimal.valueOf(resultSet.getDouble(12))).
                                withCount(resultSet.getInt(13)).
                                withDescription(resultSet.getString(14)).
                                withProducer(new Producer.Builder().
                                        withId(resultSet.getInt(15)).
                                        withName(resultSet.getString(16)).
                                        build()).
                                withNeedReceip(resultSet.getBoolean(17)).
                                withIsDeleted(resultSet.getBoolean(18)).
                                build()).
                        withDateStart(resultSet.getDate(19)).
                        withDateEnd(resultSet.getDate(20)).
                        build()).
                build();
    }
}
