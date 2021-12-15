package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.model.service.DrugService;

public class SearchDrugByNameCommand implements Command{
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    public CommandResponse execute(CommandRequest request) {
        String drugNameFromSearch = request.getParameter("drugNameFromSearch");
        DrugService drugService = DrugService.getInstance();

        return null;
    }

    public static SearchDrugByNameCommand getInstance() {
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final SearchDrugByNameCommand INSTANCE = new SearchDrugByNameCommand();
    }
}
