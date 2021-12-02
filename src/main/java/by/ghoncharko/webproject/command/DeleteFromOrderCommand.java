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
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private DeleteFromOrderCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession("user");
        if(userFromSession.isPresent()){
          final User user =(User)userFromSession.get();
            final Integer orderId = Integer.valueOf(request.getParameter(ORDER_ID_PARAM_NAME));
            final OrderService orderService = OrderService.getInstance();
            final boolean orderFromSessionIsDeleted = orderService.deleteFromOrderByOrderId(orderId);
            final boolean userRoleAsClient = user.getRole().equals(RolesHolder.CLIENT);
            if(orderFromSessionIsDeleted && userRoleAsClient){

                return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
            }
            final Integer drugId = Integer.valueOf(request.getParameter("drugId"));
            request.addAttributeToJsp(DRUG_ID_ATTRIBUTE_NAME, drugId);
            request.addAttributeToJsp(ERROR_ATTRIBUTE_NAME, ERROR_ATTRIBUTE_MESSAGE);
            return requestFactory.createForwardResponse(PagePath.ORDER_PAGE_PATH);
        }
        //error need authorize
        return requestFactory.createForwardResponse(PagePath.ORDER_PAGE_PATH);
    }

    public static DeleteFromOrderCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DeleteFromOrderCommand INSTANCE = new DeleteFromOrderCommand();
    }
}
