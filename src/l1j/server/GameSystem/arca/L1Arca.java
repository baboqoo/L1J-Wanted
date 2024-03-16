package l1j.server.GameSystem.arca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.Account;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 탐 관리 핸들러
 * 계정(Account)에 할당한다.
 * @author LinOffice
 */
public class L1Arca {
	private final Account owner;
	private int point;
	private ConcurrentHashMap<Integer, L1ArcaActivation> activations;// key: charId, value: L1ArcaActivation
	
	public L1Arca(Account owner, int point) {
		this.owner			= owner;
		this.point			= point;
		this.activations	= new ConcurrentHashMap<>();
		loadActivation();
	}

	public int getPoint() {
		return point;
	}
	
	public void addPoint(int value){
		point += value;
	}

	public ConcurrentHashMap<Integer, L1ArcaActivation> getActivations() {
		return activations;
	}
	
	public void createActivation(int charId){
		activations.put(charId, new L1ArcaActivation(charId));
	}
	
	public void putActivation(L1ArcaActivation val){
		activations.put(val.getCharId(), val);
	}
	
	public void removeActivation(int charId){
		L1ArcaActivation active = activations.remove(charId);
		if (active != null) {
			active.dispose();
		}
	}
	
	/**
	 * 가동중인 탐 시간을 조사한다.(최소값)
	 * @return time
	 */
	public long getActiveTime(){
		long time = 0L, beforeTime = 0L;
		boolean first			= false;
		L1ArcaActivation active	= null;
		long currentTime		= System.currentTimeMillis();
		for (int key : activations.keySet()) {
			active = activations.get(key);
			if (active == null || active.getEndTime() == null || active.getEndTime().getTime() <= currentTime) {
				continue;
			}
			if (!first) {
				first		= true;
				beforeTime	= active.getEndTime().getTime();
			}
			time = active.getEndTime().getTime();
			if (time > beforeTime) {
				time = beforeTime;
			}
		}
		return time - currentTime;
	}
	
	/**
	 * 가동 중인 탐 수량을 조사한다.
	 * 시간이 종료된후 예약이 있으면 교체한다.
	 * @param currentTime
	 * @return count
	 */
	public int getActiveCount(long currentTime){
		int cnt					= 0;
		L1ArcaActivation active	= null;
		for (int key : activations.keySet()) {
			active = activations.get(key);
			if (active == null || active.getEndTime() == null) {
				continue;
			}
			if (active.getEndTime().getTime() > currentTime) {// 시간이 남음
				cnt++;
			} else {
				// 예약 존재
				if (!active.getRemain().isEmpty()) {
					L1ArcaRemain remain = active.getRemainPoll();// 꺼낸다.
					active.getEndTime().setTime(currentTime + (86400000 * (long) remain.getDay()) + 10000);// 종료 시간 변경
					cnt++;
				}
			}
		}
		return cnt;
	}
	
	/**
	 * 로그아웃 시간 탐 수치 갱신
	 */
	public void lessTimeUpdate(){
		if (owner.getLastQuit() == null) {
			return;
		}
		long gameEndTime	= owner.getLastQuit().getTime();
		long currentTime	= System.currentTimeMillis();
		long dis			= currentTime - gameEndTime;
		int disValue		= (int) (dis / 720000);
		if (disValue < 1) {
			return;
		}
		int arcaValue		= Config.ALT.ARCA_REWARD_COUNT;
		
		L1ArcaActivation active = null;
		for (int key : activations.keySet()) {
			active = activations.get(key);
			if (active == null || active.getEndTime() == null) {
				continue;
			}
			
			// 시간이 남음
			if (active.getEndTime().getTime() > currentTime) {
				point += disValue * arcaValue;
			}
			// 시간이 작음
			else {
				// 게임 종료 시간보다 큼
				if (active.getEndTime().getTime() > gameEndTime) {
					long endDisTime = active.getEndTime().getTime() - gameEndTime;
					// 예약 존재
					if (!active.getRemain().isEmpty()) {
						long remainTime	= active.getRemain().getFirst().getDay() * 86400000;
						
						// 예약 시간이 종료 시간차이 보다 높음
						if (remainTime >= endDisTime) {
							point += disValue * arcaValue;
						}
						// 예약 시간이 중간에 종료 됨
						else {
							endDisTime += remainTime;// 예약 시간 추가
							point += (int)(endDisTime / 720000) * arcaValue;
							active.getRemainPoll();// 예약 제거
						}
					}
					// 예약 없음
					else {
						point += (int)(endDisTime / 720000) * arcaValue;
					}
				}
			}
		}
	}
	
