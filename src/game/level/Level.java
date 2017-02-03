package game.level;

import java.io.IOException;

public class Level {
	private double[][] vertices;
	private int[][][] faces;
	private int[][] edges;
	
	public Level(String path) {
		LevelReader lr = null;
		try {
			lr = new LevelReader(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.vertices = lr.v;
		this.faces = lr.f;
		this.edges = lr.e;
	}
	
	public double[][] getVertices()	{
		return vertices;
	}
	
	public int[][] getFaces(int n) {
		return faces[n - 3];
	}
	
	public int[][][] getFaces() {
		return faces;
	}

	public int[][] getEdges() {
		return edges;
	}
}
