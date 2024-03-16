package l1j.server.server.model;

import java.util.Random;

import l1j.server.server.construct.L1Attr;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.spell.S_SkillIconWindShackle;

public class L1WeaponSkill {
	private static final Random random = new Random(System.nanoTime());
	
	public static enum WeaponSkillAttackType {
		ALL, PVP, PVE;
		public static WeaponSkillAttackType fromString(String str){
			switch(str){
			case "PVP":	return PVP;
			case "PVE":	return PVE;
			default:	return ALL;
			}
		}
	}
	
	private int _weaponId;
	private int _probability;
	private int _fixDamage;
	private int _randomDamage;
	private int _area;
	private int _skillId = -1;
	private int _skillTime;
	private int _effectId;
	private int _effectTarget;
	private boolean _isArrowType;
	private L1Attr _attr;
	private int _enchant_probability;
	private int _enchant_damage;
	private int _int_damage;
	private int _spell_damage;
	private int _enchant_limit;
	private WeaponSkillAttackType _attackType;
	private boolean _hpStill;
	private int _hpStillProbability;
	private int _hpStillValue;
	private boolean _mpStill;
	private int _mpStillProbability;
	private int _mpStillValue;
	private int _stillEffectId;

	public L1WeaponSkill(int weaponId, int probability, int fixDamage, int randomDamage, int area, int skillId, int skillTime, int effectId, int effectTarget, boolean isArrowType, 
			L1Attr attr,
			int enchant_probability, int enchant_damage, int int_damage, int spell_damage, int enchant_limit, WeaponSkillAttackType attackType, 
			boolean hpStill, int hpStillProbability, int hpStillValue, boolean mpStill, int mpStillProbability, int mpStillValue, int stillEffectId) {
		_weaponId				= weaponId;
		_probability			= probability;
		_fixDamage				= fixDamage;
		_randomDamage			= randomDamage;
		_area					= area;
		_skillId				= skillId;
		_skillTime				= skillTime;
		_effectId				= effectId;
		_effectTarget			= effectTarget;
		_isArrowType			= isArrowType;
		_attr					= attr;
		_enchant_probability	= enchant_probability;
		_enchant_damage			= enchant_damage;
		_int_damage				= int_damage;
		_spell_damage			= spell_damage;
		_enchant_limit			= enchant_limit;
		_attackType				= attackType;
		_hpStill				= hpStill;
		_hpStillProbability		= hpStillProbability;
		_hpStillValue			= hpStillValue;
		_mpStill				= mpStill;
		_mpStillProbability		= mpStillProbability;
		_mpStillValue			= mpStillValue;
		_stillEffectId			= stillEffectId;
	}

	public int getWeaponId() {
		return _weaponId;
	}

	public int getProbability() {
		return _probability;
	}

	public int getFixDamage() {
		return _fixDamage;
	}

	public int getRandomDamage() {
		return _randomDamage;
	}

	public int getArea() {
		return _area;
	}

	public int getSkillId() {
		return _skillId;
	}

	public int getSkillTime() {
		return _skillTime;
	}

	public int getEffectId() {
		return _effectId;
	}

	public int getEffectTarget() {
		return _effectTarget;
	}

	public boolean isArrowType() {
		return _isArrowType;
	}

	public L1Attr getAttr() {
		return _attr;
	}
	
	public int getEnchantProbability(){
		return _enchant_probability;
	}
	
	public int getEnchantDamage(){
		return _enchant_damage;
	}
	
	public int getIntDamage(){
		return _int_damage;
	}
	
	public int getSpellDamage(){
		return _spell_damage;
	}
	
	public int getEnchantLimit(){
		return _enchant_limit;
	}
	
	public WeaponSkillAttackType getAttackType(){
		return _attackType;
	}
	
	public boolean isHpStill(){
		return _hpStill;
	}
	
	public int getHpStillProbability(){
		return _hpStillProbability;
	}
	
	public int getHpStillValue(){
		return _hpStillValue;
	}
	
	public boolean isMpStill(){
		return _mpStill;
	}
	
	public int getMpStillProbability(){
		return _mpStillProbability;
	}
	
	public int getMpStillValue(){
		return _mpStillValue;
	}
	
	public int getStillEffectId() {
		return _stillEffectId;
	}
	
	private static int getRandomChance(){
		return random.nextInt(1000);
	}
	
	private static int getActionChance(L1PcInstance pc, int chance){
		return (chance * 10) + (pc.getAbility().getItemSpellProbWeapon() / 10);
	}
	
