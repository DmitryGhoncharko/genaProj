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

public class UserDaoImpl extends AbstractDao<User> implements UserDao {
    private static final Logger LOG = LogManager.getLogger(UserDaoImpl.class);
    private static final String SQL_CREATE_USER = "INSERT INTO user(login, password, role_id, first_name, last_name, is_banned) VALUES (?,?,?,?,?,?)";
    private static final String SQL_FIND_ALL_USERS = "SELECT user.id,login, password, r.role_name, first_name, last_name, is_banned" +
            " FROM user" +
            " INNER JOIN role r on user.role_id = r.id";
    private static final String SQL_FIND_ALL_USERS_AS_CLIENTS_WHERE_USERS_NOT_BANNED = "SELECT user.id,login, password, r.role_name, first_name, last_name, is_banned" +
            " FROM user" +
            " INNER JOIN role r on user.role_id = r.id" +
            " WHERE user.role_id = 0 AND user.is_banned = false";
    private static final String SQL_FIND_USER_BY_ID = "SELECT user.id,login, password, r.role_name, first_name, last_name, is_banned" +
            " FROM user" +
            " INNER JOIN role r on user.role_id = r.id" +
            " WHERE user.id = ?";
    private static final String SQL_UPDATE_USER_BY_ID = "UPDATE user SET login = ?, password = ?, role_id = ?, first_name = ?, last_name = ?, is_banned = ?" +
            " WHERE id = ?";

    private static final String SQL_FIND_USER_BY_LOGIN = "SELECT user.id,login, password, r.role_name, first_name, last_name, is_banned" +
            " FROM user" +
            " INNER JOIN role r on user.role_id = r.id" +
            " WHERE login = ?";
    private static final String SQL_DELETE_USER_BY_ID = "DELETE FROM user WHERE id  = ?";

    public UserDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public User create(User entity) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_USER, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getLogin());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setInt(3, entity.getRole().ordinal());
            preparedStatement.setString(4, entity.getFirstName());
            preparedStatement.setString(5, entity.getLastName());
            preparedStatement.setBoolean(6, entity.getBanned());
            final int countRowsUpdated = preparedStatement.executeUpdate();
            if (countRowsUpdated > 0) {
                final ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
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
        } catch (SQLException e) {
            LOG.error("Cannot create user", e);
            throw new DaoException("Cannot create user", e);
        }
        LOG.error("Cannot create user");
        throw new DaoException("Cannot create user");
    }

    @Override
    public List<User> findAll() throws DaoException {
        final List<User> userList = new ArrayList<>();
        try (final Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_USERS);
            while (resultSet.next()) {
                final User user = extractEntity(resultSet);
                userList.add(user);
            }
        } catch (SQLException e) {
            LOG.error("Cannot find all users", e);
            throw new DaoException("Cannot find all users", e);
        }
        return userList;
    }

    @Override
    public List<User> findAllNotBannedUsersAsClient() throws DaoException {
        final List<User> userList = new ArrayList<>();
        try (final Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_USERS_AS_CLIENTS_WHERE_USERS_NOT_BANNED);
            while (resultSet.next()) {
                final User user = extractEntity(resultSet);
                userList.add(user);
            }
        } catch (SQLException e) {
            LOG.error("Cannot find all users", e);
            throw new DaoException("Cannot find all users", e);
        }
        return userList;
    }

    @Override
    public Optional<User> findEntityById(Integer id) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USER_BY_ID)) {
            preparedStatement.setInt(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(extractEntity(resultSet));
            }
        } catch (SQLException e) {
            LOG.error("Cannot find user by id", e);
            throw new DaoException("Cannot find user by id", e);
        }
        LOG.info("Cannot find user by id");
        return Optional.empty();
    }

    @Override
    public Optional<User> findUserByLogin(String login) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USER_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(extractEntity(resultSet));
            }
        } catch (SQLException e) {
            LOG.error("Cannot find user by login", e);
            throw new DaoException("Cannot find user by login", e);
        }
        return Optional.empty();
    }

    @Override
    public User update(User entity) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_USER_BY_ID)) {
            preparedStatement.setString(1, entity.getLogin());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setInt(3, entity.getRole().ordinal());
            preparedStatement.setString(4, entity.getFirstName());
            preparedStatement.setString(5, entity.getLastName());
            preparedStatement.setBoolean(6, entity.getBanned());
            preparedStatement.setInt(7, entity.getId());
            final int countUpdatedRows = preparedStatement.executeUpdate();
            if (countUpdatedRows > 0) {
                return entity;
            }
        } catch (SQLException e) {
            LOG.error("Cannot update user by id", e);
            throw new DaoException("Cannot update user by id", e);
        }
        LOG.error("Cannot update user by id");
        throw new DaoException("Cannot update user by id");
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_USER_BY_ID)) {
            return deleteBillet(preparedStatement, id);
        } catch (SQLException e) {
            LOG.error("Cannot delete user by id", e);
            throw new DaoException("Cannot delete user by id", e);
        }
    }

    @Override
    protected User extractEntity(ResultSet resultSet) throws SQLException {
        return new User.Builder().
                withId(resultSet.getInt(1)).
                withLogin(resultSet.getString(2)).
                withPassword(resultSet.getString(3)).
                withRole(Role.valueOf(resultSet.getString(4))).
                withFirstName(resultSet.getString(5)).
                withLastName(resultSet.getString(6)).
                withBannedStatus(resultSet.getBoolean(7)).
                build();
    }
}
