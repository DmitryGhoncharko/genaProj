package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.DrugUserOrder;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;

public interface OrderService extends Service<DrugUserOrder> {

    boolean addDrugToOrder(User user, Integer drugId, Integer drugCount) throws ServiceException;

    boolean deleteSomeCountDrugFromOrder(User user, Integer drugId, Integer count, Integer drugUserOrderId) throws ServiceException;

    boolean deleteOrderByOrderIdAndUserId(User user, Integer orderId) throws ServiceException;
}
