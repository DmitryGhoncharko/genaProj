package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.RolesHolder;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.DrugService;

import java.util.Optional;

public class CreateDrugCommand implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private CreateDrugCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession("user");
        if (userFromSession.isPresent()) {
            final User user = (User) userFromSession.get();
            final DrugService drugService = DrugService.getInstance();
            final String drugName = request.getParameter("drugName");
            final double drugPrice = Double.parseDouble(request.getParameter("drugPrice"));
            final int drugCount = Integer.parseInt(request.getParameter("drugCount"));
            final String drugDescription = request.getParameter("drugDescription");
            final String drugProducerName = request.getParameter("drugProducerName");
            final boolean drugNeedRecipe = Boolean.parseBoolean(request.getParameter("drugNeedRecipe"));
            final boolean isCreated = drugService.create(drugName, drugNeedRecipe, drugCount, drugPrice, drugDescription, drugProducerName);
            final boolean userRoleAsPharmacy = user.getRole().equals(RolesHolder.PHARMACY);
            if (isCreated && userRoleAsPharmacy) {
                drugService.create(drugName,drugNeedRecipe,drugCount,drugPrice,drugDescription,drugProducerName);
                requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
            }
            //error cannot create drug
            return requestFactory.createForwardResponse(PagePath.CREATE_DRUG_PAGE_PATH);

        }

        //need authorize as pharmacy
        return requestFactory.createForwardResponse(PagePath.CREATE_DRUG_PAGE_PATH);
    }

    public static CreateDrugCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final CreateDrugCommand INSTANCE = new CreateDrugCommand();
    }
}
