package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.dto.DrugsPaginationDto;
import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.Producer;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.DrugDao;
import by.ghoncharko.webproject.model.dao.DrugDaoImpl;
import by.ghoncharko.webproject.model.dao.ProducerDao;
import by.ghoncharko.webproject.model.dao.ProducerDaoImpl;
import by.ghoncharko.webproject.validator.ValidateCreateOrUpdatePreparate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class DrugServiceImpl implements DrugService {
    private static final Logger LOG = LogManager.getLogger(DrugServiceImpl.class);
    private static final Integer LIMIT_DRUGS_ON_ONE_PAGE = 5;
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private DrugServiceImpl() {

    }

    public static DrugServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public boolean createDrug(String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, String drugProducerName) throws ServiceException {
        final boolean isValidData = ValidateCreateOrUpdatePreparate.getInstance().validate(drugName, drugPrice, drugCount, drugDescription, drugProducerName, drugNeedRecipe);
        if (!isValidData) {
            return false;
        }
        final Connection connection = connectionPool.getConnection();
        Service.autoCommitFalse(connection);
        final ProducerDao producerDao = new ProducerDaoImpl(connection);
        try {
            final Optional<Producer> producerFromDB = producerDao.findProducerByProducerName(drugProducerName);
            if (producerFromDB.isPresent()) {
                final Producer producer = producerFromDB.get();
                createDrugByParam(drugName, drugNeedRecipe, drugCount, drugPrice, drugDescription, connection, producer);
            } else {
                final Producer createdProducer = createProducer(drugProducerName, producerDao);
                createDrugByParam(drugName, drugNeedRecipe, drugCount, drugPrice, drugDescription, connection, createdProducer);
            }
            return true;
        } catch (DaoException e) {
            Service.rollbackConnection(connection);
            LOG.error("Cannot create new drug", e);
            throw new ServiceException("Cannot create new drug", e);

        } finally {
            Service.connectionClose(connection);
        }

    }

    private Producer createProducer(String drugProducerName, ProducerDao producerDao) throws DaoException {
        return producerDao.create(new Producer.Builder().
                withName(drugProducerName).
                build());
    }

    private void createDrugByParam(String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, Connection connection, Producer producer) throws DaoException {
        final DrugDao drugDao = new DrugDaoImpl(connection);
        final Drug drug = new Drug.Builder().
                withName(drugName).
                withPrice(BigDecimal.valueOf(drugPrice)).
                withCount(drugCount).
                withDescription(drugDescription).
                withProducer(producer).
                withNeedReceip(drugNeedRecipe).
                withIsDeleted(false).
                build();
        drugDao.create(drug);
    }

    @Override
    public List<Drug> findAll() throws ServiceException {
        final Connection connection = connectionPool.getConnection();
        final DrugDao drugDao = new DrugDaoImpl(connection);
        try {
            return drugDao.findAll();
        } catch (DaoException e) {
            LOG.error("Cannot find all drugs", e);
            throw new ServiceException("Cannot find all drugs", e);
        } finally {
            Service.connectionClose(connection);
        }
    }

    @Override
    public DrugsPaginationDto findAllDrugsLimitOffsetPagination(Integer pageNumber) throws ServiceException {
        final Connection connection = connectionPool.getConnection();
        Service.autoCommitFalse(connection);
        final DrugDao drugDao = new DrugDaoImpl(connection);
        try {
            final int countDrugs = drugDao.findCountAllDrugCount();
            final int countPages = (int) Math.ceil((double) countDrugs / LIMIT_DRUGS_ON_ONE_PAGE);
            final int offset = pageNumber * LIMIT_DRUGS_ON_ONE_PAGE - LIMIT_DRUGS_ON_ONE_PAGE;
            final List<Drug> drugList = drugDao.findAllDrugsLimitOffsetPagination(LIMIT_DRUGS_ON_ONE_PAGE, offset);
            return createDrugsPaginationDto(countPages, drugList);
        } catch (DaoException e) {
            Service.rollbackConnection(connection);
            LOG.error("Cannot find all drugs", e);
            throw new ServiceException("Cannot find all drugs", e);
        } finally {
            Service.connectionClose(connection);
        }
    }

    private DrugsPaginationDto createDrugsPaginationDto(int countPages, List<Drug> drugList) {
        return new DrugsPaginationDto.Builder().
                wuthDrugList(drugList).
                withCountPages(countPages).
                build();
    }

    @Override
    public DrugsPaginationDto findAllDrugsWhereCountMoreThanZeroAndDrugNotDeletedLimitOffsetPagination(Integer pageNumber) throws ServiceException {
        final Connection connection = connectionPool.getConnection();
        Service.autoCommitFalse(connection);
        final DrugDao drugDao = new DrugDaoImpl(connection);
        try {
            final int countDrugs = drugDao.findCountAllDrugsWhereCountMoreThanZeroAndDrugIsNotDeleted();
            final int countPages = (int) Math.ceil((double) countDrugs / LIMIT_DRUGS_ON_ONE_PAGE);
            final int offset = pageNumber * LIMIT_DRUGS_ON_ONE_PAGE - LIMIT_DRUGS_ON_ONE_PAGE;
            final List<Drug> drugList = drugDao.findAllDrugsWhereCountMoreThanZeroAndDrugIsNotDeletedWithLimitOffsetPagination(LIMIT_DRUGS_ON_ONE_PAGE, offset);
            return createDrugsPaginationDto(countPages, drugList);
        } catch (DaoException e) {
            Service.rollbackConnection(connection);
            LOG.error("Cannot find all drugs where count more than zero and drug is not deleted", e);
            throw new ServiceException("Cannot find all drugs where count more than zero and drug is not deleted", e);
        } finally {
            Service.connectionClose(connection);
        }
    }

    private static class Holder {
        private static final DrugServiceImpl INSTANCE = new DrugServiceImpl();
    }
}
