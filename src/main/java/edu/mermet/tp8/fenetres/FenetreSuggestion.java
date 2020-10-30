package edu.mermet.tp8.fenetres;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.*;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import edu.mermet.tp8.Application;


public class FenetreSuggestion extends AbstractFenetreInterne{
	
	private JTextPane message;
	private JPanel boutonsPanel;
	private JButton boutonFermer;
	private JButton boutonAffichage;
	private ArrayList<String> messagesUtilisateur = new ArrayList<>();
	private Application appli;

	public FenetreSuggestion(Application appli, Action action) {
		super(action, "Suggestion du jour");
		this.setSize(400, 100);
		//Enleve la barre de titre de la fenetre
		BasicInternalFrameUI bi = (BasicInternalFrameUI) this.getUI();
		bi.setNorthPane(null);
		this.appli = appli;
		
		 // ****** Création des boutons ******
		boutonsPanel = new JPanel();
		boutonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		boutonFermer = new JButton(appli.getActionCacherSuggestion());
		boutonAffichage = new JButton(appli.getActionCacherSuggestion());
		boutonAffichage.setText("Ne plus afficher");
		boutonsPanel.add(boutonFermer);
		boutonsPanel.add(boutonAffichage);
		
	
		 // ****** Ajout du message ******
		Random rand = new Random();
		messagesUtilisateur = new ArrayList<String>();
		creerMessagesUtilisateur();
		int size = messagesUtilisateur.size();
		int indexMessage = rand.nextInt(size);

		message = new JTextPane();
		message.setText(messagesUtilisateur.get(indexMessage));
		message.setEditable(false);
		

		// ****** Ajout des boutons et du message a la fenetre ******
		add(message, BorderLayout.CENTER);
		add(boutonsPanel, BorderLayout.SOUTH);
		
	}
	
	/**
	 * Ajoute a l'attribut messageUtilisateur les messages de suggestion
	 * en fonction du niveau de competence de l'utilisateur
	 */
	private void creerMessagesUtilisateur(){
		messagesUtilisateur.add("Vous pouvez quitter l'application en sélectionnant l'option 'Quitter' du menu 'Fichier'");
		messagesUtilisateur.add("Le raccourci clavier pour fermer l'application est ctrl + Q");

		HashMap<String, String> raccourcis = new HashMap<String, String>();
		raccourcis.put("diaporama", "Le raccourci clavier pour ouvrir l'option 'Diaporama' est ctrl + D");
		raccourcis.put("conversionCelsiusFarenheit", "Le raccourci clavier pour ouvrir l'option 'Conversion Celsius/Farenheit' est ctrl + C");
		raccourcis.put("saisieDeTexte", "Le raccourci clavier pour ouvrir l'option 'Saisie de texte' est ctrl + T");
		raccourcis.put("boutons", "Le raccourci clavier pour ouvrir l'option 'Boutons' est ctrl + B");
		
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("diaporama", "L'option 'Diaporama' permet d'afficher un diaporama du Cotentin");
		options.put("conversionCelsiusFarenheit", "L'option 'Conversion Celsius/Farenheit' permet de convertir une température à l'unité de mesure désirée");
		options.put("saisieDeTexte", "L'option 'Saisie de texte' permet de saisir du texte et de le formater en gras et/ou en rouge");
		options.put("boutons", "L'option 'Boutons' permet d'afficher une fenêtre de boutons pour ouvrir les autres options de l'application");
		
		for(int i=0; i<Application.MENUS.length; i++) {
			if(appli.verifNiveauCompetencePourFonctionnalite(i)) {
				messagesUtilisateur.add(options.get(Application.MENUS[i]));
				messagesUtilisateur.add(raccourcis.get(Application.MENUS[i]));
			}
		}
		
	}


	public JButton getBoutonFermer() {
		return boutonFermer;
	}


	public JButton getBoutonAffichage() {
		return boutonAffichage;
	}
	
	
	
	
	

}
