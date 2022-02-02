package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.PaidUserOrder;
import by.ghoncharko.webproject.entity.Role;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.entity.UserOrder;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PaidUserOrderDaoImpl implements PaidUserOrderDao{
    private static final Logger LOG  = LogManager.getLogger(PaidUserOrderDaoImpl.class);
    private static final String SQL_CREATE_PAID_USER_ORDER = "INSERT INTO paid_user_order(user_order_id, date_payed) VALUES (?,?)";
    private static final String SQL_FIND_ALL_PAID_USER_ORDERS = "SELECT paid_user_order.id, uo.id, uo.order_final_price, u.id, login, password, r.role_name, first_name, last_name, is_banned, order_final_price, date_payed" +
            " FROM  paid_user_order" +
            " INNER JOIN user_order uo on paid_user_order.user_order_id = uo.id" +
            " INNER JOIN user u on uo.user_id = u.id" +
            " INNER JOIN role r on u.role_id = r.id";
    private static final String SQL_FIND_PAID_USER_ORDER_BY_ID = "SELECT paid_user_order.id, uo.id, uo.order_final_price, u.id, login, password, r.role_name, first_name, last_name, is_banned, order_final_price, date_payed" +
            " FROM  paid_user_order" +
            " INNER JOIN user_order uo on paid_user_order.user_order_id = uo.id" +
            " INNER JOIN user u on uo.user_id = u.id" +
            " INNER JOIN role r on u.role_id = r.id" +
            " WHERE paid_user_order.id = ?";
    private static final String SQL_UPDATE_PAID_USER_ORDER_BY_ID = "UPDATE paid_user_order" +
            " SET user_order_id = ?, date_payed = ?" +
            " WHERE id = ?";
    private static final String SQL_DELETE_PAID_USER_ORDER_BY_ID = "DELETE FROM paid_user_order WHERE  id = ?";
    private final Connection connection;

    public PaidUserOrderDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public PaidUserOrder create(PaidUserOrder entity) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_PAID_USER_ORDER, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt(1,entity.getUserOrder().getId());
            preparedStatement.setDate(2,entity.getDatePayed());
            final int countRowsCreated = preparedStatement.executeUpdate();
            if(countRowsCreated>0){
                final ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    return new PaidUserOrder.Builder().
                            withId(resultSet.getInt(1)).
                            withUserOrder(entity.getUserOrder()).
                            withDatePayed(entity.getDatePayed()).
                            build();
                }
            }
        }catch (SQLException e){
            LOG.error("Cannot create PaidUserOrder",e);
            throw new DaoException("Cannot create PaidUserOrder",e);
        }
        LOG.error("Cannot create PaidUserOrder");
        throw new DaoException("Cannot create PaidUserOrder");
    }

    @Override
    public List<PaidUserOrder> findAll() throws DaoException {
        final List<PaidUserOrder> paidUserOrderList = new ArrayList<>();
        try(final Statement statement = connection.createStatement()){
            final ResultSet resultSet = statement.executeQuery(SQL_FIND_PAID_USER_ORDER_BY_ID);
            while (resultSet.next()){
               final PaidUserOrder paidUserOrder = new PaidUserOrder.Builder().
                withId(resultSet.getInt(1)).
                        withUserOrder(new UserOrder.Builder().
                                withId(resultSet.getInt(2)).
                                withOrderFinalPrice(resultSet.getBigDecimal(3)).
                                withUser(new User.Builder().
                                        withId(resultSet.getInt(4)).
                                        withLogin(resultSet.getString(5)).
                                        withPassword(resultSet.getString(6)).
                                        withRole(Role.valueOf(resultSet.getString(7))).
                                        withFirstName(resultSet.getString(8)).
                                        withLastName(resultSet.getString(9)).
                                        withBannedStatus(resultSet.getBoolean(10)).build()).
                                withOrderFinalPrice(resultSet.getBigDecimal(11)).
                                build()).
                        withDatePayed(resultSet.getDate(12)).
                        build();
                    paidUserOrderList.add(paidUserOrder);
            }
        }catch (SQLException e){
            LOG.error("Cannot find all PaidUserOrders",e);
            throw new DaoException("Cannot find all PaidUserOrders",e);
        }
        LOG.info("Cannot find all PaidUserOrders");
        return Collections.emptyList();
    }

    @Override
    public Optional<PaidUserOrder> findEntityById(Integer id) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_PAID_USER_ORDER_BY_ID)){
            preparedStatement.setInt(1,id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return Optional.of(new PaidUserOrder.Builder().
                        withId(resultSet.getInt(1)).
                        withUserOrder(new UserOrder.Builder().
                                withId(resultSet.getInt(2)).
                                withOrderFinalPrice(resultSet.getBigDecimal(3)).
                                withUser(new User.Builder().
                                        withId(resultSet.getInt(4)).
                                        withLogin(resultSet.getString(5)).
                                        withPassword(resultSet.getString(6)).
                                        withRole(Role.valueOf(resultSet.getString(7))).
                                        withFirstName(resultSet.getString(8)).
                                        withLastName(resultSet.getString(9)).
                                        withBannedStatus(resultSet.getBoolean(10)).build()).
                                withOrderFinalPrice(resultSet.getBigDecimal(11)).
                                build()).
                        withDatePayed(resultSet.getDate(12)).
                        build());
            }
        }catch (SQLException e){
            LOG.error("Cannot find PaidUserOrder by id",e);
            throw new DaoException("Cannot find PaidUserOrder by id",e);
        }
        LOG.info("Cannot find paid PaidUserOrder by id");
        return Optional.empty();
    }

    @Override
    public PaidUserOrder update(PaidUserOrder entity) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_PAID_USER_ORDER_BY_ID)){
            preparedStatement.setInt(1,entity.getUserOrder().getId());
            preparedStatement.setDate(2,entity.getDatePayed());
            preparedStatement.setInt(3,entity.getId());
            final int countUpdatedRows = preparedStatement.executeUpdate();
            if(countUpdatedRows>0){
                return entity;
            }

        }catch (SQLException e){
            LOG.error("Cannot update PaidUSerOrder by id",e);
            throw new DaoException("Cannot update PaidUSerOrder by id",e);
        }
        LOG.error("Cannot update PaidUSerOrder by id");
        throw new DaoException("Cannot update PaidUSerOrder by id");
    }

    @Override
    public boolean delete(PaidUserOrder entity) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_PAID_USER_ORDER_BY_ID)){
            preparedStatement.setInt(1,entity.getId());
            final int countDeletedRows = preparedStatement.executeUpdate();
            return countDeletedRows>0;
        }catch (SQLException e){
            LOG.error("Cannot delete PaidUserOrder by id",e);
            throw new DaoException("Cannot delete PaidUserOrder by id",e);
        }
    }
}
