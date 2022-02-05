package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.service.OrderService;

import javax.sql.rowset.serial.SerialException;
import java.util.Optional;

public class AddToOrderCommand implements Command {
     private static final String DRUG_ID_PARAM_NAME = "drugId";
    private static final String COUNT_USER_BUY_DRUGS_PARAM_NAME = "countUserBuyDrugs";
    private static final String DRUG_PRICE_PARAM_NAME = "drugPrice";
    private static final String DRUG_NEED_RECIPE_PARAM_NAME = "drugNeedRecipe";
    private static final String USER_FROM_SESSION_ATTRIBUTE_NAME = "user";
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final String ERROR_ATTRIBUTE_MESSAGE = "Cannot add to order";
    private static final String DRUG_ID_ATTRIBUTE_NAME = "drugId";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private AddToOrderCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession(USER_FROM_SESSION_ATTRIBUTE_NAME);
        final int drugId = Integer.parseInt(request.getParameter(DRUG_ID_PARAM_NAME));
        if (userFromSession.isPresent()) {
            final User user = (User) userFromSession.get();
            final int drugCount = Integer.parseInt(request.getParameter(COUNT_USER_BUY_DRUGS_PARAM_NAME));
            final int userId = user.getId();
            final OrderService orderService = OrderService.getInstance();
            try{
                final boolean addedToOrder = orderService.createOrderWithStatusActive(userId, drugId, drugCount);
                if (addedToOrder) {
                    return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
                }
            }catch (ServiceException e){
                return requestFactory.createForwardResponse(PagePath.ERROR_PAGE_PATH);
            }
        }
        request.addAttributeToJsp(ERROR_ATTRIBUTE_NAME, ERROR_ATTRIBUTE_MESSAGE);
        request.addAttributeToJsp(DRUG_ID_ATTRIBUTE_NAME, drugId);
        return requestFactory.createForwardResponse("/controller?command=preparates");
    }

    public static AddToOrderCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final AddToOrderCommand INSTANCE = new AddToOrderCommand();
    }
}
