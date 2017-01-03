package game.input.mouse;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

import javax.swing.JFrame;

import game.Game;

public class Mouse {
	public int x, y;
	public boolean dragging;
	static int xDiffTotal, yDiffTotal;
	
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
		if (Game.totalTicks == 0) {
			centerMouse();
			robot.delay(10);
			click();
			robot.delay(10);
			centerMouse();
		}
	}
	
	protected static void centerMouse() {
		robot.mouseMove(Game.centerX, Game.centerY);
	}

	private static void click() {
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.delay(10);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
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
