package l1j.server.common.bin.ndl;

import java.util.HashMap;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class NdlCommonBin implements ProtoMessage {
	public static NdlCommonBin newInstance() {
		return new NdlCommonBin();
	}
	
	private HashMap<Integer, NdlCommonBinExtend> _ndl_list;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private NdlCommonBin() {
	}

	public HashMap<Integer, NdlCommonBinExtend> get_ndl_list() {
		return _ndl_list;
	}

	public NdlCommonBinExtend getNdl(int map_number) {
		return _ndl_list.get(map_number);
	}

	public void add_ndl(NdlCommonBinExtend val) {
		if (!has_ndl_list()) {
			_ndl_list = new HashMap<Integer, NdlCommonBinExtend>();
			_bit |= 0x1;
		}
		_ndl_list.put(val.get_map_number(), val);
	}

	public boolean has_ndl_list() {
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
		if (has_ndl_list()){
			for (NdlCommonBinExtend val : _ndl_list.values()) {
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
		if (has_ndl_list()) {
			for (NdlCommonBinExtend val : _ndl_list.values()) {
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
		if (has_ndl_list()){
			for (NdlCommonBinExtend val : _ndl_list.values()) {
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
				add_ndl((NdlCommonBinExtend) input.readMessage(NdlCommonBinExtend.newInstance()));
				break;
			}
			default: {
				System.out.println(String.format("[NdlCommonBin] NEW_TAG : TAG(%d)", tag));
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
	
	public static class NdlCommonBinExtend implements ProtoMessage {
		public static NdlCommonBinExtend newInstance() {
			return new NdlCommonBinExtend();
		}
		
		private int _map_number;
		private CommonNdlInfo _ndl;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private NdlCommonBinExtend() {
		}
		
		public int get_map_number(){
			return _map_number;
		}
		
		public void set_map_number(int val) {
			_map_number = val;
			_bit |= 0x1;
		}
		
		public boolean has_map_number() {
			return (_bit & 0x1) == 0x1;
		}

		public CommonNdlInfo get_ndl() {
			return _ndl;
		}

		public void set_ndl(CommonNdlInfo val) {
			_ndl = val;
			_bit |= 0x2;
		}

		public boolean has_ndl() {
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
			if (has_map_number()){
				size += ProtoOutputStream.computeUInt32Size(1, _map_number);
			}
			if (has_ndl()) {
				size += ProtoOutputStream.computeMessageSize(2, _ndl);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_map_number()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_ndl()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_map_number()){
				output.writeUInt32(1, _map_number);
			}
			if (has_ndl()){
				output.writeMessage(2, _ndl);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
			while (!input.isAtEnd()) {
				int tag = input.readTag();
				switch (tag) {
				case 0x00000008: {
					set_map_number(input.readInt32());
					break;
				}
				case 0x00000012: {
					set_ndl((CommonNdlInfo) input.readMessage(CommonNdlInfo.newInstance()));
					break;
				}
				default: {
					System.out.println(String.format("[NdlCommonBinExtend] NEW_TAG : TAG(%d)", tag));
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


