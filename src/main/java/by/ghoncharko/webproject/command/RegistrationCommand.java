package by.ghoncharko.webproject.command;


import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class RegistrationCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(RegistrationCommand.class);
    private static final String LOGIN_REQUEST_PARAM = "login";
    private static final String PASSWORD_REQUEST_PARAM = "password";
    private static final String FIRST_NAME_REQUEST_PARAM = "Lname";
    private static final String LAST_NAME_REQUEST_PARAM = "Fname";
    private static final String ERROR_REGISTRATION_ATTRIBUTE_NAME = "registrationError";
    private static final String ERROR_REGISTRATION_MESSAGE = "Invalid data for registration";
    private final RequestFactory requestFactory;
    private final UserService userService;

    public RegistrationCommand(RequestFactory requestFactory, UserService userService) {
        this.requestFactory = requestFactory;
        this.userService = userService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {

        final String userLogin = request.getParameter(LOGIN_REQUEST_PARAM);
        final String userPassword = request.getParameter(PASSWORD_REQUEST_PARAM);
        final String userName = request.getParameter(FIRST_NAME_REQUEST_PARAM);
        final String userSurName = request.getParameter(LAST_NAME_REQUEST_PARAM);
        try{
            final Optional<User> user = userService.createClientWithBannedStatusFalse(userLogin, userPassword,
                    userName, userSurName);
            if (user.isPresent()) {
                return requestFactory.createRedirectResponse(PagePath.INDEX_PATH);
            }
            request.addAttributeToJsp(ERROR_REGISTRATION_ATTRIBUTE_NAME, ERROR_REGISTRATION_MESSAGE);
            return requestFactory.createForwardResponse(PagePath.REGISTRATION_PAGE_PATH);
        }catch (ServiceException e){
            LOG.error("Cannot registration user as client",e);
            return requestFactory.createRedirectResponse(PagePath.ERROR_PAGE_PATH);
        }
    }
}
