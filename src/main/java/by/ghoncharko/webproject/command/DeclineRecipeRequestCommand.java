package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.model.service.RecipeRequestService;

public class DeclineRecipeRequestCommand implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private DeclineRecipeRequestCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
      final   Integer recipeRequestId = Integer.valueOf(request.getParameter("recipeRequestId"));
      final  Integer userId = Integer.valueOf(request.getParameter("userId"));
      final Integer drugId = Integer.valueOf(request.getParameter("drugId"));
      final RecipeRequestService recipeRequestService = RecipeRequestService.getInstance();
      final boolean recipeRequestIsDecline = recipeRequestService.declineRecipeRequest(recipeRequestId,userId,drugId);
       if(recipeRequestIsDecline){
           return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
       }
       request.addAttributeToJsp("error","err");
       return requestFactory.createForwardResponse(PagePath.RECIPE_REQUEST_PAGE_PATH);
    }

    public static DeclineRecipeRequestCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DeclineRecipeRequestCommand INSTANCE = new DeclineRecipeRequestCommand();
    }
}
