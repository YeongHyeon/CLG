package chopLifter;

import java.awt.Color;

public class Util {
	public static double rand(int max) {
		return Math.random() * max;
	}

	public static double rand(double min, double max) { // min~max�������� ������ ����.
		return min + (Math.random() * (max - min));
	}

	public static boolean prob100(double r) { // ���������� ���� r���� ������� true.
		return (Math.random() * 100) <= r;
	}

	public static Color randColor() { // 0~255������ ������ ����.
		return randColor(0, 255);
	}

	public static Color randColor(int min, int max) { // ������ ���� ���� ������ ����.
		int r = (int) rand(min, max);
		int g = (int) rand(min, max);
		int b = (int) rand(min, max);
		return new Color(r, g, b);
	}
	
	public static int randColorElement(int min, int max) {
		// TODO Auto-generated method stub
		return (int) rand(min, max);
	}
}