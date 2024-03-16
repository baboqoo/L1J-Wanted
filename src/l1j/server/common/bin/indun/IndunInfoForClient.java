package l1j.server.common.bin.indun;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.common.data.eDungeonType;
import l1j.server.server.utils.StringUtil;

public class IndunInfoForClient implements ProtoMessage{
	public static IndunInfoForClient newInstance(){
		return new IndunInfoForClient();
	}
	private int _keyItemId;
	private int _minPlayer;
	private int _maxPlayer;
	private int _minAdena;
	private int _maxAdena;
	private java.util.LinkedList<Integer> _minLevel;
	private int _bmkeyItemId;
	private int _eventKeyItemId;
	private eDungeonType _dungeon_type;
	private boolean _enable_boost_mode;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private IndunInfoForClient(){
	}
	public int get_keyItemId(){
		return _keyItemId;
	}
	public void set_keyItemId(int val){
		_bit |= 0x1;
		_keyItemId = val;
	}
	public boolean has_keyItemId(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_minPlayer(){
		return _minPlayer;
	}
	public void set_minPlayer(int val){
		_bit |= 0x2;
		_minPlayer = val;
	}
	public boolean has_minPlayer(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_maxPlayer(){
		return _maxPlayer;
	}
	public void set_maxPlayer(int val){
		_bit |= 0x4;
		_maxPlayer = val;
	}
	public boolean has_maxPlayer(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_minAdena(){
		return _minAdena;
	}
	public void set_minAdena(int val){
		_bit |= 0x8;
		_minAdena = val;
	}
	public boolean has_minAdena(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_maxAdena(){
		return _maxAdena;
	}
	public void set_maxAdena(int val){
		_bit |= 0x10;
		_maxAdena = val;
	}
	public boolean has_maxAdena(){
		return (_bit & 0x10) == 0x10;
	}
	public java.util.LinkedList<Integer> get_minLevel(){
		return _minLevel;
	}
	public void add_minLevel(int val){
		if(!has_minLevel()){
			_minLevel = new java.util.LinkedList<Integer>();
			_bit |= 0x20;
		}
		_minLevel.add(val);
	}
	public String get_minLevel_toString() {
		if (_minLevel == null || _minLevel.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int val : _minLevel) {
			if (sb.length() > 0) {
				sb.append(StringUtil.CommaString);
			}
			sb.append(val);
		}
		return sb.toString();
	}
	public boolean has_minLevel(){
		return (_bit & 0x20) == 0x20;
	}
	public int get_bmkeyItemId(){
		return _bmkeyItemId;
	}
	public void set_bmkeyItemId(int val){
		_bit |= 0x40;
		_bmkeyItemId = val;
	}
	public boolean has_bmkeyItemId(){
		return (_bit & 0x40) == 0x40;
	}
	public int get_eventKeyItemId(){
		return _eventKeyItemId;
	}
	public void set_eventKeyItemId(int val){
		_bit |= 0x80;
		_eventKeyItemId = val;
	}
	public boolean has_eventKeyItemId(){
		return (_bit & 0x80) == 0x80;
	}
	public eDungeonType get_dungeon_type(){
		return _dungeon_type;
	}
	public void set_dungeon_type(eDungeonType val){
		_bit |= 0x100;
		_dungeon_type = val;
	}
	public boolean has_dungeon_type(){
		return (_bit & 0x100) == 0x100;
	}
	public boolean get_enable_boost_mode(){
		return _enable_boost_mode;
	}
	public void set_enable_boost_mode(boolean val){
		_bit |= 0x200;
		_enable_boost_mode = val;
	}
	public boolean has_enable_boost_mode(){
		return (_bit & 0x200) == 0x200;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_keyItemId()){
			size += ProtoOutputStream.computeInt32Size(1, _keyItemId);
		}
		if (has_minPlayer()){
			size += ProtoOutputStream.computeInt32Size(2, _minPlayer);
		}
		if (has_maxPlayer()){
			size += ProtoOutputStream.computeInt32Size(3, _maxPlayer);
		}
		if (has_minAdena()){
			size += ProtoOutputStream.computeInt32Size(4, _minAdena);
		}
		if (has_maxAdena()){
			size += ProtoOutputStream.computeInt32Size(5, _maxAdena);
		}
		if (has_minLevel()){
			for(int val : _minLevel){
				size += ProtoOutputStream.computeInt32Size(6, val);
			}
		}
		if (has_bmkeyItemId()){
			size += ProtoOutputStream.computeInt32Size(7, _bmkeyItemId);
		}
		if (has_eventKeyItemId()){
			size += ProtoOutputStream.computeInt32Size(8, _eventKeyItemId);
		}
		if (has_dungeon_type()){
			size += ProtoOutputStream.computeEnumSize(9, _dungeon_type.toInt());
		}
		if (has_enable_boost_mode()){
			size += ProtoOutputStream.computeBoolSize(10, _enable_boost_mode);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_keyItemId()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_minPlayer()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_maxPlayer()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_minAdena()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_maxAdena()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_minLevel()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_keyItemId()){
			output.wirteInt32(1, _keyItemId);
		}
		if (has_minPlayer()){
			output.wirteInt32(2, _minPlayer);
		}
		if (has_maxPlayer()){
			output.wirteInt32(3, _maxPlayer);
		}
		if (has_minAdena()){
			output.wirteInt32(4, _minAdena);
		}
		if (has_maxAdena()){
			output.wirteInt32(5, _maxAdena);
		}
		if (has_minLevel()){
			for (int val : _minLevel){
				output.wirteInt32(6, val);
			}
		}
		if (has_bmkeyItemId()){
			output.wirteInt32(7, _bmkeyItemId);
		}
		if (has_eventKeyItemId()){
			output.wirteInt32(8, _eventKeyItemId);
		}
		if (has_dungeon_type()){
			output.writeEnum(9, _dungeon_type.toInt());
		}
		if (has_enable_boost_mode()){
			output.writeBool(10, _enable_boost_mode);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_keyItemId(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_minPlayer(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_maxPlayer(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_minAdena(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_maxAdena(input.readInt32());
					break;
				}
				case 0x00000030:{
					add_minLevel(input.readInt32());
					break;
				}
				case 0x00000038:{
					set_bmkeyItemId(input.readInt32());
					break;
				}
				case 0x00000040:{
					set_eventKeyItemId(input.readInt32());
					break;
				}
				case 0x00000048:{
					set_dungeon_type(eDungeonType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000050:{
					set_enable_boost_mode(input.readBool());
					break;
				}
				default:{
					System.out.println(String.format("[IndunInfoForClient] NEW_TAG : TAG(%d)", tag));
					return this;
				}
			}
		}
		return this;
	}

	@Override
	public void dispose(){
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}

