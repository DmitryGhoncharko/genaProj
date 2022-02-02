package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.Role;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.entity.UserOrder;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserOrderDaoImpl implements UserOrderDao{
    private static final Logger LOG = LogManager.getLogger(UserOrderDaoImpl.class);
    private static final String SQL_CREATE_USER_ORDER = "INSERT INTO user_order(user_id, order_final_price) VALUES (?,?)";
    private static final String SQL_FIND_ALL_USER_ORDERS = "SELECT user_order.id, u.id, u.login, u.password, r.role_name, u.first_name, u.last_name, u.is_banned , order_final_price" +
            " FROM  user_order" +
            " INNER JOIN user u ON user_order.user_id = u.id" +
            " INNER JOIN role r on u.role_id = r.id";
    private static final String SQL_FIND_USER_ORDER_BY_ID = "SELECT user_order.id, u.id, u.login, u.password, r.role_name, u.first_name, u.last_name, u.is_banned , order_final_price" +
            " FROM  user_order" +
            " INNER JOIN user u ON user_order.user_id = u.id" +
            " INNER JOIN role r ON u.role_id = r.id  " +
            " WHERE id = ?";
    private static final String SQL_UPDATE_USER_ORDER_BY_ID = "UPDATE user_order SET user_id = ?, order_final_price = ?" +
            " WHERE id = ?";
    private static final String SQL_DELETE_USER_ORDER_BY_ID = "DELETE FROM user_order WHERE  id = ?";
    private final Connection connection;

    public UserOrderDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UserOrder create(UserOrder entity) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_USER_ORDER, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt(1,entity.getUser().getId());
            preparedStatement.setDouble(2,entity.getOrderFinalPrice().doubleValue());
            final int countCreatedRows = preparedStatement.executeUpdate();
            if(countCreatedRows>0){
                final ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()){
                    return new UserOrder.Builder().
                            withId(resultSet.getInt(1)).
                            withUser(entity.getUser()).
                            withOrderFinalPrice(entity.getOrderFinalPrice()).
                            build();
                }
            }
        }catch (SQLException e){
            LOG.error("Cannot create user order",e);
            throw new DaoException("Cannot create user order",e);
        }
        LOG.error("Cannot create user order");
        throw new DaoException("Cannot create user order");
    }

    @Override
    public List<UserOrder> findAll() throws DaoException {
       final List<UserOrder> userOrderList = new ArrayList<>();
        try(final Statement statement = connection.createStatement()){
          final ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_USER_ORDERS);
          while (resultSet.next()){
              final UserOrder userOrder = new UserOrder.Builder().
                      withId(resultSet.getInt(1)).
                      withUser(new User.Builder().
                              withId(resultSet.getInt(2)).
                              withLogin(resultSet.getString(3)).
                              withPassword(resultSet.getString(4)).
                              withRole(Role.valueOf(resultSet.getString(5))).
                              withFirstName(resultSet.getString(6)).
                              withLastName(resultSet.getString(7)).
                              withBannedStatus(resultSet.getBoolean(8)).
                              build()
                      ).
                      withOrderFinalPrice(resultSet.getBigDecimal(9)).
                      build();
          }
       }catch (SQLException e){
           LOG.error("Cannot find all user orders", e);
           throw new DaoException("Cannot find all user orders", e);
       }
        return userOrderList;
    }

    @Override
    public Optional<UserOrder> findEntityById(Integer id) throws DaoException {
       try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USER_ORDER_BY_ID)){
           preparedStatement.setInt(1,id);
           final ResultSet resultSet = preparedStatement.executeQuery();
           if(resultSet.next()){
               return Optional.of(new UserOrder.Builder().
                       withId(resultSet.getInt(1)).
                       withUser(new User.Builder().
                               withId(resultSet.getInt(2)).
                               withLogin(resultSet.getString(3)).
                               withPassword(resultSet.getString(4)).
                               withRole(Role.valueOf(resultSet.getString(5))).
                               withFirstName(resultSet.getString(6)).
                               withLastName(resultSet.getString(7)).
                               withBannedStatus(resultSet.getBoolean(8)).
                               build()
                       ).
                      withOrderFinalPrice(resultSet.getBigDecimal(9)).
                       build());
           }
       }catch (SQLException e){
           LOG.error("Cannot find user order by od",e);
           throw new DaoException("Cannot find user order by od",e);
       }
       LOG.info("Cannot find user order by id");
       return Optional.empty();
    }

    @Override
    public UserOrder update(UserOrder entity) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_USER_ORDER_BY_ID)){
            preparedStatement.setInt(1,entity.getUser().getId());
            preparedStatement.setBigDecimal(2,entity.getOrderFinalPrice());
            preparedStatement.setInt(3,entity.getId());
            final  int countRowsUpdated = preparedStatement.executeUpdate();
            if(countRowsUpdated>0){
                return entity;
            }
        }catch (SQLException e){
            LOG.error("Cannot update user order by id",e);
            throw new DaoException("Cannot update user order by id",e);
        }
        LOG.error("Cannot update user order by id");
        throw new DaoException("Cannot update user order by id");
    }

    @Override
    public boolean delete(UserOrder entity) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_USER_ORDER_BY_ID)){
            preparedStatement.setInt(1,entity.getId());
            final int countRowsDeleted = preparedStatement.executeUpdate();
            return countRowsDeleted>0;
        }catch (SQLException e){
            LOG.error("Cannot delete user order by id",e);
            throw new DaoException("Cannot delete user order by id",e);
        }
    }
}
