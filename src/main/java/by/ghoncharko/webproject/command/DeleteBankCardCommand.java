package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.service.BankCardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class DeleteBankCardCommand implements Command{
    private static final Logger LOG  = LogManager.getLogger(DeleteBankCardCommand.class);
    private final RequestFactory requestFactory;
    private final BankCardService bankCardService;
    public DeleteBankCardCommand(RequestFactory requestFactory, BankCardService bankCardService){
        this.requestFactory= requestFactory;
        this.bankCardService = bankCardService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession("user");
        if(userFromSession.isPresent()){
            final User user = (User)userFromSession.get();
            final int cardId = Integer.parseInt(request.getParameter("cardId"));
            try{
                bankCardService.deleteBankCard(user,cardId);
            }catch (ServiceException e){

            }
        }
        return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
    }

}
