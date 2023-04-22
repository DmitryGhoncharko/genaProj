package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.dto.ProductPaginationDto;
import by.ghoncharko.webproject.entity.Product;
import by.ghoncharko.webproject.entity.Producer;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.ProductDao;
import by.ghoncharko.webproject.model.dao.ProductDaoImpl;
import by.ghoncharko.webproject.model.dao.ProducerDao;
import by.ghoncharko.webproject.model.dao.ProducerDaoImpl;
import by.ghoncharko.webproject.validator.ValidateCreateOrUpdatePreparate;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final Logger LOG = LogManager.getLogger(ProductServiceImpl.class);
    private static final Integer LIMIT_DRUGS_ON_ONE_PAGE = 5;
    private final ConnectionPool connectionPool;

    @Override
    public boolean createDrug(String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, String drugProducerName) throws ServiceException {
        final boolean isValidData = ValidateCreateOrUpdatePreparate.getInstance().validate(drugName, drugPrice, drugCount, drugDescription, drugProducerName, drugNeedRecipe);
        if (!isValidData) {
            return false;
        }
        try (Connection connection = connectionPool.getConnection();
             Connection connection1 = connectionPool.getConnection();){
            final ProducerDao producerDao = new ProducerDaoImpl(connection);
            final ProductDao productDao = new ProductDaoImpl(connection1);
            final Optional<Producer> producerFromDB = producerDao.findProducerByProducerName(drugProducerName);
            if (producerFromDB.isPresent()) {
                final Producer producer = producerFromDB.get();
                createDrugByParam(drugName, drugNeedRecipe, drugCount, drugPrice, drugDescription, productDao, producer);
            } else {
                final Producer createdProducer = createProducer(drugProducerName, producerDao);
                createDrugByParam(drugName, drugNeedRecipe, drugCount, drugPrice, drugDescription, productDao, createdProducer);
            }
            return true;
        } catch (Exception e) {
            LOG.error("Cannot create new drug", e);
            throw new ServiceException("Cannot create new drug", e);
        }

    }

    @Override
    public boolean updateDrug(Integer drugId, String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, String drugProducerName, Boolean drugIsDeleted) {


        try (Connection connection = connectionPool.getConnection();Connection connection1 = connectionPool.getConnection()){
            final ProducerDao producerDao = new ProducerDaoImpl(connection);
            final ProductDao productDao = new ProductDaoImpl(connection1);
            final Optional<Producer> producerFromDB = producerDao.findProducerByProducerName(drugProducerName);
            if (producerFromDB.isPresent()) {
                final Producer producer = producerFromDB.get();
                productDao.update(new Product.Builder().
                        withId(drugId).
                        withName(drugName).
                        withPrice(BigDecimal.valueOf(drugPrice)).
                        withCount(drugCount).
                        withDescription(drugDescription).
                        withProducer(producer).
                        withNeedReceip(drugNeedRecipe).
                        withIsDeleted(drugIsDeleted).
                        build());
            } else {
                final Producer createdProducer = producerDao.create(new Producer.Builder().
                        withName(drugProducerName).
                        build());
                productDao.update(new Product.Builder().
                        withId(drugId).
                        withName(drugName).
                        withPrice(BigDecimal.valueOf(drugPrice)).
                        withCount(drugCount).
                        withDescription(drugDescription).
                        withProducer(createdProducer).
                        withNeedReceip(drugNeedRecipe).
                        withIsDeleted(drugIsDeleted).
                        build());
            }
        } catch (Exception e) {


        }
        return true;
    }

    private Producer createProducer(String drugProducerName, ProducerDao producerDao) throws DaoException {
        return producerDao.create(new Producer.Builder().
                withName(drugProducerName).
                build());
    }

    private void createDrugByParam(String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, ProductDao productDao, Producer producer) throws DaoException {
        final Product product = new Product.Builder().
                withName(drugName).
                withPrice(BigDecimal.valueOf(drugPrice)).
                withCount(drugCount).
                withDescription(drugDescription).
                withProducer(producer).
                withNeedReceip(drugNeedRecipe).
                withIsDeleted(false).
                build();
        productDao.create(product);
    }

    @Override
    public List<Product> findAll() throws ServiceException {
        try (final Connection connection = connectionPool.getConnection()) {
            final ProductDao productDao = new ProductDaoImpl(connection);
            return productDao.findAll();
        } catch (Exception e) {
            LOG.error("Cannot find all drugs", e);
            throw new ServiceException("Cannot find all drugs", e);
        }
    }

    @Override
    public ProductPaginationDto findAllDrugsLimitOffsetPagination(Integer pageNumber) throws ServiceException {


        try (Connection connection= connectionPool.getConnection()){
            final ProductDao productDao = new ProductDaoImpl(connection);
            final int countDrugs = productDao.findCountAllDrugCount();
            final int countPages = (int) Math.ceil((double) countDrugs / LIMIT_DRUGS_ON_ONE_PAGE);
            final int offset = pageNumber * LIMIT_DRUGS_ON_ONE_PAGE - LIMIT_DRUGS_ON_ONE_PAGE;
            final List<Product> productList = productDao.findAllDrugsLimitOffsetPagination(LIMIT_DRUGS_ON_ONE_PAGE, offset);
            return createDrugsPaginationDto(countPages, productList);
        } catch (Exception e) {

            LOG.error("Cannot find all drugs", e);
            throw new ServiceException("Cannot find all drugs", e);
        }
    }

    private ProductPaginationDto createDrugsPaginationDto(int countPages, List<Product> productList) {
        return new ProductPaginationDto.Builder().
                wuthDrugList(productList).
                withCountPages(countPages).
                build();
    }

    @Override
    public ProductPaginationDto findAllDrugsWhereCountMoreThanZeroAndCalculateDrugCountWithCountInOrderAndDrugNotDeletedLimitOffsetPagination(User user, Integer pageNumber) throws ServiceException {

        try (Connection connection = connectionPool.getConnection();){
            final ProductDao productDao = new ProductDaoImpl(connection);
            final int userId = user.getId();
            final int countDrugs = productDao.findCountAllDrugsWhereCountMoreThanZeroAndDrugIsNotDeleted();
            final int countPages = (int) Math.ceil((double) countDrugs / LIMIT_DRUGS_ON_ONE_PAGE);
            final int offset = pageNumber * LIMIT_DRUGS_ON_ONE_PAGE - LIMIT_DRUGS_ON_ONE_PAGE;
            final List<Product> productList = productDao.findAllDrugsWhereCountMoreThanZeroAndCalculateCountWithCountInOrderAndDrugIsNotDeletedWithLimitOffsetPagination(userId, LIMIT_DRUGS_ON_ONE_PAGE, offset);
            return createDrugsPaginationDto(countPages, productList);
        } catch (Exception e) {
            LOG.error("Cannot find all drugs where count more than zero and drug is not deleted", e);
            throw new ServiceException("Cannot find all drugs where count more than zero and drug is not deleted", e);
        }
    }

    @Override
    public ProductPaginationDto findAllDrugsWhereCountMoreThanZeroAndDrugNotDeletedLimitOffsetPagination(Integer pageNumber) throws ServiceException {
        try (Connection connection = connectionPool.getConnection()){
            final ProductDao productDao = new ProductDaoImpl(connection);
            final int countDrugs = productDao.findCountAllDrugsWhereCountMoreThanZeroAndDrugIsNotDeleted();
            final int countPages = (int) Math.ceil((double) countDrugs / LIMIT_DRUGS_ON_ONE_PAGE);
            final int offset = pageNumber * LIMIT_DRUGS_ON_ONE_PAGE - LIMIT_DRUGS_ON_ONE_PAGE;
            final List<Product> productList = productDao.findAllDrugsWhereCountMoreThanZeroAndDrugIsNotDeletedLimitOffsetPagination(LIMIT_DRUGS_ON_ONE_PAGE, offset);
            return createDrugsPaginationDto(countPages, productList);
        } catch (Exception e) {
            LOG.error("Cannot find all drugs where count more than zero and drug is not deleted", e);
            throw new ServiceException("Cannot find all drugs where count more than zero and drug is not deleted", e);
        }
    }
}
