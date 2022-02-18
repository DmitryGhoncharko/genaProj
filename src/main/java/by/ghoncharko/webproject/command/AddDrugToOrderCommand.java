package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class AddDrugToOrderCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(AddDrugToOrderCommand.class);
    private final RequestFactory requestFactory;
    private final OrderService orderService;
    public AddDrugToOrderCommand(RequestFactory requestFactory, OrderService orderService){
        this.requestFactory = requestFactory;
        this.orderService = orderService;
    }
    @Override
    public CommandResponse execute(CommandRequest request) throws ServiceException {
        final Optional<Object> userFromSession = request.retrieveFromSession("user");
        if(userFromSession.isPresent()){
            final User user =(User)userFromSession.get();
            final Integer drugId = Integer.valueOf(request.getParameter("drugId"));
            final Integer drugCount = Integer.valueOf(request.getParameter("countUserAddDrugsToOrder"));
            try{
                //todo Сделать перенаправление на страницу заказа
                final boolean wasAddedToOrder = orderService.addDrugToOrder(user,drugId,drugCount);
                if(wasAddedToOrder){
                    return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
                    //todo сообщение о успехе
                }

            }catch (ServiceException e){
                LOG.error("Cannot add drug to order");
                throw e;
            }
        }
        //todo сообщение о неудаче
        return requestFactory.createForwardResponse(PagePath.PREPARATES_PAGE_PATH);
    }
}
