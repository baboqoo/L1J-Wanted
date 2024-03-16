package l1j.server.IndunSystem.minigame;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.common.data.ChatType;
import l1j.server.server.datatables.ChatLogTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.CommonUtil;

public class L1Gambling {
	private static final Logger _log = Logger.getLogger(L1Gambling.class.getName());

	public void Gambling(L1PcInstance player, int bettingmoney) {
		try {
			for (L1Object l1object : L1World.getInstance().getObject()) {
				if (l1object instanceof L1NpcInstance) {
					L1NpcInstance Npc = (L1NpcInstance) l1object;
					if (Npc.getNpcTemplate().getNpcId() == 400064) {
						L1NpcInstance dealer = Npc;
						if (bettingmoney >= 500000) { // 추가
							//String chat = "배팅금액 초과입니다 꿀꺽!";
							String chat = S_SystemMessage.getRefText(1391);
							player.sendPackets(new S_NpcChatPacket(dealer, chat, ChatType.CHAT_NORMAL), true);
							player.broadcastPacket(new S_NpcChatPacket(dealer, chat, ChatType.CHAT_NORMAL), true);
						}
						//String chat = player.getName() + "님 " + bettingmoney + "원 배팅하셨습니다."; // CHECKED OK
						String chat = player.getName() + S_SystemMessage.getRefText(1392) + bettingmoney + S_SystemMessage.getRefText(1393);
						player.sendPackets(new S_NpcChatPacket(dealer, chat, ChatType.CHAT_NORMAL), true);
						player.broadcastPacket(new S_NpcChatPacket(dealer, chat, ChatType.CHAT_NORMAL), true);
						Thread.sleep(1000);
						Thread.sleep(1000);
						Thread.sleep(1000);
						//String chat2 = "홀 or 짝 2배//// 1 ~ 6 숫자 3배///홀짝or숫자를 입력해주세요";
						String chat2 = S_SystemMessage.getRefText(1394);
						player.sendPackets(new S_NpcChatPacket(dealer, chat2, ChatType.CHAT_NORMAL), true);
						player.broadcastPacket(new S_NpcChatPacket(dealer, chat2, ChatType.CHAT_NORMAL), true);
						player.getGambling().setGamblingMoney(bettingmoney);
						player.getGambling().setGambling(true);
					}
				}
			}
		} catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	public void Gambling2(L1PcInstance pc, String chatText, int type) {
		ChatLogTable.getInstance().storeChat(pc, null, chatText, ChatType.CHAT_NORMAL);
		pc.sendPackets(new S_SystemMessage(chatText), true);
		
		for (L1PcInstance listner : L1World.getInstance().getRecognizePlayer(pc)) {		
			listner.sendPackets(new S_SystemMessage(chatText), true);		
		}
		try {
			for (L1Object l1object : L1World.getInstance().getObject()) {
				if (l1object instanceof L1NpcInstance) {
					L1NpcInstance Npc = (L1NpcInstance) l1object;
					if (Npc.getNpcTemplate().getNpcId() == 400064) {
						L1NpcInstance dealer = Npc;
						/*
						String chat9 = pc.getName() +  "님 맞추셧습니다." + pc.getGambling().getGamblingMoney() * 2 + "원 입금했습니다.";
						String chat10 = pc.getName() + "님 맞추셧습니다." + pc.getGambling().getGamblingMoney() * 3 + "원 입금했습니다.";
						String chat11 = pc.getName() + "님 틀리셧습니다 뽀찌는없습니다 즐.";
						*/  // CHECKED OK
						String chat9 = pc.getName() +  S_SystemMessage.getRefText(1395) + pc.getGambling().getGamblingMoney() * 2 + S_SystemMessage.getRefText(1396);
						String chat10 = pc.getName() + S_SystemMessage.getRefText(1395) + pc.getGambling().getGamblingMoney() * 3 + S_SystemMessage.getRefText(1396);
						String chat11 = pc.getName() + S_SystemMessage.getRefText(1397);

						int gfxid = 3204 + CommonUtil.random(6);
						switch (type) {
						case 1:
							Thread.sleep(2000);
							//String chat = pc.getName() + "님 홀을 선택하셨습니다. 멀리가시면 게임이 취소됩니다.";						  // CHECKED OK
							String chat = pc.getName() + S_SystemMessage.getRefText(1398) + S_SystemMessage.getRefText(1399) + S_SystemMessage.getRefText(1402);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat, ChatType.CHAT_NORMAL), true);
							Thread.sleep(1000);
							pc.send_effect(dealer.getId(), gfxid);
							Thread.sleep(3000);
							if (gfxid == 3204 || gfxid == 3206 || gfxid == 3208) {
								pc.sendPackets(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								int count = pc.getGambling().getGamblingMoney() * 2;
								if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;
								pc.getInventory().storeItem(L1ItemId.ADENA, count);
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
							}
							break;
						case 2:
							Thread.sleep(2000);
							//String chat2 = pc.getName() + "님 짝을 선택하셨습니다. 멀리가시면 게임이 취소됩니다."; // CHECKED OK
							String chat2 = pc.getName() + S_SystemMessage.getRefText(1398) + S_SystemMessage.getRefText(1400) + S_SystemMessage.getRefText(1402);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat2, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat2, ChatType.CHAT_NORMAL), true);
							Thread.sleep(1000);
							pc.send_effect(dealer.getId(), gfxid);
							Thread.sleep(3000);
							if (gfxid == 3205 || gfxid == 3207 || gfxid == 3209) {
								pc.sendPackets(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								int count = pc.getGambling().getGamblingMoney() * 2;
								if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;
								pc.getInventory().storeItem(L1ItemId.ADENA, count);
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
							}
							break;
						case 3:
							Thread.sleep(2000);
							//String chat3 = pc.getName() + "님 1을 선택하셨습니다. 멀리가시면 게임이 취소됩니다."; // CHECKED OK
							String chat3 = pc.getName() + S_SystemMessage.getRefText(1398) + "1" + S_SystemMessage.getRefText(1401) + S_SystemMessage.getRefText(1402);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat3, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat3, ChatType.CHAT_NORMAL), true);
							Thread.sleep(1000);
							pc.send_effect(dealer.getId(), gfxid);
							Thread.sleep(3000);
							if (gfxid == 3204) {
								pc.sendPackets(new S_NpcChatPacket(dealer, chat10, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat10, ChatType.CHAT_NORMAL), true);
								int count = pc.getGambling().getGamblingMoney() * 3;
								if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;
								pc.getInventory().storeItem(L1ItemId.ADENA, count);
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
							}
							break;
						case 4:
							Thread.sleep(2000);
							//String chat4 = pc.getName() + "님 2을 선택하셨습니다. 멀리가시면 게임이 취소됩니다."; // CHECKED OK
							String chat4 = pc.getName() + S_SystemMessage.getRefText(1398) + "2" + S_SystemMessage.getRefText(1401) + S_SystemMessage.getRefText(1402);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat4, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat4, ChatType.CHAT_NORMAL), true);
							Thread.sleep(1000);
							pc.send_effect(dealer.getId(), gfxid);
							Thread.sleep(3000);
							if (gfxid == 3205) {
								pc.sendPackets(new S_NpcChatPacket(dealer, chat10, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat10, ChatType.CHAT_NORMAL), true);
								int count = pc.getGambling().getGamblingMoney() * 3;
								if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;
								pc.getInventory().storeItem(L1ItemId.ADENA, count);
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
							}
							break;
						case 5:
							Thread.sleep(2000);
							//String chat5 = pc.getName() + "님 3을 선택하셨습니다. 멀리가시면 게임이 취소됩니다."; // CHECKED OK
							String chat5 = pc.getName() + S_SystemMessage.getRefText(1398) + "3" + S_SystemMessage.getRefText(1401) + S_SystemMessage.getRefText(1402);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat5, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat5, ChatType.CHAT_NORMAL), true);
							Thread.sleep(1000);
							pc.send_effect(dealer.getId(), gfxid);
							Thread.sleep(3000);
							if (gfxid == 3206) {
								pc.sendPackets(new S_NpcChatPacket(dealer, chat10, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat10, ChatType.CHAT_NORMAL), true);
								int count = pc.getGambling().getGamblingMoney() * 3;
								if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;
								pc.getInventory().storeItem(L1ItemId.ADENA, count);
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
							}
							break;
						case 6:
							Thread.sleep(2000);
							//String chat6 = pc.getName() + "님 4을 선택하셨습니다. 멀리가시면 게임이 취소됩니다."; // CHECKED OK
							String chat6 = pc.getName() + S_SystemMessage.getRefText(1398) + "4" + S_SystemMessage.getRefText(1401) + S_SystemMessage.getRefText(1402);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat6, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat6, ChatType.CHAT_NORMAL), true);
							Thread.sleep(1000);
							pc.send_effect(dealer.getId(), gfxid);
							Thread.sleep(3000);
							if (gfxid == 3207) {
								pc.sendPackets(new S_NpcChatPacket(dealer, chat10, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat10, ChatType.CHAT_NORMAL), true);
								int count = pc.getGambling().getGamblingMoney() * 3;
								if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;
								pc.getInventory().storeItem(L1ItemId.ADENA, count);
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
							}
							break;
						case 7:
							Thread.sleep(2000);
							//String chat7 = pc.getName() + "님 5을 선택하셨습니다. 멀리가시면 게임이 취소됩니다."; // CHECKED OK
							String chat7 = pc.getName() + S_SystemMessage.getRefText(1398) + "5" + S_SystemMessage.getRefText(1401) + S_SystemMessage.getRefText(1402);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat7, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat7, ChatType.CHAT_NORMAL), true);
							Thread.sleep(1000);
							pc.send_effect(dealer.getId(), gfxid);
							Thread.sleep(3000);
							if (gfxid == 3208) {
								pc.sendPackets(new S_NpcChatPacket(dealer, chat10, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat10, ChatType.CHAT_NORMAL), true);
								int count = pc.getGambling().getGamblingMoney() * 3;
								if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;
								pc.getInventory().storeItem(L1ItemId.ADENA, count);
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
							}
							break;
						case 8:
							Thread.sleep(2000);
							//String chat8 = pc.getName() + "님 6을 선택하셨습니다. 멀리가시면 게임이 취소됩니다."; // CHECKED OK
							String chat8 = pc.getName() + S_SystemMessage.getRefText(1398) + "6" + S_SystemMessage.getRefText(1401) + S_SystemMessage.getRefText(1402);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat8, ChatType.CHAT_NORMAL), true);
							Thread.sleep(1000);
							pc.send_effect(dealer.getId(), gfxid);
							Thread.sleep(3000);
							if (gfxid == 3209) {
								pc.sendPackets(new S_NpcChatPacket(dealer, chat10, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat10, ChatType.CHAT_NORMAL), true);
								int count = pc.getGambling().getGamblingMoney() * 3;
								if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;
								pc.getInventory().storeItem(L1ItemId.ADENA, count);
							} else {
								pc.sendPackets(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
							}
							break;
						}
						pc.getGambling().setGambling(false);
					}
				}
			}
		} catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	public void dealerTrade(L1PcInstance player) {
		//L1Object obj = L1World.getInstance().findObject(400064);
		L1Npc npc = NpcTable.getInstance().getTemplate(400064);
		if (player.getX() == 33507 && player.getY() == 32851 && player.getMoveState().getHeading() == 0) {
			//player.sendPackets(new S_MessageYN(252, npc.getDescKr()), true);
			player.sendPackets(new S_MessageYN(252, npc.getDesc()), true);
			// %0%s가 당신과 아이템의 거래를 바라고 있습니다. 거래합니까? (Y/N)	
		}
	}

}

