package by.ghoncharko.webproject.command;



import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.DrugServiceImpl;
import by.ghoncharko.webproject.model.service.OrderService;
import by.ghoncharko.webproject.model.service.OrderServiceImpl;

import java.util.List;
import java.util.Optional;

public class ShowPreparatesPageComand implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    public CommandResponse execute(CommandRequest request) {
        Optional<Object> user = request.retrieveFromSession("user");
        final DrugServiceImpl drugService = new DrugServiceImpl();
        User userFromSession = null;
        final OrderService orderService= new OrderServiceImpl();
        if(user.isPresent()){
            userFromSession = (User) user.get();
            final List<Drug> drugList = drugService.findAllWhereCountMoreThanZeroByUserId(userFromSession.getId());
            request.addAttributeToJsp("drugs", drugList);
            return requestFactory.createForwardResponse(PagePath.PREPARATES_PAGE_PATH);
        }
        List<Drug> drugList = drugService.findAllWhereCountMoreThanZero();
        request.addAttributeToJsp("drugs",drugList);
        return requestFactory.createForwardResponse(PagePath.PREPARATES_PAGE_PATH);
    }

    public static ShowPreparatesPageComand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ShowPreparatesPageComand INSTANCE = new ShowPreparatesPageComand();
    }
}
