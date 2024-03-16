package l1j.server.web.dispatcher.response.coupon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

/**
 * 쿠폰
 * @author LinOffice
 */
public class CouponDAO {
	private static CouponDAO _instance;
	public static CouponDAO getInstance() {
		if (_instance == null) {
			_instance = new CouponDAO();
		}
		return _instance;
	}
	private List<CouponVO> _list;
	private CouponDAO() {
		_list = new ArrayList<>();
		load();
	}
	
	private void load() {
		Connection con = null;
	    PreparedStatement pstm = null;
	    ResultSet rs = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("SELECT number, type, price, status, useAccount, createTime, useTime FROM app_coupon ORDER BY createTime ASC");
    		rs = pstm.executeQuery();
    		CouponVO coupon;
    		while(rs.next()) {
    			coupon = new CouponVO(rs.getString("number"), CouponType.fromString(rs.getString("type")), rs.getInt("price"), Boolean.parseBoolean(rs.getString("status")), 
    					rs.getString("useAccount"), rs.getTimestamp("createTime"), rs.getTimestamp("useTime"));
    			_list.add(coupon);
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public List<CouponVO> getList(){
		return _list;
	}
	
	public List<CouponVO> getList(boolean status){
		List<CouponVO> list = new ArrayList<CouponVO>();
		for (CouponVO coupon : _list) {
			if (coupon.isStatus() == status) {
				list.add(coupon);
			}
		}
		return list;
	}
	
	public List<CouponVO> getList(String number){
		List<CouponVO> list = new ArrayList<CouponVO>();
		for (CouponVO coupon : _list) {
			if (coupon.getNumber().contains(number)) {
				list.add(coupon);
			}
		}
		return list;
	}
	
	public CouponVO getCoupon(String number) {
		CouponVO coupon = null;
		for (CouponVO vo : _list) {
			if (vo.getNumber().equals(number)) {
				coupon = vo;
				break;
			}
		}
		return coupon;
	}
	
	public boolean updateStatus(CouponVO coupon) {
		Connection con = null;
	    PreparedStatement pstm = null;
	    try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("UPDATE app_coupon SET status=?, useAccount=?, useTime=? WHERE number=?");
    		pstm.setString(1, String.valueOf(coupon.isStatus()));
    		pstm.setString(2, coupon.getUseAccount());
    		pstm.setTimestamp(3, coupon.getUseTime());
    		pstm.setString(4, coupon.getNumber());
    		if (pstm.executeUpdate() > 0) {
    			return true;
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	    return false;
	}
	
	public boolean insert(CouponVO coupon) {
		Connection con = null;
	    PreparedStatement pstm = null;
	    try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("INSERT INTO app_coupon (number, type, price, status, useAccount, createTime, useTime) VALUES (?,?,?,?,?,?,?)");
    		int index = 0;
    		pstm.setString(++index, coupon.getNumber());
    		pstm.setString(++index, coupon.getType().toFlag());
    		pstm.setInt(++index, coupon.getValue());
    		pstm.setString(++index, String.valueOf(coupon.isStatus()));
    		pstm.setString(++index, coupon.getUseAccount());
    		pstm.setTimestamp(++index, coupon.getCreateTime());
    		pstm.setTimestamp(++index, coupon.getUseTime());
    		if (pstm.executeUpdate() > 0) {
    			_list.add(coupon);
    			return true;
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	    return false;
	}
	
	public boolean delete(CouponVO coupon) {
		Connection con = null;
	    PreparedStatement pstm = null;
	    try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("DELETE FROM app_coupon WHERE number=?");
    		pstm.setString(1, coupon.getNumber());
    		if (pstm.executeUpdate() > 0) {
    			_list.remove(coupon);
    			return true;
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	    return false;
	}
	
	public static void reload() {
		release();
		_instance = new CouponDAO();
	}
	
	public static void release() {
		CouponDAO oldInstance = _instance;
		oldInstance._list.clear();
		oldInstance = null;
	}
}

