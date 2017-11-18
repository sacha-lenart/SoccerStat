package fr.et3.polytech.JavaIHM;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CatchDatas {

	private String[] array;
	public Game game;
	private int nb_lines;
	private boolean startLoading;
	private int loadedLines;
	
	public String getArrayLine(int i) {
		return this.array[i];
	}
	
	public int getNbLines() {
		return this.nb_lines;
	}
	
	public int getLoadedLines() {
		return this.loadedLines;
	}
	
	public boolean getStartLoading() {
		return this.startLoading;
	}
	
	/**CatchDatas
	 * 
	 * Constructeur pour la classe CatchDatas
	 * Prends en paramètre le nombre de lignes du fichier chargé
	 */
	public CatchDatas() {
		this.startLoading = false;
		this.loadedLines = -1;
	}
	
	
	/**getDatas
	 * 
	 * Prend en paramètre le nom du fichier à charger 
	 * et stck le contenu dans le tableau array
	 * @param fileName
	 */
	public void getDatas(String fileName) {
		try {
			FileReader file  = new FileReader(fileName);
			BufferedReader bufRead = new BufferedReader(file);
			
			String line = bufRead.readLine();
			int i = 0;
			while(line != null) {	
				this.array[i] = line;
				line = bufRead.readLine();
				i++;
			}
			bufRead.close();
			file.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**getNbSavings
	 * 
	 * Prend en paramètre le nom d'un fichier
	 * Retourne son nombre de lignes (d'enregistrements ici)
	 * @return n
	 */
	public static int getNbSavings(String file_name) {
		try {
			FileReader file  = new FileReader(file_name);
			BufferedReader bufRead = new BufferedReader(file);
			
			String line = bufRead.readLine();
			int n = 0;
			while( line != null) {
				line = bufRead.readLine();
				n++;
			}
			bufRead.close();
			file.close();
			return n;
			
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**getPlayerIdFromLine
	 * 
	 * Prend une ligne en paramètre, et retourne l'id du joueur correspondant
	 * @param line
	 */
	public static int getPlayerIdFromLine(String line) {
		String values[] = line.split(",");
		return Integer.parseInt(values[1]); 
	}
	
	public static float[] getPlayerPosFromLine(String line) {
		String values[] = line.split(",");
		float[] res = new float[2];
		res[0] = Float.parseFloat(values[2]);
		res[1] = Float.parseFloat(values[3]);
		return res; 
	}
	
	
	public CatchDatas(String file_name) {
		
		this.nb_lines = getNbSavings(file_name);
		this.array = new String[nb_lines];
		this.getDatas(file_name);
		this.startLoading = false;
		
		this.game = new Game(nb_lines);
		int nbPlayers = 0;
		int i;
		
		// boucle principale, on remplie les stats de la partie
		for (i = 0; i < nb_lines; i++) {
			
			// on commence par vérifier si le joueur a déjà été créé 
			boolean alreadySet = false;
			int j;
			for (j = 0; j < nbPlayers; j++) {
				if (getPlayerIdFromLine(this.array[i]) == this.game.getPlayerIds(j))
					alreadySet = true;
			}
			
			// si l'id du joueur n'est pas contenu, on créé un nouveau joueur
			if (!alreadySet) {
				this.game.newPlayer(getPlayerIdFromLine(this.array[i]), nbPlayers, nb_lines/8);
				this.game.setPlayerIds(getPlayerIdFromLine(this.array[i]), nbPlayers);
				nbPlayers++;
			}
			
			// on rempli ensuite les stats du joueur concerné 
			// on recherche l'indice du joueur correspondant à son numéro
			int k = -1;
			for (j = 0; j < nbPlayers; j++) {
				if (getPlayerIdFromLine(this.array[i]) == this.game.getPlayerIds(j)) {
					k = j;
					j = nbPlayers;
				}
			}
			this.game.setPlayerDatas(this.array[i], k);
			
			// initialisation de la date de début et de fin du match
			if (i == 0)
				this.game.setStart(this.game.getPlayer(k).getDate(0));
			else if (i == nb_lines-1) 
				this.game.setEnd(this.game.getPlayer(k).getDate(this.game.getPlayer(k).getNbDatas()-1));
			
			// initialisation du tableau de dates
			this.game.setDates(i, this.game.getPlayer(k).getDate(this.game.getPlayer(k).getNbDatas()-1));
			
			// remplissage des positions sur le terrain
			float[] pos = getPlayerPosFromLine(this.array[i]);
			this.game.getPitch().incrementPitch(pos[0], pos[1]);
			
			// modification de la valeur des lignes chargées
			this.loadedLines = i+1;
			if (i==0)
				this.startLoading = true;
		}	
		
		this.game.setCurrentDate(this.game.getDates(0));
	}
}