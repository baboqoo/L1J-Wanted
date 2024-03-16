package l1j.server.server.clientpackets;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import l1j.server.GameSystem.inn.InnHelper;
import l1j.server.GameSystem.inn.InnType;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.datatables.NpcActionTable;
import l1j.server.server.datatables.NpcActionTeleportTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.TownTable;
import l1j.server.server.datatables.UBTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1UltimateBattle;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1HousekeeperInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcHtml;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.model.npc.L1NpcHtmlFactory;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.npc.L1NpcIdFactory;
import l1j.server.server.model.npc.action.L1NpcAction;
import l1j.server.server.model.npc.action.bean.L1NpcActionTeleport;
import l1j.server.server.serverpackets.S_CloseList;
import l1j.server.server.serverpackets.S_Inn;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Castle;
import l1j.server.server.templates.L1House;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Town;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.StringUtil;

public class C_NPCAction extends ClientBasePacket {
	private static final String C_NPC_ACTION = "[C] C_NPCAction";
	private static Logger _log = Logger.getLogger(C_NPCAction.class.getName());
	private static final List<String> BLOCK_ACTIONS = Arrays.asList(new String[] {
			"deadTrans", "pvpSet", "ShowHPMPRecovery", "showDisableEffectIcon", "showDungeonTimeLimit"
	});
	private static final List<String> SUB_ACTIONS = Arrays.asList(new String[] {
			"select", "map", "apply", "EnterSeller"
	});
	
	private L1PcInstance pc;
	private String s, s2;

