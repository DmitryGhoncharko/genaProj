package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.model.service.DrugService;
import by.ghoncharko.webproject.model.service.DrugServiceImpl;

public class CreateDrugCommand implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private CreateDrugCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final DrugService drugService = new DrugServiceImpl();
        final String drugName = request.getParameter("drugName");
        final Double drugPrice = Double.valueOf(request.getParameter("drugPrice"));
        final int drugCount = Integer.valueOf(request.getParameter("drugCount"));
        final String drugDescription = request.getParameter("drugDescription");
        final String drugProducerName = request.getParameter("drugProducerName");
        final boolean drugNeedRecipe = Boolean.valueOf(request.getParameter("drugNeedRecipe"));
        final boolean isCreated =  drugService.create(drugName, drugNeedRecipe, drugCount, drugPrice, drugDescription, drugProducerName);
        if(isCreated){
            requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
        }
        return requestFactory.createForwardResponse(PagePath.CREATE_DRUG_PAGE_PATH);
    }

    public static CreateDrugCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final CreateDrugCommand INSTANCE = new CreateDrugCommand();
    }
}
