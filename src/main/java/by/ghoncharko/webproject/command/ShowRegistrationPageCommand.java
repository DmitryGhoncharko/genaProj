package by.ghoncharko.webproject.command;


import by.ghoncharko.webproject.controller.RequestFactory;

public class ShowRegistrationPageCommand implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowRegistrationPageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(PagePath.REGISTRATION_PAGE_PATH);
    }

    public static ShowRegistrationPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ShowRegistrationPageCommand INSTANCE = new ShowRegistrationPageCommand();
    }
}
