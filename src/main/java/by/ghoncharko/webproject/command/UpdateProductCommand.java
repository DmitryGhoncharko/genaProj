package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.service.ProductService;

public class UpdateProductCommand implements Command{
    private final RequestFactory requestFactory;
    private final ProductService productService;

    public UpdateProductCommand(RequestFactory requestFactory, ProductService productService) {
        this.requestFactory = requestFactory;
        this.productService = productService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) throws ServiceException {
        final Integer drugId = Integer.valueOf(request.getParameter("drugId"));
        final String drugName = request.getParameter("updateDrugName");
        final Boolean drugNeedRecipe = Boolean.valueOf("updateDrugNeedRecipe");
        final Double drugPrice = Double.valueOf(request.getParameter("updateDrugPrice"));
        final Integer drugCount = Integer.valueOf(request.getParameter("updateDrugCount"));
        final String drugDescription = request.getParameter("updateDrugDescription");
        final String drugProducerName = request.getParameter("updateDrugProducer");
        final Boolean drugIsDeleted = Boolean.valueOf(request.getParameter("updateDrugIsDeleted"));
        final boolean drugIsUpdated = productService.updateDrug(drugId,drugName, drugNeedRecipe, drugCount,drugPrice, drugDescription, drugProducerName, drugIsDeleted);
        if(drugIsUpdated){
            return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
        }else {
            return requestFactory.createForwardResponse(PagePath.PREPARATES_PAGE_PATH);
        }
    }
}
