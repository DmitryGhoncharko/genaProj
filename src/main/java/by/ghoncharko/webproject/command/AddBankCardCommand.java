package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.service.BankCardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class AddBankCardCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(AddBankCardCommand.class);
    private final RequestFactory requestFactory;
    private final BankCardService bankCardService;

    public AddBankCardCommand(RequestFactory requestFactory, BankCardService bankCardService){
        this.requestFactory = requestFactory;
        this.bankCardService = bankCardService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession("user");
        if(userFromSession.isPresent()){
            final User user = (User) userFromSession.get();
            final Double balanceCard = Double.valueOf(request.getParameter("balance"));
            try{
                final  boolean bankCardIsCreated = bankCardService.addBankCard(balanceCard, user);
                if(bankCardIsCreated){
                    return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
                }
            }catch (ServiceException e){
                LOG.error("Service exception",e);
            }
        }
        return requestFactory.createForwardResponse(PagePath.INDEX_PATH);
    }
}
