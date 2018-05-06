package client;

import client.communication.MessageHandler;
import client.communication.MyClient;
import client.controller.GUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainClient extends Application {

   public static final String HOST = "127.0.0.1";
   public static final int PORT = 1002;

    private GUIController guiController;
    @Override
    public void start(Stage primaryStage) throws Exception {

        guiController = new GUIController();
        MessageHandler messageHandler = new MessageHandler(guiController);
        new MyClient(HOST,PORT,messageHandler);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/view/GeneralView.fxml"));
        fxmlLoader.setController(guiController);
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("General View");
        primaryStage.setScene(new Scene(root,1000,700));
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception{
        MainClient.launch(args);
    }
}
