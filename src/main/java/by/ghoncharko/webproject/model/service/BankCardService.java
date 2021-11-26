package by.ghoncharko.webproject.model.service;


import by.ghoncharko.webproject.entity.BankCard;

public interface BankCardService extends Service<BankCard> {
    boolean findBankCardByUserId(Integer userId);
}
