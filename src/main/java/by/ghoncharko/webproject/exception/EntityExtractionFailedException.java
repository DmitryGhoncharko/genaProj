package by.ghoncharko.webproject.exception;

public class EntityExtractionFailedException extends Exception{
    public EntityExtractionFailedException() {
    }

    public EntityExtractionFailedException(String message) {
        super(message);
    }

    public EntityExtractionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
