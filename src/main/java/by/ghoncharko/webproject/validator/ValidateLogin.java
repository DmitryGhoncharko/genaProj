package by.ghoncharko.webproject.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateLogin {
    private ValidateLogin(){
    }
    public boolean validate(String login, String password) {
        if (login != null && password != null) {
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


    public static ValidateLogin getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ValidateLogin INSTANCE = new ValidateLogin();
    }
}
