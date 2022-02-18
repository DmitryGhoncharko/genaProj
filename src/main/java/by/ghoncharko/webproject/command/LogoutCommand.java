package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.model.service.UserService;

public class LogoutCommand implements Command {
    private static final String USER_SESSION_ATTRIBUTE_NAME = "user";
    private final RequestFactory requestFactory;

    public LogoutCommand(RequestFactory requestFactory) {
        this.requestFactory =requestFactory;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (noLoggedInUserPresent(request)) {
            return requestFactory.createForwardResponse(PagePath.MAIN_PAGE_PATH);
        }
        request.clearSession();
        return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
    }

    private boolean noLoggedInUserPresent(CommandRequest request) {
        return !request.sessionExists() || (
                request.sessionExists()
                        && !request.retrieveFromSession(USER_SESSION_ATTRIBUTE_NAME)
                        .isPresent()
        );
    }
}
