package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.DrugUserOrder;
import by.ghoncharko.webproject.entity.Recipe;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.entity.UserOrder;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.DrugDao;
import by.ghoncharko.webproject.model.dao.DrugDaoImpl;
import by.ghoncharko.webproject.model.dao.DrugUserOrderDao;
import by.ghoncharko.webproject.model.dao.DrugUserOrderDaoImpl;
import by.ghoncharko.webproject.model.dao.RecipeDao;
import by.ghoncharko.webproject.model.dao.RecipeDaoImpl;
import by.ghoncharko.webproject.model.dao.UserOrderDao;
import by.ghoncharko.webproject.model.dao.UserOrderDaoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService{
    private static final Logger LOG = LogManager.getLogger(OrderServiceImpl.class);
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private OrderServiceImpl(){

    }

    @Override
    public boolean addDrugToOrder(User user, Integer drugId, Integer drugCount) throws ServiceException {
        final Connection connection = connectionPool.getConnection();
        final int userId = user.getId();
        Service.autoCommitFalse(connection);
        final DrugDao drugDao = new DrugDaoImpl(connection);

        try{
           final Optional<Drug> drugFromDB = drugDao.findDrugByDrugIdWhereCountMoreThanZeroAndDrugDontDeleted(drugId);
            if(drugFromDB.isPresent()){
                final Drug drug = drugFromDB.get();
               //если валидное кол во товаров
                //
                    final  BigDecimal drugFinalPrice = drug.getPrice().multiply(BigDecimal.valueOf(drugCount));
                   //если нужен рецепт
                    if(drug.getNeedRecipe()){
                    //todo calculate drug count on air
                    final RecipeDao recipeDao = new RecipeDaoImpl(connection);
                    final Optional<Recipe> recipe = recipeDao.findActiveRecipeByUserIdAndDrugId(userId,drugId);
                    //если есть рецепт
                    if(recipe.isPresent()){
                        final UserOrderDao userOrderDao = new UserOrderDaoImpl(connection);
                        final Optional<UserOrder> notPaidUserOrderFromDB = userOrderDao.findNotPayedUserOrderByUserId(userId);
                        if(notPaidUserOrderFromDB.isPresent()){
                            final UserOrder userOrder = notPaidUserOrderFromDB.get();
                            //todo взять айди юзер ордер и по нему найти драг который покупаем по драй айди июзер ордер айди
                            final DrugUserOrderDao drugUserOrderDao = new DrugUserOrderDaoImpl(connection);
                            final int userOrderId = userOrder.getId();
                            final Optional<DrugUserOrder> drugUserOrderFromDB = drugUserOrderDao.findDrugUserOrderByUserOrderIdAndDrugId(userOrderId, drugId);
                            if(drugFromDB.isPresent()){
                                final DrugUserOrder drugUserOrder = drugUserOrderFromDB.get();
                                final int drugUserOrderId = drugUserOrder.getId();
                                final boolean isValidCountDrugForAddToOrder = drug.getCount() - drugUserOrder.getDrugCount() - drugCount >=0;
                                final int drugCountInOrderAfterAdd = drugCount + drugUserOrder.getDrugCount();
                                if(isValidCountDrugForAddToOrder){
                                    drugUserOrderDao.updateDrugCountAndFinalPriceByDrugUserOrderId(drugCountInOrderAfterAdd,drugUserOrderId);
                                }
                            }
                            final DrugUserOrder drugUserOrder = new DrugUserOrder.Builder().
                                    withUserOrder(userOrder).
                                    withDrug(drug).
                                    withDrugCount(drugCount).
                                    withFinalPrice(drugFinalPrice).
                                    build();
                            drugUserOrderDao.create(drugUserOrder);
                            return true;
                            //todo ПРОВЕРИТЬ НЕТ ЛИ ТАКОГО ЖЕ ТОВАРА В КОРЗИНЕ, ЕСЛИ ЕСТЬ ТО НУЖНО ОБНОВИТЬ КОЛ-ВО ТОВАРА В КОРЗИНЕ
                        }
                        final UserOrder userOrder = new UserOrder.Builder().
                                withUser(user).build();
                        final  UserOrder createdUserOrder =  userOrderDao.create(userOrder);
                        final DrugUserOrderDao drugUserOrderDao = new DrugUserOrderDaoImpl(connection);
                        final DrugUserOrder drugUserOrder = new DrugUserOrder.Builder().
                                withUserOrder(createdUserOrder).
                                withDrug(drug).
                                withDrugCount(drugCount).
                                withFinalPrice(drugFinalPrice).
                                build();
                        drugUserOrderDao.create(drugUserOrder);
                        return true;
                    }else {
                        return false;
                    }
                }else {
                       final UserOrderDao userOrderDao = new UserOrderDaoImpl(connection);
                       final Optional<UserOrder> notPaidUserOrderFromDB = userOrderDao.findNotPayedUserOrderByUserId(userId);
                       if(notPaidUserOrderFromDB.isPresent()){
                           final UserOrder userOrder = notPaidUserOrderFromDB.get();
                           final DrugUserOrderDao drugUserOrderDao = new DrugUserOrderDaoImpl(connection);
                           final DrugUserOrder drugUserOrder = new DrugUserOrder.Builder().
                                   withUserOrder(userOrder).
                                   withDrug(drug).
                                   withDrugCount(drugCount).
                                   withFinalPrice(drugFinalPrice).
                                   build();
                           drugUserOrderDao.create(drugUserOrder);
                           return true;
                       }
                       final UserOrder userOrder = new UserOrder.Builder().
                               withUser(user).build();
                       final  UserOrder createdUserOrder =  userOrderDao.create(userOrder);
                       final DrugUserOrderDao drugUserOrderDao = new DrugUserOrderDaoImpl(connection);
                       final DrugUserOrder drugUserOrder = new DrugUserOrder.Builder().
                               withUserOrder(createdUserOrder).
                               withDrug(drug).
                               withDrugCount(drugCount).
                               withFinalPrice(drugFinalPrice).
                               build();
                       drugUserOrderDao.create(drugUserOrder);
                       return true;
                   }

            }
        }catch (DaoException e){
            Service.rollbackConnection(connection);
            LOG.error("Cannot create order",e);
            throw new ServiceException("Cannot create order",e);
        }finally {
            Service.connectionClose(connection);
        }
        LOG.info("Cannot create order");
        return false;
    }

    @Override
    public List<DrugUserOrder> findAll() throws ServiceException {
        return null;
    }

    @Override
    public boolean deleteSomeCountDrugFromOrder(User user, Integer drugId, Integer drugCount, Integer drugUserOrderId) throws ServiceException {
        //todo validation
        final Connection connection = connectionPool.getConnection();
        Service.autoCommitFalse(connection);
        final DrugUserOrderDao drugUserOrderDao = new DrugUserOrderDaoImpl(connection);
        try{
            final Optional<DrugUserOrder> drugUserOrderFromDB = drugUserOrderDao.findEntityById(drugUserOrderId);
            if(drugUserOrderFromDB.isPresent()){
                final DrugUserOrder drugUserOrder = drugUserOrderFromDB.get();
                if(drugUserOrder.getUserOrder().getUser().equals(user) && drugUserOrder.getDrug().getId()==drugId && drugUserOrder.getDrugCount()-drugCount>=0){

                    return   drugUserOrderDao.updateDrugCountAndFinalPriceByDrugUserOrderId(drugCount, drugUserOrderId);
                }
            }
        }catch (DaoException e){
            Service.rollbackConnection(connection);
            throw new ServiceException();
        }finally {
            Service.connectionClose(connection);
        }
        return false;
    }

    @Override
    public boolean deleteOrderByOrderIdAndUserId(User user, Integer orderId) throws ServiceException {
        final Connection connection = connectionPool.getConnection();
        Service.autoCommitFalse(connection);
        final int userId = user.getId();
        final UserOrderDao userOrderDao = new UserOrderDaoImpl(connection);
       try{
           final   Optional<UserOrder> userOrderFromDB =  userOrderDao.findPaidUserOrderByUserOrderIdAndUserId(orderId,userId);
           if(userOrderFromDB.isPresent()){
               Service.rollbackConnection(connection);
               return false;
           }
           final DrugUserOrderDao drugUserOrderDao = new DrugUserOrderDaoImpl(connection);
           final boolean drugUserOrderIsDeleted = drugUserOrderDao.deleteDrugUserOrderByUserOrderId(orderId);
           if(drugUserOrderIsDeleted){
             return userOrderDao.delete(orderId);
           }
       }catch (DaoException e){
            LOG.error("Cannot delete Order",e);
            throw new ServiceException("Cannot delete Order",e);
       }finally {
           Service.connectionClose(connection);
       }
       LOG.info("Cannot delete Order");
       return false;
    }
//перенести final price в paid user order из drug user order
    public static OrderServiceImpl getInstance() {
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final OrderServiceImpl INSTANCE = new OrderServiceImpl();
    }
}
