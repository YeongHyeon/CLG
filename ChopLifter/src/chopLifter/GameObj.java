package chopLifter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JPanel;

abstract public class GameObj extends JPanel {
	public static int ST_DEATH = 0;
	public static int ST_ALIVE = 1;
	public static int ST_BLAST = 2;

	public static int GO_RIGHT = 3;
	public static int GO_LEFT = 4;
	public static int GO_UP = 5;
	public static int GO_DOWN = 6;
	public static int GO_NEUTRAL = 7;

	Image image; // ���� ��ü �̹���
	int state; // ���� ��ü ����
	double x, y, dx, dy; // ���� ��ü ��ġ
	int tmpW, tmpH;
	int width, height, degree; // ���� ��ü ũ��
	int blast_count; // ���� ī��Ʈ

	int directionX; // ������ ��Ÿ�� ����.
	int directionY; // ������ ��Ÿ�� ����.

	int getState() {
		return state;
	} // ���� Ȯ��

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	double getLeftX() {
		return x - width;
	}

	double getLeftY() {
		return y - height;
	}

	void setShift(double gx) {
		x -= gx;
	}

	// �̹��� ���
	void drawImage(Graphics g) {
		g.drawImage(image, (int) (x - width / 2), (int) (y - height / 2), width, height, null);
	}

	// ���� �̹��� ���
	void drawBlast(Graphics g) {
		// blast_count ���� ��ŭ ���� �׸���
		for (int i = 1; i < blast_count; i++) {
			g.setColor(new Color(Util.randColorElement(128, 255), Util.randColorElement(0, 127),
					Util.randColorElement(0, 127)));
			double x0 = Util.rand(-30, 30);
			double y0 = Util.rand(-30, 30);
			double r0 = Util.rand(5, 30);
			g.fillOval((int) (x - x0 - r0 / 2), (int) (y - y0 - r0 / 2), (int) r0, (int) r0);
		}
	}

	void drawTiltImage(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.rotate(Math.toRadians(degree), (x - width / 2) + width / 2, (y - height / 2) + height / 2);
		g2d.drawImage(image, (int) (x - width / 2), (int) (y - height / 2), width, height, this);
	}

	// �ٿ���ڽ� ����
	Rectangle getBBox() {
		return new Rectangle((int) (x - Math.abs(width) / 2), (int) (y - Math.abs(height) / 2), Math.abs(width),
				Math.abs(height));
	}

	// �߻� �޼ҵ�
	abstract void blast();

	abstract void move();

	abstract void draw(Graphics g);
}