package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.MagicDollInfoTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;

public class L1CreateItem implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1CreateItem();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1CreateItem() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null) {
				return false;
			}
			StringTokenizer st = new StringTokenizer(arg);
			String desc = st.nextToken();
			int count = 1;
			if (st.hasMoreTokens()) {
				count = Integer.parseInt(st.nextToken());
			}
			
			int enchant = 0;
			if (st.hasMoreTokens()) {
				enchant = Integer.parseInt(st.nextToken());
			}
			
			int attrenchant = 0;
			if (st.hasMoreTokens()) {
				attrenchant = Integer.parseInt(st.nextToken());
			}
			
			int isId = 1;
			if (st.hasMoreTokens()) {
				isId = Integer.parseInt(st.nextToken());
			}
			
			int dollablity = 0;
			if (st.hasMoreTokens()) {
				dollablity = Integer.parseInt(st.nextToken());
			}
			
			int itemid = 0;
			try {
				itemid = Integer.parseInt(desc);
			} catch (NumberFormatException e) {
				itemid = ItemTable.getInstance().findItemIdByDescWithoutSpace(desc);
				if (itemid == 0) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("해당 아이템이 발견되지 않습니다. "), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(200), true), true);
					return false;
				}
			}
			L1Item temp = ItemTable.getInstance(). getTemplate(itemid);
			if (temp != null) {
				if (temp.isMerge()) {
					L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
					item.setEnchantLevel(0);
					item.setCount(count);
					if (isId == 1) {
						item.setIdentified(true);
					}
					pc.getInventory().storeItem(item);
					//pc.sendPackets(new S_SystemMessage("\\aD" + item.getLogNameRef() + " (ID " + itemid + ") 획득", true), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(107), item.getLogNameRef(), String.valueOf(itemid)), true);
					return true;
				}
				L1ItemInstance item = null;
				int createCount;
				for (createCount = 0; createCount < count; createCount++) {
					item = ItemTable.getInstance().createItem(itemid);
					item.setEnchantLevel(enchant);
					item.setAttrEnchantLevel(attrenchant);
					if (isId == 1) {
						item.setIdentified(true);
					}
					if (dollablity > 0) {
						item.setPotential(MagicDollInfoTable.getPotential(dollablity));
					}
					pc.getInventory().storeItem(item);
				}
				if (createCount > 0) {
//AUTO SRM:                     pc.sendPackets(new S_SystemMessage("\\aA▶[+" + enchant + "] \\aG[" + item.getLogNameRef() + "]\\aA(ID:"+ itemid + ") [생성]◀", true), true); // CHECKED OK
                    pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(326) + enchant  + "] \\aG[" + item.getLogNameRef()  + "]\\aA(ID:" + itemid  + S_SystemMessage.getRefText(327), true), true);
                    return true;
				}
				return false;
			} else {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("지정 ID의 아이템은 존재하지 않습니다"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(28), true), true);
				return false;
			}
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".템 [이름] [갯수] [인챈] [속성1~20] [확인0~1] [인형잠재력1~141]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(328), true), true);
			return false;
		}
	}
}


