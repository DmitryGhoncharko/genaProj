package by.ghoncharko.webproject.validator;

import java.sql.Date;

public class ValidateCreateRecipe {
    private ValidateCreateRecipe(){
    }
    public boolean validate(Integer userId, Integer drugId, Date dateEnd){
        if(userId!=null && drugId!=null && dateEnd!=null){
            return dateEnd.after(new Date(new java.util.Date().getDate()));
        }
        return false;
    }

    public static ValidateCreateRecipe getInstance() {
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final ValidateCreateRecipe INSTANCE = new ValidateCreateRecipe();
    }
}
