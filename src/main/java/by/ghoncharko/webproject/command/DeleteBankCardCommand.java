package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.RolesHolder;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.BankCardService;


import java.util.Optional;

public class DeleteBankCardCommand implements Command {
    private static final String CARD_ID_PARAM_NAME = "cardId";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private DeleteBankCardCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession("user");
        if(userFromSession.isPresent()){
            final User user = (User)userFromSession.get();
            final Integer cardId = Integer.valueOf(request.getParameter(CARD_ID_PARAM_NAME));
            final BankCardService bankCardService = BankCardService.getInstance();
            final boolean isDeleted = bankCardService.deleteByCardId(cardId);
            final boolean userRoleAsClient = user.getRole().equals(RolesHolder.CLIENT);
            if (isDeleted && userRoleAsClient) {
                return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
            }
            //error cannot create
            return requestFactory.createForwardResponse(PagePath.BANK_CARDS_PAGE_PATH);
        }
        //need authorize
        return requestFactory.createForwardResponse(PagePath.BANK_CARDS_PAGE_PATH);
    }

    public static DeleteBankCardCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DeleteBankCardCommand INSTANCE = new DeleteBankCardCommand();
    }
}
