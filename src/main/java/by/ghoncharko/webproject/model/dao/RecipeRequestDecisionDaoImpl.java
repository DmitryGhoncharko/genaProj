package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.Producer;
import by.ghoncharko.webproject.entity.Recipe;
import by.ghoncharko.webproject.entity.RecipeRequest;
import by.ghoncharko.webproject.entity.RecipeRequestDecision;
import by.ghoncharko.webproject.entity.Role;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RecipeRequestDecisionDaoImpl implements RecipeRequestDecisionDao{
    private static final Logger LOG = LogManager.getLogger(RecipeRequestDecisionDaoImpl.class);
    private static final String SQL_CREATE_RECIPE_REQUEST_DECISION = "INSERT INTO recipe_request_decision(recipe_request_id, is_extended, date_decision) VALUES (?,?,?)";
    private static final String SQL_FIND_ALL_RECIPE_REQUEST_DECISIONS = "SELECT" +
            " recipe_request_decision.id, rr.id, r.id, u.id, u.login, u.password, r2.role_name, u.first_name, u.last_name, u.is_banned," +
            " d.id, d.name, d.price, d.drug_count, d.description, p.id, p.producer_name, d.need_recipe, d.is_deleted, date_start, date_end" +
            " is_extended, date_decision" +
            " FROM recipe_request_decision" +
            " INNER JOIN recipe_request rr on recipe_request_decision.recipe_request_id = rr.id" +
            " INNER JOIN recipe r on rr.recipe_id = r.id" +
            " INNER JOIN user u on r.user_id = u.id" +
            " INNER JOIN role r2 on u.role_id = r2.id" +
            " INNER JOIN drug d on r.drug_id = d.id" +
            " INNER JOIN producer p on d.producer_id = p.id";
    private static final String SQL_FIND_RECIPE_REQUEST_DECISION_BY_ID = "SELECT" +
            " recipe_request_decision.id, rr.id, r.id, u.id, u.login, u.password, r2.role_name, u.first_name, u.last_name, u.is_banned," +
            " d.id, d.name, d.price, d.drug_count, d.description, p.id, p.producer_name, d.need_recipe, d.is_deleted, date_start, date_end" +
            " is_extended, date_decision" +
            " FROM recipe_request_decision" +
            " INNER JOIN recipe_request rr on recipe_request_decision.recipe_request_id = rr.id" +
            " INNER JOIN recipe r on rr.recipe_id = r.id" +
            " INNER JOIN user u on r.user_id = u.id" +
            " INNER JOIN role r2 on u.role_id = r2.id" +
            " INNER JOIN drug d on r.drug_id = d.id" +
            " INNER JOIN producer p on d.producer_id = p.id" +
            " WHERE recipe_request_decision.id = ?";
    private static final String SQL_UPDATE_RECIPE_REQUEST_DECISION_BY_ID = "UPDATE recipe_request_decision SET recipe_request_id = ? , is_extended = ?, date_decision = ?" +
            " WHERE id = ?";
    private static final String SQL_DELETE_RECIPE_REQUEST_DECISIOH_BY_ID = "DELETE FROM recipe_request_decision WHERE id = ?";
    private final Connection connection;

    public RecipeRequestDecisionDaoImpl(Connection connection){
        this.connection = connection;
    }

    @Override
    public RecipeRequestDecision create(RecipeRequestDecision entity) throws DaoException {
       try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_RECIPE_REQUEST_DECISION, Statement.RETURN_GENERATED_KEYS)){
           preparedStatement.setInt(1,entity.getRecipeRequest().getId());
           preparedStatement.setBoolean(2,entity.getExtended());
           preparedStatement.setDate(3,entity.getDateDecision());
           final  int countCreatedRows = preparedStatement.executeUpdate();
           if(countCreatedRows>0){
               final ResultSet resultSet = preparedStatement.executeQuery();
               if(resultSet.next()){
                   return new RecipeRequestDecision.Builder().
                           withId(resultSet.getInt(1)).
                           withRecipeRequest(entity.getRecipeRequest()).
                           withIsExtended(entity.getExtended()).
                           withDateDecision(entity.getDateDecision()).
                           build();
               }
           }
       }catch (SQLException e){
            LOG.error("Cannot create RecipeRequestDecision",e);
            throw new DaoException("Cannot create RecipeRequestDecision",e);
       }
       LOG.error("Cannot create RecipeRequestDecision");
       throw new DaoException("Cannot create RecipeRequestDecision");
    }

    @Override
    public List<RecipeRequestDecision> findAll() throws DaoException {
        final List<RecipeRequestDecision> recipeRequestDecisionList = new ArrayList<>();
        try(final Statement statement = connection.createStatement()){
            final ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_RECIPE_REQUEST_DECISIONS);
            while (resultSet.next()){
                final RecipeRequestDecision recipeRequestDecision = new RecipeRequestDecision.Builder().
                        withId(resultSet.getInt(1)).
                        withRecipeRequest(new RecipeRequest.Builder().
                                withId(resultSet.getInt(2)).
                                withRecipe(new Recipe.Builder().
                                        withId(resultSet.getInt(3)).
                                        withUser(new User.Builder().
                                                withId(resultSet.getInt(4)).
                                                withLogin(resultSet.getString(5)).
                                                withPassword(resultSet.getString(6)).
                                                withRole(Role.valueOf(resultSet.getString(7))).
                                                withFirstName(resultSet.getString(8)).
                                                withLastName(resultSet.getString(9)).
                                                withBannedStatus(resultSet.getBoolean(10)).
                                                build()).
                                        withDrug(new Drug.Builder().
                                                withId(resultSet.getInt(11)).
                                                withName(resultSet.getString(12)).
                                                withPrice(resultSet.getBigDecimal(13)).
                                                withCount(resultSet.getInt(14)).
                                                withDescription(resultSet.getString(15)).
                                                withProducer(new Producer.Builder().
                                                        withId(resultSet.getInt(16)).
                                                        withName(resultSet.getString(17)).
                                                        build()).
                                                withNeedReceip(resultSet.getBoolean(18)).
                                                withIsDeleted(resultSet.getBoolean(19)).
                                                build()).
                                        withDateStart(resultSet.getDate(20)).
                                        withDateEnd(resultSet.getDate(21)).
                                        build())
                                .build()).
                        withIsExtended(resultSet.getBoolean(22)).
                        withDateDecision(resultSet.getDate(23)).build();
                recipeRequestDecisionList.add(recipeRequestDecision);
            }
        }catch (SQLException e){
            LOG.error("Cannot find all RecipeRequestDecisions",e);
            throw new DaoException("Cannot find all RecipeRequestDecisions",e);
        }
        LOG.info("Cannot find all RecipeRequestDecisions");
        return Collections.emptyList();
    }

    @Override
    public Optional<RecipeRequestDecision> findEntityById(Integer id) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_RECIPE_REQUEST_DECISION_BY_ID)){
            preparedStatement.setInt(1,id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return Optional.of(new RecipeRequestDecision.Builder().
                        withId(resultSet.getInt(1)).
                        withRecipeRequest(new RecipeRequest.Builder().
                                withId(resultSet.getInt(2)).
                                withRecipe(new Recipe.Builder().
                                        withId(resultSet.getInt(3)).
                                        withUser(new User.Builder().
                                                withId(resultSet.getInt(4)).
                                                withLogin(resultSet.getString(5)).
                                                withPassword(resultSet.getString(6)).
                                                withRole(Role.valueOf(resultSet.getString(7))).
                                                withFirstName(resultSet.getString(8)).
                                                withLastName(resultSet.getString(9)).
                                                withBannedStatus(resultSet.getBoolean(10)).
                                                build()).
                                        withDrug(new Drug.Builder().
                                                withId(resultSet.getInt(11)).
                                                withName(resultSet.getString(12)).
                                                withPrice(resultSet.getBigDecimal(13)).
                                                withCount(resultSet.getInt(14)).
                                                withDescription(resultSet.getString(15)).
                                                withProducer(new Producer.Builder().
                                                        withId(resultSet.getInt(16)).
                                                        withName(resultSet.getString(17)).
                                                        build()).
                                                withNeedReceip(resultSet.getBoolean(18)).
                                                withIsDeleted(resultSet.getBoolean(19)).
                                                build()).
                                        withDateStart(resultSet.getDate(20)).
                                        withDateEnd(resultSet.getDate(21)).
                                        build())
                                .build()).
                        withIsExtended(resultSet.getBoolean(22)).
                        withDateDecision(resultSet.getDate(23)).build());
            }
        }catch (SQLException e){
            LOG.error("Cannot find RecipeRequestDecision by id",e);
            throw new DaoException("Cannot find RecipeRequestDecision by id",e);
        }
        LOG.info("Cannot find RecipeRequestDecision by id");
        return Optional.empty();
    }

    @Override
    public RecipeRequestDecision update(RecipeRequestDecision entity) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_RECIPE_REQUEST_DECISION_BY_ID)){
            preparedStatement.setInt(1,entity.getRecipeRequest().getId());
            preparedStatement.setBoolean(2,entity.getExtended());
            preparedStatement.setDate(3,entity.getDateDecision());
            preparedStatement.setInt(4,entity.getId());
            final int countUpdatedRows = preparedStatement.executeUpdate();
            if(countUpdatedRows>0){
                return entity;
            }
        }catch (SQLException e){
            LOG.error("Cannot updae RecipeRequestDecision",e);
            throw new DaoException("Cannot updae RecipeRequestDecision",e);
        }
        LOG.error("Cannot updae RecipeRequestDecision");
        throw new DaoException("Cannot updae RecipeRequestDecision");
    }

    @Override
    public boolean delete(RecipeRequestDecision entity) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_RECIPE_REQUEST_DECISIOH_BY_ID)){
            preparedStatement.setInt(1,entity.getId());
            final int countDeletedRows = preparedStatement.executeUpdate();
            return countDeletedRows>0;
        }catch (SQLException e){
            LOG.error("Cannot delete RecipeRequestDecision by id",e);
            throw new DaoException("Cannot delete RecipeRequestDecision by id",e);
        }
    }
}
