package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_TimeCollectionSetDataNoti extends ServerBasePacket {
	private static final String S_TIME_COLLECTION_SET_DATA_NOTI = "[S] S_TimeCollectionSetDataNoti";
	private byte[] _byte = null;
	public static final int SET_DATA	= 0x0a60;
	
	public S_TimeCollectionSetDataNoti(L1PcInstance pc, L1TimeCollectionUser user){
		write_init();
		write_groupId(user.getGroupId());
		write_setId(user.getSetId());
		write_setData(pc, user);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SET_DATA);
	}
	
	void write_groupId(int groupId) {
		writeRaw(0x08);// groupId
		writeRaw(groupId);
	}
	
	void write_setId(int setId) {
		writeRaw(0x10);// setId
		writeRaw(setId);
	}
	
	void write_setData(L1PcInstance pc, L1TimeCollectionUser user) {
		TimeCollectionSetDataStream os = null;
		try {
			os = new TimeCollectionSetDataStream(pc, user.getObj(), user);
			writeRaw(0x1a);// setData
			writeBytesWithLength(os.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
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
		return S_TIME_COLLECTION_SET_DATA_NOTI;
	}
}

