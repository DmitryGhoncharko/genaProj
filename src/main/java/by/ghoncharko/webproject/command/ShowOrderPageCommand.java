package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShowOrderPageCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(ShowOrderPageCommand.class);
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowOrderPageCommand(){

    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return null;
    }

    public static ShowOrderPageCommand getInstance() {
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final ShowOrderPageCommand INSTANCE = new ShowOrderPageCommand();
    }
}
