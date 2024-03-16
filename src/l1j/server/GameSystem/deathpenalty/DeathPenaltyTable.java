package l1j.server.GameSystem.deathpenalty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.deathpenalty.bean.DeathPenaltyExpObject;
import l1j.server.GameSystem.deathpenalty.bean.DeathPenaltyItemObject;
import l1j.server.GameSystem.deathpenalty.bean.DeathPenaltyObject;
import l1j.server.GameSystem.deathpenalty.user.DeathPenaltyUser;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.MagicDollInfoTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.exp.L1ExpPlayer;
import l1j.server.server.monitor.Logger.DeathPenaltyType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.inventory.S_DeathPenaltyRecoveryExpListNoti;
import l1j.server.server.serverpackets.inventory.S_DeathPenaltyRecoveryItemListNoti;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

/**
 * 사망 패널티 정보
 * @author LinOffice
 */
public class DeathPenaltyTable {
	private static Logger _log = Logger.getLogger(DeathPenaltyTable.class.getName());
	private static class newInstance {
		public static final DeathPenaltyTable INSTANCE = new DeathPenaltyTable();
	}
	public static DeathPenaltyTable getInstance(){
		return newInstance.INSTANCE;
	}
	
	private static final ConcurrentHashMap<Integer, DeathPenaltyUser> EXP_INFO	= new ConcurrentHashMap<>();// 모든 경험치 손실 정보
	private static final ConcurrentHashMap<Integer, DeathPenaltyUser> ITEM_INFO	= new ConcurrentHashMap<>();// 모든 아이템 손실 정보
	private static final ConcurrentHashMap<Integer, Integer> ITEM_RECOVERY_COST	= new ConcurrentHashMap<>();// 아이템 복구 가격 정보
	
	/**
	 * 로그인 시 패널티 정보 설정 및 출력
	 * @param pc
	 */
	public static void login(L1PcInstance pc){
		DeathPenaltyUser ext_penalty = EXP_INFO.get(pc.getId());
		if (ext_penalty == null) {
			ext_penalty = new DeathPenaltyUser();
			EXP_INFO.put(pc.getId(), ext_penalty);
		}
		DeathPenaltyUser item_penalty = ITEM_INFO.get(pc.getId());
		if (item_penalty == null) {
			item_penalty = new DeathPenaltyUser();
			ITEM_INFO.put(pc.getId(), item_penalty);
		}
		
		pc.set_penalty_exp(ext_penalty);
		pc.set_penalty_item(item_penalty);
		pc.sendPackets(new S_DeathPenaltyRecoveryExpListNoti(ext_penalty.get_list()), true);
		pc.sendPackets(new S_DeathPenaltyRecoveryItemListNoti(pc, item_penalty.get_list()), true);
	}
	
	private DeathPenaltyTable(){
		load();
	}
	
