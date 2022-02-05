package by.ghoncharko.webproject.validator;

import by.ghoncharko.webproject.entity.User;

import javax.persistence.criteria.CriteriaBuilder;

public class ValidateAddToUserOrder {
    private ValidateAddToUserOrder(){

    }

   public   boolean validate(User user, Integer drugId, Integer drugCount){
        return user != null && drugId != null && drugId >= 0 && drugCount != null && drugCount >= 0;
    }

    public static ValidateAddToUserOrder getInstance() {
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final ValidateAddToUserOrder INSTANCE = new ValidateAddToUserOrder();
    }
}
