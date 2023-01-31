package dehtiar.hillel.enums;

import java.util.Arrays;

/**
 * @author Yaroslav Dehtiar on 27.01.2023
 */
public enum Options {

  RECEIVE_FILE("-file", "(to send file on the server)"),
  EXIT("-exit", "(to close application)"),
  MESSAGE("", "or type message to other users)");

  private final String name;
  private final String description;

  Options(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public static Options getByName(String userInput) {
    return Arrays.stream(values())
        .filter(command -> command.name.equals(userInput))
        .findFirst()
        .orElse(MESSAGE);
  }

  @Override
  public String toString() {
    return name;
  }
}
