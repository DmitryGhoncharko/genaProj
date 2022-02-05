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
import by.ghoncharko.webproject.validator.ValidateAddToUserOrder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService{
    private static final Logger LOG = LogManager.getLogger(OrderServiceImpl.class);
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private OrderServiceImpl(){

    }

    @Override
    public boolean addToOrder(User user, Integer drugId, Integer drugCount) {
        final boolean isValidData = ValidateAddToUserOrder.getInstance().validate(user, drugId, drugCount);
        if(!isValidData){
            return false;
        }
        final Connection connection = connectionPool.getConnection();
        Service.autoCommitFalse(connection);
        final DrugDao drugDao = new DrugDaoImpl(connection);
        try{
            //todo think about refactor this and try to take data from database only
            final Optional<Drug> drugFromDB = drugDao.findDrugByDrugIdWhereCountMoreThanZeroAndDrugDontDeleted(drugId);
            if(drugFromDB.isPresent()){
               final Drug drug = drugFromDB.get();
               if(drug.getNeedRecipe()){
                    final RecipeDao recipeDao = new RecipeDaoImpl(connection);
                    final LocalDate localDate = LocalDate.now();
                   final   Optional<Recipe> recipeFromDB = recipeDao.findActiveRecipeByUserIdAndDrugId(user.getId(), drugId);
                    if(recipeFromDB.isPresent()){
                        final DrugUserOrderDao drugUserOrderDao = new DrugUserOrderDaoImpl(connection);
                        final UserOrderDao userOrderDao = new UserOrderDaoImpl(connection);
                        userOrderDao.create(new UserOrder.Builder().
                                withUser(user).
                                withOrderFinalPrice(drug.getPrice()).build());

                    }else {
                        return false;
                    }
               }
            }

        }catch (DaoException e){

        }

    }

    @Override
    public List<DrugUserOrder> findAll() throws ServiceException {
        return null;
    }

    public static OrderServiceImpl getInstance() {
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final OrderServiceImpl INSTANCE = new OrderServiceImpl();
    }
}
