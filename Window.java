package fr.et3.polytech.JavaIHM;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jme3.scene.CameraNode;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

public class Window{

	private static PlayGround canvasApplication;

	private static JFileChooser filechooser;

	private static Canvas canvas; // JAVA Swing Canvas
	private static JFrame frame;
	private static JPanel panel;

	private static CatchDatas cd;
	private static JLabel statLabel;
	private static JSlider time;

	public static void createNewJFrame() {

		frame = new JFrame("Java - Graphique - IHM");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(500, 500);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				canvasApplication.stop();
			}
		});

		panel = new JPanel(new BorderLayout());

		// Creation du menu
		final JMenuBar menubar = new JMenuBar();
		final JMenu objectsMenu = new JMenu("File");
		final JMenu helpMenu = new JMenu("Help");

		final JMenuItem exitObjectItem = new JMenuItem("Exit");
		final JMenuItem getControlsItem = new JMenuItem("Get controls");
		final JMenuItem BrowseFileItem = new JMenuItem("Browse...");

		objectsMenu.add(BrowseFileItem);
		objectsMenu.add(exitObjectItem);
		helpMenu.add(getControlsItem);
		menubar.add(objectsMenu);
		menubar.add(helpMenu);
		frame.setJMenuBar(menubar);

		getControlsItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFrame dial = new JFrame("Controls");
				final JPanel pane = new JPanel();
				pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

				JTextArea cautionText = new JTextArea(
						"Pour charcher un nouveau fichier, cliquez sur Browse dans l'onglet File \n"
								+ "Vous pouvez modifier la vitesse de jeu à l'aide du slider associé, vous \n"
								+ "pouvez également mettre en pause/play en cliquant sur le bouton en bas de la \n"
								+ "fenêtre. La sélection des joueurs à afficher se fait avec la liste appelée \n"
								+ "Selection. Vous pouvez sélectionner l'ensemble des joueurs ou aucun directement \n"
								+ '\n');
				cautionText.setBorder(BorderFactory.createEmptyBorder(10, 10,
						0, 10));
				cautionText.setEditable(false);
				pane.add(cautionText);

				JButton okButton = new JButton("Ok");
				okButton.setSize(30, 30);
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dial.dispose();
					}
				});

				JPanel buttonPane = new JPanel();
				buttonPane.setLayout(new BoxLayout(buttonPane,
						BoxLayout.LINE_AXIS));
				buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0,
						10));
				buttonPane.add(Box.createHorizontalGlue());
				buttonPane.add(okButton);

				pane.add(buttonPane);
				pane.add(Box.createRigidArea(new Dimension(0, 5)));
				dial.add(pane);
				dial.pack();
				dial.setLocationRelativeTo(frame);
				dial.setVisible(true);
			}
		});

		exitObjectItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

		// Listener sur le bouton Browse
		BrowseFileItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser filechooser = new JFileChooser();
				int returnvalue = filechooser.showOpenDialog(null);
				if (returnvalue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = filechooser.getSelectedFile();
					cd = new CatchDatas(selectedFile.getName());
					time.setMaximum(cd.getNbLines() - 11);
					time.setMinorTickSpacing(cd.getNbLines() / 100);
					canvasApplication.deleteLines();
					canvasApplication.changeGame(cd);
				}
			}
		});

		// JSlider pour la vitesse de jeu
		JSlider speed = new JSlider();
		JPanel speedPane = new JPanel();
		speed.setMinimum(1);
		speed.setMaximum(10);
		speed.setValue(1); // initialise à 1
		speed.setPaintTicks(true);
		speed.setPaintLabels(true);
		speed.setMinorTickSpacing(1);
		speed.setMajorTickSpacing(2);
		speed.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				canvasApplication.setSpeed(((JSlider) ce.getSource())
						.getValue());
			}
		});
		speedPane.setLayout(new BoxLayout(speedPane, BoxLayout.Y_AXIS));
		speedPane.setBorder(BorderFactory.createTitledBorder("Vitesse"));
		speedPane.add(speed);

		// JPanel pour les stats du joueur
		JPanel statPanel = new JPanel();
		statPanel.add(statLabel);

		// JComboList pour sélectionner les joueurs à afficher
		JComboBox listeJoueur = new JComboBox();
		listeJoueur.setMaximumSize(new Dimension(100, 30));
		listeJoueur.addItem("-");
		for (int i = 0; i < 14; i++) {
			if (cd.game.getPlayerIds(i) != 0)
				listeJoueur.addItem(cd.game.getPlayerIds(i));
		}
		listeJoueur.addItem("Aucun Joueur");
		listeJoueur.addItem("Toute l'équipe");

		listeJoueur.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				for (int j = 1; j <= cd.game.getNbPlayers() + 1; j++) {
					if (((JComboBox) e.getSource()).getSelectedItem() == (Object) j) {
						canvasApplication.setmustBeVisible(
								cd.game.getIdFromNum(j), true);
					}
				}
				if (((JComboBox) e.getSource()).getSelectedItem() == (Object) "Aucun Joueur") {
					for (int i = 0; i < canvasApplication.getmustBeVisible().length; i++) {
						canvasApplication.setmustBeVisible(i, false);
					}
				}
				if (((JComboBox) e.getSource()).getSelectedItem() == (Object) "Toute l'équipe") {
					for (int i = 0; i < canvasApplication.getmustBeVisible().length; i++) {
						canvasApplication.setmustBeVisible(i, true);
					}

				}
			}
		});
		// JPanel pour la liste
		JPanel list1pane = new JPanel();
		// titre du label
		list1pane.setBorder(BorderFactory.createTitledBorder("Selection"));
		list1pane.setLayout(new BoxLayout(list1pane, BoxLayout.Y_AXIS));
		list1pane.add(listeJoueur);

		// JComboList pour placer la camera virtuelle sur un joueur
		JComboBox listeJoueur2 = new JComboBox();
		listeJoueur2.setMaximumSize(new Dimension(100, 30));
		listeJoueur2.addItem("-");

		for (int i = 0; i < 14; i++) {
			if (cd.game.getPlayerIds(i) != 0)
				listeJoueur2.addItem(cd.game.getPlayerIds(i));
		}
		listeJoueur2.addItem("Terrain");
		listeJoueur2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				for (int j = 1; j <= cd.game.getNbPlayers() + 1; j++) {

					if (((JComboBox) e.getSource()).getSelectedItem() == (Object) j) {
						canvasApplication.lookAt(j);
						canvasApplication.setStats(j);
					} else if (((JComboBox) e.getSource()).getSelectedItem() == (Object) "Terrain") {
						canvasApplication.setTerrain(true);
						canvasApplication.setCamera();
					}
				}
			}

		});
		// JPanel pour la liste 2
		JPanel list2pane = new JPanel();
		list2pane.setBorder(BorderFactory.createTitledBorder("Camera"));
		list2pane.setLayout(new BoxLayout(list2pane, BoxLayout.X_AXIS));
		list2pane.add(listeJoueur2);
		list2pane.add(Box.createRigidArea(new Dimension(15, 0)));
		list2pane.add(statLabel);

		// Bouton d'activation du tracé
		JButton lineButton = new JButton(" Draw lines ");
		lineButton.setSize(30, 30);
		lineButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (canvasApplication.getLineBoolean() == false) {
					canvasApplication.setLineBoolean(true);
					((JButton) e.getSource()).setText("Delete lines");
				}
				else {
					canvasApplication.setLineBoolean(false);
					canvasApplication.deleteLines();
					((JButton) e.getSource()).setText("Draw lines    ");
				}

			}
		});

		// JSlider pour la localisation dans la mi-temps
		time.setMinimum(0);
		time.setMaximum(cd.getNbLines() - 11); // le curseur va de 0 au nombre
												// de lignes
		time.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				// capture de la nouvelle donnée
				int value = ((JSlider) ce.getSource()).getValue();
				// on réinitialise les paramètres nécessaires
				canvasApplication.setTimeSlider(value);
			}
		});

		// JButton pause/play
		JButton pausePlay = new JButton();
		pausePlay.setText("Pause");
		pausePlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Quand le bouton est pressé, on inverse la variable pause
				if (canvasApplication.getPause())
					canvasApplication.setPause(false);
				else
					canvasApplication.setPause(true);
				// On modifie la valeur du bouton
				if (canvasApplication.getPause())
					((AbstractButton) e.getSource()).setText("Play    ");
				else
					((AbstractButton) e.getSource()).setText("Pause");
			}
		});

		// Creation de deux JPanels : en haut les options, en bas l'affichage

		// JPanel des options
		JPanel options = new JPanel();
		options.setLayout(new BoxLayout(options, BoxLayout.X_AXIS));
		options.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		Dimension d = new Dimension(20, 0);
		options.add(list1pane);
		options.add(Box.createRigidArea(d));
		options.add(speedPane);
		options.add(Box.createRigidArea(d));
		options.add(list2pane);
		options.add(statPanel);
		options.add(lineButton);

		// JPanel de l'affichage du match
		JPanel view = new JPanel();
		view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));

		// JPanel pour la barre de temps, sous l'affichage
		JPanel bar = new JPanel();
		bar.setLayout(new BoxLayout(bar, BoxLayout.X_AXIS));
		bar.add(pausePlay);
		bar.add(time);

		view.add(canvas);
		view.add(bar);

		// Ajout des deux JPanel au JOanel principale
		panel.add(options, BorderLayout.NORTH);
		panel.add(view, BorderLayout.CENTER);

		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void createNewLoading() {
		frame = new JFrame("SOCCER STATS");

		// image
		JLabel imageLab = new JLabel(new ImageIcon("soccer-online.jpeg"));
		// bouton pour le chargement
		JButton b = new JButton("Select a file to load...");
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				filechooser = new JFileChooser();
				int returnvalue = filechooser.showOpenDialog(null);
				if (returnvalue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = filechooser.getSelectedFile();
					cd = new CatchDatas(filechooser.getSelectedFile()
							.toString());
				}
			}
		});

		// initialisation des paramètres de la frame
		frame.setSize(480, 400);
		frame.setLocationRelativeTo(null);
		frame.setTitle("SOCCER STATS");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Panel contenant le bouton
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(b);

		// Panel contenant les éléments
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		pane.add(imageLab);
		frame.add(pane);
		frame.add(buttonPane, BorderLayout.SOUTH);

		frame.pack();
		frame.setVisible(true);

		// tant que le fichier n'est pas totalement chargé, on répète une
		// attente
		while (cd.getLoadedLines() != cd.getNbLines()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}

		// fermeture de la frame
		frame.dispose();
	}

	public static void setWindow() {

		cd = new CatchDatas();
		createNewLoading();

		// create new JME appsettings
		AppSettings settings = new AppSettings(true);
		settings.setResolution(600, 450);
		settings.setSamples(8);

		time = new JSlider();
		statLabel = new JLabel();
		canvasApplication = new PlayGround(cd, time, statLabel);
		canvasApplication.setStats(-1);

		// nouvelle application
		JmeCanvasContext ctx = (JmeCanvasContext) canvasApplication
				.getContext();
		canvas = ctx.getCanvas();
		Dimension dim = new Dimension(settings.getWidth(), settings.getHeight());
		canvas.setPreferredSize(dim);

		// Create the JFrame with the Canvas on the middle
		createNewJFrame();
	}

}