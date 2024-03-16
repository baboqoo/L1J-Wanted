package l1j.server.common.data;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class NPCDialogInfoT implements ProtoMessage{
	public static NPCDialogInfoT newInstance(){
		return new NPCDialogInfoT();
	}
	private java.util.LinkedList<NPCDialogInfoT.LinkerT> _Linker;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private NPCDialogInfoT(){
	}
	public java.util.LinkedList<NPCDialogInfoT.LinkerT> get_Linker(){
		return _Linker;
	}
	public void add_Linker(NPCDialogInfoT.LinkerT val){
		if(!has_Linker()){
			_Linker = new java.util.LinkedList<NPCDialogInfoT.LinkerT>();
			_bit |= 0x1;
		}
		_Linker.add(val);
	}
	public boolean has_Linker(){
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
		if (has_Linker()){
			for(NPCDialogInfoT.LinkerT val : _Linker){
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
		if (has_Linker()){
			for(NPCDialogInfoT.LinkerT val : _Linker){
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
		if (has_Linker()){
			for (NPCDialogInfoT.LinkerT val : _Linker){
				output.writeMessage(1, val);
			}
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x0000000A:{
					add_Linker((NPCDialogInfoT.LinkerT)input.readMessage(NPCDialogInfoT.LinkerT.newInstance()));
					break;
				}
				default:{
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
	
	public static class LinkerT implements ProtoMessage{
		public static LinkerT newInstance(){
			return new LinkerT();
		}
		private int _LinkReq;
		private int _Index;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private LinkerT(){
		}
		public int get_LinkReq(){
			return _LinkReq;
		}
		public void set_LinkReq(int val){
			_bit |= 0x1;
			_LinkReq = val;
		}
		public boolean has_LinkReq(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_Index(){
			return _Index;
		}
		public void set_Index(int val){
			_bit |= 0x2;
			_Index = val;
		}
		public boolean has_Index(){
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
			if (has_LinkReq()){
				size += ProtoOutputStream.computeInt32Size(1, _LinkReq);
			}
			if (has_Index()){
				size += ProtoOutputStream.computeInt32Size(2, _Index);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_LinkReq()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Index()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_LinkReq()){
				output.wirteInt32(1, _LinkReq);
			}
			if (has_Index()){
				output.wirteInt32(2, _Index);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_LinkReq(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_Index(input.readInt32());
						break;
					}
					default:{
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

