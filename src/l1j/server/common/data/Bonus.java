package l1j.server.common.data;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class Bonus implements ProtoMessage{
	public static Bonus newInstance(){
		return new Bonus();
	}
	private String _dmgBonus;
	private String _missileDmgBonus;
	private String _spBonus;
	private String _hitBonus;
	private String _projBonus;
	private String _magicHitBonus;
	private String _meleeCriticalProb;
	private String _rangeCriticalProb;
	private String _magicCriticalBonus;
	private String _pierceAll;
	private String _sdamBonus;
	private String _ldamBonus;
	private String _decreaseTargetDG;
	private String _decreaseTargetER;
	private String _doubleAttackProb;
	private String _exposureWeakPointProb;
	private String _attackSpeedBonus;
	private String _hpBonus;
	private String _mpBonus;
	private String _RBonusAbility;
	private String _RBonusSpirit;
	private String _RBonusDragonSpell;
	private String _RBonusFear;
	private String _RBonusAllResistance;
	private String _expBonus;
	private String _restExpBonus;
	private String _pvpDmgBonus;
	private String _pvpDmgReductionBonus;
	private String _magicDmgBonus;
	private String _spellBuff;
	private String _restExpReduceEfficiency;
	private String _acquisitionProbItemBonus;
	private String _acquisitionProbAdenaBonus;
	private String _phBoost;
	private String _healBoost;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private long _bit;
	private Bonus(){
	}
	public String get_dmgBonus(){
		return _dmgBonus;
	}
	public void set_dmgBonus(String val){
		_bit |= 0x1L;
		_dmgBonus = val;
	}
	public boolean has_dmgBonus(){
		return (_bit & 0x1L) == 0x1L;
	}
	public String get_missileDmgBonus(){
		return _missileDmgBonus;
	}
	public void set_missileDmgBonus(String val){
		_bit |= 0x2L;
		_missileDmgBonus = val;
	}
	public boolean has_missileDmgBonus(){
		return (_bit & 0x2L) == 0x2L;
	}
	public String get_spBonus(){
		return _spBonus;
	}
	public void set_spBonus(String val){
		_bit |= 0x4L;
		_spBonus = val;
	}
	public boolean has_spBonus(){
		return (_bit & 0x4L) == 0x4L;
	}
	public String get_hitBonus(){
		return _hitBonus;
	}
	public void set_hitBonus(String val){
		_bit |= 0x8L;
		_hitBonus = val;
	}
	public boolean has_hitBonus(){
		return (_bit & 0x8L) == 0x8L;
	}
	public String get_projBonus(){
		return _projBonus;
	}
	public void set_projBonus(String val){
		_bit |= 0x10L;
		_projBonus = val;
	}
	public boolean has_projBonus(){
		return (_bit & 0x10L) == 0x10L;
	}
	public String get_magicHitBonus(){
		return _magicHitBonus;
	}
	public void set_magicHitBonus(String val){
		_bit |= 0x20L;
		_magicHitBonus = val;
	}
	public boolean has_magicHitBonus(){
		return (_bit & 0x20L) == 0x20L;
	}
	public String get_meleeCriticalProb(){
		return _meleeCriticalProb;
	}
	public void set_meleeCriticalProb(String val){
		_bit |= 0x40L;
		_meleeCriticalProb = val;
	}
	public boolean has_meleeCriticalProb(){
		return (_bit & 0x40L) == 0x40L;
	}
	public String get_rangeCriticalProb(){
		return _rangeCriticalProb;
	}
	public void set_rangeCriticalProb(String val){
		_bit |= 0x80L;
		_rangeCriticalProb = val;
	}
	public boolean has_rangeCriticalProb(){
		return (_bit & 0x80L) == 0x80L;
	}
	public String get_magicCriticalBonus(){
		return _magicCriticalBonus;
	}
	public void set_magicCriticalBonus(String val){
		_bit |= 0x100L;
		_magicCriticalBonus = val;
	}
	public boolean has_magicCriticalBonus(){
		return (_bit & 0x100L) == 0x100L;
	}
	public String get_pierceAll(){
		return _pierceAll;
	}
	public void set_pierceAll(String val){
		_bit |= 0x200L;
		_pierceAll = val;
	}
	public boolean has_pierceAll(){
		return (_bit & 0x200L) == 0x200L;
	}
	public String get_sdamBonus(){
		return _sdamBonus;
	}
	public void set_sdamBonus(String val){
		_bit |= 0x400L;
		_sdamBonus = val;
	}
	public boolean has_sdamBonus(){
		return (_bit & 0x400L) == 0x400L;
	}
	public String get_ldamBonus(){
		return _ldamBonus;
	}
	public void set_ldamBonus(String val){
		_bit |= 0x800L;
		_ldamBonus = val;
	}
	public boolean has_ldamBonus(){
		return (_bit & 0x800L) == 0x800L;
	}
	public String get_decreaseTargetDG(){
		return _decreaseTargetDG;
	}
	public void set_decreaseTargetDG(String val){
		_bit |= 0x1000L;
		_decreaseTargetDG = val;
	}
	public boolean has_decreaseTargetDG(){
		return (_bit & 0x1000L) == 0x1000L;
	}
	public String get_decreaseTargetER(){
		return _decreaseTargetER;
	}
	public void set_decreaseTargetER(String val){
		_bit |= 0x2000L;
		_decreaseTargetER = val;
	}
	public boolean has_decreaseTargetER(){
		return (_bit & 0x2000L) == 0x2000L;
	}
	public String get_doubleAttackProb(){
		return _doubleAttackProb;
	}
	public void set_doubleAttackProb(String val){
		_bit |= 0x4000L;
		_doubleAttackProb = val;
	}
	public boolean has_doubleAttackProb(){
		return (_bit & 0x4000L) == 0x4000L;
	}
	public String get_exposureWeakPointProb(){
		return _exposureWeakPointProb;
	}
	public void set_exposureWeakPointProb(String val){
		_bit |= 0x8000L;
		_exposureWeakPointProb = val;
	}
	public boolean has_exposureWeakPointProb(){
		return (_bit & 0x8000L) == 0x8000L;
	}
	public String get_attackSpeedBonus(){
		return _attackSpeedBonus;
	}
	public void set_attackSpeedBonus(String val){
		_bit |= 0x10000L;
		_attackSpeedBonus = val;
	}
	public boolean has_attackSpeedBonus(){
		return (_bit & 0x10000L) == 0x10000L;
	}
	public String get_hpBonus(){
		return _hpBonus;
	}
	public void set_hpBonus(String val){
		_bit |= 0x20000L;
		_hpBonus = val;
	}
	public boolean has_hpBonus(){
		return (_bit & 0x20000L) == 0x20000L;
	}
	public String get_mpBonus(){
		return _mpBonus;
	}
	public void set_mpBonus(String val){
		_bit |= 0x40000L;
		_mpBonus = val;
	}
	public boolean has_mpBonus(){
		return (_bit & 0x40000L) == 0x40000L;
	}
	public String get_RBonusAbility(){
		return _RBonusAbility;
	}
	public void set_RBonusAbility(String val){
		_bit |= 0x80000L;
		_RBonusAbility = val;
	}
	public boolean has_RBonusAbility(){
		return (_bit & 0x80000L) == 0x80000L;
	}
	public String get_RBonusSpirit(){
		return _RBonusSpirit;
	}
	public void set_RBonusSpirit(String val){
		_bit |= 0x100000L;
		_RBonusSpirit = val;
	}
	public boolean has_RBonusSpirit(){
		return (_bit & 0x100000L) == 0x100000L;
	}
	public String get_RBonusDragonSpell(){
		return _RBonusDragonSpell;
	}
	public void set_RBonusDragonSpell(String val){
		_bit |= 0x200000L;
		_RBonusDragonSpell = val;
	}
	public boolean has_RBonusDragonSpell(){
		return (_bit & 0x200000L) == 0x200000L;
	}
	public String get_RBonusFear(){
		return _RBonusFear;
	}
	public void set_RBonusFear(String val){
		_bit |= 0x400000L;
		_RBonusFear = val;
	}
	public boolean has_RBonusFear(){
		return (_bit & 0x400000L) == 0x400000L;
	}
	public String get_RBonusAllResistance(){
		return _RBonusAllResistance;
	}
	public void set_RBonusAllResistance(String val){
		_bit |= 0x800000L;
		_RBonusAllResistance = val;
	}
	public boolean has_RBonusAllResistance(){
		return (_bit & 0x800000L) == 0x800000L;
	}
	public String get_expBonus(){
		return _expBonus;
	}
	public void set_expBonus(String val){
		_bit |= 0x1000000L;
		_expBonus = val;
	}
	public boolean has_expBonus(){
		return (_bit & 0x1000000L) == 0x1000000L;
	}
	public String get_restExpBonus(){
		return _restExpBonus;
	}
	public void set_restExpBonus(String val){
		_bit |= 0x2000000L;
		_restExpBonus = val;
	}
	public boolean has_restExpBonus(){
		return (_bit & 0x2000000L) == 0x2000000L;
	}
	public String get_pvpDmgBonus(){
		return _pvpDmgBonus;
	}
	public void set_pvpDmgBonus(String val){
		_bit |= 0x4000000L;
		_pvpDmgBonus = val;
	}
	public boolean has_pvpDmgBonus(){
		return (_bit & 0x4000000L) == 0x4000000L;
	}
	public String get_pvpDmgReductionBonus(){
		return _pvpDmgReductionBonus;
	}
	public void set_pvpDmgReductionBonus(String val){
		_bit |= 0x8000000L;
		_pvpDmgReductionBonus = val;
	}
	public boolean has_pvpDmgReductionBonus(){
		return (_bit & 0x8000000L) == 0x8000000L;
	}
	public String get_magicDmgBonus(){
		return _magicDmgBonus;
	}
	public void set_magicDmgBonus(String val){
		_bit |= 0x10000000L;
		_magicDmgBonus = val;
	}
	public boolean has_magicDmgBonus(){
		return (_bit & 0x10000000L) == 0x10000000L;
	}
	public String get_spellBuff(){
		return _spellBuff;
	}
	public void set_spellBuff(String val){
		_bit |= 0x20000000L;
		_spellBuff = val;
	}
	public boolean has_spellBuff(){
		return (_bit & 0x20000000L) == 0x20000000L;
	}
	public String get_restExpReduceEfficiency(){
		return _restExpReduceEfficiency;
	}
	public void set_restExpReduceEfficiency(String val){
		_bit |= 0x40000000L;
		_restExpReduceEfficiency = val;
	}
	public boolean has_restExpReduceEfficiency(){
		return (_bit & 0x40000000L) == 0x40000000L;
	}
	public String get_acquisitionProbItemBonus(){
		return _acquisitionProbItemBonus;
	}
	public void set_acquisitionProbItemBonus(String val){
		_bit |= 0x80000000L;
		_acquisitionProbItemBonus = val;
	}
	public boolean has_acquisitionProbItemBonus(){
		return (_bit & 0x80000000L) == 0x80000000L;
	}
	public String get_acquisitionProbAdenaBonus(){
		return _acquisitionProbAdenaBonus;
	}
	public void set_acquisitionProbAdenaBonus(String val){
		_bit |= 0x100000000L;
		_acquisitionProbAdenaBonus = val;
	}
	public boolean has_acquisitionProbAdenaBonus(){
		return (_bit & 0x100000000L) == 0x100000000L;
	}
	public String get_phBoost(){
		return _phBoost;
	}
	public void set_phBoost(String val){
		_bit |= 0x200000000L;
		_phBoost = val;
	}
	public boolean has_phBoost(){
		return (_bit & 0x200000000L) == 0x200000000L;
	}
	public String get_healBoost(){
		return _healBoost;
	}
	public void set_healBoost(String val){
		_bit |= 0x400000000L;
		_healBoost = val;
	}
	public boolean has_healBoost(){
		return (_bit & 0x400000000L) == 0x400000000L;
	}
	@Override
	public long getInitializeBit(){
		return _bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_dmgBonus()){
			size += ProtoOutputStream.computeStringSize(1, _dmgBonus);
		}
		if (has_missileDmgBonus()){
			size += ProtoOutputStream.computeStringSize(2, _missileDmgBonus);
		}
		if (has_spBonus()){
			size += ProtoOutputStream.computeStringSize(3, _spBonus);
		}
		if (has_hitBonus()){
			size += ProtoOutputStream.computeStringSize(4, _hitBonus);
		}
		if (has_projBonus()){
			size += ProtoOutputStream.computeStringSize(5, _projBonus);
		}
		if (has_magicHitBonus()){
			size += ProtoOutputStream.computeStringSize(6, _magicHitBonus);
		}
		if (has_meleeCriticalProb()){
			size += ProtoOutputStream.computeStringSize(7, _meleeCriticalProb);
		}
		if (has_rangeCriticalProb()){
			size += ProtoOutputStream.computeStringSize(8, _rangeCriticalProb);
		}
		if (has_magicCriticalBonus()){
			size += ProtoOutputStream.computeStringSize(9, _magicCriticalBonus);
		}
		if (has_pierceAll()){
			size += ProtoOutputStream.computeStringSize(10, _pierceAll);
		}
		if (has_sdamBonus()){
			size += ProtoOutputStream.computeStringSize(11, _sdamBonus);
		}
		if (has_ldamBonus()){
			size += ProtoOutputStream.computeStringSize(12, _ldamBonus);
		}
		if (has_decreaseTargetDG()){
			size += ProtoOutputStream.computeStringSize(13, _decreaseTargetDG);
		}
		if (has_decreaseTargetER()){
			size += ProtoOutputStream.computeStringSize(14, _decreaseTargetER);
		}
		if (has_doubleAttackProb()){
			size += ProtoOutputStream.computeStringSize(15, _doubleAttackProb);
		}
		if (has_exposureWeakPointProb()){
			size += ProtoOutputStream.computeStringSize(16, _exposureWeakPointProb);
		}
		if (has_attackSpeedBonus()){
			size += ProtoOutputStream.computeStringSize(17, _attackSpeedBonus);
		}
		if (has_hpBonus()){
			size += ProtoOutputStream.computeStringSize(18, _hpBonus);
		}
		if (has_mpBonus()){
			size += ProtoOutputStream.computeStringSize(19, _mpBonus);
		}
		if (has_RBonusAbility()){
			size += ProtoOutputStream.computeStringSize(20, _RBonusAbility);
		}
		if (has_RBonusSpirit()){
			size += ProtoOutputStream.computeStringSize(21, _RBonusSpirit);
		}
		if (has_RBonusDragonSpell()){
			size += ProtoOutputStream.computeStringSize(22, _RBonusDragonSpell);
		}
		if (has_RBonusFear()){
			size += ProtoOutputStream.computeStringSize(23, _RBonusFear);
		}
		if (has_RBonusAllResistance()){
			size += ProtoOutputStream.computeStringSize(24, _RBonusAllResistance);
		}
		if (has_expBonus()){
			size += ProtoOutputStream.computeStringSize(25, _expBonus);
		}
		if (has_restExpBonus()){
			size += ProtoOutputStream.computeStringSize(26, _restExpBonus);
		}
		if (has_pvpDmgBonus()){
			size += ProtoOutputStream.computeStringSize(27, _pvpDmgBonus);
		}
		if (has_pvpDmgReductionBonus()){
			size += ProtoOutputStream.computeStringSize(28, _pvpDmgReductionBonus);
		}
		if (has_magicDmgBonus()){
			size += ProtoOutputStream.computeStringSize(29, _magicDmgBonus);
		}
		if (has_spellBuff()){
			size += ProtoOutputStream.computeStringSize(30, _spellBuff);
		}
		if (has_restExpReduceEfficiency()){
			size += ProtoOutputStream.computeStringSize(31, _restExpReduceEfficiency);
		}
		if (has_acquisitionProbItemBonus()){
			size += ProtoOutputStream.computeStringSize(32, _acquisitionProbItemBonus);
		}
		if (has_acquisitionProbAdenaBonus()){
			size += ProtoOutputStream.computeStringSize(33, _acquisitionProbAdenaBonus);
		}
		if (has_phBoost()){
			size += ProtoOutputStream.computeStringSize(34, _phBoost);
		}
		if (has_healBoost()){
			size += ProtoOutputStream.computeStringSize(35, _healBoost);
		}
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
		if (has_dmgBonus()){
			output.writeString(1, _dmgBonus);
		}
		if (has_missileDmgBonus()){
			output.writeString(2, _missileDmgBonus);
		}
		if (has_spBonus()){
			output.writeString(3, _spBonus);
		}
		if (has_hitBonus()){
			output.writeString(4, _hitBonus);
		}
		if (has_projBonus()){
			output.writeString(5, _projBonus);
		}
		if (has_magicHitBonus()){
			output.writeString(6, _magicHitBonus);
		}
		if (has_meleeCriticalProb()){
			output.writeString(7, _meleeCriticalProb);
		}
		if (has_rangeCriticalProb()){
			output.writeString(8, _rangeCriticalProb);
		}
		if (has_magicCriticalBonus()){
			output.writeString(9, _magicCriticalBonus);
		}
		if (has_pierceAll()){
			output.writeString(10, _pierceAll);
		}
		if (has_sdamBonus()){
			output.writeString(11, _sdamBonus);
		}
		if (has_ldamBonus()){
			output.writeString(12, _ldamBonus);
		}
		if (has_decreaseTargetDG()){
			output.writeString(13, _decreaseTargetDG);
		}
		if (has_decreaseTargetER()){
			output.writeString(14, _decreaseTargetER);
		}
		if (has_doubleAttackProb()){
			output.writeString(15, _doubleAttackProb);
		}
		if (has_exposureWeakPointProb()){
			output.writeString(16, _exposureWeakPointProb);
		}
		if (has_attackSpeedBonus()){
			output.writeString(17, _attackSpeedBonus);
		}
		if (has_hpBonus()){
			output.writeString(18, _hpBonus);
		}
		if (has_mpBonus()){
			output.writeString(19, _mpBonus);
		}
		if (has_RBonusAbility()){
			output.writeString(20, _RBonusAbility);
		}
		if (has_RBonusSpirit()){
			output.writeString(21, _RBonusSpirit);
		}
		if (has_RBonusDragonSpell()){
			output.writeString(22, _RBonusDragonSpell);
		}
		if (has_RBonusFear()){
			output.writeString(23, _RBonusFear);
		}
		if (has_RBonusAllResistance()){
			output.writeString(24, _RBonusAllResistance);
		}
		if (has_expBonus()){
			output.writeString(25, _expBonus);
		}
		if (has_restExpBonus()){
			output.writeString(26, _restExpBonus);
		}
		if (has_pvpDmgBonus()){
			output.writeString(27, _pvpDmgBonus);
		}
		if (has_pvpDmgReductionBonus()){
			output.writeString(28, _pvpDmgReductionBonus);
		}
		if (has_magicDmgBonus()){
			output.writeString(29, _magicDmgBonus);
		}
		if (has_spellBuff()){
			output.writeString(30, _spellBuff);
		}
		if (has_restExpReduceEfficiency()){
			output.writeString(31, _restExpReduceEfficiency);
		}
		if (has_acquisitionProbItemBonus()){
			output.writeString(32, _acquisitionProbItemBonus);
		}
		if (has_acquisitionProbAdenaBonus()){
			output.writeString(33, _acquisitionProbAdenaBonus);
		}
		if (has_phBoost()){
			output.writeString(34, _phBoost);
		}
		if (has_healBoost()){
			output.writeString(35, _healBoost);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x0000000A:{
					set_dmgBonus(input.readString());
					break;
				}
				case 0x00000012:{
					set_missileDmgBonus(input.readString());
					break;
				}
				case 0x0000001A:{
					set_spBonus(input.readString());
					break;
				}
				case 0x00000022:{
					set_hitBonus(input.readString());
					break;
				}
				case 0x0000002A:{
					set_projBonus(input.readString());
					break;
				}
				case 0x00000032:{
					set_magicHitBonus(input.readString());
					break;
				}
				case 0x0000003A:{
					set_meleeCriticalProb(input.readString());
					break;
				}
				case 0x00000042:{
					set_rangeCriticalProb(input.readString());
					break;
				}
				case 0x0000004A:{
					set_magicCriticalBonus(input.readString());
					break;
				}
				case 0x00000052:{
					set_pierceAll(input.readString());
					break;
				}
				case 0x0000005A:{
					set_sdamBonus(input.readString());
					break;
				}
				case 0x00000062:{
					set_ldamBonus(input.readString());
					break;
				}
				case 0x0000006A:{
					set_decreaseTargetDG(input.readString());
					break;
				}
				case 0x00000072:{
					set_decreaseTargetER(input.readString());
					break;
				}
				case 0x0000007A:{
					set_doubleAttackProb(input.readString());
					break;
				}
				case 0x00000082:{
					set_exposureWeakPointProb(input.readString());
					break;
				}
				case 0x0000008A:{
					set_attackSpeedBonus(input.readString());
					break;
				}
				case 0x00000092:{
					set_hpBonus(input.readString());
					break;
				}
				case 0x0000009A:{
					set_mpBonus(input.readString());
					break;
				}
				case 0x000000A2:{
					set_RBonusAbility(input.readString());
					break;
				}
				case 0x000000AA:{
					set_RBonusSpirit(input.readString());
					break;
				}
				case 0x000000B2:{
					set_RBonusDragonSpell(input.readString());
					break;
				}
				case 0x000000BA:{
					set_RBonusFear(input.readString());
					break;
				}
				case 0x000000C2:{
					set_RBonusAllResistance(input.readString());
					break;
				}
				case 0x000000CA:{
					set_expBonus(input.readString());
					break;
				}
				case 0x000000D2:{
					set_restExpBonus(input.readString());
					break;
				}
				case 0x000000DA:{
					set_pvpDmgBonus(input.readString());
					break;
				}
				case 0x000000E2:{
					set_pvpDmgReductionBonus(input.readString());
					break;
				}
				case 0x000000EA:{
					set_magicDmgBonus(input.readString());
					break;
				}
				case 0x000000F2:{
					set_spellBuff(input.readString());
					break;
				}
				case 0x000000FA:{
					set_restExpReduceEfficiency(input.readString());
					break;
				}
				case 0x00000102:{
					set_acquisitionProbItemBonus(input.readString());
					break;
				}
				case 0x0000010A:{
					set_acquisitionProbAdenaBonus(input.readString());
					break;
				}
				case 0x00000112:{
					set_phBoost(input.readString());
					break;
				}
				case 0x0000011A:{
					set_healBoost(input.readString());
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

