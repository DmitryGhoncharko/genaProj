package by.ghoncharko.webproject.model.service;


import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.Order;
import by.ghoncharko.webproject.entity.OrderStatusHolder;
import by.ghoncharko.webproject.entity.Recipe;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.exception.ServiceException;
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
    public boolean deleteFromOrderByOrderIdUserIdAndDrugId(Integer orderId, Integer userId, Integer drugId) throws ServiceException {
        if (orderId == null || userId == null || drugId == null) {
            return false;
        }
        final Connection connection = connectionPool.getConnection();
        final OrderDao orderDao = new OrderDaoImpl(connection);
        try {
            final Optional<Order> orderFromDB = orderDao.findEntityByUserIdAndDrugIdWithStatusActive(userId, drugId);
            if (orderFromDB.isPresent()) {
                return orderDao.deleteByOrderId(orderId);
            }
        } catch (DaoException e) {
            LOG.error("Cannot delete from order by order id", e);
            throw new ServiceException("Cannot delete from order by order id", e);
        } finally {
            Service.connectionClose(connection);
        }
        return false;
    }

    @Override
    public boolean pay(Integer userId, Integer drugId, Integer count, Integer orderId, Integer cardId) throws ServiceException {
        if (userId == null || drugId == null || count == null || orderId == null || cardId == null) {
            return false;
        }
        final Connection connection = connectionPool.getConnection();
        Service.autoCommitFalse(connection);
        try {
            final OrderDao orderDao = new OrderDaoImpl(connection);
            final Optional<Order> orderFromDB = orderDao.findEntityByUserIdAndDrugIdWithStatusActive(userId, drugId);
            if (orderFromDB.isPresent()) {
                final int drugCountOnDB = orderFromDB.get().getDrug().getCount();
                final double finalPrice = orderFromDB.get().getFinalPrice();
                final boolean isNeedRecipe = orderFromDB.get().getDrug().isNeedRecipe();
                if (isNeedRecipe) {
                    final RecipeDao recipeDao = new RecipeDaoImpl(connection);
                    final Optional<Recipe> recipe = recipeDao.findEntityByUserIdAndDrugId(userId, drugId);
                    if (recipe.isPresent()) {
                        if (drugCountOnDB >= count)
                            return validateAndPay(userId, drugId, count, finalPrice, connection, orderId, cardId);
                    }
                } else {
                    if (drugCountOnDB >= count)
                        return validateAndPay(userId, drugId, count, finalPrice, connection, orderId, cardId);
                }

            }
            Service.rollbackConnection(connection);
            return false;
        } catch (DaoException e) {
            LOG.error("Cannot pay order", e);
            Service.rollbackConnection(connection);
            throw new ServiceException("Cannot pay order", e);
        } finally {
            Service.connectionClose(connection);
        }
    }

    private boolean validateAndPay(Integer userId, Integer drugId, Integer count, Double finalPrice, Connection connection, Integer orderId, Integer cardId) throws DaoException {
        final BankCardDao bankCardDao = new BankCardDaoImpl(connection);
        final Optional<BankCard> bankCard = bankCardDao.findAllBankCardsByUserIdAndCardId(userId, cardId);
        if (bankCard.isPresent()) {
            final double balance = bankCard.get().getBalance();
            final double balanceAfterPay = BigDecimal.valueOf(balance).subtract(BigDecimal.valueOf(finalPrice)).doubleValue();
            if (balanceAfterPay >= 0) {
                bankCardDao.update(new BankCard.Builder().
                        withId(bankCard.get().getId()).
                        withUserId(bankCard.get().getUserId()).
                        withBalance(balanceAfterPay).build());
                final OrderDao orderDao = new OrderDaoImpl(connection);
                final boolean isUpdated = orderDao.update(userId, drugId, OrderStatusHolder.INACTIVE, orderId);
                if (isUpdated) {
                    final DrugDao drugDao = new DrugDaoImpl(connection);
                    final Optional<Drug> drug = drugDao.findEntityById(drugId);
                    if (drug.isPresent()) {
                        final int updatedCount = drug.get().getCount() - count;
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
    public List<Order> findAll() throws ServiceException {
        final Connection connection = connectionPool.getConnection();
        final OrderDao orderDao = new OrderDaoImpl(connection);
        try {
            return orderDao.findAll();
        } catch (DaoException e) {
            LOG.error("Cannot find all orders", e);
            throw new ServiceException("Cannot find all orders", e);
        } finally {
            Service.connectionClose(connection);
        }
    }

    @Override
    public List<Order> findAllWithStatusActiveByUserId(Integer userId) throws ServiceException {
        if (userId == null) {
            return Collections.emptyList();
        }
        final Connection connection = connectionPool.getConnection();
        final OrderDao orderDao = new OrderDaoImpl(connection);
        try {
            return orderDao.findAllByUserId(userId);
        } catch (DaoException e) {
            LOG.error("Cannot find all orders with status active by user id", e);
            throw new ServiceException("Cannot find all orders with status active by user id", e);
        } finally {
            Service.connectionClose(connection);
        }
    }

    @Override
    public boolean createOrderWithStatusActive(Integer userId, Integer drugId, Integer count) throws ServiceException {
        if (userId == null || drugId == null || count == null) {
            return false;
        }
        final Connection connection = connectionPool.getConnection();
        Service.autoCommitFalse(connection);
        final OrderDao orderDao = new OrderDaoImpl(connection);
        final RecipeDao recipeDao = new RecipeDaoImpl(connection);
        final DrugDao drugDao = new DrugDaoImpl(connection);
        try {
            final Optional<Drug> drugFromDB = drugDao.findEntityById(drugId);
            if (drugFromDB.isPresent()) {
                final boolean isNeedRecipe = drugFromDB.get().isNeedRecipe();
                final double price = drugFromDB.get().getPrice();
                final int drugCount = drugFromDB.get().getCount();
                final BigDecimal countAsBigDecimal = new BigDecimal(count);
                final BigDecimal priceAsBigDecimal = new BigDecimal(price);
                final double finalPrice = countAsBigDecimal.multiply(priceAsBigDecimal).doubleValue();
                if (isNeedRecipe && drugCount >= count) {
                    final Optional<Recipe> recipe = recipeDao.findEntityByUserIdAndDrugId(userId, drugId);
                    if (recipe.isPresent() && recipe.get().getDateEnd().after(new Date(new java.util.Date().getDate()))) {
                        final Optional<Order> order = orderDao.findEntityByUserIdAndDrugIdWithStatusActive(userId, drugId);
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

                final Optional<Order> order = orderDao.findEntityByUserIdAndDrugIdWithStatusActive(userId, drugId);
                if (order.isPresent() && drugCount >= count) {
                    if (orderDao.update(order.get(), count, finalPrice)) {
                        return true;
                    }
                    Service.rollbackConnection(connection);
                    return false;
                }
                if (drugCount >= count) {
                    final boolean isCreated = orderDao.create(userId, drugId, count, OrderStatusHolder.ACTIVE, finalPrice);
                    if (isCreated) {
                        return true;
                    }
                }

            }
            Service.rollbackConnection(connection);
            return false;
        } catch (DaoException e) {
            LOG.error("DaoException in service method createOrderWithStatusActive", e);
            Service.rollbackConnection(connection);
            throw new ServiceException("Cannot createOrderWithStatusActive", e);
        } finally {
            Service.connectionClose(connection);
        }
    }

    static OrderServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final OrderServiceImpl INSTANCE = new OrderServiceImpl();
    }

}
