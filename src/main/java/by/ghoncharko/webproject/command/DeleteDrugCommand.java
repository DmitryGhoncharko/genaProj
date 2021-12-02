package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.DrugService;
import by.ghoncharko.webproject.model.service.DrugServiceImpl;

import java.util.Optional;

public class DeleteDrugCommand implements Command{
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    public CommandResponse execute(CommandRequest request) {
        Optional<Object> userFromSession = request.retrieveFromSession("user");
        if(userFromSession.isPresent()){
            int drugId = Integer.valueOf(request.getParameter("drugId"));
            DrugService drugService = new DrugServiceImpl();
            boolean isDeleted = drugService.deleteByDrugId(drugId);
            if(isDeleted){
                return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
            }
        }
        return null;
    }
}
