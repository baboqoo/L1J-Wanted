package l1j.server.server.model;

import java.util.HashMap;
import java.util.Map;

import l1j.server.common.data.Gender;
import l1j.server.server.ActionCodes;
import l1j.server.server.GameServerSetting;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_CloseList;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.polymorph.S_Polymorph;
import l1j.server.server.serverpackets.spell.S_SkillIconGFX;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.StringUtil;

public class L1PolyMorph {
	// weapon equip bit
	private static final int DAGGER_EQUIP			= 1;
	private static final int SWORD_EQUIP			= 2;
	private static final int TWOHANDSWORD_EQUIP		= 4;
	private static final int AXE_EQUIP				= 8;
	private static final int SPEAR_EQUIP			= 16;
	private static final int STAFF_EQUIP			= 32;
	private static final int EDORYU_EQUIP			= 64;
	private static final int CLAW_EQUIP				= 128;
	private static final int BOW_EQUIP				= 256; 
	private static final int KIRINGKU_EQUIP			= 512;
	private static final int CHAINSWORD_EQUIP		= 1024;

	// armor equip bit
	private static final int HELM_EQUIP				= 1;
	private static final int AMULET_EQUIP			= 2;
	private static final int EARRING_EQUIP			= 4;
	private static final int TSHIRT_EQUIP			= 8;
	private static final int ARMOR_EQUIP			= 16;
	private static final int CLOAK_EQUIP			= 32;
	private static final int BELT_EQUIP				= 64;
	private static final int SHIELD_EQUIP			= 128;
	private static final int GARDER_EQUIP			= 128;
	private static final int GLOVE_EQUIP			= 256;
	private static final int RING_EQUIP				= 512;
	private static final int BOOTS_EQUIP			= 1024;

	// 변신의 원인을 나타내는 bit
	public static final int MORPH_BY_LOGIN			= 0;
	public static final int MORPH_BY_ITEMMAGIC		= MORPH_BY_LOGIN + 1;
	public static final int MORPH_BY_GM				= MORPH_BY_ITEMMAGIC << 1;
	public static final int MORPH_BY_NPC			= MORPH_BY_GM << 1; // 점성술사 케프리샤 이외의 NPC
	public static final int MORPH_BY_KEPLISHA		= MORPH_BY_NPC << 1;
	public static final int MORPH_BY_DOMINATION		= MORPH_BY_KEPLISHA << 1;
	public static final int MORPH_BY_WORLD_CLASS	= MORPH_BY_DOMINATION << 1;// 인터서버 클래스 변신
	public static final int MORPH_BY_ARMOR_SET		= MORPH_BY_WORLD_CLASS << 1;// 세트 아이템 변신
	public static final int MORPH_BY_100LEVEL		= MORPH_BY_ARMOR_SET << 1;// 100레벨 변신
	
	public static final String MAPLE_STR			= "maple";
	public static final String EV_STR				= "ev";
	public static final String RANKING_STR			= "rangking ";
	public static final String RANKING_CLASS_STR	= "ranking class polymorph";
	public static final String BASIC_CLASS_STR		= "basic class polymorph";
	public static final String LEVEL_100_STR		= " 100";
	
	public static final String[][] RANKING_CLASS_ARRAY = {
		{ "rangking prince male",		"rangking prince female" },
		{ "rangking knight male",		"rangking knight female" },
		{ "rangking elf male",			"rangking elf female" },
		{ "rangking wizard male",		"rangking wizard female" },
		{ "rangking darkelf male",		"rangking darkelf female" },
		{ "rangking dragonknight male", "rangking dragonknight female" },
		{ "rangking illusionist male",	"rangking illusionist female" },
		{ "rangking warrior male",		"rangking warrior female" },
		{ "rangking fencer male",		"rangking fencer female" },
		{ "rangking lancer male",		"rangking lancer female" }
	};
	
