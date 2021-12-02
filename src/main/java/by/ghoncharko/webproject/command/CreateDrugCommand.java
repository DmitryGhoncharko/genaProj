package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;

public class CreateDrugCommand implements Command{
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    public CommandResponse execute(CommandRequest request) {

        return null;
    }
    public static CreateDrugCommand getInstance(){
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final CreateDrugCommand INSTANCE = new CreateDrugCommand();
    }
}
