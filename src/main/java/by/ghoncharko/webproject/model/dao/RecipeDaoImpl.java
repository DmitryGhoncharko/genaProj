package by.ghoncharko.webproject.model.dao;


import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.Producer;
import by.ghoncharko.webproject.entity.Recipe;
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


public class RecipeDaoImpl implements RecipeDao {
    private static final Logger LOG = LogManager.getLogger(RecipeDaoImpl.class);

    private static final String SQL_CREATE_RECIPE = "INSERT INTO recipe" +
            " (user_id, drug_id, date_start, date_end) VALUES (?,?,?,?)";
    private static final String SQL_FIND_ALL_RECIPES = "SELECT recipe.id,recipe.date_start,recipe.date_end, u.id, u.login, u.password," +
            " u.first_name, u.last_name, r.id, r.role_name, d.id, d.name, d.price, d.drug_count, d.description," +
            " d.need_receip, p.id, p.producer_name" +
            " FROM recipe" +
            " INNER JOIN  user u ON recipe.user_id = u.id" +
            " INNER JOIN role r ON u.role_id = r.id" +
            " INNER JOIN drug d ON recipe.drug_id = d.id" +
            " INNER JOIN producer p ON d.producer_id = p.id";
    private static final String SQL_FIND_ALL_RECIPES_BY_USER_ID = "SELECT recipe.id,recipe.date_start,recipe.date_end, u.id, u.login, u.password," +
            " u.first_name, u.last_name, r.id, r.role_name, d.id, d.name, d.price, d.drug_count, d.description," +
            " d.need_receip, p.id, p.producer_name" +
            " FROM recipe" +
            " INNER JOIN  user u ON recipe.user_id = u.id" +
            " INNER JOIN role r ON u.role_id = r.id" +
            " INNER JOIN drug d ON recipe.drug_id = d.id" +
            " INNER JOIN producer p ON d.producer_id = p.id" +
            " WHERE recipe.user_id = ?";
    private static final String SQL_FIND_RECIPE_BY_ID = "SELECT recipe.id, recipe.date_start, recipe.date_end, u.id, u.login, u.password," +
            " u.first_name, u.last_name, r.id, r.role_name, d.id, d.name, d.price, d.drug_count, d.description," +
            " d.need_receip, p.id, p.producer_name" +
            " FROM recipe" +
            " INNER JOIN  user u ON recipe.user_id = u.id" +
            " INNER JOIN role r ON u.role_id = r.id" +
            " INNER JOIN drug d ON recipe.drug_id = d.id" +
            " INNER JOIN producer p ON d.producer_id = p.id" +
            " WHERE recipe.id = ?";
    private static final String SQL_FIND_RECIPE_BY_USER_ID_AND_DRUG_ID = "SELECT recipe.id, recipe.date_start, recipe.date_end, u.id, u.login, u.password," +
            " u.first_name, u.last_name, r.id, r.role_name, d.id, d.name, d.price, d.drug_count, d.description," +
            " d.need_receip, p.id, p.producer_name" +
            " FROM recipe" +
            " INNER JOIN  user u ON recipe.user_id = u.id" +
            " INNER JOIN role r ON u.role_id = r.id" +
            " INNER JOIN drug d ON recipe.drug_id = d.id" +
            " INNER JOIN producer p ON d.producer_id = p.id" +
            " WHERE user_id = ? AND drug_id=? AND date_end > ?";
    private static final String SQL_UPDATE_RECIPE = "UPDATE recipe" +
            " SET user_id = ?, drug_id = ?, date_start = ?, date_end = ?" +
            " WHERE id = ?";
    private static final String SQL_UPDATE_RECIPE_BY_USER_ID_AND_DRUG_ID = "UPDATE recipe" +
            " SET user_id = ?, drug_id = ?, date_start = ?, date_end = ?" +
            " WHERE user_id = ? AND drug_id = ?";
    private static final String SQL_DELETE_RECIPE = "DELETE FROM recipe WHERE id = ?";
    private static final String SQL_DELETE_RECIPE_BY_USER_ID_AND_DRUG_ID = "DELETE FROM recipe WHERE user_id = ? AND drug_id = ?";
    private final Connection connection;
    public RecipeDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Recipe create(Recipe entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_CREATE_RECIPE, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, entity.getUser().getId());
            preparedStatement.setInt(2, entity.getDrug().getId());
            preparedStatement.setDate(3, entity.getDateStart());
            preparedStatement.setDate(4, entity.getDateEnd());
            int countRows = preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (countRows > 0 & resultSet.next()) {
                return new Recipe.Builder().
                        withId(resultSet.getInt(1)).
                        withUser(entity.getUser()).
                        withDateStart(entity.getDateStart()).
                        withDateEnd(entity.getDateEnd()).
                        withDrug(entity.getDrug()).
                        build();
            }
        } catch (SQLException e) {
            LOG.error("cannot create recipe entity", e);
            throw new DaoException("cannot create recipe entity", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        LOG.error("cannot create recipe entity");
        throw new DaoException();
    }

    @Override
    public boolean createRecipeByUserIdAndDrugIdWithDateEnd(Integer userId, Integer drugId, Date dateStart, Date dateEnd) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_CREATE_RECIPE);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, drugId);
            preparedStatement.setDate(3, dateStart);
            preparedStatement.setDate(4, dateEnd);
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            LOG.error("cannot create recipe entity", e);
            throw new DaoException("cannot create recipe entity", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        LOG.error("cannot create recipe entity");
        return false;
    }

    @Override
    public List<Recipe> findAll() throws DaoException {
        List<Recipe> recipeList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_ALL_RECIPES);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Recipe recipe = new Recipe.Builder().
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
                                        withRoleName(resultSet.getString(10)).
                                        build()).
                                build()).
                        withDrug(new Drug.Builder().
                                withId(resultSet.getInt(11)).
                                withName(resultSet.getString(12)).
                                withPrice(resultSet.getDouble(13)).
                                withCount(resultSet.getInt(14)).
                                withDescription(resultSet.getString(15)).
                                withNeedReceip(resultSet.getBoolean(16)).
                                withProducer(new Producer.Builder().
                                        withId(resultSet.getInt(17)).
                                        withName(resultSet.getString(18)).build()
                                ).build()).build();
                recipeList.add(recipe);
            }
        } catch (SQLException e) {
            LOG.error("cannot find all recipes", e);
            throw new DaoException("cannot find all recipes", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        return recipeList;
    }

    @Override
    public List<Recipe> findRecipesByUserId(Integer userId) throws DaoException {
        List<Recipe> recipeList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_ALL_RECIPES_BY_USER_ID);
            preparedStatement.setInt(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Recipe recipe = new Recipe.Builder().
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
                                        withRoleName(resultSet.getString(10)).
                                        build()).
                                build()).
                        withDrug(new Drug.Builder().
                                withId(resultSet.getInt(11)).
                                withName(resultSet.getString(12)).
                                withPrice(resultSet.getDouble(13)).
                                withCount(resultSet.getInt(14)).
                                withDescription(resultSet.getString(15)).
                                withNeedReceip(resultSet.getBoolean(16)).
                                withProducer(new Producer.Builder().
                                        withId(resultSet.getInt(17)).
                                        withName(resultSet.getString(18)).build()
                                ).build()).build();
                recipeList.add(recipe);
            }
        } catch (SQLException e) {
            LOG.error("cannot find all recipes", e);
            throw new DaoException("cannot find all recipes", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        return recipeList;
    }

    @Override
    public Optional<Recipe> findEntityById(Integer id) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_RECIPE_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Recipe recipe = new Recipe.Builder().
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
                                        withRoleName(resultSet.getString(10)).
                                        build()).
                                build()).
                        withDrug(new Drug.Builder().
                                withId(resultSet.getInt(11)).
                                withName(resultSet.getString(12)).
                                withPrice(resultSet.getDouble(13)).
                                withCount(resultSet.getInt(14)).
                                withDescription(resultSet.getString(15)).
                                withNeedReceip(resultSet.getBoolean(16)).
                                withProducer(new Producer.Builder().
                                        withId(resultSet.getInt(17)).
                                        withName(resultSet.getString(18)).build()
                                ).build()).build();
                return Optional.of(recipe);
            }
        } catch (SQLException e) {
            LOG.error("cannot find recipe by id", e);
            throw new DaoException("cannot find recipe by id", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        return Optional.empty();
    }
    @Override
    public Optional<Recipe> findEntityByUserIdAndDrugId(Integer userId, Integer drugId) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_RECIPE_BY_USER_ID_AND_DRUG_ID);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, drugId);
            preparedStatement.setDate(3, new Date(new java.util.Date().getTime()));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Recipe recipe = new Recipe.Builder().
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
                                        withRoleName(resultSet.getString(10)).
                                        build()).
                                build()).
                        withDrug(new Drug.Builder().
                                withId(resultSet.getInt(11)).
                                withName(resultSet.getString(12)).
                                withPrice(resultSet.getDouble(13)).
                                withCount(resultSet.getInt(14)).
                                withDescription(resultSet.getString(15)).
                                withNeedReceip(resultSet.getBoolean(16)).
                                withProducer(new Producer.Builder().
                                        withId(resultSet.getInt(17)).
                                        withName(resultSet.getString(18)).build()
                                ).build()).build();
                return Optional.of(recipe);
            }
        } catch (SQLException e) {
            LOG.error("cannot find recipe by id", e);
            throw new DaoException("cannot find recipe by id", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        return Optional.empty();
    }

    @Override
    public Recipe update(Recipe entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_RECIPE);
            preparedStatement.setInt(1, entity.getUser().getId());
            preparedStatement.setInt(2, entity.getDrug().getId());
            preparedStatement.setDate(3, entity.getDateStart());
            preparedStatement.setDate(4, entity.getDateEnd());
            preparedStatement.setInt(5,entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return entity;
            }
        } catch (SQLException e) {
            LOG.error("cannot update recipe", e);
            throw new DaoException("cannot update recipe", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        LOG.error("cannot update recipe");
        throw new DaoException();
    }

    @Override
    public boolean updateDateStartAndDateEndByUserIdAndDrugId(Integer userId, Integer drugId, Date dateStart, Date dateEnd) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_RECIPE_BY_USER_ID_AND_DRUG_ID);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, drugId);
            preparedStatement.setDate(3, dateStart);
            preparedStatement.setDate(4, dateEnd);
            preparedStatement.setInt(5,userId);
            preparedStatement.setInt(6,drugId);
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            LOG.error("cannot update recipe", e);
            throw new DaoException("cannot update recipe", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        LOG.error("cannot update recipe");
       return false;
    }

    @Override
    public boolean delete(Recipe entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_DELETE_RECIPE);
            preparedStatement.setInt(1, entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            LOG.error("canot delete recipe", e);
            throw new DaoException("canot delete recipe", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        return false;
    }

    @Override
    public boolean deleteByUserIdAndDrugId(Integer userId, Integer drugId) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_DELETE_RECIPE_BY_USER_ID_AND_DRUG_ID);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2,drugId);
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            LOG.error("canot delete recipe", e);
            throw new DaoException("canot delete recipe", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        return false;
    }

}
