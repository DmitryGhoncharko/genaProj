package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.Recipe;

import java.sql.Date;
import java.util.List;

public interface RecipeService extends Service<Recipe> {
    List<Recipe> findRecipesByUserId(Integer userId);

    static RecipeService getInstance() {
        return RecipeServiceImpl.getInstance();
    }

    boolean createRecipeByUserIdAndDrugId(Integer userId, Integer drugId,Date dateEnd);
}
