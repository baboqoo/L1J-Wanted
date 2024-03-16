package l1j.server.common.bin.spell;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class SpellCommonBinExtend implements ProtoMessage {
	public static SpellCommonBinExtend newInstance() {
		return new SpellCommonBinExtend();
	}
	
	private int _spell_id;
	private CommonSpellInfo _spell;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SpellCommonBinExtend() {
	}

	public int get_spell_id(){
		return _spell_id;
	}
	
	public void set_spell_id(int spell_id){
		_spell_id = spell_id;
		_bit |= 0x1;
	}
	
	public boolean has_spell_id() {
		return (_bit & 0x1) == 0x1;
	}
	
	public CommonSpellInfo get_spell() {
		return _spell;
	}

	public void set_spell(CommonSpellInfo val) {
		_spell = val;
		_bit |= 0x2;
	}

	public boolean has_spell() {
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
		if (has_spell_id()){
			size += ProtoOutputStream.computeUInt32Size(1, _spell_id);
		}
		if (has_spell()) {
			size += ProtoOutputStream.computeMessageSize(2, _spell);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_spell_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_spell()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_spell_id()){
			output.writeUInt32(1, _spell_id);
		}
		if (has_spell()){
			output.writeMessage(2, _spell);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x00000008: {
				set_spell_id(input.readUInt32());
				break;
			}
			case 0x00000012: {
				set_spell((CommonSpellInfo) input.readMessage(CommonSpellInfo.newInstance()));
				break;
			}
			default: {
				System.out.println(String.format("[SpellCommonBinExtend] NEW_TAG : TAG(%d)", tag));
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

