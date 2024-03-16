package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_TimeCollectionRegistItem extends ServerBasePacket {
	private static final String S_TIME_COLLECTION_REGIST_ITEM = "[S] S_TimeCollectionRegistItem";
	private byte[] _byte = null;
	public static final int REGIST_ITEM	= 0x0a62;
	
	public S_TimeCollectionRegistItem(L1PcInstance pc, L1TimeCollectionUser user, int slotNo, L1ItemInstance item){
		write_init();
		write_result(user.isRegistComplet() ? S_TimeCollectionRegistItem.RegistResult.SUCCESS_SET_COMPLETE : S_TimeCollectionRegistItem.RegistResult.SUCCESS_NON_COMPLETE);
		write_buffType(user.isRegistComplet() ? user.getBuffType().toInt() : 0);
		write_groupID(user.getGroupId());
		write_setID(user.getSetId());
		write_slotNo(slotNo);
		write_nameId(item.getItem().getItemNameId());
		write_enchant(item.getEnchantLevel());
		write_extra_desc(item.getStatusBytes(pc));
		write_bless(item.getBless());
		if (user.getBuffTimer() != null) {
			write_completeTime((int) (user.getBuffTime().getTime() / 1000));
		}
		write_useRecycle(user.isRegistComplet() ? 1 : 0);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(REGIST_ITEM);
	}
	
	void write_result(S_TimeCollectionRegistItem.RegistResult result) {
		writeRaw(0x08);// result
		writeRaw(result.value);
	}
	
	void write_buffType(int buffType) {
		writeRaw(0x10);// buffType
		writeRaw(buffType);
	}
	
	void write_groupID(int groupID) {
		writeRaw(0x18);// groupID
		writeRaw(groupID);
	}
	
	void write_setID(int setID) {
		writeRaw(0x20);// setID
		writeRaw(setID);
	}
	
	void write_slotNo(int slotNo) {
		writeRaw(0x28);// slotNo
		writeRaw(slotNo);
	}
	
	void write_nameId(int nameId) {
		writeRaw(0x30);// nameId
		writeBit(nameId);
	}
	
	void write_enchant(int enchant) {
		writeRaw(0x38);// enchant
		writeBit(enchant);
	}
	
	void write_extra_desc(byte[] extra_desc) {
		writeRaw(0x42);// extra_desc
		writeBytesWithLength(extra_desc);
	}
	
	void write_bless(int bless) {
		writeRaw(0x48);// bless
		writeRaw(bless);
	}
	
	void write_completeTime(int completeTime) {
		writeRaw(0x50);// completeTime
		writeBit(completeTime);
	}
	
	void write_useRecycle(int useRecycle) {
		writeRaw(0x58);// useRecycle
		writeRaw(useRecycle);
	}
	
	public enum RegistResult{
		SUCCESS_NON_COMPLETE(0),
		SUCCESS_SET_COMPLETE(1),
		WAIT_SET_DATA(10),
		ERROR_INVALID_TARGET_ID(11),
		ERROR_INVALID_GROUP_ID(12),
		ERROR_INVALID_SET_ID(13),
		ERROR_INVALID_SLOT_NO(14),
		ERROR_INVALID_ITEM(15),
		ERROR_OVERLAP_SLOT(16),
		ERROR_RECYCLE_COUNT(17),
		ERROR_GROUP_TIME_END(18),
		ERROR_UNKNOWN(9999),
		;
		private int value;
		RegistResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(RegistResult v){
			return value == v.value;
		}
		public static RegistResult fromInt(int i){
			switch(i){
			case 0:
				return SUCCESS_NON_COMPLETE;
			case 1:
				return SUCCESS_SET_COMPLETE;
			case 10:
				return WAIT_SET_DATA;
			case 11:
				return ERROR_INVALID_TARGET_ID;
			case 12:
				return ERROR_INVALID_GROUP_ID;
			case 13:
				return ERROR_INVALID_SET_ID;
			case 14:
				return ERROR_INVALID_SLOT_NO;
			case 15:
				return ERROR_INVALID_ITEM;
			case 16:
				return ERROR_OVERLAP_SLOT;
			case 17:
				return ERROR_RECYCLE_COUNT;
			case 18:
				return ERROR_GROUP_TIME_END;
			case 9999:
				return ERROR_UNKNOWN;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eResult, %d", i));
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
		return S_TIME_COLLECTION_REGIST_ITEM;
	}
}

