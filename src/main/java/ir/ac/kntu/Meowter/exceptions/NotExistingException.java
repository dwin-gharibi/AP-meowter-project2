package ir.ac.kntu.Meowter.exceptions;

public class NotExistingException extends RuntimeException {
    public NotExistingException(String message) {
        super(message);
    }
}
