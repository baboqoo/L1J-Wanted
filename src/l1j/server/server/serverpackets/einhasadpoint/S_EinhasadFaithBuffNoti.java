package l1j.server.server.serverpackets.einhasadpoint;

import l1j.server.GameSystem.einhasadfaith.bean.EinhasadFaithInfo;
import l1j.server.common.bin.EinhasadPointFaithCommonBinLoader;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_EinhasadFaithBuffNoti extends ServerBasePacket {
	private static final String S_EINHASAD_FAITH_BUFF_NOTI = "[S] S_EinhasadFaithBuffNoti";
	private byte[] _byte = null;
	public static final int BUFF	= 0x0a88;
	
	public S_EinhasadFaithBuffNoti(S_EinhasadFaithBuffNoti.eNotiType noti_type, java.util.LinkedList<EinhasadFaithInfo> infos) {
		write_init();
		write_noti_type(noti_type);
		write_tooltip_str_id(EinhasadPointFaithCommonBinLoader.getBuffInfoT().get_tooltipStrId());
		long currentTime = System.currentTimeMillis();
		for (EinhasadFaithInfo info : infos) {
			if (info.getIndexId() == 0 || info.getExpiredTime() == null || info.getExpiredTime().getTime() <= currentTime) {
				continue;
			}
			write_enable_indexId(info.getIndexId());
		}
		for (EinhasadFaithInfo info : infos) {
			if (info.getIndexId() > 0 || info.getExpiredTime() == null || info.getExpiredTime().getTime() <= currentTime) {
				continue;
			}
			write_enable_groupId(info.getGroupId());
		}
		writeH(0x00);
	}
	
	public S_EinhasadFaithBuffNoti(S_EinhasadFaithBuffNoti.eNotiType noti_type, EinhasadFaithInfo info) {
		write_init();
		write_noti_type(noti_type);
		write_tooltip_str_id(EinhasadPointFaithCommonBinLoader.getBuffInfoT().get_tooltipStrId());
		if (info.getIndexId() > 0) {
			write_enable_indexId(info.getIndexId());
		} else {
			write_enable_groupId(info.getGroupId());
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(BUFF);
	}
	
	void write_noti_type(S_EinhasadFaithBuffNoti.eNotiType noti_type) {
		writeRaw(0x08);
		writeRaw(noti_type.value);
	}
	
	void write_tooltip_str_id(int tooltip_str_id) {
		writeRaw(0x10);
		writeBit(tooltip_str_id);
	}
	
	void write_enable_indexId(int enable_indexId) {
		writeRaw(0x18);
		writeBit(enable_indexId);
	}
	
	void write_enable_groupId(int enable_groupId) {
		writeRaw(0x20);
		writeBit(enable_groupId);
	}
	
	public enum eNotiType {
		NEW(1),
		RESTART(2),
		END(3),
		;
		private int value;
		eNotiType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eNotiType v){
			return value == v.value;
		}
		public static eNotiType fromInt(int i){
			switch(i){
			case 1:
				return NEW;
			case 2:
				return RESTART;
			case 3:
				return END;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments S_EinhasadFaithBuffNoti.eNotiType, %d", i));
			}
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
		return S_EINHASAD_FAITH_BUFF_NOTI;
	}
}

