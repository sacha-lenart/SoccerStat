package fr.et3.polytech.JavaIHM;

import javax.swing.JLabel;
import javax.swing.JSlider;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.control.BillboardControl;
import com.jme3.system.AppSettings;

public class PlayGround extends SimpleApplication {

	private BitmapText[] num_joueur;
	private Spatial[] arrow;
	private Spatial joueurSpatial;
	private Game game;
	private int index;
	private float tpfSum;
	private int speed;
	private boolean terrain;
	private ChaseCamera chaseCam;
	private JSlider time;
	private boolean pause;
	private boolean[] mustBeVisible = new boolean[14];
	private Node[] node;
	private int selectedPlayerView;
	private JLabel statLabel;
	private CameraNode camNode;
	private Geometry[][] lineGeo;
	private Mesh[][] lineMesh;
	private boolean lineBoolean;

	public void changeGame(CatchDatas cd) {
		this.game = cd.game;
		this.index = 0;
		this.tpfSum = 0.0f;
		this.speed = 1;
		this.pause = false;
		this.selectedPlayerView = 0;
		this.lineGeo = new Geometry[cd.game.getNbPlayers()][80];
		this.lineMesh = new Mesh[cd.game.getNbPlayers()][80];
		for (int i=0; i<cd.game.getNbPlayers(); i++) 
			for (int j=0; j<80; j++) {
				lineMesh[i][j] = new Mesh();
				lineGeo[i][j] = new Geometry("lineGeo",lineMesh[i][j]);
			}
		
		for (int i = 0; i < mustBeVisible.length; i++) {
			mustBeVisible[i] = true;
		}
	}
	public boolean[] getmustBeVisible() {
		return this.mustBeVisible;
	}
	
	public void setmustBeVisible(int i, boolean b) {
		this.mustBeVisible[i] = b;
	}
	public boolean getTerrain(){
		return this.terrain;
	}
	
	public void setTerrain(boolean b) {
		this.terrain=b;
	}

	public void setIndex(int i) {
		this.index = i;
	}

	public void setTpfSum(int t) {
		this.tpfSum = 0;
	}

	public void setSelectedPlayer(int i) {
		this.selectedPlayerView = i;
	}

	public boolean getPause() {
		return this.pause;
	}

	public void setPause(boolean p) {
		this.pause = p;
	}
	
	public boolean getLineBoolean() {
		return this.lineBoolean;
	}
	
	public void setLineBoolean(boolean b) {
		this.lineBoolean=b;
	}

	public PlayGround(CatchDatas cd, JSlider t, JLabel statLab) {
		this.game = cd.game;
		for (int i = 0; i < mustBeVisible.length; i++) {
			mustBeVisible[i] = true;
		}
		this.terrain=false;
		this.index = 0;
		this.tpfSum = 0.0f;
		this.speed = 1;
		this.time = t;
		this.pause = false;
		this.selectedPlayerView = 0;
		this.statLabel = statLab;
		this.lineGeo = new Geometry[cd.game.getNbPlayers()][80];
		this.lineMesh = new Mesh[cd.game.getNbPlayers()][80];
		
		for (int i=0; i<cd.game.getNbPlayers(); i++) 
			for (int j=0; j<80; j++) {
				lineMesh[i][j] = new Mesh();
				lineGeo[i][j] = new Geometry("lineGeo",lineMesh[i][j]);
			}

		AppSettings set = new AppSettings(true);
		set.setResolution(1280, 800);
		set.setSamples(8);
		set.setFrameRate(60);
		this.setSettings(set);
		this.setShowSettings(false);
		this.setDisplayStatView(false);
		this.setDisplayFps(false);
		this.setPauseOnLostFocus(false);
		this.createCanvas();
	}

