package l1j.server.common.bin.craft;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class CraftRequiredSpriteList implements ProtoMessage {
	public static CraftRequiredSpriteList newInstance() {
		return new CraftRequiredSpriteList();
	}

	private int _count;
	private java.util.LinkedList<Integer> _sprite_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CraftRequiredSpriteList() {
	}

	public int get_count() {
		return _count;
	}

	public void set_count(int val) {
		_bit |= 0x1;
		_count = val;
	}

	public boolean has_count() {
		return (_bit & 0x1) == 0x1;
	}

	public java.util.LinkedList<Integer> get_sprite_id() {
		return _sprite_id;
	}

	public void add_sprite_id(int val) {
		if (!has_sprite_id()) {
			_sprite_id = new java.util.LinkedList<Integer>();
			_bit |= 0x2;
		}
		_sprite_id.add(val);
	}

	public boolean has_sprite_id() {
		return (_bit & 0x2) == 0x2;
	}

	@Override
	public long getInitializeBit() {
		return (long) _bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_count()){
			size += ProtoOutputStream.computeInt32Size(1, _count);
		}
		if (has_sprite_id()){
			for(int val : _sprite_id){
				size += ProtoOutputStream.computeInt32Size(2, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_count()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_sprite_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_count()){
			output.wirteInt32(1, _count);
		}
		if (has_sprite_id()){
			for (int val : _sprite_id){
				output.wirteInt32(2, val);
			}
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x00000008: {
				set_count(input.readInt32());
				break;
			}
			case 0x00000010: {
				add_sprite_id(input.readInt32());
				break;
			}
			default: {
				System.out.println(String.format("[CraftRequiredSpriteList] NEW_TAG : TAG(%d)", tag));
				return this;
			}
			}
		}
		return this;
	}

	@Override
	public void dispose() {
		if (has_sprite_id()) {
			_sprite_id.clear();
			_sprite_id = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}

