package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.dto.DrugsPaginationDto;
import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.Role;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.exception.ServiceException;

import java.util.List;

public interface DrugService extends Service<Drug> {

    boolean createDrug(String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, String drugProducerName) throws ServiceException;

    DrugsPaginationDto findAllDrugsWhereCountMoreThanZeroAndCalculateDrugCountWithCountInOrderAndDrugNotDeletedLimitOffsetPagination(User user, Integer pageNumber) throws ServiceException;

    DrugsPaginationDto findAllDrugsWhereCountMoreThanZeroAndDrugNotDeletedLimitOffsetPagination(Integer pageNumber) throws ServiceException;

    DrugsPaginationDto findAllDrugsLimitOffsetPagination(Integer pageNumber) throws ServiceException;

    boolean updateDrug(Integer drugId, String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, String drugProducerName, Boolean drugIsDeleted);
}
