import java.util.*;

public abstract class Observable {
    private final List<Observer> obs = new ArrayList<>();//Liste des observateurs

    //ajoute un observateur à la liste
    public void addObserver(Observer o) {
        obs.add(o);
    }

    //notifie tous les observateurs
    public void notifyObservers() {
        for (Observer o : obs) o.update();
    }//appel update pr chaque observateur
}
