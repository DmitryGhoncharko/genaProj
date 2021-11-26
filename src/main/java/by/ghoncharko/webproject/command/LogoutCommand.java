package by.ghoncharko.webproject.command;


import by.ghoncharko.webproject.controller.RequestFactory;

public class LogoutCommand implements Command{
    private  final RequestFactory requestFactory = RequestFactory.getInstance();
    private static final String USER_SESSION_ATTRIBUTE_NAME = "user";

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (noLoggedInUserPresent(request)) {
            //todo: error - no user found cannot logout
            return null;
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
    public static LogoutCommand getInstance(){
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final LogoutCommand INSTANCE = new LogoutCommand();
    }
}
