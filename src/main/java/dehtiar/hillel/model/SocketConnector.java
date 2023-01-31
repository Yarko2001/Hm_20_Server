package dehtiar.hillel.model;

import dehtiar.hillel.NotMyExecutor;
import dehtiar.hillel.enums.Options;
import dehtiar.hillel.exceptions.CannotSetConnectionWithSocketException;
import dehtiar.hillel.service.MenuService;
import dehtiar.hillel.service.SocketConnectorService;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.Map;
import lombok.Data;

/**
 * @author Yaroslav Dehtiar on 27.01.2023
 */
@Data
public class SocketConnector implements Runnable {

  private Thread thread;
  private final Socket socket;
  private final Date connectionTimeStamp;
  private final BufferedReader reader;
  private final BufferedWriter sender;
  private boolean running;

  public static SocketConnector createAndStart(Socket socket, String name) throws CannotSetConnectionWithSocketException {

    SocketConnector clientConnector = new SocketConnector(socket, name);
    clientConnector.thread.start();

    MenuService.getInstance().printCommandMenu(clientConnector);

    return clientConnector;
  }

  private SocketConnector(Socket socket, String name) throws CannotSetConnectionWithSocketException {

    thread = new Thread(this, name);
    this.socket = socket;
    connectionTimeStamp = new Date();
    try {
      reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      sender = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    } catch (IOException e) {
      e.printStackTrace();
      throw new CannotSetConnectionWithSocketException();
    }
  }

  @Override
  public void run() {

    SocketConnectorService socketConnectorService = SocketConnectorService.getInstance();
    Map<Options, NotMyExecutor> commandHandler = initializeCommands(socketConnectorService);
    running = true;

    String userInput;
    try {
      while (running) {
        userInput = reader.readLine();
        if (userInput == null) continue;

        String command = socketConnectorService.parseUserInput(userInput);

        commandHandler
            .getOrDefault(Options.getByName(command)
                , socketConnectorService.sendMessageForAllConnected(thread.getName(), userInput))
            .execute();
      }
    } catch (SocketException e) {
      e.printStackTrace();
      System.out.println(thread.getName() + " socket exception.");
    } catch (IOException | RuntimeException e) {
      e.printStackTrace();
      System.out.println(thread.getName() + " socket is abandoned.");
    }
    SocketConnectorService.getInstance().closeConnection(this);
  }

  private Map<Options, NotMyExecutor> initializeCommands(
      SocketConnectorService clientConnectorService) {
    return Map.of(
        Options.RECEIVE_FILE, clientConnectorService.receiveFile(this),
        Options.EXIT, clientConnectorService.closeConnection(this)
    );
  }

  @Override
  public String toString() {
    return thread.getName();
  }
}
