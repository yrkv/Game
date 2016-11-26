package game.graphics;

import game.Game;
import game.level.Level;

public class Camera {
	private double x, y, z, rotX, rotY, viewDist, pixelsToUnit, screenDist;
	private Level level;
	
	public Camera(double x, double y, double z, double rotX, double rotY, Level level) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.rotX = rotX;
		this.rotY = rotY;
		this.level = level;
		viewDist = 40;
		pixelsToUnit = 128;
		screenDist = 4;
	}
	
	public void move(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}
	
	public void rotate(double rotX, double rotY) {
		final double rotationLimit = 0.01;
		this.rotX = rotX - rotationLimit < -Math.PI / 2 ? 
				-Math.PI / 2 + rotationLimit : rotX + rotationLimit > Math.PI / 2 ?
						Math.PI / 2 - rotationLimit : rotX;
		this.rotY = rotY;
	}
	
	public int[][][] view(int n) {
		int[][] f = level.getF(n);
		int[][][] points = new int[f.length][n][2];
		double[][] v = level.getV();
		for (int i = 0;i < f.length; i++) {
			boolean displayFace = true;
			for (int j = 0; j < n; j++) {
				double[] vertex = rotationMatrix(v[f[i][j]]);
				int xPos = (int) Math.round(Game.width / 2 + ((x - vertex[0]) / (z - vertex[2]) * screenDist * pixelsToUnit));
				int yPos = (int) Math.round(Game.height / 2 - ((y - vertex[1]) / (z - vertex[2]) * screenDist * pixelsToUnit));
				if (vertex[2] > z) {
					points[i][j][0] = xPos;
					points[i][j][1] = yPos;
				} else {
					displayFace = false;
				}
			}
			if (!displayFace)
				for (int j = 0; j < n; j++) {
					points[i][j][0] = -1;
					points[i][j][1] = -1;
				}
		}
		
		return points;
	}
	
	// might be changed to handle the vertex by reference instead of by value. 
	// It is probably faster to do it that way.
	private double[] rotationMatrix(double[] v) {
		double[] newV = new double[3];
		
		newV[0] = v[0] - x;
		newV[1] = v[1] - y;
		newV[2] = v[2] - z;
		
		double[] newerV = new double[3];
		newerV[0] = Math.cos(rotY) * newV[0] + Math.sin(rotY) * newV[2] + x;
		newerV[1] = newV[1] + y;
		newerV[2] = -Math.sin(rotY) * newV[0] + Math.cos(rotY) * newV[2] + z; 
		
		double[] newestV = new double[3];
		newestV[0] = newerV[0];
		newestV[1] = Math.cos(rotX) * newerV[1] - Math.sin(rotX) * newerV[2];
		newestV[2] = Math.sin(rotX) * newerV[1] + Math.cos(rotX) * newerV[2];
		
		return newestV;
	}
	
	public double[] getRot() {
		return new double[] {rotX, rotY};
	}
	
	public double[] getPos() {
		return new double[] {x, y, z};
	}
}
