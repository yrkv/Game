package game.level;

import java.io.IOException;

public class Level {
	private double[][] vertices;
	private int[][][] faces;
	
	public Level(String path) {
		LevelReader lr = null;
		try {
			lr = new LevelReader(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.vertices = lr.v;
		this.faces = lr.f;
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
}
