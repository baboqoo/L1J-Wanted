package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.IndunSystem.ruun.Ruun;
import l1j.server.server.GameServerSetting;
import l1j.server.server.construct.item.L1ItemNormalType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.ItemMentTable.ItemMentType;
import l1j.server.server.datatables.MapBalanceTable.MapBalanceData;
import l1j.server.server.datatables.NpcInfoTable.NpcInfoData;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1AiUserInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.message.S_NotificationMessage;
import l1j.server.server.serverpackets.message.S_NotificationMessage.display_position;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Drop;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.SQLUtil;
//import manager.ManagerInfoThread;  // MANAGER DISABLED

public class DropTable {
	private static Logger _log = Logger.getLogger(DropTable.class.getName());
	private static final Random random = new Random(System.nanoTime());
	
	public static float EVENT_DROP_ADENA_RATE;
	public static float EVENT_DROP_ITEM_RATE;
	
	private final HashMap<Integer, ArrayList<L1Drop>> _droplists;// monster 마다의 드롭 리스트
	private final FastTable<Integer> _dropMapNpcList	= new FastTable<Integer>();
	private final FastTable<Integer> _dropShareNpcList	= new FastTable<Integer>();

	private static DropTable _instance;
	public static DropTable getInstance() {
		if (_instance == null) {
			_instance = new DropTable();
		}
		return _instance;
	}

	private DropTable() {
		_droplists = allDropList();
		dropTypeLoad();
	}

	public static void reload() {
		DropTable oldInstance = _instance;
		_instance = new DropTable();
		oldInstance._droplists.clear();
		oldInstance._dropMapNpcList.clear();
		oldInstance._dropShareNpcList.clear();
		oldInstance = null;
	}
	
	public ArrayList<L1Drop> getDropList(int monid) {
		return _droplists.get(monid);
	}

	public boolean isDropListItem(int monid, int itemid) {
		ArrayList<L1Drop> drop = getDropList(monid);
		for (L1Drop d : drop) {
			if (d.getItemid() == itemid) {
				return true;
			}
		}
		return false;
	}
	
	public L1Drop getDrop(int monid, int itemid) {
		ArrayList<L1Drop> drop = getDropList(monid);
		for (L1Drop d : drop) {
			if (d.getItemid() == itemid) {
				return d;
			}
		}
		return null;
	}

