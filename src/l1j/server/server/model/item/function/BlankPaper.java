package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.IntRange;

public class BlankPaper extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public BlankPaper(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (!pc.isWizard()) {
				pc.sendPackets(L1ServerMessage.sm264);// \f1당신의 클래스에서는 이 아이템은 사용할 수 없습니다.
				return;
			}
			int itemId			= getItemId();
			int blankSkillid	= packet.readC();
			if ((itemId == 40090 && blankSkillid <= 7)				// 스크롤(Lv1)로 레벨 1 이하의 마법
					|| (itemId == 40091 && blankSkillid <= 15)		// 스크롤(Lv2)로 레벨 2 이하의 마법
					|| (itemId == 40092 && blankSkillid <= 22)		// 스크롤(Lv3)로 레벨 3 이하의 마법
					|| (itemId == 40093 && blankSkillid <= 31)		// 스크롤(Lv4)로 레벨 4 이하의 마법
					|| (itemId == 40094 && blankSkillid <= 39)) {	// 스크롤(Lv5)로 레벨 5 이하의 마법
				L1ItemInstance spellsc = ItemTable.getInstance().createItem(40859 + blankSkillid);
				if (spellsc != null && pc.getInventory().checkAddItem(spellsc, 1) == L1Inventory.OK) {
					L1Skills l1skills = SkillsTable.getTemplate(blankSkillid);
					if (pc.getCurrentHp() + 1 < l1skills.getHpConsume() + 1) {
						pc.sendPackets(L1ServerMessage.sm279);// \f1HP가 부족해 마법을 사용할 수 있지 않습니다.
						return;
					}
					if (pc.getCurrentMp() < l1skills.getMpConsume()) {
						pc.sendPackets(L1ServerMessage.sm278);// \f1MP가 부족해 마법을 사용할 수 있지 않습니다.
						return;
					}
					if (l1skills.getItemConsumeId() != 0 && !pc.getInventory().consumeItem(l1skills.getItemConsumeId(), l1skills.getItemConsumeCount())) {// 재료가 필요
						pc.sendPackets(L1ServerMessage.sm299);// \f1마법을 영창하기 위한 재료가 충분하지 않습니다.
						return;
					}
					pc.setCurrentHp(pc.getCurrentHp() - l1skills.getHpConsume());
					pc.setCurrentMp(pc.getCurrentMp() - l1skills.getMpConsume());
					pc.setAlignment(IntRange.ensure(pc.getAlignment() + l1skills.getAlignment(), -32767, 32767));
					pc.getInventory().removeItem(this, 1);
					pc.getInventory().storeItem(spellsc);
					pc.sendPackets(new S_ServerMessage(403, spellsc.getItem().getDesc()), true);
				}
			} else {
				pc.sendPackets(L1ServerMessage.sm591);// \f1스크롤이 그렇게 강한 마법을 기록하려면 너무나 약합니다.
			}
		}
	}
}

