package by.ghoncharko.webproject.model.service;


import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface BankCardService extends Service<BankCard> {


    List<BankCard> getBankCardsByUserId(Integer userId) throws ServiceException;



    boolean deleteCardByCardIdAndUserId(Integer cardId, Integer userId) throws ServiceException;

    boolean addBankCard(Double balance, Integer userId) throws ServiceException;

    static BankCardService getInstance() {
        return BankCardServiceImpl.getInstance();
    }
}
