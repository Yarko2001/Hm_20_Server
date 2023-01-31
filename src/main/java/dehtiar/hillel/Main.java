package dehtiar.hillel;

import dehtiar.hillel.model.MyServer;

/**
 * @author Yaroslav Dehtiar on 27.01.2023
 */
public class Main {

  public static void main(String[] args) {
    MyServer myServer = MyServer.getInstance();
    myServer.start();
  }
}
