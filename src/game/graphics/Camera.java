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
		int[][] f = level.getFaces(n);
		int[][][] points = new int[f.length][n][2];
		double[][] v = level.getVertices();
		for (int i = 0;i < f.length; i++) {
			boolean displayFace = true;
			for (int j = 0; j < n; j++) {
				double[] vertex = rotateAroundCamera(v[f[i][j]]);
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
	
	public double getDistanceOfIntersection(int n, int f, double[] vertex) { // I know this code is horrible and hard to understand. Sorry. This is my first time working with this kind of math.
		int[] face = level.getFaces(n)[f];
		double[][] vertices = level.getVertices();
		double[][] vectors = {{vertices[face[0]][0]-vertices[face[2]][0],
			vertices[face[0]][1]-vertices[face[2]][1],
			vertices[face[0]][2]-vertices[face[2]][2]
			}, {vertices[face[1]][0]-vertices[face[2]][0],
				vertices[face[1]][1]-vertices[face[2]][1],
				vertices[face[1]][2]-vertices[face[2]][2]
				}};
		double[] normalVector = {vectors[0][2] * vectors[1][1] - vectors[0][1] * vectors[1][2],
				vectors[0][2] * vectors[1][0] - vectors[0][0] * vectors[1][2],
				vectors[0][0] * vectors[1][1] - vectors[0][1] * vectors[1][0]};
		
		double d = vertices[face[0]][0] * normalVector[0] + vertices[face[0]][1] * normalVector[1] + vertices[face[0]][2] * normalVector[2];
		
		
		double[] directionVector = {vertex[0] - x, vertex[1] - y, vertex[2] - z};
		
		double t = (d - (normalVector[0] * x + normalVector[1] * y + normalVector[2] * z)) / (normalVector[0] * directionVector[0] + normalVector[1] * directionVector[1] + normalVector[2] * directionVector[2]);
		
		double[] intersection = {t * directionVector[0], t * directionVector[1], t * directionVector[2]};
		
		return Math.sqrt(Math.pow(intersection[0], 2) + Math.pow(intersection[1], 2) + Math.pow(intersection[2], 2));
	}
	
	public double getDistance(double[] point) {
		return Math.sqrt(Math.pow(point[0] - x, 2) + Math.pow(point[1] - y, 2) + Math.pow(point[2] - z, 2));
	}
	
	private double[] rotateAroundCamera(double[] vertex) {
		double[] newV = new double[3];
		
		newV[0] = vertex[0] - x;
		newV[1] = vertex[1] - y;
		newV[2] = vertex[2] - z;
		
		double[] newerV = new double[3];
		newerV[0] = Math.cos(rotY) * newV[0] + Math.sin(rotY) * newV[2];
		newerV[1] = newV[1];
		newerV[2] = -Math.sin(rotY) * newV[0] + Math.cos(rotY) * newV[2]; 
		
		double[] newestV = new double[3];
		newestV[0] = newerV[0] + x;
		newestV[1] = Math.cos(rotX) * newerV[1] - Math.sin(rotX) * newerV[2] + y;
		newestV[2] = Math.sin(rotX) * newerV[1] + Math.cos(rotX) * newerV[2] + z;
		
		return newestV;
	}
	
	public double[] getRotation() {
		return new double[] {rotX, rotY};
	}
	
	public double[] getPosition() {
		return new double[] {x, y, z};
	}
}
