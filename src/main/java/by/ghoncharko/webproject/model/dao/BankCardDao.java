package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.exception.DaoException;

import java.util.List;
import java.util.Optional;

/**
 * Interface for BankCardDao with specific methods
 *
 * @author Dmitry Ghoncharko
 * @see BankCardDaoImpl
 */
public interface BankCardDao extends Dao<BankCard> {
    /**
     * @param userId id user
     * @return bank card entity
     * @throws DaoException when cannot find bank card by user id
     * @see BankCard
     */
    Optional<BankCard> findBankCardByUserId(Integer userId) throws DaoException;
    Optional<BankCard> findAllBankCardsByUserIdAndCardId(Integer userId, Integer cardId) throws DaoException;
    List<BankCard> findAllBankCardsByUserId(Integer userId) throws DaoException;

    Optional<BankCard> findBankCardByCardId(Integer cardId) throws DaoException;
    boolean deleteByCardIdAndUserId(Integer cardId, Integer userId) throws DaoException;
    boolean addBankCard(Double balance, Integer userId) throws DaoException;
}
