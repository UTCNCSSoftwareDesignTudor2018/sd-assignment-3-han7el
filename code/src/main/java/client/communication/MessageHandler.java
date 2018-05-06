package client.communication;

import client.controller.GUIController;
import message.MessageWrapper;
import server.entity.Article;
import server.entity.Writer;
import utility.Utility;

import java.util.ArrayList;

public class MessageHandler {

    private static String REQUEST_ARTICLES="request_articles";
    private static String RECEIVE_ARTICLES ="receive_articles";
    private static final String RECEIVE_LOGIN_TYPE_WRITER ="login_successful_writer";
    private static final String RECEIVE_LOGIN_FAILED ="login_failed";
    private static final String SEND_LOGIN ="login";

    private static final String CREATE_ARTICLE ="create_article";
    private static final String EDIT_ARTICLE ="edit_article";

    private static final String REQUEST_WRITERS="request_writers";
    private static final String RECEIVE_WRITERS="receive_writers";
    private static final String CREATE_WRITER ="create_writer";
    private static final String CREATE_WRITER_FAILED ="create_writer_failed";

    private final GUIController guiController;
    private MyClient myClient;

    public MessageHandler(GUIController guiController){
        this.guiController = guiController;
        guiController.setMessageHandler(this);
    }

    public void processMessage(MessageWrapper messageWrapper){

        if(messageWrapper.getCommand().equals(RECEIVE_ARTICLES)){
            ArrayList<Article> articles = (ArrayList<Article>) Utility.deserializeObject(messageWrapper.getMessage());
            guiController.updateArticles(articles);
        }
        else if(messageWrapper.getCommand().equals(RECEIVE_WRITERS)){
            ArrayList<Writer> writers = (ArrayList<Writer>) Utility.deserializeObject(messageWrapper.getMessage());
            guiController.updateWriters(writers);
        }
        else if(messageWrapper.getCommand().equals(RECEIVE_LOGIN_FAILED)){
            guiController.loginFailed();
        }
        else if(messageWrapper.getCommand().equals(RECEIVE_LOGIN_TYPE_WRITER)){
            guiController.loginWriter((Writer)(Utility.deserializeObject(messageWrapper.getMessage())));
        }
    }

    public void requestArticles(){
        sendMessage(new MessageWrapper(REQUEST_ARTICLES,null));
    }

    public void login(Writer writer){
        sendMessage(new MessageWrapper(SEND_LOGIN,Utility.serializeObject(writer)));
    }

    public void createArticle(Article article){
        sendMessage(new MessageWrapper(CREATE_ARTICLE,Utility.serializeObject(article)));
    }

    public void editArticle(Article article){
        sendMessage(new MessageWrapper(EDIT_ARTICLE,Utility.serializeObject(article)));
    }

    private void sendMessage(MessageWrapper messageWrapper){
        myClient.sendMessage(messageWrapper);
    }

    void setClient(MyClient myClient){
        this.myClient = myClient;
    }
}
