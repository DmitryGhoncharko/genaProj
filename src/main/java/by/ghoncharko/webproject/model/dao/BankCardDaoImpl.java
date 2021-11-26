package by.ghoncharko.webproject.model.dao;


import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BankCardDaoImpl extends AbstractDao<BankCard> {
    private static final Logger LOG = LogManager.getLogger(BankCardDaoImpl.class);
    private static final String SQL_CREATE_BANK_CARD = "INSERT INTO card (user_id, balance) VALUES (?,?)";
    private static final String SQL_FIND_ALL_BANK_CARDS = "SELECT id, user_id, balance FROM card";
    private static final String SQL_FIND_BANK_CARD_BY_USER_ID = "SELECT id, user_id, balance FROM card WHERE user_id = ?";
    private static final String SQL_FIND_BANK_CARD_BY_ID = "SELECT id, user_id, balance FROM card WHERE id = ?";
    private static final String SQL_UPDATE_BANK_CARD = "UPDATE card SET  balance = ? WHERE id = ?";
    private static final String SQL_DELETE_BANK_CARD = "DELETE FROM card  WHERE id=?";

    public BankCardDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public BankCard create(BankCard entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_CREATE_BANK_CARD, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, entity.getUserId());
            preparedStatement.setDouble(2, entity.getBalance());
            final int updatedRows = preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (updatedRows > 0 & resultSet.next()) {
                return new BankCard.Builder().
                        withId(resultSet.getInt(1)).
                        withUserId(entity.getUserId()).
                        withBalance(entity.getBalance()).
                        build();

            }
        } catch (SQLException e) {
            LOG.error("SQLException in method create bankCard", e);
            throw new DaoException("SQLException in method create bankCard", e);
        } finally {
            close(preparedStatement);
        }
        LOG.error("DaoException in method create bankCard when we try create entity");
        throw new DaoException("DaoException in method create bankCard");
    }

    @Override
    public List<BankCard> findAll() throws DaoException {
        List<BankCard> bankCardList = new ArrayList<>();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            final ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_BANK_CARDS);
            while (resultSet.next()) {
                BankCard bankCard = new BankCard.Builder().
                        withId(resultSet.getInt(1)).
                        withUserId(resultSet.getInt(2)).
                        withBalance(resultSet.getDouble(3)).
                        build();
                bankCardList.add(bankCard);
            }
        } catch (SQLException e) {
            LOG.error("SQLException when we try findAll entities in BankCard", e);
            throw new DaoException("SQLException when we try findAll entities in BankCard", e);
        } finally {
            close(statement);
        }
        return bankCardList;
    }

    @Override
    public Optional<BankCard> findEntityById(Integer id) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_BANK_CARD_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new BankCard.Builder().
                        withId(resultSet.getInt(1)).
                        withUserId(resultSet.getInt(2)).
                        withBalance(resultSet.getDouble(3)).
                        build());

            }
        } catch (SQLException e) {
            LOG.error("SQLException when we try to findEntityById bankCard", e);
            throw new DaoException("SQLException when we try to findEntityById bankCard", e);
        }finally {
            close(preparedStatement);
        }
        LOG.error("cannot find bankCard entity by id");
        return Optional.empty();
    }

    public Optional<BankCard> findBankCardByUserId(Integer userId) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_BANK_CARD_BY_USER_ID);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new BankCard.Builder().
                        withId(resultSet.getInt(1)).
                        withUserId(resultSet.getInt(2)).
                        withBalance(resultSet.getDouble(3)).
                        build());

            }
        } catch (SQLException e) {
            LOG.error("SQLException when we try to findEntityById bankCard", e);
            throw new DaoException("SQLException when we try to findEntityById bankCard", e);
        }finally {
            close(preparedStatement);
        }
        LOG.error("cannot find bankCard by User id");
        return Optional.empty();
    }

    @Override
    public BankCard update(BankCard entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_BANK_CARD);
            preparedStatement.setDouble(1, entity.getBalance());
            preparedStatement.setInt(2, entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return entity;
            }
        } catch (SQLException e) {
            LOG.error("Cannot update BankCard entity", e);
            throw new DaoException("Cannot update BankCard entity",e);
        } finally {
            close(preparedStatement);
        }
        LOG.error("Cannot update bank card");
        throw new DaoException();
    }

    @Override
    public boolean delete(BankCard entity) throws DaoException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_DELETE_BANK_CARD);
            preparedStatement.setInt(1, entity.getId());
            int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                return true;
            }
        } catch (SQLException e) {
            LOG.error("cannod delete bank card",e);
            throw new DaoException("cannod delete bank card",e);
        } finally {
            close(preparedStatement);
        }
        LOG.error("cannot delete bank card");
        return false;
    }
}
