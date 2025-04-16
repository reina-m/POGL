import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Controlleur implements ActionListener {

    //Attributs Modele et vue
    private Ile ile;
    private Vue vue;

    //Attributs Etat du jeu
    private int joueurCourant = 0;
    private int actionsRestantes = 3;
    private int tour;
    private int niveauEau = 2; // pour augmenter le nombre de zones innondées après chaque tour
    private CarteTirage derniereCarte; // stocke la carte piochée du tour
    private List<Point> dernieresInondations = new ArrayList<>();

    public enum ModeSpecial { NORMAL, SABLE, HELICO }
    private ModeSpecial mode = ModeSpecial.NORMAL;

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
    public void attribuerCarte(Joueur j){
        CarteTirage c = ile.piocherCarteJoueur();
        if (c == null) {
            vue.afficherMessage("Le paquet est vide.");
            return;
        }
        derniereCarte = c;
        switch(c.getType()) {
            case CLE -> {
                j.ajouterCle(c.getElement());
                vue.afficherMessage("Le joueur " + (joueurCourant+1) + " a reçu une clé : " + c.getElement());
                ile.defausserCarteJoueur(c);
            }
            case MONTEE_DES_EAUX -> {
                // TODO : remplacer les affichages temporaires dans le terminal et les mettre dans une vue
                vue.afficherMessage("!!! MONTEE DES EAUX !!! ");
                niveauEau = Math.min(niveauEau+1, 5); // maximum 5 selon les règles du jeu
                vue.afficherMessage("Niveau d'eau : " + niveauEau);
                ile.monteeDesEaux(); // mélange la défausse et met au-dessus
                ile.defausserCarteJoueur(c);
            }
            case HELICOPTERE -> {
                j.ajouterHelico();
                vue.afficherMessage("Pouvoir spécial reçu : " + c.getType());
                ile.defausserCarteJoueur(c);
            }
            case SAC_SABLE -> {
                j.ajouterSacDeSable();
                vue.afficherMessage("Pouvoir spécial reçu : " + c.getType());
                ile.defausserCarteJoueur(c);
            }
        }
        ile.notifyObservers();
    }

    //Methode qui fini tour du joueur, inonde zones, pioche carte, affiche résumé
    public void finDeTour() {
        dernieresInondations.clear();
        for (int i = 0; i < niveauEau; i++) {
            Point p = ile.inonderAleatoireEtRetourne();
            if (p != null) {
                dernieresInondations.add(p);
                Zone.Etat etat = ile.getZone(p.x, p.y).getEtat();
                System.out.println("Zone [" + p.x + "," + p.y + "] → " + etat);
            }
        }

        actionsRestantes = 3;
        Joueur j = ile.getJoueurs()[joueurCourant];
        attribuerCarte(j);

        joueurCourant = (joueurCourant + 1) % 4;
        vue.setJoueurActif(joueurCourant);
        vue.updateActionsRestantes(actionsRestantes);
        vue.bloquerActions(false);

        if (aGagne()) {
            vue.afficherMessage("Vous avez gagné !");
            // System.exit(0);
        }
        String raison = raisonDefaite();
        if (raison != null) {
            vue.afficherMessage("Vous avez perdu : " + raison);
            // System.exit(0);
        }
    }

    //Methode ou joueur essaye de recuperer artefact
    public void recupererArtefact() {
        Joueur joueur = ile.getJoueurs()[joueurCourant];
        Zone z = ile.getZone(joueur.getX(), joueur.getY());

        boolean ok = joueur.recupererArtefact(z);
        if (ok) {
            vue.afficherMessage("Artefact récupéré !");
        } else {
            vue.afficherMessage("Pas d’artefact ou pas de clé!");
        }

        effectuerAction(() -> {});
        ile.notifyObservers(); // notifie les vues que l'état a changé
    }

    public void activerSacDeSable() { mode = ModeSpecial.SABLE; }
    public void activerHelico() { mode = ModeSpecial.HELICO; }
    public void clicSurZone(int x, int y) {
        Joueur j = ile.getJoueurs()[joueurCourant];
        switch (mode) {
            case SABLE -> {
                j.utiliserSacDeSable(ile, x, y);
                vue.afficherMessage("Zone assechée avec 🪣 !");
            }
            case HELICO -> {
                j.utiliserHelico(x, y, ile);
                vue.afficherMessage("Déplacement avec 🚁 !");
            }
            default -> { return; } // ignore si mode normal
        }
        mode = ModeSpecial.NORMAL;
        ile.notifyObservers();
        vue.update();
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

    //Fonctions pour les conditions de perte
    private String raisonDefaite() {
        if (heliportSubmerge()) return "l'héliport a été submergé";
        if (artefactIrrecuperable()) return "un artefact est devenu irréupérable";
        if (heliportInaccessible()) return "aucun joueur ne peut atteindre l'héliport";
        return null;
    }

    private boolean heliportSubmerge() {
        Point h = ile.getCoordHeliport();
        return ile.getZone(h.x, h.y).getEtat() == Zone.Etat.SUBMERGEE;
    }

    private boolean artefactIrrecuperable() {
        for (Element e : Element.values()) {
            boolean zoneEncoreAccessible = false;
            boolean artefactDejaObtenu = false;

            for (int i = 0; i < ile.getRows(); i++) {
                for (int j = 0; j < ile.getCols(); j++) {
                    Zone z = ile.getZone(i, j);
                    if (z instanceof ZoneElementaire ze && ze.getElement() == e) {
                        if (z.getEtat() != Zone.Etat.SUBMERGEE) zoneEncoreAccessible = true;
                    }
                }
            }

            for (Joueur j : ile.getJoueurs()) {
                if (j.artefacts().contains(e)) {
                    artefactDejaObtenu = true;
                    break;
                }
            }

            if (!zoneEncoreAccessible && !artefactDejaObtenu) return true;
        }
        return false;
    }

    private boolean heliportInaccessible() {
        Point h = ile.getCoordHeliport();
        boolean[][] vis = new boolean[ile.getRows()][ile.getCols()];
        Queue<Point> q = new LinkedList<>();

        for (Joueur j : ile.getJoueurs()) {
            int x = j.getX(), y = j.getY();
            q.add(new Point(x, y));
            vis[x][y] = true;
        }

        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        while (!q.isEmpty()) {
            Point p = q.poll();
            if (p.equals(h)) return false;
            for (int d = 0; d < 4; d++) {
                int nx = p.x + dx[d], ny = p.y + dy[d];
                if (nx >= 0 && ny >= 0 && nx < ile.getRows() && ny < ile.getCols()) {
                    if (!vis[nx][ny] && ile.getZone(nx, ny).getEtat() != Zone.Etat.SUBMERGEE) {
                        vis[nx][ny] = true;
                        q.add(new Point(nx, ny));
                    }
                }
            }
        }
        return true;
    }

}