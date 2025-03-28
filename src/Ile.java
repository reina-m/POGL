import java.util.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class Ile {
    private Zone[][] grille;
    private final int rows, cols;
    private Joueur[] joueurs;

    public Ile(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grille = new Zone[rows][cols];

        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                grille[i][j] = new Zone();
            }
        }
        init();
    }
    
    /**
     * Fonction qui place les zones spéciales (héliport, zone élémentaires) dans l'île
     */
    public void init() {
        joueurs = new Joueur[4];
        joueurs[0] = new Joueur(0, 0);
        joueurs[1] = new Joueur(0, cols - 1);
        joueurs[2] = new Joueur(rows - 1, 0);
        joueurs[3] = new Joueur(rows - 1, cols - 1);

        List<Point> pos = new ArrayList<>();
        // on exclus les bords car c'est là où les joueurs sont placés
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                pos.add(new Point(i, j));
            }
        }
        Collections.shuffle(pos); // on mélange les positions

        // placer héliport
        Point p = pos.removeFirst();
        grille[p.x][p.y] = new Heliport();

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

    /**
     * Fonction pour innonder aléatoirement la grille après chaque fin de tour
     */
    public void inonderAleatoire() {
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            int x = rand.nextInt(getRows());
            int y = rand.nextInt(getCols());
            if (grille[x][y].getEtat() != Zone.Etat.SUBMERGEE) {
                grille[x][y].inonder();
            }
        }
    }
}

