package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.dto.ProductPaginationDto;
import by.ghoncharko.webproject.entity.Product;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;

public interface ProductService extends Service<Product> {

    boolean createDrug(String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, String drugProducerName) throws ServiceException;

    ProductPaginationDto findAllDrugsWhereCountMoreThanZeroAndCalculateDrugCountWithCountInOrderAndDrugNotDeletedLimitOffsetPagination(User user, Integer pageNumber) throws ServiceException;

    ProductPaginationDto findAllDrugsWhereCountMoreThanZeroAndDrugNotDeletedLimitOffsetPagination(Integer pageNumber) throws ServiceException;

    ProductPaginationDto findAllDrugsLimitOffsetPagination(Integer pageNumber) throws ServiceException;

    boolean updateDrug(Integer drugId, String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, String drugProducerName, Boolean drugIsDeleted);
}
