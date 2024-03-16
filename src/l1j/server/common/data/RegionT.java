package l1j.server.common.data;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class RegionT implements ProtoMessage{
	public static RegionT newInstance(){
		return new RegionT();
	}
	private int _OriginalMapID;
	private int _SX;
	private int _SY;
	private int _EX;
	private int _EY;
	private int _MapID;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private RegionT(){
		set_SX(0);
		set_SY(0);
		set_EX(0);
		set_EY(0);
	}
	public int get_OriginalMapID(){
		return _OriginalMapID;
	}
	public void set_OriginalMapID(int val){
		_bit |= 0x1;
		_OriginalMapID = val;
	}
	public boolean has_OriginalMapID(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_SX(){
		return _SX;
	}
	public void set_SX(int val){
		_bit |= 0x2;
		_SX = val;
	}
	public boolean has_SX(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_SY(){
		return _SY;
	}
	public void set_SY(int val){
		_bit |= 0x4;
		_SY = val;
	}
	public boolean has_SY(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_EX(){
		return _EX;
	}
	public void set_EX(int val){
		_bit |= 0x8;
		_EX = val;
	}
	public boolean has_EX(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_EY(){
		return _EY;
	}
	public void set_EY(int val){
		_bit |= 0x10;
		_EY = val;
	}
	public boolean has_EY(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_MapID(){
		return _MapID;
	}
	public void set_MapID(int val){
		_bit |= 0x200;
		_MapID = val;
	}
	public boolean has_MapID(){
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
		if (has_OriginalMapID()){
			size += ProtoOutputStream.computeUInt32Size(1, _OriginalMapID);
		}
		if (has_SX()){
			size += ProtoOutputStream.computeUInt32Size(2, _SX);
		}
		if (has_SY()){
			size += ProtoOutputStream.computeUInt32Size(3, _SY);
		}
		if (has_EX()){
			size += ProtoOutputStream.computeUInt32Size(4, _EX);
		}
		if (has_EY()){
			size += ProtoOutputStream.computeUInt32Size(5, _EY);
		}
		if (has_MapID()){
			size += ProtoOutputStream.computeUInt32Size(10, _MapID);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_OriginalMapID()){
			output.writeUInt32(1, _OriginalMapID);
		}
		if (has_SX()){
			output.writeUInt32(2, _SX);
		}
		if (has_SY()){
			output.writeUInt32(3, _SY);
		}
		if (has_EX()){
			output.writeUInt32(4, _EX);
		}
		if (has_EY()){
			output.writeUInt32(5, _EY);
		}
		if (has_MapID()){
			output.writeUInt32(10, _MapID);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_OriginalMapID(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_SX(input.readUInt32());
					break;
				}
				case 0x00000018:{
					set_SY(input.readUInt32());
					break;
				}
				case 0x00000020:{
					set_EX(input.readUInt32());
					break;
				}
				case 0x00000028:{
					set_EY(input.readUInt32());
					break;
				}
				case 0x00000050:{
					set_MapID(input.readUInt32());
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

