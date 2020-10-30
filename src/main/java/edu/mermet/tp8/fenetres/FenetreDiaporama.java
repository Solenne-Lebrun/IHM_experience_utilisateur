package edu.mermet.tp8.fenetres;

import java.awt.BorderLayout;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author brunomermet
 */
public class FenetreDiaporama extends AbstractFenetreInterne {
    ImageIcon [] images;
    String[] textes;
    JLabel affichage;
    Defilement defilement;
    private int indiceCourant = 0;
    public FenetreDiaporama(Action action) {
        super(action,"Diaporama");
        images = new ImageIcon[3];
        try {
            images[0] = new ImageIcon(new ImageIcon(new URL("http://bruno.mermet.pagesperso-orange.fr/Personnel/Anes/Randos/TourDeLaHague/10bocage.jpg")).getImage().getScaledInstance(300, -1, Image.SCALE_DEFAULT));
            images[1] = new ImageIcon(new ImageIcon(new URL("http://bruno.mermet.pagesperso-orange.fr/Personnel/Anes/Randos/TourDeLaHague/12baieEcalgrain.jpg")).getImage().getScaledInstance(300, -1, Image.SCALE_DEFAULT));
            images[2] = new ImageIcon(new ImageIcon(new URL("http://bruno.mermet.pagesperso-orange.fr/Personnel/Anes/Randos/TourDeLaHague/15cote.jpg")).getImage().getScaledInstance(300, -1, Image.SCALE_DEFAULT));
        } catch (MalformedURLException ex) {
            images[0] = null;
            images[1] = null;
            images[2] = null;
        }
        
        JPanel panneauTexte = new JPanel();
        /*textes = new String[3];
        textes[0] = "bonjour";
        textes[1] = "le";
        textes[2] = "monde";*/
        affichage = new JLabel();
        panneauTexte.add(affichage);
        affichage.setIcon(images[0]);
 //       affichage.setText(textes[0]);
        JScrollPane ascenseurs = new JScrollPane(affichage);
        add(ascenseurs,BorderLayout.CENTER);
        //add(panneauTexte,BorderLayout.CENTER);
        setSize(300,300);
        //pack();
        
    }
    class Defilement implements Runnable {
        private boolean arrete;
        public Defilement() {
            arrete = false;
        }
        @Override
        public void run() {
            while (!arrete) {
                try {
                    Thread.sleep(2000);
                }
                catch (InterruptedException iex) {
                }
                indiceCourant++;
                indiceCourant = indiceCourant % 3;
                affichage.setIcon(images[indiceCourant]);
            }
        }
        public void arreter() {
            arrete = true;
        }
    }
    
    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            defilement = new Defilement();
            Thread thread = new Thread(defilement);
            thread.start();
        }
        else {
            if (defilement != null) {
                defilement.arreter();
            }
        }
    }
}
