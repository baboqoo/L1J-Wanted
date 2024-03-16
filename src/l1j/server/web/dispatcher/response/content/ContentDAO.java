package l1j.server.web.dispatcher.response.content;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.common.data.Gender;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.response.board.CharacterBoardVO;

/**
 * 컨텐츠 공모 게시판
 * @author LinOffice
 */
public class ContentDAO {
	private static ContentDAO _instance;
	public static ContentDAO getInstance() {
		if (_instance == null) {
			_instance = new ContentDAO();
		}
		return _instance;
	}
	private int _id;
	public static Map<Integer, ContentVO> _freeBoard;
	public static List<ContentVO> _contentNotice = new ArrayList<ContentVO>();
	
	private ContentDAO() {
		if (_freeBoard == null) {
			_freeBoard = new ConcurrentHashMap<Integer, ContentVO>();
		}
		excuteInfo();
	}
	
	private void excuteInfo() {
		Connection con = null;
	    PreparedStatement pstm = null;
	    ResultSet rs = null;
	    PreparedStatement answerPstm = null;
	    ResultSet answerRs = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("SELECT @RNUM := @RNUM + 1 AS ROWNUM, t.* FROM (SELECT id, name, title, content, date, chatype, chasex, readcount, mainImg, top, likenames FROM app_board_content ORDER BY date DESC, id DESC) t, (SELECT @RNUM := 0) R");
    		rs = pstm.executeQuery();
    		while(rs.next()) {
    			ContentVO vo = new ContentVO();
    			vo.setId(rs.getInt("id"));
    			vo.setName(rs.getString("name"));
    			vo.setTitle(rs.getString("title"));
    			vo.setContent(rs.getString("content"));
    			vo.setDate(rs.getTimestamp("date"));
    			vo.setChatype(rs.getInt("chatype"));
    			vo.setChaGender(Gender.fromInt(rs.getInt("chasex")));
    			vo.setReadcount(rs.getInt("readcount"));
    			vo.setMainImg(rs.getString("mainImg"));
    			vo.setTop(Boolean.parseBoolean(rs.getString("top")));
    			String text = rs.getString("likenames");
    			ArrayList<String> likeList = new ArrayList<String>();
    			if (!StringUtil.isNullOrEmpty(text)) {
    				StringTokenizer st = new StringTokenizer(text, StringUtil.LineString);
    				while(st.hasMoreElements()) {
    					likeList.add(st.nextToken());
    				}
    			}
    			vo.setLikenames(likeList);
    			
    			vo.setRownum(rs.getInt("ROWNUM"));
    			
    			answerPstm = con.prepareStatement("SELECT id, boardId, name, chaType, chaSex, date, content, likenames FROM app_board_content_comment WHERE boardId=? ORDER BY date ASC");
    			answerPstm.setInt(1, vo.getId());
    			answerRs = answerPstm.executeQuery();
    			while(answerRs.next()) {
    				String commentText = answerRs.getString("likenames");
    				ArrayList<String> commentLikeList = new ArrayList<String>();
    				if (!StringUtil.isNullOrEmpty(commentText)) {
        				StringTokenizer st = new StringTokenizer(commentText, StringUtil.LineString);
        				while(st.hasMoreElements()) {
        					commentLikeList.add(st.nextToken());
        				}
    				}
    				ContentCommentVO answer = new ContentCommentVO(answerRs.getInt("id"), answerRs.getInt("boardId"), answerRs.getString("name"), answerRs.getInt("chaType"), Gender.fromInt(answerRs.getInt("chaSex")), answerRs.getTimestamp("date"), answerRs.getString("content"), commentLikeList);
    				if (vo.getAnswerList() == null) {
    					vo.setAnswerList(new ArrayList<ContentCommentVO>());
    				}
    				vo.getAnswerList().add(answer);
    			}
    			if (vo.isTop()) {
    				_contentNotice.add(vo);// 공지사항
    			}
    			_freeBoard.put(vo.getRownum(), vo);
    			if (vo.getId() > _id){
    				_id = vo.getId();
    			}
    			SQLUtil.close(answerRs, answerPstm);
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(answerRs, answerPstm);
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public static void getList(String charName, List<CharacterBoardVO> list, String searchType){
		CharacterBoardVO vo = null;
		if (searchType.equals("board")) {
			for (ContentVO board : _freeBoard.values()) {
	    		if (board.getName().equals(charName)) {
	    			vo = new CharacterBoardVO();
					//vo.setBoardType("컨텐츠");
					vo.setBoardType("Contents");
					vo.setBoard(board);
					vo.setBoardUrl("/contents/view");
					list.add(vo);
	    		}
	    	}
		} else if (searchType.equals("comment")) {
			for (ContentVO board : _freeBoard.values()) {
				if (board.getAnswerList() == null || board.getAnswerList().isEmpty()) {
					continue;
				}
				for (ContentCommentVO comment : board.getAnswerList()) {
					if (comment.getName().equals(charName)) {
		    			vo = new CharacterBoardVO();
						//vo.setBoardType("컨텐츠");
						vo.setBoardType("Contents");
						vo.setBoard(board);
						vo.setBoardUrl("/contents/view");
						list.add(vo);
						break;
		    		}
				}
	    	}
		}
    }
    
    public static List<ContentVO> getList(int start, int end){
    	List<ContentVO> list = new ArrayList<ContentVO>();
    	int cnt=0;
    	for (ContentVO vo : _freeBoard.values()) {
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
    	return list;
    }
    
    public static List<ContentVO> getSearchList(int size, String searchType, String searchText) {// 검색한 리스트가져올때
    	List<ContentVO> list = new ArrayList<ContentVO>();
		for (ContentVO vo : _freeBoard.values()) {
			if (vo.isTop()) {
				continue;
			}
			switch(searchType) {
			case "title,contents":
				if (vo.getTitle().contains(searchText) || vo.getContent().contains(searchText)) {
					list.add(vo);
				}
				break;
			case "title":
				if (vo.getTitle().contains(searchText)) {
					list.add(vo);
				}
				break;
			case "writer":
				if (vo.getName().contains(searchText)) {
					list.add(vo);
				}
				break;
			}
			if (list.size() >= size) {
				break;
			}
		}
		return list;
	}
    
    public static ContentVO getBoard(int num) {
    	if (_freeBoard.containsKey(num)) {
    		return _freeBoard.get(num);
    	}
    	return null;
    }
    
    public static ContentVO getBoardFromId(int id) {
    	if (_freeBoard == null || _freeBoard.isEmpty()) {
    		return null;
    	}
    	for (ContentVO vo : _freeBoard.values()) {
    		if (vo.getId() == id) {
    			return vo;
    		}
    	}
    	return null;
    }
    
    public static ContentCommentVO getBoardAnser(int num) {
    	ContentCommentVO vo = null;
    	ArrayList<ContentCommentVO> answerList = null;
    	for (ContentVO board : _freeBoard.values()) {
    		answerList = board.getAnswerList();
    		if (answerList != null && !answerList.isEmpty()) {
    			for (ContentCommentVO anwer : answerList) {
        			if (anwer.getId() == num) {
        				vo = anwer;
        				break;
        			}
        		}
    		}
    	}
    	return vo;
    }
    
    public int getNextNum() {
		return ++_id;
	}
    
    public static int getNextAnswerNum() {
    	int num = 0;
    	ArrayList<ContentCommentVO> answerList = null;
    	for (ContentVO vo : _freeBoard.values()) {
    		answerList = vo.getAnswerList();
    		if (answerList != null && !answerList.isEmpty()) {
    			int answerNum = answerList.get(answerList.size() - 1).getId();
    			if (num < answerNum) {
    				num = answerNum;
    			}
    		}
    	}
    	return num+1;
    }
    
    public void insert(ContentVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("INSERT INTO app_board_content (id, name, title, content, date, chatype, chasex, readcount, mainImg, top) VALUES (?,?,?,?,?,?,?,?,?,?)");
    		int index = 0;
    		pstm.setInt(++index, vo.getId());
    		pstm.setString(++index, vo.getName());
    		pstm.setString(++index, vo.getTitle());
    		pstm.setString(++index, vo.getContent());
    		pstm.setTimestamp(++index, vo.getDate());
    		pstm.setInt(++index, vo.getChatype());
    		pstm.setInt(++index, vo.getChaGender().toInt());
    		pstm.setInt(++index, 0);
    		pstm.setString(++index, vo.getMainImg());
    		pstm.setString(++index, String.valueOf(vo.isTop()));
    		pstm.execute();
    		if (vo.isTop()) {
				_contentNotice.add(vo);
			}
    		mapInsert(vo);
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public void insertAnswer(ContentCommentVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("INSERT INTO app_board_content_comment (id, boardId, name, chatype, chasex, date, content) VALUES (?,?,?,?,?,?,?)");
    		int index = 0;
    		pstm.setInt(++index, vo.getId());
    		pstm.setInt(++index, vo.getBoardId());
    		pstm.setString(++index, vo.getName());
    		pstm.setInt(++index, vo.getChatype());
    		pstm.setInt(++index, vo.getChaGender().toInt());
    		pstm.setTimestamp(++index, vo.getDate());
    		pstm.setString(++index, vo.getContent());
    		pstm.execute();
    		anwerListAdd(vo);
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public void deleteAnswer(ContentCommentVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("DELETE FROM app_board_content_comment WHERE id=?");
    		pstm.setInt(1, vo.getId());
    		pstm.execute();
    		if (vo.getLikenames() != null && !vo.getLikenames().isEmpty()) {
    			vo.getLikenames().clear();
        		vo.setLikenames(null);
    		}
    		for (ContentVO board : _freeBoard.values()) {
    			if (board.getId() == vo.getBoardId()) {
    				board.getAnswerList().remove(vo);
    				break;
    			}
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public void readCountAdd(int rownum) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("UPDATE app_board_content SET readcount=readcount+1 WHERE id=?");
    		ContentVO vo = getBoard(rownum);
    		pstm.setInt(1, vo.getId());
    		pstm.execute();
    		vo.setReadcount(vo.getReadcount()+1);
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public void update(ContentVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("UPDATE app_board_content SET title=?, content=?, mainImg=?, top=? WHERE id=?");
    		pstm.setString(1, vo.getTitle());
    		pstm.setString(2, vo.getContent());
    		pstm.setString(3, vo.getMainImg());
    		pstm.setString(4, String.valueOf(vo.isTop()));
    		pstm.setInt(5, vo.getId());
    		pstm.execute();
    		if (vo.isTop() && !_contentNotice.contains(vo)) {
				_contentNotice.add(vo);
			} else if (!vo.isTop() && _contentNotice.contains(vo)) {
				_contentNotice.remove(vo);
			}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public void delete(ContentVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("DELETE T1, T2 FROM app_board_content AS T1 LEFT JOIN app_board_content_comment AS T2 ON T1.id = T2.boardId WHERE T1.id=?");
    		pstm.setInt(1, vo.getId());
    		pstm.execute();
    		if (vo.isTop()) {
    			_contentNotice.remove(vo);
    		}
    		mapDelete(vo);
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public void mapDelete(ContentVO vo) {
    	int deleRownum = vo.getRownum();
    	ContentVO temp = null;
    	for (int i=1; i<=_freeBoard.size(); i++) {
    		temp = _freeBoard.get(i);
    		if (i>deleRownum) {
    			temp.setRownum(i-1);
    			_freeBoard.put(i-1, temp);
    		}
    	}
    	if (vo.getAnswerList() != null) {
    		vo.getAnswerList().clear();
        	vo.setAnswerList(null);
    	}
    	_freeBoard.remove(_freeBoard.size());
    }
    
    public void mapInsert(ContentVO vo) {
    	Map<Integer, ContentVO> chage = new ConcurrentHashMap<Integer, ContentVO>();
    	chage.put(1, vo);
    	for (int i=1; i<=_freeBoard.size(); i++) {
    		_freeBoard.get(i).setRownum(i+1);
    		chage.put(i+1, _freeBoard.get(i));
    	}
    	_freeBoard.clear();
    	_freeBoard=null;
    	_freeBoard=chage;
    }
    
    public static void anwerListAdd(ContentCommentVO vo) {
    	for (ContentVO temp : _freeBoard.values()) {
    		if (temp.getId() == vo.getBoardId()) {
    			if (temp.getAnswerList() == null) {
    				temp.setAnswerList(new ArrayList<ContentCommentVO>());
    			}
    			temp.getAnswerList().add(vo);
    	    	break;
    		}
    	}
    }
    
    public boolean likeUpdate(ContentVO vo) {
    	StringBuilder sb = new StringBuilder();
    	for (String like : vo.getLikenames()) {
    		if (sb.length() > 0) {
    			sb.append(StringUtil.LineString);
    		}
    		sb.append(like);
    	}
    	Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("UPDATE app_board_content SET likenames=? WHERE id=?");
    		pstm.setString(1, sb.toString());
    		pstm.setInt(2, vo.getId());
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
    
    public boolean commentLikeUpdate(ContentCommentVO vo) {
    	StringBuilder sb = new StringBuilder();
    	for (String like : vo.getLikenames()) {
    		if (sb.length() > 0) {
    			sb.append(StringUtil.LineString);
    		}
    		sb.append(like);
    	}
    	Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("UPDATE app_board_content_comment SET likenames=? WHERE id=?");
    		pstm.setString(1, sb.toString());
    		pstm.setInt(2, vo.getId());
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
    
    public static void release() {
    	for (ContentVO vo : _freeBoard.values()) {
    		vo.getLikenames().clear();
    		if (vo.getAnswerList() != null) {
    			for (ContentCommentVO comment : vo.getAnswerList()) {
        			if (comment.getLikenames() != null) {
        				comment.getLikenames().clear();
        			}
        		}
        		vo.getAnswerList().clear();
    		}
    	}
    	_freeBoard.clear();
    	_contentNotice.clear();
    	_freeBoard = null;
    	_contentNotice = null;
    	_instance = null;
    }
}

