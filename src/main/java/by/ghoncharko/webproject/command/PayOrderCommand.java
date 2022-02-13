package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PayOrderCommand implements Command{
    private final Logger LOG = LogManager.getLogger(PayOrderCommand.class);
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private PayOrderCommand(){

    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return null;
    }

    public static PayOrderCommand getInstance() {
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final PayOrderCommand INSTANCE = new PayOrderCommand();
    }
}
