package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.BankCardService;
import by.ghoncharko.webproject.model.service.BankCardServiceImpl;

import java.util.Optional;

public class AddBankCardCommand implements Command{
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    public CommandResponse execute(CommandRequest request) {
       final BankCardService bankCardService = new BankCardServiceImpl();
        Double balance = Double.valueOf(request.getParameter("balance"));
        Optional<Object> userFromSession = request.retrieveFromSession("user");
        if(userFromSession.isPresent()){
            User user =(User)userFromSession.get();
            Integer userId = user.getId();
            boolean isCreated = bankCardService.addBankCard(balance,userId);
            if(isCreated){
                return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
            }
        }
        return requestFactory.createForwardResponse(PagePath.BANK_CARDS_PAGE_PATH);
    }
    public static AddBankCardCommand getInstance(){
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final AddBankCardCommand INSTANCE = new AddBankCardCommand();
    }
}
