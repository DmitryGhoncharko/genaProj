package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.RecipeRequestService;


import java.sql.Date;
import java.util.Optional;

public class CreateRecipeRequestCommand implements Command {
    private static final String DRUG_ID_PARAM_NAME = "drugId";
    private static final String RECIPE_DATE_START_PARAM_NAME = "recipeDateStart";
    private static final String RECIPE_DATE_END_PARAM_NAME = "recipeDateEnd";
    private static final String USER_ATTRIBUTE_SESSION_NAME = "user";
    private static final String DRUG_NEED_RECIPE_PARAM_NAME = "isNeedRecipe";
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final String ERROR_MESSAGE = "Cannot create recipe request";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private CreateRecipeRequestCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession(USER_ATTRIBUTE_SESSION_NAME);
        if (userFromSession.isPresent()) {
            final User user = (User) userFromSession.get();
            final Integer drugId = Integer.valueOf(request.getParameter(DRUG_ID_PARAM_NAME));
            final boolean isNeedRecipe = Boolean.parseBoolean(request.getParameter(DRUG_NEED_RECIPE_PARAM_NAME));
            final Date dateStart = Date.valueOf(request.getParameter(RECIPE_DATE_START_PARAM_NAME));
            final Date dateEnd = Date.valueOf(request.getParameter(RECIPE_DATE_END_PARAM_NAME));
            final int userId = user.getId();
            final RecipeRequestService recipeRequestService = RecipeRequestService.getInstance();
            final boolean recipeRequestIsCreated = recipeRequestService.createRecipeRequestByUserIdAndDrugIdWithDateStartAndDateEnd(userId, drugId, dateStart, dateEnd, isNeedRecipe);
            if (recipeRequestIsCreated) {
                return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
            }

        }
        request.addAttributeToJsp(ERROR_ATTRIBUTE_NAME, ERROR_MESSAGE);
        return requestFactory.createForwardResponse(PagePath.RECIPES_PAGE_PATH);
    }

    public static CreateRecipeRequestCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final CreateRecipeRequestCommand INSTANCE = new CreateRecipeRequestCommand();
    }
}
