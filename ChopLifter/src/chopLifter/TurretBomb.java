package chopLifter;

import java.awt.Graphics;
import java.awt.Image;

public class TurretBomb extends GameObj {
	private Image img;

	TurretBomb(Image img, int w, int h) {
		image = img;
		state = ST_DEATH;
		width = w;
		height = h;
	}

	// x, y ��ġ���� mx, my ��ġ�� ��ź �߻�
	void shot(double x, double y, double mx, double my) { // ��ź�� ��ǥ : x,y
		if (state == ST_DEATH) {
			state = ST_ALIVE;
			this.x = x;
			this.y = y;
			dx = (mx - x) / 50; // ��ź�� ���ּ� ������ x��ǥ �Ÿ��� 50����Ͽ� ��ź�� �̵��Ÿ� ��ǥ�� �����־���
			dy = (my - y) / 50; // ��ź�� ���ּ� ������ y��ǥ �Ÿ��� 50����Ͽ� ��ź�� �̵��Ÿ� ��ǥ��
			// �����־���.

			double rate = dy / dx;

			if (Math.abs(dx) <= 2) { // �����϶�
				if (dy > 0)
					dy = 3;
				else
					dy = -3;
			} else {// �ƴҶ�
				if (dx > 0) {
					dy = 5 * rate;
					dx = 5;
				} else if (dx < 0) {
					dy = -5 * rate;
					dx = -5;
				}
			}
		}
	}

	void blast() {
		state = ST_BLAST;
		blast_count = 10;
	}

	// Ÿ�̸ӿ� ���� ��ź�� ������ ó��
	void move() {
		if (state == ST_ALIVE) {
			x += dx;
			y += dy;
			if (y < -height || ChopLifter.FRAME_H + height < y || x < -width) {
				state = ST_DEATH;
			}
			if (y >= ((ChopLifter.FRAME_H / 5 * 4) + (ChopLifter.FRAME_H / 5 / 2))) {
				blast();
			}
		} else if (state == ST_BLAST) {
			blast_count--;
			if (blast_count == 0) {
				state = ST_DEATH;
			}
		}
	}

	// ��ź �׸���
	void draw(Graphics g) {
		if (state == ST_ALIVE) {
			drawImage(g);
		} else if (state == ST_BLAST) {
			drawSmallBlast(g);
		}
	}
}
