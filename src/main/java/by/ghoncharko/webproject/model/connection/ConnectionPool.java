package by.ghoncharko.webproject.model.connection;

import java.sql.Connection;

public interface ConnectionPool {
    Connection getConnection();

    void releaseConnection(Connection connection);

    void destroyPool();

    static ConnectionPool getInstance() {
        return BlockingConnectionPool.getInstance();
    }

}
