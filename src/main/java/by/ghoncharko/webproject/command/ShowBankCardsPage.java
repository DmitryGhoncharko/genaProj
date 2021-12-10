package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.BankCardService;


import java.util.List;
import java.util.Optional;

public class ShowBankCardsPage implements Command {
    private static final String USER_ATTRIBUTE_NAME = "user";
    private static final String BANK_CARDS_ATTRIBUTE_NAME = "bankCards";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowBankCardsPage() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession(USER_ATTRIBUTE_NAME);
        if (userFromSession.isPresent()) {
            final User user = (User) userFromSession.get();
            final int userId = user.getId();
            final BankCardService bankCardService = BankCardService.getInstance();
            final List<BankCard> bankCardList = bankCardService.getBankCardsByUserId(userId);
            request.addAttributeToJsp(BANK_CARDS_ATTRIBUTE_NAME, bankCardList);
            return requestFactory.createForwardResponse(PagePath.BANK_CARDS_PAGE_PATH);
        }
        return requestFactory.createForwardResponse(PagePath.MAIN_PAGE_PATH);
    }

    public static ShowBankCardsPage getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ShowBankCardsPage INSTANCE = new ShowBankCardsPage();
    }
}
