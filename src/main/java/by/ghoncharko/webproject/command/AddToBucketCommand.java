package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.RolesHolder;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.OrderService;

import java.util.Optional;

public class AddToBucketCommand implements Command {
    private static final String DRUG_ID_PARAM_NAME = "drugId";
    private static final String COUNT_USER_BUY_DRUGS_PARAM_NAME = "countUserBuyDrugs";
    private static final String DRUG_PRICE_PARAM_NAME = "drugPrice";
    private static final String DRUG_NEED_RECIPE_PARAM_NAME = "drugNeedRecipe";
    private static final String USER_ATTRIBUTE_NAME = "user";
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final String ERROR_ATTRIBUTE_MESSAGE = "need recipe";
    private static final String DRUG_ID_ATTRIBUTE_NAME = "drugId";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private AddToBucketCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession(USER_ATTRIBUTE_NAME);
        if (userFromSession.isPresent()) {
            final int drugId = Integer.parseInt(request.getParameter(DRUG_ID_PARAM_NAME));
            final int drugCount = Integer.parseInt(request.getParameter(COUNT_USER_BUY_DRUGS_PARAM_NAME));
            final double drugPrice = Double.parseDouble(request.getParameter(DRUG_PRICE_PARAM_NAME));
            final boolean isNeedRecipe = Boolean.parseBoolean(request.getParameter(DRUG_NEED_RECIPE_PARAM_NAME));
            final User user = (User) userFromSession.get();
            final int userId = user.getId();
            final OrderService orderService = OrderService.getInstance();
            final boolean isCreated = orderService.createOrderWithStatusActive(userId, drugId, drugCount, drugPrice, isNeedRecipe);
            final boolean userRoleAsClient = user.getRole().equals(RolesHolder.CLIENT);
            if (isCreated && userRoleAsClient) {
                return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
            }

            request.addAttributeToJsp(ERROR_ATTRIBUTE_NAME, ERROR_ATTRIBUTE_MESSAGE);
            request.addAttributeToJsp(DRUG_ID_ATTRIBUTE_NAME, drugId);
            return requestFactory.createForwardResponse("/controller?command=preparates");
        }
        //error nedd authorize as client
        return requestFactory.createForwardResponse("/controller?command=preparates");
    }

    public static AddToBucketCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final AddToBucketCommand INSTANCE = new AddToBucketCommand();
    }
}
