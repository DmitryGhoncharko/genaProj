package by.ghoncharko.webproject.validator;

import by.ghoncharko.webproject.entity.User;

public interface BankCardServiceValidator {
    boolean validateAddBankCard(Double balance, User user);

    boolean validateFindBankCardByUserId(User user);

    boolean validateDeleteBankCard(User user, Integer cardId);


}
