import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

package Modele;
/**
 * Interface des objets observateurs.
 */
interface Observer {
    public void update();
}

/**
 * Classe des objets pouvant être observés.
 */
abstract class Observable {

    private ArrayList<Observer> observers;
    public Observable() {
        this.observers = new ArrayList<Observer>();
    }
    public void addObserver(Observer o) {
        observers.add(o);
    }
    public void notifyObservers() {
        for(Observer o : observers) {
            o.update();
        }
    }
}

class Ile extends Observable {
    /*ATTRIBUTS : A RAJOUTER*/
    public Ile {
        /*A COMPLETER*/
    }
    /*METHODES : A COMPLETER
    * EXEMPLE : GET ZONE */
}

enum Etat {
    /**
     * État d'une zone : submergée, innondée, ou normale
     */
    SUB, INN, NORM
}

/**
 * Zones
 */
class Zone {
    private Ile ile;
    protected Etat etat;
    private final int x, y; // ses coordonées dans l'île (une grille (?))

    public Zone(...) {
        ...
        /*CONSTRUCTEUR À COMPLÉTER*/
    }

    /*GETTERS + SETTERS (?)*/
    public Etat getEtat() {return etat;}
    public int getX() {return x;}
    public int getY() {return y;}
    public void assecher() {this.etat = Etat.NORM;}
    public void innonder() {this.etat = Etat.INN;}
}

class Heliport extends Zone {
    // un héliport ne peut pas être innondé
}

enum Element {
    /**
     * Element d'une zone elementaire
     */
    AIR, EAU, TERRE, FEU
}

class ZoneElementaire extends Zone {
    private Element element;
    private boolean artefact; // ???
    public ZoneElementaire(Element element) {
        this.element = element;
    }
    public Element getElement() {return element;}
    public boolean contientARtefact() {return artefact;}
}