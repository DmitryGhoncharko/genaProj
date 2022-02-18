package by.ghoncharko.webproject.command;


import by.ghoncharko.webproject.controller.RequestFactory;


public class ShowLoginPageCommand implements Command {
    private final RequestFactory requestFactory;

    public ShowLoginPageCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(PagePath.LOGIN_PAGE_PATH);
    }
}
