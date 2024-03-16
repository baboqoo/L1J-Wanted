package l1j.server.IndunSystem.minigame;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.common.data.ChatType;
import l1j.server.server.ActionCodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.CommonUtil;

public class L1Gambling3 {
	private static final Logger _log = Logger.getLogger(L1Gambling3.class
			.getName());


	public void Gambling(L1PcInstance player, int bettingmoney){
		try {
			for (L1Object l1object : L1World.getInstance().getObject()) {
				if (l1object instanceof L1NpcInstance) {
					L1NpcInstance Npc = (L1NpcInstance) l1object;
					if (Npc.getNpcTemplate().getNpcId() == 300027){
						L1NpcInstance dealer = Npc;
						if (bettingmoney >= 500000) {                                                      //추가
							//String chat = "배팅금액 초과입니다 꿀꺽!";
							String chat = S_SystemMessage.getRefText(1391);
							player.sendPackets(new S_NpcChatPacket(dealer, chat, ChatType.CHAT_NORMAL), true);
							player.broadcastPacket(new S_NpcChatPacket(dealer, chat, ChatType.CHAT_NORMAL), true);
						}
						//String chat = player.getName()+"님 "+ bettingmoney + "아덴 배팅하셨어요~ 1마리맞출때마다 2배 입니다~!"; // CHECKED OK
						String chat = player.getName() + S_SystemMessage.getRefText(1392) + bettingmoney + S_SystemMessage.getRefText(1403);
						player.sendPackets(new S_NpcChatPacket(dealer, chat, ChatType.CHAT_NORMAL), true);
						player.broadcastPacket(new S_NpcChatPacket(dealer, chat, ChatType.CHAT_NORMAL), true);
						Thread.sleep(2000);
						//String chat2 = "배팅할 몹이름을 말해주세요~!(버그베어,장로,멧돼지,스파토이,슬라임,해골,늑대인간,괴물눈,오크전사)";
						String chat2 = S_SystemMessage.getRefText(1404);
						player.sendPackets(new S_NpcChatPacket(dealer, chat2, ChatType.CHAT_NORMAL), true);
						player.broadcastPacket(new S_NpcChatPacket(dealer, chat2, ChatType.CHAT_NORMAL), true);
						player.getGambling().setGamblingMoney3(bettingmoney);
						player.getGambling().setGambling3(true);
					}
				}
			}
		}catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	public void Gambling3(L1PcInstance pc, String chatText, int type){
		pc.sendPackets(new S_SystemMessage(chatText), true);
		for (L1PcInstance listner : L1World.getInstance().getRecognizePlayer(pc)) {
				listner.sendPackets(new S_SystemMessage(chatText), true);
		
		}
		try {
			for (L1Object l1object : L1World.getInstance().getObject()) {
				if (l1object instanceof L1NpcInstance) {
					L1NpcInstance Npc = (L1NpcInstance) l1object;
					if (Npc.getNpcTemplate().getNpcId() == 300027){
						L1NpcInstance dealer = Npc;
						//String chat8 = "과연? ㅋㅋㅋ";
						//String chat9 = "오!굿... 축하드려요... 배당금 지급 해 드렸습니다...";
						//String chat11 = "아쉽군요ㅋㅋ 다음기회에 도전해주세요~!";
						String chat8 = S_SystemMessage.getRefText(1405);
						String chat9 = S_SystemMessage.getRefText(1406);
						String chat11 = S_SystemMessage.getRefText(1407);
						int mobid1 = 300041 + CommonUtil.random(9);
						int mobid2 = 300041 + CommonUtil.random(9);
						int mobid3 = 300041 + CommonUtil.random(9);

						switch(type){
						case 1:
							Thread.sleep(1000);
							//String chat20 = "오크전사에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							String chat20 = S_SystemMessage.getRefText(1408);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat20, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat20, ChatType.CHAT_NORMAL), true);
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat8, ChatType.CHAT_NORMAL), true);
							for (L1Object l1object2 : L1World.getInstance().getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().getNpcId() == 300029){
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(dealer2.getId(), ActionCodes.ACTION_Wand), true);
										pc.broadcastPacket(new S_DoActionGFX(dealer2.getId(), ActionCodes.ACTION_Wand), true);
									}
								}
							}
							L1EffectSpawn.getInstance().spawnEffect(mobid1, 3500, 33513, 32851, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2, 3500, 33511, 32851, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3, 3500, 33509, 32851, pc.getMapId());
							Thread.sleep(1500);
							if(mobid1 == 300041 || mobid2 == 300041 || mobid3 == 300041){
								pc.sendPackets(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								if(mobid1 == 300041){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
								if(mobid2 == 300041){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
								if(mobid3 == 300041){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
							}else{
								pc.sendPackets(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
							}
							break;
						case 2:
							Thread.sleep(1000);
							//String chat21 = "스파토이에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							String chat21 = S_SystemMessage.getRefText(1409);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat21, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat21, ChatType.CHAT_NORMAL), true);
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat8, ChatType.CHAT_NORMAL), true);
							for (L1Object l1object2 : L1World.getInstance().getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().getNpcId() == 300029){
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(dealer2.getId(), ActionCodes.ACTION_Wand), true);
										pc.broadcastPacket(new S_DoActionGFX(dealer2.getId(), ActionCodes.ACTION_Wand), true);
									}
								}
							}
							L1EffectSpawn.getInstance().spawnEffect(mobid1, 3500, 33513, 32851, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2, 3500, 33511, 32851, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3, 3500, 33509, 32851, pc.getMapId());
							Thread.sleep(1500);
							if(mobid1 == 300042 || mobid2 == 300042 || mobid3 == 300042){
								pc.sendPackets(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								if(mobid1 == 300042){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
								if(mobid2 == 300042){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
								if(mobid3 == 300042){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
							}else{
								pc.sendPackets(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
							}
							break;
						case 3:
							Thread.sleep(1000);
							//String chat22 = "멧돼지에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							String chat22 = S_SystemMessage.getRefText(1410);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat22, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat22, ChatType.CHAT_NORMAL), true);
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat8, ChatType.CHAT_NORMAL), true);
							for (L1Object l1object2 : L1World.getInstance().getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().getNpcId() == 300029){
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(dealer2.getId(), ActionCodes.ACTION_Wand), true);
										pc.broadcastPacket(new S_DoActionGFX(dealer2.getId(), ActionCodes.ACTION_Wand), true);
									}
								}
							}
							L1EffectSpawn.getInstance().spawnEffect(mobid1, 3500, 33513, 32851, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2, 3500, 33511, 32851, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3, 3500, 33509, 32851, pc.getMapId());
							Thread.sleep(1500);
							if(mobid1 == 300043 || mobid2 == 300043 || mobid3 == 300043){
								pc.sendPackets(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								if(mobid1 == 300043){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
								if(mobid2 == 300043){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
								if(mobid3 == 300043){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
							}else{
								pc.sendPackets(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
							}
							break;
						case 4:
							Thread.sleep(1000);
							//String chat23 = "슬라임에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							String chat23 = S_SystemMessage.getRefText(1411);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat23, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat23, ChatType.CHAT_NORMAL), true);
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat8, ChatType.CHAT_NORMAL), true);
							for (L1Object l1object2 : L1World.getInstance().getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().getNpcId() == 300029){
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(dealer2.getId(), ActionCodes.ACTION_Wand), true);
										pc.broadcastPacket(new S_DoActionGFX(dealer2.getId(), ActionCodes.ACTION_Wand), true);
									}
								}
							}
							L1EffectSpawn.getInstance().spawnEffect(mobid1, 3500, 33513, 32851, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2, 3500, 33511, 32851, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3, 3500, 33509, 32851, pc.getMapId());
							Thread.sleep(1500);
							if(mobid1 == 300044 || mobid2 == 300044 || mobid3 == 300044){
								pc.sendPackets(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								if(mobid1 == 300044){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
								if(mobid2 == 300044){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
								if(mobid3 == 300044){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
							}else{
								pc.sendPackets(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
							}
							break;
						case 5:
							Thread.sleep(1000);
							//String chat14 = "해골에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							String chat14 = S_SystemMessage.getRefText(1412);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat14, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat14, ChatType.CHAT_NORMAL), true);
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat8, ChatType.CHAT_NORMAL), true);
							for (L1Object l1object2 : L1World.getInstance().getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().getNpcId() == 300029){
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(dealer2.getId(), ActionCodes.ACTION_Wand), true);
										pc.broadcastPacket(new S_DoActionGFX(dealer2.getId(), ActionCodes.ACTION_Wand), true);
									}
								}
							}
							L1EffectSpawn.getInstance().spawnEffect(mobid1, 3500, 33513, 32851, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2, 3500, 33511, 32851, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3, 3500, 33509, 32851, pc.getMapId());
							Thread.sleep(1500);
							if(mobid1 == 300045 || mobid2 == 300045 || mobid3 == 300045){
								pc.sendPackets(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								if(mobid1 == 300045){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
								if(mobid2 == 300045){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
								if(mobid3 == 300045){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
							}else{
								pc.sendPackets(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
							}
							break;
						case 6:
							Thread.sleep(1000);
							//String chat15 = "늑대인간에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							String chat15 = S_SystemMessage.getRefText(1413);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat15, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat15, ChatType.CHAT_NORMAL), true);
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat8, ChatType.CHAT_NORMAL), true);
							for (L1Object l1object2 : L1World.getInstance().getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().getNpcId() == 300029){
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(dealer2.getId(), ActionCodes.ACTION_Wand), true);
										pc.broadcastPacket(new S_DoActionGFX(dealer2.getId(), ActionCodes.ACTION_Wand), true);
									}
								}
							}
							L1EffectSpawn.getInstance().spawnEffect(mobid1, 3500, 33513, 32851, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2, 3500, 33511, 32851, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3, 3500, 33509, 32851, pc.getMapId());
							Thread.sleep(1500);
							if(mobid1 == 300046 || mobid2 == 300046 || mobid3 == 300046){
								pc.sendPackets(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								if(mobid1 == 300046){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
								if(mobid2 == 300046){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
								if(mobid3 == 300046){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
							}else{
								pc.sendPackets(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
							}
							break;
						case 7:
							Thread.sleep(1000);
							//String chat16 = "버그베어에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							String chat16 = S_SystemMessage.getRefText(1414);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat16, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat16, ChatType.CHAT_NORMAL), true);
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat8, ChatType.CHAT_NORMAL), true);
							for (L1Object l1object2 : L1World.getInstance().getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().getNpcId() == 300029){
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(dealer2.getId(), ActionCodes.ACTION_Wand), true);
										pc.broadcastPacket(new S_DoActionGFX(dealer2.getId(), ActionCodes.ACTION_Wand), true);
									}
								}
							}
							L1EffectSpawn.getInstance().spawnEffect(mobid1, 3500, 33513, 32851, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2, 3500, 33511, 32851, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3, 3500, 33509, 32851, pc.getMapId());
							Thread.sleep(1500);
							if(mobid1 == 300047 || mobid2 == 300047 || mobid3 == 300047){
								pc.sendPackets(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								if(mobid1 == 300047){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
								if(mobid2 == 300047){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
								if(mobid3 == 300047){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
							}else{
								pc.sendPackets(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
							}
							break;
						case 8:
							Thread.sleep(1000);
							//String chat17 = "장로에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							String chat17 = S_SystemMessage.getRefText(1415);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat17, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat17, ChatType.CHAT_NORMAL), true);
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat8, ChatType.CHAT_NORMAL), true);
							for (L1Object l1object2 : L1World.getInstance().getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().getNpcId() == 300029){
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(dealer2.getId(), ActionCodes.ACTION_Wand), true);
										pc.broadcastPacket(new S_DoActionGFX(dealer2.getId(), ActionCodes.ACTION_Wand), true);
									}
								}
							}
							L1EffectSpawn.getInstance().spawnEffect(mobid1, 3500, 33513, 32851, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2, 3500, 33511, 32851, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3, 3500, 33509, 32851, pc.getMapId());
							Thread.sleep(1500);
							if(mobid1 == 300048 || mobid2 == 300048 || mobid3 == 300048){
								pc.sendPackets(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								if(mobid1 == 300048){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
								if(mobid2 == 300048){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
								if(mobid3 == 300048){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
							}else{
								pc.sendPackets(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
							}
							break;
						case 9:
							Thread.sleep(1000);
							//String chat18 = "괴물눈에 배팅합니다~ 멀리가시면 게임이 취소됩니다!";
							String chat18 = S_SystemMessage.getRefText(1416);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat18, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat18, ChatType.CHAT_NORMAL), true);
							Thread.sleep(1000);
							pc.sendPackets(new S_NpcChatPacket(dealer, chat8, ChatType.CHAT_NORMAL), true);
							pc.broadcastPacket(new S_NpcChatPacket(dealer, chat8, ChatType.CHAT_NORMAL), true);
							for (L1Object l1object2 : L1World.getInstance().getObject()) {
								if (l1object2 instanceof L1NpcInstance) {
									L1NpcInstance Npc2 = (L1NpcInstance) l1object2;
									if (Npc2.getNpcTemplate().getNpcId() == 300029){
										L1NpcInstance dealer2 = Npc2;
										pc.sendPackets(new S_DoActionGFX(dealer2.getId(), ActionCodes.ACTION_Wand), true);
										pc.broadcastPacket(new S_DoActionGFX(dealer2.getId(), ActionCodes.ACTION_Wand), true);
									}
								}
							}
							L1EffectSpawn.getInstance().spawnEffect(mobid1, 3500, 33513, 32851, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid2, 3500, 33511, 32851, pc.getMapId());
							L1EffectSpawn.getInstance().spawnEffect(mobid3, 3500, 33509, 32851, pc.getMapId());
							Thread.sleep(1500);
							if(mobid1 == 300049 || mobid2 == 300049 || mobid3 == 300049){
								pc.sendPackets(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat9, ChatType.CHAT_NORMAL), true);
								if(mobid1 == 300049){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
								if(mobid2 == 300049){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
								if(mobid3 == 300049){
									int count = pc.getGambling().getGamblingMoney3()*2;
									if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), count) != L1Inventory.OK) continue;															
									pc.getInventory().storeItem(L1ItemId.ADENA, count);
								}
							}else{
								pc.sendPackets(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
								pc.broadcastPacket(new S_NpcChatPacket(dealer, chat11, ChatType.CHAT_NORMAL), true);
							}
							break;
						}
						pc.getGambling().setGambling3(false);
					}
				}
			}
		}catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	public void dealerTrade(L1PcInstance player) {
		//L1Object obj = L1World.getInstance().findObject(300027);
		L1Npc npc = NpcTable.getInstance().getTemplate(300027);
		if(player.getX() == 33515 && player.getY() == 32851 && player.getMoveState().getHeading() == 0){
			//player.sendPackets(new S_MessageYN(252, npc.getDescKr()), true); 
			player.sendPackets(new S_MessageYN(252, npc.getDesc()), true); 
			// %0%s가 당신과 아이템의 거래를 바라고 있습니다. 거래합니까? (Y/N)	
		}
	}


}

