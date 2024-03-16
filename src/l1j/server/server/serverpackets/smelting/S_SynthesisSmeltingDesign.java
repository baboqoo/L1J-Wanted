package l1j.server.server.serverpackets.smelting;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_SynthesisSmeltingDesign extends ServerBasePacket {
	private static final String S_SYNTHESIS_SMELTING_DESIGN = "[S] S_SynthesisSmeltingDesign";
	private byte[] _byte = null;
    public static final int DESIGN	= 0x09be;
    
    public static final S_SynthesisSmeltingDesign SAME_HASH	= new S_SynthesisSmeltingDesign(S_SynthesisSmeltingDesign.ResultCode.RC_SYNTHESIS_SMELTING_SAME_HASH_VAL);
    
    public S_SynthesisSmeltingDesign(S_SynthesisSmeltingDesign.ResultCode result){
    	write_init();
        writeRaw(0x08);// result_code
        writeRaw(result.value);
        writeH(0x00);
    }
    
    void write_init() {
    	writeC(Opcodes.S_EXTENDED_PROTOBUF);
        writeH(DESIGN);
    }
    
    private S_SynthesisSmeltingDesign(){}
    
    public static ArrayList<S_SynthesisSmeltingDesign> getSmeltingPacketList(ArrayBlockingQueue<Integer> queue){
    	ArrayList<S_SynthesisSmeltingDesign> list	= new ArrayList<S_SynthesisSmeltingDesign>();
    	S_SynthesisSmeltingDesign pckLast			= null;
    	S_SynthesisSmeltingDesign pck				= null;
    	int index = 0;
    	while (!queue.isEmpty()) {
    		try {
				pck = new S_SynthesisSmeltingDesign();
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
    
    public enum ResultCode{
    	RC_SYNTHESIS_SMELTING_LOAD_START(0),
		RC_SYNTHESIS_SMELTING_LOADING(1),
		RC_SYNTHESIS_SMELTING_LOAD_FINISH(2),
		RC_SYNTHESIS_SMELTING_SAME_HASH_VAL(3),
		RC_SYNTHESIS_SMELTING_INVALID_HASH_VAL(4),
		RC_SYNTHESIS_SMELTING_CURRENTLY_CLOSED(5),
		RC_ERROR_UNKNOWN(9999),
		;
		private int value;
		ResultCode(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ResultCode v){
			return value == v.value;
		}
		public static ResultCode fromInt(int i){
			switch(i){
			case 0:
				return RC_SYNTHESIS_SMELTING_LOAD_START;
			case 1:
				return RC_SYNTHESIS_SMELTING_LOADING;
			case 2:
				return RC_SYNTHESIS_SMELTING_LOAD_FINISH;
			case 3:
				return RC_SYNTHESIS_SMELTING_SAME_HASH_VAL;
			case 4:
				return RC_SYNTHESIS_SMELTING_INVALID_HASH_VAL;
			case 5:
				return RC_SYNTHESIS_SMELTING_CURRENTLY_CLOSED;
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
		return S_SYNTHESIS_SMELTING_DESIGN;
	}
}
