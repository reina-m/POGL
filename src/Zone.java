import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Zone {

    //Etat possible d'une zone
    enum Etat { NORMALE, INONDEE, SUBMERGEE }
    private Etat etat;

    //Constructeur
    public Zone() {
        this.etat = Etat.NORMALE;
    }
    public Etat getEtat() {
        return etat;
    }

    //Inonde la zone, NORMALE -> INONDEE -> SUBMERGEE
    public void inonder() {
        if (etat == Etat.NORMALE) etat = Etat.INONDEE;
        else if (etat == Etat.INONDEE) etat = Etat.SUBMERGEE;
    }

    //Asseche la zone, INONDEE -> NORMALE
    public void assecher() {
        if (etat == Etat.INONDEE) etat = Etat.NORMALE;
    }

    //Verifie si zone est accessible, par defaut toute zone est accessible
    public boolean estAccessible() { return true; }

    //Verifie si la zone est elementaire
    public boolean estElementaire() { return false; }

    //Verifie si la zone est l'heliport
    public boolean estHeliport() { return false; }
}

