package by.ghoncharko.webproject.model.service;



import by.ghoncharko.webproject.entity.Order;
import by.ghoncharko.webproject.exception.ServiceException;

import java.util.List;

public interface OrderService extends Service<Order> {
   boolean createOrderWithStatusActive(Integer userId, Integer drugId, Integer count) throws ServiceException;

   List<Order> findAllWithStatusActiveByUserId(Integer userId) throws ServiceException;

   boolean pay(Integer userId, Integer drugId,  Integer count, Integer orderId, Integer cardId) throws ServiceException;

   boolean deleteFromOrderByOrderIdUserIdAndDrugId(Integer orderId, Integer userId, Integer drugId) throws ServiceException;

   static OrderService getInstance() {
      return OrderServiceImpl.getInstance();
   }
}
