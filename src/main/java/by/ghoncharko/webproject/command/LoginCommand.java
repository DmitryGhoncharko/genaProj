package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class LoginCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(LoginCommand.class);
    private static final String USER_SESSION_ATTRIBUTE_NAME = "user";
    private static final String LOGIN_REQUEST_PARAM_NAME = "login";
    private static final String PASSWORD_REQUEST_PARAM_NAME = "password";
    private static final String ERROR_LOGIN_PASS_MESSAGE = "Invalid login or password";
    private static final String ERROR_LOGIN_PASS_ATTRIBUTE = "errorLoginPassMessage";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private LoginCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (request.sessionExists() && request.retrieveFromSession(USER_SESSION_ATTRIBUTE_NAME).isPresent()) {
            return requestFactory.createForwardResponse(PagePath.INDEX_PATH);
        }
        final String login = request.getParameter(LOGIN_REQUEST_PARAM_NAME);
        final String password = request.getParameter(PASSWORD_REQUEST_PARAM_NAME);
        try {
            final Optional<User> user = UserService.getInstance().authenticate(login, password);
            if (!user.isPresent()) {
                request.addAttributeToJsp(ERROR_LOGIN_PASS_ATTRIBUTE, ERROR_LOGIN_PASS_MESSAGE);
                return requestFactory.createForwardResponse(PagePath.LOGIN_PAGE_PATH);
            }
            request.clearSession();
            request.createSession();
            request.addToSession(USER_SESSION_ATTRIBUTE_NAME, user.get());
            return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
        } catch (ServiceException e) {
            LOG.error("Service exception when we try login user", e);
            return requestFactory.createForwardResponse(PagePath.ERROR_PAGE_PATH);
        }

    }

    public static LoginCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final LoginCommand INSTANCE = new LoginCommand();
    }
}
