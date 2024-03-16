package l1j.server.server.storage.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.KeyTable;
import l1j.server.server.datatables.MagicDollInfoTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.storage.CharactersItemStorage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

public class MySqlCharactersItemStorage extends CharactersItemStorage {
	private static final Logger _log = Logger.getLogger(MySqlCharactersItemStorage.class.getName());
	
	@Override
	public ArrayList<L1ItemInstance> loadItems(int objId) throws Exception {
		ArrayList<L1ItemInstance> items = null;

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			items = new ArrayList<L1ItemInstance>();
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_items WHERE char_id = ?");
			pstm.setInt(1, objId);

			L1ItemInstance item = null;
			rs = pstm.executeQuery();
			L1Item itemTemplate = null;
			
			while(rs.next()){
				int itemId = rs.getInt("item_id");
				itemTemplate = ItemTable.getInstance().getTemplate(itemId);
				if (itemTemplate == null) {
					_log.warning(String.format("item id:%d not found", itemId));
					continue;
				}
				item = ItemTable.getInstance().FunctionItem(itemTemplate);
				item.setId(rs.getInt("id"));
				item.setItem(itemTemplate);
				item.setCount(rs.getInt("count"));
				item.setEquipped(rs.getBoolean("Is_equipped"));
				item.setEnchantLevel(rs.getInt("enchantlvl"));
				item.setIdentified(rs.getBoolean("is_id"));
				item.setDurability(rs.getInt("durability"));
				item.setChargeCount(rs.getInt("charge_count"));
				item.setRemainingTime(rs.getInt("remaining_time"));
				item.setLastUsed(rs.getTimestamp("last_used"));
				item.setBless(rs.getInt("bless"));
				item.setAttrEnchantLevel(rs.getInt("attr_enchantlvl"));
				item.setSpecialEnchant(rs.getInt("special_enchant"));
				item.setPotential(MagicDollInfoTable.getPotential(rs.getInt("doll_ablity")));
				item.setEndTime(rs.getTimestamp("end_time"));
				item.setKey(rs.getInt("KeyVal"));
				item.setPackage(rs.getBoolean("package"));
				item.setEngrave(rs.getBoolean("engrave"));
				item.setScheduled(rs.getBoolean("scheduled"));
				int slot_0 = rs.getInt("slot_0");
				int slot_1 = rs.getInt("slot_1");
				if (slot_0 > 0) {
					item.insertSlot(0, ItemTable.getSmelting(slot_0));
				}
				if (slot_1 > 0) {
					item.insertSlot(1, ItemTable.getSmelting(slot_1));
				}
				item.getLastStatus().updateAll();
			    if (item.getItem().getItemId() == 80500) {
			    	KeyTable.checKey(item);
			    }
				items.add(item);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return items;
	}

	@Override
	public void storeItem(int objId, L1ItemInstance item) throws Exception {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(
					"INSERT INTO character_items SET id = ?, item_id = ?, char_id = ?, item_name = ?, count = ?, is_equipped = 0, enchantlvl = ?, "
					+ "is_id = ?, durability = ?, charge_count = ?, remaining_time = ?, last_used = ?, bless = ?, "
					+ "attr_enchantlvl = ?, special_enchant = ?, doll_ablity = ?, end_time = ?, KeyVal = ?, package = ?, engrave = ?, scheduled = ?, "
					+ "slot_0 = ?, slot_1 = ?");
			int i = 0;
			pstm.setInt(++i, item.getId());
			pstm.setInt(++i, item.getItem().getItemId());
			pstm.setInt(++i, objId);
			pstm.setString(++i, item.getItem().getDescEn());
			pstm.setInt(++i, item.getCount());
			pstm.setInt(++i, item.getEnchantLevel());
			pstm.setInt(++i, item.isIdentified() ? 1 : 0);
			pstm.setInt(++i, item.getDurability());
			pstm.setInt(++i, item.getChargeCount());
			pstm.setInt(++i, item.getRemainingTime());
			pstm.setTimestamp(++i, item.getLastUsed());
			pstm.setInt(++i, item.getBless());
			pstm.setInt(++i, item.getAttrEnchantLevel());
			pstm.setInt(++i, item.getSpecialEnchant());
			pstm.setInt(++i, item.getPotential() == null ? 0 : item.getPotential().getInfo().get_bonus_id());
			pstm.setTimestamp(++i, item.getEndTime());
			pstm.setInt(++i, item.getKey());
			pstm.setInt(++i, item.isPackage() ? 1 : 0);
			pstm.setInt(++i, item.isEngrave() ? 1 : 0);
			pstm.setInt(++i, item.isScheduled() ? 1 : 0);
			
			HashMap<Integer, L1Item> slots = item.getSlots();
			int slot_0	= slots != null && slots.containsKey(0) ? slots.get(0).getItemNameId() : 0;
			int slot_1	= slots != null && slots.containsKey(1) ? slots.get(1).getItemNameId() : 0;
			pstm.setInt(++i, slot_0);
			pstm.setInt(++i, slot_1);
			
			pstm.execute();
		} catch (SQLException e) {
			System.out.println("DB item storage error Item name: " + item.getDescEn() + " Owner ID: " + objId);
			throw e;
		} finally {
			SQLUtil.close(pstm, con);
		}
		item.getLastStatus().updateAll();
	}

