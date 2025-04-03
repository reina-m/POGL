import java.util.*;
import java.util.HashSet;
import java.util.Set;

public class Joueur {
    private int x, y;
    private Set<Element> cles;
    private Set<Element> artefacts;

    public Joueur(int x, int y) {
        this.x = x;
        this.y = y;
        cles = new HashSet<>();
        artefacts = new HashSet<>();
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public Set<Element> cles() { return cles; }
    public Set<Element> artefacts() { return artefacts; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public void ajouterCle(Element e) { cles.add(e); }
    public boolean possedeCle(Element e) { return cles.contains(e); }

    public boolean recupererArtefact(Zone z) {
        // si z est une zone elementaire
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

    public boolean estValide(Ile ile, int x, int y) {
        return x >= 0 && y >= 0 && x < ile.getRows() && y < ile.getCols() //MODIFERPARADAM
                && ile.getZone(x, y).getEtat() != Zone.Etat.SUBMERGEE;
    }

    public void deplacer(int dx, int dy, Ile ile) {

        if ((dx != 0 && dy != 0) || (dx == 0 && dy == 0)) {
            return; //MODIFIERPARADAM
        }

        int nx = x + dx;
        int ny = y + dy;

        if (estValide(ile, nx, ny)) {
            x = nx;
            y = ny;
        }
    }

    public void assecher(Ile ile, int dx, int dy) {
        int tx = x + dx;
        int ty = y + dy;
        if (tx >= 0 && ty >= 0 && tx < ile.getRows() && ty < ile.getCols()) { //MODIFIERPARADAM
            ile.getZone(tx, ty).assecher();
        }
    }
}
