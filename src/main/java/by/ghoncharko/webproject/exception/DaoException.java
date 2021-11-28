package by.ghoncharko.webproject.exception;

/**
 * Dao exception
 *
 * @author Dmitry Ghoncharko
 */
public class DaoException extends Exception {
    public DaoException() {
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
