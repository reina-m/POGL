import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Pour l'instant, controlleur contient uniquement notre main
 * (dans conway, controlleur implements actionlistener et à une fonciton actionPerformed)
 * (cela est donc à modifier)
 */

public class Controlleur {
    public static void main(String[] args) {
        Ile ile = new Ile(5,6);
        new Vue(ile);
    }
}