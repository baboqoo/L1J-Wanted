package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.BinaryOutputStreamItemInfo;

public class S_UpdateInventoryNoti extends ServerBasePacket {
	private static final String S_UPDATE_INVENTORY_NOTI = "[S] S_UpdateInventoryNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x024D;
	
	public S_UpdateInventoryNoti(L1ItemInstance item) {
		write_init();
		write_item_info(item.getItemInfo(null));
	    writeH(0x00);
	}
	
	public S_UpdateInventoryNoti(java.util.LinkedList<L1ItemInstance> item_info) {
		write_init();
		if (item_info != null && !item_info.isEmpty()) {
			for (L1ItemInstance item : item_info) {
				write_item_info(item.getItemInfo(null));
			}
		}
	    writeH(0x00);
	}
	
	public S_UpdateInventoryNoti(int object_id, byte[] companion_card_byte) {
		write_init();
		BinaryOutputStreamItemInfo os = null;
	    try {
	    	os = new BinaryOutputStreamItemInfo();
	    	os.write_object_id(object_id);
	    	os.write_companion_card(companion_card_byte);
	        write_item_info(os.getBytes());
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	    	if (os != null) {
	    		try {
	    			os.close();
	    			os = null;
	    		} catch(Exception e) {}
	    	}
	    }
	    writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_item_info(byte[] item_info) {
		writeC(0x0a);// item_info
        writeBytesWithLength(item_info);
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
		return S_UPDATE_INVENTORY_NOTI;
	}
}

