package by.ghoncharko.webproject.model.service;



import by.ghoncharko.webproject.entity.Order;

import java.util.List;

public interface OrderService extends Service<Order> {
   boolean createOrderWithStatusActive(Integer userId, Integer drugId, Integer count, Double price, boolean isNeedRecipe);

   List<Order> findAllWithStatusActive(Integer userId);

   boolean pay(Integer userId, Integer drugId, boolean isNeedRecipe, Integer count, Double finalPrice, Integer orderId);

   boolean deleteFromOrderByOrderId(Integer orderId);
}
