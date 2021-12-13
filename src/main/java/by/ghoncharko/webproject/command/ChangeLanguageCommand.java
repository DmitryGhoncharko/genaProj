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
       final String currentUrl = request.getURL();
       request.addAttributeToJsp(LANGUAGE_ATTRIBUTE_NAME, RUSSIAN_LANGUAGE_ATTRIBUTE);
       return requestFactory.createRedirectResponse(currentUrl);
    }

    public static ChangeLanguageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ChangeLanguageCommand INSTANCE = new ChangeLanguageCommand();
    }
}
