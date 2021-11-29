package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.model.service.BankCardService;
import by.ghoncharko.webproject.model.service.BankCardServiceImpl;

public class DeleteBankCardCommand implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    public CommandResponse execute(CommandRequest request) {

        Integer cardId = Integer.valueOf(request.getParameter("cardId"));
        BankCardService bankCardService = new BankCardServiceImpl();
        boolean isDeleted = bankCardService.deleteByCardId(cardId);
        if(isDeleted){
            return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
        }
        return requestFactory.createForwardResponse(PagePath.BANK_CARDS_PAGE_PATH);
    }
}
