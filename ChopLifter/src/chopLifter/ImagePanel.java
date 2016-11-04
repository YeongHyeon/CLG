package chopLifter;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import chopLifter.ChopLifterComponent.TimerHandler;

public class ImagePanel extends JPanel {
	Timer time;
	int degree = 0;

	public ImagePanel() {
		setVisible(true);
		setFocusable(true);
		setSize(640, 480);
		setBackground(Color.BLACK);
		time = new Timer(10, new TimerHandler());
		time.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Rectangle rect1 = new Rectangle(100, 100, 20, 20);
		g2d.setColor(Color.WHITE);
//		g2d.translate(rect1.x + (rect1.width / 2), rect1.y + (rect1.height / 2));
//		g2d.rotate(Math.toRadians(degree));
		g2d.rotate(Math.toRadians(degree), rect1.getX()+rect1.getWidth()/2, rect1.getY()+rect1.getHeight()/2);
		g2d.draw(rect1);
		g2d.fill(rect1);
	}

	class TimerHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			degree++;
			repaint();
		}
	}

	public static void main(String[] arg) {
		System.out.println("Start Program");
		JFrame f = new JFrame("ChopLifter_�ڿ���,����ȯ");
		f.setSize(640, 480);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ImagePanel sc = new ImagePanel();
		f.add(sc);
		f.setVisible(true);
	}
}