package l1j.server.server.model.item.function;

import java.util.ArrayList;

import javolution.util.FastMap;
import l1j.server.server.ActionCodes;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.command.L1Commands;
import l1j.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.DropTable;
import l1j.server.server.datatables.MapFixKeyTable;
import l1j.server.server.datatables.NpcSpawnTable;
import l1j.server.server.datatables.SpawnTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Spawn;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_NpcInfo;
import l1j.server.server.serverpackets.action.S_AttackPacket;
import l1j.server.server.serverpackets.inventory.S_SearchDrop;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Command;
import l1j.server.server.templates.L1Drop;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.StringUtil;

public class GMWand extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public GMWand(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (!pc.isGm()) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			int itemId = getItemId();
			int objid = 0, x = 0, y = 0;
			objid	= packet.readD();
			x		= packet.readH();
			y		= packet.readH();
			
			L1Object findObject = L1World.getInstance().findObject(objid);
			int heding = pc.targetDirection(x, y);
			if (pc.getMoveState().getHeading() != heding) {
				pc.getMoveState().setHeading(heding);
			}
			pc.sendPackets(new S_AttackPacket(pc, 0, ActionCodes.ACTION_Wand), true);
			switch(itemId){
			case 31141:case 31142:case 31143:
				characterInfoWand(pc, itemId, findObject);
				break;
			case 46161:// 드랍확인막대
				droplistWand(pc, objid);
				break;
			case 46162:// npc확인막대
				npcIdentiWand(pc, objid);
				break;
			case 50101:// 위치 체크 막대
				locCheckWand(pc, x, y);
				break;
			case 50102:// 위치 변경 막대
				locChangeWand(pc, x, y);
				break;
			case 46160:// npc제거 막대
				deleteNpcWand(pc, findObject);
				break;
			}
		}
	}
	
	void deleteNpcWand(L1PcInstance pc, L1Object findObject){
		if (findObject == null) {
			return;
		}
		if (findObject instanceof L1NpcInstance == false) {
			return;
		}
		if (findObject instanceof L1DollInstance) {
			return;
		}
		L1NpcInstance npc = (L1NpcInstance) findObject;
		boolean success = false;
		if (npc instanceof L1MonsterInstance) {
			success = deleteMonster(pc, npc);
		} else {
			success = deleteNpc(pc, npc);
		}
		StringBuilder sb = new StringBuilder();
		/*sb.append("대상 >> [").append(npc.getNpcId()).append("] '").append(npc.getName()).append("' 을(를) 삭제 합니다.");
		if (!success) {
			sb.append("DB결과 >> 스폰정보를 찾을 수 없습니다.");
		} else {
			sb.append("DB결과 >> 스폰정보를 삭제 하였습니다..");
		}*/
		sb.append("Target >> [").append(npc.getNpcId()).append("] '").append(npc.getName()).append("' will be deleted.");
		if (!success) {
			sb.append("DB Result >> Spawn information not found.");
		} else {
			sb.append("DB Result >> Spawn information has been deleted.");
		}		
		pc.sendPackets(new S_SystemMessage(sb.toString(), true), true);
		npc.setRespawn(false);
		npc.setSpawn(null);
		npc.deleteMe();
	}
	
	boolean deleteMonster(L1PcInstance pc, L1NpcInstance npc) {
		boolean success = SpawnTable.getInstance().removeSpawn(npc);// spawnlist 테이블 처리
		if (!success) {
			success = NpcSpawnTable.getInstance().removeSpawn(npc);// spawnlist_npc 테이블 처리
		}
		return success;
	}
	
	boolean deleteNpc(L1PcInstance pc, L1NpcInstance npc) {
		boolean success = NpcSpawnTable.getInstance().removeSpawn(npc);// spawnlist_npc 테이블 처리
		if (!success) {
			success = SpawnTable.getInstance().removeSpawn(npc);// spawnlist 테이블 처리
		}
		return success;
	}
	
	static FastMap<Integer, S_SearchDrop> DROPLIST;
	static S_SearchDrop getDropList(int npcId){
		if (DROPLIST == null) {
			DROPLIST = new FastMap<Integer, S_SearchDrop>();
		}
		return DROPLIST.get(npcId);
	}
	
	void droplistWand(L1PcInstance pc, int targetId) {
		pc.broadcastPacketWithMe(new S_AttackPacket(pc, 0, ActionCodes.ACTION_Wand), true);
		L1Object target = L1World.getInstance().findObject(targetId);
		if (target == null || !(target instanceof L1MonsterInstance)) {
			return;
		}
		L1MonsterInstance npc	= (L1MonsterInstance) target;
		int transformId			= npc.getNpcTemplate().getTransformId();
		int searchId			= transformId == -1 ? npc.getNpcId() : transformId;
		S_SearchDrop pck		= getDropList(searchId);
		if (pck == null) {
			ArrayList<L1Drop> dropList = DropTable.getInstance().getDropList(searchId);
			if (dropList == null || dropList.isEmpty()) {
				return;
			}
			pck = S_SearchDrop.getDropList(pc, dropList);
			DROPLIST.put(searchId, pck);
		}
		pc.sendPackets(pck);
	}
	
	/**
	 * NPC확인 막대
	 * @param pc
	 * @param targetId
	 */
	void npcIdentiWand(L1PcInstance pc, int targetId) {
		pc.broadcastPacketWithMe(new S_AttackPacket(pc, 0, ActionCodes.ACTION_Wand), true);
		L1Object target = L1World.getInstance().findObject(targetId);
		if (target != null && target instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance) target;
			StringBuilder sb = new StringBuilder();
			sb.append("DB_ID : ").append(npc.getNpcId()).append(" | DESC_KR : ").append(npc.getName()).append(" | SPRITE_ID : ").append(npc.getSpriteId());
			sb.append("\r\nLOC : ").append(npc.getX()).append(", ").append(npc.getY()).append(", ").append(npc.getMapId());
			L1Spawn spawn = npc.getSpawn();
			if (spawn != null) {
				sb.append("\r\nSPAWN_ID : ").append(spawn.getId()).append(" | SPAWN_LOC : ").append(spawn.getLocX()).append(", ").append(spawn.getLocY()).append(", ").append(spawn.getMapId());
			}
			pc.sendPackets(new S_SystemMessage(sb.toString()), true);
			if (target instanceof L1MonsterInstance) {
				pc.sendPackets(new S_NpcInfo(npc), true);
			}
		}
	}
	
	void locCheckWand(L1PcInstance pc, int locX, int locY) {
		pc.sendPackets(new S_SystemMessage("Gab :" + pc.getMap().getOriginalTile(locX, locY) + ",x :" + locX + ",y :" + locY + ", mapId :" + pc.getMapId()), true);
		if (pc.getMap().isCloseZone(locX, locY)) {
			pc.broadcastPacketWithMe(new S_EffectLocation(locX, locY, 10), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("벽으로 인식중"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1077), true), true);
		}
	}

	void locChangeWand(L1PcInstance pc, int locX, int locY) {
		String key = new StringBuilder().append(pc.getMapId()).append(locX).append(locY).toString();
		if (!pc.getMap().isCloseZone(locX, locY)) {
			if (!MapFixKeyTable.getInstance().isLockey(key)) {
				MapFixKeyTable.getInstance().storeLocFix(locX, locY, pc.getMapId());
				pc.broadcastPacketWithMe(new S_EffectLocation(locX, locY, (short) 1815), true);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("key추가 ,x :" + locX + ",y :" + locY + ", mapId :" + pc.getMapId()), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1078) + locX  + ",y :" + locY  + ", mapId :" + pc.getMapId(), true), true);
			}
		} else {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("선택좌표는 벽이 아닙니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1079), true), true);
			if (MapFixKeyTable.getInstance().isLockey(key)) {
				MapFixKeyTable.getInstance().deleteLocFix(locX, locY, pc.getMapId());
				pc.broadcastPacketWithMe(new S_EffectLocation(locX, locY, (short) 10), true);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("key삭제 ,x :" + locX + ",y :" + locY + ", mapId :" + pc.getMapId()), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1080) + locX  + ",y :" + locY  + ", mapId :" + pc.getMapId(), true), true);
			}
		}
	}
	
	void characterInfoWand(L1PcInstance pc, int itemId, L1Object findObject){
		// 캐릭정보 검사막대
		if (itemId == 31141 && findObject != null && findObject instanceof L1PcInstance) {
			try {
				L1Command command = L1Commands.get("describe");
				Class<?> cls = Class.forName(complementClassName(command.getExecutorClassName()));
				L1CommandExecutor exe = (L1CommandExecutor) cls.getMethod("getInstance").invoke(null);
				exe.execute(pc, "describe", ((L1PcInstance)findObject).getName());
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else if (itemId == 31142 && findObject != null && findObject instanceof L1PcInstance) {
			try {
				L1Command command = L1Commands.get("checkcharacter");
				Class<?> cls = Class.forName(complementClassName(command.getExecutorClassName()));
				L1CommandExecutor exe = (L1CommandExecutor) cls.getMethod("getInstance").invoke(null);
				String param = ((L1PcInstance)findObject).getName() + " equipment";
				exe.execute(pc, "checkcharacter", param);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else if (itemId == 31143 && findObject != null && findObject instanceof L1PcInstance) {
			try {
				L1Command command = L1Commands.get("checkcharacter");
				Class<?> cls = Class.forName(complementClassName(command.getExecutorClassName()));
				L1CommandExecutor exe = (L1CommandExecutor) cls.getMethod("getInstance").invoke(null);
				String param = ((L1PcInstance)findObject).getName() + " account";
				exe.execute(pc, "checkcharacter", param);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			pc.sendPackets(L1ServerMessage.sm79);
		}
	}
	
	String complementClassName(String className) {
		if (className.contains(StringUtil.PeriodString)) {
			return className;
		}
		if (className.contains(StringUtil.CommaString)) {
			return className;
		}
		return "l1j.server.server.command.executor." + className;
	}

}


