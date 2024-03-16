package l1j.server.server.templates;

import l1j.server.server.construct.item.L1ItemWeaponType;
import l1j.server.server.model.item.ablity.enchant.L1EnchantFactory;

public class L1Weapon extends L1Item {
	private static final long serialVersionUID = 1L;

	public L1Weapon() {
	}
	
	private int _hitRate; // ● 근접무기 명중률
	private int _dmgRate; // ● 근접무기 추타율
	private int _bowHitRate; // ● 활의 명중율
	private int _bowDmgRate; // ● 활의 추타율
	private int _doubleDmgChance;
	private int _magicDmgModifier;
	private int _canbedmg;
	private L1EnchantFactory _enchantInfo;
	private L1ItemWeaponType _weaponType;
	private boolean _twohandedWeapon;
	private int _weaponAddDamage;

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
	public int getDoubleDmgChance() {
		return _doubleDmgChance;
	}

	public void setDoubleDmgChance(int i) {
		_doubleDmgChance = i;
	}

	@Override
	public int getMagicDmgModifier() {
		return _magicDmgModifier;
	}

	public void setMagicDmgModifier(int i) {
		_magicDmgModifier = i;
	}

	@Override
	public int getCanbeDmg() {
		return _canbedmg;
	}

	public void setCanbeDmg(int i) {
		_canbedmg = i;
	}
	
	@Override
	public L1EnchantFactory getEnchantInfo() {
		return _enchantInfo;
	}
	
	public void setEnchantInfo(L1EnchantFactory ablity){
		_enchantInfo = ablity;
	}
	
	@Override
	public L1ItemWeaponType getWeaponType() {
		return _weaponType;
	}
	
	public void setWeaponType(L1ItemWeaponType val) {
		_weaponType = val;
	}

	@Override
	public boolean isTwohandedWeapon() {
		return _twohandedWeapon;
	}
	
	public void setTwohandedWeapon(boolean flag){
		_twohandedWeapon = flag;
	}

	@Override
	public int getWeaponAddDamage(){
		return _weaponAddDamage;
	}
	
	public void setWeaponAddDamage(int value){
		_weaponAddDamage = value;
	}
}

