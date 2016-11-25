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
	
	public int[][][] view(int n) {
		int[][] f = level.getF(n);
		int[][][] points = new int[f.length][n][2];
		double[][] v = level.getV();
		for (int i = 0;i < f.length; i++) {
			for (int j = 0; j < n; j++) {
				double[] vertex = v[f[i][j]];
				int xPos = (int) Math.round(Game.width / 2 + ((x - vertex[0]) / (z - vertex[2]) * screenDist * pixelsToUnit));
				int yPos = (int) Math.round(Game.height / 2 - ((y - vertex[1]) / (z - vertex[2]) * screenDist * pixelsToUnit));
				
				points[i][j][0] = xPos;
				points[i][j][1] = yPos;
			}
		}
		
		return points;
	}
}
