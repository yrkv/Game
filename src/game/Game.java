package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import game.graphics.Camera;
import game.graphics.RenderingLine;
import game.graphics.Screen;
import game.input.Keyboard;
import game.input.mouse.Mouse;
import game.level.Level;

public class Game extends Canvas implements Runnable {
	public static int totalTicks = 0;
	public static int width;
	public static int height;
	public static int centerX;
	public static int centerY;
	
	private boolean running = false;

	private Thread thread;
	public JFrame frame;
	private Screen screen;
	private Level level;
	private Camera cam;
	private Keyboard key;
	public Mouse mouse;
	
	public Game()
	{
		frame = new JFrame();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);

		frame.setCursor(frame.getToolkit().createCustomCursor(
				new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
				"null"));

		frame.setVisible(true);
		height = frame.getHeight();
		width = frame.getWidth();
		centerY = height / 2;
		centerX = width / 2;

		screen = new Screen(width, height);
		
		level = new Level("res/level.txt");
		key = new Keyboard();
		cam = new Camera(0, 0, 0, 0, 0, level);

		addKeyListener(key);
	}

	public static void main(String[] args)
	{
		Game game = new Game();
		game.mouse = new Mouse(game);
		game.frame.add(game);
		game.frame.setVisible(true);
		game.start();
	}
	
	private synchronized void start() {
		thread = new Thread(this, "Display");
		thread.start();
		running = true;
		mouse.start();
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

		g.setColor(Color.BLACK);
		
//		Polygon[] polygons = cam.view();
//
//		for (int i = 0; i < polygons.length; i++) {
//			g.setColor(Color.blue);
//			g.fillPolygon(polygons[i]);
//			g.setColor(Color.black);
//			g.drawPolygon(polygons[i]);
//		}

		ArrayList<RenderingLine> lines = cam.newView();

//		for (int i = 0; i < lines.size(); i++) {
//			g.setColor(Color.BLACK);
//			if (lines[i].render) g.drawLine((int) lines[i].x1, (int) lines[i].y1, (int) lines[i].x2, (int) lines[i].y2);
//		}

		while (!lines.isEmpty()) {
			RenderingLine line = lines.remove(0);
			if (line.render) g.drawLine((int) line.x1, (int) line.y1, (int) line.x2, (int) line.y2);
		}
		
		g.drawLine(mouse.x, mouse.y, mouse.x, mouse.y);
		
		g.drawString("Rotation: " + cam.getRotation()[0] + ", " + cam.getRotation()[1], 0, 12);
		g.drawString(String.format("Location: %.2f, %.2f, %.2f", cam.getPosition()[0], cam.getPosition()[1], cam.getPosition()[2]), 0, 26);
		
		g.dispose();
		bs.show();
	}
}
