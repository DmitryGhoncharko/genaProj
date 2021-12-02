package by.ghoncharko.webproject.model.service;


import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.Producer;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;

import by.ghoncharko.webproject.model.dao.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DrugServiceImpl implements DrugService {
    private static final Logger LOG = LogManager.getLogger(DrugServiceImpl.class);
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Override
    public List<Drug> findAll() {
        Connection connection = connectionPool.getConnection();
        try {
            DrugDao drugDao = new DrugDaoImpl(connection);
            return drugDao.findAll();
        } catch (DaoException e) {
            LOG.error("dao exception in methot findAll drugService", e);
            return Collections.emptyList();
        }finally {
            Service.connectionClose(connection);
        }
    }

    @Override
    public boolean update(Integer drugId, String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, String drugProducerName) {
        Connection connection = connectionPool.getConnection();
        DrugDao drugDao = new DrugDaoImpl(connection);
        ProducerDao producerDao = new ProducerDaoImpl(connection);
        try{
            Optional<Producer> producerFromDB = producerDao.findProducerByName(drugProducerName);
            if(producerFromDB.isPresent()){
                Producer producer = producerFromDB.get();
                Drug drugForUpdate = new Drug.Builder().
                        withId(drugId).
                        withName(drugName).
                        withPrice(drugPrice).
                        withCount(drugCount).
                        withDescription(drugDescription).
                        withNeedReceip(drugNeedRecipe).
                        withProducer(producer).
                        build();
                drugDao.create(drugForUpdate);
                return true;
            }else {
                Producer producerForCreate = new Producer.Builder().
                        withName(drugProducerName).build();
                Drug drugForUpdate = new Drug.Builder().
                        withId(drugId).
                        withName(drugName).
                        withPrice(drugPrice).
                        withCount(drugCount).
                        withDescription(drugDescription).
                        withNeedReceip(drugNeedRecipe).
                        withProducer(producerForCreate).
                        build();
                return true;
            }
        }catch (DaoException e){

        }finally {
            Service.connectionClose(connection);
        }
        return false;
    }

    @Override
    public List<Drug> findAllWhereCountMoreThanZeroByUserId(Integer userId){
        Connection connection = connectionPool.getConnection();

        try {
            DrugDao drugDao = new DrugDaoImpl(connection);
            return drugDao.findAllWhereCountMoreThanZeroWithStatusActiveByUserId(userId);
        } catch (DaoException e) {
            LOG.error("dao exception in methot findAll drugService", e);
            return Collections.emptyList();
        }finally {
            Service.connectionClose(connection);
        }
    }
    @Override
    public List<Drug> findAllWhereCountMoreThanZero(){
        Connection connection = connectionPool.getConnection();

        try {
            DrugDao drugDao = new DrugDaoImpl(connection);
            return drugDao.findAllWhereCountMoreThanZero();
        } catch (DaoException e) {
            LOG.error("dao exception in methot findAll drugService", e);
            return Collections.emptyList();
        }finally {
            Service.connectionClose(connection);
        }
    }

    @Override
    public boolean deleteByDrugId(Integer drugId) {
        Connection connection = connectionPool.getConnection();
        try{
            DrugDao drugDao = new DrugDaoImpl(connection);
          return   drugDao.deleteByDrugId(drugId);
        }catch (DaoException e){

        }finally {
            Service.connectionClose(connection);
        }
        return false;
    }

    public static DrugServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DrugServiceImpl INSTANCE = new DrugServiceImpl();
    }


}
