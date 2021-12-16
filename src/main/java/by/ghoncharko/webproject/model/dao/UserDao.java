package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.DaoException;

import java.util.List;
import java.util.Optional;

/**
 * Interface for specific methods UserDao
 *
 * @author Dmitry Ghoncharko
 * @see UserDaoImpl
 */
public interface UserDao extends Dao<User> {
    /**
     * @param login user login
     * @return Optional<User>
     * @throws DaoException when cannot find user by login
     * @see User
     */
    Optional<User> findUserByLogin(String login) throws DaoException;
    List<User> findAllUsersLimitOffsetPagination(Integer limit, Integer offset) throws DaoException;
    List<User> findAllClients() throws DaoException;
    int findAllUsersCount() throws DaoException;
    List<User> findAllClientsLimitOffsetPagination(Integer limit, Integer offset) throws DaoException;
    int findAllClientsCount() throws DaoException;
}
