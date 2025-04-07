import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

//Constructeur
//Initialise fenetre principale du jeu
//Cree les vues secondaires
//Assemble les elements de linterface
//Applique la police personnalise a toute linterface
class Vue extends JFrame {
    private VueIle vueIle;
    private VueJoueurs vueJoueurs;
    private VueCommande vueCmd;
    private Ile ile;

    //Constructeur
    public Vue(Ile ile) {

        this.ile = ile;
        setTitle("L'Île Interdite");
        setSize(839, 676);

        //Ajuste automatiquement la taille de la fenêtre au contenu
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); //Centre la fenetre

        applyGlobalFont(pixelFont(10f));

        //Creation des differentes vues
        vueJoueurs = new VueJoueurs(this, ile);
        vueIle = new VueIle(ile, vueJoueurs);
        vueCmd = new VueCommande(ile, vueIle, vueJoueurs);

        //Creer le panel a droite
        JPanel pan = new JPanel(new BorderLayout());
        pan.setPreferredSize(new Dimension(190, 300));
        pan.add(vueJoueurs, BorderLayout.NORTH);
        pan.add(vueCmd, BorderLayout.SOUTH);

        add(vueIle, BorderLayout.CENTER); //L'ile au centre de la vue princpiale
        add(pan, BorderLayout.EAST); //La panel avec les commandes/joueurs à droite
        setVisible(true);

        //Enelever si pas besoin apres
        //Font pixelFont = pixelFont(10f);

    }

    //Applique la police donnée à tous les elements de l'interface utilisateur
    private void applyGlobalFont(Font font) {
        UIManager.put("Label.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("ToolTip.font", font);
    }

    //Charge une police personnalisee a partir du fichier Fonts
    //Si sa echoue une police monospace standard est utilise
    protected Font pixelFont(float t) {
        try {
            Font f = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/PT.ttf"));
            return f.deriveFont(t);
        } catch (Exception e) { //Au cas ou
            return new Font("Monospaced", Font.PLAIN, (int) t); //Fallback
        }
    }

    //Met a jour la vue de l'ile
    public void update() {
        vueIle.update();
    }

    //Active ou desactive les bouttons de commande dans VueCommande
    public void bloquerActions(boolean b) {
        vueCmd.setActionsEnabled(!b);
    }

    //Associe un controleur a la vue des commandes
    public void setControlleur(Controlleur ctrl) {
        vueCmd.setControlleur(ctrl);
    }

    //Implique dans la vue des joueurs , quel joueur est actif
    public void setJoueurActif(int j) {
        vueJoueurs.setJoueurActif(j);
    }

    //Met a jour l'affichage du nombre d'actions restantes
    public void updateActionsRestantes(int actions) {
        vueCmd.updateActionsRestantes(actions);
    }

    //Refraichit l'affichage des informations des joueurs
    public void updateInfosJoueurs() {
        vueJoueurs.updateInfos();
    }

}