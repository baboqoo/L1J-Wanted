package l1j.server.IndunSystem.clandungeon;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import javolution.util.FastTable;
import l1j.server.server.ActionCodes;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.exp.L1ExpPlayer;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.CommonUtil;

public class ClanDungeonUtill {
	public static ArrayList<L1PcInstance> PcStageCK(int map) {
		return L1World.getInstance().getMapPlayer(map);
	}
	
	public static L1NpcInstance spawn(int x, int y, int mapid, FastTable<ClanDungeonObject> clandungeonList) {
		L1NpcInstance mob = null;
		try {
			for (ClanDungeonObject clanobj : clandungeonList) {
				for (int i=0; i<clanobj._count; i++) {
					mob = NpcTable.getInstance().newNpcInstance(clanobj._npcId);
					if (mob == null) {
						System.out.println("clandungeon mob == null");
						break;
					}
					mob.setId(IdFactory.getInstance().nextId());
					mob.getMoveState().setHeading(clanobj._npcId == 20931 ? 4 : 5);
					mob.setX(x);
					mob.setHomeX(x);
					mob.setY(y);
					mob.setHomeY(y);
					mob.setMap((short) mapid);
					L1World world = L1World.getInstance();
					world.storeObject(mob);
					world.addVisibleObject(mob);
					mob.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);// 채팅 개시
					ArrayList<L1PcInstance> ll = world.getRecognizePlayer(mob);
					if (ll.size() > 0) {
						L1PcInstance[] list = (L1PcInstance[])ll.toArray(new L1PcInstance[ll.size()]);
						
						mob.setActionStatus(ActionCodes.ACTION_Appear);
						S_CharVisualUpdate visual	= new S_CharVisualUpdate(mob, 0);
						for (L1PcInstance pc : list) {
							mob.onPerceive(pc);
							pc.sendPackets(visual);
						}
						mob.setActionStatus(0);
						visual.clear();
						visual = null;
						mob.setTarget(list[CommonUtil.random(list.length)]);
						list = null;
					}
					mob.onNpcAI();
					mob.getLight().turnOnOffLight();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mob;
	}
	
	public static FastTable<L1NpcInstance> spawn(int x, int y, int mapid, FastTable<ClanDungeonObject> clandungeonList, boolean boss, int randomRange) {
		FastTable<L1NpcInstance> monsterlist = new FastTable<L1NpcInstance>();
		try {
			L1Location locmap = new L1Location(x, y, mapid);
			for (ClanDungeonObject clanobj : clandungeonList) {
				if (clanobj._boss != boss) {
					continue;
				}
				L1NpcInstance mob = null;
				for (int i=0; i<clanobj._count; i++) {
					mob = NpcTable.getInstance().newNpcInstance(clanobj._npcId);
					if (mob == null) {
						System.out.println("clandungeon mob == null");
						break;
					}
					mob.setId(IdFactory.getInstance().nextId());
					mob.getMoveState().setHeading(5);
					L1Location loc = null;
					if (randomRange > 0) {
						loc = locmap.randomLocation(randomRange, false);
					}
					mob.setX(loc != null ? loc.getX() : x);
					mob.setHomeX(loc != null ? loc.getX() : x);
					mob.setY(loc != null ? loc.getY() : y);
					mob.setHomeY(loc != null ? loc.getY() : y);
					mob.setMap(loc != null ? (short) loc.getMapId() : (short) mapid);
					L1World world = L1World.getInstance();
					world.storeObject(mob);
					world.addVisibleObject(mob);
					mob.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);// 채팅 개시
					ArrayList<L1PcInstance> ll = world.getRecognizePlayer(mob);
					if (ll.size() > 0) {
						L1PcInstance[] list = (L1PcInstance[])ll.toArray(new L1PcInstance[ll.size()]);
						S_DoActionGFX action = new S_DoActionGFX(mob.getId(), ActionCodes.ACTION_Appear);
						for (L1PcInstance pc : list) {
							mob.onPerceive(pc);
							pc.sendPackets(action);
						}
						action.clear();
						action = null;
						mob.setTarget(list[CommonUtil.random(list.length)]);
						list = null;
					}
					mob.onNpcAI();
					mob.getLight().turnOnOffLight();
					monsterlist.add(mob);
				}
			}
			locmap = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return monsterlist;
	}
	
	public static L1NpcInstance[] spawn(int x, int y, int mapid, FastTable<ClanDungeonObject> clandungeonList, boolean boss) {
		L1NpcInstance[] npcArray = null;
		try {
			for (ClanDungeonObject clanobj : clandungeonList) {
				if (clanobj._boss != boss) {
					continue;
				}
				npcArray = new L1NpcInstance [clanobj._count];
				L1NpcInstance mob = null;
				for (int i=0; i<clanobj._count; i++) {
					mob = NpcTable.getInstance().newNpcInstance(clanobj._npcId);
					if (mob == null) {
						System.out.println("clandungeon mob == null");
						break;
					}
					mob.setId(IdFactory.getInstance().nextId());
					mob.getMoveState().setHeading(5);
					mob.setX(x);
					mob.setHomeX(x);
					mob.setY(y);
					mob.setHomeY(y);
					mob.setMap((short) mapid);
					L1World world = L1World.getInstance();
					world.storeObject(mob);
					world.addVisibleObject(mob);
					mob.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);// 채팅 개시
					ArrayList<L1PcInstance> ll = world.getRecognizePlayer(mob);
					if (ll.size() > 0) {
						L1PcInstance[] list = (L1PcInstance[])ll.toArray(new L1PcInstance[ll.size()]);
						S_DoActionGFX action = new S_DoActionGFX(mob.getId(), ActionCodes.ACTION_Appear);
						for (L1PcInstance pc : list) {
							mob.onPerceive(pc);
							pc.sendPackets(action);
						}
						action.clear();
						action = null;
						mob.setTarget(list[CommonUtil.random(list.length)]);
						list = null;
					}
					mob.onNpcAI();
					mob.getLight().turnOnOffLight();
					npcArray[i] = mob;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return npcArray;
	}
	
	public static L1Location getLocation(int mapid) {
		return new L1Location(33534, 32701, mapid);
	}
	
	public static L1NpcInstance[] spawn(int mapid, FastTable<ClanDungeonObject> clandungeonList, boolean boss) {
		L1NpcInstance[] npcArray = null;
		try {
			L1Location locmap = getLocation(mapid);
			for (ClanDungeonObject clanobj : clandungeonList) {
				if (clanobj._boss != boss) {
					continue;
				}
				npcArray = new L1NpcInstance [clanobj._count];
				L1NpcInstance mob = null;
				for (int i=0; i<clanobj._count; i++) {
					mob = NpcTable.getInstance().newNpcInstance(clanobj._npcId);
					if (mob == null) {
						System.out.println("clandungeon mob == null");
						break;
					}
					L1Location loc = locmap.randomLocation(22, false);
					mob.setId(IdFactory.getInstance().nextId());
					mob.getMoveState().setHeading(5);
					mob.setX(loc.getX());
					mob.setHomeX(loc.getX());
					mob.setY(loc.getY());
					mob.setHomeY(loc.getY());
					mob.setMap((short) loc.getMapId());
					L1World world = L1World.getInstance();
					world.storeObject(mob);
					world.addVisibleObject(mob);
					mob.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);// 채팅 개시
					ArrayList<L1PcInstance> ll = world.getRecognizePlayer(mob);
					if (ll.size() > 0) {
						L1PcInstance[] list = (L1PcInstance[])ll.toArray(new L1PcInstance[ll.size()]);
						S_DoActionGFX action = new S_DoActionGFX(mob.getId(), ActionCodes.ACTION_Appear);
						for (L1PcInstance pc : list) {
							mob.onPerceive(pc);
							pc.sendPackets(action);
						}
						action.clear();
						action = null;
						mob.setTarget(list[CommonUtil.random(list.length)]);
						list = null;
					}
					mob.onNpcAI();
					mob.getLight().turnOnOffLight();
					npcArray[i] = mob;
				}
			}
			locmap = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return npcArray;
	}
	
	public static void spawnStage(int mapid, FastTable<ClanDungeonObject> clandungeonList, int npcid) {
		try {
			L1Location locmap = getLocation(mapid);
			for (ClanDungeonObject clanobj : clandungeonList) {
				if (clanobj._npcId == npcid) {
					L1NpcInstance mob;
					for (int i=0; i<clanobj._count; i++) {
						mob = NpcTable.getInstance().newNpcInstance(clanobj._npcId);
						if (mob == null) {
							System.out.println("clandungeon mob == null");
							break;
						}
						L1Location loc = locmap.randomLocation(22, false);
						mob.setId(IdFactory.getInstance().nextId());
						mob.getMoveState().setHeading(5);
						mob.setX(loc.getX());
						mob.setHomeX(loc.getX());
						mob.setY(loc.getY());
						mob.setHomeY(loc.getY());
						mob.setMap((short) loc.getMapId());
						L1World world = L1World.getInstance();
						world.storeObject(mob);
						world.addVisibleObject(mob);
						ArrayList<L1PcInstance> ll = world.getRecognizePlayer(mob);
						if (ll.size() > 0) {
							L1PcInstance[] list = (L1PcInstance[])ll.toArray(new L1PcInstance[ll.size()]);
							S_DoActionGFX action = new S_DoActionGFX(mob.getId(), ActionCodes.ACTION_Appear);
							for (L1PcInstance pc : list) {
								mob.onPerceive(pc);
								pc.sendPackets(action);
							}
							action.clear();
							action = null;
							mob.setTarget(list[CommonUtil.random(list.length)]);
							list = null;
						}
						mob.onNpcAI();
						mob.getLight().turnOnOffLight();
					}
					break;
				}
			}
			locmap = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void ment(String ment, int mapid){
		ArrayList<L1PcInstance> list = PcStageCK(mapid);
		S_PacketBox pck = new S_PacketBox(S_PacketBox.GREEN_MESSAGE, ment);
		for (L1PcInstance pc : list) {
			pc.sendPackets(pck);
		}
		pck.clear();
		pck = null;
		list.clear();
	}
	
	public static void sendItem(int mapid, int itemid){
		ArrayList<L1PcInstance> list = PcStageCK(mapid);
		for (L1PcInstance pc : list) {
			if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(itemid), 1) != L1Inventory.OK) continue;
			L1ItemInstance item = pc.getInventory().storeItem(itemid, 1);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(403, item.getItem().getDesc()), true);//아이템을 획득 멘트
			}
		}
		list.clear();
	}
	
	public static void sendExp(int type, int mapid){
		ArrayList<L1PcInstance> list = PcStageCK(mapid);
		for (L1PcInstance pc : list) {
			long exp = 0;
			if (type == 2) {
				exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, pc.getLevel() < 90 ? 19 : 25);// 52기준
			} else if (type == 3) {
				exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, pc.getLevel() < 90 ? 28 : 33);// 52기준
			} else if (type == 4) {
				exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, pc.getLevel() < 90 ? 37 : 41);// 52기준
			} else if (type == 5) {
				exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, pc.getLevel() < 90 ? 46 : 49);// 52기준
			} else {
				exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, pc.getLevel() < 90 ? 14 : 19);// 52기준
			}
			if (pc.getExp() + exp >= L1ExpPlayer.LIMIT_EXP) {
				continue;
			}
			/** 폭렙 방지 **/
		    if (pc.getLevel() >= 1 && (exp + pc.getExp() > ExpTable.getExpByLevel(pc.getLevel() + 1))) {
		    	exp = ExpTable.getExpByLevel(pc.getLevel() + 1) - pc.getExp();
			}
			pc.addExp(exp);
			pc.send_effect(3944);
		}
		list.clear();
	}
	
	public static void sendEffect(int effetId, int mapid){
		ArrayList<L1PcInstance> list = PcStageCK(mapid);
		S_PacketBox pck = new S_PacketBox(S_PacketBox.HADIN_DISPLAY, effetId);
		for (L1PcInstance pc : list) {
			pc.sendPackets(pck);
		}
		pck.clear();
		pck = null;
		list.clear();
	}
	
	public static void deleteNpc(int mapid, int npcid){
		for (L1Object ob : L1World.getInstance().getVisibleObjects(mapid).values()) {
			if (ob == null) {
				break;
			}
			if (ob instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance)ob;
				if(npc.getNpcId() == npcid){
					npc.deleteMe();
				}
			}
		}
	}
	
	public static void deleteObject(int mapid){
		L1World world = L1World.getInstance();
		for (L1Object obj : world.getVisibleObjects(mapid).values()) {
			if (obj == null || obj instanceof L1DollInstance || obj instanceof L1PetInstance || obj instanceof L1SummonInstance) {
				continue;
			}
			if (obj instanceof L1ItemInstance) {
				L1ItemInstance item = (L1ItemInstance)obj;
				L1Inventory groundInventory = world.getInventory(item.getX(), item.getY(), item.getMapId());
				groundInventory.removeItem(item);
			} else if (obj instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance)obj;
				npc.deleteMe();
			}
		}
	}
	
	public static void doorSpawn(int x, int y, short map, int doorid, boolean xy, int spriteId) {
		try {
			L1Npc l1npc = NpcTable.getInstance().getTemplate(81158);
			if (l1npc == null) {
				return;
			}
			Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + l1npc.getImpl() + "Instance").getConstructors()[0];
			Object parameters[] = { l1npc };
			L1DoorInstance door = (L1DoorInstance) constructor.newInstance(parameters);
			door = (L1DoorInstance) constructor.newInstance(parameters);
			door.setId(IdFactory.getInstance().nextId());

			door.setDoorId(doorid);
			door.setSpriteId(spriteId);
			door.setX(x);
			door.setY(y);
			door.setMap((short) map);
			door.setHomeX(door.getX());
			door.setHomeY(door.getY());
			door.setDirection(xy ? 0 : 1);
			door.setLeftEdgeLocation(xy ? door.getX() - 1 : door.getY() - 7);
			door.setRightEdgeLocation(xy ? door.getX() + 7 : door.getY() + 1);
			door.setMaxHp(0);
			door.setCurrentHp(0);
			door.setKeeperId(doorid);

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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

