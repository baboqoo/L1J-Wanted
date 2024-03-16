package l1j.server.server.model.warehouse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.MagicDollInfoTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

public class PackageWarehouse extends Warehouse {
	private static final long serialVersionUID = 1L;
	protected static Logger _log = Logger.getLogger(PackageWarehouse.class.getName());

	public PackageWarehouse(String an) {
		super(an);
	}

	@Override
	protected int getMax() {
		return Config.ALT.MAX_PERSONAL_WAREHOUSE_ITEM;
	}

	@Override
	public synchronized void loadItems() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_package_warehouse WHERE account_name = ?");
			pstm.setString(1, getName());
			rs = pstm.executeQuery();
			ItemTable temp		= ItemTable.getInstance();
			L1ItemInstance item	= null;
			L1Item itemTemplate	= null;
			while (rs.next()) {
				int itemId		= rs.getInt("item_id");
				itemTemplate	= temp.getTemplate(itemId);
				if (itemTemplate == null) {
					System.out.println("PackageWarehouse template not found itemId -> " + itemId);
					continue;
				}
				item = temp.FunctionItem(itemTemplate);
				item.setId(rs.getInt("id"));
				item.setCount(rs.getInt("count"));
				item.setEquipped(false);
				item.setEnchantLevel(rs.getInt("enchantlvl"));
				item.setIdentified(rs.getInt("is_id") != 0 ? true : false);
				item.setDurability(rs.getInt("durability"));
				item.setChargeCount(rs.getInt("charge_count"));
				item.setRemainingTime(rs.getInt("remaining_time"));
				item.setLastUsed(rs.getTimestamp("last_used"));
				item.setAttrEnchantLevel(rs.getInt("attr_enchantlvl"));
				item.setSpecialEnchant(rs.getInt("special_enchant"));
				item.setPotential(MagicDollInfoTable.getPotential(rs.getInt("doll_ablity")));
				item.setBless(rs.getInt("bless"));
				if (item.getItemId() == L1ItemId.ADENA) {
					L1ItemInstance itemExist = findItemId(item.getItemId());
					if (itemExist != null) {
						deleteItem(item);
						int newCount = itemExist.getCount() + item.getCount();
						if (newCount <= L1Inventory.MAX_AMOUNT) {
							if (newCount<0) {
								newCount=0;
							}
							itemExist.setCount(newCount);
							updateItem(itemExist);
						}
					} else {
						_items.add(item);
						L1World.getInstance().storeObject(item);
					}
				} else {
					_items.add(item);
					L1World.getInstance().storeObject(item);
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public static void insertItem(String accountName, int count) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO character_package_warehouse SET id = ?, account_name = ?, item_id = ?, item_name = ?, count = ?");
			pstm.setInt(1, IdFactory.getInstance().nextId());
			pstm.setString(2, accountName);
			pstm.setInt(3, L1ItemId.PIXIE_FEATHER);
			//pstm.setString(4, "픽시 깃털");
			pstm.setString(4, "Pixie Feathers");
			pstm.setInt(5, count);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	@Override
	public synchronized void insertItem(L1ItemInstance item) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO character_package_warehouse SET id = ?, account_name = ?, item_id = ?, item_name = ?, count = ?, is_equipped=0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?, last_used = ?, attr_enchantlvl = ?, bless = ?, special_enchant = ?, doll_ablity = ?");
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
			pstm.setInt(13, item.getBless());
			pstm.setInt(14, item.getSpecialEnchant());
			pstm.setInt(15, item.getPotential() == null ? 0 : item.getPotential().getBonusId());
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
			pstm = con.prepareStatement("UPDATE character_package_warehouse SET count = ? WHERE id = ?");
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
			pstm = con.prepareStatement("DELETE FROM character_package_warehouse WHERE id = ?");
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

	public static void present(String account, int itemid, int enchant, int count) throws Exception {
		L1Item temp = ItemTable.getInstance().getTemplate(itemid);
		if(temp == null)return;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			if (account.compareToIgnoreCase("*") == 0) {
				pstm = con.prepareStatement("SELECT * FROM accounts");
			} else {
				pstm = con.prepareStatement("SELECT * FROM accounts WHERE login=?");
				pstm.setString(1, account);
			}
			rs = pstm.executeQuery();
			ArrayList<String> accountList = new ArrayList<String>();
			while (rs.next()) {
				accountList.add(rs.getString("login"));
			}
			present(accountList, itemid, enchant, count);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw e;
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public static void present(int minlvl, int maxlvl, int itemid, int enchant, int count) throws Exception {
		L1Item temp = ItemTable.getInstance().getTemplate(itemid);
		if (temp == null) {
			return;
		}
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT distinct(account_name) as account_name FROM characters WHERE level between ? and ?");
			pstm.setInt(1, minlvl);
			pstm.setInt(2, maxlvl);
			rs = pstm.executeQuery();
			ArrayList<String> accountList = new ArrayList<String>();
			while (rs.next()) {
				accountList.add(rs.getString("account_name"));
			}
			present(accountList, itemid, enchant, count);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw e;
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	// 상점에서 구입한 아이템 부가 창고로 이동
	public static boolean itemShop(String account_name, int itemid, int enchant, int count) {
		try {
			PackageWarehouse warehouse = WarehouseManager.getInstance().getPackageWarehouse(account_name);
			if (warehouse == null) {
				//System.out.println(String.format("[PackageWarehouse] NullPoint 계정[%s]", account_name));
				System.out.println(String.format("[PackageWarehouse] NullPoint account[%s]", account_name));
				return false;
			}
			L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
			if (item == null) {
				//System.out.println(String.format("[PackageWarehouse] 존재하지 않는 아이템ID[%d] 계정[%s]", itemid, account_name));
				System.out.println(String.format("[PackageWarehouse] Non-existent item ID[%d] account[%s]", itemid, account_name));
				return false;
			}
			item.setCount(count);
			if (enchant != 0) {
				item.setEnchantLevel(enchant);
			}
			warehouse.storeTradeItem(item);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static void present(ArrayList<String> accountList, int itemid, int enchant, int count) throws Exception {
		L1Item temp = ItemTable.getInstance().getTemplate(itemid);
		if (temp == null)
			//throw new Exception("존재하지 않는 아이템 ID");
			throw new Exception("Item ID does not exist");
		
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			L1ItemInstance item = null;
			for (String account : accountList) {
				if (temp.isMerge()) {
					item = ItemTable.getInstance().createItem(itemid);
					item.setEnchantLevel(enchant);
					item.setCount(count);
					pstm = con.prepareStatement("INSERT INTO character_package_warehouse SET id = ?, account_name = ?, item_id = ?, item_name = ?, count = ?, is_equipped=0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?, doll_ablity = ?");
					pstm.setInt(1, item.getId());
					pstm.setString(2, account);
					pstm.setInt(3, item.getItemId());
					//pstm.setString(4, item.getDescKr());
					pstm.setString(4, item.getDescEn());
					pstm.setInt(5, item.getCount());
					pstm.setInt(6, item.getEnchantLevel());
					pstm.setInt(7, item.isIdentified() ? 1 : 0);
					pstm.setInt(8, item.getDurability());
					pstm.setInt(9, item.getChargeCount());
					pstm.setInt(10, item.getRemainingTime());
					pstm.setInt(11, item.getPotential() == null ? 0 : item.getPotential().getBonusId());
					pstm.execute();
				} else {
					item = null;
					int createCount;
					for (createCount = 0; createCount < count; createCount++) {
						item = ItemTable.getInstance().createItem(itemid);
						item.setEnchantLevel(enchant);
						pstm = con.prepareStatement("INSERT INTO character_package_warehouse SET id = ?, account_name = ?, item_id = ?, item_name = ?, count = ?, is_equipped=0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?, doll_ablity = ?");
						pstm.setInt(1, item.getId());
						pstm.setString(2, account);
						pstm.setInt(3, item.getItemId());
						//pstm.setString(4, item.getDescKr());
						pstm.setString(4, item.getDescEn());
						pstm.setInt(5, item.getCount());
						pstm.setInt(6, item.getEnchantLevel());
						pstm.setInt(7, item.isIdentified() ? 1 : 0);
						pstm.setInt(8, item.getDurability());
						pstm.setInt(9, item.getChargeCount());
						pstm.setInt(10, item.getRemainingTime());
						pstm.setInt(11, item.getPotential() == null ? 0 : item.getPotential().getBonusId());
						pstm.execute();
					}
				}
			}
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException ignore) {
				// ignore
			}
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			//throw new Exception(".present 처리중에 에러가 발생했습니다.");
			throw new Exception("An error occurred while processing .present.");
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
}

