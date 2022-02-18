package by.ghoncharko.webproject.model.connection;

import java.sql.Connection;

/**
 * Interface for ConnectionPool
 *
 * @author Dmitry Ghoncharko
 * @see BlockingConnectionPool
 */
public interface ConnectionPool {
    /**
     * @return Instance of connection pool
     */
    static ConnectionPool getInstance() {
        return BlockingConnectionPool.getInstance();
    }

    /**
     * @return connection from connection pool
     */
    Connection getConnection();

    /**
     * @param connection add connection to connection pool
     */
    void releaseConnection(Connection connection);

    /**
     * Destroy pool - close all connections and deregister drivers
     */
    void destroyPool();

}
