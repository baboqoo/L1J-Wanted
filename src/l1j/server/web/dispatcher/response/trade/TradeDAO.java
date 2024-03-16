package l1j.server.web.dispatcher.response.trade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;
import l1j.server.web.dispatcher.response.board.CharacterBoardVO;

/**
 * 거래소
 * @author LinOffice
 */
public class TradeDAO {
	private static TradeDAO _instance;
	public static TradeDAO getInstance() {
		if (_instance == null) {
			_instance = new TradeDAO();
		}
		return _instance;
	}
	private int _id;
	public static Map<Integer, TradeVO> _trade		= new ConcurrentHashMap<Integer, TradeVO>();
	public static ArrayList<TradeVO> _tradeNotice	= new ArrayList<TradeVO>();
	
	private TradeDAO() {
		excuteInfo();
	}
	
	private void excuteInfo() {
		Connection con = null;
	    PreparedStatement pstm = null;
	    ResultSet rs = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("SELECT @RNUM := @RNUM + 1 AS ROWNUM, t.* FROM (SELECT id, title, content, bank, bankNumber, status, sellerName, sellerCharacter, sellerPhone, buyerName, buyerCharacter, buyerPhone, writeTime, send, receive, completeTime, sellerCancle, buyerCancle, top FROM app_trade ORDER BY writeTime DESC) t, (SELECT @RNUM := 0) R");
    		rs = pstm.executeQuery();
    		TradeVO vo = null;
    		while(rs.next()) {
    			vo = new TradeVO();
    			vo.setId(rs.getInt("id"));
    			vo.setTitle(rs.getString("title"));
    			vo.setContent(rs.getString("content"));
    			vo.setBank(rs.getString("bank"));
    			vo.setBankNumber(rs.getString("bankNumber"));
    			vo.setStatus(TradeStatus.getStatus(rs.getString("status")));
    			vo.setSellerName(rs.getString("sellerName"));
    			vo.setSellerCharacter(rs.getString("sellerCharacter"));
    			vo.setSellerPhone(rs.getString("sellerPhone"));
    			vo.setBuyerName(rs.getString("buyerName"));
    			vo.setBuyerCharacter(rs.getString("buyerCharacter"));
    			vo.setBuyerPhone(rs.getString("buyerPhone"));
    			vo.setWriteTime(rs.getTimestamp("writeTime"));
    			vo.setSend(Boolean.valueOf(rs.getString("send")));
    			vo.setReceive(Boolean.valueOf(rs.getString("receive")));
    			vo.setCompleteTime(rs.getTimestamp("completeTime"));
    			vo.setSellerCancle(Boolean.valueOf(rs.getString("sellerCancle")));
    			vo.setBuyerCancle(Boolean.valueOf(rs.getString("buyerCancle")));
    			vo.setRownum(rs.getInt("ROWNUM"));
    			vo.setTop(Boolean.valueOf(rs.getString("top")));
    			if (vo.isTop()) {
    				_tradeNotice.add(vo);
    			}
    			_trade.put(vo.getRownum(), vo);
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
	
	public static void getList(String charName, List<CharacterBoardVO> list, String searchType){
		CharacterBoardVO vo = null;
		if (searchType.equals("board")) {
			for (TradeVO board : _trade.values()) {
	    		if (board.getSellerCharacter().equals(charName)) {
	    			vo = new CharacterBoardVO();
					//vo.setBoardType("거래소");
					vo.setBoardType("Exchange (Marketplace)");
					vo.setBoard(board);
					vo.setBoardUrl("/trade/view");
					list.add(vo);
	    		}
	    	}
		}
    }
    
    public static List<TradeVO> getList(){
    	return new ArrayList<TradeVO>(_trade.values());
    }
    
    public static List<TradeVO> getList(TradeStatus status) {// 검색한 리스트가져올때
		List<TradeVO> list = new ArrayList<TradeVO>();
		for (TradeVO vo : _trade.values()) {
			if (vo.isTop()) {
				continue;
			}
			if (vo.getStatus().equals(status)) {
				list.add(vo);
			}
		}
		return list;
	}
    
    public static List<TradeVO> getSearchList(int size, String searchType, String searchText) {// 검색한 리스트가져올때
		List<TradeVO> list = new ArrayList<TradeVO>();
		for (TradeVO vo : _trade.values()) {
			if (vo.isTop()) {
				continue;
			}
			if (searchType.equals("title,contents")) {
				if (vo.getTitle().contains(searchText) || vo.getContent().contains(searchText)) {
					list.add(vo);
				}
			} else if (searchType.equals("title")) {
				if (vo.getTitle().contains(searchText)) {
					list.add(vo);
				}
			}
			if (list.size() >= size) {
				break;
			}
		}
		return list;
	}
    
    public static TradeVO getTrade(int rownum) {
    	return _trade.containsKey(rownum) ? _trade.get(rownum) : null;
    }
    
    public static TradeVO getTradeFromId(int id) {
    	if (_trade == null || _trade.isEmpty()) {
    		return null;
    	}
    	for (TradeVO vo : _trade.values()) {
    		if (vo.getId() == id) {
    			return vo;
    		}
    	}
    	return null;
    }
    
    public int getNextNum() {
		return ++_id;
	}
    
    public void insert(TradeVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("INSERT INTO app_trade (id, title, content, bank, bankNumber, status, sellerName, sellerCharacter, sellerPhone, buyerName, buyerCharacter, buyerPhone, writeTime, completeTime) "
    				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
    		int index = 0;
    		pstm.setInt(++index, vo.getId());
    		pstm.setString(++index, vo.getTitle());
    		pstm.setString(++index, vo.getContent());
    		pstm.setString(++index, vo.getBank());
    		pstm.setString(++index, vo.getBankNumber());
    		pstm.setString(++index, vo.getStatus().name());
    		pstm.setString(++index, vo.getSellerName());
    		pstm.setString(++index, vo.getSellerCharacter());
    		pstm.setString(++index, vo.getSellerPhone());
    		pstm.setString(++index, vo.getBuyerName());
    		pstm.setString(++index, vo.getBuyerCharacter());
    		pstm.setString(++index, vo.getBuyerPhone());
    		pstm.setTimestamp(++index, vo.getWriteTime());
    		pstm.setTimestamp(++index, vo.getCompleteTime());
    		pstm.executeUpdate();
    		mapInsert(vo);
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public void update(TradeVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("UPDATE app_trade SET title=?, content=?, bank=?, bankNumber=?, sellerName=?, sellerPhone=? WHERE id=?");
    		int index = 0;
    		pstm.setString(++index, vo.getTitle());
    		pstm.setString(++index, vo.getContent());
    		pstm.setString(++index, vo.getBank());
    		pstm.setString(++index, vo.getBankNumber());
    		pstm.setString(++index, vo.getSellerName());
    		pstm.setString(++index, vo.getSellerPhone());
    		pstm.setInt(++index, vo.getId());
    		pstm.executeUpdate();
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    // 구매 신청
    public void buyRegist(TradeVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("UPDATE app_trade SET status=?, buyerName=?, buyerCharacter=?, buyerPhone=? WHERE id=?");
    		int index = 0;
    		pstm.setString(++index, vo.getStatus().name());
    		pstm.setString(++index, vo.getBuyerName());
    		pstm.setString(++index, vo.getBuyerCharacter());
    		pstm.setString(++index, vo.getBuyerPhone());
    		pstm.setInt(++index, vo.getId());
    		pstm.executeUpdate();
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    // 인수 인계
    public void trading(TradeVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("UPDATE app_trade SET send=?, receive=? WHERE id=?");
    		pstm.setString(1, String.valueOf(vo.isSend()));
    		pstm.setString(2, String.valueOf(vo.isReceive()));
    		pstm.setInt(3, vo.getId());
    		pstm.executeUpdate();
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public void complete(TradeVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("UPDATE app_trade SET status=?, completeTime=? WHERE id=?");
    		pstm.setString(1, vo.getStatus().name());
    		pstm.setTimestamp(2, vo.getCompleteTime());
    		pstm.setInt(3, vo.getId());
    		pstm.executeUpdate();
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public void delete(TradeVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("DELETE FROM app_trade WHERE id=?");
    		pstm.setInt(1, vo.getId());
    		pstm.executeUpdate();
    		mapDelete(vo);
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public void mapDelete(TradeVO vo) {
    	int deleRownum = vo.getRownum();
    	TradeVO temp = null;
    	for (int i=1; i<=_trade.size(); i++) {
    		temp = _trade.get(i);
    		if (i>deleRownum) {
    			temp.setRownum(i-1);
    			_trade.put(i-1, temp);
    		}
    	}
    	_trade.remove(_trade.size());
    	if (vo.isTop()) {
    		_tradeNotice.remove(vo);
    	}
    }
    
    public void mapInsert(TradeVO vo) {
    	Map<Integer, TradeVO> chage = new ConcurrentHashMap<Integer, TradeVO>();
    	chage.put(1, vo);
    	for (int i=1; i<=_trade.size(); i++) {
    		_trade.get(i).setRownum(i+1);
    		chage.put(i+1, _trade.get(i));
    	}
    	_trade.clear();
    	_trade=null;
    	_trade=chage;
    	if (vo.isTop()) {
    		_tradeNotice.add(vo);
    	}
    }
    
    public static void reload() {
    	release();
    	_instance = new TradeDAO();
    }
    
    public static void release() {
    	_trade.clear();
    	_tradeNotice.clear();
    	_trade = null;
    	_tradeNotice = null;
    	_instance = null;
    }
}

