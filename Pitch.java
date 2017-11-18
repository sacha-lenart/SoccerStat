package fr.et3.polytech.JavaIHM;

public class Pitch {

	private int[][] pitchPos;
	final int lenght = 125;
	final int width = 88;
	
	public Pitch() {
		// initialisation de la grille du terrain
		this.pitchPos = new int[this.width][this.lenght];
		for (int i=0; i<this.width; i++) {
			for (int j=0; j<this.lenght; j++) {
				this.pitchPos[i][j] = 0;
			}
		}
	}
	
	public void incrementPitch(float x, float y) {
		int posx = (int) x;
		posx += 10;
		int posy = (int) y;
		posy += 10;
		this.pitchPos[posy][posx]++; 
	}
}
