import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    // Creation du jeux
    public static void main(String[] args) {
        Ile ile = new Ile();
        Vue vue = new Vue(ile);
        Controlleur ctrl = new Controlleur(ile, vue);
        vue.setControlleur(ctrl);
    }
}

