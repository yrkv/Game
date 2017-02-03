package game.graphics;

import game.Game;
import game.level.Level;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

public class RenderingLine {
	public double x1, y1, x2, y2, dx, dy;
	private Point2D p1, p2;
	public boolean render = true;
	Line3D line3D;

	public RenderingLine(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		dx = x2 - x1;
		dy = y2 - y1;
		p1 = new Point2D.Double(x1, y1);
		p2 = new Point2D.Double(x2, y2);
	}

	public RenderingLine(Line2D line) {
		x1 = line.getX1();
		y1 = line.getY1();
		x2 = line.getX2();
		y2 = line.getY2();
		dx = x2 - x1;
		dy = y2 - y1;
		p1 = new Point2D.Double(x1, y1);
		p2 = new Point2D.Double(x2, y2);
	}

	public RenderingLine(double x1, double y1, double x2, double y2, boolean render) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		dx = x2 - x1;
		dy = y2 - y1;
		p1 = new Point2D.Double(x1, y1);
		p2 = new Point2D.Double(x2, y2);
		this.render = render;
	}

	public RenderingLine(Point2D start, Point2D end) {
		x1 = start.getX();
		y1 = start.getY();
		x2 = end.getX();
		y2 = end.getY();
		dx = x2 - x1;
		dy = y2 - y1;
		p1 = start;
		p2 = end;
	}

	public RenderingLine() {
		x1 = 0;
		y1 = 0;
		x2 = 0;
		y2 = 0;
		dx = x2 - x1;
		dy = y2 - y1;
		p1 = new Point2D.Double(x1, y1);
		p2 = new Point2D.Double(x2, y2);
	}

	public Point2D getIntersection(RenderingLine line) {
		double b = (dx * (y1 - line.y1) - dy * (x1 - line.x1)) / (dx * line.dy - line.dx * dy);
		double x = line.x1 + line.dx * b;
		double y = line.y1 + line.dy * b;

		return new Point2D.Double(x, y);
	}

	public boolean intersects(Polygon polygon) {
		return (polygon.contains(p1) || polygon.contains(p2));
	}

	public boolean entirelyContainedBy(Polygon polygon) {
		return (polygon.contains(p1) && polygon.contains(p2));
	}

	private Line2D toLine2D() {
		return new Line2D.Double(x1, y1, x2, y2);
	}

	public ArrayList<Point2D> getIntersection(Polygon polygon) {
		ArrayList<Point2D> points = new ArrayList<>();

		if (!intersects(polygon)) return points;
		else
			for (int i = 1; i < polygon.npoints; i++) {
				int j = i == polygon.npoints - 1 ? 0 : i + 1;

				Line2D line = new Line2D.Double(polygon.xpoints[i], polygon.ypoints[0], polygon.xpoints[1], polygon.ypoints[1]);
				if (toLine2D().intersectsLine(line))
					points.add(getIntersection(new RenderingLine(line)));
			}

		return points;
	}
}
