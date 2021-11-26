package by.ghoncharko.webproject.model.service;



import by.ghoncharko.webproject.entity.User;

import java.util.Optional;

public interface UserService extends Service<User> {
    Optional<User> authenticate(String login, String password);

    Optional<User> createClient(String login, String password, String firstName, String lastName);

    static UserService getInstance() {
        return UserServiceImpl.getInstance();
    }

}
