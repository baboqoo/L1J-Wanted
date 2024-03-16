package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.L1Alignment;
import l1j.server.server.construct.L1Attr;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillType;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.spell.S_AvailableSpellNoti;
import l1j.server.server.serverpackets.spell.S_AddSpellPassiveNoti;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1PassiveSkills;
import l1j.server.server.templates.L1Skills;

public class Spellbook extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public Spellbook(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		try {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				int delay_id = ((L1EtcItem) getItem()).getDelayId();
				if (delay_id != 0 && pc.hasItemDelay(delay_id)) {
					return;// 지연 설정 있어
				}
				if (((L1EtcItem) getItem()).getEtcValue() == 99) {// 기본 마법서
					nomalSkill(pc);
				} else if ((getItem().isUseRoyal() && pc.isCrown())
						|| (getItem().isUseElf() && pc.isElf())
						|| (getItem().isUseDarkelf() && pc.isDarkelf())
						|| (getItem().isUseKnight() && pc.isKnight())
						|| (getItem().isUseDragonKnight() && pc.isDragonknight())
						|| (getItem().isUseIllusionist() && pc.isIllusionist())
						|| (getItem().isUseWarrior() && pc.isWarrior())
						|| (getItem().isUseFencer() && pc.isFencer())
						|| (getItem().isUseLancer() && pc.isLancer())) {// 클래스별 스킬북
					learnSkill(pc);
				} else {
					pc.sendPackets(L1ServerMessage.sm79);// 아무일도 일어나지 않았습니다.
				}
				L1ItemDelay.onItemUse(pc, this);// 아이템 지연 개시
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 일반 마법서
	 * @param pc
	 */
	private void nomalSkill(L1PcInstance pc) {
		if (pc.isIllusionist()) {
			pc.sendPackets(L1ServerMessage.sm79);
			return;
		}
		boolean isLawful	= getItem().getAlignment() == L1Alignment.NEUTRAL || getItem().getAlignment() == L1Alignment.LAWFUL;
		int skillLevel		= getItem().getSkillLevel();
		int level			= pc.getLevel();
		if (pc.isGm()) {
			learnSkill(pc, isLawful);
			return;
		}
		switch(pc.getType()){
		case 1:case 7:// 기사, 전사
			if (skillLevel == 1 && level >= 50) {
				activeSkill(pc, isLawful);
			} else if (skillLevel == 1) {
				pc.sendPackets(L1ServerMessage.sm312);
			} else {
				pc.sendPackets(L1ServerMessage.sm79);
			}
			break;
		case 0:case 4:case 5:case 8:case 9:// 군주, 다크엘프, 용기사, 검사, 창기사
			if ((skillLevel == 1 && level >= 15) 
					|| (skillLevel == 2 && level >= 30)){
				activeSkill(pc, isLawful);
			} else if (skillLevel >= 1 && skillLevel <= 2) {
				pc.sendPackets(L1ServerMessage.sm312);
			} else {
				pc.sendPackets(L1ServerMessage.sm79);
			}
			break;
		case 2:// 요정
			if ((skillLevel == 1 && level >= 10) 
					|| (skillLevel == 2 && level >= 20) 
					|| (skillLevel == 3 && level >= 30)
					|| (skillLevel == 4 && level >= 40)
					|| (skillLevel == 5 && level >= 50)
					|| (skillLevel == 6 && level >= 60)) {
				activeSkill(pc, isLawful);
			} else if (skillLevel >= 1 && skillLevel <= 6) {
				pc.sendPackets(L1ServerMessage.sm312);
			} else {
				pc.sendPackets(L1ServerMessage.sm79);
			}
			break;
		case 3:// 마법사
			if ((skillLevel == 1 && level >= 8)
					|| (skillLevel == 2 && level >= 16)
					|| (skillLevel == 3 && level >= 24)
					|| (skillLevel == 4 && level >= 32)
					|| (skillLevel == 5 && level >= 40)
					|| (skillLevel == 6 && level >= 48)
					|| (skillLevel == 7 && level >= 56)
					|| (skillLevel == 8 && level >= 64)
					|| (skillLevel == 9 && level >= 72)
					|| (skillLevel == 10 && level >= 80)
					|| (skillLevel == 11 && level >= 85)
					|| (skillLevel == 12 && level >= 90)) {
				switch(getItem().getSkillType()){
				case ACTIVE:
					activeSkill(pc, isLawful);
					break;
				case PASSIVE:
					passiveSkill(pc);
					break;
				default:break;
				}
			} else {
				pc.sendPackets(L1ServerMessage.sm312);
			}
			break;
		}
	}
	
	private void learnSkill(L1PcInstance pc){
		if (pc.getLevel() < getItem().getMinLevel()) {
			pc.sendPackets(L1ServerMessage.sm312);// LV가 낮아서
			return;
		}
		learnSkill(pc, true);
	}
	
	private void learnSkill(L1PcInstance pc, boolean isLawful){
		switch(getItem().getSkillType()){
		case ACTIVE:
			activeSkill(pc, isLawful);
			break;
		case PASSIVE:
			passiveSkill(pc);
			break;
		default:
			pc.sendPackets(L1ServerMessage.sm79);
			break;
		}
	}
	
	/**
	 * 요정 정령 마법 속성 유효성 검증
	 * @param pc
	 * @param attr
	 * @return boolean
	 */
	private boolean isElfAttr(L1PcInstance pc, int attr){
		if (pc.getElfAttr() != attr
				&& ((attr == 1 && pc.getElfAttr() != 9 && pc.getElfAttr() != 10 && pc.getElfAttr() != 12)// 땅
				|| (attr == 2 && pc.getElfAttr() != 3 && pc.getElfAttr() != 5 && pc.getElfAttr() != 9)// 불
				|| (attr == 4 && pc.getElfAttr() != 3 && pc.getElfAttr() != 6 && pc.getElfAttr() != 10)// 물
				|| (attr == 8 && pc.getElfAttr() != 5 && pc.getElfAttr() != 6 && pc.getElfAttr() != 12))) {// 바람
			pc.sendPackets(L1ServerMessage.sm79);// 아무일도 일어나지 않았습니다.
			return false;
		}
		return true;
	}
	
	/**
	 * 액티브 스킬 습득 처리
	 * @param pc
	 * @param isLawful
	 */
	private void activeSkill(L1PcInstance pc, boolean isLawful){
		L1Skills active = SkillsTable.getActiveToClassType(this, pc.getType());
		if (active == null) {
			emptyPrint(pc, L1SkillType.ACTIVE.getName());
			return;
		}
		if (!pc.isGm() && pc.isElf() && active.getAttr() != L1Attr.NONE && active.getClassType() == pc.getType() && !isElfAttr(pc, active.getAttr().toInt())) {
			return;
		}
		int skillId = active.getSkillId();
		if (pc.getSkill().isLearnActive(skillId, false)) {
			pc.sendPackets(L1ServerMessage.sm3237);// 이미 학습을 한 상태입니다.
			return;
		}
		pc.sendPackets(new S_AvailableSpellNoti(skillId, true), true);
		pc.send_effect((pc.isWizard() && !isLawful) || pc.isDarkelf() ? 231 : 224);
		pc.getSkill().spellActiveMastery(active);
		pc.getInventory().removeItem(this, 1);
	}
	
	/**
	 * 패시브 스킬 습득 처리
	 * @param pc
	 */
	private void passiveSkill(L1PcInstance pc) {
		//System.out.println("intentando aprender un skill pasivo para la clase " + pc.getType());
		L1PassiveSkills passive = SkillsTable.getPassiveToClassType(this, pc.getType());
		if (passive == null) {
			emptyPrint(pc, L1SkillType.PASSIVE.getName());
			return;
		}
		int id		= passive.getPassiveId();
		if (pc.getSkill().isLearnPassive(id)) {
			pc.sendPackets(L1ServerMessage.sm3237);// 이미 학습을 한 상태입니다.
			return;
		}
		
		// 선행 스킬
		if ((passive.getBackActive() != null && !pc.getSkill().isLearnActive(passive.getBackActive().getSkillId(), false))
				|| (passive.getBackPassive() != null && !pc.getSkill().isLearnPassive(passive.getBackPassive().getPassiveId()))) {
			pc.sendPackets(L1ServerMessage.sm5308);// 선행되는 스킬을 배우지 않았습니다.
			return;
		}

		pc.sendPackets(new S_AddSpellPassiveNoti(id, true), true);
		pc.send_effect(224);
		pc.getSkill().spellPassiveMastery(passive);
		pc.getPassiveSkill().set(passive.getPassive());// 패시브스킬 능력치 반영
		pc.getInventory().removeItem(this, 1);
	}
	
//AUTO SRM: 	private static final S_SystemMessage SKILL_NUM_EMPTY = new S_SystemMessage("스킬번호가 존재하지 않습니다. 운영자에게 문의하십시오."); // CHECKED OK
	private static final S_SystemMessage SKILL_NUM_EMPTY = new S_SystemMessage(S_SystemMessage.getRefText(1096), true);
	private void emptyPrint(L1PcInstance pc, String skillType){
		pc.sendPackets(SKILL_NUM_EMPTY);
		System.out.println(String.format(
				"[Spellbook] SKILL_TEMPLATE_NOT_FOUND : SKILLTYPE(%s), ITEMID(%d), ITEMNAME(%s), CHARNAME(%s)",
				skillType, getItemId(), getDesc(), pc.getName()
				));
	}
}


