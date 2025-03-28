import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
public class Ile {
    private Zone[][] grille;
    private final int rows, cols;

    public Ile(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grille = new Zone[rows][cols];

        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                grille[i][j] = new Zone();
            }
        }
    }

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

