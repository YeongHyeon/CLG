package chopLifter;

import java.awt.Graphics;
import java.awt.Image;

public class Person extends GameObj {
	private Image[] imgP = new Image[2];
	private int p_counter, pointer;
	private int life_cycle;
	double initX;

	Person(Image img[], int w, int h) {
		imgP = img;
		state = ST_DEATH; // 처음 만들 때 Death상태로 생성함.
		tmpW = w;
		tmpH = h;
		width = w;
		height = h;
	}

	void birth(double tx, double ty) { // 이동방향, 이동속도, 출발높이, 전환지점을 지정함.
		state = ST_ALIVE;
		initX = tx;
		x = tx;
		y = ty + 30;
		width = tmpW;
		height = tmpH;
		dx = Util.rand(1, 3);
		if (Util.prob100(50)) {
			dx *= -1;
		}
		life_cycle = 200;
	}

	// 폭발 상태 설정
	void blast() {
		state = ST_BLAST;
		blast_count = 15;
	}

	void shakeBody() {
		p_counter++;
		if (p_counter % 10 == 0) {
			pointer++;
		}
	}

	void setShiftInitX(double gx) {
		initX -= gx;
	}

	void move() {
		shakeBody();
		// ALIVE 상태에서는 좌우로 이동
		if (state == ST_ALIVE) {
			image = imgP[pointer % 2];
			x += dx;
			if (Math.abs(initX - x) > 100) {
				dx *= -1;
				x += dx;
			}
			life_cycle--;
			if (life_cycle <= 0) {
				blast();
			}
		}
		// BLAST 상태에서는 count 시간 후 DEATH로 설정
		else if (state == ST_BLAST) {
			image = imgP[1];
			blast_count--;
			degree-=6;
			if (blast_count == 0) {
				state = ST_DEATH;
			}
		}
	}

	boolean boarding(double hx, double hy) {
		if (state == ST_ALIVE) {
			shakeBody();
			if (x < hx) {
				x += 2;
			} else if (x <= hx + 2 && x >= hx - 2) {

			} else {
				x -= 2;
			}

			if (y < hy) {
				y += 2;
			} else if (y < ChopLifter.FRAME_H - 90) {

			} else {
				y -= 2;
			}

			if (x <= hx + 2 && x >= hx - 2 && y < ChopLifter.FRAME_H - 90) {
				state = ST_DEATH;
				return true;
			}
		}
		return false;
	}

	void draw(Graphics g) {
		if (state == ST_ALIVE) {
			drawImage(g);
		} else if (state == ST_BLAST) {
//			drawImage(g);
			drawTiltImage(g);
		}
	}
}