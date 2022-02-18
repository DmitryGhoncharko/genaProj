package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.model.connection.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public class DaoHelper implements AutoCloseable {
    private final Connection connection;

    public DaoHelper(ConnectionPool connectionPool) {
        this.connection = connectionPool.getConnection();
    }

    public BankCardDao createBankCardDao() {
        return new BankCardDaoImpl(connection);
    }

    public DrugDao createDrugDao() {
        return new DrugDaoImpl(connection);
    }

    public DrugUserOrderDao createDrugUserOrderDao() {
        return new DrugUserOrderDaoImpl(connection);
    }

    public PaidUserOrderDao createPaidUserOrderDao() {
        return new PaidUserOrderDaoImpl(connection);
    }

    public ProducerDao createProducerDao() {
        return new ProducerDaoImpl(connection);
    }

    public RecipeDao createRecipeDao() {
        return new RecipeDaoImpl(connection);
    }

    public RecipeRequestDecisionDao createRecipeRequestDecisionDao() {
        return new RecipeRequestDecisionDaoImpl(connection);
    }

    public UserDao createUserDao() {
        return new UserDaoImpl(connection);
    }

    public UserOrderDao createUserOrderDao() {
        return new UserOrderDaoImpl(connection);
    }

    public RecipeRequestDao createRecipeRequestDao() {
        return new RecipeRequestDaoImpl(connection);
    }

    @Override
    public void close() throws SQLException {
        if (!connection.getAutoCommit()) {
            connection.commit();
            connection.setAutoCommit(true);
        }
        connection.close();
    }

    public void startTransaction() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {

        }
    }

    public void rollbackTransactionAndCloseConnection() {
        try {
            connection.rollback();
            connection.close();
        } catch (SQLException e) {

        }
    }

    public void commitTransactionAndCloseConnection() {
        try {
            connection.commit();
            connection.close();
        } catch (SQLException e) {

        }
    }
}
