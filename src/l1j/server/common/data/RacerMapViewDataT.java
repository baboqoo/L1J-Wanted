package l1j.server.common.data;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class RacerMapViewDataT implements ProtoMessage{
	public static RacerMapViewDataT newInstance(){
		return new RacerMapViewDataT();
	}
	private RacerInfoT _racerInfo;
	private String _name;
	private int _condition;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private RacerMapViewDataT(){
	}
	public RacerInfoT get_racerInfo(){
		return _racerInfo;
	}
	public void set_racerInfo(RacerInfoT val){
		_bit |= 0x1;
		_racerInfo = val;
	}
	public boolean has_racerInfo(){
		return (_bit & 0x1) == 0x1;
	}
	public String get_name(){
		return _name;
	}
	public void set_name(String val){
		_bit |= 0x2;
		_name = val;
	}
	public boolean has_name(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_condition(){
		return _condition;
	}
	public void set_condition(int val){
		_bit |= 0x4;
		_condition = val;
	}
	public boolean has_condition(){
		return (_bit & 0x4) == 0x4;
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
		if (has_racerInfo()){
			size += ProtoOutputStream.computeMessageSize(1, _racerInfo);
		}
		if (has_name()){
			size += ProtoOutputStream.computeStringSize(2, _name);
		}
		if (has_condition()){
			size += ProtoOutputStream.computeInt32Size(3, _condition);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_racerInfo()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_condition()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_racerInfo()){
			output.writeMessage(1, _racerInfo);
		}
		if (has_name()){
			output.writeString(2, _name);
		}
		if (has_condition()){
			output.wirteInt32(3, _condition);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x0000000A:{
					set_racerInfo((RacerInfoT)input.readMessage(RacerInfoT.newInstance()));
					break;
				}
				case 0x00000012:{
					set_name(input.readString());
					break;
				}
				case 0x00000018:{
					set_condition(input.readInt32());
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

