package game.input.mouse;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import game.Game;

public class MouseMotion implements MouseMotionListener {
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
		
		Mouse.xDiffTotal += x - (Game.centerX - 3);  // works on AP comp sci computers
		Mouse.yDiffTotal += y - (Game.centerY - 26); 
		
//		Mouse.xDiffTotal += x - (Game.centerX); // works on my computer at home
//		Mouse.yDiffTotal += y - (Game.centerY);
		
		if (Mouse.frame.isFocused()) Mouse.centerMouse();
	}
}
