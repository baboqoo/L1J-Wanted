package l1j.server.server.model.item.function;

import java.sql.Timestamp;

import l1j.server.Config;
import l1j.server.GameSystem.arca.L1Arca;
import l1j.server.GameSystem.arca.L1ArcaActivation;
import l1j.server.GameSystem.arca.L1ArcaGrade;
import l1j.server.server.Account;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1SkillStatus;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ArcaActivationInfo;
import l1j.server.server.templates.L1Item;

public class TamFruit extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public TamFruit(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			use((L1PcInstance) cha, packet.readD());
		}
	}
	
	void use(L1PcInstance pc, int targetId) {
		int day						= this.getItem().getEtcValue();
		long sysTime				= System.currentTimeMillis();
		
		Account	account				= pc.getAccount();
		L1Arca arca					= account.getArca();
		L1ArcaActivation activation	= arca.getActivations().get(targetId);
		if (activation == null) {
			activation = new L1ArcaActivation(targetId);
			arca.putActivation(activation);
		}
		
		int activeCount				= arca.getActiveCount(sysTime);
		
		// 이미 적용중인 대상 예약 추가
		if (activation.getEndTime() != null && activation.getEndTime().getTime() > sysTime) {
			if(activation.offerRemain(day, this.getItemId())){
				pc.sendPackets(new S_ArcaActivationInfo(account, 0), true);
				pc.send_effect(2028);
				pc.sendPackets(L1ServerMessage.sm3917);
				pc.getInventory().removeItem(this, 1);
			}
			return;
		}
		
		// 최대 적용 가능 개수
		if (activeCount >= Config.ALT.ARCA_MAX_ACTIVATE_CHARACTERS) {
			pc.sendPackets(L1ServerMessage.sm3904);
			return;
		}
		
		// 탐을 적용시킨다
		long endTime = sysTime + (86400000 * (long) day) + 10000;
		if (activation.getEndTime() != null) {
			activation.getEndTime().setTime(endTime);
		} else {
			activation.setEndTime(new Timestamp(endTime));
		}
		
		// 적용후의 정보
		int afterCount	= arca.getActiveCount(sysTime);
		long afterTime	= arca.getActiveTime();
		
		// 탐 적용 캐릭터가 변경되므로 능력치를 재 설정한다.
		L1SkillStatus skill = pc.getSkill();
		if (skill.hasSkillEffect(L1SkillId.TAM_FRUIT_1)) {
			skill.killSkillEffectTimer(L1SkillId.TAM_FRUIT_1);
			L1BuffUtil.arcaBuffDisable(pc, L1SkillId.TAM_FRUIT_1);
		} else if (skill.hasSkillEffect(L1SkillId.TAM_FRUIT_2)) {
			skill.killSkillEffectTimer(L1SkillId.TAM_FRUIT_2);
			L1BuffUtil.arcaBuffDisable(pc, L1SkillId.TAM_FRUIT_2);
		} else if (skill.hasSkillEffect(L1SkillId.TAM_FRUIT_3)) {
			skill.killSkillEffectTimer(L1SkillId.TAM_FRUIT_3);
			L1BuffUtil.arcaBuffDisable(pc, L1SkillId.TAM_FRUIT_3);
		} else if (skill.hasSkillEffect(L1SkillId.TAM_FRUIT_4)) {
			skill.killSkillEffectTimer(L1SkillId.TAM_FRUIT_4);
			L1BuffUtil.arcaBuffDisable(pc, L1SkillId.TAM_FRUIT_4);
		} else if (skill.hasSkillEffect(L1SkillId.TAM_FRUIT_5)) {
			skill.killSkillEffectTimer(L1SkillId.TAM_FRUIT_5);
			L1BuffUtil.arcaBuffDisable(pc, L1SkillId.TAM_FRUIT_5);
		}
		
		L1ArcaGrade grade = L1ArcaGrade.getGrade(afterCount);
		if (grade == null) {
			System.out.println(String.format("[TamFruit] GRADE_UNDEFINED : GRADE(%d)", afterCount));
			return;
		}
		L1BuffUtil.arcaBuffEnable(pc, grade, afterTime);
		pc.sendPackets(new S_ArcaActivationInfo(account, 0), true);
		pc.sendPackets(L1ServerMessage.sm3916);
		pc.send_effect(2028);
		pc.getInventory().removeItem(this, 1);
	}
}

