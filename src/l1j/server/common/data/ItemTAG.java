package l1j.server.common.data;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class ItemTAG implements ProtoMessage{
	public static ItemTAG newInstance(){
		return new ItemTAG();
	}
	private int _NameID;
	private int _Count;
	private DELETE_TYPE _Delete;
	private ItemTAG.BlessTYPE _Bless;
	private int _Enchant;
	private boolean _Random;
	private int _Prob;
	private int _Ident;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private ItemTAG(){
		set_Delete(DELETE_TYPE.DELETE_NOTHING);
		set_Bless(ItemTAG.BlessTYPE.NORMAL);
	}
	public int get_NameID(){
		return _NameID;
	}
	public void set_NameID(int val){
		_bit |= 0x1;
		_NameID = val;
	}
	public boolean has_NameID(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_Count(){
		return _Count;
	}
	public void set_Count(int val){
		_bit |= 0x2;
		_Count = val;
	}
	public boolean has_Count(){
		return (_bit & 0x2) == 0x2;
	}
	public DELETE_TYPE get_Delete(){
		return _Delete;
	}
	public void set_Delete(DELETE_TYPE val){
		_bit |= 0x4;
		_Delete = val;
	}
	public boolean has_Delete(){
		return (_bit & 0x4) == 0x4;
	}
	public ItemTAG.BlessTYPE get_Bless(){
		return _Bless;
	}
	public void set_Bless(ItemTAG.BlessTYPE val){
		_bit |= 0x8;
		_Bless = val;
	}
	public boolean has_Bless(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_Enchant(){
		return _Enchant;
	}
	public void set_Enchant(int val){
		_bit |= 0x10;
		_Enchant = val;
	}
	public boolean has_Enchant(){
		return (_bit & 0x10) == 0x10;
	}
	public boolean get_Random(){
		return _Random;
	}
	public void set_Random(boolean val){
		_bit |= 0x20;
		_Random = val;
	}
	public boolean has_Random(){
		return (_bit & 0x20) == 0x20;
	}
	public int get_Prob(){
		return _Prob;
	}
	public void set_Prob(int val){
		_bit |= 0x40;
		_Prob = val;
	}
	public boolean has_Prob(){
		return (_bit & 0x40) == 0x40;
	}
	public int get_Ident(){
		return _Ident;
	}
	public void set_Ident(int val){
		_bit |= 0x80;
		_Ident = val;
	}
	public boolean has_Ident(){
		return (_bit & 0x80) == 0x80;
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
		if (has_NameID()){
			size += ProtoOutputStream.computeInt32Size(1, _NameID);
		}
		if (has_Count()){
			size += ProtoOutputStream.computeInt32Size(2, _Count);
		}
		if (has_Delete()){
			size += ProtoOutputStream.computeEnumSize(3, _Delete.toInt());
		}
		if (has_Bless()){
			size += ProtoOutputStream.computeEnumSize(4, _Bless.toInt());
		}
		if (has_Enchant()){
			size += ProtoOutputStream.computeInt32Size(5, _Enchant);
		}
		if (has_Random()){
			size += ProtoOutputStream.computeBoolSize(6, _Random);
		}
		if (has_Prob()){
			size += ProtoOutputStream.computeInt32Size(7, _Prob);
		}
		if (has_Ident()){
			size += ProtoOutputStream.computeInt32Size(8, _Ident);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_NameID()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_Count()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_NameID()){
			output.wirteInt32(1, _NameID);
		}
		if (has_Count()){
			output.wirteInt32(2, _Count);
		}
		if (has_Delete()){
			output.writeEnum(3, _Delete.toInt());
		}
		if (has_Bless()){
			output.writeEnum(4, _Bless.toInt());
		}
		if (has_Enchant()){
			output.wirteInt32(5, _Enchant);
		}
		if (has_Random()){
			output.writeBool(6, _Random);
		}
		if (has_Prob()){
			output.wirteInt32(7, _Prob);
		}
		if (has_Ident()){
			output.wirteInt32(8, _Ident);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_NameID(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_Count(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_Delete(DELETE_TYPE.fromInt(input.readEnum()));
					break;
				}
				case 0x00000020:{
					set_Bless(ItemTAG.BlessTYPE.fromInt(input.readEnum()));
					break;
				}
				case 0x00000028:{
					set_Enchant(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_Random(input.readBool());
					break;
				}
				case 0x00000038:{
					set_Prob(input.readInt32());
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
	public enum BlessTYPE{
		BLESSED(0),
		NORMAL(1),
		CURSED(2),
		UNIDENTIFIED(3),
		;
		private int value;
		BlessTYPE(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(BlessTYPE v){
			return value == v.value;
		}
		public static BlessTYPE fromInt(int i){
			switch(i){
			case 0:
				return BLESSED;
			case 1:
				return NORMAL;
			case 2:
				return CURSED;
			case 3:
				return UNIDENTIFIED;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments BlessTYPE, %d", i));
			}
		}
	}
}

