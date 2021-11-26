package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.UserService;

import java.util.Optional;

public class LoginCommand implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();
    private static final String USER_SESSION_ATTRIBUTE_NAME = "user";
    private static final String LOGIN_REQUEST_PARAM_NAME = "login";
    private static final String PASSWORD_REQUEST_PARAM_NAME = "password";
    private static final String ERROR_LOGIN_PASS_MESSAGE = "Invalid login or password" +
            "password must contains at least 1 number," +
            "at least 1 symbol, at least 1 upper symbol," +
            "length password minimum 6";
    private static final String ERROR_LOGIN_PASS_ATTRIBUTE = "errorLoginPassMessage";

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (request.sessionExists() && request.retrieveFromSession(USER_SESSION_ATTRIBUTE_NAME).isPresent()) {
            return requestFactory.createForwardResponse(PagePath.INDEX_PATH);
        }
        final String login = request.getParameter(LOGIN_REQUEST_PARAM_NAME);
        final String password = request.getParameter(PASSWORD_REQUEST_PARAM_NAME);
        final Optional<User> user = UserService.getInstance().authenticate(login, password);
        if (!user.isPresent()) {
            request.addAttributeToJsp(ERROR_LOGIN_PASS_ATTRIBUTE, ERROR_LOGIN_PASS_MESSAGE);
            return requestFactory.createForwardResponse(PagePath.LOGIN_PAGE_PATH);
        }
        request.clearSession();
        request.createSession();
        request.addToSession(USER_SESSION_ATTRIBUTE_NAME, user.get());
        return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
    }

    public static LoginCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final LoginCommand INSTANCE = new LoginCommand();
    }
}
