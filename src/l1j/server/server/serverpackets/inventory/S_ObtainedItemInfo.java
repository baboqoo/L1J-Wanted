package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ObtainedItemInfo extends ServerBasePacket {
	private static final String S_OBTAINED_ITEM_INFO = "[S] S_ObtainedItemInfo";
	public static final int OBTAINED = 0x0260;

    public S_ObtainedItemInfo(L1PcInstance pc, L1ItemInstance item, int changing_count) {
    	write_init();
        write_namd_id(item.getItem().getItemNameId());
        write_bless_code(item.getBless());
        write_changing_count(changing_count);
        write_extra_desc(!item.isIdentified() ? null : item.getStatusBytes(pc));
        writeH(0x00);
    }
    
    void write_init() {
    	writeC(Opcodes.S_EXTENDED_PROTOBUF);
        writeH(OBTAINED);
    }
    
    void write_namd_id(int namd_id) {
    	writeRaw(0x08);// namd_id
        writeBit(namd_id);
    }
    
    void write_bless_code(int bless_code) {
    	writeRaw(0x10);// bless_code
    	writeBit(bless_code);
    }
    
    void write_changing_count(int changing_count) {
    	writeRaw(0x18);// changing_count
        writeBit(changing_count);
    }
    
    void write_extra_desc(byte[] extra_desc) {
    	writeRaw(0x22);
    	writeBytesWithLength(extra_desc);
    }

    @Override
    public byte[] getContent() {
        return _bao.toByteArray();
    }

    @Override
    public String getType() {
        return S_OBTAINED_ITEM_INFO;
    }
}

