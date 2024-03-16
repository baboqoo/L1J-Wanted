package l1j.server.IndunSystem.hadin;

import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_1;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_10;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_11;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_12;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_13;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_14;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_15;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_16;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_17;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_18;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_19;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_2;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_20;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_21;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_22;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_23;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_24;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_25;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_26;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_27;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_28;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_29;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_3;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_4;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_5;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_6;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_7;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_8;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_9;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_END;
import static l1j.server.IndunSystem.hadin.HadinStatus.TALK_ISLAND_DUNGEON_READY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.common.data.ChatType;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Door;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.utils.StringUtil;

public class Hadin implements Runnable {
	private static Logger _log = Logger.getLogger(Hadin.class.getName());
	private short _map;
	private L1Party _pARTy;
	private HadinStatus stage	= TALK_ISLAND_DUNGEON_READY;
	private int SubStep = 0;
	private L1NpcInstance Npc_Hadin;
	private L1NpcInstance Hadin_Effect;
	private List<L1PcInstance> list;
	private HadinTrap HT = null;

	private boolean Running = true;
	private boolean listck = false;

	public ArrayList<L1NpcInstance> BasicNpcList;
	public ArrayList<L1NpcInstance> NpcList;

	public HashMap<String, L1NpcInstance> BossRoomDoor;

	public Hadin(int id){
		_map = (short)id;
		list = new ArrayList<L1PcInstance>();
	}

	public boolean StartCK = false;
	private boolean UserCountCK = true;

