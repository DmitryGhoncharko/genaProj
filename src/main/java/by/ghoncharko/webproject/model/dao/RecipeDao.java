package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.Recipe;
import by.ghoncharko.webproject.exception.DaoException;

import java.util.Optional;

public interface RecipeDao extends Dao<Recipe> {

    Optional<Recipe> findActiveRecipeByUserIdAndDrugId(Integer userId, Integer drugId) throws DaoException;


}
