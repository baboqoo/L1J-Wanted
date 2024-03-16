package l1j.server.GameSystem.huntingquest.user;

public class HuntingQuestUserTemp {
	private int id;
	private int user_obj_id;
	private int map_number;
	private int location_desc;
	private int quest_id;
	private int kill_count;
	private boolean complete;
	
	public HuntingQuestUserTemp(int id, int user_obj_id, int map_number, int location_desc, int quest_id, int kill_count, boolean complete) {
		this.id				= id;
		this.user_obj_id	= user_obj_id;
		this.map_number		= map_number;
		this.location_desc	= location_desc;
		this.quest_id		= quest_id;
		this.kill_count		= kill_count;
		this.complete		= complete;
	}

	public int getId() {
		return id;
	}

	public int getUserObjid() {
		return user_obj_id;
	}
	
	public int getMapNumber() {
		return map_number;
	}

	public int getLocationDesc() {
		return location_desc;
	}

	public int getQuestId() {
		return quest_id;
	}

	public int getKillCount() {
		return kill_count;
	}

	public void setKillCount(int kill_count) {
		this.kill_count = kill_count;
	}
	
	public void addKillCount(int kill_count) {
		this.kill_count += kill_count;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}
}

