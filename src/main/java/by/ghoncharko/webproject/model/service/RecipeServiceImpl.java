package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.Recipe;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.Dao;
import by.ghoncharko.webproject.model.dao.RecipeDao;
import by.ghoncharko.webproject.model.dao.RecipeDaoImpl;
import by.ghoncharko.webproject.validator.ValidateCreateRecipe;

import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RecipeServiceImpl implements RecipeService {
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private RecipeServiceImpl() {
    }

    @Override
    public List<Recipe> findRecipesByUserId(Integer userId) {
        final Connection connection = connectionPool.getConnection();
        final RecipeDao recipeDao = new RecipeDaoImpl(connection);
        try {
            return recipeDao.findRecipesByUserId(userId);
        } catch (DaoException e) {

        } finally {
            Service.connectionClose(connection);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean createRecipeByUserIdAndDrugId(Integer userId, Integer drugId, Date dateEnd) {
        final boolean dataIsValid = ValidateCreateRecipe.getInstance().validate(userId, drugId, dateEnd);
        if (dataIsValid) {
            final Connection connection = connectionPool.getConnection();
            final RecipeDao recipeDao = new RecipeDaoImpl(connection);
            final String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            final Date dateStart = Date.valueOf(timeStamp);
            try {
                Optional<Recipe> recipeFromDB = recipeDao.findEntityByUserIdAndDrugId(userId, drugId);
                if(recipeFromDB.isPresent()){
                   return recipeDao.updateDateStartAndDateEndByUserIdAndDrugId(userId, drugId, dateStart,dateEnd);
                }
                return recipeDao.createRecipeByUserIdAndDrugIdWithDateEnd(userId, drugId, dateStart, dateEnd);
            } catch (DaoException e) {

            } finally {
                Service.connectionClose(connection);
            }
        }
        return false;
    }

    @Override
    public List<Recipe> findAll() {
        return null;
    }

    static RecipeServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final RecipeServiceImpl INSTANCE = new RecipeServiceImpl();
    }
}
