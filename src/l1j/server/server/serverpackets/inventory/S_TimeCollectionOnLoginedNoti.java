package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.BinaryOutputStream;

public class S_TimeCollectionOnLoginedNoti extends ServerBasePacket {
	private static final String S_TIME_COLLECTION_ON_LOGINED_NOTI = "[S] S_TimeCollectionOnLoginedNoti";
	private byte[] _byte = null;
	public static final int NOTI	= 0x0a6c;
	
	public S_TimeCollectionOnLoginedNoti(java.util.LinkedList<S_TimeCollectionOnLoginedNoti.setData> noti_setData){
		write_init();
		for (S_TimeCollectionOnLoginedNoti.setData setData : noti_setData) {
			write_noti_setData(setData);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_noti_setData(S_TimeCollectionOnLoginedNoti.setData setData) {
		writeRaw(0x0a);
		writeBytesWithLength(setData.getBytes());
		setData.clear();
		setData = null;
	}
	
	public static class setData extends BinaryOutputStream {
		public setData(int groupId, int setId, int remainTime, int remainRefil) {
			super();
			write_groupId(groupId);
			write_setId(setId);
			write_remainTime(remainTime);
			if (remainRefil > 0) {
				write_remainRefil(remainRefil);
			}
		}
		
		void write_groupId(int groupId) {
			writeC(0x08);
			writeC(groupId);
		}
		
		void write_setId(int setId) {
			writeC(0x10);
			writeC(setId);
		}
		
		void write_remainTime(int remainTime) {
			writeC(0x18);
			writeBit(remainTime);
		}
		
		void write_remainRefil(int remainRefil) {
			writeC(0x20);
			writeC(remainRefil);
		}
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
		return S_TIME_COLLECTION_ON_LOGINED_NOTI;
	}
}

