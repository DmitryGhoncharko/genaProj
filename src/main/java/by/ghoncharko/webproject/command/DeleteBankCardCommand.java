package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeleteBankCardCommand implements Command{
    private static final Logger LOG  = LogManager.getLogger(DeleteBankCardCommand.class);
    private final RequestFactory requestFactory = RequestFactory.getInstance();
    private DeleteBankCardCommand(){
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return null;
    }

    public static DeleteBankCardCommand getInstance() {
        return Holder.INSTANCE;
    }


    private static class Holder{
        private static final DeleteBankCardCommand INSTANCE = new DeleteBankCardCommand();
    }
}
