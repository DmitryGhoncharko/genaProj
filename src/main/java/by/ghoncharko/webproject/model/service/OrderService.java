package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.DrugUserOrder;
import by.ghoncharko.webproject.entity.User;

public interface OrderService extends Service<DrugUserOrder> {

    boolean addToOrder(User user, Integer drugId, Integer drugCount);
}
