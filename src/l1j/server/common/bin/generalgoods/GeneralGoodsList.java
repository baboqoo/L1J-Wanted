package l1j.server.common.bin.generalgoods;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class GeneralGoodsList implements ProtoMessage{
	public static GeneralGoodsList newInstance(){
		return new GeneralGoodsList();
	}
	private java.util.LinkedList<Integer> _NameId;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private GeneralGoodsList(){
	}
	public java.util.LinkedList<Integer> get_NameId(){
		return _NameId;
	}
	public void add_NameId(int val){
		if(!has_NameId()){
			_NameId = new java.util.LinkedList<Integer>();
			_bit |= 0x1;
		}
		_NameId.add(val);
	}
	public boolean has_NameId(){
		return (_bit & 0x1) == 0x1;
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
		if (has_NameId()){
			for(int val : _NameId){
				size += ProtoOutputStream.computeUInt32Size(1, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_NameId()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_NameId()){
			for (int val : _NameId){
				output.writeUInt32(1, val);
			}
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					add_NameId(input.readUInt32());
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
}

