package l1j.server.common.bin.craft;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.server.utils.StringUtil;

public class Craft implements ProtoMessage {
	public static Craft newInstance() {
		return new Craft();
	}
	
	private int _id;
	private CraftAttr _craft_attr;
	private int _required_classes;
	private CraftRequiredQuestList _required_quests;
	private CraftRequiredSpriteList _required_sprites;
	private CraftRequiredItemList _required_items;
	private CraftInputList _inputs;
	private CraftOutputList _outputs;
	private int _batch_delay_sec;
	private PeriodList _period_list;
	private int _cur_successcount;
	private int _max_successcount;
	private boolean _except_npc;
	private Craft.eSuccessCountType _SuccessCountType;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private Craft() {
		set_except_npc(true);
		set_SuccessCountType(Craft.eSuccessCountType.World);
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int val) {
		_bit |= 0x1;
		_id = val;
	}

	public boolean has_id() {
		return (_bit & 0x1) == 0x1;
	}

	public CraftAttr get_craft_attr() {
		return _craft_attr;
	}

	public void set_craft_attr(CraftAttr val) {
		_bit |= 0x2;
		_craft_attr = val;
	}

	public boolean has_craft_attr() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_required_classes() {
		return _required_classes;
	}

	public void set_required_classes(int val) {
		_bit |= 0x4;
		_required_classes = val;
	}

	public boolean has_required_classes() {
		return (_bit & 0x4) == 0x4;
	}

	public CraftRequiredQuestList get_required_quests() {
		return _required_quests;
	}

	public void set_required_quests(CraftRequiredQuestList val) {
		_bit |= 0x8;
		_required_quests = val;
	}
	
