package l1j.server.server.types;

public class Point {
	protected int _x = 0;	
	protected int _y = 0;

	public Point() {  }

	public Point(int x, int y) {
		_x = x;
		_y = y;
	}

	public Point(Point pt) {
		_x = pt._x;
		_y = pt._y;
	}

	public int getX() {
		return _x;
	}

	public void setX(int x) {
		_x = x;
	}

	public int getY() {
		return _y;
	}

	public void setY(int y) {
		_y = y;
	}

	public void set(Point pt) {
		_x = pt._x;
		_y = pt._y;
	}

	public void set(int x, int y) {
		_x = x;
		_y = y;
	}

	private static final int HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
	private static final int HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

	public void forward(int heading) {
		_x += HEADING_TABLE_X[heading];
		_y += HEADING_TABLE_Y[heading];
	}

	public void backward(int heading) {
		_x -= HEADING_TABLE_X[heading];
		_y -= HEADING_TABLE_Y[heading];
	}
	public int getHeading(Point target) {
		int xDiff = target.getX() - _x;
		int yDiff = target.getY() - _y;
		
		for (int i = 0; i < 8; ++i) {
			if (HEADING_TABLE_X[i] == xDiff && HEADING_TABLE_Y[i] == yDiff) {
				return i;
			}
		}
		return -1;
	}

	public double getLineDistance(Point pt) {
		long diffX = pt.getX() - this.getX();
		long diffY = pt.getY() - this.getY();
		return Math.sqrt((diffX * diffX) + (diffY * diffY));
	}
	
	public int getTileLineDistance(int x, int y) {
		return Math.max(Math.abs(x - getX()), Math.abs(y - getY()));
	}

	public int getTileLineDistance(Point pt) {
		return Math.max(Math.abs(pt.getX() - getX()), Math.abs(pt.getY() - getY()));
	}

	public int getTileDistance(Point pt) {
		return Math.abs(pt.getX() - getX()) + Math.abs(pt.getY() - getY());
	}
	
	public boolean isInScreen(Point pt) {
		int dist = this.getTileDistance(pt);
		if (dist > 22) {
			return false;
		} 
		if (dist <= 19) {
			return true;
		}
		int dist2 = Math.abs(pt.getX() - (this.getX() - 20))
				+ Math.abs(pt.getY() - (this.getY() - 20));
		if (23 <= dist2 && dist2 <= 56) {
			return true;
		}
		return false;
	}

	public boolean isSamePoint(Point pt) {
		return (pt.getX() == getX() && pt.getY() == getY());
	}

	@Override
	public int hashCode() {
		return 7 * getX() + getY();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Point)) {
			return false;
		}
		Point pt = (Point) obj;
		return (this.getX() == pt.getX()) && (this.getY() == pt.getY());
	}

	@Override
	public String toString() {
		return String.format("(%d, %d)", _x, _y);
	}
}

