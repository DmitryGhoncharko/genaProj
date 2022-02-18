package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;


public class ShowRegistrationPageCommand implements Command {
    private final RequestFactory requestFactory;

    public ShowRegistrationPageCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }
    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(PagePath.REGISTRATION_PAGE_PATH);
    }
}
