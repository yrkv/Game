package game.input.mouse;

import java.awt.AWTException;
import java.awt.Robot;

import javax.swing.JFrame;

import game.Game;

public class Mouse {
	public int x, y;
	public boolean dragging;

	static int xDiffTotal;
	static int yDiffTotal;
	
	private static Robot robot;
	static JFrame frame;
	
	private MouseMotion mouseMotion;
	private MouseEvents mouseEvents;
	
	public Mouse(Game game) {
		frame = game.frame;
		mouseMotion = new MouseMotion();
		mouseEvents = new MouseEvents();
		game.addMouseMotionListener(mouseMotion);
		game.addMouseListener(mouseEvents);
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		x = mouseMotion.x;
		y = mouseMotion.y;
		if (Game.totalTicks == 0) centerMouse();
	}
	
	public static void centerMouse() {
		robot.mouseMove((int) frame.getLocationOnScreen().getX() + Game.centerX,
				(int) frame.getLocationOnScreen().getY() + Game.centerY);
		
//		System.out.println(frame.getLocationOnScreen().getY() + Game.centerY);
	}
	
	public static void click() {
		robot.mousePress(1);
		robot.delay(10);
		robot.mouseRelease(1);
	}
	
	public int getXDiffTotal() {
		return xDiffTotal;
	}
	
	public int getYDiffTotal() {
		return yDiffTotal;
	}
	
	void setTotalDiff(int xDiff, int yDiff) {
		xDiffTotal = xDiff;
		yDiffTotal = yDiff;
	}
}
