package l1j.server.server.serverpackets.object;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.BinaryOutputStream;

public class S_DropItem extends ServerBasePacket {
	private static final String S_DROP_ITEM = "[S] S_DropItem";
	private byte[] _byte = null;
	private static final byte[] TAIL_BYTES = getTailBytes();
	
	/**
	 * 아이템 드랍 오브젝트(구형)
	 * @param item
	 */
	public S_DropItem(L1ItemInstance item) {
		writeC(Opcodes.S_PUT_OBJECT);
		writeH(item.getX());
		writeH(item.getY());
		writeD(item.getId());
		writeH(item.getItem().getSpriteId());
		writeC(0);
		writeC(0);
		writeC(item.isNowLighting() ? item.getItem().getLightRange() : 0);
		writeC(0);
		writeD(item.getCount());
		writeC(0);
		writeC(0);
		writeS(item.getViewName());
		writeByte(TAIL_BYTES);
	}
	
	private static byte[] getTailBytes(){
    	BinaryOutputStream os = null;
    	try {
    		os = new BinaryOutputStream();
    		os.writeC(0);
    		os.writeD(0);
    		os.writeD(0);
    		os.writeC(255);
    		os.writeC(0);
    		os.writeC(0);
    		os.writeC(0);
    		os.writeH(65535);
    		os.writeD(0);
    		os.writeC(8);
    		os.writeC(0);
    		return os.getBytes();
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
		return S_DROP_ITEM;
	}

}

