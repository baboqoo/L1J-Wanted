package l1j.server.server.model.item.function;

import l1j.server.server.GameServerSetting;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Cooking;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_CurseBlind;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Item;

public class Food extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public Food(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int itemId = this.getItemId();
			switch(itemId){
			case 40056:case 40057:case 40059:case 40060:case 40061:case 40062:case 40063:case 40064:case 40065:
			case 40069:case 40072:case 40073:case 41266:case 41267:case 41274:case 41275:case 41276:case 41252:
			case 49040:case 49041:case 49042:case 49043:case 49044:case 49045:case 49046:case 49047:case 140061:
			case 140062:case 140065:case 140069:case 140072:case 410056:case 210039:case 30085:case 130020:
				// XXX 음식 마다의 만복도가 차이가 나지 않는다
				if (itemId == 40057) {// 괴물눈 고기
					floatingEye(pc);
				}
				if (pc.getFood() < GameServerSetting.MAX_FOOD_VALUE) {
					pc.setFood(pc.getFood() + 10);
					int foodvolume = (this.getItem().getFoodVolume() / 10);
					pc.addFood(foodvolume <= 0 ? 5 : foodvolume);
					pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.getFood()), true);
					pc.sendPackets(new S_ServerMessage(76, this.getItem().getDesc()), true);
				}
				break;
			default:
				if (L1Cooking.isCooking(itemId) || L1Cooking.isSoup(itemId)) {
					L1Cooking.useCookingItem(pc, this);
				}
				return;
			}
			pc.getInventory().removeItem(this, 1);
		}
	}
	
	void floatingEye(L1PcInstance pc) {
		pc.getSkill().setSkillEffect(L1SkillId.STATUS_FLOATING_EYE, 0);
		if (pc.isBlind()) {
			pc.sendPackets(S_CurseBlind.BLIND_FLOATING_EYE);
			pc.showFloatingEyeToInvis();
		}
	}
}

