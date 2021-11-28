package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;

public class DeleteFromOrderCommand implements Command{
private final  RequestFactory requestFactory = RequestFactory.getInstance();
    @Override
    public CommandResponse execute(CommandRequest request) {

        return null;
    }
}
