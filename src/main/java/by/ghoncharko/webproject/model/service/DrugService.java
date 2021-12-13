package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.exception.ServiceException;

import java.util.List;

public interface DrugService extends Service<Drug> {
    List<Drug> findAllWhereCountMoreThanZeroByUserIdAndCalculateCount(Integer userId) throws ServiceException;

    List<Drug> findAllWhereCountMoreThanZero() throws ServiceException;

    boolean deleteByDrugId(Integer drugId) throws ServiceException;

    boolean update(Integer drugId, String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription , String drugProducerName) throws ServiceException;
    List<Drug> findAllWhereNeedRecipe() throws ServiceException;
    boolean create(String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, String drugProducerName) throws ServiceException;
     static DrugService getInstance() {
        return DrugServiceImpl.getInstance();
    }
}
