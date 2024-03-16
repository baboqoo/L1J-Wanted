package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.StringUtil;

public class L1Present implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Present();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Present() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String name = st.nextToken();
			String desc = st.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(name);

			if (target == null) {
				target = CharacterTable.getInstance().restoreCharacter(name);

				if (target == null) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("존재하지 않는 캐릭터입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(535), true), true);
					return false;
				}
				target.getInventory().loadItems();
			}

			int count = 1;
			if (st.hasMoreTokens()) {
				count = Integer.parseInt(st.nextToken());
			}
			int enchant = 0;
			if (st.hasMoreTokens()) {
				enchant = Integer.parseInt(st.nextToken());
			}
			int itemid = 0;
			int Attrenchant = 0;
			if (st.hasMoreTokens()) {
				Attrenchant = Integer.parseInt(st.nextToken());
			}
			int bless = 0;
			if (st.hasMoreTokens()) {
				bless = Integer.parseInt(st.nextToken());
			}
			try {
				itemid = Integer.parseInt(desc);
			} catch (NumberFormatException e) {
				itemid = ItemTable.getInstance().findItemIdByDescWithoutSpace(desc);
				if (itemid == 0) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("해당 아이템이 발견되지 않았습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(536), true), true);
					return false;
				}
			}
			L1Item temp = ItemTable.getInstance().getTemplate(itemid);
			if (temp != null) {
				if (temp.isMerge()) {
					L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
					item.setEnchantLevel(0);
					item.setCount(count);
					if (target.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
						target.getInventory().storeItem(item);
						target.sendPackets(new S_Effect(pc.getId(), 4856), true);
//AUTO SRM: 						target.sendPackets(new S_SystemMessage("\\aD[선물] " + item.getLogNameRef() + " 획득", true), true); // CHECKED OK
						target.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(537) + item.getLogNameRef()  + S_SystemMessage.getRefText(538), true), true);
//AUTO SRM:                         pc.sendPackets(new S_SystemMessage("\\aD["+target.getName()+"] "+ item.getLogNameRef() + " (ID:" + itemid + ") 보냄", true), true); // CHECKED OK
                        pc.sendPackets(new S_SystemMessage("\\aD[" + target.getName() + "] " + item.getLogNameRef()  + " (ID:" + itemid  + S_SystemMessage.getRefText(539), true), true);
					}
				} else {
					L1ItemInstance item = null;
					int createCount;
					for (createCount = 0; createCount < count; createCount++) {
						item = ItemTable.getInstance().createItem(itemid);
						item.setEnchantLevel(enchant);
						item.setAttrEnchantLevel(Attrenchant);
						//if (target.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
							target.getInventory().storeItem(item);
							if (bless == 129) {
								item.setBless(bless);
								target.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
								target.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
							}
					//	} else {
					//		break;
					//	}
					}
					if (createCount > 0) {
						target.sendPackets(new S_Effect(pc.getId(), 4856), true);
//AUTO SRM: 						target.sendPackets(new S_SystemMessage("\\aD[선물] +" + enchant + StringUtil.EmptyOneString + item.getLogNameRef() + " (" + count + "개) 획득", true), true); // CHECKED OK
						target.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(540) + enchant  + StringUtil.EmptyOneString  + item.getLogNameRef()  + " (" + count  + S_SystemMessage.getRefText(541), true), true);
//AUTO SRM:         				pc.sendPackets(new S_SystemMessage("\\aD["+target.getName()+"] +" + enchant + StringUtil.EmptyOneString+ temp.getDesc()+"(ID:" + itemid + ") "+ count +"개 보냄", true), true); // CHECKED OK
        				pc.sendPackets(new S_SystemMessage("\\aD[" + target.getName() + "] +" + enchant  + StringUtil.EmptyOneString + temp.getDesc() + "(ID:" + itemid  + ") " + count  + S_SystemMessage.getRefText(542), true), true);
					}
				}
				return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("지정 ID의 아이템은 존재하지 않습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(543), true), true);
			return false;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".[선물] [캐릭] [아이템ID] [갯수] [인챈] [속성] [봉인129]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(544), true), true);
			return false;
		}
	}
}

