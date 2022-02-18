package by.ghoncharko.webproject.model.dao;

import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.exception.DaoException;

import java.util.List;

public interface BankCardDao extends Dao<BankCard> {
    boolean deleteCardByCardIdAndUserId(Integer cardId, Integer userId) throws DaoException;

    List<BankCard> findUserBankCardsByUserId(Integer userId) throws DaoException;
}
