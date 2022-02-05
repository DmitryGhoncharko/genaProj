package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.BankCardDao;
import by.ghoncharko.webproject.model.dao.BankCardDaoImpl;
import by.ghoncharko.webproject.model.dao.Dao;
import by.ghoncharko.webproject.validator.ValidateAddBankCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

public class BankCardServiceImpl implements BankCardService{
    private static final Logger LOG = LogManager.getLogger(BankCardServiceImpl.class);
    private final ConnectionPool connectionPool =  ConnectionPool.getInstance();

    private BankCardServiceImpl(){
    }
    @Override
    public List<BankCard> findAll() throws ServiceException {
        return null;
    }
    //mb big dec
    @Override
    public boolean addBankCard(Double balance, User user) throws ServiceException {
        final boolean isValidData = ValidateAddBankCard.getInstance().validate(balance, user);
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

    public static BankCardServiceImpl getInstance() {
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final BankCardServiceImpl INSTANCE = new BankCardServiceImpl();
    }
}
