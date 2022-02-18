package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.DrugUserOrder;
import by.ghoncharko.webproject.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface DrugUserOrderDao extends Dao<DrugUserOrder> {

    boolean updateDrugCountAndFinalPriceByDrugUserOrderId(Integer drugCount, Integer drugUserOrderId, Double finalPrice) throws DaoException;

    boolean deleteDrugUserOrderByUserOrderId(Integer userOrderId) throws DaoException;

    @Override
    Optional<DrugUserOrder> findEntityById(Integer id) throws DaoException;

    Optional<DrugUserOrder> findDrugUserOrderByUserOrderIdAndDrugId(Integer userOrderId, Integer drugId) throws DaoException;

    List<DrugUserOrder> findDrugUserOrdersByUserOrderId(Integer userOrderId) throws DaoException;

}
