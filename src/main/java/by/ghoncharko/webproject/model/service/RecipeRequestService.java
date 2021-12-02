package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.RecipeRequest;

import java.sql.Date;

public interface RecipeRequestService extends Service<RecipeRequest>{
    boolean createRecipeRequestByUserIdAndDrugIdWithDateStartAndDateEnd(Integer userId, Integer drugId, Date dateStart, Date dateEnd, boolean isNeedRecipe);

     static RecipeRequestService getInstance() {
        return RecipeRequestServiceImpl.getInstance();
    }
}
