package l1j.server.common.bin.item;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class BmItemProbData implements ProtoMessage{
	public static BmItemProbData newInstance(){
		return new BmItemProbData();
	}
	private int _nameId;
	private int _prob;
	private int _enchant;
	private int _elemental;
	private int _state;
	private int _count;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private BmItemProbData(){
	}
	public int get_nameId(){
		return _nameId;
	}
	public void set_nameId(int val){
		_bit |= 0x1;
		_nameId = val;
	}
	public boolean has_nameId(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_prob(){
		return _prob;
	}
	public void set_prob(int val){
		_bit |= 0x2;
		_prob = val;
	}
	public boolean has_prob(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_enchant(){
		return _enchant;
	}
	public void set_enchant(int val){
		_bit |= 0x4;
		_enchant = val;
	}
	public boolean has_enchant(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_elemental(){
		return _elemental;
	}
	public void set_elemental(int val){
		_bit |= 0x8;
		_elemental = val;
	}
	public boolean has_elemental(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_state(){
		return _state;
	}
	public void set_state(int val){
		_bit |= 0x10;
		_state = val;
	}
	public boolean has_state(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_count(){
		return _count;
	}
	public void set_count(int val){
		_bit |= 0x20;
		_count = val;
	}
	public boolean has_count(){
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
		if (has_nameId()){
			size += ProtoOutputStream.computeInt32Size(1, _nameId);
		}
		if (has_prob()){
			size += ProtoOutputStream.computeInt32Size(2, _prob);
		}
		if (has_enchant()){
			size += ProtoOutputStream.computeInt32Size(3, _enchant);
		}
		if (has_elemental()){
			size += ProtoOutputStream.computeInt32Size(4, _elemental);
		}
		if (has_state()){
			size += ProtoOutputStream.computeInt32Size(5, _state);
		}
		if (has_count()){
			size += ProtoOutputStream.computeInt32Size(6, _count);
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
		if (has_nameId()){
			output.wirteInt32(1, _nameId);
		}
		if (has_prob()){
			output.wirteInt32(2, _prob);
		}
		if (has_enchant()){
			output.wirteInt32(3, _enchant);
		}
		if (has_elemental()){
			output.wirteInt32(4, _elemental);
		}
		if (has_state()){
			output.wirteInt32(5, _state);
		}
		if (has_count()){
			output.wirteInt32(6, _count);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_nameId(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_prob(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_enchant(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_elemental(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_state(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_count(input.readInt32());
					break;
				}
				default:{
					System.out.println(String.format("[BmItemProbData] NEW_TAG : TAG(%d)", tag));
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

