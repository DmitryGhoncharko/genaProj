package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.dto.ProductUserOrderDto;
import by.ghoncharko.webproject.entity.DrugUserOrder;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;

import java.util.Optional;

public interface OrderService extends Service<DrugUserOrder> {

    boolean addDrugToOrder(User user, Integer drugId, Integer drugCount) throws ServiceException;

    boolean deleteSomeCountDrugFromOrder(User user, Integer drugId, Integer count, Integer drugUserOrderId) throws ServiceException;

    boolean deleteNotPayedOrderByOrderIdAndUserId(User user, Integer orderId) throws ServiceException;

    Optional<ProductUserOrderDto> findNotPaidOrderByUserId(Integer userId);

    boolean payOrder(User user, Integer cardId);
}

