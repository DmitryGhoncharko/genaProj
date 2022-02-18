package by.ghoncharko.webproject.security;

public interface PasswordHasher {

    static PasswordHasher getInstance() {
        return BcryptWithSaltHasherImpl.getInstance();
    }

    boolean checkIsEqualsPasswordAndPasswordHash(String password, String passwordHash);

    String hashPassword(String password);
}
