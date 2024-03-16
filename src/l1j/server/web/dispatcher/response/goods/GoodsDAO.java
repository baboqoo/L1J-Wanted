package l1j.server.web.dispatcher.response.goods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;
import l1j.server.web.dispatcher.response.item.ItemDAO;

/**
 * N샵
 * @author LinOffice
 */
public class GoodsDAO {
	private static GoodsDAO _instance;
	public static GoodsDAO getInstance() {
		if (_instance == null) {
			_instance = new GoodsDAO();
		}
		return _instance;
	}
	
	private static final Map<Integer, GoodsVO> GOODS = new ConcurrentHashMap<Integer, GoodsVO>();
	
	private GoodsDAO() {
		load();
	}
	
	private void load() {
		Connection con			= null;
	    PreparedStatement pstm	= null;
	    ResultSet rs			= null;
	    GoodsVO vo				= null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("SELECT * FROM app_nshop ORDER BY id ASC");
    		rs = pstm.executeQuery();
    		while(rs.next()) {
    			vo = new GoodsVO();
    			vo.setId(rs.getInt("id"));
    			vo.setItemid(rs.getInt("itemid"));
    			vo.setItemname(rs.getString("itemname"));
    			vo.setPrice(rs.getInt("price"));
    			vo.setPriceType(GoodsPriceType.fromString(rs.getString("price_type")));
    			vo.setSavedPoint(rs.getInt("saved_point"));
    			vo.setPack(rs.getInt("pack"));
    			vo.setEnchant(rs.getInt("enchant"));
    			vo.setLimitCount(rs.getInt("limitCount"));
    			vo.setFlag(GoodsFlag.fromString(rs.getString("flag")));
    			vo.setIteminfo(rs.getString("iteminfo"));
    			vo.setInvgfx(ItemDAO.getItemInfo(vo.getItemid()).getInvgfx());
    			GOODS.put(vo.getId(), vo);
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public static Map<Integer, GoodsVO> getGoods(){
		return GOODS;
	}
	
	public static GoodsVO getGoodsInfo(int id) {
		return GOODS.containsKey(id) ? GOODS.get(id) : null;
	}
	
	public static List<GoodsVO> getList(int start, int end){
    	List<GoodsVO> list = new ArrayList<>();
    	int cnt=0;
		for (GoodsVO vo : GOODS.values()) {
    		cnt++;
        	if (cnt >= start) {
        		list.add(vo);
        	}
        	if (list.size() >= end) {
        		break;
        	}
    	}
    	return list;
    }
	
	public static ArrayList<GoodsVO> getKeywordGoods(String keyword){
		ArrayList<GoodsVO> list = new ArrayList<GoodsVO>();
		for (GoodsVO vo : GOODS.values()) {
			if (vo.getItemname().contains(keyword)) {
				list.add(vo);
			}
		}
		return list;
	}
	
	public static ArrayList<GoodsVO> getKeywordGoodsLimit(String keyword, int end){
		ArrayList<GoodsVO> list = new ArrayList<GoodsVO>();
		for (GoodsVO vo : GOODS.values()) {
			if (vo.getItemname().contains(keyword)) {
				list.add(vo);
				if (list.size() >= end) {
					break;
				}
			}
		}
		return list;
	}
	
	public static void release() {
		GOODS.clear();
		_instance = null;
	}
}

