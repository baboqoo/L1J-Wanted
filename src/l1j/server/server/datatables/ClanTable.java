package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.common.data.ePLEDGE_JOIN_REQ_TYPE;
import l1j.server.server.IdFactory;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.warehouse.ClanWarehouse;
import l1j.server.server.model.warehouse.WarehouseManager;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.response.world.BloodPledgeDAO;

public class ClanTable {
	private static Logger _log = Logger.getLogger(ClanTable.class.getName());

	private static ClanTable _instance;

	private final HashMap<Integer, L1Clan> _clans		= new HashMap<Integer, L1Clan>();
	private final HashMap<Integer, L1Clan> _clancastle	= new HashMap<Integer, L1Clan>();

	public static ClanTable getInstance() {
		if (_instance == null) {
			_instance = new ClanTable();
		}
		return _instance;
	}

	private ClanTable() {
		{
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT * FROM clan_data ORDER BY clan_id");
				rs = pstm.executeQuery();
				L1Clan clan = null;
				while(rs.next()){
					clan			= new L1Clan();
					int clan_id		= rs.getInt("clan_id");
					int castle_id	= rs.getInt("hascastle");
					clan.setClanId(clan_id);
					clan.setClanName(rs.getString("clan_name"));
					clan.setLeaderId(rs.getInt("leader_id"));
					clan.setLeaderName(rs.getString("leader_name"));
					clan.setCastleId(castle_id);
					clan.setHouseId(rs.getInt("hashouse"));
					
					String alianceText = rs.getString("alliance");
					clan.setAlliance(new FastMap<Integer, Integer>(1));
					if (!StringUtil.isNullOrEmpty(alianceText)) {
						String[] alianceArray = alianceText.split(StringUtil.CommaString);
						if (alianceArray != null && alianceArray.length == 2) {
							clan.getAlliance().put(Integer.parseInt(alianceArray[0]), Integer.parseInt(alianceArray[1]));
						}
					}
					
					clan.setClanBirthDay(rs.getTimestamp("clan_birthday"));
					clan.setBot(Boolean.parseBoolean(rs.getString("bot")));
					clan.setBotStyle(rs.getInt("bot_style"));
					clan.setBotLevel(rs.getInt("bot_level"));
					clan.setOnlineMaxUser(rs.getInt("max_online_user"));
					clan.setAnnouncement(rs.getString("announcement"));
					clan.setIntroductionMessage(rs.getString("introductionMessage"));
					clan.setEnterNotice(rs.getString("enter_notice"));
					clan.setEmblemId(rs.getInt("emblem_id"));
					clan.setEmblemStatus(rs.getInt("emblem_status"));
					clan.setContribution(rs.getInt("contribution"));
				    clan.setBless(rs.getInt("bless"));
    				clan.setBlessCount(rs.getInt("bless_count"));
    				clan.setBuffTime(rs.getInt("attack"), rs.getInt("defence"), rs.getInt("pvpattack"), rs.getInt("pvpdefence"));
					clan.setEnableJoin(Boolean.parseBoolean(rs.getString("enable_join")));
					clan.setJoinType(ePLEDGE_JOIN_REQ_TYPE.fromInt(rs.getInt("join_type")));
					clan.setJoinPassword(rs.getString("join_password"));
					clan.setEinhasadBlessBuff(rs.getInt("EinhasadBlessBuff"));
					clan.setBuffFirst(rs.getInt("Buff_List1"));
					clan.setBuffSecond(rs.getInt("Buff_List2"));
					clan.setBuffThird(rs.getInt("Buff_List3"));
					clan.setClanDayDungeonTime(rs.getTimestamp("dayDungeonTime"));
					clan.setClanWeekDungeonTime(rs.getTimestamp("weekDungeonTime"));
					clan.setClanVowPotionTime(rs.getTimestamp("vowTime"));
					clan.setClanVowPotionCount(rs.getInt("vowCount"));
					clan.setclanNameChange(rs.getBoolean("clanNameChange"));
					
					String storeAllows = rs.getString("storeAllows");
					if (!StringUtil.isNullOrEmpty(storeAllows)) {
						StringTokenizer st = new StringTokenizer(storeAllows, StringUtil.LineString);
						while (st.hasMoreTokens()) {
							clan.add_store_allow_list(st.nextToken().trim());
						}
					}
					
					clan.set_limit_level(rs.getInt("limit_level"));
					
					String limitUserNames = rs.getString("limit_user_names");
					if (!StringUtil.isNullOrEmpty(limitUserNames)) {
						StringTokenizer st = new StringTokenizer(limitUserNames, StringUtil.LineString);
						while (st.hasMoreTokens()) {
							clan.add_limit_user_names(st.nextToken().trim());
						}
					}
    				
					L1World.getInstance().storeClan(clan);
					_clans.put(clan_id, clan);
					if (castle_id > 0) {
						_clancastle.put(castle_id, clan);
					}
				}
			} catch (SQLException e) {
				_log.log(Level.SEVERE, "ClanTable[]Error", e);
			} finally {
				SQLUtil.close(rs, pstm, con);
			}
		}

		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT char_name, ClanRank, level, notes, objid, Type, ClanContribution, ClanWeekContribution, pledgeJoinDate, lastLogoutTime FROM characters WHERE ClanID = ?");
				pstm.setInt(1, clan.getClanId());
				rs = pstm.executeQuery();
				while(rs.next()){
					String name					= rs.getString("char_name");
					eBloodPledgeRankType rank	= eBloodPledgeRankType.fromInt(rs.getInt("ClanRank"));
					int level					= rs.getInt("level");
					String notes				= rs.getString("notes");
					int memberId				= rs.getInt("objid");
					int type					= rs.getInt("Type");
					int contribution			= rs.getInt("ClanContribution");
					int contributionWeek		= rs.getInt("ClanWeekContribution");
					int join_date				= rs.getInt("pledgeJoinDate");
					Timestamp logout_date		= rs.getTimestamp("lastLogoutTime");
					clan.addClanMember(name, rank, level, notes, memberId, type, 0, 
							contribution, contributionWeek, 
							join_date, logout_date == null ? 0 : (int)(logout_date.getTime() / 1000),
							null);
				}
			} catch (SQLException e) {
				_log.log(Level.SEVERE, "ClanTable[]Error1", e);
			} finally {
				SQLUtil.close(rs, pstm, con);
			}
			
			clan.setTeamId(L1Clan.INTER_TEAM_IDS[CommonUtil.random(L1Clan.INTER_TEAM_IDS.length - 1)]);
		}

		ClanWarehouse clanWarehouse;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
			clanWarehouse.loadItems();
			ClanJoinningTable.getInstance().load(clan);
		}
	}

	public L1Clan createClan(L1PcInstance player, String clan_name, ePLEDGE_JOIN_REQ_TYPE joinType, String password, String explan) {
		for (L1Clan oldClans : L1World.getInstance().getAllClans()) {
			if (oldClans.getClanName().equalsIgnoreCase(clan_name)) {
				return null;
			}
		}
		long current_time = System.currentTimeMillis();
		
		L1Clan clan = new L1Clan();
		clan.setClanId(IdFactory.getInstance().nextId());
		clan.setClanName(clan_name);
		clan.setLeaderId(player.getId());
		clan.setLeaderName(player.getName());
		clan.setCastleId(0);
		clan.setHouseId(0);
		clan.setAlliance(new FastMap<Integer, Integer>(1));
		clan.setIntroductionMessage(explan);
		clan.setEnterNotice(StringUtil.EmptyString);
		clan.setClanBirthDay(new Timestamp(current_time));
		clan.setAnnouncement(StringUtil.EmptyString);
		clan.setEmblemId(0);
		clan.setEmblemStatus(0);
		clan.setBless(0);
		clan.setBlessCount(0);
		clan.setBuffTime(0, 0, 0, 0);
		clan.setEnableJoin(true);
		clan.setJoinType(joinType);
		clan.setJoinPassword(password);
		clan.set_limit_level(30);
		
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO clan_data SET clan_id=?, clan_name=?, leader_id=?, leader_name=?, "
					+ "hascastle=?, hashouse=?, alliance=?, clan_birthday=?, max_online_user=?, announcement=?, introductionMessage=?, enter_notice=?, "
					+ "emblem_id=?, emblem_status=?, contribution=?, bless=?, bless_count=?, attack=?, defence=?,"
					+ "pvpattack=?, pvpdefence=?, enable_join=?, join_type=?, join_password=?");
			int index = 0;
			pstm.setInt(++index, clan.getClanId());
			pstm.setString(++index, clan.getClanName());
			pstm.setInt(++index, clan.getLeaderId());
			pstm.setString(++index, clan.getLeaderName());
			pstm.setInt(++index, clan.getCastleId());
			pstm.setInt(++index, clan.getHouseId());
			pstm.setString(++index, StringUtil.EmptyString);
			pstm.setTimestamp(++index, clan.getClanBirthDay());
			pstm.setInt(++index, clan.getOnlineMaxUser());
			pstm.setString(++index, StringUtil.EmptyString);
			pstm.setString(++index, clan.getIntroductionMessage());
			pstm.setString(++index, clan.getEnterNotice());
			pstm.setInt(++index, 0);
			pstm.setInt(++index, 0);
			pstm.setInt(++index, 0);
			
			pstm.setInt(++index, 0);
			pstm.setInt(++index, 0);
			pstm.setInt(++index, 0);
			pstm.setInt(++index, 0);
			pstm.setInt(++index, 0);
			pstm.setInt(++index, 0);
			pstm.setString(++index, String.valueOf(clan.isEnableJoin()));
			pstm.setInt(++index, clan.getJoinType().toInt());
			pstm.setString(++index, clan.getJoinPassword());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "ClanTable[]Error2", e);
		} finally {
			SQLUtil.close(pstm, con);
		}

		L1World.getInstance().storeClan(clan);
		_clans.put(clan.getClanId(), clan);

		player.setClanid(clan.getClanId());
		player.setClanName(clan.getClanName());
		player.setPledgeJoinDate((int)(current_time / 1000));
		if (Config.WEB.WEB_SERVER_ENABLE) {
			BloodPledgeDAO.reload();
		}

		player.setBloodPledgeRank(eBloodPledgeRankType.RANK_NORMAL_KING);
		clan.addClanMember(player.getName(), player.getBloodPledgeRank(), player.getLevel(), StringUtil.EmptyString, player.getId(), player.getType(), player.getOnlineStatus(), 
				player.getClanContribution(), player.getClanWeekContribution(), 
				player.getPledgeJoinDate(), player.getLastLogoutTime() == null ? 0 : (int) (player.getLastLogoutTime().getTime() / 1000),
				player);
		clan.setTeamId(L1Clan.INTER_TEAM_IDS[CommonUtil.random(L1Clan.INTER_TEAM_IDS.length - 1)]);
		try {
			player.save();
		} catch (Exception e) {
			_log.log(Level.SEVERE, "ClanTable[]Error3", e);
		}
		return clan;
	}

	public boolean updateClan(L1Clan clan) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_data SET clan_id=?, leader_id=?, leader_name=?, hascastle=?, hashouse=?, "
							+ "alliance=?, clan_birthday=?, bot_style=?, bot_level=?, max_online_user=?, announcement=?, introductionMessage=?, enter_notice=?, "
							+ "emblem_id=?, emblem_status=?, contribution=?, enable_join=?, join_type=?, total_m=?, current_m=?, War_point=?,"
							+ "EinhasadBlessBuff=?, Buff_List1=?, Buff_List2=?, Buff_List3=?, dayDungeonTime=?, weekDungeonTime=?, limit_level=? WHERE clan_name=?");
			int index = 0;
			pstm.setInt(++index, clan.getClanId());
			pstm.setInt(++index, clan.getLeaderId());
			pstm.setString(++index, clan.getLeaderName());
			pstm.setInt(++index, clan.getCastleId());
			pstm.setInt(++index, clan.getHouseId());
			
			StringBuilder sb = new StringBuilder();
			if (clan.getAlliance() != null && !clan.getAlliance().isEmpty()) {
				for (java.util.Map.Entry<Integer, Integer> entry : clan.getAlliance().entrySet()) {
					sb.append(entry.getKey()).append(StringUtil.CommaString).append(entry.getValue());// 혈맹아이디,동맹타입
				}
			}
			pstm.setString(++index, sb.toString());
			
			pstm.setTimestamp(++index, clan.getClanBirthDay());
			pstm.setInt(++index, clan.getBotStyle());
			pstm.setInt(++index, clan.getBotLevel());
			pstm.setInt(++index, clan.getOnlineMaxUser());
			pstm.setString(++index, clan.getAnnouncement());
			pstm.setString(++index, clan.getIntroductionMessage());
			pstm.setString(++index, clan.getEnterNotice());
			pstm.setInt(++index, clan.getEmblemId());
			pstm.setInt(++index, clan.getEmblemStatus());
			pstm.setInt(++index, clan.getContribution());
			pstm.setString(++index, String.valueOf(clan.isEnableJoin()));
			pstm.setInt(++index, clan.getJoinType().toInt());
			
			pstm.setInt(++index, clan.getClanMemberList().size());
			pstm.setInt(++index, clan.getOnlineMaxUser());
			pstm.setInt(++index, 0);
			
			pstm.setInt(++index, clan.getEinhasadBlessBuff());
			pstm.setInt(++index, clan.getBuffFirst());
			pstm.setInt(++index, clan.getBuffSecond());
			pstm.setInt(++index, clan.getBuffThird());
			
			pstm.setTimestamp(++index, clan.getClanDayDungeonTime());
			pstm.setTimestamp(++index, clan.getClanWeekDungeonTime());
			pstm.setInt(++index, clan.get_limit_level());
			
			pstm.setString(++index, clan.getClanName());
			if (pstm.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "ClanTable[]Error4", e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}

	/**
	 ** 혈맹자동가입*
	 * 
	 * @param player
	 * @param clan_name
	 * @param style
	 * @return
	 */
	public void createClanBot(L1PcInstance player, String clan_name, int style) {
		for (L1Clan oldClans : L1World.getInstance().getAllClans()) {
			if (oldClans.getClanName().equalsIgnoreCase(clan_name)) {
				return;
			}
		}

		L1Clan clan = new L1Clan();
		clan.setClanId(IdFactory.getInstance().nextId());
		clan.setClanName(clan_name);
		clan.setLeaderId(player.getId());
		clan.setLeaderName(player.getName());
		clan.setCastleId(0);
		clan.setHouseId(0);
		clan.setBot(true);
		clan.setBotStyle(style);
		clan.set_limit_level(30);

		player.setClanid(clan.getClanId());
		player.setClanName(clan.getClanName());

		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO clan_data SET clan_id=?, clan_name=?, leader_id=?, leader_name=?, hascastle=?, hashouse=?, bot=?, bot_style=?");
			int index = 0;
			pstm.setInt(++index, clan.getClanId());
			pstm.setString(++index, clan.getClanName());
			pstm.setInt(++index, clan.getLeaderId());
			pstm.setString(++index, clan.getLeaderName());
			pstm.setInt(++index, clan.getCastleId());
			pstm.setInt(++index, clan.getHouseId());
			pstm.setString(++index, StringUtil.TrueString);
			pstm.setInt(++index, style);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "ClanTable[]Error5", e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		L1World.getInstance().storeClan(clan);
		_clans.put(clan.getClanId(), clan);
	}

	/**
	 * 혈맹 해산
	 * @param clan
	 */
	public void deleteClan(L1Clan clan) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_data WHERE clan_name=?");
			pstm.setString(1, clan.getClanName());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "ClanTable[]Error6", e);
		} finally {
			SQLUtil.close(pstm, con);
		}

		ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
		clanWarehouse.clearItems();
		clanWarehouse.deleteAllItems();
		
		ClanAttentionTable.getInstance().deleteClanAttention(clan.getClanName());
		ClanJoinningTable.getInstance().delete(clan);

		L1World.getInstance().removeClan(clan);
		_clans.remove(clan.getClanId());
		if (Config.WEB.WEB_SERVER_ENABLE) {
			BloodPledgeDAO.reload();
		}
		clan.dispose();
	}

	public L1Clan getTemplate(int clan_id) {
		return _clans.get(clan_id);
	}

	public static void reload() {
		ClanTable oldInstance = _instance;
		_instance = new ClanTable();
		if (oldInstance != null) {
			oldInstance._clans.clear();
			oldInstance._clancastle.clear();
		}
	}

	public L1Clan find(String clan_name) {
		for (L1Clan clan : _clans.values()) {
			if (clan.getClanName().equalsIgnoreCase(clan_name)) {
				return clan;
			}
		}
		return null;
	}

	public HashMap<Integer, L1Clan> getClanCastles() {
		return _clancastle;
	}
	
	/**
	 * 혈맹의 축복 버프 업데이트
	 * @param clanid
	 * @param count
	 */
	public void updateBlessCount(int clanid, int count) {
    	Connection con = null;
    	PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_data SET bless_count=? WHERE clan_id=?");
			pstm.setInt(1, count);
			pstm.setInt(2, clanid);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	/**
	 * 혈맹의 축복 버프 업데이트
	 * @param clanid
	 * @param bless
	 */
	public void updateBless(int clanid, int bless) {
		Connection con = null;
    	PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection(); 
			pstm = con.prepareStatement("UPDATE clan_data SET bless=? WHERE clan_id=?");
			pstm.setInt(1, bless);
			pstm.setInt(2, clanid);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	public void updateBuffTime(int a, int b, int c, int d, int clanid) {
		Connection con = null;
    	PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_data SET attack=?,defence=?,pvpattack=?,pvpdefence=? WHERE clan_id=?");
			pstm.setInt(1, a);
			pstm.setInt(2, b);
			pstm.setInt(3, c);
			pstm.setInt(4, d);
			pstm.setInt(5, clanid);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public void updateJoinPassword(L1Clan clan) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE clan_data SET join_password=? WHERE clan_id=?");
			pstm.setString(1, clan.getJoinPassword());
			pstm.setInt(2, clan.getClanId());
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public void updateContribution(L1Clan pledge){
		Connection con			=	null;
		PreparedStatement pstm	=	null;
		try {
			con		=	L1DatabaseFactory.getInstance().getConnection();
			pstm	=	con.prepareStatement("UPDATE clan_data SET contribution=? WHERE clan_id=?");
			pstm.setInt(1, pledge.getContribution());
			pstm.setInt(2, pledge.getClanId());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public void updateContribution(Collection<L1Clan> pledge_list){
		if (pledge_list == null || pledge_list.isEmpty()) {
			return;
		}
		java.util.LinkedList<L1Clan> list = null;
		for (L1Clan pledge : pledge_list) {// 저장할 혈맹을 간추린다
			if (pledge == null || pledge.isBot() || pledge.getOnlineClanMember().length <= 0) {
				continue;
			}
			if (list == null) {
				list = new java.util.LinkedList<L1Clan>();
			}
			list.add(pledge);
		}
		if (list == null || list.isEmpty()) {
			return;
		}
		Connection con			=	null;
		PreparedStatement pstm	=	null;
		try {
			con	=	L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			pstm = con.prepareStatement("UPDATE clan_data SET contribution=? WHERE clan_id=?");
			for (L1Clan pledge : list) {
				pstm.setInt(1, pledge.getContribution());
				pstm.setInt(2, pledge.getClanId());
				pstm.addBatch();// 메모리상에 올려둔다
				pstm.clearParameters();// 파라미터 재사용
			}
			pstm.executeBatch();// 메모리에 올라온 sql을 한번에 처리
			pstm.clearBatch();// 메모리 초기화
			con.commit();// 저장
		} catch (SQLException e) {
			try {
				con.rollback();// 시점 되돌림
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
			if (pledge_list != null) {
				pledge_list = null;
			}
			if (list != null) {
				list.clear();
				list = null;
			}
		}
	}
	
	public void updateClanDungeonTime(String sql){
		Connection con			=	null;
		PreparedStatement pstm	=	null;
		try {
			con		=	L1DatabaseFactory.getInstance().getConnection();
			pstm	=	con.prepareStatement(sql);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 혈맹 공지사항 변경
	 * @param clan
	 * @return
	 */
	public boolean updateIntroductionMessage(L1Clan clan) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_data SET introductionMessage=? WHERE clan_id=?");
			pstm.setString(1, clan.getIntroductionMessage());
			pstm.setInt(2, clan.getClanId());
			if (pstm.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	public void updateClanVowCount(int count, int clanid){
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_data SET vowCount=? WHERE clan_id=?");
			pstm.setInt(1, count);
			pstm.setInt(2, clanid);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public void updateClanName(boolean result, String clanName){
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_data SET clanNameChange=? WHERE clan_name=?");
			pstm.setBoolean(1, result);
			pstm.setString(2, clanName);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public void changeClanName(String oldClanName, String newClanName){
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_data SET clan_name=?, clanNameChange=? WHERE clan_name=?");
			pstm.setString(1, newClanName);
			pstm.setBoolean(2, false);
			pstm.setString(3, oldClanName);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 혈맹 알림 변경
	 * @param pledge
	 * @return boolean
	 */
	public boolean updateEnterNotice(L1Clan pledge) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_data SET enter_notice=? WHERE clan_id=?");
			pstm.setString(1, pledge.getEnterNotice());
			pstm.setInt(2, pledge.getClanId());
			if (pstm.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	/**
	 * 창고 사용 목록 업데이트
	 * @param pledge
	 */
	public void updateStoreAllow(L1Clan pledge) {
		String store_allows = null;
		LinkedList<String> allow_list = pledge.get_store_allow_list();
		if (allow_list != null && !allow_list.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (String allow_name : allow_list) {
				if (sb.length() > 0) {
					sb.append(StringUtil.LineString);
				}
				sb.append(allow_name);
			}
			store_allows = sb.toString();
		}
		
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_data SET storeAllows=? WHERE clan_id=?");
			pstm.setString(1, store_allows);
			pstm.setInt(2, pledge.getClanId());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 혈맹 가입 제한 레벨 변경
	 * @param pledge
	 * @return boolean
	 */
	public boolean updateLimitLevel(L1Clan pledge) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_data SET limit_level=? WHERE clan_id=?");
			pstm.setInt(1, pledge.get_limit_level());
			pstm.setInt(2, pledge.getClanId());
			if (pstm.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	/**
	 * 혈맹 가입 차단 이름 변경
	 * @param pledge
	 */
	public void updateLimitUserNames(L1Clan pledge) {
		String limit_user_names = null;
		LinkedList<String> limit_user_name_list = pledge.get_limit_user_names();
		if (limit_user_name_list != null && !limit_user_name_list.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (String limit_name : limit_user_name_list) {
				if (sb.length() > 0) {
					sb.append(StringUtil.LineString);
				}
				sb.append(limit_name);
			}
			limit_user_names = sb.toString();
		}
		
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_data SET limit_user_names=? WHERE clan_id=?");
			pstm.setString(1, limit_user_names);
			pstm.setInt(2, pledge.getClanId());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 혈맹 군주 변경
	 * @param pledge
	 * @return boolean
	 */
	public boolean updateMasterChange(L1Clan pledge) {
		String allows = null;
		LinkedList<String> allow_list = pledge.get_store_allow_list();
		if (allow_list != null && !allow_list.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (String allow_name : allow_list) {
				if (sb.length() > 0) {
					sb.append(StringUtil.LineString);
				}
				sb.append(allow_name);
			}
			allows = sb.toString();
		}
		
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_data SET leader_id=?, leader_name=?, storeAllows=? WHERE clan_id=?");
			pstm.setInt(1, pledge.getLeaderId());
			pstm.setString(2, pledge.getLeaderName());
			pstm.setString(3, allows);
			pstm.setInt(4, pledge.getClanId());
			if (pstm.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}

}

