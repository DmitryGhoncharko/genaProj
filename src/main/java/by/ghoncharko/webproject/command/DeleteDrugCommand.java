package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.RolesHolder;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.DrugService;


import java.util.Optional;

public class DeleteDrugCommand implements Command {
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final String ERROR_MESSAGE = "Cannot delete drug";
    private static final String USER_FROM_SESSION_ATTRIBUTE_NAME = "user";
    private static final String DRUG_ID_PARAM_NAME = "drugId";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private DeleteDrugCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession(USER_FROM_SESSION_ATTRIBUTE_NAME);
        if (userFromSession.isPresent()) {
            final User user = (User) userFromSession.get();
            final boolean userRoleAsPharmacy = user.getRole().equals(RolesHolder.PHARMACY);
            if (userRoleAsPharmacy) {
                final int drugId = Integer.parseInt(request.getParameter(DRUG_ID_PARAM_NAME));
                final DrugService drugService = DrugService.getInstance();
                final boolean drugIsDeleted = drugService.deleteByDrugId(drugId);
                if (drugIsDeleted) {
                    return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
                }
            }
        }

        request.addAttributeToJsp(ERROR_ATTRIBUTE_NAME, ERROR_MESSAGE);
        return requestFactory.createForwardResponse(PagePath.PREPARATES_PAGE_PATH);
    }

    public static DeleteDrugCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DeleteDrugCommand INSTANCE = new DeleteDrugCommand();
    }
}
