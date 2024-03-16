package l1j.server.server.model.Instance;

import java.util.ArrayList;

import l1j.server.server.ActionCodes;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.action.S_UseArrowSkill;
import l1j.server.server.serverpackets.object.S_NPCObject;
import l1j.server.server.templates.L1Npc;

public class L1ArrowInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	public L1ArrowInstance(L1Npc template) {
		super(template);
	}

	private int _targetX, _targetY;
	private boolean _action;

	public void setTarX(int x) {
		_targetX = x;
	}
	public int getTarX() {
		return _targetX;
	}

	public void setTarY(int y) {
		_targetY = y;
	}
	public int getTarY() {
		return _targetY;
	}

	public boolean getAction() {
		return _action;
	}
	public void setAction(boolean a) {
		_action = a;
	}

	public void ai() {
		L1PcInstance target = null;
		int start = 0, end = 0;
		boolean ck = false;
		ArrayList<L1PcInstance> list = L1World.getInstance().getVisiblePlayer(this);
		for (L1PcInstance pc : list) {
			if (pc == null) {
				continue;
			}
			if (getX() == getTarX()) {
				if (getY() < getTarY()) {
					start = getY();
					end = getTarY();
				} else {
					start = getTarY();
					end = getY();
				}
			} else {
				if (getX() < getTarX()) {
					start = getX();
					end = getTarX();
				} else {
					start = getTarX();
					end = getX();
				}
			}
			if (getX() == pc.getX() && getY() == pc.getY()) {
				ck = true;
				target = pc;
				break;
			}
			for (int i = start; i < end; i++) {
				if ((pc.getX() == getX() && pc.getY() == i) || (pc.getX() == i && pc.getY() == getY())) {
					ck = true;
					target = pc;
					break;
				}
			}
			if (ck) {
				break;
			}
		}
		if (list.size() > 0) {
			if (!ck) {// 화살 GFX 66
				broadcastPacket(new S_UseArrowSkill(this, 0, 66, getTarX(), getTarY(), false), true);
			} else {
				broadcastPacket(new S_UseArrowSkill(this, target.getId(), 66, target.getX(), target.getY(), true), true);
				broadcastPacketExceptTargetSight(new S_DoActionGFX(target.getId(), ActionCodes.ACTION_Damage), this, true);
				target.setCurrentHp(target.getCurrentHp() > 5 ? target.getCurrentHp() - 5 : 1); // 본섭은 Dmg 5
			}
		}
	}
	
	private ServerBasePacket _objPck;
	
	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if (perceivedFrom == null || this == null) {
			return;
		}
		perceivedFrom.addKnownObject(this);
		if (_objPck == null) {
			_objPck = new S_NPCObject(this);
		}
		perceivedFrom.sendPackets(_objPck);
	}

	@Override
	public void deleteMe() {
		_destroyed = true;
		allTargetClear();
		_master = null;
		L1World world = L1World.getInstance();
		world.removeVisibleObject(this);
		world.removeObject(this);
		for (L1PcInstance pc : world.getRecognizePlayer(this)) {
			pc.removeKnownObject(this);
			pc.sendPackets(new S_RemoveObject(this), true);
		}
		if (_objPck != null) {
			_objPck.close();
			_objPck = null;
		}
		removeAllKnownObjects();
	}

}

