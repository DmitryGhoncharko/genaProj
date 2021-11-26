package by.ghoncharko.webproject.model.dao;


import by.ghoncharko.webproject.entity.*;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl extends AbstractDao<Order> {
    private static final Logger LOG = LogManager.getLogger(OrderDaoImpl.class);
    private static final String SQL_CREATE_ORDER = "INSERT INTO drug_order" +
            " (user_id, drug_id, count,  status_id, final_price)" +
            " VALUES (?,?,?,?,?)";
    private static final String SQL_FIND_ALL_ORDERS = "SELECT drug_order.id, drug_order.count, drug_order.final_price," +
            " u.id, login, password, first_name, last_name," +
            " r.id, role_name, d.id, name, price, d.count, description, need_receip, p.id, producer_name," +
            " os.id, status_name" +
            " FROM drug_order" +
            " INNER JOIN user u ON drug_order.user_id = u.id" +
            " INNER JOIN role r ON u.role_id = r.id" +
            " INNER JOIN  drug d ON drug_order.drug_id = d.id" +
            " INNER JOIN producer p ON d.producer_id = p.id" +
            " INNER JOIN order_status os ON drug_order.status_id = os.id";
    private static final String SQL_FIND_ALLORDERS_WITH_STATUS_ACTIVE = "SELECT drug_order.id, drug_order.count,drug_order.final_price," +
            " u.id, login, password, first_name, last_name," +
            " r.id, role_name, d.id, name, price, d.count, description, need_receip, p.id, producer_name," +
            " os.id, status_name" +
            " FROM drug_order" +
            " INNER JOIN user u ON drug_order.user_id = u.id" +
            " INNER JOIN role r ON u.role_id = r.id" +
            " INNER JOIN  drug d ON drug_order.drug_id = d.id" +
            " INNER JOIN producer p ON d.producer_id = p.id" +
            " INNER JOIN order_status os ON drug_order.status_id = os.id" +
            " WHERE status_id = 1 AND drug_order.user_id = ?";
    private static final String SQL_FIND_ORDER_BY_ID = "SELECT drug_order.id, drug_order.count, drug_order.final_price," +
            " u.id, login, password, first_name, last_name," +
            " r.id, role_name, d.id, name, price, d.count, description, need_receip, p.id, producer_name," +
            " os.id, status_name" +
            " FROM drug_order" +
            " INNER JOIN user u ON drug_order.user_id = u.id" +
            " INNER JOIN role r ON u.role_id = r.id" +
            " INNER JOIN  drug d ON drug_order.drug_id = d.id" +
            " INNER JOIN producer p ON d.producer_id = p.id" +
            " INNER JOIN order_status os ON drug_order.status_id = os.id" +
            " WHERE drug_order.id = ?";
    private static final String SQL_UPDATE_ORDER = "UPDATE drug_order" +
            " SET user_id = ?, drug_id = ? ,  count = ?, status_id = ?, final_price = ?" +
            " WHERE id = ?";
    private static final String SQL_UPDATE_ORDER_STATUS_BY_USER_ID_AND_DRUG_ID = "update drug_order set status_id = ? where user_id =? AND drug_id = ? AND  id = ?";
    private static final String SQL_DELETE_ORDER = "DELETE  FROM drug_order WHERE  id = ?";

    public OrderDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Order create(Order entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_CREATE_ORDER, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, entity.getUser().getId());
            preparedStatement.setInt(2, entity.getDrug().getId());
            preparedStatement.setInt(3, entity.getCount());
            preparedStatement.setInt(4, entity.getStatus().getId());
            preparedStatement.setDouble(5, entity.getFinalPrice());
            int countRows = preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.executeQuery();
            if (countRows > 0 && resultSet.next()) {
                return new Order.Builder().
                        withId(resultSet.getInt(1)).
                        withUser(entity.getUser()).
                        withDrug(entity.getDrug()).
                        withCount(entity.getCount()).
                        withStatus(entity.getStatus()).
                        withFinalPrice(entity.getFinalPrice()).
                        build();
            }
        } catch (SQLException e) {
            LOG.error("cannot create order",e);
            throw new DaoException("cannot create order",e);
        } finally {
            close(preparedStatement);
        }
        LOG.error("cannot create order");
        throw new DaoException();
    }
    public boolean create(Integer userId, Integer drugId, Integer count, OrderStatus status, Double finalPrice) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_CREATE_ORDER);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, drugId);
            preparedStatement.setInt(3, count);
            preparedStatement.setInt(4, status.getId());
            preparedStatement.setDouble(5, finalPrice);
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            LOG.error("cannot create order",e);
            throw new DaoException("cannot create order",e);
        } finally {
            close(preparedStatement);
        }
        return false;
    }
    @Override
    public List<Order> findAll() throws DaoException {
        List<Order> orderList = new ArrayList<>();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_ORDERS);
            while (resultSet.next()) {
                Order order = new Order.Builder().
                        withId(resultSet.getInt(1)).
                        withCount(resultSet.getInt(2)).
                        withFinalPrice(resultSet.getDouble(3)).
                        withUser(new User.Builder().
                                withId(resultSet.getInt(4)).
                                withLogin(resultSet.getString(5)).
                                withPassword(resultSet.getString(6)).
                                withFirstName(resultSet.getString(7)).
                                withLastName(resultSet.getString(8)).
                                withRole(new Role.Builder().
                                        withId(resultSet.getInt(9)).
                                        withRoleName(resultSet.getString(10)).build()).
                                build()
                        ).
                        withDrug(new Drug.Builder().
                                withId(resultSet.getInt(11)).
                                withName(resultSet.getString(12)).
                                withPrice(resultSet.getDouble(13)).
                                withCount(resultSet.getInt(14)).
                                withDescription(resultSet.getString(15)).
                                withNeedReceip(resultSet.getBoolean(16)).
                                withProducer(new Producer.Builder().
                                        withId(resultSet.getInt(17)).
                                        withName(resultSet.getString(18)).
                                        build()).build()
                        ).
                        withStatus(new OrderStatus.Builder().
                                withId(resultSet.getInt(19)).
                                withName(resultSet.getString(20)).build()).build();
                orderList.add(order);
            }
        } catch (SQLException e) {
            LOG.error("cannot find all orders", e);
            throw new DaoException("cannot find all orders", e);
        } finally {
            close(statement);
        }
        return orderList;
    }

    public List<Order> findAll(Integer userId) throws DaoException {
        List<Order> orderList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_ALLORDERS_WITH_STATUS_ACTIVE);
            preparedStatement.setInt(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Order order = new Order.Builder().
                        withId(resultSet.getInt(1)).
                        withCount(resultSet.getInt(2)).
                        withFinalPrice(resultSet.getDouble(3)).
                        withUser(new User.Builder().
                                withId(resultSet.getInt(4)).
                                withLogin(resultSet.getString(5)).
                                withPassword(resultSet.getString(6)).
                                withFirstName(resultSet.getString(7)).
                                withLastName(resultSet.getString(8)).
                                withRole(new Role.Builder().
                                        withId(resultSet.getInt(9)).
                                        withRoleName(resultSet.getString(10)).build()).
                                build()
                        ).
                        withDrug(new Drug.Builder().
                                withId(resultSet.getInt(11)).
                                withName(resultSet.getString(12)).
                                withPrice(resultSet.getDouble(13)).
                                withCount(resultSet.getInt(14)).
                                withDescription(resultSet.getString(15)).
                                withNeedReceip(resultSet.getBoolean(16)).
                                withProducer(new Producer.Builder().
                                        withId(resultSet.getInt(17)).
                                        withName(resultSet.getString(18)).
                                        build()).build()
                        ).
                        withStatus(new OrderStatus.Builder().
                                withId(resultSet.getInt(19)).
                                withName(resultSet.getString(20)).build()).build();
                orderList.add(order);
            }
        } catch (SQLException e) {
            LOG.error("cannot find all orders", e);
            throw new DaoException("cannot find all orders", e);
        } finally {
            close(preparedStatement);
        }
        return orderList;
    }
    @Override
    public Optional<Order> findEntityById(Integer id) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_ORDER_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Order order = new Order.Builder().
                        withId(resultSet.getInt(1)).
                        withCount(resultSet.getInt(2)).
                        withFinalPrice(resultSet.getDouble(3)).
                        withUser(new User.Builder().
                                withId(resultSet.getInt(4)).
                                withLogin(resultSet.getString(5)).
                                withPassword(resultSet.getString(6)).
                                withFirstName(resultSet.getString(7)).
                                withLastName(resultSet.getString(8)).
                                withRole(new Role.Builder().
                                        withId(resultSet.getInt(9)).
                                        withRoleName(resultSet.getString(10)).build()).
                                build()
                        ).
                        withDrug(new Drug.Builder().
                                withId(resultSet.getInt(11)).
                                withName(resultSet.getString(12)).
                                withPrice(resultSet.getDouble(13)).
                                withCount(resultSet.getInt(14)).
                                withDescription(resultSet.getString(15)).
                                withNeedReceip(resultSet.getBoolean(16)).
                                withProducer(new Producer.Builder().
                                        withId(resultSet.getInt(17)).
                                        withName(resultSet.getString(18)).
                                        build()).build()
                        ).
                        withStatus(new OrderStatus.Builder().
                                withId(resultSet.getInt(19)).
                                withName(resultSet.getString(20)).build()).build();
                return Optional.of(order);
            }
        } catch (SQLException e) {
            LOG.error("cannot find entity by id",e);
            throw new DaoException("cannot find entity by id",e);
        } finally {
            close(preparedStatement);
        }
        return Optional.empty();
    }


    public boolean update(Integer userId, Integer drugId, OrderStatus status, Integer orderId) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_ORDER_STATUS_BY_USER_ID_AND_DRUG_ID);
            preparedStatement.setInt(1,status.getId() );
            preparedStatement.setInt(2,userId);
            preparedStatement.setInt(3, drugId);
            preparedStatement.setInt(4,orderId);
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            LOG.error("cannot update order",e );
            throw new DaoException("cannot update order",e);
        } finally {
            close(preparedStatement);
        }
        LOG.error("cannot update order");
        throw new DaoException();
    }
    public Order update(Order entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_ORDER);
            preparedStatement.setInt(1, entity.getUser().getId());
            preparedStatement.setInt(2, entity.getDrug().getId());
            preparedStatement.setInt(3, entity.getCount());
            preparedStatement.setInt(4, entity.getStatus().getId());
            preparedStatement.setDouble(5, entity.getFinalPrice());
            preparedStatement.setInt(6,entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return entity;
            }
        } catch (SQLException e) {
            LOG.error("cannot update order",e );
            throw new DaoException("cannot update order",e);
        } finally {
            close(preparedStatement);
        }
        LOG.error("cannot update order");
        throw new DaoException();
    }
    @Override
    public boolean delete(Order entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_DELETE_ORDER);
            preparedStatement.setInt(1, entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            LOG.error("cannot delete order",e);
            throw new DaoException("cannot delete order",e);
        } finally {
            close(preparedStatement);
        }
        return false;
    }
}