	void load(){
		Connection con				= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		PreparedStatement tempPstm	= null;
		ResultSet tempRs			= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			
			// 24시간이 지난 목록 삭제(프로시저 호출)
			pstm = con.prepareStatement("CALL DELETE_DEATH_PENALTY_24_HOUR()");
			pstm.executeUpdate();
			SQLUtil.close(pstm);
			
			// 경험치 정보 load
			pstm = con.prepareStatement("SELECT DISTINCT(char_id) FROM character_death_exp");
			rs = pstm.executeQuery();
			while(rs.next()){
				try {
					int char_id = rs.getInt("char_id");
					tempPstm = con.prepareStatement("SELECT T.* FROM (SELECT * FROM character_death_exp WHERE char_id = ? ORDER BY delete_time DESC LIMIT ?) T ORDER BY T.delete_time ASC");
					tempPstm.setInt(1, char_id);
					tempPstm.setInt(2, Config.PENALTY.REPAIR_STORAGE_LIMIT_SIZE);
					tempRs = tempPstm.executeQuery();
					DeathPenaltyUser penalty = new DeathPenaltyUser();
					while(tempRs.next()){
						int death_level = tempRs.getInt("death_level");
						penalty.add(new DeathPenaltyExpObject(char_id, tempRs.getTimestamp("delete_time"), 
								death_level, getLostExpRatio(death_level), tempRs.getInt("exp_value"), tempRs.getInt("recovery_cost")));
					}
					EXP_INFO.put(char_id, penalty);
				} catch(SQLException e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				} finally {
					SQLUtil.close(tempRs, tempPstm);
				}
			}
			SQLUtil.close(rs, pstm);
			
			// 아이템 정보 load
			pstm = con.prepareStatement("SELECT DISTINCT(char_id) FROM character_death_item");
			rs = pstm.executeQuery();
			while(rs.next()){
				try {
					int char_id = rs.getInt("char_id");
					tempPstm = con.prepareStatement("SELECT T.* FROM (SELECT * FROM character_death_item WHERE char_id = ? ORDER BY delete_time DESC LIMIT ?) T ORDER BY T.delete_time ASC");
					tempPstm.setInt(1, char_id);
					tempPstm.setInt(2, Config.PENALTY.REPAIR_STORAGE_LIMIT_SIZE);
					tempRs = tempPstm.executeQuery();
					DeathPenaltyUser penalty = new DeathPenaltyUser();
					while(tempRs.next()){
						int db_id	= tempRs.getInt("db_id");
						int itemId	= tempRs.getInt("itemId");
						L1ItemInstance recovery_item = ItemTable.getInstance().createItem(itemId, db_id);
						recovery_item.setCount(tempRs.getInt("count"));
						recovery_item.setEnchantLevel(tempRs.getInt("enchant"));
						recovery_item.setIdentified(Boolean.valueOf(tempRs.getString("identi")));
						recovery_item.setChargeCount(tempRs.getInt("chargeCount"));
						recovery_item.setBless(tempRs.getInt("bless"));
						recovery_item.setAttrEnchantLevel(tempRs.getInt("attrEnchant"));
						recovery_item.setSpecialEnchant(tempRs.getInt("specialEnchant"));
						recovery_item.setPotential(MagicDollInfoTable.getPotential(tempRs.getInt("potential_id")));
						
						int slot_first	= tempRs.getInt("slot_first");
						int slot_second	= tempRs.getInt("slot_second");
						if (slot_first > 0) {
							recovery_item.insertSlot(0, ItemTable.getSmelting(slot_first));
						}
						if (slot_second > 0) {
							recovery_item.insertSlot(1, ItemTable.getSmelting(slot_second));
						}
						penalty.add(new DeathPenaltyItemObject(char_id, tempRs.getTimestamp("delete_time"), 
								recovery_item, tempRs.getInt("recovery_cost")));
					}
					ITEM_INFO.put(char_id, penalty);
				} catch(SQLException e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				} finally {
					SQLUtil.close(tempRs, tempPstm);
				}
			}
			SQLUtil.close(rs, pstm);
			
			// 아이템 복구 가격 정보 load
			pstm = con.prepareStatement("SELECT itemId, cost FROM repair_item_cost");
			rs = pstm.executeQuery();
			while(rs.next()){
				ITEM_RECOVERY_COST.put(rs.getInt("itemId"), rs.getInt("cost"));
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(tempRs, tempPstm);
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private static final String EXP_INSERT_QUERY	= "INSERT INTO character_death_exp SET char_id=?, delete_time=?, death_level=?, exp_value=?, recovery_cost=?";
	private static final String ITEM_INSERT_QUERY	= "INSERT INTO character_death_item SET char_id=?, delete_time=?, "
			+ "db_id=?, itemId=?, count=?, enchant=?, identi=?, chargeCount=?, bless=?, attrEnchant=?, specialEnchant=?, potential_id=?, slot_first=?, slot_second=?, recovery_cost=?";
	
	private static final String EXP_DELETE_QUERY	= "DELETE FROM character_death_exp WHERE char_id=? AND delete_time=?";
	private static final String ITEM_DELETE_QUERY	= "DELETE FROM character_death_item WHERE db_id=?";
	
	/**
	 * 경험치 손실 정보 추가
	 * @param pc
	 * @param exp_value
	 * @param death_level
	 */
	public void insertExp(L1PcInstance pc, int exp_value, int death_level){
		int recovery_cost			= (int)((death_level * death_level * 150) * Config.PENALTY.REPAIR_EXP_COST_RATE);
		Timestamp delete_time		= new Timestamp(System.currentTimeMillis() + (Config.PENALTY.REPAIR_LIMIT_TIME_SECOND * 1000));
		DeathPenaltyExpObject obj	= new DeathPenaltyExpObject(pc.getId(), delete_time, death_level, getLostExpRatio(death_level), exp_value, recovery_cost);
		
		Connection con				= null;
		PreparedStatement pstm		= null;
		try {
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(EXP_INSERT_QUERY);
			int index = 0;
			pstm.setInt(++index, obj.get_char_id());
			pstm.setTimestamp(++index, obj.get_delete_time());
			pstm.setInt(++index, obj.get_death_level());
			pstm.setInt(++index, obj.get_exp_value());
			pstm.setInt(++index, obj.get_recovery_cost());
			if (pstm.executeUpdate() > 0) {
				DeathPenaltyUser penalty	= pc.get_penalty_exp();
				penalty.add(obj);
				pc.sendPackets(new S_DeathPenaltyRecoveryExpListNoti(penalty.get_list()), true);
				LoggerInstance.getInstance().addDeathPenaltyExp(DeathPenaltyType.LOST, pc, obj, 0);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 아이템 손실 정보 추가
	 * @param pc
	 * @param item
	 */
	public void insertItem(L1PcInstance pc, L1ItemInstance item){
		// 일반 아이템은 복구 대상이 되지 않는다.
		if (item.getItem().getItemType() == L1ItemType.NORMAL) {
			return;
		}
		Timestamp delete_time		= new Timestamp(System.currentTimeMillis() + (Config.PENALTY.REPAIR_LIMIT_TIME_SECOND * 1000));
		DeathPenaltyItemObject obj	= new DeathPenaltyItemObject(pc.getId(), delete_time, item, getRecoveryItemCost(item));
		
		Connection con				= null;
		PreparedStatement pstm		= null;
		try {
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(ITEM_INSERT_QUERY);
			int index = 0;
			pstm.setInt(++index, obj.get_char_id());
			pstm.setTimestamp(++index, obj.get_delete_time());
			L1ItemInstance recovery_item = obj.get_recovery_item();
			pstm.setInt(++index, recovery_item.getId());
			pstm.setInt(++index, recovery_item.getItemId());
			pstm.setInt(++index, recovery_item.getCount());
			pstm.setInt(++index, recovery_item.getEnchantLevel());
			pstm.setString(++index, String.valueOf(recovery_item.isIdentified()));
			pstm.setInt(++index, recovery_item.getChargeCount());
			pstm.setInt(++index, recovery_item.getBless());
			pstm.setInt(++index, recovery_item.getAttrEnchantLevel());
			pstm.setInt(++index, recovery_item.getSpecialEnchant());
			pstm.setInt(++index, recovery_item.getPotential() != null ? recovery_item.getPotential().getBonusId() : 0);
			int slot_first	= 0;
			int slot_second = 0;
			HashMap<Integer, L1Item> slots = recovery_item.getSlots();
			if (slots != null && !slots.isEmpty()) {
				slot_first	= slots.containsKey(0) ? slots.get(0).getItemNameId() : 0;
				slot_second	= slots.containsKey(1) ? slots.get(1).getItemNameId() : 0;
			}
			pstm.setInt(++index, slot_first);
			pstm.setInt(++index, slot_second);
			pstm.setInt(++index, obj.get_recovery_cost());
			if (pstm.executeUpdate() > 0) {
				DeathPenaltyUser penalty	= pc.get_penalty_item();
				penalty.add(obj);
				pc.sendPackets(new S_DeathPenaltyRecoveryItemListNoti(pc, penalty.get_list()), true);
				LoggerInstance.getInstance().addDeathPenaltyItem(DeathPenaltyType.LOST, pc, obj);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 경험치 손실 정보 제거
	 * @param obj
	 * @return boolean
	 */
	public boolean deleteExp(DeathPenaltyExpObject obj) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(EXP_DELETE_QUERY);
			pstm.setInt(1, obj.get_char_id());
			pstm.setTimestamp(2, obj.get_delete_time());
			if (pstm.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	/**
	 * 아이템 손실 정보 제거
	 * @param obj
	 * @return boolean
	 */
	public boolean deleteItem(DeathPenaltyItemObject obj) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(ITEM_DELETE_QUERY);
			pstm.setInt(1, obj.get_recovery_item().getId());
			if (pstm.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	/**
	 * 아이템 복구 가격
	 * @param item
	 * @return cost
	 */
	int getRecoveryItemCost(L1ItemInstance item){
		int cost			= Config.PENALTY.REPAIR_ITEM_DEFAULT_COST_VALUE;
		int enchantLevel	= item.getEnchantLevel();
		switch (item.getItem().getItemGrade()) {
		case ONLY:
		case MYTH:
			cost = Config.PENALTY.REPAIR_ITEM_MYTH_COST_VALUE;
			break;
		case LEGEND:
			cost = Config.PENALTY.REPAIR_ITEM_LEGEND_COST_VALUE;
			break;
		default:
			int itemId		= item.getItemId();
			if (ITEM_RECOVERY_COST.containsKey(itemId)) {
				cost = ITEM_RECOVERY_COST.get(itemId);
			}
			break;
		}
		if (enchantLevel > 0) {
			cost += enchantLevel * Config.PENALTY.REPAIR_ITEM_DEFAULT_COST_VALUE;
		}
		return cost;
	}
	
	/**
	 * 그레이트 리절렉션에 의한 경험치 복구
	 * @param pc
	 */
	public void recoveryExpSpell(L1PcInstance pc){
		java.util.LinkedList<DeathPenaltyObject> list = pc.get_penalty_exp().get_list();
		if (list == null || list.isEmpty()) {
			return;
		}
		DeathPenaltyExpObject obj = (DeathPenaltyExpObject) list.getFirst();// 가장 오래된 데이터
		long recovery_exp = (long) (obj.get_exp_value() >> 2);// 25%
		if (pc.getExp() + recovery_exp >= L1ExpPlayer.LIMIT_EXP) {
			return;
		}
	    if (((long) recovery_exp) + pc.getExp() > ExpTable.getExpByLevel(pc.getLevel() + 1)) {
	    	recovery_exp = (ExpTable.getExpByLevel(pc.getLevel() + 1) - pc.getExp());
		}
	    if (!deleteExp(obj)) {
	    	return;
	    }
    	pc.addExp(recovery_exp);
		list.remove(obj);
		pc.sendPackets(new S_DeathPenaltyRecoveryExpListNoti(list), true);
		LoggerInstance.getInstance().addDeathPenaltyExp(DeathPenaltyType.RECOVERY, pc, obj, 0);
	}
	
	/**
	 * 경험치 손실 퍼센티지
	 * @param dieLevel
	 * @return float
	 */
	public static float getLostExpRatio(int death_level){
		if (Config.PENALTY.EXP_PENALTY_VALUE_FIX) {
			return Config.PENALTY.EXP_PENALTY_VALUE_FIX_PERCENT;
		}
		if (death_level >= 87) {
			return 0.1F;
		}
		if (death_level >= 86) {
			return 0.5F;
		}
		if (death_level >= 84) {
			return 1.0F;
		}
		if (death_level >= 82) {
			return 1.5F;
		}
		if (death_level >= 80) {
			return 2.0F;
		}
		if (death_level >= 79) {
			return 2.5F;
		}
		if (death_level >= 75) {
			return 3.0F;
		}
		if (death_level >= 70) {
			return 3.5F;
		}
		if (death_level >= 65) {
			return 4.0F;
		}
		return 4.5F;
	}
	
	/**
	 * 캐릭터 삭제 시 메모리 제거
	 * @param objid
	 */
	public void removeInfo(int objid){
		if (EXP_INFO.containsKey(objid)) {
			EXP_INFO.get(objid).get_list().clear();
			EXP_INFO.remove(objid);
		}
		if (ITEM_INFO.containsKey(objid)) {
			ITEM_INFO.get(objid).get_list().clear();
			ITEM_INFO.remove(objid);
		}
	}
	
}

