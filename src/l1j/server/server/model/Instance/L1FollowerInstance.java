package l1j.server.server.model.Instance;

import java.lang.reflect.Constructor;

import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.object.S_NPCObject;
import l1j.server.server.templates.L1Npc;

public class L1FollowerInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	@Override
	public boolean noTarget() {
		L1NpcInstance npc = null;
		L1PcInstance pc = null;
		for (L1Object object : L1World.getInstance().getVisibleObjects(this)) {
			if (object == null) {
				continue;
			}
			if (object instanceof L1NpcInstance) {
				npc = (L1NpcInstance) object;
				switch(npc.getNpcTemplate(). getNpcId()){
				case 70740: //디가르뎅의 병사
				case 71093: //조사원
					setParalyzed(true);
					pc = (L1PcInstance) _master;
					if (!pc.getInventory().checkItem(40593)) {
						pc.getInventory().createItem(40593, 1);
					}
					deleteMe();
					return true;
				case 71094: //엔디아
					setParalyzed(true);
					pc = (L1PcInstance) _master;
					if (!pc.getInventory().checkItem(40582)) {
						pc.getInventory().createItem(40582, 1);
					}
					deleteMe();
					return true;
				case 71061:
				case 71062:
					if (getLocation().getTileLineDistance(_master.getLocation()) < 3) {
						pc = (L1PcInstance) _master;
						if ((pc.getX() >= 32448 && pc.getX() <= 32452) // 모퉁이 모스 주변 좌표
								&& (pc.getY() >= 33048 && pc.getY() <= 33052) && (pc.getMapId() == 440)) {
							setParalyzed(true);
							if (!pc.getInventory().checkItem(40711)) {
								pc.getInventory().createItem(40711, 1);
								pc.getQuest().setStep(L1Quest.QUEST_CADMUS, 3);
							}
							deleteMe();
							return true;
						}
					}
					break;
				case 71074:
				case 71075:
					// 완전히 지쳐 버린 리자드만파이타
					if (getLocation().getTileLineDistance(_master.getLocation()) < 3) {
						pc = (L1PcInstance) _master;
						if ((pc.getX() >= 32731 && pc.getX() <= 32735) // 리자드만장로 주변
								&& (pc.getY() >= 32854 && pc.getY() <= 32858) && (pc.getMapId() == 480)) {
							setParalyzed(true);
							if (!pc.getInventory().checkItem(40633)) {
								pc.getInventory().createItem(40633, 1);
								pc.getQuest().setStep(L1Quest.QUEST_LIZARD, 2);
							}
							deleteMe();
							return true;
						}
					}
					break;
				case 70964:
				case 70957:
					if (getLocation().getTileLineDistance(_master.getLocation()) < 3) {
						pc = (L1PcInstance) _master;
						if ((pc.getX() >= 32917 && pc.getX() <= 32921) // 밧슈 주변 좌표
								&& (pc.getY() >= 32974 && pc.getY() <= 32978) && (pc.getMapId() == 410)) {
							setParalyzed(true);
							pc.getInventory().createItem(41003, 1);
							pc.getQuest().setStep(L1Quest.QUEST_ROI, 0);
							deleteMe();
							return true;
						}
					}
					break;
				}
			}
		}

		if (_master.isDead() || getLocation().getTileLineDistance(_master.getLocation()) > 10) {
			setParalyzed(true);
			spawn(getNpcTemplate().getNpcId(), getX(), getY(), getMoveState().getHeading(), getMapId());
			deleteMe();
			return true;
		} else if (_master != null && _master.getMapId() == getMapId()) {
			if (getLocation().getTileLineDistance(_master.getLocation()) > 2) {
				setDirectionMove(moveDirection(_master.getMapId(), _master.getX(), _master.getY()));
				setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
			}
		}
		return false;
	}

	public L1FollowerInstance(L1Npc template, L1NpcInstance target, L1Character master) {
		super(template);

		_master = master;
		setId(IdFactory.getInstance().nextId());

		setMaster(master);
		setX(target.getX());
		setY(target.getY());
		setMap(target.getMapId());
		getMoveState().setHeading(target.getMoveState().getHeading());
		setLightSize(target.getLightSize());
		setMaxHp(500);
		setCurrentHp(500);

		target.setParalyzed(true);
		target.deleteMe();

		L1World.getInstance().storeObject(this);
		L1World.getInstance().addVisibleObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			onPerceive(pc);
		}
		startAI();
		master.addFollower(this);
	}

	@Override
	public synchronized void deleteMe() {
		_master.removeFollower(this);
		getMap().setPassable(getLocation(), true);
		super.deleteMe();
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
	public void onTalkAction(L1PcInstance player) {
		if (isDead()) {
			return;
		}
		switch(getNpcTemplate().getNpcId()){
		case 71093:
			player.sendPackets(new S_NPCTalkReturn(getId(), _master.equals(player) ? "searcherk2" : "searcherk4"), true);
			break;
		case 71094:
			player.sendPackets(new S_NPCTalkReturn(getId(), _master.equals(player) ? "endiaq2" : "endiaq4"), true);
			break;
		case 71062:
			player.sendPackets(new S_NPCTalkReturn(getId(), _master.equals(player) ? "kamit2" : "kamit1"), true);
			break;
		case 71075:
			player.sendPackets(new S_NPCTalkReturn(getId(), _master.equals(player) ? "llizard2" : "llizard1a"), true);
			break;
		case 70957:
			player.sendPackets(new S_NPCTalkReturn(getId(), _master.equals(player) ? "roi2" : "roi2"), true);
			break;
		}
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if (perceivedFrom == null || this == null) {
			return;
		}
		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(new S_NPCObject(this), true);
	}

	public void spawn(int npcId, int X, int Y, int H, short Map) {
		L1Npc l1npc = NpcTable.getInstance().getTemplate(npcId);
		if (l1npc != null) {
			L1NpcInstance mob = null;
			try {
				String implementationName = l1npc.getImpl();
				Constructor<?> _constructor = Class.forName((new StringBuilder()).append("l1j.server.server.model.Instance. ").append(implementationName).append("Instance").toString()).getConstructors()[0];
				mob = (L1NpcInstance) _constructor.newInstance(new Object[] { l1npc });
				mob.setId(IdFactory.getInstance().nextId());
				mob.setX(X);
				mob.setY(Y);
				mob.setHomeX(X);
				mob.setHomeY(Y);
				mob.setMap(Map);
				mob.getMoveState().setHeading(H);
				L1World world = L1World.getInstance();
				world.storeObject(mob);
				world.addVisibleObject(mob);
				L1Object object = world.findObject(mob.getId());
				L1QuestInstance newnpc = (L1QuestInstance) object;
				newnpc.onNpcAI();
				newnpc.getLight().turnOnOffLight();
				newnpc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}

