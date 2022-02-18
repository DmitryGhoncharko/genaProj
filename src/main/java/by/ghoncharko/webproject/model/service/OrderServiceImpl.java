package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.dto.DrugUserOrderDto;
import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.DrugUserOrder;
import by.ghoncharko.webproject.entity.PaidUserOrder;
import by.ghoncharko.webproject.entity.Recipe;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.entity.UserOrder;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.dao.BankCardDao;
import by.ghoncharko.webproject.model.dao.DaoHelper;
import by.ghoncharko.webproject.model.dao.DaoHelperFactory;
import by.ghoncharko.webproject.model.dao.DrugDao;
import by.ghoncharko.webproject.model.dao.DrugUserOrderDao;
import by.ghoncharko.webproject.model.dao.PaidUserOrderDao;
import by.ghoncharko.webproject.model.dao.RecipeDao;
import by.ghoncharko.webproject.model.dao.UserOrderDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService{
    private static final Logger LOG = LogManager.getLogger(OrderServiceImpl.class);
    private final DaoHelperFactory daoHelperFactory;

    public OrderServiceImpl(DaoHelperFactory daoHelperFactory){
        this.daoHelperFactory = daoHelperFactory;
    }

    @Override
    public boolean addDrugToOrder(User user, Integer drugId, Integer drugCount) throws ServiceException {
        final DaoHelper daoHelper = daoHelperFactory.create();
        daoHelper.startTransaction();
        final int userId = user.getId();
        final DrugDao drugDao = daoHelper.createDrugDao();
        try{
          final Optional<Drug> drugFromDB =  drugDao.findDrugByDrugIdWhereCountMoreThanZeroAndCalculateCountFromUserOrderAndDrugDontDeleted(userId, drugId);
          if(drugFromDB.isPresent()){
              final Drug drug = drugFromDB.get();
              final boolean countDrugToBuyIsValid = drug.getCount() - drugCount >=0;
              if(countDrugToBuyIsValid){
                  if(drug.getNeedRecipe()){
                      final RecipeDao recipeDao = daoHelper.createRecipeDao();
                      final Optional<Recipe> recipeFromDB = recipeDao.findActiveRecipeByUserIdAndDrugId(userId,drugId);
                      if(recipeFromDB.isPresent()){
                          return createOrderForUser(user, drugId, drugCount, daoHelper, userId, drug);
                      }
                  }else {
                      return createOrderForUser(user,drugId,drugCount, daoHelper,userId,drug);
                  }
              }
          }
        }catch (DaoException e){
            daoHelper.rollbackTransactionAndCloseConnection();
            throw new ServiceException();
        }finally {
            daoHelper.commitTransactionAndCloseConnection();
        }
        return false;
    }

    private boolean createOrderForUser(User user, Integer drugId, Integer drugCount, DaoHelper daoHelper, int userId, Drug drug) throws DaoException {
        final UserOrderDao userOrderDao = daoHelper.createUserOrderDao();
        final DrugUserOrderDao drugUserOrderDao= daoHelper.createDrugUserOrderDao();
        final Optional<UserOrder> userOrderFromDB =  userOrderDao.findNotPayedUserOrderByUserId(userId);
        if(userOrderFromDB.isPresent()){
            final UserOrder userOrder = userOrderFromDB.get();
            final int userOrderId = userOrder.getId();
            final Optional<DrugUserOrder> drugUserOrderFromDB = drugUserOrderDao.findDrugUserOrderByUserOrderIdAndDrugId(userOrderId, drugId);
            if(drugUserOrderFromDB.isPresent()){
                final DrugUserOrder drugUserOrder = drugUserOrderFromDB.get();
                final int drugUserOrderId = drugUserOrder.getId();
                final int updatedDrugCount = drugUserOrder.getDrugCount() + drugCount;
                final double updatedFinalPrice = drugUserOrder.getDrug().getPrice().multiply(BigDecimal.valueOf(updatedDrugCount)).doubleValue();
               return drugUserOrderDao.updateDrugCountAndFinalPriceByDrugUserOrderId(updatedDrugCount,drugUserOrderId, updatedFinalPrice);
            }
        }else {
            final BigDecimal finalPrice = drug.getPrice().multiply(BigDecimal.valueOf(drugCount));
           final UserOrder createdNewUserOrder = userOrderDao.create(new UserOrder.Builder().withUser(user).build());
           drugUserOrderDao.create(new DrugUserOrder.Builder().
                   withDrug(drug).
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
    public Optional<DrugUserOrderDto> findNotPaidOrderByUserId(Integer userId) {
        try(final DaoHelper daoHelper = daoHelperFactory.create()){
            final UserOrderDao userOrderDao = daoHelper.createUserOrderDao();
            final Optional<UserOrder> userOrderFromDB = userOrderDao.findNotPayedUserOrderByUserId(userId);
            if(userOrderFromDB.isPresent()){
                final UserOrder userOrder = userOrderFromDB.get();
                final int userOrderId = userOrder.getId();
                final DrugUserOrderDao drugUserOrderDao = daoHelper.createDrugUserOrderDao();
                final List<DrugUserOrder> drugUserOrderList = drugUserOrderDao.findDrugUserOrdersByUserOrderId(userOrderId);
                final BankCardDao bankCardDao = daoHelper.createBankCardDao();
                final List<BankCard> bankCardList = bankCardDao.findUserBankCardsByUserId(userId);
                final BigDecimal finalPrice = calculateFinalPrice(drugUserOrderList);

                return Optional.of(new DrugUserOrderDto.Builder().
                        withBankCardList(bankCardList).
                        withDrugUserOrderList(drugUserOrderList).
                        withFinalPrice(finalPrice).
                        build());
            }
        }catch (Exception e){

        }
        return Optional.empty();
    }
    private BigDecimal calculateFinalPrice(List<DrugUserOrder> drugUserOrders){
        BigDecimal finalPrice = BigDecimal.ZERO;
        for (DrugUserOrder drugUserOrder : drugUserOrders){
            finalPrice = finalPrice.add(drugUserOrder.getFinalPrice());
        }
        return finalPrice;
    }
    @Override
    public boolean deleteSomeCountDrugFromOrder(User user, Integer drugId, Integer drugCount, Integer drugUserOrderId) throws ServiceException {
        //todo validation
        final DaoHelper daoHelper = daoHelperFactory.create();
        final DrugUserOrderDao drugUserOrderDao = daoHelper.createDrugUserOrderDao();
        try{
            final Optional<DrugUserOrder> drugUserOrderFromDB = drugUserOrderDao.findEntityById(drugUserOrderId);
            if(drugUserOrderFromDB.isPresent()){
                final DrugUserOrder drugUserOrder = drugUserOrderFromDB.get();
                if(drugUserOrder.getUserOrder().getUser().equals(user) && drugUserOrder.getDrug().getId()==drugId && drugUserOrder.getDrugCount()-drugCount>=0){
                    final double updatedFinalPrice = drugUserOrder.getDrug().getPrice().multiply(BigDecimal.valueOf(drugUserOrder.getDrugCount()-drugCount)).doubleValue();
                    return drugUserOrderDao.updateDrugCountAndFinalPriceByDrugUserOrderId(drugCount, drugUserOrderId,updatedFinalPrice);
                }
            }
        }catch (DaoException e){
            daoHelper.rollbackTransactionAndCloseConnection();
            throw new ServiceException();
        }finally {
            daoHelper.commitTransactionAndCloseConnection();
        }
        return false;
    }

    @Override
    public boolean payOrder(User user, Integer cardId) {
        final DaoHelper daoHelper = daoHelperFactory.create();
        daoHelper.startTransaction();
        final UserOrderDao userOrderDao = daoHelper.createUserOrderDao();
        final int userId = user.getId();
        try{
           final Optional<UserOrder> userOrderFromDB =  userOrderDao.findNotPayedUserOrderByUserId(userId);
           if(userOrderFromDB.isPresent()){
               final UserOrder userOrder = userOrderFromDB.get();
               final DrugUserOrderDao drugUserOrderDao = daoHelper.createDrugUserOrderDao();
               final int userOrderId = userOrder.getId();
               final  List<DrugUserOrder> drugUserOrderList =   drugUserOrderDao.findDrugUserOrdersByUserOrderId(userOrderId);
               BigDecimal finalPrice = BigDecimal.ZERO;
               for(DrugUserOrder drugUserOrder : drugUserOrderList){
                   finalPrice= finalPrice.add(drugUserOrder.getFinalPrice());
                   final boolean drugUserOrderDrugNeedRecipe =  drugUserOrder.getDrug().getNeedRecipe();
                   final int drugId = drugUserOrder.getDrug().getId();
                   if(drugUserOrderDrugNeedRecipe){
                       final RecipeDao recipeDao = daoHelper.createRecipeDao();
                       final Optional<Recipe> activeRecipeInDB = recipeDao.findActiveRecipeByUserIdAndDrugId(userId,drugId);
                       if(activeRecipeInDB.isPresent()){
                           return  checkDataAndPay(user, cardId, daoHelper, userOrderId, finalPrice, drugUserOrder, drugId);
                       }
                   }else {
                       return checkDataAndPay(user, cardId, daoHelper, userOrderId, finalPrice, drugUserOrder, drugId);
                   }
               }
           }
        }catch (DaoException e){
            daoHelper.rollbackTransactionAndCloseConnection();
        }finally {
            daoHelper.commitTransactionAndCloseConnection();
        }
        return false;
    }

    private boolean checkDataAndPay(User user, Integer cardId, DaoHelper daoHelper, int userOrderId, BigDecimal finalPrice, DrugUserOrder drugUserOrder, int drugId) throws DaoException {
        final DrugDao drugDao = daoHelper.createDrugDao();
        final Optional<Drug> drugFromDB = drugDao.findEntityById(drugId);
        if(drugFromDB.isPresent()){
            final Drug drug = drugFromDB.get();
            final int newDrugCount =drug.getCount() - drugUserOrder.getDrugCount();
            if(newDrugCount>=0){
                drugDao.update(new Drug.Builder().
                        withId(drug.getId()).
                        withName(drug.getName()).
                        withPrice(drug.getPrice()).
                        withCount(newDrugCount).
                        withDescription(drug.getDescription()).
                        withProducer(drug.getProducer()).
                        withNeedReceip(drug.getNeedRecipe()).
                        withIsDeleted(drug.getDeleted()).
                        build());
                final BankCardDao bankCardDao = daoHelper.createBankCardDao();
                final Optional<BankCard> bankCardFromDB = bankCardDao.findEntityById(cardId);
                if(bankCardFromDB.isPresent()){
                    final BankCard bankCard = bankCardFromDB.get();
                    if(bankCard.getUser().equals(user)){
                        final BigDecimal cardBalanceAfterPay = bankCard.getBalance().subtract(finalPrice);
                        if(cardBalanceAfterPay.doubleValue()>=0){
                            bankCardDao.update(new BankCard.Builder().
                                    withId(bankCard.getId()).
                                    withUser(bankCard.getUser()).
                                    withBalance(cardBalanceAfterPay).
                                    build());
                            final PaidUserOrderDao paidUserOrderDao = daoHelper.createPaidUserOrderDao();
                            return  paidUserOrderDao.createPaidUserOrderWithCurrentDateByUserOrderId(userOrderId);
                        }
                    }
                }
            }

        }
        return false;
    }

    @Override
    public boolean deleteNotPayedOrderByOrderIdAndUserId(User user, Integer OrderId) throws ServiceException {
        final DaoHelper daoHelper = daoHelperFactory.create();
        daoHelper.startTransaction();
        final int userId = user.getId();
        final UserOrderDao userOrderDao = daoHelper.createUserOrderDao();
       try{
         final Optional<UserOrder> userOrderFromDB = userOrderDao.findNotPayedUserOrderByUserId(userId);
         if(userOrderFromDB.isPresent()){
             final UserOrder userOrder = userOrderFromDB.get();
             final int userOrderId = userOrder.getId();
             final DrugUserOrderDao drugUserOrderDao = daoHelper.createDrugUserOrderDao();
            return drugUserOrderDao.deleteDrugUserOrderByUserOrderId(userOrderId) && userOrderDao.delete(OrderId);
         }
       }catch (DaoException e){
           daoHelper.rollbackTransactionAndCloseConnection();
           LOG.error("Cannot delete Order",e);
            throw new ServiceException("Cannot delete Order",e);
       }finally {
           daoHelper.commitTransactionAndCloseConnection();
       }
       LOG.info("Cannot delete Order");
       return false;
    }
}
