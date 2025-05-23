import java.awt.*;
import javax.swing.*;

class VueCommande extends JPanel {
    private Controlleur ctrl;
    private final JButton finTour;  //Bouton a  ne pas desactiver
    private boolean modeAssechement = false;  //False = deplacement, true = assechement
    private final JLabel lblActionsRestantes; //Pour afficher les actions auxquelles le joueur a droit

    //Permet d'associer un controlleur a cette vue
    public void setControlleur(Controlleur ctrl) {
        this.ctrl = ctrl;
    }

    //Constructeur
    //Cree toute linterface graphique des commandes des joueurs
    //Ajouter bouttons deplacements, recuperation artefact etc...
    //Configure comportment chaque boutton
    //Organise elements avec panneaux
    public VueCommande(Ile ile, VueIle vueIle, VueJoueurs vueJoueurs) {
        setLayout(new BorderLayout());

        //Ajoute des lignes juste après chaque bouton
        Insets padding = new Insets(5, 10, 5, 10);

        //Haut (actions restantes + toggle mode)
        lblActionsRestantes = createLabel();
        JButton toggle = createButton("Mode : Déplacement", padding, "Cliquez pour changer le mode entre déplacement et assèchement");

        toggle.addActionListener(_ -> {
            modeAssechement = !modeAssechement;
            toggle.setText("Mode : " + (modeAssechement ? "Assèchement" : "Déplacement"));
        });

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        lblActionsRestantes.setAlignmentX(Component.CENTER_ALIGNMENT);
        toggle.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(lblActionsRestantes);
        topPanel.add(toggle);
        add(topPanel, BorderLayout.NORTH);

        //Centre (directional pad)
        JPanel directionPanel = new JPanel(new GridLayout(3, 3));
        directionPanel.add(new JLabel());
        directionPanel.add(createDirectionButton("↑", padding, "Déplacer vers le haut", -1, 0, ile, vueIle, vueJoueurs));
        directionPanel.add(new JLabel());

        directionPanel.add(createDirectionButton("←", padding, "Déplacer vers la gauche", 0, -1, ile, vueIle, vueJoueurs));
        directionPanel.add(createDirectionButton("•", padding, "Assécher la case actuelle", 0, 0, ile, vueIle, vueJoueurs));
        directionPanel.add(createDirectionButton("→", padding, "Déplacer vers la droite", 0, 1, ile, vueIle, vueJoueurs));

        directionPanel.add(new JLabel());
        directionPanel.add(createDirectionButton("↓", padding, "Déplacer vers le bas", 1, 0, ile, vueIle, vueJoueurs));
        directionPanel.add(new JLabel());

        add(directionPanel, BorderLayout.CENTER);

        //Bas (artefact + fin de tour)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        JButton recupArtefact = createButton("Artefact", padding, "Tente de récupérer un artefact sur cette zone");
        recupArtefact.setAlignmentX(Component.CENTER_ALIGNMENT);
        recupArtefact.addActionListener(_ -> {
            if (ctrl != null) ctrl.recupererArtefact();
        });

        finTour = createButton("Fin de tour", padding, "Terminer le tour et inonder trois zones aléatoires");
        finTour.setAlignmentX(Component.CENTER_ALIGNMENT);
        finTour.addActionListener(_ -> {
            if (ctrl != null) ctrl.finDeTour();
        });
        JButton btnSable = createButton("Sac de sable", padding, "Assécher n'importe quelle zone");
        btnSable.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSable.addActionListener(_ -> {
            if (ctrl != null) ctrl.activerSacDeSable();
        });
        JButton btnHelico = createButton("Hélicoptère", padding, "Se déplacer vers n'importe quelle zone");
        btnHelico.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHelico.addActionListener(_ -> {
            if (ctrl != null) ctrl.activerHelico();
        });
        bottomPanel.add(btnSable);
        bottomPanel.add(Box.createVerticalStrut(5));

        bottomPanel.add(btnHelico);
        bottomPanel.add(Box.createVerticalStrut(5));

        bottomPanel.add(recupArtefact);
        bottomPanel.add(Box.createVerticalStrut(5));
        bottomPanel.add(finTour);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    //Cree un boutton de direction(haut, bas , gauche, droite)
    private JButton createDirectionButton(String label, Insets padding, String tooltip, int dx, int dy, Ile ile, VueIle vueIle, VueJoueurs vueJoueurs) {
        JButton btn = createButton(label, padding, tooltip);
        btn.addActionListener(_ -> {
            if (ctrl != null) {
                if (modeAssechement || (dx == 0 && dy == 0)) {
                    ctrl.effectuerAction(() -> vueJoueurs.assecher(ile, dx, dy));
                } else {
                    ctrl.effectuerAction(() -> vueJoueurs.deplacerJoueur(dx, dy, vueIle));
                }
            }
        });
        return btn;
    }

    //Methode utilitaire qui cree un boutton standard
    private JButton createButton(String text, Insets padding, String tooltip) {
        JButton btn = new JButton(text);
        btn.setMargin(padding);
        btn.setToolTipText(tooltip);
        return btn;
    }

    //Cree un label stylise
    private JLabel createLabel() {
        JLabel lbl = new JLabel("Actions restantes: 3", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return lbl;
    }

    //Active ou desactive touts les bouttons
    public void setActionsEnabled(boolean enabled) {

        //Active ou désactive tous les boutons sauf finTour
        for (Component c : getComponents()) {
            if (c instanceof JPanel panel) {
                for (Component comp : panel.getComponents()) {
                    if (comp instanceof JButton btn && btn != finTour) {
                        btn.setEnabled(enabled);
                    }
                }
            }
        }
    }

    //Met a jour dynamiquement le nombre d'actions quil reste au joueur
    public void updateActionsRestantes(int n) {
        lblActionsRestantes.setText("Actions restantes: " + n);
    }
}
