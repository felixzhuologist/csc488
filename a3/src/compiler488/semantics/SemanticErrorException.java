package compiler488.semantics;

/*
 * Exception to be thrown whenever there is a semantic error
 */
public class SemanticErrorException extends Exception {
  public SemanticErrorException(String message) {
    super(message);
  }
}