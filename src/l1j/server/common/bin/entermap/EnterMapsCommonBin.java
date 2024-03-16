package l1j.server.common.bin.entermap;

import java.util.HashMap;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class EnterMapsCommonBin implements ProtoMessage{
	public static EnterMapsCommonBin newInstance(){
		return new EnterMapsCommonBin();
	}
	private HashMap<Integer, EnterMapsCommonBinExtend> _MapData;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private EnterMapsCommonBin(){
	}

	public HashMap<Integer, EnterMapsCommonBinExtend> get_MapData(){
		return _MapData;
	}
	public EnterMapsCommonBinExtend get_MapData(int mapId){
		return _MapData.get(mapId);
	}
	public void add_MapData(EnterMapsCommonBinExtend val){
		if(!has_MapData()){
			_MapData = new HashMap<Integer, EnterMapsCommonBinExtend>();
			_bit |= 0x1;
		}
		_MapData.put(val.get_id(), val);
	}
	public boolean has_MapData(){
		return (_bit & 0x1) == 0x1;
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
		if (has_MapData()){
			for (EnterMapsCommonBinExtend val : _MapData.values()) {
				size += ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_MapData()){
			for(EnterMapsCommonBinExtend val : _MapData.values()){
				if (!val.isInitialized()){
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
		if (has_MapData()){
			for (EnterMapsCommonBinExtend val : _MapData.values()) {
				output.writeMessage(1, val);
			}
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000012:{
					add_MapData((EnterMapsCommonBinExtend)input.readMessage(EnterMapsCommonBinExtend.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[EnterMapsCommonBin] NEW_TAG : TAG(%d)", tag));
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
	
	public static class EnterMapsCommonBinExtend implements ProtoMessage{
		public static EnterMapsCommonBinExtend newInstance(){
			return new EnterMapsCommonBinExtend();
		}
		private int _id;
		private EnterMapTAG _MapData;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private EnterMapsCommonBinExtend(){
		}
		public int get_id() {
			return _id;
		}
		public void set_id(int id){
			_id = id;
			_bit |= 0x1;
		}
		public boolean has_id(){
			return (_bit & 0x1) == 0x1;
		}
		public EnterMapTAG get_MapData(){
			return _MapData;
		}
		public void set_MapData(EnterMapTAG val){
			_MapData = val;
			_bit |= 0x2;
		}
		public boolean has_MapData(){
			return (_bit & 0x2) == 0x2;
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
			if (has_id()){
				size += ProtoOutputStream.computeUInt32Size(1, _id);
			}
			if (has_MapData()) {
				size += ProtoOutputStream.computeMessageSize(2, _MapData);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_id()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_MapData()){
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
			if (has_MapData()){
				output.writeMessage(2, _MapData);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_id(input.readInt32());
						break;
					}
					case 0x00000012:{
						set_MapData((EnterMapTAG)input.readMessage(EnterMapTAG.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[EnterMapsCommonBinExtend] NEW_TAG : TAG(%d)", tag));
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
}

