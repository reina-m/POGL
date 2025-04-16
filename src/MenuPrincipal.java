import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class MenuPrincipal extends JPanel {
    public MenuPrincipal(Runnable lancerPartie, Runnable quitter) {
        setBackground(new Color(0x0099cc)); // bleu comme l’eau
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(60, 60, 60, 60)); // padding autour du contenu

        Font pixelFont = loadPixelFont(42f);
        Font pixelSmall = loadPixelFont(16f);

        TitleLabel titre = new TitleLabel("L'Île Interdite", pixelFont);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);

        Img gif = new Img("img/j1.gif");
        gif.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton jouer = new JButton("Jouer");
        JButton quitterBtn = new JButton("Quitter");

        jouer.setFont(pixelSmall);
        quitterBtn.setFont(pixelSmall);

        jouer.setPreferredSize(new Dimension(160, 40));
        quitterBtn.setPreferredSize(new Dimension(160, 40));
        jouer.setMaximumSize(new Dimension(160, 40));
        quitterBtn.setMaximumSize(new Dimension(160, 40));

        jouer.setMargin(new Insets(10, 10, 10, 10));
        quitterBtn.setMargin(new Insets(10, 10, 10, 10));

        jouer.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitterBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        jouer.addActionListener(e -> lancerPartie.run());
        quitterBtn.addActionListener(e -> quitter.run());

        add(Box.createVerticalGlue());
        add(titre);
        add(Box.createRigidArea(new Dimension(0, 40)));
        add(gif);
        add(Box.createRigidArea(new Dimension(0, 0)));
        add(jouer);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(quitterBtn);
        add(Box.createVerticalGlue());
    }

    private Font loadPixelFont(float size) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/PT.ttf")).deriveFont(size);
        } catch (Exception e) {
            return new Font("Monospaced", Font.PLAIN, (int) size);
        }
    }
}

class Img extends JComponent {
    private final Image image;

    public Img(String path) {
        image = new ImageIcon(getClass().getResource("/" + path)).getImage();
        int w = image.getWidth(null) * 5;
        int h = image.getHeight(null) * 5;
        setPreferredSize(new Dimension(w, h));
        setMaximumSize(new Dimension(w, h));
        setMinimumSize(new Dimension(w, h));
        setSize(new Dimension(w, h));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(image, 25, 0, image.getWidth(null) * 4, image.getHeight(null) * 4, this);
    }
}

class TitleLabel extends JComponent {
    private final String text;
    private final Font font;

    public TitleLabel(String text, Font font) {
        this.text = text;
        this.font = font;
        setPreferredSize(new Dimension(800, 100));
        setMaximumSize(new Dimension(800, 100));
        setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(font);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int x = 50;
        int y = 70;

        g2.setColor(Color.BLACK);
        g2.drawString(text, x + 5, y + 5); // shadow
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y); // main
    }
}
