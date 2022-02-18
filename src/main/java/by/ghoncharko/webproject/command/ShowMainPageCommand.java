package by.ghoncharko.webproject.command;


import by.ghoncharko.webproject.controller.RequestFactory;

public class ShowMainPageCommand implements Command {
    private final RequestFactory requestFactory;

    public ShowMainPageCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(PagePath.MAIN_PAGE_PATH);
    }
}
