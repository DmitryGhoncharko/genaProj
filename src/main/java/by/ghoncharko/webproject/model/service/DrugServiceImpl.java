package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.Producer;
import by.ghoncharko.webproject.entity.Role;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.DrugDao;
import by.ghoncharko.webproject.model.dao.DrugDaoImpl;
import by.ghoncharko.webproject.model.dao.ProducerDao;
import by.ghoncharko.webproject.model.dao.ProducerDaoImpl;
import by.ghoncharko.webproject.validator.ValidateCreateOrUpdatePreparate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class DrugServiceImpl implements DrugService{
    private static final Logger LOG = LogManager.getLogger(DrugServiceImpl.class);
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private DrugServiceImpl(){

    }
//todo think about producer
    @Override
    public boolean createDrug(String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, String drugProducerName) throws ServiceException {
        final boolean isValidData = ValidateCreateOrUpdatePreparate.getInstance().validate(drugName,drugPrice,drugCount,drugDescription,drugProducerName,drugNeedRecipe);
        if(!isValidData){
            return false;
        }
        final Connection connection = connectionPool.getConnection();
        Service.autoCommitFalse(connection);
        final ProducerDao producerDao = new ProducerDaoImpl(connection);
        try{
            final Optional<Producer> producerFromDB = producerDao.findProducerByProducerName(drugProducerName);
            if (producerFromDB.isPresent()){
                final Producer producer = producerFromDB.get();
                final DrugDao drugDao = new DrugDaoImpl(connection);
                final Drug drug = new Drug.Builder().
                        withName(drugName).
                        withPrice(BigDecimal.valueOf(drugPrice)).
                        withCount(drugCount).
                        withDescription(drugDescription).
                        withProducer(producer).
                        withNeedReceip(drugNeedRecipe).
                        withIsDeleted(false).
                        build();
                drugDao.create(drug);
            }else {
               final Producer producer = producerDao.create(new Producer.Builder().
                        withName(drugProducerName).
                        build());

                final DrugDao drugDao = new DrugDaoImpl(connection);
                final Drug drug = new Drug.Builder().
                        withName(drugName).
                        withPrice(BigDecimal.valueOf(drugPrice)).
                        withCount(drugCount).
                        withDescription(drugDescription).
                        withProducer(producer).
                        withNeedReceip(drugNeedRecipe).
                        withIsDeleted(false).
                        build();
                drugDao.create(drug);

            }
            return true;
        }catch (DaoException e){
            Service.rollbackConnection(connection);
            LOG.error("Cannot create new drug",e);
            throw new ServiceException("Cannot create new drug",e);

        }finally {
            Service.connectionClose(connection);
        }

    }

    @Override
    public List<Drug> findAll() throws ServiceException {
        return null;
    }

    public static DrugServiceImpl getInstance() {
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final DrugServiceImpl INSTANCE = new DrugServiceImpl();
    }
}
