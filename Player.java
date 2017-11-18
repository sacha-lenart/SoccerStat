package fr.et3.polytech.JavaIHM;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Player {

	private int playerId;
	private int nbDatas;
	private float[] posX;
	private float[] posY;
	private float[] heading;
	private float[] direction;
	private float[] energy;
	private float[] speed;
	private float[] totalDistance;
	private Date[] date;
	private int index;
	
	public Player(int id, int nb_datas) {
		this.playerId = id;
		this.nbDatas = 0;
		this.date = new Date[nb_datas];
		this.posX = new float[nb_datas];
		this.posY = new float[nb_datas];
		this.heading = new float[nb_datas];
		this.direction = new float[nb_datas];
		this.energy = new float[nb_datas];
		this.speed = new float[nb_datas];
		this.totalDistance = new float[nb_datas];
		this.index = 0;
	}
	
	public String[] getStats(int i) {
		String[] stats = new String[3];
		DecimalFormat df = new DecimalFormat("000.00"); 
		DecimalFormat df2 = new DecimalFormat("00.000"); 
		DecimalFormat df3 = new DecimalFormat("0000.0"); 
		stats[0] = "energie : " + df.format(this.energy[i]);
		stats[1] = "vitesse : " + df2.format(this.speed[i]);
		stats[2] = "distance : " + df3.format(this.totalDistance[i]);
		return stats;
	}
	
	public void setDate(String date) {

		if (date.length() == 23) {
			date = date.substring(1, 22);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			try {
				this.date[this.nbDatas] = df.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		else if (date.length() == 24) {
			date = date.substring(1, 23);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
			try {
				
				this.date[this.nbDatas] = df.parse(date);

			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (date.length() == 21) {
			date = date.substring(1, 20);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				this.date[this.nbDatas] = df.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int setIndex(Date d) {
		int start_index = this.index;
		while (this.date[this.index+1].compareTo(d) == -1) {
			if ((this.index >= this.nbDatas-1) || (this.date[this.index+2] == null))
				return -1;
			this.index++;
		}
		if (this.index-start_index > 1)
			return 0;
		else return 1;
	}

	public Date getDate(int i) {
		return this.date[i];
	}
	
	public void setPosition(float x, float y) {
		this.posX[this.nbDatas] = x;
		this.posY[this.nbDatas] = y;
	}
	
	public void setHeading(float h) {
		this.heading[this.nbDatas] = h;
	}
	
	public void setDirection(float d) {
		this.direction[this.nbDatas] = d;
	}
	
	public void setEnergy(float e) {
		this.energy[this.nbDatas] = e;
	}
	
	public void setSpeed(float sp) {
		this.speed[this.nbDatas] = sp;
	}
	
	public void setTotalDistance(float td) {
		this.totalDistance[this.nbDatas] = td;
	}
	
	/**
	 * @return the playerId
	 */
	public int getPlayerId() {
		return playerId;
	}

	/**
	 * @return the nbDatas
	 */
	public int getNbDatas() {
		return this.nbDatas;
	}

	/**
	 * @return the posX
	 */
	public float getPosX(int i) {
		return this.posX[i];
	}

	/**
	 * @return the posY
	 */
	public float getPosY(int i) {
		return this.posY[i];
	}

	/**
	 * @return the heading
	 */
	public float getHeading(int i) {
		return this.heading[i];
	}

	/**
	 * @return the direction
	 */
	public float getDirection(int i) {
		return this.direction[i];
	}

	/**
	 * @return the energy
	 */
	public float getEnergy(int i) {
		return this.energy[i];
	}

	/**
	 * @return the speed
	 */
	public float getSpeed(int i) {
		return this.speed[i];
	}

	/**
	 * @return the totalDistance
	 */
	public float getTotalDistance(int i) {
		return this.totalDistance[i];
	}
	
	public int getIndex() {
		return this.index;
	}

	public void setNbDatas() {
		this.nbDatas++;
	}
	
	public void resetIndex() {
		this.index = 0;
	}
	
}