	@Override
	public void simpleInitApp() {

		assetManager.registerLocator("stade.zip", ZipLocator.class);
		Spatial field_geom = assetManager.loadModel("stade/soccer.obj");
		Node field_node = new Node("field");
		field_node.attachChild(field_geom);
		rootNode.attachChild(field_node);

		DirectionalLight directionLight = new DirectionalLight(new Vector3f(-2,
				-10, 1));
		directionLight.setColor(ColorRGBA.White.mult(1.3f));
		rootNode.addLight(directionLight);
		viewPort.setBackgroundColor(new ColorRGBA(0.1f, 0.1f, 0.1f, 0.1f));

		simpleInitPlayer();

		flyCam.setEnabled(false);
		camNode = new CameraNode("CameraNode", getCamera());
		camNode.setEnabled(false);
		chaseCam = new ChaseCamera(cam, field_geom, inputManager);
		chaseCam.setEnabled(true);
		chaseCam.setDragToRotate(true);
		chaseCam.setInvertVerticalAxis(true);
		chaseCam.setRotationSpeed(10.0f);
		chaseCam.setMinVerticalRotation(/* (float)-(Math.PI/2-0.0001f) */(float) (Math.PI / 10 - 0.0001f));
		chaseCam.setMaxVerticalRotation((float) Math.PI / 2);
		chaseCam.setMinDistance(7.5f);
		chaseCam.setMaxDistance(100.0f);
	}

	public Node getNode(int i) {
		return this.node[i];
	}
	
	public void lookAt(int j) {
		chaseCam.setEnabled(false);
		camNode.setEnabled(true);
		getNode(this.game.getIdFromNum(j)).attachChild(camNode);
	}

	public void simpleInitPlayer() {

		arrow = new Spatial[14];
		num_joueur = new BitmapText[14];
		node = new Node[14];

		for (int i = 0; i < 14; i++) {
			joueurSpatial = assetManager.loadModel("stade/player.obj");
			arrow[i] = joueurSpatial;
			node[i] = new Node();
			BillboardControl bbControl = new BillboardControl();
			bbControl.setAlignment(BillboardControl.Alignment.Screen);
			num_joueur[i] = new BitmapText(guiFont, false);
			num_joueur[i].setText(Integer.toString(this.game.getPlayerIds(i)));
			num_joueur[i].setSize(1.0f);
			num_joueur[i].setLocalTranslation(0f, 2f, 0f);
			num_joueur[i].setQueueBucket(Bucket.Transparent);
			num_joueur[i].addControl(bbControl);

			node[i].attachChild(arrow[i]);
			node[i].attachChild(num_joueur[i]);

			rootNode.setLocalTranslation(10.0f, 10.0f, 10.0f);
			rootNode.attachChild(node[i]);
		}

	}

	public void setSpeed(int s) {
		this.speed = s;
	}

	public int getIndex() {
		return this.index;
	}

	public void incrementIndex() {
		// tant que la date suivante du tableau est identique, on
		// incrémente l'index
		if (this.index < this.game.getNbLines()) {
			while (this.game.getDates(this.index).compareTo(
					this.game.getDates(this.index + 1)) == 0) {
				if (this.index + 1 < this.game.getNbLines())
					this.index++;
			}
			// on donne a la date courante la valeur suivante
			this.game.setCurrentDate(this.game.getDates(this.index + 2));
			this.index += 2;
		}
	}

	public void setTimeSlider(int v) {
		if (this.index != v) {
			this.setIndex(v);
			this.game.setCurrentDate(this.game.getDates(v));
			this.setTpfSum(0);
			for (int k = 0; k < this.game.getNbPlayers(); k++)
				this.game.getPlayer(k).resetIndex();
		}
	}

	public void setStats(int num) {
		if (num >= 0) {
			int id = this.game.getIdFromNum(num);
			selectedPlayerView = num;
			String[] stats = this.game.getPlayer(id).getStats(
					this.game.getPlayer(id).getIndex());
			this.statLabel.setText("<html>" + stats[0] + "<br />" + stats[1]
					+ "<br />" + stats[2] + "</html>");
		}
		else this.statLabel.setText("<html>" + "energie : 000.00" + "<br />" + "vitesse : 00.000"
					+ "<br />" + "distance : 0000.0" + "</html>");
	}

	public void setCamera() {
		if (this.terrain == true) {
			this.camNode.setEnabled(false);
			this.chaseCam.setEnabled(true);

		}
	}
	
