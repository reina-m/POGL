import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Zone {
    /**
     * Les états possibles d'une zone
     */
    enum Etat { NORMALE, INONDEE, SUBMERGEE }
    private Etat etat;

    public Zone() {
        this.etat = Etat.NORMALE;
    }
    public Etat getEtat() {
        return etat;
    }

    public void inonder() {
        if (etat == Etat.NORMALE) etat = Etat.INONDEE;
        else if (etat == Etat.INONDEE) etat = Etat.SUBMERGEE;
    }
    public void assecher() {
        if (etat == Etat.INONDEE) etat = Etat.NORMALE;
    }
    // par défaut, ce n'est pas une zone spéciale
    public boolean estElementaire() { return false; }
    public boolean estHeliport() { return false; }
}

