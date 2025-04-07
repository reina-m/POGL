import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controlleur implements ActionListener {

    //Attributs Modele et vue
    private Ile ile;
    private Vue vue;

    //Attributs Etat du jeu
    private int joueurCourant = 0;
    private int actionsRestantes = 3;

    //constructeur
    public Controlleur(Ile ile, Vue vue) {
        this.ile = ile;
        this.vue = vue;
    }

    public void actionPerformed(ActionEvent e) {
        //A completer plus tard quand on branchera les boutons ici
    }

    //Methode pour effectuer action(NB 3 par tour max par joueur)
    public void effectuerAction(Runnable action) {
        if (actionsRestantes > 0) {
            action.run();
            actionsRestantes--;
            vue.update();
            vue.updateActionsRestantes(actionsRestantes);
            if (actionsRestantes == 0) {
                vue.bloquerActions(true);//Desactiver les boutons sauf Fin de Tour
            }
        }
    }

    //Attribue aleatoirement une cle a chaque joueur, utilise dans finDeTour
    public void attribuerCleAleatoire(Joueur J){
        Random rand = new Random();
        if(rand.nextDouble() < 0.75) {
            Element[] elements = Element.values();
            Element cle = elements[rand.nextInt(elements.length)];
            J.ajouterCle(cle);
            System.out.println("Le joueur " + (joueurCourant + 1) + " a reçu une clé : " + cle);
        } else {
            System.out.println("Le joueur " + (joueurCourant + 1) + "n'a rien reçu.");
        }
    }

    //Methode qui fini tour du joueur, indonde des nouvelles zones, donne cle
    //change au prochain joueur, update le UI et verifie une victoire
    public void finDeTour() {

        ile.inonderAleatoire();
        actionsRestantes = 3; //Trois actions par joueur par tour max

        Joueur j = ile.getJoueurs()[joueurCourant];
        attribuerCleAleatoire(j);//Donner une cle aleatoire ou rien au joueur

        joueurCourant = (joueurCourant + 1) % 4;  //On change le joueur courant

        vue.setJoueurActif(joueurCourant);
        vue.update();
        vue.updateActionsRestantes(actionsRestantes);
        vue.updateInfosJoueurs();
        vue.bloquerActions(false);
        if (aGagne()) {
            JOptionPane.showMessageDialog(null, "Vous avez gagné !");
            System.exit(0);
        }
    }

    //Methode ou joueur essaye de recuperer artefact
    public void recupererArtefact() {
        Joueur joueur = ile.getJoueurs()[joueurCourant];
        Zone z = ile.getZone(joueur.getX(), joueur.getY());

        boolean ok = joueur.recupererArtefact(z);
        if (ok) {
            System.out.println("Artefact récupérée!");
        } else {
            System.out.println("Pas d’artefact ou pas de clé!");
        }

        effectuerAction(() -> {});
        vue.update();
        vue.updateInfosJoueurs();
    }

    //Verifie si un joueur a recuperer les 4 artefacts
    private boolean tousArtefactsRecuperes() {
        return EnumSet.allOf(Element.class).stream()
                .allMatch(e -> Arrays.stream(ile.getJoueurs())
                        .anyMatch(j -> j.artefacts().contains(e)));
    }

    //Verifie si tout les joeurs sont sur le heliport
    private boolean tousSurHeliport() {
        Point h = ile.getCoordHeliport();
        return Arrays.stream(ile.getJoueurs())
                .allMatch(j -> j.getX() == h.x && j.getY() == h.y);
    }

    //Renvoie true si la condition de victoire est remplie
    private boolean aGagne() {
        return tousArtefactsRecuperes() && tousSurHeliport();
    }

}