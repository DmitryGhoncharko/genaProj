package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.RecipeRequest;
import by.ghoncharko.webproject.exception.DaoException;

import java.sql.Date;

public interface RecipeRequestDao extends Dao<RecipeRequest> {
    boolean createRecipeRequestByUserIdAndDrugIdWithDateStartAndDateEnd(Integer userId, Integer drugId, Date dateStart, Date dateEnd) throws DaoException;
}
