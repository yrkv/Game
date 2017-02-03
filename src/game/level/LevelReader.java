package game.level;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class LevelReader {
	protected double[][] v;
	protected int[][][] f;
	protected int[][] e;

	public LevelReader(String path) throws IOException {
		BufferedReader fr = new BufferedReader(new FileReader(path));

		String vLine = fr.readLine();
		StringTokenizer st = new StringTokenizer(vLine);
		String nextToken = st.nextToken();
		assert nextToken.equals("v") : "invalid file";
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
		nextToken = st.nextToken();
		assert nextToken.equals("f") : "invalid file";
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

		String eLine = fr.readLine();
		st = new StringTokenizer(eLine);
		nextToken = st.nextToken();
		assert nextToken.equals("e") : "invalid file";
		int edges = Integer.parseInt(st.nextToken());

		e = new int[edges][4];
		for (int i = 0; i < edges; i++) {
			StringTokenizer edge = new StringTokenizer(fr.readLine());
			e[i][0] = Integer.parseInt(edge.nextToken());
			e[i][1] = Integer.parseInt(edge.nextToken());
			e[i][2] = Integer.parseInt(edge.nextToken());
			e[i][3] = Integer.parseInt(edge.nextToken());
		}
	}
}
