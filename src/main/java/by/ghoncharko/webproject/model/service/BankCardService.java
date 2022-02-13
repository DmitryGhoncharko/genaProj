package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;

import java.util.List;

public interface BankCardService extends Service<BankCard> {
    //mb big dec
    boolean addBankCard(Double balance, User user) throws ServiceException;

    void deleteBankCard(User user, Integer cardId) throws ServiceException;

    List<BankCard> findBankCardsByUserId(User user) throws ServiceException;
}
