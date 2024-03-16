package l1j.server.server.model.Instance;

import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.Config;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.action.S_ChangeHeading;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Npc;

public class L1PeopleInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	public L1PeopleInstance(L1Npc template) {
		super(template);
		_restCallCount = new AtomicInteger(0);
	}

	@Override
	public void onAction(L1PcInstance pc, int adddmg) {
		if (getCurrentHp() > 0 && !isDead()) {
			L1Attack attack = new L1Attack(pc, this);
			if (attack.calcHit()) {
				attack.calcDamage();
				attack.addPcPoisonAttack(pc, this);
			}
			attack.action();
			attack.commit();
			attack = null;
		}
	}

	@Override
	public void onAction(L1PcInstance pc) {
		if (getCurrentHp() > 0 && !isDead()) {
			L1Attack attack = new L1Attack(pc, this);
			if (attack.calcHit()) {
				attack.calcDamage();
				attack.addPcPoisonAttack(pc, this);
			}
			attack.action();
			attack.commit();
			attack = null;
		}
	}

	@Override
	public void onNpcAI() {
		if (isAiRunning()) {
			return;
		}
		_actived = false;
		startAI();
	}

	@Override
	public void onTalkAction(L1PcInstance pc) {
		int npcid = getNpcTemplate().getNpcId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(npcid);
		String htmlid = null;

		if (getNpcTemplate().isChangeHead()) {
			talkChangeHead(pc);
		}

		if (talking != null) {
			if (!pc.isElf()) {
				switch (npcid) {
				case 70839: // 도에트
					if (pc.isDarkelf()) {
						htmlid = "doettM2";
					} else if (pc.isDragonknight()) {
						htmlid = "doettM3";
					} else if (pc.isIllusionist()) {
						htmlid = "doettM4";
					} else {
						htmlid = "doettM1";
					}
					break;
				case 70854: // 후린달렌
					if (pc.isDarkelf()) {
						htmlid = "hurinE3";
					} else if (pc.isDragonknight()) {
						htmlid = "hurinE4";
					} else if (pc.isIllusionist()) {
						htmlid = "hurinE5";
					} else {
						htmlid = "hurinM1";
					}
					break;
				case 70843: // 모리엔
					if (pc.isDarkelf()) {
						htmlid = "morienM2";
					} else if (pc.isDragonknight()) {
						htmlid = "morienM3";
					} else if (pc.isIllusionist()) {
						htmlid = "morienM4";
					} else {
						htmlid = "morienM1";
					}
					break;
				case 70849: // 테오도르
					if (pc.isDarkelf()) {
						htmlid = "theodorM2";
					} else if (pc.isDragonknight()) {
						htmlid = "theodorM3";
					} else if (pc.isIllusionist()) {
						htmlid = "theodorM4";
					} else {
						htmlid = "theodorM1";
					}
					break;
				default:break;
				}
			}
			int objid = getId();
			// html 표시 패킷 송신
			if (htmlid != null) {// htmlid가 지정되고 있는 경우
				pc.sendPackets(new S_NPCTalkReturn(objid, htmlid), true);
				if (pc.isGm())
				  pc.sendPackets(new S_SystemMessage("Dialog " + htmlid), true);
			} else {
				pc.sendPackets(new S_NPCTalkReturn(talking, objid, pc.getAlignment() < -500 ? 2 : 1), true);
				if (pc.isGm())
				  pc.sendPackets(new S_SystemMessage("Dialog " + talking.getCaoticAction() + " | " + talking.getNormalAction()), true);				
			}
		}
		htmlid = null;
	}

	@Override
	public void onFinalAction(L1PcInstance player, String action) {}

	public void doFinalAction(L1PcInstance player) {}

	private static final long REST_MILLISEC = 10000;

	private AtomicInteger _restCallCount;
	
	private void talkChangeHead(L1PcInstance pc){
		int pcX		= pc.getX();
		int pcY		= pc.getY();
		int npcX	= getX();
		int npcY	= getY();
		if (pcX == npcX && pcY < npcY) {
			getMoveState().setHeading(0);
		} else if (pcX > npcX && pcY < npcY) {
			getMoveState().setHeading(1);
		} else if (pcX > npcX && pcY == npcY) {
			getMoveState().setHeading(2);
		} else if (pcX > npcX && pcY > npcY) {
			getMoveState().setHeading(3);
		} else if (pcX == npcX && pcY > npcY) {
			getMoveState().setHeading(4);
		} else if (pcX < npcX && pcY > npcY) {
			getMoveState().setHeading(5);
		} else if (pcX < npcX && pcY == npcY) {
			getMoveState().setHeading(6);
		} else if (pcX < npcX && pcY < npcY) {
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

	@Override
	public void receiveDamage(L1Character attacker, int damage) {
		if (isDead()) {
			return;
		}
		if (getCurrentHp() > 0 && !isDead()) {
			if (damage > 0) { 
				setHate(attacker, 0);
				removeSleepSkill();
			}

			if (attacker instanceof L1PcInstance && damage > 0) {
				L1PcInstance pc = (L1PcInstance) attacker;
				pc.setPetTarget(this);
			}

			int newHp = getCurrentHp() - damage;
			if (newHp <= 0 && !isDead()) {
				setCurrentHp(0);
				setDead(true);
				setActionStatus(ActionCodes.ACTION_Die);
				Death death = new Death(attacker);
				GeneralThreadPool.getInstance().execute(death);
			}
			if (newHp > 0) {
				setCurrentHp(newHp);
			}
		} else {
			setCurrentHp(0);
			setDead(true);
			setActionStatus(ActionCodes.ACTION_Die);
			Death death = new Death(attacker);
			GeneralThreadPool.getInstance().execute(death);
		}
	}

	class Death implements Runnable {
		L1Character _lastAttacker;

		public Death(L1Character lastAttacker) {
			_lastAttacker = lastAttacker;
		}

		@Override
		public void run() {
			try {
				setDeathProcessing(true);
				setCurrentHp(0);
				setDead(true);
				setActionStatus(ActionCodes.ACTION_Die);
				getMap().setPassable(getLocation(), true);
				Broadcaster.broadcastPacket(L1PeopleInstance.this, new S_DoActionGFX(getId(), ActionCodes.ACTION_Die), true);

				startChat(CHAT_TIMING_DEAD);
				int align = 5000;
				if (align > 0) {
					_lastAttacker.addAlignment(-align);
				}
				setDeathProcessing(false);
				allTargetClear();
				startDeleteTimer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}

