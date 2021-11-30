package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.Recipe;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.RecipeDao;
import by.ghoncharko.webproject.model.dao.RecipeDaoImpl;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

public class RecipeServiceImpl implements RecipeService{
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    @Override
    public List<Recipe> findRecipesByUserId(Integer userId) {
        final Connection connection = connectionPool.getConnection();
        RecipeDao recipeDao = new RecipeDaoImpl(connection);
        try{
            return recipeDao.findRecipesByUserId(userId);
        }catch (DaoException e){

        }finally {
            Service.connectionClose(connection);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Recipe> findAll() throws DaoException {
        return null;
    }
}
