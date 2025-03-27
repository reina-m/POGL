
/**
 * À FAIRE : FACTORISER LES VUES EN DIFFÉRENTS FICHIERS 
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Vue principale du jeu. Créer les différentes vues et affiche
 * (donc extends : JFrame)
 */

class Vue extends JFrame {
    private VueIle vueIle;
    private VueJoueurs vueJoueurs;
    private VueCommande vueCmd;
    private Ile ile; // elle dépend du 'modèle', l'île dans notre cas

    public Vue(Ile ile) {
        this.ile = ile;
        setTitle("L'Île Interdite");
        setSize(800, 600); // amélioration : resize??
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // création des différentes vues
        vueJoueurs = new VueJoueurs(this, ile);
        vueIle = new VueIle(ile, vueJoueurs);
        vueCmd = new VueCommande(ile, vueIle, vueJoueurs);

        JPanel pan = new JPanel(new BorderLayout());
        pan.setPreferredSize(new Dimension(100, 300));
        pan.add(vueJoueurs, BorderLayout.NORTH);
        pan.add(vueCmd, BorderLayout.SOUTH);

        add(vueIle, BorderLayout.CENTER); // l'île au centre de la vue princpiale
        add(pan, BorderLayout.EAST); // la panel avec les commandes / joueurs à droite
        setVisible(true);
    }
}

/**
 * Vue de l'île dans notre vue principale
 * (donc extends JPanel)
 */
class VueIle extends JPanel {
    private Ile ile;
    private VueJoueurs vueJoueurs; // référence à la VueJoueurs (pour savoir ou dans l'île se positionne les joueurs

    public VueIle(Ile ile, VueJoueurs vueJoueurs) {
        this.ile = ile;
        this.vueJoueurs = vueJoueurs; // Associer la VueJoueurs
        setLayout(new GridLayout(ile.getRows(), ile.getCols()));
        update();
    }

    public void update() {
        removeAll();
        Zone[][] grille = ile.getGrille();
        int[][] posJ = vueJoueurs.getPositions(); // récupère les positions des joueurs

        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                JPanel pan = new JPanel();
                pan.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                pan.setBackground(getCouleur(grille[i][j].getEtat()));

                // vérifier si un joueur est présent sur cette panule
                for (int p = 0; p < posJ.length; p++) {
                    if (posJ[p][0] == i && posJ[p][1] == j) {
                        pan.setBackground(getCouleurJoueur(p)); // applique la couleur du joueur
                    }
                }
                add(pan);
            }
        }
        revalidate();
        repaint();
    }

    private Color getCouleur(Zone.Etat etat) {
        return switch (etat) {
            case NORMALE -> Color.WHITE;
            case INONDEE -> Color.CYAN;
            case SUBMERGEE -> Color.BLUE;
        };
    }

    private Color getCouleurJoueur(int p) {
        Color[] couleurs = {Color.RED, Color.ORANGE, Color.GREEN, Color.BLACK};
        return couleurs[p];
    }
}


class VueJoueurs extends JPanel {
    private JLabel[] icones; // les icones des joueurs (un texte pour dire de quel joueur il s'agit
    /*Positions initiales des joeurs :
    * (les différentes dépendences sont à modifier)
    */
    private Ile ile;
    private int sel = 0;

    private int[][] pos;

    public VueJoueurs(Vue vue, Ile ile) {
        this.ile = ile;
        setLayout(new GridLayout(4, 1, 10, 10));
        icones = new JLabel[4];
        Color[] couleurs = {Color.RED, Color.ORANGE, Color.GREEN, Color.BLACK};

        // positions dynamiques centrées aux coins de la grille
        int r = ile.getRows();
        int c = ile.getCols();
        pos = new int[][] {
                {0, 0},           // coin haut gauche
                {0, c - 1},       // coin haut droit
                {r - 1, 0},       // coin bas gauche
                {r - 1, c - 1}    // coin bas droit
        };

        for (int i = 0; i < 4; i++) {
            int p = i;
            icones[i] = creerIcone("J" + (i + 1), couleurs[i], () -> select(p));
            add(icones[i]);
        }
    }

    private JLabel creerIcone(String nom, Color couleur, Runnable clic) {
        // des trucs esthétique. risquent d'être changés
        JLabel lbl = new JLabel(nom, SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(couleur);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        lbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                clic.run();
            }
        });
        return lbl;
    }

    public int[][] getPositions() {
        return pos;
    }

    // pour la 'bordure' si on clique sur l'icône du joueur
    private void select(int p) {
        sel = p;
        resetBords();
        icones[p].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));
    }

    private void resetBords() {
        for (JLabel icon : icones) {
            icon.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        }
    }

    public void deplacerJoueur(int dx, int dy, VueIle vueIle) {
        int x = pos[sel][0] + dx;
        int y = pos[sel][1] + dy;
        if (estValide(x, y)) {
            pos[sel][0] = x;
            pos[sel][1] = y;
        }
        vueIle.update();
    }

    /*Dis si le mouvement est valide ou pas (si on est hors bords / on va dans une zone submergée)
    * il y a beaucoup d'autres cas à considérer / fonctions à coder pour améliorer ce projet */
    private boolean estValide(int x, int y) {
        return x >= 0 && x < ile.getRows() && y >= 0 && y < ile.getCols() && ile.getGrille()[x][y].getEtat() != Zone.Etat.SUBMERGEE;
    }
}

class VueCommande extends JPanel {
    public VueCommande(Ile ile, VueIle vueIle, VueJoueurs vueJoueurs) {
        setLayout(new BorderLayout());

        // création du panneau pour les flèches de déplacement
        JPanel pan = new JPanel(new GridLayout(3, 3));
        JButton haut = new JButton("↑");
        JButton bas = new JButton("↓");
        JButton gauche = new JButton("←");
        JButton droite = new JButton("→");

        // les actions à faire selon le bouton qui est cliqué
        haut.addActionListener(e -> { vueJoueurs.deplacerJoueur(-1, 0, vueIle); });
        bas.addActionListener(e -> { vueJoueurs.deplacerJoueur(1, 0, vueIle); });
        gauche.addActionListener(e -> { vueJoueurs.deplacerJoueur(0, -1, vueIle); });
        droite.addActionListener(e -> { vueJoueurs.deplacerJoueur(0, 1, vueIle); });

        // ajouter toutes ces directions au panel
        pan.add(new JLabel()); pan.add(haut); pan.add(new JLabel());
        pan.add(gauche); pan.add(new JLabel()); pan.add(droite);
        pan.add(new JLabel()); pan.add(bas); pan.add(new JLabel());

        // création du bouton "Fin de tour"
        JButton finTour = new JButton("Fin de tour");
        finTour.addActionListener(e -> {
            ile.inonderAleatoire();
            vueIle.update();
        });

        // ne pas oublier : ajouter les contrôles et le bouton "Fin de tour" dans la vue
        add(pan, BorderLayout.CENTER);
        add(finTour, BorderLayout.SOUTH);
    }
}
