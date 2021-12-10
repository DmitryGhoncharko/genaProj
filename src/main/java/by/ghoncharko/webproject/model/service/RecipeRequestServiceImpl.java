package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.RecipeRequest;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.RecipeDao;
import by.ghoncharko.webproject.model.dao.RecipeDaoImpl;
import by.ghoncharko.webproject.model.dao.RecipeRequestDao;
import by.ghoncharko.webproject.model.dao.RecipeRequestDaoImpl;
import by.ghoncharko.webproject.validator.ValidateAcceptRecipeRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RecipeRequestServiceImpl implements RecipeRequestService {
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final Logger LOG = LogManager.getLogger(RecipeRequestServiceImpl.class);

    private RecipeRequestServiceImpl() {
    }

    @Override
    public boolean createRecipeRequestByUserIdAndDrugIdWithDateStartAndDateEnd(Integer userId, Integer drugId, Date dateStart, Date dateEnd, boolean isNeedRecipe) {
        if (isNeedRecipe) {
            Connection connection = connectionPool.getConnection();
            RecipeRequestDao recipeRequestDao = new RecipeRequestDaoImpl(connection);
            RecipeDao recipeDao = new RecipeDaoImpl(connection);
            try {
             Optional<RecipeRequest> recipeRequestFromDB =  recipeRequestDao.findRecipeRequestByUserIdAndDrugId(userId, drugId);
             if(!recipeRequestFromDB.isPresent()){
                 return recipeRequestDao.createRecipeRequestByUserIdAndDrugIdWithDateStartAndDateEnd(userId, drugId, dateStart, dateEnd);
             }
            } catch (DaoException e) {
                Service.rollbackConnection(connection);
                LOG.error("DaoException", e);
                return false;
            } finally {
                Service.connectionClose(connection);
            }
        }
        return false;
    }

    @Override
    public boolean acceptRecipeRequest(Integer recipeRequestId, Integer userId, Integer drugId, Date updatedDateEnd) {
        final boolean isValide = ValidateAcceptRecipeRequest.getInstance().validate(recipeRequestId, userId, drugId, updatedDateEnd);
        if (isValide) {
            final Connection connection = connectionPool.getConnection();
            Service.autoCommitFalse(connection);
            final RecipeRequestDao recipeRequestDao = new RecipeRequestDaoImpl(connection);
            final RecipeDao recipeDao = new RecipeDaoImpl(connection);
            try {
              final   String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                final Date dateStart = Date.valueOf(timeStamp);
                final boolean recipeIsUpdated = recipeDao.updateDateStartAndDateEndByUserIdAndDrugId(userId, drugId, dateStart, updatedDateEnd);
                final boolean recipeRequestIsDeleted = recipeRequestDao.deleteById(recipeRequestId);
                if (recipeIsUpdated && recipeRequestIsDeleted) {
                    return true;
                }
            } catch (DaoException e) {
                LOG.error("DaoException", e);
                Service.rollbackConnection(connection);
                return false;
            } finally {
                Service.connectionClose(connection);
            }
        }
        return false;
    }

    @Override
    public boolean declineRecipeRequest(Integer recipeRequestId, Integer userId, Integer drugId) {
        final Connection connection = connectionPool.getConnection();

        final RecipeRequestDao recipeRequestDao = new RecipeRequestDaoImpl(connection);
        try {
            return recipeRequestDao.deleteById(recipeRequestId);

        } catch (DaoException e) {

            LOG.error("DaoException", e);
            return false;
        } finally {
            Service.connectionClose(connection);
        }
    }

    @Override
    public List<RecipeRequest> findAll() {
       final Connection connection = connectionPool.getConnection();
       final RecipeRequestDao recipeRequestDao = new RecipeRequestDaoImpl(connection);
       try{
           return recipeRequestDao.findAll();
       }catch (DaoException e){

       }finally {
           Service.connectionClose(connection);
       }
       return Collections.emptyList();
    }

    static RecipeRequestServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final RecipeRequestServiceImpl INSTANCE = new RecipeRequestServiceImpl();
    }
}
