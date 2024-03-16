package l1j.server.server.model;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.GameServer;
import l1j.server.server.command.L1Commands;
import l1j.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.warehouse.PrivateWarehouse;
import l1j.server.server.model.warehouse.WarehouseManager;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.trade.S_ExchangeItemListNoti;
import l1j.server.server.serverpackets.trade.S_TradeStatus;
import l1j.server.server.serverpackets.trade.S_ExchangeItemListNoti.eExchangeType;
import l1j.server.server.templates.L1Command;
import l1j.server.server.utils.StringUtil;
//import manager.Manager;  // MANAGER DISABLED

public class L1Trade {
	private static Logger _log = Logger.getLogger(L1Trade.class.getName());

	public L1Trade() {
	}

	public void TradeAddItem(L1PcInstance player, int itemobjid, int itemcount) {
		L1PcInstance trading_partner = (L1PcInstance) L1World.getInstance().findObject(player.getTradeID());
		L1ItemInstance l1iteminstance = player.getInventory().getItem(itemobjid);
		if (l1iteminstance != null && trading_partner != null && !l1iteminstance.isEquipped()) {
			if (l1iteminstance.getCount() < itemcount || 0 >= itemcount) {// 허상버그 관련추가
				TradeCancel(player);
				return;
			}

			player.getInventory().tradeItem(l1iteminstance, itemcount, player.getTradeWindowInventory());

			if (l1iteminstance.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE || l1iteminstance.getItemId() == L1ItemId.LOW_CHARACTER_TRADE) {
				int lv = player.getLevel();
				long currentLvExp = ExpTable.getExpByLevel(lv);
				long nextLvExp = ExpTable.getExpByLevel(lv + 1);
				double neededExp = nextLvExp - currentLvExp;
				double currentExp = player.getExp() - currentLvExp;
				int per = (int) ((currentExp / neededExp) * 100.0);
				
				String itemName = l1iteminstance.getViewName() + StringUtil.EmptyOneString + player.getClassNameEn() + "[" + Integer.toString(player.getLevel()) + "]";
				player.sendPackets(new S_ExchangeItemListNoti(l1iteminstance, itemName, itemcount, eExchangeType.EXC_SELF, player), true);
				trading_partner.sendPackets(new S_ExchangeItemListNoti(l1iteminstance, itemName, itemcount, eExchangeType.EXC_OPPOSITE, trading_partner), true);
				player.sendPackets(new S_SystemMessage("--------------------------------------------------"), true);
//AUTO SRM: 				player.sendPackets(new S_SystemMessage("캐릭터 판매중."), true); // CHECKED OK
				player.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1111), true), true);
//AUTO SRM: 				player.sendPackets(new S_SystemMessage("거래후 거래한금액은 이 계정창고로 들어갑니다."), true); // CHECKED OK
				player.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1112), true), true);
				player.sendPackets(new S_SystemMessage("--------------------------------------------------"), true);

				trading_partner.sendPackets(new S_SystemMessage("--------------------------------------------------"), true);
//AUTO SRM: 				trading_partner.sendPackets(new S_SystemMessage("상태방 캐릭정보."), true); // CHECKED OK
				trading_partner.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1113), true), true);
