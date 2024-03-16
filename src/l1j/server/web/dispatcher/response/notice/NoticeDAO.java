package l1j.server.web.dispatcher.response.notice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

/**
 * 공지사항
 * @author LinOffice
 */
public class NoticeDAO {
	private static NoticeDAO _instance;
	public static NoticeDAO getInstance() {
		if (_instance == null) {
			_instance = new NoticeDAO();
		}
		return _instance;
	}
	private int _id;
	
	public static Map<Integer, NoticeVO> _notice;
	public static Map<Integer, NoticeVO> _update;
	public static Map<Integer, NoticeVO> _event;
	public static List<NoticeVO> _topNotice;
	public static List<NoticeVO> _topUpdate;
	public static List<NoticeVO> _topEvent;
	
	private NoticeDAO() {
		_notice		= new ConcurrentHashMap<Integer, NoticeVO>();
		_update		= new ConcurrentHashMap<Integer, NoticeVO>();
		_event		= new ConcurrentHashMap<Integer, NoticeVO>();
		_topNotice	= new ArrayList<NoticeVO>();
		_topUpdate	= new ArrayList<NoticeVO>();
		_topEvent	= new ArrayList<NoticeVO>();
		excuteInfo();
	}
	
	private void excuteInfo() {
		Connection con = null;
	    PreparedStatement pstm = null;
	    ResultSet rs = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("SELECT @RNUM := @RNUM + 1 AS ROWNUM, t.* FROM (SELECT id, name, title, content, date, readcount, type, top, mainImg FROM app_board_notice WHERE type=0) t, (SELECT @RNUM := 0) R ORDER BY id DESC, date DESC");
    		rs = pstm.executeQuery();
    		while(rs.next()) {
    			NoticeVO vo = new NoticeVO(rs);
    			_notice.put(vo.getRownum(), vo);
    			if (vo.isTop()) {
    				_topNotice.add(vo);
    			}
    			if (vo.getId() > _id) {
    				_id = vo.getId();
    			}
    		}
    		SQLUtil.close(rs, pstm);
    		
    		pstm = con.prepareStatement("SELECT @RNUM := @RNUM + 1 AS ROWNUM, t.* FROM (SELECT id, name, title, content, date, readcount, type, top, mainImg FROM app_board_notice WHERE type=1) t, (SELECT @RNUM := 0) R  ORDER BY id DESC, date DESC");
    		rs = pstm.executeQuery();
    		while(rs.next()) {
    			NoticeVO vo = new NoticeVO(rs);
    			_update.put(vo.getRownum(), vo);
    			if (vo.isTop()) {
    				_topUpdate.add(vo);
    			}
    			if (vo.getId() > _id) {
    				_id = vo.getId();
    			}
    		}
    		SQLUtil.close(rs, pstm);
    		
    		pstm = con.prepareStatement("SELECT @RNUM := @RNUM + 1 AS ROWNUM, t.* FROM (SELECT id, name, title, content, date, readcount, type, top, mainImg FROM app_board_notice WHERE type=2) t, (SELECT @RNUM := 0) R ORDER BY id DESC, date DESC");
    		rs = pstm.executeQuery();
    		while(rs.next()) {
    			NoticeVO vo = new NoticeVO(rs);
    			_event.put(vo.getRownum(), vo);
    			if (vo.isTop()) {
    				_topEvent.add(vo);
    			}
    			if (vo.getId() > _id) {
    				_id = vo.getId();
    			}
    		}
    	} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
    
    public static List<NoticeVO> getList(int start, int end, int type){
    	List<NoticeVO> list = new ArrayList<>();
    	int cnt=0;
    	switch(type) {
    	case 1:
    		for (NoticeVO vo : _update.values()) {
    			if (vo.isTop()) {
    				continue;
    			}
        		cnt++;
            	if (cnt >= start) {
            		list.add(vo);
            	}
            	if (list.size() >= end) {
            		break;
            	}
        	}
    		break;
    	case 2:
    		for (NoticeVO vo : _event.values()) {
    			if (vo.isTop()) {
    				continue;
    			}
        		cnt++;
            	if (cnt >= start) {
            		list.add(vo);
            	}
            	if (list.size() >= end) {
            		break;
            	}
        	}
    		break;
    	default:
    		for (NoticeVO vo : _notice.values()) {
    			if (vo.isTop()) {
    				continue;
    			}
        		cnt++;
            	if (cnt >= start) {
            		list.add(vo);
            	}
            	if (list.size() >= end) {
            		break;
            	}
        	}
    		break;
    	}
    	return list;
    }
    
