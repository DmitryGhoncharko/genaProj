package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.exception.ServiceException;

public interface Command {

    CommandResponse execute(CommandRequest request) throws ServiceException;

    static Command of(String name) {
        return CommandRegistry.of(name);
    }


}
