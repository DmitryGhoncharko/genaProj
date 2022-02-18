package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.exception.ServiceException;

public class ShowCreateDrugPageCommand implements Command{
    private final RequestFactory requestFactory;

    public ShowCreateDrugPageCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    @Override
    public CommandResponse execute(CommandRequest request) throws ServiceException {
        return requestFactory.createForwardResponse(PagePath.CREATE_DRUG_PAGE_PATH);
    }
}
