package l1j.server.server.clientpackets;

import static l1j.server.server.model.skill.L1SkillId.ARROW_OF_AURAKIA;
import static l1j.server.server.model.skill.L1SkillId.AVENGER;
import static l1j.server.server.model.skill.L1SkillId.BLADE;
import static l1j.server.server.model.skill.L1SkillId.BLOW_ATTACK;
import static l1j.server.server.model.skill.L1SkillId.BONE_BREAK;
import static l1j.server.server.model.skill.L1SkillId.CAL_CLAN_ADVANCE;
import static l1j.server.server.model.skill.L1SkillId.COUNTER_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.CRUEL;
import static l1j.server.server.model.skill.L1SkillId.DANCING_BLADES;
import static l1j.server.server.model.skill.L1SkillId.DESPERADO;
import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;
import static l1j.server.server.model.skill.L1SkillId.EMPIRE;
import static l1j.server.server.model.skill.L1SkillId.FIRE_WALL;
import static l1j.server.server.model.skill.L1SkillId.FORCE_STUN;
import static l1j.server.server.model.skill.L1SkillId.HALPHAS;
import static l1j.server.server.model.skill.L1SkillId.HELLFIRE;
import static l1j.server.server.model.skill.L1SkillId.IMMUNE_TO_HARM;
import static l1j.server.server.model.skill.L1SkillId.INFERNO;
import static l1j.server.server.model.skill.L1SkillId.JUDGEMENT;
import static l1j.server.server.model.skill.L1SkillId.LIFE_STREAM;
import static l1j.server.server.model.skill.L1SkillId.LUCIFER;
import static l1j.server.server.model.skill.L1SkillId.MASS_TELEPORT;
import static l1j.server.server.model.skill.L1SkillId.PANTERA;
import static l1j.server.server.model.skill.L1SkillId.PHANTOM;
import static l1j.server.server.model.skill.L1SkillId.POS_WAVE;
import static l1j.server.server.model.skill.L1SkillId.POWER_GRIP;
import static l1j.server.server.model.skill.L1SkillId.PRESSURE;
import static l1j.server.server.model.skill.L1SkillId.PRIME;
import static l1j.server.server.model.skill.L1SkillId.PROTECTION_FROM_ELEMENTAL;
import static l1j.server.server.model.skill.L1SkillId.RECOVERY;
import static l1j.server.server.model.skill.L1SkillId.SHADOW_STEP;
import static l1j.server.server.model.skill.L1SkillId.SHOCK_STUN;
import static l1j.server.server.model.skill.L1SkillId.SUMMON_GREATER_ELEMENTAL;
import static l1j.server.server.model.skill.L1SkillId.SUMMON_LESSER_ELEMENTAL;
import static l1j.server.server.model.skill.L1SkillId.SUMMON_MONSTER;
import static l1j.server.server.model.skill.L1SkillId.TELEPORT;
import static l1j.server.server.model.skill.L1SkillId.TELEPORT_TO_MATHER;
import static l1j.server.server.model.skill.L1SkillId.TRUE_TARGET;
import static l1j.server.server.model.skill.L1SkillId.TYRANT;
import static l1j.server.server.model.skill.L1SkillId.VISION_TELEPORT;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.construct.L1Attr;
import l1j.server.server.construct.L1Grade;
import l1j.server.server.construct.item.L1ItemWeaponType;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1SkillStatus;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.model.sprite.L1Sprite;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Skills;

public class C_UseSkill extends ClientBasePacket {
	private L1PcInstance pc;
	private L1World world;
	private L1Map pcMap;
	private L1SkillStatus status;
	private L1Object target;
	private int skillId, targetId, targetX, targetY;
	private L1Skills skills;
	
