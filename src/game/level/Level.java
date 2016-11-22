package game.level;

public class Level {
	private int[] v, f;
	
	public Level(int[] v, int[] f) {
		this.v = v;
		this.f = f;
		
		
	}
	
	public int[] getV()	{
		return v;
	}
	
	public int[] getF()	{
		return f;
	}
}
