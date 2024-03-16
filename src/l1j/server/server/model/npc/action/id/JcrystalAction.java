package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.StringUtil;

public class JcrystalAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new JcrystalAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private JcrystalAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return ordealQuest(pc, s, obj);
	}
	
	private String ordealQuest(L1PcInstance pc, String s, L1Object obj){
		if (s.equalsIgnoreCase("a")) {
			final int[] item_ids = { 246, 247, 248, 249, 40660 };
			final int[] item_amounts = { 1, 1, 1, 1, 5 };
			L1ItemInstance item = null;
			for (int i = 0; i < item_ids.length; i++) {
				item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
				//pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().getDesc(), item.getItem().getDescKr()), true);
				pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
				pc.getQuest().setStep(L1Quest.QUEST_CRYSTAL, 1);
			}
			return StringUtil.EmptyString;
		} else if (s.equalsIgnoreCase("b")) {
			L1ItemInstance weapon = pc.getInventory().getEquippedWeapon();
			if (weapon != null && (weapon.getItemId() == 246 || weapon.getItemId() == 247 || weapon.getItemId() == 248 || weapon.getItemId() == 249)) {
				return "jcrystal5";
			}
			if (pc.getInventory().checkItem(40660)) {
				return "jcrystal4";
			}
			pc.getInventory().consumeItem(246, 1);
			pc.getInventory().consumeItem(247, 1);
			pc.getInventory().consumeItem(248, 1);
			pc.getInventory().consumeItem(249, 1);
			pc.getInventory().consumeItem(40620, 1);
			pc.getQuest().setStep(L1Quest.QUEST_CRYSTAL, 2);
			pc.getTeleport().start(32801, 32895, (short) 483, 4, true);
		} else if (s.equalsIgnoreCase("c")) {
			L1ItemInstance weapon = pc.getInventory().getEquippedWeapon();
			if (weapon != null && (weapon.getItemId() == 246 || weapon.getItemId() == 247 || weapon.getItemId() == 248 || weapon.getItemId() == 249)) {
				return "jcrystal5";
			}
			pc.getInventory().checkItem(40660);
			L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40660);
			int sc = l1iteminstance.getCount();
			if (sc > 0) {
				pc.getInventory().consumeItem(40660, sc);
			}
			pc.getInventory().consumeItem(246, 1);
			pc.getInventory().consumeItem(247, 1);
			pc.getInventory().consumeItem(248, 1);
			pc.getInventory().consumeItem(249, 1);
			pc.getInventory().consumeItem(40620, 1);
			pc.getQuest().setStep(L1Quest.QUEST_CRYSTAL, 0);
			pc.getTeleport().start(32736, 32800, (short) 483, 4, true);
		}
		return null;
	}
}

