package by.ghoncharko.webproject.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateRegistration {
    private ValidateRegistration(){
    }

    public boolean validateRegistration(String login, String password, String firstName, String lastName) {
        if (login != null && password != null && firstName != null && lastName != null) {
            Pattern pattern = Pattern.compile(".{6,35}");
            Matcher matcher = pattern.matcher(login);
            boolean loginIsValide = matcher.find();
            pattern = Pattern.compile("(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,35}");
            matcher = pattern.matcher(password);
            boolean passwordIsValide = matcher.find();
            pattern = Pattern.compile(".{1,35}");
            matcher = pattern.matcher(firstName);
            boolean firstNameIsValide = matcher.find();
            matcher = pattern.matcher(lastName);
            boolean lastNameIsValide = matcher.find();
            return loginIsValide && passwordIsValide && firstNameIsValide && lastNameIsValide;
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
