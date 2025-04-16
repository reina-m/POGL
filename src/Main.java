import javax.swing.*;

public class Main {

    private static JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::showMenu);
    }

    public static void restart() {
        main(null);
    }

    public static void showMenu() {
        frame = new JFrame("L'Île Interdite");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(839, 676);
        frame.setLocationRelativeTo(null);

        Runnable startGame = Main::startGame;
        Runnable quit = () -> System.exit(0);

        frame.setContentPane(new MenuPrincipal(startGame, quit));
        frame.setVisible(true);
    }

    public static void startGame() {
        Ile ile = new Ile();
        Vue vue = new Vue(ile);
        Controlleur ctrl = new Controlleur(ile, vue);
        vue.setControlleur(ctrl);
        frame.setResizable(false);
        frame.setSize(839, 668);
        frame.setContentPane(vue);
        frame.revalidate();
        frame.repaint();
    }
}
