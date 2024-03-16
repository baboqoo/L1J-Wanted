package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_AddCompletedAchievementBatch extends ServerBasePacket {
	private static final String S_ADD_COMPLETED_ACHIEVEMENT_BATCH = "[S] S_AddCompletedAchievementBatch";
	private byte[] _byte = null;
	public static final int BATCH = 0x022f;
	
	public S_AddCompletedAchievementBatch(int total_pages, int current_page, java.util.LinkedList<S_AddCompletedAchievementBatch.CompletedAchievement> completed_achievmenet) {
		write_init();
		write_total_pages(total_pages);
		write_current_page(current_page);
		if (completed_achievmenet != null && !completed_achievmenet.isEmpty()) {
			write_completed_achievmenet(completed_achievmenet);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(BATCH);
	}
	
	void write_total_pages(int total_pages) {
		writeRaw(0x08);// total_pages
		writeBit(total_pages);
	}
	
	void write_current_page(int current_page) {
		writeRaw(0x10);// current_page
		writeBit(current_page);
	}
	
	void write_completed_achievmenet(java.util.LinkedList<S_AddCompletedAchievementBatch.CompletedAchievement> completed_achievmenet) {
		for (S_AddCompletedAchievementBatch.CompletedAchievement val : completed_achievmenet) {
			writeRaw(0x1a);
			writeRaw(getBitSize(val._achievement_id) + getBitSize(val._completed_time) + 4);
			
			writeRaw(0x08);// achievement_id
			writeBit(val._achievement_id);
			
			writeRaw(0x10);// completed_time
			writeBit(val._completed_time);
			
			writeRaw(0x18);// get_reward
			writeB(val._get_reward);
		}
	}
	
	public static class CompletedAchievement {
		private int _achievement_id;
		private long _completed_time;
		private boolean _get_reward;
		public CompletedAchievement(int _achievement_id, long _completed_time, boolean _get_reward) {
			this._achievement_id = _achievement_id;
			this._completed_time = _completed_time;
			this._get_reward = _get_reward;
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
	
	@Override
	public String getType() {
		return S_ADD_COMPLETED_ACHIEVEMENT_BATCH;
	}
}

