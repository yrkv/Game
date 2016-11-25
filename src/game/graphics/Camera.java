package game.graphics;

import game.Game;
import game.level.Level;

public class Camera {
	private double x, y, z, rot, viewDist, pixelsToUnit, screenDist;
	private Level level;
	
	public Camera(double x, double y, double z, double rot, Level level) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.rot = rot;
		this.level = level;
		viewDist = 40;
		pixelsToUnit = 128;
		screenDist = 4;
	}
	
	public void move() {
		
	}
	
	public int[][][] view3() {
		int[][] f3 = level.getF3();
		int[][][] points3 = new int[f3.length][3][2];
		double[][] v = level.getV();
		for (int i = 0;i < f3.length; i++) {
			for (int j = 0; j < 3; j++) {
				double[] vertex = v[f3[i][j]];
				int xPos = (int) Math.round(Game.width / 2 + ((x - vertex[0]) / (z - vertex[2]) * screenDist * pixelsToUnit));
				int yPos = (int) Math.round(Game.height / 2 - ((y - vertex[1]) / (z - vertex[2]) * screenDist * pixelsToUnit));
				
				points3[i][j][0] = xPos;
				points3[i][j][1] = yPos;
			}
		}
		
		return points3;
	}
	
	public int[][][] view4() {
		int[][] f4 = level.getF4();
		int[][][] points4 = new int[f4.length][4][2];
		double[][] v = level.getV();
		for (int i = 0;i < f4.length; i++) {
			for (int j = 0; j < 4; j++) {
				double[] vertex = v[f4[i][j]];
				int xPos = (int) Math.round(Game.width / 2 + ((x - vertex[0]) / (z - vertex[2]) * screenDist * pixelsToUnit));
				int yPos = (int) Math.round(Game.height / 2 - ((y - vertex[1]) / (z - vertex[2]) * screenDist * pixelsToUnit));
				
				points4[i][j][0] = xPos;
				points4[i][j][1] = yPos;
			}
		}
		
		return points4;
	}
}
