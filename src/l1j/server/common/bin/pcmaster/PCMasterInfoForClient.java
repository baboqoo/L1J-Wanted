package l1j.server.common.bin.pcmaster;

import java.util.HashMap;

import l1j.server.common.StringKLoader;
import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.server.model.item.ablity.ItemAbilityFactory;
import l1j.server.server.utils.StringUtil;

public class PCMasterInfoForClient implements ProtoMessage{
	public static PCMasterInfoForClient newInstance(){
		return new PCMasterInfoForClient();
	}
	private HashMap<String, PCMasterInfoForClient.UtilityT> _utilities;
	private HashMap<Integer, PCMasterInfoForClient.PCBonusMapT> _pc_bonus_map_infos;
	private PCMasterInfoForClient.NotificationT _notification;
	private java.util.LinkedList<PCMasterInfoForClient.BuffGroupT> _buff_group;
	private java.util.LinkedList<PCMasterInfoForClient.BuffBonusT> _buff_bonus;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private PCMasterInfoForClient(){
	}
	
	public HashMap<String, PCMasterInfoForClient.UtilityT> get_utilities() {
		return _utilities;
	}
	public void add_utilities(PCMasterInfoForClient.UtilityT val) {
		if (_utilities == null) {
			_utilities = new HashMap<String, PCMasterInfoForClient.UtilityT>();
			_bit |= 0x1;
		}
		_utilities.put(val._action, val);
	}
	public String get_utilities_toString() {
		StringBuilder sb = new StringBuilder();
		for (PCMasterInfoForClient.UtilityT val : _utilities.values()) {
			if (sb.length() > 0) {
				sb.append(StringUtil.LineString);
			}
			sb.append("ACTION = ").append(val._action);
			sb.append(", COST = ").append(val._cost);
		}
		return sb.toString();
	}
	public boolean has_utilities() {
		return (_bit & 0x1) == 0x1;
	}
	
