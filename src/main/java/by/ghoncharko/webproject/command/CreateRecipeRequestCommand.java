package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.RecipeRequestService;
import by.ghoncharko.webproject.model.service.RecipeRequestServiceImpl;


import java.sql.Date;
import java.util.Optional;

public class CreateRecipeRequestCommand implements Command {
    private static final String DRUG_ID_PARAM_NAME = "drugId";
    private static final String RECIPE_DATE_START_PARAM_NAME = "recipeDateStart";
    private static final String RECIPE_DATE_END_PARAM_NAME = "recipeDateEnd";
    private static final String USER_ATTRIBUTE_SESSION_NAME = "user";
    private static final String DRUG_NEED_RECIPE_PARAM_NAME = "needRecipe";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private CreateRecipeRequestCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Integer drugId = Integer.valueOf(request.getParameter(DRUG_ID_PARAM_NAME));
        final boolean isNeedRecipe = Boolean.parseBoolean(request.getParameter(DRUG_NEED_RECIPE_PARAM_NAME));
        final Date dateStart = Date.valueOf(request.getParameter(RECIPE_DATE_START_PARAM_NAME));
        final Date dateEnd = Date.valueOf(request.getParameter(RECIPE_DATE_END_PARAM_NAME));
        final Optional<Object> userFromSession = request.retrieveFromSession(USER_ATTRIBUTE_SESSION_NAME);

        if (userFromSession.isPresent()) {
            final User user = (User) userFromSession.get();
            final int userId = user.getId();
            final RecipeRequestService recipeRequestService = RecipeRequestServiceImpl.getInstance();
            recipeRequestService.createRecipeRequestByUserIdAndDrugIdWithDateStartAndDateEnd(userId, drugId, dateStart, dateEnd, isNeedRecipe);
            return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
        }
        //TODO ADD ERRRO MESSAGE
        return null;
    }

    public static CreateRecipeRequestCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final CreateRecipeRequestCommand INSTANCE = new CreateRecipeRequestCommand();
    }
}
