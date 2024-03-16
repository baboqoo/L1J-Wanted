package l1j.server.common.bin.portrait;

import java.util.HashMap;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.common.data.PortraitT;

public class PortraitCommonBin implements ProtoMessage {
	public static PortraitCommonBin newInstance() {
		return new PortraitCommonBin();
	}
	
	private HashMap<Integer, PortraitCommonBinExtend> _portrait_list;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private PortraitCommonBin() {
	}

	public HashMap<Integer, PortraitCommonBinExtend> get_portrait_list() {
		return _portrait_list;
	}

	public PortraitCommonBinExtend get_portrait(int asset_id) {
		return _portrait_list.get(asset_id);
	}

	public void add_portrait(PortraitCommonBinExtend val) {
		if (!has_portrait_list()) {
			_portrait_list = new HashMap<Integer, PortraitCommonBinExtend>();
			_bit |= 0x1;
		}
		_portrait_list.put(val.get_AssetID(), val);
	}

	public boolean has_portrait_list() {
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
		if (has_portrait_list()){
			for (PortraitCommonBinExtend val : _portrait_list.values()) {
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
		if (has_portrait_list()) {
			for (PortraitCommonBinExtend val : _portrait_list.values()) {
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
		if (has_portrait_list()){
			for (PortraitCommonBinExtend val : _portrait_list.values()) {
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
				add_portrait((PortraitCommonBinExtend) input.readMessage(PortraitCommonBinExtend.newInstance()));
				break;
			}
			default: {
				System.out.println(String.format("[PortraitCommonBin] NEW_TAG : TAG(%d)", tag));
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
	
	public static class PortraitCommonBinExtend implements ProtoMessage {
		public static PortraitCommonBinExtend newInstance() {
			return new PortraitCommonBinExtend();
		}
		
		private int _AssetID;
		private PortraitT _portrait;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private PortraitCommonBinExtend() {
		}

		public int get_AssetID(){
			return _AssetID;
		}
		
		public void set_AssetID(int val){
			_bit |= 0x1;
			_AssetID = val;
		}
		
		public boolean has_AssetID(){
			return (_bit & 0x1) == 0x1;
		}
		
		public PortraitT get_portrait() {
			return _portrait;
		}

		public void set_portrait(PortraitT val) {
			_bit |= 0x2;
			_portrait = val;
		}

		public boolean has_portrait() {
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
			if (has_AssetID()){
				size += ProtoOutputStream.computeUInt32Size(1, _AssetID);
			}
			if (has_portrait()) {
				size += ProtoOutputStream.computeMessageSize(2, _portrait);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_AssetID()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_portrait()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_AssetID()){
				output.writeUInt32(1, _AssetID);
			}
			if (has_portrait()){
				output.writeMessage(2, _portrait);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
			while (!input.isAtEnd()) {
				int tag = input.readTag();
				switch (tag) {
				case 0x00000008:
					set_AssetID(input.readInt32());
					break;
				case 0x00000012:
					set_portrait((PortraitT) input.readMessage(PortraitT.newInstance()));
					break;
				default:
					System.out.println(String.format("[PortraitCommonBinExtend] NEW_TAG : TAG(%d)", tag));
					return this;
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

