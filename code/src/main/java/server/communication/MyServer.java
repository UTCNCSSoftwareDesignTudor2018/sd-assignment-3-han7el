package server.communication;

import java.io.IOException;
import java.net.ServerSocket;

public class MyServer {

    private final ServerSocket serverSocket;
    private final int portNumber;
    
    public MyServer(int portNumber) throws IOException{
        this.portNumber = portNumber;
        this.serverSocket = new ServerSocket(portNumber);

        System.out.println("Server listening on port: " + this.portNumber);
    }

    public void listen()
    {
        try{
            while(true) {
                new ClientHandler(serverSocket.accept(), new MessageHandler());
            } }catch (IOException e){
            e.printStackTrace();
        }
    }
}
