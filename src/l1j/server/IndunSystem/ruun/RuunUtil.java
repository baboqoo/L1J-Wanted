package l1j.server.IndunSystem.ruun;

import java.util.ArrayList;

import javolution.util.FastTable;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.L1SpawnUtil;

public class RuunUtil {
	private static class newInstance {
		public static final RuunUtil INSTANCE = new RuunUtil();
	}
	protected static RuunUtil getInstance(){
		return newInstance.INSTANCE;
	}
	
	protected ArrayList<L1PcInstance> pcMapList(int mapId) {
		return L1World.getInstance().getMapPlayer(mapId);
	}
	
	protected void mapServerMsgSend(int mapId, int msg){
		S_ServerMessage serverMsg = new S_ServerMessage(msg);
		ArrayList<L1PcInstance> list = pcMapList(mapId);
		for (L1PcInstance pc : list) {
			pc.sendPackets(serverMsg);
		}
		serverMsg.clear();
		serverMsg = null;
		list.clear();
	}
	
	protected void mapSystemMsgSend(int mapId, String msg){
		S_SystemMessage sysMsg = new S_SystemMessage(msg, true);
		ArrayList<L1PcInstance> list = pcMapList(mapId);
		for (L1PcInstance pc : list) {
			pc.sendPackets(sysMsg);
		}
		sysMsg.clear();
		sysMsg = null;
		list.clear();
	}
	
	protected void timerSend(int mapId, int time){
		S_PacketBox timer = new S_PacketBox(S_PacketBox.TIME_COUNT, time);
		ArrayList<L1PcInstance> list = pcMapList(mapId);
		for (L1PcInstance pc : list) {
			pc.sendPackets(timer);
		}
		timer.clear();
		timer = null;
		list.clear();
	}
	
	protected synchronized FastTable<L1NpcInstance> spawn(RuunRound stage) {
		FastTable<L1NpcInstance> npcList = new FastTable<L1NpcInstance>();
		L1NpcInstance mob = null;
		L1Location locmap = null;
		try {
			for (RuunSpawnObject data : RuunLoader.getInstance().getRuunData()) {
				if (data.getStage() != stage._type) {
					continue;
				}
				locmap = new L1Location(data.getLocX(), data.getLocY(), data.getMapId());
				
				int liveCount = 0;
				L1NpcInstance liveNpc = null;
				L1World world = L1World.getInstance();
				for (L1Object obj : world.getVisibleObjects(data.getMapId()).values()) {
					if (obj == null) {
						continue;
					}
					if (obj instanceof L1NpcInstance) {
						liveNpc = (L1NpcInstance)obj;
						if (liveNpc.getNpcId() == data.getNpcId() && liveNpc.getHomeX() == data.getLocX() && liveNpc.getHomeY() == data.getLocY()) {
							liveCount++;
							npcList.add(liveNpc);
						}
					}
				}
				int spawnCount = data.getCount() - liveCount;
				if (spawnCount <= 0) {
					continue;
				}
				NpcTable temp = NpcTable.getInstance();
				for (int i = 0; i < spawnCount; i++) {
					mob = temp.newNpcInstance(data.getNpcId());
					if (mob == null) {
						System.out.println("ruunData mob == null");
						break;
					}
					mob.setId(IdFactory.getInstance().nextId());
					mob.getMoveState().setHeading(5);
					L1Location loc = null;
					if (data.getRange() > 0) {
						loc = locmap.randomLocation(data.getRange(), false);
					}
					mob.setX(loc == null ? data.getLocX() : loc.getX());
					mob.setHomeX(data.getLocX());
					mob.setY(loc == null ? data.getLocY() : loc.getY());
					mob.setHomeY(data.getLocY());
					mob.setMap((short) data.getMapId());
					
					world.storeObject(mob);
					world.addVisibleObject(mob);
					mob.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);// 채팅 개시
					
					// 스폰 이미지
					L1SpawnUtil.spawnAction(mob, mob.getNpcId());
					
					mob.onNpcAI();
					mob.getLight().turnOnOffLight();
					npcList.add(mob);
				}
				locmap = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return npcList;
	}
	
	protected void objectDelete(int mapId){
		for (L1Object obj : L1World.getInstance().getVisibleObjects(mapId).values()) {
			if (obj == null || obj instanceof L1DollInstance || obj instanceof L1PetInstance || obj instanceof L1SummonInstance) {
				continue;
			}
			if (obj instanceof L1ItemInstance) {
				L1ItemInstance item = (L1ItemInstance)obj;
				L1Inventory groundInventory = L1World.getInstance().getInventory(item.getX(), item.getY(), item.getMapId());
				groundInventory.removeItem(item);
			} else if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance npc = (L1MonsterInstance)obj;
				npc.deleteMe();
			}
		}
	}
	
	protected void objectDeleteList(FastTable<L1NpcInstance> list){
		for (L1NpcInstance npc : list) {
			if (npc == null) {
				continue;
			}
			npc.deleteMe();
		}
	}
}

