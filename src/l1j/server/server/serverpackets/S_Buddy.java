package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Buddy;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.BinaryOutputStream;

public class S_Buddy extends ServerBasePacket {
    private static final String _S_Buddy = "[S] _S_Buddy";

    public static final int BUDDY_LIST = 0x0151;
    public static final int BUDDY_INFO_CHANGE = 0x091e;
    
    private byte[] _byte = null;
    
    public S_Buddy(L1Buddy buddy, L1PcInstance pc) {
        writeC(Opcodes.S_EXTENDED_PROTOBUF);
        writeH(BUDDY_LIST);
        writeC(0x08);// type
        writeC(eBUDDY_LIST_TYPE.eBUDDY_LIST_TYPE_BUDDIES.value);
        
        L1PcInstance target = null;
        for (String name : buddy.getBuddy().keySet()) {
        	BinaryOutputStream os = null;
        	try {
        		os = new BinaryOutputStream();
        		os.writeC(0x0a);// name
        		os.writeStringWithLength(name);
                
        		target = L1World.getInstance().getPlayer(name);
        		os.writeC(0x10);// online
        		os.writeB(target != null);
                
        		os.writeC(0x1a);// memo
                os.writeStringWithLength(buddy.getMemo(name));
                
                os.writeC(0x20);// class
                os.writeBit(target != null ? target.getType() : -1);
        		
                writeC(0x12);// buddys
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
        writeH(0x00);
    }
    
    public S_Buddy(String name, boolean result) {
    	writeC(Opcodes.S_EXTENDED_PROTOBUF);
        writeH(BUDDY_INFO_CHANGE);
        
        writeC(0x0a);
        writeStringWithLength(name);
        
        writeC(0x10);
        writeB(result);
        
        writeH(0x00);
    }
    
    public enum eBUDDY_LIST_TYPE{
		eBUDDY_LIST_TYPE_BUDDIES(1),
		;
		private int value;
		eBUDDY_LIST_TYPE(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eBUDDY_LIST_TYPE v){
			return value == v.value;
		}
		public static eBUDDY_LIST_TYPE fromInt(int i){
			switch(i){
			case 1:
				return eBUDDY_LIST_TYPE_BUDDIES;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eBUDDY_LIST_TYPE, %d", i));
			}
		}
	}

    @Override
    public byte[] getContent() {
        if(_byte == null)_byte = _bao.toByteArray();
        return _byte;
    }

    @Override
    public String getType() {
        return _S_Buddy;
    }
}

