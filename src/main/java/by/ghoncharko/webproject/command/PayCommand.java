package by.ghoncharko.webproject.command;


import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.entity.RolesHolder;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.BankCardService;

import by.ghoncharko.webproject.model.service.OrderService;


import java.util.Optional;

public class PayCommand implements Command {
    private static final String ORDER_ID_PARAM_NAME = "orderId";
    private static final String IS_NEED_RECIPE_PARAM_NAME = "isNeedRecipe";
    private static final String DRUG_ID_PARAM_NAME = "drugId";
    private static final String ORDER_COUNT_PARAM_NAME = "orderCount";
    private static final String USER_ATTRIBUTE_NAME = "user";
    private static final String ORDER_FINAL_PRICE_PARAM_NAME = "orderFinalPrice";
    private static final String CARD_ID_PARAM_NAME = "cardId";
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final String ERROR_ATTRIBUTE_MESSAGE = "something wrong";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private PayCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession(USER_ATTRIBUTE_NAME);
        if(userFromSession.isPresent()){
            final Integer orderId = Integer.valueOf(request.getParameter(ORDER_ID_PARAM_NAME));
            final boolean isNeedRecipe = Boolean.parseBoolean(request.getParameter(IS_NEED_RECIPE_PARAM_NAME));
            final Integer drugId = Integer.valueOf(request.getParameter(DRUG_ID_PARAM_NAME));
            final Integer orderCount = Integer.valueOf(request.getParameter(ORDER_COUNT_PARAM_NAME));
            final User user = (User) userFromSession.get();
            final Double finalPrice = Double.valueOf(request.getParameter(ORDER_FINAL_PRICE_PARAM_NAME));
            final Integer bankCardId = Integer.valueOf(request.getParameter(CARD_ID_PARAM_NAME));
            final BankCardService bankCardService = BankCardService.getInstance();
            final OrderService orderService = OrderService.getInstance();
            final Optional<BankCard> bankCard = bankCardService.getBankCardsByCardId(bankCardId);
            final boolean userRoleAsClient = user.getRole().equals(RolesHolder.CLIENT);
            if (bankCard.isPresent() && userRoleAsClient) {
                final boolean isPayed = orderService.pay(user.getId(), drugId, isNeedRecipe, orderCount, finalPrice, orderId, bankCardId);
                if (isPayed) {
                    //todo new pagePath to currentPage
                    return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
                }
            }
        }

        //todo new pagePath to currentPage and show error add bank card
        request.addAttributeToJsp(ERROR_ATTRIBUTE_NAME, ERROR_ATTRIBUTE_MESSAGE);
        return requestFactory.createForwardResponse("/controller?command=order");

    }

    public static PayCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final PayCommand INSTANCE = new PayCommand();
    }
}
