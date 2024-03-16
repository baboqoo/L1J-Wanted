package l1j.server.web.dispatcher.response.guide;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

/**
 * 가이드
 * @author LinOffice
 */
public class GuideDAO {
	private static GuideDAO _instance;
	public static GuideDAO getInstance() {
		if (_instance == null) {
			_instance = new GuideDAO();
		}
		return _instance;
	}
	
	private List<GuideVO> _list;
	private List<GuideRecommendVO> _recommendList;
	private HashMap<Integer, ArrayList<GuideBossVO>> _bossList;
	private int _cnt;
	
	private GuideDAO() {
		_list			= new ArrayList<GuideVO>();
		_recommendList	= new ArrayList<GuideRecommendVO>();
		_bossList		= new HashMap<Integer, ArrayList<GuideBossVO>>();
		load();
	}
	
	private void load() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			
			pstm = con.prepareStatement("SELECT * FROM app_guide ORDER BY id ASC");
			rs = pstm.executeQuery();
			GuideVO guide = null;
			while(rs.next()) {
				int id = rs.getInt("id");
				if (id > _cnt) {
					_cnt = id;
				}
				guide = new GuideVO(id, rs.getString("title"), rs.getString("content"));
				_list.add(guide);
			}
			SQLUtil.close(rs, pstm);
			
			pstm = con.prepareStatement("SELECT * FROM app_guide_recommend ORDER BY id ASC");
			rs = pstm.executeQuery();
			GuideRecommendVO recommend = null;
			while(rs.next()) {
				recommend = new GuideRecommendVO(rs.getInt("id"), rs.getString("title"), rs.getString("content"), rs.getString("url"), rs.getString("img"));
				_recommendList.add(recommend);
			}
			SQLUtil.close(rs, pstm);
			
			pstm = con.prepareStatement("SELECT * FROM app_guide_boss ORDER BY id ASC, loc ASC, number ASC");
			rs = pstm.executeQuery();
			GuideBossVO boss = null;
			while(rs.next()) {
				boss = new GuideBossVO(rs.getInt("id"), rs.getInt("loc"), rs.getString("locName"), rs.getInt("number"), rs.getString("bossName"), 
						rs.getString("bossImg"), rs.getString("spawnLoc"), rs.getString("spawnTime"), rs.getString("dropName"));
				ArrayList<GuideBossVO> list = _bossList.get(boss.getLoc());
				if (list == null) {
					list = new ArrayList<GuideBossVO>();
					_bossList.put(boss.getLoc(), list);
				}
				list.add(boss);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public boolean update(GuideVO vo) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE app_guide SET title=?, content=? WHERE id=?");
			pstm.setString(1, vo.getTitle());
			pstm.setString(2, vo.getContent());
			pstm.setInt(3, vo.getId());
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
	
	public boolean insert(GuideVO vo) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO app_guide SET id=?, title=?, content=?");
			pstm.setInt(1, vo.getId());
			pstm.setString(2, vo.getTitle());
			pstm.setString(3, vo.getContent());
			if (pstm.executeUpdate() > 0) {
				_list.add(vo);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	public boolean delete(GuideVO vo) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM app_guide WHERE id=?");
			pstm.setInt(1, vo.getId());
			if (pstm.executeUpdate() > 0) {
				_list.remove(vo);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	public List<GuideVO> getList(){
		return _list;
	}
	
	public List<GuideRecommendVO> getRecommendList(){
		return _recommendList;
	}
	
	public List<GuideBossVO> getBossList(int loc){
		return _bossList.get(loc);
	}
	
	public HashMap<Integer, ArrayList<GuideBossVO>> getBoss(){
		return _bossList;
	}
	
	public GuideVO getGuide(int id) {
		for (GuideVO vo : _list) {
			if (vo.getId() == id) {
				return vo;
			}
		}
		return null;
	}
	
	public int nextId() {
		return ++_cnt;
	}
	
	public static void reload() {
		release();
		_instance = new GuideDAO();
	}
	
	public static void release() {
		GuideDAO old = _instance;
		old._list.clear();
		old._recommendList.clear();
		for (ArrayList<GuideBossVO> list : old._bossList.values()) {
			list.clear();
		}
		old._bossList.clear();
		old._list = null;
		old._recommendList = null;
		old._bossList = null;
		old = null;
	}
}

