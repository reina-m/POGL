import javax.swing.*;

public class Main {

    private static JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::showMenu);
    }

    //Redemarre le jeu
    public static void restart() {
        main(null);
    }

    //Affiche le menu principal
    public static void showMenu() {
        frame = new JFrame("L'Île Interdite");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(839, 676);
        frame.setLocationRelativeTo(null);

        //Actions pour le menu
        Runnable startGame = Main::startGame;
        Runnable quit = () -> System.exit(0);

        // Configure le menu principal
        frame.setContentPane(new MenuPrincipal(startGame, quit));
        frame.setVisible(true);
    }

    //Demarre une nouvelle partie
    public static void startGame() {
        Ile ile = new Ile();
        Vue vue = new Vue(ile);
        Controlleur ctrl = new Controlleur(ile, vue);

        //Definition des controleurs
        vue.setControlleur(ctrl);
        vue.setControlleurIle(ctrl);

        // Configuration de la vue du jeu
        frame.setResizable(false);
        frame.setSize(839, 668);
        frame.setContentPane(vue);
        frame.revalidate();
        frame.repaint();
    }
}
