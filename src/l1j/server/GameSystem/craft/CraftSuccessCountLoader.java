package l1j.server.GameSystem.craft;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javolution.util.FastMap;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.craft.bean.L1CraftSuccessCountUser;
import l1j.server.common.bin.craft.Craft;
import l1j.server.common.bin.craft.Craft.eSuccessCountType;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

/**
 * 제작 한정 성공 수량 유저 정보
 * @author LinOffice
 */
public class CraftSuccessCountLoader {
	private static class newInstance {
		private static final CraftSuccessCountLoader INSTANCE = new CraftSuccessCountLoader();
	}
	public static CraftSuccessCountLoader getInstance(){
		return newInstance.INSTANCE;
	}
	
	// 유저의 한정 제작 정보 <케릭터오브젝트, <craftId, L1CraftSuccessCountUser>>
	static final FastMap<Integer, FastMap<Integer, L1CraftSuccessCountUser>> DATA = new FastMap<Integer, FastMap<Integer,L1CraftSuccessCountUser>>();
	// 서버 한정 제작 수량
	static final FastMap<Integer, Integer> SERVER_CURRENT_COUNT = new FastMap<Integer, Integer>();
	
	/**
	 * 캐릭터의 한정 제작 정보 반환
	 * @param charId
	 * @param craftId
	 * @return L1CraftSuccessCountUser
	 */
	static L1CraftSuccessCountUser getCraftSuccessCountUser(int charId, int craftId){
		FastMap<Integer, L1CraftSuccessCountUser> userMap = DATA.get(charId);
		return userMap == null ? null : userMap.get(craftId);
	}

	/**
	 * 한정 제작 현재 성공 수량 조사
	 * @param pc
	 * @param craftId
	 * @param success_count_type
	 * @return count
	 */
	public static int getCurrentCount(L1PcInstance pc, int craftId, Craft.eSuccessCountType success_count_type){
		switch(success_count_type){
		case Account:
			return getAccountCurrentCount(pc, craftId);
		case Character:
			return getCharacterCurrentCount(pc, craftId);
		case AllServers:
		case World:
			return getServerCurrentCount(craftId);
		default:
			throw new IllegalArgumentException(String.format("invalid arguments CraftSuccessCountLoader.getCurrentCount, %s", pc.getName()));
		}
	}
	
	/**
	 * 서버의 한정 제작 현재수량 반환
	 * @param craftId
	 * @return count
	 */
	static int getServerCurrentCount(int craftId){
		return !SERVER_CURRENT_COUNT.containsKey(craftId) ? 0 : SERVER_CURRENT_COUNT.get(craftId);
	}
	
	/**
	 * 캐릭터의 현재 제작 수량 반환
	 * @param pc
	 * @param craftId
	 * @return count
	 */
	static int getCharacterCurrentCount(L1PcInstance pc, int craftId){
		L1CraftSuccessCountUser user = getCraftSuccessCountUser(pc.getId(), craftId);
		return user == null ? 0 : user.getCurrentCount();
	}
	
	/**
	 * 계정의 현재 제작 수량 반환
	 * @param pc
	 * @param craftId
	 * @return count
	 */
	static int getAccountCurrentCount(L1PcInstance pc, int craftId){
		int count = 0;
		for (FastMap<Integer, L1CraftSuccessCountUser> data : DATA.values()) {
			for (Map.Entry<Integer, L1CraftSuccessCountUser> entry : data.entrySet()) {
				int key							= entry.getKey();
				L1CraftSuccessCountUser value	= entry.getValue();
				if (key == craftId && value.getAccountName().equals(pc.getAccountName())) {
					count += value.getCurrentCount();
				}
			}
		}
		return count;
	}
	
	/**
	 * 기본 생성자(유저의 정보를 담당하므로 싱글톤 생성후 파기하지 않는다)
	 * 데이터를 로드한다.
	 */
	private CraftSuccessCountLoader(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		L1CraftSuccessCountUser user	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM craft_success_count_user ORDER BY accountName ASC, charId ASC, craftId ASC");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				user = new L1CraftSuccessCountUser(rs);
				FastMap<Integer, L1CraftSuccessCountUser> userMap = DATA.get(user.getCharId());
				if (userMap == null) {
					userMap = new FastMap<Integer, L1CraftSuccessCountUser>();
					DATA.put(user.getCharId(), userMap);
				}
				userMap.put(user.getCraftId(), user);
				
				// 서버 한정 제작 수량 로드
				if (user.getSuccessCountType() == eSuccessCountType.World || user.getSuccessCountType() == eSuccessCountType.AllServers) {
					Integer serverCount = SERVER_CURRENT_COUNT.get(user.getCraftId());
					SERVER_CURRENT_COUNT.put(user.getCraftId(), serverCount == null ? user.getCurrentCount() : serverCount + user.getCurrentCount());
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	/**
	 * 캐릭터의 한정 제작 정보를 생성한다.
	 * @param pc
	 * @param craftId
	 * @param success_count_type
	 * @param count
	 * @return L1CraftSuccessCountUser
	 */
	L1CraftSuccessCountUser createUser(L1PcInstance pc, int craftId, Craft.eSuccessCountType success_count_type, int count){
		L1CraftSuccessCountUser user = new L1CraftSuccessCountUser(pc.getAccountName(), pc.getId(), craftId, success_count_type, count);
		FastMap<Integer, L1CraftSuccessCountUser> userMap = DATA.get(user.getCharId());
		if (userMap == null) {
			userMap = new FastMap<Integer, L1CraftSuccessCountUser>();
			DATA.put(user.getCharId(), userMap);
		}
		userMap.put(user.getCraftId(), user);
		return user;
	}
	
	static final String UPSERT_QUERY = "INSERT INTO craft_success_count_user "
			+ "(accountName, charId, craftId, success_count_type, currentCount) VALUES (?,?,?,?,?) "
			+ "ON DUPLICATE KEY UPDATE success_count_type=?, currentCount=?";
	
	/**
	 * 한정 제작 수량 갱신
	 * insert 와 update 동시 진행
	 * @param pc
	 * @param craftId
	 * @param success_count_type
	 * @param count
	 * @return boolean
	 */
	public boolean upsert(L1PcInstance pc, int craftId, Craft.eSuccessCountType success_count_type, int count){
		L1CraftSuccessCountUser user	= getCraftSuccessCountUser(pc.getId(), craftId);
		if (user == null) {
			user	= createUser(pc, craftId, success_count_type, count);// 생성
		} else {
			user.addCurrentCount(count);// 제작 수량 추가
		}
		
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(UPSERT_QUERY);
			int i	= 0;
			pstm.setString(++i,	user.getAccountName());
			pstm.setInt(++i,	user.getCharId());
			pstm.setInt(++i,	user.getCraftId());
			pstm.setString(++i,	user.getSuccessCountType().name());
			pstm.setInt(++i,	user.getCurrentCount());
			pstm.setString(++i,	user.getSuccessCountType().name());
			pstm.setInt(++i,	user.getCurrentCount());
			if (pstm.executeUpdate() > 0) {
				if (success_count_type == eSuccessCountType.World || success_count_type == eSuccessCountType.AllServers) {
					Integer serverCount = getServerCurrentCount(craftId) + count;
					SERVER_CURRENT_COUNT.put(craftId, serverCount);
				}
				return true;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
}

