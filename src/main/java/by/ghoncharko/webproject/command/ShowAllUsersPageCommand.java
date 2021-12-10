package by.ghoncharko.webproject.command;

public class ShowAllUsersPageCommand implements Command {
    private ShowAllUsersPageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return null;
    }

    public static ShowAllUsersPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ShowAllUsersPageCommand INSTANCE = new ShowAllUsersPageCommand();
    }
}
