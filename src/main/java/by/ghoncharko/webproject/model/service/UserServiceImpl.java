package by.ghoncharko.webproject.model.service;


import by.ghoncharko.webproject.entity.RolesHolder;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.UserDao;
import by.ghoncharko.webproject.model.dao.UserDaoImpl;
import by.ghoncharko.webproject.validator.ValidateAuthenticate;
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
    private UserServiceImpl(){

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
    public Optional<User> authenticate(String login, String password) {
        final Connection connection = connectionPool.getConnection();
        boolean loginAndPasswordValide = ValidateAuthenticate.getInstance().validate(login, password);
        if (loginAndPasswordValide) {
            try {
                final UserDao userDao = new UserDaoImpl(connection);
                Optional<User> user = userDao.findUserByLogin(login);
                if (user.isPresent()) {
                    String userPasswordFromDB = user.get().getPassword();
                    if (BCrypt.checkpw(password, userPasswordFromDB)) {
                        return user;
                    }
                }
            } catch (DaoException e) {
                e.printStackTrace();
            } finally {
                Service.connectionClose(connection);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> createClient(String login, String password, String firstName, String lastName) {
        Connection connection = connectionPool.getConnection();
        boolean isValide = ValidateRegistration.getInstance().validateRegistration(login, password, firstName, lastName);
        if(isValide){
            try {
                String hashedPassword = BCrypt.hashpw(password, SALT);
                User user = new User.Builder().
                        withLogin(login).
                        withPassword(hashedPassword).
                        withFirstName(firstName).
                        withLastName(lastName).
                        withRole(RolesHolder.CLIENT).build();
                UserDao userDao = new UserDaoImpl(connection);
                User userWithId = userDao.create(user);
                return Optional.of(userWithId);
            } catch (DaoException e) {
                LOG.error("DaoException", e);
                return Optional.empty();
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
