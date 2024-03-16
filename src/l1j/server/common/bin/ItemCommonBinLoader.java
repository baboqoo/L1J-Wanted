package l1j.server.common.bin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.DescKLoader;
import l1j.server.common.bin.item.CommonItemInfo;
import l1j.server.common.bin.item.CommonItemInfo.eItemCategory;
import l1j.server.common.bin.item.ItemCommonBin;
import l1j.server.common.bin.item.ItemCommonBinExtend;
import l1j.server.common.data.BodyPart;
import l1j.server.common.data.CharacterClass;
import l1j.server.common.data.ExtendedWeaponType;
import l1j.server.common.data.Material;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * item-common.bin 파일 로더
 * @author LinOffice
 */
public class ItemCommonBinLoader {
	private static Logger _log = Logger.getLogger(ItemCommonBinLoader.class.getName());
	private static final HashMap<Integer, CommonItemInfo> DATA = new HashMap<>();
	private static ItemCommonBin bin;
	
	private static ItemCommonBinLoader _instance;
	public static ItemCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new ItemCommonBinLoader();
		}
		return _instance;
	}
	
	public static CommonItemInfo getCommonInfo(int name_id) {
		return DATA.get(name_id);
	}

	private ItemCommonBinLoader() {
		if (Config.COMMON.COMMON_ITEM_BIN_UPDATE) {
			loadFile();
		} else {
			load();
		}
	}
	
	private void loadFile(){
		bin = ItemCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/item-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(ItemCommonBin) %d", bin.getInitializeBit()));
		
		regist();
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_item_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_item_common SET "
			+ "name_id=?, icon_id=?, sprite_id=?, desc_id=?, real_desc=?, desc_kr=?, material=?, weight_1000ea=?, "
			+ "level_limit_min=?, level_limit_max=?, "
			+ "prince_permit=?, knight_permit=?, elf_permit=?, magician_permit=?, "
			+ "darkelf_permit=?, dragonknight_permit=?, illusionist_permit=?, warrior_permit=?, "
			+ "fencer_permit=?, lancer_permit=?, equip_bonus_list=?, interaction_type=?, "
			+ "real_weight=?, spell_range=?, item_category=?, body_part=?, ac=?, extended_weapon_type=?, "
			+ "large_damage=?, small_damage=?, hit_bonus=?, damage_bonus=?, armor_series_info=?, cost=?, "
			+ "can_set_mage_enchant=?, merge=?, pss_event_item=?, market_searching_item=?, "
			+ "lucky_bag_reward_list=?, element_enchant_table=?, accessory_enchant_table=?, "
			+ "bm_prob_open=?, enchant_type=?, is_elven=?, forced_elemental_enchant=?, max_enchant=?, energy_lost=?, "
			+ "prob=?, pss_heal_item=?, useInterval=?";
	
	private void regist(){
		try {
			HashMap<Integer, ItemCommonBinExtend> list = bin.get_item_list();
			
			int regiCnt = 1, limitCnt = 0;
			HashMap<Integer, ArrayList<CommonItemInfo>> regiMap = new HashMap<>();
			for (ItemCommonBinExtend info : list.values()) {
				if (limitCnt >= 300) {
					limitCnt = 0;
					regiCnt++;
				}
				ArrayList<CommonItemInfo> regilist = regiMap.get(regiCnt);
				if (regilist == null) {
					regilist = new ArrayList<>();
					regiMap.put(regiCnt, regilist);
				}
				regilist.add(info.get_item());
				limitCnt++;
			}
			
			Connection con			= null;
			PreparedStatement pstm	= null;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				pstm	= con.prepareStatement(TRUNCATE_QUERY);
				pstm.execute();
			} catch(SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				SQLUtil.close(pstm, con);
			}
			
			for (ArrayList<CommonItemInfo> regiList : regiMap.values()) {
				if (regiList == null || regiList.isEmpty()) {
					continue;
				}
				
				try {
					con		= L1DatabaseFactory.getInstance().getConnection();
					con.setAutoCommit(false);
					
					pstm	= con.prepareStatement(INSERT_QUERY);
					for (CommonItemInfo info : regiList) {
						DATA.put(info.get_name_id(), info);
						int idx = 0;
						pstm.setInt(++idx, info.get_name_id());
						pstm.setInt(++idx, info.get_icon_id());
						pstm.setInt(++idx, info.get_sprite_id());
						pstm.setString(++idx, info.get_desc() == null ? null : info.get_desc());
						pstm.setString(++idx, info.get_real_desc() == null ? null : info.get_real_desc());
						pstm.setString(++idx, info.get_desc() == null ? null : DescKLoader.getDesc(info.get_desc()));
						Material material = info.get_material();
						pstm.setString(++idx, material == null ? String.format("%s(%d)", Material.NONE.name(), Material.NONE.toInt()) : String.format("%s(%d)", material.name(), material.toInt()));
						pstm.setInt(++idx, info.get_weight_1000ea());
						pstm.setInt(++idx, info.get_level_limit_min());
						pstm.setInt(++idx, info.get_level_limit_max());
						java.util.LinkedList<CharacterClass> class_permit = info.get_class_permit();
						boolean isAllPermit = class_permit != null && class_permit.contains(CharacterClass.CLASS_ALL);
						pstm.setString(++idx, isAllPermit || class_permit != null && class_permit.contains(CharacterClass.PRINCE) ? StringUtil.TrueString : StringUtil.FalseString);
						pstm.setString(++idx, isAllPermit || class_permit != null && class_permit.contains(CharacterClass.KNIGHT) ? StringUtil.TrueString : StringUtil.FalseString);
						pstm.setString(++idx, isAllPermit || class_permit != null && class_permit.contains(CharacterClass.ELF) ? StringUtil.TrueString : StringUtil.FalseString);
						pstm.setString(++idx, isAllPermit || class_permit != null && class_permit.contains(CharacterClass.MAGICIAN) ? StringUtil.TrueString : StringUtil.FalseString);
						pstm.setString(++idx, isAllPermit || class_permit != null && class_permit.contains(CharacterClass.DARKELF) ? StringUtil.TrueString : StringUtil.FalseString);
						pstm.setString(++idx, isAllPermit || class_permit != null && class_permit.contains(CharacterClass.DRAGON_KNIGHT) ? StringUtil.TrueString : StringUtil.FalseString);
						pstm.setString(++idx, isAllPermit || class_permit != null && class_permit.contains(CharacterClass.ILLUSIONIST) ? StringUtil.TrueString : StringUtil.FalseString);
						pstm.setString(++idx, isAllPermit || class_permit != null && class_permit.contains(CharacterClass.WARRIOR) ? StringUtil.TrueString : StringUtil.FalseString);
						pstm.setString(++idx, isAllPermit || class_permit != null && class_permit.contains(CharacterClass.FENCER) ? StringUtil.TrueString : StringUtil.FalseString);
						pstm.setString(++idx, isAllPermit || class_permit != null && class_permit.contains(CharacterClass.LANCER) ? StringUtil.TrueString : StringUtil.FalseString);
						pstm.setString(++idx, info.get_equip_bonus_list_toString());
						pstm.setInt(++idx, info.get_interaction_type());
						pstm.setInt(++idx, info.get_real_weight());
						pstm.setInt(++idx, info.get_spell_range());
						eItemCategory category = info.get_item_category();
						pstm.setString(++idx, category == null ? String.format("%s(%d)", eItemCategory.NONE.name(), eItemCategory.NONE.toInt()) : String.format("%s(%d)", category.name(), category.toInt()));
						BodyPart bodyPart = info.get_body_part();
						pstm.setString(++idx, bodyPart == null ? String.format("%s(%d)", BodyPart.NONE.name(), BodyPart.NONE.toInt()) : String.format("%s(%d)", bodyPart.name(), bodyPart.toInt()));
						pstm.setInt(++idx, info.get_ac());
						ExtendedWeaponType extended = info.get_extended_weapon_type();
						pstm.setString(++idx, extended == null ? String.format("%s(%d)", ExtendedWeaponType.NONE.name(), ExtendedWeaponType.NONE.toInt()) : String.format("%s(%d)", extended.name(), extended.toInt()));
						pstm.setInt(++idx, info.get_large_damage());
						pstm.setInt(++idx, info.get_small_damage());
						pstm.setInt(++idx, info.get_hit_bonus());
						pstm.setInt(++idx, info.get_damage_bonus());
						pstm.setString(++idx, info.get_armor_series_info_toString());
						pstm.setInt(++idx, info.get_cost());
						pstm.setString(++idx, String.valueOf(info.get_can_set_mage_enchant()));
						pstm.setString(++idx, String.valueOf(info.get_merge()));
						pstm.setString(++idx, String.valueOf(info.get_pss_event_item()));
						pstm.setString(++idx, String.valueOf(info.get_market_searching_item()));
						pstm.setString(++idx, info.get_lucky_bag_reward_list_toString());
						pstm.setInt(++idx, info.get_element_enchant_table());
						pstm.setInt(++idx, info.get_accessory_enchant_table());
						pstm.setInt(++idx, info.get_bm_prob_open());
						pstm.setInt(++idx, info.get_enchant_type());
						pstm.setString(++idx, String.valueOf(info.get_is_elven()));
						pstm.setInt(++idx, info.get_forced_elemental_enchant());
						pstm.setInt(++idx, info.get_max_enchant());
						pstm.setString(++idx, String.valueOf(info.get_energy_lost()));
						pstm.setInt(++idx, info.get_prob());
						pstm.setString(++idx, String.valueOf(info.get_pss_heal_item()));
						pstm.setLong(++idx, info.get_useInterval());
						
						pstm.addBatch();
						pstm.clearParameters();
					}
					pstm.executeBatch();
					pstm.clearBatch();
					con.commit();
				} catch(SQLException e) {
					try {
						con.rollback();
					} catch(SQLException sqle){
						_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
					}
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				} catch(Exception e) {
					try {
						con.rollback();
					} catch(SQLException sqle){
						_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
					}
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				} finally {
					try {
						con.setAutoCommit(true);
					} catch (SQLException e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
					SQLUtil.close(pstm, con);
				}
			}
			System.out.println("item-common.bin [update completed]. TABLE : bin_item_common");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void load() {
		CommonItemInfo info		= null;
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM bin_item_common");
			rs = pstm.executeQuery();
			while (rs.next()) {
				info = new CommonItemInfo(rs);
				DATA.put(info.get_name_id(), info);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public static void reload() {
		DATA.clear();
		if (Config.COMMON.COMMON_ITEM_BIN_UPDATE) {
			_instance.loadFile();
		} else {
			_instance.load();
		}
	}
}

