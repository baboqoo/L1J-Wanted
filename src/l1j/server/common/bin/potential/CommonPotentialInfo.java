package l1j.server.common.bin.potential;

import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.server.utils.StringUtil;

public class CommonPotentialInfo implements ProtoMessage{
	public static CommonPotentialInfo newInstance(){
		return new CommonPotentialInfo();
	}
	private java.util.LinkedList<CommonPotentialInfo.BonusInfoT> _bonus_list;
	private java.util.LinkedList<CommonPotentialInfo.MaterialInfoT> _material_list;
	private CommonPotentialInfo.EventInfoT _event_config;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CommonPotentialInfo(){
	}
	
	public java.util.LinkedList<CommonPotentialInfo.BonusInfoT> get_bonus_list(){
		return _bonus_list;
	}
	public void add_bonus_list(CommonPotentialInfo.BonusInfoT val){
		if(!has_bonus_list()){
			_bonus_list = new java.util.LinkedList<CommonPotentialInfo.BonusInfoT>();
			_bit |= 0x1;
		}
		_bonus_list.add(val);
	}
	public CommonPotentialInfo.BonusInfoT get_bonus(int bonus_id) {
		for (CommonPotentialInfo.BonusInfoT bonusT : _bonus_list) {
			if (bonusT.get_bonus_id() == bonus_id) {
				return bonusT;
			}
		}
		return null;
	}
	public HashMap<Integer, ArrayList<CommonPotentialInfo.BonusInfoT>> get_bonus_prob_list(int dollGrade, int bonus_grade, boolean is_event) {
		HashMap<Integer, ArrayList<CommonPotentialInfo.BonusInfoT>> result = new HashMap<>();
		for (CommonPotentialInfo.BonusInfoT bonus : _bonus_list) {
			if (bonus._bonus_grade > dollGrade || bonus._bonus_grade < bonus_grade || (bonus._bonus_id > 141 && bonus._bonus_id < 161)) {
				continue;
			}
			if (is_event && bonus._bonus_grade > bonus_grade) {
				continue;
			}
			ArrayList<CommonPotentialInfo.BonusInfoT> list = result.get(bonus._bonus_grade);
			if (list == null) {
				list = new ArrayList<CommonPotentialInfo.BonusInfoT>();
				result.put(bonus._bonus_grade, list);
			}
			list.add(bonus);
		}
		return result;
	}

