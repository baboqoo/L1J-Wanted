package l1j.server.server.controller.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.data.ChatType;
import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.L1CastleType;
import l1j.server.server.construct.L1TownType;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.datatables.WarTimeTable;
import l1j.server.server.datatables.WarTimeTable.WarTimeData;
import l1j.server.server.datatables.WarTimeTable.WarTimeInfo;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1WarSpawn;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1CastleGuardInstance;
import l1j.server.server.model.Instance.L1CrownInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1GuardInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_CastleMaster;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SiegeEventNoti;
import l1j.server.server.serverpackets.S_SiegeEventNoti.SIEGE_EVENT_KIND;
import l1j.server.server.serverpackets.S_SiegeInjuryTimeNoti;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Castle;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;
//import manager.Manager;  // MANAGER DISABLED

/**
 * 공성전 컨트롤러
 * @author LinOffice
 */
public class War implements ControllerInterface {
	private static class newInstance {
		public static final War INSTANCE = new War();
	}
	public static War getInstance() {
		return newInstance.INSTANCE;
	}
	
	private L1Castle[] _l1castle		= new L1Castle[8];
	private long[] _war_start_time		= new long[8];
	private long[] _war_end_time		= new long[8];
	private boolean[] _is_now_war		= new boolean[8];
	//private boolean[] 강제종료				= new boolean[8];
	private boolean[] _forceShutdown = new boolean[8];
	private WarTimer[] _war_timer		= new WarTimer[8];
	private boolean[] _war_end			= new boolean[8];
	private String[] _war_defence_clan	= new String[8];
	private String[] _war_attack_clan	= new String[8];
	private int[] _war_time				= new int[8];
	public boolean _isWolrdWarGiran, _isWolrdWarOrc, _isWolrdWarHeine;
	
	private War() {
		for (int i = 0; i < _l1castle.length; i++) {
			_l1castle[i]		= CastleTable.getInstance().getCastleTable(i + 1);
			_war_start_time[i]	= _l1castle[i].getWarTime().getTimeInMillis();
			_war_end_time[i]	= LongType_setTime(_war_start_time[i], Config.PLEDGE.WAR_TIME_UNIT, Config.PLEDGE.WAR_TIME);
		}
	}
	
	public void reloadTime(){
		for (int i = 0; i < _l1castle.length; i++) {
			_l1castle[i]		= CastleTable.getInstance().getCastleTable(i + 1);
			_war_start_time[i]	= _l1castle[i].getWarTime().getTimeInMillis();
			_war_end_time[i]	= LongType_setTime(_war_start_time[i], Config.PLEDGE.WAR_TIME_UNIT, Config.PLEDGE.WAR_TIME);
		}
	}
	
