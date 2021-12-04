package by.ghoncharko.webproject.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateLogin {
    private ValidateLogin(){
    }
    public boolean validate(String login, String password) {
        if (login != null && password != null) {
            Pattern pattern = Pattern.compile(".{6,35}");
            Matcher matcher = pattern.matcher(login);
            boolean loginIsValide = matcher.find();
            pattern = Pattern.compile("(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,35}");
            matcher = pattern.matcher(password);
            boolean passwordIsValide = matcher.find();
            return loginIsValide && passwordIsValide;
        }
        return false;
    }


    public static ValidateLogin getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ValidateLogin INSTANCE = new ValidateLogin();
    }
}
