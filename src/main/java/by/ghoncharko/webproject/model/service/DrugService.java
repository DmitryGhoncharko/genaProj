package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.exception.ServiceException;

public interface DrugService extends Service<Drug> {

    boolean createDrug(String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, String drugProducerName) throws ServiceException;
}
