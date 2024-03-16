package l1j.server.server.serverpackets.alchemy;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_AlchemyDesign extends ServerBasePacket {
	private static final String S_ALCHEMY_DESIGN = "[S] S_AlchemyDesign";
	private byte[] _byte = null;
    public static final int ALCHEMY_DESIGN	= 0x007b;
    
    public static final S_AlchemyDesign SAME_HASH	= new S_AlchemyDesign(AlchemyResultCode.RC_ALCHEMY_SAME_HASH_VAL);
    
    public S_AlchemyDesign(AlchemyResultCode result_code){
    	write_init();
        write_result_code(result_code);
        writeH(0x00);
    }
    
    void write_init() {
    	writeC(Opcodes.S_EXTENDED_PROTOBUF);
        writeH(ALCHEMY_DESIGN);
    }
    
    void write_result_code(AlchemyResultCode result_code) {
    	writeRaw(0x08);// result_code
    	writeRaw(result_code.value);
    }
    
    private S_AlchemyDesign(){}
    
    public static ArrayList<S_AlchemyDesign> getAlchemyPacketList(ArrayBlockingQueue<Integer> queue){
    	ArrayList<S_AlchemyDesign> list	= new ArrayList<S_AlchemyDesign>();
    	S_AlchemyDesign pckLast			= null;
    	S_AlchemyDesign pck				= null;
    	int index = 0;
    	while (!queue.isEmpty()) {
    		try {
				pck = new S_AlchemyDesign();
        		pck.write_init();
        		int code = queue.poll();
        		if (code == 0x08) {
        			pck.writeRaw(code);// result_code
        			pck.writeRaw(queue.poll());// 02
        		} else if (code == 0x12 || code == 0x22) {
        			pck.writeRaw(0x08);
            		pck.writeRaw(index > 1 ? 1 : 0);
        			pck.writeRaw(code);
        			pck.writeFromQueue(queue, queue.poll());
        		}
        		pck.writeH(0x00);
        		if (code == 0x08) {
        			pckLast = pck;
        		} else {
        			list.add(pck);
        		}
        		index++;
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	if (pckLast != null) {
    		list.add(pckLast);
    	}
    	return list;
    }
    
    public enum AlchemyResultCode{
		RC_ALCHEMY_LOAD_START(0),
		RC_ALCHEMY_LOADING(1),
		RC_ALCHEMY_LOAD_FINISH(2),
		RC_ALCHEMY_SAME_HASH_VAL(3),
		RC_ALCHEMY_INVALID_HASH_VAL(4),
		RC_ALCHEMY_CURRENTLY_CLOSED(5),
		RC_ERROR_UNKNOWN(9999);
		private int value;
		AlchemyResultCode(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(AlchemyResultCode v){
			return value == v.value;
		}
		public static AlchemyResultCode fromInt(int i){
			switch(i){
			case 0:
				return RC_ALCHEMY_LOAD_START;
			case 1:
				return RC_ALCHEMY_LOADING;
			case 2:
				return RC_ALCHEMY_LOAD_FINISH;
			case 3:
				return RC_ALCHEMY_SAME_HASH_VAL;
			case 4:
				return RC_ALCHEMY_INVALID_HASH_VAL;
			case 5:
				return RC_ALCHEMY_CURRENTLY_CLOSED;
			case 9999:
				return RC_ERROR_UNKNOWN;
			default:
				return null;
			}
		}
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
		return S_ALCHEMY_DESIGN;
	}
}
