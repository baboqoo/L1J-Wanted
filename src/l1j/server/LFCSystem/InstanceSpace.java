package l1j.server.LFCSystem;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import l1j.server.LFCSystem.Loader.InstanceLoadManager;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;

public class InstanceSpace {
	private static Object	_lock 	= new Object();
	
	private static InstanceSpace 	_instance;
	public static InstanceSpace getInstance(){
		if (_instance == null)
			_instance = new InstanceSpace();
		return _instance;
	}
	
	public static void release(){
		if (_instance != null){
			_instance.clear();
			_instance = null;
		}
	}
	
	public static void reload(){
		InstanceSpace tmp = _instance;
		_instance = new InstanceSpace();
		tmp.clear();
		tmp = null;
	}
	
	private ArrayDeque<Integer>					_mapQ;
	private HashMap<Integer, InstanceObject> 	_objMap;
	private InstanceSpace(){
		_mapQ = new ArrayDeque<Integer>(InstanceLoadManager.MIS_COPYMAP_SIZE);
		int size = InstanceLoadManager.MIS_COPYMAP_START_ID + InstanceLoadManager.MIS_COPYMAP_SIZE;
		for (int i = InstanceLoadManager.MIS_COPYMAP_START_ID; i < size; i++)
			_mapQ.push(i);
		
		_objMap = new HashMap<Integer, InstanceObject>(InstanceLoadManager.MIS_COPYMAP_SIZE);
	}
	
	private L1Map popMap(InstanceObject obj){
		L1Map map = null;
		synchronized(_lock){
			if (_mapQ.isEmpty()){
				obj.notifySizeOver();
				return null;
			}
			
			if (obj.getBaseMapId() == -1)
				return null;
			
			map = L1WorldMap.getInstance().cloneMap(obj.getBaseMapId(), _mapQ.pop());
			obj.setCopyMap(map);
			_objMap.put(obj.getCopyMapId(), obj);
		}
		
		return map;
	}
	
	private void pushMap(InstanceObject obj){
		L1Map map = null;
		synchronized(_lock){
			map = obj.getCopyMap();
			if (map == null)
				return;
			
			_objMap.put(map.getId(), null);
			_mapQ.push(map.getId());
			L1WorldMap.getInstance().removeMap(map.getId());
		}
	}
	
	public void startInstance(InstanceObject obj){
		if (_mapQ.isEmpty()){
			obj.notifySizeOver();
			return;
		}
		
		if (popMap(obj) == null)
			return;
		
		try {
			obj.init();
			GeneralThreadPool.getInstance().execute(obj);
		} catch(Exception e){
			e.printStackTrace();
			obj.dispose();
		}
	}
	
	public void releaseInstance(InstanceObject obj){
		if (obj == null || obj.getCopyMapId() == -1)
			return;
		
		try {
			
			int copyMapId = obj.getCopyMapId();
			Iterator<L1Object> it = L1World.getInstance().getVisibleObjects(copyMapId).values().iterator();
			while(it.hasNext()){
				L1Object l1obj = it.next();
				if ((l1obj instanceof L1DollInstance) || (l1obj instanceof L1SummonInstance))
					continue;
				
				if ((l1obj instanceof L1NpcInstance))
					((L1NpcInstance) l1obj).deleteMe();
			}
			
			Collection<L1ItemInstance> items 			= L1World.getInstance().getAllItem();
			L1ItemInstance[] 				itemArray 	= items.toArray(new L1ItemInstance[items.size()]);
			L1ItemInstance 				item			= null;
			L1Inventory						inv				=	 null;
			for (int i = 0; i < itemArray.length; i++){
				item = itemArray[i];
				if (item == null)
					continue;
				
				if (copyMapId != item.getMapId())
					continue;
				
				inv = L1World.getInstance().getInventory(item.getX(), item.getY(), (short)copyMapId);
				if (inv == null)
					continue;
				
				inv.removeItem(item);
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		pushMap(obj);
		obj.dispose();
		obj = null;
	}
	
	public static boolean isInInstance(L1PcInstance pc){
		return isInInstance(pc.getMapId());
	}
	
	public static boolean isInInstance(int mapId){
		int size	= InstanceLoadManager.MIS_COPYMAP_START_ID + InstanceLoadManager.MIS_COPYMAP_SIZE;
		return (mapId >= InstanceLoadManager.MIS_COPYMAP_START_ID && mapId < size);
	}
	
	public void getBackPc(L1PcInstance pc){
		if (isInInstance(pc)){
			pc.setMap((short)InstanceLoadManager.MIS_ERRBACK_MAPID);
			pc.setX(InstanceLoadManager.MIS_ERRBACK_X);
			pc.setY(InstanceLoadManager.MIS_ERRBACK_Y);
		}
	}
	
	public int getIdenMap(int mapId){
		if (isInInstance(mapId)){
			InstanceObject obj = _objMap.get(mapId);
			if (obj == null)
				return mapId;
			
			return obj.getBaseMapId();
		}
		
		return mapId;
	}
	
	public void clear(){
		if (_objMap != null){
			Iterator<InstanceObject> 	it 	= _objMap.values().iterator();
			InstanceObject 			obj	= null;
			while(it.hasNext()){
				obj = it.next();
				releaseInstance(obj);
			}
			
			_objMap.clear();
			_objMap = null;
		}
		
		if (_mapQ != null){
			_mapQ.clear();
			_mapQ = null;
		}
	}
	
	public static void teleportInstance(L1PcInstance pc, short mapId, int x, int y){
		pc.getTeleport().start(x, y, (short)mapId, pc.getMoveState().getHeading(), true);
	}
	
	public InstanceObject getOpenObject(int mapid){
		return _objMap.get(mapid);
	}
	
	public int getOpenSize(){
		return _objMap.size();
	}
	
	public Collection<InstanceObject> getOpens(){
		return _objMap.values();
	}
	
	public Collection<Integer> getOpensMaps(){
		return _objMap.keySet();
	}
}

