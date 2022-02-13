package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.BankCardDao;
import by.ghoncharko.webproject.model.dao.BankCardDaoImpl;
import by.ghoncharko.webproject.validator.BankCardServiceValidator;
import by.ghoncharko.webproject.validator.BankCardServiceValidatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;

public class BankCardServiceImpl implements BankCardService{
    private static final Logger LOG = LogManager.getLogger(BankCardServiceImpl.class);
    private static final BankCardServiceValidator bankValidator = BankCardServiceValidatorImpl.getInstance();
    private BankCardServiceImpl(){
    }
    private final ConnectionPool connectionPool =  ConnectionPool.getInstance();
    @Override
    public List<BankCard> findAll() throws ServiceException {
        return null;
    }

    @Override
    public boolean addBankCard(Double balance, User user) throws ServiceException {
        final boolean isValidData = bankValidator.validateAddBankCard(balance, user);
        if(!isValidData){
            return false;
        }
        final Connection connection = connectionPool.getConnection();
        final BankCardDao bankCardDao  = new BankCardDaoImpl(connection);
        final BankCard bankCard = new BankCard.Builder().
                withBalance(BigDecimal.valueOf(balance)).
                withUser(user).
                build();
        try{
            bankCardDao.create(bankCard);
        }catch (DaoException e){
            LOG.error("Cannot add bank card",e);
            throw new ServiceException("Cannot add bank card",e);
        }finally {
            Service.connectionClose(connection);
        }
        return true;
    }

    @Override
    public List<BankCard> findBankCardsByUserId(User user) throws ServiceException {
        final boolean dataIsValid = bankValidator.validateFindBankCardByUserId(user);
        if(!dataIsValid){
            return Collections.emptyList();
        }
        final Connection connection = connectionPool.getConnection();
        final BankCardDao bankCardDao = new BankCardDaoImpl(connection);
        final int userId = user.getId();
        try{
            return bankCardDao.findUserBankCardsByUserId(userId);
        }catch (DaoException e){
            LOG.error("Cannot find bank cards by user id",e);
            throw new ServiceException("Cannot find bank cards by user id",e);
        }finally {
            Service.connectionClose(connection);
        }
    }

    @Override
    public void deleteBankCard(User user, Integer cardId) throws ServiceException {
        final boolean dataIsValid = bankValidator.validateDeleteBankCard(user, cardId);
        if(!dataIsValid){
            return;
        }
        final Connection connection = connectionPool.getConnection();
        final int userId = user.getId();
        final BankCardDao bankCardDao = new BankCardDaoImpl(connection);
        try{
            bankCardDao.deleteCardByCardIdAndUserId(cardId,userId);
        }catch (DaoException e){
            LOG.error("Cannot delete bank card by card id and user id ",e);
            throw new ServiceException("Cannot delete bank card by card id and user id ",e);
        }finally {
            Service.connectionClose(connection);
        }
    }

    public static BankCardServiceImpl getInstance() {
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final BankCardServiceImpl INSTANCE = new BankCardServiceImpl();
    }
}
