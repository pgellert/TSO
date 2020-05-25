package tsostore;

public class AbortException extends Exception {

  public AbortException() {
  }

  public AbortException(String message) {
    super(message);
  }
}
