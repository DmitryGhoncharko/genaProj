package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.RolesHolder;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.DrugService;


import java.util.Optional;

public class DeleteDrugCommand implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private DeleteDrugCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession("user");
        if (userFromSession.isPresent()) {
            final User user = (User)userFromSession.get();
            final int drugId = Integer.parseInt(request.getParameter("drugId"));
            final DrugService drugService = DrugService.getInstance();
            final boolean isDeleted = drugService.deleteByDrugId(drugId);
            final boolean userRoleAsPharmacy = user.getRole().equals(RolesHolder.PHARMACY);
            if (isDeleted && userRoleAsPharmacy) {
                return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
            }
            //error cannot delete
            return requestFactory.createForwardResponse(PagePath.PREPARATES_PAGE_PATH);
        }
        //error need authorize
        return requestFactory.createForwardResponse(PagePath.PREPARATES_PAGE_PATH);
    }
}