//AUTO SRM: 				trading_partner.sendPackets(new S_SystemMessage("클래스: [" + player.getClassName() + "] 레벨: [" + Integer.toString(player.getLevel()) + StringUtil.PeriodString + per + "%] 엘릭서: [" + player.getElixirStats() + "]"), true); // CHECKED OK
				trading_partner.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1114) + player.getClassNameEn()  + S_SystemMessage.getRefText(1115) + Integer.toString(player.getLevel())  + StringUtil.PeriodString  + per  + S_SystemMessage.getRefText(1116) + player.getElixirStats()  + "]", true), true);
				trading_partner.sendPackets(new S_SystemMessage("--------------------------------------------------"), true);
				
				try {
					L1Command command = L1Commands.get("checkcharacter");
					Class<?> cls = Class.forName(complementClassName(command.getExecutorClassName()));
					L1CommandExecutor exe = (L1CommandExecutor) cls.getMethod("getInstance").invoke(null);
					String param = ((L1PcInstance)player).getName() + " inv";
					exe.execute(trading_partner, "checkcharacter", param);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			} else {
				player.sendPackets(new S_ExchangeItemListNoti(l1iteminstance, itemcount, eExchangeType.EXC_SELF, player), true);
				trading_partner.sendPackets(new S_ExchangeItemListNoti(l1iteminstance, itemcount, eExchangeType.EXC_OPPOSITE, trading_partner), true);
			}
		}
	}
	
	private String complementClassName(String className) {
		if (className.contains(StringUtil.PeriodString))
			return className;
		if (className.contains(StringUtil.CommaString))
			return className;
		return "l1j.server.server.command.executor." + className;
	}

	public void doCharacterTrade(L1PcInstance player, boolean characterTrade1, L1PcInstance target, boolean characterTrade2) {
		if (player.getNetConnection() == null || target.getNetConnection() == null) {
//AUTO SRM: 			player.sendPackets(new S_SystemMessage("거래 대상이 비정상 접속중입니다."), true); // CHECKED OK
			player.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1117), true), true);
//AUTO SRM: 			target.sendPackets(new S_SystemMessage("거래 대상이 비정상 접속중입니다."), true); // CHECKED OK
			target.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1117), true), true);

			TradeCancel(player);
			return;
		}
		if (characterTrade1 && target.getAccount().countCharacters() >= target.getAccount().getCharSlot()) {
//AUTO SRM: 			player.sendPackets(new S_SystemMessage("거래 대상에게 빈 캐릭터 슬롯이 없습니다."), true); // CHECKED OK
			player.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1118), true), true);
//AUTO SRM: 			target.sendPackets(new S_SystemMessage("빈 캐릭터 슬롯이 없습니다. 캐릭터 슬롯을 확보하고 다시 시도해주시기 바랍니다."), true); // CHECKED OK
			target.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1119), true), true);

			TradeCancel(player);
			return;
		}

		if (characterTrade2 && player.getAccount().countCharacters() >= player.getAccount().getCharSlot()) {
//AUTO SRM: 			target.sendPackets(new S_SystemMessage("거래 대상에게 빈 캐릭터 슬롯이 없습니다."), true); // CHECKED OK
			target.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1118), true), true);
