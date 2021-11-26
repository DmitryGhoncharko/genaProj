package by.ghoncharko.webproject.command;


import by.ghoncharko.webproject.controller.RequestFactory;

public class ShowRecipesPageCommand implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    public CommandResponse execute(CommandRequest request) {

        return null;
    }

    public static ShowRecipesPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ShowRecipesPageCommand INSTANCE = new ShowRecipesPageCommand();
    }
}