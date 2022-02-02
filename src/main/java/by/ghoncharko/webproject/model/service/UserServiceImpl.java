package by.ghoncharko.webproject.model.service;



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
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);
    private static final String SALT = BCrypt.gensalt();
    private static final int LIMIT = 10;
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private UserServiceImpl() {

    }

    @Override
    public List<User> findAll() throws ServiceException {
        final Connection connection = connectionPool.getConnection();
        final UserDao userDao = new UserDaoImpl(connection);
        try {
            return userDao.findAll();
        } catch (DaoException e) {
            LOG.error("cannot find all users",e);
            throw new ServiceException("cannot find all users",e);
        } finally {
            Service.connectionClose(connection);
        }
    }

    @Override
    public List<User> findAllLimitOffsetPagination(Integer currentPage) throws ServiceException {
        final Connection connection = connectionPool.getConnection();
        final UserDao userDao = new UserDaoImpl(connection);
        try {
            int offset = LIMIT*(currentPage-1);
            return userDao.findAllUsersLimitOffsetPagination(LIMIT, offset);
        } catch (DaoException e) {
            LOG.error("cannot find all users",e);
            throw new ServiceException("cannot find all users",e);
        } finally {
            Service.connectionClose(connection);
        }
    }

    @Override
    public int findMaxPagesCountForAllUsers() throws ServiceException {
        final Connection connection = connectionPool.getConnection();
        UserDao userDao = new UserDaoImpl(connection);
        try{
            int allUsersCount = userDao.findAllUsersCount();
            return allUsersCount/LIMIT;
        }catch (DaoException e){

        }finally {
            Service.connectionClose(connection);
        }
        throw new ServiceException();
    }

    @Override
    public List<User> findAllClientsLimitOffsetPagination(Integer currentPageNumber) throws ServiceException {
        final Connection connection = connectionPool.getConnection();
        final UserDao userDao = new UserDaoImpl(connection);
        try {
            int offset = LIMIT*(currentPageNumber-1);
            return userDao.findAllClientsLimitOffsetPagination(LIMIT,offset);
        } catch (DaoException e) {
            LOG.error("DaoException", e);
            throw new ServiceException("Cannot find all clients",e);
        } finally {
            Service.connectionClose(connection);
        }

    }

    @Override
    public int findMaxPagesForUsersAsClients() throws ServiceException {
        final Connection connection = connectionPool.getConnection();
        final UserDao userDao = new UserDaoImpl(connection);
        try{
         int allClientsCount =   userDao.findAllClientsCount();
         return allClientsCount/LIMIT;
        }catch (DaoException e){

        }finally {
            Service.connectionClose(connection);
        }
        throw new ServiceException();
    }

    @Override
    public Optional<User> authenticate(String login, String password) throws ServiceException {
        final boolean loginAndPasswordValide = ValidateLogin.getInstance().validate(login, password);
        if (!loginAndPasswordValide) {
            return Optional.empty();
        }
        try(final Connection connection = connectionPool.getConnection()){
            final UserDao userDao = new UserDaoImpl(connection);
            final Optional<User> user = userDao.findUserByLogin(login);
            if (user.isPresent()) {
                final String userPasswordFromDB = user.get().getPassword();
                if (BCrypt.checkpw(password, userPasswordFromDB)) {
                    return user;
                }
            }
        }catch (DaoException e){

        }
    }

    @Override
    public Optional<User> createClient(String login, String password, String firstName, String lastName) throws ServiceException {
        final Connection connection = connectionPool.getConnection();
        final boolean isValidData = ValidateRegistration.getInstance().validateRegistration(login, password, firstName, lastName);
        if (isValidData) {
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