//AUTO SRM: 			player.sendPackets(new S_SystemMessage("빈 캐릭터 슬롯이 없습니다. 캐릭터 슬롯을 확보하고 다시 시도해주시기 바랍니다."), true); // CHECKED OK
			player.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1119), true), true);

			TradeCancel(player);
			return;
		}

		if (characterTrade1) {
			PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(player.getAccountName());
			if (warehouse == null) {
				TradeCancel(player);
				return;
			}

			for (L1ItemInstance item : target.getTradeWindowInventory().getItems()) {
				if (warehouse.checkAddItemToWarehouse(item, item.getCount()) == L1Inventory.SIZE_OVER) {
					target.sendPackets(L1ServerMessage.sm75);// \f1상대가 물건을너무 가지고 있어거래할 수 없습니다.
					TradeCancel(player);
					return;
				}
			}
		}

		if (characterTrade2) {
			PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(target.getAccountName());

			if (warehouse == null) {
				TradeCancel(player);
				return;
			}

			for (L1ItemInstance item : player.getTradeWindowInventory().getItems()) {
				if (warehouse.checkAddItemToWarehouse(item, item.getCount()) == L1Inventory.SIZE_OVER) {
					player.sendPackets(L1ServerMessage.sm75);// \f1상대가 물건을 너무 가지고 있어거래할 수없습니다.
					TradeCancel(player);
					return;
				}
			}
		}

		if (characterTrade1) {
			PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(player.getAccountName());

			while (target.getTradeWindowInventory().getItems().size() > 0) {
				L1ItemInstance item = (L1ItemInstance) target.getTradeWindowInventory().getItems().get(0);
				if (item.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE || item.getItemId() == L1ItemId.LOW_CHARACTER_TRADE) {
					target.getTradeWindowInventory().consumeItem(item.getItemId(), item.getCount());
				} else {
					target.getTradeWindowInventory().tradeItem(item, item.getCount(), warehouse);
				}
			}

			if (!characterTrade2) {
				while (player.getTradeWindowInventory().getItems().size() > 0) {
					L1ItemInstance item = (L1ItemInstance) player.getTradeWindowInventory().getItems().get(0);
					if (item.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE || item.getItemId() == L1ItemId.LOW_CHARACTER_TRADE) {
						player.getTradeWindowInventory().consumeItem(item.getItemId(), item.getCount());
					} else {
						player.getTradeWindowInventory().tradeItem(item, item.getCount(), player.getInventory());
					}
				}
			}
		}

		if (characterTrade2) {
			PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(target.getAccountName());
			while (player.getTradeWindowInventory().getItems().size() > 0) {
				L1ItemInstance item = (L1ItemInstance) player.getTradeWindowInventory().getItems().get(0);
				if (item.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE || item.getItemId() == L1ItemId.LOW_CHARACTER_TRADE) {
					player.getTradeWindowInventory().consumeItem(item.getItemId(), item.getCount());
				} else {
					player.getTradeWindowInventory().tradeItem(item, item.getCount(), warehouse);
				}
			}

			if (!characterTrade1) {
				while (target.getTradeWindowInventory().getItems().size() > 0) {
					L1ItemInstance item = (L1ItemInstance) target.getTradeWindowInventory().getItems().get(0);
					if (item.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE || item.getItemId() == L1ItemId.LOW_CHARACTER_TRADE) {
						target.getTradeWindowInventory().consumeItem(item.getItemId(), item.getCount());
					} else {
						target.getTradeWindowInventory().tradeItem(item, item.getCount(), target.getInventory());
					}
				}
			}
		}

		player.sendPackets(S_TradeStatus.OK);
		target.sendPackets(S_TradeStatus.OK);
		player.setTradeOk(false);
		target.setTradeOk(false);
		player.setTradeReady(false);
		target.setTradeReady(false);
		player.setTradeID(0);
		target.setTradeID(0);
		
		player.getLight().turnOnOffLight();
		target.getLight().turnOnOffLight();

		String playerAccountName = player.getAccountName();
		String targetAccountName = target.getAccountName();

		if (characterTrade1) {
			player.setAccountName(targetAccountName);
			try {
				CharacterTable.getInstance().updateCharacterAccount(player);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}

		if (characterTrade2) {
			target.setAccountName(playerAccountName);
			try {
				CharacterTable.getInstance().updateCharacterAccount(target);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}

		GameServer.disconnectChar(player);
		GameServer.disconnectChar(target);
	}

	public void TradeOK(L1PcInstance player) {
		int cnt;
		L1PcInstance trading_partner = (L1PcInstance) L1World.getInstance().findObject(player.getTradeID());
		if (trading_partner != null && trading_partner.getTradeID() == player.getId()) {
			List<?> player_tradelist = player.getTradeWindowInventory().getItems();
			int player_tradecount = player.getTradeWindowInventory().getSize();
			List<?> trading_partner_tradelist = trading_partner.getTradeWindowInventory().getItems();
			int trading_partner_tradecount = trading_partner.getTradeWindowInventory().getSize();
			L1ItemInstance l1iteminstance1 = null, l1iteminstance2 = null;

			// 캐릭 교환인지 본다.
			boolean characterTrade1 = false, characterTrade2 = false;

			for (cnt = 0; cnt < player_tradecount; cnt++) {
				l1iteminstance1 = (L1ItemInstance) player_tradelist.get(cnt);
				if (l1iteminstance1.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE || l1iteminstance1.getItemId() == L1ItemId.LOW_CHARACTER_TRADE) {
					characterTrade1 = true;
					break;
				}
			}
			for (cnt = 0; cnt < trading_partner_tradecount; cnt++) {
				l1iteminstance2 = (L1ItemInstance) trading_partner_tradelist.get(cnt);
				if (l1iteminstance2.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE || l1iteminstance2.getItemId() == L1ItemId.LOW_CHARACTER_TRADE) {
					characterTrade2 = true;
					break;
				}
			}

			if (characterTrade1 || characterTrade2) {
				doCharacterTrade(player, characterTrade1, trading_partner, characterTrade2);
			} else {
				for (cnt = 0; cnt < player_tradecount; cnt++) {
					l1iteminstance1 = (L1ItemInstance) player_tradelist.get(0);
					player.getTradeWindowInventory().tradeItem(l1iteminstance1, l1iteminstance1.getCount(), trading_partner.getInventory());
					//Manager.getInstance().TradeAppend(l1iteminstance1.getDescKr(), player.getName(), trading_partner.getName()); // MANAGER DISABLED
					/** 로그파일저장 **/
					LoggerInstance.getInstance().addTrade(true, player, trading_partner, l1iteminstance1, l1iteminstance1.getCount());

				}
				for (cnt = 0; cnt < trading_partner_tradecount; cnt++) {
					l1iteminstance2 = (L1ItemInstance) trading_partner_tradelist.get(0);
					trading_partner.getTradeWindowInventory().tradeItem(l1iteminstance2, l1iteminstance2.getCount(), player.getInventory());
					//Manager.getInstance().TradeAppend(l1iteminstance2.getDescKr(), trading_partner.getName(), player.getName()); // MANAGER DISABLED
					/** 로그파일저장 **/
					LoggerInstance.getInstance().addTrade(true, trading_partner, player, l1iteminstance2, l1iteminstance2.getCount());

				}

				player.sendPackets(S_TradeStatus.OK);
				trading_partner.sendPackets(S_TradeStatus.OK);
				player.setTradeOk(false);
				trading_partner.setTradeOk(false);
				player.setTradeReady(false);
				trading_partner.setTradeReady(false);
				player.setTradeID(0);
				trading_partner.setTradeID(0);
				player.getLight().turnOnOffLight();
				trading_partner.getLight().turnOnOffLight();
			}
		} else {
			TradeCancel(player);
		}
	}

	public void TradeCancel(L1PcInstance player) {
		int cnt;
		L1PcInstance trading_partner = (L1PcInstance) L1World.getInstance().findObject(player.getTradeID());
		
		{
			List<?> player_tradelist = player.getTradeWindowInventory().getItems();
			int player_tradecount = player.getTradeWindowInventory().getSize();
			L1ItemInstance l1iteminstance = null;
			for (cnt=0; cnt<player_tradecount; cnt++) {
				l1iteminstance = (L1ItemInstance) player_tradelist.get(0);
				player.getTradeWindowInventory().tradeItem(l1iteminstance, l1iteminstance.getCount(), player.getInventory());
				/** 로그파일저장 **/
				LoggerInstance.getInstance().addTrade(false, player, trading_partner, l1iteminstance, l1iteminstance.getCount());
			}
			player.sendPackets(S_TradeStatus.CANCEL);
			player.setTradeOk(false);
			player.setTradeID(0);
			player.setTradeReady(false);
		}

		if (trading_partner != null && trading_partner.getTradeID() == player.getId()) {
			List<?> trading_partner_tradelist = trading_partner.getTradeWindowInventory().getItems();
			int trading_partner_tradecount = trading_partner.getTradeWindowInventory().getSize();
			L1ItemInstance l1iteminstance = null;
			for (cnt=0; cnt<trading_partner_tradecount; cnt++) {
				l1iteminstance = (L1ItemInstance) trading_partner_tradelist.get(0);
				trading_partner.getTradeWindowInventory().tradeItem(l1iteminstance, l1iteminstance.getCount(), trading_partner.getInventory());
				/** 로그파일저장 **/
				LoggerInstance.getInstance().addTrade(false, trading_partner, player, l1iteminstance, l1iteminstance.getCount());
			}
			trading_partner.sendPackets(S_TradeStatus.CANCEL);
			trading_partner.setTradeOk(false);
			trading_partner.setTradeID(0);
			trading_partner.setTradeReady(false);
		}
	}
}

