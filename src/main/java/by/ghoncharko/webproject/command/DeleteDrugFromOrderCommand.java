package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.service.OrderService;
import by.ghoncharko.webproject.model.service.OrderServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.Optional;

public class DeleteDrugFromOrderCommand implements Command{
    private static final Logger LOG  = LogManager.getLogger(DeleteDrugFromOrderCommand.class);
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private DeleteDrugFromOrderCommand(){

    }
    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession("user");
        if(userFromSession.isPresent()){
            final User user =(User) userFromSession.get();
            final OrderService orderService = OrderServiceImpl.getInstance();
            final Integer drugId = Integer.valueOf(request.getParameter("drugId"));
            final Integer drugCount = Integer.valueOf(request.getParameter("drugCount"));
            final Integer drugUserOrderId = Integer.valueOf(request.getParameter("drugUserOrderId"));
            try{
                orderService.deleteSomeCountDrugFromOrder(user,drugId,drugCount,drugUserOrderId);
                return  requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
            }catch (ServiceException e){
                LOG.error("ccc");
            }
        }
        return requestFactory.createForwardResponse(PagePath.INDEX_PATH);
    }

    public static DeleteDrugFromOrderCommand getInstance() {
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final DeleteDrugFromOrderCommand INSTANCE = new DeleteDrugFromOrderCommand();
    }
}