	@Override
	public void deleteItem(L1ItemInstance item) throws Exception {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_items WHERE id = ?");
			pstm.setInt(1, item.getId());
			pstm.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	@Override
	public void updateItemListAll(ArrayList<L1ItemInstance> itemList) throws Exception {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			
			pstm = con.prepareStatement(
					"UPDATE character_items SET " +
					"item_id = ?" +
					",count = ?" +
					",durability = ?" +
					",charge_count = ?" +
					",remaining_time = ?" +
					",enchantlvl = ?" +
					",is_equipped = ?" +
					",is_id = ?" +
					",last_used = ?" +
					",bless = ?" +
					",attr_enchantlvl = ?" +
					",special_enchant = ?" +
					",doll_ablity = ?" +
					",end_time = ?" +
					",engrave = ?" +
					",scheduled = ?" +
					",slot_0 = ?" +
					",slot_1 = ?" +
					" WHERE id = ?");
			
			for (L1ItemInstance item : itemList) {
				int i = 0;
				pstm.setInt(++i, item.getItemId());
				pstm.setInt(++i, item.getCount());
				pstm.setInt(++i, item.getDurability());
				pstm.setInt(++i, item.getChargeCount());
				pstm.setInt(++i, item.getRemainingTime());
				pstm.setInt(++i, item.getEnchantLevel());
				pstm.setInt(++i, (item.isEquipped() ? 1 : 0));
				pstm.setInt(++i, (item.isIdentified() ? 1 : 0));
				pstm.setTimestamp(++i, item.getLastUsed());
				pstm.setInt(++i, item.getBless());
				pstm.setInt(++i, item.getAttrEnchantLevel());
				pstm.setInt(++i, item.getSpecialEnchant());
				pstm.setInt(++i, item.getPotential() == null ? 0 : item.getPotential().getInfo().get_bonus_id());
				pstm.setTimestamp(++i, item.getEndTime());
				pstm.setInt(++i, item.isEngrave() ? 1 : 0);
				pstm.setInt(++i, item.isScheduled() ? 1 : 0);
				
				HashMap<Integer, L1Item> slots = item.getSlots();
				int slot_0	= slots != null && slots.containsKey(0) ? slots.get(0).getItemNameId() : 0;
				int slot_1	= slots != null && slots.containsKey(1) ? slots.get(1).getItemNameId() : 0;
				pstm.setInt(++i, slot_0);
				pstm.setInt(++i, slot_1);
				
				pstm.setInt(++i, item.getId());
				pstm.addBatch();
				pstm.clearParameters();
			}
			pstm.executeBatch();
			pstm.clearBatch();
			con.commit();
			
			// 정상적 완료 후 상태 업데이트
			for (L1ItemInstance item : itemList) {
				item.getLastStatus().updateAll();
			}
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				sqle.printStackTrace();
			}
			throw e;
		} catch (Exception e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				sqle.printStackTrace();
			}
			throw e;
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			SQLUtil.close(pstm, con);
		}
	}
	
	@Override
	public void updateItemAll(L1ItemInstance item) throws Exception {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(
					"UPDATE character_items SET " +
					"item_id = ?" +
					",count = ?" +
					",durability = ?" +
					",charge_count = ?" +
					",remaining_time = ?" +
					",enchantlvl = ?" +
					",is_equipped = ?" +
					",is_id = ?" +
					",last_used = ?" +
					",bless = ?" +
					",attr_enchantlvl = ?" +
					",special_enchant = ?" +
					",doll_ablity = ?" +
					",end_time = ?" +
					",engrave = ?" +
					",scheduled = ?" +
					",slot_0 = ?" +
					",slot_1 = ?" +
					" WHERE id = ?");
			int i = 0;
			pstm.setInt(++i, item.getItemId());
			pstm.setInt(++i, item.getCount());
			pstm.setInt(++i, item.getDurability());
			pstm.setInt(++i, item.getChargeCount());
			pstm.setInt(++i, item.getRemainingTime());
			pstm.setInt(++i, item.getEnchantLevel());
			pstm.setInt(++i, (item.isEquipped() ? 1 : 0));
			pstm.setInt(++i, (item.isIdentified() ? 1 : 0));
			pstm.setTimestamp(++i, item.getLastUsed());
			pstm.setInt(++i, item.getBless());
			pstm.setInt(++i, item.getAttrEnchantLevel());
			pstm.setInt(++i, item.getSpecialEnchant());
			pstm.setInt(++i, item.getPotential() == null ? 0 : item.getPotential().getInfo().get_bonus_id());
			pstm.setTimestamp(++i, item.getEndTime());
			pstm.setInt(++i, item.isEngrave() ? 1 : 0);
			pstm.setInt(++i, item.isScheduled() ? 1 : 0);
			
			HashMap<Integer, L1Item> slots = item.getSlots();
			int slot_0	= slots != null && slots.containsKey(0) ? slots.get(0).getItemNameId() : 0;
			int slot_1	= slots != null && slots.containsKey(1) ? slots.get(1).getItemNameId() : 0;
			pstm.setInt(++i, slot_0);
			pstm.setInt(++i, slot_1);
			
			pstm.setInt(++i, item.getId());

			pstm.execute();

			item.getLastStatus().updateAll();
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	@Override
	public void updateItemId(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET item_id = ? WHERE id = ?", item.getItemId());
		item.getLastStatus().updateItemId();
	}

	@Override
	public void updateItemCount(L1ItemInstance item) throws Exception {
		/** 패키지상점 **/
		executeUpdate(item.getId(), "UPDATE character_items SET count = ?, package = ? WHERE id = ?", item.getCount(), item.isPackage() == false ? 0 : 1);
		item.getLastStatus().updateCount();
	}

	@Override
	public void updateItemDurability(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET durability = ? WHERE id = ?", item.getDurability());
		item.getLastStatus().updateDuraility();
	}

	@Override
	public void updateItemChargeCount(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET charge_count = ? WHERE id = ?", item.getChargeCount());
		item.getLastStatus().updateChargeCount();
	}

	@Override
	public void updateItemRemainingTime(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET remaining_time = ? WHERE id = ?", item.getRemainingTime());
		item.getLastStatus().updateRemainingTime();
	}

	@Override
	public void updateItemEnchantLevel(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET enchantlvl = ? WHERE id = ?", item.getEnchantLevel());
		item.getLastStatus().updateEnchantLevel();
	}

	@Override
	public void updateItemEquipped(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET is_equipped = ? WHERE id = ?", (item.isEquipped() ? 1 : 0));
		item.getLastStatus().updateEquipped();
	}

	@Override
	public void updateItemIdentified(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET is_id = ? WHERE id = ?", (item.isIdentified() ? 1 : 0));
		item.getLastStatus().updateIdentified();
	}

	@Override
	public void updateItemDelayEffect(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET last_used = ? WHERE id = ?", item.getLastUsed());
		item.getLastStatus().updateLastUsed();
	}

	@Override
	public void updateItemBless(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET bless = ? WHERE id = ?", item.getBless());
		item.getLastStatus().updateBless();
	}

	@Override
	public void updateItemAttrEnchantLevel(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET attr_enchantlvl = ? WHERE id = ?", item.getAttrEnchantLevel());
		item.getLastStatus().updateAttrEnchantLevel();
	}
	
	@Override
	public void updateItemSpecialEnchant(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET special_enchant = ? WHERE id = ?", item.getSpecialEnchant());
		item.getLastStatus().updateSpecialEnchant();
	}
	
	@Override
	public void updateItemPotential(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET doll_ablity = ? WHERE id = ?", item.getPotential() == null ? 0 : item.getPotential().getInfo().get_bonus_id());
		item.getLastStatus().updatePotential();
	}
	
	@Override
	public void updateItemSlotAll(L1ItemInstance item) throws Exception {
		HashMap<Integer, L1Item> slots = item.getSlots();
		int slot_0	= slots != null && slots.containsKey(0) ? slots.get(0).getItemNameId() : 0;
		int slot_1	= slots != null && slots.containsKey(1) ? slots.get(1).getItemNameId() : 0;
		executeUpdate("UPDATE character_items SET slot_0 = ?, slot_1 = ? WHERE id = ?", slot_0, slot_1, item.getId());
		item.getLastStatus().updateSlots();
	}
	
	@Override
	public void updateItemSlot(L1ItemInstance item, int slot_no, int stone_name_id) throws Exception {
		executeUpdate(item.getId(), String.format("UPDATE character_items SET slot_%d = ? WHERE id = ?", slot_no), stone_name_id);
		item.getLastStatus().updateSlots();
	}

	@Override
	public void updateItemEndTime(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET end_time = ? WHERE id = ?", item.getEndTime());
		item.getLastStatus().updateEndTime();
	}
	
	@Override
	public void updateItemScheduled(L1ItemInstance item) throws Exception {
		executeUpdate(item.getId(), "UPDATE character_items SET scheduled = ? WHERE id = ?", item.isScheduled() ? 1 : 0);
		item.getLastStatus().updateScheduled();
	}

	@Override
	public int getItemCount(int objId) throws Exception {
		int count = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_items WHERE char_id='" + objId + "'");
			rs = pstm.executeQuery();
			while (rs.next()) {
				count++;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return count;
	}

	private void executeUpdate(int objId, String sql, int updateNum) throws SQLException {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sql.toString());
			pstm.setInt(1, updateNum);
			pstm.setInt(2, objId);
			pstm.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	private void executeUpdate(int objId, String sql, int updateNum, int updateNum2) throws SQLException {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sql.toString());
			pstm.setInt(1, updateNum);
			pstm.setInt(2, updateNum2);
			pstm.setInt(3, objId);
			pstm.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	private void executeUpdate(int objId, String sql, Timestamp ts) throws SQLException {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sql.toString());
			pstm.setTimestamp(1, ts);
			pstm.setInt(2, objId);
			pstm.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 가변인자 쿼리
	 * @param sql
	 * @param args
	 * @throws SQLException
	 */
	private void executeUpdate(String sql, Object... args) throws SQLException {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sql.toString());
			setupPrepareStatement(pstm, args);
			pstm.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	private void setupPrepareStatement(PreparedStatement pstm, Object[] args) throws SQLException {
        for (int i = 0; i < args.length; ++i) {
            pstm.setObject(i + 1, args[i]);
        }
    }
}

