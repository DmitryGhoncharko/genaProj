package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeleteOrderCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(DeleteOrderCommand.class);

    private final RequestFactory requestFactory = RequestFactory.getInstance();
    private DeleteOrderCommand(){

    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return null;
    }

    public static DeleteOrderCommand getInstance() {
        return Holder.INSTACNE;
    }
    private static class Holder{
        private static final DeleteOrderCommand INSTACNE = new DeleteOrderCommand();
    }
}
