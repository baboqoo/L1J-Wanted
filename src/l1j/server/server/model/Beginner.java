package l1j.server.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

public class Beginner {
	private static Logger _log = Logger.getLogger(Beginner.class.getName());

	private static Beginner _instance;

	public static Beginner getInstance() {
		if (_instance == null) {
			_instance = new Beginner();
		}
		return _instance;
	}

	private Beginner() {}
	
	public static void reload() {
		Beginner oldInstance = _instance;
		_instance = new Beginner();
		if(oldInstance != null);
	}

	public int GiveItemToActivePc(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM beginner WHERE activate IN (?,?)");

			pstm.setString(1, "A");
			pstm.setString(2, pc.getClassFeature().getClassFlag());
			
			rs = pstm.executeQuery();
			while (rs.next()) {
				int itemid	= rs.getInt("item_id");
				int count	= rs.getInt("count");
				int enchant	= rs.getInt("enchantlvl");
				L1Item temp	= ItemTable.getInstance().getTemplate(itemid);
				if (temp != null) {
					if (!temp.isMerge()) {
						L1ItemInstance item = null;
						int createCount;
						for (createCount = 0; createCount < count; createCount++) {
							item = ItemTable.getInstance().createItem(itemid);
							item.setEnchantLevel(enchant);
							if (pc.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
								pc.getInventory().storeItem(item);
							} else {
								break;
							}
						}
						if (createCount > 0) {
							//pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", item.getDescKr(), createCount)), true);
							pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", item.getDesc(), createCount)), true);
						}
					} else {
						L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
						item.setCount(count);
						item.setEnchantLevel(enchant);
						if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
							pc.getInventory().storeItem(item);
							//pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", item.getDescKr(), count)), true);
							pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", item.getDesc(), count)), true);
						}
					}
				}
			}
		} catch (SQLException e1) {
			_log.log(Level.SEVERE, e1.getLocalizedMessage(), e1);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return 0;
	}
	
	public void writeBookmark(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		PreparedStatement pstm1 = null;
		ResultSet rs = null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM beginner_teleport");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				int i = 0;
				pstm1	= con.prepareStatement("INSERT INTO character_teleport SET id = ?, char_id = ?, name = ?, locx = ?, locy = ?, mapid = ?, speed = ?, num = ?");
				pstm1.setInt(++i, IdFactory.getInstance().nextId());
				pstm1.setInt(++i, pc.getId());
				pstm1.setString(++i, rs.getString("name"));
				pstm1.setInt(++i, rs.getInt("locx")); 
				pstm1.setInt(++i, rs.getInt("locy"));
				pstm1.setShort(++i, rs.getShort("mapid"));
				pstm1.setInt(++i, -1);
				pstm1.setInt(++i, 0);
				pstm1.execute();
				SQLUtil.close(pstm1);
			}
		} catch (Exception e) {
			//_log.log(Level.SEVERE, "북마크의 추가로 에러가 발생했습니다.", e);
			_log.log(Level.SEVERE, "An error occurred while adding a bookmark.", e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm1);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public int GiveItem(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm1 = null;
		PreparedStatement pstm2 = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm1 = con.prepareStatement("SELECT * FROM beginner WHERE activate IN (?,?)");
			pstm1.setString(1, "A");
			pstm1.setString(2, pc.getClassFeature().getClassFlag());
			
			rs = pstm1.executeQuery();
			while (rs.next()) {
				try {
					int i = 0;
					pstm2 = con.prepareStatement("INSERT INTO character_items SET id=?, item_id=?, char_id=?, item_name=?, count=?, is_equipped=0, enchantlvl=?, is_id=1, durability=0, charge_count=?, remaining_time=0, last_used=?, bless=1, attr_enchantlvl=0, special_enchant=0");					
					pstm2.setInt(++i, IdFactory.getInstance().nextId());
					pstm2.setInt(++i, rs.getInt("item_id"));
					pstm2.setInt(++i, pc.getId());
					pstm2.setString(++i, rs.getString("item_name"));
					pstm2.setInt(++i, rs.getInt("count"));
					pstm2.setInt(++i, rs.getInt("enchantlvl"));
					pstm2.setInt(++i, rs.getInt("charge_count"));
					pstm2.setTimestamp(++i, null);
					pstm2.execute();
				} catch (SQLException e2) {
					_log.log(Level.SEVERE, e2.getLocalizedMessage(), e2);
				} finally {
					SQLUtil.close(pstm2);
				}
			}
		} catch (SQLException e1) {
			_log.log(Level.SEVERE, e1.getLocalizedMessage(), e1);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm2);
			SQLUtil.close(pstm1);
			SQLUtil.close(con);
		}
		return 0;
	}
}
