package by.ghoncharko.webproject.command;



import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.Order;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.OrderServiceImpl;

import java.util.List;
import java.util.Optional;

public class ShowOrderPageCommand implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession("user");
        User user = (User) userFromSession.get();
        Integer userId = user.getId();
        OrderServiceImpl orderService = new OrderServiceImpl();

        List<Order> orderList = orderService.findAllWithStatusActive(userId);
        request.addAttributeToJsp("orders", orderList);
        return requestFactory.createForwardResponse(PagePath.ORDER_PAGE_PATH);
    }

    public static ShowOrderPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ShowOrderPageCommand INSTANCE = new ShowOrderPageCommand();
    }
}
