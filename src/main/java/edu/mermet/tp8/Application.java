package edu.mermet.tp8;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import edu.mermet.tp8.fenetres.FenetreAide;
import edu.mermet.tp8.fenetres.FenetreBoutons;
import edu.mermet.tp8.fenetres.FenetreCommentFaire;
import edu.mermet.tp8.fenetres.FenetreConversion;
import edu.mermet.tp8.fenetres.FenetreDiaporama;
import edu.mermet.tp8.fenetres.FenetreSuggestion;
import edu.mermet.tp8.fenetres.FenetreTexte;

/**
 *
 * @author brunomermet
 */
public class Application extends JFrame {

    private JInternalFrame conversion;
    private JInternalFrame texte;
    private JInternalFrame diaporama;
    private JInternalFrame boutons;
    private JInternalFrame suggestion;
    private FenetreAide aide;
    private Action actionAfficherConversion;
    private Action actionAfficherTexte;
    private Action actionAfficherDiaporama;
    private Action actionAfficherBoutons;

    private Action actionCacherSuggestion;
    private Action actionAfficherAide;

    JMenu menuApplication;

    final private Map<String, Integer> complexiteFonctionnalites;
    final private double coefAugmentationNiveau;
    private Properties properties;
    private Path utilisateurPath;
    private File userFile;
    
    /**
     * L'utilisateur ne gagne plus en competence lorsque son
     * niveau depasse le produit de ce coefficient et de
     * la complexite de la fonctionnalite utilisee
     */
    private static final double COEFF_LIMITE = 4 ; 

    // liste des menus
    public static final String[] MENUS = { "conversionCelsiusFarenheit", "saisieDeTexte", "diaporama", "boutons" };

