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


public class UserDaoImpl extends AbstractDao<User> {
    private static final Logger LOG = LogManager.getLogger(UserDaoImpl.class);
    private static final String SQL_CREATE_USER = "INSERT INTO user" +
            " (login, password, role_id, first_name, last_name)" +
            " VALUES (?,?,?,?,?)";
    private static final String SQL_FIND_ALL_USERS = "SELECT" +
            " user.id,login,password,role_id,first_name,last_name, role.id,role.role_name " +
            " FROM user " +
            " INNER JOIN role ON user.role_id = role.id";
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

    public UserDaoImpl(Connection connection) {
        super(connection);
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
            close(preparedStatement);
        }
        LOG.error("cannot create user");
        throw new DaoException("cannot create user");
    }

    @Override
    public List<User> findAll() throws DaoException {
        List<User> userList = new ArrayList<>();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_USERS);
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
            close(statement);
        }
        return userList;
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
            close(preparedStatement);
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
            close(preparedStatement);
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
            close(preparedStatement);
        }
        return false;
    }

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
            close(preparedStatement);
        }
        return Optional.empty();
    }
}
