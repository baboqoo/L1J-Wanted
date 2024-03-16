package l1j.server.common.bin.spell;

import java.util.HashMap;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class PassiveSpellCommonBin implements ProtoMessage {
	public static PassiveSpellCommonBin newInstance() {
		return new PassiveSpellCommonBin();
	}
	
	private HashMap<Integer, SpellCommonBinExtend> _spell_list;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private PassiveSpellCommonBin() {
	}

	public HashMap<Integer, SpellCommonBinExtend> get_spell_list() {
		return _spell_list;
	}

	public SpellCommonBinExtend getSpell(int name_id) {
		return _spell_list.get(name_id);
	}

	public void add_spell(SpellCommonBinExtend val) {
		if (!has_spell_list()) {
			_spell_list = new HashMap<Integer, SpellCommonBinExtend>();
			_bit |= 0x1;
		}
		_spell_list.put(val.get_spell_id(), val);
	}

	public boolean has_spell_list() {
		return (_bit & 0x1) == 0x1;
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
		if (has_spell_list()){
			for (SpellCommonBinExtend val : _spell_list.values()) {
				size += ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_spell_list()) {
			for (SpellCommonBinExtend val : _spell_list.values()) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_spell_list()){
			for (SpellCommonBinExtend val : _spell_list.values()) {
				output.writeMessage(1, val);
			}
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x00000012: {
				add_spell((SpellCommonBinExtend) input.readMessage(SpellCommonBinExtend.newInstance()));
				break;
			}
			default: {
				System.out.println(String.format("[PassiveSpellCommonBin] NEW_TAG : TAG(%d)", tag));
				return this;
			}
			}
		}
		return this;
	}

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}

