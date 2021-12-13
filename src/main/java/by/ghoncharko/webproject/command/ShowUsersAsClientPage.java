package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.service.UserService;


import java.util.List;

public class ShowUsersAsClientPage implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowUsersAsClientPage() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final UserService userService = UserService.getInstance();
       try{
           List<User> userList = userService.findAllClients();
           request.addAttributeToJsp("users",userList);
           return requestFactory.createForwardResponse(PagePath.USERS_PAGE_PATH);
       }catch (ServiceException e){
           return requestFactory.createForwardResponse(PagePath.ERROR_PAGE_PATH);
       }
    }

    public static ShowUsersAsClientPage getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ShowUsersAsClientPage INSTANCE = new ShowUsersAsClientPage();
    }
}
