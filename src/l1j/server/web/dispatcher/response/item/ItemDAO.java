package l1j.server.web.dispatcher.response.item;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.common.DescKLoader;
import l1j.server.common.data.Material;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 아이템
 * @author LinOffice
 */
public class ItemDAO {
	private static ItemDAO _instance;
	public static ItemDAO getInstance() {
		if (_instance == null) {
			_instance = new ItemDAO();
		}
		return _instance;
	}
	
	private static final Map<String, String> ITEM_SEARCH	= new ConcurrentHashMap<String, String>();
	private static final Map<Integer, ItemVO> ITEMS			= new ConcurrentHashMap<Integer, ItemVO>();
	
	public static ItemVO getItemInfo(int itemId) {
		return ITEMS.get(itemId);
	}
	
	public static ItemVO getItemInfo(String name) {
		ItemVO item = null;
		for (ItemVO vo : ITEMS.values()) {
			if (vo.getName().contains(name)) {
				item = vo;
				break;
			}
		}
		return item;
	}
	
	private static final String ITEM_SKILL_MATCHES = "마법 주문서.*|.*(각인).*|.*(기운).*|Spell Scroll.*|.*(Engraved).*|.*(Vigor).*";
	public static ItemVO getItemInfoSkill(String name) {
		ItemVO item = null;
		for (ItemVO vo : ITEMS.values()) {
			if (vo.getName().matches(ITEM_SKILL_MATCHES)) {
				continue;
			}
			if (vo.getName().contains(name)) {
				item = vo;
				break;
			}
		}
		return item;
	}
	
	public static String getSearchKeyword(String keyword) {
		return ITEM_SEARCH.get(keyword);
	}
	
	public static Collection<ItemVO> getList(){
		return ITEMS.values();
	}
	
	private ItemDAO() {
		load();
	}
	
