package chopLifter;

import java.awt.Graphics;
import java.awt.Image;

public class Missile extends GameObj {
	private Image[] img = new Image[2];
	private int hDir, shotDir;
	private int tmp_counter, inertia_counter; // 관성카운터
	private boolean drop;

	// 생성자
	Missile(Image[] imgMissile, int w, int h) {
		img = imgMissile;
		state = ST_DEATH;
		tmpW = w;
		tmpH = h;
	}

	// x, y 위치에서 mx, my 위치로 폭탄 발사
	void shot(double shipX, double shipY, int hDir, int d, int degree) {
		if (state == ST_DEATH) {
			state = ST_ALIVE;
			this.x = shipX;
			this.y = shipY;
			this.hDir = hDir;
			shotDir = d;
			dx = 0;
			dy = 0;
			tmp_counter = 10;
			drop = true;

			init();
		}
	}

	void init() {
		if (Math.abs(degree) < 5) {
			image = img[0];
			if (hDir > 0) { // 방향에 따라 이미지의 좌우를 대칭시킨다.
				width = tmpW;
			} else {
				width = -tmpW;
			}
			height = tmpH;
		} else {
			image = img[1];
			if (hDir > 0) { // 방향에 따라 이미지의 좌우를 대칭시킨다.
				width = tmpW + 5;
			} else {
				width = -tmpW + 5;
			}
			height = tmpH + 5;
		}
	}

	void setInertia() {
		inertia_counter = 60;
	}

	void blast() {
		state = ST_BLAST;
		blast_count = 10;
		x = -10;
		y = -10;
	}

	void move() { // Target을 잃었을 경우 실행
		if (state == ST_ALIVE) {
			if (shotDir == 0) { // 전방 발사
				if (hDir > 0) {
					dx--;
				} else {
					dx++;
				}
				dy += 0.1;
			} else if (shotDir == 1) { // 떨어뜨리기
				if (inertia_counter <= 0) { // 관성이 없을때
					// NOP
				} else { // 관성이 있을때 진행방향으로 떨어짐
					inertia_counter += 2;
					if (hDir > 0) {
						dx = -1;
						width = tmpW;
					} else {
						dx = 1;
						width = -tmpW;
					}
				}
				dy++;
			}
			x += dx;
			y += dy;
			inertia_counter--;

			if (x < -tmpW || x > ChopLifter.FRAME_W + tmpW || y < -height) {
				state = ST_DEATH;
				inertia_counter = 0;
			}
			if (y >= ((ChopLifter.FRAME_H / 5 * 4) + (ChopLifter.FRAME_H / 5 / 2))) {
				inertia_counter = 0;
				blast();
			}
		} else if (state == ST_BLAST) {
			blast_count--;
			if (blast_count == 0) {
				state = ST_DEATH;
			}
		}
	}

	// 그리기
	void draw(Graphics g) {
		if (state == ST_ALIVE) {
			drawImage(g);
		} else if (state == ST_BLAST) {
			drawSmallBlast(g);
		}
	}
}