package l1j.server.server.serverpackets.system;

import l1j.server.GameSystem.freebuffshield.GoldenBuffInfo;
import l1j.server.server.utils.BinaryOutputStream;

public class GoldenBuffInfoStream extends BinaryOutputStream {
	
	protected GoldenBuffInfoStream(GoldenBuffInfo info) {
		super();
		write_index(info.getIndex());
		write_type(info.getType());
		write_grade(info.getGrade());
		write_remain_time(info.getRemainTime());
	}
	
	void write_index(int index) {
		writeC(0x08);
		writeC(index);
	}
	
	void write_type(int type) {
		writeC(0x10);
		writeC(type);
	}
	
	void write_grade(java.util.LinkedList<Integer> grade) {
		for (int val : grade) {
			writeC(0x18);
			writeC(val);
		}
	}
	
	void write_remain_time(int remain_time) {
		writeC(0x20);
		writeBit(remain_time);
	}
}

