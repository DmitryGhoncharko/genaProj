package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;

public class ChangeLanguageCommand implements Command {
    private static final String LANGUAGE_ATTRIBUTE_NAME = "lang";
    private static final String RUSSIAN_LANGUAGE_ATTRIBUTE = "ru_RU";
    private final RequestFactory requestFactory = RequestFactory.getInstance();
    private ChangeLanguageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
       final String currentURI = request.getParameter("crntURI");
       return requestFactory.createRedirectResponse(currentURI);
    }

    public static ChangeLanguageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ChangeLanguageCommand INSTANCE = new ChangeLanguageCommand();
    }
}
