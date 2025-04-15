import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

//Constructeur
//Initialise grille vertcialle
//Associe des couleurs a chaque joueurs
//lie linterface graphique d lile
//Cree icone cliquable pour chaque joueurs
class VueJoueurs extends JPanel implements Observer {
    private JLabel[] icones= new JLabel[4];
    private JLabel[] infoLabels = new JLabel[4];
    private Ile ile;
    private Joueur[] joueurs;
    private int joueurSelectionne = 0;

    public VueJoueurs(Vue vue, Ile ile) {
        this.ile = ile;
        this.joueurs = ile.getJoueurs();
        setLayout(new GridLayout(6, 1, 10, 10));

        Color[] couleurs = {
                Color.decode("#fcbcd4"), //Joueur 0
                Color.decode("#dcdcdc"), //Joueur 1
                Color.decode("#d4c4ec"), //Joueur 2
                Color.decode("#a4fcec")  //Joueur 3
        };

        Font px = vue.pixelFont(10f);

        for (int i = 0; i < 4; i++) {
            int p = i;
            JPanel joueurPanel = new JPanel();
            joueurPanel.setLayout(new BorderLayout());
            joueurPanel.setPreferredSize(new Dimension(90, 60));

            JLabel nom = creerIcone("J" + (i + 1), couleurs[i], () -> select(p));
            nom.setFont(px);
            icones[i] = nom;

            //Pour afficher les artefacts/les cles des differents joueurs
            JLabel infos = new JLabel("<html>Clés: " +
            "<br>Artefacts: " +  "<br>🪣: " + " 🚁: " + "</html>");
            infos.setHorizontalAlignment(SwingConstants.CENTER);
            infoLabels[i] = infos;

            joueurPanel.add(nom, BorderLayout.CENTER);
            joueurPanel.add(infos, BorderLayout.SOUTH);

            add(joueurPanel);
        }
    }

    //Cree un icone cliquable pour representer un joueurs
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

    //Met a jour le joueur actuellement selectionne
    private void select(int p) {
        joueurSelectionne = p;
        resetBords();
        icones[p].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));
    }

    //Remet toutes les bordures des icones a une bordures noire standard
    private void resetBords() {
        for (JLabel icon : icones) {
            icon.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        }
    }

    //Change le joueur actif(celui surligne en jaune)
    public void setJoueurActif(int j) {
        select(j);
    }

    //Retourne le joueur actuellement selectionne
    public Joueur getJoueurActif() {
        return joueurs[joueurSelectionne];
    }

    //Retourne un tableau avec les positions x,y de chaque joueurs
    public int[][] getPositions() {
        int[][] pos = new int[4][2];
        for (int i = 0; i < 4; i++) {
            pos[i][0] = joueurs[i].getX();
            pos[i][1] = joueurs[i].getY();
        }
        return pos;
    }

    //Deplace le joueur actif selon les deplacements
    public void deplacerJoueur(int dx, int dy, VueIle vueIle) {
        joueurs[joueurSelectionne].deplacer(dx, dy, ile);
        vueIle.update();
    }

    //Demande au joueur actif d'assecher une case situe a dx, dy de sa position
    public void assecher(Ile ile, int dx, int dy) {
        joueurs[joueurSelectionne].assecher(ile, dx, dy);
    }

    //Met a jour les informations affiches sous chaque icone de joueur
    public void updateInfos() {
        for (int i = 0; i < joueurs.length; i++) {
            String cles = joueurs[i].cles().toString();
            String artefacts = joueurs[i].artefacts().toString();
            int s = joueurs[i].getSacsDeSable();
            int h = joueurs[i].getHelicos();
            infoLabels[i].setText("<html>Clés: " + cles +
                    "<br>Artefacts: " + artefacts +
                    "<br>🪣: " + s + " 🚁: " + h +
                    "</html>");
        }
    }
    @Override
    public void update() {
        updateInfos();
    }
}
