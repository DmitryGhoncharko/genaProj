package by.ghoncharko.webproject.model.service;


import by.ghoncharko.webproject.entity.BankCard;

import java.util.List;
import java.util.Optional;

public interface BankCardService extends Service<BankCard> {
    boolean findBankCardByUserId(Integer userId);

    List<BankCard> getBankCardsByUserId(Integer userId);

    Optional<BankCard> getBankCardByCardId(Integer cardId);

    boolean deleteCardByCardIdAndUserId(Integer cardId, Integer userId);

    boolean addBankCard(Double balance, Integer userId);

    static BankCardService getInstance() {
        return BankCardServiceImpl.getInstance();
    }
}
