package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.dto.ProductUserOrderDto;
import by.ghoncharko.webproject.entity.*;
import by.ghoncharko.webproject.entity.Product;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.*;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static final Logger LOG = LogManager.getLogger(OrderServiceImpl.class);
    private final ConnectionPool connectionPool;



    @Override
    public boolean addDrugToOrder(User user, Integer drugId, Integer drugCount) throws ServiceException {

        try (Connection connection = connectionPool.getConnection(); Connection connection1 = connectionPool.getConnection(); Connection connection2 = connectionPool.getConnection(); Connection connection3 = connectionPool.getConnection()){
            final int userId = user.getId();
            final ProductDao productDao = new ProductDaoImpl(connection);
            final Optional<Product> drugFromDB = productDao.findDrugByDrugIdWhereCountMoreThanZeroAndCalculateCountFromUserOrderAndDrugDontDeleted(userId, drugId);
            if (drugFromDB.isPresent()) {
                final Product product = drugFromDB.get();
                final boolean countDrugToBuyIsValid = product.getCount() - drugCount >= 0;
                if (countDrugToBuyIsValid) {
                    if (product.getNeedRecipe()) {
                        final RecipeDao recipeDao = new RecipeDaoImpl(connection1);
                        final Optional<Recipe> recipeFromDB = recipeDao.findActiveRecipeByUserIdAndDrugId(userId, drugId);
                        if (recipeFromDB.isPresent()) {
                            UserOrderDao userOrderDao = new UserOrderDaoImpl(connection2);
                            DrugUserOrderDao drugUserOrderDao = new ProductUserOrderDaoImpl(connection3);
                            return createOrderForUser(user, drugId, drugCount, userOrderDao,drugUserOrderDao, userId, product);
                        }
                    } else {
                        UserOrderDao userOrderDao = new UserOrderDaoImpl(connection2);
                        DrugUserOrderDao drugUserOrderDao = new ProductUserOrderDaoImpl(connection3);
                        return createOrderForUser(user, drugId, drugCount, userOrderDao,drugUserOrderDao, userId, product);
                    }
                }
            }
        } catch (Exception e) {
            throw new ServiceException();
        }
        return false;
    }

    private boolean createOrderForUser(User user, Integer drugId, Integer drugCount,UserOrderDao userOrderDao, DrugUserOrderDao drugUserOrderDao, int userId, Product product) throws DaoException {
        final Optional<UserOrder> userOrderFromDB = userOrderDao.findNotPayedUserOrderByUserId(userId);
        if (userOrderFromDB.isPresent()) {
            final UserOrder userOrder = userOrderFromDB.get();
            final int userOrderId = userOrder.getId();
            final Optional<DrugUserOrder> drugUserOrderFromDB = drugUserOrderDao.findDrugUserOrderByUserOrderIdAndDrugId(userOrderId, drugId);
            if (drugUserOrderFromDB.isPresent()) {
                final DrugUserOrder drugUserOrder = drugUserOrderFromDB.get();
                final int drugUserOrderId = drugUserOrder.getId();
                final int updatedDrugCount = drugUserOrder.getDrugCount() + drugCount;
                final double updatedFinalPrice = drugUserOrder.getProduct().getPrice().multiply(BigDecimal.valueOf(updatedDrugCount)).doubleValue();
                return drugUserOrderDao.updateDrugCountAndFinalPriceByDrugUserOrderId(updatedDrugCount, drugUserOrderId, updatedFinalPrice);
            }
        } else {
            final BigDecimal finalPrice = product.getPrice().multiply(BigDecimal.valueOf(drugCount));
            final UserOrder createdNewUserOrder = userOrderDao.create(new UserOrder.Builder().withUser(user).build());
            drugUserOrderDao.create(new DrugUserOrder.Builder().
                    withDrug(product).
                    withUserOrder(createdNewUserOrder).
                    withDrugCount(drugCount).
                    withFinalPrice(finalPrice).build());
            return true;
        }
        return false;
    }

    @Override
    public List<DrugUserOrder> findAll() throws ServiceException {
        return null;
    }

    @Override
    public Optional<ProductUserOrderDto> findNotPaidOrderByUserId(Integer userId) {
        try (final Connection connection = connectionPool.getConnection(); Connection connection1 = connectionPool.getConnection(); Connection connection2 = connectionPool.getConnection()) {
            final UserOrderDao userOrderDao = new UserOrderDaoImpl(connection);
            final Optional<UserOrder> userOrderFromDB = userOrderDao.findNotPayedUserOrderByUserId(userId);
            if (userOrderFromDB.isPresent()) {
                final UserOrder userOrder = userOrderFromDB.get();
                final int userOrderId = userOrder.getId();
                final DrugUserOrderDao drugUserOrderDao = new ProductUserOrderDaoImpl(connection1);
                final List<DrugUserOrder> drugUserOrderList = drugUserOrderDao.findDrugUserOrdersByUserOrderId(userOrderId);
                final BankCardDao bankCardDao = new BankCardDaoImpl(connection2);
                final List<BankCard> bankCardList = bankCardDao.findUserBankCardsByUserId(userId);
                final BigDecimal finalPrice = calculateFinalPrice(drugUserOrderList);

                return Optional.of(new ProductUserOrderDto.Builder().
                        withBankCardList(bankCardList).
                        withDrugUserOrderList(drugUserOrderList).
                        withFinalPrice(finalPrice).
                        build());
            }
        } catch (Exception e) {

        }
        return Optional.empty();
    }

    private BigDecimal calculateFinalPrice(List<DrugUserOrder> drugUserOrders) {
        BigDecimal finalPrice = BigDecimal.ZERO;
        for (DrugUserOrder drugUserOrder : drugUserOrders) {
            finalPrice = finalPrice.add(drugUserOrder.getFinalPrice());
        }
        return finalPrice;
    }

    @Override
    public boolean deleteSomeCountDrugFromOrder(User user, Integer drugId, Integer drugCount, Integer drugUserOrderId) throws ServiceException {

        try (Connection connection = connectionPool.getConnection()){
            final DrugUserOrderDao drugUserOrderDao = new ProductUserOrderDaoImpl(connection);
            final Optional<DrugUserOrder> drugUserOrderFromDB = drugUserOrderDao.findEntityById(drugUserOrderId);
            if (drugUserOrderFromDB.isPresent()) {
                final DrugUserOrder drugUserOrder = drugUserOrderFromDB.get();
                if (drugUserOrder.getUserOrder().getUser().equals(user) && drugUserOrder.getProduct().getId() == drugId && drugUserOrder.getDrugCount() - drugCount >= 0) {
                    final double updatedFinalPrice = drugUserOrder.getProduct().getPrice().multiply(BigDecimal.valueOf(drugUserOrder.getDrugCount() - drugCount)).doubleValue();
                    return drugUserOrderDao.updateDrugCountAndFinalPriceByDrugUserOrderId(drugCount, drugUserOrderId, updatedFinalPrice);
                }
            }
        } catch (Exception e) {
            throw new ServiceException();
        }
        return false;
    }

    @Override
    public boolean payOrder(User user, Integer cardId) {

        try(Connection connection = connectionPool.getConnection(); Connection connection1 = connectionPool.getConnection(); Connection connection2 = connectionPool.getConnection()) {
            final int userId = user.getId();
            final UserOrderDao userOrderDao = new UserOrderDaoImpl(connection);
            final Optional<UserOrder> userOrderFromDB = userOrderDao.findNotPayedUserOrderByUserId(userId);
            if (userOrderFromDB.isPresent()) {
                final UserOrder userOrder = userOrderFromDB.get();
                final DrugUserOrderDao drugUserOrderDao = new ProductUserOrderDaoImpl(connection1);
                final int userOrderId = userOrder.getId();
                final List<DrugUserOrder> drugUserOrderList = drugUserOrderDao.findDrugUserOrdersByUserOrderId(userOrderId);
                BigDecimal finalPrice = BigDecimal.ZERO;
                for (DrugUserOrder drugUserOrder : drugUserOrderList) {
                    finalPrice = finalPrice.add(drugUserOrder.getFinalPrice());
                    final boolean drugUserOrderDrugNeedRecipe = drugUserOrder.getProduct().getNeedRecipe();
                    final int drugId = drugUserOrder.getProduct().getId();
                    if (drugUserOrderDrugNeedRecipe) {
                        final RecipeDao recipeDao = new RecipeDaoImpl(connection2);
                        final Optional<Recipe> activeRecipeInDB = recipeDao.findActiveRecipeByUserIdAndDrugId(userId, drugId);
                        if (activeRecipeInDB.isPresent()) {
                            return checkDataAndPay(user, cardId, connectionPool, userOrderId, finalPrice, drugUserOrder, drugId);
                        }
                    } else {
                        return checkDataAndPay(user, cardId, connectionPool, userOrderId, finalPrice, drugUserOrder, drugId);
                    }
                }
            }
        } catch (Exception e) {

        }
        return false;
    }

    private boolean checkDataAndPay(User user, Integer cardId, ConnectionPool connectionPool, int userOrderId, BigDecimal finalPrice, DrugUserOrder drugUserOrder, int drugId) throws DaoException, SQLException {
        try(Connection connection = connectionPool.getConnection(); Connection connection1 = connectionPool.getConnection(); Connection connection2 = connectionPool.getConnection()){
            final ProductDao productDao = new ProductDaoImpl(connection);
            final Optional<Product> drugFromDB = productDao.findEntityById(drugId);
            if (drugFromDB.isPresent()) {
                final Product product = drugFromDB.get();
                final int newDrugCount = product.getCount() - drugUserOrder.getDrugCount();
                if (newDrugCount >= 0) {
                    productDao.update(new Product.Builder().
                            withId(product.getId()).
                            withName(product.getName()).
                            withPrice(product.getPrice()).
                            withCount(newDrugCount).
                            withDescription(product.getDescription()).
                            withProducer(product.getProducer()).
                            withNeedReceip(product.getNeedRecipe()).
                            withIsDeleted(product.getDeleted()).
                            build());
                    final BankCardDao bankCardDao = new BankCardDaoImpl(connection1);
                    final Optional<BankCard> bankCardFromDB = bankCardDao.findEntityById(cardId);
                    if (bankCardFromDB.isPresent()) {
                        final BankCard bankCard = bankCardFromDB.get();
                        if (bankCard.getUser().equals(user)) {
                            final BigDecimal cardBalanceAfterPay = bankCard.getBalance().subtract(finalPrice);
                            if (cardBalanceAfterPay.doubleValue() >= 0) {
                                bankCardDao.update(new BankCard.Builder().
                                        withId(bankCard.getId()).
                                        withUser(bankCard.getUser()).
                                        withBalance(cardBalanceAfterPay).
                                        build());
                                final PaidUserOrderDao paidUserOrderDao = new PaidUserOrderDaoImpl(connection2);
                                return paidUserOrderDao.createPaidUserOrderWithCurrentDateByUserOrderId(userOrderId);
                            }
                        }
                    }
                }

            }
        }
        return false;
    }

    @Override
    public boolean deleteNotPayedOrderByOrderIdAndUserId(User user, Integer OrderId) throws ServiceException {


        try(Connection connection = connectionPool.getConnection(); Connection connection1 = connectionPool.getConnection()) {
            final int userId = user.getId();
            final UserOrderDao userOrderDao = new UserOrderDaoImpl(connection);
            final Optional<UserOrder> userOrderFromDB = userOrderDao.findNotPayedUserOrderByUserId(userId);
            if (userOrderFromDB.isPresent()) {
                final UserOrder userOrder = userOrderFromDB.get();
                final int userOrderId = userOrder.getId();
                final DrugUserOrderDao drugUserOrderDao = new ProductUserOrderDaoImpl(connection1);
                return drugUserOrderDao.deleteDrugUserOrderByUserOrderId(userOrderId) && userOrderDao.delete(OrderId);
            }
        } catch (Exception e) {
            LOG.error("Cannot delete Order", e);
            throw new ServiceException("Cannot delete Order", e);
        }
        LOG.info("Cannot delete Order");
        return false;
    }
}
