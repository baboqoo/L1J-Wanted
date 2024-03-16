package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.templates.L1Item;

public class BravePotion extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public BravePotion(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.isPotionPenalty()) {
				pc.sendPackets(L1ServerMessage.sm698); // \f1마력에 의해 아무것도 마실 수가 없습니다.
				return;
			}
			if (!(pc.isCrown() && getItem().isUseRoyal() 
					|| pc.isKnight() && getItem().isUseKnight()
					|| pc.isElf() && getItem().isUseElf() 
					|| pc.isWizard() && getItem().isUseMage()
					|| pc.isDarkelf() && getItem().isUseDarkelf()
					|| pc.isDragonknight() && getItem().isUseDragonKnight()
					|| pc.isIllusionist() && getItem().isUseIllusionist() 
					|| pc.isWarrior() && getItem().isUseWarrior()
					|| pc.isFencer() && getItem().isUseFencer()
					|| pc.isLancer() && getItem().isUseLancer())) {
				pc.sendPackets(L1ServerMessage.sm79);// \f1 아무것도 일어나지 않았습니다.
				return;
			}
			pc.cancelAbsoluteBarrier();
			int itemId = this.getItemId();
			switch(itemId){
			case 210036:case 30077:case 130032:// 유그드라 열매, 상아탑의 유그드라 열매, 유그드라 열매 [공성전]
				ugdra(pc, itemId);
				break;
			default:
				brave(pc, itemId);
				break;
			}
			pc.getInventory().removeItem(this, 1);
		}
	}
	
	private void brave(L1PcInstance pc, int item_id) {
		int time = 0;
		if (item_id == L1ItemId.POTION_OF_EMOTION_BRAVERY || item_id == 30073 || item_id == 130030) {// 용기의 물약
			time = 300;
		} else if (item_id == L1ItemId.B_POTION_OF_EMOTION_BRAVERY) {// 축복 용기의 물약
			time = 350;
		} else if (item_id == 41415) {// 복지 용기의 물약
			time = 1800;
		} else if (item_id == L1ItemId.ELBEN_WAFER || item_id == 30076 || item_id == 130031) { // 엘븐와퍼
			time = 480;
			if (pc.getSkill().hasSkillEffect(L1SkillId.STATUS_BRAVE)){ // 용기와는 중복 하지 않는다.
				pc.getSkill().killSkillEffectTimer(L1SkillId.STATUS_BRAVE);
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
				pc.getMoveState().setBraveSpeed(0);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.FOCUS_WAVE)){
				pc.getSkill().killSkillEffectTimer(L1SkillId.FOCUS_WAVE);
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
				pc.getMoveState().setBraveSpeed(0);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.HURRICANE)){
				pc.getSkill().killSkillEffectTimer(L1SkillId.HURRICANE);
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
				pc.getMoveState().setBraveSpeed(0);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.SAND_STORM)){
				pc.getSkill().killSkillEffectTimer(L1SkillId.SAND_STORM);
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
				pc.getMoveState().setBraveSpeed(0);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.DANCING_BLADES)){ // 댄싱하고는 중복하지않는다
				pc.getSkill().killSkillEffectTimer(L1SkillId.DANCING_BLADES);
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
				pc.getMoveState().setBraveSpeed(0);
			}
		} else if (item_id == 140068) { // 축복 엘븐와퍼
			time = 700;
			if (pc.getSkill().hasSkillEffect(L1SkillId.STATUS_BRAVE)){ // 용기 효과와는 중복 하지 않는다.
				pc.getSkill().killSkillEffectTimer(L1SkillId.STATUS_BRAVE);
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
				pc.getMoveState().setBraveSpeed(0);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.FOCUS_WAVE)){
				pc.getSkill().killSkillEffectTimer(L1SkillId.FOCUS_WAVE);
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
				pc.getMoveState().setBraveSpeed(0);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.HURRICANE)){
				pc.getSkill().killSkillEffectTimer(L1SkillId.HURRICANE);
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
				pc.getMoveState().setBraveSpeed(0);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.SAND_STORM)){
				pc.getSkill().killSkillEffectTimer(L1SkillId.SAND_STORM);
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
				pc.getMoveState().setBraveSpeed(0);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.DANCING_BLADES)){ // 댄싱하고는 중복하지않는다
				pc.getSkill().killSkillEffectTimer(L1SkillId.DANCING_BLADES);
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
				pc.getMoveState().setBraveSpeed(0);
			}
		} else if (item_id == 210110) { // 복지 엘븐와퍼
			time = 1800;
			if (pc.getSkill().hasSkillEffect(L1SkillId.STATUS_BRAVE)){ // 용기 효과와는 중복 하지 않는다.
				pc.getSkill().killSkillEffectTimer(L1SkillId.STATUS_BRAVE);
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
				pc.getMoveState().setBraveSpeed(0);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.FOCUS_WAVE)){
				pc.getSkill().killSkillEffectTimer(L1SkillId.FOCUS_WAVE);
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
				pc.getMoveState().setBraveSpeed(0);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.HURRICANE)){
				pc.getSkill().killSkillEffectTimer(L1SkillId.HURRICANE);
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
				pc.getMoveState().setBraveSpeed(0);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.SAND_STORM)){
				pc.getSkill().killSkillEffectTimer(L1SkillId.SAND_STORM);
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
				pc.getMoveState().setBraveSpeed(0);
			}
		} else if (item_id == 40031 || item_id == 30075 || item_id == 130029) {// 악마의 피
			time = 600;
		} else if (item_id == 210115) {// 복지 악마의 피
			time = 1800;
		} else if (item_id == 40733) { // 명예의 코인
			time = 600;
			if (pc.getSkill().hasSkillEffect(L1SkillId.STATUS_ELFBRAVE)){
				pc.getSkill().killSkillEffectTimer(L1SkillId.STATUS_ELFBRAVE);
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
				pc.getMoveState().setBraveSpeed(0);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.HOLY_WALK)){ // 호-리 워크와는 중복 하지 않는다
				pc.getSkill().killSkillEffectTimer(L1SkillId.HOLY_WALK);
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
				pc.getMoveState().setBraveSpeed(0);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.MOVING_ACCELERATION)){ // 무빙 악 세레이션과는 중복 하지 않는다
				pc.getSkill().killSkillEffectTimer(L1SkillId.MOVING_ACCELERATION);
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
				pc.getMoveState().setBraveSpeed(0);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.FOCUS_WAVE)){
				pc.getSkill().killSkillEffectTimer(L1SkillId.FOCUS_WAVE);
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
				pc.getMoveState().setBraveSpeed(0);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.HURRICANE)){
				pc.getSkill().killSkillEffectTimer(L1SkillId.HURRICANE);
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
				pc.getMoveState().setBraveSpeed(0);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.SAND_STORM)){
				pc.getSkill().killSkillEffectTimer(L1SkillId.SAND_STORM);
				pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
				pc.getMoveState().setBraveSpeed(0);
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.STATUS_FRUIT)){ // 유그드라열매와는 중복안됨
				pc.getSkill().killSkillEffectTimer(L1SkillId.STATUS_FRUIT);
				pc.getMoveState().setBraveSpeed(0);
			}
		}

		if (item_id == L1ItemId.ELBEN_WAFER || item_id == 140068 || item_id == 210110 || item_id == 30076 || item_id == 130031) { // 엘븐 와퍼
			int skilltime = pc.getSkill().getSkillEffectTimeSec(L1SkillId.STATUS_ELFBRAVE);
			skilltime += time;
			if (skilltime > 7200) {
				skilltime = 7200;
			}
			pc.sendPackets(new S_SkillBrave(pc.getId(), 3, skilltime), true);
			pc.broadcastPacket(new S_SkillBrave(pc.getId(), 3, 0), true);
			pc.getSkill().setSkillEffect(L1SkillId.STATUS_ELFBRAVE, skilltime * 1000);
			pc.getMoveState().setBraveSpeed(3);
		} else {
			int skilltime = pc.getSkill().getSkillEffectTimeSec(L1SkillId.STATUS_BRAVE);
			skilltime += time;
			if (skilltime > 7200) {
				skilltime = 7200;
			}
			pc.sendPackets(new S_SkillBrave(pc.getId(), 1, skilltime), true);
			pc.broadcastPacket(new S_SkillBrave(pc.getId(), 1, 0), true);
			pc.getSkill().setSkillEffect(L1SkillId.STATUS_BRAVE, skilltime * 1000);
			pc.getMoveState().setBraveSpeed(1);
		}
		pc.send_effect(751);
	}
	
	private void ugdra(L1PcInstance pc, int item_id) {// 유그드라열매
		int time = 0;
		if (item_id == 210036 || item_id == 30077 || item_id == 130032) {
			time = 480;
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.STATUS_BRAVE)) {
			pc.getSkill().killSkillEffectTimer(L1SkillId.STATUS_BRAVE);
			pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
			pc.getMoveState().setBraveSpeed(0);
		} else if (pc.getSkill().hasSkillEffect(L1SkillId.STATUS_FRUIT)) {
			pc.getSkill().killSkillEffectTimer(L1SkillId.STATUS_FRUIT);
			pc.broadcastPacketWithMe(new S_SkillBrave(pc.getId(), 0, 0), true);
			pc.getMoveState().setBraveSpeed(0);
		}
		
		int skilltime = pc.getSkill().getSkillEffectTimeSec(L1SkillId.STATUS_FRUIT);
		skilltime += time;
		if (skilltime > 7200) {
			skilltime = 7200;
		}
		boolean darkhorse = pc.isPassiveStatus(L1PassiveId.DARKHORSE);
		int fruitType = darkhorse ? 3 : 4;
		pc.sendPackets(new S_SkillBrave(pc.getId(), fruitType, skilltime), true);
		Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), fruitType, 0), true);
		pc.getSkill().setSkillEffect(L1SkillId.STATUS_FRUIT, skilltime * 1000);
		pc.getMoveState().setBraveSpeed(fruitType);
		pc.send_effect(7110);
	}
	
}

