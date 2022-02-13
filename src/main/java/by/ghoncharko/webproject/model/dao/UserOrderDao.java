package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.UserOrder;
import by.ghoncharko.webproject.exception.DaoException;

import java.util.Optional;

public interface UserOrderDao extends Dao<UserOrder> {

    Optional<UserOrder> findNotPayedUserOrderByUserId(Integer userId) throws DaoException;

    Optional<UserOrder> findPaidUserOrderByUserOrderIdAndUserId(Integer userOrderId, Integer userId) throws DaoException;
}
