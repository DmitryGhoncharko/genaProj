package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.PaidUserOrder;
import by.ghoncharko.webproject.exception.DaoException;

public interface PaidUserOrderDao extends Dao<PaidUserOrder> {

    boolean createPaidUserOrderWithCurrentDateByUserOrderId(Integer userOrderId) throws DaoException;
}
