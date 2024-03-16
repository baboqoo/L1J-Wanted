package l1j.server.IndunSystem.orim;

import java.util.ArrayList;
import java.util.Iterator;

import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;

public class OrimController extends Thread {
	private static OrimController _instance;
	private boolean Close;
	private boolean _InDunStart	= false;
	private boolean _InDunOpen	= false;

	private final ArrayList<L1PcInstance> playmember = new ArrayList<L1PcInstance>();

	private L1Location[] dependLoc;
	private L1Location attackLoc;
	private ArrayList<L1NpcInstance> _list		= new ArrayList<L1NpcInstance>();
	private ArrayList<L1MonsterInstance> _list1	= new ArrayList<L1MonsterInstance>();
	private int spawnCount;
	private int shipType;
	private L1NpcInstance shellNpc1 = null, shellNpc2 = null;
	private int totalScore;
	private int dependCount;
	private int attackCount;
	private int dangerCount;
	public boolean explain;
	int[][] _MonsterList = { 
			{ 91214, 91213, 91216, 91215, 91214 }, { 91202, 91203, 91204, 91205, 91206 },
			{ 91203, 91204, 91205, 91208, 91209 }, { 91209, 91204, 91208, 91205, 91202 },
			{ 91207, 91209, 91211, 91208, 91210 }, { 91211, 91212, 91218, 91217, 91219 },
			{ 91219, 91212, 91217, 91220, 91221 }, { 91220, 91212, 91213, 91207, 91218 },
			{ 91221, 91212, 91207, 91217, 91218 }, { 91211, 91212, 91217, 91217, 91218 },
			{ 91218, 91212, 91217, 91207, 91218 }, { 91219, 91212, 91217, 91207, 91218 },
			{ 91211, 91212, 91217, 91220, 91219 } };

	int[] _MonsterList1 = { 91202, 91203, 91204, 91205, 91206 };
	int[] _MonsterList2 = { 91207, 91204, 91205, 91208, 91209 };
	int[] _MonsterList3 = { 91209, 91204, 91208, 91205, 91206 };
	int[] _MonsterList4 = { 91210, 91209, 91211, 91208, 91207 };
	int[] _MonsterList5 = { 91211, 91212, 91213, 91213, 91215 };

	public OrimController() {
		Close = false;
	}

	public static OrimController getInstance() {
		if (_instance == null) {
			_instance = new OrimController();
		}
		return _instance;
	}

	public boolean getInDunStart() {
		return _InDunStart;
	}

	public void setInDunStart(boolean indun) {
		_InDunStart = indun;
	}

	public boolean getInDunOpen() {
		return _InDunOpen;
	}

	public void setInDunOpen(boolean indun) {
		_InDunOpen = indun;
	}

	public void addPlayMember(L1PcInstance pc) {
		playmember.add(pc);
	}

	public int getPlayMembersCount() {
		return playmember.size();
	}

	public void removePlayMember(L1PcInstance pc) {
		playmember.remove(pc);
	}

	public void clearPlayMember() {
		playmember.clear();
	}

	public boolean isPlayMember(L1PcInstance pc) {
		return playmember.contains(pc);
	}

	public L1PcInstance[] getPlayMemberArray() {
		return (L1PcInstance[]) playmember.toArray(new L1PcInstance[getPlayMembersCount()]);
	}

	private void checkMember() {
		for (L1PcInstance pc : getPlayMemberArray())
			try {
				if (((pc == null ? 1 : 0) | (pc.getMapId() != 9101 ? 1 : 0)) != 0) {
					removePlayMember(pc);
					if (pc.getParty().isLeader(pc)) Close = true;
				} else if (pc.getParty() == null) {
					Close = true;
				}
			} catch (Exception e) {
				
			}
	}

	public void attackFormat() {
		attackLoc = null;
	}

	public L1NpcInstance getShell1() {
		return shellNpc1;
	}

	public L1NpcInstance getShell2() {
		return shellNpc2;
	}

	public void setShell1(L1NpcInstance npc) {
		shellNpc1 = npc;
	}

