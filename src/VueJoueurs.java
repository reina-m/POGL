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
        setLayout(new GridLayout(6, 1, 5, 7));

        Color[] couleurs = {
                Color.decode("#fcbcd4"),
                Color.decode("#dcdcdc"),
                Color.decode("#d4c4ec"),
                Color.decode("#a4fcec")
        };

        Font px = vue.pixelFont(10f);

        for (int i = 0; i < 4; i++) {
            int p = i;
            JPanel joueurPanel = new JPanel();
            joueurPanel.setLayout(new BoxLayout(joueurPanel, BoxLayout.X_AXIS));
            joueurPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            joueurPanel.setPreferredSize(new Dimension(240, 70));
            joueurPanel.setMinimumSize(new Dimension(240, 70));

            JLabel icon = new JLabel(new ImageIcon(getClass().getResource("/img/j" + i + ".png")));
            icon.setPreferredSize(new Dimension(82, 82));
            icon.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            icon.setOpaque(true);
            icon.setBackground(couleurs[i]);
            icon.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) { select(p); }
            });
            icones[i] = icon;

            JPanel infosPanel = new JPanel();
            infosPanel.setLayout(new BoxLayout(infosPanel, BoxLayout.Y_AXIS));
            infosPanel.setOpaque(false);

            JLabel infos = new JLabel();
            infos.setFont(px);
            infos.setPreferredSize(new Dimension(150, 48));
            infos.setMaximumSize(new Dimension(150, 48));
            infos.setMinimumSize(new Dimension(150, 48));
            infoLabels[i] = infos;
            infosPanel.add(infos);

            joueurPanel.add(Box.createHorizontalStrut(5));
            joueurPanel.add(icon);
            joueurPanel.add(Box.createHorizontalStrut(8));
            joueurPanel.add(infosPanel);

            add(joueurPanel);
        }
        select(0); // sélectionne automatiquement le premier joueur
        updateInfos();
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
            Joueur j = joueurs[i];

            JPanel joueurPanel = (JPanel) getComponent(i);
            JPanel infosPanel = (JPanel) joueurPanel.getComponent(3);
            infosPanel.removeAll();

            // clés row
            JPanel cleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
            cleRow.setOpaque(false);
            cleRow.add(new JLabel("Clés:"));
            for (Element e : j.cles()) {
                cleRow.add(new JLabel(loadIcon("cle_" + e.name().toLowerCase() + ".png")));
            }

            // artefacts row
            JPanel arteRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
            arteRow.setOpaque(false);
            arteRow.add(new JLabel("Artefacts:"));
            for (Element e : j.artefacts()) {
                arteRow.add(new JLabel(loadIcon("artefact_" + e.name().toLowerCase() + ".png")));
            }

            // pouvoirs row
            JPanel powRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
            powRow.setOpaque(false);
            powRow.add(new JLabel(loadIcon("sac.png")));
            powRow.add(new JLabel(": " + j.getSacsDeSable()));
            powRow.add(new JLabel(loadIcon("helico.png")));
            powRow.add(new JLabel(": " + j.getHelicos()));

            infosPanel.add(cleRow);
            infosPanel.add(arteRow);
            infosPanel.add(powRow);

            infosPanel.revalidate();
            infosPanel.repaint();
        }
    }


    @Override
    public void update() {
        updateInfos();
    }

    private ImageIcon loadIcon(String name) {
        java.net.URL url = getClass().getResource("/img/" + name);
        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT);
            return new ImageIcon(img);
        }
        return null;
    }

}
