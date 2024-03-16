package l1j.server.server.model.Instance;

import java.util.logging.Logger;

import l1j.server.server.ActionCodes;
import l1j.server.server.controller.action.War;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.object.S_NPCObject;
import l1j.server.server.templates.L1Npc;

public class L1CataInstance extends L1NpcInstance {
	public L1CataInstance(L1Npc template) {
		super(template);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(L1CataInstance.class.getName());

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if (perceivedFrom == null || this == null) {
			return;
		}
		if (0 < getCurrentHp()) {
			onNpcAI();
		}
		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(new S_NPCObject(this), true);
	}

	@SuppressWarnings("unused")
	@Override
	public void onTalkAction(L1PcInstance pc) {
		if (pc == null) {
			return;
		}
		int objid = getId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().getNpcId());
		String htmlid = null;
		String[] htmldata = null;

		if (talking != null) {
			if (!pc.isCrown()) {
				pc.sendPackets(new S_ServerMessage(2498), true); // 투석기 사용: 실패(혈맹 군주만사용 가능)
				return;
			}
		}
		// html 표시 패킷 송신
		if (htmlid != null) { // htmlid가 지정되고 있는 경우
			if (htmldata != null) {// html 지정이 있는 경우는 표시
				pc.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata), true);
			} else {
				pc.sendPackets(new S_NPCTalkReturn(objid, htmlid), true);
			}
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + htmlid), true);							

		} else {
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + talking.getCaoticAction() + " | " + talking.getNormalAction()), true);							
			pc.sendPackets(new S_NPCTalkReturn(talking, objid, pc.getAlignment() < -500 ? 2 : 1), true);
		}
	}

	@Override
	public void onAction(L1PcInstance pc) {
		if (pc == null) {
			return;
		}
		if (getCurrentHp() > 0 && !isDead()) {
			L1Attack attack = new L1Attack(pc, this);
			if (attack.calcHit()) {
				attack.calcDamage();
				attack.addPcPoisonAttack(pc, this);
			}
			attack.action();
			attack.commit();
		}
	}

	@Override
	public void receiveManaDamage(L1Character attacker, int mpDamage) {
		if (attacker == null) {
			return;
		}
		if (mpDamage > 0 && !isDead()) {
			onNpcAI();
			int newMp = getCurrentMp() - mpDamage;
			if (newMp < 0) {
				newMp = 0;
			}
			setCurrentMp(newMp);
		}
	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) {
		if (attacker == null) {
			return;
		}
		int npcId = getNpcId();
		int castleid = 0;
		if (npcId == 7000084 || npcId == 7000085) {// 켄트
			castleid = 1;
		} else if (npcId == 7000086 || npcId == 7000087) {// 오성
			castleid = 2;
		} else if (npcId == 7000082 || npcId == 7000083) {// 기란
			castleid = 4;
		}
		boolean isNowWar = false;
		isNowWar = War.getInstance().isNowWar(castleid);
		if (!isNowWar) {
			return;
		}
		if (getCurrentHp() > 0 && !isDead()) {
			if (damage > 0) {
				removeSleepSkill();
			}
			int newHp = getCurrentHp() - damage;
			if (newHp <= 0 && !isDead()) {
				int transformId = getNpcTemplate().getTransformId();
				if (transformId == -1) {
					setCurrentHp(0);
					setDead(true);
					setActionStatus(ActionCodes.ACTION_Die);
					die(this);
				}
			}
		} else if (!isDead()) {
			setDead(true);
			setActionStatus(ActionCodes.ACTION_Die);
			die(this);
		}
	}

	@Override
	public void setCurrentHp(int i) {
		super.setCurrentHp(i);
	}

	@Override
	public void setCurrentMp(int i) {
		super.setCurrentMp(i);
	}

	public void die(L1Character lastAttacker) {
		try {
			setDeathProcessing(true);
			setCurrentHp(0);
			setDead(true);
			setActionStatus(ActionCodes.ACTION_Die);
			this.broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Die), true);
			setDeathProcessing(false);
		} catch (Exception e) {
		}
	}
}