	public void setShell2(L1NpcInstance npc) {
		shellNpc2 = npc;
	}

	public void spawnShell() {
		L1SpawnUtil.spawn5(32792, 32809, (short)9101, 4, 91240, 1);
		L1SpawnUtil.spawn5(32803, 32809, (short)9101, 5, 91240, 1);
		L1SpawnUtil.spawn5(32798, 32806, (short)9101, 5, 91232, 1);
	}

	public void addScore(int i) {
		totalScore += i;
		scoreMark();
	}

	public int getDeCount() {
		return dependCount;
	}

	public void addDeCount() {
		dependCount += 1;
	}

	public int getAtCount() {
		return attackCount;
	}

	public void addAtCount() {
		attackCount += 1;
	}

	public void run() {
		try {
			setInDunOpen(true);
			setInDunStart(true);
			spawnShell();
			totalScore = 0;
			explain = false;
			try {
				Thread.sleep(10000L);
			} catch (Exception e) {
				
			}
			spawnCount = getPlayMembersCount();

			MSGOrimAndPartyleader(0);
			if (!explain) {
				MSGOrimAndPartyleader(1);
			}

			MSGOrim(0, 0);
			SpawnMonster(0, 0);
			try {
				Thread.sleep(15000L);
			} catch (Exception e) {
				
			}
			getScore();
			int stage = 1;
			for (int stageNum = 1; stageNum <= 12; stageNum++) {
				dependCount = 0;
				attackCount = 0;
				shipType = 0;
				stage = stageNum;
				stageMark(stageNum);

				MSGOrim(1, stageNum);
				eventSpawn((stageNum - 1) % 3);
				try {
					Thread.sleep(15000L);
				} catch (Exception e) {
					
				}
				MSGOrim(2, stageNum);
				StarZone(1, 1);
				try {
					Thread.sleep(15000L);
				} catch (Exception e) {
					
				}
				attackFormat();

				MSGOrim(3, stageNum);
				StarZone(1, 1);
				try {
					Thread.sleep(15000L);
				} catch (Exception e) {
					
				}
				spawnShelling();

				if (Close) break;
				attackFormat();

				MSGOrim(4, stageNum);
				StarZone(1, 1);
				spawnShip(stageNum, 0);
				try {
					Thread.sleep(15000L);
				} catch (Exception e) {
					
				}
				attackFormat();

				MSGOrim(5, stageNum);
				try {
					Thread.sleep(10000L);
				} catch (Exception e) {
					
				}
				MSG1(stageNum, 0);

				MSG1(stageNum, 1);
				SpawnMonster(stageNum, 0);
				if ((stageNum == 4) || (stageNum == 8) || (stageNum == 12))
					Boss(stageNum);
				try {
					Thread.sleep(20000L);
				} catch (Exception e) {
					
				}
				getScore();
				MSG1(stageNum, 2);
				SpawnMonster(stageNum, 0);
				if (stageNum == 12)
					Boss(12);
				try {
					Thread.sleep(10000L);
				} catch (Exception e) {
					
				}
				getScore();
				if (_list1.size() > spawnCount + 1)
					MSGOrim(10, stageNum);
				try {
					Thread.sleep(10000L);
				} catch (Exception e) {
					
				}
				MSG1(stageNum, 3);
				SpawnMonster(stageNum, 0);

				if (stageNum == 12)
					Boss(12);
				try {
					Thread.sleep(15000L);
				} catch (Exception e) {
					
				}
				getScore();
				if (_list1.size() > spawnCount + 1) {
					MSGOrim(11, stageNum);
				}
				try {
					Thread.sleep(5000L);
				} catch (Exception e) {
					
				}
				getScore();

				for (int k = 0; k < 3; k++) {
					MSGOlden(1);
					SpawnMonster(stageNum, 1);
					try {
						Thread.sleep(10000L);
					} catch (Exception e) {
						
					}
					getScore();
				}

				MSGOlden(2);

				checkMember();
				Effect(3);
				try {
					Thread.sleep(2000L);
				} catch (Exception e) {
					
				}
				OrimFight summonTrd = OrimFight.getInstance(shipType);
				summonTrd.start();

				SpawnMonster(stageNum, 0);
				try {
					Thread.sleep(5000L);
				} catch (Exception e) {
					
				}
				MSGOrim(6, stageNum);
				try {
					Thread.sleep(20000L);
				} catch (Exception e) {
					
				}
				getScore();
				checkMember();

				if (Close) break;
			}
			if (stage == 12) {
				MSGOrim(12, stage);
				cuttleSpawn();
				try {
					Thread.sleep(30000L);
				} catch (Exception e) {
					
				}
			}
			End();
			_instance = null;
			_InDunStart = false;
			_InDunOpen = false;
		} catch (Exception e) {
			
		}
	}

