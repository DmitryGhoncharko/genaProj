package by.ghoncharko.webproject.model.connection;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Realization connection pool with two blocking queue for given away and ready to given away connections
 * Singleton
 *
 * @author Dmitry Ghoncharko
 */
public class BlockingConnectionPool implements ConnectionPool {
    private static final Logger LOG = LogManager.getLogger(BlockingConnectionPool.class);
    private static final int DEFAULT_POOL_SIZE = 8;
    private static final Lock lock = new ReentrantLock(true);
    private static final AtomicBoolean isCreated = new AtomicBoolean();
    private static BlockingConnectionPool instance;
    private static BlockingQueue<ProxyConnection> freeConnections;
    private static BlockingQueue<ProxyConnection> givenAwayConnections;

    private BlockingConnectionPool() {
        freeConnections = new LinkedBlockingDeque<>(DEFAULT_POOL_SIZE);
        givenAwayConnections = new LinkedBlockingDeque<>(DEFAULT_POOL_SIZE);
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            try {
                Connection connection = ConnectionCreator.getConnecion();
                ProxyConnection proxyConnection = new ProxyConnection(connection);
                freeConnections.add(proxyConnection);
                LOG.info("Connection created");
            } catch (SQLException e) {
                LOG.error("Couldn't create connection to database", e.getMessage());
            }
        }
        if (freeConnections.size() == 0) {
            LOG.fatal("Connection pool don't created connections. Pool size -", freeConnections.size());
            throw new RuntimeException("Connection pool don't created connections");
        }
        LOG.info("Connection pool was created");
    }

    public static BlockingConnectionPool getInstance() {

        if (!isCreated.get()) {
            lock.lock();
            if (instance == null) {
                instance = new BlockingConnectionPool();
                isCreated.set(true);
            }
            lock.unlock();
        }

        return instance;


    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = new ProxyConnection(freeConnections.take());
            givenAwayConnections.add((ProxyConnection) connection);
        } catch (InterruptedException e) {
            LOG.error("InterruptedException in method getConnection", e.getMessage());
            Thread.currentThread().isInterrupted();
        }
        return connection;
    }

    public void releaseConnection(Connection connection) {

        if (connection instanceof ProxyConnection && givenAwayConnections.remove(connection)) {
            try {
                freeConnections.put((ProxyConnection) connection);
            } catch (InterruptedException e) {
                LOG.error("InterruptedException", e.getMessage());
                Thread.currentThread().isInterrupted();
            }
        }
    }

    public void destroyPool() {
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            closeAnyConnection();
        }
        deregisterDrivers();
    }

    private void closeAnyConnection() {
        try {
            freeConnections.take().reallyCLose();
        } catch (InterruptedException e) {
            LOG.error("InterruptedException in method destroy pool", e.getMessage());
        } catch (SQLException e) {
            LOG.error("SQLException in method destroy pool", e.getMessage());
        }
    }

    private void deregisterDrivers() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                LOG.error("SQLException in method deregisterDrivers", e.getMessage());
            }

        }
    }
}
