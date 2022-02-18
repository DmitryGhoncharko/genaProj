package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class PayOrderCommand implements Command{
    private final Logger LOG = LogManager.getLogger(PayOrderCommand.class);
    private final RequestFactory requestFactory;
    private final OrderService orderService;
    public PayOrderCommand(RequestFactory requestFactory, OrderService orderService){
        this.requestFactory = requestFactory;
        this.orderService = orderService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession("user");
        if(userFromSession.isPresent()){
            final User user =(User)userFromSession.get();
            final Integer cardId = Integer.parseInt(request.getParameter("cardId"));
            final boolean orderIsPayed = orderService.payOrder(user,cardId);
            if(orderIsPayed){
                return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
            }
        }
        return requestFactory.createForwardResponse(PagePath.ORDER_PAGE_PATH);
    }


}
