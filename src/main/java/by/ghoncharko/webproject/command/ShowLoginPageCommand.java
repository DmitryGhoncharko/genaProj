package by.ghoncharko.webproject.command;


import by.ghoncharko.webproject.controller.RequestFactory;

public class ShowLoginPageCommand implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowLoginPageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(PagePath.LOGIN_PAGE_PATH);
    }

    public static ShowLoginPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ShowLoginPageCommand INSTANCE = new ShowLoginPageCommand();
    }
}
