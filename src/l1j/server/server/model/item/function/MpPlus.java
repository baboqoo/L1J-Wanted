package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;

public class MpPlus extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public MpPlus(L1Item item) {
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
			int delay_id = 0;
			delay_id = ((L1EtcItem) this.getItem()).getDelayId();
			if (delay_id != 0 && pc.hasItemDelay(delay_id)) {
				return;// 지연 설정 있어
			}
			switch(this.getItemId()){
			case 40066:	plus(pc, 7 + CommonUtil.random(6));		break;// 송편
			case 41413:	plus(pc, 7 + CommonUtil.random(6));		break;// 월병
			case 40067:	plus(pc, 8 + CommonUtil.random(10));	break;// 쑥송편
			case 41414:	plus(pc, 15 + CommonUtil.random(16));	break;// 복월병
			case 410002:plus(pc, 44);							break;// 빛나는 나뭇잎
			case 40735:	plus(pc, 60);							break;// 용기의 코인
			case 40042:	plus(pc, 50);							break;// 정신력의 물약
			case 41404:	plus(pc, 80 + CommonUtil.random(21));	break;// 쿠작의 영약
			case 41412:	plus(pc, 5 + CommonUtil.random(16));	break;// 금쫑즈
			case 60414:	plus(pc, 8 + CommonUtil.random(10));	break;// 축복의 마나 포션
			case 500220:case 43054:witchPotion(pc);				break;
			}
			L1ItemDelay.onItemUse(pc, this);// 아이템 지연 개시
		}
	}
	
	private void plus(L1PcInstance pc, int mp){
		pc.setCurrentMp(pc.getCurrentMp() + mp);
		pc.sendPackets(L1ServerMessage.sm338_MP);// 당신의%0가 회복되어 갑니다.
		pc.getInventory().removeItem(this, 1);
	}
	
	private void witchPotion(L1PcInstance pc){
		if (pc.getSkill().hasSkillEffect(L1SkillId.STATUS_WITCH_POTION)) {
			int time = pc.getSkill().getSkillEffectTimeSec(L1SkillId.STATUS_WITCH_POTION);
			//pc.sendPackets(new S_SystemMessage(String.format("%d초 후에 사용할 수 있습니다.", time)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(97), String.valueOf(time)), true);
			return;
		}
		pc.setCurrentMp(pc.getCurrentMp() + getItem().getEtcValue());
		pc.sendPackets(L1ServerMessage.sm176);
		pc.getSkill().setSkillEffect(L1SkillId.STATUS_WITCH_POTION, 60000 * 20);
		pc.getInventory().removeItem(this, 1);
	}
}


