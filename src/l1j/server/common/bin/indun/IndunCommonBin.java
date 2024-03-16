package l1j.server.common.bin.indun;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.StringTokenizer;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.common.data.eDungeonType;
import l1j.server.server.utils.StringUtil;

public class IndunCommonBin implements ProtoMessage{
	public static IndunCommonBin newInstance(){
		return new IndunCommonBin();
	}
	private HashMap<Integer, IndunCommonBinExtend> _indun_list;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private IndunCommonBin(){
	}
	public HashMap<Integer, IndunCommonBinExtend> get_indun_list(){
		return _indun_list;
	}
	public IndunCommonBinExtend get_indun(int map_kind) {
		return _indun_list.get(map_kind);
	}
	public void add_indun(IndunCommonBinExtend val){
		if (!has_indun_list()) {
			_indun_list = new HashMap<Integer, IndunCommonBinExtend>();
			_bit |= 0x1;
		}
		_indun_list.put(val.get_map_kind(), val);
	}
	public boolean has_indun_list(){
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
		if (has_indun_list()){
			for (IndunCommonBinExtend val : _indun_list.values()) {
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
		if (!has_indun_list()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_indun_list()){
			for (IndunCommonBinExtend val : _indun_list.values()) {
				output.writeMessage(1, val);
			}
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000012:
					add_indun((IndunCommonBinExtend) input.readMessage(IndunCommonBinExtend.newInstance()));
					break;
				default:
					System.out.println(String.format("[IndunCommonBin] NEW_TAG : TAG(%d)", tag));
					return this;
			}
		}
		return this;
	}

	@Override
	public void dispose(){
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	
	public static class IndunCommonBinExtend implements ProtoMessage{
		public static IndunCommonBinExtend newInstance(){
			return new IndunCommonBinExtend();
		}
		private int _map_kind;
		private IndunInfoForClient _indun;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private IndunCommonBinExtend(){
		}
		public IndunCommonBinExtend(ResultSet rs) throws SQLException {
			_map_kind			= rs.getInt("mapKind");
			IndunInfoForClient indun = IndunInfoForClient.newInstance();
			indun.set_keyItemId(rs.getInt("keyItemId"));
			indun.set_minPlayer(rs.getInt("minPlayer"));
			indun.set_maxPlayer(rs.getInt("maxPlayer"));
			indun.set_minAdena(rs.getInt("minAdena"));
			indun.set_maxAdena(rs.getInt("maxAdena"));
			String minLevels	= rs.getString("minLevel");
			if (!StringUtil.isNullOrEmpty(minLevels)) {
				StringTokenizer st = new StringTokenizer(minLevels, StringUtil.CommaString);
				while (st.hasMoreElements()) {
					indun.add_minLevel(Integer.parseInt(st.nextToken().trim()));
				}
			}
			indun.set_bmkeyItemId(rs.getInt("bmkeyItemId"));
			indun.set_eventKeyItemId(rs.getInt("eventKeyItemId"));
			String dungeon_type	= rs.getString("dungeon_type");
			dungeon_type		= dungeon_type.substring(dungeon_type.indexOf("(") + 1, dungeon_type.indexOf(")"));
			indun.set_dungeon_type(eDungeonType.fromInt(Integer.parseInt(dungeon_type)));
			indun.set_enable_boost_mode(Boolean.parseBoolean(rs.getString("enable_boost_mode")));
			_indun = indun;
		}
		public int get_map_kind() {
			return _map_kind;
		}
		public void set_map_kind(int val) {
			_bit |= 0x1;
			_map_kind = val;
		}
		public boolean has_map_kind() {
			return (_bit & 0x1) == 0x1;
		}
		public IndunInfoForClient get_indun(){
			return _indun;
		}
		public void set_indun(IndunInfoForClient val){
			_bit |= 0x2;
			_indun = val;
		}
		public boolean has_indun(){
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
			if (has_map_kind()){
				size += ProtoOutputStream.computeUInt32Size(1, _map_kind);
			}
			if (has_indun()) {
				size += ProtoOutputStream.computeMessageSize(2, _indun);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_map_kind()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_indun()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_map_kind()){
				output.writeUInt32(1, _map_kind);
			}
			if (has_indun()){
				output.writeMessage(2, _indun);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:
						set_map_kind(input.readInt32());
						break;
					case 0x00000012:
						set_indun((IndunInfoForClient) input.readMessage(IndunInfoForClient.newInstance()));
						break;
					default:
						System.out.println(String.format("[IndunCommonBinExtend] NEW_TAG : TAG(%d)", tag));
						return this;
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

