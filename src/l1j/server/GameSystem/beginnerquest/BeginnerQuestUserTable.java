package l1j.server.GameSystem.beginnerquest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.beginnerquest.bean.L1QuestProgress;
import l1j.server.common.bin.QuestCommonBinLoader;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 초급 퀘스트 유저 정보 테이블 로더
 * @author LinOffice
 */
public class BeginnerQuestUserTable {
	private static BeginnerQuestUserTable _instance;
	public static BeginnerQuestUserTable getInstance() {
		if (_instance == null) {
			_instance = new BeginnerQuestUserTable();
		}
		return _instance;
	}

	private ConcurrentHashMap<Integer, QuestTemp> QuestList = new ConcurrentHashMap<Integer, QuestTemp>();

	/**
	 * 생성자
	 */
	private BeginnerQuestUserTable() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM character_beginner_quest");
			rs		= pstm.executeQuery();
			QuestTemp temp = null;
			while (rs.next()) {
				temp = new QuestTemp();
				temp.charId	= rs.getInt("charId");
				temp.info	= rs.getString("info");
				QuestList.put(temp.charId, temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	/**
	 * 로그인시 퀘스트 진행 상태를 케릭터에 업로드한다.
	 * 
	 * @param pc
	 */
	public void load_progress(L1PcInstance pc) {
		QuestTemp quest = QuestList.get(pc.getId());
		if (quest != null) {
			StringTokenizer s	= new StringTokenizer(quest.info, StringUtil.LineString);
			String temp			= null;
			L1QuestProgress info	= null;
			while (s.hasMoreElements()) {// 행 기준
				temp = s.nextToken();
				StringTokenizer mdata = new StringTokenizer(temp, StringUtil.CommaString);
				info			= new L1QuestProgress();
				while (mdata.hasMoreElements()) {
					String detail = mdata.nextToken().trim();
					if (detail.startsWith("QUEST_ID: ")) {
						int questId = Integer.parseInt(detail.replace("QUEST_ID: ", StringUtil.EmptyString).trim());
						info.setQuestId(questId);
					} else if (detail.startsWith("START_TIME: ")) {
						long startTime = Long.parseLong(detail.replace("START_TIME: ", StringUtil.EmptyString).trim());
						info.setStartTime(startTime);
					} else if (detail.startsWith("FINISH_TIME: ")) {
						long endTime = Long.parseLong(detail.replace("FINISH_TIME: ", StringUtil.EmptyString).trim());
						info.setFinishTime(endTime);
					} else if (detail.startsWith("OBJECTIVES: ")) {
						String progress = detail.replace("OBJECTIVES: ", StringUtil.EmptyString);
						String[] progressArray = progress.split(StringUtil.SlushString);
						for (int i=0; i<progressArray.length; i++) {
							String[] array = progressArray[i].split(StringUtil.ColonString);
							if (array == null || array.length != 2) {
								continue;
							}
							info.setQuantity(Integer.parseInt(array[0].trim()), Integer.parseInt(array[1].trim()));
						}
					}
				}
				info.setBin(QuestCommonBinLoader.getQuest(info.getQuestId()));
				pc.getQuest().putQuestProgress(info.getQuestId(), info);
			}
			return;
		}
		
		// TODO 퀘스트 생성
		quest = new QuestTemp();
		quest.charId = pc.getId();
		quest.info = StringUtil.EmptyString;
		regist_progress(quest);
	}

	/**
	 * 정보 업데이트
	 * 
	 * @param pc
	 */
	public void update_progress(L1PcInstance pc) {
		synchronized (pc.getQuest().syncQuest) {
			QuestTemp temp = QuestList.get(pc.getId());
			if (temp == null) {
				return;
			}
			StringBuffer sb = new StringBuffer();
			for (L1QuestProgress info : pc.getQuest().getQuestProgressList().values()) {
				if (sb.length() > 0) {
					sb.append(StringUtil.LineString);
				}
				sb.append("QUEST_ID: ").append(Integer.toString(info.getQuestId()));
				sb.append(", START_TIME: ").append(Long.toString(info.getStartTime()));
				sb.append(", FINISH_TIME: ").append(Long.toString(info.getFinishTime()));
				if (info.getObjectives() != null) {
					sb.append(", OBJECTIVES: ");
					int cnt = 0;
					for (Map.Entry<Integer, Integer> entry : info.getObjectives().entrySet()) {
						if (cnt > 0) {
							sb.append(StringUtil.SlushString);
						}
						sb.append(entry.getKey()).append(StringUtil.ColonString).append(entry.getValue());
						cnt++;
					}
				}
			}
			temp.info = sb.toString();
			update(temp);
		}
	}

	public void update(QuestTemp temp) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE character_beginner_quest SET info=? WHERE charId=?");
			pstm.setString(1, temp.info);
			pstm.setInt(2, temp.charId);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	public void delete(int charId) {
		Connection con = null;
		PreparedStatement pstm = null;
		removeInfo(charId);
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_beginner_quest WHERE charId=?");
			pstm.setInt(1, charId);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	// 케릭터 삭제시 메모리영역 제거
	public void removeInfo(int charId){
		QuestList.remove(charId);
	}

	/**
	 * 서버 종료시 전체 정보 업데이트
	 */
	public void updateAll() {
		if (QuestList == null || QuestList.isEmpty()) {
			return;
		}
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			
			pstm = con.prepareStatement("UPDATE character_beginner_quest SET info=? WHERE charId=?");
			for (QuestTemp temp : QuestList.values()) {
				pstm.setString(1, temp.info);
				pstm.setInt(2, temp.charId);
				pstm.addBatch();
				pstm.clearParameters();
			}
			
			pstm.executeBatch();
			pstm.clearBatch();
			con.commit();
		} catch(SQLException e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				sqle.printStackTrace();
			}
			e.printStackTrace();
		} catch(Exception e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				sqle.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			SQLUtil.close(pstm, con);
		}
	}

	/**
	 * 케릭 로그인시 데이터 베이스에 생성
	 * 
	 * @param mon
	 */
	void regist_progress(QuestTemp temp) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO character_beginner_quest SET charId=?, info=?");
			pstm.setInt(1, temp.charId);
			pstm.setString(2, temp.info);
			pstm.execute();
			QuestList.put(temp.charId, temp);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public static void reload() {
		BeginnerQuestUserTable oldInstance = _instance;
		_instance = new BeginnerQuestUserTable();
		oldInstance.QuestList.clear();
		oldInstance = null;
	}

	private class QuestTemp {
		public int charId;
		public String info;
	}
}
