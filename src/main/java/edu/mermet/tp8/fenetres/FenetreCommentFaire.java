package edu.mermet.tp8.fenetres;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FenetreCommentFaire extends JDialog{

	ResourceBundle titres, textes;
	JList<String> listeGuides;
	JEditorPane guide;
	JPanel affichage;

	public FenetreCommentFaire() {
		setTitle("Comment Faire ?");
		setSize(600, 300);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		affichage = new JPanel();
		GridLayout grid = new GridLayout(0, 2, 0, 0);
		affichage.setLayout(grid);

		titres = ResourceBundle.getBundle("titres");
		textes = ResourceBundle.getBundle("textes");

		guide = new JEditorPane();
		guide.setContentType("text/html");

		String nomsGuides[] = { titres.getString("commentFaire"), titres.getString("conversionCelsiusVersFarenheit"),
				titres.getString("conversionFarenheitVersCelsius"), titres.getString("texteEnGras"),
				titres.getString("texteEnRouge"), titres.getString("quitter") };

		listeGuides = new JList<>(nomsGuides);

		listeGuides.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent lse) {

				String s = "";
				if (listeGuides.getSelectedValue().equals(titres.getString("quitter"))) {
					dispose();
				} else if (listeGuides.getSelectedValue().equals(titres.getString("commentFaire"))) {
					s = textes.getString("commentFaire");
				} else if (listeGuides.getSelectedValue().equals(titres.getString("conversionCelsiusVersFarenheit"))) {
					s = textes.getString("conversionCelsiusVersFarenheit");
				} else if (listeGuides.getSelectedValue().equals(titres.getString("conversionFarenheitVersCelsius"))) {
					s = textes.getString("conversionFarenheitVersCelsius");
				} else if (listeGuides.getSelectedValue().equals(titres.getString("texteEnGras"))) {
					s = textes.getString("texteEnGras");
				} else if (listeGuides.getSelectedValue().equals(titres.getString("texteEnRouge"))) {
					s = textes.getString("texteEnRouge");
				}

				guide.setText("<html><body><p>" + s + "</p></body></html>");

			}
		});
		

		listeGuides.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
				BorderFactory.createMatteBorder(0, 0, 0, 5, Color.GRAY)));
		
		guide.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
		
		
		affichage.add(listeGuides);
		affichage.add(guide);

		getContentPane().add(affichage);

	}
}
