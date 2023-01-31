package dehtiar.hillel.model;

import dehtiar.hillel.service.ServerConnectionWaiter;
import dehtiar.hillel.service.ServerInputListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Yaroslav Dehtiar on 27.01.2023
 */
public class MyServer {

  private final List<SocketConnector> socketConnectors = new CopyOnWriteArrayList<>();

  private MyServer() {}

  private static class MyServerHolder {
    private final static MyServer instance = new MyServer();
  }

  public static MyServer getInstance() {
    return MyServer.MyServerHolder.instance;
  }

  public List<SocketConnector> getSocketConnectors() {
    return socketConnectors;
  }

  public void start(){
    try {
      ServerSocket serverSocket = new ServerSocket(10160);
      System.out.println("Server was started!");

      ServerConnectionWaiter serverConnectionsWaiter = new ServerConnectionWaiter(serverSocket);
      serverConnectionsWaiter.start();

      ServerInputListener serverInputListener = new ServerInputListener();
      serverInputListener.start();

    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Server crashed.");
      System.exit(1);
    }

  }

}
