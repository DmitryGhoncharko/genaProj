package by.ghoncharko.webproject.model.service;


import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.Order;
import by.ghoncharko.webproject.entity.OrderStatusHolder;
import by.ghoncharko.webproject.entity.Recipe;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.BankCardDao;
import by.ghoncharko.webproject.model.dao.BankCardDaoImpl;
import by.ghoncharko.webproject.model.dao.DrugDao;
import by.ghoncharko.webproject.model.dao.DrugDaoImpl;
import by.ghoncharko.webproject.model.dao.OrderDao;
import by.ghoncharko.webproject.model.dao.OrderDaoImpl;
import by.ghoncharko.webproject.model.dao.RecipeDao;
import by.ghoncharko.webproject.model.dao.RecipeDaoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
    private static final Logger LOG = LogManager.getLogger(OrderServiceImpl.class);
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private OrderServiceImpl() {
    }

    @Override
    public boolean deleteFromOrderByOrderId(Integer orderId) {
        final Connection connection = connectionPool.getConnection();
        final OrderDao orderDao = new OrderDaoImpl(connection);
        try {
            return orderDao.deleteByOrderId(orderId);
        } catch (DaoException e) {
            LOG.error("Dao exception", e);
        } finally {
            Service.connectionClose(connection);
        }
        return false;
    }

    @Override
    public boolean pay(Integer userId, Integer drugId, boolean isNeedRecipe, Integer count, Double finalPrice, Integer orderId, Integer cardId) {
        Connection connection = connectionPool.getConnection();
        Service.autoCommitFalse(connection);
        try {
            if (isNeedRecipe) {
                RecipeDao recipeDao = new RecipeDaoImpl(connection);
                Optional<Recipe> recipe = recipeDao.findEntityByUserIdAndDrugId(userId, drugId);
                if (recipe.isPresent()) {
                    return validateAndPay(userId, drugId, count, finalPrice, connection, orderId, cardId);
                }
            } else {
                return validateAndPay(userId, drugId, count, finalPrice, connection, orderId, cardId);
            }
            Service.rollbackConnection(connection);
            return false;
        } catch (DaoException e) {
            Service.rollbackConnection(connection);
            return false;
        } finally {
            Service.connectionClose(connection);
        }

    }

    private boolean validateAndPay(Integer userId, Integer drugId, Integer count, Double finalPrice, Connection connection, Integer orderId, Integer cardId) throws DaoException {
        BankCardDao bankCardDao = new BankCardDaoImpl(connection);
        Optional<BankCard> bankCard = bankCardDao.findAllBankCardsByCardId(cardId);
        if (bankCard.isPresent()) {
            Double balance = bankCard.get().getBalance();
            double balanceAfterPay = BigDecimal.valueOf(balance).subtract(BigDecimal.valueOf(finalPrice)).doubleValue();
            if (balanceAfterPay >= 0) {
                bankCardDao.update(new BankCard.Builder().
                        withId(bankCard.get().getId()).
                        withUserId(bankCard.get().getUserId()).
                        withBalance(balanceAfterPay).build());
                OrderDao orderDao = new OrderDaoImpl(connection);
                boolean isUpdated = orderDao.update(userId, drugId, OrderStatusHolder.INACTIVE, orderId);
                if (isUpdated) {
                    DrugDao drugDao = new DrugDaoImpl(connection);
                    Optional<Drug> drug = drugDao.findEntityById(drugId);
                    if (drug.isPresent()) {
                        int updatedCount = drug.get().getCount() - count;
                        if (updatedCount >= 0) {
                            return drugDao.update(updatedCount, drugId);
                        }
                        return false;
                    }

                }
            }
        }
        return false;
    }

    @Override
    public List<Order> findAll() {
        final Connection connection = connectionPool.getConnection();
        final OrderDao orderDao = new OrderDaoImpl(connection);
        try {
            return orderDao.findAll();
        } catch (DaoException e) {
            LOG.error("DaoException", e);
        } finally {
            Service.connectionClose(connection);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Order> findAllWithStatusActive(Integer userId) {
        final Connection connection = connectionPool.getConnection();
        final OrderDao orderDao = new OrderDaoImpl(connection);
        try {
            return orderDao.findAllByUserId(userId);
        } catch (DaoException e) {
            LOG.error("DaoException", e);
            return Collections.emptyList();
        } finally {
            Service.connectionClose(connection);
        }
    }

    @Override
    public boolean createOrderWithStatusActive(Integer userId, Integer drugId, Integer count, Double price, boolean isNeedRecipe) {
        final Connection connection = connectionPool.getConnection();
        Service.autoCommitFalse(connection);
        final OrderDao orderDao = new OrderDaoImpl(connection);
        final RecipeDao recipeDao = new RecipeDaoImpl(connection);

        final Double finalPrice = count * price;
        try {
            if (isNeedRecipe) {
                Optional<Recipe> recipe = recipeDao.findEntityByUserIdAndDrugId(userId, drugId);
                if (recipe.isPresent() && recipe.get().getDateEnd().after(new Date(new java.util.Date().getDate()))) {
                    Optional<Order> order = orderDao.findEntityByUserIdAndDrugIdWithStatusActive(userId, drugId);
                    if (order.isPresent()) {
                        if (orderDao.update(order.get(), count, finalPrice)) {
                            return true;
                        } else {
                            Service.rollbackConnection(connection);
                            return false;
                        }
                    }
                    if (orderDao.create(userId, drugId, count, OrderStatusHolder.ACTIVE, finalPrice)) {
                        return true;
                    }
                }
                Service.rollbackConnection(connection);
                return false;
            }
            Optional<Order> order = orderDao.findEntityByUserIdAndDrugIdWithStatusActive(userId, drugId);
            if (order.isPresent()) {
                if (orderDao.update(order.get(), count, finalPrice)) {
                    return true;
                }
                Service.rollbackConnection(connection);
                return false;
            }
            boolean isCreated = orderDao.create(userId, drugId, count, OrderStatusHolder.ACTIVE, finalPrice);
            if (isCreated) {
                return true;
            }
        } catch (DaoException e) {
            Service.rollbackConnection(connection);
            LOG.error("DaoException in service method createOrderWithStatusActive", e);
            return false;
        } finally {
            Service.connectionClose(connection);
        }
        Service.rollbackConnection(connection);
        return false;
    }

    static OrderServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final OrderServiceImpl INSTANCE = new OrderServiceImpl();
    }

}
