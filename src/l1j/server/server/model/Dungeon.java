package l1j.server.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.bin.ShipCommonBinLoader;
import l1j.server.common.bin.ship.L1ShipStatus;
import l1j.server.common.bin.ship.ShipCommonBin;
import l1j.server.server.construct.message.L1NotificationMessege;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.Instance.L1PcInstance;
//import l1j.server.server.model.gametime.GameTimeClock;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;

public class Dungeon {
	private static Logger _log = Logger.getLogger(Dungeon.class.getName());

	private static Dungeon _instance = null;

	private static final Map<String, NewDungeon> _dungeonMap = new HashMap<String, NewDungeon>();
	
	private enum DungeonType {
		NONE, SHIP_FOR_FI, SHIP_FOR_HEINE, SHIP_FOR_PI, SHIP_FOR_HIDDENDOCK, SHIP_FOR_GLUDIN, SHIP_FOR_TI,
	};

	public static Dungeon getInstance() {
		if (_instance == null) {
			_instance = new Dungeon();
		}
		return _instance;
	}

	private static class NewDungeon {
		int _newX, _newY;
		short _newMapId;
		int _heading;
		DungeonType _dungeonType;
		int _min_level, _max_level;

		private NewDungeon(int newX, int newY, short newMapId, int heading, DungeonType dungeonType, int min_level, int max_level) {
			_newX			= newX;
			_newY			= newY;
			_newMapId		= newMapId;
			_heading		= heading;
			_dungeonType	= dungeonType;
			_min_level		= min_level;
			_max_level		= max_level;
		}
	}

