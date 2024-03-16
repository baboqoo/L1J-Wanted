package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionNotiType;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_TimeCollectionBuffNoti extends ServerBasePacket {
	private static final String S_TIME_COLLECTION_BUFF_NOTI = "[S] S_TimeCollectionBuffNoti";
	private byte[] _byte = null;
	public static final int BUFF_NOTI	= 0x0a6b;
	
	public S_TimeCollectionBuffNoti(L1TimeCollectionUser user, L1TimeCollectionNotiType notiType){
		write_init();
		write_notiType(notiType);
		write_groupId(user.getGroupId());
		write_setId(user.getSetId());
		write_buffCount(user.getBuffIndex());
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(BUFF_NOTI);
	}
	
	void write_notiType(L1TimeCollectionNotiType notiType) {
		writeRaw(0x08);// notiType
		writeRaw(notiType.toInt());
	}
	
	void write_groupId(int groupId) {
		writeRaw(0x10);// groupId
		writeRaw(groupId);
	}
	
	void write_setId(int setId) {
		writeRaw(0x18);// setId
		writeRaw(setId);
	}
	
	void write_buffCount(int buffCount) {
		writeRaw(0x20);// buffCount
		writeRaw(buffCount);
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
		return S_TIME_COLLECTION_BUFF_NOTI;
	}
}

