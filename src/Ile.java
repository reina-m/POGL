import java.util.*;
import java.awt.*;
import java.util.List;

public class Ile extends Observable {
    private Zone[][] grille;
    private final int rows= 10, cols = 10;
    private Joueur[] joueurs = new Joueur[4];
    private Point coordHeliport; //Coordonnées de l'heliport

    //Constructeur ile
    public Ile() {

        grille = new Zone[rows][cols];
        initialiserGrille();
        initZonesSpeciales();
    }

    //Initilalise la grille du jeux
    private void initialiserGrille() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grille[i][j] = new Zone();
                if (!estIle(i, j)) {
                    grille[i][j].inonder(); //Simule deux fois pour devenir submerge
                    grille[i][j].inonder();
                }
            }
        }
    }


    //Initialise les positions des joueurs, de l'heliport et des zones elementaires.
    private void initZonesSpeciales() {

        //Initialisation des joueurs
        joueurs[0] = new Joueur(2, 4);
        joueurs[1] = new Joueur(2, 5);
        joueurs[2] = new Joueur(7, 4);
        joueurs[3] = new Joueur(7, 5);

        List<Point> positionsDisponibles = new ArrayList<>();
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                if (estIle(i, j) && !estPositionJoueur(i, j)) {
                    positionsDisponibles.add(new Point(i, j));
                }
            }
        }
        Collections.shuffle(positionsDisponibles);

        //Place l'heliport
        Point heli = positionsDisponibles.removeFirst();
        grille[heli.x][heli.y] = new Heliport();
        coordHeliport = heli;

        //Place 2 zones pour chaque elements
        for (Element e : Element.values()) {
            for (int k = 0; k < 2; k++) {
                Point p = positionsDisponibles.removeFirst();
                grille[p.x][p.y] = new ZoneElementaire(e);
            }
        }
    }

    //Verifie si les coordoonees i,j correspondent a une position de depart de joueurs
    private boolean estPositionJoueur(int i, int j) {
        return (i == 2 && (j == 4 || j == 5)) || (i == 7 && (j == 4 || j == 5));
    }

    //Verifie si on peut jouer sur ce block
    public boolean estIle(int i, int j) {
        return switch (i) {
            case 2, 7 -> (j >= 4 && j <= 5);
            case 3, 6 -> (j >= 3 && j <= 6);
            case 4, 5 -> (j >= 2 && j <= 7);
            default -> false;
        };
    }

    //Fonction pour innonder aleatoirement la grille apres chaque fin de tour
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
        notifyObservers();
    }

    //Getters
    public int getRows() {return rows;}
    public int getCols() {return cols;}
    public Zone[][] getGrille() {return grille;}
    public Zone getZone(int x, int y) {return grille[x][y];}
    public Joueur[] getJoueurs() {return joueurs;}
    public Point getCoordHeliport() {return coordHeliport;}

}

