package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.RecipeService;

import java.util.Optional;

public class DeleteRecipeCommand implements Command{
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    public CommandResponse execute(CommandRequest request) {
      final   Optional<Object> user = request.retrieveFromSession("user");
        if(user.isPresent()){
           final User userFromSession =(User)user.get();
           final Integer userId = userFromSession.getId();
           final Integer drugId = Integer.valueOf(request.getParameter("drugId"));
            final RecipeService recipeService = RecipeService.getInstance();
            final  boolean isDeleted = recipeService.deleteRecipeByUserIdAndDrugId(userId,drugId);
            if(isDeleted){
                return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
            }
        }
        return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
    }

    public static DeleteRecipeCommand getInstance() {
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final DeleteRecipeCommand INSTANCE  = new DeleteRecipeCommand();
    }
}
