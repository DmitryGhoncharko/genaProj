package by.ghoncharko.webproject.command;


import by.ghoncharko.webproject.controller.RequestFactory;

public class ShowMainPageCommand implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowMainPageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(PagePath.MAIN_PAGE_PATH);
    }

    public static ShowMainPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ShowMainPageCommand INSTANCE = new ShowMainPageCommand();
    }
}
