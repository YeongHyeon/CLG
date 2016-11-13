package chopLifter;

import java.awt.Graphics;
import java.awt.Image;

public class HelicopterBase extends GameObj {

	HelicopterBase(Image img, int w, int h) {
		image = img;
		state = ST_ALIVE;
//		x = ChopLifter.FRAME_W - (w / 2) - 50;
		x = ChopLifter.FRAME_W - 130;
		y = ChopLifter.FRAME_H / 5 * 4 + (h / 2);
		width = w;
		height = h;
	}

	// 폭발 상태 설정
	void blast() {
		state = ST_BLAST;
		blast_count = 15;
	}

	void move() {
		// ALIVE 상태에서는 좌우로 이동
		if (state == ST_ALIVE) {

		}
		// BLAST 상태에서는 count 시간 후 DEATH로 설정
		else if (state == ST_BLAST) {
			blast_count--;
			if (blast_count == 0) {
				state = ST_DEATH;
			}
		}
	}

	void draw(Graphics g) {
		if (state == ST_ALIVE) {
			drawTiltImage(g);
		} else if (state == ST_BLAST) {
			drawImage(g);
		}
	}
}