	public C_UseSkill(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		pc = client.getActiveChar();
		if (pc == null) {
			return;
		}

		pcMap = pc.getMap();
		if (!pcMap.isUsableSkill()) {
			pc.sendPackets(L1ServerMessage.sm563);// \f1 여기에서는 사용할 수 없습니다.
			return;
		}
		
		// 중량 오버
		if (!pc._isWeightNoActionPenalty && pc.getInventory().getWeightPercent() > 82) {
			pc.sendPackets(L1ServerMessage.sm316);
			return;
		}
		
		// 스킬을 사용할 수 없는 변신
		L1PolyMorph poly = PolyTable.getInstance().getTemplate(pc.getSpriteId());
		if (poly != null && !poly.canUseSkill()) {
			pc.sendPackets(L1ServerMessage.sm285);
			return;
		}
		
		// TODO skill id
		skillId	= readH();
		
		// 멈춤상태 불가능(리커버리 제외)
		if (skillId != RECOVERY && pc.isStop()) {
			if (skillId == TELEPORT || skillId == MASS_TELEPORT || skillId == TELEPORT_TO_MATHER) {
				pc.isPressureDeathRecall();
				pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
			}
			return;
		}
		
		skills	= SkillsTable.getTemplate(skillId);
		if (skills == null) {
			System.out.println(String.format("[C_UseSkill] SKILL_TEMPLATE_EMPTY : SKILL_ID(%d), CHAR_NAME(%s)", skillId, pc.getName()));
			return;
		}
		
		status = pc.getSkill();
		// 딜레이 여부 검증
		if (isDelay()) {
			return;
		}

		if (abyte0.length > 4) {
			parse();
		}

		// 스킬 범위버그 수정
		world		= L1World.getInstance();
		target		= world.findObject(targetId);

		if (!isValidation()) {
			return;
		}

		pc.cancelAbsoluteBarrier();// 앱솔루트 배리어의 해제
		if (skillId == PRIME) {
			primeDelete();
		}

		try {
			if (target != null && target instanceof L1PcInstance && ((L1PcInstance) target).isLancer() && isMaelstromSkill() && isMaelstromResult()) {
				maelstromTargetChange();
			}
			
			// TODO execute
			L1SkillUse l1skilluse = new L1SkillUse();
			l1skilluse.handleCommands(pc, skillId, targetId, targetX, targetY, 0, L1SkillUseType.NORMAL);
			l1skilluse = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 스킬 사용 딜레이 검증
	 * @return boolean
	 */
	boolean isDelay() {
		// 딜레이 여부 검증
		int delay_group_id = skills.getDelayGroupId();
		if (status.isSkillDelay(delay_group_id)) {
			/*long delay_duration		= status.getSkillDelayDuration(delay_group_id);
			int distance_duration	= (int)(delay_duration - System.currentTimeMillis());
			if (distance_duration > 0) {
				int global_delay = 1500;
				if (global_delay > distance_duration) {
					global_delay = distance_duration;
				}
				pc.sendPackets(new S_SpellDelay(delay_group_id, distance_duration, global_delay), true);
			}*/
			return true;
		}
		return false;
	}
	
	/**
	 * 스킬 사용가능 검증
	 * @return boolean
	 */
	boolean isValidation() {
		int skillRange	= skills.getRanged();
		if (skillRange < 0) {
			skillRange = 15;
		}
		if (skillId == POS_WAVE && pc._isLancerForm) {
			skillRange = 5;
		}
		// 거리 증가
		if ((skillId == PANTERA && pc.isPassiveStatus(L1PassiveId.PANTERA_SHOCK))
				|| (skillId == CRUEL && pc.isPassiveStatus(L1PassiveId.CRUEL_CONVICTION))
				|| (skillId == SHADOW_STEP && pc.isPassiveStatus(L1PassiveId.SHADOW_STEP_CHASER))) {
			skillRange++;
		}
		// 타겟 좌표 검증
		if (target instanceof L1Character) {
			if (target.getMapId() != pc.getMapId()) {
				return false;
			}
			L1Sprite sprite = ((L1Character) target).getSprite();
			if (sprite != null) {
				skillRange += sprite.get_width();
			}
			if (pc.getLocation().getTileLineDistance(target.getLocation()) > skillRange) {
				return false;
			}
		}
		
		// 지면상의 아이템
		if (target instanceof L1ItemInstance && target.getX() != 0 && target.getY() != 0) {
			return false;
		}
		
		// 존재버그 검증
		L1PcInstance other = world.getPlayer(pc.getName());
		if (other == null && pc.getAccessLevel() != Config.ALT.GMCODE) {
			pc.sendPackets(L1SystemMessage.TWO_CHAR_CHECK);
			pc.denals_disconnect(String.format("[C_UseSkill] WORLD_NOT_FOUND_USER : NAME(%s)", pc.getName()));
			return false;
		}
		
		// 비전 텔레포트
		if (skillId == VISION_TELEPORT && isVisionTeleportNotEnable()) {
			return false;
		}
		
		// 아우라키아의 화살
		if (skillId == ARROW_OF_AURAKIA && (target instanceof L1NpcInstance == false || ((L1NpcInstance) target).getNpcId() != 7800300)) {
			return false;
		}
		
		// 콜 클랜 어드밴스 대상
		if (skillId == CAL_CLAN_ADVANCE && !isCallClanAdvenceEnable()) {
			return false;
		}
		
		// 공성 지역에서 사용 할 수 없는 스킬
		int castle_id = L1CastleLocation.getCastleIdByArea(pc);
		if (castle_id != 0 && skillId == MASS_TELEPORT) {
			pc.sendPackets(L1ServerMessage.sm563);
			return false;
		}
		
		// 속성 스킬 검증
		if (!isElementalAgrees()) {
			return false;
		}
		
		if (skillId == PRIME && pc.getClanid() == 0) {// 혈맹 체크
			pc.sendPackets(L1ServerMessage.sm7091);// 혈맹이 없는 상태에서 해당 마법은 사용할 수 없습니다.
			return false;
		}
		if (skillId == SUMMON_MONSTER || skillId == SUMMON_LESSER_ELEMENTAL || skillId == SUMMON_GREATER_ELEMENTAL) { 
			Object[] petlist = pc.getPetList().values().toArray();
			for (Object petObject : petlist) {
				if (petObject instanceof L1PetInstance) {
					pc.sendPackets(L1ServerMessage.sm319);
					return false;
				}
			}
		}
		if (skillId == LUCIFER && status.hasSkillEffect(IMMUNE_TO_HARM)) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("사용 불가능: 이뮨 투 함 상태"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(131), true), true);
			return false;
		}
		
		// 착용 장비 검증
		L1ItemWeaponType weaponType = pc.getWeapon() != null ? pc.getWeapon().getItem().getWeaponType() : null;
		if ((skillId == COUNTER_BARRIER || skillId == SHOCK_STUN || skillId == FORCE_STUN) && weaponType != L1ItemWeaponType.TOHAND_SWORD) {// 양손검 체크
			pc.sendPackets(L1ServerMessage.sm3845);
			return false;
		}
		if ((skillId == INFERNO || skillId == JUDGEMENT || skillId == PHANTOM || skillId == PANTERA) && weaponType != L1ItemWeaponType.SWORD) {// 한손검 체크
			pc.sendPackets(L1ServerMessage.sm3845);
			return false;
		}
		if ((skillId == HELLFIRE || skillId == BLADE || skillId == TYRANT) && !L1ItemWeaponType.isSwordWeapon(weaponType)) {// 검 체크
			pc.sendPackets(L1ServerMessage.sm3845);
			return false;
		}
		if (skillId == BLOW_ATTACK && !L1ItemWeaponType.isShortWeapon(weaponType)){
			pc.sendPackets(L1ServerMessage.sm3845);
			return false;
		}
		if (skillId == AVENGER && weaponType != L1ItemWeaponType.CLAW && weaponType != L1ItemWeaponType.EDORYU) {// 이도류, 크로우 검사
			pc.sendPackets(L1ServerMessage.sm3845);
			return false;
		}
		if (skillId == HALPHAS && weaponType != L1ItemWeaponType.CHAINSWORD) {// 체인소드 검사
			pc.sendPackets(L1ServerMessage.sm3845);
			return false;
		}
		if (skillId == DANCING_BLADES && weaponType != L1ItemWeaponType.SWORD && weaponType != L1ItemWeaponType.DAGGER) {
			pc.sendPackets(L1ServerMessage.sm3845);
			return false;
		}
		if (skillId == EMPIRE && pc.getInventory().getEquippedShield() == null) {// 방패 체크
			pc.sendPackets(L1ServerMessage.sm3845);
			return false;
		}
		
		return true;
	}
	
