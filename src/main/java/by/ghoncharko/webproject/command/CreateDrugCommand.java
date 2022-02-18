package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.service.DrugService;

public class CreateDrugCommand implements Command{
    private final RequestFactory requestFactory;
    private final DrugService drugService;

    public CreateDrugCommand(RequestFactory requestFactory, DrugService drugService) {
        this.requestFactory = requestFactory;
        this.drugService = drugService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) throws ServiceException {
        final String drugName = request.getParameter("drugName");
        final Boolean drugNeedRecipe = Boolean.valueOf("drugNeedRecipe");
        final Double drugPrice = Double.valueOf(request.getParameter("drugPrice"));
        final Integer drugCount = Integer.valueOf(request.getParameter("drugCount"));
        final String drugDescription = request.getParameter("drugDescription");
        final String drugProducerName = request.getParameter("drugProducerName");
        final boolean drugIsCreated = drugService.createDrug(drugName, drugNeedRecipe, drugCount, drugPrice, drugDescription, drugProducerName);
        if(drugIsCreated){
            return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
        }else {
            return requestFactory.createForwardResponse(PagePath.CREATE_DRUG_PAGE_PATH);
        }
    }
}

