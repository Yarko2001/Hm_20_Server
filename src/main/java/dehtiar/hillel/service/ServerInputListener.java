package dehtiar.hillel.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Yaroslav Dehtiar on 27.01.2023
 */
public class ServerInputListener extends Thread{

  private final BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

  @Override
  public void run() {
    while (true) {
      String input;
      try {
        input = inputReader.readLine();

        if (input.equals("-close")) MyServerService.getInstance().closeServer();

        MenuService.getInstance().sendToEveryone("Server", input);
      } catch (IOException e) {
        e.printStackTrace();
        System.out.println("Problems while trying to stop the server from terminal");
      }
    }
  }

}
