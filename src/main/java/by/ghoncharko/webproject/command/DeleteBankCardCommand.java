package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.model.service.BankCardService;
import by.ghoncharko.webproject.model.service.BankCardServiceImpl;

public class DeleteBankCardCommand implements Command {
    private static final String CARD_ID_PARAM_NAME = "cardId";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private DeleteBankCardCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {

        final Integer cardId = Integer.valueOf(request.getParameter(CARD_ID_PARAM_NAME));
        final BankCardService bankCardService = new BankCardServiceImpl();
        final boolean isDeleted = bankCardService.deleteByCardId(cardId);
        if (isDeleted) {
            return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
        }
        //todo error message
        return requestFactory.createForwardResponse(PagePath.BANK_CARDS_PAGE_PATH);
    }

    public static DeleteBankCardCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DeleteBankCardCommand INSTANCE = new DeleteBankCardCommand();
    }
}
