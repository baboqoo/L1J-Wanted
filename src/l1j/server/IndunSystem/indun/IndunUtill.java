package l1j.server.IndunSystem.indun;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.ActionCodes;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;

public class IndunUtill {
	private static Logger _log = Logger.getLogger(IndunUtill.class.getName());
	private static final FastMap<Integer, FastTable<IndunSpawnObject>> SPAWN_DATA = new FastMap<Integer, FastTable<IndunSpawnObject>>();
	
	private static IndunUtill _instance;
	public static IndunUtill getInstance(){
		if (_instance == null) {
			_instance = new IndunUtill();
		}
		return _instance;
	}
	private IndunUtill(){
		load();
	}
	
	public ArrayList<L1NpcInstance> spawnList(int mapid, int type, int timeMillisToDelete) {
		ArrayList<L1NpcInstance> npcList = new ArrayList<L1NpcInstance>();
		L1Npc l1npc				= null;
		L1NpcInstance npc		= null;
		for (IndunSpawnObject obj : SPAWN_DATA.get(type)) {
			l1npc = obj.getTemplate();
			if (l1npc != null) {
				try {
					npc = NpcTable.getInstance().newNpcInstance(l1npc.getNpcId());
					npc.setId(IdFactory.getInstance().nextId());
					if (type == 22 || type == 24 || type == 38 || (type >= 32 && type <= 37)) {
						npc.setX(obj.getLocX() + CommonUtil.random(5) - CommonUtil.random(5));
						npc.setY(obj.getLocY() + CommonUtil.random(5) - CommonUtil.random(5));
					} else {
						npc.setX(obj.getLocX());
						npc.setY(obj.getLocY());
					}
					npc.setMap((short) mapid);
					npc.setHomeX(npc.getX());
					npc.setHomeY(npc.getY());
					npc.getMoveState().setHeading(obj.getHeaing());
					npc.setLightSize(l1npc.getLightSize());
					npc.getLight().turnOnOffLight();
					
					npc.setActionStatus(ActionCodes.ACTION_Appear);
					S_CharVisualUpdate visual = new S_CharVisualUpdate(npc, 0);
					for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc)) {
						npc.onPerceive(pc);
						pc.sendPackets(visual);
					}
					npc.setActionStatus(0);
					visual.clear();
					visual = null;
					
					L1World.getInstance().storeObject(npc);
					L1World.getInstance().addVisibleObject(npc);
					npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);// 채팅 개시
					npc.onNpcAI();
					if (0 < timeMillisToDelete) {
						L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);
						timer.begin();
					}
					npcList.add(npc);
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
		}
		return npcList;
	}
	
	public L1NpcInstance spawn(int mapid, int type, int timeMillisToDelete) {
		L1Npc l1npc				= null;
		L1NpcInstance npc		= null;
		for (IndunSpawnObject obj : SPAWN_DATA.get(type)) {
			l1npc = obj.getTemplate();
			if (l1npc != null) {
				try {
					npc = NpcTable.getInstance().newNpcInstance(l1npc.getNpcId());
					npc.setId(IdFactory.getInstance().nextId());
					if (type == 22 || type == 24 || type == 38 || (type >= 32 && type <= 37)) {
						npc.setX(obj.getLocX() + CommonUtil.random(5) - CommonUtil.random(5));
						npc.setY(obj.getLocY() + CommonUtil.random(5) - CommonUtil.random(5));
					} else {
						npc.setX(obj.getLocX());
						npc.setY(obj.getLocY());
					}
					npc.setMap((short) mapid);
					npc.setHomeX(npc.getX());
					npc.setHomeY(npc.getY());
					npc.getMoveState().setHeading(obj.getHeaing());
					npc.setLightSize(l1npc.getLightSize());
					npc.getLight().turnOnOffLight();
					
					npc.setActionStatus(ActionCodes.ACTION_Appear);
					
					L1World world = L1World.getInstance();
					S_CharVisualUpdate visual = new S_CharVisualUpdate(npc, 0);
					for (L1PcInstance pc : world.getVisiblePlayer(npc)) {
						npc.onPerceive(pc);
						pc.sendPackets(visual);
					}
					npc.setActionStatus(0);
					visual.clear();
					visual = null;
					
					world.storeObject(npc);
					world.addVisibleObject(npc);
					npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);// 채팅 개시
					npc.onNpcAI();
					
					if (0 < timeMillisToDelete) {
						L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);
						timer.begin();
					}
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
		}
		return npc;
	}
	
	public void spawn(int x, int y, short MapId, int Heading, int npcId, int randomRange) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(MapId);
			if (randomRange == 0) {
				npc.getLocation().set(x, y, MapId);
				npc.getLocation().forward(Heading);
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(x + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(y + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation())) {
						break;
					}
				} while (tryCount < 50);
				if (tryCount >= 50) {
					npc.getLocation().forward(Heading);
				}
			}
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(Heading);
			L1World world = L1World.getInstance();
			world.storeObject(npc);
			world.addVisibleObject(npc);
			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);// 채팅 개시
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public L1DoorInstance doorSpawn(int spriteId, int x, int y, short map, boolean direction) {
		L1DoorInstance door = null;
		try {
			L1Npc l1npc = NpcTable.getInstance().getTemplate(81158);
			if (l1npc != null) {
				String s = l1npc.getImpl();
				Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance").getConstructors()[0];
				Object parameters[] = { l1npc };
				door = (L1DoorInstance) constructor.newInstance(parameters);
				door.setId(IdFactory.getInstance().nextId());
				door.setDoorId(9000);
				door.setSpriteId(spriteId);
				door.setX(x);
				door.setY(y);
				door.setMap((short) map);
				door.setHomeX(door.getX());
				door.setHomeY(door.getY());
				door.setDirection(direction ? 0 : 1);
				door.setLeftEdgeLocation(direction ? door.getX() : door.getY());
				door.setRightEdgeLocation(direction ? door.getX() : door.getY());
				door.setMaxHp(0);
				door.setCurrentHp(0);
				door.setKeeperId(9000);
				if (door.getSpriteId() == 20348 || door.getSpriteId() == 20430 || door.getSpriteId() == 20344 || door.getSpriteId() == 20426) {// 아우라키아 석상
					door.setName("$35802");
					door.setDesc(door.getName());
				}
				L1World world = L1World.getInstance();
				S_DoActionGFX gfx = new S_DoActionGFX(door.getId(), ActionCodes.ACTION_Close);
				for (L1PcInstance pc : world.getVisiblePlayer(door)) {
					door.onPerceive(pc);
					pc.sendPackets(gfx);
				}
				gfx.clear();
				gfx = null;
				door.isPassibleDoor(false);
				world.storeObject(door);
				world.addVisibleObject(door);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return door;
	}
	
	public L1DoorInstance doorSpawnRange(int spriteId, int x, int y, short map, int range, boolean direction) {
		L1DoorInstance door = null;
		try {
			L1Npc l1npc = NpcTable.getInstance().getTemplate(81158);
			if (l1npc != null) {
				String s = l1npc.getImpl();
				Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance").getConstructors()[0];
				Object parameters[] = { l1npc };
				door = (L1DoorInstance) constructor.newInstance(parameters);
				door.setId(IdFactory.getInstance().nextId());
				door.setDoorId(9000);
				door.setSpriteId(spriteId);
				door.setX(x);
				door.setY(y);
				door.setMap((short) map);
				door.setHomeX(door.getX());
				door.setHomeY(door.getY());
				door.setDirection(direction ? 0 : 1);
				door.setLeftEdgeLocation(direction ? door.getX() - range : door.getY() - range);
				door.setRightEdgeLocation(direction ? door.getX() + range : door.getY() + range);
				door.setMaxHp(0);
				door.setCurrentHp(0);
				door.setKeeperId(9000);
				L1World world = L1World.getInstance();
				S_DoActionGFX gfx = new S_DoActionGFX(door.getId(), ActionCodes.ACTION_Close);
				for (L1PcInstance pc : world.getVisiblePlayer(door)) {
					door.onPerceive(pc);
					pc.sendPackets(gfx);
				}
				gfx.clear();
				gfx = null;
				door.isPassibleDoor(false);
				world.storeObject(door);
				world.addVisibleObject(door);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return door;
	}
	
	public void deleteDoor(int map) {
		L1DoorInstance door = null;
		for (L1Object obj : L1World.getInstance().getVisibleObjects(map).values()) {
			if (obj instanceof L1DoorInstance) {
				door = (L1DoorInstance) obj;
				if (door.getDoorId() == 9000) {
					door.setCurrentHp(0);
					door.setDead(true);
					door.isPassibleDoor(true);
					door.setActionStatus(ActionCodes.ACTION_Open);
					door.getMap().setPassable(door.getLocation(), true);
					Broadcaster.broadcastPacket(door, new S_DoActionGFX(door.getId(), ActionCodes.ACTION_Open), true);
					door.deleteMe();
				}
			}
		}
	}
	
	public void deleteNpc(int npcId, int map) {
		L1NpcInstance npc = null;
		for (L1Object obj : L1World.getInstance().getVisibleObjects(map).values()) {
			if (obj instanceof L1NpcInstance) {
				npc = (L1NpcInstance) obj;
				if (npc.getNpcId() == npcId) {
					npc.deleteMe();
				}
			}
		}
	}
	
	public void deleteNpcAction(int npcId, int map) {
		L1NpcInstance npc = null;
		for (L1Object obj : L1World.getInstance().getVisibleObjects(map).values()) {
			if (obj instanceof L1NpcInstance) {
				npc = (L1NpcInstance) obj;
				if (npc.getNpcId() == npcId) {
					npc.setDead(true);
					npc.setActionStatus(ActionCodes.ACTION_Die);
					Broadcaster.broadcastPacket(npc, new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_Die), true);
					npc.deleteMe();
				}
			}
		}
	}
	
	public void mapObjectDelete(int map){
		Collection<L1Object> cklist = L1World.getInstance().getVisibleObjects(map).values();
		for (L1Object obj : cklist) {
			if (obj == null || obj instanceof L1DollInstance || obj instanceof L1PetInstance || obj instanceof L1SummonInstance) {
				continue;
			}
			if (obj instanceof L1ItemInstance) {
				L1ItemInstance item = (L1ItemInstance)obj;
				L1Inventory groundInventory = L1World.getInstance().getInventory(item.getX(), item.getY(), item.getMapId());
				groundInventory.removeItem(item);
			} else if (obj instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance)obj;
				npc.deleteMe();
			}
		}
	}
	
	public void sendPacket(ArrayList<L1PcInstance> pclist, ServerBasePacket pck, boolean ck){
		for (L1PcInstance pc : pclist) {
			pc.sendPackets(pck);
		}
		if (ck) {
			try {
				pck.clear();
				pck = null;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void load(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_indun");
			rs = pstm.executeQuery();
			IndunSpawnObject obj = null;
			while(rs.next()){
				obj = new IndunSpawnObject(rs);
				FastTable<IndunSpawnObject> list = SPAWN_DATA.get(obj.getType());
				if (list == null) {
					list = new FastTable<IndunSpawnObject>();
					SPAWN_DATA.put(obj.getType(), list);
				}
				list.add(obj);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}

