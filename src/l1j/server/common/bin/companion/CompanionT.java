package l1j.server.common.bin.companion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.server.utils.StringUtil;

public class CompanionT implements ProtoMessage{
	public static CompanionT newInstance(){
		return new CompanionT();
	}
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CompanionT(){
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
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
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
	public static class StatT implements ProtoMessage{
		public static StatT newInstance(){
			return new StatT();
		}
		private java.util.LinkedList<CompanionT.StatT.BaseStatBonusT> _BaseStatBonus;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private StatT(){
		}
		public java.util.LinkedList<CompanionT.StatT.BaseStatBonusT> get_BaseStatBonus(){
			return _BaseStatBonus;
		}
		public void add_BaseStatBonus(CompanionT.StatT.BaseStatBonusT val){
			if(!has_BaseStatBonus()){
				_BaseStatBonus = new java.util.LinkedList<CompanionT.StatT.BaseStatBonusT>();
				_bit |= 0x1;
			}
			_BaseStatBonus.add(val);
		}
		public boolean has_BaseStatBonus(){
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
			if (has_BaseStatBonus()){
				for(CompanionT.StatT.BaseStatBonusT val : _BaseStatBonus){
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
			if (has_BaseStatBonus()){
				for(CompanionT.StatT.BaseStatBonusT val : _BaseStatBonus){
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
			if (has_BaseStatBonus()){
				for (CompanionT.StatT.BaseStatBonusT val : _BaseStatBonus){
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
						add_BaseStatBonus((CompanionT.StatT.BaseStatBonusT)input.readMessage(CompanionT.StatT.BaseStatBonusT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[CompanionT.StatT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class BaseStatBonusT implements ProtoMessage{
			public static BaseStatBonusT newInstance(){
				return new BaseStatBonusT();
			}
			private int _id;
			private CompanionT.eStatType _statType;
			private int _value;
			private int _meleeDmg;
			private int _meleeHit;
			private int _regenHP;
			private int _AC;
			private int _spellDmg;
			private int _spellHit;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private BaseStatBonusT(){
			}
			public BaseStatBonusT(ResultSet rs) throws SQLException {
				_id				= rs.getInt("id");
				String statType	= rs.getString("statType");
				statType		= statType.substring(statType.indexOf("(") + 1, statType.indexOf(")"));
				_statType		= CompanionT.eStatType.fromInt(Integer.parseInt(statType));
				_value			= rs.getInt("value");
				_meleeDmg		= rs.getInt("meleeDmg");
				_meleeHit		= rs.getInt("meleeHit");
				_regenHP		= rs.getInt("regenHP");
				_AC				= rs.getInt("ac");
				_spellDmg		= rs.getInt("spellDmg");
				_spellHit		= rs.getInt("spellHit");
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
			public CompanionT.eStatType get_statType(){
				return _statType;
			}
			public void set_statType(CompanionT.eStatType val){
				_bit |= 0x2;
				_statType = val;
			}
			public boolean has_statType(){
				return (_bit & 0x2) == 0x2;
			}
			public int get_value(){
				return _value;
			}
			public void set_value(int val){
				_bit |= 0x4;
				_value = val;
			}
			public boolean has_value(){
				return (_bit & 0x4) == 0x4;
			}
			public int get_meleeDmg(){
				return _meleeDmg;
			}
			public void set_meleeDmg(int val){
				_bit |= 0x8;
				_meleeDmg = val;
			}
			public boolean has_meleeDmg(){
				return (_bit & 0x8) == 0x8;
			}
			public int get_meleeHit(){
				return _meleeHit;
			}
			public void set_meleeHit(int val){
				_bit |= 0x10;
				_meleeHit = val;
			}
			public boolean has_meleeHit(){
				return (_bit & 0x10) == 0x10;
			}
			public int get_regenHP(){
				return _regenHP;
			}
			public void set_regenHP(int val){
				_bit |= 0x20;
				_regenHP = val;
			}
			public boolean has_regenHP(){
				return (_bit & 0x20) == 0x20;
			}
			public int get_AC(){
				return _AC;
			}
			public void set_AC(int val){
				_bit |= 0x40;
				_AC = val;
			}
			public boolean has_AC(){
				return (_bit & 0x40) == 0x40;
			}
			public int get_spellDmg(){
				return _spellDmg;
			}
			public void set_spellDmg(int val){
				_bit |= 0x80;
				_spellDmg = val;
			}
			public boolean has_spellDmg(){
				return (_bit & 0x80) == 0x80;
			}
			public int get_spellHit(){
				return _spellHit;
			}
			public void set_spellHit(int val){
				_bit |= 0x100;
				_spellHit = val;
			}
			public boolean has_spellHit(){
				return (_bit & 0x100) == 0x100;
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
				if (has_statType()){
					size += ProtoOutputStream.computeEnumSize(2, _statType.toInt());
				}
				if (has_value()){
					size += ProtoOutputStream.computeInt32Size(3, _value);
				}
				if (has_meleeDmg()){
					size += ProtoOutputStream.computeInt32Size(4, _meleeDmg);
				}
				if (has_meleeHit()){
					size += ProtoOutputStream.computeInt32Size(5, _meleeHit);
				}
				if (has_regenHP()){
					size += ProtoOutputStream.computeInt32Size(6, _regenHP);
				}
				if (has_AC()){
					size += ProtoOutputStream.computeInt32Size(7, _AC);
				}
				if (has_spellDmg()){
					size += ProtoOutputStream.computeInt32Size(8, _spellDmg);
				}
				if (has_spellHit()){
					size += ProtoOutputStream.computeInt32Size(9, _spellHit);
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
				if (!has_statType()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_value()){
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
				if (has_statType()){
					output.writeEnum(2, _statType.toInt());
				}
				if (has_value()){
					output.wirteInt32(3, _value);
				}
				if (has_meleeDmg()){
					output.wirteInt32(4, _meleeDmg);
				}
				if (has_meleeHit()){
					output.wirteInt32(5, _meleeHit);
				}
				if (has_regenHP()){
					output.wirteInt32(6, _regenHP);
				}
				if (has_AC()){
					output.wirteInt32(7, _AC);
				}
				if (has_spellDmg()){
					output.wirteInt32(8, _spellDmg);
				}
				if (has_spellHit()){
					output.wirteInt32(9, _spellHit);
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
							set_statType(CompanionT.eStatType.fromInt(input.readEnum()));
							break;
						}
						case 0x00000018:{
							set_value(input.readInt32());
							break;
						}
						case 0x00000020:{
							set_meleeDmg(input.readInt32());
							break;
						}
						case 0x00000028:{
							set_meleeHit(input.readInt32());
							break;
						}
						case 0x00000030:{
							set_regenHP(input.readInt32());
							break;
						}
						case 0x00000038:{
							set_AC(input.readInt32());
							break;
						}
						case 0x00000040:{
							set_spellDmg(input.readInt32());
							break;
						}
						case 0x00000048:{
							set_spellHit(input.readInt32());
							break;
						}
						default:{
							System.out.println(String.format("[CompanionT.BaseStatBonusT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class SkillEnchantTierT implements ProtoMessage{
		public static SkillEnchantTierT newInstance(){
			return new SkillEnchantTierT();
		}
		private int _tier;
		private CompanionT.SkillEnchantTierT.EnchantCostT _EnchantCost;
		private CompanionT.SkillEnchantTierT.OpenCostT _OpenCost;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private SkillEnchantTierT(){
		}
		public SkillEnchantTierT(ResultSet rs) throws SQLException {
			_tier				= rs.getInt("tier");
			String enchantCost	= rs.getString("enchantCost");
			if (!StringUtil.isNullOrEmpty(enchantCost)) {
				CompanionT.SkillEnchantTierT.EnchantCostT cost = CompanionT.SkillEnchantTierT.EnchantCostT.newInstance();
				StringTokenizer st	= new StringTokenizer(enchantCost, StringUtil.LineString);
				while (st.hasMoreElements()) {
					String token	= st.nextToken();
					if (token.startsWith("FRIEND_SHIP: ")) {
						token		= token.replace("FRIEND_SHIP: ", StringUtil.EmptyString);
						StringTokenizer friendship	= new StringTokenizer(token.trim(), " OR ");
						while (friendship.hasMoreElements()) {
							cost.add_friendship(Integer.parseInt(friendship.nextToken().trim()));
						}
					} else if (token.startsWith("CATALYST_ITEM: : ")) {
						token		= token.replace("CATALYST_ITEM: : ", StringUtil.EmptyString);
						cost.set_catalystItem(Integer.parseInt(token.trim()));
					}
				}
				set_EnchantCost(cost);
			}
			
			String openCost		= rs.getString("openCost");
			if (!StringUtil.isNullOrEmpty(openCost)) {
				CompanionT.SkillEnchantTierT.OpenCostT cost = CompanionT.SkillEnchantTierT.OpenCostT.newInstance();
				StringTokenizer st	= new StringTokenizer(openCost, StringUtil.LineString);
				while (st.hasMoreElements()) {
					String token	= st.nextToken();
					if (token.startsWith("LEVEL: ")) {
						token		= token.replace("LEVEL: ", StringUtil.EmptyString);
						cost.set_level(Integer.parseInt(token.trim()));
					} else if (token.startsWith("MIN_ENCHANT: ")) {
						token		= token.replace("MIN_ENCHANT: ", StringUtil.EmptyString);
						cost.set_minEnchant(Integer.parseInt(token.trim()));
					} else if (token.startsWith("FRIENSHIP: ")) {
						token		= token.replace("FRIENSHIP: ", StringUtil.EmptyString);
						cost.set_friendship(Integer.parseInt(token.trim()));
					} else if (token.startsWith("ADENA: ")) {
						token		= token.replace("ADENA: ", StringUtil.EmptyString);
						cost.set_adena(Integer.parseInt(token.trim()));
					}
				}
				set_OpenCost(cost);
			}
		}
		public int get_tier(){
			return _tier;
		}
		public void set_tier(int val){
			_bit |= 0x1;
			_tier = val;
		}
		public boolean has_tier(){
			return (_bit & 0x1) == 0x1;
		}
		public CompanionT.SkillEnchantTierT.EnchantCostT get_EnchantCost(){
			return _EnchantCost;
		}
		public void set_EnchantCost(CompanionT.SkillEnchantTierT.EnchantCostT val){
			_bit |= 0x2;
			_EnchantCost = val;
		}
		public String get_EnchantCost_toString() {
			if (_EnchantCost == null) {
				return null;
			}
			StringBuilder sb = new StringBuilder();
			java.util.LinkedList<Integer> friendship = _EnchantCost.get_friendship();
			if (friendship != null && !friendship.isEmpty()) {
				sb.append("FRIEND_SHIP: ");
				int friendshipCnt = 0;
				for (int val : friendship) {
					if (friendshipCnt++ > 0) {
						sb.append(" OR ");
					}
					sb.append(val);
				}
			}
			sb.append(StringUtil.LineString);
			if (_EnchantCost.has_catalystItem()) {
				sb.append("CATALYST_ITEM: : ").append(_EnchantCost.get_catalystItem());
			}
			return sb.toString();
		}
		public boolean has_EnchantCost(){
			return (_bit & 0x2) == 0x2;
		}
		public CompanionT.SkillEnchantTierT.OpenCostT get_OpenCost(){
			return _OpenCost;
		}
		public void set_OpenCost(CompanionT.SkillEnchantTierT.OpenCostT val){
			_bit |= 0x4;
			_OpenCost = val;
		}
		public String get_OpenCost_toString() {
			if (_OpenCost == null) {
				return null;
			}
			StringBuilder sb = new StringBuilder();
			if (_OpenCost.has_level()) {
				sb.append("LEVEL: ").append(_OpenCost.get_level()).append(StringUtil.LineString);
			}
			if (_OpenCost.has_minEnchant()) {
				sb.append("MIN_ENCHANT: ").append(_OpenCost.get_minEnchant()).append(StringUtil.LineString);
			}
			if (_OpenCost.has_friendship()) {
				sb.append("FRIENSHIP: ").append(_OpenCost.get_friendship()).append(StringUtil.LineString);
			}
			if (_OpenCost.has_adena()) {
				sb.append("ADENA: ").append(_OpenCost.get_adena()).append(StringUtil.LineString);
			}
			return sb.toString();
		}
		public boolean has_OpenCost(){
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
			if (has_tier()){
				size += ProtoOutputStream.computeUInt32Size(1, _tier);
			}
			if (has_EnchantCost()){
				size += ProtoOutputStream.computeMessageSize(2, _EnchantCost);
			}
			if (has_OpenCost()){
				size += ProtoOutputStream.computeMessageSize(3, _OpenCost);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_tier()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_EnchantCost()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_OpenCost()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_tier()){
				output.writeUInt32(1, _tier);
			}
			if (has_EnchantCost()){
				output.writeMessage(2, _EnchantCost);
			}
			if (has_OpenCost()){
				output.writeMessage(3, _OpenCost);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_tier(input.readUInt32());
						break;
					}
					case 0x00000012:{
						set_EnchantCost((CompanionT.SkillEnchantTierT.EnchantCostT)input.readMessage(CompanionT.SkillEnchantTierT.EnchantCostT.newInstance()));
						break;
					}
					case 0x0000001A:{
						set_OpenCost((CompanionT.SkillEnchantTierT.OpenCostT)input.readMessage(CompanionT.SkillEnchantTierT.OpenCostT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[CompanionT.SkillEnchantTierT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class EnchantCostT implements ProtoMessage{
			public static EnchantCostT newInstance(){
				return new EnchantCostT();
			}
			private java.util.LinkedList<Integer> _friendship;
			private int _catalystItem;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private EnchantCostT(){
			}
			public java.util.LinkedList<Integer> get_friendship(){
				return _friendship;
			}
			public void add_friendship(int val){
				if(!has_friendship()){
					_friendship = new java.util.LinkedList<Integer>();
					_bit |= 0x1;
				}
				_friendship.add(val);
			}
			public boolean has_friendship(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_catalystItem(){
				return _catalystItem;
			}
			public void set_catalystItem(int val){
				_bit |= 0x2;
				_catalystItem = val;
			}
			public boolean has_catalystItem(){
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
				if (has_friendship()){
					for(int val : _friendship){
						size += ProtoOutputStream.computeUInt32Size(1, val);
					}
				}
				if (has_catalystItem()){
					size += ProtoOutputStream.computeUInt32Size(2, _catalystItem);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (has_friendship()){
					if (_friendship.isEmpty()) {
						_memorizedIsInitialized = -1;
						return false;
					}
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_friendship()){
					for (int val : _friendship){
						output.writeUInt32(1, val);
					}
				}
				if (has_catalystItem()){
					output.writeUInt32(2, _catalystItem);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							add_friendship(input.readUInt32());
							break;
						}
						case 0x00000010:{
							set_catalystItem(input.readUInt32());
							break;
						}
						default:{
							System.out.println(String.format("[CompanionT.EnchantCostT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class OpenCostT implements ProtoMessage{
			public static OpenCostT newInstance(){
				return new OpenCostT();
			}
			private int _level;
			private int _minEnchant;
			private int _friendship;
			private int _adena;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private OpenCostT(){
			}
			public int get_level(){
				return _level;
			}
			public void set_level(int val){
				_bit |= 0x1;
				_level = val;
			}
			public boolean has_level(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_minEnchant(){
				return _minEnchant;
			}
			public void set_minEnchant(int val){
				_bit |= 0x2;
				_minEnchant = val;
			}
			public boolean has_minEnchant(){
				return (_bit & 0x2) == 0x2;
			}
			public int get_friendship(){
				return _friendship;
			}
			public void set_friendship(int val){
				_bit |= 0x4;
				_friendship = val;
			}
			public boolean has_friendship(){
				return (_bit & 0x4) == 0x4;
			}
			public int get_adena(){
				return _adena;
			}
			public void set_adena(int val){
				_bit |= 0x8;
				_adena = val;
			}
			public boolean has_adena(){
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
				if (has_level()){
					size += ProtoOutputStream.computeUInt32Size(1, _level);
				}
				if (has_minEnchant()){
					size += ProtoOutputStream.computeUInt32Size(2, _minEnchant);
				}
				if (has_friendship()){
					size += ProtoOutputStream.computeUInt32Size(3, _friendship);
				}
				if (has_adena()){
					size += ProtoOutputStream.computeUInt32Size(4, _adena);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_level()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_level()){
					output.writeUInt32(1, _level);
				}
				if (has_minEnchant()){
					output.writeUInt32(2, _minEnchant);
				}
				if (has_friendship()){
					output.writeUInt32(3, _friendship);
				}
				if (has_adena()){
					output.writeUInt32(4, _adena);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							set_level(input.readUInt32());
							break;
						}
						case 0x00000010:{
							set_minEnchant(input.readUInt32());
							break;
						}
						case 0x00000018:{
							set_friendship(input.readUInt32());
							break;
						}
						case 0x00000020:{
							set_adena(input.readUInt32());
							break;
						}
						default:{
							System.out.println(String.format("[CompanionT.OpenCostT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class WildSkillT implements ProtoMessage{
		public static WildSkillT newInstance(){
			return new WildSkillT();
		}
		private java.util.LinkedList<CompanionT.WildSkillT.SkillT> _Skill;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private WildSkillT(){
		}
		public java.util.LinkedList<CompanionT.WildSkillT.SkillT> get_Skill(){
			return _Skill;
		}
		public void add_Skill(CompanionT.WildSkillT.SkillT val){
			if(!has_Skill()){
				_Skill = new java.util.LinkedList<CompanionT.WildSkillT.SkillT>();
				_bit |= 0x1;
			}
			_Skill.add(val);
		}
		public boolean has_Skill(){
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
			if (has_Skill()){
				for(CompanionT.WildSkillT.SkillT val : _Skill){
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
			if (has_Skill()){
				for(CompanionT.WildSkillT.SkillT val : _Skill){
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
			if (has_Skill()){
				for (CompanionT.WildSkillT.SkillT val : _Skill){
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
						add_Skill((CompanionT.WildSkillT.SkillT)input.readMessage(CompanionT.WildSkillT.SkillT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[CompanionT.WildSkillT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class SkillT implements ProtoMessage{
			public static SkillT newInstance(){
				return new SkillT();
			}
			private int _id;
			private int _descNum;
			private java.util.LinkedList<CompanionT.WildSkillT.SkillT.EnchantBonusT> _EnchantBonus;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private SkillT(){
			}
			public SkillT(ResultSet rs) throws SQLException {
				_id					= rs.getInt("id");
				_descNum			= rs.getInt("descNum");
				String enchantBonus = rs.getString("enchantBonus");
				if (!StringUtil.isNullOrEmpty(enchantBonus)) {
					StringTokenizer st = new StringTokenizer(enchantBonus, StringUtil.LineString);
					while (st.hasMoreElements()) {
						StringTokenizer options = new StringTokenizer(st.nextToken().trim(), StringUtil.CommaString);
						CompanionT.WildSkillT.SkillT.EnchantBonusT bonus = CompanionT.WildSkillT.SkillT.EnchantBonusT.newInstance();
						while (options.hasMoreElements()) {
							String token	= options.nextToken().trim();
							if (token.startsWith("VALUE: ")) {
								token		= token.replace("VALUE: ", StringUtil.EmptyString);
								bonus.set_value(Integer.parseInt(token.trim()));
							} else if (token.startsWith("MELEE_DMG: ")) {
								token		= token.replace("MELEE_DMG: ", StringUtil.EmptyString);
								bonus.set_meleeDmg(Integer.parseInt(token.trim()));
							} else if (token.startsWith("MELEE_HIT: ")) {
								token		= token.replace("MELEE_HIT: ", StringUtil.EmptyString);
								bonus.set_meleeHit(Integer.parseInt(token.trim()));
							} else if (token.startsWith("MELEE_CRI_HIT: ")) {
								token		= token.replace("MELEE_CRI_HIT: ", StringUtil.EmptyString);
								bonus.set_meleeCriHit(Double.parseDouble(token.trim()));
							} else if (token.startsWith("IGNORE_REDUCTION: ")) {
								token		= token.replace("IGNORE_REDUCTION: ", StringUtil.EmptyString);
								bonus.set_ignoreReduction(Integer.parseInt(token.trim()));
							} else if (token.startsWith("BLOOD_SUCK_HIT: ")) {
								token		= token.replace("BLOOD_SUCK_HIT: ", StringUtil.EmptyString);
								bonus.set_bloodSuckHit(Double.parseDouble(token.trim()));
							} else if (token.startsWith("BLOOD_SUCK_HEAL: ")) {
								token		= token.replace("BLOOD_SUCK_HEAL: ", StringUtil.EmptyString);
								bonus.set_bloodSuckHeal(Integer.parseInt(token.trim()));
							} else if (token.startsWith("REGEN_HP: ")) {
								token		= token.replace("REGEN_HP: ", StringUtil.EmptyString);
								bonus.set_regenHP(Integer.parseInt(token.trim()));
							} else if (token.startsWith("AC: ")) {
								token		= token.replace("AC: ", StringUtil.EmptyString);
								bonus.set_AC(Integer.parseInt(token.trim()));
							} else if (token.startsWith("MR: ")) {
								token		= token.replace("MR: ", StringUtil.EmptyString);
								bonus.set_MR(Integer.parseInt(token.trim()));
							} else if (token.startsWith("POTION_HP: ")) {
								token		= token.replace("POTION_HP: ", StringUtil.EmptyString);
								bonus.set_potionHP(Integer.parseInt(token.trim()));
							} else if (token.startsWith("DMG_REDUCTOIN: ")) {
								token		= token.replace("DMG_REDUCTOIN: ", StringUtil.EmptyString);
								bonus.set_dmgReduction(Integer.parseInt(token.trim()));
							} else if (token.startsWith("MAX_HP: ")) {
								token		= token.replace("MAX_HP: ", StringUtil.EmptyString);
								bonus.set_maxHP(Integer.parseInt(token.trim()));
							} else if (token.startsWith("SPELL_DMG_MULTI: ")) {
								token		= token.replace("SPELL_DMG_MULTI: ", StringUtil.EmptyString);
								bonus.set_spellDmgMulti(Integer.parseInt(token.trim()));
							} else if (token.startsWith("SPELL_HIT: ")) {
								token		= token.replace("SPELL_HIT: ", StringUtil.EmptyString);
								bonus.set_spellHit(Integer.parseInt(token.trim()));
							} else if (token.startsWith("MOVE_DELAY_REDUCE: ")) {
								token		= token.replace("MOVE_DELAY_REDUCE: ", StringUtil.EmptyString);
								bonus.set_moveDelayReduce(Double.parseDouble(token.trim()));
							} else if (token.startsWith("ATTACK_DELAY_REDUCE: ")) {
								token		= token.replace("ATTACK_DELAY_REDUCE: ", StringUtil.EmptyString);
								bonus.set_attackDelayReduce(Double.parseDouble(token.trim()));
							} else if (token.startsWith("FIRE_ELEMENTAL_DMG: ")) {
								token		= token.replace("FIRE_ELEMENTAL_DMG: ", StringUtil.EmptyString);
								bonus.set_fireElementalDmg(Integer.parseInt(token.trim()));
							} else if (token.startsWith("WATER_ELEMENTAL_DMG: ")) {
								token		= token.replace("WATER_ELEMENTAL_DMG: ", StringUtil.EmptyString);
								bonus.set_waterElementalDmg(Integer.parseInt(token.trim()));
							} else if (token.startsWith("AIR_ELEMENTAL_DMG: ")) {
								token		= token.replace("AIR_ELEMENTAL_DMG: ", StringUtil.EmptyString);
								bonus.set_airElementalDmg(Integer.parseInt(token.trim()));
							} else if (token.startsWith("EARTH_ELEMENTAL_DMG: ")) {
								token		= token.replace("EARTH_ELEMENTAL_DMG: ", StringUtil.EmptyString);
								bonus.set_earthElementalDmg(Integer.parseInt(token.trim()));
							} else if (token.startsWith("LIGHT_ELEMENTAL_DMG: ")) {
								token		= token.replace("LIGHT_ELEMENTAL_DMG: ", StringUtil.EmptyString);
								bonus.set_lightElementalDmg(Integer.parseInt(token.trim()));
							} else if (token.startsWith("COMBO_DMG_MULTI: ")) {
								token		= token.replace("COMBO_DMG_MULTI: ", StringUtil.EmptyString);
								bonus.set_comboDmgMulti(Integer.parseInt(token.trim()));
							} else if (token.startsWith("SPELL_DMG_ADD: ")) {
								token		= token.replace("SPELL_DMG_ADD: ", StringUtil.EmptyString);
								bonus.set_spellDmgAdd(Integer.parseInt(token.trim()));
							}
						}
						add_EnchantBonus(bonus);
					}
				}
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
			public int get_descNum(){
				return _descNum;
			}
			public void set_descNum(int val){
				_bit |= 0x2;
				_descNum = val;
			}
			public boolean has_descNum(){
				return (_bit & 0x2) == 0x2;
			}
			public java.util.LinkedList<CompanionT.WildSkillT.SkillT.EnchantBonusT> get_EnchantBonus(){
				return _EnchantBonus;
			}
			public void add_EnchantBonus(CompanionT.WildSkillT.SkillT.EnchantBonusT val){
				if(!has_EnchantBonus()){
					_EnchantBonus = new java.util.LinkedList<CompanionT.WildSkillT.SkillT.EnchantBonusT>();
					_bit |= 0x4;
				}
				_EnchantBonus.add(val);
			}
			public String get_EnchantBonus_toString() {
				if (_EnchantBonus == null || _EnchantBonus.isEmpty()) {
					return null;
				}
				StringBuilder sb = new StringBuilder();
				for (CompanionT.WildSkillT.SkillT.EnchantBonusT bonus : _EnchantBonus) {
					if (sb.length() > 0) {
						sb.append(StringUtil.LineString);
					}
					sb.append("VALUE: ").append(bonus.get_value());
					if (bonus.has_meleeDmg()) {
						sb.append(", MELEE_DMG: ").append(bonus.get_meleeDmg());
					}
					if (bonus.has_meleeHit()) {
						sb.append(", MELEE_HIT: ").append(bonus.get_meleeHit());
					}
					if (bonus.has_meleeCriHit()) {
						sb.append(", MELEE_CRI_HIT: ").append(bonus.get_meleeCriHit());
					}
					if (bonus.has_ignoreReduction()) {
						sb.append(", IGNORE_REDUCTION: ").append(bonus.get_ignoreReduction());
					}
					if (bonus.has_bloodSuckHit()) {
						sb.append(", BLOOD_SUCK_HIT: ").append(bonus.get_bloodSuckHit());
					}
					if (bonus.has_bloodSuckHeal()) {
						sb.append(", BLOOD_SUCK_HEAL: ").append(bonus.get_bloodSuckHeal());
					}
					if (bonus.has_regenHP()) {
						sb.append(", REGEN_HP: ").append(bonus.get_regenHP());
					}
					if (bonus.has_AC()) {
						sb.append(", AC: ").append(bonus.get_AC());
					}
					if (bonus.has_MR()) {
						sb.append(", MR: ").append(bonus.get_MR());
					}
					if (bonus.has_potionHP()) {
						sb.append(", POTION_HP: ").append(bonus.get_potionHP());
					}
					if (bonus.has_dmgReduction()) {
						sb.append(", DMG_REDUCTOIN: ").append(bonus.get_dmgReduction());
					}
					if (bonus.has_maxHP()) {
						sb.append(", MAX_HP: ").append(bonus.get_maxHP());
					}
					if (bonus.has_spellDmgMulti()) {
						sb.append(", SPELL_DMG_MULTI: ").append(bonus.get_spellDmgMulti());
					}
					if (bonus.has_spellHit()) {
						sb.append(", SPELL_HIT: ").append(bonus.get_spellHit());
					}
					if (bonus.has_moveDelayReduce()) {
						sb.append(", MOVE_DELAY_REDUCE: ").append(bonus.get_moveDelayReduce());
					}
					if (bonus.has_attackDelayReduce()) {
						sb.append(", ATTACK_DELAY_REDUCE: ").append(bonus.get_attackDelayReduce());
					}
					if (bonus.has_fireElementalDmg()) {
						sb.append(", FIRE_ELEMENTAL_DMG: ").append(bonus.get_fireElementalDmg());
					}
					if (bonus.has_waterElementalDmg()) {
						sb.append(", WATER_ELEMENTAL_DMG: ").append(bonus.get_waterElementalDmg());
					}
					if (bonus.has_airElementalDmg()) {
						sb.append(", AIR_ELEMENTAL_DMG: ").append(bonus.get_airElementalDmg());
					}
					if (bonus.has_earthElementalDmg()) {
						sb.append(", EARTH_ELEMENTAL_DMG: ").append(bonus.get_earthElementalDmg());
					}
					if (bonus.has_lightElementalDmg()) {
						sb.append(", LIGHT_ELEMENTAL_DMG: ").append(bonus.get_lightElementalDmg());
					}
					if (bonus.has_comboDmgMulti()) {
						sb.append(", COMBO_DMG_MULTI: ").append(bonus.get_comboDmgMulti());
					}
					if (bonus.has_spellDmgAdd()) {
						sb.append(", SPELL_DMG_ADD: ").append(bonus.get_spellDmgAdd());
					}
				}
				return sb.toString();
			}
			public boolean has_EnchantBonus(){
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
				if (has_descNum()){
					size += ProtoOutputStream.computeInt32Size(2, _descNum);
				}
				if (has_EnchantBonus()){
					for(CompanionT.WildSkillT.SkillT.EnchantBonusT val : _EnchantBonus){
						size += ProtoOutputStream.computeMessageSize(3, val);
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
				if (!has_descNum()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (has_EnchantBonus()){
					for(CompanionT.WildSkillT.SkillT.EnchantBonusT val : _EnchantBonus){
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
				if (has_descNum()){
					output.wirteInt32(2, _descNum);
				}
				if (has_EnchantBonus()){
					for (CompanionT.WildSkillT.SkillT.EnchantBonusT val : _EnchantBonus){
						output.writeMessage(3, val);
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
							set_descNum(input.readInt32());
							break;
						}
						case 0x0000001A:{
							add_EnchantBonus((CompanionT.WildSkillT.SkillT.EnchantBonusT)input.readMessage(CompanionT.WildSkillT.SkillT.EnchantBonusT.newInstance()));
							break;
						}
						default:{
							System.out.println(String.format("[CompanionT.SkillT] NEW_TAG : TAG(%d)", tag));
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
			
			public static class EnchantBonusT implements ProtoMessage{
				public static EnchantBonusT newInstance(){
					return new EnchantBonusT();
				}
				private int _value;
				private int _meleeDmg;
				private int _meleeHit;
				private double _meleeCriHit;
				private int _ignoreReduction;
				private double _bloodSuckHit;
				private int _bloodSuckHeal;
				private int _regenHP;
				private int _AC;
				private int _MR;
				private int _potionHP;
				private int _dmgReduction;
				private int _maxHP;
				private int _spellDmgMulti;
				private int _spellHit;
				private double _moveDelayReduce;
				private double _attackDelayReduce;
				private int _fireElementalDmg;
				private int _waterElementalDmg;
				private int _airElementalDmg;
				private int _earthElementalDmg;
				private int _lightElementalDmg;
				private int _comboDmgMulti;
				private int _spellDmgAdd;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private EnchantBonusT(){
				}
				public int get_value(){
					return _value;
				}
				public void set_value(int val){
					_bit |= 0x1;
					_value = val;
				}
				public boolean has_value(){
					return (_bit & 0x1) == 0x1;
				}
				public int get_meleeDmg(){
					return _meleeDmg;
				}
				public void set_meleeDmg(int val){
					_bit |= 0x2;
					_meleeDmg = val;
				}
				public boolean has_meleeDmg(){
					return (_bit & 0x2) == 0x2;
				}
				public int get_meleeHit(){
					return _meleeHit;
				}
				public void set_meleeHit(int val){
					_bit |= 0x4;
					_meleeHit = val;
				}
				public boolean has_meleeHit(){
					return (_bit & 0x4) == 0x4;
				}
				public double get_meleeCriHit(){
					return _meleeCriHit;
				}
				public void set_meleeCriHit(double val){
					_bit |= 0x8;
					_meleeCriHit = val;
				}
				public boolean has_meleeCriHit(){
					return (_bit & 0x8) == 0x8;
				}
				public int get_ignoreReduction(){
					return _ignoreReduction;
				}
				public void set_ignoreReduction(int val){
					_bit |= 0x10;
					_ignoreReduction = val;
				}
				public boolean has_ignoreReduction(){
					return (_bit & 0x10) == 0x10;
				}
				public double get_bloodSuckHit(){
					return _bloodSuckHit;
				}
				public void set_bloodSuckHit(double val){
					_bit |= 0x20;
					_bloodSuckHit = val;
				}
				public boolean has_bloodSuckHit(){
					return (_bit & 0x20) == 0x20;
				}
				public int get_bloodSuckHeal(){
					return _bloodSuckHeal;
				}
				public void set_bloodSuckHeal(int val){
					_bit |= 0x40;
					_bloodSuckHeal = val;
				}
				public boolean has_bloodSuckHeal(){
					return (_bit & 0x40) == 0x40;
				}
				public int get_regenHP(){
					return _regenHP;
				}
				public void set_regenHP(int val){
					_bit |= 0x80;
					_regenHP = val;
				}
				public boolean has_regenHP(){
					return (_bit & 0x80) == 0x80;
				}
				public int get_AC(){
					return _AC;
				}
				public void set_AC(int val){
					_bit |= 0x100;
					_AC = val;
				}
				public boolean has_AC(){
					return (_bit & 0x100) == 0x100;
				}
				public int get_MR(){
					return _MR;
				}
				public void set_MR(int val){
					_bit |= 0x200;
					_MR = val;
				}
				public boolean has_MR(){
					return (_bit & 0x200) == 0x200;
				}
				public int get_potionHP(){
					return _potionHP;
				}
				public void set_potionHP(int val){
					_bit |= 0x400;
					_potionHP = val;
				}
				public boolean has_potionHP(){
					return (_bit & 0x400) == 0x400;
				}
				public int get_dmgReduction(){
					return _dmgReduction;
				}
				public void set_dmgReduction(int val){
					_bit |= 0x800;
					_dmgReduction = val;
				}
				public boolean has_dmgReduction(){
					return (_bit & 0x800) == 0x800;
				}
				public int get_maxHP(){
					return _maxHP;
				}
				public void set_maxHP(int val){
					_bit |= 0x1000;
					_maxHP = val;
				}
				public boolean has_maxHP(){
					return (_bit & 0x1000) == 0x1000;
				}
				public int get_spellDmgMulti(){
					return _spellDmgMulti;
				}
				public void set_spellDmgMulti(int val){
					_bit |= 0x2000;
					_spellDmgMulti = val;
				}
				public boolean has_spellDmgMulti(){
					return (_bit & 0x2000) == 0x2000;
				}
				public int get_spellHit(){
					return _spellHit;
				}
				public void set_spellHit(int val){
					_bit |= 0x4000;
					_spellHit = val;
				}
				public boolean has_spellHit(){
					return (_bit & 0x4000) == 0x4000;
				}
				public double get_moveDelayReduce(){
					return _moveDelayReduce;
				}
				public void set_moveDelayReduce(double val){
					_bit |= 0x8000;
					_moveDelayReduce = val;
				}
				public boolean has_moveDelayReduce(){
					return (_bit & 0x8000) == 0x8000;
				}
				public double get_attackDelayReduce(){
					return _attackDelayReduce;
				}
				public void set_attackDelayReduce(double val){
					_bit |= 0x10000;
					_attackDelayReduce = val;
				}
				public boolean has_attackDelayReduce(){
					return (_bit & 0x10000) == 0x10000;
				}
				public int get_fireElementalDmg(){
					return _fireElementalDmg;
				}
				public void set_fireElementalDmg(int val){
					_bit |= 0x20000;
					_fireElementalDmg = val;
				}
				public boolean has_fireElementalDmg(){
					return (_bit & 0x20000) == 0x20000;
				}
				public int get_waterElementalDmg(){
					return _waterElementalDmg;
				}
				public void set_waterElementalDmg(int val){
					_bit |= 0x40000;
					_waterElementalDmg = val;
				}
				public boolean has_waterElementalDmg(){
					return (_bit & 0x40000) == 0x40000;
				}
				public int get_airElementalDmg(){
					return _airElementalDmg;
				}
				public void set_airElementalDmg(int val){
					_bit |= 0x80000;
					_airElementalDmg = val;
				}
				public boolean has_airElementalDmg(){
					return (_bit & 0x80000) == 0x80000;
				}
				public int get_earthElementalDmg(){
					return _earthElementalDmg;
				}
				public void set_earthElementalDmg(int val){
					_bit |= 0x100000;
					_earthElementalDmg = val;
				}
				public boolean has_earthElementalDmg(){
					return (_bit & 0x100000) == 0x100000;
				}
				public int get_lightElementalDmg(){
					return _lightElementalDmg;
				}
				public void set_lightElementalDmg(int val){
					_bit |= 0x200000;
					_lightElementalDmg = val;
				}
				public boolean has_lightElementalDmg(){
					return (_bit & 0x200000) == 0x200000;
				}
				public int get_comboDmgMulti(){
					return _comboDmgMulti;
				}
				public void set_comboDmgMulti(int val){
					_bit |= 0x400000;
					_comboDmgMulti = val;
				}
				public boolean has_comboDmgMulti(){
					return (_bit & 0x400000) == 0x400000;
				}
				public int get_spellDmgAdd(){
					return _spellDmgAdd;
				}
				public void set_spellDmgAdd(int val){
					_bit |= 0x800000;
					_spellDmgAdd = val;
				}
				public boolean has_spellDmgAdd(){
					return (_bit & 0x800000) == 0x800000;
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
					if (has_value()){
						size += ProtoOutputStream.computeInt32Size(1, _value);
					}
					if (has_meleeDmg()){
						size += ProtoOutputStream.computeInt32Size(2, _meleeDmg);
					}
					if (has_meleeHit()){
						size += ProtoOutputStream.computeInt32Size(3, _meleeHit);
					}
					if (has_meleeCriHit()){
						size += ProtoOutputStream.computeDoubleSize(4, _meleeCriHit);
					}
					if (has_ignoreReduction()){
						size += ProtoOutputStream.computeInt32Size(5, _ignoreReduction);
					}
					if (has_bloodSuckHit()){
						size += ProtoOutputStream.computeDoubleSize(6, _bloodSuckHit);
					}
					if (has_bloodSuckHeal()){
						size += ProtoOutputStream.computeInt32Size(7, _bloodSuckHeal);
					}
					if (has_regenHP()){
						size += ProtoOutputStream.computeInt32Size(8, _regenHP);
					}
					if (has_AC()){
						size += ProtoOutputStream.computeInt32Size(9, _AC);
					}
					if (has_MR()){
						size += ProtoOutputStream.computeInt32Size(10, _MR);
					}
					if (has_potionHP()){
						size += ProtoOutputStream.computeInt32Size(11, _potionHP);
					}
					if (has_dmgReduction()){
						size += ProtoOutputStream.computeInt32Size(12, _dmgReduction);
					}
					if (has_maxHP()){
						size += ProtoOutputStream.computeInt32Size(13, _maxHP);
					}
					if (has_spellDmgMulti()){
						size += ProtoOutputStream.computeInt32Size(14, _spellDmgMulti);
					}
					if (has_spellHit()){
						size += ProtoOutputStream.computeInt32Size(15, _spellHit);
					}
					if (has_moveDelayReduce()){
						size += ProtoOutputStream.computeDoubleSize(16, _moveDelayReduce);
					}
					if (has_attackDelayReduce()){
						size += ProtoOutputStream.computeDoubleSize(17, _attackDelayReduce);
					}
					if (has_fireElementalDmg()){
						size += ProtoOutputStream.computeInt32Size(18, _fireElementalDmg);
					}
					if (has_waterElementalDmg()){
						size += ProtoOutputStream.computeInt32Size(19, _waterElementalDmg);
					}
					if (has_airElementalDmg()){
						size += ProtoOutputStream.computeInt32Size(20, _airElementalDmg);
					}
					if (has_earthElementalDmg()){
						size += ProtoOutputStream.computeInt32Size(21, _earthElementalDmg);
					}
					if (has_lightElementalDmg()){
						size += ProtoOutputStream.computeInt32Size(22, _lightElementalDmg);
					}
					if (has_comboDmgMulti()){
						size += ProtoOutputStream.computeInt32Size(23, _comboDmgMulti);
					}
					if (has_spellDmgAdd()){
						size += ProtoOutputStream.computeInt32Size(24, _spellDmgAdd);
					}
					_memorizedSerializedSize = size;
					return size;
				}
				@Override
				public boolean isInitialized(){
					if(_memorizedIsInitialized == 1)
						return true;
					if (!has_value()){
						_memorizedIsInitialized = -1;
						return false;
					}
					_memorizedIsInitialized = 1;
					return true;
				}
				@Override
				public void writeTo(ProtoOutputStream output) throws java.io.IOException{
					if (has_value()){
						output.wirteInt32(1, _value);
					}
					if (has_meleeDmg()){
						output.wirteInt32(2, _meleeDmg);
					}
					if (has_meleeHit()){
						output.wirteInt32(3, _meleeHit);
					}
					if (has_meleeCriHit()){
						output.writeDouble(4, _meleeCriHit);
					}
					if (has_ignoreReduction()){
						output.wirteInt32(5, _ignoreReduction);
					}
					if (has_bloodSuckHit()){
						output.writeDouble(6, _bloodSuckHit);
					}
					if (has_bloodSuckHeal()){
						output.wirteInt32(7, _bloodSuckHeal);
					}
					if (has_regenHP()){
						output.wirteInt32(8, _regenHP);
					}
					if (has_AC()){
						output.wirteInt32(9, _AC);
					}
					if (has_MR()){
						output.wirteInt32(10, _MR);
					}
					if (has_potionHP()){
						output.wirteInt32(11, _potionHP);
					}
					if (has_dmgReduction()){
						output.wirteInt32(12, _dmgReduction);
					}
					if (has_maxHP()){
						output.wirteInt32(13, _maxHP);
					}
					if (has_spellDmgMulti()){
						output.wirteInt32(14, _spellDmgMulti);
					}
					if (has_spellHit()){
						output.wirteInt32(15, _spellHit);
					}
					if (has_moveDelayReduce()){
						output.writeDouble(16, _moveDelayReduce);
					}
					if (has_attackDelayReduce()){
						output.writeDouble(17, _attackDelayReduce);
					}
					if (has_fireElementalDmg()){
						output.wirteInt32(18, _fireElementalDmg);
					}
					if (has_waterElementalDmg()){
						output.wirteInt32(19, _waterElementalDmg);
					}
					if (has_airElementalDmg()){
						output.wirteInt32(20, _airElementalDmg);
					}
					if (has_earthElementalDmg()){
						output.wirteInt32(21, _earthElementalDmg);
					}
					if (has_lightElementalDmg()){
						output.wirteInt32(22, _lightElementalDmg);
					}
					if (has_comboDmgMulti()){
						output.wirteInt32(23, _comboDmgMulti);
					}
					if (has_spellDmgAdd()){
						output.wirteInt32(24, _spellDmgAdd);
					}
				}
				@Override
				public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
					while(!input.isAtEnd()){
						int tag = input.readTag();
						switch(tag){
							case 0x00000008:{
								set_value(input.readInt32());
								break;
							}
							case 0x00000010:{
								set_meleeDmg(input.readInt32());
								break;
							}
							case 0x00000018:{
								set_meleeHit(input.readInt32());
								break;
							}
							case 0x00000021:{
								set_meleeCriHit(input.readDouble());
								break;
							}
							case 0x00000028:{
								set_ignoreReduction(input.readInt32());
								break;
							}
							case 0x00000031:{
								set_bloodSuckHit(input.readDouble());
								break;
							}
							case 0x00000038:{
								set_bloodSuckHeal(input.readInt32());
								break;
							}
							case 0x00000040:{
								set_regenHP(input.readInt32());
								break;
							}
							case 0x00000048:{
								set_AC(input.readInt32());
								break;
							}
							case 0x00000050:{
								set_MR(input.readInt32());
								break;
							}
							case 0x00000058:{
								set_potionHP(input.readInt32());
								break;
							}
							case 0x00000060:{
								set_dmgReduction(input.readInt32());
								break;
							}
							case 0x00000068:{
								set_maxHP(input.readInt32());
								break;
							}
							case 0x00000070:{
								set_spellDmgMulti(input.readInt32());
								break;
							}
							case 0x00000078:{
								set_spellHit(input.readInt32());
								break;
							}
							case 0x00000081:{
								set_moveDelayReduce(input.readDouble());
								break;
							}
							case 0x00000089:{
								set_attackDelayReduce(input.readDouble());
								break;
							}
							case 0x00000090:{
								set_fireElementalDmg(input.readInt32());
								break;
							}
							case 0x00000098:{
								set_waterElementalDmg(input.readInt32());
								break;
							}
							case 0x000000A0:{
								set_airElementalDmg(input.readInt32());
								break;
							}
							case 0x000000A8:{
								set_earthElementalDmg(input.readInt32());
								break;
							}
							case 0x000000B0:{
								set_lightElementalDmg(input.readInt32());
								break;
							}
							case 0x000000B8:{
								set_comboDmgMulti(input.readInt32());
								break;
							}
							case 0x000000C0:{
								set_spellDmgAdd(input.readInt32());
								break;
							}
							default:{
								System.out.println(String.format("[CompanionT.EnchantBonusT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class ClassInfoT implements ProtoMessage{
		public static ClassInfoT newInstance(){
			return new ClassInfoT();
		}
		private java.util.LinkedList<CompanionT.ClassInfoT.ClassT> _Class;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ClassInfoT(){
		}
		public java.util.LinkedList<CompanionT.ClassInfoT.ClassT> get_Class(){
			return _Class;
		}
		public void add_Class(CompanionT.ClassInfoT.ClassT val){
			if(!has_Class()){
				_Class = new java.util.LinkedList<CompanionT.ClassInfoT.ClassT>();
				_bit |= 0x1;
			}
			_Class.add(val);
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
				for(CompanionT.ClassInfoT.ClassT val : _Class){
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
				for(CompanionT.ClassInfoT.ClassT val : _Class){
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
				for (CompanionT.ClassInfoT.ClassT val : _Class){
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
						add_Class((CompanionT.ClassInfoT.ClassT)input.readMessage(CompanionT.ClassInfoT.ClassT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[CompanionT.ClassInfoT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class ClassT implements ProtoMessage{
			public static ClassT newInstance(){
				return new ClassT();
			}
			private String _class;
			private int _classId;
			private CompanionT.eCategory _category;
			private CompanionT.eElement _element;
			private java.util.LinkedList<CompanionT.ClassInfoT.ClassT.SkillT> _Skill;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private ClassT(){
			}
			public ClassT(ResultSet rs) throws SQLException {
				_class			= rs.getString("class");
				_classId		= rs.getInt("classId");
				String category	= rs.getString("category");
				category		= category.substring(category.indexOf("(") + 1, category.indexOf(")"));
				_category		= CompanionT.eCategory.fromInt(Integer.parseInt(category));
				String element	= rs.getString("element");
				element			= element.substring(element.indexOf("(") + 1, element.indexOf(")"));
				_element		= CompanionT.eElement.fromInt(Integer.parseInt(element));
				String skills	= rs.getString("skill");
				if (!StringUtil.isNullOrEmpty(skills)) {
					StringTokenizer st = new StringTokenizer(skills, StringUtil.LineString);
					while (st.hasMoreElements()) {
						String[] array = st.nextToken().split(StringUtil.CommaString);
						if (array == null || array.length != 2) {
							continue;
						}
						
						CompanionT.ClassInfoT.ClassT.SkillT skill = CompanionT.ClassInfoT.ClassT.SkillT.newInstance();
						array[0] = array[0].replace("TIER: ", StringUtil.EmptyString);
						skill.set_tier(Integer.parseInt(array[0].trim()));
						
						array[1] = array[1].replace("SKILLID: ", StringUtil.EmptyString);
						StringTokenizer ids = new StringTokenizer(array[1].trim(), " OR ");
						while (ids.hasMoreElements()) {
							skill.add_skillId(Integer.parseInt(ids.nextToken().trim()));
						}
						add_Skill(skill);
					}
				}
			}
			public String get_class(){
				return _class;
			}
			public void set_class(String val){
				_bit |= 0x1;
				_class = val;
			}
			public boolean has_class(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_classId(){
				return _classId;
			}
			public void set_classId(int val){
				_bit |= 0x2;
				_classId = val;
			}
			public boolean has_classId(){
				return (_bit & 0x2) == 0x2;
			}
			public CompanionT.eCategory get_category(){
				return _category;
			}
			public void set_category(CompanionT.eCategory val){
				_bit |= 0x4;
				_category = val;
			}
			public boolean has_category(){
				return (_bit & 0x4) == 0x4;
			}
			public CompanionT.eElement get_element(){
				return _element;
			}
			public void set_element(CompanionT.eElement val){
				_bit |= 0x10;
				_element = val;
			}
			public boolean has_element(){
				return (_bit & 0x10) == 0x10;
			}
			public java.util.LinkedList<CompanionT.ClassInfoT.ClassT.SkillT> get_Skill(){
				return _Skill;
			}
			public CompanionT.ClassInfoT.ClassT.SkillT get_Skill(int skill_id){
				for (CompanionT.ClassInfoT.ClassT.SkillT skillT : _Skill) {
					for (int id : skillT.get_skillId()) {
						if (id == skill_id) {
							return skillT;
						}
					}
				}
				return null;
			}
			public void add_Skill(CompanionT.ClassInfoT.ClassT.SkillT val){
				if(!has_Skill()){
					_Skill = new java.util.LinkedList<CompanionT.ClassInfoT.ClassT.SkillT>();
					_bit |= 0x20;
				}
				_Skill.add(val);
			}
			public String get_Skill_toString() {
				if (_Skill == null || _Skill.isEmpty()) {
					return null;
				}
				StringBuilder sb = new StringBuilder();
				for (CompanionT.ClassInfoT.ClassT.SkillT skill : _Skill) {
					if (sb.length() > 0) {
						sb.append(StringUtil.LineString);
					}
					
					sb.append("TIER: ").append(skill.get_tier());
					
					java.util.LinkedList<Integer> skillId = skill.get_skillId();
					if (skillId != null && !skillId.isEmpty()) {
						sb.append(", SKILLID: ");
						int idCnt = 0;
						for (int id : skillId) {
							if (idCnt++ > 0) {
								sb.append(" OR ");
							}
							sb.append(id);
						}
					}
				}
				return sb.toString();
			}
			public boolean has_Skill(){
				return (_bit & 0x20) == 0x20;
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
				if (has_class()){
					size += ProtoOutputStream.computeStringSize(1, _class);
				}
				if (has_classId()){
					size += ProtoOutputStream.computeUInt32Size(2, _classId);
				}
				if (has_category()){
					size += ProtoOutputStream.computeEnumSize(3, _category.toInt());
				}
				if (has_element()){
					size += ProtoOutputStream.computeEnumSize(5, _element.toInt());
				}
				if (has_Skill()){
					for(CompanionT.ClassInfoT.ClassT.SkillT val : _Skill){
						size += ProtoOutputStream.computeMessageSize(6, val);
					}
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_class()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_classId()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_category()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_element()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (has_Skill()){
					for(CompanionT.ClassInfoT.ClassT.SkillT val : _Skill){
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
				if (has_class()){
					output.writeString(1, _class);
				}
				if (has_classId()){
					output.writeUInt32(2, _classId);
				}
				if (has_category()){
					output.writeEnum(3, _category.toInt());
				}
				if (has_element()){
					output.writeEnum(5, _element.toInt());
				}
				if (has_Skill()){
					for (CompanionT.ClassInfoT.ClassT.SkillT val : _Skill){
						output.writeMessage(6, val);
					}
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x0000000A:{
							set_class(input.readString());
							break;
						}
						case 0x00000010:{
							set_classId(input.readUInt32());
							break;
						}
						case 0x00000018:{
							set_category(CompanionT.eCategory.fromInt(input.readEnum()));
							break;
						}
						case 0x00000028:{
							set_element(CompanionT.eElement.fromInt(input.readEnum()));
							break;
						}
						case 0x00000032:{
							add_Skill((CompanionT.ClassInfoT.ClassT.SkillT)input.readMessage(CompanionT.ClassInfoT.ClassT.SkillT.newInstance()));
							break;
						}
						default:{
							System.out.println(String.format("[CompanionT.ClassT] NEW_TAG : TAG(%d)", tag));
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
			
			public static class SkillT implements ProtoMessage{
				public static SkillT newInstance(){
					return new SkillT();
				}
				private int _tier;
				private java.util.LinkedList<Integer> _skillId;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private SkillT(){
				}
				public int get_tier(){
					return _tier;
				}
				public void set_tier(int val){
					_bit |= 0x1;
					_tier = val;
				}
				public boolean has_tier(){
					return (_bit & 0x1) == 0x1;
				}
				public java.util.LinkedList<Integer> get_skillId(){
					return _skillId;
				}
				public void add_skillId(int val){
					if(!has_skillId()){
						_skillId = new java.util.LinkedList<Integer>();
						_bit |= 0x2;
					}
					_skillId.add(val);
				}
				public boolean has_skillId(){
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
					if (has_tier()){
						size += ProtoOutputStream.computeUInt32Size(1, _tier);
					}
					if (has_skillId()){
						for(int val : _skillId){
							size += ProtoOutputStream.computeUInt32Size(2, val);
						}
					}
					_memorizedSerializedSize = size;
					return size;
				}
				@Override
				public boolean isInitialized(){
					if(_memorizedIsInitialized == 1)
						return true;
					if (!has_tier()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (has_skillId()){
						if (_skillId.isEmpty()) {
							_memorizedIsInitialized = -1;
							return false;
						}
					}
					_memorizedIsInitialized = 1;
					return true;
				}
				@Override
				public void writeTo(ProtoOutputStream output) throws java.io.IOException{
					if (has_tier()){
						output.writeUInt32(1, _tier);
					}
					if (has_skillId()){
						for (int val : _skillId){
							output.writeUInt32(2, val);
						}
					}
				}
				@Override
				public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
					while(!input.isAtEnd()){
						int tag = input.readTag();
						switch(tag){
							case 0x00000008:{
								set_tier(input.readUInt32());
								break;
							}
							case 0x00000010:{
								add_skillId(input.readUInt32());
								break;
							}
							default:{
								System.out.println(String.format("[CompanionT.SkillT] NEW_TAG : TAG(%d)", tag));
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
	
	public enum eStatType{
		STR(0),
		CON(1),
		INT(2),
		;
		private int value;
		eStatType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eStatType v){
			return value == v.value;
		}
		public static eStatType fromInt(int i){
			switch(i){
			case 0:
				return STR;
			case 1:
				return CON;
			case 2:
				return INT;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eStatType, %d", i));
			}
		}
	}
	
	public enum eCommand{
		TM_Aggressive(2),
		TM_Defensive(3),
		TM_GetItem(6),
		TM_Attack(7),
		TM_PullBack(9),
		Dismiss(100),
		Joke(101),
		Happy(102),
		;
		private int value;
		eCommand(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eCommand v){
			return value == v.value;
		}
		public static eCommand fromInt(int i){
			switch(i){
			case 2:
				return TM_Aggressive;
			case 3:
				return TM_Defensive;
			case 6:
				return TM_GetItem;
			case 7:
				return TM_Attack;
			case 9:
				return TM_PullBack;
			case 100:
				return Dismiss;
			case 101:
				return Joke;
			case 102:
				return Happy;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eCommand, %d", i));
			}
		}
	}
	
	public enum eCategory{
		FIERCE_ANIMAL(1),	// 맹수
		DEVINE_BEAST(2),	// 신수
		PET(3),				// 애완
		WILD(4),			// 야생
		DOG_FIGHT(5),		// 투견
		;
		private int value;
		eCategory(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eCategory v){
			return value == v.value;
		}
		public static eCategory fromInt(int i){
			switch(i){
			case 1:
				return FIERCE_ANIMAL;
			case 2:
				return DEVINE_BEAST;
			case 3:
				return PET;
			case 4:
				return WILD;
			case 5:
				return DOG_FIGHT;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eCategory, %d", i));
			}
		}
	}
	
	public enum eElement{
		NONE(0),
		FIRE(1),
		WATER(2),
		AIR(3),
		EARTH(4),
		LIGHT(5),
		;
		private int value;
		eElement(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eElement v){
			return value == v.value;
		}
		public static eElement fromInt(int i){
			switch(i){
			case 0:
				return NONE;
			case 1:
				return FIRE;
			case 2:
				return WATER;
			case 3:
				return AIR;
			case 4:
				return EARTH;
			case 5:
				return LIGHT;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eElement, %d", i));
			}
		}
	}
}

