package l1j.server.server.serverpackets.interracing;

import l1j.server.common.data.RacerInfoT;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_InterRacingMovingNoti extends ServerBasePacket {
	private static final String S_INTER_RACING_MOVING_NOTI = "[S] S_InterRacingMovingNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0901;
	
	public S_InterRacingMovingNoti(RacerInfoT racer){
		write_init();
		write_race(racer);
        writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_race(RacerInfoT race) {
		writeRaw(0x0a);
		writeBytesWithLength(get_RacerInfo(race));
	}
	
	byte[] get_RacerInfo(RacerInfoT race) {
		RacerInfoStream os = null;
		try {
			os = new RacerInfoStream(race);
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
		return S_INTER_RACING_MOVING_NOTI;
	}
}

