package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.model.service.RecipeRequestService;

import java.sql.Date;

public class AcceptRecipeRequestCommand implements Command {
    private static final String RECIPE_REQUEST_ID_PARAM_NAME = "recipeRequestId";
    private static final String USER_ID_PARAM_NAME = "userId";
    private static final String DRUG_ID_PARAM_NAME = "drugId";
    private static final String UPDATED_DATE_END_PARAM_NAME = "updatedDateEnd";
    private static final String ERROR_ATRIBUTE_NAME = "error";
    private static final String ERROR_MESSAGE = "err";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private AcceptRecipeRequestCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Integer recipeRequestId = Integer.valueOf(request.getParameter(RECIPE_REQUEST_ID_PARAM_NAME));
        final Integer userId = Integer.valueOf(request.getParameter(USER_ID_PARAM_NAME));
        final Integer drugId = Integer.valueOf(request.getParameter(DRUG_ID_PARAM_NAME));
        final Date updatedDateEnd = Date.valueOf(request.getParameter(UPDATED_DATE_END_PARAM_NAME));
        final RecipeRequestService recipeRequestService = RecipeRequestService.getInstance();
        final boolean recipeRequestIsAccepted = recipeRequestService.acceptRecipeRequest(recipeRequestId, userId, drugId, updatedDateEnd);
        if (recipeRequestIsAccepted) {
            return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
        }
        request.addAttributeToJsp(ERROR_ATRIBUTE_NAME, ERROR_MESSAGE);
        return requestFactory.createForwardResponse(PagePath.RECIPE_REQUEST_PAGE_PATH);
    }

    public static AcceptRecipeRequestCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final AcceptRecipeRequestCommand INSTANCE = new AcceptRecipeRequestCommand();
    }
}
