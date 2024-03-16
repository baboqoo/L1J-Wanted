package l1j.server.GameSystem.charactertrade.loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.charactertrade.bean.CharInfo;
import l1j.server.GameSystem.charactertrade.bean.InvenInfo;
import l1j.server.GameSystem.charactertrade.bean.CharacterTradeObject;
import l1j.server.common.data.Gender;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.SkillsInfoTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.serverpackets.S_CharacterTrade;
import l1j.server.server.templates.CharacterSkillInfo;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1PassiveSkills;
import l1j.server.server.templates.L1SkillsInfo;
import l1j.server.server.utils.SQLUtil;

public class CharacterTradeLoader {
	private static final Object 	_lock = new Object();
	private static CharacterTradeLoader _instance;
	public static CharacterTradeLoader getInstance(){
		if (_instance == null) {
			_instance = new CharacterTradeLoader();
		}
		return _instance;
	}
	
	public static void release(){
		if (_instance != null) {
			_instance.store();
			_instance = null;
		}
	}
	
	private CharInfo createCharInfo(ResultSet rs) throws SQLException{
		CharInfo cInfo = new CharInfo();
		if (rs.next()) {
			cInfo.name 			= rs.getString("char_name");
			cInfo.level			= rs.getInt("level");
			cInfo.type			= rs.getInt("type");
			cInfo.gender		= Gender.fromInt(rs.getInt("gender"));
			cInfo.clanName 		= rs.getString("Clanname");
			cInfo.str			= rs.getInt("Str");
			cInfo.dex			= rs.getInt("Dex");
			cInfo.con			= rs.getInt("Con");
			cInfo.wis			= rs.getInt("Wis");
			cInfo.intel			= rs.getInt("Intel");
			cInfo.cha			= rs.getInt("Cha");
			cInfo.elixir		= rs.getInt("ElixirStatus");
			cInfo.hp			= rs.getInt("MaxHp");
			cInfo.mp			= rs.getInt("MaxMp");
			cInfo.ac			= rs.getInt("Ac");
			cInfo.einstate		= rs.getInt("EinCardState");
		}
		return cInfo;
	}
	
