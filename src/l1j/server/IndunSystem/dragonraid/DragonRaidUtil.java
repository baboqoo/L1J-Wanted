package l1j.server.IndunSystem.dragonraid;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Account;
import l1j.server.server.ActionCodes;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SceneNoti;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class DragonRaidUtil {
	private static Logger _log = Logger.getLogger(DragonRaidUtil.class.getName());
	
	private static class newInstance{
		public static final DragonRaidUtil INSTANCE = new DragonRaidUtil();
	}
	public static DragonRaidUtil getInstance(){
		return newInstance.INSTANCE;
	}
	private DragonRaidUtil(){}
	
	private static final ServerBasePacket[] TIMER_END_MESSAGES = { 
//AUTO SRM: 		new S_SystemMessage("5분 뒤 레이드가 종료됩니다."), new S_SystemMessage("3분 뒤 레이드가 종료됩니다."), new S_SystemMessage("1분 뒤 레이드가 종료됩니다."), new S_SystemMessage("10초 뒤 레이드가 종료됩니다."), new S_SystemMessage("5초 뒤 레이드가 종료됩니다.") }; // CHECKED OK
		new S_SystemMessage(S_SystemMessage.getRefText(30), true), 
		new S_SystemMessage(S_SystemMessage.getRefText(1269), true), 
		new S_SystemMessage(S_SystemMessage.getRefText(1270), true), 
		new S_SystemMessage(S_SystemMessage.getRefText(1271), true), 
		new S_SystemMessage(S_SystemMessage.getRefText(1272), true) };
	
	public void mapGreenMsgSend(ArrayList<L1PcInstance> list, String msg){
		S_PacketBox green = new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg);
		for (L1PcInstance pc : list) {
			if (pc == null) {
				continue;
			}
			pc.sendPackets(green);
		}
		green.clear();
		green = null;
	}
	
	public void mapServerMsgSend(ArrayList<L1PcInstance> list, int msg){
		S_ServerMessage serverMsg = new S_ServerMessage(msg);
		for (L1PcInstance pc : list) {
			if (pc == null) {
				continue;
			}
			pc.sendPackets(serverMsg);
		}
		serverMsg.clear();
		serverMsg = null;
	}
	
	public void mapTimerEndMsgSend(ArrayList<L1PcInstance> list, int type){
		for (L1PcInstance pc : list) {
			if (pc == null) {
				continue;
			}
			pc.sendPackets(TIMER_END_MESSAGES[type]);
		}
	}
	
	public void mapSystemMsgSend(ArrayList<L1PcInstance> list, String msg){
		S_SystemMessage systemMsg = new S_SystemMessage(msg, true);
		for (L1PcInstance pc : list) {
			if (pc == null) {
				continue;
			}
			pc.sendPackets(systemMsg);
		}
		systemMsg.clear();
		systemMsg = null;
	}
	
	public void mapWindowSizeSend(ArrayList<L1PcInstance> list){
		for (L1PcInstance pc : list) {
			if (pc == null) {
				continue;
			}
			pc.sendPackets(S_SceneNoti.HIDDEN_WAKE);
		}
	}

	public void teleporterDelete(DragonRaildType type){
		for (L1Object obj : L1World.getInstance().getObject()) {
			if (obj == null) {
				continue;
			}
			if (obj instanceof L1NpcInstance == false) {
				continue;
			}
			L1NpcInstance npc = (L1NpcInstance) obj;
			int npcId = npc.getNpcTemplate().getNpcId();
			if (type == DragonRaildType.ANTARAS 
					&& (npcId == 900300 || npcId == 900304)) {//안타라스
				npc.deleteMe();
			} else if (type == DragonRaildType.FAFURION 
					&& (npcId == 900301 
					|| npcId == 900305 
					|| npcId == 900306
					|| npcId == 900307 
					|| npcId == 900308
					|| npcId == 900309 
					|| npcId == 900310)) {//파푸리온
				npc.deleteMe();
			} else if (type == DragonRaildType.RINDVIOR 
					&& (npcId == 900302
					|| npcId == 900311
					|| npcId == 900312)) {//린드비오르
				npc.deleteMe();
			} else if (type == DragonRaildType.VALAKAS 
					&& (npcId == 900303 || npcId == 900313)) {//발라카스
				npc.deleteMe();
			}
		}
	}
	
	public void failDelete(int map) {
		for (L1Object obj : L1World.getInstance().getVisibleObjects(map).values()) {
			if (obj == null) {
				continue;
			}
			if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance npc = (L1MonsterInstance) obj;
				npc.setCurrentHp(0);
				npc.setDead(true);
				npc.setActionStatus(ActionCodes.ACTION_Die);
				Broadcaster.broadcastPacket(npc, new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_Die), true);
				npc.deleteMe();
			}
		}
	}
	
	public L1NpcInstance spawn(int x, int y, short MapId, int Heading, int npcId, int randomRange) {
		L1NpcInstance npc = null;
		try {
			npc = NpcTable.getInstance().newNpcInstance(npcId);
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
					Thread.sleep(1);
				} while (tryCount < 50);
				if (tryCount >= 50) {
					npc.getLocation().forward(Heading);
				}
			}
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(Heading);

			npc.setActionStatus(npcId == 900516 || npcId == 900519 ? ActionCodes.ACTION_Appear : ActionCodes.ACTION_AxeWalk);
			L1World world = L1World.getInstance();
			S_CharVisualUpdate visual = new S_CharVisualUpdate(npc, 0);
			for (L1PcInstance pc : world.getVisiblePlayer(npc)) {
				npc.onPerceive(pc);
				pc.sendPackets(visual);
			}
			npc.setActionStatus(0);
			visual.clear();
			visual = null;
			npc.setParalysisTime(npc.getSprite().getMoveSpeed(npcId == 900516 || npcId == 900519 ? ActionCodes.ACTION_Appear : ActionCodes.ACTION_AxeWalk));
			
			world.storeObject(npc);
			world.addVisibleObject(npc);

			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return npc;
	}
	
	public void doorSpawn(int x, int y, short map, int doorId, int spriteId) {
		try {
			L1Npc l1npc = NpcTable.getInstance().getTemplate(81158);
			if (l1npc != null) {
				String s = l1npc.getImpl();
				Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance").getConstructors()[0];
				Object parameters[] = { l1npc };
				L1DoorInstance door = (L1DoorInstance) constructor.newInstance(parameters);
				door = (L1DoorInstance) constructor.newInstance(parameters);
				door.setId(IdFactory.getInstance().nextId());

				door.setDoorId(doorId);
				door.setSpriteId(spriteId);
				door.setX(x);
				door.setY(y);
				door.setMap((short) map);
				door.setHomeX(door.getX());
				door.setHomeY(door.getY());
				door.setDirection(0);
				door.setLeftEdgeLocation(door.getX());
				door.setRightEdgeLocation(door.getX());
				door.setMaxHp(0);
				door.setCurrentHp(0);
				door.setKeeperId(doorId);

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
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	public void Delete_Door(int doorId, int mapid) {
		L1DoorInstance door = null;
		for (L1Object obj : L1World.getInstance().getVisibleObjects(mapid).values()) {
			if (obj == null) {
				continue;
			}
			if (obj instanceof L1DoorInstance) {
				door = (L1DoorInstance) obj;
				if (door.getDoorId() == doorId) {
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
	
	public boolean dieCheck(int mapId, int npcId){
		L1MonsterInstance mob = null;
		for (L1Object object : L1World.getInstance().getVisibleObjects(mapId).values()) {
			if (object == null) {
				continue;
			}
			if (object instanceof L1MonsterInstance) {
				mob = (L1MonsterInstance)object;
				if (mob != null && mob.isDead() && mob.getNpcId() == npcId) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void objectDelete(int mapId){
		L1World world = L1World.getInstance();
		for (L1Object obj : world.getVisibleObjects(mapId).values()) {
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
	
	public void raidBuffTimeUpdate(ArrayList<L1PcInstance> pcList){
		if (pcList == null || pcList.isEmpty()) {
			return;
		}
		Connection con			= null;
		PreparedStatement pstm	= null;
		Account account			= null;
		try {
			con 	=	L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			pstm	=	con.prepareStatement("UPDATE accounts SET DragonRaid_Buff=? WHERE login=?");
			for (L1PcInstance pc : pcList) {
				if (pc == null) {
					continue;
				}
				account = pc.getAccount();
				if (account == null) {
					continue;
				}
				pstm.setTimestamp(1, account.getDragonRaid());
				pstm.setString(2, account.getName());
				pstm.addBatch();
				pstm.clearParameters();
			}
			pstm.executeBatch();
			pstm.clearBatch();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				sqle.printStackTrace();
			}
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				sqle.printStackTrace();
			}
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			SQLUtil.close(pstm, con);
		}
	}
}


