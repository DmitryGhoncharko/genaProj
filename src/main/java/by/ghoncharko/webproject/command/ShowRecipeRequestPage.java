package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.RecipeRequest;
import by.ghoncharko.webproject.model.service.RecipeRequestService;

import java.util.List;

public class ShowRecipeRequestPage implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowRecipeRequestPage() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final RecipeRequestService recipeRequestService = RecipeRequestService.getInstance();
        final List<RecipeRequest> recipeRequestList = recipeRequestService.findAll();
        request.addAttributeToJsp("recipeRequests", recipeRequestList);
        return requestFactory.createForwardResponse(PagePath.RECIPE_REQUEST_PAGE_PATH);
    }

    public static ShowRecipeRequestPage getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ShowRecipeRequestPage INSTANCE = new ShowRecipeRequestPage();
    }
}

