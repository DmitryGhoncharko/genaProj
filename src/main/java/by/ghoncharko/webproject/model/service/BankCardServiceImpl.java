package by.ghoncharko.webproject.model.service;


import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.exception.DaoException;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.BankCardDao;
import by.ghoncharko.webproject.model.dao.BankCardDaoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
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
