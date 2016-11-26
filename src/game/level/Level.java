package game.level;

public class Level {
	private double[][] vertices;
	private int[][][] faces;
	
	public Level(double[][] vertices, int[][][] faces) {
		this.vertices = vertices;
		this.faces = faces;
	}
	
	public double[][] getVertices()	{
		return vertices;
	}
	
	public int[][] getFaces(int n) {
		return faces[n - 3];
	}
}
