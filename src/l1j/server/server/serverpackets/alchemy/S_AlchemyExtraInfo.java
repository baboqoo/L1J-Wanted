package l1j.server.server.serverpackets.alchemy;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_AlchemyExtraInfo extends ServerBasePacket {
	private static final String S_ALCHEMY_EXTRA_INFO = "[S] S_AlchemyExtraInfo";
	private byte[] _byte = null;
    public static final int INFO = 0x0080;
    
    public S_AlchemyExtraInfo(L1DollInstance currentDoll){
    	write_init();
        if (currentDoll != null) {
        	write_summoned_petball_item_id(currentDoll.getItemObjId());
        }
        writeH(0x00);
    }
    
    void write_init() {
    	writeC(Opcodes.S_EXTENDED_PROTOBUF);
        writeH(INFO);
    }
    
    void write_summoned_petball_item_id(int summoned_petball_item_id) {
    	writeC(0x08);// summoned_petball_item_id
		writeBit(summoned_petball_item_id);
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
		return S_ALCHEMY_EXTRA_INFO;
	}
}
