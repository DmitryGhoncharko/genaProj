package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.service.OrderService;
import by.ghoncharko.webproject.model.service.OrderServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class AddDrugToOrderCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(AddDrugToOrderCommand.class);
    private final RequestFactory requestFactory = RequestFactory.getInstance();
    private AddDrugToOrderCommand(){

    }
    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession("user");
        if(userFromSession.isPresent()){
            final User user =(User)userFromSession.get();
            final Integer drugId = Integer.valueOf(request.getParameter("drugId"));
            final Integer drugCount = Integer.valueOf(request.getParameter("drugCount"));
            final OrderService orderService = OrderServiceImpl.getInstance();
            try{
                //todo Сделать перенаправление на страницу заказа
                final boolean wasAddedToOrder = orderService.addDrugToOrder(user,drugId,drugCount);
                if(wasAddedToOrder){
                    return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
                    //todo сообщение о успехе
                }

            }catch (ServiceException e){
                LOG.error("Cannot add drug to order");
                return requestFactory.createForwardResponse(PagePath.ERROR_PAGE_PATH);
            }
        }
        //todo сообщение о неудаче
        return requestFactory.createForwardResponse(PagePath.PREPARATES_PAGE_PATH);
    }

    public static AddDrugToOrderCommand getInstance() {
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final AddDrugToOrderCommand INSTANCE = new AddDrugToOrderCommand();
    }
}
