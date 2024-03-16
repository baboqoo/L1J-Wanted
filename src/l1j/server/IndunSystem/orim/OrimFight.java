package l1j.server.IndunSystem.orim;

import java.util.ArrayList;
import java.util.Iterator;

import l1j.server.common.data.ChatType;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;

class OrimFight extends Thread {
	private static OrimFight _instance;
	public static OrimFight getInstance(int type) {
		if (_instance == null)_instance = new OrimFight(type);
		return _instance;
	}
	
	public L1NpcInstance _oldin;
	private int type;
	L1PcInstance summonChar;

	private ArrayList<L1NpcInstance> _mimikList = new ArrayList<L1NpcInstance>();

	OrimFight(int t) {
		type = t;
	}

	public void run() {
		int random = CommonUtil.random(OrimController.getInstance().getPlayMembersCount());
		int ranDie = CommonUtil.random(3) + 1;
		summonChar = OrimController.getInstance().getPlayMemberArray()[random];
		if (!summonChar.isDead()) {
			switch (type) {
			case 0:
				for (int i = 0; i < 3; i++) {
					L1SpawnUtil.spawn5(32673, 32801, (short) 9101, 4, 91222, 8);
				}
				L1SpawnUtil.spawn5(32676, 32796, (short)9101, 4, 91243, 1);
				L1SpawnUtil.spawn5(32676, 32796, (short)9101, 4, 91235, 1);

				summonChar.getTeleport().start(32673, 32801, (short)9101, 2, true);
				break;
			case 1:
				for (int i = 0; i < 3; i++) {
					L1SpawnUtil.spawn5(32735, 32862, (short)9101, 4, 91222, 8);
				}
				L1SpawnUtil.spawn5(32739, 32857, (short)9101, 4, 91243, 1);
				L1SpawnUtil.spawn5(32739, 32857, (short)9101, 4, 91235, 1);

				summonChar.getTeleport().start(32735, 32862, (short)9101, 2, true);
				break;
			case 2:
				for (int i = 0; i < 3; i++) {
					L1SpawnUtil.spawn5(32799, 32863, (short)9101, 4, 91222, 8);
				}
				L1SpawnUtil.spawn5(32804, 32857, (short)9101, 4, 91243, 1);
				L1SpawnUtil.spawn5(32804, 32857, (short)9101, 4, 91235, 1);
				
				summonChar.getTeleport().start(32799, 32863, (short)9101, 2, true);
			}

			getOwnNpc();
			oldinMSG(1);
			try {
				Thread.sleep(8000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			oldinMSG(2);
			int boundTime = 0;
			while ((dieMimik() < ranDie) && (OrimController.getInstance().getInDunOpen())) {
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				boundTime++;
				if (boundTime == 15)
					break;
			}
			if(boundTime == 15)	
				summonChar.getTeleport().start(32582, 32927, (short)0, 4, true);
			else
				summonChar.getTeleport().start(32797, 32801, (short)9101, 4, true);
			ownEnd();
		}
	}

	private void getOwnNpc() {
		L1NpcInstance npc = null;
		for (L1Object obj : L1World.getInstance().getObject())
			if ((obj instanceof L1NpcInstance)) {
				npc = (L1NpcInstance) obj;
				if (npc.getNpcId() == 91235)
					_oldin = npc;
				else if (npc.getNpcId() == 91222)
					_mimikList.add(npc);
			}
	}

	int dieMimik() {
		int dieCount = 0;
		for (int i = 0; i < _mimikList.size(); i++) {
			if (((L1NpcInstance) _mimikList.get(i)).isDead()) {
				dieCount++;
			}
		}
		return dieCount;
	}

	private void oldinMSG(int i) {
		switch (i) {
		case 1:
			//Broadcaster.broadcastPacket(_oldin, new S_NpcChatPacket(_oldin, "자네는 지금 오림에게 속고 있는거야.", ChatType.CHAT_NORMAL), true);
			Broadcaster.broadcastPacket(_oldin, new S_NpcChatPacket(_oldin, "$12123", ChatType.CHAT_NORMAL), true);
			break;
		case 2:
			//Broadcaster.broadcastPacket(_oldin, new S_NpcChatPacket(_oldin, "그를 믿어서는 안돼!. 어서 이 배를 빠져 나가 말하는 섬으로 돌아가!", ChatType.CHAT_NORMAL), true);
			Broadcaster.broadcastPacket(_oldin, new S_NpcChatPacket(_oldin, "$12124", ChatType.CHAT_NORMAL), true);
			break;
		}
	}

	private void ownEnd() {
		for (int i = 0; i < _mimikList.size(); i++) {
			if (!((L1NpcInstance) _mimikList.get(i)).isDead()) {
				((L1NpcInstance) _mimikList.get(i)).setDead(true);
				((L1NpcInstance) _mimikList.get(i)).setActionStatus(8);
				((L1NpcInstance) _mimikList.get(i)).setCurrentHp(0);
				((L1NpcInstance) _mimikList.get(i)).deleteMe();
			}
		}
		_mimikList.clear();

		Iterator<L1Object> it = L1World.getInstance().getVisibleObjects(9101).values().iterator();
		L1NpcInstance npc	= null;
		L1Object obj		= null;
		while (it.hasNext()) {
			obj = it.next();
			if (obj instanceof L1NpcInstance) {
				npc = (L1NpcInstance) obj;
				if (npc.getNpcTemplate().getNpcId() != 91243 && npc.getNpcTemplate().getNpcId() != 91235) 
					continue;
				npc.deleteMe();
			}

		}

		summonChar = null;
		_instance = null;
	}
}
