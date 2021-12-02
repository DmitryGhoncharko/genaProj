package by.ghoncharko.webproject.command;


import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.RolesHolder;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.DrugService;


import java.util.List;
import java.util.Optional;

public class ShowPreparatesPageComand implements Command {
    private static final String USER_aTTRIBUTE_NAME = "user";
    private static final String DRUGS_ATTRIBUTE_NAME = "drugs";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowPreparatesPageComand() {

    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession(USER_aTTRIBUTE_NAME);
        final DrugService drugService = DrugService.getInstance();
        if (userFromSession.isPresent()) {
            final User user = (User) userFromSession.get();
            boolean userRoleAsClient = user.getRole().equals(RolesHolder.CLIENT);
            boolean userRoleAsPahrmacy = user.getRole().equals(RolesHolder.PHARMACY);
            if (userRoleAsClient) {
                final List<Drug> drugList = drugService.findAllWhereCountMoreThanZeroByUserId(user.getId());
                request.addAttributeToJsp(DRUGS_ATTRIBUTE_NAME, drugList);
                return requestFactory.createForwardResponse(PagePath.PREPARATES_PAGE_PATH);
            }
            if (userRoleAsPahrmacy) {
                List<Drug> drugList = drugService.findAllWhereCountMoreThanZero();
                request.addAttributeToJsp("drugs", drugList);
                return requestFactory.createForwardResponse(PagePath.PREPARATES_PAGE_PATH);
            }
        }
        List<Drug> drugList = drugService.findAllWhereCountMoreThanZero();
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
