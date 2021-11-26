package by.ghoncharko.webproject.command;


import by.ghoncharko.webproject.entity.Role;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandRegistry {
    private static final CommandRegistry MAIN_PAGE = new CommandRegistry(ShowMainPageCommand.getInstance(), "main_page");
    private static final CommandRegistry LOGIN_PAGE = new CommandRegistry(ShowLoginPageCommand.getInstance(), "login");
    private static final CommandRegistry LOGIN_COMMAND = new CommandRegistry(LoginCommand.getInstance(), "logincmnd");
    private static final CommandRegistry LOGOUT_COMMAND = new CommandRegistry(LogoutCommand.getInstance(), "logout");
    private static final CommandRegistry REGISTRATION_PAGE = new CommandRegistry(ShowRegistrationPageCommand.getInstance(), "registration");
    private static final CommandRegistry REGISTRATION_COMMAND = new CommandRegistry(RegistrationCommand.getInstance(), "registrationcmnd");
    private static final CommandRegistry ERROR_PAGE_COMMAND = new CommandRegistry(ShowErrorCommandPage.getInstance(), "error");
    private static final CommandRegistry PREPARATES_PAGE = new CommandRegistry(ShowPreparatesPageComand.getInstance(), "preparates");
    private static final CommandRegistry ADD_TO_BUCKET_COMMAND = new CommandRegistry(AddToBucketCommand.getInstance(), "addToBucket");
    private static final CommandRegistry RECIPES_PAGE_COMMAND = new CommandRegistry(ShowRecipesPageCommand.getInstance(), "recipes");
    private static final CommandRegistry ORDER_PAGE_COMMAND = new CommandRegistry(ShowOrderPageCommand.getInstance(), "order");
    private static final CommandRegistry PAY_COMMAND = new CommandRegistry(PayCommand.getInstance(), "pay");
    private static final List<CommandRegistry> COMMAND_REGISTRY_LIST = Arrays.asList(MAIN_PAGE, LOGIN_PAGE, LOGIN_COMMAND, REGISTRATION_PAGE, REGISTRATION_COMMAND, LOGOUT_COMMAND, ERROR_PAGE_COMMAND, PREPARATES_PAGE, ADD_TO_BUCKET_COMMAND,
            RECIPES_PAGE_COMMAND, ORDER_PAGE_COMMAND, PAY_COMMAND);
    private final Command command;
    private final String commandName;
    private final List<Role> allowedRoles;

    private CommandRegistry(Command command, String commandName, Role... allowedRoles) {
        this.command = command;
        this.commandName = commandName;
        this.allowedRoles = allowedRoles != null && allowedRoles.length > 0 ? Arrays.asList(allowedRoles) : Collections.emptyList();
    }

    public Command getCommand() {
        return command;
    }

    public static List<Role> getAllowedRoles(String commandName) {
        for (CommandRegistry commandRegistry : COMMAND_REGISTRY_LIST) {
            if (commandRegistry.commandName.equalsIgnoreCase(commandName)) {
                return commandRegistry.allowedRoles;
            }
        }
        return Collections.emptyList();
    }


    static Command of(String name) {
        for (CommandRegistry constant : COMMAND_REGISTRY_LIST) {
            if (constant.commandName.equalsIgnoreCase(name)) {
                return constant.command;
            }
        }
        return MAIN_PAGE.command;
    }
}