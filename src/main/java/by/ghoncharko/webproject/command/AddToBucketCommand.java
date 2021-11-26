package by.ghoncharko.webproject.command;



import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.OrderService;
import by.ghoncharko.webproject.model.service.OrderServiceImpl;

import java.util.Optional;

public class AddToBucketCommand implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Integer drugId = Integer.valueOf(request.getParameter("drugId"));
        final Integer drugCount = Integer.valueOf(request.getParameter("countUserBuyDrugs"));
        final Double drugPrice = Double.valueOf(request.getParameter("drugPrice"));
        final boolean isNeedRecipe = Boolean.parseBoolean(request.getParameter("drugNeedRecipe"));
        final Optional<Object> userFromSession = request.retrieveFromSession("user");
        if (userFromSession.isPresent()) {
            final User user = (User) userFromSession.get();
            final Integer userId = user.getId();
            final OrderService orderService = OrderServiceImpl.getInstance();
            final boolean isCreated = orderService.createOrderWithStatusActive(userId, drugId, drugCount, drugPrice, isNeedRecipe);
            //show error message on jsp page

            if (isCreated) {
                return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
            }
        }
//        request.addAttributeToJsp("error","cannot add to bucket");
        return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
    }

    public static AddToBucketCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final AddToBucketCommand INSTANCE = new AddToBucketCommand();
    }
}
