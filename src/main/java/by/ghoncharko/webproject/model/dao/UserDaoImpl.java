package by.ghoncharko.webproject.model.dao;


import by.ghoncharko.webproject.entity.Role;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class UserDaoImpl implements UserDao {
    private static final Logger LOG = LogManager.getLogger(UserDaoImpl.class);
    private static final String SQL_CREATE_USER = "INSERT INTO user" +
            " (login, password, role_id, first_name, last_name)" +
            " VALUES (?,?,?,?,?)";
    private static final String SQL_FIND_ALL_USERS_LIMIT_OFFSET_PAGINATION = "SELECT" +
            " user.id,login,password,role_id,first_name,last_name, role.id,role.role_name " +
            " FROM user " +
            " INNER JOIN role ON user.role_id = role.id" +
            " LIMIT ? OFFSET ?";
    private static final String SQL_FIND_ALL_USERS_COUNT = "SELECT" +
            " COUNT(id) FROM user";
    private static final String SQL_FIND_ALL_USERS_AS_CLIENT_LIMIT_OFFSET_PAGINATION = "SELECT" +
            " user.id,login,password,first_name,last_name, role.id,role.role_name " +
            " FROM user " +
            " INNER JOIN role ON user.role_id = role.id" +
            " WHERE user.role_id = 1" +
            " LIMIT ? OFFSET ?";
    private static final String SQL_FIND_ALL_USERS_AS_CLIENT_COUNT = "SELECT" +
            " COUNT(id) FROM user";
    private static final String SQL_FIND_USER_BY_ID = "SELECT" +
            " user.id,login,password,role_id,first_name,last_name, role.id,role.role_name" +
            " FROM user" +
            " INNER JOIN role ON user.role_id = role.id" +
            " WHERE user.id = ?";
    private static final String SQL_UPDATE_USER = "UPDATE user" +
            " SET login = ?, password = ?, role_id = ?, first_name = ?, last_name = ? " +
            " WHERE id = ?";
    private static final String SQL_DELETE_USER = "DELETE FROM user" +
            " WHERE id=?";

    private static final String SQL_FIND_USER_BY_LOGIN = "SELECT" +
            " user.id, login, password, first_name, last_name, role.id, role.role_name" +
            " FROM user" +
            " INNER JOIN role ON user.role_id = role.id" +
            " WHERE login = ?";
    private final Connection connection;
    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User create(User entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_CREATE_USER, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getLogin());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setInt(3, entity.getRole().getId());
            preparedStatement.setString(4, entity.getFirstName());
            preparedStatement.setString(5, entity.getLastName());
            int countRows = preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (countRows > 0 && resultSet.next()) {
                return new User.Builder().
                        withId(resultSet.getInt(1)).
                        withLogin(entity.getLogin()).
                        withPassword(entity.getPassword()).
                        withRole(entity.getRole()).
                        withFirstName(entity.getFirstName()).
                        withLastName(entity.getLastName()).
                        build();
            }
        } catch (SQLException e) {
            LOG.error("cannot create user", e);
            throw new DaoException("cannot create user", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        LOG.error("cannot create user");
        throw new DaoException("cannot create user");
    }

    @Override
    public List<User> findAllUsersLimitOffsetPagination(Integer limit, Integer offset) throws DaoException {
        List<User> userList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_ALL_USERS_LIMIT_OFFSET_PAGINATION);
            preparedStatement.setInt(1,limit);
            preparedStatement.setInt(2,offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User.Builder().
                        withId(resultSet.getInt(1)).
                        withLogin(resultSet.getString(2)).
                        withPassword(resultSet.getString(3)).
                        withRole(new Role.Builder().
                                withId(resultSet.getInt(4)).
                                withRoleName(resultSet.getString(5)).
                                build()).
                        withFirstName(resultSet.getString(6)).
                        withLastName(resultSet.getString(7)).
                        build();
                userList.add(user);
            }
        } catch (SQLException e) {
            LOG.error("cannot find all users", e);
            throw new DaoException("cannot find all users", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        return userList;
    }

    @Override
    public int findAllUsersCount() throws DaoException {
        Statement statement = null;
        try{
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_USERS_COUNT);
            if(resultSet.next()){
                return resultSet.getInt(1);
            }
        }catch (SQLException e){

        }finally {
            Dao.closeStatement(statement);
        }
        throw new DaoException();
    }

    @Override
    public List<User> findAll() throws DaoException {
        List<User> userList = new ArrayList<>();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_USERS_LIMIT_OFFSET_PAGINATION);
            while (resultSet.next()) {
                User user = new User.Builder().
                        withId(resultSet.getInt(1)).
                        withLogin(resultSet.getString(2)).
                        withPassword(resultSet.getString(3)).
                        withRole(new Role.Builder().
                                withId(resultSet.getInt(4)).
                                withRoleName(resultSet.getString(5)).
                                build()).
                        withFirstName(resultSet.getString(6)).
                        withLastName(resultSet.getString(7)).
                        build();
                userList.add(user);
            }
        } catch (SQLException e) {
            LOG.error("cannot find all users", e);
            throw new DaoException("cannot find all users", e);
        } finally {
            Dao.closeStatement(statement);
        }
        return userList;
    }

    @Override
    public List<User> findAllClients() throws DaoException {
        List<User> userList = new ArrayList<>();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_USERS_AS_CLIENT_LIMIT_OFFSET_PAGINATION);
            while (resultSet.next()) {
                User user = new User.Builder().
                        withId(resultSet.getInt(1)).
                        withLogin(resultSet.getString(2)).
                        withPassword(resultSet.getString(3)).
                        withFirstName(resultSet.getString(4)).
                        withLastName(resultSet.getString(5)).
                        withRole(new Role.Builder().
                                withId(resultSet.getInt(6)).
                                withRoleName(resultSet.getString(7)).
                                build()).
                        build();
                userList.add(user);
            }
        } catch (SQLException e) {
            LOG.error("cannot find all users", e);
            throw new DaoException("cannot find all users", e);
        } finally {
            Dao.closeStatement(statement);
        }
        return userList;
    }

    @Override
    public List<User> findAllClientsLimitOffsetPagination(Integer limit, Integer offset) throws DaoException {
        List<User> userList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_ALL_USERS_AS_CLIENT_LIMIT_OFFSET_PAGINATION);
            preparedStatement.setInt(1,limit);
            preparedStatement.setInt(2,offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User.Builder().
                        withId(resultSet.getInt(1)).
                        withLogin(resultSet.getString(2)).
                        withPassword(resultSet.getString(3)).
                        withFirstName(resultSet.getString(4)).
                        withLastName(resultSet.getString(5)).
                        withRole(new Role.Builder().
                                withId(resultSet.getInt(6)).
                                withRoleName(resultSet.getString(7)).
                                build()).
                        build();
                userList.add(user);
            }
        } catch (SQLException e) {
            LOG.error("cannot find all users", e);
            throw new DaoException("cannot find all users", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        return userList;
    }

    @Override
    public int findAllClientsCount() throws DaoException {
        Statement statement = null;
        try{
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_USERS_AS_CLIENT_COUNT);
            if(resultSet.next()){
                return resultSet.getInt(1);
            }
        }catch (SQLException e){

        }finally {
            Dao.closeStatement(statement);
        }
        throw new DaoException();
    }

    @Override
    public Optional<User> findEntityById(Integer id) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_USER_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new User.Builder().
                        withId(resultSet.getInt(1)).
                        withLogin(resultSet.getString(2)).
                        withPassword(resultSet.getString(3)).
                        withRole(new Role.Builder().
                                withId(resultSet.getInt(4)).
                                withRoleName(resultSet.getString(5)).
                                build()).
                        withFirstName(resultSet.getString(6)).
                        withLastName(resultSet.getString(7)).
                        build());
            }
        } catch (SQLException e) {
            LOG.error("cannot find user by id", e);
            throw new DaoException("cannot find all users", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        return Optional.empty();
    }

    @Override
    public User update(User entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_USER);
            preparedStatement.setString(1, entity.getLogin());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setInt(3, entity.getRole().getId());
            preparedStatement.setString(4, entity.getFirstName());
            preparedStatement.setString(5, entity.getLastName());
            preparedStatement.setInt(6, entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return entity;
            }
        } catch (SQLException e) {
            LOG.error("cannot update user", e);
            throw new DaoException("cannot update user", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        LOG.error("cannot update user");
        throw new DaoException("cannot update user");
    }

    @Override
    public boolean delete(User entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_DELETE_USER);
            preparedStatement.setInt(1, entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            LOG.error("cannot delete user", e);
            throw new DaoException("cannot delete user", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        return false;
    }
    @Override
    public Optional<User> findUserByLogin(String login) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_USER_BY_LOGIN);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new User.Builder().
                        withId(resultSet.getInt(1)).
                        withLogin(resultSet.getString(2)).
                        withPassword(resultSet.getString(3)).
                        withFirstName(resultSet.getString(4)).
                        withLastName(resultSet.getString(5)).
                        withRole(new Role.Builder().
                                withId(resultSet.getInt(6)).
                                withRoleName(resultSet.getString(7)).build()).
                        build());
            }
        } catch (SQLException e) {
            LOG.error("cannot find user by login", e);
            throw new DaoException("cannot find user by login", e);
        } finally {
            Dao.closeStatement(preparedStatement);
        }
        return Optional.empty();
    }
}
