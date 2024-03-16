package l1j.server.server.templates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.IdFactory;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Bookmarks;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class L1BookMark {
    private static Logger _log = Logger.getLogger(L1BookMark.class.getName());

    private int _charId;
    private int _id;
    private String _name;
    private int _locX;
    private int _locY;
    private short _mapId;
    private int _randomX;
    private int _randomY;
    private int _speed_id;

    public int getSpeed_id() {
       return _speed_id;
    }
    public void setSpeed_id(int i) {
        _speed_id = i;
    }
    public int getId() {
        return _id;
    }
    public void setId(int i) {
        _id = i;
    }
    public int getCharId() {
        return _charId;
    }
    private int _NumId;

    public int getNumId() {
        return _NumId;
    }

    public void setNumId(int i) {
        _NumId = i;
    }

    private int _Temp_id;

    public int getTemp_id() {
        return _Temp_id;
    }

    public void setTemp_id(int i) {
        _Temp_id = i;
    }

    public void setCharId(int i) {
        _charId = i;
    }

    public String getName() {
        return _name;
    }

    public void setName(String s) {
        _name = s;
    }

    public int getLocX() {
        return _locX;
    }

    public void setLocX(int i) {
        _locX = i;
    }

    public int getLocY() {
        return _locY;
    }

    public void setLocY(int i) {
        _locY = i;
    }

    public short getMapId() {
        return _mapId;
    }

    public void setMapId(short i) {
        _mapId = i;
    }

    public int getRandomX() {
        return _randomX;
    }

    public void setRandomX(int i) {
        _randomX = i;
    }

    public int getRandomY() {
        return _randomY;
    }

    public void setRandomY(int i) {
        _randomY = i;
    }

    public L1BookMark() {
    }

   public static void bookmarkDB(L1PcInstance pc) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        L1BookMark bookmark = null;
        try {
        	con = L1DatabaseFactory.getInstance().getConnection();
        	pstm = con.prepareStatement("SELECT * FROM character_teleport WHERE char_id='" + pc.getId() + "' ORDER BY num_id ASC");
        	rs = pstm.executeQuery();
        	int i = 0;
        	while (rs.next()) {
        		bookmark = new L1BookMark();
        		bookmark.setCharId(rs.getInt("char_id"));
        		bookmark.setId(rs.getInt("id"));
        		bookmark.setNumId(i);
        		bookmark.setTemp_id(i);
        		bookmark.setSpeed_id(rs.getInt("speed_id"));
        		bookmark.setName(rs.getString("name"));
        		bookmark.setLocX(rs.getInt("locx"));
        		bookmark.setLocY(rs.getInt("locy"));
        		bookmark.setMapId(rs.getShort("mapid"));
        		bookmark.setRandomX(rs.getShort("randomX"));
        		bookmark.setRandomY(rs.getShort("randomY"));
        		pc.addBookMark(bookmark);
        		i++;
        	}
        	SQLUtil.close(rs, pstm);
        	
        	pstm = con.prepareStatement("SELECT * FROM character_teleport WHERE char_id='" + pc.getId() + "' AND speed_id > -1 ORDER BY speed_id ASC");
            rs = pstm.executeQuery();
            while (rs.next()) {
            	pc._speedbookmarks.add(pc.getBookMark().get(rs.getInt("num_id")));
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        } finally {
			SQLUtil.close(rs, pstm, con);
       }
    }
    
    public static void deleteBookmark(L1PcInstance pc, String s) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            L1BookMark book = pc.getBookMark(s);
            if (book != null) {
                con = L1DatabaseFactory.getInstance().getConnection();
                pstm = con.prepareStatement("DELETE FROM character_teleport WHERE id=? AND char_id=?");
                pstm.setInt(1, book.getId());
                pstm.setInt(2, pc.getId());
                pstm.execute();
                int del_id = book.getNumId();
                for (L1BookMark books : pc.getBookMarkArray()) {
                    if (book == books) {
                    	pc.removeBookMark(book);
                    }
                    if (books.getNumId() > del_id){
                        books.setNumId(books.getNumId() - 1);
                        books.setTemp_id(books.getTemp_id() - 1);
                    }
                }
                if (pc._speedbookmarks.contains(book)) {
                    del_id = book.getSpeed_id();
                    for (L1BookMark books : pc.getSpeedBookMarkArray()) {
                        if (book == books) {
                        	pc._speedbookmarks.remove(book);
                        }
                        if (books.getSpeed_id() > del_id) {
                        	books.setSpeed_id(books.getSpeed_id() - 1);
                        }
                    }
                }

                WriteBookmark(pc);
                pc.getBookMark().clear();
                pc._speedbookmarks.clear();
                bookmarkDB(pc);
           }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	SQLUtil.close(pstm, con);
        }
    }

    public static synchronized void addBookmark(L1PcInstance pc, String s) {
        if (!pc.getMap().isMarkable() && !pc.isGm()) {
            pc.sendPackets(L1ServerMessage.sm214); // \f1여기를 기억할 수가 없습니다.
            return;
        }
        if (pc.getBookMarkSize() >= pc.getBookMarkCount()) {
        	pc.sendPackets(pc.getBookMarkCount() == 100 ? L1ServerMessage.sm2930 : L1ServerMessage.sm676);
            return;
        }
        if (pc.getBookMark(s) == null) {
            Connection con = null;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            try {
                con = L1DatabaseFactory.getInstance().getConnection();
                
                pstm = con.prepareStatement("SELECT A.newid, B.numid FROM (SELECT max(id)+1 as newid FROM character_teleport) A, (SELECT max(num_id)+1 as numid FROM character_teleport WHERE char_id=?) B");
                pstm.setInt(1, pc.getId());
                rs = pstm.executeQuery();
                rs.next();
                int id = rs.getInt("newid");
                int Numid = rs.getInt("numid");
                L1BookMark bookmark = new L1BookMark();
                bookmark.setId(id);
                bookmark.setNumId(Numid);
                bookmark.setTemp_id(Numid);
                bookmark.setSpeed_id(-1);
                bookmark.setCharId(pc.getId());
                bookmark.setName(s);
                bookmark.setLocX(pc.getX());
                bookmark.setLocY(pc.getY());
                bookmark.setMapId(pc.getMapId());
                SQLUtil.close(rs);
            	SQLUtil.close(pstm);
            	
                pstm = con.prepareStatement("INSERT INTO character_teleport SET id=?, num_id=?, speed_id=?, char_id=?, name=?, locx=?, locy=?, mapid=?");
                pstm.setInt(1, bookmark.getId());
                pstm.setInt(2, bookmark.getNumId());
                pstm.setInt(3, bookmark.getSpeed_id());
                pstm.setInt(4, bookmark.getCharId());
                pstm.setString(5, bookmark.getName());
                pstm.setInt(6, bookmark.getLocX());
                pstm.setInt(7, bookmark.getLocY());
                pstm.setInt(8, bookmark.getMapId());
                pstm.execute();
                pc.addBookMark(bookmark);
                pc.sendPackets(new S_Bookmarks(s, bookmark.getMapId(), bookmark.getLocX(), bookmark.getLocY(), bookmark.getNumId()), true);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
            	SQLUtil.close(rs, pstm, con);
            }
        } else pc.sendPackets(L1ServerMessage.sm1655);// 같은 기억명으로 저장할 수 없습니다.
    }

    public static void WriteBookmark(L1PcInstance pc) {
    	int size = pc.getBookMarkSize();
    	if (size <= 0) {
    		return;
    	}
        Connection con			= null;
        PreparedStatement pstm	= null;
        ResultSet rs			= null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            con.setAutoCommit(false);
            
            pstm = con.prepareStatement("UPDATE character_teleport SET num_id=?, speed_id=?  WHERE id=?");
            for (int i = 0; i < size; i++) {
            	pstm.setInt(1, pc.getBookMark().get(i).getTemp_id());
            	if (pc._speedbookmarks.contains(pc.getBookMark().get(i))) {
            		pstm.setInt(2, pc.getBookMark().get(i).getSpeed_id());
            	} else {
            		pstm.setInt(2, -1);
            	}
            	pstm.setInt(3, pc.getBookMark().get(i).getId());
            	pstm.addBatch();
				pstm.clearParameters();
            }
            
            pstm.executeBatch();
			pstm.clearBatch();
			con.commit();
        } catch (SQLException e) {
        	try {
				con.rollback();
			} catch(SQLException sqle){
				_log.log(Level.WARNING, "WriteBookmark exception occurred.", sqle);
			}
        	_log.log(Level.WARNING, "WriteBookmark exception occurred.", e);
        } catch (Exception e) {
        	try {
				con.rollback();
			} catch(SQLException sqle){
				_log.log(Level.WARNING, "WriteBookmark exception occurred.", sqle);
			}
        	_log.log(Level.WARNING, "WriteBookmark exception occurred.", e);
        } finally {
        	try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				_log.log(Level.WARNING, "WriteBookmark exception occurred.", e);
			}
            SQLUtil.close(rs, pstm, con);
       }
    }
    
    public synchronized static void Bookmarkitem(L1PcInstance pc, L1ItemInstance useItem, int obj_id, boolean isChange) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1BookMark bookmark = null;
		ArrayList<L1BookMark> new_list = new ArrayList<L1BookMark>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			if (obj_id == 700024 || obj_id == 700025) {
				pstm = con.prepareStatement("SELECT * FROM beginner_addteleport WHERE item_obj_id='" + obj_id + "' ORDER BY num_id ASC");
				rs = pstm.executeQuery();
				while (rs.next()) {
					bookmark = new L1BookMark();
					bookmark.setId(rs.getInt("id"));
					bookmark.setNumId(rs.getInt("num_id"));
					bookmark.setSpeed_id(-1);
					bookmark.setCharId(pc.getId());
					bookmark.setName(rs.getString("name"));
					bookmark.setLocX(rs.getInt("locx"));
					bookmark.setLocY(rs.getInt("locy"));
					bookmark.setMapId(rs.getShort("mapid"));
					bookmark.setRandomX(rs.getInt("randomX"));
					bookmark.setRandomY(rs.getInt("randomY"));

					new_list.add(bookmark);
				}
			} else {
				pstm = con.prepareStatement("SELECT * FROM item_bookmark WHERE item_id='" + obj_id + "' ORDER BY book_id ASC");
				rs = pstm.executeQuery();
				while (rs.next()) {
					bookmark = new L1BookMark();
					bookmark.setId(rs.getInt("id"));
					bookmark.setNumId(rs.getInt("book_id"));
					bookmark.setSpeed_id(-1);
					bookmark.setCharId(pc.getId());
					bookmark.setName(rs.getString("name"));
					bookmark.setLocX(rs.getInt("locx"));
					bookmark.setLocY(rs.getInt("locy"));
					bookmark.setMapId(rs.getShort("mapid"));
					bookmark.setRandomX(0);
					bookmark.setRandomY(0);

					new_list.add(bookmark);
				}
			}
			SQLUtil.close(rs);
			SQLUtil.close(pstm);

			if (pc.getBookMarkSize() + new_list.size() > pc.getBookMarkCount()) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("기억 확장 필요: 저장할 공간이 없습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1157), true), true);
				return;
			}
			
			for (L1BookMark book : new_list) {
				String sql = "INSERT INTO character_teleport (num_id,speed_id,char_id,name,locx,locy,mapid,randomX,randomY) VALUES (?, ?, ?, ?,?,?,?,?,?) ";
				SQLUtil.execute(con, sql,
						new Object[] {  Integer.valueOf(book.getNumId()), Integer.valueOf(book.getSpeed_id()),
								Integer.valueOf(book.getCharId()), book.getName(), Integer.valueOf(book.getLocX()),
								Integer.valueOf(book.getLocY()), Integer.valueOf(book.getMapId()), Integer.valueOf(book.getRandomX()),
								Integer.valueOf(book.getRandomY())});
				
				pc.addBookMark(book);
				pc.sendPackets(new S_Bookmarks(book.getName(), book.getMapId(), book.getLocX(), book.getLocY(), book.getId()), true);
			}

			pc.getInventory().removeItem(useItem, 1);
		} catch (SQLException e) {
			_log.log(Level.WARNING, "Bookmarkitem exception occurred.", e);
		} finally {
			bookmark = null;
			SQLUtil.close(rs, pstm, con);
		}
	}
    
    public static ArrayList<L1BookMark> ShowBookmarkitem(L1PcInstance pc, int obj_id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1BookMark bookmark = null;
		ArrayList<L1BookMark> new_list = new ArrayList<L1BookMark>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			if (obj_id == 700024 || obj_id == 700025) {
				pstm = con.prepareStatement("SELECT * FROM beginner_addteleport WHERE item_obj_id='" + obj_id + "' ORDER BY num_id ASC");
				rs = pstm.executeQuery();
				while (rs.next()) {
					bookmark = new L1BookMark();
					bookmark.setId(rs.getInt("id"));
					bookmark.setNumId(rs.getInt("num_id"));
					bookmark.setSpeed_id(-1);
					bookmark.setCharId(pc.getId());
					bookmark.setName(rs.getString("name"));
					bookmark.setLocX(rs.getInt("locx"));
					bookmark.setLocY(rs.getInt("locy"));
					bookmark.setMapId(rs.getShort("mapid"));
					bookmark.setRandomX(rs.getInt("randomX"));
					bookmark.setRandomY(rs.getInt("randomY"));

					new_list.add(bookmark);
				}
			} else {
				pstm = con.prepareStatement("SELECT * FROM item_bookmark WHERE item_id='" + obj_id + "' ORDER BY book_id ASC");
				rs = pstm.executeQuery();
				while (rs.next()) {
					bookmark = new L1BookMark();
					bookmark.setId(rs.getInt("id"));
					bookmark.setNumId(rs.getInt("book_id"));
					bookmark.setName(rs.getString("name"));
					bookmark.setLocX(rs.getInt("locx"));
					bookmark.setLocY(rs.getInt("locy"));
					bookmark.setMapId(rs.getShort("mapid"));

					new_list.add(bookmark);
				}
			}
			return new_list;
		} catch (SQLException e) {
			_log.log(Level.WARNING, "ShowBookmarkitem exception occurred.", e);
		} finally {
			bookmark = null;
			SQLUtil.close(rs, pstm, con);
		}
		return new_list;
	}

    public static void deleteBookmarkItem(int obj_id) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("DELETE FROM character_teleport WHERE item_obj_id=?");
            pstm.setInt(1, obj_id);
            pstm.execute();
        } catch (SQLException e) {
        	e.printStackTrace();
        } finally {
            SQLUtil.close(pstm, con);
        }
    }

    public synchronized static void MakeBookmarkItem(L1PcInstance pc, L1ItemInstance item) {
        int size = pc._speedbookmarks.size();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT max(id)+1 as newid FROM character_teleport");
            rs = pstm.executeQuery();
            rs.next();
            int Numid = rs.getInt("newid");
            for (int i = 0; i < size; i++) {
                pstm = con.prepareStatement("INSERT INTO character_teleport SET id=?,num_id=?,speed_id=?, char_id=?, name=?, locx=?, locy=?, mapid=?,randomX=?,randomY=?,item_obj_id=?");
                pstm.setInt(1, Numid++);
                pstm.setInt(2, i);
                pstm.setInt(3, -1);
                pstm.setInt(4, 0);
                pstm.setString(5, pc._speedbookmarks.get(i).getName());
                pstm.setInt(6, pc._speedbookmarks.get(i).getLocX());
                pstm.setInt(7, pc._speedbookmarks.get(i).getLocY());
                pstm.setInt(8, pc._speedbookmarks.get(i).getMapId());
                pstm.setInt(9, pc._speedbookmarks.get(i).getRandomX());
                pstm.setInt(10, pc._speedbookmarks.get(i).getRandomY());
                pstm.setInt(11, item.getId());
                pstm.execute();
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
    }
    
    @SuppressWarnings("finally")
	public static synchronized int ItemBookmarkChehck(int itemid) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int count = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT COUNT(*) AS cnt FROM item_bookmark WHERE item_id = ?");
			pstm.setInt(1, itemid);
			rs = pstm.executeQuery();
			if (rs.next()) {
				count = rs.getInt("cnt");
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "An error occurred while adding a bookmark.", e);
		} finally {
			SQLUtil.close(rs, pstm, con);
			return count;
		}
	}
    
    public static synchronized boolean ItemBookmarkLoad(L1PcInstance pc, int itemid) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			try {
				pstm = con.prepareStatement("SELECT * FROM item_bookmark WHERE item_id = ?");
				pstm.setInt(1, itemid);
				rs = pstm.executeQuery();
				while(rs.next()){
					try {
						String name = rs.getString("name");
						if (pc.getBookMark(name) != null) {
//AUTO SRM: 							pc.sendPackets(new S_SystemMessage("추가하려는 기억중에 현재 기억창에 중복되는 '기억이름'이 있습니다. > " + name), true); // CHECKED OK
							pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1158) + name, true), true);
							return false;
						}
					} catch (Exception e) {
					}
				}
			} catch (SQLException e) {
				_log.log(Level.SEVERE, "An error occurred while adding a bookmark.", e);
			} finally {
				SQLUtil.close(rs, pstm);
			}
			try {
				pstm = con.prepareStatement("SELECT * FROM item_bookmark WHERE item_id = ? ORDER BY id ASC");
				pstm.setInt(1, itemid);
				rs = pstm.executeQuery();
				while(rs.next()){
					try {
						int id = IdFactory.getInstance().nextId();
						L1BookMark bookmark = new L1BookMark();
						bookmark.setId(id);
						bookmark.setCharId(pc.getId());
						bookmark.setName(rs.getString("name"));
						bookmark.setLocX(rs.getInt("locx"));
						bookmark.setLocY(rs.getInt("locy"));
						bookmark.setMapId(rs.getShort("mapid"));
						insertBookmark(bookmark);
						pc.addBookMark(bookmark);
						pc.sendPackets(new S_Bookmarks(bookmark.getName(), bookmark.getLocX(), bookmark.getLocY(), bookmark.getMapId(), bookmark.getId()), true);
					} catch (Exception e) {
					}
				}
			} catch (SQLException e) {
				_log.log(Level.SEVERE, "An error occurred while adding a bookmark.", e);
			} finally {
				SQLUtil.close(rs, pstm);
			}
			try {
				pstm = con.prepareStatement("DELETE FROM item_bookmark WHERE item_id=?");
				pstm.setInt(1, itemid);
				pstm.executeUpdate();
			} catch (SQLException e) {
				_log.log(Level.SEVERE, "An error occurred due to bookmark deletion.", e);
			} finally {
				SQLUtil.close(pstm);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "A bookmark error occurred.", e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return true;
	}
    
    public static void insertBookmark(L1BookMark bookmark) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO character_teleport SET id = ?, char_id = ?, name = ?, locx = ?, locy = ?, mapid = ?, randomX = ?, randomY = ?");
			pstm.setInt(1, bookmark.getId());
			pstm.setInt(2, bookmark.getCharId());
			pstm.setString(3, bookmark.getName());
			pstm.setInt(4, bookmark.getLocX());
			pstm.setInt(5, bookmark.getLocY());
			pstm.setInt(6, bookmark.getMapId());
			pstm.setInt(7, 0);
			pstm.setInt(8, 0);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "An error occurred while adding a bookmark.", e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
    
    public static synchronized void ItemaddBookmarkByTeleport(L1PcInstance pc, int itemid) {
		int size = pc.getBookMarkSize();
		if (size <= 0) {
			return;
		}
		FastTable<L1BookMark> list = new FastTable<L1BookMark>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_teleport WHERE char_id=? ORDER BY id ASC");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();

			L1BookMark bookmark = null;
			while(rs.next()){
				bookmark = new L1BookMark();
				bookmark.setId(rs.getInt("id"));
				bookmark.setCharId(rs.getInt("char_id"));
				bookmark.setName(rs.getString("name"));
				bookmark.setLocX(rs.getInt("locx"));
				bookmark.setLocY(rs.getInt("locy"));
				bookmark.setMapId(rs.getShort("mapid"));
				bookmark.setRandomX(rs.getShort("randomX"));
				bookmark.setRandomY(rs.getShort("randomY"));
				list.add(bookmark);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}

		if (list.size() > 0) {
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				int count = 0;
				for(L1BookMark book : list){
					if(count >= pc.getBookMarkCount())continue;
					pstm = con.prepareStatement("INSERT INTO item_bookmark SET book_id = ?, item_id=?, name = ?, locx = ?, locy = ?, mapid = ?");
					pstm.setInt(1, book.getId());
					pstm.setInt(2, itemid);
					pstm.setString(3, book.getName());
					pstm.setInt(4, book.getLocX());
					pstm.setInt(5, book.getLocY());
					pstm.setInt(6, book.getMapId());
					pstm.executeUpdate();
					count++;
					SQLUtil.close(pstm);
				}
			} catch (SQLException e) {
				_log.log(Level.SEVERE, "An error occurred while adding a bookmark.", e);
			} finally {
				SQLUtil.close(pstm, con);
			}
		}
		list.clear();
		list = null;
	}
    
}

