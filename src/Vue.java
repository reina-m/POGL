
/**
 * TODO: FACTORISER LES VUES EN DIFFÉRENTS FICHIERS
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
        setSize(800, 600);
        // ajuste automatiquement la taille de la fenêtre au contenu
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
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
        setLocationRelativeTo(null); // centre la fenêtre
    }
    public void update() {
        vueIle.update();
    }
    public void bloquerActions(boolean b) {
        vueCmd.setActionsEnabled(!b);
    }
    public void setControlleur(Controlleur ctrl) {
        vueCmd.setControlleur(ctrl);
    }
    public void setJoueurActif(int j) {
        vueJoueurs.setJoueurActif(j); // délègue à VueJoueurs
    }
}

/**
 * Vue de l'île dans notre vue principale
 * (donc extends JPanel)
 */
class VueIle extends JPanel {
    private Ile ile;
    private VueJoueurs vueJoueurs;

    public VueIle(Ile ile, VueJoueurs vueJoueurs) {
        this.ile = ile;
        this.vueJoueurs = vueJoueurs;
        setLayout(new GridBagLayout());
        update();
    }

    public void update() {
        removeAll();
        Zone[][] grille = ile.getGrille();
        int[][] posJ = vueJoueurs.getPositions();

        GridBagConstraints gbc = new GridBagConstraints();

        for (int i = 0; i < grille.length; i++) {
            // Add offset for hexagonal shape
            gbc.gridx = (6 - grille[i].length) / 2; // Centers the row

            for (int j = 0; j < grille[i].length; j++) {
                JPanel pan = new JPanel();
                pan.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                pan.setPreferredSize(new Dimension(60, 60)); //AJOUTER PAR ADAM MAYBE ENLEVER
                pan.setBackground(getCouleur(grille[i][j].getEtat()));

                // Check joueurs
                for (int p = 0; p < posJ.length; p++) {
                    if (posJ[p][0] == i && posJ[p][1] == j) {
                        pan.setBackground(getCouleurJoueur(p));
                    }
                }

                gbc.gridy = i; // MAYBE USELESS ENLEVER
                add(pan, gbc);
                gbc.gridx++; // ,AYBE USELESS ENLEVER
            }
        }
        revalidate();
        repaint();
    }

    private Color getCouleur(Zone.Etat etat) {
        return switch (etat) {
            case NORMALE->Color.WHITE;
            case INONDEE -> Color.CYAN;
            case SUBMERGEE -> Color.BLUE;
            default-> Color.GRAY;
        };
    }

    private Color getCouleurJoueur(int p) {
        Color[] colors = {Color.RED, Color.ORANGE, Color.GREEN, Color.BLACK};
        return colors[p];
    }
}

class VueJoueurs extends JPanel {
    private JLabel[] icones;
    private Ile ile;
    private Joueur[] joueurs;
    private int sel = 0;

    public VueJoueurs(Vue vue, Ile ile) {
        this.ile = ile;
        this.joueurs = ile.getJoueurs();
        setLayout(new GridLayout(4, 1, 10, 10));
        icones = new JLabel[4];
        Color[] couleurs = {Color.RED, Color.ORANGE, Color.GREEN, Color.BLACK};

        for (int i = 0; i < 4; i++) {
            int p = i;
            icones[i] = creerIcone("J" + (i + 1), couleurs[i], () -> select(p));
            add(icones[i]);
        }
    }

    private JLabel creerIcone(String nom, Color couleur, Runnable clic) {
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
        int[][] pos = new int[4][2];
        for (int i = 0; i < 4; i++) {
            pos[i][0] = joueurs[i].getX();
            pos[i][1] = joueurs[i].getY();
        }
        return pos;
    }

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
        joueurs[sel].deplacer(dx, dy, ile);
        vueIle.update();
    }

    public void assecher(Ile ile, int dx, int dy) {
        joueurs[sel].assecher(ile, dx, dy);
    }

    public void setJoueurActif(int j) {
        select(j);
    }

    public Joueur getJoueurActif() {
        return joueurs[sel];
    }
}

class VueCommande extends JPanel {
    private Controlleur ctrl;
    private JButton finTour; // bouton à ne pas désactiver
    private boolean modeAssechement = false; // false = déplacement, true = assechement

