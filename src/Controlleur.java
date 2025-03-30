import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Controlleur implements ActionListener {
    // attributs : doit dépendre du modèle
    private Ile ile;
    private Vue vue;
    private int joueurCourant = 0; // index joueur actif
    private int actionsRestantes = 3;

    // constructeur :
    public Controlleur(Ile ile, Vue vue) {
        this.ile = ile;
        this.vue = vue;
    }

    public void actionPerformed(ActionEvent e) {
        // à compléter plus tard quand on branchera les boutons ici
    }

    //TODO : afficher le nombre de coups restants au joueur

    // méthodes :
    // le joueur peut faire jusqu'à 3 actions max
    public void effectuerAction(Runnable action) {
        if (actionsRestantes > 0) {
            action.run();
            actionsRestantes--;
            vue.update(); // méthode à ajouter dans Vue
            if (actionsRestantes == 0) {
                // désactiver les boutons sauf Fin de Tour
                vue.bloquerActions(true);
            }
        }
    }

    public void finDeTour() {
        // TODO : innonder aléatoire ne doit pas innoder des cases déjà submergées
        ile.inonderAleatoire();
        actionsRestantes = 3;
        joueurCourant = (joueurCourant + 1) % 4; // on change le joueur courant
        vue.setJoueurActif(joueurCourant);
        vue.update();
        vue.bloquerActions(false);
    }

}