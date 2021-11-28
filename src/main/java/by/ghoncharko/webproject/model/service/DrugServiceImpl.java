package by.ghoncharko.webproject.model.service;


import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.DrugDaoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

public class DrugServiceImpl implements Service<Drug> {
    private static final Logger LOG = LogManager.getLogger(DrugServiceImpl.class);
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Override
    public List<Drug> findAll() {
        Connection connection = connectionPool.getConnection();
        try {
            return new DrugDaoImpl(connection).findAll();
        } catch (DaoException e) {
            LOG.error("dao exception in methot findAll drugService", e);
            return Collections.emptyList();
        }finally {
            Service.connectionClose(connection);
        }
    }
    public List<Drug> findAllWhereCountMoreThanZeroByUserId(Integer userId){
        Connection connection = connectionPool.getConnection();

        try {
            return new DrugDaoImpl(connection).findAllWhereCountMoreThanZeroWithStatusActiveByUserId(userId);
        } catch (DaoException e) {
            LOG.error("dao exception in methot findAll drugService", e);
            return Collections.emptyList();
        }finally {
            Service.connectionClose(connection);
        }
    }
    public List<Drug> findAllWhereCountMoreThanZero(){
        Connection connection = connectionPool.getConnection();

        try {
            return new DrugDaoImpl(connection).findAllWhereCountMoreThanZero();
        } catch (DaoException e) {
            LOG.error("dao exception in methot findAll drugService", e);
            return Collections.emptyList();
        }finally {
            Service.connectionClose(connection);
        }
    }

    public static DrugServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DrugServiceImpl INSTANCE = new DrugServiceImpl();
    }


}
