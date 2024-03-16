package l1j.server.web.dispatcher.response.board;

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

/**
 * 자유 게시판
 * @author LinOffice
 */
public class BoardDAO {
	private static BoardDAO _instance;
	public static BoardDAO getInstance() {
		if (_instance == null) {
			_instance = new BoardDAO();
		}
		return _instance;
	}
	
	public static Map<Integer, BoardVO> _freeBoard;
	private int _cnt;
	private BoardDAO() {
		if (_freeBoard == null) {
			_freeBoard = new ConcurrentHashMap<Integer, BoardVO>();
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
    		pstm = con.prepareStatement("SELECT @RNUM := @RNUM + 1 AS ROWNUM, t.* FROM (SELECT id, name, title, content, date, chatype, chasex, readcount, mainImg, likenames FROM app_board_free ORDER BY id DESC, date DESC) t, (SELECT @RNUM := 0) R");
    		rs = pstm.executeQuery();
    		while(rs.next()) {
    			BoardVO vo = new BoardVO();
    			vo.setId(rs.getInt("id"));
    			vo.setName(rs.getString("name"));
    			vo.setTitle(rs.getString("title"));
    			vo.setContent(rs.getString("content"));
    			vo.setDate(rs.getTimestamp("date"));
    			vo.setChatype(rs.getInt("chatype"));
    			vo.setChaGender(Gender.fromInt(rs.getInt("chasex")));
    			vo.setReadcount(rs.getInt("readcount"));
    			vo.setMainImg(rs.getString("mainImg"));
    			String text = rs.getString("likenames");
    			ArrayList<String> likeList = new ArrayList<String>();
    			if (text != null && !text.equals("")) {
    				StringTokenizer st = new StringTokenizer(text, StringUtil.LineString);
    				while (st.hasMoreElements()) {
    					likeList.add(st.nextToken());
    				}
    			}
    			vo.setLikenames(likeList);
    			vo.setRownum(rs.getInt("ROWNUM"));
    			
    			answerPstm = con.prepareStatement("SELECT id, boardId, name, chaType, chaSex, date, content, likenames FROM app_board_free_comment WHERE boardId=? ORDER BY date ASC");
    			answerPstm.setInt(1, vo.getId());
    			answerRs = answerPstm.executeQuery();
    			while(answerRs.next()) {
    				String commentText = answerRs.getString("likenames");
    				ArrayList<String> commentLikeList = new ArrayList<String>();
    				if (commentText != null && !commentText.equals("")) {
        				StringTokenizer st = new StringTokenizer(commentText, StringUtil.LineString);
        				while (st.hasMoreElements()) {
        					commentLikeList.add(st.nextToken());
        				}
    				}
    				BoardCommentVO answer = new BoardCommentVO(answerRs.getInt("id"), answerRs.getInt("boardId"), answerRs.getString("name"), answerRs.getInt("chaType"), Gender.fromInt(answerRs.getInt("chaSex")), answerRs.getTimestamp("date"), answerRs.getString("content"), commentLikeList);
    				if (vo.getAnswerList() == null) {
    					vo.setAnswerList(new ArrayList<BoardCommentVO>());
    				}
    				vo.getAnswerList().add(answer);
    			}
    			_freeBoard.put(vo.getRownum(), vo);
    			SQLUtil.close(answerRs, answerPstm);
    			
    			if (vo.getId() > _cnt) {
    				_cnt = vo.getId();
    			}
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
			for (BoardVO board : _freeBoard.values()) {
	    		if (board.getName().equals(charName)) {
	    			vo = new CharacterBoardVO();
					//vo.setBoardType("게시판");
					vo.setBoardType("Bulletin Board");
					vo.setBoard(board);
					vo.setBoardUrl("/board/view");
					list.add(vo);
	    		}
	    	}
		} else if (searchType.equals("comment")) {
			for (BoardVO board : _freeBoard.values()) {
				if (board.getAnswerList() == null || board.getAnswerList().isEmpty()) {
					continue;
				}
				for (BoardCommentVO comment : board.getAnswerList()) {
					if (comment.getName().equals(charName)) {
		    			vo = new CharacterBoardVO();
						//vo.setBoardType("게시판");
						vo.setBoardType("Bulletin Board");
						vo.setBoard(board);
						vo.setBoardUrl("/board/view");
						list.add(vo);
						break;
		    		}
				}
	    	}
		}
    }
    
    public static List<BoardVO> getList(int start, int end){
    	List<BoardVO> list = new ArrayList<BoardVO>();
    	int cnt=0;
    	for (BoardVO vo : _freeBoard.values()) {
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
    
    public static List<BoardVO> getSearchList(int size, String searchType, String searchText) {// 검색한 리스트가져올때
		List<BoardVO> list = new ArrayList<BoardVO>();
		for (BoardVO vo : _freeBoard.values()) {
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
    
    public static BoardVO getBoard(int num) {
    	if (_freeBoard.containsKey(num)) {
    		return _freeBoard.get(num);
    	}
    	return null;
    }
    
    public static BoardVO getBoardFromId(int id) {
    	if (_freeBoard == null || _freeBoard.isEmpty()) {
    		return null;
    	}
    	for (BoardVO vo : _freeBoard.values()) {
    		if (vo.getId() == id) {
    			return vo;
    		}
    	}
    	return null;
    }
    
    public static BoardCommentVO getBoardAnser(int num) {
    	BoardCommentVO vo = null;
    	ArrayList<BoardCommentVO> answerList = null;
    	for (BoardVO board : _freeBoard.values()) {
    		answerList = board.getAnswerList();
    		if (answerList != null && !answerList.isEmpty()) {
    			for (BoardCommentVO anwer : answerList) {
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
		return ++_cnt;
	}
    
    public static int getNextAnswerNum() {
    	int num = 0;
    	ArrayList<BoardCommentVO> answerList = null;
    	for (BoardVO vo : _freeBoard.values()) {
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
    
    public void insert(BoardVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("INSERT INTO app_board_free (id, name, title, content, date, chatype, chasex, readcount, mainImg) VALUES (?,?,?,?,?,?,?,?,?)");
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
    		pstm.executeUpdate();
    		mapInsert(vo);
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public void insertAnswer(BoardCommentVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("INSERT INTO app_board_free_comment (id, boardId, name, chatype, chasex, date, content) VALUES (?,?,?,?,?,?,?)");
    		int index = 0;
    		pstm.setInt(++index, vo.getId());
    		pstm.setInt(++index, vo.getBoardId());
    		pstm.setString(++index, vo.getName());
    		pstm.setInt(++index, vo.getChatype());
    		pstm.setInt(++index, vo.getChaGender().toInt());
    		pstm.setTimestamp(++index, vo.getDate());
    		pstm.setString(++index, vo.getContent());
    		pstm.executeUpdate();
    		anwerListAdd(vo);
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public void deleteAnswer(BoardCommentVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("DELETE FROM app_board_free_comment WHERE id=?");
    		pstm.setInt(1, vo.getId());
    		pstm.executeUpdate();
    		if (vo.getLikenames() != null && !vo.getLikenames().isEmpty()) {
    			vo.getLikenames().clear();
        		vo.setLikenames(null);
    		}
    		for (BoardVO board : _freeBoard.values()) {
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
    		pstm = con.prepareStatement("UPDATE app_board_free SET readcount=readcount+1 WHERE id=?");
    		BoardVO vo = getBoard(rownum);
    		pstm.setInt(1, vo.getId());
    		pstm.executeUpdate();
    		vo.setReadcount(vo.getReadcount()+1);
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public void update(BoardVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("UPDATE app_board_free SET title=?, content=?, mainImg=? WHERE id=?");
    		pstm.setString(1, vo.getTitle());
    		pstm.setString(2, vo.getContent());
    		pstm.setString(3, vo.getMainImg());
    		pstm.setInt(4, vo.getId());
    		pstm.executeUpdate();
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public void delete(BoardVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("DELETE T1, T2 FROM app_board_free AS T1 LEFT JOIN app_board_free_comment AS T2 ON T1.id = T2.boardId WHERE T1.id=?");
    		pstm.setInt(1, vo.getId());
    		pstm.executeUpdate();
    		SQLUtil.close(pstm);
    		mapDelete(vo);
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public void mapDelete(BoardVO vo) {
    	int deleRownum = vo.getRownum();
    	BoardVO temp = null;
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
    
    public void mapInsert(BoardVO vo) {
    	Map<Integer, BoardVO> chage = new ConcurrentHashMap<Integer, BoardVO>();
    	chage.put(1, vo);
    	for (int i=1; i<=_freeBoard.size(); i++) {
    		_freeBoard.get(i).setRownum(i+1);
    		chage.put(i+1, _freeBoard.get(i));
    	}
    	_freeBoard.clear();
    	_freeBoard=null;
    	_freeBoard=chage;
    }
    
    public static void anwerListAdd(BoardCommentVO vo) {
    	for (BoardVO temp : _freeBoard.values()) {
    		if (temp.getId() == vo.getBoardId()) {
    			if (temp.getAnswerList() == null) {
    				temp.setAnswerList(new ArrayList<BoardCommentVO>());
    			}
    			temp.getAnswerList().add(vo);
    	    	break;
    		}
    	}
    }
    
    public boolean likeUpdate(BoardVO vo) {
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
    		pstm = con.prepareStatement("UPDATE app_board_free SET likenames=? WHERE id=?");
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
    
    public boolean commentLikeUpdate(BoardCommentVO vo) {
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
    		pstm = con.prepareStatement("UPDATE app_board_free_comment SET likenames=? WHERE id=?");
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
    	for (BoardVO vo : _freeBoard.values()) {
    		vo.getLikenames().clear();
    		if (vo.getAnswerList() != null) {
    			for (BoardCommentVO comment : vo.getAnswerList()) {
        			if (comment.getLikenames() != null) {
        				comment.getLikenames().clear();
        			}
        		}
        		vo.getAnswerList().clear();
    		}
    	}
    	_freeBoard.clear();
    	_freeBoard = null;
    	_instance = null;
    }
}

