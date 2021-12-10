package by.ghoncharko.webproject.command;

public class ChangeRoleUserCommand implements Command {
    private ChangeRoleUserCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return null;
    }

    public static ChangeRoleUserCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ChangeRoleUserCommand INSTANCE = new ChangeRoleUserCommand();
    }
}
