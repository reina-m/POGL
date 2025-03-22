import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import Modele.*
package Vues;

class CVue {
    private JFrame frame;
    private VueIle ile;
    private VueCommandes commandes;

    /** Construction d'une vue attachée à un modèle. */
    public CVue(Ile ile) {
        /** Définition de la fenêtre principale. */
        frame = new JFrame();
        frame.setTitle("L'île interdite");
        frame.setLayout(new FlowLayout());

        /** Définition des deux vues et ajout à la fenêtre. A COMPLETER
         * grille = new VueGrille(modele);
         * frame.add(grille);
         * commandes = new VueCommandes(modele);
         * frame.add(commandes);
         */

        /**
         *  - Ajustement de la taille de la fenêtre en fonction du contenu.
         *  - Indiquer qu'on quitte l'application si la fenêtre est fermée.
         *  - Préciser que la fenêtre doit bien apparaître à l'écran.
         */
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class VueIle extends JPanel implements Observer {
    private Ile ile;
    /*POUR MÉHTHODES ET FONCTIONS : VOIR VUE GRILLE DANS CONWAY*/
    }
}

class VueCommandes extends JPanel {
    private Ile ile ;
}

/*A RAJOUTER (?) DES VUES DE ZONES , SELON LES DIFFÉRENTES ZONES, VUE DES JOEURS*/