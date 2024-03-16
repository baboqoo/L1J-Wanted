package l1j.server.GameSystem.tjcoupon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.tjcoupon.bean.TJCouponBean;
import l1j.server.GameSystem.tjcoupon.user.TJCouponUser;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

/**
 * TJ 쿠폰 데이터 로더
 * @author LinOffice
 */
public class TJCouponLoader {
	private static class newInstance{
		public static final TJCouponLoader INSTANCE = new TJCouponLoader();
	}
	public static TJCouponLoader getInstance(){
		return newInstance.INSTANCE;
	}
	
	private static final ConcurrentHashMap<Integer, TJCouponUser> DATA = new ConcurrentHashMap<>();

	/**
	 * 캐릭터의 복구 정보를 반환한다.
	 * @param charId
	 * @return TjCouponUser
	 */
	public static TJCouponUser getUser(int charId){
		TJCouponUser user = DATA.get(charId);
		if (user == null) {
			user = new TJCouponUser();
			DATA.put(charId, user);
		}
		return user;
	}
	
	/**
	 * 캐릭터의 복구 정보를 제거한다.
	 * @param charId
	 */
	public static void remove(int charId){
		if (!DATA.containsKey(charId)) {
			return;
		}
		DATA.remove(charId);
	}
	
	private TJCouponLoader(){
		load();
	}
	
	void load(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		TJCouponBean coupon		= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM tj_coupon ORDER BY lostTime ASC");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				coupon	= new TJCouponBean(rs);
				getUser(coupon.getCharId()).put(coupon);
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
	 * 캐릭터의 복구 정보를 database에 갱신한다.
	 * @param pc
	 */
	public void save(L1PcInstance pc){
		if (pc == null) {
			return;
		}
		int charId				= pc.getId();
		Connection con			= null;
		PreparedStatement pstm	= null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("DELETE FROM tj_coupon WHERE charId=?");
			pstm.setInt(1, charId);
			pstm.execute();
			SQLUtil.close(pstm);
			
			ArrayList<TJCouponBean> list = getUser(charId).getCoupons();
			if (list.isEmpty()) {
				return;
			}
			con.setAutoCommit(false);
			
			pstm	= con.prepareStatement("INSERT INTO tj_coupon SET objId=?, charId=?, itemId=?, count=?, enchantLevel=?, attrLevel=?, bless=?, lostTime=?");
			for (TJCouponBean bean : list) {
				if (bean == null) {
					continue;
				}
				int index = 0;
				pstm.setInt(++index, bean.getObjId());
				pstm.setInt(++index, bean.getCharId());
				pstm.setInt(++index, bean.getItemId());
				pstm.setInt(++index, bean.getCount());
				pstm.setInt(++index, bean.getEnchantLevel());
				pstm.setInt(++index, bean.getAttrLevel());
				pstm.setInt(++index, bean.getBless());
				pstm.setTimestamp(++index, bean.getLostTime());
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
}

