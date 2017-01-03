package game.input.mouse;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import game.Game;

public class MouseMotion implements MouseMotionListener {
	private static int maxYDiffTotal = (int) (Math.PI * 50);
	int x, y;
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

        Mouse.xDiffTotal += x - Game.centerX;
        Mouse.yDiffTotal += y - Game.centerY;

        if (Mouse.yDiffTotal > maxYDiffTotal) Mouse.yDiffTotal = maxYDiffTotal;
        if (Mouse.yDiffTotal < -maxYDiffTotal) Mouse.yDiffTotal = -maxYDiffTotal;

		if (Mouse.frame.isFocused()) Mouse.centerMouse();
	}
}
