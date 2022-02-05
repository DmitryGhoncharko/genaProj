package by.ghoncharko.webproject.validator;

import by.ghoncharko.webproject.entity.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateAddBankCard {
    private ValidateAddBankCard(){
    }
    public boolean validate(Double balance, User user){
        if(balance!=null && user != null){
            Pattern pattern = Pattern.compile("^[0-9]{0,14}[.]{0,1}[0-9]{0,2}$");
            Matcher matcher = pattern.matcher(balance.toString());
            return matcher.matches();
        }
        return false;
    }
    
    public static ValidateAddBankCard getInstance(){
        return Holer.INSTANCE;
    }

    private static class Holer{
        private static final ValidateAddBankCard INSTANCE = new ValidateAddBankCard();
    }
}
