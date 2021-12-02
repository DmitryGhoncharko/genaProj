package by.ghoncharko.webproject.model.service;


import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.Producer;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;

import by.ghoncharko.webproject.model.dao.DrugDao;
import by.ghoncharko.webproject.model.dao.DrugDaoImpl;
import by.ghoncharko.webproject.model.dao.ProducerDao;
import by.ghoncharko.webproject.model.dao.ProducerDaoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DrugServiceImpl implements DrugService {
    private static final Logger LOG = LogManager.getLogger(DrugServiceImpl.class);
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private DrugServiceImpl(){
    }
    @Override
    public List<Drug> findAll() {
        final Connection connection = connectionPool.getConnection();
        try {
            final DrugDao drugDao = new DrugDaoImpl(connection);
            return drugDao.findAll();
        } catch (DaoException e) {
            LOG.error("dao exception in method findAll drugService", e);
            return Collections.emptyList();
        } finally {
            Service.connectionClose(connection);
        }
    }

    @Override
    public boolean update(Integer drugId, String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, String drugProducerName) {
        final Connection connection = connectionPool.getConnection();
        Service.autoCommitFalse(connection);
        final DrugDao drugDao = new DrugDaoImpl(connection);
        final ProducerDao producerDao = new ProducerDaoImpl(connection);
        try {
            final Optional<Producer> producerFromDB = producerDao.findProducerByName(drugProducerName);
            if (producerFromDB.isPresent()) {
                final Producer producer = producerFromDB.get();
                updateDrugIfRecipeIsExistInDatabase(drugId, drugName, drugNeedRecipe, drugCount, drugPrice, drugDescription, drugDao, producer);
            } else {
                updateDrugIfRecipeIsNotExistInDatabase(drugId, drugName, drugNeedRecipe, drugCount, drugPrice, drugDescription, drugProducerName, drugDao);
            }
            return true;
        } catch (DaoException e) {
            LOG.error("DaoException", e);
            Service.rollbackConnection(connection);
        } finally {
            Service.connectionClose(connection);
        }
        Service.rollbackConnection(connection);
        return false;
    }

    private void updateDrugIfRecipeIsNotExistInDatabase(Integer drugId, String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, String drugProducerName, DrugDao drugDao) throws DaoException {
        final Producer producerForCreate = new Producer.Builder().
                withName(drugProducerName).build();
        final Drug drugForUpdate = createDrug(drugId, drugName, drugNeedRecipe, drugCount, drugPrice, drugDescription, producerForCreate);
        drugDao.create(drugForUpdate);
    }


    private void updateDrugIfRecipeIsExistInDatabase(Integer drugId, String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, DrugDao drugDao, Producer producer) throws DaoException {
        final Drug drugForUpdate = createDrug(drugId, drugName, drugNeedRecipe, drugCount, drugPrice, drugDescription, producer);
        drugDao.create(drugForUpdate);
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
    public List<Drug> findAllWhereCountMoreThanZeroByUserId(Integer userId) {
        final Connection connection = connectionPool.getConnection();

        try {
            final DrugDao drugDao = new DrugDaoImpl(connection);
            return drugDao.findAllWhereCountMoreThanZeroWithStatusActiveByUserId(userId);
        } catch (DaoException e) {
            LOG.error("dao exception in methot findAll drugService", e);
            return Collections.emptyList();
        } finally {
            Service.connectionClose(connection);
        }
    }

    @Override
    public List<Drug> findAllWhereCountMoreThanZero() {
        final Connection connection = connectionPool.getConnection();
        try {
            final DrugDao drugDao = new DrugDaoImpl(connection);
            return drugDao.findAllWhereCountMoreThanZero();
        } catch (DaoException e) {
            LOG.error("dao exception in methot findAll drugService", e);
        } finally {
            Service.connectionClose(connection);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean create(String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, String drugProducerName) {
        final Connection connection = connectionPool.getConnection();
        Service.autoCommitFalse(connection);
        final DrugDao drugDao = new DrugDaoImpl(connection);
        final ProducerDao producerDao = new ProducerDaoImpl(connection);
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
            Service.rollbackConnection(connection);
            LOG.error("DaoException", e);
        } finally {
            Service.connectionClose(connection);
        }
        Service.rollbackConnection(connection);
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
    public boolean deleteByDrugId(Integer drugId) {
        final Connection connection = connectionPool.getConnection();
        try {
            final DrugDao drugDao = new DrugDaoImpl(connection);
            return drugDao.deleteByDrugId(drugId);
        } catch (DaoException e) {
            LOG.error("DaoException", e);
        } finally {
            Service.connectionClose(connection);
        }
        return false;
    }

     static DrugServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DrugServiceImpl INSTANCE = new DrugServiceImpl();
    }


}
