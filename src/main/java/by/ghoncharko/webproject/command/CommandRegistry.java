package by.ghoncharko.webproject.command;


import by.ghoncharko.webproject.entity.Role;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandRegistry {
    private static final CommandRegistry MAIN_PAGE = new CommandRegistry(ShowMainPageCommand.getInstance(), "main_page");
    private static final CommandRegistry LOGIN_PAGE = new CommandRegistry(ShowLoginPageCommand.getInstance(), "login", Role.UNAUTHORIZED);
    private static final CommandRegistry LOGOUT_COMMAND = new CommandRegistry(LogoutCommand.getInstance(), "logout", Role.ADMIN,Role.CLIENT, Role.DOCTOR, Role.PHARMACY);
    private static final CommandRegistry REGISTRATION_PAGE = new CommandRegistry(ShowRegistrationPageCommand.getInstance(),"registration", Role.UNAUTHORIZED);
    private static final CommandRegistry REGISTRATION_COMMAND = new CommandRegistry(RegistrationCommand.getInstance(),"registrationcmnd",Role.UNAUTHORIZED );
    private static final CommandRegistry LOGIN_COMMAND  = new CommandRegistry(LoginCommand.getInstance(),"logincmnd",Role.UNAUTHORIZED);
    private static final CommandRegistry PREPARATE_PAGE = new CommandRegistry(ShowPreparatesPageCommand.getInstance(), "preparates");
    private static final CommandRegistry ADD_DRUG_TO_ORDER_COMMAND = new CommandRegistry(AddDrugToOrderCommand.getInstance(),"addToOrder",Role.CLIENT);
    private static final CommandRegistry ADD_BANK_CARD_COMMAND = new CommandRegistry(AddBankCardCommand.getInstance(),"addCard",Role.CLIENT);
    private static final CommandRegistry DELETE_BANK_CARD_COMMAND = new CommandRegistry(DeleteBankCardCommand.getInstance(),"deleteCard", Role.CLIENT );
    private static final CommandRegistry DELETE_DRUG_FROM_ORDER_COMMAND  = new CommandRegistry(DeleteDrugFromOrderCommand.getInstance(),"deleteDrugFromOrder",Role.CLIENT);
    private static final CommandRegistry DELETE_ORDER_COMMAND = new CommandRegistry(DeleteOrderCommand.getInstance(),"deleteOrder",Role.CLIENT);
    private static final CommandRegistry PAY_ORDER_COMMAND = new CommandRegistry(PayOrderCommand.getInstance(),"payOrder",Role.CLIENT);
    private static final CommandRegistry BANK_CARD_PAGE = new CommandRegistry(ShowBankCardsPageCommand.getInstance(),"bankCard",Role.CLIENT);
    private static final CommandRegistry ORDER_PAGE = new CommandRegistry(ShowOrderPageCommand.getInstance(),"orderPage",Role.CLIENT);
    private static final List<CommandRegistry> COMMAND_REGISTRY_LIST = Arrays.asList(MAIN_PAGE, LOGIN_PAGE,LOGOUT_COMMAND, REGISTRATION_PAGE, REGISTRATION_COMMAND, LOGIN_COMMAND,
            PREPARATE_PAGE, ADD_DRUG_TO_ORDER_COMMAND, ADD_BANK_CARD_COMMAND, DELETE_BANK_CARD_COMMAND, DELETE_DRUG_FROM_ORDER_COMMAND, DELETE_ORDER_COMMAND, PAY_ORDER_COMMAND,
            BANK_CARD_PAGE, ORDER_PAGE);
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
