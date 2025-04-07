import java.util.*;

public abstract class Observable {
    private final List<Observer> obs = new ArrayList<>();
    public void addObserver(Observer o) {
        obs.add(o);
    }
    public void notifyObservers() {
        for (Observer o : obs) o.update();
    }
}
