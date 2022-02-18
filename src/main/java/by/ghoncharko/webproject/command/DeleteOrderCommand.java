package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class DeleteOrderCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(DeleteOrderCommand.class);

    private final RequestFactory requestFactory;
    private final OrderService orderService;
    public DeleteOrderCommand(RequestFactory requestFactory, OrderService orderService){
        this.requestFactory = requestFactory;
        this.orderService = orderService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession("user");
        if(userFromSession.isPresent()){
            final User user =(User) userFromSession.get();
            final Integer userOrderId = Integer.valueOf(request.getParameter("drugUserOrderId"));
            try{
              final boolean orderIsDeleted =  orderService.deleteNotPayedOrderByOrderIdAndUserId(user,userOrderId);
              if(orderIsDeleted){
                  return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
              }
            }catch (ServiceException e){

            }

        }
        return requestFactory.createForwardResponse(PagePath.ORDER_PAGE_PATH);
    }

}
