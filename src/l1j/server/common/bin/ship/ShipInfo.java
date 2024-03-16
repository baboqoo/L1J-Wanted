package l1j.server.common.bin.ship;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class ShipInfo implements ProtoMessage{
	public static ShipInfo newInstance(){
		return new ShipInfo();
	}
	
	private java.util.LinkedList<ShipInfo.ShipInfoListT> _ShipInfoList;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private ShipInfo(){
	}
	public java.util.LinkedList<ShipInfo.ShipInfoListT> get_ShipInfoList(){
		return _ShipInfoList;
	}
	public void add_ShipInfoList(ShipInfo.ShipInfoListT val){
		if(!has_ShipInfoList()){
			_ShipInfoList = new java.util.LinkedList<ShipInfo.ShipInfoListT>();
			_bit |= 0x1;
		}
		_ShipInfoList.add(val);
	}
	public boolean has_ShipInfoList(){
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
		if (has_ShipInfoList()){
			for (ShipInfo.ShipInfoListT val : _ShipInfoList) {
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
		if (!has_ShipInfoList()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_ShipInfoList()){
			for (ShipInfo.ShipInfoListT val : _ShipInfoList) {
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
					add_ShipInfoList((ShipInfo.ShipInfoListT)input.readMessage(ShipInfo.ShipInfoListT.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[ShipInfo] NEW_TAG : TAG(%d)", tag));
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
	public static class PointT implements ProtoMessage{
		public static PointT newInstance(){
			return new PointT();
		}
		private int _x;
		private int _y;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private PointT(){
		}
		public int get_x(){
			return _x;
		}
		public void set_x(int val){
			_bit |= 0x1;
			_x = val;
		}
		public boolean has_x(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_y(){
			return _y;
		}
		public void set_y(int val){
			_bit |= 0x2;
			_y = val;
		}
		public boolean has_y(){
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
			if (has_x()){
				size += ProtoOutputStream.computeUInt32Size(1, _x);
			}
			if (has_y()){
				size += ProtoOutputStream.computeUInt32Size(2, _y);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_x()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_y()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_x()){
				output.writeUInt32(1, _x);
			}
			if (has_y()){
				output.writeUInt32(2, _y);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_x(input.readUInt32());
						break;
					}
					case 0x00000010:{
						set_y(input.readUInt32());
						break;
					}
					default:{
						System.out.println(String.format("[ShipInfo.PointT] NEW_TAG : TAG(%d)", tag));
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
	public static class RandomPoint implements ProtoMessage{
		public static RandomPoint newInstance(){
			return new RandomPoint();
		}
		private int _x;
		private int _y;
		private int _range;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private RandomPoint(){
			set_range(0);
		}
		public int get_x(){
			return _x;
		}
		public void set_x(int val){
			_bit |= 0x1;
			_x = val;
		}
		public boolean has_x(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_y(){
			return _y;
		}
		public void set_y(int val){
			_bit |= 0x2;
			_y = val;
		}
		public boolean has_y(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_range(){
			return _range;
		}
		public void set_range(int val){
			_bit |= 0x4;
			_range = val;
		}
		public boolean has_range(){
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
			if (has_x()){
				size += ProtoOutputStream.computeUInt32Size(1, _x);
			}
			if (has_y()){
				size += ProtoOutputStream.computeUInt32Size(2, _y);
			}
			if (has_range()){
				size += ProtoOutputStream.computeUInt32Size(3, _range);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_x()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_y()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_x()){
				output.writeUInt32(1, _x);
			}
			if (has_y()){
				output.writeUInt32(2, _y);
			}
			if (has_range()){
				output.writeUInt32(3, _range);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_x(input.readUInt32());
						break;
					}
					case 0x00000010:{
						set_y(input.readUInt32());
						break;
					}
					case 0x00000018:{
						set_range(input.readUInt32());
						break;
					}
					default:{
						System.out.println(String.format("[ShipInfo.RandomPoint] NEW_TAG : TAG(%d)", tag));
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
	public static class BoxT implements ProtoMessage{
		public static BoxT newInstance(){
			return new BoxT();
		}
		private int _startX;
		private int _startY;
		private int _endX;
		private int _endY;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private BoxT(){
		}
		public int get_startX(){
			return _startX;
		}
		public void set_startX(int val){
			_bit |= 0x1;
			_startX = val;
		}
		public boolean has_startX(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_startY(){
			return _startY;
		}
		public void set_startY(int val){
			_bit |= 0x2;
			_startY = val;
		}
		public boolean has_startY(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_endX(){
			return _endX;
		}
		public void set_endX(int val){
			_bit |= 0x4;
			_endX = val;
		}
		public boolean has_endX(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_endY(){
			return _endY;
		}
		public void set_endY(int val){
			_bit |= 0x8;
			_endY = val;
		}
		public boolean has_endY(){
			return (_bit & 0x8) == 0x8;
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
			if (has_startX()){
				size += ProtoOutputStream.computeUInt32Size(1, _startX);
			}
			if (has_startY()){
				size += ProtoOutputStream.computeUInt32Size(2, _startY);
			}
			if (has_endX()){
				size += ProtoOutputStream.computeUInt32Size(3, _endX);
			}
			if (has_endY()){
				size += ProtoOutputStream.computeUInt32Size(4, _endY);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_startX()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_startY()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_endX()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_endY()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_startX()){
				output.writeUInt32(1, _startX);
			}
			if (has_startY()){
				output.writeUInt32(2, _startY);
			}
			if (has_endX()){
				output.writeUInt32(3, _endX);
			}
			if (has_endY()){
				output.writeUInt32(4, _endY);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_startX(input.readUInt32());
						break;
					}
					case 0x00000010:{
						set_startY(input.readUInt32());
						break;
					}
					case 0x00000018:{
						set_endX(input.readUInt32());
						break;
					}
					case 0x00000020:{
						set_endY(input.readUInt32());
						break;
					}
					default:{
						System.out.println(String.format("[ShipInfo.BoxT] NEW_TAG : TAG(%d)", tag));
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
	public static class ScheduleT implements ProtoMessage{
		public static ScheduleT newInstance(){
			return new ScheduleT();
		}
		private String _schedule_day;
		private byte[] _schedule_time;
		private int _schedule_duration;
		private int _ship_operating_duration;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ScheduleT(){
		}
		public String get_schedule_day(){
			return _schedule_day;
		}
		public void set_schedule_day(String val){
			_bit |= 0x1;
			_schedule_day = val;
		}
		public boolean has_schedule_day(){
			return (_bit & 0x1) == 0x1;
		}
		public byte[] get_schedule_time(){
			return _schedule_time;
		}
		public void set_schedule_time(byte[] val){
			_bit |= 0x2;
			_schedule_time = val;
		}
		public boolean has_schedule_time(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_schedule_duration(){
			return _schedule_duration;
		}
		public void set_schedule_duration(int val){
			_bit |= 0x4;
			_schedule_duration = val;
		}
		public boolean has_schedule_duration(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_ship_operating_duration(){
			return _ship_operating_duration;
		}
		public void set_ship_operating_duration(int val){
			_bit |= 0x8;
			_ship_operating_duration = val;
		}
		public boolean has_ship_operating_duration(){
			return (_bit & 0x8) == 0x8;
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
			if (has_schedule_day()){
				size += ProtoOutputStream.computeStringSize(1, _schedule_day);
			}
			if (has_schedule_time()){
				size += ProtoOutputStream.computeBytesSize(2, _schedule_time);
			}
			if (has_schedule_duration()){
				size += ProtoOutputStream.computeInt32Size(3, _schedule_duration);
			}
			if (has_ship_operating_duration()){
				size += ProtoOutputStream.computeInt32Size(4, _ship_operating_duration);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_schedule_day()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_schedule_time()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_schedule_duration()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_ship_operating_duration()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_schedule_day()){
				output.writeString(1, _schedule_day);
			}
			if (has_schedule_time()){
				output.writeBytes(2, _schedule_time);
			}
			if (has_schedule_duration()){
				output.wirteInt32(3, _schedule_duration);
			}
			if (has_ship_operating_duration()){
				output.wirteInt32(4, _ship_operating_duration);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:{
						set_schedule_day(input.readString());
						break;
					}
					case 0x00000012:{
						set_schedule_time(input.readBytes());
						break;
					}
					case 0x00000018:{
						set_schedule_duration(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_ship_operating_duration(input.readInt32());
						break;
					}
					default:{
						System.out.println(String.format("[ShipInfo.ScheduleT] NEW_TAG : TAG(%d)", tag));
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
	public static class ShipInfoListT implements ProtoMessage{
		public static ShipInfoListT newInstance(){
			return new ShipInfoListT();
		}
		private int _id;
		private java.util.LinkedList<ShipInfo.ShipInfoListT.ShipT> _Ship;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ShipInfoListT(){
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
		
		public java.util.LinkedList<ShipInfo.ShipInfoListT.ShipT> get_Ship(){
			return _Ship;
		}
		public void add_Ship(ShipInfo.ShipInfoListT.ShipT val){
			if(!has_Ship()){
				_Ship = new java.util.LinkedList<ShipInfo.ShipInfoListT.ShipT>();
				_bit |= 0x2;
			}
			_Ship.add(val);
		}
		public boolean has_Ship(){
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
			if (hasId()) {
				size += ProtoOutputStream.computeUInt32Size(1, _id);
			}
			if (has_Ship()){
				for(ShipInfo.ShipInfoListT.ShipT val : _Ship){
					size += ProtoOutputStream.computeMessageSize(2, val);
				}
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!hasId()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_Ship()){
				for(ShipInfo.ShipInfoListT.ShipT val : _Ship){
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
			if (hasId()) {
				output.writeUInt32(1, _id);
			}
			if (has_Ship()){
				for (ShipInfo.ShipInfoListT.ShipT val : _Ship){
					output.writeMessage(2, val);
				}
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						setId(input.readInt32());
						break;
					}
					case 0x00000012:{
						add_Ship((ShipInfo.ShipInfoListT.ShipT)input.readMessage(ShipInfo.ShipInfoListT.ShipT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[ShipInfo.ShipInfoListT] NEW_TAG : TAG(%d)", tag));
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
		public static class ShipT implements ProtoMessage{
			public static ShipT newInstance(){
				return new ShipT();
			}
			private int _id;
			private int _dockWorld;
			private int _shipWorld;
			private int _ticket;
			private int _levelLimit;
			private ShipInfo.BoxT _Dock;
			private ShipInfo.PointT _ShipLoc;
			private int _destWorld;
			private ShipInfo.RandomPoint _DestLoc;
			private ShipInfo.ScheduleT _Schedule;
			private int _returnWorld;
			private ShipInfo.PointT _ReturnLoc;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private ShipT(){
			}
			
			public ShipT(ResultSet rs) throws SQLException {
				this._id			= rs.getInt("id");
				this._dockWorld		= rs.getInt("dockWorld");
				this._shipWorld		= rs.getInt("shipWorld");
				this._ticket		= rs.getInt("ticket");
				this._levelLimit	= rs.getInt("levelLimit");
				this._Dock			= ShipInfo.BoxT.newInstance();
				this._Dock.set_startX(rs.getInt("dock_startX"));
				this._Dock.set_startY(rs.getInt("dock_startY"));
				this._Dock.set_endX(rs.getInt("dock_endX"));
				this._Dock.set_endY(rs.getInt("dock_endY"));
				this._ShipLoc		= ShipInfo.PointT.newInstance();
				this._ShipLoc.set_x(rs.getInt("shipLoc_x"));
				this._ShipLoc.set_y(rs.getInt("shipLoc_y"));
				this._destWorld		= rs.getInt("destWorld");
				this._DestLoc		= ShipInfo.RandomPoint.newInstance();
				this._DestLoc.set_x(rs.getInt("destLoc_x"));
				this._DestLoc.set_y(rs.getInt("destLoc_y"));
				this._DestLoc.set_range(rs.getInt("destLoc_range"));
				this._Schedule		= ShipInfo.ScheduleT.newInstance();
				this._Schedule.set_schedule_day(rs.getString("schedule_day"));
				this._Schedule.set_schedule_time(rs.getBytes("schedule_time"));
				this._Schedule.set_schedule_duration(rs.getInt("schedule_duration"));
				this._Schedule.set_ship_operating_duration(rs.getInt("schedule_ship_operating_duration"));
				this._returnWorld	= rs.getInt("returnWorld");
				this._ReturnLoc		= ShipInfo.PointT.newInstance();
				this._ReturnLoc.set_x(rs.getInt("returnLoc_x"));
				this._ReturnLoc.set_y(rs.getInt("returnLoc_y"));
			}
			
			public int get_id(){
				return _id;
			}
			public void set_id(int val){
				_bit |= 0x1;
				_id = val;
			}
			public boolean has_id(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_dockWorld(){
				return _dockWorld;
			}
			public void set_dockWorld(int val){
				_bit |= 0x2;
				_dockWorld = val;
			}
			public boolean has_dockWorld(){
				return (_bit & 0x2) == 0x2;
			}
			public int get_shipWorld(){
				return _shipWorld;
			}
			public void set_shipWorld(int val){
				_bit |= 0x4;
				_shipWorld = val;
			}
			public boolean has_shipWorld(){
				return (_bit & 0x4) == 0x4;
			}
			public int get_ticket(){
				return _ticket;
			}
			public void set_ticket(int val){
				_bit |= 0x8;
				_ticket = val;
			}
			public boolean has_ticket(){
				return (_bit & 0x8) == 0x8;
			}
			public int get_levelLimit(){
				return _levelLimit;
			}
			public void set_levelLimit(int val){
				_bit |= 0x10;
				_levelLimit = val;
			}
			public boolean has_levelLimit(){
				return (_bit & 0x10) == 0x10;
			}
			public ShipInfo.BoxT get_Dock(){
				return _Dock;
			}
			public void set_Dock(ShipInfo.BoxT val){
				_bit |= 0x20;
				_Dock = val;
			}
			public boolean has_Dock(){
				return (_bit & 0x20) == 0x20;
			}
			public ShipInfo.PointT get_ShipLoc(){
				return _ShipLoc;
			}
			public void set_ShipLoc(ShipInfo.PointT val){
				_bit |= 0x40;
				_ShipLoc = val;
			}
			public boolean has_ShipLoc(){
				return (_bit & 0x40) == 0x40;
			}
			public int get_destWorld(){
				return _destWorld;
			}
			public void set_destWorld(int val){
				_bit |= 0x80;
				_destWorld = val;
			}
			public boolean has_destWorld(){
				return (_bit & 0x80) == 0x80;
			}
			public ShipInfo.RandomPoint get_DestLoc(){
				return _DestLoc;
			}
			public void set_DestLoc(ShipInfo.RandomPoint val){
				_bit |= 0x100;
				_DestLoc = val;
			}
			public boolean has_DestLoc(){
				return (_bit & 0x100) == 0x100;
			}
			public ShipInfo.ScheduleT get_Schedule(){
				return _Schedule;
			}
			public void set_Schedule(ShipInfo.ScheduleT val){
				_bit |= 0x200;
				_Schedule = val;
			}
			public boolean has_Schedule(){
				return (_bit & 0x200) == 0x200;
			}
			public int get_returnWorld(){
				return _returnWorld;
			}
			public void set_returnWorld(int val){
				_bit |= 0x400;
				_returnWorld = val;
			}
			public boolean has_returnWorld(){
				return (_bit & 0x400) == 0x400;
			}
			public ShipInfo.PointT get_ReturnLoc(){
				return _ReturnLoc;
			}
			public void set_ReturnLoc(ShipInfo.PointT val){
				_bit |= 0x800;
				_ReturnLoc = val;
			}
			public boolean has_ReturnLoc(){
				return (_bit & 0x800) == 0x800;
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
				if (has_dockWorld()){
					size += ProtoOutputStream.computeUInt32Size(2, _dockWorld);
				}
				if (has_shipWorld()){
					size += ProtoOutputStream.computeUInt32Size(3, _shipWorld);
				}
				if (has_ticket()){
					size += ProtoOutputStream.computeUInt32Size(4, _ticket);
				}
				if (has_levelLimit()){
					size += ProtoOutputStream.computeUInt32Size(5, _levelLimit);
				}
				if (has_Dock()){
					size += ProtoOutputStream.computeMessageSize(6, _Dock);
				}
				if (has_ShipLoc()){
					size += ProtoOutputStream.computeMessageSize(7, _ShipLoc);
				}
				if (has_destWorld()){
					size += ProtoOutputStream.computeUInt32Size(8, _destWorld);
				}
				if (has_DestLoc()){
					size += ProtoOutputStream.computeMessageSize(9, _DestLoc);
				}
				if (has_Schedule()){
					size += ProtoOutputStream.computeMessageSize(10, _Schedule);
				}
				if (has_returnWorld()){
					size += ProtoOutputStream.computeUInt32Size(11, _returnWorld);
				}
				if (has_ReturnLoc()){
					size += ProtoOutputStream.computeMessageSize(12, _ReturnLoc);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_id()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_dockWorld()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_shipWorld()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_ticket()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_levelLimit()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_Dock()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_ShipLoc()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_destWorld()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_DestLoc()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_Schedule()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_returnWorld()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_ReturnLoc()){
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
				if (has_dockWorld()){
					output.writeUInt32(2, _dockWorld);
				}
				if (has_shipWorld()){
					output.writeUInt32(3, _shipWorld);
				}
				if (has_ticket()){
					output.writeUInt32(4, _ticket);
				}
				if (has_levelLimit()){
					output.writeUInt32(5, _levelLimit);
				}
				if (has_Dock()){
					output.writeMessage(6, _Dock);
				}
				if (has_ShipLoc()){
					output.writeMessage(7, _ShipLoc);
				}
				if (has_destWorld()){
					output.writeUInt32(8, _destWorld);
				}
				if (has_DestLoc()){
					output.writeMessage(9, _DestLoc);
				}
				if (has_Schedule()){
					output.writeMessage(10, _Schedule);
				}
				if (has_returnWorld()){
					output.writeUInt32(11, _returnWorld);
				}
				if (has_ReturnLoc()){
					output.writeMessage(12, _ReturnLoc);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							set_id(input.readUInt32());
							break;
						}
						case 0x00000010:{
							set_dockWorld(input.readUInt32());
							break;
						}
						case 0x00000018:{
							set_shipWorld(input.readUInt32());
							break;
						}
						case 0x00000020:{
							set_ticket(input.readUInt32());
							break;
						}
						case 0x00000028:{
							set_levelLimit(input.readUInt32());
							break;
						}
						case 0x00000032:{
							set_Dock((ShipInfo.BoxT)input.readMessage(ShipInfo.BoxT.newInstance()));
							break;
						}
						case 0x0000003A:{
							set_ShipLoc((ShipInfo.PointT)input.readMessage(ShipInfo.PointT.newInstance()));
							break;
						}
						case 0x00000040:{
							set_destWorld(input.readUInt32());
							break;
						}
						case 0x0000004A:{
							set_DestLoc((ShipInfo.RandomPoint)input.readMessage(ShipInfo.RandomPoint.newInstance()));
							break;
						}
						case 0x00000052:{
							set_Schedule((ShipInfo.ScheduleT)input.readMessage(ShipInfo.ScheduleT.newInstance()));
							break;
						}
						case 0x00000058:{
							set_returnWorld(input.readUInt32());
							break;
						}
						case 0x00000062:{
							set_ReturnLoc((ShipInfo.PointT)input.readMessage(ShipInfo.PointT.newInstance()));
							break;
						}
						default:{
							System.out.println(String.format("[ShipInfo.ShipT] NEW_TAG : TAG(%d)", tag));
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
}