	private void load() {
		Connection con				= null;
	    PreparedStatement pstm		= null;
	    ResultSet rs				= null;
	    PreparedStatement subPstm	= null;
	    ResultSet subRs				= null;
		try {
    		con		= L1DatabaseFactory.getInstance().getConnection();
    		/*pstm	= con.prepareStatement("SELECT A.*, "
    				+ "CONCAT("
    				+ "CASE WHEN A.use_royal = 1 THEN '군주,' ELSE '' END, "
    				+ "CASE WHEN A.use_knight = 1 THEN '기사,' ELSE '' END, "
    				+ "CASE WHEN A.use_elf = 1 THEN '요정,' ELSE '' END, "
    				+ "CASE WHEN A.use_mage = 1 THEN '마법사,' ELSE '' END, "
    				+ "CASE WHEN A.use_darkelf = 1 THEN '다크엘프,' ELSE '' END, "
    				+ "CASE WHEN A.use_dragonknight = 1 THEN '용기사,' ELSE '' END, "
    				+ "CASE WHEN A.use_illusionist = 1 THEN '환술사,' ELSE '' END, "
    				+ "CASE WHEN A.use_warrior = 1 THEN '전사,' ELSE '' END, "
    				+ "CASE WHEN A.use_fencer = 1 THEN '검사,' ELSE '' END, "
    				+ "CASE WHEN A.use_lancer = 1 THEN '창기사' ELSE '' END"
    				+ ") AS USE_CLASS "
    				+ "FROM (" 
    				+ "SELECT item_id, desc_kr, item_name_id, iconId, 0 AS type, '' AS useType, weight, material, -1 AS safenchant, trade, bless, use_royal, use_knight, use_mage, use_elf, use_darkelf, use_dragonknight, use_illusionist, use_warrior, use_fencer, use_lancer, 0 AS ac, 0 AS smallDmg, 0 AS largeDmg, 0 AS hit, 0 AS dmg, 0 AS longHit, 0 AS longDmg, 0 AS mr, 0 AS sp, 0 AS canbedmg, '' AS magicName FROM etcitem WHERE desc_kr NOT LIKE '%로봇%' AND desc_kr NOT LIKE '%운영자%' AND desc_kr NOT LIKE '%(기운)%' AND desc_kr NOT LIKE '%수련자의%' AND desc_kr NOT LIKE '%상아탑의%' UNION " 
    				+ "SELECT item_id, desc_kr, item_name_id, iconId, 1 AS type, type AS useType, weight, material, safenchant, trade, bless, use_royal, use_knight, use_mage, use_elf, use_darkelf, use_dragonknight, use_illusionist, use_warrior, use_fencer, use_lancer, 0 AS ac, dmg_small AS smallDmg, dmg_large AS largeDmg, hitmodifier AS hit, dmgmodifier AS dmg, 0 AS longHit, 0 AS longDmg, m_def AS mr, add_sp AS sp, canbedmg, Magic_name AS magicName FROM weapon WHERE desc_kr NOT LIKE '%둠 블레이드%' AND desc_kr NOT LIKE '%수련자의%' AND desc_kr NOT LIKE '%상아탑의%' UNION " 
    				+ "SELECT item_id, desc_kr, item_name_id, iconId, 2 AS type, type AS useType, weight, material, safenchant, trade, bless, use_royal, use_knight, use_mage, use_elf, use_darkelf, use_dragonknight, use_illusionist, use_warrior, use_fencer, use_lancer, ac, 0 AS smallDmg, 0 AS largeDmg, hit_rate AS hit, dmg_rate AS dmg, bow_hit_rate AS longHit, bow_dmg_rate AS longDmg, m_def AS mr, add_sp AS sp, 0 AS canbedmg, Magic_name AS magicName FROM armor WHERE desc_kr NOT LIKE '%수련자의%' AND desc_kr NOT LIKE '%상아탑의%') A " + 
    				"ORDER BY item_id ASC");*/

    		pstm	= con.prepareStatement("SELECT A.*, "
    				+ "CONCAT("
    				+ "CASE WHEN A.use_royal = 1 THEN 'Monarch,' ELSE '' END, "
    				+ "CASE WHEN A.use_knight = 1 THEN 'Knight,' ELSE '' END, "
    				+ "CASE WHEN A.use_elf = 1 THEN 'Elf,' ELSE '' END, "
    				+ "CASE WHEN A.use_mage = 1 THEN 'Wizard,' ELSE '' END, "
    				+ "CASE WHEN A.use_darkelf = 1 THEN 'Dark Elf,' ELSE '' END, "
    				+ "CASE WHEN A.use_dragonknight = 1 THEN 'Dragon Knight,' ELSE '' END, "
    				+ "CASE WHEN A.use_illusionist = 1 THEN 'Illusionist,' ELSE '' END, "
    				+ "CASE WHEN A.use_warrior = 1 THEN 'Warrior,' ELSE '' END, "
    				+ "CASE WHEN A.use_fencer = 1 THEN 'Fencer,' ELSE '' END, "
    				+ "CASE WHEN A.use_lancer = 1 THEN 'Lancer' ELSE '' END"
    				+ ") AS USE_CLASS "
    				+ "FROM (" 
    				+ "SELECT item_id, desc_en as desc_kr, item_name_id, iconId, 0 AS type, '' AS useType, weight, material, -1 AS safenchant, trade, bless, use_royal, use_knight, use_mage, use_elf, use_darkelf, use_dragonknight, use_illusionist, use_warrior, use_fencer, use_lancer, 0 AS ac, 0 AS smallDmg, 0 AS largeDmg, 0 AS hit, 0 AS dmg, 0 AS longHit, 0 AS longDmg, 0 AS mr, 0 AS sp, 0 AS canbedmg, '' AS magicName FROM etcitem WHERE desc_kr NOT LIKE '%로봇%' AND desc_kr NOT LIKE '%운영자%' AND desc_kr NOT LIKE '%(기운)%' AND desc_kr NOT LIKE '%수련자의%' AND desc_kr NOT LIKE '%상아탑의%' UNION " 
    				+ "SELECT item_id, desc_en as desc_kr, item_name_id, iconId, 1 AS type, type AS useType, weight, material, safenchant, trade, bless, use_royal, use_knight, use_mage, use_elf, use_darkelf, use_dragonknight, use_illusionist, use_warrior, use_fencer, use_lancer, 0 AS ac, dmg_small AS smallDmg, dmg_large AS largeDmg, hitmodifier AS hit, dmgmodifier AS dmg, 0 AS longHit, 0 AS longDmg, m_def AS mr, add_sp AS sp, canbedmg, Magic_name AS magicName FROM weapon WHERE desc_kr NOT LIKE '%둠 블레이드%' AND desc_kr NOT LIKE '%수련자의%' AND desc_kr NOT LIKE '%상아탑의%' UNION " 
    				+ "SELECT item_id, desc_en as desc_kr, item_name_id, iconId, 2 AS type, type AS useType, weight, material, safenchant, trade, bless, use_royal, use_knight, use_mage, use_elf, use_darkelf, use_dragonknight, use_illusionist, use_warrior, use_fencer, use_lancer, ac, 0 AS smallDmg, 0 AS largeDmg, hit_rate AS hit, dmg_rate AS dmg, bow_hit_rate AS longHit, bow_dmg_rate AS longDmg, m_def AS mr, add_sp AS sp, 0 AS canbedmg, Magic_name AS magicName FROM armor WHERE desc_kr NOT LIKE '%수련자의%' AND desc_kr NOT LIKE '%상아탑의%') A " + 
    				"ORDER BY item_id ASC");

    		rs = pstm.executeQuery();
    		ItemVO vo = null;
    		while(rs.next()) {
    			vo = new ItemVO();
    			vo.setItemid(rs.getInt("item_id"));
    			vo.setName(rs.getString("desc_kr").replaceAll(ItemTable.COLOR_REPLACE_STR, StringUtil.EmptyString));// 색깔 포함 이름 치환
    			vo.setItemNameId(rs.getInt("item_name_id"));
    			vo.setInvgfx(rs.getInt("iconId"));
    			if (!isImageFileExists(vo.getInvgfx())) {// 이미지가 없으면 디폴트 처리
    				vo.setInvgfx(-1);
    			}
    			vo.setItemType(rs.getInt("type"));
    			vo.setUseType(rs.getString("useType"));
    			vo.setWeight(rs.getInt("weight"));
    			vo.setMaterial(Material.fromString(rs.getString("material")).toNameEn());
    			vo.setSafenchant(rs.getInt("safenchant"));
    			int trade = rs.getInt("trade");
    			vo.setTrade(trade == 1);
    			int bless = rs.getInt("bless");
    			//vo.setBless(bless == 0 ? "축복받은 " : bless == 2 ? "저주받은 " : StringUtil.EmptyString);
				vo.setBless(bless == 0 ? "Blessed " : bless == 2 ? "Cursed " : StringUtil.EmptyString);
    			
    			List<String> use_class = Arrays.asList(rs.getString("USE_CLASS").split(StringUtil.CommaString));
    			vo.setUseClass(new ArrayList<String>(use_class));
    			
    			vo.setAc(rs.getInt("ac"));
    			vo.setSmallDmg(rs.getInt("smallDmg"));
    			vo.setLargeDmg(rs.getInt("largeDmg"));
    			vo.setHit(rs.getInt("hit"));
    			vo.setDmg(rs.getInt("dmg"));
    			if (vo.getItemType() != 2) {
    				vo.setLongHit(vo.getHit());
    				vo.setLongDmg(vo.getDmg());
    			} else {
    				vo.setLongHit(rs.getInt("longHit"));
    				vo.setLongDmg(rs.getInt("longDmg"));
    			}
    			vo.setMr(rs.getInt("mr"));
    			vo.setSp(rs.getInt("sp"));
    			
    			String useType = vo.getUseType();
    			vo.setCanbedmg(useType.matches("BOW|SINGLE_BOW|STAFF|TOHAND_STAFF") || rs.getInt("canbedmg") == 1);
    			vo.setTwohand(useType.equals("BOW") || useType.startsWith("TOHAND"));
    			
    			String magicName = rs.getString("magicName");
    			if (!StringUtil.isNullOrEmpty(magicName)) {
    				if (magicName.contains(StringUtil.ColonString)) {
    					String[] array = magicName.split(StringUtil.ColonString);
    					for (int i=0; i<array.length; i++) {
    						array[i] = DescKLoader.getDesc(array[i]);
    					}
    					vo.setMagicName(StringUtil.concat(array));
    				} else {
    					vo.setMagicName(DescKLoader.getDesc(magicName));
    				}
    			}
    			
    			vo.setDropMonster(new ArrayList<Integer>());
    			int itemId = vo.getItemid();
    			if (itemId != 40308 && itemId != 3000028 
    					&& !(itemId >= 7700 && itemId <= 7706)
    					&& !(itemId >= 31109 && itemId <= 31132) 
    					&& itemId != 400253 && itemId != 400254
    					&& !vo.getName().matches("기사단의.*|수련자의.*|상아탑의.*|Templar.*|Trainee.*")) {// 제외할 아이템
    				subPstm	= con.prepareStatement("SELECT mobId FROM droplist WHERE itemId=?");
    				subPstm.setInt(1, itemId);
        			subRs	= subPstm.executeQuery();
        			while(subRs.next()) {
        				vo.getDropMonster().add(subRs.getInt("mobId"));
        			}
        			SQLUtil.close(subRs, subPstm);
    			}
    			
    			ITEMS.put(vo.getItemid(), vo);
    		}
    		SQLUtil.close(rs, pstm);
    		
    		pstm	= con.prepareStatement("SELECT item_name, item_keyword FROM app_item_search");
    		rs		= pstm.executeQuery();
    		while(rs.next()) {
    			ITEM_SEARCH.put(rs.getString("item_keyword"), rs.getString("item_name"));
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
    		SQLUtil.close(subRs, subPstm);
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private static final String IMG_PATH = "./appcenter/img/item/%d.png";
	private boolean isImageFileExists(int iconId){
		File file = new File(String.format(IMG_PATH, iconId));
		boolean result = file.exists();
		file = null;
		return result;
	}
	
	public static void release() {
		ITEMS.clear();
		ITEM_SEARCH.clear();
		_instance = null;
	}
}

