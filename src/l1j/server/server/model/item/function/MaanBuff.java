package l1j.server.server.model.item.function;

import java.sql.Timestamp;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;

public class MaanBuff extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public MaanBuff(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			buff(pc, this.getItemId());
		}
	}
	
	private void buff(L1PcInstance pc, int itemId){
		Timestamp lastUsed	= this.getLastUsed();
		long currentTime	= System.currentTimeMillis();
		int delayEffect		= this.getItem().getDelayEffect();
		if (lastUsed == null || currentTime >= (lastUsed.getTime() + (delayEffect * 1000))) {
			if (itemId >= 410032 && itemId <= 410035 && !pc.getInventory().consumeItem(L1ItemId.GEMSTONE, 1000)) {
				pc.sendPackets(L1ServerMessage.sm337_GEMSTONE);
				return;
			}
			if (itemId >= 410036 && itemId <= 410038 && !pc.getInventory().consumeItem(L1ItemId.GEMSTONE, 2000)) {
				pc.sendPackets(L1ServerMessage.sm337_GEMSTONE);
				return;
			}
			int skillid=0;
			switch (itemId){
			case 410032:skillid = L1SkillId.FAFU_MAAN;break;
			case 410033:skillid = L1SkillId.LIND_MAAN;break;
			case 410034:skillid = L1SkillId.ANTA_MAAN;break;
			case 410035:skillid = L1SkillId.VALA_MAAN;break;
			case 410036:skillid = L1SkillId.BIRTH_MAAN;break;
			case 410037:skillid = L1SkillId.SHAPE_MAAN;break;
			case 410038:skillid = L1SkillId.LIFE_MAAN;break;
			case 410039:skillid = L1SkillId.LIFE_MAAN;break;
			case 450011:skillid = L1SkillId.BLACK_MAAN;break;
			case 450012:skillid = L1SkillId.ABSOLUTE_MAAN;break;
			}
			L1BuffUtil.skillAction(pc, skillid);
			if (getLastUsed() == null) {
				setLastUsed(new Timestamp(currentTime));
			} else {
				getLastUsed().setTime(currentTime);
			}
			if (itemId == 410039) {
				pc.getInventory().removeItem(this, 1);
			}
			pc.getInventory().saveItem(this, L1PcInventory.COL_SAVE_ALL);
		} else {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(((delayEffect - (currentTime - lastUsed.getTime()) / 1000) / 60) + "분 " + ((delayEffect - (currentTime - lastUsed.getTime()) / 1000) % 60) + "초 후에 사용할 수 있습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(100) + " " + ((delayEffect - (currentTime - lastUsed.getTime()) / 1000) / 60)  + " " + S_SystemMessage.getRefText(106) + " " + ((delayEffect - (currentTime - lastUsed.getTime()) / 1000) % 60) + " " + S_SystemMessage.getRefText(1306), true), true);
		}
	}
}


