package l1j.server.common.bin.npc;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class DropItemT implements ProtoMessage {
	public static DropItemT newInstance(){
		return new DropItemT();
	}
	private int _name_id;
	private int _bless;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private DropItemT(){
	}
	public int get_name_id(){
		return _name_id;
	}
	public void set_name_id(int val){
		_bit |= 0x1;
		_name_id = val;
	}
	public boolean has_name_id(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_bless(){
		return _bless;
	}
	public void set_bless(int val){
		_bit |= 0x2;
		_bless = val;
	}
	public boolean has_bless(){
		return (_bit & 0x2) == 0x2;
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
		if (has_name_id()){
			size += ProtoOutputStream.computeUInt32Size(1, _name_id);
		}
		if (has_bless()){
			size += ProtoOutputStream.computeInt32Size(2, _bless);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_name_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_name_id()){
			output.writeUInt32(1, _name_id);
		}
		if (has_bless()){
			output.wirteInt32(2, _bless);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_name_id(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_bless(input.readInt32());
					break;
				}
				default:{
					System.out.println(String.format("[DropItemT] NEW_TAG : TAG(%d)", tag));
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

