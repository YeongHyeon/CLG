package chopLifter;

import java.awt.Graphics;
import java.awt.Image;

public class Missile extends GameObj {
	private Image[] img = new Image[2];
	private int pDir, shotDir;
	private int tmp_counter, inertia_counter; // ����ī����
	private boolean drop;

	// ������
	Missile(Image[] imgMissile, int w, int h) {
		img = imgMissile;
		state = ST_DEATH;
		tmpW = w;
		tmpH = h;
	}

	// x, y ��ġ���� mx, my ��ġ�� ��ź �߻�
	void shot(double shipX, double shipY, int pDir, int d, int degree) {
		if (state == ST_DEATH) {
			state = ST_ALIVE;
			this.x = shipX;
			this.y = shipY;
			this.pDir = pDir;
			shotDir = d;
			dx = 0;
			dy = 0;
			tmp_counter = 10;
			drop = true;

			if (Math.abs(degree) < 5) {
				image = img[0];
				if (pDir == GO_LEFT) { // ���⿡ ���� �̹����� �¿츦 ��Ī��Ų��.
					width = tmpW;
				} else {
					width = -tmpW;
				}
				height = tmpH;
			} else {
				image = img[1];
				if (pDir == GO_LEFT) { // ���⿡ ���� �̹����� �¿츦 ��Ī��Ų��.
					width = tmpW + 5;
				} else {
					width = -tmpW + 5;
				}
				height = tmpH + 5;
			}
		}
	}

	void setInertia() {
		inertia_counter = 50;
	}

	void blast() {
		state = ST_DEATH;
	}

	void move() { // Target�� �Ҿ��� ��� ����
		if (state == ST_ALIVE) {
			if (shotDir == 0) { // ���� �߻�
				if (pDir == GO_LEFT) {
					dx--;
				} else {
					dx++;
				}
				dy += 0.1;
			} else if (shotDir == 1) { // ����߸���
				if (inertia_counter <= 0) { // ������ ������
					// NOP
				} else { // ������ ������ ����������� ������
					inertia_counter++;
					if (pDir == GO_LEFT) {
						dx -= 0.5;
						width = tmpW;
					} else {
						dx += 0.5;
						width = -tmpW;
					}
				}
				dy++;
			}
			x += dx;
			y += dy;
			inertia_counter--;
		}
		if (x < -tmpW || x > ChopLifter.FRAME_W + tmpW || y < -height || y > ChopLifter.FRAME_H + height) {
			state = ST_DEATH;
			inertia_counter = 0;
		}
	}

	// �׸���
	void draw(Graphics g) {
		if (state == ST_ALIVE) {
			drawImage(g);
		}
	}
}