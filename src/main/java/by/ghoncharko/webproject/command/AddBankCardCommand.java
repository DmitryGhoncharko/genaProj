package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.service.BankCardService;
import by.ghoncharko.webproject.model.service.BankCardServiceImpl;


import java.util.Optional;

public class AddBankCardCommand implements Command {
    private static final String BALANCE_PARAM_NAME = "balance";
    private static final String USER_FROM_SESSION_ATTRIBUTE_NAME = "user";
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final String ERROR_ATTRIBUTE_MESSAGE = "Cannot add bank card";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private AddBankCardCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession(USER_FROM_SESSION_ATTRIBUTE_NAME);
        if (userFromSession.isPresent()) {
            final User user = (User) userFromSession.get();
            final BankCardService bankCardService = BankCardServiceImpl.getInstance();
            final double balance = Double.parseDouble(request.getParameter(BALANCE_PARAM_NAME));
            try{
                final boolean bankCardAdded = bankCardService.addBankCard(balance, user);
                if (bankCardAdded) {
                    return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
                }
            }catch (ServiceException e){
                return requestFactory.createForwardResponse(PagePath.ERROR_PAGE_PATH);
            }
        }
        request.addAttributeToJsp(ERROR_ATTRIBUTE_NAME, ERROR_ATTRIBUTE_MESSAGE);
        return requestFactory.createForwardResponse(PagePath.BANK_CARDS_PAGE_PATH);
    }

    public static AddBankCardCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final AddBankCardCommand INSTANCE = new AddBankCardCommand();
    }
}
