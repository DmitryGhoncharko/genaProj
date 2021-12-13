package by.ghoncharko.webproject.model.service;


import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.BankCardDao;
import by.ghoncharko.webproject.model.dao.BankCardDaoImpl;
import by.ghoncharko.webproject.validator.ValidateDeleteBankCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

public class BankCardServiceImpl implements BankCardService {
    private static final Logger LOG = LogManager.getLogger(BankCardServiceImpl.class);
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private BankCardServiceImpl() {
    }

    @Override
    public List<BankCard> getBankCardsByUserId(Integer userId) throws ServiceException {
        if (userId == null) {
            return Collections.emptyList();
        }
        final Connection connection = connectionPool.getConnection();
        final BankCardDao bankCardDao = new BankCardDaoImpl(connection);
        try {
            return bankCardDao.findAllBankCardsByUserId(userId);
        } catch (DaoException e) {
            LOG.error("DaoException when try get bank cards by user id", e);
            throw new ServiceException("DaoException when try get bank cards by user id", e);
        } finally {
            Service.connectionClose(connection);
        }
    }

    @Override
    public boolean deleteCardByCardIdAndUserId(Integer cardId, Integer userId) throws ServiceException {
        final boolean isValideData = ValidateDeleteBankCard.getInstance().validate(cardId, userId);
        if (!isValideData) {
            return false;
        }
        final Connection connection = connectionPool.getConnection();
        final BankCardDao bankCardDao = new BankCardDaoImpl(connection);
        try {
            return bankCardDao.deleteByCardIdAndUserId(cardId, userId);
        } catch (DaoException e) {
            LOG.error("Cannot delete card by card id and user id", e);
            throw new ServiceException("Cannot delete card by card id and user id", e);
        } finally {
            Service.connectionClose(connection);
        }
    }

    @Override
    public boolean addBankCard(Double balance, Integer userId) throws ServiceException {
        if (balance == null || userId == null) {
            return false;
        }
        final Connection connection = connectionPool.getConnection();
        final BankCardDao bankCardDao = new BankCardDaoImpl(connection);
        try {
            return bankCardDao.addBankCard(balance, userId);
        } catch (DaoException e) {
            LOG.error("Cannot add bank card", e);
            throw new ServiceException("Cannot add bank card", e);
        } finally {
            Service.connectionClose(connection);
        }
    }

    //
    @Override
    public List<BankCard> findAll() {
        return null;
    }

    static BankCardServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final BankCardServiceImpl INSTANCE = new BankCardServiceImpl();
    }
}
