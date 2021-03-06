package chopLifter;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;

import chopLifter.ChopLifterComponent.TimerHandler;

public class ImagePanel extends JPanel {
	static final int R = 100;
	Rectangle rect[] = new Rectangle[R];

	Timer time;
	int degree = 0;

	public ImagePanel() {
		setVisible(true);
		setFocusable(true);
		setSize(640, 480);
		setBackground(Color.BLACK);

		for (int i = 0; i < R; i++) {
			rect[i] = new Rectangle((i%10 * 20), ((i/10) * 20), 20, 20);
		}

		time = new Timer(50, new TimerHandler());
		time.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform beforeLotate = g2d.getTransform();
		g2d.setColor(Color.WHITE);
		for (int i = 0; i < R; i++) {
			g2d.setTransform(beforeLotate);
			g2d.rotate(Math.toRadians(degree), rect[i].getX() + rect[i].getWidth() / 2,
					rect[i].getY() + rect[i].getHeight() / 2);
			g2d.draw(rect[i]);
		}
	}

	class TimerHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			degree++;
			repaint();
		}
	}

	public static void main(String[] arg) {
		System.out.println("Start Program");
		JFrame f = new JFrame("ROTATE");
		f.setSize(200+16, 200+16+28);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ImagePanel sc = new ImagePanel();
		f.add(sc);
		f.setVisible(true);
	}
}