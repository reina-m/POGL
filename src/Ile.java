import java.util.*;
import java.awt.*;
import java.util.List;

public class Ile extends Observable {
    private Zone[][] grille;
    private final int rows= 10, cols = 10;
    private Joueur[] joueurs = new Joueur[4];
    private Point coordHeliport; //Coordonnées de l'heliport
    private PaquetCartes<Point> paquetZones; // paquet pour les zones a inonder`
    private PaquetCartes<CarteTirage> paquetCartesJoueur; // paquet pioche des joueurs

    //Constructeur ile
    public Ile() {

        grille = new Zone[rows][cols];
        initialiserGrille();
        initZonesSpeciales();
        initPaquetZones();
        initPaquetCartesJoueur();
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

    //Initialise le paquet de cartes des zones valides
    private void initPaquetZones() {
        List<Point> c = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (estIle(i, j)) {
                    c.add(new Point(i, j));
                }
            }
        }
        paquetZones = new PaquetCartes<>(c);
    }

    //Initialise le paquet de pioche des joueurs
    private void initPaquetCartesJoueur() {
        List<CarteTirage> c = new ArrayList<>();
        for (Element e : Element.values()) {
            for (int i = 0; i < 5; i++) c.add(new CarteTirage(CarteTirage.Type.CLE, e));
        }
        for (int i = 0; i < 3; i++) c.add(new CarteTirage(CarteTirage.Type.MONTEE_DES_EAUX, null));
        for (int i = 0; i < 2; i++) c.add(new CarteTirage(CarteTirage.Type.HELICOPTERE, null));
        for (int i = 0; i < 2; i++) c.add(new CarteTirage(CarteTirage.Type.SAC_SABLE, null));

        paquetCartesJoueur = new PaquetCartes<>(c);
    }
    //Renvoie une carte tiree du paquet joueur
    public CarteTirage piocherCarteJoueur() {
        return paquetCartesJoueur.piocher();
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
    //NOUVELLE VERSION : utilise paquet pour inonder
    public void inonderAleatoire() {
        int c = 0;
        while (c < 3) {
            Point p = paquetZones.piocher();
            if (p == null) break;
            Zone z = grille[p.x][p.y];
            if (z.getEtat() == Zone.Etat.NORMALE) {

                paquetZones.defausser(p);
                z.inonder();
                c++;
            } else if (z.getEtat() == Zone.Etat.INONDEE) {
                paquetZones.retirerCarte(p);// retire définitivement si submergée
                z.inonder();
                c++;
            }
        }
        notifyObservers();
    }
    public Point inonderAleatoireEtRetourne() {
        while (true) {
            Point p = paquetZones.piocher();
            if (p == null) return null;

            Zone z = grille[p.x][p.y];
            if (z.getEtat() == Zone.Etat.NORMALE) {
                z.inonder();
                paquetZones.defausser(p);
                notifyObservers();
                return p;
            } else if (z.getEtat() == Zone.Etat.INONDEE) {
                z.inonder();
                paquetZones.retirerCarte(p);
                notifyObservers();
                return p;
            }
        }
    }
    public void defausserCarteJoueur(CarteTirage c) {
        paquetCartesJoueur.defausser(c);
    }

    //Getters
    public int getRows() {return rows;}
    public int getCols() {return cols;}
    public Zone[][] getGrille() {return grille;}
    public Zone getZone(int x, int y) {return grille[x][y];}
    public Joueur[] getJoueurs() {return joueurs;}
    public Point getCoordHeliport() {return coordHeliport;}

}

