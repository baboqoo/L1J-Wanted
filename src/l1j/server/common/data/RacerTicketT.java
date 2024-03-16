package l1j.server.common.data;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class RacerTicketT implements ProtoMessage{
	public static RacerTicketT newInstance(){
		return new RacerTicketT();
	}
	private int _laneId;
	private String _name;
	private int _condition;
	private double _winRate;
	private int _price;
	private int _racerId;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private RacerTicketT(){
	}
	public int get_laneId(){
		return _laneId;
	}
	public void set_laneId(int val){
		_bit |= 0x1;
		_laneId = val;
	}
	public boolean has_laneId(){
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
	public double get_winRate(){
		return _winRate;
	}
	public void set_winRate(double val){
		_bit |= 0x8;
		_winRate = val;
	}
	public boolean has_winRate(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_price(){
		return _price;
	}
	public void set_price(int val){
		_bit |= 0x10;
		_price = val;
	}
	public boolean has_price(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_racerId(){
		return _racerId;
	}
	public void set_racerId(int val){
		_bit |= 0x20;
		_racerId = val;
	}
	public boolean has_racerId(){
		return (_bit & 0x20) == 0x20;
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
		if (has_laneId()){
			size += ProtoOutputStream.computeInt32Size(1, _laneId);
		}
		if (has_name()){
			size += ProtoOutputStream.computeStringSize(2, _name);
		}
		if (has_condition()){
			size += ProtoOutputStream.computeInt32Size(3, _condition);
		}
		if (has_winRate()){
			size += ProtoOutputStream.computeDoubleSize(4, _winRate);
		}
		if (has_price()){
			size += ProtoOutputStream.computeInt32Size(5, _price);
		}
		if (has_racerId()){
			size += ProtoOutputStream.computeInt32Size(6, _racerId);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_laneId()){
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
		if (!has_winRate()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_price()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_racerId()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_laneId()){
			output.wirteInt32(1, _laneId);
		}
		if (has_name()){
			output.writeString(2, _name);
		}
		if (has_condition()){
			output.wirteInt32(3, _condition);
		}
		if (has_winRate()){
			output.writeDouble(4, _winRate);
		}
		if (has_price()){
			output.wirteInt32(5, _price);
		}
		if (has_racerId()){
			output.wirteInt32(6, _racerId);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_laneId(input.readInt32());
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
				case 0x00000021:{
					set_winRate(input.readDouble());
					break;
				}
				case 0x00000028:{
					set_price(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_racerId(input.readInt32());
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

