package l1j.server.server.model.warehouse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.MagicDollInfoTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

public class ClanWarehouse extends Warehouse {
	private static final long serialVersionUID = 1L;
	private static Logger _log = Logger.getLogger(ClanWarehouse.class.getName());
	private int _warehouse = 0;
	public ClanWarehouse(String clan) {
		super(clan);
	}
	
	@Override
	protected int getMax() {
		return Config.ALT.MAX_CLAN_WAREHOUSE_ITEM;
	}
	
	@Override
	public synchronized void loadItems() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM clan_warehouse WHERE clan_name = ?");
			pstm.setString(1, getName());
			rs = pstm.executeQuery();
			ItemTable temp		= ItemTable.getInstance();
			L1ItemInstance item = null;
			L1Item itemTemplate	= null;
			while (rs.next()) {
				int itemId		= rs.getInt("item_id");
				itemTemplate	= temp.getTemplate(itemId);
				if (itemTemplate == null) {
					System.out.println("ClanWarehouse template not found itemId -> " + itemId);
					continue;
				}
				item = temp.FunctionItem(itemTemplate);
				item.setId(rs.getInt("id"));
				item.setCount(rs.getInt("count"));
				item.setEquipped(false);
				item.setEnchantLevel(rs.getInt("enchantlvl"));
				item.setIdentified(rs.getInt("is_id") != 0);
				item.setDurability(rs.getInt("durability"));
				item.setChargeCount(rs.getInt("charge_count"));
				item.setRemainingTime(rs.getInt("remaining_time"));
				item.setLastUsed(rs.getTimestamp("last_used"));
				item.setBless(item.getItem().getBless());
				item.setAttrEnchantLevel(rs.getInt("attr_enchantlvl"));
				item.setSpecialEnchant(rs.getInt("special_enchant"));
				item.setPotential(MagicDollInfoTable.getPotential(rs.getInt("doll_ablity")));
				_items.add(item);
				L1World.getInstance().storeObject(item);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	@Override
	public synchronized void insertItem(L1ItemInstance item) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO clan_warehouse SET id = ?, clan_name = ?, item_id = ?, item_name = ?, count = ?, is_equipped=0, enchantlvl = ?, is_id= ?, durability = ?, charge_count = ?, remaining_time = ?, last_used = ?, attr_enchantlvl = ?, special_enchant = ?, doll_ablity = ?, package = ?");
			pstm.setInt(1, item.getId());
			pstm.setString(2, getName());
			pstm.setInt(3, item.getItemId());
			//pstm.setString(4, item.getDescKr());
			pstm.setString(4, item.getDescEn());
			pstm.setInt(5, item.getCount());
			pstm.setInt(6, item.getEnchantLevel());
			pstm.setInt(7, item.isIdentified() ? 1 : 0);
			pstm.setInt(8, item.getDurability());
			pstm.setInt(9, item.getChargeCount());
			pstm.setInt(10, item.getRemainingTime());
			pstm.setTimestamp(11, item.getLastUsed());
			pstm.setInt(12, item.getAttrEnchantLevel());
			pstm.setInt(13, item.getSpecialEnchant());
			pstm.setInt(14, item.getPotential() == null ? 0 : item.getPotential().getBonusId());
			pstm.setInt(15, item.isPackage() == false ? 0 : 1);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	@Override
	public synchronized void updateItem(L1ItemInstance item) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_warehouse SET count = ? WHERE id = ?");
			pstm.setInt(1, item.getCount());
			pstm.setInt(2, item.getId());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	@Override
	public synchronized void deleteItem(L1ItemInstance item) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_warehouse WHERE id = ?");
			pstm.setInt(1, item.getId());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		int index = _items.indexOf(item);
		if (index >= 0) {
			_items.remove(index);
		}
	}

	public synchronized void deleteAllItems() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_warehouse WHERE clan_name = ?");
			pstm.setString(1, getName());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	public int getWarehouseUsingChar() {
		return _warehouse;
	}

	public boolean setWarehouseUsingChar(int objid, int prevOwner) {
		synchronized (this) {
			if (objid == _warehouse) {
				return true;
			}
			if (objid != 0 && _warehouse != 0) {
				if (prevOwner != _warehouse) {
					return false;
				}
			}
			_warehouse = objid;
			return true;
		}
	}
}

