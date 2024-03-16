package l1j.server.server.serverpackets.smelting;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_SynthesisSmeltingProb extends ServerBasePacket {
	private static final String S_SYNTHESIS_SMELTING_PROB = "[S] S_SynthesisSmeltingProb";
	private byte[] _byte = null;
	public static final int PROB	= 0x09bc;
    
    public static class SmeltingProb {
    	private int _name_id;
    	private int _million_prob;
    	private int _bless_code;
		
    	public SmeltingProb(int _name_id, int _million_prob, int _bless_code) {
			this._name_id		= _name_id;
			this._million_prob	= _million_prob;
			this._bless_code	= _bless_code;
		}
    	
    	public void set_prob(int val) {
    		_million_prob = val;
    	}
    }

    public S_SynthesisSmeltingProb(S_SynthesisSmeltingProb.ResultCode result_code, java.util.LinkedList<SmeltingProb> prob_list){
    	write_init();
        write_result_code(result_code);
        if (result_code == S_SynthesisSmeltingProb.ResultCode.RC_SUCCESS) {
        	for (SmeltingProb prob : prob_list) {
        		write_output_items(prob);
            }
        }
        writeH(0x00);
    }
    
    void write_init() {
    	writeC(Opcodes.S_EXTENDED_PROTOBUF);
        writeH(PROB);
    }
    
    void write_result_code(S_SynthesisSmeltingProb.ResultCode result_code) {
    	writeRaw(0x08);// result_code
        writeRaw(result_code.value);
    }
    
    void write_output_items(SmeltingProb prob) {
    	int length = getBitSize(prob._name_id) + getBitSize(prob._million_prob) + 4;
    	writeRaw(0x12);
    	writeRaw(length);
    	
    	writeRaw(0x08);
    	writeBit(prob._name_id);
    	
    	writeRaw(0x10);
    	writeBit(prob._million_prob);
    	
    	writeRaw(0x18);
    	writeRaw(prob._bless_code);
    }
    
    public enum ResultCode{
		RC_SUCCESS(0),
		RC_FAIL(1),
		RC_ERROR_INVALID_INPUT(2),
		RC_ERROR_INVEN_OVER(3),
		RC_ERROR_WEIGHT_OVER(4),
		RC_ERROR_ALCHEMY_DOES_NOT_EXIST(5),
		RC_ERROR_NO_REQUIRED_ITEM(6),
		RC_ERROR_BLOCKED_ALCHEMY_ID(7),
		RC_ERROR_CURRENTLY_CLOSED(8),
		RC_ERROR_PETBALL_SUMMONING(9),
		RC_HYPER_SUCCESS(10),
		RC_ERROR_NOT_ENOUGH_SUBINPUT(11),
		RC_ALCHEMY_DOES_NOT_EXIST(12),
		RC_ERROR_UNKNOWN_SUBINPUT(13),
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
				return RC_SUCCESS;
			case 1:
				return RC_FAIL;
			case 2:
				return RC_ERROR_INVALID_INPUT;
			case 3:
				return RC_ERROR_INVEN_OVER;
			case 4:
				return RC_ERROR_WEIGHT_OVER;
			case 5:
				return RC_ERROR_ALCHEMY_DOES_NOT_EXIST;
			case 6:
				return RC_ERROR_NO_REQUIRED_ITEM;
			case 7:
				return RC_ERROR_BLOCKED_ALCHEMY_ID;
			case 8:
				return RC_ERROR_CURRENTLY_CLOSED;
			case 9:
				return RC_ERROR_PETBALL_SUMMONING;
			case 10:
				return RC_HYPER_SUCCESS;
			case 11:
				return RC_ERROR_NOT_ENOUGH_SUBINPUT;
			case 12:
				return RC_ALCHEMY_DOES_NOT_EXIST;
			case 13:
				return RC_ERROR_UNKNOWN_SUBINPUT;
			case 9999:
				return RC_ERROR_UNKNOWN;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments ResultCode, %d", i));
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
		return S_SYNTHESIS_SMELTING_PROB;
	}
}
