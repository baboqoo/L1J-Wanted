package l1j.server.server.model.Instance;

import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.object.S_NPCObject;
import l1j.server.server.templates.L1Npc;

public class L1TreasureInstance extends L1MonsterInstance {
	private static final long serialVersionUID = 1L;

	private boolean _excavation = false;
	public boolean isExcavation(){
		return _excavation;
	}
	public void setExcavation(){
		_excavation = true;
		broadcastPacket(new S_NPCObject(this), true);// 상태 변경 전달
	}
	
	public L1TreasureInstance(L1Npc template) {
		super(template);
	}
	
	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if (perceivedFrom == null || this == null) {
			return;
		}
		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(new S_NPCObject(this), true);
	}

	@Override
	public void onAction(L1PcInstance pc) {
		;
	}

	@Override
	public void onTalkAction(L1PcInstance pc) {
		;
	}

	@Override
	public void deleteMe() {
		_destroyed = true;
		if (getInventory() != null) {
			getInventory().clearItems();
		}
		L1World world = L1World.getInstance();
		world.removeVisibleObject(this);
		world.removeObject(this);
		for (L1PcInstance pc : world.getRecognizePlayer(this)) {
			pc.removeKnownObject(this);
			pc.sendPackets(new S_RemoveObject(this), true);
		}
		removeAllKnownObjects();
	}
}

