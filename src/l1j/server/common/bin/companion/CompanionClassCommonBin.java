package l1j.server.common.bin.companion;

import java.util.HashMap;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class CompanionClassCommonBin implements ProtoMessage{
	public static CompanionClassCommonBin newInstance(){
		return new CompanionClassCommonBin();
	}
	private HashMap<Integer, CompanionClassCommonBinExtend> _Class;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CompanionClassCommonBin(){
	}
	public HashMap<Integer, CompanionClassCommonBinExtend> get_Class(){
		return _Class;
	}
	public void add_Class(CompanionClassCommonBinExtend val){
		if(!has_Class()){
			_Class = new HashMap<Integer, CompanionClassCommonBinExtend>();
			_bit |= 0x1;
		}
		_Class.put(val._class_id, val);
	}
	public boolean has_Class(){
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
		if (has_Class()){
			for (CompanionClassCommonBinExtend val : _Class.values()) {
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
		if (has_Class()){
			for(CompanionClassCommonBinExtend val : _Class.values()){
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
		if (has_Class()){
			for (CompanionClassCommonBinExtend val : _Class.values()) {
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
					add_Class((CompanionClassCommonBinExtend)input.readMessage(CompanionClassCommonBinExtend.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[CompanionClassCommonBin] NEW_TAG : TAG(%d)", tag));
					return this;
				}
			}
		}
		return this;
	}

	@Override
	public void dispose(){
		if (has_Class()) {
			for (CompanionClassCommonBinExtend val : _Class.values()) {
				val.dispose();
			}
			_Class.clear();
			_Class = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	
	public static class CompanionClassCommonBinExtend implements ProtoMessage{
		public static CompanionClassCommonBinExtend newInstance(){
			return new CompanionClassCommonBinExtend();
		}
		private int _class_id;
		private CompanionT.ClassInfoT.ClassT _Class;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private CompanionClassCommonBinExtend(){
		}
		public int get_class_id() {
			return _class_id;
		}
		public void set_class_id(int val) {
			_bit |= 0x1;
			_class_id = val;
		}
		public boolean has_class_id() {
			return (_bit & 0x1) == 0x1;
		}
		public CompanionT.ClassInfoT.ClassT get_Class(){
			return _Class;
		}
		public void set_Class(CompanionT.ClassInfoT.ClassT val){
			_bit |= 0x2;
			_Class = val;
		}
		public boolean has_Class(){
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
			if (has_class_id()){
				size += ProtoOutputStream.computeUInt32Size(1, _class_id);
			}
			if (has_Class()) {
				size += ProtoOutputStream.computeMessageSize(2, _Class);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_class_id()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Class()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_class_id()){
				output.writeUInt32(1, _class_id);
			}
			if (has_Class()){
				output.writeMessage(2, _Class);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_class_id(input.readInt32());
						break;
					}
					case 0x00000012:{
						set_Class((CompanionT.ClassInfoT.ClassT)input.readMessage(CompanionT.ClassInfoT.ClassT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[CompanionClassCommonBinExtend] NEW_TAG : TAG(%d)", tag));
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

