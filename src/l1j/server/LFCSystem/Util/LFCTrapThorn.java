package l1j.server.LFCSystem.Util;

import l1j.server.LFCSystem.Loader.InstanceLoadManager;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;

public class LFCTrapThorn {
	private L1NpcInstance 	_shadow;
	private L1NpcInstance 	_thorn;
	public LFCTrapThorn(L1NpcInstance shadow, L1NpcInstance thorn){
		_shadow 	= shadow;
		_thorn		= thorn;
	}
	
	public LFCTrapThorn(int mapId, int x, int y){
		_shadow		= spawn(InstanceLoadManager.MIS_SP_THORNSHADOW_ID, mapId, x, y);
		_thorn		= spawn(InstanceLoadManager.MIS_SP_THORN_ID, mapId, x, y);
	}
	
	public L1NpcInstance getShadowNpc(){
		return _shadow;
	}
	
	public L1NpcInstance getthornNpc(){
		return _thorn;
	}
	
	private L1NpcInstance spawn(int npcId, int mapId, int x, int y){
		L1NpcInstance npc = null;
		try {
			npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap((short)mapId);
			npc.getLocation().forward(0);
			npc.setX(x);
			npc.setY(y);
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(0);
			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);	
		} catch (Exception e) {
			e.printStackTrace();
			npc = null;
		}
		return npc;
	}
	
	public void delete(){
		if (_shadow != null)
			_shadow.deleteMe();
		if (_thorn != null)
			_thorn.deleteMe();
	}
}

