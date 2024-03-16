package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1CharacterConfig;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class PiersAction implements L1NpcIdAction {// 피어스
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new PiersAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private PiersAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return pierce(pc, s, obj);
	}
	
	private String pierce(L1PcInstance pc, String s, L1Object obj){
		L1NpcInstance npc = (L1NpcInstance) obj;
		String desc = npc.getNpcTemplate().getDesc();
		
		L1CharacterConfig config = pc.getConfig();
		if (config.PiersItemId == null) {
			config.PiersItemId	= new int[19];
			config.PiersEnchant	= new int[19];
		}
		
		if (s.equals("a")) { // 어둠의 힘을 원합니다.
			int[] list = new int[2];
			for (L1ItemInstance item : pc.getInventory().getItems()) {
				switch (item.getItemId()) {
				case 13:case 81:case 162:case 177:case 194:
					if (item.getEnchantLevel() == 8 && pc.getInventory().checkItem(L1ItemId.ADENA, 5000000)) {
						list[0] = 1;
					} else if (item.getEnchantLevel() == 9 && pc.getInventory().checkItem(L1ItemId.ADENA, 10000000)) {
						list[1] = 2;
					}
					break;
				default:break;
				}
			}
			int count = list[0] + list[1];
			switch (count) {
			case 1:return "piers03";
			case 2:return "piers02";
			case 3:return "piers01";
			default:return "piers04";
			}
		} else if (s.equals("0")) {
			pierce(pc, obj.getId(), desc, config.PiersItemId[0], config.PiersEnchant[0], config.PiersItemId[18]);
		} else if (s.equals("1")) {
			pierce(pc, obj.getId(), desc, config.PiersItemId[1], config.PiersEnchant[1], config.PiersItemId[18]);
		} else if (s.equals("2")) {
			pierce(pc, obj.getId(), desc, config.PiersItemId[2], config.PiersEnchant[2], config.PiersItemId[18]);
		} else if (s.equals("3")) {
			pierce(pc, obj.getId(), desc, config.PiersItemId[3], config.PiersEnchant[3], config.PiersItemId[18]);
		} else if (s.equals("4")) {
			pierce(pc, obj.getId(), desc, config.PiersItemId[4], config.PiersEnchant[4], config.PiersItemId[18]);
		} else if (s.equals("5")) {
			pierce(pc, obj.getId(), desc, config.PiersItemId[5], config.PiersEnchant[5], config.PiersItemId[18]);
		} else if (s.equals("6")) {
			pierce(pc, obj.getId(), desc, config.PiersItemId[6], config.PiersEnchant[6], config.PiersItemId[18]);
		} else if (s.equals("7")) {
			pierce(pc, obj.getId(), desc, config.PiersItemId[7], config.PiersEnchant[7], config.PiersItemId[18]);
		} else if (s.equals("8")) {
			pierce(pc, obj.getId(), desc, config.PiersItemId[8], config.PiersEnchant[8], config.PiersItemId[18]);
		} else if (s.equals("9")) {
			pierce(pc, obj.getId(), desc, config.PiersItemId[9], config.PiersEnchant[9], config.PiersItemId[18]);
		} else if (s.equals("A")) { // +7 파괴의 크로우
			pierceItemCheck(pc, obj.getId(), 8, 5000000, 30018, 7);
		} else if (s.equals("B")) { // +7 파괴의 이도류
			pierceItemCheck(pc, obj.getId(), 8, 5000000, 30019, 7);
		} else if (s.equals("C")) { // +8 파괴의 크로우
			pierceItemCheck(pc, obj.getId(), 9, 10000000, 30020, 8);
		} else if (s.equals("D")) { // +8 파괴의 이도류
			pierceItemCheck(pc, obj.getId(), 9, 10000000, 30021, 8);
		}
		return null;
	}
	
	private void pierce(L1PcInstance pc, int objid, String desc, int checkItem, int checkEnchant, int adena) {
		if (pc.getInventory().checkEnchant(checkItem, checkEnchant) && pc.getInventory().checkItem(L1ItemId.ADENA, adena)) {
			pc.getInventory().DeleteEnchant(checkItem, checkEnchant);
			pc.getInventory().consumeItem(L1ItemId.ADENA, adena);
			pc.getInventory().createItem(desc, pc.getConfig().PiersItemId[16], 1, pc.getConfig().PiersItemId[17]);
			pc.sendPackets(new S_NPCTalkReturn(objid, StringUtil.EmptyString), true);
			for (int i = 0; i < 20; i++) {
				pc.getConfig().PiersItemId[i] = 0;
				pc.getConfig().PiersEnchant[i] = 0;
			}
		} else {
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + "piers04"), true);											

			pc.sendPackets(new S_NPCTalkReturn(objid, "piers04"), true);
		}
	}
	
	private void pierceItemCheck(L1PcInstance pc, int objid, int enchant, int adena, int newItem, int newItemEnchant) {
		int listcount = 0;
		String[] list = new String[10];
		for (int i = 0; i < 10; i++) {
			list[i] = StringUtil.EmptyOneString;
		}
		for (L1ItemInstance item : pc.getInventory().getItems()) {
			switch (item.getItemId()) {
			case 13:case 81:case 162:case 177:case 194:
				if (item.getEnchantLevel() == enchant && pc.getInventory().checkItem(L1ItemId.ADENA, adena)) {
					list[listcount] = StringUtil.PlusString + item.getEnchantLevel() + StringUtil.EmptyOneString + item.getItem().getDesc();
					pc.getConfig().PiersItemId[listcount] = item.getItemId();
					pc.getConfig().PiersEnchant[listcount] = enchant;
					listcount++;
				}
				break;
			default:break;
			}
		}
		if (listcount == 0 || !pc.getInventory().checkItem(L1ItemId.ADENA, adena)){
			
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + "piers04"), true);											
			
			pc.sendPackets(new S_NPCTalkReturn(objid, "piers04"), true);
			return;
		}
		pc.getConfig().PiersItemId[16] = newItem;
		pc.getConfig().PiersItemId[17] = newItemEnchant;
		pc.getConfig().PiersItemId[18] = adena;

		if (pc.isGm())
			pc.sendPackets(new S_SystemMessage("Dialog " + "piers00"), true);											

		pc.sendPackets(new S_NPCTalkReturn(objid, "piers00", list), true);
	}
}