	@Override
	public void run() {
		Hadin_Setting();
		BossRoomDoor = HadinSpawn.getInstance().fillSpawnTable2(_map, 22);
		list = getParty().getList();
		int firtCkCount = 0;
		while(Running){
			try {
				if(UserCountCK){
					int i = 0;
					for(L1PcInstance pc : list){
						if(pc.getMapId() != _map){
							firtCkCount++;
							break;
						}else{
							i++;
						}
					}					
					if(i >= getParty().getNumOfMembers()){
						UserCountCK = false;
						firtCkCount = 0;
					} else {
						if(firtCkCount >= 10){
							RETURN_TEL();
							UserCountCK = false;
						} else {
							try{
								Thread.sleep(1500);
							} catch(Exception e){}
							continue;
						}
					}
				}
				for(L1PcInstance pc : list){
					if(pc == null) continue;
					if(getParty().getNumOfMembers() < 5 || !pc.isInParty() || pc.getMapId() != _map){//7명
						pc.getTeleport().start(32574, 32942, (short) 0, 5, true);
						if(list.contains(pc)){
							list.remove(pc);
						}
					}
					if(pc.getMapId() != _map){
						if (pc.isInParty()){ 
							getParty().leaveMember(pc);
						}
					}
					if(getParty().getNumOfMembers() < 5){//7명
						listck = true;
						Running = false;
					}
				}
				if(listck)
					break;

				if(NpcList != null){
					for(L1NpcInstance npc : NpcList){
						if(npc == null || npc.isDead())
							NpcList.remove(npc);
					}
				}
				switch(stage){
				/** 하딘 로비 **/
				case TALK_ISLAND_DUNGEON_READY:
						Sleep(3000);
						//오, 왔는가? 잠시 기다리게나, 준비할 것이 있네.
						Npc_Hadin.broadcastPacket(new S_NpcChatPacket(Npc_Hadin, "$7598", ChatType.CHAT_NORMAL), true);
						Sleep(3000);
						//최종 점검을 시작해볼까? 
						Npc_Hadin.broadcastPacket(new S_NpcChatPacket(Npc_Hadin, "$8693", ChatType.CHAT_NORMAL), true);
						BonginSendPacekt(8693);
						Sleep(3000);
						//던전에 들어서면, 자네들의 최소한의 자격을 시험받게 될것이네
						Npc_Hadin.broadcastPacket(new S_NpcChatPacket(Npc_Hadin, "$8695", ChatType.CHAT_NORMAL), true);
						Sleep(3000);
						//어렵지 않은 상대들일테니, 쉽게 통과할것이라고 믿고있네
						Npc_Hadin.broadcastPacket(new S_NpcChatPacket(Npc_Hadin, "$8696", ChatType.CHAT_NORMAL), true);
						Sleep(3000);
						//그리고, 자네들이 트랩을 해제하거나, 설치하기 위한 발판이 준비되어 있네
						Npc_Hadin.broadcastPacket(new S_NpcChatPacket(Npc_Hadin, "$8697", ChatType.CHAT_NORMAL), true);
						Sleep(3000);
						//발판은 최대한 알기쉽게 표현해 놓았지만..
						Npc_Hadin.broadcastPacket(new S_NpcChatPacket(Npc_Hadin, "$8698", ChatType.CHAT_NORMAL), true);
						Sleep(3000);
						//내 미적 감각에 태클은 사양하네
						Npc_Hadin.broadcastPacket(new S_NpcChatPacket(Npc_Hadin, "$8699", ChatType.CHAT_NORMAL), true);
						Sleep(3000);
						//그 밖에도, 무언가 기대할만한 것이 보이기도 하겠지만, 시간에 늦지않게 서둘러주게
						Npc_Hadin.broadcastPacket(new S_NpcChatPacket(Npc_Hadin, "$8700", ChatType.CHAT_NORMAL), true);
						Sleep(3000);
						//조심하게! 안전은 책임질 수 없네
						Npc_Hadin.broadcastPacket(new S_NpcChatPacket(Npc_Hadin, "$8701", ChatType.CHAT_NORMAL), true);
						Sleep(3000);
						//그럼 시작해볼까?
						Npc_Hadin.broadcastPacket(new S_NpcChatPacket(Npc_Hadin, "$8702", ChatType.CHAT_NORMAL), true);                           
					Sleep(3000);
					for(L1PcInstance member : getParty().getMembersArray()){
						if(member == null)continue;
						member.getTeleport().start(32665, 32793, _map, 3, true);
					}
					HT = new HadinTrap(_map);
					Sleep(5000);
					stage = TALK_ISLAND_DUNGEON_1;
					break;
					/** 첫방 -> 문 오픈 -> 2번째방 몬스터 2번 스폰**/
				case TALK_ISLAND_DUNGEON_1:
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if (SubStep == 0){
						Sleep(2000);
						Sleep(2000);
						Npc_Hadin.teleport(32716, 32846, 6, _map);
						Hadin_Effect.teleport(32716, 32846, 6, _map);
						//DoorOpen("해골문 1");
						DoorOpen("Skull Gate 1");
					}
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 1, true);
						SubStep++;
					} else {
						stage = TALK_ISLAND_DUNGEON_2;
						SubStep = 0;
					}
					break;
					/** 몬스터 다 잡았을시 -> 발판 체크 4개**/
				case TALK_ISLAND_DUNGEON_2:
					if(HT.LEVEL_1_TRAP_CK){
						//DoorOpen("해골문 2");
						DoorOpen("Skull Gate 2");
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 2, true);
						Effect();
						stage = TALK_ISLAND_DUNGEON_3;
					}
					break;
					/** 3번째방 몬스터 스폰 **/
				case TALK_ISLAND_DUNGEON_3:
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 2, true);
						SubStep++;
					} else {
						stage = TALK_ISLAND_DUNGEON_4;
						SubStep = 0;
					}
					break;
					/** 트랩 1개 4개 문 오픈 **/
				case TALK_ISLAND_DUNGEON_4:
					if (HT.LEVEL_2_TRAP_CK){
						/*DoorOpen("해골문 4");
						DoorOpen("해골문 5");
						DoorOpen("해골문 6");
						DoorOpen("해골문 8");*/
						DoorOpen("Skull Gate 4");
						DoorOpen("Skull Gate 5");
						DoorOpen("Skull Gate 6");
						DoorOpen("Skull Gate 8");
						Effect();
						stage = TALK_ISLAND_DUNGEON_5;
					}
					break;
					/** 몬스터 스폰  **/
				case TALK_ISLAND_DUNGEON_5:
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 3, true);
						SubStep++;
					} else {
						stage = TALK_ISLAND_DUNGEON_6;
						SubStep = 0;
					}
					break;
					/** 발판 5개 체크 -> 문 2개 오픈**/
				case TALK_ISLAND_DUNGEON_6:
					if (HT.LEVEL_3_TRAP_CK){
						//DoorOpen("해골문 7");
						//DoorOpen("해골문 9");
						DoorOpen("Skull Gate 7");
						DoorOpen("Skull Gate 9");
						Effect();
						stage = TALK_ISLAND_DUNGEON_7;
					}
					break;
				case TALK_ISLAND_DUNGEON_7:
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 4, true);
						SubStep++;
					} else {
						//DoorOpen("철창문 7");
						DoorOpen("Iron Bar Gate 7");
						Effect();
						stage = TALK_ISLAND_DUNGEON_8;
						SubStep = 0;
						Sleep(3000);
					}
					break;
				case TALK_ISLAND_DUNGEON_8:
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 5, true);
						SubStep++;
					} else {
						//DoorOpen("해골문 10");
						DoorOpen("Skull Gate 10");
						Effect();
						SubStep = 0;
						stage = TALK_ISLAND_DUNGEON_9;
						Sleep(3000);
					}
					break;
				case TALK_ISLAND_DUNGEON_9:
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 6, true);
						SubStep++;
					} else {
						//DoorOpen("해골문 11");
						DoorOpen("Skull Gate 11");
						//DoorOpen("철창문 8");
						//DoorOpen("철창문 9");
						DoorOpen("Iron Bar Gate 8");
						DoorOpen("Iron Bar Gate 9");
						Effect();						
						SubStep = 0;
						stage = TALK_ISLAND_DUNGEON_10;
						Sleep(3000);
					}						
					break;
				case TALK_ISLAND_DUNGEON_10:
					if (HT.LEVEL_4_TRAP_CK){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 7, true);
						Effect();
						stage = TALK_ISLAND_DUNGEON_11;
					}
					break;
				case TALK_ISLAND_DUNGEON_11:
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 7, true);
						SubStep++;
					} else {
						//DoorOpen("해골문 12");
						DoorOpen("Skull Gate 12");
						Effect();
						SubStep = 0;
						stage = TALK_ISLAND_DUNGEON_12;
						Sleep(3000);
					}
					break;
				case TALK_ISLAND_DUNGEON_12:
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 8, true);
						SubStep++;
					} else {
						//DoorOpen("해골문 13");
						DoorOpen("Skull Gate 13");
						Effect();
						SubStep = 0;
						stage = TALK_ISLAND_DUNGEON_13;
						Sleep(3000);
					}
					break;
				case TALK_ISLAND_DUNGEON_13:
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 9, true);
						SubStep++;
					} else {
						SubStep = 0;
						//DoorOpen("해골문 14");
						DoorOpen("Skull Gate 14");
						Effect();
						stage = TALK_ISLAND_DUNGEON_14;
					}
					break;
				case TALK_ISLAND_DUNGEON_14://15
					Sleep(30000);
					//모두들 긴장하게! 거대한 어둠이 다가오네!
					Npc_Hadin.broadcastPacket(new S_NpcChatPacket(Npc_Hadin, "$7654", ChatType.CHAT_NORMAL), true);
					BonginSendPacekt(7654);
					Sleep(3000);
					//악한것들이 또 몰려오고 있네! 준비하게!
					Npc_Hadin.broadcastPacket(new S_NpcChatPacket(Npc_Hadin, "$7653", ChatType.CHAT_NORMAL), true);
					BonginSendPacekt(7653);
					Sleep(3000);
					//예상보다 빠르게 다가오고 있네! 조심하게!
					Npc_Hadin.broadcastPacket(new S_NpcChatPacket(Npc_Hadin, "$7652", ChatType.CHAT_NORMAL), true);
					BonginSendPacekt(7652);
					Sleep(3000);
					stage = TALK_ISLAND_DUNGEON_15;					
					break;
				case TALK_ISLAND_DUNGEON_15:
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 10, true);
						SubStep++;
					} else {
						SubStep = 0;
						stage = TALK_ISLAND_DUNGEON_16;
						Effect();
						BonginSendPacekt(8708);
						Sleep(3000);
					}
					break;
				case TALK_ISLAND_DUNGEON_16:
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 11, true);
						SubStep++;
					} else {
						SubStep = 0;
						stage = TALK_ISLAND_DUNGEON_17;						
						BonginSendPacekt(8709);
						Effect();
						Sleep(3000);
					}
					break;
				case TALK_ISLAND_DUNGEON_17:
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 12, true);
						SubStep++;
					} else {
						SubStep = 0;
						stage = TALK_ISLAND_DUNGEON_18;
						BonginSendPacekt(8710);
						Effect();
						Sleep(3000);
					}
					break;
				case TALK_ISLAND_DUNGEON_18:
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 13, true);
						SubStep++;
					} else {
						SubStep = 0;
						stage = TALK_ISLAND_DUNGEON_19;
						BonginSendPacekt(8711);
						Effect();
						Sleep(3000);
					}
					break;
				case TALK_ISLAND_DUNGEON_19:
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 14, true);
						SubStep++;
					} else {
						SubStep = 0;
						stage = TALK_ISLAND_DUNGEON_20;
						BonginSendPacekt(8712);
						Effect();
						Sleep(3000);
					}
					break;
				case TALK_ISLAND_DUNGEON_20:
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 15, true);
						SubStep++;
					} else {
						SubStep = 0;
						stage = TALK_ISLAND_DUNGEON_21;
						BonginSendPacekt(8713);
						Effect();
						Sleep(3000);
					}
					break;
				case TALK_ISLAND_DUNGEON_21:
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 16, true);
						SubStep++;
					} else {
						SubStep = 0;
						stage = TALK_ISLAND_DUNGEON_22;
						BonginSendPacekt(8714);
						Effect();
						Sleep(3000);
					}
					break;
				case TALK_ISLAND_DUNGEON_22:
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 17, true);
						SubStep++;
					} else {
						SubStep = 0;
						stage = TALK_ISLAND_DUNGEON_23;
						BonginSendPacekt(8715);
						Effect();
						Sleep(3000);
					}
					break;
				case TALK_ISLAND_DUNGEON_23:
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 18, true);
						SubStep++;
					} else {
						SubStep = 0;
						stage = TALK_ISLAND_DUNGEON_24;
						BonginSendPacekt(8716);
						Effect();
						Sleep(3000);
					}
					break;
				case TALK_ISLAND_DUNGEON_24:
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 19, true);
						SubStep++;
					} else {
						SubStep = 0;
						stage = TALK_ISLAND_DUNGEON_25;
						BonginSendPacekt(8717);
						Effect();
						Sleep(3000);
					}
					break;
				case TALK_ISLAND_DUNGEON_25:
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 20, true);
						SubStep++;
					} else {
						SubStep = 0;
						stage = TALK_ISLAND_DUNGEON_26;
						Effect();
						BonginSendPacekt(7820);
						Sleep(3000);
						BonginSendPacekt(7821);
						Sleep(3000);
						BonginSendPacekt(7822);
						Sleep(3000);
						BonginSendPacekt(7823);
						Sleep(3000);
						BonginSendPacekt(7824);
						Sleep(3000);
						BonginSendPacekt(7825);
						Sleep(3000);
						BonginSendPacekt(7826);					
						Sleep(10000);
					}
					break;
				case TALK_ISLAND_DUNGEON_26://여기서 케레 스폰
					if(NpcList != null && NpcList.size() > 0)
						continue;
					if(SubStep <= 0){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 21, true);
						SubStep++;
					} else {
						BonginSendPacekt(7827);
						Sleep(3000);
						BonginSendPacekt(7828);
						Sleep(3000);
						BonginSendPacekt(7829);
						Sleep(3000);
						SubStep = 0;
						stage = TALK_ISLAND_DUNGEON_27;
						Effect();
						Sleep(5000);
					}
					break;
				case TALK_ISLAND_DUNGEON_27:
					L1NpcInstance door = null;
					//door = BossRoomDoor.get("보스방 후문 문 8");
					door = BossRoomDoor.get("Boss Room Back Door Gate 8");					
					door.PASS = 0;
					door.broadcastPacket(new S_Door(door.getX(), door.getY(), 0, door.PASS), true);
					//케레니스가 쓰러지다니..
					door.broadcastPacket(new S_NpcChatPacket(Npc_Hadin, "$7833", ChatType.CHAT_NORMAL), true);
					Sleep(3000);
					//door = BossRoomDoor.get("보스방 후문 문 7");
					door = BossRoomDoor.get("Boss Room Back Door Gate 7");
					door.PASS = 0;
					//이건 무슨 현상이지!
					Npc_Hadin.broadcastPacket(new S_NpcChatPacket(Npc_Hadin, "$7835", ChatType.CHAT_NORMAL), true);
					door.broadcastPacket(new S_Door(door.getX(), door.getY(), 0, door.PASS), true);
					Sleep(3000);
					Npc_Hadin.broadcastPacket(new S_NpcChatPacket(Npc_Hadin, "$7836", ChatType.CHAT_NORMAL), true);					
					Sleep(3000);
					//door = BossRoomDoor.get("보스방 후문 문 6");
					door = BossRoomDoor.get("Boss Room Back Door Gate 6");
					door.PASS = 0;
					door.broadcastPacket(new S_Door(door.getX(), door.getY(), 0, door.PASS), true);
					//모두들 대피해라! 이곳을 봉인하겠다!
					Npc_Hadin.broadcastPacket(new S_NpcChatPacket(Npc_Hadin, "$7837", ChatType.CHAT_NORMAL), true);
					Sleep(3000);
					//door = BossRoomDoor.get("보스방 후문 문 5");
					door = BossRoomDoor.get("Boss Room Back Door Gate 5");
					door.PASS = 0;
					door.broadcastPacket(new S_Door(door.getX(), door.getY(), 0, door.PASS), true);
					//남쪽 출구를 곧 막을테니, 어서 빠져나가게!
					Npc_Hadin.broadcastPacket(new S_NpcChatPacket(Npc_Hadin, "$7838", ChatType.CHAT_NORMAL), true);
					Sleep(3000);
					//door = BossRoomDoor.get("보스방 후문 문 16");
					door = BossRoomDoor.get("Boss Room Back Door Gate 16");
					door.PASS = 0;
					door.broadcastPacket(new S_Door(door.getX(), door.getY(), 0, door.PASS), true);
					//동쪽으로 빠져나가면, 탈출구가 보일 것이다!
					Npc_Hadin.broadcastPacket(new S_NpcChatPacket(Npc_Hadin, "$7839", ChatType.CHAT_NORMAL), true);
					Sleep(3000);
					//door = BossRoomDoor.get("보스방 후문 문 15");
					door = BossRoomDoor.get("Boss Room Back Door Gate 15");
					door.PASS = 0;
					door.broadcastPacket(new S_Door(door.getX(), door.getY(), 0, door.PASS), true);
					//살아있다면 다음을 노릴 수 있다! 어서들 탈출해라!
					Npc_Hadin.broadcastPacket(new S_NpcChatPacket(Npc_Hadin, "$7840", ChatType.CHAT_NORMAL), true);
					Sleep(3000);
					//door = BossRoomDoor.get("보스방 후문 문 14");
					door = BossRoomDoor.get("Boss Room Back Door Gate 14");
					door.PASS = 0;
					door.broadcastPacket(new S_Door(door.getX(), door.getY(), 0, door.PASS), true);
					Sleep(3000);
					//door = BossRoomDoor.get("보스방 후문 문 13");
					door = BossRoomDoor.get("Boss Room Back Door Gate 13");
					door.PASS = 0;
					door.broadcastPacket(new S_Door(door.getX(), door.getY(), 0, door.PASS), true);
					Npc_Hadin.broadcastPacket(new S_Effect(Npc_Hadin.getId(), 169), true);
					Npc_Hadin.teleport(32747, 32930, 5, _map);
					Hadin_Effect.teleport(32747, 32930, 5, _map);
					Sleep(10000);
					BonginSendPacekt(8718);
					stage = TALK_ISLAND_DUNGEON_28;
					break;
				case TALK_ISLAND_DUNGEON_28:
					Sleep(3000);
					/*DoorOpen("철창문 14");
					DoorOpen("철창문 13");
					DoorOpen("철창문 12");
					DoorOpen("철창문 11");
					DoorOpen("철창문 10");*/
					DoorOpen("Iron Bar Gate 14");
					DoorOpen("Iron Bar Gate 13");
					DoorOpen("Iron Bar Gate 12");
					DoorOpen("Iron Bar Gate 11");
					DoorOpen("Iron Bar Gate 10");
					Effect();
					stage = TALK_ISLAND_DUNGEON_29;
					break;
				case TALK_ISLAND_DUNGEON_29:
					if (HT.LAST_TRAP_CK){
						NpcList = HadinSpawn.getInstance().fillSpawnTable(_map, 99, true);
						stage = TALK_ISLAND_DUNGEON_END;
					}
					Sleep(15000);
					break;
					/** 보상방 이벤트 이후 **/
				case TALK_ISLAND_DUNGEON_END:
					if(!HT.Running){
						RETURN_TEL();
						Running = false;
					}
					break;
				default:
					break;
				}
			}catch(Exception e){
				//System.out.println("Hadin Event Thread Error instanceID : "+_map +" -> "+e);
			}finally{
				try{
					Thread.sleep(1500);
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
		}
		//System.out.println("Hadin Thread Delete");
		Hadin_Delete();
	}

	public void Start(){
		GeneralThreadPool.getInstance().execute(this);
	}

	private void Sleep(int time){
		try{
			Thread.sleep(time);
		}catch(Exception e){}
	}

	private void Hadin_Setting(){
		for(L1NpcInstance npc : BasicNpcList){
			if(npc != null){
				//if(npc.getName().equalsIgnoreCase("하딘"))
				if(npc.getDesc().equalsIgnoreCase("$7597"))	// Hardin			
					Npc_Hadin = npc;
				//else if(npc.getKoreanName().equalsIgnoreCase("하딘 바닥 이펙트"))
				else if(npc.getId() == 900159) // hardin floor effect
					Hadin_Effect = npc;
			}
		}
	}

	private L1Party getParty(){
		return _pARTy;
	}

	public void setParty(L1Party p){
		_pARTy = p;
	}

	private void Hadin_Delete(){
		Collection<L1Object> cklist = L1World.getInstance().getVisibleObjects(_map).values();
		for(L1Object ob : cklist){
			if(ob == null)continue;
			if(ob instanceof L1ItemInstance){
				L1ItemInstance obj = (L1ItemInstance)ob;
				L1Inventory groundInventory = L1World.getInstance().getInventory(obj.getX(), obj.getY(), obj.getMapId());
				groundInventory.removeItem(obj);
			}else if(ob instanceof L1NpcInstance){
				L1NpcInstance npc = (L1NpcInstance)ob;
				npc.deleteMe();
			}
		}
		HadinCreator.getInstance().removeHadin(_map);
		if(HT != null){//트랩 쓰레드 같이 종료
			HT.Running = false;
			HT = null;
		}
	}

	public void OpenDoor(int i){
		L1DoorInstance door = DoorSpawnTable.getInstance().getDoor(i);
		if (door != null){
			if(door.getOpenStatus() == ActionCodes.ACTION_Close)
				door.open();
		}
	}

	private void DoorOpen(String name){
		for(L1NpcInstance npc : BasicNpcList){
			if(npc != null){
				if(npc instanceof L1DoorInstance){
					L1DoorInstance door = (L1DoorInstance)npc;
					if(door.getSpawnLocation().equalsIgnoreCase(name)){
						door.open();
						break;
					}
				}
			}
		}
	}

	private void Effect() { // 화면 떨림 이펙트.
		for(L1PcInstance c : getParty().getMembersArray())
			c.sendPackets(new S_Effect(c.getId(), 1249), true);
	}

	private void BonginSendPacekt(int count){
		for(L1PcInstance pc : getParty().getMembersArray())
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, StringUtil.DollarString + count), true);
	}

	private void RETURN_TEL(){
		for(L1PcInstance pc : getParty().getMembersArray())
			pc.getTeleport().start(32572, 32944, (short) 0, 3, true);
	}
}