	public C_NPCAction(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		int objid	= readD();
		s	= readS();
		s2	= null;
		if (BLOCK_ACTIONS.contains(s)) {
			return;
		}
		if (SUB_ACTIONS.contains(s)) {
			s2 = readS();
		}
		int[] materials			= null;
		int[] counts			= null;
		int[] createitem		= null;
		int[] createcount		= null;

		String htmlid			= null;
		String success_htmlid	= null;
		String failure_htmlid	= null;
		String[] htmldata		= null;

		L1Object obj = L1World.getInstance().findObject(objid);
		int npcId = 0;
		if (obj != null) {
			if (obj instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				npcId = npc.getNpcId();
				if (npcId == 800817 || npcId == 800818) {// 인터서버 입장
					interServerPortalEnter(npcId);
					return;
				}
				if (npcId == 80088 && s.equalsIgnoreCase("ent")) {
					s2 = readS();// 펫메치
				}
				npc.onFinalAction(pc, s);	
				if (pc.isGm()) {
					pc.sendPackets(new S_SystemMessage(String.format("\\aD > NPC number: %d > Action: %s", npc.getNpcId(), s), true), true);
				}
			} else if (obj instanceof L1PcInstance) {
				L1PcInstance target = (L1PcInstance) obj;
				if (s.matches("[0-9]+")) {
					summonMonster(target);
				} else {
					if (target.isMagicItem()) {// 변신 마법서
						L1PolyMorph.MagicBookPoly(target, s, 1200);
						target.setMagicItem(false);
					} else {
						L1PolyMorph.handleCommands(target, s);
					}
				}
				return;
			}
		} else {
			_log.warning(String.format("object not found, objid %d", objid));
		}
		
		L1NpcAction action = NpcActionTable.getInstance().get(s, pc, obj);
		if (action != null && !s.matches("dismiss|77|99")) {
			L1NpcHtml result = action.execute(s, pc, obj, readByte());
			if (result != null) {
				if (pc.isGm())
					pc.sendPackets(new S_SystemMessage("Dialog " + result), true);													
				pc.sendPackets(new S_NPCTalkReturn(obj.getId(), result), true);
			}
			return;
		}
		// npc action section process
		L1NpcActionTeleport npcTelAction	= NpcActionTeleportTable.getTeleport(npcId, s);
		L1NpcHtmlAction npcHtmlAction		= L1NpcHtmlFactory.getAction(s);
		L1NpcIdAction npcIdAction			= L1NpcIdFactory.getIdAction(npcId);
		if (npcTelAction != null) {
			htmlid = npcTelAction.action(pc);// 텔레포트만 되는 액션
		} else if (npcHtmlAction != null) {
			htmlid = npcHtmlAction.excute(pc, s, s2, obj, npcId);// html로 호출하는 액션
		} else if (npcIdAction != null) {
			htmlid = npcIdAction.excute(pc, s, s2, obj, npcId);// npcId로 호출하는 액션
		} else if (obj instanceof L1HousekeeperInstance && s.equalsIgnoreCase("g")) {// 아지트 관리인 지하레이드
			if (obj instanceof L1HousekeeperInstance) {
				L1Clan clan = pc.getClan();
				int houseId = clan.getHouseId();
				if (houseId != 0) {
					L1House house = HouseTable.getInstance().getHouseTable(houseId);
					int keeperId = house.getKeeperId();
					//int rank = pc.getClanRank();
					if (((L1NpcInstance) obj).getNpcTemplate().getNpcId() == keeperId && house.isPurchaseBasement()) {
						//pc.sendPackets(new S_SystemMessage("\\aA알림:[지하통로]를 클리어 하였습니다. 내일 이용하세요"), true);
					} else {
						htmlid = "agit2";
					}
				}
			}		
		}
		/** 여관 **/	
		else if(InnHelper.isHelper(npcId)){
			L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(npcId);
			String htmlid_inn = "inn";
			if (talking != null)
				htmlid_inn = talking.getNormalAction();			
			if(s.equalsIgnoreCase("room")){// 방대여
				if(pc.getInventory().checkItem(L1ItemId.INN_ROOM_KEY))
					htmlid = htmlid_inn +"5";// 이미 방을 빌렷다
				else{
					pc.sendPackets(new S_Inn(obj.getId(), 8, htmlid_inn + "2", "300", ((L1NpcInstance) obj).getDesc()), true);
					return;
				}
			}else if(s.equalsIgnoreCase("hall")){// 홀대여
				if(pc.isCrown() || pc.isGm()){
				    if(pc.getInventory().checkItem(L1ItemId.INN_HALL_KEY))
				    	htmlid = htmlid_inn + "15";// 이미 홀을 빌렷다
				    else{
					    pc.sendPackets(new S_Inn(obj.getId(), 40, htmlid_inn + "12", "600", ((L1NpcInstance) obj).getDesc()), true);
					    return;
				    }
				}else
					htmlid = htmlid_inn + "10";// 홀은 왕자나 공주만 대여가능합니다.
			}else if(s.equalsIgnoreCase("return")){// 반납
				int cash = 0;
				int keyCount = pc.getInventory().findItemIdCount(L1ItemId.INN_ROOM_KEY);
				if(keyCount > 0){
					cash += keyCount * 60;
					pc.getInventory().consumeItem(L1ItemId.INN_ROOM_KEY, keyCount);
				}
				keyCount = pc.getInventory().findItemIdCount(L1ItemId.INN_HALL_KEY);
				if(keyCount > 0){
					cash += keyCount * 120;
					pc.getInventory().consumeItem(L1ItemId.INN_HALL_KEY, keyCount);
				}
				
				if(cash > 0){
					if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), cash) != L1Inventory.OK) return;
					pc.getInventory().storeItem(L1ItemId.ADENA, cash);
					htmlid = htmlid_inn + "20";
					htmldata = (new String[] { ((L1NpcInstance) obj).getDesc(), Integer.toString(cash)});
				}else{
					pc.sendPackets(L1SystemMessage.INN_KEY_EMPTY);
					htmlid = StringUtil.EmptyString;
				}
			}else if(s.equalsIgnoreCase("enter")){// 들어가기
				// 여관키
				L1ItemInstance[] 	key = pc.getInventory().findItemsId(L1ItemId.INN_ROOM_KEY);// 방
				if(key == null)		key = pc.getInventory().findItemsId(L1ItemId.INN_HALL_KEY);// 홀
				if(key == null){
					pc.sendPackets(L1SystemMessage.INN_KEY_EMPTY);
					return;
				}
				if(key.length == 0){
					pc.sendPackets(L1SystemMessage.INN_KEY_EMPTY);
					return;
				}
				short keymap = 0;
				long currentTime = System.currentTimeMillis();
				for(int i = 0; i < key.length; i++){
					if(key[i].getEndTime().getTime() > currentTime){
						keymap = (short) key[i].getKey();
						break;
					}
				}
				if(keymap == 0){
					pc.sendPackets(L1SystemMessage.INN_KEY_EMPTY);
					return;
				}
				
				InnHelper helper	= InnHelper.getHelper(npcId);
				InnType innType		= (keymap >= helper.getRoomIds()[0] && keymap <= helper.getRoomIds()[1]) ? InnType.ROOM : (keymap >= helper.getHallIds()[0] && keymap <= helper.getHallIds()[1]) ? InnType.HALL : null;
				if(innType == null){
					pc.sendPackets(L1SystemMessage.INN_KEY_OTHER_HOUSE);
					return;
				}
				int[] inLoc = innType == InnType.ROOM ? helper.getRoomLoc().getLoc() : helper.getHallLoc().getLoc();// 입장 좌표
				pc.getTeleport().start(inLoc[0], inLoc[1], (short) keymap, 5, false);
				return;
			}else
				htmlid = StringUtil.EmptyString;
		}
		else if(s.equalsIgnoreCase("askwartime")) {
			if (npcId == 60514) {
				htmldata = warTime(L1CastleLocation.KENT_CASTLE_ID);
				htmlid = "ktguard7";
			} else if (npcId == 60560) {
				htmldata = warTime(L1CastleLocation.OT_CASTLE_ID);
				htmlid = "orcguard7";
			} else if (npcId == 60552 || npcId == 5155) {
				htmldata = warTime(L1CastleLocation.WW_CASTLE_ID);
				htmlid = "wdguard7";
			} else if (npcId == 60524 || npcId == 60525 || npcId == 60529) {
				htmldata = warTime(L1CastleLocation.GIRAN_CASTLE_ID);
				htmlid = "grguard7";
			} else if (npcId == 70857) {
				htmldata = warTime(L1CastleLocation.HEINE_CASTLE_ID);
				htmlid = "heguard7";
			} else if (npcId == 60530 || npcId == 60531) {
				htmldata = warTime(L1CastleLocation.DOWA_CASTLE_ID);
				htmlid = "dcguard7";
			} else if (npcId == 60533 || npcId == 60534) {
				htmldata = warTime(L1CastleLocation.ADEN_CASTLE_ID);
				htmlid = "adguard7";
			} else if (npcId == 81156) {
				htmldata = warTime(L1CastleLocation.DIAD_CASTLE_ID);
				htmlid = "dfguard3";
			}
		} else if (s.equalsIgnoreCase("pay")) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			htmldata = houseBuyPayment(npc);
			htmlid = "agpay";
		} else if (s.equalsIgnoreCase("hall") && obj instanceof L1HousekeeperInstance) {
			L1Clan clan = pc.getClan();
			if (clan != null) {
				int houseId = clan.getHouseId();
				if (houseId != 0) {
					L1House house = HouseTable.getInstance().getHouseTable(houseId);
					int keeperId = house.getKeeperId();
					if (npcId == keeperId) {
						if (house.isPurchaseBasement()) {
							int[] loc = L1HouseLocation.getBasementLoc(houseId);
							pc.getTeleport().start(loc[0], loc[1], (short) (loc[2]), 5, true);
						} else {
							pc.sendPackets(L1ServerMessage.sm1098);
						}
					}
				}
			}
			htmlid = StringUtil.EmptyString;
		} else if (s.equalsIgnoreCase("sco")) {
			htmldata = new String[10];
			htmlid = "colos3";
		} else if (s.matches("contract1yes|contract1no")) {
			if (s.equalsIgnoreCase("contract1yes")) {
				htmlid = "lyraev5";
			} else if (s.equalsIgnoreCase("contract1no")) {
				pc.getQuest().setStep(L1Quest.QUEST_LYRA, 0);
				htmlid = "lyraev4";
			}
			int totem = 0;
			boolean to1 = pc.getInventory().checkItem(40131);
			boolean to2 = pc.getInventory().checkItem(40132);
			boolean to3 = pc.getInventory().checkItem(40133);
			boolean to4 = pc.getInventory().checkItem(40134);
			boolean to5 = pc.getInventory().checkItem(40135);
			if (to1) {
				totem++;
			}
			if (to2) {
				totem++;
			}
			if (to3) {
				totem++;
			}
			if (to4) {
				totem++;
			}
			if (to5) {
				totem++;
			}
			if (totem != 0) {
				materials = new int[totem];
				counts = new int[totem];
				createitem = new int[totem];
				createcount = new int[totem];

				totem = 0;
				if (to1) {
					L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40131);
					int i1 = l1iteminstance.getCount();
					materials[totem] = 40131;
					counts[totem] = i1;
					createitem[totem] = L1ItemId.ADENA;
					createcount[totem] = i1 * 50;
					totem++;
				}
				if (to2) {
					L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40132);
					int i1 = l1iteminstance.getCount();
					materials[totem] = 40132;
					counts[totem] = i1;
					createitem[totem] = L1ItemId.ADENA;
					createcount[totem] = i1 * 100;
					totem++;
				}
				if (to3) {
					L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40133);
					int i1 = l1iteminstance.getCount();
					materials[totem] = 40133;
					counts[totem] = i1;
					createitem[totem] = L1ItemId.ADENA;
					createcount[totem] = i1 * 50;
					totem++;
				}
				if (to4) {
					L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40134);
					int i1 = l1iteminstance.getCount();
					materials[totem] = 40134;
					counts[totem] = i1;
					createitem[totem] = L1ItemId.ADENA;
					createcount[totem] = i1 * 30;
					totem++;
				}
				if (to5) {
					L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40135);
					int i1 = l1iteminstance.getCount();
					materials[totem] = 40135;
					counts[totem] = i1;
					createitem[totem] = L1ItemId.ADENA;
					createcount[totem] = i1 * 200;
					totem++;
				}
			}
		} 
		/** 상점 세율 보기 **/
		else if (s.matches("pandora6|cold6|balsim3|mellin3|glen3|s_merchant3|vergil3|ralf6|rose6|andyn3|catty3|luth3|shivan3")) {
			htmlid = s;
			int taxRatesCastle = L1CastleLocation.getCastleTaxRateByNpcId(npcId);
			htmldata = new String[] { String.valueOf(taxRatesCastle) };
		} else if (s.equalsIgnoreCase("ask")) {
			if (obj instanceof L1NpcInstance) {
				L1Town town = TownTable.getTownFromNpcId(npcId);
				if (town != null && town.get_townid() >= L1TownLocation.TOWNID_TALKING_ISLAND && town.get_townid() <= L1TownLocation.TOWNID_OREN) {
					String leader = town.get_leader_name();
					if (leader != null && leader.length() != 0) {
						htmlid = "owner";
						htmldata = new String[] { leader };
					} else {
						htmlid = "noowner";
					}
				}
			}
		} else if (npcId == 80053) {
			int karmaLevel = pc.getKarmaLevel();
			if (s.equalsIgnoreCase("a")) {
				int aliceMaterialId = 0;
				int[] aliceMaterialIdList = { 40991, 196, 197, 198, 199, 200, 201, 202, 203 };
				for (int id : aliceMaterialIdList) {
					if (pc.getInventory().checkItem(id)) {
						aliceMaterialId = id;
						break;
					}
				}
				if (aliceMaterialId == 0) {
					htmlid = "alice_no";
				} else if (aliceMaterialId == 40991) {
					if (karmaLevel <= -1) {
						materials = new int[] { 40995, 40718, 40991 };
						counts = new int[] { 100, 100, 1 };
						createitem = new int[] { 196 };
						createcount = new int[] { 1 };
						success_htmlid = "alice_1";
						failure_htmlid = "alice_no";
					} else {
						htmlid = "aliceyet";
					}
				} else if (aliceMaterialId == 196) {
					if (karmaLevel <= -2) {
						materials = new int[] { 40997, 40718, 196 };
						counts = new int[] { 100, 100, 1 };
						createitem = new int[] { 197 };
						createcount = new int[] { 1 };
						success_htmlid = "alice_2";
						failure_htmlid = "alice_no";
					} else {
						htmlid = "alice_1";
					}
				} else if (aliceMaterialId == 197) {
					if (karmaLevel <= -3) {
						materials = new int[] { 40990, 40718, 197 };
						counts = new int[] { 100, 100, 1 };
						createitem = new int[] { 198 };
						createcount = new int[] { 1 };
						success_htmlid = "alice_3";
						failure_htmlid = "alice_no";
					} else {
						htmlid = "alice_2";
					}
				} else if (aliceMaterialId == 198) {
					if (karmaLevel <= -4) {
						materials = new int[] { 40994, 40718, 198 };
						counts = new int[] { 50, 100, 1 };
						createitem = new int[] { 199 };
						createcount = new int[] { 1 };
						success_htmlid = "alice_4";
						failure_htmlid = "alice_no";
					} else {
						htmlid = "alice_3";
					}
				} else if (aliceMaterialId == 199) {
					if (karmaLevel <= -5) {
						materials = new int[] { 40993, 40718, 199 };
						counts = new int[] { 50, 100, 1 };
						createitem = new int[] { 200 };
						createcount = new int[] { 1 };
						success_htmlid = "alice_5";
						failure_htmlid = "alice_no";
					} else {
						htmlid = "alice_4";
					}
				} else if (aliceMaterialId == 200) {
					if (karmaLevel <= -6) {
						materials = new int[] { 40998, 40718, 200 };
						counts = new int[] { 50, 100, 1 };
						createitem = new int[] { 201 };
						createcount = new int[] { 1 };
						success_htmlid = "alice_6";
						failure_htmlid = "alice_no";
					} else {
						htmlid = "alice_5";
					}
				} else if (aliceMaterialId == 201) {
					if (karmaLevel <= -7) {
						materials = new int[] { 40996, 40718, 201 };
						counts = new int[] { 10, 100, 1 };
						createitem = new int[] { 202 };
						createcount = new int[] { 1 };
						success_htmlid = "alice_7";
						failure_htmlid = "alice_no";
					} else {
						htmlid = "alice_6";
					}
				} else if (aliceMaterialId == 202) {
					if (karmaLevel <= -8) {
						materials = new int[] { 40992, 40718, 202 };
						counts = new int[] { 10, 100, 1 };
						createitem = new int[] { 203 };
						createcount = new int[] { 1 };
						success_htmlid = "alice_8";
						failure_htmlid = "alice_no";
					} else {
						htmlid = "alice_7";
					}
				} else if (aliceMaterialId == 203) {
					htmlid = "alice_8";
				}
			}
		} else if (npcId == 80072) {
			int karmaLevel = pc.getKarmaLevel();
			if (s.equalsIgnoreCase("0")) {
				htmlid = "lsmitha";
			} else if (s.equalsIgnoreCase("1")) {
				htmlid = "lsmithb";
			} else if (s.equalsIgnoreCase("2")) {
				htmlid = "lsmithc";
			} else if (s.equalsIgnoreCase("3")) {
				htmlid = "lsmithd";
			} else if (s.equalsIgnoreCase("4")) {
				htmlid = "lsmithe";
			} else if (s.equalsIgnoreCase("5")) {
				htmlid = "lsmithf";
			} else if (s.equalsIgnoreCase("6")) {
				htmlid = StringUtil.EmptyString;
			} else if (s.equalsIgnoreCase("7")) {
				htmlid = "lsmithg";
			} else if (s.equalsIgnoreCase("8")) {
				htmlid = "lsmithh";
			} else if (s.equalsIgnoreCase("a") && karmaLevel >= 1) {
				materials = new int[] { 20158, 40669, 40678 };
				counts = new int[] { 1, 50, 100 };
				createitem = new int[] { 20083 };
				createcount = new int[] { 1 };
				success_htmlid = StringUtil.EmptyString;
				failure_htmlid = "lsmithaa";
			} else if (s.equalsIgnoreCase("b") && karmaLevel >= 2) {
				materials = new int[] { 20144, 40672, 40678 };
				counts = new int[] { 1, 50, 100 };
				createitem = new int[] { 20131 };
				createcount = new int[] { 1 };
				success_htmlid = StringUtil.EmptyString;
				failure_htmlid = "lsmithbb";
			} else if (s.equalsIgnoreCase("c") && karmaLevel >= 3) {
				materials = new int[] { 20075, 40671, 40678 };
				counts = new int[] { 1, 50, 100 };
				createitem = new int[] { 20069 };
				createcount = new int[] { 1 };
				success_htmlid = StringUtil.EmptyString;
				failure_htmlid = "lsmithcc";
			} else if (s.equalsIgnoreCase("d") && karmaLevel >= 4) {
				materials = new int[] { 20183, 40674, 40678 };
				counts = new int[] { 1, 20, 100 };
				createitem = new int[] { 20179 };
				createcount = new int[] { 1 };
				success_htmlid = StringUtil.EmptyString;
				failure_htmlid = "lsmithdd";
			} else if (s.equalsIgnoreCase("e") && karmaLevel >= 5) {
				materials = new int[] { 20190, 40674, 40678 };
				counts = new int[] { 1, 40, 100 };
				createitem = new int[] { 20209 };
				createcount = new int[] { 1 };
				success_htmlid = StringUtil.EmptyString;
				failure_htmlid = "lsmithee";
			} else if (s.equalsIgnoreCase("f") && karmaLevel >= 6) {
				materials = new int[] { 20078, 40674, 40678 };
				counts = new int[] { 1, 5, 100 };
				createitem = new int[] { 20290 };
				createcount = new int[] { 1 };
				success_htmlid = StringUtil.EmptyString;
				failure_htmlid = "lsmithff";
			} else if (s.equalsIgnoreCase("g") && karmaLevel >= 7) {
				materials = new int[] { 20078, 40670, 40678 };
				counts = new int[] { 1, 1, 100 };
				createitem = new int[] { 20261 };
				createcount = new int[] { 1 };
				success_htmlid = StringUtil.EmptyString;
				failure_htmlid = "lsmithgg";
			} else if (s.equalsIgnoreCase("h") && karmaLevel >= 8) {
				materials = new int[] { 40719, 40673, 40678 };
				counts = new int[] { 1, 1, 100 };
				createitem = new int[] { 20031 };
				createcount = new int[] { 1 };
				success_htmlid = StringUtil.EmptyString;
				failure_htmlid = "lsmithhh";
			}
		} else if (npcId == 80057) {
			htmlid = karmaCheck(pc.getKarmaLevel());
			htmldata = new String[] { String.valueOf(pc.getKarmaPercent()) };
		} else if (npcId == 70534 || npcId == 70556 || npcId == 70572 || npcId == 70631 || npcId == 70663 || npcId == 70761 || npcId == 70788 || npcId == 70806 || npcId == 70830 || npcId == 70876) {
			if (s.equalsIgnoreCase("r")) {
				/*if (obj instanceof L1NpcInstance) {
					int town_id = L1TownLocation.getTownIdByNpcid(npcId);
				}*/
			} else if (s.equalsIgnoreCase("t")) {
			} else if (s.equalsIgnoreCase("c")) {
			}
		} else if (npcId == 71006) {
			if (s.equalsIgnoreCase("0")) {
				if (pc.getLevel() > 25) {
					htmlid = "jpe0057";
				} else if (pc.getInventory().checkItem(41213)) {
					htmlid = "jpe0056";
				} else if (pc.getInventory().checkItem(41210) || pc.getInventory().checkItem(41211)) {
					htmlid = "jpe0055";
				} else if (pc.getInventory().checkItem(41209)) {
					htmlid = "jpe0054";
				} else if (pc.getInventory().checkItem(41212)) {
					htmlid = "jpe0056";
					materials	= new int[] { 41212 };
					counts		= new int[] { 1 };
					createitem	= new int[] { 41213 };
					createcount	= new int[] { 1 };
				} else {
					htmlid = "jpe0057";
				}
			}
		} else if (npcId == 71055) {
			if (s.equalsIgnoreCase("0")) {
				final int[] item_ids = { 40701 };
				final int[] item_amounts = { 1 };
				L1ItemInstance item = null;
				for (int i = 0; i < item_ids.length; i++) {
					if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(item_ids[i]), item_amounts[i]) != L1Inventory.OK) continue;
					item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
					pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
				}
				pc.getQuest().setStep(L1Quest.QUEST_LUKEIN1, 1);
				htmlid = "lukein8";
			} else if (s.equalsIgnoreCase("1")) {
				pc.getQuest().setEnd(L1Quest.QUEST_TBOX3);
				materials	= new int[] { 40716 }; // 할아버지의 보물
				counts		= new int[] { 1 };
				createitem	= new int[] { 20269 }; // 해골목걸이
				createcount	= new int[] { 1 };
				htmlid		= "lukein0";
			} else if (s.equalsIgnoreCase("2")) {
				htmlid = "lukein12";
				pc.getQuest().setStep(L1Quest.QUEST_RESTA, 3);
			}
		} else if (npcId == 71063) {
			if (s.equalsIgnoreCase("0")) {
				materials	= new int[] { 40701 };
				counts		= new int[] { 1 };
				createitem	= new int[] { 40702 };
				createcount	= new int[] { 1 };
				htmlid		= "maptbox1";
				pc.getQuest().setEnd(L1Quest.QUEST_TBOX1);
				int[] nextbox = { 1, 2, 3 };
				int pid = CommonUtil.random(nextbox.length);
				int nb = nextbox[pid];
				switch(nb){
				case 1:pc.getQuest().setStep(L1Quest.QUEST_LUKEIN1, 2);break;
				case 2:pc.getQuest().setStep(L1Quest.QUEST_LUKEIN1, 3);break;
				case 3:pc.getQuest().setStep(L1Quest.QUEST_LUKEIN1, 4);break;
				}
			}
		} else if (npcId == 71064 || npcId == 71065 || npcId == 71066) {
			if (s.equalsIgnoreCase("0")) {
				materials	= new int[] { 40701 };
				counts		= new int[] { 1 };
				createitem	= new int[] { 40702 };
				createcount	= new int[] { 1 };
				htmlid		= "maptbox1";
				pc.getQuest().setEnd(L1Quest.QUEST_TBOX2);
				int[] nextbox2 = { 1, 2, 3, 4, 5, 6 };
				int pid = CommonUtil.random(nextbox2.length);
				int nb2 = nextbox2[pid];
				switch(nb2){
				case 1:pc.getQuest().setStep(L1Quest.QUEST_LUKEIN1, 5);break;
				case 2:pc.getQuest().setStep(L1Quest.QUEST_LUKEIN1, 6);break;
				case 3:pc.getQuest().setStep(L1Quest.QUEST_LUKEIN1, 7);break;
				case 4:pc.getQuest().setStep(L1Quest.QUEST_LUKEIN1, 8);break;
				case 5:pc.getQuest().setStep(L1Quest.QUEST_LUKEIN1, 9);break;
				case 6:pc.getQuest().setStep(L1Quest.QUEST_LUKEIN1, 10);break;
				}
			}
		} else if (npcId == 71067 || npcId == 71068 || npcId == 71069 || npcId == 71070 || npcId == 71071 || npcId == 71072) {// 작은 상자-3번째
			if (s.equalsIgnoreCase("0")) {
				htmlid		= "maptboxi";
				materials	= new int[] { 40701 }; // 작은 보물의 지도
				counts		= new int[] { 1 };
				createitem	= new int[] { 40716 }; // 할아버지의 보물
				createcount	= new int[] { 1 };
				pc.getQuest().setEnd(L1Quest.QUEST_TBOX3);
				pc.getQuest().setStep(L1Quest.QUEST_LUKEIN1, 11);
			}
		} else if (npcId == 71056) {// 시미즈(해적섬)
			if (s.equalsIgnoreCase("a")) {
				pc.getQuest().setStep(L1Quest.QUEST_SIMIZZ, 1);
				htmlid = "SIMIZZ7";
			} else if (s.equalsIgnoreCase("b")) {
				if (pc.getInventory().checkItem(40661) && pc.getInventory().checkItem(40662) && pc.getInventory().checkItem(40663)) {
					pc.getQuest().setStep(L1Quest.QUEST_SIMIZZ, 2);
					htmlid		= "SIMIZZ8";
					materials	= new int[] { 40661, 40662, 40663 };
					counts		= new int[] { 1, 1, 1 };
					createitem	= new int[] { 20044 };
					createcount	= new int[] { 1 };
				} else {
					htmlid = "SIMIZZ9";
				}
			} else if (s.equalsIgnoreCase("d")) {
				htmlid = "SIMIZZ12";
				pc.getQuest().setStep(L1Quest.QUEST_SIMIZZ, L1Quest.QUEST_END);
			}
		} else if (npcId == 71057) {// 도일(해적섬)
			if (s.equalsIgnoreCase("3")) {
				htmlid = "doil4";
			} else if (s.equalsIgnoreCase("6")) {
				htmlid = "doil6";
			} else if (s.equalsIgnoreCase("1")) {
				if (pc.getInventory().checkItem(40714)) {
					htmlid		= "doil8";
					materials	= new int[] { 40714 };
					counts		= new int[] { 1 };
					createitem	= new int[] { 40647 };
					createcount	= new int[] { 1 };
					pc.getQuest().setStep(L1Quest.QUEST_DOIL, L1Quest.QUEST_END);
				} else {
					htmlid = "doil7";
				}
			}
		} else if (npcId == 71059) {// 루디 안(해적섬)
			if (s.equalsIgnoreCase("A")) {
				htmlid = "rudian6";
				final int[] item_ids = { 40700 };
				final int[] item_amounts = { 1 };
				L1ItemInstance item = null;
				for (int i = 0; i < item_ids.length; i++) {
					if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(item_ids[i]), item_amounts[i]) != L1Inventory.OK) continue;
					item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
					pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
				}
				pc.getQuest().setStep(L1Quest.QUEST_RUDIAN, 1);
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(40710)) {
					htmlid		= "rudian8";
					materials	= new int[] { 40700, 40710 };
					counts		= new int[] { 1, 1 };
					createitem	= new int[] { 40647 };
					createcount	= new int[] { 1 };
					pc.getQuest().setStep(L1Quest.QUEST_RUDIAN, L1Quest.QUEST_END);
				} else {
					htmlid = "rudian9";
				}
			}
		} else if (npcId == 71074) {
			if (s.equalsIgnoreCase("A")) {
				htmlid = "lelder5";
				pc.getQuest().setStep(L1Quest.QUEST_LIZARD, 1);
			} else if (s.equalsIgnoreCase("B")) {
				htmlid = "lelder10";
				pc.getInventory().consumeItem(40633, 1);
				pc.getQuest().setStep(L1Quest.QUEST_LIZARD, 3);
			} else if (s.equalsIgnoreCase("C")) {
				htmlid		= "lelder13";
				materials	= new int[] { 40634 };
				counts		= new int[] { 1 };
				createitem	= new int[] { 20167 }; // 리자드망로브
				createcount	= new int[] { 1 };
				pc.getQuest().setStep(L1Quest.QUEST_LIZARD, L1Quest.QUEST_END);
			}
		} else if (npcId == 80079) {
			if (s.equalsIgnoreCase("0")) {
				if (!pc.getInventory().checkItem(41312)) {
					if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(41312), 1) != L1Inventory.OK) return;
					L1ItemInstance item = pc.getInventory().storeItem(41312, 1);
					if (item != null) {
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
						pc.getQuest().setStep(L1Quest.QUEST_KEPLISHA, L1Quest.QUEST_END);
					}
					htmlid = "keplisha7";
				}
			} else if (s.equalsIgnoreCase("1")) {
				if (!pc.getInventory().checkItem(41314)) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 1000)) {
						materials	= new int[] { L1ItemId.ADENA, 41313 };
						counts		= new int[] { 1000, 1 };
						createitem	= new int[] { 41314 };
						createcount	= new int[] { 1 };
						int htmlA	= CommonUtil.random(3) + 1;
						int htmlB	= CommonUtil.random(100) + 1;
						switch (htmlA) {
						case 1:htmlid = "horosa" + htmlB;break;// horosa1 ~ horosa100
						case 2:htmlid = "horosb" + htmlB;break;// horosb1 ~ horosb100
						case 3:htmlid = "horosc" + htmlB;break;// horosc1 ~ horosc100
						default:break;
						}
					} else {
						htmlid = "keplisha8";
					}
				}
			} else if (s.equalsIgnoreCase("2")) {
				if (pc.getSpriteId() != pc.getClassId()) {
					htmlid = "keplisha9";
				} else {
					if (pc.getInventory().consumeItem(41314, 1)) {
						int html	= CommonUtil.random(9) + 1;
						keplisha(6180 + CommonUtil.random(64));
						switch (html) {
						case 1:htmlid = "horomon11";break;
						case 2:htmlid = "horomon12";break;
						case 3:htmlid = "horomon13";break;
						case 4:htmlid = "horomon21";break;
						case 5:htmlid = "horomon22";break;
						case 6:htmlid = "horomon23";break;
						case 7:htmlid = "horomon31";break;
						case 8:htmlid = "horomon32";break;
						case 9:htmlid = "horomon33";break;
						default:break;
						}
					}
				}
			} else if (s.equalsIgnoreCase("3")) {
				if(pc.getInventory().consumeItem(41312, 1))htmlid = StringUtil.EmptyString;
				if(pc.getInventory().consumeItem(41313, 1))htmlid = StringUtil.EmptyString;
				if(pc.getInventory().consumeItem(41314, 1))htmlid = StringUtil.EmptyString;
			}
		} else if (npcId == 71170 ) {
			if (s.equalsIgnoreCase("request las weapon manual")) {
				materials	= new int[] { 41027 };
				counts		= new int[] { 1 };
				createitem	= new int[] { 40965 };
				createcount	= new int[] { 1 };
				htmlid		= StringUtil.EmptyString;
			}
		}
		
		if (htmlid != null && htmlid.equalsIgnoreCase("colos2"))htmldata = ultimateInfo(npcId);
		if (createitem != null) {
			boolean isCreate = true;
			for (int j = 0; j < materials.length; j++) {
				if (!pc.getInventory().checkItemNotEquipped(materials[j], counts[j])) {
					L1Item temp = ItemTable.getInstance().getTemplate(materials[j]);
					pc.sendPackets(new S_ServerMessage(337, temp.getDesc()), true);
					isCreate = false;
				}
			}

			if (isCreate) {
				int create_count = 0, create_weight = 0;
				L1Item temp = null;
				for (int k = 0; k < createitem.length; k++) {
					temp = ItemTable.getInstance().getTemplate(createitem[k]);
					if (temp.isMerge()) {
						if(!pc.getInventory().checkItem(createitem[k]))create_count += 1;
					} else {
						create_count += createcount[k];
					}
					create_weight += temp.getWeight() * createcount[k] / 1000;
				}
				if (pc.getInventory().getSize() + create_count > L1PcInventory.MAX_SIZE) {
					pc.sendPackets(L1ServerMessage.sm263);
					return;
				}
				if (pc.getMaxWeight() < pc.getInventory().getWeight() + create_weight) {
					pc.sendPackets(L1ServerMessage.sm82);
					return;
				}

				for (int j = 0; j < materials.length; j++) {
					pc.getInventory().consumeItem(materials[j], counts[j]);
				}
				L1ItemInstance item = null;
				for (int k = 0; k < createitem.length; k++) {
					if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(createitem[k]), createcount[k]) != L1Inventory.OK) continue;
					item = pc.getInventory().storeItem(createitem[k], createcount[k]);
					if (item != null) {
						String desc = ItemTable.getInstance().getTemplate(createitem[k]).getDesc();
						String createrName = StringUtil.EmptyString;
						if (obj instanceof L1NpcInstance) {
							createrName = ((L1NpcInstance) obj).getNpcTemplate().getDesc();
						}
						pc.sendPackets(createcount[k] > 1 ? new S_ServerMessage(143, createrName, desc + " (" + createcount[k] + ")") : new S_ServerMessage(143, createrName, desc), true);
					}
				}
				if (success_htmlid != null) {
					if (pc.isGm())
						pc.sendPackets(new S_SystemMessage("Dialog " + success_htmlid), true);													
					pc.sendPackets(new S_NPCTalkReturn(objid, success_htmlid, htmldata), true);
				}
			} else {
				if (failure_htmlid != null) {
					if (pc.isGm())
						pc.sendPackets(new S_SystemMessage("Dialog " + failure_htmlid), true);													
					pc.sendPackets(new S_NPCTalkReturn(objid, failure_htmlid, htmldata), true);
				}
			}
		}

		if (htmlid != null) {
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + htmlid), true);													

			pc.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata), true);
		}
	}

	private String karmaCheck(int level) {
		if(level == 0 || level < -7 || 7 < level)	return StringUtil.EmptyString;
		if(0 < level)								return "vbk" + level;
		if(level < 0)								return "vyk" + Math.abs(level);
		return StringUtil.EmptyString;
	}
	
	private String[] ultimateInfo(int npcId) {
		L1UltimateBattle ub = UBTable.getInstance().getUbForNpcId(npcId);
		if(ub == null)return null;
		return ub.makeUbInfoStrings();
	}
	
	private static final String[] summonstr_list = new String[] { "7", "263", "519", "8", "264", "520", "9", "265", "521", "10", "266", "522", "11", "267", "523", "12", "268", "524", "13", "269", "525", "14", "270", "526", "15", "271", "527", "16", "17", "18", "274" };
	private static final int[] summonid_list = new int[] { 
		810820, 810821, 810822, // 28
		810823, 810824, 810825,// 32
		810826, 810827, 810828,// 36
		810829, 810830, 810831, // 40
		810832, 810833, 810834,// 44
		810835, 810836, 810837,// 48
		810839, 810838, 810840,// 52
		810841, 810842, 810843, // 56
		810844, 810845, 810846,// 60
		810847, // 64
		810848,// 68
		810850, 810849 // 72
	};
	private static final int[] summonlvl_list = new int[] { 28, 28, 28, 32, 32, 32, 36, 36, 36, 40, 40, 40, 44, 44, 44, 48, 48, 48, 52, 52, 52, 56, 56, 56, 60, 60, 60, 64, 68, 72, 72 };// 술자 레벨제한
	private static final int[] summoncha_list = new int[] { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 14, 36, 36, 50 };// 카리
	
	private void summonMonster(L1PcInstance pc) {
		int summonid = 0, levelrange = 0, summoncost = 0;
		for (int loop = 0; loop < summonstr_list.length; loop++) {
			if (s.equalsIgnoreCase(summonstr_list[loop])) {
				summonid	= summonid_list[loop];
				levelrange	= summonlvl_list[loop];
				summoncost	= summoncha_list[loop];
				break;
			}
		}
		if (pc.getLevel() < levelrange) {
			pc.sendPackets(L1ServerMessage.sm743);
			return;
		}

		int petcost = 0;
		Object[] petlist = pc.getPetList().values().toArray();
		for (Object pet : petlist) {
			petcost += ((L1NpcInstance) pet).getPetcost();
		}
		if ((summonid == 810850 || summonid == 810849 || summonid == 810848) && petcost != 0) {
			pc.sendPackets(new S_CloseList(pc.getId()), true);
			return;
		}
		int charisma = pc.getAbility().getTotalCha() + 6 - petcost;
		int summoncount = 0;
		if(levelrange <= 52)		summoncount = charisma / summoncost;
		else if(levelrange == 56)	summoncount = charisma / (summoncost + 2);
		else if(levelrange == 60)	summoncount = charisma / (summoncost + 4);
		else if(levelrange == 64)	summoncount = charisma / (summoncost + 6);
		else						summoncount = charisma / summoncost;

		if(levelrange <= 52 && summoncount > 5)		summoncount = 5;
		else if(levelrange == 56 && summoncount > 4)summoncount = 4;
		else if(levelrange == 60 && summoncount > 3)summoncount = 3;
		else if(levelrange == 64 && summoncount > 2)summoncount = 2;

		L1Npc npcTemp = NpcTable.getInstance().getTemplate(summonid);
		L1SummonInstance summon = null;
		for (int cnt = 0; cnt < summoncount; cnt++) {
			summon = new L1SummonInstance(npcTemp, pc, 0);
			if (summonid == 810850 || summonid == 810849 || summonid == 810848)
				summon.setPetcost(pc.getAbility().getTotalCha() + 7);
			else {
				if(levelrange <= 52)		summon.setPetcost(summoncost);
				else if(levelrange == 56)	summon.setPetcost(summoncost + 2);
				else if(levelrange == 60)	summon.setPetcost(summoncost + 4);
				else if(levelrange == 64)	summon.setPetcost(summoncost + 6);
				else						summoncount = charisma / summoncost;
			}
		}
		pc.sendPackets(new S_CloseList(pc.getId()), true);
	}

	private void keplisha(int polyId) {
		if (pc.getInventory().consumeItem(L1ItemId.ADENA, 100)) {
			pc.removeShapeChange();
			L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_KEPLISHA);
			pc.send_effect(6130);
		} else
			pc.sendPackets(L1ServerMessage.sm189);
	}

	private String[] houseBuyPayment(L1NpcInstance npc) {
		//String name = npc.getNpcTemplate().getDescKr();
		String name = npc.getNpcTemplate().getDescEn();
		String[] result;
		result = new String[] { name, "2000", "1", "1", "00" };
		L1Clan clan = pc.getClan();
		if (clan != null) {
			int houseId = clan.getHouseId();
			if (houseId != 0) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				int keeperId = house.getKeeperId();
				if (npc.getNpcTemplate().getNpcId() == keeperId) {
					Calendar cal = house.getTaxDeadline();
					int month	= cal.get(Calendar.MONTH) + 1;
					int day		= cal.get(Calendar.DATE);
					int hour	= cal.get(Calendar.HOUR_OF_DAY);
					result		= new String[] { name, "2000", String.valueOf(month), String.valueOf(day), String.valueOf(hour) };
				}
			}
		}
		return result;
	}

	private String[] warTime(int castleId) {
		L1Castle castle = CastleTable.getInstance().getCastleTable(castleId);
		if(castle == null)return null;
		Calendar warTime = castle.getWarTime();
		int year	= warTime.get(Calendar.YEAR);
		int month	= warTime.get(Calendar.MONTH) + 1;
		int day		= warTime.get(Calendar.DATE);
		int hour	= warTime.get(Calendar.HOUR_OF_DAY);
		int minute	= warTime.get(Calendar.MINUTE);
		String[] result;
		if(castleId == L1CastleLocation.OT_CASTLE_ID)
			result = new String[] { String.valueOf(year), String.valueOf(month), String.valueOf(day), String.valueOf(hour), String.valueOf(minute) };
		else
			result = new String[] { StringUtil.EmptyString, String.valueOf(year), String.valueOf(month), String.valueOf(day), String.valueOf(hour), String.valueOf(minute) };
		return result;
	}
	
	private void interServerPortalEnter(int npcId){
		int[] loc = null;
		switch(npcId){
		case 800817:loc = L1TownLocation.getGetBackLoc(L1TownLocation.WOLRDWAR_HEINE_BASE);break;
		case 800818:loc = L1TownLocation.getGetBackLoc(L1TownLocation.WOLRDWAR_WINDAWOOD_BASE);break;
		default:return;
		}
		pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true, true);
	}
	
	@Override
	public String getType() {
		return C_NPC_ACTION;
	}
}

