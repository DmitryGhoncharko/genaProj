package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.service.DrugService;

import java.util.Optional;

public class UpdateDrugCommand implements Command {
    private static final String USER_SESSION_ATTRIBUTE_NAME = "user";
    private static final String DRUG_ID_PARAM_NAME = "drugId";
    private static final String UPDATE_DRUG_NAME_PARAM_NAME = "updateDrugName";
    private static final String UPDATE_DRUG_NEED_RECIPE_PARAM_NAME = "updateDrugNeedRecipe";
    private static final String UPDATE_DRUG_COUNT_PARAM_NAME = "updateDrugCount";
    private static final String UPDATE_DRUG_PRICE_PARAM_NAME = "updateDrugPrice";
    private static final String UPDATE_DRUG_DESCRIPTION_PARAM_NAME = "updateDrugDescription";
    private static final String UPDATE_DRUG_PRODUCER_PARAM_NAME = "updateDrugProducer";
    private static final String PRODUCER_ID_PARAM_NAME = "producerId";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private UpdateDrugCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> userFromSession = request.retrieveFromSession(USER_SESSION_ATTRIBUTE_NAME);
        if (userFromSession.isPresent()) {
            final DrugService drugService = DrugService.getInstance();
            final int drugId = Integer.parseInt(request.getParameter(DRUG_ID_PARAM_NAME));
            final String updatedDrugName = request.getParameter(UPDATE_DRUG_NAME_PARAM_NAME);
            final boolean updatedDrugIsNeedRecipe = Boolean.parseBoolean(request.getParameter(UPDATE_DRUG_NEED_RECIPE_PARAM_NAME));
            final int updatedDrugCount = Integer.parseInt(request.getParameter(UPDATE_DRUG_COUNT_PARAM_NAME));
            final double updateDrugPrice = Double.parseDouble(request.getParameter(UPDATE_DRUG_PRICE_PARAM_NAME));
            final String updateDrugDescription = request.getParameter(UPDATE_DRUG_DESCRIPTION_PARAM_NAME);
            final String updateDrugProducerName = request.getParameter(UPDATE_DRUG_PRODUCER_PARAM_NAME);
            try{
                final boolean isUpdated = drugService.update(drugId,  updatedDrugName, updatedDrugIsNeedRecipe, updatedDrugCount, updateDrugPrice, updateDrugDescription, updateDrugProducerName);
                if (isUpdated) {
                    return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
                }

            }catch (ServiceException e){
                return requestFactory.createForwardResponse(PagePath.ERROR_PAGE_PATH);
            }
        }
        //todo add error and forward to preparates page
        return requestFactory.createForwardResponse(PagePath.INDEX_PATH);
    }
    public static UpdateDrugCommand getInstance(){
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final UpdateDrugCommand INSTANCE = new UpdateDrugCommand();
    }
}
