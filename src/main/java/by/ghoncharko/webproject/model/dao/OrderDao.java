package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.UserOrder;
import by.ghoncharko.webproject.entity.OrderStatus;
import by.ghoncharko.webproject.exception.DaoException;

import java.util.List;
import java.util.Optional;

/**
 * Interface for OrderDao with specific methods
 *
 * @author Dmitry Ghoncharko
 * @see OrderDaoImpl
 */
public interface OrderDao extends Dao<UserOrder> {
    /**
     * @param userId     user id
     * @param drugId     drug id
     * @param count      count of drugs
     * @param status     status for order (ACTIVE, INACTIVE)
     * @param finalPrice final price for order
     * @return true if order created and false if cannot create order
     * @throws DaoException when cannot create order
     */
    boolean create(Integer userId, Integer drugId, Integer count, OrderStatus status, Double finalPrice) throws DaoException;

    /**
     * @param userId user id
     * @return List<Order>
     * @throws DaoException when cannot find orders by user id
     * @see UserOrder
     */
    List<UserOrder> findAllByUserId(Integer userId) throws DaoException;

    /**
     * @param userId user id
     * @param drugId drug id
     * @return Optional<Order>
     * @throws DaoException when cannot find entity by user id and drug id with status ACTIVE
     * @see UserOrder
     */
    Optional<UserOrder> findEntityByUserIdAndDrugIdWithStatusActive(Integer userId, Integer drugId) throws DaoException;

    /**
     * @param userId  user id
     * @param drugId  drug id
     * @param status  status(ACTIVE, INACTIVE)
     * @param orderId order id
     * @return true if order updated and false if cannot update order
     * @throws DaoException when cannot update order
     */
    boolean update(Integer userId, Integer drugId, OrderStatus status, Integer orderId) throws DaoException;

    /**
     * @param entity     order entity
     * @param count      count drugs
     * @param finalPrice final price
     * @return true if order updated and false if cannot update order
     * @throws DaoException when cannot update order
     */
    boolean update(UserOrder entity, Integer count, Double finalPrice) throws DaoException;

    /**
     * @param orderId order id
     * @return true if order delete and false if cannot delete order
     * @throws DaoException when cannot delete order
     */
    boolean deleteByOrderId(Integer orderId) throws DaoException;
}