	private HashMap<Integer, ArrayList<L1Drop>> allDropList() {
		HashMap<Integer, ArrayList<L1Drop>> droplistMap = new HashMap<Integer, ArrayList<L1Drop>>();
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM droplist");
			rs = pstm.executeQuery();
			L1Drop drop = null;
			while(rs.next()){
				int mobId = rs.getInt("mobId");
				int itemId = rs.getInt("itemId");
				int min = rs.getInt("min");
				int max = rs.getInt("max");
				int chance = rs.getInt("chance");
				int enchant = rs.getInt("Enchant");
				drop = new L1Drop(mobId, itemId, min, max, chance, enchant);
				
				ArrayList<L1Drop> dropList = droplistMap.get(drop.getMobid());
				if (dropList == null) {
					dropList = new ArrayList<L1Drop>();
					droplistMap.put(new Integer(drop.getMobid()), dropList);
				}
				dropList.add(drop);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return droplistMap;
	}
	
	private void dropTypeLoad(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM droptype_npc");
			rs = pstm.executeQuery();
			while(rs.next()){
				int mobId = rs.getInt("mobId");
				String type = rs.getString("type");
				switch(type){
				case "map":
					_dropMapNpcList.add(mobId);
					break;
				case "share":
					_dropShareNpcList.add(mobId);
					break;
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public static final int aa = 1000000;

	// 인벤트리에 드롭을 설정
	public void setDrop(L1NpcInstance npc, L1Inventory inventory) {
		if (Config.SERVER.STANDBY_SERVER) {
			return;
		}
		// 드롭 리스트의 취득
		int mobId = npc.getNpcTemplate().getNpcId();
		ArrayList<L1Drop> dropList = _droplists.get(mobId);
		if (dropList == null) {
			return;
		}
		// 레이트 취득
		double droprate = Config.RATE.RATE_DROP_ITEMS;
		if (droprate <= 0) {
			droprate = 0;
		}
		double adenarate = Config.RATE.RATE_DROP_ADENA;
		if (adenarate <= 0) {
			adenarate = 0;
		}
		double knightrate = Config.RATE.RATE_DROP_KNIGHT_COIN;
		if (knightrate <= 0) {
			knightrate = 0;
		}
		if (droprate <= 0 && adenarate <= 0 && knightrate <= 0) {
			return;
		}
		
		// 균열의 오만의 탑
		if (GameServerSetting.OMAN_CRACK > 0 && npc.getMapId() == GameServerSetting.OMAN_CRACK) {
			droprate *= 1.2D;// 아이템 드랍 확률 증가
			adenarate *= 5D;// 아데나 드랍률 5배 상향
		}
		// 아이템 드랍률 상향 이벤트
		if (EVENT_DROP_ITEM_RATE > 0) {
			droprate *= EVENT_DROP_ITEM_RATE;
		}
		// 아데나 드랍률 상향 이벤트
		if (EVENT_DROP_ADENA_RATE > 0) {
			adenarate *= EVENT_DROP_ADENA_RATE;
		}
		// 특정 맵 드랍율 설정
		MapBalanceData mapBalance = npc.getMap().getBalance();
		if (mapBalance != null) {
			droprate *= mapBalance.getDropValue();
			adenarate *= mapBalance.getAdenaValue();
		}
		
		int itemId;
		int itemCount;
		int addCount;
		int randomChance;
		L1ItemInstance item;
		
		ItemTable temp = ItemTable.getInstance();
		for (L1Drop drop : dropList) {// 드롭 아이템의 취득
			itemId = drop.getItemid();
			if (adenarate == 0 && itemId == L1ItemId.ADENA) {
				continue;// 아데나레이트 0으로 드롭이 아데나의 경우는 스르
			}
			if (knightrate == 0 && itemId == L1ItemId.KNIGHT_COIN) {
				continue;// 아데나레이트 0으로 드롭이 아데나의 경우는 스르
			}
			// 드롭 찬스 판정
			randomChance = random.nextInt(0xf4240) + 1;
			double rateOfMapId = MapsTable.getInstance().getDropRate(npc.getMap().getBaseMapId());
			if (droprate == 0 || (drop.getChance() * droprate * rateOfMapId) < randomChance) {
				continue;
			}
			// 드롭 개수를 설정
			int min = drop.getMin();
			int max = drop.getMax();
			itemCount = min;
			addCount = max - min + 1;
			if (addCount > 1) {
				itemCount += random.nextInt(addCount);
			}
			if (itemId == L1ItemId.ADENA) {
				itemCount *= adenarate;// 드롭이 아데나의 경우는 아데나레이트를 건다
			} else if (itemId == L1ItemId.KNIGHT_COIN) {
				itemCount *= knightrate;// 드롭이 기사단의 주화의 경우는 레이트를 건다
			}
			itemCount = IntRange.ensure(itemCount, 0, 2000000000);
			
			// 아이템의 생성
			if (temp.getTemplate(itemId) != null) {
				item = temp.createItem(itemId);
				if (item == null) {
					continue;
				}
				item.setCount(itemCount);		
				if (drop.getEnchant() != 0) {
					item.setEnchantLevel(drop.getEnchant());
				}
				// 아이템 격납
				inventory.storeItem(item);
			} else {
				//_log.info("[드랍 리스트 로딩중]없는 아이템입니다: " + itemId);
				_log.info("[Loading drop list] This item does not exist: " + itemId);
			}
		}
		
		/** 환상 이벤트 **/
		if(Config.ALT.ALT_FANTASYEVENT == true){
			setDropFantasyEvent(temp, inventory);
		}
	}
	
	private void setDropFantasyEvent(ItemTable temp, L1Inventory inventory){
		L1ItemInstance Fitem;
		int itemRandom = random.nextInt(100) + 1;
		int countRandom = random.nextInt(100) + 1;
		int item1Random = random.nextInt(100 + 1);
		int Fcount = 0;
		int Itemnum = item1Random <= 50 ? 40127 : 40128;
		if (countRandom <= 90) {
			Fcount = 1;
		} else if (countRandom >= 91) {
			Fcount = 2;
		}
		if (itemRandom <= 40) {
		} else if (itemRandom >= 46 || itemRandom <= 70) {
			Fitem = temp.createItem(Itemnum);
			Fitem.setCount(Fcount);
			inventory.storeItem(Fitem);				
		} else if (itemRandom >= 96) {
			Fitem = temp.createItem(Itemnum);
			Fitem.setCount(Fcount);
			inventory.storeItem(Fitem);					
		}
	}
	
	private boolean antQueenDropValidation(int itemId, short mapId) {
		if ((itemId == 12 || itemId == 40287) && mapId != 15891) {
			return false;
		}
		if ((itemId == 61 || itemId == 42000) && mapId != 15892) {
			return false;
		}
		if ((itemId == 202011 || itemId == 40286) && mapId != 15893) {
			return false;
		}
		if ((itemId == 134 || itemId == 41500) && mapId != 15894) {
			return false;
		}
		if ((itemId == 86 || itemId == 40280) && mapId != 15895) {
			return false;
		}
		if ((itemId == 202013 || itemId == 220010) && mapId != 15896) {
			return false;
		}
		if ((itemId == 202012 || itemId == 220050) && mapId != 15897) {
			return false;
		}
		if ((itemId == 202014 || itemId == 210133) && mapId != 15898) {
			return false;
		}
		if ((itemId == 203041 || itemId == 2027) && mapId != 15899) {
			return false;
		}
		if (itemId == 203049 && mapId != 15902) {
			return false;
		}
		return true;
	}

	// 드랍을 설정
	public void drop(L1NpcInstance npc, ArrayList<?> acquisitorList, ArrayList<?> hateList, L1PcInstance pc) {
		if (Config.SERVER.STANDBY_SERVER) {
			return;
		}
		L1Inventory inventory = npc.getInventory();
		if (inventory == null || inventory.getItems() == null || inventory.getItems().size() <= 0) {
			return;
		}
		if (acquisitorList.size() != hateList.size()) {
			return;
		}
		int mobId = npc.getNpcTemplate().getNpcId();
		if (_dropShareNpcList.contains(mobId)) {
			dropShare(npc);// 자동분배
			return;
		}
		if (_dropMapNpcList.contains(mobId)) {
			dropMap(npc);// 맵 범위 드랍
			return;
		}
		short mapId = npc.getMapId();
		// 헤이트의 합계를 취득
		int totalHate = 0;
		L1Character acquisitor;
		for (int i = hateList.size() - 1; i >= 0; i--) {
			acquisitor = (L1Character) acquisitorList.get(i);
			if ((Config.ALT.AUTO_LOOT == 2)
					// 오토 루팅 2의 경우는 사몬 및 애완동물은 생략한다
					&& (acquisitor instanceof L1SummonInstance || acquisitor instanceof L1PetInstance)) {
				acquisitorList.remove(i);
				hateList.remove(i);
			} else if (acquisitor != null && acquisitor.getMapId() == mapId
					&& acquisitor.getLocation().getTileLineDistance(npc.getLocation()) <= Config.ALT.LOOTING_RANGE) {
				totalHate += (Integer) hateList.get(i);
			} else {
				// null였거나 죽기도 하고 멀었으면 배제
				acquisitorList.remove(i);
				hateList.remove(i);
			}
		}
		
		boolean isEinPenalty	= pc instanceof L1AiUserInstance == false && Config.EIN.REST_EXP_ITEM_PENALTY;
		boolean isEinhasadValue	= false;
		int einPenaltyProb		= 150;// 아인하사드 수치 존재시 150%기준
		if (isEinPenalty) {
			isEinhasadValue		= pc.getAccount().getEinhasad().getPoint() >= Config.EIN.REST_EXP_DEFAULT_RATION;
			if (!isEinhasadValue) {
				einPenaltyProb	= 100;
			}
			// 루운성 입장권
			if (pc._isRuunPaper && Ruun.isRuunMap(pc.getMapId())) {
				einPenaltyProb += 10;
			}
		}
		boolean isPCCafe		= pc.isPCCafe();
		boolean isEinFavor		= pc.getSkill().hasSkillEffect(L1SkillId.EINHASAD_FAVOR);
		
		// 드롭의 분배
		L1ItemInstance item;
		L1Inventory targetInventory = null;
		L1PcInstance player = null;
		int randomInt;
		int chanceHate;
		for (int i = inventory.getSize(); i > 0; i--) {
			item = null;
			try {
				item = (L1ItemInstance) inventory.getItems().get(0);
				if (item == null) {
					continue;
				}
			} catch (Exception e) {
				//System.out.println("드랍리스트 오류 표시 아이디 :" + npc.getNpcId() + " [이름] :" + npc.getName()
				System.out.println("Drop list error display ID:" + npc.getNpcId() + " [Name]:" + npc.getName()
						+ " / x : " + npc.getX() + " y : " + npc.getY() + " m : " + mapId
					    + " / item: " + item.getItem().getItemId());
			}
			if (item.getItem().getItemType() == L1ItemType.NORMAL && item.getItem().getType() == L1ItemNormalType.LIGHT.getId()) {
				item.setNowLighting(false);// light계 아이템
			}
			int itemId = item.getItem().getItemId();
			if (itemId == 700015 && !pc.getInventory().checkItem(700012)) {
				continue;
			}
			if (itemId == L1ItemId.ADENA && pc.getMap().isBeginZone()) {
				continue;// 말하는섬 아데나 드랍 불가
			}
			if (itemId == L1ItemId.KNIGHT_COIN && (pc.getLevel() >= 80 || !pc.getMap().isBeginZone())) {
				continue;// 기사단의 주화
			}
			if (itemId == 410510 && (mapId >= 3050 && mapId <= 3100)) {
				item.setCount((int)(Math.random() * 3) + 4);// 혹한의 신전 하드
			}
			if (mobId == 5182 && !antQueenDropValidation(itemId, mapId)) {//	에르자베 맵별 아이템 드랍
				continue;
			}
			
			// 아인하사드 아이템 패널티 사용시
			if (isEinPenalty && !questItem(item)) {
				if (!isEinhasadValue) {
					if (!isPCCafe && !isEinFavor) {
						continue;
					}
					if ((isPCCafe || isEinFavor) 
							&& random.nextInt(150) + 1 > einPenaltyProb + (itemId == L1ItemId.ADENA ? pc.getAbility().getLuckyAdena() : pc.getAbility().getLuckyItem())) {
						continue;
					}
				}
			}
			
			NpcInfoData npcInfo = npc.getInfo();
			if ((Config.ALT.AUTO_LOOT != 0 || AutoLoot.isAutoLoot(itemId) || (npcInfo != null && npcInfo._autoLoot)) && totalHate > 0) {
				randomInt = random.nextInt(totalHate);
				chanceHate = 0;
				for (int j = hateList.size() - 1; j >= 0; j--) {
					chanceHate += (Integer) hateList.get(j);
					if (chanceHate > randomInt) {
						acquisitor = (L1Character) acquisitorList.get(j);
						if (acquisitor.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) {
							targetInventory = acquisitor.getInventory();
							if (acquisitor instanceof L1PcInstance) {
								player = (L1PcInstance) acquisitor;
								
								L1ItemInstance adenaItem = player.getInventory().findItemId(L1ItemId.ADENA);
								// 소지 아데나를 체크
								if (adenaItem != null && adenaItem.getCount() > L1Inventory.MAX_AMOUNT) {
									targetInventory = L1World.getInstance().getInventory(acquisitor.getX(), acquisitor.getY(), acquisitor.getMapId());
									// 가질 수 없기 때문에 발밑에 떨어뜨린다
									player.sendPackets(L1SystemMessage.ADENA_LOOT_MAX_FAIL);
								} else {
									if (player.isInParty()) {// 파티의 경우
										L1Party party = player.getParty();
										if (item.getItemId() == L1ItemId.ADENA) {// 아데나일경우
										    if (party.isAutoDistribution()) {// 자동분배 타입일때
											    int adenaCount = item.getCount() / serchCount(player);
											    for (L1PcInstance pc2 : L1World.getInstance().getVisiblePartyPlayer(player, 14)) {
												    targetInventory = pc2.getInventory();
												    inventory.tradeItem(item, adenaCount, targetInventory);
												    for (L1PcInstance partymember : party.getMembersArray()) {
												    	if (!partymember.getConfig().RootMent) {
												    		continue;
												    	}
														//partymember.sendPackets(new S_SystemMessage(String.format("아데나 (%d) 획득: %s(%s)", adenaCount, pc2.getName(), npc.getName())), true);
														partymember.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(86), String.valueOf(adenaCount), pc2.getName(), npc.getName()), true);
											        }
											    }
										    } else {
										    	for (L1PcInstance partymember : party.getMembersArray()) {
										    		if (!partymember.getConfig().RootMent) {
										    			continue;
										    		}
													//partymember.sendPackets(new S_SystemMessage(String.format("아데나 (%d) 획득: %s(%s)", item.getCount(), player.getName(), npc.getName())), true);
													partymember.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(86), String.valueOf(item.getCount()), player.getName(), npc.getName()), true);
										    	}
										    }
										} else {// 그외템
											for (L1PcInstance partymember : party.getMembersArray()) {
												if (!partymember.getConfig().RootMent) {
													continue;
												}
												partymember.sendPackets(new S_ServerMessage(813, npc.getName(), item.getLogNameRef(), player.getName()), true);
											}		    	
										}
									} else {// 솔로의 경우
										if (player.getConfig().RootMent) {
											if (npc.getNpcId() == 70983)
												player.sendPackets(new S_ServerMessage(143, "$927", item.getLogNameRef()), true);
											else if (npc.getNpcId() == 45015)
												player.sendPackets(new S_ServerMessage(143, "$952", item.getLogNameRef()), true);
											else if (npc.getNpcId() == 70982)
												player.sendPackets(new S_ServerMessage(143, "$952", item.getLogNameRef()), true);
											else if (npc.getNpcId() == 70981)
												player.sendPackets(new S_ServerMessage(143, "$928", item.getLogNameRef()), true);
											else if (npc.getNpcId() == 70984)
												player.sendPackets(new S_ServerMessage(143, "$929", item.getLogNameRef()), true);
											else
												player.sendPackets(new S_ServerMessage(143, npc.getName(), item.getLogNameRef()), true);
										}
									}
								}
							} else {
								targetInventory = L1World.getInstance().getInventory(npc.getX(), npc.getY(), mapId); 
							}
						} else {
							targetInventory = L1World.getInstance().getInventory(acquisitor.getX(), acquisitor.getY(), acquisitor.getMapId()); // 가질 수 없기 때문에발밑에떨어뜨린다
						}
						break;
					}
				}
			} else {// Non 오토 루팅
				item.setDropNpc(npc.getNpcTemplate());
				int maxHatePc	= -1;
				int maxHate		= -1;
				int ownerHate	= 0;
				for (int j = hateList.size() - 1; j >= 0; j--) {
					ownerHate = (Integer) hateList.get(j);
					if (maxHate < ownerHate) {
						maxHatePc = j;
						maxHate = ownerHate;
					}
					
					Object obj = acquisitorList.get(j);
					if (obj == null || obj instanceof L1PcInstance == false || !npc.knownsObject((L1Object)obj)) {
						continue;
					}
					item.putItemOuter((L1PcInstance)obj, (int) Math.round(((double) ownerHate / (double) totalHate) * 100));// 공헌도 확률 설정
				}
				// 아이템 획득 우선순위
				item.startItemOwnerTimer(maxHatePc != -1 && acquisitorList.get(maxHatePc) instanceof L1PcInstance ? (L1PcInstance) acquisitorList.get(maxHatePc) : pc);
				
				
				List<Integer> dirList = new ArrayList<Integer>();
				for (int j = 0; j < 8; j++) {
					dirList.add(j);
				}
				int x = 0, y = 0, dir = 0;
				do {
					if (dirList.size() == 0) {
						x = y = 0;
						break;
					}
					randomInt = random.nextInt(dirList.size());
					dir = dirList.get(randomInt);
					dirList.remove(randomInt);
					switch (dir) {
					case 0:x=0;	y=-1;break;
					case 1:x=1;	y=-1;break;
					case 2:x=1;	y=0;break;
					case 3:x=1;	y=1;break;
					case 4:x=0;	y=1;break;
					case 5:x=-1;y=1;break;
					case 6:x=-1;y=0;break;
					case 7:x=-1;y=-1;break;
					}
				} while (!npc.getMap().isPassable(npc.getX(), npc.getY(), dir));
				targetInventory = L1World.getInstance().getInventory(npc.getX() + x, npc.getY() + y, mapId);
				if (ItemMentTable.isMent(ItemMentType.DROP, item.getItemId())) {
					S_NotificationMessage message = new S_NotificationMessage(display_position.screen_top, String.format(ItemMentTable.DROP_MESSAGE, item.getViewName()), "00 ff 00", 10);
					if (pc.getConfig().isGlobalMessege()) {
						pc.sendPackets(message, false);
					}
					L1World.getInstance().broadcastPacket(pc, message, true);
				}
			}
			/*if (item.getItem().getItemId() == L1ItemId.ADENA) {
				ManagerInfoThread.AdenMake = Long.valueOf(ManagerInfoThread.AdenMake.longValue() + item.getCount());   // MANAGER DISABLED
			}*/   // MANAGER DISABLED
			if (targetInventory != null) {
				inventory.tradeItem(item, item.getCount(), targetInventory);
			}
		}
		npc.getLight().turnOnOffLight();
	}

