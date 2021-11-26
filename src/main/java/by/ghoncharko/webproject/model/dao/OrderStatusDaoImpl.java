package by.ghoncharko.webproject.model.dao;


import by.ghoncharko.webproject.entity.OrderStatus;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class OrderStatusDaoImpl extends AbstractDao<OrderStatus> {
    private static final Logger LOG = LogManager.getLogger(OrderStatusDaoImpl.class);
    private static final String SQL_CREATE_ORDER_STATUS = "INSERT INTO order_status" +
            " (status_name) VALUES (?)";
    private static final String SQL_FIND_ALL_ORDER_STATUS = "SELECT id , status_name FROM order_status";
    private static final String SQL_FIND_ORDER_STATUS_BY_ID = "SELECT status_name FROM order_status WHERE id = ?";
    private static final String SQL_UPDATE_ORDER_STATUS = "UPDATE order_status SET status_name = ? WHERE id = ?";
    private static final String SQL_DELETE_ORDER_STATUS = "DELETE FROM order_status WHERE id = ?";


    private OrderStatusDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public OrderStatus create(OrderStatus entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_CREATE_ORDER_STATUS, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getName());
            int countRows = preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (countRows > 0 && resultSet.next()) {
                return new OrderStatus.Builder().
                        withId(resultSet.getInt(1)).
                        withName(entity.getName()).
                        build();
            }
        } catch (SQLException e) {
            LOG.error("cannot create order status", e);
            throw new DaoException("cannot create order status", e);
        } finally {
            close(preparedStatement);
        }
        LOG.error("cannot create order status");
        throw new DaoException();
    }

    @Override
    public List<OrderStatus> findAll() throws DaoException {
        List<OrderStatus> orderStatusList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_ALL_ORDER_STATUS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                OrderStatus orderStatus = new OrderStatus.Builder().
                        withId(resultSet.getInt(1)).
                        withName(resultSet.getString(2)).
                        build();
                orderStatusList.add(orderStatus);
            }
        } catch (SQLException e) {
            LOG.error("cannot find all order status",e);
            throw new DaoException("cannot create order status", e);
        } finally {
            close(preparedStatement);
        }
        return orderStatusList;
    }

    @Override
    public Optional<OrderStatus> findEntityById(Integer id) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_ORDER_STATUS_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                OrderStatus orderStatus = new OrderStatus.Builder().
                        withId(id).
                        withName(resultSet.getString(1)).
                        build();
                return Optional.of(orderStatus);
            }
        } catch (SQLException e) {
            LOG.error("SQLException in method findEntityById",e);
            throw new DaoException("SQLException in method findEntityById",e);
        } finally {
            close(preparedStatement);
        }
        return Optional.empty();
    }

    @Override
    public OrderStatus update(OrderStatus entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_ORDER_STATUS);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return entity;
            }
        } catch (SQLException e) {
            LOG.error("cannot update order status",e);
            throw new DaoException("cannot update order status",e);
        } finally {
            close(preparedStatement);
        }
        LOG.error("cannot update order status");
        throw new DaoException("cannot update order status");
    }

    @Override
    public boolean delete(OrderStatus entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_DELETE_ORDER_STATUS);
            preparedStatement.setInt(1, entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            LOG.error("cannot delete order status",e);
            throw new DaoException("cannot delete order status",e);
        } finally {
            close(preparedStatement);
        }
        return false;
    }
}
