package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.model.service.DrugService;

import java.sql.Date;
import java.util.List;

public class ShowPreparatesWhichNeedRecipePage implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowPreparatesWhichNeedRecipePage() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final DrugService drugService = DrugService.getInstance();
        final List<Drug> drugList = drugService.findAllWhereNeedRecipe();
        final Integer userId = Integer.valueOf(request.getParameter("userId"));
        final String userFirstName = request.getParameter("userFirstName");
        final String userLastName = request.getParameter("userLastName");
        final Date currentDate = new Date(new java.util.Date().getTime());
        request.addAttributeToJsp("currentDate",currentDate);
        request.addAttributeToJsp("userId",userId);
        request.addAttributeToJsp("userFirstName",userFirstName);
        request.addAttributeToJsp("userLastName",userLastName);
        request.addAttributeToJsp("drugs", drugList);
        return requestFactory.createForwardResponse(PagePath.PREPARATES_WHICH_NEED_RECIPE_PAGE_PATH);
    }

    public static ShowPreparatesWhichNeedRecipePage getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ShowPreparatesWhichNeedRecipePage INSTANCE = new ShowPreparatesWhichNeedRecipePage();
    }
}
