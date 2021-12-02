package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.model.service.DrugService;
import by.ghoncharko.webproject.model.service.DrugServiceImpl;

public class UpdateDrugCommand implements Command{
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    public CommandResponse execute(CommandRequest request) {
        DrugService drugService = new DrugServiceImpl();
        int drugId = Integer.parseInt(request.getParameter("drugId"));
        String updatedDrugName = request.getParameter("updateDrugName");
        boolean updatedDrugIsNeedRecipe = Boolean.parseBoolean(request.getParameter("updateDrugNeedRecipe"));
        int updatedDrugCount = Integer.parseInt(request.getParameter("updateDrugCount"));
        double updateDrugPrice = Double.parseDouble(request.getParameter("updateDrugPrice"));
        String updateDrugDescription = request.getParameter("updateDrugDescription");
        String updateDrugProducerName = request.getParameter("updateDrugProducer");
        boolean isUpdated =  drugService.update(drugId, updatedDrugName, updatedDrugIsNeedRecipe, updatedDrugCount, updateDrugPrice, updateDrugDescription, updateDrugProducerName);
            if(isUpdated){
                return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
            }
            //todo add error and forward to preparates page
        return null;
    }
}
