package l1j.server.server.model.Instance;

import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.templates.L1Npc;

public class L1FurnitureInstance extends L1NpcInstance {

	private static final long serialVersionUID = 1L;

	private int _itemObjId;

	public L1FurnitureInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance player) {}

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
			if (pc == null) {
				continue;
			}
			pc.removeKnownObject(this);
			pc.sendPackets(new S_RemoveObject(this), true);
		}
		removeAllKnownObjects();
	}

	public int getItemObjId() {
		return _itemObjId;
	}

	public void setItemObjId(int i) {
		_itemObjId = i;
	}

}

