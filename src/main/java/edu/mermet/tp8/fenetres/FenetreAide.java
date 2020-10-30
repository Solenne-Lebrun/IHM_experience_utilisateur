package edu.mermet.tp8.fenetres;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FenetreAide extends AbstractFenetreInterne{
	JFrame f; 
	
	public FenetreAide(Action monAction, String nom) {
		super(monAction, "Aide");
		f=new JFrame();  
		//***** Création d'une JOptionPane avec un message particulier dépendant du nom donnée en paramètre *****
		switch (nom) {
		case "general":
		    JOptionPane.showMessageDialog(f,"Conversion de degrés Celsius en degrés Farenheit.");  	
			break;
		case "celsius":
		    JOptionPane.showMessageDialog(f,"Valeur en degrés Celsius.");  	
			break;
		case "farenheit":
		    JOptionPane.showMessageDialog(f,"Valeur en degrés Farenheit.");  	
			break;
		default:
			break;
		}
		//***** fin de la JOptionPane *****
	}

}
