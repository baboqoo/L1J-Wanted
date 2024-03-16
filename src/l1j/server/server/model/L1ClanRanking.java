package l1j.server.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class L1ClanRanking implements Runnable {
	
	private static final Logger _log = Logger.getLogger(L1ClanRanking.class.getName());

	private static L1ClanRanking _instance = new L1ClanRanking();

	public static L1ClanRanking getInstance() {
		return _instance;
	}

	private boolean _isStart;
	
	private int _currentTime;
	private int _clanRank = 0;
	
	private HashMap<Integer, RankData> _curankData = new HashMap<Integer, RankData>();
	private HashMap<String, Integer> _parankData = new HashMap<String, Integer>();	
		
	public void start() {
		if (!_isStart) {
			_isStart = true;
			GeneralThreadPool.getInstance().execute(this);
		}
	}

	@Override
	public void run() {
		try {		
			_parankData.putAll(new HashMap<String, Integer>());
			_curankData.putAll(new HashMap<Integer, RankData>());		

			while (_isStart) {
				if (isChangeTime()) {
					clanchange();	
					_currentTime = (int) (System.currentTimeMillis() / 1000);
				}
				Thread.sleep(1000 * 60);
			}
		} catch (Exception e) {
			_log.log(Level.WARNING, StringUtil.EmptyString, e);
		}
	}


	private boolean isChangeTime() {
		if (_currentTime == 0)
			return true;
		return RealTimeClock.getInstance().getRealTime().get(Calendar.MINUTE) == 0;
	}

	public void gmcommand() {
		try {
			clanchange();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void clanchange() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			clearAllDatas();
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT clan_id, clan_name, ranktime, rankdate FROM clan_data WHERE ranktime > 0 ORDER BY ranktime");
			rs = pstm.executeQuery();

			while (rs.next()) {
				int claniD = rs.getInt("clan_id");
				String clannamE =rs.getString("clan_name");
				int timE = rs.getInt("ranktime");
				Timestamp datA = rs.getTimestamp("rankdate");
				addRankerData(claniD, clannamE, timE, datA);
			}
		} catch (Exception e) {
			_log.log(Level.WARNING, StringUtil.EmptyString, e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private int getCurrentRank() {
		return _clanRank;
	}
	
	private int getNextRank() {
		return ++_clanRank;
	}
	
	private int getMaxRank() {
		return 200;
	}
	
	private int getOldRank(String name) {
		if (_parankData.containsKey(name))
			return _parankData.get(name);
		return 0;
	}


	private void addRankerData(int clanId, String name, int time, Timestamp data) {
		int currentRank = getCurrentRank();
		int maxRank = getMaxRank();
		if (currentRank < maxRank) {
			int oldRank = getOldRank(name);
			int nextRank = getNextRank();
			_curankData.put(nextRank, new RankData(nextRank, oldRank, clanId, name, time, data));
		}
	}
	

	public RankData[] getRankerDatas() {		
		return _curankData.values().toArray(new RankData[_curankData.values().size()]);		
	}


	private void clearAllDatas() {
		_parankData.clear();	
		_parankData.putAll(new HashMap<String, Integer>());
		for (RankData data : getRankerDatas()) {
			_parankData.put(data.getClanName(), data.getCurrentR());
		}	
		_curankData.clear();	
		_curankData.putAll(new HashMap<Integer, RankData>());
		_clanRank = 0;		
	}
	
	public RankData getClanRankerData(String name) {
		RankData[] datas = getRankerDatas();
		if (datas != null) {
			for (RankData data : datas) {
				if (data.getClanName().equals(name))
					return data;
			}
		}
		return null;
	}
	
	public class RankData {
		private int _currentR;
		private int _pastR;
		private int _clanid;
		private String _clanname;
		private int _comtime;
		private Timestamp _date;

		public int getCurrentR() {
			return _currentR;
		}

		public int getPastR() {
			return _pastR;
		}

		public int getClanid() {
			return _clanid;
		}

		public String getClanName() {
			return _clanname;
		}

		public int getComTime() {
			return _comtime;
		}

		public Timestamp getDate() {
			return _date;
		}
		
		public RankData(int currentRank, int pastRank, int clanId, String clanName, int tiMe, Timestamp daTa) {
			_currentR = currentRank;
			_pastR = pastRank == 0 ? currentRank : pastRank;
			_clanid = clanId;
			_clanname = clanName;
			_comtime = tiMe;
			_date = daTa;		
		}
	}
}
