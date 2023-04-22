package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.Recipe;
import by.ghoncharko.webproject.entity.RecipeRequest;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.RecipeDao;
import by.ghoncharko.webproject.model.dao.RecipeDaoImpl;
import by.ghoncharko.webproject.model.dao.RecipeRequestDao;
import by.ghoncharko.webproject.model.dao.RecipeRequestDaoImpl;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
public class RecipeRequestServiceImpl implements RecipeRequestService {
    private static final Logger LOG = LogManager.getLogger(RecipeRequestServiceImpl.class);

    private final ConnectionPool connectionPool;



    @Override
    public List<RecipeRequest> findAll() throws ServiceException {
        return null;
    }

    @Override
    public boolean createRecipeRequest(User user, Integer recipeId) {


        try(Connection connection = connectionPool.getConnection(); Connection connection1 = connectionPool.getConnection()) {
            final int userId = user.getId();
            final RecipeDao recipeDao = new RecipeDaoImpl(connection);
            final Optional<Recipe> recipeFromDB = recipeDao.findActiveRecipeByUserIdAndDrugId(userId, recipeId);
            if (recipeFromDB.isPresent()) {
                final Recipe recipe = recipeFromDB.get();
                final RecipeRequestDao recipeRequestDao = new RecipeRequestDaoImpl(connection1);
                final boolean recipeRequestIsExistOrRejected = recipeRequestDao.findRecipeRequestIsExistOrRejected(recipeId, userId);
                if (!recipeRequestIsExistOrRejected) {
                    recipeRequestDao.create(new RecipeRequest.Builder().
                            withRecipe(recipe).build());
                    return true;
                }
            }
        } catch (Exception e) {

        }
        return false;
    }
}
