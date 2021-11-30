package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.RecipeRequestService;
import by.ghoncharko.webproject.model.service.RecipeRequestServiceImpl;
import by.ghoncharko.webproject.model.service.RecipeService;
import by.ghoncharko.webproject.model.service.RecipeServiceImpl;

import java.sql.Date;
import java.util.Optional;

public class CreateRecipeRequestCommand implements Command{
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    public CommandResponse execute(CommandRequest request) {
        Integer drugId = Integer.valueOf(request.getParameter("drugId"));
        Date dateStart = Date.valueOf(request.getParameter("recipeDateStart"));
        Date dateEnd = Date.valueOf(request.getParameter("recipeDateEnd"));
        Optional<Object> userFromSession = request.retrieveFromSession("user");

        if(userFromSession.isPresent()){
            User user =(User)userFromSession.get();
            int userId = user.getId();
            RecipeRequestService recipeRequestService = RecipeRequestServiceImpl.getInstance();
            recipeRequestService.createRecipeRequestByUserIdAndDrugIdWithDateStartAndDateEnd(userId,drugId,dateStart,dateEnd);
            return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
        }
        return null;
    }
    public static CreateRecipeRequestCommand getInstance(){
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final CreateRecipeRequestCommand INSTANCE  = new CreateRecipeRequestCommand();
    }
}