	public static final String[][] BASIC_CLASS_ARRAY = {
		{ "basic prince male 80",		"basic prince female 80" },
		{ "basic knight male 80",		"basic knight female 80" },
		{ "basic elf male 80",			"basic elf female 80" },
		{ "basic wizard male 80",		"basic wizard female 80" },
		{ "basic darkelf male 80",		"basic darkelf female 80" },
		{ "basic dragonknight male 80",	"basic dragonknight female 80" },
		{ "basic illusionist male 80",	"basic illusionist female 80" },
		{ "basic warrior male 80",		"basic warrior female 80" },
		{ "basic fencer male 80",		"basic fencer female 80" },
		{ "basic lancer male 80",		"basic lancer female 80" }
	};

	private static final Map<Integer, Integer> weaponFlgMap = new HashMap<Integer, Integer>();
	static {
		weaponFlgMap.put(1, SWORD_EQUIP);
		weaponFlgMap.put(2, DAGGER_EQUIP);
		weaponFlgMap.put(3, TWOHANDSWORD_EQUIP);
		weaponFlgMap.put(4, BOW_EQUIP);
		weaponFlgMap.put(5, SPEAR_EQUIP);
		weaponFlgMap.put(6, AXE_EQUIP);
		weaponFlgMap.put(7, STAFF_EQUIP);
		weaponFlgMap.put(8, BOW_EQUIP);
		weaponFlgMap.put(9, BOW_EQUIP);
		weaponFlgMap.put(10, BOW_EQUIP);
		weaponFlgMap.put(11, CLAW_EQUIP);
		weaponFlgMap.put(12, EDORYU_EQUIP);
		weaponFlgMap.put(13, BOW_EQUIP);
		weaponFlgMap.put(14, SPEAR_EQUIP);
		weaponFlgMap.put(15, AXE_EQUIP);
		weaponFlgMap.put(16, STAFF_EQUIP);
		weaponFlgMap.put(17, KIRINGKU_EQUIP);
		weaponFlgMap.put(18, CHAINSWORD_EQUIP);
	}
	private static final Map<Integer, Integer> armorFlgMap = new HashMap<Integer, Integer>();
	static {
		armorFlgMap.put(1, HELM_EQUIP);
		armorFlgMap.put(2, ARMOR_EQUIP);
		armorFlgMap.put(3, TSHIRT_EQUIP);
		armorFlgMap.put(4, CLOAK_EQUIP);
		armorFlgMap.put(5, GLOVE_EQUIP);
		armorFlgMap.put(6, BOOTS_EQUIP);
		armorFlgMap.put(7, SHIELD_EQUIP);
		armorFlgMap.put(7, GARDER_EQUIP);
		armorFlgMap.put(8, AMULET_EQUIP);
		armorFlgMap.put(9, RING_EQUIP);
		armorFlgMap.put(10, BELT_EQUIP);
		armorFlgMap.put(12, EARRING_EQUIP);
	}

	private int _id;
	private String _name;
	private int _polyId;
	private int _minLevel;
	private int _weaponEquipFlg;
	private int _armorEquipFlg;
	private boolean _canUseSkill;
	private int _causeFlg;

	public L1PolyMorph(int id, String name, int polyId, int minLevel, int weaponEquipFlg, int armorEquipFlg, boolean canUseSkill, int causeFlg) {
		_id				= id;
		_name			= name;
		_polyId			= polyId;
		_minLevel		= minLevel;
		_weaponEquipFlg	= weaponEquipFlg;
		_armorEquipFlg	= armorEquipFlg;
		_canUseSkill	= canUseSkill;
		_causeFlg		= causeFlg;
	}

