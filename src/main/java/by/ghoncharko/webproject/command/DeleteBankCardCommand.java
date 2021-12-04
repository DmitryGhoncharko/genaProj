package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.BankCardService;


import java.util.Optional;

public class DeleteBankCardCommand implements Command {
    private static final String CARD_ID_PARAM_NAME = "cardId";
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final String ERROR_MESSAGE = "Cannod delete bank card";
    private static final String USER_FROM_SESSION_ATTRIBUTE_NAME = "user";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private DeleteBankCardCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession(USER_FROM_SESSION_ATTRIBUTE_NAME);
        if (userFromSession.isPresent()) {
            final Integer cardId = Integer.valueOf(request.getParameter(CARD_ID_PARAM_NAME));
            final BankCardService bankCardService = BankCardService.getInstance();
            final boolean bankCardIsDeleted = bankCardService.deleteByCardId(cardId);
            if (bankCardIsDeleted) {
                return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
            }

        }
        request.addAttributeToJsp(ERROR_ATTRIBUTE_NAME, ERROR_MESSAGE);
        return requestFactory.createForwardResponse(PagePath.BANK_CARDS_PAGE_PATH);
    }

    public static DeleteBankCardCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DeleteBankCardCommand INSTANCE = new DeleteBankCardCommand();
    }
}
