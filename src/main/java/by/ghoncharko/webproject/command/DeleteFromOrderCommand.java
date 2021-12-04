package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.RolesHolder;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.OrderService;


import java.util.Optional;

public class DeleteFromOrderCommand implements Command {
    private static final String ORDER_ID_PARAM_NAME = "orderId";
    private static final String DRUG_ID_ATTRIBUTE_NAME = "drugId";
    private static final String ERROR_ATTRIBUTE_NAME = "errorDelete";
    private static final String ERROR_ATTRIBUTE_MESSAGE = "cannod delete from order";
    private static final String USER_FROM_SESSION_ATTRIBUTE_NAME = "user";
    private static final String DRUG_ID_PARAM_NAME = "drugId";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private DeleteFromOrderCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession(USER_FROM_SESSION_ATTRIBUTE_NAME);
        final Integer drugId = Integer.valueOf(request.getParameter(DRUG_ID_PARAM_NAME));
        if(userFromSession.isPresent()){
            final User user =(User)userFromSession.get();
            final boolean userRoleAsClient = user.getRole().equals(RolesHolder.CLIENT);
            if(userRoleAsClient){
                final Integer orderId = Integer.valueOf(request.getParameter(ORDER_ID_PARAM_NAME));
                final OrderService orderService = OrderService.getInstance();
                final boolean orderFromSessionIsDeleted = orderService.deleteFromOrderByOrderId(orderId);
                if(orderFromSessionIsDeleted){
                    return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
                }
            }
        }
        request.addAttributeToJsp(DRUG_ID_ATTRIBUTE_NAME, drugId);
        request.addAttributeToJsp(ERROR_ATTRIBUTE_NAME, ERROR_ATTRIBUTE_MESSAGE);
        return requestFactory.createForwardResponse(PagePath.ORDER_PAGE_PATH);
    }

    public static DeleteFromOrderCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DeleteFromOrderCommand INSTANCE = new DeleteFromOrderCommand();
    }
}
