package by.ghoncharko.webproject.validator;

public class ValidateDeleteBankCard {
    private ValidateDeleteBankCard(){
    }

    public boolean validate(Integer userId, Integer cardId){
        return userId != null && cardId != null && cardId >= 0;
    }

    public static ValidateDeleteBankCard getInstance() {
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final ValidateDeleteBankCard INSTANCE = new ValidateDeleteBankCard();
    }
}
