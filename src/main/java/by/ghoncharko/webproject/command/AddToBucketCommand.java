package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.OrderService;
import by.ghoncharko.webproject.model.service.OrderServiceImpl;

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

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Integer drugId = Integer.valueOf(request.getParameter(DRUG_ID_PARAM_NAME));
        final Integer drugCount = Integer.valueOf(request.getParameter(COUNT_USER_BUY_DRUGS_PARAM_NAME));
        final Double drugPrice = Double.valueOf(request.getParameter(DRUG_PRICE_PARAM_NAME));
        final boolean isNeedRecipe = Boolean.parseBoolean(request.getParameter(DRUG_NEED_RECIPE_PARAM_NAME));
        final Optional<Object> userFromSession = request.retrieveFromSession(USER_ATTRIBUTE_NAME);
        if (userFromSession.isPresent()) {
            final User user = (User) userFromSession.get();
            final Integer userId = user.getId();
            final OrderService orderService = OrderServiceImpl.getInstance();
            final boolean isCreated = orderService.createOrderWithStatusActive(userId, drugId, drugCount, drugPrice, isNeedRecipe);
            //show error message on jsp page
            if (!isCreated) {
                request.addAttributeToJsp(ERROR_ATTRIBUTE_NAME, ERROR_ATTRIBUTE_MESSAGE);
                request.addAttributeToJsp(DRUG_ID_ATTRIBUTE_NAME, drugId);
                return requestFactory.createForwardResponse("/controller?command=preparates");
            }
            return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
        }
        return null;
    }

    public static AddToBucketCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final AddToBucketCommand INSTANCE = new AddToBucketCommand();
    }
}
