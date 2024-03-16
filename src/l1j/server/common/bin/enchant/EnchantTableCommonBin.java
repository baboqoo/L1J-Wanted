package l1j.server.common.bin.enchant;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class EnchantTableCommonBin implements ProtoMessage{
	public static EnchantTableCommonBin newInstance() {
		return new EnchantTableCommonBin();
	}
	
	private EnchantTableCommonBinExtend _enchant;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private EnchantTableCommonBin() {
	}

	public EnchantTableCommonBinExtend get_enchant() {
		return _enchant;
	}

	public void set_enchant(EnchantTableCommonBinExtend val) {
		_enchant = val;
		_bit |= 0x1;
	}

	public boolean has_enchant() {
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
		if (has_enchant()) {
			size += ProtoOutputStream.computeMessageSize(1, _enchant);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_enchant()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_enchant()){
			output.writeMessage(1, _enchant);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x00000012: {
				set_enchant((EnchantTableCommonBinExtend) input.readMessage(EnchantTableCommonBinExtend.newInstance()));
				break;
			}
			default: {
				System.out.println(String.format("[EnchantTableCommonBin] NEW_TAG : TAG(%d)", tag));
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
	
	public static class EnchantTableCommonBinExtend implements ProtoMessage {
		public static EnchantTableCommonBinExtend newInstance() {
			return new EnchantTableCommonBinExtend();
		}
		private int _id;
		private AccessoryEnchantTableT _enchant;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private EnchantTableCommonBinExtend() {
		}
		
		public int get_id(){
			return _id;
		}
		public void set_id(int id){
			_id = id;
			_bit |= 0x1;
		}
		public boolean has_id(){
			return (_bit & 0x1) == 0x1;
		}

		public AccessoryEnchantTableT get_enchant() {
			return _enchant;
		}

		public void set_enchant(AccessoryEnchantTableT val) {
			_bit |= 0x2;
			_enchant = val;
		}

		public boolean has_enchant() {
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
			if (has_id()){
				size += ProtoOutputStream.computeUInt32Size(1, _id);
			}
			if (has_enchant()) {
				size += ProtoOutputStream.computeMessageSize(2, _enchant);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_id()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_enchant()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_id()){
				output.writeUInt32(1, _id);
			}
			if (has_enchant()){
				output.writeMessage(2, _enchant);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
			
			while (!input.isAtEnd()) {
				int tag = input.readTag();
				switch (tag) {
				case 0x00000008: {
					set_id(input.readInt32());
					break;
				}
				case 0x00000012: {
					set_enchant((AccessoryEnchantTableT) input.readMessage(AccessoryEnchantTableT.newInstance()));
					break;
				}
				default: {
					System.out.println(String.format("[EnchantTableCommonBinExtend] NEW_TAG : TAG(%d)", tag));
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
}

