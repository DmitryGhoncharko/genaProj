package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShowRegistrationPageCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ShowRegistrationPageCommand.class);
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowRegistrationPageCommand() {

    }

    public static ShowRegistrationPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(PagePath.REGISTRATION_PAGE_PATH);
    }

    private static class Holder {
        private static final ShowRegistrationPageCommand INSTANCE = new ShowRegistrationPageCommand();
    }
}
