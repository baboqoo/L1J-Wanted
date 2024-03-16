package l1j.server.web.dispatcher.response.market;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.response.item.ItemDAO;
import l1j.server.web.dispatcher.response.item.ItemVO;

public class MPSECore {
	private static final Object _lock = new Object();
	private static MPSECore _instance;
	private HashMap<String, MPSEElement> _elements;
	private HashMap<String, ArrayList<String>> _keywords;
	
	private static LinkedHashMap<Integer, LinkedHashMap<Integer, LinkedList<MarketSearchRankVO>>> SEARCH_RANK = new LinkedHashMap<>();
	
	public static LinkedHashMap<Integer, LinkedHashMap<Integer, LinkedList<MarketSearchRankVO>>> getSearchRank() {
		return SEARCH_RANK;
	}

	public static MPSECore getInstance(){
		if (_instance == null) {
			_instance = new MPSECore();
		}
		return _instance;
	}

	public static void release() {
		SEARCH_RANK.clear();
		if (_instance != null){
			_instance.store();
			_instance = null;
		}
	}

	private MPSECore(){
		ArrayList<String> dicList = createDictionary();
		ArrayList<String> keyList = null;
		MPSEElement element = null;
		int dicSize = dicList.size();
		int rows = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String keyword = null;
		String key = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			
			pstm = createPstm(con);
			this._keywords = new HashMap<>(dicList.size());
			this._elements = new HashMap<>(1024);
			for (int i = 0; i < dicSize; i++) {
				keyword = (String)dicList.get(i);
				if (i != 0) {
					pstm.clearParameters();
				}
				
				key = String.format("%%%s%%", new Object[] { keyword });
				pstm.setString(1, key);
				pstm.setString(2, key);
				pstm.setString(3, key);
				rs = pstm.executeQuery();
				rows = calcRows(rs);
				if (rows <= 0) {
					SQLUtil.close(rs);
				} else {
					keyList = new ArrayList<>(rows);
					this._keywords.put(keyword, keyList);
					while (rs.next()) {
						String name = rs.getString("desc_kr").replaceAll(ItemTable.COLOR_REPLACE_STR, StringUtil.EmptyString);// 색깔 포함 이름 치환
						if (!keyList.contains(name)) {
							keyList.add(name);
						}
						element = (MPSEElement)this._elements.get(name);
						if (element == null) {
							element = new MPSEElement();
							element.name = name;
							element.invGfx = rs.getInt("iconId");
							this._elements.put(name, element);
						}

						int bless = rs.getInt("bless");
						if (bless == 0) {
							element.blessId = rs.getInt("item_id");
						} else if (bless == 1) {
							element.normalId = rs.getInt("item_id");
						} else {
							element.curseId = rs.getInt("item_id");
						}
					}
					SQLUtil.close(rs);
				}
			}
			SQLUtil.close(rs, pstm);
			
			pstm = con.prepareStatement("SELECT * FROM app_shop_rank ORDER BY group_type, shop_type, id");
			rs = pstm.executeQuery();
			while (rs.next()) {
				String groupType = rs.getString("group_type");
				groupType = groupType.substring(0, groupType.indexOf("."));
				int group_type = Integer.parseInt(groupType);
				
				String shopType = rs.getString("shop_type");
				shopType = shopType.substring(0, shopType.indexOf("."));
				int shop_type = Integer.parseInt(shopType);
				
				int itemId = rs.getInt("item_id");
				ItemVO item = ItemDAO.getItemInfo(itemId);
				if (item == null) {
					System.out.println(String.format("[MPSECore] APP_SHOP_RANK_TEMPLATE_EMPTY : ITEM_ID(%d)", itemId));
					continue;
				}
				int enchant = rs.getInt("enchant");
				int search_rank = rs.getInt("search_rank");
				
				LinkedHashMap<Integer, LinkedList<MarketSearchRankVO>> group_map = SEARCH_RANK.get(group_type);
				if (group_map == null) {
					group_map = new LinkedHashMap<>();
					SEARCH_RANK.put(group_type, group_map);
				}
				
				LinkedList<MarketSearchRankVO> type_list = group_map.get(shop_type);
				if (type_list == null) {
					type_list = new LinkedList<>();
					group_map.put(shop_type, type_list);
				}
				type_list.add(new MarketSearchRankVO(group_type, shop_type, itemId, item, enchant, search_rank));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public void store() {
		ArrayList<String> keywords = new ArrayList<>(this._keywords.keySet());
		int size = keywords.size();
		if(size <= 0)return;
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			pstm = con.prepareStatement("INSERT IGNORE INTO app_dictionary_item SET schar=?");
			for (int i = 0; i < size; i++) {
				String s = (String)keywords.get(i);
				pstm.setString(1, s);
				pstm.addBatch();
				pstm.clearParameters();
				if ((i % 10000 == 0) && (i > 0)) {
					pstm.executeBatch();
					pstm.clearBatch();
					con.commit();
				}
			}
			pstm.executeBatch();
			pstm.clearBatch();
			con.commit();
		} catch (Exception e) {
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
	}

	private ArrayList<String> createDictionary() {
		ArrayList<String> dictionary = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int rownum = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM app_dictionary_item");
			rs = pstm.executeQuery(); rs.last();
			rownum = rs.getRow(); rs.beforeFirst();
			if (rownum <= 0) {
				return new ArrayList<>();
			}
			dictionary = new ArrayList<>(rownum);

			while(rs.next()) {
				dictionary.add(rs.getString("schar"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return dictionary;
	}

	private int calcRows(ResultSet rs) throws SQLException {
		rs.last();
		int r = rs.getRow();
		rs.beforeFirst();
		return r;
	}

	public MPSEElement getElement(String s) {
		return (MPSEElement)this._elements.get(s);
	}

	public ArrayList<String> getKeyworlds(String s) {
		ArrayList<String> list = this._keywords.get(s);
		if (list != null) {
			return list;
		}
		String qry = String.format("%%%s%%", new Object[] { s });
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		MPSEElement element = null;
		int rows = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = createPstm(con);
			pstm.setString(1, qry);
			pstm.setString(2, qry);
			pstm.setString(3, qry);
			rs = pstm.executeQuery();
			rows = calcRows(rs);
			if (rows <= 0) {
				return null;
			}
			list = new ArrayList<>(rows);
			synchronized (_lock) {
				this._keywords.put(s, list);
			}
			
			while(rs.next()) {
				String name = rs.getString("desc_kr").replaceAll(ItemTable.COLOR_REPLACE_STR, StringUtil.EmptyString);// 색깔 포함 이름 치환
				if (!list.contains(name)) {
					list.add(name);
				}
				element = (MPSEElement)this._elements.get(name);
				if (element == null) {
					element = new MPSEElement();
					element.name = name;
					element.invGfx = rs.getInt("iconId");
					synchronized (_lock) {
						this._elements.put(name, element);
					}
				}

				int bless = rs.getInt("bless");
				if (bless == 0) {
					element.blessId		= rs.getInt("item_id");
				} else if (bless == 1) {
					element.normalId	= rs.getInt("item_id");
				} else {
					element.curseId		= rs.getInt("item_id");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return list;
	}

	private PreparedStatement createPstm(Connection con) throws SQLException {
		return con.prepareStatement("SELECT item_id, desc_kr, bless, iconId FROM etcitem WHERE desc_kr LIKE ? UNION SELECT item_id, desc_kr, bless, iconId FROM weapon WHERE desc_kr LIKE ? UNION SELECT item_id, desc_kr, bless, iconId FROM armor WHERE desc_kr LIKE ?");
	}
}
