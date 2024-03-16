package l1j.server.IndunSystem.fantasyisland;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.message.S_NotificationMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.message.S_NotificationMessage.display_position;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class FantasyIslandUtil {
	private static Logger _log = Logger.getLogger(FantasyIslandUtil.class.getName());
	private static FantasyIslandUtil _instance;
	public static FantasyIslandUtil getInstance() {
		if (_instance == null) {
			_instance = new FantasyIslandUtil();
		}
		return _instance;
	}
	
	public static final ServerBasePacket WAND_GET_MSG = new S_SystemMessage("$17948", true);// 강력한 공격마법 지원
	
	public static final ServerBasePacket[] ROUND_GFX = {
		new S_PacketBox(S_PacketBox.ROUND, 1, 3),// 1라운드
		new S_PacketBox(S_PacketBox.ROUND, 2, 3),// 2라운드
		new S_PacketBox(S_PacketBox.ROUND, 3, 3)// 3라운드
	};
	
	public static final ServerBasePacket[] UNICORN_FIRST_GREEN_MSG = {
		new S_NotificationMessage(display_position.screen_top, "$23794", "00 ff 00", 4),//.........저...도....세요.........
		new S_NotificationMessage(display_position.screen_top, "$23795", "00 ff 00", 4),//.......용사..님 저를.... 도..와주세요....
		new S_NotificationMessage(display_position.screen_top, "$23796", "00 ff 00", 4)//...제가.. 보이시나요?? 제발 도와주세요!!!!!
	};
	
	public static final ServerBasePacket[] ROUND_READY_GREEN_MSG = {
		new S_NotificationMessage(display_position.screen_top, "$17701", "00 ff 00", 4),// 적들이 몰려오고 있습니다.
		new S_NotificationMessage(display_position.screen_top, "$17703", "00 ff 00", 4),// 적들이 더 몰려옵니다. 준비해 주세요
		new S_NotificationMessage(display_position.screen_top, "$17947", "00 ff 00", 4)// 마법 막대를 이용해 적을 처치해주세요.
	};
	
	public static final ServerBasePacket[] SOUL_SPAWN_GREEN_MSG = {
		new S_NotificationMessage(display_position.screen_top, "$17941", "00 ff 00", 4),// 불의 대정령이 나타났습니다!!!
		new S_NotificationMessage(display_position.screen_top, "$17942", "00 ff 00", 4),// 바람의 대정령이 나타났습니다!!!
		new S_NotificationMessage(display_position.screen_top, "$17943", "00 ff 00", 4),// 물의 대정령이 나타났습니다!!!
		new S_NotificationMessage(display_position.screen_top, "$17944", "00 ff 00", 4)// 땅의 대정령이 나타났습니다!!!
	};
	
	public static final ServerBasePacket[] BOSS_SPAWN_GRREEN_MSG = {
		new S_NotificationMessage(display_position.screen_top, "$17995 : $17713", "00 ff 00", 4),// 구미호: 유니콘을 빼앗아가려고? 그렇게 놔둘 순 없지!!
		new S_NotificationMessage(display_position.screen_top, "$24667 : $17713", "00 ff 00", 4),// 아비쉬: 유니콘을 빼앗아가려고? 그렇게 놔둘 순 없지!!
		new S_NotificationMessage(display_position.screen_top, "$24668 : $17713", "00 ff 00", 4)// 아즈모단: 유니콘을 빼앗아가려고? 그렇게 놔둘 순 없지!!
	};
	
	public static final ServerBasePacket[] RAID_COMPLETE_GREEN_MSG = {
		new S_NotificationMessage(display_position.screen_top, "$17708", "00 ff 00", 4),// 감사합니다!
		new S_NotificationMessage(display_position.screen_top, "$17709", "00 ff 00", 4),// 당분간 그것은 돌아올 수 없을 것입니다.
		new S_NotificationMessage(display_position.screen_top, "$17710", "00 ff 00", 4)// 어서 몽환의 섬으로 돌아가 봐야겠군요.
	};
	
	public static final ServerBasePacket UNICORN_DANGEROUS = new S_NotificationMessage(display_position.screen_top, "$17969", "00 ff 00", 4);// 위험한 상황입니다!! 정황 막대를 사용하셔야 합니다!!!
	
	private final FastMap<Integer, FastTable<FantasyIslandObject>> _data;

	private FantasyIslandUtil() {
		_data = load();
	}
	
	private FastMap<Integer, FastTable<FantasyIslandObject>> load(){
		FastMap<Integer, FastTable<FantasyIslandObject>> data = new FastMap<>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_fantasyIsland ORDER BY id ASC");
			rs = pstm.executeQuery();
			FantasyIslandObject obj = null;
			FastTable<FantasyIslandObject> list = null;
			while (rs.next()) {
				obj = new FantasyIslandObject(rs);
				list = data.get(obj.getType());
				if (list == null) {
					list = new FastTable<FantasyIslandObject>();
					data.put(obj.getType(), list);
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
		return data;
	}

	public void spawn(int mapid, int type) {
		spawn(mapid, type, false);
	}

	public ArrayList<L1NpcInstance> spawn(int mapid, int type, boolean returnList) {
		L1Npc temp = null;
		L1NpcInstance npc = null;
		ArrayList<L1NpcInstance> list = null;
		if (returnList) {
			list = new ArrayList<L1NpcInstance>();
		}
		try {
			for (FantasyIslandObject obj : _data.get(type)) {// 타입에 해당하는 데이타
				for (int i = 0 ; i < obj.getCount(); i++) {// 수량만큼 반복
					temp = obj.getTemplate();
					if (temp != null) {
						try {
							npc = NpcTable.getInstance().newNpcInstance(temp.getNpcId());
							npc.setId(IdFactory.getInstance().nextId());
							npc.setX(obj.getLocX());
							npc.setY(obj.getLocY());
							npc.setMap((short) mapid);
							npc.setHomeX(npc.getX());
							npc.setHomeY(npc.getY());
							npc.getMoveState().setHeading(obj.getHeading());
							npc.setLightSize(temp.getLightSize());
							npc.getLight().turnOnOffLight();
							L1World world = L1World.getInstance();
							world.storeObject(npc);
							world.addVisibleObject(npc);
							npc.onNpcAI();
							if (returnList) {
								list.add(npc);
							}
						} catch (Exception e) {
							_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void spawnDelay(int mapid, int type, long delay) {
		L1Npc temp = null;
		L1NpcInstance npc = null;
		try {
			for (FantasyIslandObject obj : _data.get(type)) {// 타입에 해당하는 데이타
				for (int i = 0 ; i < obj.getCount(); i++) {// 수량만큼 반복
					temp = obj.getTemplate();
					if (temp != null) {
						try {
							npc = NpcTable.getInstance().newNpcInstance(temp.getNpcId());
							npc.setId(IdFactory.getInstance().nextId());
							npc.setX(obj.getLocX());
							npc.setY(obj.getLocY());
							npc.setMap((short) mapid);
							npc.setHomeX(npc.getX());
							npc.setHomeY(npc.getY());
							npc.getMoveState().setHeading(obj.getHeading());
							npc.setLightSize(temp.getLightSize());
							npc.getLight().turnOnOffLight();
							L1World world = L1World.getInstance();
							world.storeObject(npc);
							world.addVisibleObject(npc);
							npc.onNpcAI();
							int npcId = npc.getNpcId();
							if (npcId == 7200016 
									|| npcId == 7200017 
									|| npcId == 7200018 
									|| npcId == 7200019
									|| npcId == 7200037 
									|| npcId == 7200038 
									|| npcId == 7200039 
									|| npcId == 7200040) {
								soulSpawnMent(npc.getNpcId(), mapid);
							}
							Thread.sleep(delay);
						} catch (Exception e) {
							_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public L1NpcInstance bossSpawn(int mapid, int type) {
		L1Npc temp = null;
		L1NpcInstance npc = null;
		try {
			for (FantasyIslandObject obj : _data.get(type)) {// 타입에 해당하는 데이타
				temp = obj.getTemplate();
				if (temp != null) {
					try {
						npc = NpcTable.getInstance().newNpcInstance(temp.getNpcId());
						npc.setId(IdFactory.getInstance().nextId());
						npc.setX(obj.getLocX());
						npc.setY(obj.getLocY());
						npc.setMap((short) mapid);
						npc.setHomeX(npc.getX());
						npc.setHomeY(npc.getY());
						npc.getMoveState().setHeading(obj.getHeading());
						npc.setLightSize(temp.getLightSize());
						npc.getLight().turnOnOffLight();
						L1World world = L1World.getInstance();
						world.storeObject(npc);
						world.addVisibleObject(npc);
						npc.onNpcAI();
					} catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
				temp = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return npc;
	}
	
	private void soulSpawnMent(int npcId, int mapId){
		for (L1PcInstance pc : L1World.getInstance().getMapPlayer(mapId)) {
			if (pc == null) {
				continue;
			}
			switch(npcId){
			case 7200016:case 7200039:// 바람의 대정령
				pc.sendPackets(SOUL_SPAWN_GREEN_MSG[1]);// 바람의 대정령이 나타났습니다!!!
				break;
			case 7200017:case 7200040:// 물의 대정령
				pc.sendPackets(SOUL_SPAWN_GREEN_MSG[2]);// 물의 대정령이 나타났습니다!!!
				break;
			case 7200018:case 7200038:// 땅의 대정령
				pc.sendPackets(SOUL_SPAWN_GREEN_MSG[3]);// 땅의 대정령이 나타났습니다!!!
				break;
			case 7200019:case 7200037:// 불의 대정령
				pc.sendPackets(SOUL_SPAWN_GREEN_MSG[0]);// 불의 대정령이 나타났습니다!!!
				break;
			}
		}
	}
}

