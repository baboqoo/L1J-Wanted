package l1j.server.server.model;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_SpecialResistance;
import l1j.server.server.serverpackets.S_SpecialResistance.ResistanceKind;
import l1j.server.server.utils.CalcChaStat;
import l1j.server.server.utils.IntRange;

public class L1Resistance {
	private static final int LIMIT_MIN		= -128;
	private static final int LIMIT_MAX		= 127;
	private static final int LIMIT_MIN_MR	= 0;
	private static final int LIMIT_MAX_MR	= 500;

	private int baseMr;// 기본 마법 방어
	private int addedMr;// 아이템이나 마법에 의해 추가된 마법 방어를 포함한 마법 방어
	private int fire, water, wind, earth;// 속성 방어
	private int petrifaction, sleep, freeze;// 특화 내성
	private int toleranceSkill, toleranceSpirit, toleranceDragon, toleranceFear;// 내성
	private int hitupSkill, hitupSpirit, hitupDragon, hitupFear, charismaHitup;// 적중
	
	private final L1Character owner;
	private final boolean isPc;
	private L1PcInstance pc;

	L1Resistance(L1Character cha) {
		init();
		owner	= cha;
		isPc	= owner instanceof L1PcInstance;
		if (isPc) {
			pc = (L1PcInstance) owner;
		}
	}
	
	public void init() {
		baseMr = addedMr = 0;
		fire = water = wind = earth = 0;
		petrifaction = sleep = freeze = 0;
		toleranceSkill = toleranceSpirit = toleranceDragon = toleranceFear = 0;
		hitupSkill = hitupSpirit = hitupDragon = hitupFear = charismaHitup = 0;
	}
	
	private int checkMrRange(int value, final int min) {
		return IntRange.ensure(value, min, LIMIT_MAX_MR);
	}
	
	private byte checkRange(int value) {
		return (byte)IntRange.ensure(value, LIMIT_MIN, LIMIT_MAX);
	}
	
	public int getEffectedMrBySkill() {
		int effectedMr = getMr();
		if (owner.getSkill().hasSkillEffect(L1SkillId.ERASE_MAGIC) || owner.getSkill().hasSkillEffect(L1SkillId.MOB_ERASE_MAGIC)) {
			effectedMr >>= 1;// 50%
		}
		return effectedMr;
	}

	public int getAddedMr() {
		return addedMr;
	}
	public int getMr() {
		return checkMrRange(baseMr + addedMr, LIMIT_MIN_MR);
	}
	public int getBaseMr() {
		return baseMr;
	}
	public void addMr(int i) {
		setAddedMr(addedMr + i);
	}	
	public void setBaseMr(int i) {
		baseMr = checkMrRange(i, LIMIT_MIN_MR);
	}
	private void setAddedMr(int i) {
		addedMr = checkMrRange(i, -baseMr);
		if (isPc) {
			pc.sendPackets(new S_SPMR(pc), true);
		}
	}
		
	public int getFreeze() {
		return freeze;
	}
	public void addFreeze(int i) {
		freeze = checkRange(freeze + i);
	}
	public int getPetrifaction() {
		return petrifaction;
	}
	public void addPetrifaction(int i) {
		petrifaction 	= checkRange(petrifaction + i);
	}
	public int getSleep() {
		return sleep;
	}
	public void addSleep(int i) {
		sleep = checkRange(sleep + i);
	}
	
	// TODO 속성 방어
	public int getFire() {
		return fire;
	}
	public void addFire(int i) {
		fire  = checkRange(fire + i);
	}
	public int getWater() {
		return water;
	}
	public void addWater(int i) {
		water = checkRange(water + i);
	}
	public int getWind() {
		return wind;
	}
	public void addWind(int i) {
		wind  = checkRange(wind + i);
	}
	public int getEarth() {
		return earth;
	}
	public void addEarth(int i) {
		earth = checkRange(earth + i);
	}
	
	public void addAllNaturalResistance(int i) {
		addFire(i);
		addWater(i);
		addWind(i);
		addEarth(i);
	}
	