	public boolean has_bonus_list(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<CommonPotentialInfo.MaterialInfoT> get_material_list(){
		return _material_list;
	}
	public void add_material_list(CommonPotentialInfo.MaterialInfoT val){
		if(!has_material_list()){
			_material_list = new java.util.LinkedList<CommonPotentialInfo.MaterialInfoT>();
			_bit |= 0x2;
		}
		_material_list.add(val);
	}
	public String get_material_list_toString() {
		if (_material_list == null || _material_list.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (CommonPotentialInfo.MaterialInfoT val : _material_list) {
			if (sb.length() > 0) {
				sb.append(StringUtil.LineString);
			}
			sb.append("GRADE: ").append(val.get_potential_grade());
			sb.append(", NAME_ID: ").append(val.get_nameId());
			sb.append(", AMOUNT: ").append(val.get_amount());
		}
		return sb.toString();
	}
	public boolean has_material_list(){
		return (_bit & 0x2) == 0x2;
	}
	public CommonPotentialInfo.EventInfoT get_event_config(){
		return _event_config;
	}
	public void set_event_config(CommonPotentialInfo.EventInfoT val){
		_bit |= 0x4;
		_event_config = val;
	}
	public String get_event_config_toString() {
		if (_event_config == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		java.util.LinkedList<Integer> event_item_list = _event_config._event_item_list;
		if (event_item_list != null && !event_item_list.isEmpty()) {
			sb.append("ITEM_LIST: ");
			int itemCnt = 0;
			for (int item : event_item_list) {
				if (itemCnt++ > 0) {
					sb.append(StringUtil.CommaString);
				}
				sb.append(item);
			}
		}
		
		sb.append(StringUtil.LineString);
		
		java.util.LinkedList<CommonPotentialInfo.MaterialInfoT> event_material = _event_config._event_material;
		if (event_material != null && !event_material.isEmpty()) {
			sb.append("MATERIAL: ");
			int materialCnt = 0;
			for (CommonPotentialInfo.MaterialInfoT material : event_material) {
				if (materialCnt++ > 0) {
					sb.append(StringUtil.CommaString);
				}
				sb.append("GRADE = ").append(material.get_potential_grade());
				sb.append(" & NAME_ID = ").append(material.get_nameId());
				sb.append(" & AMOUNT = ").append(material.get_amount());
			}
		}
		return sb.toString();
	}
	public boolean has_event_config(){
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
		if (has_bonus_list()){
			for(CommonPotentialInfo.BonusInfoT val : _bonus_list){
				size += ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		if (has_material_list()){
			for(CommonPotentialInfo.MaterialInfoT val : _material_list){
				size += ProtoOutputStream.computeMessageSize(2, val);
			}
		}
		if (has_event_config()){
			size += ProtoOutputStream.computeMessageSize(3, _event_config);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_bonus_list()){
			for(CommonPotentialInfo.BonusInfoT val : _bonus_list){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_material_list()){
			for(CommonPotentialInfo.MaterialInfoT val : _material_list){
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
		if (has_bonus_list()){
			for (CommonPotentialInfo.BonusInfoT val : _bonus_list){
				output.writeMessage(1, val);
			}
		}
		if (has_material_list()){
			for (CommonPotentialInfo.MaterialInfoT val : _material_list){
				output.writeMessage(2, val);
			}
		}
		if (has_event_config()){
			output.writeMessage(3, _event_config);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x0000000A:{
					add_bonus_list((CommonPotentialInfo.BonusInfoT)input.readMessage(CommonPotentialInfo.BonusInfoT.newInstance()));
					break;
				}
				case 0x00000012:{
					add_material_list((CommonPotentialInfo.MaterialInfoT)input.readMessage(CommonPotentialInfo.MaterialInfoT.newInstance()));
					break;
				}
				case 0x0000001A:{
					set_event_config((CommonPotentialInfo.EventInfoT)input.readMessage(CommonPotentialInfo.EventInfoT.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[CommonPotentialInfo] NEW_TAG : TAG(%d)", tag));
					return this;
				}
			}
		}
		return this;
	}

	@Override
	public void dispose(){
		if (has_bonus_list()) {
			for (CommonPotentialInfo.BonusInfoT val : _bonus_list) {
				val.dispose();
			}
			_bonus_list.clear();
			_bonus_list = null;
		}
		if (has_material_list()) {
			for (CommonPotentialInfo.MaterialInfoT val : _material_list) {
				val.dispose();
			}
			_material_list.clear();
			_material_list = null;
		}
		if (has_event_config()) {
			_event_config.dispose();
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	
	public static class BonusInfoT implements ProtoMessage{
		public static BonusInfoT newInstance(){
			return new BonusInfoT();
		}
		private int _bonus_id;
		private int _bonus_grade;
		private int _bonus_desc;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private BonusInfoT(){
		}
		public int get_bonus_id(){
			return _bonus_id;
		}
		public void set_bonus_id(int val){
			_bit |= 0x1;
			_bonus_id = val;
		}
		public boolean has_bonus_id(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_bonus_grade(){
			return _bonus_grade;
		}
		public void set_bonus_grade(int val){
			_bit |= 0x2;
			_bonus_grade = val;
		}
		public boolean has_bonus_grade(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_bonus_desc(){
			return _bonus_desc;
		}
		public void set_bonus_desc(int val){
			_bit |= 0x4;
			_bonus_desc = val;
		}
		public boolean has_bonus_desc(){
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
			if (has_bonus_id()){
				size += ProtoOutputStream.computeInt32Size(1, _bonus_id);
			}
			if (has_bonus_grade()){
				size += ProtoOutputStream.computeInt32Size(2, _bonus_grade);
			}
			if (has_bonus_desc()){
				size += ProtoOutputStream.computeInt32Size(3, _bonus_desc);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_bonus_id()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_bonus_grade()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_bonus_id()){
				output.wirteInt32(1, _bonus_id);
			}
			if (has_bonus_grade()){
				output.wirteInt32(2, _bonus_grade);
			}
			if (has_bonus_desc()){
				output.wirteInt32(3, _bonus_desc);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_bonus_id(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_bonus_grade(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_bonus_desc(input.readInt32());
						break;
					}
					default:{
						System.out.println(String.format("[CommonPotentialInfo.BonusInfoT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class MaterialInfoT implements ProtoMessage{
		public static MaterialInfoT newInstance(){
			return new MaterialInfoT();
		}
		private int _potential_grade;
		private int _nameId;
		private int _amount;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private MaterialInfoT(){
		}
		public int get_potential_grade(){
			return _potential_grade;
		}
		public void set_potential_grade(int val){
			_bit |= 0x1;
			_potential_grade = val;
		}
		public boolean has_potential_grade(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_nameId(){
			return _nameId;
		}
		public void set_nameId(int val){
			_bit |= 0x2;
			_nameId = val;
		}
		public boolean has_nameId(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_amount(){
			return _amount;
		}
		public void set_amount(int val){
			_bit |= 0x4;
			_amount = val;
		}
		public boolean has_amount(){
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
			if (has_potential_grade()){
				size += ProtoOutputStream.computeInt32Size(1, _potential_grade);
			}
			if (has_nameId()){
				size += ProtoOutputStream.computeInt32Size(2, _nameId);
			}
			if (has_amount()){
				size += ProtoOutputStream.computeInt32Size(3, _amount);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_potential_grade()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_nameId()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_amount()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_potential_grade()){
				output.wirteInt32(1, _potential_grade);
			}
			if (has_nameId()){
				output.wirteInt32(2, _nameId);
			}
			if (has_amount()){
				output.wirteInt32(3, _amount);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_potential_grade(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_nameId(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_amount(input.readInt32());
						break;
					}
					default:{
						System.out.println(String.format("[CommonPotentialInfo.MaterialInfoT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class EventInfoT implements ProtoMessage{
		public static EventInfoT newInstance(){
			return new EventInfoT();
		}
		private java.util.LinkedList<Integer> _event_item_list;
		private java.util.LinkedList<CommonPotentialInfo.MaterialInfoT> _event_material;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private EventInfoT(){
		}
		public java.util.LinkedList<Integer> get_event_item_list(){
			return _event_item_list;
		}
		public void add_event_item_list(int val){
			if(!has_event_item_list()){
				_event_item_list = new java.util.LinkedList<Integer>();
				_bit |= 0x1;
			}
			_event_item_list.add(val);
		}
		public boolean has_event_item_list(){
			return (_bit & 0x1) == 0x1;
		}
		public java.util.LinkedList<CommonPotentialInfo.MaterialInfoT> get_event_material(){
			return _event_material;
		}
		public void add_event_material(CommonPotentialInfo.MaterialInfoT val){
			if(!has_event_material()){
				_event_material = new java.util.LinkedList<CommonPotentialInfo.MaterialInfoT>();
				_bit |= 0x2;
			}
			_event_material.add(val);
		}
		public CommonPotentialInfo.MaterialInfoT get_event_material(int grade) {
			if(!has_event_material()){
				return null;
			}
			for (CommonPotentialInfo.MaterialInfoT val : _event_material) {
				if (val.get_potential_grade() == grade) {
					return val;
				}
			}
			return null;
		}
		public boolean has_event_material(){
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
			if (has_event_item_list()){
				for(int val : _event_item_list){
					size += ProtoOutputStream.computeInt32Size(1, val);
				}
			}
			if (has_event_material()){
				for(CommonPotentialInfo.MaterialInfoT val : _event_material){
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
			if (has_event_item_list()){
				if (_event_item_list.isEmpty()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
			if (has_event_material()){
				for(CommonPotentialInfo.MaterialInfoT val : _event_material){
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
			if (has_event_item_list()){
				for (int val : _event_item_list){
					output.wirteInt32(1, val);
				}
			}
			if (has_event_material()){
				for (CommonPotentialInfo.MaterialInfoT val : _event_material){
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
						add_event_item_list(input.readInt32());
						break;
					}
					case 0x00000012:{
						add_event_material((CommonPotentialInfo.MaterialInfoT)input.readMessage(CommonPotentialInfo.MaterialInfoT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[CommonPotentialInfo.EventInfoT] NEW_TAG : TAG(%d)", tag));
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public void dispose(){
			if (has_event_item_list()) {
				_event_item_list.clear();
				_event_item_list = null;
			}
			if (has_event_material()) {
				for (CommonPotentialInfo.MaterialInfoT val : _event_material) {
					val.dispose();
				}
				_event_material.clear();
				_event_material = null;
			}
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
}

