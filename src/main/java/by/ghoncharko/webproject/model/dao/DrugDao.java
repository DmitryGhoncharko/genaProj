package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.exception.DaoException;

import java.util.Optional;

public interface DrugDao extends Dao<Drug>{

    Optional<Drug> findDrugByDrugIdWhereCountMoreThanZeroAndDrugDontDeleted(Integer drugId) throws DaoException;
}
