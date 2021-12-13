package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.RecipeRequest;
import by.ghoncharko.webproject.exception.ServiceException;

import java.sql.Date;

public interface RecipeRequestService extends Service<RecipeRequest>{
    boolean createRecipeRequestByUserIdAndDrugIdWithDateStartAndDateEnd(Integer userId, Integer drugId) throws ServiceException;

    boolean acceptRecipeRequest(Integer recipeRequestId, Integer userId, Integer drugId, Date updatedDateEnd);
    boolean declineRecipeRequest(Integer recipeRequestId, Integer userId, Integer drugId);
     static RecipeRequestService getInstance() {
        return RecipeRequestServiceImpl.getInstance();
    }
}
