package by.ghoncharko.webproject.model.dao;


import by.ghoncharko.webproject.entity.Entity;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Interface Dao for all Dao - describe simple CRUD operations with entities
 *
 * @param <T> any type who implements Entity
 * @author Dmitry Ghoncharko
 * @see Entity - Interface-marker
 */
public interface Dao<T extends Entity> {
    Logger LOG = LogManager.getLogger(Dao.class);

    /**
     * @param entity accept entity which extends Entity(Interface-marker)
     * @return entity if  create was successful
     * @throws DaoException when cannot create entity
     * @see Entity - Interface-marker
     */
    T create(T entity) throws DaoException;

    /**
     * @return List<T> where T - entity which extends Entity(Interface-marker)
     * @throws DaoException when cannot findAll entities
     * @see Entity - Interface-marker
     */
    List<T> findAll() throws DaoException;

    /**
     * @param id entity id
     * @return Optional<T> where T - entity which extends Entity(Interface-marker)
     * @throws DaoException when cannot find entity by id
     * @see Entity - Interface-marker
     */
    Optional<T> findEntityById(Integer id) throws DaoException;

    /**
     * @param entity accept entity which extends Entity(Interface-marker)
     * @return T where T - entity which extends Entity(Interface-marker)
     * @throws DaoException when cannot update entity
     * @see Entity - Interface-marker
     */
    T update(T entity) throws DaoException;

    /**
     * @return true if delete was success and false if delete was unsuccess
     * @throws DaoException when cannot delete entity
     * @see Entity - Interface-marker
     */
    boolean delete(Integer id) throws DaoException;
}