	private void getScore() {
		checkMember();
		int subScore = 0;
		try{
		for (int i = 0; i < _list1.size(); i++) {
			L1MonsterInstance mon = (L1MonsterInstance) _list1.get(i);
			if ((mon.getCurrentHp() <= 0) || (mon.isDead())) {
				subScore += mon.getLevel() / getPlayMembersCount();
				_list1.remove(i);
			}
		}
		addScore(subScore);
		} catch (Exception e) {
			
		}
	}

	private void InDunclear() {
		_list.clear();
		_list1.clear();

		L1MonsterInstance mob = null;
		L1EffectInstance efNpc = null;
		L1NpcInstance Npc = null;

		Iterator<L1Object> localIterator = L1World.getInstance().getVisibleObjects(9101).values().iterator();
	
		while (localIterator.hasNext()) {
			Object obj = localIterator.next();
			if ((obj instanceof L1MonsterInstance)) {
				mob = (L1MonsterInstance) obj;
				if (!mob.isDead()) {
					mob.setDead(true);
					mob.setActionStatus(8);
					mob.setCurrentHp(0);
					mob.deleteMe();
				}
			} else if ((obj instanceof L1EffectInstance)) {
				efNpc = (L1EffectInstance) obj;
				if (efNpc.getNpcTemplate().getNpcId() != 91232)
					efNpc.deleteMe();
			} else if ((obj instanceof L1NpcInstance)) {
				Npc = (L1NpcInstance) obj;
				Npc.deleteMe2();
			}
		}
	}

	private void Boss(int i) {
		L1SpawnUtil.spawn3(32797, 32801, (short) 9101, 0, 91243, 1, false, 2000);
		switch (i) {
		case 4:
			int k1 = 91223 + CommonUtil.random(3);
			L1SpawnUtil.spawn5(32797, 32801, (short)9101, 4, k1, 1);
			break;
		case 8:
			int k2 = 91223 + CommonUtil.random(3);
			L1SpawnUtil.spawn5(32797, 32801, (short)9101, 4, k2, 1);
			break;
		case 12:
			for (int j = 0; j < 3; j++) {
				int bossId = 91226 + CommonUtil.random(6);
				L1SpawnUtil.spawn5(32797, 32801, (short)9101, 4, bossId, 1);
			}
		}
	}

	private void cuttleSpawn() {
		L1SpawnUtil.spawn5(32801, 32795, (short)9101, 4, 91200, 0);
		L1SpawnUtil.spawn5(32804, 32796, (short)9101, 4, 91201, 0);
		L1SpawnUtil.spawn5(32793, 32796, (short)9101, 4, 91201, 0);
	}

	private void eventSpawn(int i) {
		switch (i) {
		case 0:
			for (L1PcInstance pc : getPlayMemberArray())
				try {
					pc.sendPackets(new S_EffectLocation(32801, 32788, 8142), true);
					pc.sendPackets(new S_EffectLocation(32801, 32788, 8142), true);
				} catch (Exception e) {
					
				}
			break;
		case 1:
			for (int j = 0; j < 5; j++) {
				L1SpawnUtil.spawn6(32799, 32788, (short)9101, 91241, 15, 10000, 9101);
			}
			break;
		case 2:
			for (int j = 0; j < 5; j++)
				L1SpawnUtil.spawn6(32799, 32788, (short)9101, 91242, 20, 10000, 9101);
		}
	}

