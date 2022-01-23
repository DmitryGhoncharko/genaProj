package by.ghoncharko.webproject.command;


import by.ghoncharko.webproject.entity.Role;
import by.ghoncharko.webproject.entity.RolesHolder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandRegistry {
    private static final CommandRegistry MAIN_PAGE = new CommandRegistry(ShowMainPageCommand.getInstance(), "main_page");
    private static final CommandRegistry LOGIN_PAGE = new CommandRegistry(ShowLoginPageCommand.getInstance(), "login", Role.UNAUTHORIZED);
    private static final CommandRegistry LOGIN_COMMAND = new CommandRegistry(LoginCommand.getInstance(), "logincmnd", RolesHolder.UNAUTHORIZED);
    private static final CommandRegistry LOGOUT_COMMAND = new CommandRegistry(LogoutCommand.getInstance(), "logout", RolesHolder.ADMIN,RolesHolder.CLIENT, RolesHolder.DOCTOR, RolesHolder.PHARMACY);
    private static final CommandRegistry REGISTRATION_PAGE = new CommandRegistry(ShowRegistrationPageCommand.getInstance(), "registration");
    private static final CommandRegistry REGISTRATION_COMMAND = new CommandRegistry(RegistrationCommand.getInstance(), "registrationcmnd");
    private static final CommandRegistry ERROR_PAGE = new CommandRegistry(ShowErrorCommandPage.getInstance(), "error");
    private static final CommandRegistry PREPARATES_PAGE = new CommandRegistry(ShowPreparatesPageComand.getInstance(), "preparates");
    private static final CommandRegistry ADD_TO_BUCKET_COMMAND = new CommandRegistry(AddToOrderCommand.getInstance(), "addToBucket",RolesHolder.CLIENT);
    private static final CommandRegistry RECIPES_PAGE = new CommandRegistry(ShowRecipesPageCommand.getInstance(), "recipes",RolesHolder.CLIENT);
    private static final CommandRegistry ORDER_PAGE = new CommandRegistry(ShowOrderPageCommand.getInstance(), "order",RolesHolder.CLIENT);
    private static final CommandRegistry PAY_COMMAND = new CommandRegistry(PayCommand.getInstance(), "pay",RolesHolder.CLIENT);
    private static final CommandRegistry DELETE_FROM_ORDER_COMMAND = new CommandRegistry(DeleteFromOrderCommand.getInstance(), "deleteFromOrder",RolesHolder.CLIENT);
    private static final CommandRegistry BANK_CARDS_PAGE = new CommandRegistry(ShowBankCardsPage.getInstance(), "card",RolesHolder.CLIENT);
    private static final CommandRegistry ADD_BANK_CARD_COMMAND = new CommandRegistry(AddBankCardCommand.getInstance(), "addCard",RolesHolder.CLIENT);
    private static final CommandRegistry DELETE_BANK_CARD_COMMAND = new CommandRegistry(DeleteBankCardCommand.getInstance(), "deleteCard",RolesHolder.CLIENT);
    private static final CommandRegistry CREATE_RECIPE_REQUEST_COMMAND = new CommandRegistry(CreateRecipeRequestCommand.getInstance(), "createRecipeRequest",RolesHolder.CLIENT);
    private static final CommandRegistry DELETE_DRUG_COMMAND = new CommandRegistry(DeleteDrugCommand.getInstance(), "deleteDrug",RolesHolder.PHARMACY);
    private static final CommandRegistry CREATE_DRUG_COMMAND = new CommandRegistry(CreateDrugCommand.getInstance(), "createDrugcmnd",RolesHolder.PHARMACY);
    private static final CommandRegistry CREATE_DRUG_PAGE = new CommandRegistry(ShowCreatePreparatePage.getInstance(), "createDrug",RolesHolder.PHARMACY);
    private static final CommandRegistry UPDATE_DRUG_COMMAND = new CommandRegistry(UpdateDrugCommand.getInstance(), "updateDrug",RolesHolder.PHARMACY);
    private static final CommandRegistry RECIPE_REQUEST_PAGE = new CommandRegistry(ShowRecipeRequestPage.getInstance(),"recipeRequest",RolesHolder.DOCTOR);
    private static final CommandRegistry ACCEPT_RECIPE_REQUEST_COMMAND = new CommandRegistry(AcceptRecipeRequestCommand.getInstance(),"acceptRecipeRequest",RolesHolder.DOCTOR);
    private static final CommandRegistry DECLINE_RECIPE_REQUEST_COMMAND = new CommandRegistry(DeclineRecipeRequestCommand.getInstance(),"declineRecipeRequest", RolesHolder.DOCTOR);
    private static final CommandRegistry USERS_AS_CLIENT_PAGE = new CommandRegistry(ShowUsersAsClientPage.getInstance(),"usersAsClients", RolesHolder.DOCTOR);
    private static final CommandRegistry PREPARATES_NEED_RECIPE_PAGE = new CommandRegistry(ShowPreparatesWhichNeedRecipePage.getInstance(),"showPrepWithNeedRecipe", RolesHolder.DOCTOR);
    private static final CommandRegistry CREATE_RECIPE_TO_USER_COMMAND = new CommandRegistry(CreateRecipeToUserCommand.getInstance(),"createRecipeForUser",RolesHolder.DOCTOR);
    private static final CommandRegistry DELETE_RECIPE_COMMAND = new CommandRegistry(DeleteRecipeCommand.getInstance(),"deleteRecipecmnd",RolesHolder.CLIENT);
    private static final CommandRegistry CHANGE_LANGUAGE_COMMAND = new CommandRegistry(ChangeLanguageCommand.getInstance(),"changeLanguage");
    private static final List<CommandRegistry> COMMAND_REGISTRY_LIST = Arrays.asList(MAIN_PAGE, LOGIN_PAGE, LOGIN_COMMAND, REGISTRATION_PAGE, REGISTRATION_COMMAND, LOGOUT_COMMAND, ERROR_PAGE, PREPARATES_PAGE, ADD_TO_BUCKET_COMMAND,
            RECIPES_PAGE, ORDER_PAGE, PAY_COMMAND, DELETE_FROM_ORDER_COMMAND, BANK_CARDS_PAGE, ADD_BANK_CARD_COMMAND, DELETE_BANK_CARD_COMMAND, CREATE_RECIPE_REQUEST_COMMAND,
            DELETE_DRUG_COMMAND, CREATE_DRUG_COMMAND, CREATE_DRUG_PAGE, UPDATE_DRUG_COMMAND, RECIPE_REQUEST_PAGE, ACCEPT_RECIPE_REQUEST_COMMAND,DECLINE_RECIPE_REQUEST_COMMAND,
            USERS_AS_CLIENT_PAGE, PREPARATES_NEED_RECIPE_PAGE, CREATE_RECIPE_TO_USER_COMMAND, DELETE_RECIPE_COMMAND, CHANGE_LANGUAGE_COMMAND);
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
