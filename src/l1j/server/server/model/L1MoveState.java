package l1j.server.server.model;

public class L1MoveState {
	private int _heading; // ● 방향 0. 좌상 1. 상 2. 우상 3. 오른쪽 4. 우하 5. 하 6. 좌하 7. 좌
	private int _moveSpeed; // ● 스피드 0. 통상 1. 헤이 파업 2. 슬로우
	private int _braveSpeed; // ● 치우침 이브 상태 0. 통상 1. 치우침 이브
	private int _drunken;
	private boolean _fourthGear;

	public int getHeading() {
		return _heading;
	}
	public void setHeading(int i) {
		_heading = i & 7;
	}

	public int getMoveSpeed() {
		return _moveSpeed;
	}
	public void setMoveSpeed(int i) {
		_moveSpeed = i;
	}

	public int getBraveSpeed() {
		return _braveSpeed;
	}
	public void setBraveSpeed(int i) {
		_braveSpeed = i;
	}
	
	public int getDrunken() {
		return _drunken;
	}
    public void setDrunken(int i) {
    	_drunken = i;
    }
    
    public boolean isFourthGear() {
    	return _fourthGear;
    }
    public void setFourthGear(boolean i) {
    	_fourthGear = i;
    }
}

