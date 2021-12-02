package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.DrugService;
import by.ghoncharko.webproject.model.service.DrugServiceImpl;

import java.util.Optional;

public class DeleteDrugCommand implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private DeleteDrugCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession("user");
        if (userFromSession.isPresent()) {
            final int drugId = Integer.valueOf(request.getParameter("drugId"));
            final DrugService drugService = new DrugServiceImpl();
            final boolean isDeleted = drugService.deleteByDrugId(drugId);
            if (isDeleted) {
                return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
            }
        }
        return null;
    }
}