	public int getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}

	public int getPolyId() {
		return _polyId;
	}

	public int getMinLevel() {
		return _minLevel;
	}

	public int getWeaponEquipFlg() {
		return _weaponEquipFlg;
	}

	public int getArmorEquipFlg() {
		return _armorEquipFlg;
	}

	public boolean canUseSkill() {
		return _canUseSkill;
	}

	public int getCauseFlg() {
		return _causeFlg;
	}
	
	public static void handleCommands(L1PcInstance pc, String s) {
		int time = polyAlignTime(pc.getAlignment(), 7200, 2400);
		handleCommands(pc, s, time);
	}

	public static void handleCommands(L1PcInstance pc, String s, int time) {
		if (pc == null || pc.isDead()) {
			return;
		}
		L1PolyMorph poly = PolyTable.getInstance().getTemplate(s);
		if (poly != null || s.equals(StringUtil.EmptyString)) {
			if (s.equals(StringUtil.EmptyString)) {
                if (pc.getSpriteId() == 6034 || pc.getSpriteId() == 6035) {
				} else {
					pc.removeShapeChange();
				    pc.sendPackets(new S_CloseList(pc.getId()), true);
				}
			} else if (pc.getLevel() >= poly.getMinLevel() || pc.isGm() || GameServerSetting.POLY_LEVEL_EVENT) {
                if (pc.getSpriteId() == 6034 || pc.getSpriteId() == 6035) {
				} else {
				    doPoly(pc, poly.getPolyId(), time, MORPH_BY_ITEMMAGIC);
				    pc.sendPackets(new S_CloseList(pc.getId()), true);
				}
			} else {
				pc.sendPackets(L1ServerMessage.sm181);
			}
		}
	}

	public static void doPoly(L1Character cha, int polyId, int timeSecs, int cause) {
		if (cha == null || cha.isDead()) {
			return;
		}
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getMapId() == 5302 || pc.getMapId() == 5490) { // 낚시터
				pc.sendPackets(L1ServerMessage.sm1170); // 이곳에서 변신할수 없습니다.
				return;
			}
			if (pc.getSpriteId() == 6034 || pc.getSpriteId() == 6035 || !isMatchCause(polyId, cause)) {
				pc.sendPackets(L1ServerMessage.sm181); // \f1 그러한 monster에게는 변신할 수 없습니다.
				return;
			}
			
			if (cause == MORPH_BY_ARMOR_SET) {
				pc._isArmorSetPoly = true;
			}
			
			if (cause == MORPH_BY_100LEVEL) {
				if (pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE)) {
					pc.getSkill().removeSkillEffect(L1SkillId.SHAPE_CHANGE);
				} else if (pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION)) {
					pc.getSkill().removeSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION);
				}
				if(!pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL)){
				    pc.getAbility().addAddedStr(5);
			        pc.getAbility().addAddedCon(5);
			        pc.getAbility().addAddedDex(5);
			        pc.getAbility().addAddedInt(5);
			        pc.getAbility().addAddedWis(5);
			        pc.getAbility().addAddedCha(5);
			        pc.addMaxHp(500);
			        pc.getResistance().addToleranceAll(5);
			        pc.sendPackets(new S_OwnCharStatus(pc), true);
				}
				if (pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL)) {
					pc.getSkill().killSkillEffectTimer(L1SkillId.SHAPE_CHANGE_100LEVEL);
				}
				pc.getSkill().setSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL, timeSecs * 1000);
			} else if (cause == MORPH_BY_DOMINATION) {
				if (pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE)) {
					pc.getSkill().removeSkillEffect(L1SkillId.SHAPE_CHANGE);
				} else if (pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL)) {
					pc.getSkill().removeSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL);
				}
				if (!pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION)) {
				    pc.getAbility().addAddedStr(1);
			        pc.getAbility().addAddedCon(1);
			        pc.getAbility().addAddedDex(1);
			        pc.getAbility().addAddedInt(1);
			        pc.getAbility().addAddedWis(1);
			        pc.getAbility().addAddedCha(1);
			        pc.addMaxHp(200);
			        pc.sendPackets(new S_OwnCharStatus(pc), true);
				}
				if (pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION)) {
					pc.getSkill().killSkillEffectTimer(L1SkillId.SHAPE_CHANGE_DOMINATION);
				}
				pc.getSkill().setSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION, timeSecs * 1000);
			} else {
				if (pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION)) {
					pc.getSkill().removeSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION);
				} else if (pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL)) {
					pc.getSkill().removeSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL);
				} else if (pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE)) {
					pc.getSkill().killSkillEffectTimer(L1SkillId.SHAPE_CHANGE);
				}
				pc.getSkill().setSkillEffect(L1SkillId.SHAPE_CHANGE, timeSecs * 1000);
			}
			
			if (pc.getSpriteId() != polyId) {
				pc.setSpriteId(polyId);
				S_Polymorph shape = new S_Polymorph(pc.getId(), polyId, pc.getCurrentWeapon());
				if (!pc.isGmInvis() && !pc.isInvisble() && !pc.isGhost()) {
					pc.broadcastPacket(shape);
				}
				pc.sendPackets(shape, true);
				pc.getInventory().takeoffEquip(polyId);
				L1ItemInstance weapon = pc.getWeapon();
				if (weapon != null) {
					pc.broadcastPacketWithMe(new S_CharVisualUpdate(pc), true);
				}
				if ((polyId == 19398 || polyId == 19816) && !pc._isLegendPledgePoly) {
					doLegendPledgePoly(pc, true);// 레전드 혈맹 변신 능력치 세팅
				} else if (polyId != 19398 && polyId != 19816 && pc._isLegendPledgePoly) {
					doLegendPledgePoly(pc, false);// 레전드 혈맹 변신 능력치 제거
				}
			}
			
			pc.sendPackets(cause == MORPH_BY_100LEVEL ? new S_SkillIconGFX(S_SkillIconGFX.POLY, timeSecs, true, true) : new S_SkillIconGFX(S_SkillIconGFX.POLY, timeSecs, cause == MORPH_BY_DOMINATION), true);
			pc.sendPackets(new S_PacketBox(S_PacketBox.ATTACK_RANGE, pc, pc.getWeapon()), true);
		} else if (cha instanceof L1MonsterInstance) {
			L1MonsterInstance mob = (L1MonsterInstance) cha;
			mob.getSkill().killSkillEffectTimer(L1SkillId.SHAPE_CHANGE);
			mob.getSkill().setSkillEffect(L1SkillId.SHAPE_CHANGE, timeSecs * 1000);
			if (mob.getSpriteId() != polyId) {
				mob.setSpriteId(polyId);
				mob.broadcastPacket(new S_Polymorph(mob.getId(), polyId, 0), true);
			}
		}
	}
	
	private static void doLegendPledgePoly(L1PcInstance pc, boolean result){// 레전드 혈맹 변신
		pc.ability.addPVPDamageReduction(result ? 3 : -3);
		pc.getResistance().addToleranceAll(result ? 2 : -2);
		pc.addMaxHp(result ? 150 : -150);
		pc._isLegendPledgePoly = result;
	}
	
	private static final int[] PRIVATE_SHOP_POLYS = { 11479, 11427, 10047, 9688, 11322, 10069, 10034,10032 };
	public static void doPolyPraivateShop(L1Character cha, int polyIndex){
		if (cha == null || cha.isDead()) {
			return;
		}
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getSpriteId() != PRIVATE_SHOP_POLYS[polyIndex - 1]) {
				pc.setSpriteId(PRIVATE_SHOP_POLYS[polyIndex - 1]);
				L1ItemInstance weapon = pc.getWeapon();
				boolean weaponTakeoff = (weapon != null && !isEquipableWeapon(PRIVATE_SHOP_POLYS[polyIndex - 1], weapon.getItem().getType()));
				if (weaponTakeoff) {
					pc.getInventory().setEquipped(weapon, false);
				}
				pc.sendPackets(new S_Polymorph(pc.getId(), PRIVATE_SHOP_POLYS[polyIndex - 1], ActionCodes.ACTION_Shop), true);
				if (!pc.isGmInvis() && !pc.isInvisble()) {
					pc.broadcastPacket(new S_Polymorph(pc.getId(),PRIVATE_SHOP_POLYS[polyIndex - 1], ActionCodes.ACTION_Shop), true);
				}
			}
			pc.broadcastPacketWithMe(new S_CharVisualUpdate(pc, ActionCodes.ACTION_Shop), true);
		}
	}
	
	public static void doPolyTreasure(L1Character cha){
		if (cha == null || cha.isDead()) {
			return;
		}
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getSpriteId() != 20555) {
				pc.removeShapeChange();
				pc.setSpriteId(20555);
				pc.sendPackets(new S_Polymorph(pc.getId(), 20555, pc.getCurrentWeapon()), true);
				if (!pc.isGmInvis() && !pc.isInvisble()) {
					pc.broadcastPacket(new S_Polymorph(pc.getId(), 20555, pc.getCurrentWeapon()), true);
				}
			}
			pc.broadcastPacketWithMe(new S_CharVisualUpdate(pc, pc.getCurrentWeapon()), true);
		}
	}
	
	public static void undoPolySimple(L1Character cha){
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int classId = pc.getClassId();
			pc.setSpriteId(classId);
			if (!pc.isDead()) {
				pc.broadcastPacketWithMe(new S_Polymorph(pc.getId(), classId, pc.getCurrentWeapon()), true);
				pc.broadcastPacketWithMe(new S_CharVisualUpdate(pc, pc.getCurrentWeapon()), true);
			}
		}
	}
	
	public static void doPolyAutoClanjoin(L1Character cha){
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int classId = pc.getClassId();
			if (pc.isAutoClanjoin()) {
				classId = pc.getGender() == Gender.MALE ? 6094 : 6080;// 군주 기마
			}
			pc.setSpriteId(classId);
			if (!pc.isDead()) {
				pc.broadcastPacketWithMe(new S_Polymorph(pc.getId(), classId, pc.getCurrentWeapon()), true);
				pc.broadcastPacketWithMe(new S_CharVisualUpdate(pc, pc.getCurrentWeapon()), true);
			}
		}
	}
	
	public static void undoPoly(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getMapId() == 5143) { // 펫레이싱
				doPoly(pc, 5065, 1000, MORPH_BY_NPC);
				return;
			}
			if (pc._isArmorSetPoly) {
				pc._isArmorSetPoly = false;
			}
			if ((pc.getSpriteId() == 19398 || pc.getSpriteId() == 19816) && pc._isLegendPledgePoly) {// 레전드 혈맹 변신
				doLegendPledgePoly(pc, false);
			}
			int classId = pc.getClassId();
			pc.setSpriteId(classId);
			pc.broadcastPacketWithMe(new S_Polymorph(pc.getId(), classId, pc.getCurrentWeapon()), true);
			L1ItemInstance weapon = pc.getWeapon();
			
			if (weapon != null) {
				pc.broadcastPacketWithMe(new S_CharVisualUpdate(pc), true);
			}
			pc.sendPackets(new S_PacketBox(S_PacketBox.ATTACK_RANGE, pc, pc.getWeapon()), true);
			if (pc.getNetConnection() != null) {
				L1InterServer inter = pc.getNetConnection().getInter();
				if (L1InterServer.isWorldPolyInter(inter)) {
					worldClassPoly(pc, true);
				} else if (L1InterServer.isAnonymityInter(inter)) {
					anonymityPoly(pc, true);
				}
			}
		} else if (cha instanceof L1MonsterInstance) {
			L1MonsterInstance mob = (L1MonsterInstance) cha;
			mob.setSpriteId(mob.getNpcTemplate().getSpriteId());
			mob.broadcastPacket(new S_Polymorph(mob.getId(), mob.getSpriteId(), 0), true);
		}
	}

	public static void MagicBookPoly(L1PcInstance pc, String s, int time) {
		if (pc == null || pc.isDead()) {
			return;
		}
		L1PolyMorph poly = PolyTable.getInstance().getTemplate(s);
		if (poly != null) {
			doPoly(pc, poly.getPolyId(), time, MORPH_BY_ITEMMAGIC);
			pc.sendPackets(new S_CloseList(pc.getId()), true);
		}
		if (pc.getMagicItemId() != 0) {
			pc.getInventory().consumeItem(pc.getMagicItemId(), 1);
			pc.setMagicItemId(0);
		}
	}
	
	private static String basicClassPolyName(L1PcInstance pc){
		boolean isMale = pc.getGender() == Gender.MALE;
		switch(pc.getType()){
		case 0:return isMale ? "basic prince male war" : "basic prince female war";
		case 1:return isMale ? "basic knight male war" : "basic knight female war";
		case 2:return isMale ? "basic elf male war" : "basic elf female war";
		case 3:return isMale ? "basic wizard male war" : "basic wizard female war";
		case 4:return isMale ? "basic darkelf male war" : "basic darkelf female war";
		case 5:return isMale ? "basic dragonknight male war" : "basic dragonknight female war";
		case 6:return isMale ? "basic illusionist male war" : "basic illusionist female war";
		case 7:return isMale ? "basic warrior male war" : "basic warrior female war";
		case 8:return isMale ? "basic fencer male war" : "basic fencer female war";
		case 9:return isMale ? "basic lancer male war" : "basic lancer female war";
		default:return StringUtil.EmptyString;
		}
	}
	
	// 익명 변신
	public static void anonymityPoly(L1PcInstance pc, boolean result){
		if (result) {
			L1PolyMorph poly = PolyTable.getInstance().getTemplate(basicClassPolyName(pc));
			if (poly != null) {
				if (pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE)) {
					pc.getSkill().removeSkillEffect(L1SkillId.SHAPE_CHANGE);
				} else if (pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION)) {
					if (pc.getConfig()._dominationPolyRing) {
						return;
					}
					pc.getSkill().removeSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION);
				} else if (pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL)) {
					if (pc.getConfig()._level100PolyRing) {
						return;
					}
					pc.getSkill().removeSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL);
				}
				int polyId = poly.getPolyId();
				if (pc.getSpriteId() != polyId) {
					L1ItemInstance weapon = pc.getWeapon();
					pc.setSpriteId(polyId);
					pc.sendPackets(new S_Polymorph(pc.getId(), polyId, pc.getCurrentWeapon()), true);
					if (!pc.isGmInvis() && !pc.isInvisble() && !pc.isGhost()) {
						pc.broadcastPacket(new S_Polymorph(pc.getId(), polyId, pc.getCurrentWeapon()), true);
					}
					pc.getInventory().takeoffEquip(polyId);
					weapon = pc.getWeapon();
					if (weapon != null) {
						pc.broadcastPacketWithMe(new S_CharVisualUpdate(pc), true);
					}
				}
				//pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 824, result), true);
				pc.sendPackets(new S_SkillIconGFX(S_SkillIconGFX.POLY, -1, false), true);
				if (pc._isAnonymityPoly) {
					return;
				}
				setAnonymityPolyAblity(pc, true);
				pc._isAnonymityPoly = true;
			}
		} else {
			undoPoly(pc);
			//pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 824, result), true);
			if (!pc._isAnonymityPoly) {
				return;
			}
			setAnonymityPolyAblity(pc, false);
			pc._isAnonymityPoly = false;
		}
	}
	
	private static void setAnonymityPolyAblity(L1PcInstance pc, boolean flag){
		pc.getAbility().addAddedStr(flag ? 1 : -1);
        pc.getAbility().addAddedCon(flag ? 1 : -1);
        pc.getAbility().addAddedDex(flag ? 1 : -1);
        pc.getAbility().addAddedInt(flag ? 1 : -1);
        pc.getAbility().addAddedWis(flag ? 1 : -1);
        pc.getAbility().addAddedCha(flag ? 1 : -1);
        pc.addMaxHp(flag ? 200 : -200);
	}
	
	public static void worldClassPoly(L1PcInstance pc, boolean result){
		if (result) {
			L1PolyMorph poly = PolyTable.getInstance().getTemplate(basicClassPolyName(pc));
			if (poly != null) {
				if (pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE)) {
					pc.getSkill().removeSkillEffect(L1SkillId.SHAPE_CHANGE);
				} else if (pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION)) {
					if (pc.getConfig()._dominationPolyRing) {
						return;
					}
					pc.getSkill().removeSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION);
				} else if (pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL)) {
					if (pc.getConfig()._level100PolyRing) {
						return;
					}
					pc.getSkill().removeSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL);
				}
				int polyId = poly.getPolyId();
				if (pc.getSpriteId() != polyId) {
					L1ItemInstance weapon = pc.getWeapon();
					pc.setSpriteId(polyId);
					pc.sendPackets(new S_Polymorph(pc.getId(), polyId, pc.getCurrentWeapon()), true);
					if (!pc.isGmInvis() && !pc.isInvisble() && !pc.isGhost()) {
						pc.broadcastPacket(new S_Polymorph(pc.getId(), polyId, pc.getCurrentWeapon()), true);
					}
					pc.getInventory().takeoffEquip(polyId);
					weapon = pc.getWeapon();
					if (weapon != null) {
						pc.broadcastPacketWithMe(new S_CharVisualUpdate(pc), true);
					}
				}
				//pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 841, result), true);
				pc.sendPackets(new S_SkillIconGFX(S_SkillIconGFX.POLY, -1, false), true);
				if (pc._isWorldPoly) {
					return;
				}
				setWorldPolyAblity(pc, true);
				pc._isWorldPoly = true;
			}
		} else {
			undoPoly(pc);
			//pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 841, result), true);
			if (!pc._isWorldPoly) {
				return;
			}
			setWorldPolyAblity(pc, false);
			pc._isWorldPoly = false;
		}
	}
	
	private static void setWorldPolyAblity(L1PcInstance pc, boolean flag){
		pc.getResistance().addHitupAll(flag ? 5 : -5);
		pc.ability.addPVPDamage(flag ? 3 : -3);
	}
	
	public static boolean isEquipableWeapon(int polyId, int weaponType) {
		L1PolyMorph poly = PolyTable.getInstance().getTemplate(polyId);
		if (poly == null) {
			return true;
		}
		Integer flg = weaponFlgMap.get(weaponType);
		if (flg != null) {
			return 0 != (poly.getWeaponEquipFlg() & flg);
		}
		return true;
	}

	public static boolean isEquipableArmor(int polyId, int armorType) {
		L1PolyMorph poly = PolyTable.getInstance().getTemplate(polyId);
		if (poly == null) {
			return true;
		}
		Integer flg = armorFlgMap.get(armorType);
		if (flg != null) {
			return 0 != (poly.getArmorEquipFlg() & flg);
		}
		return true;
	}
	
	// 지정한 polyId가 무엇에 의해 변신해, 그것이 변신 당할까?
	public static boolean isMatchCause(int polyId, int cause) {
		L1PolyMorph poly = PolyTable.getInstance(). getTemplate(polyId);
		if (poly == null) {
			return true;
		}
		if (cause == MORPH_BY_LOGIN || cause == MORPH_BY_DOMINATION || cause == MORPH_BY_100LEVEL || cause == MORPH_BY_ARMOR_SET) {
			return true;
		}
		return 0 != (poly.getCauseFlg() & cause);
	}
	
	public static int polyAlignTime(int align, int max, int min) {
		if (align >= 32767) {
			return max;
		}
		if (align <= -32768) {
			return min;
		}
		double d = 65535 / (max - min);
		int ex = align + 32768;
		if (ex > 65535) {
			ex = 65535;
		}
		int time = (int) (ex / d + min);
		return IntRange.ensure(time, min, max);
	}
	
	public static void poly(L1PcInstance pc, int polyId) {
		if (pc.getInventory().consumeItem(L1ItemId.ADENA, 100)) {
			pc.removeShapeChange();
			doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_NPC);
		} else {
			pc.sendPackets(L1ServerMessage.sm189);
		}
	}
}

