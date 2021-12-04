package by.ghoncharko.webproject.command;


import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.Recipe;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.RecipeService;

import java.util.List;
import java.util.Optional;

public class ShowRecipesPageCommand implements Command {
    private static final String USER_ATTRIBUTE_NAME = "user";
    private static final String RECIPES_ATTRIBUTE_NAME = "recipes";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowRecipesPageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession(USER_ATTRIBUTE_NAME);
        if (userFromSession.isPresent()) {
            final RecipeService recipeService = RecipeService.getInstance();
            final User user = (User) userFromSession.get();
            final int userId = user.getId();
            final List<Recipe> recipeList = recipeService.findRecipesByUserId(userId);
            request.addAttributeToJsp(RECIPES_ATTRIBUTE_NAME, recipeList);
            return requestFactory.createForwardResponse(PagePath.RECIPES_PAGE_PATH);
        }
        return requestFactory.createForwardResponse(PagePath.INDEX_PATH);
    }

    public static ShowRecipesPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ShowRecipesPageCommand INSTANCE = new ShowRecipesPageCommand();
    }
}
