package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.RolesHolder;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.BankCardService;


import java.util.Optional;

public class AddBankCardCommand implements Command {
    private static final String BALANCE_PARAM_NAME = "balance";
    private static final String USER_ATTRIBUTE_NAME = "user";
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private AddBankCardCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession(USER_ATTRIBUTE_NAME);
        if (userFromSession.isPresent()) {
            final User user = (User) userFromSession.get();
            final BankCardService bankCardService = BankCardService.getInstance();
            final double balance = Double.parseDouble(request.getParameter(BALANCE_PARAM_NAME));
            final boolean userRoleAsClient = user.getRole().equals(RolesHolder.CLIENT);
            final int userId = user.getId();
            final boolean isCreated = bankCardService.addBankCard(balance, userId);
            if (isCreated && userRoleAsClient) {
                return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
            }
            //cannot add bank card

            return requestFactory.createForwardResponse(PagePath.BANK_CARDS_PAGE_PATH);
        }
        return requestFactory.createForwardResponse(PagePath.MAIN_PAGE_PATH);
    }

    public static AddBankCardCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final AddBankCardCommand INSTANCE = new AddBankCardCommand();
    }
}
