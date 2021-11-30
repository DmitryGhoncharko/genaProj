package by.ghoncharko.webproject.command;


import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.Recipe;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.RecipeService;
import by.ghoncharko.webproject.model.service.RecipeServiceImpl;

import java.util.List;
import java.util.Optional;

public class ShowRecipesPageCommand implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    public CommandResponse execute(CommandRequest request) {
        RecipeService recipeService = new RecipeServiceImpl();
        Optional<Object> userFromSession = request.retrieveFromSession("user");
        if(userFromSession.isPresent()){
            User user =(User)userFromSession.get();
            int userId = user.getId();
            List<Recipe> recipeList = recipeService.findRecipesByUserId(userId);
            request.addAttributeToJsp("recipes",recipeList);
            return requestFactory.createForwardResponse(PagePath.RECIPES_PAGE_PATH);
        }
        return null;
    }

    public static ShowRecipesPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ShowRecipesPageCommand INSTANCE = new ShowRecipesPageCommand();
    }
}
