package l1j.server.common.bin.spell;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class CompanionSpellBuff implements ProtoMessage{
	public static CompanionSpellBuff newInstance(){
		return new CompanionSpellBuff();
	}
	private int _on_icon_id;
	private int _off_icon_id;
	private int _icon_priority;
	private int _tooltip_str_id;
	private int _new_str_id;
	private int _end_str_id;
	private int _is_good;
	private int _duration_show_type;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CompanionSpellBuff(){
	}
	public int get_on_icon_id(){
		return _on_icon_id;
	}
	public void set_on_icon_id(int val){
		_bit |= 0x1;
		_on_icon_id = val;
	}
	public boolean has_on_icon_id(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_off_icon_id(){
		return _off_icon_id;
	}
	public void set_off_icon_id(int val){
		_bit |= 0x2;
		_off_icon_id = val;
	}
	public boolean has_off_icon_id(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_icon_priority(){
		return _icon_priority;
	}
	public void set_icon_priority(int val){
		_bit |= 0x4;
		_icon_priority = val;
	}
	public boolean has_icon_priority(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_tooltip_str_id(){
		return _tooltip_str_id;
	}
	public void set_tooltip_str_id(int val){
		_bit |= 0x8;
		_tooltip_str_id = val;
	}
	public boolean has_tooltip_str_id(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_new_str_id(){
		return _new_str_id;
	}
	public void set_new_str_id(int val){
		_bit |= 0x10;
		_new_str_id = val;
	}
	public boolean has_new_str_id(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_end_str_id(){
		return _end_str_id;
	}
	public void set_end_str_id(int val){
		_bit |= 0x20;
		_end_str_id = val;
	}
	public boolean has_end_str_id(){
		return (_bit & 0x20) == 0x20;
	}
	public int get_is_good(){
		return _is_good;
	}
	public void set_is_good(int val){
		_bit |= 0x40;
		_is_good = val;
	}
	public boolean has_is_good(){
		return (_bit & 0x40) == 0x40;
	}
	public int get_duration_show_type(){
		return _duration_show_type;
	}
	public void set_duration_show_type(int val){
		_bit |= 0x80;
		_duration_show_type = val;
	}
	public boolean has_duration_show_type(){
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
		if (has_on_icon_id()){
			size += ProtoOutputStream.computeInt32Size(1, _on_icon_id);
		}
		if (has_off_icon_id()){
			size += ProtoOutputStream.computeInt32Size(2, _off_icon_id);
		}
		if (has_icon_priority()){
			size += ProtoOutputStream.computeInt32Size(3, _icon_priority);
		}
		if (has_tooltip_str_id()){
			size += ProtoOutputStream.computeInt32Size(4, _tooltip_str_id);
		}
		if (has_new_str_id()){
			size += ProtoOutputStream.computeInt32Size(5, _new_str_id);
		}
		if (has_end_str_id()){
			size += ProtoOutputStream.computeInt32Size(6, _end_str_id);
		}
		if (has_is_good()){
			size += ProtoOutputStream.computeInt32Size(7, _is_good);
		}
		if (has_duration_show_type()){
			size += ProtoOutputStream.computeInt32Size(8, _duration_show_type);
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
		if (has_on_icon_id()){
			output.wirteInt32(1, _on_icon_id);
		}
		if (has_off_icon_id()){
			output.wirteInt32(2, _off_icon_id);
		}
		if (has_icon_priority()){
			output.wirteInt32(3, _icon_priority);
		}
		if (has_tooltip_str_id()){
			output.wirteInt32(4, _tooltip_str_id);
		}
		if (has_new_str_id()){
			output.wirteInt32(5, _new_str_id);
		}
		if (has_end_str_id()){
			output.wirteInt32(6, _end_str_id);
		}
		if (has_is_good()){
			output.wirteInt32(7, _is_good);
		}
		if (has_duration_show_type()){
			output.wirteInt32(8, _duration_show_type);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_on_icon_id(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_off_icon_id(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_icon_priority(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_tooltip_str_id(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_new_str_id(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_end_str_id(input.readInt32());
					break;
				}
				case 0x00000038:{
					set_is_good(input.readInt32());
					break;
				}
				case 0x00000040:{
					set_duration_show_type(input.readInt32());
					break;
				}
				default:{
					System.out.println(String.format("[CompanionSpellBuff] NEW_TAG : TAG(%d)", tag));
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

