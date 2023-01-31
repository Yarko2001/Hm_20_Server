package dehtiar.hillel.exceptions;

import java.io.IOException;

/**
 * @author Yaroslav Dehtiar on 27.01.2023
 */
public class UserInputIsNullException extends IOException {

  public UserInputIsNullException() {
    super("User closed application incorrectly. Command is null.");
  }
}
