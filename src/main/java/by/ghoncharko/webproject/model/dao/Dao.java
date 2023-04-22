package by.ghoncharko.webproject.model.dao;


import by.ghoncharko.webproject.entity.Entity;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;


public interface Dao<T extends Entity> {
    Logger LOG = LogManager.getLogger(Dao.class);


    T create(T entity) throws DaoException;


    List<T> findAll() throws DaoException;


    Optional<T> findEntityById(Integer id) throws DaoException;


    T update(T entity) throws DaoException;


    boolean delete(Integer id) throws DaoException;
}
