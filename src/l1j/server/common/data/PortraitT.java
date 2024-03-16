package l1j.server.common.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class PortraitT implements ProtoMessage{
	public static PortraitT newInstance(){
		return new PortraitT();
	}
	private int _AssetID;
	private String _Desc;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private PortraitT(){
	}
	public PortraitT(ResultSet rs) throws SQLException {
		_AssetID	= rs.getInt("asset_id");
		_Desc		= rs.getString("desc_id");
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
	public String get_Desc(){
		return _Desc;
	}
	public void set_Desc(String val){
		_bit |= 0x2;
		_Desc = val;
	}
	public boolean has_Desc(){
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
		if (has_AssetID()){
			size += ProtoOutputStream.computeUInt32Size(1, _AssetID);
		}
		if (has_Desc()){
			size += ProtoOutputStream.computeStringSize(2, _Desc);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_AssetID()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_Desc()){
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
		if (has_Desc()){
			output.writeString(2, _Desc);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_AssetID(input.readUInt32());
					break;
				}
				case 0x00000012:{
					set_Desc(input.readString());
					break;
				}
				default:{
					System.out.println(String.format("[PortraitT] NEW_TAG : TAG(%d)", tag));
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

