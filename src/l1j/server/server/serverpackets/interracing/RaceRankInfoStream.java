package l1j.server.server.serverpackets.interracing;

import l1j.server.common.data.RaceRankInfoT;
import l1j.server.server.utils.BinaryOutputStream;

public class RaceRankInfoStream extends BinaryOutputStream {

	public RaceRankInfoStream(RaceRankInfoT raceRankInfo) {
		super();
		write_rank(raceRankInfo.get_rank());
		write_serverNumber(raceRankInfo.get_serverNumber());
		write_name(raceRankInfo.get_name());
		write_weeklyPrize(raceRankInfo.get_weeklyPrize());
	}
	
	void write_rank(int rank) {
		writeC(0x08);
		writeBit(rank);
	}
	
	void write_serverNumber(int serverNumber) {
		writeC(0x10);
		writeBit(serverNumber);
	}
	
	void write_name(String name) {
		writeC(0x1a);
		writeStringWithLength(name);
	}
	
	void write_weeklyPrize(int weeklyPrize) {
		writeC(0x20);
		writeBit(weeklyPrize);
	}
	
}

