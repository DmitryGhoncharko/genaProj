package by.ghoncharko.webproject.model.service;


import by.ghoncharko.webproject.entity.*;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.BankCardDaoImpl;
import by.ghoncharko.webproject.model.dao.DrugDaoImpl;
import by.ghoncharko.webproject.model.dao.OrderDaoImpl;
import by.ghoncharko.webproject.model.dao.RecipeDaoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
    private static final Logger LOG = LogManager.getLogger(OrderServiceImpl.class);
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Override
    public boolean pay(Integer userId, Integer drugId, boolean isNeedRecipe, Integer count, Double finalPrice, Integer orderId) {
        Connection connection = connectionPool.getConnection();
        Service.autoCommitFalse(connection);
        try {
            if (isNeedRecipe) {
                RecipeDaoImpl recipeDao = new RecipeDaoImpl(connection);
                Optional<Recipe> recipe = recipeDao.findEntityByUserIdAndDrugId(userId, drugId);
                if (recipe.isPresent()) {
                    return  validateAndPay(userId, drugId, count, finalPrice, connection, orderId);
                }
            }else {
                return validateAndPay(userId, drugId, count, finalPrice, connection, orderId);
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

    private boolean validateAndPay(Integer userId, Integer drugId, Integer count, Double finalPrice, Connection connection, Integer orderId) throws DaoException {
        BankCardDaoImpl bankCardDao = new BankCardDaoImpl(connection);
        Optional<BankCard> bankCard = bankCardDao.findBankCardByUserId(userId);
        if (bankCard.isPresent()) {
            Double balance = bankCard.get().getBalance();
            double balanceAfterPay = balance - finalPrice;
            if (balanceAfterPay >= 0) {
                bankCardDao.update(new BankCard.Builder().
                        withId(bankCard.get().getId()).
                        withUserId(bankCard.get().getUserId()).
                        withBalance(balanceAfterPay).build());
                OrderDaoImpl orderDao = new OrderDaoImpl(connection);
                boolean isUpdated = orderDao.update(userId, drugId, OrderStatusHolder.INACTIVE, orderId);
                if (isUpdated) {
                    DrugDaoImpl drugDao = new DrugDaoImpl(connection);
                    Optional<Drug> drug = drugDao.findEntityById(drugId);
                    if(drug.isPresent()){
                       int updatedCount = drug.get().getCount()-count;
                       if(updatedCount>=0){
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
    public List<Order> findAll() throws DaoException {
        OrderDaoImpl orderDao = new OrderDaoImpl(connectionPool.getConnection());
        return orderDao.findAll();
    }

    //with status active
    @Override
    public List<Order> findAllWithStatusActive(Integer userId) {
        Connection connection = connectionPool.getConnection();
        OrderDaoImpl orderDao = new OrderDaoImpl(connection);
        try {
            return orderDao.findAll(userId);
        } catch (DaoException e) {
            LOG.error("");
            return Collections.emptyList();
        } finally {
            Service.connectionClose(connection);
        }
    }

    @Override
    public boolean createOrderWithStatusActive(Integer userId, Integer drugId, Integer count, Double price, boolean isNeedRecipe) {
        Connection connection = connectionPool.getConnection();
        Service.autoCommitFalse(connection);
        OrderDaoImpl orderDao = new OrderDaoImpl(connection);
        RecipeDaoImpl recipeDao = new RecipeDaoImpl(connection);
        Double finalPrice = count * price;
        try {
            if (isNeedRecipe) {
                Optional<Recipe> recipe = recipeDao.findEntityByUserIdAndDrugId(userId, drugId);
                if (recipe.isPresent()) {
                    return orderDao.create(userId, drugId, count, OrderStatusHolder.ACTIVE, finalPrice);
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

    public static OrderServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final OrderServiceImpl INSTANCE = new OrderServiceImpl();
    }

}