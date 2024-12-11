package ir.ac.kntu.Meowter.exceptions;

public class LoginValidationException extends RuntimeException {
  public LoginValidationException(String message) {
    super(message);
  }
}
