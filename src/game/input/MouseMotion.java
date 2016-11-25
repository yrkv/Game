package game.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseMotion implements MouseMotionListener {
	public int x, y;
	public boolean dragging = false;

	public void mouseDragged(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		dragging = true;
	}

	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		dragging = false;
	}
}
