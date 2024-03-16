package l1j.server.server.utils;

public class Dice {
	private final int _faces;

	public Dice(int faces) {
		_faces = faces;
	}

	public int getFaces() {
		return _faces;
	}

	public int roll() {
		return CommonUtil.random(_faces) + 1;
	}

	public int roll(int count) {
		int n = 0;
		for (int i = 0; i < count; i++) {
			n += roll();
		}
		return n;
	}
}

