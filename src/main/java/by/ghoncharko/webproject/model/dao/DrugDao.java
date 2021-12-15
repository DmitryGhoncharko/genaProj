package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.exception.DaoException;

import java.util.List;

/**
 * Interface for DrugDao with specific methods
 *
 * @author Dmitry Ghoncharko
 * @see DrugDaoImpl
 */
public interface DrugDao extends Dao<Drug> {
    /**
     * @return List<Drug>
     * @throws DaoException when cannot found drugs
     * @see Drug
     */
    List<Drug> findAllWhereCountMoreThanZeroLimitOffsetPagination(Integer limit, Integer offset) throws DaoException;

    int findCountAllWhereCountMoreThanZero() throws DaoException;
    /**
     * @param userId accept userId
     * @return List<Drug>
     * @throws DaoException when cannot find drugs
     * @see Drug
     */
    List<Drug> findAllWhereCountMoreThanZeroWithStatusActiveByUserIdAndCalculateCountLimitOffsetPagination(Integer userId,Integer limit, Integer offset) throws DaoException;

    int findAllWhereCountMoreThanZeroWithStatusActiveByUserId(Integer userId) throws DaoException;

    List<Drug> findAllLimitOffsetPagination(Integer limit, Integer offset) throws DaoException;

    int findCountAllDrugs() throws DaoException;
    /**
     * @param drugId accept drug id
     * @return count drugs
     * @throws DaoException when cannot find drug
     */
    Integer getCountByDrugId(Integer drugId) throws DaoException;

    /**
     * @param count  count of drugs
     * @param drugId drug id
     * @return true if drug update and false if drug cannot update
     * @throws DaoException when cannot update drug
     */
    boolean update(Integer count, Integer drugId) throws DaoException;
    List<Drug> findAllWhereNeedRecipeLimitOffsetPagination(Integer limit, Integer offset) throws DaoException;
    int findCountAllDrugsWhereNeedRecipe() throws DaoException;
    boolean deleteByDrugId(Integer drugId) throws DaoException;
}
