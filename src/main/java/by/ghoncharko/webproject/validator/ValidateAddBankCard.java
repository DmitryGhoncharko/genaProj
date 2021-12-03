package by.ghoncharko.webproject.validator;

public class ValidateAddBankCard {
    private static final Double MAX_PERMITTED_VALUE = 99999999999999.99d;
    public boolean validateAddBankCard(Double balance, Integer userId){
        if(balance!=null && userId != null){
            return balance >= 0 && balance <= MAX_PERMITTED_VALUE;
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
