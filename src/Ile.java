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

    public Ile() {

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
            }
        }
        init();
    }

    public void init() { //MODIFIERPARADAM
        joueurs = new Joueur[4];

        joueurs[0] = new Joueur(0, 0);   //TOP a gauche
        joueurs[1] = new Joueur(0, 1);  //top a droite
        joueurs[2] = new Joueur(5, 0);   //bottom gauche
        joueurs[3] = new Joueur(5, 1);   //bottom droite

        List<Point> pos = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            for (int j = 0; j < grille[i].length; j++) {

                if (!((i == 1 && (j == 0 || j == 3)) ||
                        (i == 4 && (j == 0 || j == 3)))) {
                    pos.add(new Point(i, j));
                }
            }
        }
        Collections.shuffle(pos);

        Point p = pos.removeFirst();
        grille[p.x][p.y] = new Heliport();

        for (Element e : Element.values()) {
            for (int k = 0; k < 2; k++) {
                Point z = pos.removeFirst();
                grille[z.x][z.y] = new ZoneElementaire(e);
            }
        }
    }

    public int getRows() { return rows; }
    public int getCols(int row) { return grille[row].length; } //MODIFIERPARADAM
    public Zone[][] getGrille() { return grille; }
    public Zone getZone(int x, int y) { return grille[x][y]; }
    public Joueur[] getJoueurs() { return joueurs; }

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

