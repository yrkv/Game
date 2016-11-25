package game.input.mouse;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import game.Game;

public class MouseMotion implements MouseMotionListener {
	int x, y, xDiff, yDiff;
	boolean dragging = false;

	public void mouseDragged(MouseEvent e) {
		motion(e);
		dragging = true;
	}

	public void mouseMoved(MouseEvent e) {
		motion(e);
		dragging = false;
	}
	
	private void motion(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		xDiff = x - Game.centerX;
		yDiff = y - Game.centerY;
	}
}
