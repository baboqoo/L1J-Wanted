package l1j.server.common.bin.timecollection;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class TimeCollectionCommonBin implements ProtoMessage{
	public static TimeCollectionCommonBin newInstance() {
		return new TimeCollectionCommonBin();
	}
	private TimeCollectionCommonBinExtend _collection;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private TimeCollectionCommonBin() {
	}
	
	public TimeCollectionCommonBinExtend get_collection() {
		return _collection;
	}
	public void set_collection(TimeCollectionCommonBinExtend val) {
		_bit |= 0x1;
		_collection = val;
	}
	public boolean has_collection() {
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
		if (has_collection()) {
			size += ProtoOutputStream.computeMessageSize(1, _collection);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_collection()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_collection()){
			output.writeMessage(1, _collection);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x00000012:
				set_collection((TimeCollectionCommonBinExtend) input.readMessage(TimeCollectionCommonBinExtend.newInstance()));
				break;
			default:
				System.out.println(String.format("[TimeCollectionCommonBin] NEW_TAG : TAG(%d)", tag));
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
	
	public static class TimeCollectionCommonBinExtend implements ProtoMessage{
		public static TimeCollectionCommonBinExtend newInstance() {
			return new TimeCollectionCommonBinExtend();
		}
		private int _id;
		private TimeCollection _collection;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private TimeCollectionCommonBinExtend() {
		}

		public int get_id() {
			return _id;
		}
		public void set_id(int val) {
			_bit |= 0x1;
			_id = val;
		}
		public boolean has_id() {
			return (_bit & 0x1) == 0x1;
		}
		
		public TimeCollection get_collection() {
			return _collection;
		}
		public void set_collection(TimeCollection val) {
			_bit |= 0x2;
			_collection = val;
		}
		public boolean has_collection() {
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
			if (has_collection()) {
				size += ProtoOutputStream.computeMessageSize(2, _collection);
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
			if (!has_collection()) {
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
			if (has_collection()){
				output.writeMessage(2, _collection);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
			while (!input.isAtEnd()) {
				int tag = input.readTag();
				switch (tag) {
				case 0x00000008:
					set_id(input.readInt32());
					break;
				case 0x00000012:
					set_collection((TimeCollection) input.readMessage(TimeCollection.newInstance()));
					break;
				default:
					System.out.println(String.format("[TimeCollectionCommonBinExtend] NEW_TAG : TAG(%d)", tag));
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

