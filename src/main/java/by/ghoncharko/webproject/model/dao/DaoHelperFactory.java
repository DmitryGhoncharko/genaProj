package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.model.connection.ConnectionPool;


public class DaoHelperFactory {

    private final ConnectionPool connectionPool;

    public DaoHelperFactory(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }

    public DaoHelper create(){
        return new DaoHelper(connectionPool);
    }
}