	public void line() {
		
		for(int i=0; i < this.game.getNbPlayers(); i++) {
			if (this.mustBeVisible[i]) {
				// récupération des anciennes coorddonnées du joueur
				int k = this.game.getPlayer(i).getIndex();
				float[] posX = new float[80]; // stockées dans 
				float[] posY = new float[80];
				int l = 0;
				while (l<80) {
					if (k > 0) {
						posX[l] = this.game.getPlayer(i).getPosX(k);
						posY[l] = this.game.getPlayer(i).getPosY(k);
						k--;
						l++;
					}
					else break;
				}
				
				// affichage des lignes
				for (int j=0; j<l-1; j++) {
					lineMesh[i][j].setMode(Mesh.Mode.Lines);
					lineMesh[i][j].setBuffer(VertexBuffer.Type.Position,3,new float[]{posX[j] - 105/2, 1, -(posY[j] - 68/2),
																				posX[j+1] - 105/2,1, -(posY[j+1] - 68/2)});
					lineMesh[i][j].setLineWidth(1);
					
					// modification de la couleur en fonction de la vitesse
					Material mat = new Material (assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
					if(this.game.getPlayer(i).getSpeed(this.game.getPlayer(i).getIndex()-j) < 1.0f)
						mat.setColor("Color", ColorRGBA.Blue);
					else if (this.game.getPlayer(i).getSpeed(this.game.getPlayer(i).getIndex()-j) < 5.0f)
							mat.setColor("Color", ColorRGBA.Yellow);
					else mat.setColor("Color", ColorRGBA.Red);
					lineGeo[i][j].setMaterial(mat);
					
					rootNode.attachChild(lineGeo[i][j]);
				}
			}
		}
	}
	
	public void deleteLines() {
		for (int i=0; i<this.game.getNbPlayers(); i++) 
			for (int j=0; j<80; j++) {
				lineMesh[i][j].setBuffer(VertexBuffer.Type.Position,3,new float[]{0,0,0,0,0,0});
				lineGeo[i][j] = new Geometry("lineGeo",lineMesh[i][j]);
			}
	}

	@Override
	public void simpleUpdate(float tpf) {

		// on va incrémenter la date courante toutes les 5 millisecondes
		if (!pause)
			this.tpfSum += speed * tpf;
		if (this.tpfSum >= 0.05) {

			// on commence par vérifier que la date courrante est différente de
			// la date de fin
			if (this.game.getCurrentDate().compareTo(this.game.getEnd()) != 0) {
				this.incrementIndex();
				this.tpfSum -= 0.05f;
				while (this.tpfSum > 0.05) {
					this.incrementIndex();
					this.tpfSum -= 0.05f;
				}
			}

			// il faut, pour chaque joueur, récupérer l'index
			// de ses tableaux correspondant à la date courante
			int i;
			for (i = 0; i < 14; i++) {
				boolean visible = false;
				// si la nouvelle date courante est la meme que la prochaine du
				// joueur,
				// on incrémente son index
				// il faut tout d'abord vérifié que la date du joueur n'est pas
				// nulle
				if (i < this.game.getNbPlayers()) {
					if ((game.getPlayer(i).getDate(
							game.getPlayer(i).getIndex() + 1) != null))
						if (game.getPlayer(i)
								.getDate(game.getPlayer(i).getIndex())
								.compareTo(game.getCurrentDate()) == -1) {
							visible = true;
							if (this.game.getPlayer(i).setIndex(
									this.game.getCurrentDate()) == -1)
								visible = false;
						}

					// actualisation de la position
					node[i].setLocalTranslation(
							this.game.getPlayer(i).getPosX(
									this.game.getPlayer(i).getIndex()) - 105 / 2,
							1.0f,
							-(this.game.getPlayer(i).getPosY(
									this.game.getPlayer(i).getIndex()) - 68 / 2));

					// actualisation de la direction
					// on utilise un quaternion pour pouvoir effectuer la
					// rotation
					// à partir du repère de l'origine
					Quaternion rot = new Quaternion();
					float angle = this.game.getPlayer(i).getDirection(
							this.game.getPlayer(i).getIndex())
							- (float) Math.PI;
					rot.fromAngleAxis(angle, new Vector3f(0, 1, 0));
					node[i].setLocalRotation(rot);
				}

				// affichage ou non de la flèche
				if ((visible) && (mustBeVisible[i]) == true) {
					arrow[i].setCullHint(CullHint.Never);
					num_joueur[i].setText(Integer.toString(this.game
							.getPlayerIds(i)));
				} else {
					arrow[i].setCullHint(CullHint.Always);
					num_joueur[i].setText(" ");
				}
			}

			// modification de la valeur du JSlider de temps
			this.time.setValue(this.index);

			// affichage des valeurs du joueur sélectionné
			if (this.selectedPlayerView != 0)
				this.setStats(this.selectedPlayerView);
			else this.setStats(-1);
			
			// affichage des lignes
			if(lineBoolean==true) {
				line();
			}
		}
	}
}