	/**
	 * 속성 스킬 검증
	 * @return boolean
	 */
	boolean isElementalAgrees() {
		L1Attr skillAttr	= skills.getAttr();
		if (skillAttr == L1Attr.NONE) {
			return true;
		}
		int elemental		= pc.getElfAttr();
		if (skills.getClassType() == 2
				&& !pc.isGm() 
				&& ((skillAttr == L1Attr.EARTH && elemental != 1 && elemental != 9 && elemental != 10 && elemental != 12)
					|| (skillAttr == L1Attr.FIRE && elemental != 2 && elemental != 3 && elemental != 5 && elemental != 9)
					|| (skillAttr == L1Attr.WATER && elemental != 4 && elemental != 3 && elemental != 6 && elemental != 10)
					|| (skillAttr == L1Attr.WIND && elemental != 8 && elemental != 5 && elemental != 6 && elemental != 12))) {
			return false;
		}
		
		if (skillId == PROTECTION_FROM_ELEMENTAL && elemental == 0) {
			pc.sendPackets(L1ServerMessage.sm280);
			return false;
		}
		return true;
	}
	
	/**
	 * 패킷 파싱
	 * @throws Exception
	 */
	void parse() throws Exception {
		switch(skillId){
		case TRUE_TARGET:
			targetId	= readD();
			targetX		= readH();
			targetY		= readH();
			break;
		case TELEPORT:case MASS_TELEPORT:
			targetId	= readH();
			targetX		= readH();
			targetY		= readH();
			break;
		case SUMMON_MONSTER:
			targetX		= readC();
			targetY		= readC();
			break;
		case FIRE_WALL:case LIFE_STREAM:case VISION_TELEPORT:
			targetX		= readH();
			targetY		= readH();
			break;
		default:
			if (isRead(4)) {
				targetId	= readD();
			}
			if (isRead(2)) {
				targetX		= readH();
			}
			if (isRead(2)) {
				targetY		= readH();
			}
			break;
		}
	}
	