	public String WarTimeString(int castle) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Timestamp(_war_start_time[castle - 1]));
	}

	private long LongType_setTime(long o, int type, long time) {
		if (Calendar.DATE == type) {
			return o + (60000 * 60 * 24 * time);
		}
		if (Calendar.HOUR_OF_DAY == type) {
			return o + (60000 * 60 * time);
		}
		if (Calendar.MINUTE == type) {
			return o + (60000 * time);
		}
		return 0;
	}

	public void setWarStartTime(String name, Calendar cal) {
		if (StringUtil.isNullOrEmpty(name)) {
			return;
		}
		for (int i = 0; i < _l1castle.length; i++) {
			L1Castle castle = _l1castle[i];
			if (castle.getName().startsWith(name)) {
				castle.setWarTime(cal);
				_war_start_time[i] = ((Calendar) cal.clone()).getTimeInMillis();
				_war_end_time[i] = LongType_setTime(_war_start_time[i], Config.PLEDGE.WAR_TIME_UNIT, Config.PLEDGE.WAR_TIME);
				
				/** 월드 공성전 셋팅 **/
				if (castle.getName().startsWith(L1TownType.GIRAN.getName()) && !_isWolrdWarGiran) {
					_isWolrdWarGiran = true;
					wolrdWarStartAction(castle.getName(), 33624, 32753, (short)4, 800702);
				} else if (castle.getName().startsWith(L1TownType.ORCISH_FOREST.getName()) && !_isWolrdWarOrc) {
					_isWolrdWarOrc = true;
					wolrdWarStartAction(castle.getName(), 32787, 32328, (short)4, 800703);
				} else if (castle.getName().startsWith(L1TownType.HEINE.getName()) && !_isWolrdWarHeine) {
					_isWolrdWarHeine = true;
					wolrdWarStartAction(castle.getName(), 0, 0, (short)0, 0);
				}
				break;
			}
		}
	}
	
	private void wolrdWarStartAction(String casleName, int x, int y, short mapId, int npcId){
		//String message = String.format("잠시후 %s 월드 공성전이 시작됩니다.", casleName);
		String message = String.format(S_SystemMessage.getRefText(43) + "%s " + S_SystemMessage.getRefText(14), casleName);
		L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(message, true), true);
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message), true);
		if (npcId > 0) {
			L1SpawnUtil.spawn2(x, y, mapId, 0, npcId, 0, 7200 * 1000, 0);// 균열 열림
		}
	}

	public void setWarExitTime(L1PcInstance pc, String name) {
		if (StringUtil.isNullOrEmpty(name)) {
			return;
		}
		for (int i = 0; i < _l1castle.length; i++) {
			L1Castle castle = _l1castle[i];
			if (castle.getName().startsWith(name)) {
				_forceShutdown[castle.getId() - 1] = true;
				//pc.sendPackets(new S_SystemMessage(String.format("%s 공성 강제 종료", castle.getName())), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(83), castle.getName()), true);
				break;
			}
		}
	}

	@Override
	public void execute() {
		try {
			if (Config.PLEDGE.WAR_TIME_FIX) {
				fixTimeSetting();
			}
			checkWarTime();// 전쟁 시간을 체크
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void execute(L1PcInstance pc) {
	}
	
	private void checkWarTime() {
		long currentTime = System.currentTimeMillis();
		for (int i = 0; i < _l1castle.length; i++) {
			if (currentTime >= _war_start_time[i] && currentTime <= _war_end_time[i]) {// 공성시간 내
				if (_forceShutdown[i] == true || _war_end[i] == true) {
					warTimerStop(i);
					endWar(i);
					continue;
				}
				if (_is_now_war[i] == false) {// 공성시작
					startWar(i);
					continue;
				}
			} else {
				if (_forceShutdown[i] == true || _is_now_war[i] == true) {
					warTimerStop(i);
					endWar(i);
					continue;
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void fixTimeSetting(){
		FastTable<WarTimeData> list = WarTimeTable.getWarTimeDatas();
		if (list == null || list.isEmpty()) {
			return;
		}
		long currentTime = System.currentTimeMillis();
		for (WarTimeData data : list) {
			if (data._times == null || data._times.isEmpty()) {
				continue;
			}
			if (!_is_now_war[data._castleId - 1] && !(_war_start_time[data._castleId - 1] <= currentTime && _war_end_time[data._castleId - 1] >= currentTime)) {
				Calendar cal = (Calendar) Calendar.getInstance().clone();
				for (WarTimeInfo info : data._times) {// 성의 공성시간
					if (info._day == cal.getTime().getDay()) {// 지정한 날과 같을때
						long nextTime = CommonUtil.getNextDayHourMinutTime(info._day, info._hour, info._minute);
						if (_war_start_time[data._castleId - 1] < nextTime) {// 다음 공성시간이 지정된 시간보다 클경우
							cal.setTimeInMillis(nextTime);
							CastleTable.getInstance().updateWarTime(_l1castle[data._castleId - 1].getName(), cal);
							setWarStartTime(_l1castle[data._castleId - 1].getName(), cal);
							break;
						}
					}
				}
			}
		}
	}
	
	public boolean isNowWarFromName(String name) {
		for (int i = 0; i < _l1castle.length; i++) {
			L1Castle castle = _l1castle[i];
			if (castle.getName().startsWith(name)) {
				return isNowWar(castle.getId());
			}
		}
		return false;
	}

	public boolean isNowWar(int castle_id) {
		if (castle_id <= 0) {
			return false;
		}
		return _is_now_war[castle_id - 1];
	}

	public void checkCastleWar(L1PcInstance player) {
		boolean ck = false;
		for (int i = 0; i < _l1castle.length; i++) {
			if (_is_now_war[i]) {
				if (!ck) {
					player.sendPackets(L1SystemMessage.LOGIN_WAR_CHECK_MSG);
					player.sendPackets(L1SystemMessage.LOGIN_WAR_CASTLE_MSG);
					ck = true;
				}
				String castleName = L1CastleType.fromInt(i + 1).getName();
				String clanName = StringUtil.EmptyString;
				for (L1War war : L1World.getInstance().getWarList()) {// 전쟁리스트를 취득
					if (war.GetCastleId() == i + 1) {
						clanName = war.GetDefenceClanName();
						break;
					}
				}
				//player.sendPackets(new S_SystemMessage(String.format("[%s = %s 혈맹]", castleName, clanName)), true);
				player.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(84), castleName, clanName), true);
				player.sendPackets(new S_SiegeEventNoti(i + 1, SIEGE_EVENT_KIND.SIEGE_EVENT_PROGRESSING), true);// 공성전 진행중
			}
		}
	}

	//private void 모의전종료() {
	private void endSimulation() {	
		for (L1War war : L1World.getInstance().getWarList()) {
			war.CeaseWar(war.GetDefenceClanName(), war.GetEnemyClanName(war.GetDefenceClanName()));
//AUTO SRM: 			L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, String.format("[%s 혈맹] VS [%s 혈맹]의 전쟁을 강제 종결 시킵니다.", war.GetDefenceClanName(), war.GetEnemyClanName(war.GetDefenceClanName()))), true); // CHECKED OK
			L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,String.format("[" + "%s " + S_SystemMessage.getRefTextNS(917) + "%s " + S_SystemMessage.getRefText(918), war.GetDefenceClanName(), war.GetEnemyClanName(war.GetDefenceClanName()))), true);
		}
	}

	private class WarTimer implements Runnable {
		private int castleid = 0;
		private int count = 0;
		public boolean on = true;

		public WarTimer(int _castleid) {
			castleid = _castleid;
		}

		@Override
		public void run() {
			// TODO 자동 생성된 메소드 스텁
			try {
				if (!on) {
					return;
				}
				if (count-- > 0) {
					GeneralThreadPool.getInstance().schedule(this, 100);
					return;
				}
				if (_war_time[castleid]-- > 0) {
					count = 9;
					GeneralThreadPool.getInstance().schedule(this, 100);
					return;
				}
				_war_end[castleid] = true;
			} catch (Exception e) {
			}
		}
	}
	
	public void warTimerStop(int i){
		try {
			WarTimer timer = _war_timer[i];
			if (timer != null) {
				timer.on = false;
				_war_timer[i] = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void startWar(int i){
		boolean worldWar = _l1castle[i].getCastle() == L1CastleType.ORC || _l1castle[i].getCastle() == L1CastleType.GIRAN || _l1castle[i].getCastle() == L1CastleType.HEINE;
		L1WarSpawn warspawn = null;
		try {
			// 기를 spawn 한다
			L1Clan clan = null;
			warspawn = new L1WarSpawn();
			warspawn.SpawnFlag(i + 1);
			// warspawn.SpawnCrown(i + 1);
			// 성문을 수리해 닫는다

			for (L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()) {
				if (L1CastleLocation.checkInWarArea(i + 1, door)) {
					door.setAutoStatus(0);// 자동수리를 해제
					door.repairGate();
				}
			}

			endSimulation();

			if (_l1castle[i].getCastleSecurity() == 1) {
				securityStart(_l1castle[i]);// 치안관리
			}
			L1World world = L1World.getInstance();
			world.broadcastPacketToAll(new S_SiegeEventNoti(i + 1, SIEGE_EVENT_KIND.SIEGE_EVENT_KIND_START), true);// 공성전 시작
//AUTO SRM: 			world.broadcastPacketToAll(new S_SystemMessage(worldWar ? String.format("월드 공성: %s 월드 공성전이 시작되었습니다!", _l1castle[i].getName()) : String.format("공성: %s 공성전이 시작되었습니다!", _l1castle[i].getName())), true); // CHECKED OK
			world.broadcastPacketToAll(new S_SystemMessage(worldWar ? String.format(S_SystemMessage.getRefText(911) + "%s " + S_SystemMessage.getRefText(912), _l1castle[i].getName()) : String.format(S_SystemMessage.getRefText(913) + " %s " + S_SystemMessage.getRefText(914), _l1castle[i].getName()), true), true);
			//Manager.getInstance().castleMessageAppend(_l1castle[i].getName(), " start"); // MANAGER DISABLED
			// L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.MSG_WAR_BEGIN, i + 1), true);// %s의 공성전이 시작되었습니다.
			int[] loc = new int[3];
			for (L1PcInstance pc : world.getAllPlayers()) {
				int castleId = i + 1;
				if (L1CastleLocation.checkInWarArea(castleId, pc) && !pc.isGm()) {
					clan = world.getClan(pc.getClanName());
					if (clan != null) {
						if (clan.getCastleId() == castleId) {
							int time = (int) ((_war_end_time[castleId - 1] - System.currentTimeMillis()) / 1000);
							pc.sendPackets(new S_SiegeInjuryTimeNoti(S_SiegeInjuryTimeNoti.SIEGE_KIND.SIEGE_DEFFENCE, time, clan.getClanName()), true);
							continue;
						}
					}

					loc = L1CastleLocation.getGetBackLoc(castleId);
					pc.getTeleport().start(loc[0], loc[1], (short) loc[2], 5, true);
				}
			}
			for (L1Clan tclan : world.getAllClans()) {
				if (i + 1 == tclan.getCastleId()) {
					_war_defence_clan[i] = tclan.getClanName();
					break;
				}
			}
			_war_time[i] = 60 * 50;
			GeneralThreadPool.getInstance().schedule(_war_timer[i] = new WarTimer(i), 1);
			
			for (L1GuardInstance guard : world.getAllGuard()) {
				int[] locb = L1CastleLocation.getWarArea(i + 1);
				if (guard.getMapId() == locb[4]
						&& guard.getX() >= locb[0]
						&& guard.getX() <= locb[1]
						&& guard.getY() >= locb[2]
						&& guard.getY() <= locb[3]) {
					Broadcaster.broadcastPacket(guard, new S_NpcChatPacket(guard, S_SystemMessage.getRefText(13), ChatType.CHAT_NORMAL), true);
				}
			}
			for (L1CastleGuardInstance guard : world.getAllCastleGuard()) {
				int[] locb = L1CastleLocation.getWarArea(i + 1);
				if (guard.getMapId() == locb[4]
						&& guard.getX() >= locb[0]
						&& guard.getX() <= locb[1]
						&& guard.getY() >= locb[2]
						&& guard.getY() <= locb[3]) {
					Broadcaster.broadcastPacket(guard, new S_NpcChatPacket(guard, S_SystemMessage.getRefText(13), ChatType.CHAT_NORMAL), true);
				}
			}
			
			/** 월드 공성전  **/
			if (worldWar) {
				L1SpawnUtil.worldWarSpawn(_l1castle[i].getId());
			}
		} catch (Exception e) {
			//System.out.println("공성 시간 내 시작 에러 : \r\n");
			System.out.println("Error starting during siege time: \r\n");
			e.printStackTrace();
		}
		_is_now_war[i] = true;
	}
	
	private void endWar(int i){
		boolean worldWar = _l1castle[i].getCastle() == L1CastleType.ORC || _l1castle[i].getCastle() == L1CastleType.GIRAN || _l1castle[i].getCastle() == L1CastleType.HEINE;
		L1WarSpawn warspawn = null;
		try {
			L1World world = L1World.getInstance();
			world.broadcastPacketToAll(new S_SiegeEventNoti(i + 1, SIEGE_EVENT_KIND.SIEGE_EVENT_KIND_END), true);// 공성전 종료
//AUTO SRM: 			world.broadcastPacketToAll(new S_SystemMessage(worldWar ? "월드 공성전이 종료되었습니다!" : String.format("공성: %s 공성전이 종료되었습니다!", _l1castle[i].getName())), true); // CHECKED OK
			world.broadcastPacketToAll(new S_SystemMessage(worldWar ? S_SystemMessage.getRefText(915) : String.format(S_SystemMessage.getRefText(913) + "%s " + S_SystemMessage.getRefText(916), _l1castle[i].getName()), true), true);
			//Manager.getInstance().castleMessageAppend(_l1castle[i].getName(), "end"); // MANAGER DISABLED
			// L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.MSG_WAR_END, i + 1), true); // %s의 공성전이 종료했습니다.
			_war_start_time[i] = LongType_setTime(_war_start_time[i], Config.PLEDGE.WAR_INTERVAL_UNIT, Config.PLEDGE.WAR_INTERVAL);
			_war_end_time[i] = LongType_setTime(_war_end_time[i], Config.PLEDGE.WAR_INTERVAL_UNIT, Config.PLEDGE.WAR_INTERVAL);
			_l1castle[i].setWarTime(castle_Calendar_save(_war_start_time[i]));
			_l1castle[i].setTaxRate(10); // 세율10프로
			//_l1castle[i].setPublicMoney(0); // 공금클리어
			CastleTable.getInstance().updateCastle(_l1castle[i]);
			securityStart(_l1castle[i]);// 치안관리
			
			int castle_id = i + 1;
			int castle_id2 = 0; 
			try {
				L1ItemInstance createItem = null;
				for (L1PcInstance pc : world.getAllPlayers()) {
					L1Clan clan = world.getClan(pc.getClanName());
					if (clan != null && !pc.isPrivateShop()) {
						castle_id2 = clan.getCastleId();
						if (castle_id2 == castle_id) {
							for (L1ItemInstance item : getWarWinItemList()) {
								pc.getInventory().storeItem(item.getItemId(), item.getCount());
							}
							
							// 지급할 아이템 아이디
							String[] itemIds = null;
							try {
								int idx = Config.PLEDGE.WAR_REWARD_ITEMID.indexOf(StringUtil.CommaString);
								// ,로 있을경우
								if (idx > -1) {
									itemIds = Config.PLEDGE.WAR_REWARD_ITEMID.split(StringUtil.CommaString);
								} else {
									itemIds = new String[1];
									itemIds[0] = Config.PLEDGE.WAR_REWARD_ITEMID;
								}
							} catch(Exception e) {
								e.printStackTrace();
							}
							// 지급할 아이템 갯수
							String[] counts = null;
							try{
								int idx = Config.PLEDGE.WAR_REWARD_NUMBER.indexOf(StringUtil.CommaString);
								// ,로 있을경우
								if (idx > -1) {
									counts = Config.PLEDGE.WAR_REWARD_NUMBER.split(StringUtil.CommaString);
								} else {
									counts = new String[1];
									counts[0] = Config.PLEDGE.WAR_REWARD_NUMBER;
								}
							} catch(Exception e) {
								e.printStackTrace();
							}
							// 아이템 아이디나 카운트가 없을경우
							if (itemIds == null || counts == null) {
								return;
							}
							for (int j = 0; j < itemIds.length; j++) {
								int itemId = Integer.parseInt(itemIds[j]);
								int count = Integer.parseInt(counts[j]);
								if (itemId <= 0 || count <= 0) {
									continue;
								}
								createItem = pc.getInventory().storeItem(itemId, count);
								if (createItem != null) {
									//pc.sendPackets(new S_SystemMessage(String.format("%s (%d)을 얻었습니다.", createItem.getDescKr(), count)), true);
									//pc.sendPackets(new S_SystemMessage(String.format("%s (%d)을 얻었습니다.", createItem.getDesc(), count)), true);
									pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(85), createItem.getDesc(), String.valueOf(count)), true);
								}
							}
							
							if (worldWar && L1CastleLocation.checkInWarArea(castle_id, pc)) {
								createItem = pc.getInventory().storeItem(130050, 1); // 개선용사의 상자
								if (createItem != null) {
									//pc.sendPackets(new S_ServerMessage(403, createItem.getDescKr()), true); //아이템을 획득 멘트
									pc.sendPackets(new S_ServerMessage(403, createItem.getDesc()), true); //아이템을 획득 멘트
								}
							}
						} else {
							if (worldWar && L1CastleLocation.checkInWarArea(castle_id, pc)) {
								createItem = pc.getInventory().storeItem(130051, 1); // 참전용사의 상자
								if (createItem != null) {
									//pc.sendPackets(new S_ServerMessage(403, createItem.getDescKr()), true); //아이템을 획득 멘트
									pc.sendPackets(new S_ServerMessage(403, createItem.getDesc()), true); //아이템을 획득 멘트
								}
							}
						}
					}
				}
			} catch (Exception e) {
				//System.out.println("공성아이템지급에러" + e);
				System.out.println("Siege item payment error" + e);
			}
			
			L1FieldObjectInstance flag = null;
			L1CrownInstance crown = null;
			L1TowerInstance tower = null;
			L1NpcInstance npc = null;
			for (L1Object l1object : world.getObject()) {
				if (l1object == null) {
					continue;
				}
				// 전쟁 에리어내의 기를 지운다
				if (l1object instanceof L1FieldObjectInstance) {
					flag = (L1FieldObjectInstance) l1object;
					if (L1CastleLocation.checkInWarArea(castle_id, flag)) {
						flag.deleteMe();
					}
				} else if (l1object instanceof L1CrownInstance) {// 크라운이 있는 경우는, 크라운을 지워 타워를 spawn 한다
					crown = (L1CrownInstance) l1object;
					if (L1CastleLocation.checkInWarArea(castle_id, crown)) {
						crown.deleteMe();
					}
				} else if (l1object instanceof L1TowerInstance) {
					tower = (L1TowerInstance) l1object;
					if (L1CastleLocation.checkInWarArea(castle_id, tower)) {
						tower.deleteMe();
					}
				} else if (l1object instanceof L1NpcInstance) {// 월드 공성전 몬스터가 있는 경우 지운다
					npc = (L1NpcInstance) l1object;
					if (castle_id == L1CastleType.ORC.getId() && (npc.getNpcId() == 800551 || (npc.getNpcId() >= 800604 && npc.getNpcId() <= 800607))) {// 오크요새
						npc.deleteMe();
					} else if (castle_id == L1CastleType.GIRAN.getId() && (npc.getNpcId() == 800553 || (npc.getNpcId() >= 800612 && npc.getNpcId() <= 800615))) {// 기란성
						npc.deleteMe();
					} else if (castle_id == L1CastleType.HEINE.getId() && (npc.getNpcId() == 800554 || (npc.getNpcId() >= 800616 && npc.getNpcId() <= 800619))) {// 하이네성
						npc.deleteMe();
					}
				}
			}

			warspawn = new L1WarSpawn();
			warspawn.SpawnTower(castle_id);
			for (L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()) {
				if (L1CastleLocation.checkInWarArea(castle_id, door)) {
					door.repairGate();
				}
			}
			_war_defence_clan[i] = null;
			_war_attack_clan[i] = null;
			_war_time[i] = 0;
			for (L1PcInstance tp : world.getAllPlayers()) {
				if (tp.warZone) {
					tp.warZone = false;
					tp.sendPackets(S_SiegeInjuryTimeNoti.CASTLE_WAR_TIME_NONE);
				}
			/*	if (tp.hasSkillEffect(L1SkillId.주군의버프)) {
					tp.removeSkillEffect(L1SkillId.주군의버프);
					tp.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 490, false), true);
				}*/

			}
			/** 공성 정상적으로 종료후 성 설정 **/
			for (L1Clan c : world.getAllClans()) {
				if (i + 1 == c.getCastleId()) {
					/** 월드 공성전 종료후 성혈 삭제 **/
					if (worldWar) {
						c.setCastleId(0);
					}
					ClanTable.getInstance().updateClan(c);
					if (i == 0) {
						L1SpawnUtil.spawn(33067, 32764, (short) 4, 6, 6200008, 0, 3600000, c);// 켄성
					} else if (i == 1) {
						L1SpawnUtil.spawn(32734, 32441, (short) 4, 6, 6200008, 0, 3600000, c);// 오크성
					} else if (i == 3) {
						L1SpawnUtil.spawn(32441, 32805, (short) 4, 6, 6200008, 0, 3600000, c);// 기란성
					}
					break;
				}
			}
			
			/** 월드 공성전 종료 시간 설정 **/
			if (worldWar) {
				world.broadcastPacketToAll(new S_CastleMaster(castle_id, 0), true);
				GeneralThreadPool.getInstance().execute(new WorldWarEnd(_l1castle[i].getCastle()));
			}
		} catch (Exception e) {
			//System.out.println("공성 시간 외  종료 에러 : \r\n");
			System.out.println("Error ending siege outside of time: \r\n");
			e.printStackTrace();
		}
		_is_now_war[i] = false;
		_war_end[i] = false;
		_forceShutdown[i] = false;
	}

	public void AttackClanSetting(int castleid, String name) {
		_war_attack_clan[castleid - 1] = name;
		try {
			WarTimer timer = _war_timer[castleid - 1];
			if (timer != null) {
				timer.on = false;
				_war_timer[castleid - 1] = null;
			}
			if (AttackDefenceCheck(castleid) == 1) {
			int time = (int) ((_war_end_time[castleid - 1] - System.currentTimeMillis()) / 1000);
				_war_time[castleid - 1] = time;
			} else
				_war_time[castleid - 1] = 20 * 60;
			GeneralThreadPool.getInstance().schedule(_war_timer[castleid - 1] = new WarTimer(castleid - 1), 1);
		} catch (Exception e) {
		}
	}

	public int AttackDefenceCheck(int castleid) {
		try {
			if (_war_attack_clan[castleid - 1] == null
					|| _war_attack_clan[castleid - 1].equalsIgnoreCase(StringUtil.EmptyString)) {
				return 1;
			}
			return _war_defence_clan[castleid - 1].equalsIgnoreCase(_war_attack_clan[castleid - 1]) ? 1 : 2;
		} catch (Exception e) {
			return 1;
		}
	}

	public void WarTime_SendPacket(int castleid, L1PcInstance pc) {
		if (isNowWar(castleid)) {
			S_SiegeInjuryTimeNoti.SIEGE_KIND siegeKind = null; // 1 수성 2 공성 
			String clanname = null;
			if (pc.getClan() != null) {
				L1Clan clan = pc.getClan();
				clanname = pc.getClan().getClanName();
				if (castleid == pc.getClan().getCastleId()) {
					siegeKind = S_SiegeInjuryTimeNoti.SIEGE_KIND.SIEGE_DEFFENCE;
				} else {
					List<L1War> wars = L1World.getInstance().getWarList(); // 전쟁 리스트를 취득
					for (L1War war : wars) {
						boolean isInWar = war.CheckClanInWar(clan.getClanName());
						boolean isAttackClan = war.CheckAttackClan(clan.getClanName());
						if (isInWar && isAttackClan) {
							siegeKind = S_SiegeInjuryTimeNoti.SIEGE_KIND.SIEGE_ATTACK;
							break;
						}
					}
				}
			}
			
			if (siegeKind == null) {
				siegeKind = S_SiegeInjuryTimeNoti.SIEGE_KIND.SIEGE_ATTACK;
				//clanname = "무선포혈맹";
				clanname = "WirelessPledge";
			}
			int time = (int) ((_war_end_time[castleid - 1] - System.currentTimeMillis()) / 1000);
			pc.sendPackets(new S_SiegeInjuryTimeNoti(siegeKind, time, clanname), true);

			boolean is_buff = eBloodPledgeRankType.isAuthRankAtEliteKnight(pc.getBloodPledgeRank());
			if (is_buff && !pc.getSkill().hasSkillEffect(L1SkillId.BUFF_JUGUN)) {
				pc.getSkill().setSkillEffect(L1SkillId.BUFF_JUGUN, 3600000);
				pc.sendPackets(S_PacketBox.JUGUN_BUFF_ON);
			} else if (!is_buff && pc.getSkill().hasSkillEffect(L1SkillId.BUFF_JUGUN)) {
				pc.getSkill().removeSkillEffect(L1SkillId.BUFF_JUGUN);
				pc.sendPackets(S_PacketBox.JUGUN_BUFF_OFF);
			}
		} else {
			pc.warZone = false;
			//pc.sendPackets(S_War.CASTLE_WAR_TIME_NONE);
			if (pc.getSkill().hasSkillEffect(L1SkillId.BUFF_JUGUN)) {
				pc.getSkill().removeSkillEffect(L1SkillId.BUFF_JUGUN);
				pc.sendPackets(S_PacketBox.JUGUN_BUFF_OFF);
			}
		}
	}
	
	public int endtime(int castleid) {
		int time = (int) ((_war_end_time[castleid - 1] - System.currentTimeMillis()) / 1000);
		return time;
	}

	private Calendar castle_Calendar_save(long time) {
		Calendar cal = (Calendar) Calendar.getInstance().clone();
		Date date = new Date();
		date.setTime(time);
		cal.setTime(date);
		return cal;
	}

	private void securityStart(L1Castle castle) {
		int castleId = castle.getId();
		int a = 0, b = 0, c = 0, d = 0, e = 0;
		int[] loc = new int[3];
		L1Clan clan = null;
		switch (castleId) {
		case 1:
		case 2:
		case 3:
		case 4:
			a = 52;
			b = 248;
			c = 249;
			d = 250;
			e = 251;
			break;
		case 5:
		case 6:
		case 7:
		default:break;
		}
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if ((pc.getMapId() == a || pc.getMapId() == b || pc.getMapId() == c || pc.getMapId() == d || pc.getMapId() == e)
					&& !pc.isGm()) {
				clan = pc.getClan();
				if (clan != null && clan.getCastleId() == castleId) {
					continue;
				}
				loc = L1CastleLocation.getGetBackLoc(castleId);
				pc.getTeleport().start(loc[0], loc[1], (short) loc[2], 5, true);
			}
		}
		castle.setCastleSecurity(0);
		CastleTable.getInstance().updateCastle(castle);
		CharacterTable.getInstance().updateLoc(castleId, a, b, c, d, e);
	}
	
	private static List<L1ItemInstance> itemList = null;
	private List<L1ItemInstance> getWarWinItemList(){
		if (itemList == null || itemList.isEmpty()) {
			itemList = new ArrayList<L1ItemInstance>();
			presentlist();
		}
		return itemList;
	}
	
	private void presentlist() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1ItemInstance item = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM castle_present");		
			rs = pstm.executeQuery();
			while(rs.next()){
				item = new L1ItemInstance();
				item.setItemId(rs.getInt("itemid"));
				item.setCount(rs.getInt("count"));
				itemList.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private static final S_ServerMessage WOLRD_WAR_END_10	= new S_ServerMessage(5209, "10");// 강력한 힘에 의해 시공의 균열이 닫히려 합니다. {0}분 남음
	private static final S_ServerMessage WOLRD_WAR_END_1	= new S_ServerMessage(5209, "1");// 강력한 힘에 의해 시공의 균열이 닫히려 합니다. {0}분 남음
	private static final S_ServerMessage WOLRD_WAR_END_30	= new S_ServerMessage(5208, "30");// 시공의 균열이 닫히고 있습니다. {0}초 남음
	
	private class WorldWarEnd implements Runnable {
		L1CastleType _castle;
		public WorldWarEnd(L1CastleType castle){
			_castle = castle;
		}

		@Override
		public void run() {
			try {
				L1World world = L1World.getInstance();
				Thread.sleep(1200 * 1000);
				for (L1PcInstance pc : world.getAllPlayers()) {
					if ((_castle == L1CastleType.ORC && (pc.getMapId() == 15483 || pc.getMapId() == 15493)) 
							|| (_castle == L1CastleType.GIRAN && (pc.getMapId() == 15482 || pc.getMapId() == 15492))
							|| (_castle == L1CastleType.HEINE && (pc.getMapId() == 15484 || pc.getMapId() == 15494))) {
						pc.sendPackets(WOLRD_WAR_END_10);// 강력한 힘에 의해 시공의 균열이 닫히려 합니다. {0}분 남음
					}
				}
				Thread.sleep(540 * 1000);
				for (L1PcInstance pc : world.getAllPlayers()) {
					if ((_castle == L1CastleType.ORC && (pc.getMapId() == 15483 || pc.getMapId() == 15493))
							|| (_castle == L1CastleType.GIRAN && (pc.getMapId() == 15482 || pc.getMapId() == 15492))
							|| (_castle == L1CastleType.HEINE && (pc.getMapId() == 15484 || pc.getMapId() == 15494))) {
						pc.sendPackets(WOLRD_WAR_END_1);// 강력한 힘에 의해 시공의 균열이 닫히려 합니다. {0}분 남음
					}
				}
				Thread.sleep(30 * 1000);
				for (L1PcInstance pc : world.getAllPlayers()) {
					if ((_castle == L1CastleType.ORC && (pc.getMapId() == 15483 || pc.getMapId() == 15493))
							|| (_castle == L1CastleType.GIRAN && (pc.getMapId() == 15482 || pc.getMapId() == 15492))
							|| (_castle == L1CastleType.HEINE && (pc.getMapId() == 15484 || pc.getMapId() == 15494))) {
						pc.sendPackets(WOLRD_WAR_END_30);// 시공의 균열이 닫히고 있습니다. {0}초 남음
					}
				}
				Thread.sleep(30 * 1000);
				if (_castle == L1CastleType.ORC && _isWolrdWarOrc) {
					_isWolrdWarOrc = false;
				} else if (_castle == L1CastleType.GIRAN && _isWolrdWarGiran) {
					_isWolrdWarGiran = false;
				} else if (_castle == L1CastleType.HEINE && _isWolrdWarHeine) {
					_isWolrdWarHeine = false;
				}
				for (L1PcInstance pc : world.getAllPlayers()) {
					if ((_castle == L1CastleType.ORC && (pc.getMapId() == 15483 || pc.getMapId() == 15493))
							|| (_castle == L1CastleType.GIRAN && (pc.getMapId() == 15482 || pc.getMapId() == 15492))
							|| (_castle == L1CastleType.HEINE && (pc.getMapId() == 15484 || pc.getMapId() == 15494))) {
						int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_ADEN);
						pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true, true);
					}
				}
				if (_castle == L1CastleType.ORC || _castle == L1CastleType.GIRAN) {
					for (L1MerchantInstance npc : world.getAllMerchant()) {
						if (npc == null) {
							continue;
						}
						if ((_castle == L1CastleType.GIRAN && npc.getNpcId() == 800702) 
								|| (_castle == L1CastleType.ORC && npc.getNpcId() == 800703)) {
							npc.deleteMe();// 균열닫힘
							break;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}


