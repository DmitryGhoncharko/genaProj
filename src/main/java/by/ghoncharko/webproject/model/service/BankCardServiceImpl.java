package by.ghoncharko.webproject.model.service;


import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.BankCardDao;
import by.ghoncharko.webproject.model.dao.BankCardDaoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BankCardServiceImpl implements BankCardService {
    private static final Logger LOG = LogManager.getLogger(BankCardServiceImpl.class);
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Override
    public boolean findBankCardByUserId(Integer userId) {
        Connection connection = connectionPool.getConnection();
        try {
            BankCardDao bankCardDao = new BankCardDaoImpl(connection);
            Optional<BankCard> bankCard = bankCardDao.findBankCardByUserId(userId);
            if (bankCard.isPresent()) {
                return true;
            }
        } catch (DaoException e) {
            LOG.error("DaoException", e);

        } finally {
            Service.connectionClose(connection);
        }
        return false;
    }
    @Override
    public List<BankCard> getBankCardsByUserId(Integer userId) {
        Connection connection = connectionPool.getConnection();
        try {
            BankCardDao bankCardDao = new BankCardDaoImpl(connection);
            List<BankCard> bankCard = bankCardDao.findAllBankCardsByUserId(userId);
            return bankCard;
        } catch (DaoException e) {
            LOG.error("DaoException", e);

        } finally {
            Service.connectionClose(connection);
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<BankCard> getBankCardsByCardId(Integer cardId) {
        Connection connection = connectionPool.getConnection();
        try {
            BankCardDao bankCardDao = new BankCardDaoImpl(connection);
            Optional<BankCard> bankCard = bankCardDao.findAllBankCardsByCardId(cardId);
            if(bankCard.isPresent()){
                return bankCard;
            }
            return Optional.empty();
        } catch (DaoException e) {
            LOG.error("DaoException", e);

        } finally {
            Service.connectionClose(connection);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteByCardId(Integer cardId) {
        Connection connection = connectionPool.getConnection();
        try{
            BankCardDao bankCardDao = new BankCardDaoImpl(connection);
            return bankCardDao.deleteByCardId(cardId);
        }catch (DaoException e){

        }finally {
            Service.connectionClose(connection);
        }
        return false;
    }

    @Override
    public boolean addBankCard(Double balance, Integer userId) {
        Connection connection = connectionPool.getConnection();
        BankCardDao bankCardDao = new BankCardDaoImpl(connection);
        try{
           return bankCardDao.addBankCard(balance,userId);
        }catch (DaoException e){

        }finally {
            Service.connectionClose(connection);
        }
        return false;
    }

    @Override
    public List<BankCard> findAll() {
        return null;
    }

    public static BankCardServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final BankCardServiceImpl INSTANCE = new BankCardServiceImpl();
    }
}
