package by.ghoncharko.webproject.model.service;


import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.Producer;
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

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DrugServiceImpl implements DrugService {
    private static final Logger LOG = LogManager.getLogger(DrugServiceImpl.class);
    private static final int LIMIT = 5;
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private DrugServiceImpl() {
    }

    @Override
    public List<Drug> findAll() throws ServiceException {
        return null;
    }

    @Override
    public List<Drug> findAllLimitOffsetPagination(Integer pageNumber) throws ServiceException {
        final Connection connection = connectionPool.getConnection();
        final DrugDao drugDao = new DrugDaoImpl(connection);
        try {
            final int offset = LIMIT * (pageNumber - 1);
            return drugDao.findAllLimitOffsetPagination(LIMIT, offset);
        } catch (DaoException e) {
            LOG.error("Dao exception in method findAll drugService", e);
            throw new ServiceException("Cannot find all drugs", e);
        } finally {
            Service.connectionClose(connection);
        }
    }
    public int findMaxCountPagesForAllDrugs() throws ServiceException{
        final Connection connection = connectionPool.getConnection();
        final DrugDao drugDao = new DrugDaoImpl(connection);
        try{
            int countDrugs = drugDao.findCountAllDrugs();
            return  countDrugs/LIMIT;
        }catch (DaoException e){

        }finally {
            Service.connectionClose(connection);
        }
        return 0;
    }

    @Override
    public List<Drug> findAllWhereNeedRecipeLimitOffsetPagination(Integer pageNumber) throws ServiceException {
        final Connection connection = connectionPool.getConnection();
        final DrugDao drugDao = new DrugDaoImpl(connection);
        try {
            final int offset = LIMIT * (pageNumber - 1);
            return drugDao.findAllWhereNeedRecipeLimitOffsetPagination(LIMIT,offset);
        } catch (DaoException e) {
            LOG.error("Cannot find all where need recipe", e);
            throw new ServiceException("Cannot find all where need recipe", e);
        } finally {
            Service.connectionClose(connection);
        }
    }

    @Override
    public int findMaxCountPagesForAllNeedRicipesDrugs() throws ServiceException {
        final Connection connection = connectionPool.getConnection();
        final DrugDao drugDao = new DrugDaoImpl(connection);
        try{
            int countDrugs = drugDao.findCountAllDrugsWhereNeedRecipe();
            return countDrugs/LIMIT;
        }catch (DaoException e){

        }finally {
            Service.connectionClose(connection);
        }
        return 0;
    }

    @Override
    public boolean update(Integer drugId, String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, String drugProducerName) throws ServiceException {
        final boolean isValidData = ValidateCreateOrUpdatePreparate.getInstance().validate(drugName, drugPrice, drugCount, drugDescription, drugProducerName, drugNeedRecipe);
        if (isValidData) {
            final Connection connection = connectionPool.getConnection();
            Service.autoCommitFalse(connection);
            final DrugDao drugDao = new DrugDaoImpl(connection);
            final ProducerDao producerDao = new ProducerDaoImpl(connection);
            try {
                final Optional<Producer> producerFromDB = producerDao.findProducerByName(drugProducerName);
                if (producerFromDB.isPresent()) {
                    final Producer producer = producerFromDB.get();
                    return updateDrugIfProducerIsExistInDatabase(drugId, drugName, drugNeedRecipe, drugCount, drugPrice, drugDescription, drugDao, producer);
                }
                return updateDrugIfProduserIsNotExistInDatabase(drugId, drugName, drugNeedRecipe, drugCount, drugPrice, drugDescription, drugProducerName, drugDao, producerDao);
            } catch (DaoException e) {
                LOG.error("Cannot update drug", e);
                Service.rollbackConnection(connection);
                throw new ServiceException("Cannot update drug", e);
            } finally {
                Service.connectionClose(connection);
            }
        }
        return false;
    }

    private boolean updateDrugIfProduserIsNotExistInDatabase(Integer drugId, String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, String drugProducerName, DrugDao drugDao, ProducerDao producerDao) throws DaoException {
        final Producer producerForCreate = new Producer.Builder().
                withName(drugProducerName).build();
        final Producer producer = producerDao.create(producerForCreate);
        final Drug drugForUpdate = createDrug(drugId, drugName, drugNeedRecipe, drugCount, drugPrice, drugDescription, producer);
        drugDao.update(drugForUpdate);
        return true;
    }


    private boolean updateDrugIfProducerIsExistInDatabase(Integer drugId, String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, DrugDao drugDao, Producer producer) throws DaoException {
        final Drug drugForUpdate = createDrug(drugId, drugName, drugNeedRecipe, drugCount, drugPrice, drugDescription, producer);
        drugDao.update(drugForUpdate);
        return true;
    }

    private Drug createDrug(Integer drugId, String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, Producer producerForCreate) {
        return new Drug.Builder().
                withId(drugId).
                withName(drugName).
                withPrice(drugPrice).
                withCount(drugCount).
                withDescription(drugDescription).
                withNeedReceip(drugNeedRecipe).
                withProducer(producerForCreate).
                build();
    }

    @Override
    public List<Drug> findAllWhereCountMoreThanZeroByUserIdAndCalculateCountLimitOffsetPagination(Integer userId, Integer pageNumber) throws ServiceException {
        if (userId == null) {
            return Collections.emptyList();
        }
        final Connection connection = connectionPool.getConnection();
        final DrugDao drugDao = new DrugDaoImpl(connection);
        try {
            final int offset = LIMIT * (pageNumber - 1);
            return drugDao.findAllWhereCountMoreThanZeroWithStatusActiveByUserIdAndCalculateCountLimitOffsetPagination(userId, LIMIT,offset);
        } catch (DaoException e) {
            LOG.error("Cannot find all drugs where count more than zero by user id", e);
            throw new ServiceException("Cannot find all drugs where count more than zero by user id", e);
        } finally {
            Service.connectionClose(connection);
        }
    }

    @Override
    public int findMaxCountPagesForAllWhereCountMoreThanZeroWithStatusActiveByUserId(Integer userId) {
        final Connection connection = connectionPool.getConnection();
        final DrugDao drugDao = new DrugDaoImpl(connection);
        try{
            int countDrugs = drugDao.findAllWhereCountMoreThanZeroWithStatusActiveByUserId(userId);
          return countDrugs/LIMIT;
        }catch (DaoException e){

        }finally {
            Service.connectionClose(connection);
        }
        return 0;
    }

    @Override
    public List<Drug> findAllWhereCountMoreThanZeroLimitOffsetPagination(Integer pageNumber) throws ServiceException {
        final Connection connection = connectionPool.getConnection();
        final DrugDao drugDao = new DrugDaoImpl(connection);
        try {
            final int offset = LIMIT * (pageNumber - 1);
            return drugDao.findAllWhereCountMoreThanZeroLimitOffsetPagination(LIMIT,offset);
        } catch (DaoException e) {
            LOG.error("Cannot find all drugs where count more than zero", e);
            throw new ServiceException("Cannot find all drugs where count more than zero", e);
        } finally {
            Service.connectionClose(connection);
        }
    }

    @Override
    public int findMaxCountPagesForAllWhereCountMoreThanZero() throws ServiceException {
        final Connection connection = connectionPool.getConnection();
        final DrugDao drugDao = new DrugDaoImpl(connection);
        try{
            int countDrugs = drugDao.findCountAllWhereCountMoreThanZero();
            return countDrugs/LIMIT;
        }catch (DaoException e){

        }finally {
            Service.connectionClose(connection);
        }
        throw new ServiceException();
    }

    @Override
    public boolean create(String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, String drugProducerName) throws ServiceException {
        final boolean isValidData = ValidateCreateOrUpdatePreparate.getInstance().validate(drugName, drugPrice, drugCount, drugDescription, drugProducerName, drugNeedRecipe);
        if (isValidData) {
            final Connection connection = connectionPool.getConnection();
            Service.autoCommitFalse(connection);
            final ProducerDao producerDao = new ProducerDaoImpl(connection);
            final DrugDao drugDao = new DrugDaoImpl(connection);
            try {
                Optional<Producer> producer = producerDao.findProducerByName(drugProducerName);
                if (producer.isPresent()) {
                    final Drug drug = createDrugWithoutId(drugName, drugNeedRecipe, drugCount, drugPrice, drugDescription, producer.get());
                    drugDao.create(drug);
                } else {
                    final Producer createdProducer = producerDao.create(new Producer.Builder().withName(drugProducerName).build());
                    final Drug drug = createDrugWithoutId(drugName, drugNeedRecipe, drugCount, drugPrice, drugDescription, createdProducer);
                    drugDao.create(drug);
                }
                return true;
            } catch (DaoException e) {
                LOG.error("Cannot create drug", e);
                Service.rollbackConnection(connection);
                throw new ServiceException("Cannot create drug", e);
            } finally {
                Service.connectionClose(connection);
            }
        }
        return false;
    }

    private Drug createDrugWithoutId(String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, Producer producer2) {
        return new Drug.Builder().
                withCount(drugCount).
                withDescription(drugDescription).
                withName(drugName).
                withNeedReceip(drugNeedRecipe).
                withPrice(drugPrice).
                withProducer(producer2).
                build();
    }

    @Override
    public boolean deleteByDrugId(Integer drugId) throws ServiceException {
        if (drugId == null) {
            return false;
        }
        final Connection connection = connectionPool.getConnection();
        final DrugDao drugDao = new DrugDaoImpl(connection);
        try {
            return drugDao.deleteByDrugId(drugId);
        } catch (DaoException e) {
            LOG.error("Cannot delete drug by id", e);
            throw new ServiceException("Cannot delete drug by id", e);
        } finally {
            Service.connectionClose(connection);
        }
    }

    static DrugServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DrugServiceImpl INSTANCE = new DrugServiceImpl();
    }

}
