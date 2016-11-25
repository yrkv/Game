package game.input.mouse;

import java.awt.AWTException;
import java.awt.Robot;

import javax.swing.JFrame;

import game.Game;

public class Mouse {
	public int x, y;
	public boolean dragging;

	private int xDiffTotal, yDiffTotal;
	
	private Robot robot;
	private JFrame frame;
	
	private MouseMotion mouseMotion = new MouseMotion();
	private MouseEvents mouseEvents = new MouseEvents();
	
	public Mouse(Game game) {
		game.addMouseMotionListener(mouseMotion);
		game.addMouseListener(mouseEvents);
		frame = game.frame;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		x = mouseMotion.x;
		y = mouseMotion.y;
		xDiffTotal += mouseMotion.xDiff;
		yDiffTotal += mouseMotion.yDiff;
		if (frame.isFocused()) centerMouse();
	}
	
	public void centerMouse() {
		robot.mouseMove((int) frame.getLocationOnScreen().getX() + Game.centerX,
				(int) frame.getLocationOnScreen().getY() + Game.centerY);
	}
	
	public int getXDiffTotal() {
		return xDiffTotal;
	}
	
	public int getYDiffTotal() {
		return yDiffTotal;
	}
}
