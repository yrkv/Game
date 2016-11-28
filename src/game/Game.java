package game;

import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Robot;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import javax.swing.JFrame;

import game.graphics.Camera;
import game.graphics.Screen;
import game.input.Keyboard;
import game.input.mouse.Mouse;
import game.level.Level;

public class Game extends Canvas implements Runnable {
	public static int totalTicks = 0;
	public static int width = 1024;
	public static int height = width * 9 / 16; // 576
	public static int centerX = width / 2;
	public static int centerY = height / 2;
	
	private boolean running = false;

	private Thread thread;
	public JFrame frame;
	private Screen screen;
	private Level level;
	private Camera cam;
	private Keyboard key;
	public Mouse mouse;
	
	private Color[] colors;

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
		key = new Keyboard();
		cam = new Camera(0, 0, 0, 0, 0, level);
		
		addKeyListener(key);
		
//		Random rand = new Random();
//
//		for (int i = 0; i < 6; i++) {
//			float red = rand.nextFloat();
//			float green = rand.nextFloat();
//			float blue = rand.nextFloat();
//			Color randomColor = new Color(red, green, blue);
//		}
	}

	public static void main(String[] args)
	{
		Game game = new Game();
		game.frame = new JFrame();
		game.mouse = new Mouse(game);

		game.frame.setSize(width, height);
		game.frame.setResizable(false);
		game.frame.setTitle("game");
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);

		game.frame.add(game);
		game.frame.setCursor(game.frame.getToolkit().createCustomCursor(
	            new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
	            "null"));

		game.frame.setVisible(true);
		
		game.start();
	}
	
	private synchronized void start() {
		thread = new Thread(this, "Display");
		thread.start();
		running = true;
		Mouse.centerMouse();
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
		key.update();
		
		if (key.q) cam.move(0, 0.1, 0);
		if (key.e) cam.move(0, -0.1, 0);
		
		if (key.left) cam.move(-Math.cos(cam.getRotation()[1])/10, 0, -Math.sin(cam.getRotation()[1])/10);
		if (key.right) cam.move(Math.cos(cam.getRotation()[1])/10, 0, Math.sin(cam.getRotation()[1])/10);
		
		if (key.up) cam.move(-Math.sin(cam.getRotation()[1])/10, 0, Math.cos(cam.getRotation()[1])/10);
		if (key.down) cam.move(Math.sin(cam.getRotation()[1])/10, 0, -Math.cos(cam.getRotation()[1])/10);
		
		mouse.update();
		cam.rotate(-mouse.getYDiffTotal() / 100.0, -mouse.getXDiffTotal() / 100.0);
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
				frames = 0;
				updates = 0;
			}
		}
		stop();
	}
	
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		g.clearRect(0, 0, width, height);
		
		Polygon[] polygons = cam.view();

		for (int i = 0; i < polygons.length; i++) {
//			g.setColor(Color.blue);
//			g.fillPolygon(polygons[i]);
			g.setColor(Color.black);
			g.drawPolygon(polygons[i]);
		}
		
		
		
		g.drawLine(mouse.x, mouse.y, mouse.x, mouse.y);
		
		g.drawString("Rotation: " + cam.getRotation()[0] + ", " + cam.getRotation()[1], 0, 12);
		g.drawString(String.format("Location: %.2f, %.2f, %.2f", cam.getPosition()[0], cam.getPosition()[1], cam.getPosition()[2]), 0, 26);
		
		g.dispose();
		bs.show();
	}
}