	// 자동 분배
	private void dropShare(L1NpcInstance npc) {
		L1Inventory inventory = npc.getInventory();
		L1ItemInstance item;
		L1Inventory targetInventory;
		L1PcInstance player;
		L1PcInstance acquisitor;
		ArrayList<L1PcInstance> acquisitorList = L1World.getInstance().getMapPlayer(npc.getMapId());
		for (int i = inventory.getSize(); i > 0; i--) {
			item = inventory.getItems().get(0);
			if (item.getItem().getItemType() == L1ItemType.NORMAL && item.getItem().getType() == L1ItemNormalType.LIGHT.getId()) {
				item.setNowLighting(false);
			}
			acquisitor = acquisitorList.get(random.nextInt(acquisitorList.size()));
			if (acquisitor.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) {
				targetInventory = acquisitor.getInventory();
				player = acquisitor;
				L1ItemInstance l1iteminstance = player.getInventory().findItemId(L1ItemId.ADENA); // 소지
				if (l1iteminstance != null && l1iteminstance.getCount() > L1Inventory.MAX_AMOUNT) {
					targetInventory = L1World.getInstance().getInventory(acquisitor.getX(), acquisitor.getY(), acquisitor.getMapId()); // 가질 수
					player.sendPackets(L1SystemMessage.ADENA_LOOT_MAX_FAIL);
				} else {
					for (L1PcInstance temppc : acquisitorList) {
						temppc.sendPackets(new S_ServerMessage(813, npc.getName(), item.getLogNameRef(), player.getName()), true);
					}
				}
			} else {
				targetInventory = L1World.getInstance().getInventory(acquisitor.getX(), acquisitor.getY(), acquisitor.getMapId()); // 가질 수
			}
			inventory.tradeItem(item, item.getCount(), targetInventory);
		}
		npc.getLight().turnOnOffLight();
	}
	
