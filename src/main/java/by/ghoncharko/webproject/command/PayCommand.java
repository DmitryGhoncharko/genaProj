package by.ghoncharko.webproject.command;


import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.BankCardService;
import by.ghoncharko.webproject.model.service.BankCardServiceImpl;
import by.ghoncharko.webproject.model.service.OrderService;
import by.ghoncharko.webproject.model.service.OrderServiceImpl;

import java.util.Optional;

public class PayCommand implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    public CommandResponse execute(CommandRequest request) {
        Integer orderId = Integer.valueOf(request.getParameter("orderId"));
        boolean isNeedRecipe = Boolean.parseBoolean(request.getParameter("isNeedRecipe"));
        Integer drugId = Integer.valueOf(request.getParameter("drugId"));
        Integer orderCount = Integer.valueOf(request.getParameter("orderCount"));
        Optional<Object> userFromSession = request.retrieveFromSession("user");
        User user = (User) userFromSession.get();
        Double finalPrice = Double.valueOf(request.getParameter("orderFinalPrice"));
        Integer bankCardId = Integer.valueOf(request.getParameter("cardId"));
        BankCardService bankCardService = new BankCardServiceImpl();
        OrderService orderService = new OrderServiceImpl();
        Optional<BankCard> bankCard = bankCardService.getBankCardsByCardId(bankCardId);
        if (bankCard.isPresent()) {
            boolean isPayed = orderService.pay(user.getId(), drugId, isNeedRecipe, orderCount, finalPrice, orderId, bankCardId);
            if (isPayed) {
                //todo new pagePath to currentPage
                return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
            }
        }
        //todo new pagePath to currentPage and show error add bank card
        request.addAttributeToJsp("error", "something wrong");
        return requestFactory.createForwardResponse("/controller?command=order");

    }

    public static PayCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final PayCommand INSTANCE = new PayCommand();
    }
}
