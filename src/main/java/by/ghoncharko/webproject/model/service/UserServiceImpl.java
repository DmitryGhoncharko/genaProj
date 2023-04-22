package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.Role;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.UserDao;
import by.ghoncharko.webproject.model.dao.UserDaoImpl;
import by.ghoncharko.webproject.security.BcryptWithSaltHasherImpl;
import by.ghoncharko.webproject.validator.UserServiceValidator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);
    private static final int LIMIT = 10;
    private final ConnectionPool connectionPool;
    private final UserServiceValidator userServiceValidator;



    @Override
    public List<User> findAll() throws ServiceException {
        return null;
    }

    @Override
    public List<User> findAllNotBannedUsersAsClients() {
        try (final Connection connection = connectionPool.getConnection()) {
            final UserDao userDao = new UserDaoImpl(connection);
            return userDao.findAllNotBannedUsersAsClient();
        } catch (Exception e) {

        }
        return Collections.emptyList();
    }

    @Override
    public Optional<User> authenticate(String login, String password) throws ServiceException {
        if (!userServiceValidator.validateAuthenticate(login, password)) {
            return Optional.empty();
        }
        try (Connection connection = connectionPool.getConnection()) {

            final UserDao userDao = new UserDaoImpl(connection);
            final Optional<User> user = userDao.findUserByLogin(login);
            if (user.isPresent()) {
                final String userPasswordFromDB = user.get().getPassword();
                if (BcryptWithSaltHasherImpl.getInstance().checkIsEqualsPasswordAndPasswordHash(password, userPasswordFromDB)) {
                    return user;
                }
            }
        } catch (Exception e) {
            LOG.error("Cannot authenticate user", e);
            throw new ServiceException("Cannot authenticate user", e);
        }
        LOG.info("Cannot authenticate user");
        return Optional.empty();
    }

    @Override
    public Optional<User> createClientWithBannedStatusFalse(String login, String password, String firstName, String lastName) throws ServiceException {
        if (!userServiceValidator.validateCreateClientWithBannedStatusFalse(login, password, firstName, lastName)) {
            return Optional.empty();
        }

        try (final Connection connection = connectionPool.getConnection()) {
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
        } catch (Exception e) {
            LOG.error("Cannot create user as client", e);
            throw new ServiceException("Cannot create user as client", e);
        }
    }
}
