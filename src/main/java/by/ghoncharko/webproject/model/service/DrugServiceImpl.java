package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.dto.DrugsPaginationDto;
import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.Producer;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.dao.DaoHelper;
import by.ghoncharko.webproject.model.dao.DaoHelperFactory;
import by.ghoncharko.webproject.model.dao.DrugDao;
import by.ghoncharko.webproject.model.dao.ProducerDao;
import by.ghoncharko.webproject.validator.ValidateCreateOrUpdatePreparate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class DrugServiceImpl implements DrugService {
    private static final Logger LOG = LogManager.getLogger(DrugServiceImpl.class);
    private static final Integer LIMIT_DRUGS_ON_ONE_PAGE = 5;
    private final DaoHelperFactory daoHelperFactory;

    public DrugServiceImpl(DaoHelperFactory daoHelperFactory) {
        this.daoHelperFactory = daoHelperFactory;
    }

    @Override
    public boolean createDrug(String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, String drugProducerName) throws ServiceException {
        final boolean isValidData = ValidateCreateOrUpdatePreparate.getInstance().validate(drugName, drugPrice, drugCount, drugDescription, drugProducerName, drugNeedRecipe);
        if (!isValidData) {
            return false;
        }
        final DaoHelper daoHelper = daoHelperFactory.create();
        daoHelper.startTransaction();
        final ProducerDao producerDao = daoHelper.createProducerDao();
        final DrugDao drugDao = daoHelper.createDrugDao();
        try {
            final Optional<Producer> producerFromDB = producerDao.findProducerByProducerName(drugProducerName);
            if (producerFromDB.isPresent()) {
                final Producer producer = producerFromDB.get();
                createDrugByParam(drugName, drugNeedRecipe, drugCount, drugPrice, drugDescription, drugDao, producer);
            } else {
                final Producer createdProducer = createProducer(drugProducerName, producerDao);
                createDrugByParam(drugName, drugNeedRecipe, drugCount, drugPrice, drugDescription, drugDao, createdProducer);
            }
            return true;
        } catch (Exception e) {
            daoHelper.rollbackTransactionAndCloseConnection();
            LOG.error("Cannot create new drug", e);
            throw new ServiceException("Cannot create new drug", e);
        } finally {
            daoHelper.commitTransactionAndCloseConnection();
        }

    }

    @Override
    public boolean updateDrug(Integer drugId, String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, String drugProducerName, Boolean drugIsDeleted) {
        final DaoHelper daoHelper = daoHelperFactory.create();
        daoHelper.startTransaction();
        final ProducerDao producerDao = daoHelper.createProducerDao();
        final DrugDao drugDao = daoHelper.createDrugDao();
        try {
            final Optional<Producer> producerFromDB = producerDao.findProducerByProducerName(drugProducerName);
            if (producerFromDB.isPresent()) {
                final Producer producer = producerFromDB.get();
                drugDao.update(new Drug.Builder().
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
                drugDao.update(new Drug.Builder().
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
        } catch (DaoException e) {


        }
        return true;
    }

    private Producer createProducer(String drugProducerName, ProducerDao producerDao) throws DaoException {
        return producerDao.create(new Producer.Builder().
                withName(drugProducerName).
                build());
    }

    private void createDrugByParam(String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription, DrugDao drugDao, Producer producer) throws DaoException {
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
        try (final DaoHelper daoHelper = daoHelperFactory.create()) {
            final DrugDao drugDao = daoHelper.createDrugDao();
            return drugDao.findAll();
        } catch (Exception e) {
            LOG.error("Cannot find all drugs", e);
            throw new ServiceException("Cannot find all drugs", e);
        }
    }

    @Override
    public DrugsPaginationDto findAllDrugsLimitOffsetPagination(Integer pageNumber) throws ServiceException {

        final DaoHelper daoHelper = daoHelperFactory.create();
        daoHelper.startTransaction();
        final DrugDao drugDao = daoHelper.createDrugDao();
        try {
            final int countDrugs = drugDao.findCountAllDrugCount();
            final int countPages = (int) Math.ceil((double) countDrugs / LIMIT_DRUGS_ON_ONE_PAGE);
            final int offset = pageNumber * LIMIT_DRUGS_ON_ONE_PAGE - LIMIT_DRUGS_ON_ONE_PAGE;
            final List<Drug> drugList = drugDao.findAllDrugsLimitOffsetPagination(LIMIT_DRUGS_ON_ONE_PAGE, offset);
            return createDrugsPaginationDto(countPages, drugList);
        } catch (Exception e) {
            daoHelper.rollbackTransactionAndCloseConnection();
            LOG.error("Cannot find all drugs", e);
            throw new ServiceException("Cannot find all drugs", e);
        } finally {
            daoHelper.commitTransactionAndCloseConnection();
        }
    }

    private DrugsPaginationDto createDrugsPaginationDto(int countPages, List<Drug> drugList) {
        return new DrugsPaginationDto.Builder().
                wuthDrugList(drugList).
                withCountPages(countPages).
                build();
    }

    @Override
    public DrugsPaginationDto findAllDrugsWhereCountMoreThanZeroAndCalculateDrugCountWithCountInOrderAndDrugNotDeletedLimitOffsetPagination(User user, Integer pageNumber) throws ServiceException {
        final DaoHelper daoHelper = daoHelperFactory.create();
        daoHelper.startTransaction();
        final DrugDao drugDao = daoHelper.createDrugDao();
        try {
            final int userId = user.getId();
            final int countDrugs = drugDao.findCountAllDrugsWhereCountMoreThanZeroAndDrugIsNotDeleted();
            final int countPages = (int) Math.ceil((double) countDrugs / LIMIT_DRUGS_ON_ONE_PAGE);
            final int offset = pageNumber * LIMIT_DRUGS_ON_ONE_PAGE - LIMIT_DRUGS_ON_ONE_PAGE;
            final List<Drug> drugList = drugDao.findAllDrugsWhereCountMoreThanZeroAndCalculateCountWithCountInOrderAndDrugIsNotDeletedWithLimitOffsetPagination(userId, LIMIT_DRUGS_ON_ONE_PAGE, offset);
            return createDrugsPaginationDto(countPages, drugList);
        } catch (Exception e) {
            daoHelper.rollbackTransactionAndCloseConnection();
            LOG.error("Cannot find all drugs where count more than zero and drug is not deleted", e);
            throw new ServiceException("Cannot find all drugs where count more than zero and drug is not deleted", e);
        } finally {
            daoHelper.commitTransactionAndCloseConnection();
        }
    }

    @Override
    public DrugsPaginationDto findAllDrugsWhereCountMoreThanZeroAndDrugNotDeletedLimitOffsetPagination(Integer pageNumber) throws ServiceException {
        final DaoHelper daoHelper = daoHelperFactory.create();
        daoHelper.startTransaction();
        final DrugDao drugDao = daoHelper.createDrugDao();
        try {
            final int countDrugs = drugDao.findCountAllDrugsWhereCountMoreThanZeroAndDrugIsNotDeleted();
            final int countPages = (int) Math.ceil((double) countDrugs / LIMIT_DRUGS_ON_ONE_PAGE);
            final int offset = pageNumber * LIMIT_DRUGS_ON_ONE_PAGE - LIMIT_DRUGS_ON_ONE_PAGE;
            final List<Drug> drugList = drugDao.findAllDrugsWhereCountMoreThanZeroAndDrugIsNotDeletedLimitOffsetPagination(LIMIT_DRUGS_ON_ONE_PAGE, offset);
            return createDrugsPaginationDto(countPages, drugList);
        } catch (Exception e) {
            daoHelper.rollbackTransactionAndCloseConnection();
            LOG.error("Cannot find all drugs where count more than zero and drug is not deleted", e);
            throw new ServiceException("Cannot find all drugs where count more than zero and drug is not deleted", e);
        } finally {
            daoHelper.commitTransactionAndCloseConnection();
        }
    }
}
