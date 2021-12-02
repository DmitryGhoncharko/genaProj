package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.entity.Order;
import by.ghoncharko.webproject.entity.RolesHolder;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.BankCardService;
import by.ghoncharko.webproject.model.service.OrderService;


import java.util.List;
import java.util.Optional;

public class ShowOrderPageCommand implements Command {
    private static final String USER_ATTRIBUTE_NAME = "user";
    private static final String BANK_CARDS_ATTRIBUTE_NAME = "bankCards";
    private static final String ORDERS_ATTRIBUTE_NAME = "orders";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowOrderPageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession(USER_ATTRIBUTE_NAME);
        if(userFromSession.isPresent()){
            final User user = (User) userFromSession.get();
            final int userId = user.getId();
            final OrderService orderService = OrderService.getInstance();
            final BankCardService bankCardService = BankCardService.getInstance();
            final List<BankCard> bankCardList = bankCardService.getBankCardsByUserId(userId);
            final List<Order> orderList = orderService.findAllWithStatusActive(userId);
            final boolean userRoleAsClient = user.getRole().equals(RolesHolder.CLIENT);
            if(userRoleAsClient){
                request.addAttributeToJsp(BANK_CARDS_ATTRIBUTE_NAME, bankCardList);
                request.addAttributeToJsp(ORDERS_ATTRIBUTE_NAME, orderList);
                return requestFactory.createForwardResponse(PagePath.ORDER_PAGE_PATH);
            }
        }
        return requestFactory.createForwardResponse(PagePath.INDEX_PATH);
    }

    public static ShowOrderPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ShowOrderPageCommand INSTANCE = new ShowOrderPageCommand();
    }
}
