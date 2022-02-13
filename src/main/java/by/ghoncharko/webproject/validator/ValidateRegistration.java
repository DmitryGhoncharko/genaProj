package by.ghoncharko.webproject.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateRegistration {
    private ValidateRegistration(){
    }

    public boolean validateRegistration(String login, String password, String firstName, String lastName) {
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

    public static ValidateRegistration getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ValidateRegistration INSTANCE = new ValidateRegistration();
    }
}
