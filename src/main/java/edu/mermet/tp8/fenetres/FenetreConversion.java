package edu.mermet.tp8.fenetres;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author brunomermet
 */
public class FenetreConversion extends AbstractFenetreInterne {
    private JTextField champCelsius;
    private JTextField champFarenheit;
    private JButton boutonConvertir;
    private Action actionConvertir;
    private boolean celsiusAFocus;
    
    private FenetreAide aide;
    private Action actionAfficherCelsius;
    private Action actionAfficherFarenheit;
    
    private JLabel image;
    URL url = null;
    
    public FenetreConversion(Action action) {
        super(action,"Conversion celsius/Farenheit");
        this.setSize(new Dimension(100,50));
        this.setLayout(new GridLayout(3,1));
        JPanel ligneCelsius = new JPanel();
        ligneCelsius.setLayout(new FlowLayout(FlowLayout.TRAILING));
        JLabel labCelsius = new JLabel("Celsius :");
        champCelsius = new JTextField(15);
        champCelsius.setToolTipText("Valeur en degrés Celsius");
        labCelsius.setLabelFor(champCelsius);
        ligneCelsius.add(labCelsius);
        ligneCelsius.add(champCelsius);
        
        //***** Création d'un icon et affichage d'une fenetre quand on clique dessus *****
		try {
			url = new URL("http://download.seaicons.com/icons/gakuseisean/radium-neue/16/Help-and-Support-icon.png");
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		image = new JLabel();
		
		image.addMouseListener(new MouseAdapter() {
            @Override 
            public void mouseClicked( MouseEvent event ) {
                    aide = new FenetreAide(actionAfficherCelsius,"celsius");
                }
        } );
		image.setIcon(new ImageIcon(url));
        ligneCelsius.add(image);
        //***** fin de création d'un icon et affichage d'une fenetre quand on clique dessus *****
        
        this.add(ligneCelsius);
        celsiusAFocus = true;
        champCelsius.addFocusListener(new EcouteurFocus(true));
        JPanel ligneFarenheit = new JPanel();
        ligneFarenheit.setLayout(new FlowLayout(FlowLayout.TRAILING));
        JLabel labFarenheit = new JLabel("Farenheit :");
        champFarenheit = new JTextField(15);
        champFarenheit.setToolTipText("Valeur en degrés Farenheit");
        labFarenheit.setLabelFor(champFarenheit);
        ligneFarenheit.add(labFarenheit);
        ligneFarenheit.add(champFarenheit);
        
        //***** Création d'un icon et affichage d'une fenetre quand on clique dessus *****
        image = new JLabel();
		image.addMouseListener(new MouseAdapter() {
            @Override 
            public void mouseClicked( MouseEvent event ) {
                    aide = new FenetreAide(actionAfficherCelsius, "farenheit");
                }
        } );
		image.setIcon(new ImageIcon(url));
        ligneFarenheit.add(image);
        //***** fin de création d'un icon et affichage d'une fenetre quand on clique dessus *****

        this.add(ligneFarenheit);
        champFarenheit.addFocusListener(new EcouteurFocus(false));
        JPanel ligneValider = new JPanel();
        ligneValider.setLayout(new FlowLayout(FlowLayout.CENTER));
        actionConvertir = new ActionConvertir();
        boutonConvertir = new JButton(actionConvertir);
        ligneValider.add(boutonConvertir);
        this.add(ligneValider);
        
        pack();
        getRootPane().setDefaultButton(boutonConvertir);
    }

    private class EcouteurFocus implements FocusListener {
        private boolean aStocker;

        public EcouteurFocus(boolean b) {
            aStocker = b;
        }

        @Override
        public void focusGained(FocusEvent fe) {
            celsiusAFocus = aStocker;
        }

        @Override
        public void focusLost(FocusEvent fe) {
            return;
        }
    }

    private class ActionConvertir extends AbstractAction {

        public ActionConvertir() {
            super("Convertir");
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            double tempCelsius = 0;
            double tempFarenheit = 0;
            if (celsiusAFocus) {
                try {
                    tempCelsius = Double.parseDouble(champCelsius.getText());
                tempFarenheit = 9./5*tempCelsius+32;
                champFarenheit.setText(""+tempFarenheit);
                }
                catch (NumberFormatException nfe) {
                    champFarenheit.setText("Format incorrect");
                }
                }
            else {
                try {
                    tempFarenheit = Double.parseDouble(champFarenheit.getText());
                    tempCelsius = (tempFarenheit - 32) *5./9;
                    champCelsius.setText(""+tempCelsius);
                }
                catch (NumberFormatException nfe) {
                    champCelsius.setText("Format incorrect");
                }
                
            }
        }
    }
    
}
