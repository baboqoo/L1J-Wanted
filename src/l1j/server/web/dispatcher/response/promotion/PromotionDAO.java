package l1j.server.web.dispatcher.response.promotion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

/**
 * 프로모션
 * @author LinOffice
 */
public class PromotionDAO {
	private static PromotionDAO _instance;
	public static PromotionDAO getInstance() {
		if (_instance == null) {
			_instance = new PromotionDAO();
		}
		return _instance;
	}
	private int _id;
	private static final ArrayList<PromotionVO> DATA = new ArrayList<PromotionVO>();
	private PromotionDAO() {
		load();
	}
	
	private void load() {
		Connection con			= null;
	    PreparedStatement pstm	= null;
	    ResultSet rs			= null;
	    try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("SELECT * FROM app_promotion ORDER BY id DESC");
    		rs = pstm.executeQuery();
    		PromotionVO vo = null;
    		while(rs.next()) {
    			vo = new PromotionVO(rs.getInt("id"), rs.getString("title"), rs.getString("subText"), rs.getString("promotionDate"), rs.getString("targetLink"), rs.getString("promotionImg"), rs.getString("listallImg"));
    			DATA.add(vo);
    			if (vo.getId() > _id) {
    				_id = vo.getId();
    			}
    		}
	    } catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public static ArrayList<PromotionVO> getPromotionList(){
		return DATA;
	}
	
	public static PromotionVO getPromotion(int id) {
		PromotionVO vo = null;
		for (PromotionVO promo : DATA) {
			if (promo.getId() == id) {
				vo = promo;
				break;
			}
		}
		return vo;
	}
	
	public int nextId() {
		return ++_id;
	}
	
	public boolean insertPromotion(PromotionVO vo) {
		Connection con			= null;
	    PreparedStatement pstm	= null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("INSERT INTO app_promotion SET id=?, title=?, subText=?, promotionDate=?, targetLink=?, promotionImg=?, listallImg=?");
    		int index = 0;
    		pstm.setInt(++index, vo.getId());
    		pstm.setString(++index, vo.getTitle());
    		pstm.setString(++index, vo.getSubText());
    		pstm.setString(++index, vo.getPromotionDate());
    		pstm.setString(++index, vo.getTargetLink());
    		pstm.setString(++index, vo.getPromotionImg());
    		pstm.setString(++index, vo.getListallImg());
    		if (pstm.executeUpdate() > 0) {
    			DATA.add(0, vo);
    			return true;
    		}
	    } catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	public boolean updatePromotion(PromotionVO vo) {
		Connection con			= null;
	    PreparedStatement pstm	= null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("UPDATE app_promotion SET title=?, subText=?, promotionDate=?, targetLink=?, promotionImg=?, listallImg=? WHERE id=?");
    		int index = 0;
    		pstm.setString(++index, vo.getTitle());
    		pstm.setString(++index, vo.getSubText());
    		pstm.setString(++index, vo.getPromotionDate());
    		pstm.setString(++index, vo.getTargetLink());
    		pstm.setString(++index, vo.getPromotionImg());
    		pstm.setString(++index, vo.getListallImg());
    		pstm.setInt(++index, vo.getId());
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
	
	public boolean deletePromotion(PromotionVO vo) {
		Connection con			= null;
	    PreparedStatement pstm	= null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("DELETE FROM app_promotion WHERE id=?");
    		pstm.setInt(1, vo.getId());
    		if (pstm.executeUpdate() > 0) {
    			DATA.remove(vo);
    			return true;
    		}
	    } catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	public static void release() {
		DATA.clear();
		_instance = null;
	}
}

