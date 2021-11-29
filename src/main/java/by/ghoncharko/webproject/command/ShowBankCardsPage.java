package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.BankCardService;
import by.ghoncharko.webproject.model.service.BankCardServiceImpl;

import java.util.List;
import java.util.Optional;

public class ShowBankCardsPage implements Command{
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    public CommandResponse execute(CommandRequest request) {
        BankCardService bankCardService = new BankCardServiceImpl();
        Optional<Object> userFromSession = request.retrieveFromSession("user");
        if(userFromSession.isPresent()){
            User user = (User) userFromSession.get();
            int userId = user.getId();
            List<BankCard> bankCardList = bankCardService.getBankCardsByUserId(userId);
            request.addAttributeToJsp("bankCards", bankCardList);
            return requestFactory.createForwardResponse(PagePath.BANK_CARDS_PAGE_PATH);
        }
        request.addAttributeToJsp("error", "dont have permission to this");
        return requestFactory.createForwardResponse(PagePath.BANK_CARDS_PAGE_PATH);
    }
    public static ShowBankCardsPage getInstance(){
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final ShowBankCardsPage INSTANCE = new ShowBankCardsPage();
    }
}
