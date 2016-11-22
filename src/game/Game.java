package game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JComponent;
import javax.swing.JFrame;

import game.graphics.Screen;

public class Game extends JComponent
{
	public static int totalTicks = 0;
	public static int width = 256;
	public static int height = width * 9 / 16;
	public static double scale = 3;

	private JFrame frame;
	private Screen screen;

	private BufferedImage image = new BufferedImage(width, height,
			BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster()
			.getDataBuffer()).getData();

	public Game()
	{
		frame = new JFrame();
		screen = new Screen(width, height);
	}

	public static void main(String[] args)
	{
		Game game = new Game();
		game.frame = new JFrame();

		game.frame.setSize((int) (width * scale), (int) (height * scale));
		game.frame.setResizable(false);
		game.frame.setTitle("game");
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);

		game.frame.add(game);

		game.frame.setVisible(true);
	}

	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;

		

		g2.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}
}
