package l1j.server.IndunSystem.ice;

import l1j.server.server.serverpackets.message.S_SystemMessage;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_ExpBoostingInfo;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.einhasad.S_RestExpInfoNoti;
import l1j.server.server.serverpackets.einhasad.S_RestGaugeChargeNoti;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 혹한의 신전 핸들러
 * @author LinOffice
 */
public class IceRaid implements Runnable {
	private static Logger _log = Logger.getLogger(IceRaid.class.getName());
	private int _map;
	protected IceRaidType _type;
	private boolean Running = false, FirstRoom = false, SecondRoom = false, ThirdRoom = false, BossRoom = false, End = false;
	private int Time = 3600;

	public IceRaid(int mapId, IceRaidType type){
		_map = mapId;
		_type = type;
	}
	
	private static final int[] _MonsterNomalList	= { 5080, 5081,  5082, 5083, 5084, 5085 };
	private static final int[] _MonsterHardList		= { 5200, 5201,  5202, 5203, 5204 };
	private static final int[] _bossNomalList		= { 45609, 45609, 45609, 81201, 81201, 55535 };
	private static final int[] _bossHardList		= { 45609, 45609, 45609, 81201, 81201, 5205 };

	@Override
	public void run() {
		Running = true;
		FirstRoom = true;
		Time = 3600;
		SpawnMonster();
		while(Running){
			try {
				Check();
				if (End) {
					reset();
					break;
				} else if (FirstRoom) {
					First();
				} else if (SecondRoom) {
					Second();
				} else if (ThirdRoom) {
					Third();
				} else if (BossRoom) {
					Boss();
				}
			} catch(Exception e){
			} finally {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
		}
		IceRaid_Delete();
		Calendar cal = Calendar.getInstance();
		String AMPM = cal.get(Calendar.AM_PM) == 0 ? "AM" : "PM";
	    System.out.println(cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + " " + AMPM + "   ■■■■■■ Temple of Frost ends " +  _map + " ■■■■■■");
	    cal = null;
	}

	public void Start(){
		Calendar cal = Calendar.getInstance();
		String AMPM = cal.get(Calendar.AM_PM) == 0 ? "AM" : "PM";
		GeneralThreadPool.getInstance().schedule(this, 2000);
	    System.out.println(cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + " " + AMPM + "   ■■■■■■ Temple of Frost starts " +  _map + " ■■■■■■");
	    cal = null;
	}

	private void Check() {
		if (Time > 0) {
			Time--;
		}
		if (Time <= 0) {
			End();
		} else if (Time <= 3590) {
			CheckPc();
		}
		if (Time % 60 == 0) {
			int min = Time / 60;
			for (L1PcInstance pc : L1World.getInstance().getMapPlayer(_map)) {
//AUTO SRM: 				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, min + "분 후에 마을로 강제 이동 됩니다."), true); // CHECKED OK
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,min  + S_SystemMessage.getRefText(32)), true);
			}
		}
	}
	
	private void First() {
		if (_list1.size() > 0) {
			for (int i = _list1.size()-1; i >= 0; i--) {
				L1NpcInstance npc = _list1.get(i);
				if (npc.getCurrentHp() <= 0) {
					remove(npc, 1);
				}
			}
		} else {
			openDoor(5147); // 첫번째 문 개방.
			FirstRoom = false;
			SecondRoom = true;
		}
	}

	private void Second() {
		if (_list2.size() > 0) {
			for (int i = _list2.size()-1; i >= 0; i--) {
				L1NpcInstance npc = _list2.get(i);
				if (npc.getCurrentHp() <= 0) {
					remove(npc, 2);
				}
			}
		} else {
			openDoor(5148); // 두번째 문 개방.
			SecondRoom = false;
			ThirdRoom = true;
		}
	}

	private void Third() {
		if (_list3.size() > 0) {
			for (int i = _list3.size()-1; i >= 0; i--) {
				L1NpcInstance npc = _list3.get(i);
				if (npc.getCurrentHp() <= 0) {
					remove(npc, 3);
				}
			}
		} else {
			openDoor(5150); // 세번째 문 개방.
			ThirdRoom = false;
			BossRoom = true;
		}
	}
	
	private void Boss() {
		if (_list4.size() > 0) {
			for (int i = _list4.size()-1; i >= 0; i--) {
				L1NpcInstance npc = _list4.get(i);
				if (npc.getCurrentHp() <= 0) {
					remove(npc, 4);
				}
			}
		} else {
			for (L1PcInstance pc : L1World.getInstance().getMapPlayer(_map)) {
				pc.getAccount().getEinhasad().addPoint(_type == IceRaidType.HARD ? 2500000 : 500000, pc);
				pc.sendPackets(_type == IceRaidType.HARD ? S_RestGaugeChargeNoti.PLUS_250 : S_RestGaugeChargeNoti.PLUS_50);
				pc.sendPackets(new S_RestExpInfoNoti(pc), true);
				pc.sendPackets(new S_ExpBoostingInfo(pc), true);
				
//AUTO SRM: 				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "벽 뒤에 있는 스빈을 만나십시오."), true); // CHECKED OK
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(33)), true);
			}
			openDoor(5151); // 네섯번째 문 개방.
			BossRoom = false;
		}
	}
	
	private void IceRaid_Delete(){
		for (L1Object obj : L1World.getInstance().getVisibleObjects(_map).values()) {
			if (obj == null) {
				continue;
			}
			if (obj instanceof L1DollInstance || obj instanceof L1PetInstance || obj instanceof L1SummonInstance) {
				continue;
			}
			if (obj instanceof L1ItemInstance) {
				L1ItemInstance item = (L1ItemInstance)obj;
				L1Inventory groundInventory = L1World.getInstance().getInventory(item.getX(), item.getY(), item.getMapId());
				groundInventory.removeItem(item);
			} else if (obj instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance)obj;
				npc.deleteMe();
			}
		}
		IceRaidCreator.getInstance().removeIceRaid(_map);
	}
	
	private void reset() {
		Running = false;
		ListClear(1);
		ListClear(2);
		ListClear(3);
		ListClear(4);
		for (L1Object obj : L1World.getInstance().getVisibleObjects(_map).values()) {
			if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance mon = (L1MonsterInstance) obj;
				mon.deleteMe();
			}
		}
	}
	
	private void CheckPc() {
		int check = 0;
		for (L1Object obj : L1World.getInstance().getVisibleObjects(_map).values()) {
			if (obj instanceof L1PcInstance) {
				check = 1;
			}
		}
		if (check == 0) {
			End();
		}
	}
	
	private void openDoor(int doorId) {
		L1DoorInstance door = null;
		for (L1Object object : L1World.getInstance().getVisibleObjects(_map).values()) {
			if (object instanceof L1DoorInstance) {
				door = (L1DoorInstance)object;
				if (door.getNpcTemplate().getNpcId() == doorId) {
					if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
						door.open();
					}
				}
			}
		}
	}

	private void End() {
		for (L1PcInstance pc : L1World.getInstance().getMapPlayer(_map)) {
			pc.getTeleport().start(34068, 32311, (short)4, 4, true);// 이부분이 떨어질 좌표에요
		}
		End = true;
	}
	
	private final ArrayList<L1NpcInstance> _list1 = new ArrayList<L1NpcInstance>();
	private final ArrayList<L1NpcInstance> _list2 = new ArrayList<L1NpcInstance>();
	private final ArrayList<L1NpcInstance> _list3 = new ArrayList<L1NpcInstance>();
	private final ArrayList<L1NpcInstance> _list4 = new ArrayList<L1NpcInstance>();
	
	public void add(L1NpcInstance npc, int room_number) {
		switch (room_number) {
		case 1:
			if (npc == null || _list1.contains(npc)) {
				return;
			}
			_list1.add(npc);
			break;
		case 2:
			if (npc == null || _list2.contains(npc)) {
				return;
			}
			_list2.add(npc);
			break;
		case 3:
			if (npc == null || _list3.contains(npc)) {
				return;
			}
			_list3.add(npc);
			break;
		case 4:
			if (npc == null || _list4.contains(npc)) {
				return;
			}
			_list4.add(npc);
			break;
		}
	}
	
	private void remove(L1NpcInstance npc, int room_number) {
		switch (room_number) {
		case 1:
			if (npc == null || !_list1.contains(npc)) {
				return;
			}
			_list1.remove(npc);
			break;
		case 2:
			if (npc == null || !_list2.contains(npc)) {
				return;
			}
			_list2.remove(npc);
			break;
		case 3:
			if (npc == null || !_list3.contains(npc)) {
				return;
			}
			_list3.remove(npc);
			break;
		case 4:
			if (npc == null || !_list4.contains(npc)) {
				return;
			}
			_list4.remove(npc);
			break;
		}
	}

	private void ListClear(int room_number) {
		switch (room_number) {
		case 1:_list1.clear();break;
		case 2:_list2.clear();break;
		case 3:_list3.clear();break;
		case 4:_list4.clear();break;
		}
	}
	
	private void SpawnMonster() {
		// NPC스폰
		spawn(32733, 32802, (short) _map, 4, 50086, 1, _type, 0); // 수니아 물약상점
		spawn(32736, 32802, (short) _map, 4, 5086, 1, _type, 0); // 상아탑 첩보원
		spawn(32794, 32922, (short) _map, 6, 5087, 1, _type, 0); // 스빈
		
		// 문짝 스폰
		spawn(32784, 32818, (short) _map, 0, 5147, 1, _type, 0);// 1번째 문
		spawn(32852, 32806, (short) _map, 0, 5148, 1, _type, 0);// 2번째 문
		spawn(32845, 32873, (short) _map, 0, 5150, 1, _type, 0);// 3번째 문
		spawn(32800, 32921, (short) _map, 0, 5151, 1, _type, 0);// 4번째 문 스빈
		
		// 구슬 스폰
		spawn(32794, 32820, (short) _map, 0, 50087, 12, _type, 0);
		spawn(32866, 32809, (short) _map, 0, 50087, 12, _type, 0);
		spawn(32834, 32857, (short) _map, 0, 50087, 12, _type, 0);
		spawn(32847, 32900, (short) _map, 0, 50087, 12, _type, 0);
		
		// 1번방
		for (int i = 0; i < 15 + (_type == IceRaidType.HARD ? 2 : 0); i++) {
			spawn(32765, 32818, (short) _map, 0, _type == IceRaidType.NORMAL ? (_MonsterNomalList[CommonUtil.random(100) % _MonsterNomalList.length]) : (_MonsterHardList[CommonUtil.random(100) % _MonsterHardList.length]), 12, _type, 1);
		}
		
		// 2번 방
		for (int i = 0; i < 15 + (_type == IceRaidType.HARD ? 2 : 0); i++) {
			spawn(32833, 32806, (short) _map, 0, _type == IceRaidType.NORMAL ? (_MonsterNomalList[CommonUtil.random(100) % _MonsterNomalList.length]) : (_MonsterHardList[CommonUtil.random(100) % _MonsterHardList.length]), 13, _type, 2);
		}
		
		// 3번 방
		for (int i = 0; i < 15 + (_type == IceRaidType.HARD ? 2 : 0); i++) {
			spawn(32850, 32853, (short) _map, 0, _type == IceRaidType.NORMAL ? (_MonsterNomalList[CommonUtil.random(100) % _MonsterNomalList.length]) : (_MonsterHardList[CommonUtil.random(100) % _MonsterHardList.length]), 10, _type, 3);
		}
		
		// 4번 방
		for (int i = 0; i < 15 + (_type == IceRaidType.HARD ? 2 : 0); i++) {
			spawn(32831, 32921, (short)_map, 6, _type == IceRaidType.NORMAL ? (_MonsterNomalList[CommonUtil.random(100) % _MonsterNomalList.length]) : (_MonsterHardList[CommonUtil.random(100) % _MonsterHardList.length]), 12, _type, 0);
		}
		
		// 보스 랜덤 스폰
		spawn(32845, 32920, (short)_map, 6, _type == IceRaidType.NORMAL ? (_bossNomalList[CommonUtil.random(_bossNomalList.length)]) : (_bossHardList[CommonUtil.random(_bossHardList.length)]), 0, _type, 4);
	}
	
	public static void spawn(int x, int y, short MapId, int Heading, int npcId, int randomRange, IceRaidType type, int room_number) {
		try {// 타입이란부분에 월래2였는데 얼음여왕은 1을 죠 타입을따라가보면
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(MapId);
			if (randomRange == 0) {
				npc.getLocation().set(x, y, MapId);
				npc.getLocation().forward(Heading);
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(x + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(y + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while(tryCount < 50);
				if (tryCount >= 50) {
					npc.getLocation().forward(Heading);
				}
			}
			if (npc instanceof L1DoorInstance) {
				switch (npc.getNpcId()) {
				case 5147:
					((L1DoorInstance) npc).setLeftEdgeLocation(32816);
					((L1DoorInstance) npc).setRightEdgeLocation(32821);
					((L1DoorInstance) npc).setDirection(1);
					break;
				case 5148:
					((L1DoorInstance) npc).setLeftEdgeLocation(32804);
					((L1DoorInstance) npc).setRightEdgeLocation(32809);
					((L1DoorInstance) npc).setDirection(1);
					break;
				case 5150:
					((L1DoorInstance) npc).setLeftEdgeLocation(32843);
					((L1DoorInstance) npc).setRightEdgeLocation(32847);
					((L1DoorInstance) npc).setDirection(0);
					break;
				case 5151:
					((L1DoorInstance) npc).setLeftEdgeLocation(32819);
					((L1DoorInstance) npc).setRightEdgeLocation(32924);
					((L1DoorInstance) npc).setDirection(1);
					break;
				default:
					break;
				}
			}
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(Heading);

			L1World world = L1World.getInstance();
			world.storeObject(npc);
			world.addVisibleObject(npc);
			
			if (room_number > 0) {
				IceRaid ice = IceRaidCreator.getInstance().getIceRaid(MapId);
				ice.add(npc, room_number);
			}
			
			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
}


