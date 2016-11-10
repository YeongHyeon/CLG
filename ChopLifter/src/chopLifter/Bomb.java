package chopLifter;

import java.awt.Graphics;
import java.awt.Image;

public class Bomb extends GameObj {
	private double dx, dy; // ��ź�� ���� �̵� �Ÿ� �밢������ �̵��� ���̱� ������ 2�� ������ �ʿ�
	private int tmpW, tmpH;

	Bomb(Image img, int w, int h) {
		image = img;
		state = ST_DEATH;
		tmpW = w;
		width = tmpW;
		tmpH = h;
		height = tmpH;
	}

	// x, y ��ġ���� mx, my ��ġ�� ��ź �߻�
	void shot(double enemyX, double enemyY, double shipX, double shipY) {
		if (state == ST_DEATH) {
			state = ST_ALIVE;
			this.x = enemyX;
			this.y = enemyY;
			dx = (shipX - x) / 50;
			dy = (shipY - y) / 50;
			if(dx < 0){
				width = -tmpW;
			} else {
				width = tmpW;
			}
			if(dy < 0){
				height = -tmpH;
			} else {
				height = tmpH;
			}
		}
	}

	// ���� ���� ����
	void blast() {
		state = ST_DEATH;
		x = ChopLifter.FRAME_W;
		y = ChopLifter.FRAME_H;
	}

	void move() {
		if (state == ST_ALIVE) {
			x += dx;
			y += dy;
			if (y < -40 || ChopLifter.FRAME_H + 40 < y || x < -40 || ChopLifter.FRAME_W + 40 < x) { // ȭ������γ����� ����
				state = ST_DEATH;
			}
		}
	}

	// �׸���
	void draw(Graphics g) {
		if (state == ST_ALIVE) {
			drawImage(g);
		}
	}
}