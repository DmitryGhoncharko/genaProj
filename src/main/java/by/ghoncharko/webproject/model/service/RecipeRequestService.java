package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.RecipeRequest;
import by.ghoncharko.webproject.entity.User;

public interface RecipeRequestService extends Service<RecipeRequest> {
    boolean createRecipeRequest(User user, Integer recipeId);
}
