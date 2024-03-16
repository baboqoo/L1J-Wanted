package l1j.server.server.serverpackets.interracing;

import l1j.server.common.data.RaceRankInfoT;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_InterRacingRankInfo extends ServerBasePacket {
	private static final String S_INTER_RACING_RANK_INFO = "[S] S_InterRacingRankInfo";
	private byte[] _byte = null;
	public static final int NOTI = 0x0906;
	
	public S_InterRacingRankInfo(int raceKind, int raceTrack, java.util.LinkedList<RaceRankInfoT> rankInfo){
		write_init();
		write_raceKind(raceKind);
		write_raceTrack(raceTrack);
		write_rankInfo(rankInfo);
        writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_raceKind(int raceKind) {
		writeRaw(0x08);
		writeRaw(raceKind);
	}
	
	void write_raceTrack(int raceTrack) {
		writeRaw(0x10);
		writeRaw(raceTrack);
	}
	
	void write_rankInfo(java.util.LinkedList<RaceRankInfoT> rankInfo) {
		if (rankInfo == null || rankInfo.isEmpty()) {
			return;
		}
		for (RaceRankInfoT val : rankInfo) {
			writeRaw(0x1a);
			writeBytesWithLength(get_rankInfo(val));
		}
	}
	
	byte[] get_rankInfo(RaceRankInfoT rankInfo) {
		RaceRankInfoStream os = null;
		try {
			os = new RaceRankInfoStream(rankInfo);
			return os.getBytes();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_INTER_RACING_RANK_INFO;
	}
}

