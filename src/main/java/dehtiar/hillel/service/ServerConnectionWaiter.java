package dehtiar.hillel.service;

import dehtiar.hillel.exceptions.CannotSetConnectionWithSocketException;
import dehtiar.hillel.model.MyServer;
import dehtiar.hillel.model.SocketConnector;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Yaroslav Dehtiar on 27.01.2023
 */
public class ServerConnectionWaiter extends Thread {

  private int sessionNumber;
  private final ServerSocket serverSocket;

  public ServerConnectionWaiter(ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
  }

  @Override
  public void run() {
    MenuService menuService = MenuService.getInstance();

    try {
      while (true) {
        Socket clientSocket = serverSocket.accept();
        try {
          SocketConnector clientConnector = SocketConnector.createAndStart(clientSocket, getClientName());
          MyServer.getInstance()
              .getSocketConnectors()
              .add(clientConnector);

          menuService.sendToEveryone("", clientConnector + " connected our server.");
        } catch (CannotSetConnectionWithSocketException e) {
          e.printStackTrace();
          clientSocket.close();
        }
      }
    } catch (IOException e) {
      System.out.println("Connection is broken.");
      e.printStackTrace();
      System.exit(1);
    }
  }

  private synchronized String getClientName() {
    return "Client-" + (++sessionNumber);
  }

}
