package chopLifter;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;

@SuppressWarnings("serial")
class ChopLifterComponent extends JComponent {
	public static int TIME_SLICE = 50;
	public static int MAX_CLOUD = 10;
	public static int MAX_MOUNTAIN = 30;
	public static int MAX_ENEMY_PLANE = 3;
	public static int MAX_PERSON = 10;
	public static int MAX_TURRET = 5;

	private Timer t;
	private Cloud[] cloud = new Cloud[MAX_CLOUD];
	private Mountain[] mountain = new Mountain[MAX_MOUNTAIN];
	private Helicopter heli;
	private EnemyPlane[] enemyPlane = new EnemyPlane[MAX_ENEMY_PLANE];
	private Missile missile;
	private Bomb bomb;
	Image imgCloud, imgMountain, imgEnemyPlane, imgPlaneBomb;
	Image[] imgHelicopter = new Image[2];
	Image[] imgMissile = new Image[2];

	ChopLifterComponent() {

		// 이미지 읽기
		try {
			imgCloud = ImageIO.read(new File("images/cloud.png"));
			imgMountain = ImageIO.read(new File("images/mountain.png"));
			imgHelicopter[0] = ImageIO.read(new File("images/ahpach1.png"));
			imgHelicopter[1] = ImageIO.read(new File("images/ahpach2.png"));
			imgEnemyPlane = ImageIO.read(new File("images/enemyPlane.png"));
			imgMissile[0] = ImageIO.read(new File("images/missile1.png"));
			imgMissile[1] = ImageIO.read(new File("images/missile2.png"));
			imgPlaneBomb = ImageIO.read(new File("images/bomb.png"));
			System.out.println("ImageRead");
		} catch (IOException e) {
			System.out.println("IOException Exit Program");
			System.exit(-1);
		}

		// 객체 초기화
		heli = new Helicopter(imgHelicopter, 120, 70);
		for (int i = 0; i < MAX_CLOUD; i++) {
			cloud[i] = new Cloud(imgCloud);
		}

		double mx = ChopLifter.Left_End_X;
		for (int i = 0; i < MAX_MOUNTAIN; i++) {
			mountain[i] = new Mountain(imgMountain, mx);
			mx += Util.rand(50, ChopLifter.FRAME_W / 3);
		}
		for (int i = 0; i < MAX_ENEMY_PLANE; i++) {
			enemyPlane[i] = new EnemyPlane(imgEnemyPlane, 80, 40);
		}
		missile = new Missile(imgMissile, 30, 15);
		bomb = new Bomb(imgPlaneBomb, 30, 30);

		// 키 이벤트 등록
		this.addKeyListener(new KeyHandler());
		this.setFocusable(true);
		// 타이머 등록
		t = new Timer(TIME_SLICE, new TimerHandler());
		t.start();
		System.out.println("Timer Start");

		this.addKeyListener(new KeyHandler());
		this.requestFocus();
		System.out.println("KeyHandler Run");
	}

	class TimerHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			heli.move();

			for (Cloud c : cloud) {
				c.move();
			}
			for (Mountain m : mountain) {
				m.move();
			}

			for (EnemyPlane ep : enemyPlane) {
				if (ep.getState() == EnemyPlane.ST_DEATH && Util.prob100(10)) {
					ep.birth(heli.getX());
				}
				if (ep.getState() == EnemyPlane.ST_ALIVE && Util.prob100(1) && !heli.isLanded() && !heli.isLanding()) {
					// bomb.shot(ep.getX(), ep.getY(), heli.getX(),
					// heli.getY());
				}

				if (ep.getState() == EnemyPlane.ST_ALIVE && heli.getState() == heli.ST_ALIVE) {
					if (heli.getBBox().intersects(ep.getBBox())) {
						heli.blast();
						ep.blast();
					}
				}
				ep.move();
			}

			missile.move();
			for (EnemyPlane ep : enemyPlane) {
				if (missile.getState() == Missile.ST_ALIVE) {
					if (ep.getState() == EnemyPlane.ST_ALIVE) {
						if (ep.getBBox().intersects(missile.getBBox())) {
							ep.blast();
							missile.blast();
							break; // 하나 터지면 탈출
						}
					}
				}
			}

			bomb.move();
			// 폭탄 충돌처리
			if (bomb.getState() == Bomb.ST_ALIVE) {
				if (heli.getState() == Helicopter.ST_ALIVE) {
					if (heli.getBBox().intersects(bomb.getBBox())) {
						heli.blast();
						bomb.blast();
					}
				}
			}

			shiftBackGround();

