package by.ghoncharko.webproject.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

public class BcryptWithSaltHasherImpl implements PasswordHasher {
    private static final Logger LOG = LogManager.getLogger(BcryptWithSaltHasherImpl.class);
    private static final String SALT = BCrypt.gensalt();

    private BcryptWithSaltHasherImpl() {
    }

    public static BcryptWithSaltHasherImpl getInstance() {
        return Holder.INSTANCE;
    }


    @Override
    public boolean checkIsEqualsPasswordAndPasswordHash(String password, String passwordHash) { ;
        return BCrypt.checkpw(password, passwordHash);
    }

    @Override
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, SALT);
    }

    private static class Holder {
        private static final BcryptWithSaltHasherImpl INSTANCE = new BcryptWithSaltHasherImpl();
    }
}