	/**
	 * 마엘스트롬에 의한 대상 변경
	 */
	void maelstromTargetChange(){
		targetId	= pc.getId();
		targetX		= pc.getX();
		targetY		= pc.getY();
	}
	
	/**
	 * 마엘스트롬 적용 스킬 검증
	 * @return boolean
	 */
	boolean isMaelstromSkill(){
		return skillId == SHOCK_STUN || skillId == EMPIRE 
				|| (skillId == DESPERADO && !pc.isPassiveStatus(L1PassiveId.DESPERADO_ABSOLUTE)) 
				|| skillId == EARTH_BIND || skillId == BONE_BREAK || skillId == POWER_GRIP 
				|| (skillId == PHANTOM && !pc.isPassiveStatus(L1PassiveId.PHANTOM_DEATH)) 
				|| (skillId == PANTERA && !pc.isPassiveStatus(L1PassiveId.PANTERA_SHOCK))
				|| (skillId == PRESSURE && !pc.isPassiveStatus(L1PassiveId.PRESSURE_DEATH_RECALL)) 
				|| (skillId == CRUEL && !pc.isPassiveStatus(L1PassiveId.CRUEL_CONVICTION));
	}
	
	/**
	 * 마엘스트롬 적용 여부
	 * @return boolean
	 */
	boolean isMaelstromResult(){
		L1PcInstance targetPc = (L1PcInstance) target;
		if (targetPc.isPassiveStatus(L1PassiveId.MAELSTROM) && targetPc.getInventory().checkItem(L1ItemId.GEMSTONE, 15) && (int)(Math.random() * 100) + 1 < Config.SPELL.MAELSTROM_PROB) {
			targetPc.getInventory().consumeItem(L1ItemId.GEMSTONE, 15);
			targetPc.send_effect(19394);
			return true;
		}
		return false;
	}
	
	/**
	 * 비전 텔레포트 유효성 검사
	 * @return boolean
	 */
	boolean isVisionTeleportNotEnable(){
		return !pc.glanceCheck(skills.getRanged(), targetX, targetY, false) || !pcMap.isInMap(targetX, targetY) || !pcMap.isPassable(targetX, targetY);
	}
	
	/**
	 * 프라임 제거
	 */
	void primeDelete(){
		for (L1PcInstance member : L1World.getInstance().getVisibleClanPlayer(pc, 18)) {
			if (member.getSkill().hasSkillEffect(PRIME)) {
				member.getSkill().removeSkillEffect(PRIME);
			}
		}
	}
	
	/**
	 * 콜 클랜 어드밴스 사용 가능 유효성 검사
	 * @return boolean
	 */
	boolean isCallClanAdvenceEnable(){
		if (pc.getClanid() == 0) {
			pc.sendPackets(L1ServerMessage.sm7091);// 혈맹이 없는 상태에서 해당 마법은 사용할 수 없습니다.
			return false;
		}
		if (target instanceof L1PcInstance == false) {
			return false;
		}
		L1PcInstance cal_target = (L1PcInstance) target;
		if (cal_target == null || pc.getLocation().getLineDistance(cal_target.getLocation()) > skills.getRanged() 
				|| pc.getParty() == null || !pc.getParty().isMember(cal_target)) {
			return false;
		}
		
		// 유일 등급 CC기에 적중된 상태
		ArrayList<L1Skills> only_actives = SkillsTable.getGradeActiveList(L1Grade.ONLY);
		if (only_actives != null && !only_actives.isEmpty()) {
			L1SkillStatus status = cal_target.getSkill();
			for (L1Skills only : only_actives) {
				if (status.hasSkillEffect(only.getSkillId())) {
					return false;
				}
			}
		}
		return true;
	}
	
}

