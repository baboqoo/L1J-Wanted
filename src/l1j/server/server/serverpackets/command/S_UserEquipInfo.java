package l1j.server.server.serverpackets.command;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.BinaryOutputStream;

public class S_UserEquipInfo extends ServerBasePacket {
	private static final String S_USER_EQUIP_INFO = "[S] S_UserEquipInfo";
	private byte[] _byte = null;
	public static final int EQUIP	= 0x0a10;

	public S_UserEquipInfo(L1PcInstance target){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(EQUIP);
		
		writeC(0x0a);
		writeStringWithLength(target.getName());
		
		writeC(0x10);
		writeC(target.getGender().toInt());
		
		writeC(0x18);
		writeC(target.getType());
		
		writeC(0x22);
		writeBytesWithLength(getEquipListBytes(target));
		
        writeH(0x00);
	}
	
	private byte[] getEquipListBytes(L1PcInstance target){
		BinaryOutputStream os = null;
		try {
			os = new BinaryOutputStream();
			for (L1ItemInstance item : target.getInventory().getItems()) {
				if (item == null) {
					continue;
				}
				os.writeC(0x0a);
				os.writeBytesWithLength(getEquipBytes(target, item));
			}
			return os.getBytes();
		} catch (Exception e) {
            e.printStackTrace();
        } finally {
        	try {
        		if (os != null) {
            		os.close();
            		os = null;
            	}
        	} catch (Exception e1){
        		e1.printStackTrace();
        	}
        }
		return null;
	}
	
	private byte[] getEquipBytes(L1PcInstance target, L1ItemInstance item){
		BinaryOutputStream os = null;
		try {
			os = new BinaryOutputStream();
			os.writeC(0x08);
			os.writeBit(item.getId());
			
			os.writeC(0x10);// equip_position
			os.writeC(0x00);
			
			os.writeC(0x18);// is_equiped
			os.writeB(item.isEquipped());
			
			os.writeC(0x22);
			os.writeBytesWithLength(item.getItemInfo(target));
			return os.getBytes();
		} catch (Exception e) {
            e.printStackTrace();
        } finally {
        	try {
        		if (os != null) {
            		os.close();
            		os = null;
            	}
        	} catch (Exception e1){
        		e1.printStackTrace();
        	}
        }
		return null;
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
		return S_USER_EQUIP_INFO;
	}
}

