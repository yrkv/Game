package game.graphics;

import game.level.Level;

public class Line3D {
	private double[] v1, v2;

	public Line3D(double[] v1, double[] v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	public double getDistanceOfIntersection(Level level, int f) {
		int[] face = level.getFaces(4)[f];
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


		double[] directionVector = {v2[0] - v1[0], v2[1] - v1[1], v2[2] - v1[2]};

		double t = (d - (normalVector[0] * v1[0] + normalVector[1] * v1[1] + normalVector[2] * v1[2])) / (normalVector[0] * directionVector[0] + normalVector[1] * directionVector[1] + normalVector[2] * directionVector[2]);

		double[] intersection = {t * directionVector[0], t * directionVector[1], t * directionVector[2]};

		return Math.sqrt(Math.pow(intersection[0], 2) + Math.pow(intersection[1], 2) + Math.pow(intersection[2], 2));
	}
}
