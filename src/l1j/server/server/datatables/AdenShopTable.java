package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1AdenShopItem;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class AdenShopTable {

	private static Logger _log = Logger.getLogger(AdenShopTable.class.getName());

	private static AdenShopTable _instance;

	private final HashMap<Integer, L1AdenShopItem> _allShops = new HashMap<Integer, L1AdenShopItem>();
	private List<L1AdenShopItem> list = new ArrayList<L1AdenShopItem>();

	public static int data_length = 0;

	public static AdenShopTable getInstance() {
		if (_instance == null) {
			_instance = new AdenShopTable();
		}
		return _instance;
	}

	public static void reload() {
		AdenShopTable oldInstance = _instance;
		_instance = new AdenShopTable();
		oldInstance._allShops.clear();
		oldInstance.list.clear();
	}

	private AdenShopTable() {
		loadShops();
	}

	private void loadShops() {
		data_length = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM shop_aden ORDER BY id ASC");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int itemid = rs.getInt("itemid");
				// String itemname = rs.getString("itemname");
				int price = rs.getInt("price");
				int type = rs.getInt("type");
				int status = rs.getInt("status");
				String html = rs.getString("html");
				int pack = rs.getInt("pack");
				int enchant = rs.getInt("enchant");
				if (type < 2 || type > 5) type = 5;
				_allShops.put(itemid, new L1AdenShopItem(itemid, price, pack, html, status, type, enchant));
				list.add( _allShops.get(itemid) );
				L1Item item = ItemTable.getInstance().getTemplate(itemid);
				//String itemname = item.getDescKr();
				String itemname = item.getDescEn();
				if (pack > 1) {
					itemname = itemname + "(" + pack + ")";
				}
				if (item.getMaxUseTime() > 0) {
					itemname = itemname + " [" + item.getMaxUseTime() + "]";
				} else if (item.getItemId() == 65648) {
					//itemname = itemname + " [7일]";
					itemname = itemname + " [7 " + S_SystemMessage.getRefTextNS(663) + "]";
				} else if (item.getItemId() >= 30022 && item.getItemId() <= 30025) {
					itemname = itemname + " [18000]";
				} else if (item.getItemId() >= 22320 && item.getItemId() <= 22327) {
					//itemname = itemname + " [3시간]";
					itemname = itemname + " [3 " + S_SystemMessage.getRefTextNS(664) + "]";
				}
				data_length += 30;
				data_length += itemname.getBytes("UTF-16LE").length + 2;
				if (!html.equalsIgnoreCase(StringUtil.EmptyString)) {
					byte[] test = html.getBytes("EUC-KR");
					for (int i = 0; i < test.length;) {
						if ((test[i] & 0xff) >= 0x7F) {
							i += 2;
						} else {
							i += 1;
						}
						data_length += 2;
					}
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {

		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public L1AdenShopItem get(int itemid) {
		return _allShops.get(itemid);
	}

	public int Size() {
		return _allShops.size();
	}

	public Collection<L1AdenShopItem> toArray() {
		return _allShops.values();
	}

	public List<L1AdenShopItem> getList() {
		return list;
	}

}