	/**
	 * 악운의 단검 증발
	 * @param pc
	 * @param cha
	 * @param weapon
	 * @return damage
	 */
	public static double dagger_of_ill_luck(L1PcInstance pc, L1Character cha, L1ItemInstance weapon) {
		double dmg = 0;
		if (getActionChance(pc, 3) >= getRandomChance()) {
			dmg = cha.getCurrentHp() >> 1;
			if (cha.getCurrentHp() - dmg < 0) {
				dmg = 0;
			}
			String msg = weapon.getLogNameRef();
			pc.sendPackets(new S_ServerMessage(158, msg), true);
			pc.getInventory().removeItem(weapon, 1);
		}
		if (cha instanceof L1PcInstance) {
			L1PcInstance targetPc = (L1PcInstance) cha;
			L1PinkName.onAction(targetPc, pc);
		}
		return dmg;
	}
	
	/**
	 * 펌프킨커스
	 * @param pc
	 * @param cha
	 * @return damage
	 */
	public static double pumpkin_curs(L1PcInstance pc, L1Character cha) {
		double dmg = 0;
		if (pc.getWeapon() == null) {
			return 0;
		}
		int chance = 1 + (pc.getWeapon().getEnchantLevel() >> 1); //확률
		if (cha.isFreeze()) {
			return 0;
		}
		if (cha.getSkill().hasSkillEffect(L1SkillId.COUNTER_MAGIC)) {
			cha.getSkill().removeSkillEffect(L1SkillId.COUNTER_MAGIC);
			pc.send_effect(cha.getId(), 10702);
			return 0;
		}
		if (getRandomChance() <= getActionChance(pc, chance)) {// 조정
			int sp = pc.getAbility().getSp();
			int intel = pc.getAbility().getTotalInt();
			dmg = (intel + sp) + random.nextInt(intel + sp) * 0.3;
			int skilltime = 4; //패킷인식이 4의 배수만 인식하므로 4의배수로만 설정
			if (cha.getSkill().hasSkillEffect(L1SkillId.WIND_SHACKLE)) {
				cha.getSkill().removeSkillEffect(L1SkillId.WIND_SHACKLE);
			}
			cha.getSkill().setSkillEffect(L1SkillId.WIND_SHACKLE, skilltime * 1000);
			if (cha.getSkill().hasSkillEffect(L1SkillId.ERASE_MAGIC)) {
				cha.getSkill().removeSkillEffect(L1SkillId.ERASE_MAGIC);
			}
			if (cha instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) cha;
				targetPc.broadcastPacketWithMe(new S_SkillIconWindShackle(targetPc.getId(), skilltime), true);
				targetPc.send_effect(7849);
			} else if (cha instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				Broadcaster.broadcastPacket(npc, new S_Effect(npc.getId(), 7849), true);
			}
		}
		return calcDamageReduction(cha, dmg, L1Attr.RAY);
	}
	
	/**
	 * 마인드 브레이크
	 * @param pc
	 * @param cha
	 * @param effect
	 * @param enchant
	 * @return damage
	 */
	public static double mind_break(L1PcInstance pc, L1Character cha, int effect, int enchant) {
		double dmg = 0;
		if (getActionChance(pc, 2 + enchant) >= getRandomChance()) {
			dmg = 25;
			if (cha.getCurrentMp() >= 5) {
				cha.setCurrentMp(cha.getCurrentMp() - 5);
				if (dmg <= 0) {
					dmg = 0;
				}
			}
			S_EffectLocation packet = new S_EffectLocation(cha.getX(), cha.getY(), effect);
			pc.broadcastPacketWithMe(packet, true);
		}
		return calcDamageReduction(cha, dmg, L1Attr.WATER);
	}
	
	private static double calcDamageReduction(L1Character cha, double damage, L1Attr attr) {
		if (cha.isFreeze()) {
			return 0;
		}
		if (damage < 0) {
			damage = 0;
		}
		int resist = 0;
		switch (attr) {
		case EARTH:
			resist = cha.getResistance().getEarth();
			break;
		case FIRE:
			resist = cha.getResistance().getFire();
			break;
		case WATER:
			resist = cha.getResistance().getWater();
			break;
		case WIND:
			resist = cha.getResistance().getWind();
			break;
		default:
			break;
		}
		if (resist <= 0) {
			return damage;
		}
		int resistFloor = (int) (0.32D * Math.abs(resist));
		resistFloor *= resist >= 0 ? 1 : -1;
		double attrDeffence = resistFloor / 32.0;
		return (1.0D - attrDeffence) * damage;
	}

}

