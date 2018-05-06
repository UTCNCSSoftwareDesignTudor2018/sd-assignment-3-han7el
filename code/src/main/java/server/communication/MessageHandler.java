package server.communication;

import message.MessageWrapper;
import server.controller.Controller;
import server.entity.Article;
import server.entity.Writer;
import server.observer.Observer;
import utility.Utility;

public class MessageHandler implements Observer {

    private static final String REQUEST_ARTICLES="request_articles";
    private static final String RECEIVE_ARTICLES ="receive_articles";
    private static final String REQUEST_LOGIN ="login";
    private static final String SEND_LOGIN_TYPE_WRITER ="login_successful_writer";
    private static final String SEND_LOGIN_FAILED ="login_failed";

    private static final String CREATE_ARTICLE ="create_article";
    private static final String EDIT_ARTICLE ="edit_article";

    private static final String REQUEST_WRITERS="request_writers";
    private static final String RECEIVE_WRITERS="receive_writers";

    private static final Controller controller = Controller.getController();

    private ClientHandler clientHandler;
    private Writer loggedWriter = null;

    public MessageHandler(){
        controller.addObserver(this);
    }

    public void processMessage(MessageWrapper messageWrapper){

        if(messageWrapper.getCommand().equals(REQUEST_ARTICLES)){
            sendUpdateArticlesMessage();
        }
        else if(messageWrapper.getCommand().equals(REQUEST_LOGIN)){
            loggedWriter = controller.login((Writer)Utility.deserializeObject(messageWrapper.getMessage()));
            if(loggedWriter == null) {
                clientHandler.sendMessage(new MessageWrapper(SEND_LOGIN_FAILED,null));
            }
            if(loggedWriter != null){
                clientHandler.sendMessage(new MessageWrapper(SEND_LOGIN_TYPE_WRITER,Utility.serializeObject(loggedWriter)));
            }
        }
        else  if(messageWrapper.getCommand().equals(REQUEST_WRITERS)){
            clientHandler.sendMessage(new MessageWrapper(RECEIVE_WRITERS,Utility.serializeObject(controller.getAllWriters())));
        }
        else if(messageWrapper.getCommand().equals(CREATE_ARTICLE)){
            Article article = (Article)Utility.deserializeObject(messageWrapper.getMessage());
            article.setAuthorid(loggedWriter.getWriterid());
            controller.createArticle(article);
        }
        else if(messageWrapper.getCommand().equals(EDIT_ARTICLE)){
            Article article = (Article)Utility.deserializeObject(messageWrapper.getMessage());
            controller.updateArticle(article);
        }
    }

    private void sendUpdateArticlesMessage(){
        clientHandler.sendMessage(new MessageWrapper(RECEIVE_ARTICLES,Utility.serializeObject(controller.getAllArticles())));
    }

    public void setClientHandler(ClientHandler clientHandler){
        this.clientHandler = clientHandler;
    }

    @Override
    public void updateArticles() {
        sendUpdateArticlesMessage();
    }

    public void close(){
        controller.removeObserver(this);
    }
}
