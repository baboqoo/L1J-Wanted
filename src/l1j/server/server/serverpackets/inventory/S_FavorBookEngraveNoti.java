package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_FavorBookEngraveNoti extends ServerBasePacket {
	private static final String S_FAVOR_BOOK_ENGRAVE_NOTI = "[S] S_FavorBookEngraveNoti";
	private byte[] _byte = null;
	public static final int ENGRAVE_NOTI	= 0x0a5c;
	
	public S_FavorBookEngraveNoti(FavorEngraveType type, L1PcInstance pc, L1FavorBookUserObject user, boolean initial_loading){
		write_init();
		write_type(type);
		write_result_slot_info(pc, user);
		write_initial_loading(initial_loading);
        writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ENGRAVE_NOTI);
	}
	
	void write_type(FavorEngraveType type) {
		writeRaw(0x08);// type
		writeRaw(type.value);
	}
	
	void write_result_slot_info(L1PcInstance pc, L1FavorBookUserObject user) {
		SlotInfoTStream os = null;
		try {
			os = new SlotInfoTStream(pc, user.getObj(), user, user.getCurrentItem());
			writeRaw(0x12);// result_slot_info
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
	
	void write_initial_loading(boolean initial_loading) {
		writeRaw(0x18);// initial_loading 0: 획득, 1:목록
		writeB(initial_loading);
	}
	
	public enum FavorEngraveType{
		ADD(0),
		REMOVE(1),
		;
		private int value;
		FavorEngraveType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(FavorEngraveType v){
			return value == v.value;
		}
		public static FavorEngraveType fromInt(int i){
			switch(i){
			case 0:
				return ADD;
			case 1:
				return REMOVE;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments FavorEngraveType, %d", i));
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
		return S_FAVOR_BOOK_ENGRAVE_NOTI;
	}
}

