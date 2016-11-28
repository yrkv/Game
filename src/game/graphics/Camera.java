package game.graphics;

import java.awt.Polygon;

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
	
	
//	double[][] v = {{1, 0, 10}, {0, 0, 10}, {0, 1, 10}, {1, 1, 10}, {1, 0, 11}, {0, 0, 11}, {0, 1, 11}, {1, 1, 11}};
//	int[][][] f = {{}, {{0, 1, 2, 3}, {4, 5, 6, 7}, {1, 2, 6, 5}, {2, 3, 7, 6}, {0, 1, 5, 4}, {0, 3, 7, 4}}};
	
	public Polygon[] view() {
		int[][][] f = level.getFaces();
		double[][] v = level.getVertices();
		
		Polygon[] polygons = new Polygon[f[0].length + f[1].length];
		
		for (int n = 0; n <= 1; n++) {
			for (int i = 0; i < f[n].length; i++) {
				int[] xpoints = new int[n + 3];
				int[] ypoints = new int[n + 3];
				
				boolean displayFace = true;
				for (int j = 0; j < f[n][i].length; j++) {
					double[] vertex = rotateAroundCamera(v[f[n][i][j]]);
					
					int xPos = (int) Math.round(Game.width / 2 + ((x - vertex[0]) / (z - vertex[2]) * screenDist * pixelsToUnit));
					int yPos = (int) Math.round(Game.height / 2 - ((y - vertex[1]) / (z - vertex[2]) * screenDist * pixelsToUnit));
					
					if (vertex[2] > z) {
						xpoints[j] = xPos;
						ypoints[j] = yPos;
					} else {
						displayFace = false;
					}
				}
				
				if (!displayFace)
					for (int j = 0; j < n; j++) {
						xpoints[j] = -1;
						ypoints[j] = -1;
					}
				
				polygons[i] = new Polygon(xpoints, ypoints, n + 3);
			}
		}
		
		int[] arr = new int[polygons.length];
		for (int i = 0; i < arr.length; i++) arr[i] = i;
		
		for (int i = 0; i < polygons.length; i++) {
			for (int j = 0; j < polygons.length; j++) {
				if (j == i) continue;
				
				for (int k = 0; k < polygons[j].xpoints.length; k++) {
					boolean samePoint = false;
					for (int p = 0; p < polygons[i].xpoints.length; p++)
						if (polygons[i].xpoints[p] == polygons[j].xpoints[k] && polygons[i].ypoints[p] == polygons[j].ypoints[k])
							samePoint = true;
					
					if (samePoint) continue; // continue on to the next point if the point is in both polygons
					
					if (polygons[i].contains(polygons[j].xpoints[k], polygons[j].ypoints[k])) {
						double distanceToFace = getDistanceOfIntersection(polygons[i].npoints, polygons[i].npoints == 3 ? i : i - f[0].length, v[j]);
						double distanceToPoint = getDistance(v[j]);
						
						if (distanceToPoint > distanceToFace) {
							if (j < i) {
								Polygon temporaryPolygon = new Polygon(polygons[j].xpoints, polygons[j].ypoints, polygons[j].npoints);
								polygons[j] = new Polygon(polygons[i].xpoints, polygons[i].ypoints, polygons[i].npoints);
								polygons[i] = temporaryPolygon;
								int temp = arr[j];
								arr[j] = arr[i];
								arr[i] = temp;
							}
						}
						else {
							if (i < j) {
								Polygon temporaryPolygon = new Polygon(polygons[j].xpoints, polygons[j].ypoints, polygons[j].npoints);
								polygons[j] = new Polygon(polygons[i].xpoints, polygons[i].ypoints, polygons[i].npoints);
								polygons[i] = temporaryPolygon;
								int temp = arr[j];
								arr[j] = arr[i];
								arr[i] = temp;
							}
						}
					}
				}
			}
		}
		
		for (int i = 0; i < arr.length; i++) System.out.print(arr[i] + ", ");
		System.out.println();
		return polygons;
	}
	
	public double getDistanceOfIntersection(int n, int f, double[] vertex) { // I know this code is horrible and hard to understand. Sorry. 
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
