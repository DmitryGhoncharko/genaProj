package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.model.service.RecipeService;

import java.sql.Date;

public class CreateRecipeToUserCommand implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private CreateRecipeToUserCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Integer userId = Integer.valueOf(request.getParameter("userId"));
        Integer drugId = Integer.valueOf(request.getParameter("drugId"));
        Date dateEnd = Date.valueOf(request.getParameter("dateEnd"));
        RecipeService recipeService = RecipeService.getInstance();
        boolean recipeIsCreated =  recipeService.createRecipeByUserIdAndDrugId(userId,drugId,dateEnd);
        if(recipeIsCreated){
            return  requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
        }
        //error
        return requestFactory.createForwardResponse(PagePath.PREPARATES_WHICH_NEED_RECIPE_PAGE_PATH);
    }

    public static CreateRecipeToUserCommand getInstance() {

        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final CreateRecipeToUserCommand INSTANCE = new CreateRecipeToUserCommand();
    }
}
