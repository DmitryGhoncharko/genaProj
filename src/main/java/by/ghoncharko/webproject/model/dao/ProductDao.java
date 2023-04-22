package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.Product;
import by.ghoncharko.webproject.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface ProductDao extends Dao<Product> {

    Optional<Product> findDrugByDrugIdWhereCountMoreThanZeroAndCalculateCountFromUserOrderAndDrugDontDeleted(Integer userId, Integer drugId) throws DaoException;

    List<Product> findAllDrugsWhereCountMoreThanZeroAndCalculateCountWithCountInOrderAndDrugIsNotDeletedWithLimitOffsetPagination(Integer userId, Integer limit, Integer offset) throws DaoException;

    int findCountAllDrugsWhereCountMoreThanZeroAndDrugIsNotDeleted() throws DaoException;

    List<Product> findAllDrugsLimitOffsetPagination(Integer limit, Integer offset) throws DaoException;

    int findCountAllDrugCount() throws DaoException;

    List<Product> findAllDrugsWhereCountMoreThanZeroAndDrugIsNotDeletedLimitOffsetPagination(Integer limit, Integer offset) throws DaoException;
}