    public void setControlleur(Controlleur ctrl) {
        this.ctrl = ctrl;
    }

    public VueCommande(Ile ile, VueIle vueIle, VueJoueurs vueJoueurs) {
        setLayout(new BorderLayout());
        // création du panneau pour les flèches de déplacement
        JPanel pan = new JPanel(new GridLayout(3, 3));
        JButton haut = new JButton("↑");
        JButton bas = new JButton("↓");
        JButton gauche = new JButton("←");
        JButton droite = new JButton("→");
        JButton centre = new JButton("•");

        // ajoute des lignes juste après chaque bouton
        Insets padding = new Insets(5, 10, 5, 10); // marges : haut, gauche, bas, droite
        haut.setMargin(padding);
        bas.setMargin(padding);
        gauche.setMargin(padding);
        droite.setMargin(padding);
        centre.setMargin(padding);

        haut.setToolTipText("Déplacer vers le haut");
        bas.setToolTipText("Déplacer vers le bas");
        gauche.setToolTipText("Déplacer vers la gauche");
        droite.setToolTipText("Déplacer vers la droite");
        centre.setToolTipText("Assécher la case actuelle");

        // les actions à faire selon le bouton qui est cliqué
        // actions dynamiques : déplacement ou assechement
        haut.addActionListener(e -> {
            if (ctrl != null) {
                if (modeAssechement) ctrl.effectuerAction(() -> vueJoueurs.assecher(ile, -1, 0));
                else ctrl.effectuerAction(() -> vueJoueurs.deplacerJoueur(-1, 0, vueIle));
            }
        });
        bas.addActionListener(e -> {
            if (ctrl != null) {
                if (modeAssechement) ctrl.effectuerAction(() -> vueJoueurs.assecher(ile, 1, 0));
                else ctrl.effectuerAction(() -> vueJoueurs.deplacerJoueur(1, 0, vueIle));
            }
        });
        gauche.addActionListener(e -> {
            if (ctrl != null) {
                if (modeAssechement) ctrl.effectuerAction(() -> vueJoueurs.assecher(ile, 0, -1));
                else ctrl.effectuerAction(() -> vueJoueurs.deplacerJoueur(0, -1, vueIle));
            }
        });
        droite.addActionListener(e -> {
            if (ctrl != null) {
                if (modeAssechement) ctrl.effectuerAction(() -> vueJoueurs.assecher(ile, 0, 1));
                else ctrl.effectuerAction(() -> vueJoueurs.deplacerJoueur(0, 1, vueIle));
            }
        });
        centre.addActionListener(e -> {
            if (ctrl != null && modeAssechement) {
                ctrl.effectuerAction(() -> vueJoueurs.assecher(ile, 0, 0));
            }
        });

        // ajouter toutes ces directions au panel
        pan.add(new JLabel()); pan.add(haut); pan.add(new JLabel());
        pan.add(gauche); pan.add(centre); pan.add(droite);
        pan.add(new JLabel()); pan.add(bas); pan.add(new JLabel());

        // mode assèchement / déplacement
        JButton toggle = new JButton("Mode : Déplacement");
        toggle.setMargin(padding);
        toggle.setToolTipText("Cliquez pour changer le mode entre déplacement et assèchement");

        toggle.addActionListener(e -> {
            modeAssechement = !modeAssechement;
            toggle.setText("Mode : " + (modeAssechement ? "Assèchement" : "Déplacement"));
        });
        add(toggle, BorderLayout.NORTH);

        // création du bouton "Fin de tour"
        finTour = new JButton("Fin de tour");
        finTour.setToolTipText("Terminer le tour et inonder trois zones aléatoires");
        finTour.setMargin(padding);
        finTour.addActionListener(e -> {
            if (ctrl != null) ctrl.finDeTour();
        });

        // ne pas oublier : ajouter les contrôles et le bouton "Fin de tour" dans la vue
        add(pan, BorderLayout.CENTER);
        add(finTour, BorderLayout.SOUTH);
    }
    public void setActionsEnabled(boolean enabled) {
        // active ou désactive tous les boutons sauf finTour
        for (Component c : this.getComponents()) {
            if (c instanceof JPanel) {
                for (Component b : ((JPanel) c).getComponents()) {
                    if (b instanceof JButton) {
                        b.setEnabled(enabled);
                    }
                }
            }
        }
    }

}
