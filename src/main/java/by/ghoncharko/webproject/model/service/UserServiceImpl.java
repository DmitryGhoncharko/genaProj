package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.Role;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.dao.DaoHelper;
import by.ghoncharko.webproject.model.dao.DaoHelperFactory;
import by.ghoncharko.webproject.model.dao.UserDao;
import by.ghoncharko.webproject.security.BcryptWithSaltHasherImpl;
import by.ghoncharko.webproject.validator.UserServiceValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);
    private static final int LIMIT = 10;
    private final DaoHelperFactory daoHelperFactory;
    private final UserServiceValidator userServiceValidator;
    public UserServiceImpl(DaoHelperFactory daoHelperFactory, UserServiceValidator userServiceValidator) {
        this.daoHelperFactory = daoHelperFactory;
        this.userServiceValidator = userServiceValidator;
    }

    @Override
    public List<User> findAll() throws ServiceException {
        return null;
    }

    @Override
    public Optional<User> authenticate(String login, String password) throws ServiceException {
        if (!userServiceValidator.validateAuthenticate(login,password)) {
            return Optional.empty();
        }
        try(final DaoHelper daoHelper = daoHelperFactory.create()){

            final UserDao userDao = daoHelper.createUserDao();
            final Optional<User> user = userDao.findUserByLogin(login);
            if (user.isPresent()) {
                final String userPasswordFromDB = user.get().getPassword();
                if (BcryptWithSaltHasherImpl.getInstance().checkIsEqualsPasswordAndPasswordHash(password,userPasswordFromDB)) {
                    return user;
                }
            }
        }catch (Exception e){
            LOG.error("Cannot authenticate user",e);
            throw new ServiceException("Cannot authenticate user",e);
        }
        LOG.info("Cannot authenticate user");
        return Optional.empty();
    }

    @Override
    public Optional<User> createClientWithBannedStatusFalse(String login, String password, String firstName, String lastName) throws ServiceException {
        if (!userServiceValidator.validateCreateClientWithBannedStatusFalse(login,password,firstName,lastName)) {
            return Optional.empty();
        }

        try(final DaoHelper daoHelper = daoHelperFactory.create()) {
                final String hashedPassword = BcryptWithSaltHasherImpl.getInstance().hashPassword(password);
                final User user = new User.Builder().
                        withLogin(login).
                        withPassword(hashedPassword).
                        withFirstName(firstName).
                        withLastName(lastName).
                        withRole(Role.CLIENT).
                        withBannedStatus(false).
                        build();
                final UserDao userDao = daoHelper.createUserDao();
                final User userWithId = userDao.create(user);
                return Optional.of(userWithId);
            } catch (Exception e) {
                LOG.error("Cannot create user as client", e);
                throw new ServiceException("Cannot create user as client", e);
            }
    }
}
