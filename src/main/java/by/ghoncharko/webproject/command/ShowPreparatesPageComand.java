package by.ghoncharko.webproject.command;


import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.RolesHolder;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;
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

        try {
            if (userFromSession.isPresent()) {
                final User user = (User) userFromSession.get();
                boolean userRoleAsClient = user.getRole().equals(RolesHolder.CLIENT);
                boolean userRoleAsPahrmacy = user.getRole().equals(RolesHolder.PHARMACY);
                if (userRoleAsClient) {
                    final int maxPagesCount = drugService.findMaxCountPagesForAllWhereCountMoreThanZeroWithStatusActiveByUserId(user.getId());
                    final Integer pageNumber = Integer.valueOf(request.getParameter("page"));
                    final List<Drug> drugList = drugService.findAllWhereCountMoreThanZeroByUserIdAndCalculateCountLimitOffsetPagination(user.getId(),pageNumber);
                    request.addAttributeToJsp("maxPagesCount",maxPagesCount);
                    request.addAttributeToJsp(DRUGS_ATTRIBUTE_NAME, drugList);
                    return requestFactory.createForwardResponse(PagePath.PREPARATES_PAGE_PATH);
                }
                if (userRoleAsPahrmacy) {
                    final int maxPagesCount = drugService.findMaxCountPagesForAllDrugs();
                    final Integer pageNumber = Integer.valueOf(request.getParameter("page"));
                    final List<Drug> drugList = drugService.findAllLimitOffsetPagination(pageNumber);
                    request.addAttributeToJsp("maxPagesCount", maxPagesCount);
                    request.addAttributeToJsp("drugs", drugList);
                    return requestFactory.createForwardResponse(PagePath.PREPARATES_PAGE_PATH);
                }
            }
            final Integer currentPageNumber = Integer.valueOf(request.getParameter("page"));
            final int maxPagesCount = drugService.findMaxCountPagesForAllWhereCountMoreThanZero();
           final List<Drug> drugList = drugService.findAllWhereCountMoreThanZeroLimitOffsetPagination(currentPageNumber);
           request.addAttributeToJsp("currentPageNumber",currentPageNumber);
           request.addAttributeToJsp("maxPagesCount",maxPagesCount);
            request.addAttributeToJsp("drugs", drugList);
            return requestFactory.createForwardResponse(PagePath.PREPARATES_PAGE_PATH);
        } catch (ServiceException e) {
            return requestFactory.createForwardResponse(PagePath.ERROR_PAGE_PATH);
        }
    }

    public static ShowPreparatesPageComand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ShowPreparatesPageComand INSTANCE = new ShowPreparatesPageComand();
    }
}