	private void spawnShip(int stageNum, int heading) {
		if ((stageNum == 4) && (totalScore > 1000)) {
			L1SpawnUtil.spawn3(32797, 32815, (short)9101, heading, 91236, 1, false, 125000);
			shipType = 1;
		} else if ((stageNum == 8) && (totalScore > 2000)) {
			L1SpawnUtil.spawn3(32797, 32819, (short)9101, heading, 91237, 1, false, 125000);
			shipType = 2;
		} else {
			L1SpawnUtil.spawn3(32797, 32819, (short)9101, heading, 91234, 1, false, 125000);
			shipType = 0;
		}
	}

	private void SpawnMonster(int stageNum, int j) {
		int ran = 0;

		if (j == 0) {
			for (int k = 0; k < spawnCount * 3 / 2; k++) {
				ran = CommonUtil.random(_MonsterList[stageNum].length);
				L1SpawnUtil.spawn5(32797, 32801, (short) 9101, 4, _MonsterList[stageNum][ran], 5);
			}
		} else {
			for (int k = 0; k < spawnCount; k++) {
				ran = CommonUtil.random(_MonsterList[stageNum].length);
				int x = 32789 + CommonUtil.random(18);
				int y = 32796 + CommonUtil.random(13);

				L1SpawnUtil.spawn3(x, y, (short)9101, 0, 91244, 1, false, 2000);

				L1SpawnUtil.spawn5(x, y, (short)9101, 4, _MonsterList[stageNum][ran], 1);
			}

			Effect(0);
		}
	}

	private void StarZone(int i, int j) {
		if (i == 1) {
			attackFormat();
		      int x = 32789 + CommonUtil.random(18);
		      int y = 32796 + CommonUtil.random(13);
			int time = j == 0 ? 10000 : 10000 + CommonUtil.random(10) * 1000;
			L1SpawnUtil.spawn3(x, y, (short)9101, 0, 91238, 1, false, time);
			attackLoc = new L1Location(x, y, 9101);
		}

		if (j == 1) {
			if (spawnCount <= 2) {
				return;
			}
			dependLoc = new L1Location[spawnCount - 2];
			for (int k = 0; k < spawnCount - 2; k++) {
				dependLoc[k] = new L1Location();
			      int x1 = 32789 + CommonUtil.random(18);
			      int y1 = 32796 + CommonUtil.random(13);
				L1SpawnUtil.spawn3(x1, y1, (short)9101, 0, 91239, 1, false, 10000 + CommonUtil.random(5) * 1000);
				dependLoc[k] = new L1Location(x1, y1, 9101);
			}
		}
	}

