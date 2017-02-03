import game.graphics.RenderingLine;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RenderingLineTest {
	@Test
	void lineIntersections() {
		RenderingLine line1 = new RenderingLine(0, 0, 2, 2);
		RenderingLine line2 = new RenderingLine(1, 0, 1, 2);
		assertEquals(new Point2D.Double(1, 1), line1.getIntersection(line2));

		line1 = new RenderingLine(0, 0, 0, 2);
		line2 = new RenderingLine(-1, 1, 1, 1);
		assertEquals(new Point2D.Double(0, 1), line1.getIntersection(line2));
	}
}
