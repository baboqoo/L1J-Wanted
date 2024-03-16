package l1j.server.common.bin.craft;

import java.util.HashMap;

import l1j.server.common.DescKLoader;
import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.server.utils.StringUtil;

public class CraftInputList implements ProtoMessage {
	public static CraftInputList newInstance() {
		return new CraftInputList();
	}

	private java.util.LinkedList<CraftInputItem> _arr_input_item;
	private java.util.LinkedList<CraftInputItem> _arr_option_item;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	
	/**
	 * 제작에 필요한 재료와 슬롯정보 유효성 검사
	 * 슬롯별 증가되는 추가 확률을 계산하여 반환한다.
	 * @param inputSlotList
	 * @return increaseProb(검증 실패시 -1반환)
	 */
	public int validationAndProb(HashMap<Integer, CraftInputSlot> inputSlotList) {
		if (!has_arr_input_item()) {
			return -1;
		}
		
		int currentSlotNumber	= 1;
		int increaseProb		= 0;
		for (CraftInputItem input : get_arr_input_item()) {
			if (currentSlotNumber != input.get_slot()) {
				continue;
			}
			CraftInputSlot slotInfo = inputSlotList.get(input.get_slot());
			if (slotInfo != null && slotInfo.validation(input)) {
				increaseProb	+= input.get_increase_prob();// 슬롯별 증가 확률
				slotInfo.set_count(input.get_count());
				++currentSlotNumber;
			}
		}
		
		// 모든 슬롯이 유효한지 검사
		if (inputSlotList.size() != currentSlotNumber - 1) {
			return -1;
		}
		return increaseProb;
	}

	private CraftInputList() {
	}

	public java.util.LinkedList<CraftInputItem> get_arr_input_item() {
		return _arr_input_item;
	}

	public void add_arr_input_item(CraftInputItem val) {
		if (!has_arr_input_item()) {
			_arr_input_item = new java.util.LinkedList<CraftInputItem>();
			_bit |= 0x1;
		}
		_arr_input_item.add(val);
	}
	
	public String get_arr_input_item_toString() {
		if (_arr_input_item == null || _arr_input_item.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (CraftInputItem input : _arr_input_item) {
			if (sb.length() > 0) {
				sb.append(StringUtil.LineString);
			}
			sb.append("NAME_ID: ").append(input.get_name_id());
			sb.append(", COUNT: ").append(input.get_count());
			sb.append(", SLOT: ").append(input.get_slot());
			sb.append(", ENCHANT: ").append(input.get_enchant());
			sb.append(", BLESS: ").append(input.get_bless());
			sb.append(", DESC: ").append(input.get_desc());
			sb.append(", DESC_KR: ").append(DescKLoader.getDesc(input.get_desc()));
			sb.append(", ICON: ").append(input.get_iconId());
			if (input.has_elemental_enchant_type()) {
				sb.append(", ELEMENTAL_TYPE: ").append(input.get_elemental_enchant_type());
			}
			if (input.has_elemental_enchant_value()) {
				sb.append(", ELEMENTAL_VAL: ").append(input.get_elemental_enchant_value());
			}
			if (input.has_increase_prob()) {
				sb.append(", INCREASE_PROB: ").append(input.get_increase_prob());
			}
			if (input.has_all_enchants_allowed()) {
				sb.append(", ALL_ENCHANTS_ALLOWED: ").append(input.get_all_enchants_allowed());
			}
		}
		return sb.toString();
	}

	public boolean has_arr_input_item() {
		return (_bit & 0x1) == 0x1;
	}

	public java.util.LinkedList<CraftInputItem> get_arr_option_item() {
		return _arr_option_item;
	}

	public void add_arr_option_item(CraftInputItem val) {
		if (!has_arr_option_item()) {
			_arr_option_item = new java.util.LinkedList<CraftInputItem>();
			_bit |= 0x2;
		}
		_arr_option_item.add(val);
	}
	
	public String get_arr_option_item_toString() {
		if (_arr_option_item == null || _arr_option_item.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (CraftInputItem input : _arr_option_item) {
			if (sb.length() > 0) {
				sb.append(StringUtil.LineString);
			}
			sb.append("NAME_ID: ").append(input.get_name_id());
			sb.append(", COUNT: ").append(input.get_count());
			sb.append(", SLOT: ").append(input.get_slot());
			sb.append(", ENCHANT: ").append(input.get_enchant());
			sb.append(", BLESS: ").append(input.get_bless());
			sb.append(", DESC: ").append(input.get_desc());
			sb.append(", DESC_KR: ").append(DescKLoader.getDesc(input.get_desc()));
			sb.append(", ICON: ").append(input.get_iconId());
			if (input.has_elemental_enchant_type()) {
				sb.append(", ELEMENTAL_TYPE: ").append(input.get_elemental_enchant_type());
			}
			if (input.has_elemental_enchant_value()) {
				sb.append(", ELEMENTAL_VAL: ").append(input.get_elemental_enchant_value());
			}
			if (input.has_increase_prob()) {
				sb.append(", INCREASE_PROB: ").append(input.get_increase_prob());
			}
			if (input.has_all_enchants_allowed()) {
				sb.append(", ALL_ENCHANTS_ALLOWED: ").append(input.get_all_enchants_allowed());
			}
		}
		return sb.toString();
	}

	public boolean has_arr_option_item() {
		return (_bit & 0x2) == 0x2;
	}

	@Override
	public long getInitializeBit() {
		return (long) _bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_arr_input_item()){
			for(CraftInputItem val : _arr_input_item){
				size += ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		if (has_arr_option_item()){
			for(CraftInputItem val : _arr_option_item){
				size += ProtoOutputStream.computeMessageSize(2, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_arr_input_item()) {
			for (CraftInputItem val : _arr_input_item) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_arr_option_item()) {
			for (CraftInputItem val : _arr_option_item) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_arr_input_item()){
			for (CraftInputItem val : _arr_input_item){
				output.writeMessage(1, val);
			}
		}
		if (has_arr_option_item()){
			for (CraftInputItem val : _arr_option_item){
				output.writeMessage(2, val);
			}
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x0000000A: {
				add_arr_input_item((CraftInputItem) input.readMessage(CraftInputItem.newInstance()));
				break;
			}
			case 0x00000012: {
				add_arr_option_item((CraftInputItem) input.readMessage(CraftInputItem.newInstance()));
				break;
			}
			default: {
				System.out.println(String.format("[CraftInputList] NEW_TAG : TAG(%d)", tag));
				return this;
			}
			}
		}
		return this;
	}

	@Override
	public void dispose() {
		if (has_arr_input_item()) {
			for (CraftInputItem val : _arr_input_item)
				val.dispose();
			_arr_input_item.clear();
			_arr_input_item = null;
		}
		if (has_arr_option_item()) {
			for (CraftInputItem val : _arr_option_item)
				val.dispose();
			_arr_option_item.clear();
			_arr_option_item = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	
}

