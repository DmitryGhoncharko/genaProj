package by.ghoncharko.webproject.model.service;


import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.BankCardDao;
import by.ghoncharko.webproject.model.dao.BankCardDaoImpl;
import by.ghoncharko.webproject.validator.ValidateAddBankCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BankCardServiceImpl implements BankCardService {
    private static final Logger LOG = LogManager.getLogger(BankCardServiceImpl.class);
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private BankCardServiceImpl() {
    }

    @Override
    public boolean findBankCardByUserId(Integer userId) {
        final Connection connection = connectionPool.getConnection();
        try {
            final BankCardDao bankCardDao = new BankCardDaoImpl(connection);
            final Optional<BankCard> bankCard = bankCardDao.findBankCardByUserId(userId);
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
        final Connection connection = connectionPool.getConnection();
        try {
            final BankCardDao bankCardDao = new BankCardDaoImpl(connection);
            return bankCardDao.findAllBankCardsByUserId(userId);
        } catch (DaoException e) {
            LOG.error("DaoException", e);

        } finally {
            Service.connectionClose(connection);
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<BankCard> getBankCardsByCardId(Integer cardId) {
        final Connection connection = connectionPool.getConnection();
        try {
            final BankCardDao bankCardDao = new BankCardDaoImpl(connection);
            return bankCardDao.findAllBankCardsByCardId(cardId);
        } catch (DaoException e) {
            LOG.error("DaoException", e);

        } finally {
            Service.connectionClose(connection);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteByCardId(Integer cardId) {
        final Connection connection = connectionPool.getConnection();
        try {
            final BankCardDao bankCardDao = new BankCardDaoImpl(connection);
            return bankCardDao.deleteByCardId(cardId);
        } catch (DaoException e) {
            LOG.error("DaoException", e);
        } finally {
            Service.connectionClose(connection);
        }
        return false;
    }

    @Override
    public boolean addBankCard(Double balance, Integer userId) {
        final boolean isValide = ValidateAddBankCard.getInstance().validateAddBankCard(balance, userId);
        if (isValide) {
            final Connection connection = connectionPool.getConnection();
            final BankCardDao bankCardDao = new BankCardDaoImpl(connection);
            try {
                return bankCardDao.addBankCard(balance, userId);
            } catch (DaoException e) {
                LOG.error("DaoException", e);
            } finally {
                Service.connectionClose(connection);
            }
        }
        return false;
    }

    @Override
    public List<BankCard> findAll() {
        final Connection connection = connectionPool.getConnection();
        try {
            final BankCardDao bankCardDao = new BankCardDaoImpl(connection);
            return bankCardDao.findAll();
        } catch (DaoException e) {
            LOG.error("DaoException", e);
        } finally {
            Service.connectionClose(connection);
        }
        return Collections.emptyList();
    }

     static BankCardServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final BankCardServiceImpl INSTANCE = new BankCardServiceImpl();
    }
}
