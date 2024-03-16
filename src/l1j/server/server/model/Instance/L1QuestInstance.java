package l1j.server.server.model.Instance;

import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Quest;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.action.S_ChangeHeading;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.StringUtil;

public class L1QuestInstance extends L1NpcInstance {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public L1QuestInstance(L1Npc template) {
		super(template);
		_restCallCount = new AtomicInteger(0);
	}

	@Override
	public void onNpcAI() {
		int npcId = getNpcTemplate().getNpcId();
		if (isAiRunning()) {
			return;
		}
		if (npcId == 71075 || npcId == 70957 || npcId == 81209) {
			return;
		}
		_actived = false;
		startAI();
	}

	@Override
	public void onAction(L1PcInstance pc) {
		L1Attack attack = new L1Attack(pc, this);
		if (attack.calcHit()) {
			attack.calcDamage();
			attack.addPcPoisonAttack(pc, this);
		}
		attack.action();
		attack.commit();
	}

	@Override
	public void onTalkAction(L1PcInstance pc) {
		int npcId = getNpcTemplate().getNpcId();
		String htmlid = null;

		if (npcId == 71092 || npcId == 71093) {
			htmlid = pc.isKnight() && pc.getQuest().getStep(L1Quest.QUEST_LEVEL45) == 4 ? "searcherk1" : "searcherk4";
		} else if (npcId == 71094) {
			htmlid = pc.isDarkelf() && pc.getQuest().getStep(L1Quest.QUEST_LEVEL50) == 1 ? "endiaq1" : "endiaq4";
		} else if (npcId == 71062) {
			htmlid = pc.getQuest().getStep(L1Quest.QUEST_CADMUS) == 2 ? "kamit1b" : "kamit1";
		} else if (npcId == 71075) {
			htmlid = pc.getQuest().getStep(L1Quest.QUEST_LIZARD) == 1 ? "llizard1b" : "llizard1a";
		} else if (npcId == 70957 || npcId == 81209) {
			htmlid = pc.getQuest().getStep(L1Quest.QUEST_ROI) != 1 ? "roi1" : "roi2";
		}
		
		if (htmlid != null) {
			pc.sendPackets(new S_NPCTalkReturn(getId(), htmlid), true);  
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + htmlid), true);
		}
		
		talkChangeHead(pc);
	}

	@Override
	public void onFinalAction(L1PcInstance pc, String action) {
		if (action.equalsIgnoreCase("start")) {
			int npcId = getNpcTemplate().getNpcId();
			@SuppressWarnings("unused")
			L1FollowerInstance follow = null;
			if ((npcId == 71092 || npcId == 71093) && pc.isKnight() && pc.getQuest().getStep(L1Quest.QUEST_LEVEL45) == 4) {
				L1Npc l1npc = NpcTable.getInstance().getTemplate(71093);
				follow = new L1FollowerInstance(l1npc, this, pc);
				pc.sendPackets(new S_NPCTalkReturn(getId(), StringUtil.EmptyString), true);
			} else if (npcId == 71094 && pc.isDarkelf() && pc.getQuest().getStep(L1Quest.QUEST_LEVEL50) == 1) {
				L1Npc l1npc = NpcTable.getInstance().getTemplate(71094);
				follow = new L1FollowerInstance(l1npc, this, pc);
				pc.sendPackets(new S_NPCTalkReturn(getId(), StringUtil.EmptyString), true);
			} else if (npcId == 71062 && pc.getQuest().getStep(L1Quest.QUEST_CADMUS) == 2) {
				L1Npc l1npc = NpcTable.getInstance().getTemplate(71062);
				if (pc.getFollowerList().size() < 1) {
					follow = new L1FollowerInstance(l1npc, this, pc);
				}
				pc.sendPackets(new S_NPCTalkReturn(getId(), StringUtil.EmptyString), true);
			} else if (npcId == 71075 && pc.getQuest().getStep(L1Quest.QUEST_LIZARD) == 1) {
				L1Npc l1npc = NpcTable.getInstance().getTemplate(71075);
				follow = new L1FollowerInstance(l1npc, this, pc);
				pc.sendPackets(new S_NPCTalkReturn(getId(), StringUtil.EmptyString), true);
			} else if (npcId == 70957 || npcId == 81209) {
				L1Npc l1npc = NpcTable.getInstance().getTemplate(70957);
				follow = new L1FollowerInstance(l1npc, this, pc);
				pc.sendPackets(new S_NPCTalkReturn(getId(), StringUtil.EmptyString), true);
			}
		}
	}

	private static final long REST_MILLISEC = 10000;

	private AtomicInteger _restCallCount;
	
	private void talkChangeHead(L1PcInstance pc){
		int pcx		= pc.getX();// PC의 X좌표
		int pcy		= pc.getY();// PC의 Y좌표
		int npcx	= getX();// NPC의 X좌표
		int npcy	= getY();// NPC의 Y좌표
		if (pcx == npcx && pcy < npcy) {
			getMoveState().setHeading(0);
		} else if (pcx > npcx && pcy < npcy) {
			getMoveState().setHeading(1);
		} else if (pcx > npcx && pcy == npcy) {
			getMoveState().setHeading(2);
		} else if (pcx > npcx && pcy > npcy) {
			getMoveState().setHeading(3);
		} else if (pcx == npcx && pcy > npcy) {
			getMoveState().setHeading(4);
		} else if (pcx < npcx && pcy > npcy) {
			getMoveState().setHeading(5);
		} else if (pcx < npcx && pcy == npcy) {
			getMoveState().setHeading(6);
		} else if (pcx < npcx && pcy < npcy) {
			getMoveState().setHeading(7);
		}
		broadcastPacket(new S_ChangeHeading(this), true);
		if (_restCallCount.getAndIncrement() == 0) {
			setRest(true);
		}
		GeneralThreadPool.getInstance().schedule(new RestMonitor(), REST_MILLISEC);
	}

	public class RestMonitor implements Runnable {
		@Override
		public void run() {
			if (_restCallCount.decrementAndGet() == 0) {
				setRest(false);
			}
		}
	}
}