    public static List<NoticeVO> getSearchList(int size, String searchType, String searchText, int type) {// 검색한 리스트가져올때
		List<NoticeVO> list = new ArrayList<>();
		switch(type) {
		case 1:
			for (NoticeVO vo : _update.values()) {
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
			break;
		case 2:
			for (NoticeVO vo : _event.values()) {
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
			break;
		default:
			for (NoticeVO vo : _notice.values()) {
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
			break;
		}
		return list;
	}
    
    public static List<NoticeVO> getTopList(int type){
    	switch(type) {
    	case 0:return _topNotice;
    	case 1:return _topUpdate;
    	case 2:return _topEvent;
    	default:return null;
    	}
    }
    
    public static NoticeVO getNotice(int num, int type) {
    	switch(type) {
    	case 1:if(_update.containsKey(num))return _update.get(num);break;
    	case 2:if(_event.containsKey(num))return _event.get(num);break;
    	default:if(_notice.containsKey(num))return _notice.get(num);break;
    	}
    	return null;
    }
    
    public int getNextNum() {
    	return ++_id;
    }
    
    public void insert(NoticeVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("INSERT INTO app_board_notice (id, name, title, content, date, readcount, type, mainImg) VALUES (?,?,?,?,?,?,?,?)");
    		int index = 0;
    		pstm.setInt(++index, vo.getId());
    		pstm.setString(++index, vo.getName());
    		pstm.setString(++index, vo.getTitle());
    		pstm.setString(++index, vo.getContent());
    		pstm.setTimestamp(++index, vo.getDate());
    		pstm.setInt(++index, 0);
    		pstm.setInt(++index, vo.getType());
    		pstm.setString(++index, vo.getMainImg());
    		pstm.executeUpdate();
    		mapInsert(vo);
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public void mapInsert(NoticeVO vo) {
    	Map<Integer, NoticeVO> chage = new ConcurrentHashMap<>();
    	chage.put(1, vo);
    	switch(vo.getType()) {
    	case 1:
    		for (int i=1; i<=_update.size(); i++) {
    			_update.get(i).setRownum(i+1);
        		chage.put(i+1, _update.get(i));
        		
        	}
    		_update.clear();
    		_update=null;
    		_update=chage;
    		break;
    	case 2:
    		for (int i=1; i<=_event.size(); i++) {
    			_event.get(i).setRownum(i+1);
        		chage.put(i+1, _event.get(i));
        		
        	}
    		_event.clear();
    		_event=null;
    		_event=chage;
    		break;
    	default:
    		for (int i=1; i<=_notice.size(); i++) {
        		_notice.get(i).setRownum(i+1);
        		chage.put(i+1, _notice.get(i));
        		
        	}
    		_notice.clear();
        	_notice=null;
        	_notice=chage;
    		break;
    	}
    }
    
    public void readCountAdd(int rownum, int type) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("UPDATE app_board_notice SET readcount=readcount+1 WHERE id=?");
    		NoticeVO vo = getNotice(rownum, type);
    		pstm.setInt(1, vo.getId());
    		pstm.executeUpdate();
    		vo.setReadcount(vo.getReadcount()+1);
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public void update(NoticeVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("UPDATE app_board_notice SET title=?, content=?, type=?, mainImg=? WHERE id=?");
    		int index = 0;
    		pstm.setString(++index, vo.getTitle());
    		pstm.setString(++index, vo.getContent());
    		pstm.setInt(++index, vo.getType());
    		pstm.setString(++index, vo.getMainImg());
    		pstm.setInt(++index, vo.getId());
    		pstm.executeUpdate();
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public void delete(NoticeVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("DELETE FROM app_board_notice WHERE id=?");
    		pstm.setInt(1, vo.getId());
    		pstm.executeUpdate();
    		
    		if (vo.isTop()) {
    			switch(vo.getType()) {
    			case 1:_topUpdate.remove(vo);break;
    			case 2:_topEvent.remove(vo);break;
    			default:_topNotice.remove(vo);break;
    			}
    		}
    		
    		mapDelete(vo);
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public void mapDelete(NoticeVO vo) {
    	int deleRownum = vo.getRownum();
    	switch(vo.getType()) {
    	case 1:
    		for (int i=1; i<=_update.size(); i++) {
        		if (i > deleRownum) {
        			_update.get(i).setRownum(i-1);
        			_update.put(i-1, _update.get(i));
        		}
        	}
    		_update.remove(_update.size());
    		break;
    	case 2:
    		for (int i=1; i<=_event.size(); i++) {
        		if (i > deleRownum) {
        			_event.get(i).setRownum(i-1);
        			_event.put(i-1, _event.get(i));
        		}
        	}
    		_event.remove(_event.size());
    		break;
    	default:
    		for (int i=1; i<=_notice.size(); i++) {
        		if (i > deleRownum) {
        			_notice.get(i).setRownum(i-1);
        			_notice.put(i-1, _notice.get(i));
        		}
        	}
    		_notice.remove(_notice.size());
    		break;
    	}
    }
    
    public static void release() {
    	_notice.clear();
    	_update.clear();
    	_event.clear();
    	_topNotice.clear();
    	_topUpdate.clear();
    	_topEvent.clear();
    	_notice = null;
    	_update = null;
    	_event = null;
    	_topNotice = null;
    	_topUpdate = null;
    	_topEvent = null;
    	_instance = null;
    }
}

