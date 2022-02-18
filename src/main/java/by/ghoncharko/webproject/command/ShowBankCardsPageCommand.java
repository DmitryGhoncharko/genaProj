package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.dao.Dao;
import by.ghoncharko.webproject.model.service.BankCardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class ShowBankCardsPageCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(ShowPreparatesPageCommand.class);
    private final RequestFactory requestFactory;
    private final BankCardService bankCardService;
    public ShowBankCardsPageCommand(RequestFactory requestFactory, BankCardService bankCardService){
        this.requestFactory = requestFactory;
        this.bankCardService = bankCardService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession("user");
        if(userFromSession.isPresent()){
            final User user = (User)userFromSession.get();
            try{
              final   List<BankCard> userBankCards = bankCardService.findBankCardsByUserId(user);
              request.addAttributeToJsp("bankCards",userBankCards);
              requestFactory.createForwardResponse(PagePath.BANK_CARDS_PAGE_PATH);
            }catch (ServiceException e){

            }
        }
        return requestFactory.createForwardResponse(PagePath.BANK_CARDS_PAGE_PATH);
    }
}