	public HashMap<Integer, PCMasterInfoForClient.PCBonusMapT> get_pc_bonus_map_infos() {
		return _pc_bonus_map_infos;
	}
	public void add_pc_bonus_map_infos(PCMasterInfoForClient.PCBonusMapT val) {
		if (_pc_bonus_map_infos == null) {
			_pc_bonus_map_infos = new HashMap<Integer, PCMasterInfoForClient.PCBonusMapT>();
			_bit |= 0x2;
		}
		_pc_bonus_map_infos.put(val._id, val);
	}
	public String get_pc_bonus_map_infos_toString() {
		StringBuilder sb = new StringBuilder();
		for (PCMasterInfoForClient.PCBonusMapT val : _pc_bonus_map_infos.values()) {
			if (sb.length() > 0) {
				sb.append(StringUtil.LineString);
			}
			sb.append("ID = ").append(val._id);
			sb.append(", BONUS_MAP_TIME = ").append(val._bonus_map_time);
			sb.append(", DEATH_PENALTY_SHIELD = ").append(val._death_penalty_shield);
		}
		return sb.toString();
	}
	public boolean has_pc_bonus_map_infos() {
		return (_bit & 0x2) == 0x2;
	}
	public PCMasterInfoForClient.NotificationT get_notification(){
		return _notification;
	}
	public void set_notification(PCMasterInfoForClient.NotificationT val){
		_bit |= 0x4;
		_notification = val;
	}
	public String get_notification_toString() {
		if (_notification == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("DELAY: ").append(_notification.get_delay());
		java.util.LinkedList<PCMasterInfoForClient.NotificationT.NotiElementT> elements = _notification.get_noti_element();
		if (elements != null && !elements.isEmpty()) {
			for (PCMasterInfoForClient.NotificationT.NotiElementT elementT : elements) {
				sb.append("\r\nNOTI_ELEMENT: DESC=").append(elementT.get_desc()).append(" & DESC_KR=").append(StringKLoader.getString(Integer.parseInt(elementT.get_desc().substring(1)))).append(" & URL=").append(elementT.get_url());
			}
		}
		return sb.toString();
	}
	public boolean has_notification(){
		return (_bit & 0x4) == 0x4;
	}
	public java.util.LinkedList<PCMasterInfoForClient.BuffGroupT> get_buff_group(){
		return _buff_group;
	}
	public void add_buff_group(PCMasterInfoForClient.BuffGroupT val){
		if(!has_buff_group()){
			_buff_group = new java.util.LinkedList<PCMasterInfoForClient.BuffGroupT>();
			_bit |= 0x8;
		}
		_buff_group.add(val);
	}
	public String get_buff_group_toString() {
		if (_buff_group == null || _buff_group.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (PCMasterInfoForClient.BuffGroupT groupT : _buff_group) {
			if (sb.length() > 0) {
				sb.append(StringUtil.LineString);
			}
			sb.append("ID: ").append(groupT.get_id());
			java.util.LinkedList<Integer> costs = groupT.get_cost();
			if (costs != null && !costs.isEmpty()) {
				sb.append(", COST( ");
				int costCnt = 0;
				for (int cost : costs) {
					if (costCnt++ > 0) {
						sb.append(" AND ");
					}
					sb.append(cost);
				}
				sb.append(" )");
			}
			java.util.LinkedList<Integer> times = groupT.get_time();
			if (times != null && !times.isEmpty()) {
				sb.append(", TIME( ");
				int timeCnt = 0;
				for (int time : times) {
					if (timeCnt++ > 0) {
						sb.append(" AND ");
					}
					sb.append(time);
				}
				sb.append(" )");
			}
			java.util.LinkedList<PCMasterInfoForClient.BuffGroupT.BuffT> buffs = groupT.get_buff();
			if (buffs != null && !buffs.isEmpty()) {
				sb.append(", BUFF( ");
				int buffCnt = 0;
				for (PCMasterInfoForClient.BuffGroupT.BuffT buff : buffs) {
					if (buffCnt++ > 0) {
						sb.append(" AND ");
					}
					sb.append("GRADE=").append(buff.get_grade()).append(" & DESC=");
					int descCnt = 0;
					for (byte desc : buff._desc) {
						if (descCnt++ > 0) {
							sb.append(StringUtil.CommaString);
						}
						sb.append(desc & 0xFF);
					}
				}
				sb.append(" )");
			}
		}
		return sb.toString();
	}
	public boolean has_buff_group(){
		return (_bit & 0x8) == 0x8;
	}
	public java.util.LinkedList<PCMasterInfoForClient.BuffBonusT> get_buff_bonus(){
		return _buff_bonus;
	}
	public void add_buff_bonus(PCMasterInfoForClient.BuffBonusT val){
		if(!has_buff_bonus()){
			_buff_bonus = new java.util.LinkedList<PCMasterInfoForClient.BuffBonusT>();
			_bit |= 0x10;
		}
		_buff_bonus.add(val);
	}
	public String get_buff_bonus_toString() {
		if (_buff_bonus == null || _buff_bonus.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (PCMasterInfoForClient.BuffBonusT bonusT : _buff_bonus) {
			if (sb.length() > 0) {
				sb.append(StringUtil.LineString);
			}
			
			sb.append("MAP_ID( ");
			int mapCnt = 0;
			for (int map_id : bonusT.get_map_id()) {
				if (mapCnt++ > 0) {
					sb.append(" AND ");
				}
				sb.append(map_id);
			}
			sb.append(" )");
			
			if (bonusT.has_desc()) {
				sb.append(", DESC: ").append(bonusT.get_desc()).append(", DESC_KR: ").append(StringKLoader.getString(Integer.parseInt(bonusT.get_desc().substring(1))));
			}
			
			sb.append(", MIN_LEVEL: ").append(bonusT.get_min_level());
			sb.append(", MAX_LEVEL: ").append(bonusT.get_max_level());
			
			java.util.LinkedList<PCMasterInfoForClient.BuffBonusT.BuffCategoryT> buff_categorys = bonusT._buff_category;
			if (buff_categorys != null && !buff_categorys.isEmpty()) {
				sb.append(", BUFF_CATEGORY( ");
				int categoryCnt = 0;
				for (PCMasterInfoForClient.BuffBonusT.BuffCategoryT categoryT : buff_categorys) {
					if (categoryCnt++ > 0) {
						sb.append(" AND ");
					}
					sb.append("TYPE=").append(categoryT.get_type());
					
					java.util.LinkedList<Integer> groups = categoryT._group;
					if (groups != null && !groups.isEmpty()) {
						sb.append(" & GROUP{ ");
						int groupCnt = 0;
						for (int group : groups) {
							if (groupCnt++ > 0) {
								sb.append(" AND ");
							}
							sb.append(group);
						}
						sb.append(" }");
					}
				}
				sb.append(" )");
			}
			
		}
		return sb.toString();
	}
	public boolean has_buff_bonus(){
		return (_bit & 0x10) == 0x10;
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
		if (has_utilities()){
			for(PCMasterInfoForClient.UtilityT val : _utilities.values()){
				size += ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		if (has_pc_bonus_map_infos()){
			for(PCMasterInfoForClient.PCBonusMapT val : _pc_bonus_map_infos.values()){
				size += ProtoOutputStream.computeMessageSize(2, val);
			}
		}
		if (has_notification()){
			size += ProtoOutputStream.computeMessageSize(3, _notification);
		}
		if (has_buff_group()){
			for(PCMasterInfoForClient.BuffGroupT val : _buff_group){
				size += ProtoOutputStream.computeMessageSize(4, val);
			}
		}
		if (has_buff_bonus()){
			for(PCMasterInfoForClient.BuffBonusT val : _buff_bonus){
				size += ProtoOutputStream.computeMessageSize(5, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_utilities()){
			for (PCMasterInfoForClient.UtilityT val : _utilities.values()) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_pc_bonus_map_infos()){
			for (PCMasterInfoForClient.PCBonusMapT val : _pc_bonus_map_infos.values()) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_buff_group()){
			for(PCMasterInfoForClient.BuffGroupT val : _buff_group){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_buff_bonus()){
			for(PCMasterInfoForClient.BuffBonusT val : _buff_bonus){
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
		if (has_utilities()){
			for (PCMasterInfoForClient.UtilityT val : _utilities.values()){
				output.writeMessage(1, val);
			}
		}
		if (has_pc_bonus_map_infos()){
			for (PCMasterInfoForClient.PCBonusMapT val : _pc_bonus_map_infos.values()){
				output.writeMessage(2, val);
			}
		}
		if (has_notification()){
			output.writeMessage(3, _notification);
		}
		if (has_buff_group()){
			for (PCMasterInfoForClient.BuffGroupT val : _buff_group){
				output.writeMessage(4, val);
			}
		}
		if (has_buff_bonus()){
			for (PCMasterInfoForClient.BuffBonusT val : _buff_bonus){
				output.writeMessage(5, val);
			}
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x0000000A:
					add_utilities((PCMasterInfoForClient.UtilityT) input.readMessage(PCMasterInfoForClient.UtilityT.newInstance()));
					break;
				case 0x00000012:
					add_pc_bonus_map_infos((PCMasterInfoForClient.PCBonusMapT) input.readMessage(PCMasterInfoForClient.PCBonusMapT.newInstance()));
					break;
				case 0x0000001A:
					set_notification((PCMasterInfoForClient.NotificationT)input.readMessage(PCMasterInfoForClient.NotificationT.newInstance()));
					break;
				case 0x00000022:
					add_buff_group((PCMasterInfoForClient.BuffGroupT)input.readMessage(PCMasterInfoForClient.BuffGroupT.newInstance()));
					break;
				case 0x0000002A:
					add_buff_bonus((PCMasterInfoForClient.BuffBonusT)input.readMessage(PCMasterInfoForClient.BuffBonusT.newInstance()));
					break;
				default:
					System.out.println(String.format("[PCMasterInfoForClient] NEW_TAG : TAG(%d)", tag));
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
	
	public static class UtilityT implements ProtoMessage{
		public static UtilityT newInstance(){
			return new UtilityT();
		}
		private String _action;
		private int _cost;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private UtilityT(){
		}
		public String get_action() {
			return _action;
		}
		public void set_action(String val) {
			_bit |= 0x1;
			_action = val;
		}
		public boolean has_action() {
			return (_bit & 0x1) == 0x1;
		}
		public int get_cost() {
			return _cost;
		}
		public void set_cost(int val) {
			_bit |= 0x2;
			_cost = val;
		}
		public boolean has_cost() {
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
			if (has_action()){
				size += ProtoOutputStream.computeStringSize(1, _action);
			}
			if (has_cost()){
				size += ProtoOutputStream.computeInt32Size(2, _cost);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_action()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_cost()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_action()){
				output.writeString(1, _action);
			}
			if (has_cost()){
				output.wirteInt32(2, _cost);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:
						set_action(input.readString());
						break;
					case 0x00000010:
						set_cost(input.readInt32());
						break;
					default:
						System.out.println(String.format("[PCMasterInfoForClient.UtilityT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class PCBonusMapT implements ProtoMessage{
		public static PCBonusMapT newInstance(){
			return new PCBonusMapT();
		}
		private int _id;
		private boolean _bonus_map_time;
		private boolean _death_penalty_shield;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private PCBonusMapT(){
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
		
		public boolean is_bonus_map_time() {
			return _bonus_map_time;
		}
		public void set_bonus_map_time(boolean val) {
			_bit |= 0x2;
			_bonus_map_time = val;
		}
		public boolean has_bonus_map_time() {
			return (_bit & 0x2) == 0x2;
		}
		
		public boolean is_death_penalty_shield() {
			return _death_penalty_shield;
		}
		public void set_death_penalty_shield(boolean val) {
			_bit |= 0x4;
			_death_penalty_shield = val;
		}
		public boolean has_death_penalty_shield() {
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
			if (has_id()){
				size += ProtoOutputStream.computeInt32Size(1, _id);
			}
			if (has_bonus_map_time()){
				size += ProtoOutputStream.computeBoolSize(2, _bonus_map_time);
			}
			if (has_death_penalty_shield()){
				size += ProtoOutputStream.computeBoolSize(3, _death_penalty_shield);
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
			if (!has_bonus_map_time()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_death_penalty_shield()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_id()){
				output.wirteInt32(1, _id);
			}
			if (has_bonus_map_time()){
				output.writeBool(2, _bonus_map_time);
			}
			if (has_death_penalty_shield()){
				output.writeBool(3, _death_penalty_shield);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:
						set_id(input.readInt32());
						break;
					case 0x00000010:
						set_bonus_map_time(input.readBool());
						break;
					case 0x00000018:
						set_death_penalty_shield(input.readBool());
						break;
					default:
						System.out.println(String.format("[PCMasterInfoForClient.PCBonusMapT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class NotificationT implements ProtoMessage{
		public static NotificationT newInstance(){
			return new NotificationT();
		}
		private int _delay;
		private java.util.LinkedList<PCMasterInfoForClient.NotificationT.NotiElementT> _noti_element;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private NotificationT(){
		}
		public int get_delay(){
			return _delay;
		}
		public void set_delay(int val){
			_bit |= 0x1;
			_delay = val;
		}
		public boolean has_delay(){
			return (_bit & 0x1) == 0x1;
		}
		public java.util.LinkedList<PCMasterInfoForClient.NotificationT.NotiElementT> get_noti_element(){
			return _noti_element;
		}
		public void add_noti_element(PCMasterInfoForClient.NotificationT.NotiElementT val){
			if(!has_noti_element()){
				_noti_element = new java.util.LinkedList<PCMasterInfoForClient.NotificationT.NotiElementT>();
				_bit |= 0x2;
			}
			_noti_element.add(val);
		}
		public boolean has_noti_element(){
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
			if (has_delay()){
				size += ProtoOutputStream.computeInt32Size(1, _delay);
			}
			if (has_noti_element()){
				for(PCMasterInfoForClient.NotificationT.NotiElementT val : _noti_element){
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
			if (!has_delay()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_noti_element()){
				for(PCMasterInfoForClient.NotificationT.NotiElementT val : _noti_element){
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
			if (has_delay()){
				output.wirteInt32(1, _delay);
			}
			if (has_noti_element()){
				for (PCMasterInfoForClient.NotificationT.NotiElementT val : _noti_element){
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
						set_delay(input.readInt32());
						break;
					}
					case 0x00000012:{
						add_noti_element((PCMasterInfoForClient.NotificationT.NotiElementT)input.readMessage(PCMasterInfoForClient.NotificationT.NotiElementT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[PCMasterInfoForClient.NotificationT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class NotiElementT implements ProtoMessage{
			public static NotiElementT newInstance(){
				return new NotiElementT();
			}
			private String _desc;
			private String _url;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private NotiElementT(){
			}
			public String get_desc(){
				return _desc;
			}
			public void set_desc(String val){
				_bit |= 0x1;
				_desc = val;
			}
			public boolean has_desc(){
				return (_bit & 0x1) == 0x1;
			}
			public String get_url(){
				return _url;
			}
			public void set_url(String val){
				_bit |= 0x2;
				_url = val;
			}
			public boolean has_url(){
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
				if (has_desc()){
					size += ProtoOutputStream.computeStringSize(1, _desc);
				}
				if (has_url()){
					size += ProtoOutputStream.computeStringSize(2, _url);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_desc()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_desc()){
					output.writeString(1, _desc);
				}
				if (has_url()){
					output.writeString(2, _url);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x0000000A:{
							set_desc(input.readString());
							break;
						}
						case 0x00000012:{
							set_url(input.readString());
							break;
						}
						default:{
							System.out.println(String.format("[PCMasterInfoForClient.NotificationT.NotiElementT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class BuffGroupT implements ProtoMessage{
		public static BuffGroupT newInstance(){
			return new BuffGroupT();
		}
		private int _id;
		private java.util.LinkedList<Integer> _cost;
		private java.util.LinkedList<Integer> _time;
		private java.util.LinkedList<PCMasterInfoForClient.BuffGroupT.BuffT> _buff;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private BuffGroupT(){
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
		public java.util.LinkedList<Integer> get_cost(){
			return _cost;
		}
		public void add_cost(int val){
			if(!has_cost()){
				_cost = new java.util.LinkedList<Integer>();
				_bit |= 0x2;
			}
			_cost.add(val);
		}
		public boolean has_cost(){
			return (_bit & 0x2) == 0x2;
		}
		public java.util.LinkedList<Integer> get_time(){
			return _time;
		}
		public void add_time(int val){
			if(!has_time()){
				_time = new java.util.LinkedList<Integer>();
				_bit |= 0x4;
			}
			_time.add(val);
		}
		public boolean has_time(){
			return (_bit & 0x4) == 0x4;
		}
		public java.util.LinkedList<PCMasterInfoForClient.BuffGroupT.BuffT> get_buff(){
			return _buff;
		}
		public void add_buff(PCMasterInfoForClient.BuffGroupT.BuffT val){
			if(!has_buff()){
				_buff = new java.util.LinkedList<PCMasterInfoForClient.BuffGroupT.BuffT>();
				_bit |= 0x8;
			}
			_buff.add(val);
		}
		public boolean has_buff(){
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
			if (has_id()){
				size += ProtoOutputStream.computeInt32Size(1, _id);
			}
			if (has_cost()){
				for(int val : _cost){
					size += ProtoOutputStream.computeInt32Size(2, val);
				}
			}
			if (has_time()){
				for(int val : _time){
					size += ProtoOutputStream.computeInt32Size(3, val);
				}
			}
			if (has_buff()){
				for(PCMasterInfoForClient.BuffGroupT.BuffT val : _buff){
					size += ProtoOutputStream.computeMessageSize(4, val);
				}
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
			if (!has_cost()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_time()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_buff()){
				for(PCMasterInfoForClient.BuffGroupT.BuffT val : _buff){
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
			if (has_id()){
				output.wirteInt32(1, _id);
			}
			if (has_cost()){
				for (int val : _cost){
					output.wirteInt32(2, val);
				}
			}
			if (has_time()){
				for (int val : _time){
					output.wirteInt32(3, val);
				}
			}
			if (has_buff()){
				for (PCMasterInfoForClient.BuffGroupT.BuffT val : _buff){
					output.writeMessage(4, val);
				}
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
					case 0x00000010:{
						add_cost(input.readInt32());
						break;
					}
					case 0x00000018:{
						add_time(input.readInt32());
						break;
					}
					case 0x00000022:{
						add_buff((PCMasterInfoForClient.BuffGroupT.BuffT)input.readMessage(PCMasterInfoForClient.BuffGroupT.BuffT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[PCMasterInfoForClient.BuffGroupT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class BuffT implements ProtoMessage{
			public static BuffT newInstance(){
				return new BuffT();
			}
			
			public static class BuffEnumDescBonus {
				private ItemAbilityFactory _factory;
				private int _value;
				
				public BuffEnumDescBonus(ItemAbilityFactory factory, int value) {
					_factory = factory;
					_value = value;
				}
				
				public ItemAbilityFactory getFactory() {
					return _factory;
				}
				public int getValue() {
					return _value;
				}
			}
			
			private java.util.LinkedList<BuffEnumDescBonus> _enum_desc_bonus_list;
			void add_enum_desc_bonus_list(ItemAbilityFactory factory, byte[] val) {
				if (_enum_desc_bonus_list == null) {
					_enum_desc_bonus_list = new java.util.LinkedList<BuffEnumDescBonus>();
				}
				_enum_desc_bonus_list.add(new BuffEnumDescBonus(factory, parseEnumDescVal(val)));
			}
			public java.util.LinkedList<BuffEnumDescBonus> get_enum_desc_bonus_list() {
				return _enum_desc_bonus_list;
			}
			
			static int parseEnumDescVal(byte[] val) {
				switch (val.length) {
				case 4:
					return (val[0] & 0xFF) | (val[1] << 8 & 0xFF00) | (val[2] << 16 & 0xFF0000) | (val[3] << 24 & 0xFF000000);
				case 2:
					return (val[0] & 0xFF) | (val[1] << 8 & 0xFF00);
				default:
					return val[0] & 0xFF;
				}
			}
			
			private int _grade;
			private byte[] _desc;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private BuffT(){
			}
			public int get_grade(){
				return _grade;
			}
			public void set_grade(int val){
				_bit |= 0x1;
				_grade = val;
			}
			public boolean has_grade(){
				return (_bit & 0x1) == 0x1;
			}
			public byte[] get_desc(){
				return _desc;
			}
			public void set_desc(byte[] val){
				_bit |= 0x2;
				_desc = val;
			}
			public boolean has_desc(){
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
				if (has_grade()){
					size += ProtoOutputStream.computeInt32Size(1, _grade);
				}
				if (has_desc()){
					size += ProtoOutputStream.computeBytesSize(2, _desc);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_grade()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_grade()){
					output.wirteInt32(1, _grade);
				}
				if (has_desc()){
					output.writeBytes(2, _desc);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							set_grade(input.readInt32());
							break;
						}
						case 0x00000012:{
							byte[] array = input.readBytes();
							java.util.LinkedList<Byte> list = new java.util.LinkedList<Byte>();
							for (int i=0; i<array.length; i++) {
								byte enum_key = array[i];
								if (enum_key == 0) {
									continue;
								}
								list.add(enum_key);
								ItemAbilityFactory factory = ItemAbilityFactory.getAbility(enum_key & 0xFF);
								if (factory == null) {
									System.out.println(String.format("[PCMasterInfoForClient.BuffGroupT.BuffT] UNDEFINED_ENUM_DESC", enum_key & 0xFF));
									continue;
								}
								int val_size = factory.get_val_size();
								byte[] enum_value = new byte[val_size];
								if (val_size > 0) {
									for (int j=0; j<val_size; j++) {
										byte val = array[++i];
										enum_value[j] = val;
										list.add(val);
									}
								}
								add_enum_desc_bonus_list(factory, enum_value);
							}
							byte[] result = new byte[list.size()];
							for (int i=0; i<list.size(); i++) {
								result[i] = list.get(i);
							}
							set_desc(result);
							list.clear();
							list = null;
							break;
						}
						default:{
							System.out.println(String.format("[PCMasterInfoForClient.BuffGroupT.BuffT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class BuffBonusT implements ProtoMessage{
		public static BuffBonusT newInstance(){
			return new BuffBonusT();
		}
		private java.util.LinkedList<Integer> _map_id;
		private String _desc;
		private int _min_level;
		private int _max_level;
		private java.util.LinkedList<PCMasterInfoForClient.BuffBonusT.BuffCategoryT> _buff_category;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private BuffBonusT(){
		}
		public java.util.LinkedList<Integer> get_map_id(){
			return _map_id;
		}
		public void add_map_id(int val){
			if(!has_map_id()){
				_map_id = new java.util.LinkedList<Integer>();
				_bit |= 0x1;
			}
			_map_id.add(val);
		}
		public boolean has_map_id(){
			return (_bit & 0x1) == 0x1;
		}
		public String get_desc(){
			return _desc;
		}
		public void set_desc(String val){
			_bit |= 0x2;
			_desc = val;
		}
		public boolean has_desc(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_min_level(){
			return _min_level;
		}
		public void set_min_level(int val){
			_bit |= 0x4;
			_min_level = val;
		}
		public boolean has_min_level(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_max_level(){
			return _max_level;
		}
		public void set_max_level(int val){
			_bit |= 0x8;
			_max_level = val;
		}
		public boolean has_max_level(){
			return (_bit & 0x8) == 0x8;
		}
		public java.util.LinkedList<PCMasterInfoForClient.BuffBonusT.BuffCategoryT> get_buff_category(){
			return _buff_category;
		}
		public void add_buff_category(PCMasterInfoForClient.BuffBonusT.BuffCategoryT val){
			if(!has_buff_category()){
				_buff_category = new java.util.LinkedList<PCMasterInfoForClient.BuffBonusT.BuffCategoryT>();
				_bit |= 0x10;
			}
			_buff_category.add(val);
		}
		public boolean has_buff_category(){
			return (_bit & 0x10) == 0x10;
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
			if (has_map_id()){
				for(int val : _map_id){
					size += ProtoOutputStream.computeInt32Size(1, val);
				}
			}
			if (has_desc()){
				size += ProtoOutputStream.computeStringSize(2, _desc);
			}
			if (has_min_level()){
				size += ProtoOutputStream.computeInt32Size(3, _min_level);
			}
			if (has_max_level()){
				size += ProtoOutputStream.computeInt32Size(4, _max_level);
			}
			if (has_buff_category()){
				for(PCMasterInfoForClient.BuffBonusT.BuffCategoryT val : _buff_category){
					size += ProtoOutputStream.computeMessageSize(5, val);
				}
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_map_id()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_desc()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_buff_category()){
				for(PCMasterInfoForClient.BuffBonusT.BuffCategoryT val : _buff_category){
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
			if (has_map_id()){
				for (int val : _map_id){
					output.wirteInt32(1, val);
				}
			}
			if (has_desc()){
				output.writeString(2, _desc);
			}
			if (has_min_level()){
				output.wirteInt32(3, _min_level);
			}
			if (has_max_level()){
				output.wirteInt32(4, _max_level);
			}
			if (has_buff_category()){
				for (PCMasterInfoForClient.BuffBonusT.BuffCategoryT val : _buff_category){
					output.writeMessage(5, val);
				}
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						add_map_id(input.readInt32());
						break;
					}
					case 0x00000012:{
						set_desc(input.readString());
						break;
					}
					case 0x00000018:{
						set_min_level(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_max_level(input.readInt32());
						break;
					}
					case 0x0000002A:{
						add_buff_category((PCMasterInfoForClient.BuffBonusT.BuffCategoryT)input.readMessage(PCMasterInfoForClient.BuffBonusT.BuffCategoryT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[PCMasterInfoForClient.BuffBonusT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class BuffCategoryT implements ProtoMessage{
			public static BuffCategoryT newInstance(){
				return new BuffCategoryT();
			}
			private int _type;
			private java.util.LinkedList<Integer> _group;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private BuffCategoryT(){
			}
			public int get_type(){
				return _type;
			}
			public void set_type(int val){
				_bit |= 0x1;
				_type = val;
			}
			public boolean has_type(){
				return (_bit & 0x1) == 0x1;
			}
			public java.util.LinkedList<Integer> get_group(){
				return _group;
			}
			public void add_group(int val){
				if(!has_group()){
					_group = new java.util.LinkedList<Integer>();
					_bit |= 0x2;
				}
				_group.add(val);
			}
			public boolean has_group(){
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
				if (has_type()){
					size += ProtoOutputStream.computeInt32Size(1, _type);
				}
				if (has_group()){
					for(int val : _group){
						size += ProtoOutputStream.computeInt32Size(2, val);
					}
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_type()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_group()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_type()){
					output.wirteInt32(1, _type);
				}
				if (has_group()){
					for (int val : _group){
						output.wirteInt32(2, val);
					}
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							set_type(input.readInt32());
							break;
						}
						case 0x00000010:{
							add_group(input.readInt32());
							break;
						}
						default:{
							System.out.println(String.format("[PCMasterInfoForClient.BuffBonusT.BuffCategoryT] NEW_TAG : TAG(%d)", tag));
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

