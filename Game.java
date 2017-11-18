package fr.et3.polytech.JavaIHM;

import java.util.Date;

public class Game {

	private int[] playerIds;
	private Player[] players;
	private Pitch pitch;
	private Date start; // date de début de la partie
	private Date end; // date de fin de la partie
	private float totalDistance;
	private Date[] dates;
	private Date currentDate;
	
	public int getIdFromNum(int num) {
		for(int i=0; i< 14; i++) {
			if (this.playerIds[i] == num)
				return i;
		}
		return -1;
	}
	
	public Game(int nb_lines) {
		// initialisation du tableau d'ids
		this.playerIds = new int[14];
		this.players = new Player[14];
		for (int i = 0; i < 14; i++) {
			this.playerIds[i] = 0;
		}
		
		this.pitch = new Pitch();
		this.dates = new Date[nb_lines];
	}
	
	public int getNbPlayers() {
		int res = 0;
		for (int i=0; i<14; i++) {
			if (this.playerIds[i] != 0) {
				res++;
			}
		}
		return res;
	}

	/**
	 * @return the playerIds
	 */
	public int getPlayerIds(int i) {
		return this.playerIds[i];
	}
	
	public Pitch getPitch() {
		return this.pitch;
	}
	
	public Date getEnd() {
		return this.end;
	}
	
	public Date getStart() {
		return this.start;
	}
	
	public Player getPlayer(int i) {
		return this.players[i];	
	}
	
	/**
	 * @param playerIds the playerIds to set
	 */
	public void setPlayerIds(int playerIds, int i) {
		this.playerIds[i] = playerIds;
	}
	
	public void setStart(Date d) {
		this.start = d;
	}
	
	public void setEnd(Date d) {
		this.end = d;
	}
	
	public Date getCurrentDate() {
		return this.currentDate;
	}
	
	public void setCurrentDate(Date d) {
		this.currentDate = d;
	}
	
	public Date getDates(int i) {
		return this.dates[i];
	}
	
	public void setDates(int i, Date d) {
		this.dates[i] = d;
	}
	
	/**
	 * newPlayer
	 * Ajoute un joueur au match
	 * @param id
	 * @param numPlayer
	 */
	public void newPlayer(int id, int numPlayer, int n) {
		this.players[numPlayer] = new Player(id, n);
	}
	
	public void setPlayerDatas(String line, int i) { 
		// on commence par séparer les données
		String[] datas = line.split(",");
		
		// ensuite, on set les différentes données 
		// en faisant appel aux setters de Player
		this.players[i].setDate(datas[0]);
		this.players[i].setPosition(Float.parseFloat(datas[2]), Float.parseFloat(datas[3]));
		this.players[i].setHeading(Float.parseFloat(datas[4]));
		this.players[i].setDirection(Float.parseFloat(datas[5]));
		this.players[i].setEnergy(Float.parseFloat(datas[6]));
		this.players[i].setSpeed(Float.parseFloat(datas[7]));
		this.players[i].setTotalDistance(Float.parseFloat(datas[8]));
		
		// on incrémente l'indice des données
		this.players[i].setNbDatas();
	}
	
	public int getNbLines() {
		return this.dates.length;
	}
}