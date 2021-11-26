package by.ghoncharko.webproject.command;



import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.model.service.DrugServiceImpl;

import java.util.List;

public class ShowPreparatesPageComand implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    public CommandResponse execute(CommandRequest request) {
        final DrugServiceImpl drugService = new DrugServiceImpl();
        final List<Drug> drugList = drugService.findAllWhereCountMoreThanZero();
        request.addAttributeToJsp("drugs", drugList);
        return requestFactory.createForwardResponse(PagePath.PREPARATES_PAGE_PATH);
    }

    public static ShowPreparatesPageComand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ShowPreparatesPageComand INSTANCE = new ShowPreparatesPageComand();
    }
}
