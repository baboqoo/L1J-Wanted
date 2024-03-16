package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_TimeCollectionChangeBuff extends ServerBasePacket {
	private static final String S_TIME_COLLECTION_CHANGE_BUFF = "[S] S_TimeCollectionChangeBuff";
	private byte[] _byte = null;
	public static final int CHANGE_BUFF	= 0x0a68;
	
	public S_TimeCollectionChangeBuff(L1TimeCollectionUser user){
		write_init();
		write_result(0);
		write_groupID(user.getGroupId());
		write_setID(user.getSetId());
		write_buffType(user.getBuffType().toInt());
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CHANGE_BUFF);
	}
	
	void write_result(int result) {
		writeRaw(0x08);// result
		writeRaw(result);
	}
	
	void write_groupID(int groupID) {
		writeRaw(0x10);// groupID
		writeRaw(groupID);
	}
	
	void write_setID(int setID) {
		writeRaw(0x18);// setID
		writeRaw(setID);
	}
	
	void write_buffType(int buffType) {
		writeRaw(0x20);// buffType
		writeRaw(buffType);
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
		return S_TIME_COLLECTION_CHANGE_BUFF;
	}
}

