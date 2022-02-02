package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.DaoException;

import java.util.Optional;

public interface UserDao extends Dao<User> {
    
    Optional<User> findUserByLogin(String login) throws DaoException;
}
