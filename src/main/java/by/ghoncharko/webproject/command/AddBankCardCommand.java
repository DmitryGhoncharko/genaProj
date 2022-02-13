package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.service.BankCardService;
import by.ghoncharko.webproject.model.service.BankCardServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class AddBankCardCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(AddBankCardCommand.class);
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private AddBankCardCommand(){
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession("user");
        if(userFromSession.isPresent()){
            final User user = (User) userFromSession.get();
            final BankCardService bankCardService = BankCardServiceImpl.getInstance();
            final Double balanceCard = Double.valueOf(request.getParameter("balance"));

            try{
                final  boolean bankCardIsCreated = bankCardService.addBankCard(balanceCard, user);
                if(bankCardIsCreated){
                    return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
                }
                return requestFactory.createForwardResponse(PagePath.INDEX_PATH);
            }catch (ServiceException e){

            }
        }
    }

    public static AddBankCardCommand getInstance() {
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final AddBankCardCommand INSTANCE = new AddBankCardCommand();
    }
}
