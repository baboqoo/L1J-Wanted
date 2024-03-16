package l1j.server.server.serverpackets.smelting;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_SmeltingMake extends ServerBasePacket {
	private static final String S_SMELTING_MAKE = "[S] S_SmeltingMake";
	private byte[] _byte = null;
    public static final int MAKE	= 0x09c0;
    
    public static final S_SmeltingMake ERROR_INVALID_INPUT			= new S_SmeltingMake(S_SmeltingMake.ResultCode.RC_ERROR_INVALID_INPUT, null);
    public static final S_SmeltingMake ERROR_ALCHEMY_DOES_NOT_EXIST	= new S_SmeltingMake(S_SmeltingMake.ResultCode.RC_ERROR_ALCHEMY_DOES_NOT_EXIST, null);
    public static final S_SmeltingMake ERROR_NOT_ENOUGH_SUBINPUT	= new S_SmeltingMake(S_SmeltingMake.ResultCode.RC_ERROR_NOT_ENOUGH_SUBINPUT, null);
    public static final S_SmeltingMake ERROR_NO_REQUIRED_ITEM		= new S_SmeltingMake(S_SmeltingMake.ResultCode.RC_ERROR_NO_REQUIRED_ITEM, null);
    
    public S_SmeltingMake(S_SmeltingMake.ResultCode result, L1ItemInstance output_item){
    	write_init();
    	write_result_code(result);
        if (result == S_SmeltingMake.ResultCode.RC_SUCCESS) {
        	write_output_items(output_item);
        }
        writeH(0x00);
    }
    
    void write_init() {
    	writeC(Opcodes.S_EXTENDED_PROTOBUF);
        writeH(MAKE);
    }
    
    void write_result_code(S_SmeltingMake.ResultCode result_code) {
    	writeRaw(0x08);// result_code
        writeRaw(result_code.value);
    }
    
    void write_output_items(L1ItemInstance output_item) {
    	int size = getBitSize(output_item.getId()) + getBitSize(output_item.getIconId()) + getBitSize(output_item.getBless()) + 3;
    	writeRaw(0x12);// output_items
    	writeRaw(size);
    	
    	write_item_id(output_item.getId());
    	write_icon_id(output_item.getIconId());
    	write_bless_code(output_item.getBless());
        //write_item_grade(output_item.getItem().getEtcValue() - 1);
    }
    
    void write_item_id(int item_id) {
    	writeRaw(0x08);// item_id
        writeBit(item_id);
    }
    
    void write_icon_id(int icon_id) {
    	writeRaw(0x10);// icon_id
        writeBit(icon_id);
    }
    
    void write_bless_code(int bless_code) {
    	writeRaw(0x18);// bless_code
        writeBit(bless_code);
    }
    
    void write_item_grade(int item_grade) {
    	writeRaw(0x20);// item_grade
        writeRaw(item_grade);
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
		RC_ERROR_UNKNOWN_SUBINPUT(12),
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
		return S_SMELTING_MAKE;
	}
}
