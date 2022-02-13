package by.ghoncharko.webproject.model.service;



import by.ghoncharko.webproject.entity.Role;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.UserDao;
import by.ghoncharko.webproject.model.dao.UserDaoImpl;
import by.ghoncharko.webproject.security.BcryptWithSaltHasherImpl;
import by.ghoncharko.webproject.validator.ValidateLogin;
import by.ghoncharko.webproject.validator.ValidateRegistration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);
    private static final int LIMIT = 10;
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private UserServiceImpl() {
    }

    @Override
    public List<User> findAll() throws ServiceException {
        return null;
    }

    @Override
    public Optional<User> authenticate(String login, String password) throws ServiceException {
        final boolean loginAndPasswordValid = ValidateLogin.getInstance().validate(login, password);
        if (!loginAndPasswordValid) {
            return Optional.empty();
        }
        final Connection connection = connectionPool.getConnection();
        try{
            final UserDao userDao = new UserDaoImpl(connection);
            final Optional<User> user = userDao.findUserByLogin(login);
            if (user.isPresent()) {
                final String userPasswordFromDB = user.get().getPassword();
                if (BcryptWithSaltHasherImpl.getInstance().checkIsEqualsPasswordAndPasswordHash(password,userPasswordFromDB)) {
                    return user;
                }
            }
        }catch (DaoException e){
            LOG.error("Cannot authenticate user",e);
            throw new ServiceException("Cannot authenticate user",e);
        }finally {
            Service.connectionClose(connection);
        }
        LOG.info("Cannot authenticate user");
        return Optional.empty();
    }

    @Override
    public Optional<User> createClientWithBannedStatusFalse(String login, String password, String firstName, String lastName) throws ServiceException {
        final boolean isValidData = ValidateRegistration.getInstance().validateRegistration(login, password, firstName, lastName);
        if (!isValidData) {
            return Optional.empty();
        }
        final Connection connection = connectionPool.getConnection();
        try {
                final String hashedPassword = BcryptWithSaltHasherImpl.getInstance().hashPassword(password);
                final User user = new User.Builder().
                        withLogin(login).
                        withPassword(hashedPassword).
                        withFirstName(firstName).
                        withLastName(lastName).
                        withRole(Role.CLIENT).
                        withBannedStatus(false).
                        build();
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

    static UserServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        private static final UserServiceImpl INSTANCE = new UserServiceImpl();
    }
}
