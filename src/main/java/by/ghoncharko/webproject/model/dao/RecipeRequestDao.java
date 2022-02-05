package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.RecipeRequest;
import by.ghoncharko.webproject.exception.DaoException;

public interface RecipeRequestDao extends Dao<RecipeRequest> {
    boolean findRecipeRequestIsExistOrRejected(Integer recipeId, Integer userId) throws DaoException;
}
