package by.ghoncharko.webproject.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleUserServiceValidator implements UserServiceValidator{
    @Override
    public boolean validateAuthenticate(String login, String password) {
        if(login!=null && password !=null){
            Pattern pattern = Pattern.compile("[a-zA-Z0-9]{6,45}");
            Matcher matcher = pattern.matcher(login);
            boolean loginIsValide = matcher.matches();
            pattern = Pattern.compile("(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,35}");
            matcher = pattern.matcher(password);
            boolean passwordIsValide = matcher.matches();
            return loginIsValide && passwordIsValide;
        }
        return false;
    }

    @Override
    public boolean validateCreateClientWithBannedStatusFalse(String login, String password, String firstName, String lastName) {
        if (login != null && password != null && firstName != null && lastName != null) {
            Pattern pattern = Pattern.compile("[a-zA-Z0-9]{6,45}");
            Matcher matcher = pattern.matcher(login);
            boolean loginIsValid = matcher.matches();
            pattern = Pattern.compile("(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,45}");
            matcher = pattern.matcher(password);
            boolean passwordIsValid = matcher.matches();
            pattern = Pattern.compile("[a-zA-Z0-9]{1,45}");
            matcher = pattern.matcher(firstName);
            boolean firstNameIsValid = matcher.matches();
            matcher = pattern.matcher(lastName);
            boolean lastNameIsValid = matcher.matches();
            return loginIsValid && passwordIsValid && firstNameIsValid && lastNameIsValid;
        }
        return false;
    }
}
