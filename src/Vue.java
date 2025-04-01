
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
        pan.setPreferredSize(new Dimension(190, 300));
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
    public void updateActionsRestantes(int actions) {
        vueCmd.updateActionsRestantes(actions);
    }
    public void updateInfosJoueurs() {
        vueJoueurs.updateInfos();
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
                Zone zone = grille[i][j];
                if (zone instanceof ZoneElementaire ze) {
                    Element e = ze.getElement();
                    switch (e) {
                        case AIR -> pan.setBackground(Color.PINK);
                        case FEU -> pan.setBackground(Color.ORANGE);
                        case EAU -> pan.setBackground(Color.CYAN.darker());
                        case TERRE -> pan.setBackground(new Color(139, 69, 19)); // marron
                    }
                } else if (zone.estHeliport()) {
                    pan.setBackground(Color.MAGENTA);
                } else {
                    pan.setBackground(getCouleur(zone.getEtat()));
                }

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
    private JLabel[] icones;
    private Ile ile;
    private Joueur[] joueurs;
    private JLabel[] infoLabels = new JLabel[4];
    private int sel = 0;

    public VueJoueurs(Vue vue, Ile ile) {
        this.ile = ile;
        this.joueurs = ile.getJoueurs();
        setLayout(new GridLayout(4, 1, 10, 10));
        icones = new JLabel[4];
        Color[] couleurs = {Color.RED, Color.ORANGE, Color.GREEN, Color.BLACK};

        for (int i = 0; i < 4; i++) {
            int p = i;
            JPanel joueurPanel = new JPanel();
            joueurPanel.setLayout(new BorderLayout());

            JLabel nom = creerIcone("J" + (i + 1), couleurs[i], () -> select(p));
            icones[i] = nom;

            // pour afficher les artéfacts / les clés des différents joueurs
            JLabel infos = new JLabel("Clés:  /  Artefacts: ");
            infos.setFont(new Font("Arial", Font.PLAIN, 10));
            infos.setHorizontalAlignment(SwingConstants.CENTER);

            infoLabels[i] = infos;

            joueurPanel.add(nom, BorderLayout.CENTER);
            joueurPanel.add(infos, BorderLayout.SOUTH);

            add(joueurPanel);
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
    public void updateInfos() {
        for (int i = 0; i < joueurs.length; i++) {
            String cles = joueurs[i].cles().toString();
            String artefacts = joueurs[i].artefacts().toString();
            infoLabels[i].setText("Clés: " + cles + " / Artefacts: " + artefacts);
        }
    }
}

class VueCommande extends JPanel {
    private Controlleur ctrl;
    private JButton finTour; // bouton à ne pas désactiver
    private boolean modeAssechement = false; // false = déplacement, true = assechement
    private JLabel lblActionsRestantes; // pour afficher les actions auxquelles le joueur a droit

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
        JPanel pan2 = new JPanel();
        pan2.setLayout(new BoxLayout(pan2, BoxLayout.Y_AXIS));

        lblActionsRestantes = new JLabel("Actions restantes: 3", SwingConstants.CENTER);
        lblActionsRestantes.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblActionsRestantes.setFont(new Font("Arial", Font.BOLD, 14));
        lblActionsRestantes.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pan2.add(lblActionsRestantes);

        toggle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pan2.add(toggle);
        add(pan2, BorderLayout.NORTH);

        // panel pour les boutons en bas (artefact + fin de tour)
        JPanel pan3 = new JPanel();
        pan3.setLayout(new BoxLayout(pan3, BoxLayout.Y_AXIS));
        
        JButton recupArtefact = new JButton("Récupérer artefact");
        recupArtefact.setToolTipText("Tente de récupérer un artefact sur cette zone");
        recupArtefact.setMargin(padding);
        recupArtefact.setAlignmentX(Component.CENTER_ALIGNMENT);
        recupArtefact.addActionListener(e -> {
            if (ctrl != null) ctrl.recupererArtefact();
        });
        add(pan, BorderLayout.CENTER);
        pan3.add(recupArtefact);
        
        finTour = new JButton("Fin de tour");
        finTour.setToolTipText("Terminer le tour et inonder trois zones aléatoires");
        finTour.setMargin(padding);
        finTour.setAlignmentX(Component.CENTER_ALIGNMENT);
        finTour.addActionListener(e -> {
            if (ctrl != null) ctrl.finDeTour();
        });
        pan3.add(Box.createVerticalStrut(5)); // petit espace entre les boutons
        pan3.add(finTour);
        add(pan3, BorderLayout.SOUTH);
    }
    public void setActionsEnabled(boolean enabled) {
        // active ou désactive tous les boutons sauf finTour
        for (Component c : this.getComponents()) {
            if (c instanceof JPanel) {
                for (Component b : ((JPanel) c).getComponents()) {
                    if (b instanceof JButton btn && btn != finTour) {
                        b.setEnabled(enabled);
                    }
                }
            }
        }
    }
    public void updateActionsRestantes(int n) {
        lblActionsRestantes.setText("Actions restantes: " + n);
    }
}
