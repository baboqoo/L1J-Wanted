package l1j.server.server.types;

/** 좌표 좌상의 점(left, top), 및 우하의 점(right, bottom)에 의해 둘러싸이는 좌표의 area를 지정하는 클래스. */
public class Rectangle {
	private int _left;
	private int _top;
	private int _right;
	private int _bottom;

	public Rectangle(Rectangle rect) {
		set(rect);
	}

	public Rectangle(int left, int top, int right, int bottom) {
		set(left, top, right, bottom);
	}

	public Rectangle() {
		this(0, 0, 0, 0);
	}

	public void set(Rectangle rect) {
		set(rect.getLeft(), rect.getTop(), rect.getWidth(), rect.getHeight());
	}

	public void set(int left, int top, int right, int bottom) {
		_left	= left;
		_top	= top;
		_right	= right;
		_bottom	= bottom;
	}

	public int getLeft() {
		return _left;
	}

	public int getTop() {
		return _top;
	}

	public int getRight() {
		return _right;
	}

	public int getBottom() {
		return _bottom;
	}

	public int getWidth() {
		return _right - _left;
	}

	public int getHeight() {
		return _bottom - _top;
	}

	/**
	 * 지정된 점(x, y)이, 이 Rectangle의 범위내에 있는지를 판정한다.
	 * 
	 * @param x
	 *            판정하는 점의 X좌표
	 * @param y
	 *            판정하는 점의 Y좌표
	 * @return 점(x, y)이 이 Rectangle의 범위내에 있는 경우, true.
	 */
	public boolean contains(int x, int y) {
		return (_left <= x && x <= _right) && (_top <= y && y <= _bottom);
	}

	/**
	 * 지정된 Point가, 이 Rectangle의 범위내에 있는지를 판정한다.
	 * 
	 * @param pt
	 *            판정하는 Point
	 * @return pt가 이 Rectangle의 범위내에 있는 경우, true.
	 */
	public boolean contains(Point pt) {
		return contains(pt.getX(), pt.getY());
	}
}

