package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.warehouse.PackageWarehouse;
import l1j.server.server.model.warehouse.WarehouseManager;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.StringUtil;

public class L1AllPresent implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1AllPresent();
	}

	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	
	private L1AllPresent() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer kwang = new StringTokenizer(arg);
			String item	= kwang.nextToken();
			int itemid	= Integer.parseInt(item, 10);
			int enchant	= Integer.parseInt(kwang.nextToken(), 10);
			int count	= Integer.parseInt(kwang.nextToken(), 10);
			L1Item tempItem = ItemTable.getInstance().getTemplate(itemid);
			if (tempItem == null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("존재하지 않는 아이템입니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(174), true), true);
				return false;
			}
			for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
				if (!player.isPrivateShop() && player.getNetConnection() != null) {
					PresentGive(tempItem, itemid, count, enchant, player);
				}
			}
			return true;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " 아이템ID 인챈트 갯수 로 입력해 주세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(175), true), true);
			return false;
		}
	}
	
	private void PresentGive(L1Item temp, int itemid, int count, int enchant, L1PcInstance target) {
		PackageWarehouse pwh = WarehouseManager.getInstance().getPackageWarehouse(target.getAccountName());
		if (temp.isMerge()) {
			L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
			item.setIdentified(true);
			item.setEnchantLevel(0);
			item.setCount(count);
			pwh.storeTradeItem(item);
			if (target != null) {
				//target.sendPackets(new S_SystemMessage("당신에게 " + item.getDescKr() + (item.getCount() > 0 ? (" (" + item.getCount() + ")") : StringUtil.EmptyString) + " 를 주었습니다."), true); // CHECKED OK
//AUTO SRM: 				target.sendPackets(new S_SystemMessage("당신에게 " + item.getDesc() + (item.getCount() > 0 ? (" (" + item.getCount() + ")") : StringUtil.EmptyString) + " 를 주었습니다.", true), true); // CHECKED OK
				target.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(176) + item.getDesc()  + (item.getCount() > 0 ? (" ("  + item.getCount()  + ")") : StringUtil.EmptyString)  + S_SystemMessage.getRefText(177), true), true);
//AUTO SRM: 				target.sendPackets(new S_SystemMessage("\\fY 부가 아이템 창고에서 선물을 찾을수있습니다."), true); // CHECKED OK
				target.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(178), true), true);
			}
		} else {
			L1ItemInstance item = null;
			int createCount;
			for (createCount = 0; createCount < count; createCount++) {
				item = ItemTable.getInstance().createItem(itemid);
				item.setIdentified(true);
				item.setEnchantLevel(enchant);
				pwh.storeTradeItem(item);
			}
			if (createCount > 0) {
				if (target != null) {
					//target.sendPackets(new S_SystemMessage("당신에게 " + item.getDescKr() + (item.getCount() > 0 ? (" (" + item.getCount() + ")") : StringUtil.EmptyString) + " 를 주었습니다."), true); // CHECKED OK
//AUTO SRM: 					target.sendPackets(new S_SystemMessage("당신에게 " + item.getDesc() + (item.getCount() > 0 ? (" (" + item.getCount() + ")") : StringUtil.EmptyString) + " 를 주었습니다.", true), true); // CHECKED OK
					target.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(176) + item.getDesc()  + (item.getCount() > 0 ? (" ("  + item.getCount()  + ")") : StringUtil.EmptyString)  + S_SystemMessage.getRefText(177), true), true);
//AUTO SRM: 					target.sendPackets(new S_SystemMessage("\\fY 부가 아이템 창고에서 선물을 찾을수있습니다."), true); // CHECKED OK
					target.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(178), true), true);
				}
			}
		}
	}
}


