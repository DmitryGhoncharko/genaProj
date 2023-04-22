package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.*;
import by.ghoncharko.webproject.entity.Product;
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

public class RecipeDaoImpl extends AbstractDao<Recipe> implements RecipeDao {
    private static final Logger LOG = LogManager.getLogger(RecipeDaoImpl.class);
    private static final String SQL_CREATE_RECIPE = "INSERT INTO recipe(user_id, drug_id, date_start, date_end)  VALUES (?,?,?,?)";
    private static final String SQL_FIND_ALL_RECIPES = "SELECT" +
            " recipe.id, u.id, u.login, u.password, r.role_name, u.first_name, u.last_name, u.is_banned,  d.id," +
            " d.name, d.price, d.drug_count, d.description, p.id, p.producer_name, d.need_recipe, d.is_deleted,  date_start, date_end" +
            " FROM recipe" +
            " INNER JOIN user u ON recipe.user_id = u.id" +
            " INNER JOIN drug d ON recipe.drug_id = d.id" +
            " INNER JOIN producer p ON d.producer_id = p.id" +
            " INNER JOIN role r ON u.role_id = r.id";
    private static final String SQL_FIND_RECIPE_BY_RECIPE_ID = "SELECT" +
            " recipe.id, u.id, u.login, u.password, r.role_name, u.first_name, u.last_name, u.is_banned,  d.id," +
            " d.name, d.price, d.drug_count, d.description, p.id, p.producer_name, d.need_recipe, d.is_deleted,  date_start, date_end" +
            " FROM recipe" +
            " INNER JOIN user u ON recipe.user_id = u.id" +
            " INNER JOIN drug d ON recipe.drug_id = d.id" +
            " INNER JOIN producer p ON d.producer_id = p.id" +
            " INNER JOIN role r ON u.role_id = r.id" +
            " WHERE recipe.id = ?";
    //TODO MB REWRITE CHECK DATE
    private static final String SQL_FIND_RECIPE_BY_RECIPE_ID_AND_USER_ID_WHERE_DATE_END_MORE_THAN_ATTRIBUTE = "SELECT" +
            " recipe.id, u.id, u.login, u.password, r.role_name, u.first_name, u.last_name, u.is_banned,  d.id," +
            " d.name, d.price, d.drug_count, d.description, p.id, p.producer_name, d.need_recipe, d.is_deleted,  date_start, date_end" +
            " FROM recipe" +
            " INNER JOIN user u ON recipe.user_id = u.id" +
            " INNER JOIN drug d ON recipe.drug_id = d.id" +
            " INNER JOIN producer p ON d.producer_id = p.id" +
            " INNER JOIN role r ON u.role_id = r.id" +
            " WHERE u.id = ? AND  drug_id = ? AND  date_end >=current_date";
    private static final String SQL_UPDATE_RECIPE_BY_RECIPE_ID = "UPDATE recipe" +
            " SET user_id = ?, drug_id = ?, date_start = ?, date_end = ?" +
            "WHERE id = ?";
    private static final String SQL_DELETE_RECIPE_BY_RECIPE_ID = "DELETE FROM recipe WHERE id = ?";


