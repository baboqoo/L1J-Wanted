package l1j.server.server.templates;

import l1j.server.server.model.item.ablity.enchant.L1EnchantFactory;

public class L1Armor extends L1Item {
	private static final long serialVersionUID = 1L;

	public L1Armor() {}

	private int _ac;
	private int _ac_sub;
	private int _carryBonus;
	private int _hitRate; // ● 근접무기 명중률
	private int _dmgRate; // ● 근접무기 추타율
	private int _bowHitRate; // ● 활의 명중율
	private int _bowDmgRate; // ● 활의 추타율
	private int _attrWater;
	private int _attrWind;
	private int _attrFire;
	private int _attrEarth;
	private int _attrAll;
	private int _registStone;
	private int _registSleep;
	private int _registFreeze;
	private int _registBlind;
	private int _polyDescId;
	
	private int _retrieveEnchantLevel;
	private L1EnchantFactory _enchantInfo;
	
	@Override
	public int getAc() {
		return _ac;
	}
	public void setAc(int i) {
		this._ac = i;
	}
	
	@Override
	public int getAcSub() {
		return _ac_sub;
	}
	public void setAcSub(int i) {
		this._ac_sub = i;
	}

	@Override
	public int getCarryBonus() {
		return _carryBonus;
	}
	public void setCarryBonus(int i) {
		_carryBonus = i;
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
	public int getRetrieveEnchantLevel() {
		return _retrieveEnchantLevel;
	}
	public void setRetrieveEnchantLevel(int level) {
		_retrieveEnchantLevel = level;
	}
	
	@Override
	public int getPolyDescId() {
		return _polyDescId;
	}
	public void setPolyDescId(int val) {
		_polyDescId = val;
	}
	
	@Override
	public L1EnchantFactory getEnchantInfo() {
		return _enchantInfo;
	}
	public void setEnchantInfo(L1EnchantFactory ablity){
		_enchantInfo = ablity;
	}

}