	// TODO 적중
	public int getHitupSkill() {
	    return checkRange(hitupSkill + charismaHitup);
	}

	public void addHitupSkill(int i){
		hitupSkill = checkRange(hitupSkill + i);
		if (isPc) {
			pc.sendPackets(new S_SpecialResistance(S_SpecialResistance.PIERCE, ResistanceKind.ABILITY, getHitupSkill()), true);
		}
	}
	
	public int getHitupSpirit() {
	    return checkRange(hitupSpirit + charismaHitup);
	}

	public void addHitupSpirit(int i){ 
		hitupSpirit = checkRange(hitupSpirit + i);
		if (isPc) {
			pc.sendPackets(new S_SpecialResistance(S_SpecialResistance.PIERCE, ResistanceKind.SPIRIT, getHitupSpirit()), true);
		}
	}
	    
	public int getHitupDragon() {
	    return checkRange(hitupDragon + charismaHitup);
	}

	public void addHitupDragon(int i){ 
		hitupDragon = checkRange(hitupDragon + i);
		if (isPc) {
			pc.sendPackets(new S_SpecialResistance(S_SpecialResistance.PIERCE, ResistanceKind.DRAGON_SPELL, getHitupDragon()), true);
		}
	}
	    
	public int getHitupFear() {
	    return checkRange(hitupFear + charismaHitup);
	}

	public void addHitupFear(int i){ 
		hitupFear = checkRange(hitupFear + i);
		if (isPc) {
			pc.sendPackets(new S_SpecialResistance(S_SpecialResistance.PIERCE, ResistanceKind.FEAR, getHitupFear()), true);
		}
	}
	
	public void addHitupAll(int i) {
		if (i == 0) {
			return;
		}
		addHitupSkill(i);
		addHitupSpirit(i);
		addHitupDragon(i);
		addHitupFear(i);
	}
	
	// TODO 내성
	public int getToleranceSkill(){
		return checkRange(toleranceSkill);
	}
	
	public void addToleranceSkill(int i) { 
		toleranceSkill = checkRange(toleranceSkill + i);
		if (isPc) {
			pc.sendPackets(new S_SpecialResistance(S_SpecialResistance.RESISTANCE, ResistanceKind.ABILITY, getToleranceSkill()), true);
		}
	}
	
	public int getToleranceSpirit(){
		return checkRange(toleranceSpirit);
	}
	
	public void addToleranceSpirit(int i){ 
		toleranceSpirit = checkRange(toleranceSpirit + i);
		if (isPc) {
			pc.sendPackets(new S_SpecialResistance(S_SpecialResistance.RESISTANCE, ResistanceKind.SPIRIT, getToleranceSpirit()), true);
		}
	}
	
	public int getToleranceDragon(){
		return checkRange(toleranceDragon);
	}
	
	public void addToleranceDragon(int i){ 
		toleranceDragon = checkRange(toleranceDragon + i);
		if (isPc) {
			pc.sendPackets(new S_SpecialResistance(S_SpecialResistance.RESISTANCE, ResistanceKind.DRAGON_SPELL, getToleranceDragon()), true);
		}
	}
	
	public int getToleranceFear(){
		return checkRange(toleranceFear);
	}
	
	public void addToleranceFear(int i){ 
		toleranceFear = checkRange(toleranceFear + i);
		if (isPc) {
			pc.sendPackets(new S_SpecialResistance(S_SpecialResistance.RESISTANCE, ResistanceKind.FEAR, getToleranceFear()), true);
		}
	}
	
	public void addToleranceAll(int i) {
		if (i == 0) {
			return;
		}
		addToleranceSkill(i);
		addToleranceSpirit(i);
		addToleranceDragon(i);
		addToleranceFear(i);
	}
	
	public void updateCharismaHitup(){
		charismaHitup = CalcChaStat.pierceAll(owner.getAbility().getTotalCha());
		if (isPc) {
			pc.sendPackets(new S_SpecialResistance(this), true);
		}
	}
	
}

