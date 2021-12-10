package by.ghoncharko.webproject.model.service;


import by.ghoncharko.webproject.entity.RolesHolder;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.UserDao;
import by.ghoncharko.webproject.model.dao.UserDaoImpl;
import by.ghoncharko.webproject.validator.ValidateLogin;
import by.ghoncharko.webproject.validator.ValidateRegistration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);
    private static final String SALT = BCrypt.gensalt();
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private UserServiceImpl() {

    }

    @Override
    public List<User> findAll() {
        final Connection connection = connectionPool.getConnection();
        try {
            UserDao userDao = new UserDaoImpl(connection);
            return userDao.findAll();
        } catch (DaoException e) {
            LOG.error("DaoException", e);
            return Collections.emptyList();
        } finally {
            Service.connectionClose(connection);
        }
    }

    @Override
    public List<User> findAllClients() {
        final Connection connection = connectionPool.getConnection();
        final UserDao userDao = new UserDaoImpl(connection);
        try {
            return userDao.findAllClients();
        } catch (DaoException e) {
            LOG.error("DaoException", e);
        } finally {
            Service.connectionClose(connection);
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<User> authenticate(String login, String password) throws ServiceException {
        final Connection connection = connectionPool.getConnection();
        final boolean loginAndPasswordValide = ValidateLogin.getInstance().validate(login, password);
        if (loginAndPasswordValide) {
            try {
                final UserDao userDao = new UserDaoImpl(connection);
                final Optional<User> user = userDao.findUserByLogin(login);
                if (user.isPresent()) {
                    final String userPasswordFromDB = user.get().getPassword();
                    if (BCrypt.checkpw(password, userPasswordFromDB)) {
                        return user;
                    }
                }
            } catch (DaoException e) {
                LOG.error("Cannot authenticate user", e);
                throw new ServiceException("Cannot authenticate user", e);
            } finally {
                Service.connectionClose(connection);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> createClient(String login, String password, String firstName, String lastName) throws ServiceException {
        final Connection connection = connectionPool.getConnection();
        final boolean isValideData = ValidateRegistration.getInstance().validateRegistration(login, password, firstName, lastName);
        if (isValideData) {
            try {
                final String hashedPassword = BCrypt.hashpw(password, SALT);
                final User user = new User.Builder().
                        withLogin(login).
                        withPassword(hashedPassword).
                        withFirstName(firstName).
                        withLastName(lastName).
                        withRole(RolesHolder.CLIENT).build();
                final UserDao userDao = new UserDaoImpl(connection);
                final User userWithId = userDao.create(user);
                return Optional.of(userWithId);
            } catch (DaoException e) {
                LOG.error("Cannot create user as client", e);
                throw new ServiceException("Cannot create user as client", e);
            } finally {
                Service.connectionClose(connection);
            }
        }
        return Optional.empty();
    }

    static UserServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        private static final UserServiceImpl INSTANCE = new UserServiceImpl();
    }
}
