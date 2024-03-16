package l1j.server.server.model.Instance;

import l1j.server.server.ActionCodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.templates.L1Npc;

public class L1TowerFromLfcInstance extends L1TowerInstance {
	private static final long serialVersionUID = 1L;

	public L1TowerFromLfcInstance(L1Npc template) {
		super(template);
	}

	public void init() {
		getMap().setPassable(getX(), getY(), false);
	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) {
		if (!(attacker instanceof L1PcInstance) || attacker == null)
			return;
		if (getCurrentHp() > 0 && !isDead()) {
			int newHp = getCurrentHp() - damage;
			if (newHp <= 0 && !isDead()) {
				_crackStatus = 0;
				setDeath(attacker);
			} else if (newHp > 0) {
				setCurrentHp(newHp);
				if ((getMaxHp() >> 2) > getCurrentHp()) {
					if (_crackStatus != 3) {
						sendView(new S_DoActionGFX(getId(), ActionCodes.ACTION_TowerCrack3));
						setActionStatus(ActionCodes.ACTION_TowerCrack3);
						_crackStatus = 3;
					}
				} else if (((getMaxHp() << 1) >> 2) > getCurrentHp()) {
					if (_crackStatus != 2) {
						sendView(new S_DoActionGFX(getId(), ActionCodes.ACTION_TowerCrack2));
						setActionStatus(ActionCodes.ACTION_TowerCrack2);
						_crackStatus = 2;
					}
				} else if (((getMaxHp() * 3) >> 2) > getCurrentHp()) {
					if (_crackStatus != 1) {
						sendView(new S_DoActionGFX(getId(), ActionCodes.ACTION_TowerCrack1));
						setActionStatus(ActionCodes.ACTION_TowerCrack1);
						_crackStatus = 1;
					}
				}
			}
		} else if (!isDead())
			setDeath(attacker);
	}

	private void setDeath(L1Character attacker) {
		setDead(true);
		setActionStatus(ActionCodes.ACTION_TowerDie);
		getMap().setPassable(getX(), getY(), true);
		_lastattacker = attacker;
		sendDead();
	}

	public void sendDead() {
		sendView(new S_DoActionGFX(getId(), ActionCodes.ACTION_TowerDie));
	}

	private void sendView(ServerBasePacket packet) {
		broadcastPacket(packet);
	}
}

