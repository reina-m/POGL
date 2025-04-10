import java.util.*;

// classe generique pour representer un paquet de cartes
// pas besoin de tester car j'utilise beaucoup de fonctions déjà fournies en java

public class PaquetCartes<T> {
    private LinkedList<T> pioche = new LinkedList<>(); // pioche principale
    private List<T> defausse = new ArrayList<>(); // cartes defaussees

    // constructeur
    public PaquetCartes(Collection<T> cartes) {
        pioche.addAll(cartes);
        melanger();
    }

    // pioche une carte, melange la defausse si vide
    public T piocher() {
        if (pioche.isEmpty()) {
            if (defausse.isEmpty()) return null;
            melangerDefausse();
        }
        return pioche.poll();
    }

    // ajoute une carte a la defausse
    public void defausser(T c) {
        defausse.add(c);
    }

    // melange la pioche actuelle
    public void melanger() {
        Collections.shuffle(pioche);
    }

    // melange la defausse et la remet en haut de la pioche
    // doit être réalisée automatiquement lorsque la dernière carte a été tirée
    public void melangerDefausse() {
        Collections.shuffle(defausse);
        pioche.addAll(defausse);
        defausse.clear();
    }

    // pour tests
    public int taillePioche() { return pioche.size(); }
    public int tailleDefausse() { return defausse.size(); }
}
