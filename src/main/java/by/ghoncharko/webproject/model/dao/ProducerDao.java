package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.Producer;
import by.ghoncharko.webproject.exception.DaoException;

import java.util.Optional;

public interface ProducerDao extends Dao<Producer> {

    Optional<Producer> findProducerByName(String producerName) throws DaoException;
}
