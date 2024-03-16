package l1j.server.server.serverpackets.interracing;

import l1j.server.common.data.RacerInfoT;
import l1j.server.server.utils.BinaryOutputStream;

public class RacerInfoStream extends BinaryOutputStream {

	public RacerInfoStream(RacerInfoT race) {
		super();
		write_racerId(race.get_racerId());
		write_x(race.get_x());
		write_y(race.get_y());
		write_dir(race.get_dir());
		write_laneId(race.get_laneId());
	}
	
	void write_racerId(int racerId) {
		writeC(0x08);
		writeBit(racerId);
	}
	
	void write_x(int x) {
		writeC(0x10);
		writeBit(x);
	}
	
	void write_y(int y) {
		writeC(0x18);
		writeBit(y);
	}
	
	void write_dir(int dir) {
		writeC(0x20);
		writeC(dir);
	}
	
	void write_laneId(int laneId) {
		writeC(0x28);
		writeBit(laneId);
	}
}

