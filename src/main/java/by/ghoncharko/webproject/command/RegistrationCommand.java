package by.ghoncharko.webproject.command;


import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.UserService;

import java.util.Optional;

public class RegistrationCommand implements Command {
    private static final RequestFactory requestFactory = RequestFactory.getInstance();
    private static final String LOGIN_REQUEST_PARAM = "login";
    private static final String PASSWORD_REQUEST_PARAM = "password";
    private static final String FIRST_NAME_REQUEST_PARAM = "Lname";
    private static final String LAST_NAME_REQUEST_PARAM = "Fname";
    private static final String ERROR_REGISTRATION_ATTRIBUTE = "errorRegistrationMessage";
    private static final String ERROR_REGISTRATION_MESSAGE = "Create unique login";

    private RegistrationCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {

        final String userLogin = request.getParameter(LOGIN_REQUEST_PARAM);
        final String userPassword = request.getParameter(PASSWORD_REQUEST_PARAM);
        final String userName = request.getParameter(FIRST_NAME_REQUEST_PARAM);
        final String userSurName = request.getParameter(LAST_NAME_REQUEST_PARAM);
        final Optional<User> user = UserService.getInstance().createClient(userLogin, userPassword,
                userName, userSurName);
        if (user.isPresent()) {
            return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
        }
        request.addAttributeToJsp(ERROR_REGISTRATION_ATTRIBUTE, ERROR_REGISTRATION_MESSAGE);
        return requestFactory.createForwardResponse(PagePath.REGISTRATION_PAGE_PATH);
    }

    public static RegistrationCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final RegistrationCommand INSTANCE = new RegistrationCommand();
    }
}
