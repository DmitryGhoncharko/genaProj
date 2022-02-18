package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface DrugDao extends Dao<Drug>{

    Optional<Drug> findDrugByDrugIdWhereCountMoreThanZeroAndCalculateCountFromUserOrderAndDrugDontDeleted(Integer userId, Integer drugId) throws DaoException;

    List<Drug> findAllDrugsWhereCountMoreThanZeroAndCalculateCountWithCountInOrderAndDrugIsNotDeletedWithLimitOffsetPagination(Integer userId, Integer limit, Integer offset) throws DaoException;

    int findCountAllDrugsWhereCountMoreThanZeroAndDrugIsNotDeleted() throws DaoException;

    List<Drug> findAllDrugsLimitOffsetPagination(Integer limit, Integer offset) throws DaoException;

    int findCountAllDrugCount() throws DaoException;

    List<Drug> findAllDrugsWhereCountMoreThanZeroAndDrugIsNotDeletedLimitOffsetPagination(Integer limit, Integer offset) throws DaoException;
}
