package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.RecipeRequestService;
import by.ghoncharko.webproject.model.service.RecipeRequestServiceImpl;


import java.sql.Date;
import java.util.Optional;

public class CreateRecipeRequestCommand implements Command{
    private static final String DRUG_ID_PARAM_NAME = "drugId";
    private static final String RECIPE_DATE_START_PARAM_NAME = "recipeDateStart";
    private static final String RECIPE_DATE_END_PARAM_NAME = "recipeDateEnd";
    private static final String USER_ATTRIBUTE_SESSION_NAME = "user";
    private static final String DRUG_NEED_RECIPE_PARAM_NAME = "needRecipe";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    public CommandResponse execute(CommandRequest request) {
        Integer drugId = Integer.valueOf(request.getParameter(DRUG_ID_PARAM_NAME));
        boolean isNeedRecipe = Boolean.parseBoolean(request.getParameter(DRUG_NEED_RECIPE_PARAM_NAME));
        Date dateStart = Date.valueOf(request.getParameter(RECIPE_DATE_START_PARAM_NAME));
        Date dateEnd = Date.valueOf(request.getParameter(RECIPE_DATE_END_PARAM_NAME));
        Optional<Object> userFromSession = request.retrieveFromSession(USER_ATTRIBUTE_SESSION_NAME);

        if(userFromSession.isPresent()){
            User user =(User)userFromSession.get();
            int userId = user.getId();
            RecipeRequestService recipeRequestService = RecipeRequestServiceImpl.getInstance();
            recipeRequestService.createRecipeRequestByUserIdAndDrugIdWithDateStartAndDateEnd(userId,drugId,dateStart,dateEnd, isNeedRecipe);
            return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
        }
        //TODO ADD ERRRO MESSAGE
        return null;
    }
    public static CreateRecipeRequestCommand getInstance(){
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final CreateRecipeRequestCommand INSTANCE  = new CreateRecipeRequestCommand();
    }
}
