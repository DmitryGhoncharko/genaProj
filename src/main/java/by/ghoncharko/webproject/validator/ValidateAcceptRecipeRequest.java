package by.ghoncharko.webproject.validator;

import java.sql.Date;

public class ValidateAcceptRecipeRequest {
    private ValidateAcceptRecipeRequest() {
    }

    public boolean validate(Integer recipeRequestId, Integer userId, Integer drugId, Date updateDateEnd) {
        if (recipeRequestId != null && userId != null && drugId != null && updateDateEnd != null) {
            return updateDateEnd.after(new Date(new java.util.Date().getDate()));
        }
        return false;
    }
    
    public static ValidateAcceptRecipeRequest getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ValidateAcceptRecipeRequest INSTANCE = new ValidateAcceptRecipeRequest();
    }
}
