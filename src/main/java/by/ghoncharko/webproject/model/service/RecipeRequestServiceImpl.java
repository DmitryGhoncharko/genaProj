package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.Recipe;
import by.ghoncharko.webproject.entity.RecipeRequest;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.dao.DaoHelper;
import by.ghoncharko.webproject.model.dao.DaoHelperFactory;
import by.ghoncharko.webproject.model.dao.RecipeDao;
import by.ghoncharko.webproject.model.dao.RecipeRequestDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class RecipeRequestServiceImpl implements RecipeRequestService {
    private static final Logger LOG = LogManager.getLogger(RecipeRequestServiceImpl.class);

    private final DaoHelperFactory daoHelperFactory;

    public RecipeRequestServiceImpl(DaoHelperFactory daoHelperFactory) {
        this.daoHelperFactory = daoHelperFactory;
    }

    @Override
    public List<RecipeRequest> findAll() throws ServiceException {
        return null;
    }

    @Override
    public boolean createRecipeRequest(User user, Integer recipeId) {
        final DaoHelper daoHelper = daoHelperFactory.create();
        daoHelper.startTransaction();
        final int userId = user.getId();
        final RecipeDao recipeDao = daoHelper.createRecipeDao();
        try {
            final Optional<Recipe> recipeFromDB = recipeDao.findActiveRecipeByUserIdAndDrugId(userId, recipeId);
            if (recipeFromDB.isPresent()) {
                final Recipe recipe = recipeFromDB.get();
                final RecipeRequestDao recipeRequestDao = daoHelper.createRecipeRequestDao();
                final boolean recipeRequestIsExistOrRejected = recipeRequestDao.findRecipeRequestIsExistOrRejected(recipeId, userId);
                if (!recipeRequestIsExistOrRejected) {
                    recipeRequestDao.create(new RecipeRequest.Builder().
                            withRecipe(recipe).build());
                    return true;
                }
            }
        } catch (DaoException e) {
            daoHelper.rollbackTransactionAndCloseConnection();
        }
        return false;
    }
}
