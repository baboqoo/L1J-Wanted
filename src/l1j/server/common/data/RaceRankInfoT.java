package l1j.server.common.data;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class RaceRankInfoT implements ProtoMessage{
	public static RaceRankInfoT newInstance(){
		return new RaceRankInfoT();
	}
	private int _rank;
	private int _serverNumber;
	private String _name;
	private int _weeklyPrize;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private RaceRankInfoT(){
	}
	public int get_rank(){
		return _rank;
	}
	public void set_rank(int val){
		_bit |= 0x1;
		_rank = val;
	}
	public boolean has_rank(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_serverNumber(){
		return _serverNumber;
	}
	public void set_serverNumber(int val){
		_bit |= 0x2;
		_serverNumber = val;
	}
	public boolean has_serverNumber(){
		return (_bit & 0x2) == 0x2;
	}
	public String get_name(){
		return _name;
	}
	public void set_name(String val){
		_bit |= 0x4;
		_name = val;
	}
	public boolean has_name(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_weeklyPrize(){
		return _weeklyPrize;
	}
	public void set_weeklyPrize(int val){
		_bit |= 0x8;
		_weeklyPrize = val;
	}
	public boolean has_weeklyPrize(){
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
		if (has_rank()){
			size += ProtoOutputStream.computeInt32Size(1, _rank);
		}
		if (has_serverNumber()){
			size += ProtoOutputStream.computeInt32Size(2, _serverNumber);
		}
		if (has_name()){
			size += ProtoOutputStream.computeStringSize(3, _name);
		}
		if (has_weeklyPrize()){
			size += ProtoOutputStream.computeUInt32Size(4, _weeklyPrize);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_rank()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_serverNumber()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_name()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_weeklyPrize()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_rank()){
			output.wirteInt32(1, _rank);
		}
		if (has_serverNumber()){
			output.wirteInt32(2, _serverNumber);
		}
		if (has_name()){
			output.writeString(3, _name);
		}
		if (has_weeklyPrize()){
			output.writeUInt32(4, _weeklyPrize);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_rank(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_serverNumber(input.readInt32());
					break;
				}
				case 0x0000001A:{
					set_name(input.readString());
					break;
				}
				case 0x00000020:{
					set_weeklyPrize(input.readUInt32());
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

