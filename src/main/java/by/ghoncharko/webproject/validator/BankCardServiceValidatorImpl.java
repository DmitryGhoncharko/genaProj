package by.ghoncharko.webproject.validator;

import by.ghoncharko.webproject.entity.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankCardServiceValidatorImpl implements BankCardServiceValidator{

    public boolean validateAddBankCard(Double balance, User user){
        if(balance!=null && user != null){
            Pattern pattern = Pattern.compile("^[0-9]{0,14}[.]?[0-9]{0,2}$");
            Matcher matcher = pattern.matcher(balance.toString());
            return matcher.matches();
        }
        return false;
    }

    public boolean validateFindBankCardByUserId(User user){
        return user != null;
    }

    public boolean validateDeleteBankCard(User user, Integer cardId){
        return user != null && cardId >= 0;
    }
}
