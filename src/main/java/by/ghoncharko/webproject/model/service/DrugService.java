package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.exception.ServiceException;

import java.util.List;

public interface DrugService extends Service<Drug> {
    List<Drug> findAllWhereCountMoreThanZeroByUserIdAndCalculateCountLimitOffsetPagination(Integer userId, Integer pageNumber) throws ServiceException;
    int findMaxCountPagesForAllWhereCountMoreThanZeroWithStatusActiveByUserId(Integer userId);
    List<Drug> findAllWhereCountMoreThanZeroLimitOffsetPagination(Integer pageNumber) throws ServiceException;
    int findMaxCountPagesForAllWhereCountMoreThanZero() throws ServiceException;
    boolean deleteByDrugId(Integer drugId) throws ServiceException;
    int findMaxCountPagesForAllDrugs() throws ServiceException;
    List<Drug> findAllLimitOffsetPagination(Integer page) throws ServiceException;

    boolean update(Integer drugId, String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription , String drugProducerName) throws ServiceException;
    List<Drug> findAllWhereNeedRecipeLimitOffsetPagination(Integer pageNumber) throws ServiceException;
    int findMaxCountPagesForAllNeedRicipesDrugs() throws ServiceException;
    boolean create(String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, String drugProducerName) throws ServiceException;
     static DrugService getInstance() {
        return DrugServiceImpl.getInstance();
    }
}
