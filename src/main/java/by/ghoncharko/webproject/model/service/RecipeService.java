package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.Recipe;

import java.util.List;

public interface RecipeService extends Service<Recipe> {
    List<Recipe> findRecipesByUserId(Integer userId);

    static RecipeService getInstance() {
        return RecipeServiceImpl.getInstance();
    }
}