	private void setInfo(Connection con, CharacterTradeObject obj){
		PreparedStatement 		pstm	= null;
		ResultSet 				rs		= null;
		CharInfo				cInfo	= null;
		try {
			pstm	= con.prepareStatement("SELECT * FROM characters WHERE objid='" + obj.getCharId() + "'");
			rs		= pstm.executeQuery();
			cInfo	= createCharInfo(rs);
			obj.setCharPck(S_CharacterTrade.getCharacterInfo(cInfo));
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm);
		}
	}
	
	private void setInfo(Connection con, ArrayList<CharacterTradeObject> list){
		PreparedStatement 		pstm	= null;
		ResultSet 				rs		= null;
		CharacterTradeObject	obj		= null;
		CharInfo				cInfo	= null;
		int						size	= list.size();	
		try {
			pstm						= con.prepareStatement("SELECT * FROM characters WHERE objid=?");
			for (int i=0; i<size; i++) {
				obj 						= list.get(i);
				pstm.setInt(1, obj.getCharId());
				rs							= pstm.executeQuery();
				cInfo						= createCharInfo(rs);
				obj.setCharPck(S_CharacterTrade.getCharacterInfo(cInfo));
				SQLUtil.close(rs);
				pstm.clearParameters();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm);
		}
	}
	
	private void setSkills(Connection con, CharacterTradeObject obj){
		PreparedStatement 				pstm	= null;
		ResultSet 						rs		= null;
		ArrayDeque<CharacterSkillInfo> 	spQ		= null;
		L1SkillsInfo					si		= null;
		L1PassiveSkills					ps		= null;
		int								rows	= 0;
		try {
			pstm	= con.prepareStatement("SELECT * FROM character_skills_active WHERE char_obj_id=?");
			pstm.setInt(1, obj.getCharId());
			rs		= pstm.executeQuery();
			rows	= calcRows(rs);
			if (rows > 0) {
				if (spQ == null) {
					spQ = new ArrayDeque<CharacterSkillInfo>();
				}
				while (rs.next()) {
					si	= SkillsInfoTable.getSkillInfo(rs.getInt("skill_id"));
					if (si == null) {
						continue;
					}
					spQ.offer(new CharacterSkillInfo(si.getIcon(), si.getName()));
				}
			}
			SQLUtil.close(rs, pstm);
			
			pstm	= con.prepareStatement("SELECT * FROM character_skills_passive WHERE char_obj_id=?");
			pstm.setInt(1, obj.getCharId());
			rs		= pstm.executeQuery();
			rows	= calcRows(rs);
			if (rows > 0) {
				if (spQ == null) {
					spQ = new ArrayDeque<CharacterSkillInfo>();
				}
				while (rs.next()) {
					ps	= SkillsTable.getPassiveTemplate(rs.getInt("passive_id"));
					if (si == null) {
						continue;
					}
					spQ.offer(new CharacterSkillInfo(ps.getOnIconId(), ps.getName()));
				}
			}
			
			obj.setSpellPck(S_CharacterTrade.getSpellList(spQ));
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm);
		}
	}
	
	private void setSkills(Connection con, ArrayList<CharacterTradeObject> list){
		for (CharacterTradeObject obj : list) {
			setSkills(con, obj);
		}
	}
	
	private ArrayDeque<InvenInfo> createItemQ(ResultSet rs) throws SQLException{
		ArrayDeque<InvenInfo> itemQ 	= null;
		InvenInfo				item	= null;
		int 					rows	= calcRows(rs);
		if (rows > 0) {
			itemQ = new ArrayDeque<InvenInfo>(rows);
			while (rs.next()) {
				item			= new InvenInfo();
				int itemId 		= rs.getInt("item_id");
				L1Item itm 		= ItemTable.getInstance().getTemplate(itemId);
				item.id 		= rs.getInt("id");
				item.bless		= rs.getInt("bless");
				item.count		= rs.getInt("count");
				item.iden		= rs.getInt("is_id");
				item.enchant	= rs.getInt("enchantlvl");
				item.attr		= rs.getInt("attr_enchantlvl");
				item.item		= itm;
				itemQ.offer(item);
			}
		}
		return itemQ;
	}
	
	private void setItems(Connection con, CharacterTradeObject obj){
		PreparedStatement 			pstm	= null;
		ResultSet 					rs		= null;
		ArrayDeque<InvenInfo> 		itemQ	= null;
		try {
			pstm		= con.prepareStatement("SELECT * FROM character_items WHERE char_id='" + obj.getCharId() + "'");
			rs			= pstm.executeQuery();
			itemQ		= createItemQ(rs);
			obj.setInvenPck(S_CharacterTrade.getInvList(itemQ));
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm);
		}
	}
	
	private void setItems(Connection con, ArrayList<CharacterTradeObject> list){
		int 						size 	= list.size();
		PreparedStatement 			pstm	= null;
		ResultSet 					rs		= null;
		CharacterTradeObject		obj		= null;
		ArrayDeque<InvenInfo> itemQ	= null;
		try {
			pstm	= con.prepareStatement("SELECT * FROM character_items WHERE char_id=?");
			for (int i=0; i<size; i++) {
				obj 		= list.get(i);
				pstm.setInt(1, obj.getCharId());
				rs			= pstm.executeQuery();
				itemQ		= createItemQ(rs);
				obj.setInvenPck(S_CharacterTrade.getInvList(itemQ));
				SQLUtil.close(rs);
				pstm.clearParameters();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm);
		}
	}
	
	private HashMap<Integer, CharacterTradeObject> _objs;
	private CharacterTradeLoader(){
		_objs = new HashMap<Integer, CharacterTradeObject>(64);
		ArrayList<CharacterTradeObject> ctl		= new ArrayList<CharacterTradeObject>(256);
		CharacterTradeObject			obj		= null;
		Connection 						con		= null;
		PreparedStatement 				pstm	= null;
		ResultSet 						rs		= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM marble");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				obj = new CharacterTradeObject(rs.getInt("marble_id"), rs.getInt("char_id"), rs.getString("char_name"));
				_objs.put(obj.getMarbleId(), obj);
				ctl.add(obj);
			}
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			setItems(con, ctl);
			setSkills(con, ctl);
			setInfo(con, ctl);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public ArrayList<CharacterTradeObject> getValues(){
		ArrayList<CharacterTradeObject> list	= null;
		synchronized(_lock){
			list = new ArrayList<CharacterTradeObject>(_objs.values());
		}
		return list;
	}
	
	public CharacterTradeObject get(String name){
		ArrayList<CharacterTradeObject> 	list	= getValues();
		CharacterTradeObject 				obj 	= null;
		int 								size	= list.size();
		for (int i=0; i<size; i++) {
			obj = list.get(i);
			if (obj.getCharName().equalsIgnoreCase(name)) {
				return obj;
			}
		}
		return null;
	}
	
	public CharacterTradeObject get(int i){
		synchronized(_lock){
			return _objs.get(i);
		}
	}
	
	public CharacterTradeObject remove(int i){
		if (!_objs.containsKey(i)) {
			return null;
		}
		synchronized(_lock){
			return _objs.remove(i);
		}
	}
	
	public void reset(CharacterTradeObject obj){
		if (obj == null) {
			return;
		}
		synchronized(_lock){
			_objs.put(obj.getMarbleId(), obj);
		}
	}
	
	public void set(CharacterTradeObject obj){
		if (obj == null) {
			return;
		}
		GeneralThreadPool.getInstance().execute(new PckCreator(obj));
		synchronized(_lock){
			_objs.put(obj.getMarbleId(), obj);
		}
	}
	
	private void marbleInsert(Connection con, CharacterTradeObject obj){
		PreparedStatement pstm = null;
		try {
			pstm = con.prepareStatement("INSERT INTO marble SET marble_id='" + obj.getMarbleId() + "', char_id='" + obj.getCharId() + "', char_name='" + obj.getCharName() + "'");
			pstm.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
		}
	}
	
	public void marbleDelete(Connection con, int marbleid){
		PreparedStatement 	pstm	= null;
		try {
			pstm = con.prepareStatement("DELETE FROM marble WHERE marble_id='" + marbleid + "'");
			pstm.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
		}
	}
	
	private void store(){
		if (_objs == null || _objs.size() <= 0) {
			return;
		}
		
		ArrayList<CharacterTradeObject> list 	= new ArrayList<CharacterTradeObject>(_objs.values());
		int 					size 	= list.size();
		Connection 				con		= null;
		PreparedStatement 		pstm	= null;
		CharacterTradeObject			obj		= null;
		try {
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("DELETE FROM marble");
			pstm.execute();
			SQLUtil.close(pstm);
			
			con.setAutoCommit(false);
			pstm	= con.prepareStatement("INSERT INTO marble SET marble_id=?, char_id=?, char_name=? ON DUPLICATE KEY UPDATE char_id=?, char_name=?");
			for (int i=0; i<size; i++) {
				obj = list.get(i);
				if (obj == null) {
					continue;
				}
				pstm.setInt(1, obj.getMarbleId());
				pstm.setInt(2, obj.getCharId());
				pstm.setString(3, obj.getCharName());
				pstm.setInt(4, obj.getCharId());
				pstm.setString(5, obj.getCharName());
				obj.dispose();
				pstm.addBatch();// 메모리상에 올림
				pstm.clearParameters();// 파라미터 재사용을 위해 비움
			}
			pstm.executeBatch();
			pstm.clearBatch();
			con.commit();
		} catch(Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			SQLUtil.close(pstm, con);
		}
		_objs.clear();
	}
	
	private int calcRows(ResultSet rs) throws SQLException{
		rs.last();
		int r = rs.getRow();
		rs.beforeFirst();
		return r;
	}
	
	class PckCreator implements Runnable{
		private CharacterTradeObject obj;
		PckCreator(CharacterTradeObject obj){
			this.obj = obj;
		}

		@Override
		public void run() {
			Connection con = null;
			try {
				con	= L1DatabaseFactory.getInstance().getConnection();
				setItems(con, obj);
				setSkills(con, obj);
				setInfo(con, obj);
				marbleInsert(con, obj);
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(con);
			}
		}
	}
}

