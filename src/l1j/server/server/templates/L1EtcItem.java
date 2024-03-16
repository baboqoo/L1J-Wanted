package l1j.server.server.templates;

import l1j.server.server.construct.L1Alignment;
import l1j.server.server.construct.item.L1ItemLimitType;
import l1j.server.server.construct.item.L1ItemSpellBookAttr;
import l1j.server.server.model.item.function.L1HealingPotion;
import l1j.server.server.model.skill.L1SkillType;

public class L1EtcItem extends L1Item {
	private static final long serialVersionUID = 1L;

	public L1EtcItem() {
	}

	private boolean _merge;
	private int _locx;
	private int _locy;
	private short _mapid;
	private int _delay_id;
	private int _delay_time;
	private int _delay_effect;
	private int _maxChargeCount;
	private int _acBonus;
	private int _hitRate; // ● 근접무기 명중률
	private int _dmgRate; // ● 근접무기 추타율
	private int _bowHitRate; // ● 활의 명중율
	private int _bowDmgRate; // ● 활의 추타율
	private int _carryBonus;
	private int _attrWater;
	private int _attrWind;
	private int _attrFire;
	private int _attrEarth;
	private int _attrAll;
	private int _registStone;
	private int _registSleep;
	private int _registFreeze;
	private int _registBlind;
	
	private int _skillLevel;
	private L1ItemSpellBookAttr _skillAttr;
	private L1SkillType _skillType;
	private L1Alignment _alignment;
	private int _buffDurationSecond;
	private int _etcValue;
	private L1ItemLimitType _limitType;
	private int _prob;
	private L1HealingPotion _potion;
	
	@Override
	public boolean isMerge() {
		return _merge;
	}
	
	public void setMerge(boolean val) {
		_merge = val;
	}

	@Override
	public int getLocX() {
		return _locx;
	}
	
	public void set_locx(int locx) {
		_locx = locx;
	}

	@Override
	public int getLocY() {
		return _locy;
	}
	
	public void set_locy(int locy) {
		_locy = locy;
	}

	@Override
	public short getMapId() {
		return _mapid;
	}
	
	public void set_mapid(short mapid) {
		_mapid = mapid;
	}

	@Override
	public int getDelayId() {
		return _delay_id;
	}
	
	public void setDelayId(int delay_id) {
		_delay_id = delay_id;
	}

	@Override
	public int getDelayTime() {
		return _delay_time;
	}
	
	public void setDelayTime(int delay_time) {
		_delay_time = delay_time;
	}

	@Override
	public int getDelayEffect() {
		return _delay_effect;
	}
	
	public void setDelayEffect(int delay_effect) {
		_delay_effect = delay_effect;
	}

	@Override
	public int getMaxChargeCount() {
		return _maxChargeCount;
	}
	
	public void setMaxChargeCount(int i) {
		_maxChargeCount = i;
	}
	
	@Override
	public int getAcBonus() {
		return _acBonus;
	}
	
	public void setAcBonus(int i) {
		this._acBonus = i;
	}

	@Override
	public int getHitRate() {
		return _hitRate;
	}
	
	public void setHitRate(int i) {
		_hitRate = i;
	}

	@Override
	public int getDmgRate() {
		return _dmgRate;
	}
	
	public void setDmgRate(int i) {
		_dmgRate = i;
	}

	@Override
	public int getBowHitRate() {
		return _bowHitRate;
	}
	
	public void setBowHitRate(int i) {
		_bowHitRate = i;
	}

	@Override
	public int getBowDmgRate() {
		return _bowDmgRate;
	}
	
	public void setBowDmgRate(int i) {
		_bowDmgRate = i;
	}

	@Override
	public int getCarryBonus() {
		return _carryBonus;
	}
	
	public void setCarryBonus(int i) {
		_carryBonus = i;
	}
	
	@Override
	public int getAttrWater() {
		return this._attrWater;
	}
	
	public void setAttrWater(int i) {
		_attrWater = i;
	}

	@Override
	public int getAttrWind() {
		return this._attrWind;
	}
	
	public void setAttrWind(int i) {
		_attrWind = i;
	}

	@Override
	public int getAttrFire() {
		return this._attrFire;
	}
	
	public void setAttrFire(int i) {
		_attrFire = i;
	}

	@Override
	public int getAttrEarth() {
		return this._attrEarth;
	}
	
	public void setAttrEarth(int i) {
		_attrEarth = i;
	}
	
	@Override
	public int getAttrAll() {
		return this._attrAll;
	}
	
	public void setAttrAll(int i) {
		_attrAll = i;
	}

	@Override
	public int getRegistStone() {
		return this._registStone;
	}
	
	public void setRegistStone(int i) {
		_registStone = i;
	}
	
	@Override
	public int getRegistSleep() {
		return this._registSleep;
	}
	
	public void setRegistSleep(int i) {
		_registSleep = i;
	}
	
	@Override
	public int getRegistFreeze() {
		return this._registFreeze;
	}
	
	public void setRegistFreeze(int i) {
		_registFreeze = i;
	}

	@Override
	public int getRegistBlind() {
		return this._registBlind;
	}
	
	public void setRegistBlind(int i) {
		_registBlind = i;
	}
	
	@Override
	public int getSkillLevel() {
		return _skillLevel;
	}
	public void setSkillLevel(int val) {
		_skillLevel = val;
	}
	
	@Override
	public L1ItemSpellBookAttr getSkillAttr() {
		return _skillAttr;
	}
	public void setSkillAttr(L1ItemSpellBookAttr val) {
		_skillAttr = val;
	}
	
	@Override
	public L1SkillType getSkillType(){
		return _skillType;
	}
	public void setSkillType(L1SkillType val){
		_skillType = val;
	}
	
	@Override
	public L1Alignment getAlignment() {
		return _alignment;
	}
	public void setAlignment(L1Alignment val) {
		_alignment = val;
	}
	
	@Override
	public int getBuffDurationSecond() {
		return _buffDurationSecond;
	}
	public void setBuffDurationSecond(int val) {
		_buffDurationSecond = val;
	}
	
	@Override
	public int getEtcValue() {
		return _etcValue;
	}
	
	public void setEtcValue(int val){
		_etcValue = val;
	}
	
	@Override
	public L1ItemLimitType getLimitType() {
		return _limitType;
	}
	
	public void setLimitType(L1ItemLimitType val){
		_limitType = val;
	}
	
	@Override
	public int getProb() {
		return _prob;
	}
	
	public void setProb(int val) {
		_prob = val;
	}
	
	@Override
	public L1HealingPotion getHealingPotion() {
		return _potion;
	}
	
	public void setHealingPotion(L1HealingPotion val){
		_potion = val;
	}

}

