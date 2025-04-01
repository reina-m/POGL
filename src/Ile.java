package src;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.awt.Point;

public class Ile {
    private Zone[][] grille;
    private final int rows = 6; //MODIFIERPARADAM
    private Joueur[] joueurs;
    private Point coordHeliport; // les coordonnées de l'héliport

    public Ile() {
        this.rows = 10;
        this.cols = 10;
        grille = new Zone[rows][cols];

        grille = new Zone[6][]; //MODIFIERPARADAM
        grille[0] = new Zone[2];
        grille[1] = new Zone[4];
        grille[2] = new Zone[6];
        grille[3] = new Zone[6];
        grille[4] = new Zone[4];
        grille[5] = new Zone[2];

        for (int i = 0; i < grille.length; i++) { //MODIFIERPARADAM
            for (int j = 0; j < grille[i].length; j++) {
                grille[i][j] = new Zone();
                if (!estIle(i, j)) {
                    grille[i][j].inonder();
                    grille[i][j].inonder();
                }
            }
        }
        init();
    }

    public void init() { //MODIFIERPARADAM
        joueurs = new Joueur[4];
        joueurs[0] = new Joueur(2, 4);
        joueurs[1] = new Joueur(2, 5);
        joueurs[2] = new Joueur(7, 4);
        joueurs[3] = new Joueur(7, 5);

        List<Point> pos = new ArrayList<>();
        // on exclus les bords car c'est là où les joueurs sont placés
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                if (estIle(i, j) && !((i == 2 && (j == 4 || j == 5)) || (i == 7 && (j == 4 || j == 5)))) {
                    pos.add(new Point(i, j));
                }
            }
        }
        Collections.shuffle(pos);

        Point p = pos.removeFirst();
        grille[p.x][p.y] = new Heliport();
        coordHeliport = p;

        for (Element e : Element.values()) {
            for (int k = 0; k < 2; k++) {
                Point z = pos.removeFirst();
                grille[z.x][z.y] = new ZoneElementaire(e);
            }
        }
    }

    // getters : 
    public int getRows() {
        return rows;
    }
    public int getCols() {
        return cols;
    }
    public Zone[][] getGrille() {
        return grille;
    }
    public Zone getZone(int x, int y) {return grille[x][y];}
    public Joueur[] getJoueurs() {
        return joueurs;
    }
    public Point getCoordHeliport() {return coordHeliport;}

    private boolean estIle(int i, int j) {

        if (i < 2 || i > 7) return false;
        if (i == 2 || i == 7) {
            return (j >= 4 && j <= 5);
        } else if (i == 3 || i == 6) {
            return (j >= 3 && j <= 6);
        } else if (i == 4 || i == 5) {
            return (j >= 2 && j <= 7);
        }
        return false;
    }

    public void inonderAleatoire() {
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            int row = rand.nextInt(getRows());
            int col = rand.nextInt(grille[row].length);
            if (grille[row][col].getEtat() != Zone.Etat.SUBMERGEE) {
                grille[row][col].inonder();
            }
        }
    }
}

