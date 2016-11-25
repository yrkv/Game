package game.level;

public class Level {
	private int[][] f3, f4;
	private double[][] v;
	private int[][][] f;
	
	public Level(double[][] v, int[][][] f) {
		this.v = v;
		this.f = f;
	}
	
	public double[][] getV()	{
		return v;
	}
	
	public int[][] getF(int n) {
		return f[n - 3];
	}
}
