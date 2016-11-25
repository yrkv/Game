package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JComponent;
import javax.swing.JFrame;

import game.graphics.Camera;
import game.graphics.Screen;
import game.level.Level;

public class Game extends JComponent
{
	public static int totalTicks = 0;
	public static int width = 1024;
	public static int height = width * 9 / 16; // 576

	private JFrame frame;
	private Screen screen;
	private Level level;
	private Camera cam;

//	private BufferedImage image = new BufferedImage(width, height,
//			BufferedImage.TYPE_INT_RGB);
//	private int[] pixels = ((DataBufferInt) image.getRaster()
//			.getDataBuffer()).getData();

	public Game()
	{
		frame = new JFrame();
		screen = new Screen(width, height);
		double[][] v = {{1, 0, 10}, {0, 0, 10}, {0, 1, 10}, {1, 1, 10}, {1, 0, 11}, {0, 0, 11}, {0, 1, 11}, {1, 1, 11}};
		int[][] f3 = {};
		int[][] f4 = {{0, 1, 2, 3}, {4, 5, 6, 7}, {1, 2, 6, 5}, {2, 3, 7, 6}, {0, 1, 5, 4}, {0, 3, 7, 4}};
		
		level = new Level(v, f3, f4);
		
		cam = new Camera(-4, 1.5, 0, 0, level);
	}

	public static void main(String[] args)
	{
		Game game = new Game();
		game.frame = new JFrame();

		game.frame.setSize(width, height);
		game.frame.setResizable(false);
		game.frame.setTitle("game");
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);

		game.frame.add(game);

		game.frame.setVisible(true);
	}

	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.black);
		
		int[][][] points = cam.view3();

		for (int i = 0; i < points.length; i++) {
			int[] a = {points[i][0][0], points[i][1][0], points[i][2][0]};
			int[] b = {points[i][0][1], points[i][1][1], points[i][2][1]};
			g2.drawPolygon(a, b, 3);
		}
		
		points = cam.view4();

		for (int i = 0; i < points.length; i++) {
			int[] a = {points[i][0][0], points[i][1][0], points[i][2][0], points[i][3][0]};
			int[] b = {points[i][0][1], points[i][1][1], points[i][2][1], points[i][3][1]};
			g2.drawPolygon(a, b, 4);
		}

		//g2.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}
}
