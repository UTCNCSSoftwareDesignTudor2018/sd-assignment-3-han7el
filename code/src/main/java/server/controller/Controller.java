package server.controller;

import server.entity.Article;
import server.entity.Writer;
import server.observer.Observable;
import server.observer.Observer;
import server.persistence.PersistenceDAO;

import java.util.ArrayList;
import java.util.List;

public class Controller implements Observable {

    private static final Controller singleInstance = new Controller();
    private final PersistenceDAO  persistenceDAO;
    private final List<Observer> observers;

    private Controller(){
        persistenceDAO = PersistenceDAO.getPersistence();
        observers = new ArrayList<>();
    }

    public static Controller getController(){
        return singleInstance;
    }

    public ArrayList<Article> getAllArticles(){
        return persistenceDAO.getAllArticles();
    }

    public void createArticle(Article article){
        persistenceDAO.insertArticle(article);
        notifyAllObservers();
    }

    public Writer login(Writer writer){
        return persistenceDAO.loginWriter(writer);
    }

    public ArrayList<Writer> getAllWriters(){
        return persistenceDAO.getAllWriters();
    }

    public void updateWriter(Writer writer){
        persistenceDAO.updateWriter(writer);
        notifyAllObservers();
    }

    public void updateArticle(Article article){
        Article original = persistenceDAO.getArticleById(article.getArticleid());
        article.setAuthorid(original.getAuthorid());
        persistenceDAO.updateArticle(article);
        notifyAllObservers();
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyAllObservers() {

        for(Observer o : observers){
            o.updateArticles();
            System.out.println("notified observer: " + o);
        }
    }
}
