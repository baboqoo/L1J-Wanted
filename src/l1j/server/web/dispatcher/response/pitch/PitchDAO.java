package l1j.server.web.dispatcher.response.pitch;

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
 * 홍보 게시판
 * @author LinOffice
 */
public class PitchDAO {
	private static PitchDAO _instance;
	public static PitchDAO getInstance() {
		if (_instance == null) {
			_instance = new PitchDAO();
		}
		return _instance;
	}
	private int _id;
	
	public static Map<Integer, PitchVO> _freeBoard;
	public static List<PitchVO> _contentNotice = new ArrayList<PitchVO>();
	
	private PitchDAO() {
		if (_freeBoard == null) {
			_freeBoard = new ConcurrentHashMap<Integer, PitchVO>();
		}
		load();
	}
	
	private void load() {
		Connection con = null;
	    PreparedStatement pstm = null;
	    ResultSet rs = null;
	    PreparedStatement answerPstm = null;
	    ResultSet answerRs = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("SELECT @RNUM := @RNUM + 1 AS ROWNUM, t.* FROM (SELECT id, name, title, content, date, chatype, chasex, readcount, mainImg, top, likenames FROM app_board_pitch ORDER BY date DESC, id DESC) t, (SELECT @RNUM := 0) R");
    		rs = pstm.executeQuery();
    		while(rs.next()) {
    			PitchVO vo = new PitchVO();
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
    			
    			answerPstm = con.prepareStatement("SELECT id, boardId, name, chaType, chaSex, date, content, likenames FROM app_board_pitch_comment WHERE boardId=? ORDER BY date ASC");
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
    				PitchCommentVO answer = new PitchCommentVO(answerRs.getInt("id"), answerRs.getInt("boardId"), answerRs.getString("name"), answerRs.getInt("chaType"), Gender.fromInt(answerRs.getInt("chaSex")), answerRs.getTimestamp("date"), answerRs.getString("content"), commentLikeList);
    				if (vo.getAnswerList() == null) {
    					vo.setAnswerList(new ArrayList<PitchCommentVO>());
    				}
    				vo.getAnswerList().add(answer);
    			}
    			if (vo.isTop()) {
    				_contentNotice.add(vo);// 공지사항
    			}
    			_freeBoard.put(vo.getRownum(), vo);
    			if (vo.getId() > _id) {
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
			for (PitchVO board : _freeBoard.values()) {
	    		if (board.getName().equals(charName)) {
	    			vo = new CharacterBoardVO();
					//vo.setBoardType("홍보");
					vo.setBoardType("Promotion");
					vo.setBoard(board);
					vo.setBoardUrl("/pitch/view");
					list.add(vo);
	    		}
	    	}
		} else if (searchType.equals("comment")) {
			for (PitchVO board : _freeBoard.values()) {
				if (board.getAnswerList() == null || board.getAnswerList().isEmpty()) {
					continue;
				}
				for (PitchCommentVO comment : board.getAnswerList()) {
					if (comment.getName().equals(charName)) {
		    			vo = new CharacterBoardVO();
						//vo.setBoardType("홍보");
						vo.setBoardType("Promotion");
						vo.setBoard(board);
						vo.setBoardUrl("/pitch/view");
						list.add(vo);
						break;
		    		}
				}
	    	}
		}
    }
    
