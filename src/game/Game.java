package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import game.graphics.Camera;
import game.graphics.Screen;
import game.level.Level;

public class Game extends Canvas implements Runnable {
	public static int totalTicks = 0;
	public static int width = 1024;
	public static int height = width * 9 / 16; // 576
	
	private boolean running = false;

	private Thread thread;
	private JFrame frame;
	private Screen screen;
	private Level level;
	private Camera cam;

	private BufferedImage image = new BufferedImage(width, height,
			BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster()
			.getDataBuffer()).getData();

	public Game()
	{
		frame = new JFrame();
		screen = new Screen(width, height);
		double[][] v = {{1, 0, 10}, {0, 0, 10}, {0, 1, 10}, {1, 1, 10}, {1, 0, 11}, {0, 0, 11}, {0, 1, 11}, {1, 1, 11}};
		int[][][] f = {{}, {{0, 1, 2, 3}, {4, 5, 6, 7}, {1, 2, 6, 5}, {2, 3, 7, 6}, {0, 1, 5, 4}, {0, 3, 7, 4}}};
		
		level = new Level(v, f);
		
		cam = new Camera(5, -2, 0, 20.0 / 180 * Math.PI, 20.0 / 180 * Math.PI, level);
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
		
		game.start();
	}
	
	private synchronized void start() {
		thread = new Thread(this, "Display");
		thread.start();
		running = true;
	}
	
	private synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void update() {
		
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
					update();
					updates++;
					totalTicks++;
					delta--;
			}
			render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle("game | " + updates + " ups, " + frames + " fps"); 
				// ups is so shaky because of how high fps is, it wont be an issue once the game has more stuff.
				frames = 0;
				updates = 0;
			}
		}
	}
	
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
//		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		// I'm going to have to swap to a system using this if I want to map images to the models.
		// I'm still not sure if I want to do that.
		
		g.setColor(Color.black);
		
		int[][][] points = cam.view(3);

		for (int i = 0; i < points.length; i++) {
			int[] a = {points[i][0][0], points[i][1][0], points[i][2][0]};
			int[] b = {points[i][0][1], points[i][1][1], points[i][2][1]};
			g.drawPolygon(a, b, 3);
		}
		
		points = cam.view(4);

		for (int i = 0; i < points.length; i++) {
			int[] a = {points[i][0][0], points[i][1][0], points[i][2][0], points[i][3][0]};
			int[] b = {points[i][0][1], points[i][1][1], points[i][2][1], points[i][3][1]};
			g.drawPolygon(a, b, 4);
		}
		
		g.dispose();
		bs.show();
	}
}
