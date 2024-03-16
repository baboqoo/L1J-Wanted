package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.StringUtil;

public class SmeltingConfigure {
	private static final Logger _log = Logger.getLogger(SmeltingConfigure.class.getName());
	private static final String SMELTING_CONFIG_FILE	= "./config/smelting.properties";

	public List<Integer> SMELTING_ALCHEMY_USED_IDS = new ArrayList<>();
	public byte[] SMELTING_HASH;
	public int SMELTING_ALCHEMY_1_PROB;
	public int SMELTING_ALCHEMY_2_PROB;
	public int SMELTING_ALCHEMY_3_PROB;
	public int SMELTING_ALCHEMY_4_PROB;
	public int SMELTING_ALCHEMY_FUSION_FEE_ITEM_ID;
	public int SMELTING_ALCHEMY_CHANGE_FEE_ITEM_ID;
	public int SMELTING_ALCHEMY_1_FEE_COST;
	public int SMELTING_ALCHEMY_2_FEE_COST;
	public int SMELTING_ALCHEMY_3_FEE_COST;
	public int SMELTING_ALCHEMY_4_FEE_COST;
	public int SMELTING_ALCHEMY_5_FEE_COST;
	public int SMELTING_ALCHEMY_6_FEE_COST;
	public int SMELTING_ALCHEMY_7_FEE_COST;
	public int SMELTING_LIMIT_SLOT_VALUE;
	public List<L1ItemType> SMELTING_EQUIP_TYPES = new ArrayList<>();
	public List<Integer> SMELTING_EQUIP_WEAPON_TYPES = new ArrayList<>();
	public List<Integer> SMELTING_EQUIP_ARMOR_TYPES = new ArrayList<>();
	
	public void load(){
		SMELTING_ALCHEMY_USED_IDS.clear();
		SMELTING_EQUIP_TYPES.clear();
		SMELTING_EQUIP_WEAPON_TYPES.clear();
		SMELTING_EQUIP_ARMOR_TYPES.clear();
		
		try {
			Properties 	setting 				= new Properties();
			InputStream is						= new FileInputStream(new File(SMELTING_CONFIG_FILE));
			setting.load(is);
			is.close();
			
			String alchemyUsedIds				= new String(setting.getProperty("SMELTING_ALCHEMY_USED_IDS",
												"1, 2, 3, 4, 5, 6")
												.getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			if (StringUtil.isNullOrEmpty(alchemyUsedIds)) {
				throw new Error("CONFIG_ALCHEMY_USED_IDS_EMPTY");
			}
			String[] usedArray				= alchemyUsedIds.split(StringUtil.CommaString);
			for (int i=0; i<usedArray.length; i++) {
				SMELTING_ALCHEMY_USED_IDS.add(Integer.parseInt(usedArray[i].trim()));
			}
			
			String smeltingHash					= new String(setting.getProperty("SMELTING_HASH",
												"1C 0D 76 80 E3 B3 3F 2D BE BC 3B 47 A5 D7 0B 66 9B 59 34 1B")
												.getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			if (StringUtil.isNullOrEmpty(smeltingHash)) {
				throw new Error("CONFIG_SMELTING_HASH_EMPTY");
			}
			String[] array						= smeltingHash.split(StringUtil.EmptyOneString);
			SMELTING_HASH						= new byte[array.length];
			for (int i=0; i<array.length; i++) {
				SMELTING_HASH[i]				= (byte)(Integer.parseInt(array[i].trim(), 16) & 0xFF);
			}
			
			SMELTING_ALCHEMY_1_PROB				= Integer.parseInt(setting.getProperty("SMELTING_ALCHEMY_1_PROB", "10"));
			SMELTING_ALCHEMY_2_PROB				= Integer.parseInt(setting.getProperty("SMELTING_ALCHEMY_2_PROB", "10"));
			SMELTING_ALCHEMY_3_PROB				= Integer.parseInt(setting.getProperty("SMELTING_ALCHEMY_3_PROB", "10"));
			SMELTING_ALCHEMY_4_PROB				= Integer.parseInt(setting.getProperty("SMELTING_ALCHEMY_4_PROB", "10"));
			
			SMELTING_ALCHEMY_FUSION_FEE_ITEM_ID	= Integer.parseInt(setting.getProperty("SMELTING_ALCHEMY_FUSION_FEE_ITEM_ID", "40308"));
			SMELTING_ALCHEMY_CHANGE_FEE_ITEM_ID	= Integer.parseInt(setting.getProperty("SMELTING_ALCHEMY_CHANGE_FEE_ITEM_ID", "31469"));
			
			SMELTING_ALCHEMY_1_FEE_COST			= Integer.parseInt(setting.getProperty("SMELTING_ALCHEMY_1_FEE_COST", "10000"));
			SMELTING_ALCHEMY_2_FEE_COST			= Integer.parseInt(setting.getProperty("SMELTING_ALCHEMY_2_FEE_COST", "20000"));
			SMELTING_ALCHEMY_3_FEE_COST			= Integer.parseInt(setting.getProperty("SMELTING_ALCHEMY_3_FEE_COST", "50000"));
			SMELTING_ALCHEMY_4_FEE_COST			= Integer.parseInt(setting.getProperty("SMELTING_ALCHEMY_4_FEE_COST", "100000"));
			SMELTING_ALCHEMY_5_FEE_COST			= Integer.parseInt(setting.getProperty("SMELTING_ALCHEMY_5_FEE_COST", "10"));
			SMELTING_ALCHEMY_6_FEE_COST			= Integer.parseInt(setting.getProperty("SMELTING_ALCHEMY_6_FEE_COST", "10"));
			SMELTING_ALCHEMY_7_FEE_COST			= Integer.parseInt(setting.getProperty("SMELTING_ALCHEMY_7_FEE_COST", "1"));
			
			SMELTING_LIMIT_SLOT_VALUE			= Integer.parseInt(setting.getProperty("SMELTING_LIMIT_SLOT_VALUE", "2"));
			
			String equipTypes 					= new String(setting.getProperty("SMELTING_EQUIP_TYPES", "2").getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			StringTokenizer equipTypeSt			= new StringTokenizer(equipTypes, StringUtil.CommaString);
			while (equipTypeSt.hasMoreElements()) {
				int val = Integer.parseInt(equipTypeSt.nextToken().trim());
				L1ItemType type = L1ItemType.fromInt(val);
				if (type == null) {
					System.out.println(String.format("[CONFIG_SMELTING_EQUIP_TYPE_NOT_FOUND : %d]", val));
					continue;
				}
				SMELTING_EQUIP_TYPES.add(type);
			}
			String equipWeaponTypes 			= new String(setting.getProperty("SMELTING_EQUIP_WEAPON_TYPES", "0").getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			StringTokenizer equipWeaponTypeSt	= new StringTokenizer(equipWeaponTypes, StringUtil.CommaString);
			while (equipWeaponTypeSt.hasMoreElements()) {
				SMELTING_EQUIP_WEAPON_TYPES.add(Integer.parseInt(equipWeaponTypeSt.nextToken().trim()));
			}
			String equipArmorTypes 				= new String(setting.getProperty("SMELTING_EQUIP_ARMOR_TYPES", "2").getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			StringTokenizer equipArmorTypeSt	= new StringTokenizer(equipArmorTypes, StringUtil.CommaString);
			while (equipArmorTypeSt.hasMoreElements()) {
				SMELTING_EQUIP_ARMOR_TYPES.add(Integer.parseInt(equipArmorTypeSt.nextToken().trim()));
			}
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + SMELTING_CONFIG_FILE + " File.");
		}
	}
}

