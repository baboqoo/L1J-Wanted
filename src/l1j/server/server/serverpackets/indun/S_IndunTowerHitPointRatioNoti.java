package l1j.server.server.serverpackets.indun;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunTowerHitPointRatioNoti extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_TOWER_HIT_POINT_RATIO_NOTI = "[S] S_IndunTowerHitPointRatioNoti";
	public static final int NOTI = 0x08bd;

	public S_IndunTowerHitPointRatioNoti(int object_id, int hp_ratio) {
		write_init();
		write_object_id(object_id);
		write_hp_ratio(hp_ratio);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_object_id(int object_id) {
		writeRaw(0x08);// object_id
		writeBit(object_id);
	}
	
	void write_hp_ratio(int hp_ratio) {
		writeRaw(0x10);// hp_ratio
		writeBit(hp_ratio);
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
		return S_INDUN_TOWER_HIT_POINT_RATIO_NOTI;
	}
}
