package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1Revenge;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.revenge.S_RevengeInfo;
import l1j.server.server.serverpackets.revenge.S_RevengeInfoNoti;
import l1j.server.server.templates.L1RevengeTemp;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 복수 시스템 정보
 * @author LinOffice
 */
public class RevengeTable {
	private static Logger _log = Logger.getLogger(RevengeTable.class.getName());
	private static class newInstance {
		public static final RevengeTable INSTANCE = new RevengeTable();
	}
	public static RevengeTable getInstance() {
		return newInstance.INSTANCE;
	}
	
	private static final FastMap<Integer, L1Revenge> DATA = new FastMap<Integer, L1Revenge>();
	
	public static L1Revenge getRevenge(int charObjId) {
		return DATA.get(charObjId);
	}
	
	private Object _monitor = new Object();
	private int _id;
	
	private RevengeTable(){
		load();
	}
	
	private void load(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			// 시간이 지난 목록 제거
			pstm = con.prepareStatement("DELETE FROM character_revenge WHERE endtime <= NOW()");
			pstm.executeUpdate();
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("SELECT DISTINCT(char_id) FROM character_revenge");
			rs = pstm.executeQuery();
			PreparedStatement revengePstm = null;
			ResultSet revengeRs = null;
			while(rs.next()){
				try {
					revengePstm = con.prepareStatement("SELECT * FROM character_revenge WHERE char_id=?");
					int charid = rs.getInt("char_id");
					revengePstm.setInt(1, charid);
					revengeRs = revengePstm.executeQuery();
					L1Revenge revenge = new L1Revenge(charid);
					while (revengeRs.next()) {
						L1RevengeTemp temp = new L1RevengeTemp(revengeRs);
						revenge.add(temp.getUserName(), temp);
						if (temp.getNumber() > _id) {
							_id = temp.getNumber();
						}
					}
					DATA.put(charid, revenge);
				} catch(Exception e){
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				} finally {
					SQLUtil.close(revengeRs, revengePstm);
				}
			}
		} catch(Exception e){
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	// 케릭터 logout때만 Database갱신
	public void store(L1PcInstance pc){
		if (DATA == null || !DATA.containsKey(pc.getId()) || DATA.get(pc.getId()).getRevenges() == null) {
			return;
		}
		FastTable<L1RevengeTemp> list	=	new FastTable<L1RevengeTemp>(DATA.get(pc.getId()).getRevenges().values());
		L1RevengeTemp obj				=	null;
		Connection con					=	null;
		PreparedStatement pstm			=	null;
		try {
			con 	=	L1DatabaseFactory.getInstance().getConnection();
			pstm	=	con.prepareStatement("DELETE FROM character_revenge WHERE char_id=?");
			pstm.setInt(1, pc.getId());
			pstm.execute();
			SQLUtil.close(pstm);
			if (list.isEmpty()) {
				return;
			}
			
			con.setAutoCommit(false);
			pstm	=	con.prepareStatement("INSERT INTO character_revenge SET number=?, char_id=?, result=?, starttime=?, endtime=?, chasestarttime=?, chaseendtime=?, "
					+ "usecount=?, amount=?, targetobjid=?, targetclass=?, targetname=?, targetclanid=?, targetclanname=?");
			for (int i=0; i<list.size(); i++) {
				obj = list.get(i);
				int index = 0;
				pstm.setInt(++index, obj.getNumber());
				pstm.setInt(++index, obj.getCharId());
				pstm.setInt(++index, obj.getActionType().toInt());
				pstm.setTimestamp(++index, obj.getRegisterTimestamp());
				pstm.setTimestamp(++index, obj.getUnregisterDuration());
				pstm.setTimestamp(++index, obj.getActionTimestamp());
				pstm.setTimestamp(++index, obj.getActionDuration());
				pstm.setInt(++index, obj.getActionRemainCount());
				pstm.setInt(++index, obj.getActionCount());
				pstm.setInt(++index, obj.getUserUid());
				pstm.setInt(++index, obj.getGameClass());
				pstm.setString(++index, obj.getUserName());
				pstm.setInt(++index, obj.getPledgeId());
				pstm.setString(++index, obj.getPledgeName());
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
				_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
			}
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
			SQLUtil.close(pstm, con);
		}
	}
	
	public void checkRevenge(L1PcInstance pc, L1PcInstance attacker) {
		// 피격자 경우
		String attackerclanname = attacker.getClanid() != 0 ? attacker.getClanName() : StringUtil.EmptyString;
		if (getRevengeStep(pc, attacker.getName(), S_RevengeInfo.eAction.TAUNT)) {// 승리가 있을때
			changeRevenge(pc, attacker.getName(), S_RevengeInfo.eAction.PURSUIT, Config.REVENGE.REVENGE_PURSUIT_MAX_COUNT, attacker.getClanid(), attackerclanname);
		} else if (getRevengeStep(pc, attacker.getName(), S_RevengeInfo.eAction.PURSUIT)) {// 패배가 있을때
			updateRevenge(pc, attacker.getName(), Config.REVENGE.REVENGE_PURSUIT_MAX_COUNT);
		} else {
			writeRevenge(pc, S_RevengeInfo.eAction.PURSUIT, Config.REVENGE.REVENGE_PURSUIT_MAX_COUNT, 1, attacker.getId(), attacker.getType(), attacker.getName(), attacker.getClanid(), attackerclanname);
		}
		
		// 공격자의 경우
		String targetclanname = pc.getClanid() != 0 ? pc.getClanName() : StringUtil.EmptyString;
		if (getRevengeStep(attacker, pc.getName(), S_RevengeInfo.eAction.PURSUIT)) {// 패배가 있을때
			changeRevenge(attacker, pc.getName(), S_RevengeInfo.eAction.TAUNT, Config.REVENGE.REVENGE_TAUNT_MAX_COUNT, pc.getClanid(), targetclanname);
		} else if (getRevengeStep(attacker, pc.getName(), S_RevengeInfo.eAction.TAUNT)) {// 승리가 있을때
			updateRevenge(attacker, pc.getName(), Config.REVENGE.REVENGE_TAUNT_MAX_COUNT);
        	attacker.sendPackets(new S_RevengeInfoNoti(pc.getName()), true);
        	attacker.sendPackets(new S_RevengeInfo(attacker), true);
		} else {
			writeRevenge(attacker, S_RevengeInfo.eAction.TAUNT, Config.REVENGE.REVENGE_TAUNT_MAX_COUNT, 1, pc.getId(), pc.getType(), pc.getName(), pc.getClanid(), targetclanname);
			attacker.sendPackets(new S_RevengeInfoNoti(pc.getName()), true);
			attacker.sendPackets(new S_RevengeInfo(attacker), true);
		}
	}
	
	public FastMap<Integer, L1Revenge> getInfoList(){
		return DATA;
	}
	
	// 케릭터 삭제시 메모리 영역 제거
	public void removeInfo(int objid){
		if (DATA.containsKey(objid)) {
			DATA.remove(objid);
		}
	}
	
	// 마지막 primaryKey 구하기
	public int nextKey(){
		synchronized(_monitor){
			return ++_id;
		}
	}

	/** 등록 **/
	public void writeRevenge(L1PcInstance pc, S_RevengeInfo.eAction actionType, int usecount, int amount, int targetobjid, int targetclass, String targetname, int targetclanid, String targetclanname) {
		long sysTime = System.currentTimeMillis();
		L1RevengeTemp temp = new L1RevengeTemp(nextKey(), pc.getId(), actionType, new Timestamp(sysTime), new Timestamp(sysTime + (Config.REVENGE.REVENGE_DURATION_SECOND * 1000)), null, null, usecount, amount, targetobjid, targetclass, targetname, targetclanid, targetclanname);
		if (DATA.containsKey(pc.getId())) {
			DATA.get(pc.getId()).add(temp.getUserName(), temp);
		} else {
			L1Revenge revenge = new L1Revenge(pc.getId());
			revenge.add(temp.getUserName(), temp);
			DATA.put(pc.getId(), revenge);
		}
	}
	
	/** 대상의 복수정보 취득 **/
	public L1RevengeTemp getRevenge(L1PcInstance pc, String targetname){
		if (DATA.containsKey(pc.getId())) {
			return DATA.get(pc.getId()).getRevenge(targetname);
		}
		return null;
	}
	
	/** 결과 체크 **/
	public boolean getRevengeStep(L1PcInstance pc, String targetname, S_RevengeInfo.eAction actionType){
		L1RevengeTemp temp = getRevenge(pc, targetname);
		return temp != null && temp.getActionType() == actionType;
	}
	
	/** 사용 횟수 취득 **/
	public int getActionRemainCount(L1PcInstance pc, String targetname) {
		L1RevengeTemp temp = getRevenge(pc, targetname);
		if (temp != null) {
			return temp.getActionRemainCount();
		}
		return 0;
	}
	
	/** 결과 변경 **/
	public void changeRevenge(L1PcInstance pc, String targetname, S_RevengeInfo.eAction actionType, int actionRemainCount, int pledgeId, String pledgeName) {
		L1RevengeTemp temp = getRevenge(pc, targetname);
		long sysTime = System.currentTimeMillis();
		temp.getRegisterTimestamp().setTime(sysTime);
		temp.getUnregisterDuration().setTime(sysTime + (Config.REVENGE.REVENGE_DURATION_SECOND * 1000));
		temp.setActionType(actionType);
		temp.setActionCount(1);
		temp.setActionRemainCount(actionRemainCount);
		temp.setPledgeId(pledgeId);
		temp.setPledgeName(pledgeName);
	}
	
	/** 누적 업데이트 **/
	public void updateRevenge(L1PcInstance pc, String targetname, int actionRemainCount) {
		L1RevengeTemp temp = getRevenge(pc, targetname);
		long sysTime = System.currentTimeMillis();
		temp.getRegisterTimestamp().setTime(sysTime);
		temp.getUnregisterDuration().setTime(sysTime + (Config.REVENGE.REVENGE_DURATION_SECOND * 1000));
		temp.setActionCount(temp.getActionCount() + 1);
		temp.setActionRemainCount(actionRemainCount);
	}
	
	/** 도발하기 **/
	public void startTaunt(L1PcInstance pc, String targetname) {
		L1RevengeTemp temp = getRevenge(pc, targetname);
		temp.setActionRemainCount(temp.getActionRemainCount() - 1);
	}
	
	/** 추적시간 취득 **/
	public Timestamp getPursuitTimer(L1PcInstance pc, String targetname) {
		L1RevengeTemp temp = getRevenge(pc, targetname);
		if (temp != null) {
			return temp.getActionDuration();
		}
		return null;
	}

	/** 추적시작 **/
	public void startPursuit(L1PcInstance pc, String targetname) {
		L1RevengeTemp temp = getRevenge(pc, targetname);
		long sysTime = System.currentTimeMillis();
		temp.setActionTimestamp(new Timestamp(sysTime));
		temp.setActionDuration(new Timestamp(sysTime + (Config.REVENGE.REVENGE_PURSUIT_DURATION_SECOND * 1000)));
		temp.setActionRemainCount(temp.getActionRemainCount() - 1);
	}
	
	/** 타겟 추적종료 **/
	public void endTargetPursuit(L1PcInstance pc, String targetname) {
		L1RevengeTemp temp = getRevenge(pc, targetname);
		temp.setActionTimestamp(null);
		temp.setActionDuration(null);
		pc.setRevengeTarget(null);
	}
	
	/** 전체 추적종료 **/
	public void endAllPursuit(L1PcInstance pc) {
		if (DATA.containsKey(pc.getId())) {
			for (L1RevengeTemp temp : DATA.get(pc.getId()).getRevenges().values()) {
				temp.setActionTimestamp(null);
				temp.setActionDuration(null);
			}
		}
	}
	
	/** 삭제 **/
	public void DeleteRevenge(L1PcInstance pc, String targetname) {
		DATA.get(pc.getId()).remove(targetname);
	}
	public void DeleteRevenge(L1PcInstance pc, int targetobj) {
		DATA.get(pc.getId()).remove(targetobj);
	}

}

