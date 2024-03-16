package l1j.server.common.bin.chargedtimemap;

import l1j.server.common.StringKLoader;
import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.server.utils.StringUtil;

public class ChargedTimeMapDataT implements ProtoMessage{
	public static ChargedTimeMapDataT newInstance(){
		return new ChargedTimeMapDataT();
	}
	private java.util.LinkedList<ChargedTimeMapDataT.GroupT> _groups;
	private java.util.LinkedList<ChargedTimeMapDataT.MultiGroupListT> _multi_group_list;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private ChargedTimeMapDataT(){
	}
	public java.util.LinkedList<ChargedTimeMapDataT.GroupT> get_groups(){
		return _groups;
	}
	public ChargedTimeMapDataT.GroupT get_group(int group){
		for (ChargedTimeMapDataT.GroupT val : _groups) {
			if (val.get_group() == group) {
				return val;
			}
		}
		return null;
	}
	public void add_groups(ChargedTimeMapDataT.GroupT val){
		if(!has_groups()){
			_groups = new java.util.LinkedList<ChargedTimeMapDataT.GroupT>();
			_bit |= 0x1;
		}
		_groups.add(val);
	}
	public String get_groups_toString(){
		if (!has_groups() || _groups.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (ChargedTimeMapDataT.GroupT groupT : _groups) {
			if (sb.length() > 0) {
				sb.append(StringUtil.LineString);
			}
			
			sb.append("GROUP: ").append(groupT._group);
			sb.append(", MAX_CHARGED_COUNT: ").append(groupT._max_charge_count);
			sb.append(", MAX_CHARGED_TIME: ").append(groupT._max_charge_time);
			sb.append(", COST_PER_TIME: ").append(groupT._cost_per_time);
			sb.append(", STR_INDEX: ").append(groupT._str_index);
			sb.append(" [").append(groupT._str_index >= 0 ? StringKLoader.getString(groupT._str_index) : "NULL").append("]");
		}
		return sb.toString();
	}
	public boolean has_groups(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<ChargedTimeMapDataT.MultiGroupListT> get_multi_group_list(){
		return _multi_group_list;
	}
	public ChargedTimeMapDataT.MultiGroupListT get_multi_group(int group) {
		for (ChargedTimeMapDataT.MultiGroupListT val : _multi_group_list) {
			if (val.get_group() == group) {
				return val;
			}
		}
		return null;
	}
	public void add_multi_group_list(ChargedTimeMapDataT.MultiGroupListT val){
		if(!has_multi_group_list()){
			_multi_group_list = new java.util.LinkedList<ChargedTimeMapDataT.MultiGroupListT>();
			_bit |= 0x2;
		}
		_multi_group_list.add(val);
	}
	public String get_multi_group_list_toString(){
		if (!has_multi_group_list() || _multi_group_list.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (ChargedTimeMapDataT.MultiGroupListT listT : _multi_group_list) {
			if (sb.length() > 0) {
				sb.append(StringUtil.LineString);
			}
			sb.append("GROUP: ").append(listT._group);
			sb.append(", COMPONENTS: ").append(listT.get_components());
		}
		return sb.toString();
	}
	public boolean has_multi_group_list(){
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
		if (has_groups()){
			for(ChargedTimeMapDataT.GroupT val : _groups){
				size += ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		if (has_multi_group_list()){
			for(ChargedTimeMapDataT.MultiGroupListT val : _multi_group_list){
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
		if (has_groups()){
			for(ChargedTimeMapDataT.GroupT val : _groups){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_multi_group_list()){
			for(ChargedTimeMapDataT.MultiGroupListT val : _multi_group_list){
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
		if (has_groups()){
			for (ChargedTimeMapDataT.GroupT val : _groups){
				output.writeMessage(1, val);
			}
		}
		if (has_multi_group_list()){
			for (ChargedTimeMapDataT.MultiGroupListT val : _multi_group_list){
				output.writeMessage(2, val);
			}
		}
	}

	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x0000000A:{
					add_groups((ChargedTimeMapDataT.GroupT)input.readMessage(ChargedTimeMapDataT.GroupT.newInstance()));
					break;
				}
				case 0x00000012:{
					add_multi_group_list((ChargedTimeMapDataT.MultiGroupListT)input.readMessage(ChargedTimeMapDataT.MultiGroupListT.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[ChargedTimeMapDataT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class GroupT implements ProtoMessage{
		public static GroupT newInstance(){
			return new GroupT();
		}
		private int _group;
		private int _max_charge_count;
		private int _max_charge_time;
		private int _cost_per_time;
		private int _str_index;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private GroupT(){
		}
		public int get_group(){
			return _group;
		}
		public void set_group(int val){
			_bit |= 0x1;
			_group = val;
		}
		public boolean has_group(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_max_charge_count(){
			return _max_charge_count;
		}
		public void set_max_charge_count(int val){
			_bit |= 0x2;
			_max_charge_count = val;
		}
		public boolean has_max_charge_count(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_max_charge_time(){
			return _max_charge_time;
		}
		public void set_max_charge_time(int val){
			_bit |= 0x4;
			_max_charge_time = val;
		}
		public boolean has_max_charge_time(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_cost_per_time(){
			return _cost_per_time;
		}
		public void set_cost_per_time(int val){
			_bit |= 0x8;
			_cost_per_time = val;
		}
		public boolean has_cost_per_time(){
			return (_bit & 0x8) == 0x8;
		}
		public int get_str_index(){
			return _str_index;
		}
		public void set_str_index(int val){
			_bit |= 0x10;
			_str_index = val;
		}
		public boolean has_str_index(){
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
			if (has_group()){
				size += ProtoOutputStream.computeInt32Size(1, _group);
			}
			if (has_max_charge_count()){
				size += ProtoOutputStream.computeInt32Size(2, _max_charge_count);
			}
			if (has_max_charge_time()){
				size += ProtoOutputStream.computeInt32Size(3, _max_charge_time);
			}
			if (has_cost_per_time()){
				size += ProtoOutputStream.computeInt32Size(4, _cost_per_time);
			}
			if (has_str_index()){
				size += ProtoOutputStream.computeInt32Size(5, _str_index);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_group()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_max_charge_count()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_max_charge_time()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_cost_per_time()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_group()){
				output.wirteInt32(1, _group);
			}
			if (has_max_charge_count()){
				output.wirteInt32(2, _max_charge_count);
			}
			if (has_max_charge_time()){
				output.wirteInt32(3, _max_charge_time);
			}
			if (has_cost_per_time()){
				output.wirteInt32(4, _cost_per_time);
			}
			if (has_str_index()){
				output.wirteInt32(5, _str_index);
			}
		}

		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_group(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_max_charge_count(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_max_charge_time(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_cost_per_time(input.readInt32());
						break;
					}
					case 0x00000028:{
						set_str_index(input.readInt32());
						break;
					}
					default:{
						System.out.println(String.format("[ChargedTimeMapDataT.GroupT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class MultiGroupListT implements ProtoMessage{
		public static MultiGroupListT newInstance(){
			return new MultiGroupListT();
		}
		private int _group;
		private java.util.LinkedList<Integer> _components;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private MultiGroupListT(){
		}
		public int get_group(){
			return _group;
		}
		public void set_group(int val){
			_bit |= 0x1;
			_group = val;
		}
		public boolean has_group(){
			return (_bit & 0x1) == 0x1;
		}
		public java.util.LinkedList<Integer> get_components(){
			return _components;
		}
		public void add_components(int val){
			if(!has_components()){
				_components = new java.util.LinkedList<Integer>();
				_bit |= 0x2;
			}
			_components.add(val);
		}
		public boolean has_components(){
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
			if (has_group()){
				size += ProtoOutputStream.computeInt32Size(1, _group);
			}
			if (has_components()){
				for(int val : _components){
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
			if (!has_group()){
				_memorizedIsInitialized = -1;
				return false;
			}
			/*if (has_components()){
				for(int val : _components){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}*/
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_group()){
				output.wirteInt32(1, _group);
			}
			if (has_components()){
				for (int val : _components){
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
						set_group(input.readInt32());
						break;
					}
					case 0x00000010:{
						add_components(input.readInt32());
						break;
					}
					default:{
						System.out.println(String.format("[ChargedTimeMapDataT.MultiGroupListT] NEW_TAG : TAG(%d)", tag));
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

