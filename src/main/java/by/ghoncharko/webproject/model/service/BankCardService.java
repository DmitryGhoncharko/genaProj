package by.ghoncharko.webproject.model.service;


import by.ghoncharko.webproject.entity.BankCard;

import java.util.List;
import java.util.Optional;

public interface BankCardService extends Service<BankCard> {
    boolean findBankCardByUserId(Integer userId);

    List<BankCard> getBankCardsByUserId(Integer userId);

    Optional<BankCard> getBankCardsByCardId(Integer cardId);

    boolean deleteByCardId(Integer cardId);

    boolean addBankCard(Double balance, Integer userId);
}
