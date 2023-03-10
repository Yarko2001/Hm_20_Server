package dehtiar.hillel.service;

import dehtiar.hillel.model.MyServer;

/**
 * @author Yaroslav Dehtiar on 27.01.2023
 */
public class MyServerService {

  private MyServerService() {
  }

  private static class MyServerServiceHolder {

    private final static MyServerService instance = new MyServerService();
  }

  public static MyServerService getInstance() {
    return MyServerService.MyServerServiceHolder.instance;
  }

  public void closeServer() {

    MyServer.getInstance()
        .getSocketConnectors()
        .stream()
        .peek(socketConnector -> MenuService.getInstance()
            .sendPrivateMessage("[Server]: ", socketConnector, "Sorry our server is closing"))
        .forEach(socketConnector -> SocketConnectorService.getInstance()
            .closeConnection(socketConnector));

    System.out.println("Server is stopped!");
    System.exit(0);
  }

}