	/**
	 * 탐 포인트를 업데이트 한다.
	 */
	public void updatePoint(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET Tam_Point=? WHERE login=?");
			pstm.setInt(1, point);
			pstm.setString(2, owner.getName());
			pstm.execute();
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 탐 정보를 데이터베이스에 갱신한다.
	 * logout
	 */
	public void merge(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			
			// 탐 포인트 업데이트
			pstm	= con.prepareStatement("UPDATE accounts SET Tam_Point=? WHERE login=?");
			pstm.setInt(1, point);
			pstm.setString(2, owner.getName());
			pstm.execute();
			
			if (activations != null && !activations.isEmpty()) {
				update_activations(con);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 탐 활성화 및 예약 정보 업데이트
	 * @param con
	 */
	void update_activations(Connection con){
		PreparedStatement pstm	= null;
		L1ArcaActivation active	= null;
		
		try {
			con.setAutoCommit(false);
			
			// 탐 종료 시간 업데이트
			pstm	= con.prepareStatement("UPDATE characters SET TamEndTime=? WHERE objid=?");
			int timeCnt = 0;
			for (int key : activations.keySet()) {
				active = activations.get(key);
				pstm.setTimestamp(1, active.getEndTime());
				pstm.setInt(2, active.getCharId());
				pstm.addBatch();
				pstm.clearParameters();
				timeCnt++;
			}
			if (timeCnt > 0) {
				pstm.executeBatch();
				pstm.clearBatch();
				con.commit();
			}
			SQLUtil.close(pstm);
			
			// 탐 예약 시간 삭제
			pstm	= con.prepareStatement("DELETE FROM character_arca WHERE charId=?");
			int deleteCnt = 0;
			for (int key : activations.keySet()) {
				active = activations.get(key);
				pstm.setInt(1, active.getCharId());
				pstm.addBatch();
				pstm.clearParameters();
				deleteCnt++;
			}
			if (deleteCnt > 0) {
				pstm.executeBatch();
				pstm.clearBatch();
				con.commit();
			}
			SQLUtil.close(pstm);
			
			// 탐 예약 시간 추가
			pstm	= con.prepareStatement("INSERT INTO character_arca (id, charId, day) VALUES (?,?,?)");
			int insertCnt = 0;
			for (int key : activations.keySet()) {
				active = activations.get(key);
				if (active.getRemain().isEmpty()) {
					continue;
				}
				for (L1ArcaRemain remain : active.getRemain()) {
					pstm.setInt(1, remain.getId());
					pstm.setInt(2, remain.getCharId());
					pstm.setInt(3, remain.getDay());
					pstm.addBatch();
					pstm.clearParameters();
					insertCnt++;
				}
			}
			if (insertCnt > 0) {
				pstm.executeBatch();
				pstm.clearBatch();
				con.commit();
			}
			
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
			SQLUtil.close(pstm);
		}
	}
	
	/**
	 * 계정의 캐릭터 탐 정보 로드
	 */
	void loadActivation(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		L1ArcaActivation obj	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT objid, TamEndTime FROM characters WHERE account_name=?");
			pstm.setString(1, owner.getName());
			rs		= pstm.executeQuery();
			while (rs.next()) {
				obj = new L1ArcaActivation(rs);
				activations.put(obj.getCharId(), obj);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("==========================\r\nSTART\r\n");
		sb.append("ACCOUNT: ").append(owner.getName()).append(StringUtil.LineString);
		sb.append("POINT: ").append(point).append(StringUtil.LineString);
		sb.append("ACTIVATE_SIZE: ").append(activations.size()).append(StringUtil.LineString);
		L1ArcaActivation active = null;
		for (int i=1; i<=activations.size(); i++) {
			sb.append("==========================\r\n");
			sb.append("\tACTIVATE_INDEX : ").append(i).append(StringUtil.LineString);
			active = activations.get(i);
			sb.append("\tACTIVATE_ID: ").append(active.getCharId()).append(StringUtil.LineString);
			sb.append("\tEND_TIME: ").append(active.getEndTime()).append(StringUtil.LineString);
			sb.append("\tREMAIN_SIZE: ").append(active.getRemain().size()).append(StringUtil.LineString);
			if (active.getRemain().size() > 0) {
				sb.append("--------------------------\r\n");
				for (L1ArcaRemain remain : active.getRemain()) {
					sb.append("\t\tREMAIN_DAY: ").append(remain.getDay()).append(StringUtil.LineString);
					sb.append("\t\tREMAIN_ITEM: ").append(remain.getUseItemId()).append(StringUtil.LineString);
				}
				sb.append("--------------------------\r\n");
			}
		}
		sb.append("END\r\n==========================");
		return sb.toString();
	}
}

