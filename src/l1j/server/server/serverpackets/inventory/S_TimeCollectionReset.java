package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_TimeCollectionReset extends ServerBasePacket {
	private static final String S_TIME_COLLECTION_RESET = "[S] S_TimeCollectionReset";
	private byte[] _byte = null;
	public static final int RESET	= 0x0a64;
	
	public S_TimeCollectionReset(L1TimeCollectionUser user){
		write_init();
		write_result(0);
		write_groupID(user.getGroupId());
		write_setID(user.getSetId());
		write_setData(user);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(RESET);
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
	
	void write_setData(L1TimeCollectionUser user) {
		TimeCollectionSetDataStream os = null;
		try {
			os = new TimeCollectionSetDataStream();
			os.write_setId(user.getSetId());
			os.write_useRecycle(user.getBuffIndex());
			os.write_buffType(user.getBuffType().toInt());
			os.write_completeTime(0);
			os.write_enchantSum(0);
			os.write_state(TIME_COLLECTION_STATE.TIME_COLLECTION_NONE);
			if (user.getObj().getSet().get_ExtraTimeId() > 0) {
				os.write_useRefill(user.getRefillCount());
			}
			writeRaw(0x22);// setData
			writeBytesWithLength(os.getBytes());
		} catch(Exception e) {
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
		return S_TIME_COLLECTION_RESET;
	}
}

