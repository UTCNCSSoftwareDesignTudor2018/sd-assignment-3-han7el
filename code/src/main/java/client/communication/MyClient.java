package client.communication;

import com.google.gson.Gson;
import message.MessageWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MyClient {

    private final Socket serverSocket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final MessageHandler messageHandler;
    private final Gson gson = new Gson();
    private Thread listenerThread;

    public MyClient(String hostName,int portNumber,MessageHandler messageHandler) throws IOException {

        this.messageHandler = messageHandler;
        serverSocket = new Socket(hostName,portNumber);
        out = new PrintWriter(serverSocket.getOutputStream(),true);
        in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        messageHandler.setClient(this);
        listen();
    }

    public void listen(){

        listenerThread = new Thread(new IncomingMessageHandler());
        listenerThread.start();
    }

    public void sendMessage(MessageWrapper messageWrapper){
        out.println(gson.toJson(messageWrapper));
    }

    public void closeConnection(){
        try{
            listenerThread.interrupt();
            in.close();
            out.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class IncomingMessageHandler implements Runnable{

        @Override
        public void run() {
            String messageString;
            MessageWrapper messageWrapper;
            try{
                while((messageString=MyClient.this.in.readLine())!=null){
                    messageWrapper = gson.fromJson(messageString,MessageWrapper.class);
                    MyClient.this.messageHandler.processMessage(messageWrapper);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
