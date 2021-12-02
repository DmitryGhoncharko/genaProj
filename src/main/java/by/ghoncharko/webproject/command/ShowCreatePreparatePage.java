package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;

public class ShowCreatePreparatePage implements Command {
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowCreatePreparatePage() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(PagePath.CREATE_DRUG_PAGE_PATH);
    }
}
