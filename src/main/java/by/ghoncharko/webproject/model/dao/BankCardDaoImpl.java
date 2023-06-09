package by.ghoncharko.webproject.model.dao;


import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.entity.Role;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class BankCardDaoImpl extends AbstractDao<BankCard> implements BankCardDao {
    private static final Logger LOG = LogManager.getLogger(BankCardDaoImpl.class);
    private static final String SQL_CREATE_BANK_CARD = "INSERT INTO bank_card(user_id, balance) VALUES (?,?)";
    private static final String SQL_FIND_ALL_BANK_CARDS = "SELECT bank_card.id, u.id, u.login, u.password, r.role_name, u.first_name, u.last_name, u.is_banned ,balance" +
            " FROM  bank_card" +
            " INNER JOIN user u on bank_card.user_id = u.id" +
            " INNER JOIN role r on u.role_id = r.id";
    private static final String SQL_FIND_BANK_CARD_BY_ID = "SELECT bank_card.id, u.id, u.login, u.password, r.role_name, u.first_name, u.last_name, u.is_banned ,balance" +
            " FROM  bank_card" +
            " INNER JOIN user u on bank_card.user_id = u.id" +
            " INNER JOIN role r on u.role_id = r.id" +
            " WHERE bank_card.id = ?";
    private static final String SQL_FIND_USER_BANKS_CARDS_BY_USER_ID = "SELECT bank_card.id, u.id, u.login, u.password, r.role_name, u.first_name, u.last_name, u.is_banned ,balance" +
            " FROM  bank_card" +
            " INNER JOIN user u on bank_card.user_id = u.id" +
            " INNER JOIN role r on u.role_id = r.id" +
            " WHERE user_id = ?";
    private static final String SQL_UPDATE_BANK_CARD_BY_CARD_ID = "UPDATE bank_card SET user_id = ?, balance = ?" +
            " WHERE id = ?";
    private static final String SQL_DELETE_BANK_CARD_BY_CARD_ID = "DELETE FROM bank_card WHERE id = ?";
    private static final String SQL_DELETE_BANK_CARD_BY_CARD_ID_AND_USER_ID = "DELETE FROM bank_card WHERE id = ? AND user_id = ?";

    public BankCardDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public BankCard create(BankCard entity) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_BANK_CARD, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, entity.getUser().getId());
            preparedStatement.setDouble(2, entity.getBalance().doubleValue());
            final int countRows = preparedStatement.executeUpdate();
            if (countRows > 0) {
                final ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    return new BankCard.Builder().
                            withId(resultSet.getInt(1)).
                            withUser(entity.getUser()).
                            withBalance(entity.getBalance()).build();
                }
            }
        } catch (SQLException e) {
            LOG.error("Cannot create bank card", e);
            throw new DaoException("Cannot create bank card", e);
        }
        throw new DaoException("Cannot create bank card");
    }

    @Override
    public List<BankCard> findAll() throws DaoException {
        final List<BankCard> bankCardList = new ArrayList<>();
        try (final Statement statement = connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_BANK_CARDS);
            while (resultSet.next()) {
                final BankCard bankCard = extractEntity(resultSet);
                bankCardList.add(bankCard);
            }
        } catch (SQLException e) {
            LOG.error("Cannot find all bank cards ", e);
            throw new DaoException("Cannot find all bank cards ", e);
        }
        return bankCardList;
    }

    @Override
    public Optional<BankCard> findEntityById(Integer id) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BANK_CARD_BY_ID)) {
            preparedStatement.setInt(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(extractEntity(resultSet));
            }
        } catch (SQLException e) {
            LOG.error("Cannot find bank card by id", e);
            throw new DaoException("Cannot find bank card by id", e);
        }
        LOG.info("Cannot find bank card by card id");
        return Optional.empty();
    }

    @Override
    public List<BankCard> findUserBankCardsByUserId(Integer userId) throws DaoException {
        final List<BankCard> bankCardList = new ArrayList<>();
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USER_BANKS_CARDS_BY_USER_ID)) {
            preparedStatement.setInt(1, userId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final BankCard bankCard = extractEntity(resultSet);
                bankCardList.add(bankCard);
            }
        } catch (SQLException e) {
            LOG.error("Cannot find bank cards by user id", e);
            throw new DaoException("Cannot find bank cards by user id", e);
        }
        return bankCardList;
    }

    @Override
    public BankCard update(BankCard entity) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_BANK_CARD_BY_CARD_ID)) {
            preparedStatement.setInt(1, entity.getUser().getId());
            preparedStatement.setDouble(2, entity.getBalance().doubleValue());
            preparedStatement.setInt(3, entity.getId());
            final int countUpdatedRows = preparedStatement.executeUpdate();
            if (countUpdatedRows > 0) {
                return entity;
            }
        } catch (SQLException e) {
            LOG.error("Cannot update bank card by card id");
            throw new DaoException("Cannot update bank card by card id", e);
        }
        LOG.error("Cannot update bank card by card id");
        throw new DaoException("Cannot update bank card by card id");
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BANK_CARD_BY_CARD_ID)) {
            return deleteBillet(preparedStatement, id);
        } catch (SQLException e) {
            LOG.error("Cannot delete bank card by card id", e);
            throw new DaoException("Cannot delete bank card by card id", e);
        }
    }

    @Override
    public boolean deleteCardByCardIdAndUserId(Integer cardId, Integer userId) throws DaoException {
        try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BANK_CARD_BY_CARD_ID_AND_USER_ID)) {
            preparedStatement.setInt(1, cardId);
            preparedStatement.setInt(2, userId);
            final int countRowsDeleted = preparedStatement.executeUpdate();
            return countRowsDeleted > 0;
        } catch (SQLException e) {
            LOG.error("Cannot delete bank card by card id and user id", e);
            throw new DaoException("Cannot delete bank card by card id and user id", e);
        }
    }

    @Override
    protected BankCard extractEntity(ResultSet resultSet) throws SQLException {
        return new BankCard.Builder().
                withId(resultSet.getInt(1)).
                withUser(new User.Builder().
                        withId(resultSet.getInt(2)).
                        withLogin(resultSet.getString(3)).
                        withPassword(resultSet.getString(4)).
                        withRole(Role.valueOf(resultSet.getString(5))).
                        withFirstName(resultSet.getString(6)).
                        withLastName(resultSet.getString(7)).
                        withBannedStatus(resultSet.getBoolean(8)).
                        build()).
                withBalance(BigDecimal.valueOf(resultSet.getDouble(9))).
                build();
    }

}
