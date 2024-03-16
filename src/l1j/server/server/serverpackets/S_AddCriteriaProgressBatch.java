package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_AddCriteriaProgressBatch extends ServerBasePacket {
	private static final String S_ADD_CRITERIA_PROGRESS_BATCH = "[S] S_AddCriteriaProgressBatch";
	private byte[] _byte = null;
	public static final int BATCH = 0x0230;
	
	public S_AddCriteriaProgressBatch(int total_pages, int current_page, java.util.LinkedList<S_AddCriteriaProgressBatch.CriteriaProgress> criteria_progress) {
		write_init();
		write_total_pages(total_pages);
		write_current_page(current_page);
		if (criteria_progress != null && !criteria_progress.isEmpty()) {
			write_criteria_progress(criteria_progress);
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
	
	void write_criteria_progress(java.util.LinkedList<S_AddCriteriaProgressBatch.CriteriaProgress> criteria_progress) {
		for (S_AddCriteriaProgressBatch.CriteriaProgress val : criteria_progress) {
			writeRaw(0x1a);
			writeRaw(getBitSize(val._criteria_id) + getBitSize(val._quantity) + 2);
			
			writeRaw(0x08);// _criteria_id
			writeBit(val._criteria_id);
			
			writeRaw(0x10);// _quantity
			writeBit(val._quantity);
		}
	}
	
	public static class CriteriaProgress {
		private int _criteria_id;
		private long _quantity;
		public CriteriaProgress(int _criteria_id, long _quantity) {
			this._criteria_id = _criteria_id;
			this._quantity = _quantity;
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
		return S_ADD_CRITERIA_PROGRESS_BATCH;
	}
}

