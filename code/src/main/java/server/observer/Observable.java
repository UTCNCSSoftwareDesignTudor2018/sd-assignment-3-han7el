package server.observer;

public interface Observable {

    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyAllObservers();
}
