import java.util.*;
import java.util.HashSet;
import java.util.Set;

public class Joueur {
    private int x, y;
    private final Set<Element> cles= new HashSet<>();
    private final Set<Element> artefacts= new HashSet<>();

    //Contrsucteur
    public Joueur(int x, int y) {
        this.x = x;
        this.y = y;
    }
    //Ajoute  un element/cle a la collection du joueur
    public void ajouterCle(Element e) { cles.add(e); }

    //Verifie si le joueur a un element/cle specific
    public boolean possedeCle(Element e) { return cles.contains(e); }

    //Permet au joueur de recuperer un artefact a partie d'une zone donnee
    public boolean recupererArtefact(Zone z) {

        //Si z est une zone elementaire
        if (z instanceof ZoneElementaire ze) {
            Element e = ze.getElement();
            if (cles.contains(e)) {
                artefacts.add(e);
                cles.remove(e);
                return true;
            }
        }
        return false;
    }

    //Verifie si position et valide dans ile, et si zone a cette position nest pas submerge
    public boolean estValide(Ile ile, int x, int y) {
        return x >= 0 && y >= 0 && x < ile.getRows() && y < ile.getCols()
                && ile.getZone(x, y).getEtat() != Zone.Etat.SUBMERGEE;
    }

    //Deplace joueur si nouvelle position est valide
    public void deplacer(int dx, int dy, Ile ile) {

        //Cas erreur mouvement diagonale
        if ((dx != 0 && dy != 0) || (dx == 0 && dy == 0)) {
            return;
        }

        int nx = x + dx;
        int ny = y + dy;

        if (estValide(ile, nx, ny)) {
            x = nx;
            y = ny;
        }
    }

    //Asseche une zone
    public void assecher(Ile ile, int dx, int dy) {
        int tx = x + dx;
        int ty = y + dy;
        if (tx >= 0 && ty >= 0 && tx < ile.getRows() && ty < ile.getCols()) { //MODIFIERPARADAM
            ile.getZone(tx, ty).assecher();
        }
    }

    //Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public Set<Element> cles() { return cles; }
    public Set<Element> artefacts() { return artefacts; }

    //Setters
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
}
