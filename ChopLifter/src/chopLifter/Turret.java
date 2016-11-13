package chopLifter;

import java.awt.*;

public class Turret extends GameObj {

	private Image[] img = new Image[3];
	private double heliX;
	private int poro_num;

	// 생성자
	Turret(Image[] imgTurret, double tx, int w, int h) {
		img = imgTurret;
		state = ST_ALIVE;
		this.x = tx;
		this.y = ChopLifter.FRAME_H / 5 * 4 - 10;
		width = w;
		height = h;
		image = img[0];
		poro_num = 0;
	}

	void birth() {
		state = ST_ALIVE;
		image = img[0];
		poro_num = 0;
	}

	void blast() {
		state = ST_BLAST;
		blast_count = 15;
	}

	void setPosin(double d) {
		heliX = d;
	}

	void setPoro() {
		poro_num++;
	}

	int getPoro() {
		return poro_num;
	}

	void move() {
		if (state == ST_ALIVE) {
			if (heliX > x) {
				image = img[0];
			} else {
				image = img[1];
			}
		} else if (state == ST_BLAST) {
			blast_count--;
			if (blast_count == 0) {
				state = ST_DEATH;
			}
		} else if (state == ST_DEATH) {
			image = img[2];
		}
	}

	void draw(Graphics g) {
		if (state == ST_ALIVE) {
			drawImage(g);
		} else if (state == ST_BLAST) {
			if (blast_count % 2 == 0) {
				drawImage(g);
			}
			drawBlast(g);
		} else if (state == ST_DEATH) {
			drawImage(g);
		}
	}

}
