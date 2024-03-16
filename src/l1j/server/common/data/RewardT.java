package l1j.server.common.data;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class RewardT implements ProtoMessage{
	public static RewardT newInstance(){
		return new RewardT();
	}
	private RewardT.eType _Type;
	private int _AssetID;
	private long _Amount;
	private boolean _HighlightReward;
	private int _WorldNumber;
	private int _X;
	private int _Y;
	private int _Direction;
	private int _BuffDuration;
	private int _BuffGroupIndex;
	private String _StatueHtml;
	private String _StatueObjectName;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private RewardT(){
		set_AssetID(0);
		set_Amount(1);
	}
	public RewardT.eType get_Type(){
		return _Type;
	}
	public void set_Type(RewardT.eType val){
		_bit |= 0x1;
		_Type = val;
	}
	public boolean has_Type(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_AssetID(){
		return _AssetID;
	}
	public void set_AssetID(int val){
		_bit |= 0x2;
		_AssetID = val;
	}
	public boolean has_AssetID(){
		return (_bit & 0x2) == 0x2;
	}
	public long get_Amount(){
		return _Amount;
	}
	public void set_Amount(long val){
		_bit |= 0x4;
		_Amount = val;
	}
	public boolean has_Amount(){
		return (_bit & 0x4) == 0x4;
	}
	public boolean get_HighlightReward(){
		return _HighlightReward;
	}
	public void set_HighlightReward(boolean val){
		_bit |= 0x8;
		_HighlightReward = val;
	}
	public boolean has_HighlightReward(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_WorldNumber(){
		return _WorldNumber;
	}
	public void set_WorldNumber(int val){
		_bit |= 0x10;
		_WorldNumber = val;
	}
	public boolean has_WorldNumber(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_X(){
		return _X;
	}
	public void set_X(int val){
		_bit |= 0x20;
		_X = val;
	}
	public boolean has_X(){
		return (_bit & 0x20) == 0x20;
	}
	public int get_Y(){
		return _Y;
	}
	public void set_Y(int val){
		_bit |= 0x40;
		_Y = val;
	}
	public boolean has_Y(){
		return (_bit & 0x40) == 0x40;
	}
	public int get_Direction(){
		return _Direction;
	}
	public void set_Direction(int val){
		_bit |= 0x80;
		_Direction = val;
	}
	public boolean has_Direction(){
		return (_bit & 0x80) == 0x80;
	}
	public int get_BuffDuration(){
		return _BuffDuration;
	}
	public void set_BuffDuration(int val){
		_bit |= 0x100;
		_BuffDuration = val;
	}
	public boolean has_BuffDuration(){
		return (_bit & 0x100) == 0x100;
	}
	public int get_BuffGroupIndex(){
		return _BuffGroupIndex;
	}
	public void set_BuffGroupIndex(int val){
		_bit |= 0x200;
		_BuffGroupIndex = val;
	}
	public boolean has_BuffGroupIndex(){
		return (_bit & 0x200) == 0x200;
	}
	public String get_StatueHtml(){
		return _StatueHtml;
	}
	public void set_StatueHtml(String val){
		_bit |= 0x400;
		_StatueHtml = val;
	}
	public boolean has_StatueHtml(){
		return (_bit & 0x400) == 0x400;
	}
	public String get_StatueObjectName(){
		return _StatueObjectName;
	}
	public void set_StatueObjectName(String val){
		_bit |= 0x800;
		_StatueObjectName = val;
	}
	public boolean has_StatueObjectName(){
		return (_bit & 0x800) == 0x800;
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
		if (has_Type()){
			size += ProtoOutputStream.computeEnumSize(1, _Type.toInt());
		}
		if (has_AssetID()){
			size += ProtoOutputStream.computeUInt32Size(2, _AssetID);
		}
		if (has_Amount()){
			size += ProtoOutputStream.computeUInt64Size(3, _Amount);
		}
		if (has_HighlightReward()){
			size += ProtoOutputStream.computeBoolSize(4, _HighlightReward);
		}
		if (has_WorldNumber()){
			size += ProtoOutputStream.computeInt32Size(5, _WorldNumber);
		}
		if (has_X()){
			size += ProtoOutputStream.computeInt32Size(6, _X);
		}
		if (has_Y()){
			size += ProtoOutputStream.computeInt32Size(7, _Y);
		}
		if (has_Direction()){
			size += ProtoOutputStream.computeInt32Size(8, _Direction);
		}
		if (has_BuffDuration()){
			size += ProtoOutputStream.computeInt32Size(9, _BuffDuration);
		}
		if (has_BuffGroupIndex()){
			size += ProtoOutputStream.computeInt32Size(10, _BuffGroupIndex);
		}
		if (has_StatueHtml()){
			size += ProtoOutputStream.computeStringSize(11, _StatueHtml);
		}
		if (has_StatueObjectName()){
			size += ProtoOutputStream.computeStringSize(12, _StatueObjectName);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_Type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_Type()){
			output.writeEnum(1, _Type.toInt());
		}
		if (has_AssetID()){
			output.writeUInt32(2, _AssetID);
		}
		if (has_Amount()){
			output.wirteUInt64(3, _Amount);
		}
		if (has_HighlightReward()){
			output.writeBool(4, _HighlightReward);
		}
		if (has_WorldNumber()){
			output.wirteInt32(5, _WorldNumber);
		}
		if (has_X()){
			output.wirteInt32(6, _X);
		}
		if (has_Y()){
			output.wirteInt32(7, _Y);
		}
		if (has_Direction()){
			output.wirteInt32(8, _Direction);
		}
		if (has_BuffDuration()){
			output.wirteInt32(9, _BuffDuration);
		}
		if (has_BuffGroupIndex()){
			output.wirteInt32(10, _BuffGroupIndex);
		}
		if (has_StatueHtml()){
			output.writeString(11, _StatueHtml);
		}
		if (has_StatueObjectName()){
			output.writeString(12, _StatueObjectName);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_Type(RewardT.eType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_AssetID(input.readUInt32());
					break;
				}
				case 0x00000018:{
					set_Amount(input.readUInt64());
					break;
				}
				case 0x00000020:{
					set_HighlightReward(input.readBool());
					break;
				}
				case 0x00000028:{
					set_WorldNumber(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_X(input.readInt32());
					break;
				}
				case 0x00000038:{
					set_Y(input.readInt32());
					break;
				}
				case 0x00000040:{
					set_Direction(input.readInt32());
					break;
				}
				case 0x00000048:{
					set_BuffDuration(input.readInt32());
					break;
				}
				case 0x00000050:{
					set_BuffGroupIndex(input.readInt32());
					break;
				}
				case 0x0000005A:{
					set_StatueHtml(input.readString());
					break;
				}
				case 0x00000062:{
					set_StatueObjectName(input.readString());
					break;
				}
				default:{
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
	public enum eType{
		ITEM(1),
		EXP(2),
		SPELL_BUFF(3),
		AUTO_USE_ITEM(4),
		BUFF_STATUE(5),
		;
		private int value;
		eType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eType v){
			return value == v.value;
		}
		public static eType fromInt(int i){
			switch(i){
			case 1:
				return ITEM;
			case 2:
				return EXP;
			case 3:
				return SPELL_BUFF;
			case 4:
				return AUTO_USE_ITEM;
			case 5:
				return BUFF_STATUE;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eType, %d", i));
			}
		}
	}
}

