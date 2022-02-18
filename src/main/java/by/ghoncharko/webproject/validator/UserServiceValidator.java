package by.ghoncharko.webproject.validator;

public interface UserServiceValidator {
   boolean validateAuthenticate(String login, String password);

   boolean validateCreateClientWithBannedStatusFalse(String login, String password, String firstName, String lastName);

}
