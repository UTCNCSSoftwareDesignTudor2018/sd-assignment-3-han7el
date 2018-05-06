package client.controller;

import client.communication.MessageHandler;
import client.commonmark.CMParser;
import com.sun.xml.internal.stream.buffer.stax.StreamWriterBufferCreator;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jdk.internal.org.objectweb.asm.tree.IincInsnNode;
import org.controlsfx.control.CheckComboBox;
import server.entity.Article;
import server.entity.Writer;
import sun.security.util.Password;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GUIController implements Initializable {

    private WriterController writerController = null;
    private MessageHandler messageHandler;

    @FXML
    private TableView<Article> articleTable;
    private final ObservableList<Article> articlesList = FXCollections.observableArrayList();
    @FXML
    private WebView webView;
    @FXML
    private TextField userNameTextField;
    @FXML
    private PasswordField passwordField;

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void updateArticles(ArrayList<Article> articles){
        Platform.runLater(()->
        {
            articlesList.clear();
            articlesList.addAll(articles);
        });
    }

    public void updateWriters(ArrayList<Writer> writers){
        //not used
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageHandler.requestArticles();
        initializeTable();
    }

    private void initializeTable(){
        TableColumn<Article, String> titleCol = new TableColumn<Article, String>("Article title");
        titleCol.setMinWidth(100);
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Article, String> authorCol = new TableColumn<Article, String>("Writer");
        authorCol.setMinWidth(50);
        authorCol.setCellValueFactory(new PropertyValueFactory<>("writer"));
        TableColumn<Article, String> abstractCol = new TableColumn<Article, String>("Abstract");
        abstractCol.setMinWidth(50);
        abstractCol.setCellValueFactory(new PropertyValueFactory<>("articleAbstract"));
        articleTable.setItems(articlesList);
        articleTable.getColumns().addAll(titleCol, authorCol, abstractCol);

        articleTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
          System.out.println(newValue);
          if(newValue != null){
              webView.getEngine().loadContent(CMParser.getCMParser().parseToWebPage(newValue.getBody()),"text/html");
        }
        });
    }

    @FXML
    private void handleLoginAction(ActionEvent event){
        Writer newWriter = new Writer();
        newWriter.setUsername(userNameTextField.getText());
        newWriter.setPassword(passwordField.getText());
        Platform.runLater(()->
        messageHandler.login(newWriter));
    }

    public void loginWriter(Writer writer){
        Platform.runLater(()->
        {
            if(writerController != null)
                writerController.stage.close();
            writerController = new WriterController();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/view/WriterView.fxml"));
            fxmlLoader.setController(writerController);
            Parent root1 = null;
            try{
                root1 = fxmlLoader.load();
                Stage stage = new Stage();
                writerController.setStage(stage);
                stage.setTitle("Writer " + writer.getUsername());
                stage.setScene(new Scene(root1,1000,700));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void loginFailed(){
        Platform.runLater(()->
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Login failed!");
            errorAlert.setContentText("Invalid username or password!");
            errorAlert.showAndWait();
        });
    }

    public class WriterController implements Initializable{

        private Stage stage;
        @FXML
        private GridPane gridPane;
        @FXML
        private GridPane gridPaneEdit;
        private CheckComboBox<Article> relatedArticles;

        @FXML
        private TextField titleTextField;
        @FXML
        private TextArea abstractTextArea;
        @FXML
        private TextArea bodyTextArea;
        @FXML
        private WebView articleWebView;
        @FXML
        private Button createButton;

        @FXML
        private ComboBox<Article> editedArticleComboBox;
        @FXML
        private TextField titleEditTextField;
        @FXML
        private TextArea abstractEditTextArea;
        @FXML
        private WebView articleEditWebView;
        @FXML
        private TextArea bodyEditTextArea;
        @FXML
        private Button editButton;
        @FXML
        private CheckComboBox<Article> relatedEditArticles;

        @Override
        public void initialize(URL location, ResourceBundle resources) {

            initEditedArticleComboBox();
            initCheckComboBox();
            editedArticleComboBox.setOnAction(event -> selectEdited());
            createButton.setOnAction(event -> create());
            editButton.setOnAction(event -> edit());
            bodyTextArea.textProperty().addListener(((observable, oldValue, newValue) ->
                articleWebView.getEngine().loadContent(CMParser.getCMParser().parseToWebPage(newValue),"text/html")));
            bodyEditTextArea.textProperty().addListener((observable, oldValue, newValue) ->
                articleEditWebView.getEngine().loadContent(CMParser.getCMParser().parseToWebPage(newValue),
                            "text/html"));

        }

        public void selectEdited()
        {
            Article article = editedArticleComboBox.getSelectionModel().getSelectedItem();
            if(article != null){
                titleEditTextField.setText(article.getTitle());
                abstractEditTextArea.setText(article.getAbstractt());
                bodyEditTextArea.setText(article.getBody());
            }
        }

        public void create(){
            Article article = new Article();
            article.setTitle(titleTextField.getText());
            article.setAbstractt(abstractTextArea.getText());
            article.setBody(bodyTextArea.getText());
            messageHandler.createArticle(article);

            titleTextField.setText("");
            abstractTextArea.setText("");
            bodyTextArea.setText("");
            relatedArticles.getCheckModel().clearChecks();
        }

        public void edit(){
            Article originalArticle = editedArticleComboBox.getSelectionModel().getSelectedItem();
            Article editedArticle = new Article();
            editedArticle.setArticleid(originalArticle.getArticleid());
            editedArticle.setTitle(titleEditTextField.getText());
            editedArticle.setAbstractt(abstractEditTextArea.getText());
            editedArticle.setBody(bodyEditTextArea.getText());

            messageHandler.editArticle(editedArticle);

            titleEditTextField.setText("");
            abstractEditTextArea.setText("");
            bodyEditTextArea.setText("");
            relatedEditArticles.getCheckModel().clearChecks();

        }

        private void initEditedArticleComboBox(){
            editedArticleComboBox.setItems(GUIController.this.articlesList);
            editedArticleComboBox.setConverter(new StringConverter<Article>() {
                @Override
                public String toString(Article object) {
                    return object.getTitle();
                }

                @Override
                public Article fromString(String string) {
                    return null;
                }
            });
        }

        private void initCheckComboBox(){
            relatedArticles=new CheckComboBox<>(GUIController.this.articlesList);
            relatedArticles.setConverter(new StringConverter<Article>() {
                @Override
                public String toString(Article object) {
                    return object.getTitle();
                }

                @Override
                public Article fromString(String string) {
                    return null;
                }
            });
            gridPane.add(relatedArticles,1,2);
            relatedEditArticles=new CheckComboBox<>(GUIController.this.articlesList);
            relatedEditArticles.setConverter(new StringConverter<Article>() {
                @Override
                public String toString(Article object) {
                    return object.getTitle();
                }

                @Override
                public Article fromString(String string) {
                    return null;
                }
            });
            gridPaneEdit.add(relatedEditArticles,1,3);
        }

        public void setStage(Stage stage){
            this.stage = stage;
        }
    }
}
