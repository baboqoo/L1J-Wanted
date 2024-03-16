package l1j.server.server.serverpackets.smelting;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_SmeltingUpdateSlotInfoNoti extends ServerBasePacket {
	private static final String S_SMELTING_UPDATE_SLOT_INFO_NOTI = "[S] S_SmeltingUpdateSlotInfoNoti";
	private byte[] _byte = null;
    public static final int SLOT	= 0x09c2;
    
    public S_SmeltingUpdateSlotInfoNoti(int slot_no, int target_object_id, int scroll_name_id, SmeltingResult result){
    	write_init();
    	write_target_object_id(target_object_id);
    	write_slot_no(slot_no);
        write_scroll_name_id(scroll_name_id);
        write_result(result);
        writeH(0x00);
    }
    
    void write_init() {
    	writeC(Opcodes.S_EXTENDED_PROTOBUF);
        writeH(SLOT);
    }
    
    void write_target_object_id(int target_object_id) {
    	writeRaw(0x08);// target_object_id
        writeBit(target_object_id);
    }
    
    void write_slot_no(int slot_no) {
    	writeRaw(0x10);// slot_no
        writeRaw(slot_no);
    }
    
    void write_scroll_name_id(int scroll_name_id) {
    	writeRaw(0x18);// scroll_name_id
        writeBit(scroll_name_id);
    }
    
    void write_result(SmeltingResult result) {
    	writeRaw(0x20);// result
        writeRaw(result.toInt());
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
		return S_SMELTING_UPDATE_SLOT_INFO_NOTI;
	}
}