	public String get_required_quests_toString() {
		if (_required_quests == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("AND_OR: ").append(_required_quests.get_and_or());
		sb.append("\r\nCOUNT: ").append(_required_quests.get_count());
		CraftQuestFlag questFlag = _required_quests.get_flag();
		if (questFlag != null) {
			sb.append("\r\nQUEST_FLAG: ").append("FIRST=").append(questFlag.get_flag1()).append(" AND SECOND=").append(questFlag.get_flag2());
		}
		return sb.toString();
	}

	public boolean has_required_quests() {
		return (_bit & 0x8) == 0x8;
	}

	public CraftRequiredSpriteList get_required_sprites() {
		return _required_sprites;
	}

	public void set_required_sprites(CraftRequiredSpriteList val) {
		_bit |= 0x10;
		_required_sprites = val;
	}
	
	public String get_required_sprites_toString() {
		if (_required_sprites == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("COUNT: ").append(_required_sprites.get_count());
		java.util.LinkedList<Integer> sprite_ids = _required_sprites.get_sprite_id();
		if (sprite_ids != null && !sprite_ids.isEmpty()) {
			sb.append("\r\nSPRITE: ");
			int spriteCnt = 0;
			for (int val : sprite_ids) {
				if (spriteCnt++ > 0) {
					sb.append(StringUtil.CommaString);
				}
				sb.append(val);
			}
		}
		return sb.toString();
	}

	public boolean has_required_sprites() {
		return (_bit & 0x10) == 0x10;
	}

	public CraftRequiredItemList get_required_items() {
		return _required_items;
	}

	public void set_required_items(CraftRequiredItemList val) {
		_bit |= 0x20;
		_required_items = val;
	}
	
	public String get_required_items_toString() {
		if (_required_items == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("AND_OR: ").append(_required_items.get_and_or());
		sb.append("\r\nCOUNT: ").append(_required_items.get_count());
		java.util.LinkedList<CraftRequiredItem> items = _required_items.get_items();
		if (items != null && !items.isEmpty()) {
			sb.append("\r\nITEMS=====> ");
			for (CraftRequiredItem val : items) {
				sb.append("\r\nNAME_ID=").append(val.get_name_id()).append(", COUNT=").append(val.get_count()).append(", NAGATIVE=").append(val.get_is_nagative());
			}
		}
		return sb.toString();
	}

	public boolean has_required_items() {
		return (_bit & 0x20) == 0x20;
	}

	public CraftInputList get_inputs() {
		return _inputs;
	}

	public void set_inputs(CraftInputList val) {
		_bit |= 0x40;
		_inputs = val;
	}

	public boolean has_inputs() {
		return (_bit & 0x40) == 0x40;
	}

	public CraftOutputList get_outputs() {
		return _outputs;
	}

	public void set_outputs(CraftOutputList val) {
		_bit |= 0x80;
		_outputs = val;
	}

	public boolean has_outputs() {
		return (_bit & 0x80) == 0x80;
	}

	public int get_batch_delay_sec() {
		return _batch_delay_sec;
	}

	public void set_batch_delay_sec(int val) {
		_bit |= 0x100;
		_batch_delay_sec = val;
	}

	public boolean has_batch_delay_sec() {
		return (_bit & 0x100) == 0x100;
	}

	public PeriodList get_period_list() {
		return _period_list;
	}

	public void set_period_list(PeriodList val) {
		_bit |= 0x200;
		_period_list = val;
	}
	
	public String get_period_list_toString() {
		if (_period_list == null) {
			return null;
		}
		java.util.LinkedList<Period> period = _period_list.get_period();
		if (period == null || period.isEmpty()) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		for (Period val : period) {
			if (sb.length() > 0) {
				sb.append(StringUtil.LineString);
			}
			sb.append("START_TIME: ").append(val.get_start_time());
			sb.append(", DURATION_SEC: ").append(val.get_duration_sec());
		}
		return sb.toString();
	}

	public boolean has_period_list() {
		return (_bit & 0x200) == 0x200;
	}

	public int get_cur_successcount() {
		return _cur_successcount;
	}

	public void set_cur_successcount(int val) {
		_bit |= 0x400;
		_cur_successcount = val;
	}

	public boolean has_cur_successcount() {
		return (_bit & 0x400) == 0x400;
	}

	public int get_max_successcount() {
		return _max_successcount;
	}

	public void set_max_successcount(int val) {
		_bit |= 0x800;
		_max_successcount = val;
	}

	public boolean has_max_successcount() {
		return (_bit & 0x800) == 0x800;
	}
	
	public boolean get_except_npc() {
		return _except_npc;
	}

	public void set_except_npc(boolean val) {
		_bit |= 0x1000;
		_except_npc = val;
	}

	public boolean has_except_npc() {
		return (_bit & 0x1000) == 0x1000;
	}
	
	public Craft.eSuccessCountType get_SuccessCountType() {
		return _SuccessCountType;
	}

	public void set_SuccessCountType(Craft.eSuccessCountType val) {
		_bit |= 0x2000;
		_SuccessCountType = val;
	}

	public boolean has_SuccessCountType() {
		return (_bit & 0x2000) == 0x2000;
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
		if (has_id()){
			size += ProtoOutputStream.computeInt32Size(1, _id);
		}
		if (has_craft_attr()){
			size += ProtoOutputStream.computeMessageSize(2, _craft_attr);
		}
		if (has_required_classes()){
			size += ProtoOutputStream.computeInt32Size(3, _required_classes);
		}
		if (has_required_quests()){
			size += ProtoOutputStream.computeMessageSize(4, _required_quests);
		}
		if (has_required_sprites()){
			size += ProtoOutputStream.computeMessageSize(5, _required_sprites);
		}
		if (has_required_items()){
			size += ProtoOutputStream.computeMessageSize(6, _required_items);
		}
		if (has_inputs()){
			size += ProtoOutputStream.computeMessageSize(7, _inputs);
		}
		if (has_outputs()){
			size += ProtoOutputStream.computeMessageSize(8, _outputs);
		}
		if (has_batch_delay_sec()){
			size += ProtoOutputStream.computeInt32Size(9, _batch_delay_sec);
		}
		if (has_period_list()){
			size += ProtoOutputStream.computeMessageSize(10, _period_list);
		}
		if (has_cur_successcount()){
			size += ProtoOutputStream.computeInt32Size(11, _cur_successcount);
		}
		if (has_max_successcount()){
			size += ProtoOutputStream.computeInt32Size(12, _max_successcount);
		}
		if (has_except_npc()){
			size += ProtoOutputStream.computeBoolSize(13, _except_npc);
		}
		if (has_SuccessCountType()){
			size += ProtoOutputStream.computeEnumSize(14, _SuccessCountType.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_craft_attr()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_required_classes()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_required_quests()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_required_sprites()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_required_items()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_inputs()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_outputs()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_id()){
			output.wirteInt32(1, _id);
		}
		if (has_craft_attr()){
			output.writeMessage(2, _craft_attr);
		}
		if (has_required_classes()){
			output.wirteInt32(3, _required_classes);
		}
		if (has_required_quests()){
			output.writeMessage(4, _required_quests);
		}
		if (has_required_sprites()){
			output.writeMessage(5, _required_sprites);
		}
		if (has_required_items()){
			output.writeMessage(6, _required_items);
		}
		if (has_inputs()){
			output.writeMessage(7, _inputs);
		}
		if (has_outputs()){
			output.writeMessage(8, _outputs);
		}
		if (has_batch_delay_sec()){
			output.wirteInt32(9, _batch_delay_sec);
		}
		if (has_period_list()){
			output.writeMessage(10, _period_list);
		}
		if (has_cur_successcount()){
			output.wirteInt32(11, _cur_successcount);
		}
		if (has_max_successcount()){
			output.wirteInt32(12, _max_successcount);
		}
		if (has_except_npc()){
			output.writeBool(13, _except_npc);
		}
		if (has_SuccessCountType()){
			output.writeEnum(14, _SuccessCountType.toInt());
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x00000008: {
				set_id(input.readInt32());
				break;
			}
			case 0x00000012: {
				set_craft_attr((CraftAttr) input.readMessage(CraftAttr.newInstance()));
				break;
			}
			case 0x00000018: {
				set_required_classes(input.readInt32());
				break;
			}
			case 0x00000022: {
				set_required_quests((CraftRequiredQuestList) input.readMessage(CraftRequiredQuestList.newInstance()));
				break;
			}
			case 0x0000002A: {
				set_required_sprites((CraftRequiredSpriteList) input.readMessage(CraftRequiredSpriteList.newInstance()));
				break;
			}
			case 0x00000032: {
				set_required_items((CraftRequiredItemList) input.readMessage(CraftRequiredItemList.newInstance()));
				break;
			}
			case 0x0000003A: {
				set_inputs((CraftInputList) input.readMessage(CraftInputList.newInstance()));
				break;
			}
			case 0x00000042: {
				set_outputs((CraftOutputList) input.readMessage(CraftOutputList.newInstance()));
				break;
			}
			case 0x00000048: {
				set_batch_delay_sec(input.readInt32());
				break;
			}
			case 0x00000052: {
				set_period_list((PeriodList) input.readMessage(PeriodList.newInstance()));
				break;
			}
			case 0x00000058: {
				set_cur_successcount(input.readInt32());
				break;
			}
			case 0x00000060: {
				set_max_successcount(input.readInt32());
				break;
			}
			case 0x00000068: {
				set_except_npc(input.readBool());
				break;
			}
			case 0x00000070: {
				set_SuccessCountType(Craft.eSuccessCountType.fromInt(input.readEnum()));
				break;
			}
			default: {
				System.out.println(String.format("[Craft] NEW_TAG : TAG(%d)", tag));
				return this;
			}
			}
		}
		return this;
	}

	@Override
	public void dispose() {
		if (has_craft_attr() && _craft_attr != null) {
			_craft_attr.dispose();
			_craft_attr = null;
		}
		if (has_required_quests() && _required_quests != null) {
			_required_quests.dispose();
			_required_quests = null;
		}
		if (has_required_sprites() && _required_sprites != null) {
			_required_sprites.dispose();
			_required_sprites = null;
		}
		if (has_required_items() && _required_items != null) {
			_required_items.dispose();
			_required_items = null;
		}
		if (has_inputs() && _inputs != null) {
			_inputs.dispose();
			_inputs = null;
		}
		if (has_outputs() && _outputs != null) {
			_outputs.dispose();
			_outputs = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	
	public enum eSuccessCountType{
		World(0),		// 현재 서버
		Account(1),		// 계정
		Character(2),	// 캐릭터
		AllServers(3),	// 모든 서버
		;
		private int value;
		eSuccessCountType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eSuccessCountType v){
			return value == v.value;
		}
		public static eSuccessCountType fromInt(int i){
			switch(i){
			case 0:
				return World;
			case 1:
				return Account;
			case 2:
				return Character;
			case 3:
				return AllServers;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eSuccessCountType, %d", i));
			}
		}
		public static eSuccessCountType fromString(String val){
			switch(val){
			case "World":
				return World;
			case "Account":
				return Account;
			case "Character":
				return Character;
			case "AllServers":
				return AllServers;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eSuccessCountType, %s", val));
			}
		}
	}
}

