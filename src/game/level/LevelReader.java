package game.level;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class LevelReader {
	protected double[][] v;
	protected int[][][] f;

	public LevelReader(String path) throws IOException {
		BufferedReader fr = new BufferedReader(new FileReader(path));

		String vLine = fr.readLine();
		StringTokenizer st = new StringTokenizer(vLine);
		st.nextToken();
		int vertices = Integer.parseInt(st.nextToken());
		v = new double[vertices][3];
		for (int i = 0; i < vertices; i++) {
			StringTokenizer vertex = new StringTokenizer(fr.readLine());
			v[i][0] = Double.parseDouble(vertex.nextToken());
			v[i][1] = Double.parseDouble(vertex.nextToken());
			v[i][2] = Double.parseDouble(vertex.nextToken());
		}

		String fLine = fr.readLine();
		st = new StringTokenizer(fLine);
		st.nextToken();
		int faces3 = Integer.parseInt(st.nextToken());
		int faces4 = Integer.parseInt(st.nextToken());
		f = new int[][][] {new int[faces3][3], new int[faces4][4]};
		for (int i = 0; i < faces3; i++) {
			StringTokenizer face = new StringTokenizer(fr.readLine());
			f[0][i][0] = Integer.parseInt(face.nextToken());
			f[0][i][1] = Integer.parseInt(face.nextToken());
			f[0][i][2] = Integer.parseInt(face.nextToken());
		}
		for (int i = 0; i < faces4; i++) {
			StringTokenizer face = new StringTokenizer(fr.readLine());
			f[1][i][0] = Integer.parseInt(face.nextToken());
			f[1][i][1] = Integer.parseInt(face.nextToken());
			f[1][i][2] = Integer.parseInt(face.nextToken());
			f[1][i][3] = Integer.parseInt(face.nextToken());
		}
	}
}
