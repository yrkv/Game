package game.level;

public class Level {
	private int[][] f3, f4;
	private double[][] v;
	
	public Level(double[][] v, int[][] f3, int[][] f4) {
		this.v = v;
		this.f3 = f3;
		this.f4 = f4;
	}
	
	public double[][] getV()	{
		return v;
	}
	
	public int[][] getF4()	{
		return f4;
	}
	
	public int[][] getF3()	{
		return f3;
	}
}
