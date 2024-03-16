package l1j.server.common.data;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class ElementalResistance implements ProtoMessage {
	public static ElementalResistance newInstance(){
		return new ElementalResistance();
	}
	private int _fire;
	private int _water;
	private int _air;
	private int _earth;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private ElementalResistance(){
	}
	public int get_fire(){
		return _fire;
	}
	public void set_fire(int val){
		_bit |= 0x1;
		_fire = val;
	}
	public boolean has_fire(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_water(){
		return _water;
	}
	public void set_water(int val){
		_bit |= 0x2;
		_water = val;
	}
	public boolean has_water(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_air(){
		return _air;
	}
	public void set_air(int val){
		_bit |= 0x4;
		_air = val;
	}
	public boolean has_air(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_earth(){
		return _earth;
	}
	public void set_earth(int val){
		_bit |= 0x8;
		_earth = val;
	}
	public boolean has_earth(){
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
		if (has_fire()){
			size += ProtoOutputStream.computeInt32Size(1, _fire);
		}
		if (has_water()){
			size += ProtoOutputStream.computeInt32Size(2, _water);
		}
		if (has_air()){
			size += ProtoOutputStream.computeInt32Size(3, _air);
		}
		if (has_earth()){
			size += ProtoOutputStream.computeInt32Size(4, _earth);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_fire()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_water()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_air()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_earth()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_fire()){
			output.wirteInt32(1, _fire);
		}
		if (has_water()){
			output.wirteInt32(2, _water);
		}
		if (has_air()){
			output.wirteInt32(3, _air);
		}
		if (has_earth()){
			output.wirteInt32(4, _earth);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_fire(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_water(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_air(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_earth(input.readInt32());
					break;
				}
				default:{
					System.out.println(String.format("[ElementalResistance] NEW_TAG : TAG(%d)", tag));
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