    public RecipeDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Recipe create(Recipe entity) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_RECIPE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, entity.getUser().getId());
            preparedStatement.setInt(2, entity.getDrug().getId());
            preparedStatement.setDate(3, entity.getDateStart());
            preparedStatement.setDate(4, entity.getDateEnd());
            final int countUpdatedRows = preparedStatement.executeUpdate();
            if (countUpdatedRows > 0) {
                final ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    return new Recipe.Builder().
                            withId(resultSet.getInt(1)).
                            withUser(entity.getUser()).
                            withDrug(entity.getDrug()).
                            withDateStart(entity.getDateStart()).
                            withDateEnd(entity.getDateEnd()).
                            build();
                }
            }
        } catch (SQLException e) {
            LOG.error("Cannot create recipe", e);
            throw new DaoException("Cannot create recipe", e);
        }
        LOG.error("Cannot create recipe");
        throw new DaoException("Cannot create recipe");
    }


    @Override
    public Optional<Recipe> findActiveRecipeByUserIdAndDrugId(Integer userId, Integer drugId) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_RECIPE_BY_RECIPE_ID_AND_USER_ID_WHERE_DATE_END_MORE_THAN_ATTRIBUTE)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, drugId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(extractEntity(resultSet));
            }
        } catch (SQLException e) {
            LOG.error("Cannot find recipe by recipe id and user id", e);
            throw new DaoException("Cannot find recipe by recipe id and user id", e);
        }
        LOG.info("Cannot find recipe by recipe id and user id");
        return Optional.empty();
    }

    @Override
    public List<Recipe> findAll() throws DaoException {
        final List<Recipe> recipeList = new ArrayList<>();
        try (final Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_RECIPES);
            while (resultSet.next()) {
                final Recipe recipe = extractEntity(resultSet);
                recipeList.add(recipe);
            }
        } catch (SQLException e) {
            LOG.error("Cannot find all recipes", e);
            throw new DaoException("Cannot find all recipes", e);
        }
        return recipeList;
    }

    @Override
    public Optional<Recipe> findEntityById(Integer id) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_RECIPE_BY_RECIPE_ID)) {
            preparedStatement.setInt(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(extractEntity(resultSet));
            }
        } catch (SQLException e) {
            LOG.error("Cannot find recipe by id", e);
            throw new DaoException("Cannot find recipe by id", e);
        }
        LOG.info("Not found recipe by id");
        return Optional.empty();
    }

    @Override
    public Recipe update(Recipe entity) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_RECIPE_BY_RECIPE_ID)) {
            preparedStatement.setInt(1, entity.getUser().getId());
            preparedStatement.setInt(2, entity.getDrug().getId());
            preparedStatement.setDate(3, entity.getDateStart());
            preparedStatement.setDate(4, entity.getDateEnd());
            preparedStatement.setInt(5, entity.getId());
            final int countUpdatedRows = preparedStatement.executeUpdate();
            if (countUpdatedRows > 0) {
                return entity;
            }
        } catch (SQLException e) {
            LOG.error("Cannot update recipe by id", e);
            throw new DaoException("Cannot update recipe by id", e);
        }
        LOG.error("Cannot update recipe by id");
        throw new DaoException("Cannot update recipe by id");
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_RECIPE_BY_RECIPE_ID)) {
            return deleteBillet(preparedStatement, id);
        } catch (SQLException e) {
            LOG.error("Cannot delete recipe by id", e);
            throw new DaoException("Cannot delete recipe by id", e);
        }
    }

    @Override
    protected Recipe extractEntity(ResultSet resultSet) throws SQLException {
        return new Recipe.Builder().
                withId(resultSet.getInt(1)).
                withUser(new User.Builder().
                        withId(resultSet.getInt(2)).
                        withLogin(resultSet.getString(3)).
                        withPassword(resultSet.getString(4)).
                        withRole(Role.valueOf(resultSet.getString(5))).
                        withFirstName(resultSet.getString(6)).
                        withLastName(resultSet.getString(7)).
                        withBannedStatus(resultSet.getBoolean(8)).
                        build()).
                withDrug(new Product.Builder().
                        withId(resultSet.getInt(9)).
                        withName(resultSet.getString(10)).
                        withPrice(BigDecimal.valueOf(resultSet.getDouble(11))).
                        withCount(resultSet.getInt(12)).
                        withDescription(resultSet.getString(13)).
                        withProducer(new Producer.Builder().
                                withId(resultSet.getInt(14)).
                                withName(resultSet.getString(15)).
                                build()).
                        withNeedReceip(resultSet.getBoolean(16)).
                        withIsDeleted(resultSet.getBoolean(17)).
                        build()).
                withDateStart(resultSet.getDate(18)).
                withDateEnd(resultSet.getDate(19)).
                build();
    }
}
