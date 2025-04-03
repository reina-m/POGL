import java.util.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class Ile {
    private Zone[][] grille;
    private final int rows, cols;
    private Joueur[] joueurs;
    private Point coordHeliport; // les coordonnées de l'héliport

    public Ile() {
        this.rows = 10;
        this.cols = 10;
        grille = new Zone[rows][cols];

        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                grille[i][j] = new Zone();
                if (!estIle(i, j)) {
                    grille[i][j].inonder();
                    grille[i][j].inonder();
                }
            }
        }
        init();
    }
    
    /**
     * Fonction qui place les zones spéciales (héliport, zone élémentaires) dans l'île
     */
    public void init() {
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
        Collections.shuffle(pos); // on mélange les positions

        // placer héliport
        Point p = pos.removeFirst();
        grille[p.x][p.y] = new Heliport();
        coordHeliport = p;

        // placer 2 zones pour chaque élément
        for (Element e : Element.values()) {
            for (int k = 0; k < 2; k++) {
                Point z = pos.removeFirst();
                grille[z.x][z.y] = new ZoneElementaire(e);
            }
        }
        // le reste en Zone normale (déjà fait dans le constructeur)
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

    public boolean estIle(int i, int j) {

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

    /**
     * Fonction pour innonder aléatoirement la grille après chaque fin de tour
     */
    public void inonderAleatoire() {
        Random r = new Random();
        int c = 0;
        while (c < 3) {
            int i = r.nextInt(rows), j = r.nextInt(cols);
            if (estIle(i, j)) {
                Zone z = grille[i][j];
                if (z.getEtat() != Zone.Etat.SUBMERGEE) {
                    z.inonder();
                    c++;
                }
            }
        }
    }
}

