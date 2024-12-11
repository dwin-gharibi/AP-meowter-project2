package ir.ac.kntu.Meowter.exceptions;

public class ExistingUserException extends RuntimeException {
    public ExistingUserException(String message) {
        super(message);
    }
}