			// 전체 다시 그리기
			repaint();
		}
	}

	void shiftBackGround() {
		double absX = heli.getAbsoluteX();
		if (heli.directionX == Helicopter.GO_LEFT) {
			if (absX >= (ChopLifter.Right_End_X - ChopLifter.FRAME_W / 2)) {
				// NOP
			} else {
				if (absX <= (ChopLifter.Left_End_X + ChopLifter.FRAME_W / 2)) {
					// NOP
				} else {
					for (Mountain m : mountain) {
						m.setShift(heli.getDegree()); // 산이 움직이는 속도는 나중에 더 느리게 수정
					}
					for (EnemyPlane ep : enemyPlane) {
						ep.setShift(heli.getDegree()); 
					}
				}
			}
		} else if (heli.directionX == Helicopter.GO_RIGHT) {
			if (absX <= (ChopLifter.Left_End_X + ChopLifter.FRAME_W / 2)) {
				// NOP
			} else {
				if (absX >= (ChopLifter.Right_End_X - ChopLifter.FRAME_W / 2)) {
					// NOP
				} else {
					for (Mountain m : mountain) {
						m.setShift(heli.getDegree());
					}
					for (EnemyPlane ep : enemyPlane) {
						ep.setShift(heli.getDegree()); 
					}
				}
			}
		}
		if (absX > (ChopLifter.Left_End_X + ChopLifter.FRAME_W / 2)
				&& absX < (ChopLifter.Right_End_X - ChopLifter.FRAME_W / 2)) {
			heli.setFixX();
		}
	}

	class KeyHandler extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int code = e.getKeyCode();

			// 키 입력 처리 - - -
			if (code == KeyEvent.VK_UP) {
				heli.moveUp();
				// System.out.println("UP");
			} else if (code == KeyEvent.VK_DOWN && !heli.isLanding()) {
				heli.moveDown();
				// System.out.println("DOWN");
			} else if (code == KeyEvent.VK_LEFT && !heli.isLanded()) {
				if (heli.getDegree() <= 0 && heli.getAbsoluteX() > ChopLifter.Left_End_X + 120) {
					heli.moveLeft();
					missile.setInertia();
					// shiftBackGround();
					// System.out.println("LEFT");
				}
			} else if (code == KeyEvent.VK_RIGHT && !heli.isLanded()) {
				if (heli.getDegree() >= 0 && heli.getAbsoluteX() < ChopLifter.Right_End_X - 120) {
					heli.moveRight();
					missile.setInertia();
					// shiftBackGround();
					// System.out.println("RIGHT");
				}
			} else if (e.getKeyChar() == 'z') {
				missile.shot(heli.getX(), heli.getY(), heli.directionX, 0, heli.getDegree());
			} else if (e.getKeyChar() == 'x' && !heli.isLanded()) {
				missile.shot(heli.getX(), heli.getY(), heli.directionX, 1, heli.getDegree());
			}
			// 전체 다시 그리기
			repaint();
		}

	}

	public void paintComponent(Graphics g) {
		// 하늘
		g.setColor(new Color(0, 188, 255));
		g.fillRect(0, 0, ChopLifter.FRAME_W, ChopLifter.FRAME_H);
		// 구름
		for (Cloud c : cloud) {
			c.draw(g);
		}
		// 산
		for (Mountain m : mountain) {
			m.draw(g);
		}
		// 땅
		g.setColor(new Color(255, 255, 81));
		g.fillRect(0, ChopLifter.FRAME_H / 5 * 4, ChopLifter.FRAME_W, ChopLifter.FRAME_H);
		g.setColor(new Color(255, 220, 81));
		g.fillRect(0, ChopLifter.FRAME_H / 5 * 4 + 10, ChopLifter.FRAME_W, ChopLifter.FRAME_H);
		g.setColor(new Color(255, 200, 81));
		g.fillRect(0, ChopLifter.FRAME_H / 5 * 4 + 30, ChopLifter.FRAME_W, ChopLifter.FRAME_H);
		g.setColor(new Color(255, 188, 81));
		g.fillRect(0, ChopLifter.FRAME_H / 5 * 4 + 60, ChopLifter.FRAME_W, ChopLifter.FRAME_H);

		bomb.draw(g);
		for (EnemyPlane ep : enemyPlane) {
			ep.draw(g);
		}
		missile.draw(g);

		heli.draw(g);
	}
}

public class ChopLifter {
	public static int FRAME_W = 1000;
	public static int FRAME_H = 562;
	public static int Left_End_X = -2000;
	public static int Right_End_X = 1000;

	public static void main(String[] arg) {
		System.out.println("Start Program");
		JFrame f = new JFrame("ChopLifter_박영현,정도환");
		f.setSize(FRAME_W + 16, FRAME_H + 34);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ChopLifterComponent sc = new ChopLifterComponent();
		f.add(sc);
		f.setVisible(true);
	}
}
