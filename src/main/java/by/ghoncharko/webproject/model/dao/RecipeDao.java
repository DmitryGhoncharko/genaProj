package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.Recipe;
import by.ghoncharko.webproject.exception.DaoException;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

/**
 * Interface for specific methods RecipeDao
 *
 * @author Dmitry Ghoncharko
 * @see RecipeDaoImpl
 */
public interface RecipeDao extends Dao<Recipe> {
    /**
     * @param userId user id
     * @param drugId drug id
     * @return Optional<Recipe>
     * @throws DaoException when cannot find recipe by user id and drug id
     * @see Recipe
     */
    Optional<Recipe> findEntityByUserIdAndDrugId(Integer userId, Integer drugId) throws DaoException;

    List<Recipe> findRecipesByUserId(Integer userId) throws DaoException;

    boolean updateDateStartAndDateEndByUserIdAndDrugId( Integer userId, Integer drugId, Date dateStart, Date dateEnd) throws DaoException;
    boolean deleteByUserIdAndDrugId(Integer userId, Integer drugId) throws DaoException;

    boolean createRecipeByUserIdAndDrugIdWithDateEnd(Integer userId, Integer drugId,Date dateStart, Date dateEnd) throws DaoException;
}
