package l1j.server.server.templates;

public class L1CharSoldier {

	public L1CharSoldier(int charId) {
		_charId = charId;
	}

	private int _charId;

	public int getCharId() {
		return _charId;
	}

	public void setCharId(int i) {
		_charId = i;
	}

	private int _npcid;

	public int getSoldierNpc() {
		return _npcid;
	}

	public void setSoldierNpc(int i) {
		_npcid = i;
	}

	private int _count;

	public int getSoldierCount() {
		return _count;
	}

	public void setSoldierCount(int i) {
		_count = i;
	}

	private int _castleid;

	public int getSoldierCastleId() {
		return _castleid;
	}

	public void setSoldierCastleId(int i) {
		_castleid = i;
	}

	private int _time;

	public int getSoldierTime() {
		return _time;
	}

	public void setSoldierTime(int i) {
		_time = i;
	}
}
