import javax.swing.*;
import java.awt.*;
import java.util.*;

class Message extends JComponent {
    private Font font = new Font("Monospaced", Font.BOLD, 12); //default

    //methode pour definir la police d'ecriture
    public void setFont(Font f) {
        this.font = f;
    }

    //sous classe d'une ligne de message
    private static class Msg {
        String txt;
        long t0;//tmps ou le message a ete cree

        Msg(String txt) {
            this.txt = txt;
            this.t0 = System.currentTimeMillis();
        }

        //Calcul de la transparence (alpha) du message en fonction de son temps d'existence
        float alpha() {
            long dt = System.currentTimeMillis() - t0;
            if (dt < 1000) return 1f;
            if (dt > 4000) return 0f;
            return 1f - (dt - 1000) / 3000f; // fade out
        }

        //verifie si message a expire
        boolean expired() {
            return System.currentTimeMillis() - t0 > 4000;
        }
    }

    //liste des messages affiches
    private final java.util.List<Msg> msgs = new ArrayList<>();

    //ajoute message a liste
    public void addMessage(String txt) {
        msgs.add(new Msg(txt));
        repaint();
    }

    //dessine message sur ecran
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int y = getHeight() - 20;

        g2.setFont(font);

        Iterator<Msg> it = msgs.iterator();
        while (it.hasNext()) {
            Msg m = it.next();
            if (m.expired()) { it.remove(); continue; }

            float a = m.alpha();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
            g2.setColor(Color.WHITE);
            g2.drawString(m.txt, 16, y);
            y -= 25;
        }

        g2.setComposite(AlphaComposite.SrcOver); //reinitialiser le mode de transparence
    }
}