	// 맵 범위 드랍
	private void dropMap(L1NpcInstance npc) {
		L1Inventory inventory	= npc.getInventory();
		L1Location loc			= npc.getLocation();
		L1Location randomLoc;
		L1ItemInstance item;
		L1Inventory targetInventory;
		L1Character acquisitor;
		for (int i = inventory.getSize(); i > 0; i--) {
			randomLoc = loc.randomLocation(15, false);
			item = inventory.getItems().get(0);
			item.setDropNpc(npc.getNpcTemplate());
			acquisitor = npc;
			if (item.getItem().getItemType() == L1ItemType.NORMAL && item.getItem().getType() == L1ItemNormalType.LIGHT.getId()) {
				item.setNowLighting(false);
			}
			targetInventory = L1World.getInstance().getInventory(randomLoc.getX(), randomLoc.getY(), acquisitor.getMapId());
			inventory.tradeItem(item, item.getCount(), targetInventory);
		}
		npc.getLight().turnOnOffLight();
		randomLoc = null;
	}
	
	private int serchCount(L1PcInstance player){
		int pmember = 0;
		for (L1PcInstance each : L1World.getInstance().getVisiblePartyPlayer(player, 14)) {
			if (each == null) {
				continue;
			}
			pmember ++;
		}
		return pmember;
	}
	
	private boolean questItem(L1ItemInstance item){
		int itemId = item.getItem().getItemId();
		return (itemId == 30027 || itemId == 30028 || (itemId >= 30073 && itemId <= 30101)
				|| itemId == 40029 || itemId == 40030 || itemId == L1ItemId.KNIGHT_COIN 
				|| (itemId >= 40095 && itemId <= 40099) || (itemId >= 130054 && itemId <= 130057)
				|| itemId == 140029 || itemId == 240029 || (itemId >= 410137 && itemId <= 410139)
				|| itemId == 410515 || itemId == 410516 || (itemId >= 60723 && itemId <= 60725)
				|| itemId == 810007
				|| (item.getItem().getItemType() == L1ItemType.NORMAL && item.getItem().getType() == L1ItemNormalType.QUEST_ITEM.getId()));
	}

}

