package l1j.server.common.data;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class SpellTAG implements ProtoMessage{
	public static SpellTAG newInstance(){
		return new SpellTAG();
	}
	private int _ID;
	private int _Effect;
	private byte[] _FailHtml;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SpellTAG(){
	}
	public int get_ID(){
		return _ID;
	}
	public void set_ID(int val){
		_bit |= 0x1;
		_ID = val;
	}
	public boolean has_ID(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_Effect(){
		return _Effect;
	}
	public void set_Effect(int val){
		_bit |= 0x2;
		_Effect = val;
	}
	public boolean has_Effect(){
		return (_bit & 0x2) == 0x2;
	}
	public byte[] get_FailHtml(){
		return _FailHtml;
	}
	public void set_FailHtml(byte[] val){
		_bit |= 0x4;
		_FailHtml = val;
	}
	public boolean has_FailHtml(){
		return (_bit & 0x4) == 0x4;
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
		if (has_ID()){
			size += ProtoOutputStream.computeInt32Size(1, _ID);
		}
		if (has_Effect()){
			size += ProtoOutputStream.computeInt32Size(2, _Effect);
		}
		if (has_FailHtml()){
			size += ProtoOutputStream.computeBytesSize(3, _FailHtml);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_ID()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_ID()){
			output.wirteInt32(1, _ID);
		}
		if (has_Effect()){
			output.wirteInt32(2, _Effect);
		}
		if (has_FailHtml()){
			output.writeBytes(3, _FailHtml);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_ID(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_Effect(input.readInt32());
					break;
				}
				case 0x0000001A:{
					set_FailHtml(input.readBytes());
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

