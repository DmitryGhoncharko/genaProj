package by.ghoncharko.webproject.command;


import by.ghoncharko.webproject.controller.SimpleRequestFactory;
import by.ghoncharko.webproject.entity.Role;
import by.ghoncharko.webproject.model.connection.BlockingConnectionPool;
import by.ghoncharko.webproject.model.connection.ConnectionPool;
import by.ghoncharko.webproject.model.dao.DaoHelperFactory;
import by.ghoncharko.webproject.model.service.BankCardServiceImpl;
import by.ghoncharko.webproject.model.service.DrugServiceImpl;
import by.ghoncharko.webproject.model.service.OrderServiceImpl;
import by.ghoncharko.webproject.model.service.UserServiceImpl;
import by.ghoncharko.webproject.validator.BankCardServiceValidatorImpl;
import by.ghoncharko.webproject.validator.SimpleUserServiceValidator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandRegistry {

    private static final CommandRegistry MAIN_PAGE = new CommandRegistry(()->new ShowMainPageCommand(new SimpleRequestFactory()), "main_page");
    private static final CommandRegistry LOGIN_PAGE = new CommandRegistry(()-> new ShowLoginPageCommand(new SimpleRequestFactory()), "login", Role.UNAUTHORIZED);
    private static final CommandRegistry LOGOUT_COMMAND = new CommandRegistry(()->new LogoutCommand(new SimpleRequestFactory()), "logout", Role.ADMIN,Role.CLIENT, Role.DOCTOR, Role.PHARMACY);
    private static final CommandRegistry REGISTRATION_PAGE = new CommandRegistry(()-> new ShowRegistrationPageCommand(new SimpleRequestFactory()),"registration", Role.UNAUTHORIZED);
    private static final CommandRegistry REGISTRATION_COMMAND = new CommandRegistry(()->new RegistrationCommand(new SimpleRequestFactory(),new UserServiceImpl(new DaoHelperFactory(ConnectionPool.getInstance()), new SimpleUserServiceValidator())),"registrationcmnd",Role.UNAUTHORIZED );
    private static final CommandRegistry LOGIN_COMMAND  = new CommandRegistry(()->new LoginCommand(new SimpleRequestFactory(),new UserServiceImpl(new DaoHelperFactory(BlockingConnectionPool.getInstance()),new SimpleUserServiceValidator())),"logincmnd",Role.UNAUTHORIZED);
    private static final CommandRegistry PREPARATES_PAGE = new CommandRegistry(()->new ShowPreparatesPageCommand(new SimpleRequestFactory(), new DrugServiceImpl(new DaoHelperFactory(BlockingConnectionPool.getInstance()))), "preparates");
    private static final CommandRegistry ADD_DRUG_TO_ORDER_COMMAND = new CommandRegistry(()->new AddDrugToOrderCommand(new SimpleRequestFactory(), new OrderServiceImpl(new DaoHelperFactory(BlockingConnectionPool.getInstance()))),"addToOrder",Role.CLIENT);
    private static final CommandRegistry ADD_BANK_CARD_COMMAND = new CommandRegistry(()->new AddBankCardCommand(new SimpleRequestFactory(), new BankCardServiceImpl(new DaoHelperFactory(BlockingConnectionPool.getInstance()),new BankCardServiceValidatorImpl())),"addCard",Role.CLIENT);
    private static final CommandRegistry DELETE_BANK_CARD_COMMAND = new CommandRegistry(()->new DeleteBankCardCommand(new SimpleRequestFactory(), new BankCardServiceImpl(new DaoHelperFactory(BlockingConnectionPool.getInstance()), new BankCardServiceValidatorImpl())),"deleteCard", Role.CLIENT );
    private static final CommandRegistry BANK_CARD_PAGE = new CommandRegistry(()->new ShowBankCardsPageCommand(new SimpleRequestFactory(), new BankCardServiceImpl(new DaoHelperFactory(BlockingConnectionPool.getInstance()), new BankCardServiceValidatorImpl())),"card",Role.CLIENT);
    private static final CommandRegistry ORDER_PAGE = new CommandRegistry(()-> new ShowOrderPageCommand(new SimpleRequestFactory(), new OrderServiceImpl(new DaoHelperFactory(BlockingConnectionPool.getInstance()))),"orderPage",Role.CLIENT);
    private static final CommandRegistry DELETE_ORDER_COMMAND = new CommandRegistry(()->new DeleteOrderCommand(new SimpleRequestFactory(), new OrderServiceImpl(new DaoHelperFactory(BlockingConnectionPool.getInstance()))),"deleteOrder",Role.CLIENT);
    private static final CommandRegistry PAY_ORDER_COMMAND = new CommandRegistry(()->new PayOrderCommand(new SimpleRequestFactory(), new OrderServiceImpl(new DaoHelperFactory(BlockingConnectionPool.getInstance()))),"payOrder",Role.CLIENT);
    private static final CommandRegistry CREATE_DRUG_PAGE = new CommandRegistry(()->new ShowCreateDrugPageCommand(new SimpleRequestFactory()),"createDrug", Role.PHARMACY);
    private static final CommandRegistry CREATE_DRUG_COMMAND = new CommandRegistry(()->new CreateDrugCommand(new SimpleRequestFactory(), new DrugServiceImpl(new DaoHelperFactory(BlockingConnectionPool.getInstance()))),"createDrugcmnd",Role.PHARMACY);
    private static final CommandRegistry UPDATE_DRUG_COMMAND = new CommandRegistry(()-> new UpdateDrugCommand(new SimpleRequestFactory(), new DrugServiceImpl(new DaoHelperFactory(BlockingConnectionPool.getInstance()))),"updateDrug", Role.PHARMACY);
    private static final List<CommandRegistry> COMMAND_REGISTRY_LIST = Arrays.asList(MAIN_PAGE, LOGIN_PAGE, LOGOUT_COMMAND, REGISTRATION_PAGE, REGISTRATION_COMMAND, LOGIN_COMMAND, PREPARATES_PAGE, ADD_DRUG_TO_ORDER_COMMAND, BANK_CARD_PAGE, ADD_BANK_CARD_COMMAND, DELETE_BANK_CARD_COMMAND,ORDER_PAGE, DELETE_ORDER_COMMAND,PAY_ORDER_COMMAND, CREATE_DRUG_PAGE, CREATE_DRUG_COMMAND, UPDATE_DRUG_COMMAND);
    private final CommandCreator commandCreator;
    private final String commandName;
    private final List<Role> allowedRoles;

    private CommandRegistry(CommandCreator commandCreator, String commandName, Role... allowedRoles) {
        this.commandCreator = commandCreator;
        this.commandName = commandName;
        this.allowedRoles = allowedRoles != null && allowedRoles.length > 0 ? Arrays.asList(allowedRoles) : Collections.emptyList();
    }

    public Command getCommandCreator() {
        return commandCreator.create();
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
                return constant.commandCreator.create();
            }
        }
        return MAIN_PAGE.commandCreator.create();
    }
}
