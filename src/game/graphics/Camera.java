package game.graphics;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;

import com.sun.org.apache.regexp.internal.RE;
import game.Game;
import game.level.Level;
import javafx.scene.shape.Line;

public class Camera {
	private double x, y, z, rotX, rotY, viewDist, pixelsToUnit, screenDist, fov;
	private Level level;
	private double[] location = new double[3];
	
	public Camera(double x, double y, double z, double rotX, double rotY, Level level) {
		location[0] = this.x = x;
		location[1] = this.y = y;
		location[2] = this.z = z;
		this.rotX = rotX;
		this.rotY = rotY;
		this.level = level;
		viewDist = 40;
		pixelsToUnit = 128;
		screenDist = 4;
		fov = 60 * Math.PI / 180;
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

	public Polygon[] view() {
		int[][][] f = level.getFaces();
		double[][] v = level.getVertices();
		
		Polygon[] polygons = new Polygon[f[0].length + f[1].length];
		
//		for (int n = 0; n <= 1; n++) {
		int n = 1; // change later
		for (int i = 0; i < f[n].length; i++) {
			int[] xPoints = new int[n + 3];
			int[] yPoints = new int[n + 3];

			boolean displayFace = true;
			for (int j = 0; j < f[n][i].length; j++) {
				double[] vertex = rotateAroundCamera(v[f[n][i][j]]);

				int xPos = (int) Math.round(Game.width / 2 + (Math.atan((x - vertex[0]) / (z - vertex[2])) / fov * Game.width));
				int yPos = (int) Math.round(Game.height / 2 - (Math.atan((y - vertex[1]) / (z - vertex[2])) / fov * Game.width));

				if (vertex[2] >= z) {
					xPoints[j] = xPos;
					yPoints[j] = yPos;
				} else {
					displayFace = false;
				}
			}
			
			if (!displayFace)
				for (int j = 0; j < n + 3; j++) {
					xPoints[j] = -1;
					yPoints[j] = -1;
				}
			
			polygons[i] = new Polygon(xPoints, yPoints, n + 3);
		}
		
		return polygons;
	}

	/*public ArrayList<RenderingLine> newView() {
		double[][] v = level.getVertices();
		int[][][] f = level.getFaces();
		int[][] e = level.getEdges();

		ArrayList<RenderingLine> newLines = new ArrayList<>();


		for (int i = 0; i < e.length; i++) {
			double[][] vertices = new double[4][];
			int[] xPoints = new int[4];
			int[] yPoints = new int[4];

			vertices[0] = rotateAroundCamera(v[e[i][0]]);
			vertices[1] = rotateAroundCamera(v[e[i][1]]);

			xPoints[0] = (int) Math.round(Game.width / 2 + (Math.atan((x - vertices[0][0]) / (z - vertices[0][2])) / fov * Game.width));
			yPoints[0] = (int) Math.round(Game.height / 2 - (Math.atan((y - vertices[0][1]) / (z - vertices[0][2])) / fov * Game.width));

			xPoints[1] = (int) Math.round(Game.width / 2 + (Math.atan((x - vertices[1][0]) / (z - vertices[1][2])) / fov * Game.width));
			yPoints[1] = (int) Math.round(Game.height / 2 - (Math.atan((y - vertices[1][1]) / (z - vertices[1][2])) / fov * Game.width));

			Line2D line = new Line2D.Double(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
			RenderingLine line1 = new RenderingLine(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);

			for (int j = i + 1; j < e.length; j++) {
				vertices[2] = rotateAroundCamera(v[e[j][0]]);
				vertices[3] = rotateAroundCamera(v[e[j][1]]);

				xPoints[2] = (int) Math.round(Game.width / 2 + (Math.atan((x - vertices[2][0]) / (z - vertices[2][2])) / fov * Game.width));
				yPoints[2] = (int) Math.round(Game.height / 2 - (Math.atan((y - vertices[2][1]) / (z - vertices[2][2])) / fov * Game.width));

				xPoints[3] = (int) Math.round(Game.width / 2 + (Math.atan((x - vertices[3][0]) / (z - vertices[3][2])) / fov * Game.width));
				yPoints[3] = (int) Math.round(Game.height / 2 - (Math.atan((y - vertices[3][1]) / (z - vertices[3][2])) / fov * Game.width));

				RenderingLine line2 = new RenderingLine(xPoints[2], yPoints[2], xPoints[3], yPoints[3]);

				Line2D newLine = new Line2D.Double(xPoints[2], yPoints[2], xPoints[3], yPoints[3]);

				if (line.intersectsLine(newLine)) {
					Line3D[] splitLines = new Line3D[4];
					for (int k = 0; k < 4; k++)
						splitLines[k] = new Line3D(location, vertices[k]);

					Polygon[] poly = new  Polygon[2];
					poly[0] = faceToPolygon(e[j][2]);
					poly[1] = faceToPolygon(e[i][2]);

					boolean[] render = new boolean[4];

					for (int k = 0; k < 1; k++) {
						if (poly[k].contains(xPoints[k*2], yPoints[k*2])) {
							if (splitLines[k*2].getDistanceOfIntersection(level, e[j][2]) >= getDistance(vertices[k*2]))
								render[k*2] = true;
							if (splitLines[k*2 + 1].getDistanceOfIntersection(level, e[j][3]) >= getDistance(vertices[k*2 + 1]))
								render[k*2 + 1] = true;
						} else {
							if (splitLines[k*2].getDistanceOfIntersection(level, e[j][3]) >= getDistance(vertices[k*2]))
								render[k*2] = true;
							if (splitLines[k*2 + 1].getDistanceOfIntersection(level, e[j][2]) >= getDistance(vertices[k*2 + 1]))
								render[k*2 + 1] = true;
						}
					}

					Point2D intersection = line1.getIntersection(line2);
					int intX = (int) intersection.getX();
					int intY = (int) intersection.getX();

					newLines.add(new RenderingLine(xPoints[0], yPoints[0], intX, intY, render[0]));
					newLines.add(new RenderingLine(intX, intY, xPoints[1], yPoints[1], render[1]));
					newLines.add(new RenderingLine(xPoints[2], yPoints[2], intX, intY, render[2]));
					newLines.add(new RenderingLine(intX, intY, xPoints[3], yPoints[3], render[3]));
				}
			}
		}
		return newLines;
	}*/

	public ArrayList<RenderingLine> newView() {
		ArrayList<RenderingLine> lines = new ArrayList<>();
		int[][] edges = level.getEdges();
		double[][] vertices = level.getVertices();
		for (int i = 0; i < edges.length; i++) {
			Point p1 = viewVertex(edges[i][0]);
			Point p2 = viewVertex(edges[i][1]);
			RenderingLine initialLine = new RenderingLine(p1, p2);

			LinkedList<RenderingLine> newLines = new LinkedList<>();
			newLines.add(initialLine);
			int lastSize = 0;
			int currSize = newLines.size();

			while (currSize != lastSize) {
				for (int j = 0; j < currSize; j++) {
					RenderingLine line = newLines.removeFirst();

					for (int p = 0; p < level.getFaces().length; p++) {
						Polygon polygon = faceToPolygon(p);
						if (line.entirelyContainedBy(polygon)) {
							double[] vertex = vertices[edges[i][0]];
							Line3D line3D = new Line3D(location, vertex);
							double distanceToIntersection = line3D.getDistanceOfIntersection(level, p);
							if (distanceToIntersection > getDistance(vertex)) newLines.add(line);
						} else if (line.intersects(polygon)) {
							ArrayList<Point2D> intersections = new ArrayList<>();
							intersections.add(p1);
							intersections.addAll(line.getIntersection(polygon));
							intersections.add(p2);

							for (int k = 0; k < intersections.size() - 1; k++) {
								Point2D point1 = intersections.get(i);
								Point2D point2 = intersections.get(i+1);



							}
						}
					}
				}
			}

			lines.addAll(newLines);
		}

		return  lines;
	}

	private double[] midpoint3D(double[] a, double[] b) {
		return new double[] {a[0] + b[0], a[1] + b[1], a[2] + b[2]};
	}

	private Polygon faceToPolygon(int face) {
		int[][][] f = level.getFaces();

		int[] faceVertices = f[1][face];

		int[] xPoints = new int[faceVertices.length];
		int[] yPoints = new int[faceVertices.length];

		for (int i = 0; i < faceVertices.length; i++) {
			Point point = viewVertex(faceVertices[i]);
			xPoints[i] = (int) point.getX();
			yPoints[i] = (int) point.getY();
		}

		return new Polygon(xPoints, yPoints, faceVertices.length);
	}

	private Point viewVertex(int vertex) {
		return viewVertex(level.getVertices()[vertex]);
	}

	private Point viewVertex(double[] vertex) {
		double[] v = rotateAroundCamera(vertex);

		int pointX = (int) Math.round(Game.width / 2 + (Math.atan((x - v[0]) / (z - v[2])) / fov * Game.width));
		int pointY = (int) Math.round(Game.height / 2 - (Math.atan((y - v[1]) / (z - v[2])) / fov * Game.width));

		return new Point(pointX, pointY);
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
