package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.RecipeRequest;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.RecipeRequestDao;
import by.ghoncharko.webproject.model.dao.RecipeRequestDaoImpl;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class RecipeRequestServiceImpl implements RecipeRequestService{
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    @Override
    public boolean createRecipeRequestByUserIdAndDrugIdWithDateStartAndDateEnd(Integer userId, Integer drugId, Date dateStart, Date dateEnd, boolean isNeedRecipe) {
        if(isNeedRecipe){
            Connection connection = connectionPool.getConnection();
            RecipeRequestDao recipeRequestDao = new RecipeRequestDaoImpl(connection);
            try{
                return recipeRequestDao.createRecipeRequestByUserIdAndDrugIdWithDateStartAndDateEnd(userId, drugId, dateStart, dateEnd);
            }catch (DaoException e){

            }finally {
                Service.connectionClose(connection);
            }
        }
       return false;
    }

    @Override
    public List<RecipeRequest> findAll() {
        return null;
    }

    public static RecipeRequestServiceImpl getInstance(){
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final RecipeRequestServiceImpl INSTANCE = new RecipeRequestServiceImpl();
    }
}
