package server.communication;

import com.google.gson.Gson;
import message.MessageWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler {

    private static int clientNumbers = 0;
    private final Socket clientSocket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final MessageHandler messageHandler;
    private final Gson gson = new Gson();
    private Thread listenerThread;
    private int clientNumber = clientNumbers++;

    public ClientHandler(Socket clientSocket, MessageHandler messageHandler) throws IOException {

        this.clientSocket = clientSocket;
        this.messageHandler = messageHandler;
        out = new PrintWriter(clientSocket.getOutputStream(),true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        messageHandler.setClientHandler(this);
        System.out.println("Client connected!");
        listen();
    }

    public void sendMessage(MessageWrapper messageWrapper) {
        System.out.println("Send to client " + ClientHandler.this.clientNumber + " " + messageWrapper);
        out.println(gson.toJson(messageWrapper));
    }

    private void listen(){
        listenerThread = new Thread(new ClientHandler.IncomingMessageHandler());
        listenerThread.start();
    }

    private class IncomingMessageHandler implements Runnable
    {

        @Override
        public void run() {
            String messageString;
            MessageWrapper messageWrapper;
            System.out.println("Listening for client" + clientNumber);
            try{
                while((messageString=ClientHandler.this.in.readLine())!=null){
                    messageWrapper = gson.fromJson(messageString,MessageWrapper.class);
                    System.out.println("Receive from client " + ClientHandler.this.clientNumber + " " + messageWrapper);
                    ClientHandler.this.messageHandler.processMessage(messageWrapper);
                }
            }catch (IOException e){
                System.out.println(e.getMessage());
            }finally {
                System.out.println("Communication lost with client " + clientNumber);
                messageHandler.close();
                out.close();
                try{
                    in.close();
                }catch (IOException e){
                    System.out.println(e.getMessage());
                }finally {
                    try{
                        clientSocket.close();
                    }catch (IOException e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}
