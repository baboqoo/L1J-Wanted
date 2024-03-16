package l1j.server.common.data;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class CommonBonusDescription implements ProtoMessage{
	public static CommonBonusDescription newInstance(){
		return new CommonBonusDescription();
	}
	private String _name;
	private int _enum_desc;
	private java.util.LinkedList<String> _value;
	private int _user_Level;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CommonBonusDescription(){
	}
	public String get_name(){
		return _name;
	}
	public void set_name(String val){
		_bit |= 0x1;
		_name = val;
	}
	public boolean has_name(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_enum_desc(){
		return _enum_desc;
	}
	public void set_enum_desc(int val){
		_bit |= 0x2;
		_enum_desc = val;
	}
	public boolean has_enum_desc(){
		return (_bit & 0x2) == 0x2;
	}
	public java.util.LinkedList<String> get_value(){
		return _value;
	}
	public void add_value(String val){
		if(!has_value()){
			_value = new java.util.LinkedList<String>();
			_bit |= 0x4;
		}
		_value.add(val);
	}
	public boolean has_value(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_user_Level(){
		return _user_Level;
	}
	public void set_user_Level(int val){
		_bit |= 0x8;
		_user_Level = val;
	}
	public boolean has_user_Level(){
		return (_bit & 0x8) == 0x8;
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
		if (has_name()){
			size += ProtoOutputStream.computeStringSize(1, _name);
		}
		if (has_enum_desc()){
			size += ProtoOutputStream.computeUInt32Size(2, _enum_desc);
		}
		if (has_value()){
			for(String val : _value){
				size += ProtoOutputStream.computeStringSize(3, val);
			}
		}
		if (has_user_Level()){
			size += ProtoOutputStream.computeUInt32Size(4, _user_Level);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_enum_desc()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_value()){
//			for(String val : _value){
//				if (!val.isInitialized()){
//					_memorizedIsInitialized = -1;
//					return false;
//				}
//			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_name()){
			output.writeString(1, _name);
		}
		if (has_enum_desc()){
			output.writeUInt32(2, _enum_desc);
		}
		if (has_value()){
			for (String val : _value){
				output.writeString(3, val);
			}
		}
		if (has_user_Level()){
			output.writeUInt32(4, _user_Level);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x0000000A:{
					set_name(input.readString());
					break;
				}
				case 0x00000010:{
					set_enum_desc(input.readUInt32());
					break;
				}
				case 0x0000001A:{
					add_value(input.readString());
					break;
				}
				case 0x00000020:{
					set_user_Level(input.readUInt32());
					break;
				}
				default:{
					System.out.println(String.format("[CommonBonusDescription] NEW_TAG : TAG(%d)", tag));
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

