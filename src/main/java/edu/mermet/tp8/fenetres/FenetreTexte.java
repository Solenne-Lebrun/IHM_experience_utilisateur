package edu.mermet.tp8.fenetres;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import edu.mermet.tp8.Application;

/**
 *
 * @author brunomermet
 */
public class FenetreTexte extends AbstractFenetreInterne {
    private JCheckBox gras;
    private JCheckBox rouge;
    private Action actionGras;
    private Action actionRouge;
    private JTextArea texte;
    public FenetreTexte(Action action) {
        super(action, "Texte");
        actionGras = new ActionGras();
        gras = new JCheckBox(actionGras);
        actionRouge = new ActionRouge();
        rouge = new JCheckBox(actionRouge);
        JPanel panneauBouton = new JPanel();
        panneauBouton.add(gras);
        panneauBouton.add(rouge);
        add(panneauBouton,BorderLayout.NORTH);
        texte = new JTextArea(6,20);
        texte.setLineWrap(true);
        texte.setWrapStyleWord(true);
        JScrollPane panneauTexte = new JScrollPane(texte);
        add(panneauTexte,BorderLayout.CENTER);
        JMenuBar barre = new JMenuBar();
        JMenu style = new JMenu("Style");
        style.add(new JMenuItem(actionGras));
        style.add(new JCheckBoxMenuItem(actionRouge));
        barre.add(style);
        this.setJMenuBar(barre);
        pack();
    }

    

    private class ActionGras extends AbstractAction {
        private boolean gras;
        public ActionGras() {
            super("gras");
            gras = false;
            putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
            putValue(Action.SELECTED_KEY,false);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            Font police = texte.getFont();
            if (!gras) {
                police = police.deriveFont(Font.BOLD);//|Font.ITALIC);
                //police = police.deriveFont((float)24.0);
            }
            else {
                police = police.deriveFont(Font.PLAIN);
            }
            gras = !gras;
            putValue(Action.SELECTED_KEY,gras);
            texte.setFont(police);
        }
    }

    private class ActionRouge extends AbstractAction {
        private boolean rouge;
        public ActionRouge() {
            super("rouge");
            rouge = false;
            putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
            putValue(Action.SELECTED_KEY,false);

        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (!rouge) {
                texte.setForeground(Color.RED);
            }
            else {
                texte.setForeground(Color.BLACK);
            }
            rouge = !rouge;
            //putValue(Action.SELECTED_KEY,rouge);
        }
    }
}
