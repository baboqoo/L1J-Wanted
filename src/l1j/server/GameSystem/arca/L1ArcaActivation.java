package l1j.server.GameSystem.arca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

/**
 * 캐릭터의 탐 상태
 * @author LinOffice
 */
public class L1ArcaActivation {
	private int charId;
	private Timestamp endTime;
	private Deque<L1ArcaRemain> remain;
	
	public L1ArcaActivation(int charId){
		this.charId			= charId;
		this.endTime		= null;
		this.remain			= new ConcurrentLinkedDeque<>();
	}
	
	public L1ArcaActivation(ResultSet rs) throws SQLException {
		this(rs.getInt("objid"), rs.getTimestamp("TamEndTime"));
	}
	
	public L1ArcaActivation(int charId, Timestamp endTime) {
		this.charId			= charId;
		this.endTime		= endTime;
		this.remain			= new ConcurrentLinkedDeque<>();
		loadRemain();
	}

	public int getCharId() {
		return charId;
	}

	public Timestamp getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Timestamp time){
		endTime = time;
	}

	public Deque<L1ArcaRemain> getRemain() {
		return remain;
	}
	
	public L1ArcaRemain getRemainPoll(){
		return remain.poll();
	}
	
	/**
	 * 예약한다.
	 * @param day
	 * @return boolean
	 */
	public boolean offerRemain(int day, int itemId){
		if (remain.size() >= 2) {// 캐릭터당 예약가능 최대 2개
			return false;
		}
		int id = remain.isEmpty() ? 1 : remain.getLast().getId() + 1;
		remain.offer(new L1ArcaRemain(id, charId, day, itemId));// 적재
		return true;
	}
	
	/**
	 * 종료시간 및 예약 정보를 제거
	 */
	public void dispose(){
		endTime = null;
		remain.clear();
	}
	
	/**
	 * 예약 중인 탐 정보를 로드한다.
	 */
	void loadRemain(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM character_arca WHERE charId=? ORDER BY id ASC");
			pstm.setInt(1, charId);
			rs		= pstm.executeQuery();
			while (rs.next()) {
				remain.offer(new L1ArcaRemain(rs));// 적재
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
}

