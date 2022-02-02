package by.ghoncharko.webproject.security;

public interface PasswordHasher {

        boolean checkIsEqualsPasswordAndPasswordHash(String password, String passwordHash);

        String hashPassword(String password);

         static PasswordHasher getInstance() {
                return BcryptWithSaltHasherImpl.getInstance();
        }
}
