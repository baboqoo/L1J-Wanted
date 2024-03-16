package l1j.server.common.bin.enchant;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class EnchantScrollTableInfoCommonBin implements ProtoMessage {
	public static EnchantScrollTableInfoCommonBin newInstance() {
		return new EnchantScrollTableInfoCommonBin();
	}
	
	private EnchantScrollTableInfoExtend _extend;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private EnchantScrollTableInfoCommonBin() {
	}

	public EnchantScrollTableInfoExtend get_extend() {
		return _extend;
	}

	public void set_extend(EnchantScrollTableInfoExtend val) {
		_extend = val;
		_bit |= 0x1;
	}

	public boolean has_extend() {
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
		if (has_extend()) {
			size += ProtoOutputStream.computeMessageSize(1, _extend);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_extend()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_extend()){
			output.writeMessage(1, _extend);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x00000012: {
				set_extend((EnchantScrollTableInfoExtend) input.readMessage(EnchantScrollTableInfoExtend.newInstance()));
				break;
			}
			default: {
				System.out.println(String.format("[EnchantScrollTableInfoCommonBin] NEW_TAG : TAG(%d)", tag));
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
	
	public static class EnchantScrollTableInfoExtend implements ProtoMessage {
		public static EnchantScrollTableInfoExtend newInstance() {
			return new EnchantScrollTableInfoExtend();
		}
		private int _id;
		private EnchantScrollTableT _enchantScrollT;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private EnchantScrollTableInfoExtend() {
		}
		
		public int getId(){
			return _id;
		}
		public void setId(int id){
			_id = id;
			_bit |= 0x1;
		}
		public boolean hasId(){
			return (_bit & 0x1) == 0x1;
		}

		public EnchantScrollTableT getEnchantScrollT() {
			return _enchantScrollT;
		}

		public void setEnchantScrollT(EnchantScrollTableT val) {
			_bit |= 0x2;
			_enchantScrollT = val;
		}

		public boolean hasEnchantScrollT() {
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
			if (hasId()){
				size += ProtoOutputStream.computeUInt32Size(1, _id);
			}
			if (hasEnchantScrollT()) {
				size += ProtoOutputStream.computeMessageSize(2, _enchantScrollT);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!hasId()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!hasEnchantScrollT()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (hasId()){
				output.writeUInt32(1, _id);
			}
			if (hasEnchantScrollT()){
				output.writeMessage(2, _enchantScrollT);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
			
			while (!input.isAtEnd()) {
				int tag = input.readTag();
				switch (tag) {
				case 0x00000008: {
					setId(input.readInt32());
					break;
				}
				case 0x00000012: {
					setEnchantScrollT((EnchantScrollTableT) input.readMessage(EnchantScrollTableT.newInstance()));
					break;
				}
				default: {
					System.out.println(String.format("[EnchantScrollTableInfoExtend] NEW_TAG : TAG(%d)", tag));
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

