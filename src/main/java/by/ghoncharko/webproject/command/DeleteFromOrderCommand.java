package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.model.service.OrderService;
import by.ghoncharko.webproject.model.service.OrderServiceImpl;

public class DeleteFromOrderCommand implements Command{
private final  RequestFactory requestFactory = RequestFactory.getInstance();
    @Override
    public CommandResponse execute(CommandRequest request) {
        Integer orderId = Integer.valueOf(request.getParameter("orderId"));
        OrderService orderService = new OrderServiceImpl();
        if(orderService.deleteFromOrderByOrderId(orderId)){
            return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
        }
        final Integer drugId = Integer.valueOf(request.getParameter("drugId"));
        request.addAttributeToJsp("drugId",drugId);
        request.addAttributeToJsp("errorDelete","cannod delete from order");
        return  requestFactory.createForwardResponse(PagePath.ORDER_PAGE_PATH);
    }
    public static DeleteFromOrderCommand getInstance(){
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final DeleteFromOrderCommand INSTANCE = new DeleteFromOrderCommand();
    }
}
