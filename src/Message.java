import javax.swing.*;
import java.awt.*;
import java.util.*;

class Message extends JComponent {
    private Font font = new Font("Monospaced", Font.BOLD, 12); // default
    public void setFont(Font f) {
        this.font = f;
    }
    // sous classe d'une ligne de message
    private static class Msg {
        String txt;
        long t0;

        Msg(String txt) {
            this.txt = txt;
            this.t0 = System.currentTimeMillis();
        }

        float alpha() {
            long dt = System.currentTimeMillis() - t0;
            if (dt < 1000) return 1f;
            if (dt > 4000) return 0f;
            return 1f - (dt - 1000) / 3000f; // fade out
        }

        boolean expired() {
            return System.currentTimeMillis() - t0 > 4000;
        }
    }

    private final java.util.List<Msg> msgs = new ArrayList<>();

    public void addMessage(String txt) {
        msgs.add(new Msg(txt));
        repaint();
    }

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

        g2.setComposite(AlphaComposite.SrcOver);
    }
}