    public static List<PitchVO> getList(int start, int end){
    	List<PitchVO> list = new ArrayList<PitchVO>();
    	int cnt=0;
    	for (PitchVO vo : _freeBoard.values()) {
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
    
    public static List<PitchVO> getSearchList(int size, String searchType, String searchText) {// 검색한 리스트가져올때
    	List<PitchVO> list = new ArrayList<PitchVO>();
		for (PitchVO vo : _freeBoard.values()) {
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
    
    public static PitchVO getBoard(int num) {
    	if (_freeBoard.containsKey(num)) {
    		return _freeBoard.get(num);
    	}
    	return null;
    }
    
    public static PitchVO getBoardFromId(int id) {
    	if (_freeBoard == null || _freeBoard.isEmpty()) {
    		return null;
    	}
    	for (PitchVO vo : _freeBoard.values()) {
    		if (vo.getId() == id) {
    			return vo;
    		}
    	}
    	return null;
    }
    
    public static PitchCommentVO getBoardAnser(int num) {
    	PitchCommentVO vo = null;
    	ArrayList<PitchCommentVO> answerList = null;
    	for (PitchVO board : _freeBoard.values()) {
    		answerList = board.getAnswerList();
    		if (answerList != null && !answerList.isEmpty()) {
    			for (PitchCommentVO anwer : answerList) {
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
    	ArrayList<PitchCommentVO> answerList = null;
    	for (PitchVO vo : _freeBoard.values()) {
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
    
    public void insert(PitchVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("INSERT INTO app_board_pitch (id, name, title, content, date, chatype, chasex, readcount, mainImg, top) VALUES (?,?,?,?,?,?,?,?,?,?)");
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
    
    public void insertAnswer(PitchCommentVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("INSERT INTO app_board_pitch_comment (id, boardId, name, chatype, chasex, date, content) VALUES (?,?,?,?,?,?,?)");
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
    
    public void deleteAnswer(PitchCommentVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("DELETE FROM app_board_pitch_comment WHERE id=?");
    		pstm.setInt(1, vo.getId());
    		pstm.execute();
    		if (vo.getLikenames() != null && !vo.getLikenames().isEmpty()) {
    			vo.getLikenames().clear();
        		vo.setLikenames(null);
    		}
    		for (PitchVO board : _freeBoard.values()) {
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
    		pstm = con.prepareStatement("UPDATE app_board_pitch SET readcount=readcount+1 WHERE id=?");
    		PitchVO vo = getBoard(rownum);
    		pstm.setInt(1, vo.getId());
    		pstm.execute();
    		vo.setReadcount(vo.getReadcount()+1);
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public void update(PitchVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("UPDATE app_board_pitch SET title=?, content=?, mainImg=?, top=? WHERE id=?");
    		int index = 0;
    		pstm.setString(++index, vo.getTitle());
    		pstm.setString(++index, vo.getContent());
    		pstm.setString(++index, vo.getMainImg());
    		pstm.setString(++index, String.valueOf(vo.isTop()));
    		pstm.setInt(++index, vo.getId());
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
    
    public void delete(PitchVO vo) {
		Connection con = null;
	    PreparedStatement pstm = null;
		try {
    		con = L1DatabaseFactory.getInstance().getConnection();
    		pstm = con.prepareStatement("DELETE T1, T2 FROM app_board_pitch AS T1 LEFT JOIN app_board_pitch_comment AS T2 ON T1.id = T2.boardId WHERE T1.id=?");
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
    
    public void mapDelete(PitchVO vo) {
    	int deleRownum = vo.getRownum();
    	PitchVO temp = null;
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
    
    public void mapInsert(PitchVO vo) {
    	Map<Integer, PitchVO> chage = new ConcurrentHashMap<Integer, PitchVO>();
    	chage.put(1, vo);
    	for (int i=1; i<=_freeBoard.size(); i++) {
    		_freeBoard.get(i).setRownum(i+1);
    		chage.put(i+1, _freeBoard.get(i));
    	}
    	_freeBoard.clear();
    	_freeBoard=null;
    	_freeBoard=chage;
    }
    
    public static void anwerListAdd(PitchCommentVO vo) {
    	for (PitchVO temp : _freeBoard.values()) {
    		if (temp.getId() == vo.getBoardId()) {
    			if (temp.getAnswerList() == null) {
    				temp.setAnswerList(new ArrayList<PitchCommentVO>());
    			}
    			temp.getAnswerList().add(vo);
    	    	break;
    		}
    	}
    }
    
    public boolean likeUpdate(PitchVO vo) {
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
    		pstm = con.prepareStatement("UPDATE app_board_pitch SET likenames=? WHERE id=?");
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
    
    public boolean commentLikeUpdate(PitchCommentVO vo) {
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
    		pstm = con.prepareStatement("UPDATE app_board_pitch_comment SET likenames=? WHERE id=?");
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
    	for (PitchVO vo : _freeBoard.values()) {
    		vo.getLikenames().clear();
    		if (vo.getAnswerList() != null) {
    			for (PitchCommentVO comment : vo.getAnswerList()) {
        			if (comment.getLikenames() != null) {
        				comment.getLikenames().clear();
        			}
        		}
        		vo.getAnswerList().clear();
    		}
    	}
    	_freeBoard.clear();
    	_contentNotice = null;
    	_freeBoard = null;
    	_contentNotice = null;
    	_instance = null;
    }
}

