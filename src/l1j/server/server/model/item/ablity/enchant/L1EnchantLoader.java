package l1j.server.server.model.item.ablity.enchant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.util.FastMap;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.templates.L1Armor;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Weapon;
import l1j.server.server.utils.SQLUtil;

/**
 * 아이템 인첸트 구간별 능력치
 * @author LinOffice
 */
public class L1EnchantLoader {
	private static L1EnchantLoader _instance;
	public static L1EnchantLoader getInstance(){
		if (_instance == null) {
			_instance = new L1EnchantLoader();
		}
		return _instance;
	}
	private static final FastMap<Integer, L1EnchantFactory> DATA = new FastMap<Integer, L1EnchantFactory>();
	
	/**
	 * 인챈트데이터 조사
	 * @param itemId
	 * @return L1EnchantAblity
	 */
	public static L1EnchantFactory getEnchantAblity(int itemId){
		return DATA.get(itemId);
	}
	
	private L1EnchantLoader(){
		load();
	}
	
	private void load(){
		Connection con					= null;
		PreparedStatement pstm			= null;
		ResultSet rs					= null;
		L1EnchantFactory factory		= null;
		L1EnchantAblity ablity			= null;
		L1Item item						= null;
		ItemTable temp					= ItemTable.getInstance();
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM item_enchant_ablity ORDER BY itemId ASC, enchant DESC");
			rs		= pstm.executeQuery();
			while(rs.next()){
				ablity	= new L1EnchantAblity(rs);
				factory	= DATA.get(ablity.getItemId());
				if (factory == null) {
					item = temp.getTemplate(ablity.getItemId());
					if (item == null) {
						System.out.println(String.format("[L1EnchantAblityLoader] NOT FOUND ITEM ID(%d)", ablity.getItemId()));
						continue;
					}
					if (item.getItemType() == L1ItemType.NORMAL) {
						System.out.println(String.format("[L1EnchantAblityLoader] NOT ENCHANT ITEM ID(%d)", ablity.getItemId()));
						continue;
					}
					factory	= new L1EnchantFactory();
					// 아이템에 등록한다.
					if (item instanceof L1Weapon) {
						((L1Weapon) item).setEnchantInfo(factory);
					} else if (item instanceof L1Armor) {
						((L1Armor) item).setEnchantInfo(factory);
					}
					DATA.put(ablity.getItemId(), factory);
				}
				factory.putAblity(ablity.getEnchant(), ablity);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public void reload(){
		for (L1EnchantFactory obj : DATA.values()) {
			obj.dispose();
		}
		DATA.clear();
		load();
	}
}

