package by.ghoncharko.webproject.command;


import by.ghoncharko.webproject.controller.RequestFactory;

public class ShowErrorCommandPage implements Command {
     private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowErrorCommandPage() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(PagePath.ERROR_PAGE_PATH);
    }

    public static ShowErrorCommandPage getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ShowErrorCommandPage INSTANCE = new ShowErrorCommandPage();
    }
}