    public Application(String utilisateur) {
        super("multi-fenêtres");
        gestionUtilisateur(utilisateur);
        complexiteFonctionnalites = new HashMap<>();
        complexiteFonctionnalites.put("quitter", 1);
        complexiteFonctionnalites.put("diaporama", 2);
        complexiteFonctionnalites.put("conversionCelsiusFarenheit", 4);
        complexiteFonctionnalites.put("saisieDeTexte", 4);
        complexiteFonctionnalites.put("boutons", 8);

        JDesktopPane desktopPane = new JDesktopPane();
        this.setContentPane(desktopPane);
        setSize(600, 300);

        coefAugmentationNiveau = 0.1;

        // ****** Barre de menu ******
        JMenuBar barre = new JMenuBar();
        // ------ menu Fichier ------
        JMenu menuFichier = new JMenu("Fichier");
        menuFichier.setMnemonic(KeyEvent.VK_F);
        JMenuItem quitter = new JMenuItem("Quitter");
        quitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent aev) {
                try {
                    augmenterNiveauUtilisateur("quitter");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
        quitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        menuFichier.add(quitter);
        barre.add(menuFichier);
        this.setJMenuBar(barre);
        // ------ menu Applications ------
        menuApplication = new JMenu("Applications");
        menuApplication.setMnemonic(KeyEvent.VK_A);
        actionAfficherConversion = new ActionAfficherConversion();
        JMenuItem itemConversion = new JMenuItem(actionAfficherConversion);
        itemConversion.setVisible(false);
        menuApplication.add(itemConversion);
        actionAfficherTexte = new ActionAfficherTexte();
        JMenuItem itemTexte = new JMenuItem(actionAfficherTexte);
        menuApplication.add(itemTexte);
        actionAfficherDiaporama = new ActionAfficherDiaporama();
        JMenuItem itemDiaporama = new JMenuItem(actionAfficherDiaporama);
        menuApplication.add(itemDiaporama);
        actionAfficherBoutons = new ActionAfficherBoutons();
        JMenuItem itemBoutons = new JMenuItem(actionAfficherBoutons);
        menuApplication.add(itemBoutons);
        barre.add(menuApplication);

        // affiche les items du menu Application en fonction de l'utiliateur
        for (int i = 0; i < MENUS.length; i++) {
            switch (properties.getProperty(MENUS[i])) {
            case "auto":
                menuApplication.getItem(i).setVisible(verifNiveauCompetencePourFonctionnalite(i));
                break;
            case "affiche":
                menuApplication.getItem(i).setVisible(true);
                break;
            case "cache":
                menuApplication.getItem(i).setVisible(false);
                break;

            default:
                break;
            }
        }

        // ------ menu Aide ------
        JMenu menuAide = new JMenu("Aide");
        menuAide.setMnemonic(KeyEvent.VK_L);
        actionAfficherAide = new ActionAfficherAide();
        JMenuItem commentFaire = new JMenuItem(actionAfficherAide);
        commentFaire.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				FenetreCommentFaire fenetreCommentFaire = new FenetreCommentFaire();
				fenetreCommentFaire.setVisible(true);
			}
		});
        menuAide.add(commentFaire);
        JMenuItem configurationDesMenus = new JMenuItem("Configuration des menus");
        configurationDesMenus.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                FenetreConfigurationMenus confMenu = new FenetreConfigurationMenus();
                sauvegarderProprietes();
            }
        });

        menuAide.add(configurationDesMenus);

        barre.add(menuAide);
        // ****** Fin barre de menu ******

        // ****** Création des fenêtres ******
        // ------ fenêtre conversion ------
        conversion = new FenetreConversion(actionAfficherConversion);
        this.add(conversion);
        // ------ fenêtre texte ------
        texte = new FenetreTexte(actionAfficherTexte);
        this.add(texte);
        // ------ fenêtre diaporama ------
        diaporama = new FenetreDiaporama(actionAfficherDiaporama);
        this.add(diaporama);
        // ------ fenêtre boutons ------
        boutons = new FenetreBoutons(this, actionAfficherBoutons);
        this.add(boutons);
        // ------ fenêtre suggestion ------
        actionCacherSuggestion = new ActionCacherSuggestion();
        suggestion = new FenetreSuggestion(this, actionCacherSuggestion);

        // centrer horizontalementla fenetre de suggestion
        Dimension tailleJFrame = this.getSize();
        Dimension tailleSuggestion = suggestion.getSize();
        suggestion.setLocation((tailleJFrame.width - tailleSuggestion.width) / 2, 10);

        this.add(suggestion);

        if (properties.getProperty("suggestion").equals("auto")) {
            suggestion.setVisible(true);
        }

        // ****** Fin création fenêtres ******

        // ****** Création d'un menu contextuel ******
        JPopupMenu popupMenu = new JPopupMenu("Aide");
        actionAfficherAide = new ActionAfficherAide();
        JMenuItem aideItem = new JMenuItem(actionAfficherAide);
        popupMenu.add(aideItem);
        conversion.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if (event.isPopupTrigger()) {
                    System.out.println("Show popup");
                    popupMenu.show(event.getComponent(), event.getX(), event.getY());
                }
            }
        });
        this.add(popupMenu);
        // ------Fin de création de menu ------

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Gestion des utilisateurs de l'application dans un dossier .ihm dans le
     * dossier utilisateur du systeme. Chaque utilisateur possede son propre
     * utilisateur.xml contenant ses properties.
     */
    private void gestionUtilisateur(String utilisateur) {
        String userHomeDir = System.getProperty("user.home");

        Path path = Paths.get(userHomeDir + File.separator + ".ihm");

        // si .ihm n'existe pas on le cree
        if (Files.notExists(path)) {

            File folder = new File(path.toString());
            boolean created = folder.mkdir();

            if (created) {
                System.out.println("Création du dossier .ihm dans " + folder.getPath());
            }
        }

        // acces au .xml de l'utilisateur
        utilisateurPath = Paths.get(userHomeDir + File.separator + ".ihm" + File.separator + utilisateur + ".xml");
        properties = new Properties();

        userFile = new File(utilisateurPath.toString());

        if (Files.notExists(utilisateurPath)) {

            try {
                boolean created = userFile.createNewFile();

                if (created) {
                    System.out.println("Création du fichier " + utilisateur + ".xml dans " + userFile.getPath());
                }

                FileOutputStream fos = new FileOutputStream(userFile);
                // le niveau initial de l'utilisateur est a 1
                properties.setProperty("niveauUtilisateur", "1");

                // la configuration des menus est auto par defaut
                for (String menu : MENUS) {
                    properties.setProperty(menu, "auto");
                }

                properties.setProperty("suggestion", "auto");

                properties.storeToXML(fos, null);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            FileInputStream fis = new FileInputStream(userFile);
            properties.loadFromXML(fis);
            fis.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        properties.list(System.out);

    }

    /**
     * Actualise l'affichage du Menu
     */
    void actualiserAffichage() {
        for (int i = 0; i < MENUS.length; i++) {
            switch (properties.getProperty(MENUS[i])) {
            case "auto":
                menuApplication.getItem(i).setVisible(verifNiveauCompetencePourFonctionnalite(i));
                break;
            case "affiche":
                menuApplication.getItem(i).setVisible(true);
                break;
            case "cache":
                menuApplication.getItem(i).setVisible(false);
                break;

            default:
                break;
            }
        }
    }

    /**
     * Retourne si un menu devrait apparaître si le niveau de competence de
     * l'utilisateur est assez eleve, c'est-a-dire s'il atteint au moins le niveau
     * de complexite de la fonctionnalité.
     * 
     * @param i l'index dans le tableau MENUS
     * @return vrai si le menu doit etre affiche, faux sinon
     */
    public boolean verifNiveauCompetencePourFonctionnalite(int i) {
        if (complexiteFonctionnalites.get(MENUS[i]) <= getNiveauUtilisateur()) {
            return true;
        }
        return false;
    }

    /**
     * Retourne le niveau de competence de l'utilisateur.
     * 
     * @return le niveau de competence de l'utilisateur.
     */
    private double getNiveauUtilisateur() {
        double retour = Double.parseDouble((String) properties.get("niveauUtilisateur"));
        return retour;
    }

    /**
     * Sauvegarde des proprietes dans le user.xml
     */
    private void sauvegarderProprietes() {
        System.out.println("Propriétés sauvegardées");
        try {
            FileOutputStream fos = new FileOutputStream(userFile);
            properties.storeToXML(fos, null);
            properties.list(System.out);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    /**
     * Augmente le niveau de l'utilisateur en fonction des fonctionnalites utilisees
     * et de son niveau actuel.
     */
    private void augmenterNiveauUtilisateur(String fonctionnalite) throws Exception {
        if (complexiteFonctionnalites.containsKey(fonctionnalite)) {

            double limiteAugmentation = COEFF_LIMITE * complexiteFonctionnalites.get(fonctionnalite);
       
            // deuxieme niveau de fonctionnalite au-dessus
            if (getNiveauUtilisateur() < limiteAugmentation) {
                properties
                        .setProperty("niveauUtilisateur",
                                (getNiveauUtilisateur()
                                        + (complexiteFonctionnalites.get(fonctionnalite) * coefAugmentationNiveau))
                                        + "");
                actualiserAffichage();
            } else {

                System.out.println(
                        "Niveau de l'utilisateur trop élevé par rapport à la complexité de la fonctionnalité pour être augmenté : niveau utilisateur : "
                                + getNiveauUtilisateur() + " Complexité fonctionnalité :  "
                                + complexiteFonctionnalites.get(fonctionnalite));
            }
        } else {
            throw new Exception("Aucune complexité n'est assignée à cette fonctionnalité.");
        }
        sauvegarderProprietes();
    }

    private class ActionAfficherBoutons extends AbstractAction {
        public ActionAfficherBoutons() {
            super("Boutons");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_B);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            boutons.setVisible(true);
            enableBoutons(false);
            try {
                augmenterNiveauUtilisateur("boutons");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class ActionAfficherDiaporama extends AbstractAction {
        public ActionAfficherDiaporama() {
            super("Diaporama");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            diaporama.setVisible(true);
            enableDiaporama(false);
            try {
                augmenterNiveauUtilisateur("diaporama");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class ActionAfficherTexte extends AbstractAction {
        public ActionAfficherTexte() {
            super("Saisie de texte");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            texte.setVisible(true);
            enableTexte(false);
            try {
                augmenterNiveauUtilisateur("saisieDeTexte");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public class ActionAfficherConversion extends AbstractAction {
        public ActionAfficherConversion() {
            super("Conversion Celsius/Farenheit");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            conversion.setVisible(true);
            enableConversion(false);
            try {
                augmenterNiveauUtilisateur("conversionCelsiusFarenheit");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public class ActionCacherSuggestion extends AbstractAction {
        public ActionCacherSuggestion() {
            super("Fermer");

        }

        @Override
        public void actionPerformed(ActionEvent ae) {

            // Si l'utilisateur selectionne "Ne plus afficher" la propriete suggestion est
            // set a "cache"
            if (ae.getSource() == ((FenetreSuggestion) suggestion).getBoutonAffichage()) {

                properties.setProperty("suggestion", "cache");
                sauvegarderProprietes();
            }
            suggestion.dispose();
        }
    }

    // Affichage d'une fenetre d'aide
    private class ActionAfficherAide extends AbstractAction {
        public ActionAfficherAide() {
            super("Aide");
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (conversion.isSelected()) {
                aide = new FenetreAide(actionAfficherAide, "general");
                enableAide(true);
            }
        }
    }

    public void enableConversion(boolean b) {
        actionAfficherConversion.setEnabled(b);
    }

    public void enableTexte(boolean b) {
        actionAfficherTexte.setEnabled(b);
    }

    public void enableDiaporama(boolean b) {
        actionAfficherDiaporama.setEnabled(b);
    }

    public void enableBoutons(boolean b) {
        actionAfficherBoutons.setEnabled(b);
    }

    public void enableAide(boolean b) {
        actionAfficherAide.setEnabled(b);
    }

    public Action getActionAfficherConversion() {
        return actionAfficherConversion;
    }

    public Action getActionAfficherTexte() {
        return actionAfficherTexte;
    }

    public Action getActionAfficherDiaporama() {
        return actionAfficherDiaporama;
    }

    public Action getActionCacherSuggestion() {
        return actionCacherSuggestion;
    }

    public class FenetreConfigurationMenus extends JDialog {

        private JPanel boutonsRadio;

        public FenetreConfigurationMenus() {
            super();
            setTitle("Configuration des menus");
            setModal(true);
            setLocationRelativeTo(null);
            boutonsRadio = new JPanel();
            boutonsRadio.setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();

            gc.gridy = 0;
            creerBoutonsRadios("Conversion Celsius/Farenheit", gc);
            gc.gridy = 1;
            creerBoutonsRadios("Saisie de texte", gc);
            gc.gridy = 2;
            creerBoutonsRadios("Diaporama", gc);
            gc.gridy = 3;
            creerBoutonsRadios("Boutons", gc);

            add(boutonsRadio, BorderLayout.CENTER);

            JButton valider = new JButton("Valider");
            valider.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {

                    actualiserAffichage();
                    setVisible(false);
                }
            });

            JButton annuler = new JButton("Annuler");
            annuler.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    setVisible(false);
                }
            });

            JPanel action = new JPanel();
            action.add(valider);
            action.add(annuler);

            add(action, BorderLayout.SOUTH);

            pack();

            setVisible(true);

        }

        public void creerBoutonsRadios(String intitule, GridBagConstraints gc) {
            JLabel texte1 = new JLabel(intitule);
            JRadioButton auto = new JRadioButton("Auto");
            auto.addActionListener(new ActionChangerConfigurationMenu(gc.gridy));
            JRadioButton affiche = new JRadioButton("Affiché");
            affiche.addActionListener(new ActionChangerConfigurationMenu(gc.gridy));
            JRadioButton cache = new JRadioButton("Caché");
            cache.addActionListener(new ActionChangerConfigurationMenu(gc.gridy));

            ButtonGroup groupe = new ButtonGroup();
            groupe.add(auto);
            groupe.add(affiche);
            groupe.add(cache);

            switch (properties.getProperty(MENUS[gc.gridy])) {
            case "auto":
                auto.setSelected(true);
                break;
            case "affiche":
                affiche.setSelected(true);
                break;
            case "cache":
                cache.setSelected(true);
                break;

            default:
                break;
            }

            gc.gridx = 0;
            boutonsRadio.add(texte1, gc);
            gc.gridx = 1;
            boutonsRadio.add(auto, gc);
            gc.gridx = 2;
            boutonsRadio.add(affiche, gc);
            gc.gridx = 3;
            boutonsRadio.add(cache, gc);

        }

        private class ActionChangerConfigurationMenu extends AbstractAction {

            private int numeroMenu;

            public ActionChangerConfigurationMenu(int numeroMenu) {
                super("Saisie de texte");
                this.numeroMenu = numeroMenu;
            }

            @Override
            public void actionPerformed(ActionEvent ae) {

                switch (((JRadioButton) ae.getSource()).getText()) {
                case "Auto":
                    properties.setProperty(MENUS[numeroMenu], "auto");
                    break;
                case "Affiché":
                    properties.setProperty(MENUS[numeroMenu], "affiche");
                    break;
                case "Caché":
                    properties.setProperty(MENUS[numeroMenu], "cache");
                    break;

                default:
                    break;
                }
            }

        }

    }

    public static void main(String[] args) {
        if (args.length == 1) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    new Application(args[0]);
                }
            });

        } else {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    new Application("login");
                }
            });
        }
    }
}