	private void sendMessage(String msg) {
		for (L1PcInstance pc : getPlayMemberArray())
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg), true);
	}

	private void sendMessage1(String msg) {
		for (L1PcInstance pc : getPlayMemberArray())
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg), true);
	}

	private void sendMessage2(String msg) {
		for (L1PcInstance pc : getPlayMemberArray())
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg), true);
	}

	private void talkPartyleader(String msg) {
		checkMember();
		L1PcInstance pc = getPlayMemberArray()[0];
		if (pc.getParty().isLeader(pc))
			for (L1PcInstance member : L1World.getInstance().getVisiblePlayer(pc)) {
				pc.sendPackets(new S_SystemMessage(msg, true), true);
				member.sendPackets(new S_SystemMessage(msg, true), true);
			}
	}

	private void MSGOrimAndPartyleader(int i) {
		checkMember();
		switch (i) {
		case 0:			
			/*sendMessage("오림: 여러분 당황하지 마시고 제 말을 들어주세요.");
			talkPartyleader("여러분 당황하지 마시고 제 말을 들어주세요.");
			try {
				Thread.sleep(5000L);
			} catch (Exception e) {
			}
			sendMessage("오림: 스승님을 만나러 섬을 떠나 본토로 가고 있습니다.");
			talkPartyleader("오림은 지금 스승을 만나러 섬을 떠나 본토로 가고 있습니다.");
			try {
				Thread.sleep(5000L);
			} catch (Exception e) {
			}
			sendMessage("오림: 저는 지금 흑마법사 올딘에게 쫓기고 있습니다.");
			talkPartyleader("오림은 지금 흑마법사 올딘에게 몰리우고 있습니다.");
			try {
				Thread.sleep(5000L);
			} catch (Exception e) {
			}
			sendMessage("오림 : 상황 설명이 필요 없다면, [alt + 1]으로 생략하겠습니다.");
			talkPartyleader("상황 설명이 필요 없다면, [alt + 1]으로 생략합니다.");*/
			
			sendMessage("$9529");
			talkPartyleader("$9529");
			try {
				Thread.sleep(5000L);
			} catch (Exception e) {
			}
			sendMessage("$12098");
			talkPartyleader("$12098");
			try {
				Thread.sleep(5000L);
			} catch (Exception e) {
			}
			sendMessage("$12019");
			talkPartyleader("$12019");
			try {
				Thread.sleep(5000L);
			} catch (Exception e) {
			}
			sendMessage("$9530");
			talkPartyleader("$9530");

			try {
				Thread.sleep(5000L);
			} catch (Exception e) {
			}
		case 1:
			/*sendMessage("오림 : 우리 배를 공격하고 있는 것은 올딘이라 불리는 것입니다.");
			talkPartyleader("우리 배를 공격하고 있는 것은 올딘이라 불리는 것입니다.");
			try {
				Thread.sleep(5000L);
			} catch (Exception e) {
			}
			sendMessage("오림 : 우선 함포를 사용하는 방법을 알려 드리겠습니다.");
			talkPartyleader("우선 함포를 사용하는 방법을 알려 드리겠습니다.");
			try {
				Thread.sleep(5000L);
			} catch (Exception e) {
			}
			sendMessage("오림 : 잠시후 바닥에 붉은색 원이 생기면 중심에 들어가주세요.");
			talkPartyleader("잠시후 바닥에 붉은색 원이 생기면 중심에 들어가주세요.");
			try {
				Thread.sleep(3000L);
			} catch (Exception e) {
			}
			sendMessage("오림 : 저는 대포 사이에 있는 같은 무늬의 발판위에 서겠습니다.");
			talkPartyleader("저는 대포 사이에 있는 같은 무늬의 발판위에 서겠습니다.");
			StarZone(1, 0);
			try {
				Thread.sleep(3000L);
			} catch (Exception e) {
			}
			sendMessage("오림 : 그런 뒤, 제가 [alt + 4]를 누르게 되면 포가 발사됩니다.");
			talkPartyleader("그런 뒤, 제가 [alt + 4]를 누르게 되면 포가 발사됩니다.");
			try {
				Thread.sleep(10000L);
			} catch (Exception e) {
			}
			attackFormat();
			sendMessage("오림 : 시간이 없으니 다음으로 넘어가겠습니다.");
			talkPartyleader("시간이 없으니 다음으로 넘어가겠습니다.");
			try {
				Thread.sleep(3000L);
			} catch (Exception e) {
			}
			sendMessage("오림 : 하지만 적들도 공격을 하기 때문에, 방어 역시 필요합니다.");
			talkPartyleader("하지만 적들도 공격을 하기 때문에, 방어 역시 필요합니다.");
			try {
				Thread.sleep(3000L);
			} catch (Exception e) {
			}
			sendMessage("오림 : 방어를 위해서는, 노란색 원 안에 들어가 주세요.");
			talkPartyleader("방어를 위해서는, 노란색 원 안에 들어가 주세요.");
			StarZone(0, 1);
			try {
				Thread.sleep(3000L);
			} catch (Exception e) {
			}
			sendMessage("오림 : 저는 같은 자리에서 [alt + 2]로 방어 주문을 시전 합니다.");
			talkPartyleader("저는 같은 자리에서 [alt + 2]로 방어 주문을 시전 합니다.");*/
		
			sendMessage("$9531");
			talkPartyleader("$9531");
			try {
				Thread.sleep(5000L);
			} catch (Exception e) {
			}
			sendMessage("$9633");
			talkPartyleader("$9633");
			try {
				Thread.sleep(5000L);
			} catch (Exception e) {
			}
			sendMessage("$9534");
			talkPartyleader("$9534");
			try {
				Thread.sleep(3000L);
			} catch (Exception e) {
			}
			sendMessage("$9535");
			talkPartyleader("$9535");
			StarZone(1, 0);
			try {
				Thread.sleep(3000L);
			} catch (Exception e) {
			}
			sendMessage("$9536");
			talkPartyleader("$9536");
			try {
				Thread.sleep(10000L);
			} catch (Exception e) {
			}
			attackFormat();
			sendMessage("$9546");
			talkPartyleader("$9546");
			try {
				Thread.sleep(3000L);
			} catch (Exception e) {
			}
			sendMessage("$9537");
			talkPartyleader("$9537");
			try {
				Thread.sleep(3000L);
			} catch (Exception e) {
			}
			sendMessage("$9538");
			talkPartyleader("$9538");
			StarZone(0, 1);
			try {
				Thread.sleep(3000L);
			} catch (Exception e) {
			}
			sendMessage("$9539");
			talkPartyleader("$9539");			

			try {
				Thread.sleep(12000L);
			} catch (Exception e) {
			}
		}
	}

	private void MSGOrim(int i, int stageNum) {
		checkMember();
		switch (i) {
		case 0:
			/*sendMessage("오림: 이제, 적의 공격에 대비해야 합니다. 서둘러 주세요.");
			try {
				Thread.sleep(5000L);
			} catch (Exception e) {
			}
			sendMessage("오림: 배에 숨어들었던 몬스터들이 있습니다. 처치해주세요.");
			break;*/
			sendMessage("$9540");
			try {
				Thread.sleep(5000L);
			} catch (Exception e) {
			}
			sendMessage("$9603");
			break;
		case 1:
			sendMessage("$9548");
			try {
				Thread.sleep(5000L);
			} catch (Exception e) {
			}
			switch (stageNum) {
			case 1:
			case 4:
			case 7:
			case 10:
				//sendMessage("오림 : 주변에 상어떼가 있습니다. 조심하세요.");
				sendMessage("$9541");
				break;
			case 2:
			case 5:
			case 8:
			case 11:
				//sendMessage("오림: 근처에 바다 하피의 서식지가 있나 봅니다. 조심하세요.");
				sendMessage("$9542");
				break;
			case 3:
			case 6:
			case 9:
			case 12:
				//sendMessage("오림 : 바다 드레이크네요. 신기하다고 가까이 가지 마세요.");
				sendMessage("$9543");
			}
			break;
		case 2:
			/*if (stageNum == 1)
				sendMessage("오림 : 적의 배가 다가오고 있습니다! 준비해주세요!");
			else if (stageNum == 4)
				sendMessage("오림 : 으..! 이번에 오는 배에서는 올딘의 기운이 느껴집니다!");
			else if (stageNum == 8)
				sendMessage("오림 : 조심하세요! 이번에 오는 배는 여느때보다 강한 기운이 있습니다.");
			else {
				sendMessage("오림 : 전보다 강한 기운이 느껴집니다! 조심하세요!");
			}
			Effect(0);
			break;*/	
			if (stageNum == 1)
				sendMessage("$9550");
			else if (stageNum == 4)
				sendMessage("$9556");
			else if (stageNum == 8)
				sendMessage("$9554");
			else {
				sendMessage("$9588");
			}
			Effect(0);
			break;	
		/*case 3:
			sendMessage("오림 : 적의 공격으로 배가 손상됩니다. 여러번 손상되면 배가 침몰하니 주의하세요.");
			Effect(0);
			break;
		case 4:
			sendMessage("오림 : 배가 곧 충돌합니다! 바다에 빠지지 않게 조심하세요!");
			Effect(0);
			break;
		case 5:
			sendMessage("오림 : 배가 충돌하면, 적들이 뛰여들것입니다! 조심하세요!");
			Effect(0);
			break;
		case 6:
			sendMessage("오림 : 어서 탈출하세요! 어딘가에 당신을 가둔 물건이 있을겁니다!");
			break;
		case 7:
			sendMessage("오림 : 배가 침몰하기 직전입니다. 어떻게든 배를 사수해야 합니다!");
			Effect(0);
			break;
		case 8:
			sendMessage("오림 : 배가 침몰합니다! 서둘러 탈출하세요! 이상태로는 무리입니다!");
			Effect(1);
			break;
		case 9:
			sendMessage("오림 : 거의다 물리쳤습니다. 조금 더 힘내주세요!");
			break;
		case 10:
			sendMessage("오림 : 난입한 적이 우리 배에 손상을 입히고 있습니다! 서둘러주세요!");
			break;
		case 11:
			sendMessage("오림 : 시간이 별로 없습니다. 서둘러주세요.");
			break;
		case 12:
			sendMessage("오림 : 저 징그러운건 뭐지! 위험해 보입니다!");
			try {
				Thread.sleep(5000L);
			} catch (Exception e) {
			}
			sendMessage("오림 : 으윽! 바다 생물까지 공격해 오고 있습니다! 하필, 이럴때!");
			try {
				Thread.sleep(5000L);
			} catch (Exception e) {
			}
		}*/
		
		case 3:
			sendMessage("$12097");
			Effect(0);
			break;
		case 4:
			sendMessage("$9551");
			Effect(0);
			break;
		case 5:
			sendMessage("$9555");
			Effect(0);
			break;
		case 6:
			sendMessage("$9606");
			break;
		case 7:
			sendMessage("$9587");
			Effect(0);
			break;
		case 8:
			sendMessage("$9562");
			Effect(1);
			break;
		case 9:
			sendMessage("$9585");
			break;
		case 10:
			sendMessage("$9561");
			break;
		case 11:
			sendMessage("$9545");
			break;
		case 12:
			sendMessage("$9544");
			try {
				Thread.sleep(5000L);
			} catch (Exception e) {
			}
			sendMessage("$9549");
			try {
				Thread.sleep(5000L);
			} catch (Exception e) {
			}
		}
	}

	private void MSG1(int stageNum, int j) {
		checkMember();
		switch (j) {
		case 0:
			//sendMessage1("전투가 시작됩니다 [" + stageNum + "/12]");
			switch (stageNum) {
				case 1: sendMessage1("$9609"); break;
				case 2: sendMessage1("$9610"); break;
				case 3: sendMessage1("$9611"); break;
				case 4: sendMessage1("$9612"); break;
				case 5: sendMessage1("$9613"); break;
				case 6: sendMessage1("$9614"); break;
				case 7: sendMessage1("$9615"); break;
				case 8: sendMessage1("$9616"); break;
				case 9: sendMessage1("$9617"); break;
				case 10: sendMessage1("$9618"); break;
				case 11: sendMessage1("$9619"); break;
				case 12: sendMessage1("$920"); break;
			}
			try {
				Thread.sleep(5000L);
			} catch (Exception e) {
			}
		case 1:
			//sendMessage1("첫번째 부대가 난입 했습니다.");
			sendMessage1("$9563");
			break;
		case 2:
			//sendMessage1("두번째 부대가 난입 했습니다.");
			sendMessage1("$9564");
			break;
		case 3:
			if ((stageNum == 4) || (stageNum == 8) || (stageNum == 12))
				MSGOrim(9, 0);
			else
				//sendMessage1("세번째 부대가 난입 했습니다.");
				sendMessage1("$9565");
		}
	}

	private void MSGOlden(int i) {
		checkMember();
		switch (i) {
		case 1:
			//sendMessage2("올딘 : 게으른것들! 어서 나가 싸워라! 저것들을 다 쓸어버려!");
			sendMessage2("$9560");
			break;
		case 2:
			//sendMessage2("올딘 : 저놈을 잡아 들여라!");
			sendMessage2("$9608");
		}
	}

	private void stageMark(int stageNum) {
		checkMember();
		for (L1PcInstance pc : getPlayMemberArray())
			pc.sendPackets(new S_PacketBox(S_PacketBox.ROUND, stageNum), true);
	}

	private void scoreMark() {
		checkMember();
		for (L1PcInstance pc : getPlayMemberArray())
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, S_PacketBox.SCORE_MARK, Integer.toString(totalScore)), true);
}

	public void addMonList(L1NpcInstance npc) {
		if (npc == null) return;
		_list.add(npc);
	}

	private void EndTelePort() {
		checkMember();
		for (L1PcInstance c : getPlayMemberArray())
			c.getTeleport().start(32596, 32916, (short)0, 5, true);
	}

	private void Effect(int i) {
		for (L1PcInstance c : getPlayMemberArray())
			try {
				switch (i) {
				case 0:
					c.sendPackets(new S_PacketBox(S_PacketBox.HADIN_DISPLAY, 1), true);
					c.sendPackets(new S_PacketBox(S_PacketBox.HADIN_DISPLAY, 2), true);
					break;
				case 1:
					c.sendPackets(new S_PacketBox(S_PacketBox.HADIN_DISPLAY, 1), true);
					break;
				case 2:
					c.sendPackets(new S_PacketBox(S_PacketBox.HADIN_DISPLAY, 2), true);
					break;
				case 3:
					c.sendPackets(new S_PacketBox(S_PacketBox.HADIN_DISPLAY, 4), true);
				}
			} catch (Exception e) {
				
			}
	}

	private void spawnShelling() {
		checkMember();
		for (int shellCount = 0; shellCount < 5 - attackCount / 2; shellCount++) {
			for (L1PcInstance pc : getPlayMemberArray())
				try {
					int x = 32789 + CommonUtil.random(18);
					int y = 32796 + CommonUtil.random(13);
					pc.sendPackets(new S_EffectLocation(x, y, 762), true);
					pc.sendPackets(new S_EffectLocation(x, 32815, 8233), true);
				} catch (Exception e) {
					
				}
			Effect(2);
			try {
				Thread.sleep(1000L);
			} catch (Exception e) {
				
			}
			if (shellCount + 1 > dependCount) {
				int x = 32789 + CommonUtil.random(18);
				int y = 32796 + CommonUtil.random(13);

				L1SpawnUtil.spawn5(x, y, (short)9101, 4, 91233, 1);
				dangerCount += 1;
				if (dangerCount == 15) {
					MSGOrim(7, 0);
					try {
						Thread.sleep(5000L);
					} catch (Exception e) {
						
					}
				} else if (dangerCount > 17) {
					MSGOrim(8, 0);
					try {
						Thread.sleep(10000L);
					} catch (Exception e) {
						
					}
					Close = true;
					break;
				}
			}
		}
	}

	public Boolean dependTrap() {
		int count = 0;
		if ( dependLoc.length == 0) {
			return Boolean.valueOf(false);
		}
		for (int i = 0; i < dependLoc.length; i++) {
			for (L1PcInstance pc : getPlayMemberArray())
				try {
					if ((pc.getX() == dependLoc[i].getX()) && (pc.getY() == dependLoc[i].getY()))
						count++;
				} catch (Exception e) {
					
				}
		}
		if (count == dependLoc.length) {
			return Boolean.valueOf(true);
		}
		return Boolean.valueOf(false);
	}

	public Boolean attackTrap() {
		checkMember();
		if (attackLoc == null)
			return Boolean.valueOf(false);
		boolean tr = false;
		for (L1PcInstance pc : getPlayMemberArray()) {
			try {
				if ((pc.getX() == attackLoc.getX()) && (pc.getY() == attackLoc.getY()))
					tr = true;
			} catch (Exception e) {
				
			}
		}
		return Boolean.valueOf(tr);
	}

	private void End() {
		setInDunOpen(false);
		setInDunStart(false);
		EndTelePort();
		InDunclear();
		clearPlayMember();
//AUTO SRM: 		L1World.getInstance().broadcastServerMessage("해상던전이 초기화 되였습니다.", true); // CHECKED OK
		L1World.getInstance().broadcastServerMessage(S_SystemMessage.getRefText(49), true);
	}
}

