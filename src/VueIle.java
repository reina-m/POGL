import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class VueIle extends JPanel implements Observer {
    private final Ile ile;
    private final VueJoueurs vueJoueurs;
    private static final int TILE_SIZE = 64;
    private Controlleur ctrl;

    //Constructeur
    //Initialise vue ile
    public VueIle(Ile ile, VueJoueurs vueJoueurs) {
        this.ile = ile;
        this.vueJoueurs = vueJoueurs;
        setLayout(null);
        new Timer(100, e -> repaint()).start();
        update();
    }

    //Supprime tout les elements actuelles affiches
    //Ajuste la taille en fonction de la taille de grille
    public void updateIle() {
        removeAll();

        Zone[][] grille = ile.getGrille();
        int[][] posJ = vueJoueurs.getPositions();

        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                JLayeredPane pan = createZonePanel(i, j, grille[i][j], posJ);
                add(pan);
                int finalI = i, finalJ = j;
                pan.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        ctrl.clicSurZone(finalI, finalJ);
                    }
                });

            }
        }


        //Taille auto en fonction de la grille
        setPreferredSize(new Dimension(grille[0].length * TILE_SIZE, grille.length * TILE_SIZE));


        revalidate();
        repaint();
    }

    //Cree un paneau graphique pour une zone individuelle
    private JLayeredPane createZonePanel(int row, int col, Zone z, int[][] posJ) {
        JLayeredPane pan = new JLayeredPane();
        pan.setBounds(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE); //Position absolue

        //Image du background
        JLabel fond = new JLabel(loadIcon(row + "_" + col + ".png", "eau.png"));
        fond.setBounds(0, 0, TILE_SIZE, TILE_SIZE);
        pan.add(fond, JLayeredPane.DEFAULT_LAYER);

        //Zone overlay (elementaire ou heliport)
        if (z != null && ile.estIle(row, col)) {
            ImageIcon overlayIcon = getZoneOverlay(z);
            if (overlayIcon != null) {
                JLabel overlay = new JLabel(overlayIcon);
                overlay.setBounds(0, 0, TILE_SIZE, TILE_SIZE);
                pan.add(overlay, JLayeredPane.PALETTE_LAYER);
            }

            //Etat overlay (inondee ou submergee)
            ImageIcon etatOverlay = getEtatOverlay(z.getEtat());
            if (etatOverlay != null) {
                JLabel ov = new JLabel(etatOverlay);
                ov.setBounds(0, 0, TILE_SIZE, TILE_SIZE);
                pan.add(ov, JLayeredPane.MODAL_LAYER);
            }
        }

        //Ajouter joueurs sur cette zone
        int n = 0;
        for (int p = 0; p < posJ.length; p++) {
            if (posJ[p][0] == row && posJ[p][1] == col) {
                JLabel pj = new JLabel(loadIcon("j" + p + ".gif", null));
                pj.setBounds(5 * n, -5, TILE_SIZE, TILE_SIZE); //Decalage horizontale
                pan.add(pj, JLayeredPane.DRAG_LAYER);
                n++;
            }
        }

        return pan;
    }

    //Retourne l'image corresepondant a une zone speciale
    private ImageIcon getZoneOverlay(Zone z) {
        String filename = null;
        if (z.estElementaire()) {
            Element e = ((ZoneElementaire) z).getElement();
            filename = switch (e) {
                case AIR -> "air.png";
                case EAU -> "eau2.png";
                case TERRE -> "terre.png";
                case FEU -> "feu.png";
            };
        } else if (z.estHeliport()) {
            filename = "heliport.png";
        }
        return filename != null ? loadIcon(filename, null) : null;
    }

    //Retourne une icone representant l'etat d'une zone
    private ImageIcon getEtatOverlay(Zone.Etat etat) {
        return switch (etat) {
            case INONDEE -> loadIcon("inn.png", null);
            case SUBMERGEE -> loadIcon("sub.png", null);
            default -> null;
        };
    }

    //Charge un icone depuis les ressources img
    private ImageIcon loadIcon(String name, String fallback) {
        java.net.URL url = getClass().getResource("/img/" + name);
        if (url != null) {
            return new ImageIcon(url);
        } else if (fallback != null) {
            java.net.URL fallbackUrl = getClass().getResource("/img/" + fallback);
            if (fallbackUrl != null) return new ImageIcon(fallbackUrl);
        }
        return null;
    }
    public void update() {
        updateIle();
    }

    public void setControlleur(Controlleur ctrl) {
        this.ctrl = ctrl;
    }
}
