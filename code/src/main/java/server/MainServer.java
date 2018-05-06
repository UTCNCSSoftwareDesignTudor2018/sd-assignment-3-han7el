package server;

import server.communication.MyServer;

import java.io.IOException;

public class MainServer {

    public static final int PORT = 1002;

    public static void main(String[] args) throws IOException {

        new MyServer(PORT).listen();
    }
}
