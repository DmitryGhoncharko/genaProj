package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;

import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.service.DrugService;

import java.util.Optional;

public class CreateDrugCommand implements Command {
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final String ERROR_ATTRIBUTE_MESSAGE = "Cannot create drug";
    private static final String DRUG_NAME_PARAM_NAME = "drugName";
    private static final String DRUG_PRICE_PARAM_NAME = "drugPrice";
    private static final String DRUG_COUNT_PARAM_NAME = "drugCount";
    private static final String DRUG_DESCRIPTION_PARAM_NAME = "drugDescription";
    private static final String DRUG_PRODUCER_NAME_PARAM_NAME = "drugProducerName";
    private static final String DRUG_NEED_RECIPE_PARAM_NAME = "drugNeedRecipe";
    private static final String USER_SESSION_ATTRIBUTE_NAME = "user";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private CreateDrugCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession(USER_SESSION_ATTRIBUTE_NAME);
        if (userFromSession.isPresent()) {
            final DrugService drugService = DrugService.getInstance();
            final String drugName = request.getParameter(DRUG_NAME_PARAM_NAME);
            final double drugPrice = Double.parseDouble(request.getParameter(DRUG_PRICE_PARAM_NAME));
            final int drugCount = Integer.parseInt(request.getParameter(DRUG_COUNT_PARAM_NAME));
            final String drugDescription = request.getParameter(DRUG_DESCRIPTION_PARAM_NAME);
            final String drugProducerName = request.getParameter(DRUG_PRODUCER_NAME_PARAM_NAME);
            final boolean drugNeedRecipe = Boolean.parseBoolean(request.getParameter(DRUG_NEED_RECIPE_PARAM_NAME));
            try{
                final boolean drugIsCreated = drugService.create(drugName, drugNeedRecipe, drugCount, drugPrice, drugDescription, drugProducerName);
                if (drugIsCreated) {
                    return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
                }
            }catch (ServiceException e){
                requestFactory.createForwardResponse(PagePath.ERROR_PAGE_PATH);
            }
        }
        request.addAttributeToJsp(ERROR_ATTRIBUTE_NAME, ERROR_ATTRIBUTE_MESSAGE);
        return requestFactory.createForwardResponse(PagePath.CREATE_DRUG_PAGE_PATH);

    }

    public static CreateDrugCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final CreateDrugCommand INSTANCE = new CreateDrugCommand();
    }
}
