package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.Role;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao{
    private static final Logger LOG = LogManager.getLogger(UserDaoImpl.class);
    private static final String SQL_CREATE_USER = "INSERT INTO user(login, password, role_id, first_name, last_name, banned) VALUES (?,?,?,?,?,?)";
    private static final String SQL_FIND_ALL_USERS = "SELECT user.id,login, password, r.role_name, first_name, last_name, banned" +
            " FROM user" +
            " INNER JOIN role r on user.role_id = r.id";
    private static final String SQL_FIND_USER_BY_ID = "SELECT user.id,login, password, r.role_name, first_name, last_name, banned" +
            " FROM user" +
            " INNER JOIN role r on user.role_id = r.id" +
            " WHERE user.id = ?";
    private static final String SQL_UPDATE_USER_BY_ID  = "UPDATE user SET login = ?, password = ?, role_id = ?, first_name = ?, last_name = ?, banned = ?" +
            " WHERE id = ?";
    private static final String SQL_DELETE_USER_BY_ID = "DELETE FROM user WHERE id  = ?";
    private final Connection connection;

    public UserDaoImpl(Connection connection) {
       this.connection = connection;
    }

    @Override
    public User create(User entity) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_USER, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1,entity.getLogin());
            preparedStatement.setString(2,entity.getPassword());
            preparedStatement.setInt(3,entity.getRole().ordinal());
            preparedStatement.setString(4,entity.getFirstName());
            preparedStatement.setString(5,entity.getLastName());
            preparedStatement.setBoolean(6,entity.getBanned());
            final int countRowsUpdated = preparedStatement.executeUpdate();
            if(countRowsUpdated>0){
                final ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()){
                    return new User.Builder().
                            withId(resultSet.getInt(1)).
                            withLogin(entity.getLogin()).
                            withPassword(entity.getPassword()).
                            withRole(entity.getRole()).
                            withFirstName(entity.getFirstName()).
                            withLastName(entity.getLastName()).
                            withBannedStatus(entity.getBanned()).
                            build();
                }
            }
        }catch (SQLException e){
            LOG.error("Cannot create user",e);
            throw new DaoException("Cannot create user", e);
        }
        LOG.error("Cannot create user");
        throw new DaoException("Cannot create user");
    }

    @Override
    public List<User> findAll() throws DaoException {
        final List<User> userList = new ArrayList<>();
        try(final Statement statement = connection.createStatement()){
            final ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_USERS);
            while (resultSet.next()){
                final User user = new User.Builder().
                        withId(resultSet.getInt(1)).
                        withLogin(resultSet.getString(2)).
                        withRole(Role.valueOf(resultSet.getString(3))).
                        withFirstName(resultSet.getString(4)).
                        withLastName(resultSet.getString(5)).
                        withBannedStatus(resultSet.getBoolean(6)).
                        build();
                userList.add(user);
            }
        }catch (SQLException e){
            LOG.error("Cannot find all users",e);
            throw new DaoException("Cannot find all users",e);
        }
        return userList;
    }

    @Override
    public Optional<User> findEntityById(Integer id) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USER_BY_ID)){
            preparedStatement.setInt(1,id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return Optional.of(new User.Builder().
                        withId(resultSet.getInt(1)).
                        withLogin(resultSet.getString(2)).
                        withRole(Role.valueOf(resultSet.getString(3))).
                        withFirstName(resultSet.getString(4)).
                        withLastName(resultSet.getString(5)).
                        withBannedStatus(resultSet.getBoolean(6)).
                        build());
            }
        }catch (SQLException e){
            LOG.error("Cannot find user by id",e);
            throw new DaoException("Cannot find user by id",e);
        }
        LOG.info("Cannot find user by id");
        return Optional.empty();
    }

    @Override
    public User update(User entity) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_USER_BY_ID)){
            preparedStatement.setString(1,entity.getLogin());
            preparedStatement.setString(2,entity.getPassword());
            preparedStatement.setInt(3,entity.getRole().ordinal());
            preparedStatement.setString(4,entity.getFirstName());
            preparedStatement.setString(5,entity.getLastName());
            preparedStatement.setBoolean(6,entity.getBanned());
            preparedStatement.setInt(7,entity.getId());
            final int countUpdatedRows = preparedStatement.executeUpdate();
            if(countUpdatedRows>0){
                return entity;
            }
        }catch (SQLException e){
            LOG.error("Cannot update user by id",e);
            throw new DaoException("Cannot update user by id",e);
        }
        LOG.error("Cannot update user by id");
        throw new DaoException("Cannot update user by id");
    }

    @Override
    public boolean delete(User entity) throws DaoException {
        try(final PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_USER_BY_ID)){
            preparedStatement.setInt(1,entity.getId());
            final int countDeletedRows = preparedStatement.executeUpdate();
            return countDeletedRows>0;
        }catch (SQLException e){
            LOG.error("Cannot delete user by id",e);
            throw new DaoException("Cannot delete user by id",e);
        }
    }
}
