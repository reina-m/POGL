import javax.swing.*;
import java.awt.*;

class MenuPrincipal extends JPanel {
    public MenuPrincipal(Runnable lancerPartie, Runnable quitter) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(0x0099cc)); // bleu comme l’eau

        JLabel titre = new JLabel("L'Île Interdite");
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setFont(new Font("Monospaced", Font.BOLD, 48));
        titre.setForeground(Color.WHITE);
        add(Box.createVerticalGlue());
        add(titre);
        add(Box.createRigidArea(new Dimension(0, 40)));

        JButton jouer = new JButton("Jouer");
        jouer.setAlignmentX(Component.CENTER_ALIGNMENT);
        jouer.addActionListener(e -> lancerPartie.run());
        add(jouer);

        JButton quitterBtn = new JButton("Quitter");
        quitterBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitterBtn.addActionListener(e -> quitter.run());
        add(quitterBtn);
        add(Box.createVerticalGlue());
    }
}