	public static boolean dg(String key, L1PcInstance pc) {
		if (_dungeonMap.containsKey(key)) {
			NewDungeon newDungeon = _dungeonMap.get(key);
			short newMap = newDungeon._newMapId;
			int newX = newDungeon._newX, newY = newDungeon._newY, heading = newDungeon._heading;
			DungeonType dungeonType = newDungeon._dungeonType;
			int min_level = newDungeon._min_level, max_level = newDungeon._max_level;
			int level = pc.getLevel();
			if (min_level > 0 && min_level > level) {
				pc.sendPackets(L1ServerMessage.sm5359);// 입장에 필요한 조건이 맞지 않습니다.
				return false;
			}
			if (max_level > 0 && max_level < level) {
				pc.sendPackets(L1ServerMessage.sm5359);// 입장에 필요한 조건이 맞지 않습니다.
				return false;
			}
			boolean teleportable = false;
			if (dungeonType == DungeonType.NONE) {
				teleportable = true;
			} else {
				/*long nowtime = GameTimeClock.getInstance().getGameTime().getSeconds() % 86400;
				if(nowtime >= 15 * 360
						&& nowtime < 25 * 360 // 1.30~2. 30
						|| nowtime >= 45 * 360
						&& nowtime < 55 * 360 // 4.30~5. 30
						|| nowtime >= 75 * 360
						&& nowtime < 85 * 360 // 7.30~8. 30
						|| nowtime >= 105 * 360
						&& nowtime < 115 * 360 // 10.30~11. 30
						|| nowtime >= 135 * 360 && nowtime < 145 * 360 || nowtime >= 165 * 360 && nowtime < 175 * 360 || nowtime >= 195 * 360 && nowtime < 205 * 360
						|| nowtime >= 225 * 360 && nowtime < 235 * 360) {
					if((pc.getInventory().checkItem(40299, 1) && dungeonType == DungeonType.SHIP_FOR_GLUDIN) // TalkingIslandShiptoAdenMainland
							|| (pc.getInventory().checkItem(40301, 1) && dungeonType == DungeonType.SHIP_FOR_HEINE) // AdenMainlandShiptoForgottenIsland
							|| (pc.getInventory().checkItem(40302, 1) && dungeonType == DungeonType.SHIP_FOR_PI)) // ShipPirateislandtoHiddendock
						teleportable = true;
				}else if(nowtime >= 0 && nowtime < 360 || nowtime >= 30 * 360 && nowtime < 40 * 360 || nowtime >= 60 * 360 && nowtime < 70 * 360 || nowtime >= 90 * 360
						&& nowtime < 100 * 360 || nowtime >= 120 * 360 && nowtime < 130 * 360 || nowtime >= 150 * 360 && nowtime < 160 * 360 || nowtime >= 180 * 360
						&& nowtime < 190 * 360 || nowtime >= 210 * 360 && nowtime < 220 * 360) {
					if((pc.getInventory().checkItem(40298, 1) && dungeonType == DungeonType.SHIP_FOR_TI) // AdenMainlandShiptoTalkingIsland
							|| (pc.getInventory().checkItem(40300, 1) && dungeonType == DungeonType.SHIP_FOR_FI) // ForgottenIslandShiptoAdenMainland
							|| (pc.getInventory().checkItem(40303, 1) && dungeonType == DungeonType.SHIP_FOR_HIDDENDOCK)) // ShipHiddendocktoPirateisland
						teleportable = true;
				}*/
				teleportable = stayShipCheck(pc, dungeonType);
			}

			if (teleportable) {
				pc.getSkill().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
				pc.stopMpRegenerationByItem64Second();
				pc.getTeleport().initPortal(newX, newY, newMap, heading);
				if (newMap == 54) {
					pc.sendPackets(L1ServerMessage.sm6648);// 밤시간 동안 무작위 텔레토프를 할 수 없는 지역입니다.
					pc.sendPackets(L1NotificationMessege.NIGHT_CANNOT_TELEPORT);// 밤시간 동안 무작위 텔레토프를 할 수 없는 지역입니다.
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 배가 머무는 시간을 체크한다
	 * @param shipInfo
	 * @return boolean
	 */
	private static boolean stayShipCheck(L1PcInstance pc, DungeonType dungeonType){
		int shipType = -1;
		switch(dungeonType){
		case SHIP_FOR_FI:// 잊혀진 섬 행 배
			String today	= CommonUtil.WEEK_DAY_ARRAY[Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1];
			if (!today.matches(Config.DUNGEON.ISLAND_DAY_LOCAL_REGEX)) {
				return false;// 요일 제한
			}
			shipType = 0;
			break;
		default:break;
		}
		if (shipType == -1) {
			return false;
		}
		ShipCommonBin ship = ShipCommonBinLoader.getShip(shipType);
		if (ship == null
				|| pc.getLevel() < ship.getShip().get_levelLimit()
				|| ship.getStatus() != L1ShipStatus.STAY
				|| !ship.getShipTime().containsKey(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1)
				|| pc.getInventory().findItemNameId(ship.getShip().get_ticket()) == null) {
			return false;
		}
		return true;
	}
	
	public static void reload() {
		_dungeonMap.clear();
		_instance = new Dungeon();
	}

	private Dungeon() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM dungeon");
			rs = pstm.executeQuery();
			NewDungeon newDungeon = null;
			while(rs.next()){
				int srcMapId	= rs.getInt("src_mapid");
				int srcX		= rs.getInt("src_x");
				int srcY		= rs.getInt("src_y");
				String key		= new StringBuilder().append(srcMapId).append(srcX).append(srcY).toString();
				int newX		= rs.getInt("new_x");
				int newY		= rs.getInt("new_y");
				int newMapId	= rs.getInt("new_mapid");
				int heading		= rs.getInt("new_heading");
				DungeonType dungeonType = DungeonType.NONE;
				int min_level	= rs.getInt("min_level");
				int max_level	= rs.getInt("max_level");
				
				if ((srcX >= 33427 && srcX <= 33435) && srcY == 33506 && srcMapId == 4// 하이네 선착장 -> 잊혀진섬행 배
						|| (srcX >= 32733 && srcX <= 32736) && srcY == 32794 && srcMapId == 83) {// 잊혀진섬행 배 -> 하이네 선착장
					dungeonType = DungeonType.SHIP_FOR_FI;
				} else if ((srcX >= 32935 && srcX <= 32936) && srcY == 33057 && srcMapId == 70 // 잊혀진섬 선착장 -> 하이네행 배
						|| (srcX >= 32732 && srcX <= 32735) && srcY == 32796 && srcMapId == 84) {// 하이네행 배->잊혀진섬 선착장
					dungeonType = DungeonType.SHIP_FOR_HEINE;
				} else if ((srcX >= 32750 && srcX <= 32752) && srcY == 32874 && srcMapId == 445 // 숨겨진 선착장 -> 해적섬행 배
						|| (srcX >= 32731 && srcX <= 32733) && srcY == 32796 && srcMapId == 447) {// 해적섬행 배 -> 숨겨진 선착장
					dungeonType = DungeonType.SHIP_FOR_PI;
				} else if ((srcX >= 32296 && srcX <= 32298) && srcY == 33087 && srcMapId == 440 // 해적섬 선착장 -> 숨겨진 선착장행 배
						|| (srcX >= 32735 && srcX <= 32737) && srcY == 32794 && srcMapId == 446) {// 숨겨진 선착장행 배 -> 해적섬 선착장
					dungeonType = DungeonType.SHIP_FOR_HIDDENDOCK;
				} else if ((srcX >= 32630 && srcX <= 32632) && srcY == 32983 && srcMapId == 0 // 말하는섬 선착장 -> 글루디오행 배
						|| (srcX >= 32733 && srcX <= 32735) && srcY == 32796 && srcMapId == 5) {// 글루디오행 배 -> 말하는섬 선착장
					dungeonType = DungeonType.SHIP_FOR_GLUDIN;
				} else if ((srcX >= 32540 && srcX <= 32545) && srcY == 32728 && srcMapId == 4 // 글루디오 선착장 -> 말하는섬행 배
						|| (srcX >= 32734 && srcX <= 32737) && srcY == 32794 && srcMapId == 6) {// 말하는섬행 배 -> 글루디오 선착장
					dungeonType = DungeonType.SHIP_FOR_TI;
				}
				newDungeon = new NewDungeon(newX, newY, (short) newMapId, heading, dungeonType, min_level, max_level);
				if (_dungeonMap.containsKey(key)) {
					//_log.log(Level.WARNING, "같은 키의 dungeon 데이터가 있습니다. key=" + key);
					_log.log(Level.WARNING, "There is dungeon data with the same key. key=" + key);
				}
				_dungeonMap.put(key, newDungeon);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}

