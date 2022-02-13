package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShowBankCardsPageCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(ShowPreparatesPageCommand.class);
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowBankCardsPageCommand(){

    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return null;
    }

    public static ShowBankCardsPageCommand getInstance() {
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final ShowBankCardsPageCommand INSTANCE = new ShowBankCardsPageCommand();
    }
}
