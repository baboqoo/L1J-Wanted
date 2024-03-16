package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javolution.util.FastMap;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.data.eEinhasadStatType;
import l1j.server.server.model.L1Ability;
import l1j.server.server.model.L1Character;
import l1j.server.server.templates.L1CharEinStat;
import l1j.server.server.utils.SQLUtil;

/**
 * 캐릭터 아인하사드 스탯 정보
 * @author LinOffice
 */
public class CharacterEinhasadStatTable {
	private static final FastMap<Integer, L1CharEinStat> DATA = new FastMap<>();
	private static class newInstance {
		public static final CharacterEinhasadStatTable INSTANCE = new CharacterEinhasadStatTable();
	}
	public static CharacterEinhasadStatTable getInstance(){
		return newInstance.INSTANCE;
	}
	
	private static final String LOAD_QUERY = "SELECT * FROM character_einhasadstat";
	private static final String DELETE_QUERY = "DELETE FROM character_einhasadstat WHERE objid=?";
	private static final String CREATE_QUERY = "INSERT INTO character_einhasadstat SET objid=?, "
			+ "bless=?, lucky=?, vital=?, itemSpellProb=?, absoluteRegen=?, potion=?, "
			+ "bless_efficiency=?, bless_exp=?, "
			+ "lucky_item=?, lucky_adena=?, "
			+ "vital_potion=?, vital_heal=?, "
			+ "itemSpellProb_armor=?, itemSpellProb_weapon=?, "
			+ "absoluteRegen_hp=?, absoluteRegen_mp=?, "
			+ "potion_critical=?, potion_delay=?";
	private static final String RESET_QUERY = "UPDATE character_einhasadstat SET "
			+ "bless=0, lucky=0, vital=0, itemSpellProb=0, absoluteRegen=0, potion=0, "
			+ "bless_efficiency=0, bless_exp=0, "
			+ "lucky_item=0, lucky_adena=0, "
			+ "vital_potion=0, vital_heal=0, "
			+ "itemSpellProb_armor=0, itemSpellProb_weapon=0, "
			+ "absoluteRegen_hp=0, absoluteRegen_mp=0, "
			+ "potion_critical=0, potion_delay=0 "
			+ "WHERE objid=?";
	
	private CharacterEinhasadStatTable(){
		load();
	}
	
	private void load(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		L1CharEinStat temp	= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(LOAD_QUERY);
			rs = pstm.executeQuery();
			while (rs.next()) {
				int objid = rs.getInt("objid");
				temp = new L1CharEinStat(objid,
						rs.getByte("bless"), rs.getByte("lucky"), rs.getByte("vital"), rs.getByte("itemSpellProb"), rs.getByte("absoluteRegen"), rs.getByte("potion"),
						rs.getInt("bless_efficiency"), rs.getInt("bless_exp"), 
						rs.getInt("lucky_item"), rs.getInt("lucky_adena"),
						rs.getInt("vital_potion"), rs.getInt("vital_heal"), 
						rs.getInt("itemSpellProb_armor"), rs.getInt("itemSpellProb_weapon"),
						rs.getInt("absoluteRegen_hp"), rs.getInt("absoluteRegen_mp"), 
						rs.getInt("potion_critical"), rs.getInt("potion_delay"));
				DATA.put(objid, temp);
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	/**
	 * 케릭터의 스탯 정보를 취득
	 * @param cha
	 * @return
	 */
	public L1CharEinStat getEinSate(L1Character cha){
		synchronized(DATA){
			return DATA.containsKey(cha.getId()) ? DATA.get(cha.getId()) : null;
		}
	}
	
	/**
	 * 로그인시 정보를 케릭터에 로드
	 * @param cha
	 */
	public void login(L1Character cha){
		L1CharEinStat temp = getEinSate(cha);
		if (temp != null) {
			L1Ability ability = cha.getAbility();
			ability.setStatBless(temp.getBless());
			ability.setStatLucky(temp.getLucky());
			ability.setStatVital(temp.getVital());
			ability.setStatItemSpellProb(temp.getItemSpellProb());
			ability.setStatAbsoluteRegen(temp.getAbsoluteRegen());
			ability.setStatPotion(temp.getPotion());
			ability.setBlessEfficiency(temp.getBless_efficiency());
			ability.setBlessExp(temp.getBless_exp());
			ability.setLuckyItem(temp.getLucky_item());
			ability.setLuckyAdena(temp.getLucky_adena());
			ability.setVitalPotion(temp.getVital_potion());
			ability.setVitalHeal(temp.getVital_heal());
			ability.setItemSpellProbArmor(temp.getItemSpellProb_armor());
			ability.setItemSpellProbWeapon(temp.getItemSpellProb_weapon());
			ability.setAbsoluteRegenHp(temp.getAbsoluteRegen_hp());
			ability.setAbsoluteRegenMp(temp.getAbsoluteRegen_mp());
			ability.setPotionCritical(temp.getPotion_critical());
			ability.setPotionDelay(temp.getPotion_delay());
			return;
		}
		create(new L1CharEinStat(cha.getId()));
	}
	
	/**
	 * 스탯 적용시 정보 업데이트
	 * @param temp
	 * @param cha
	 * @param type
	 */
	public void updateTemp(L1CharEinStat temp, L1Character cha, eEinhasadStatType type){
		L1Ability ability = cha.getAbility();
		switch(type){
		case BLESS:
			temp.setBless(ability.getStatBless());
			temp.setBless_efficiency(ability.getBlessEfficiency());
			temp.setBless_exp(ability.getBlessExp());
			break;
		case LUCKY:
			temp.setLucky(ability.getStatLucky());
			temp.setLucky_item(ability.getLuckyItem());
			temp.setLucky_adena(ability.getLuckyAdena());
			break;
		case VITAL:
			temp.setVital(ability.getStatVital());
			temp.setVital_potion(ability.getVitalPotion());
			temp.setVital_heal(ability.getVitalHeal());
			break;
		case ITEM_SPELL_PROB:
			temp.setItemSpellProb(ability.getStatItemSpellProb());
			temp.setItemSpellProb_armor(ability.getItemSpellProbArmor());
			temp.setItemSpellProb_weapon(ability.getItemSpellProbWeapon());
			break;
		case ABSOLUTE_REGEN:
			temp.setAbsoluteRegen(ability.getStatAbsoluteRegen());
			temp.setAbsoluteRegen_hp(ability.getAbsoluteRegenHp());
			temp.setAbsoluteRegen_mp(ability.getAbsoluteRegenMp());
			break;
		case POTION:
			temp.setPotion(ability.getStatPotion());
			temp.setPotion_critical(ability.getPotionCritical());
			temp.setPotion_delay(ability.getPotionDelay());
			break;
		default:
			return;
		}
		update(temp, type);
	}
	
	private void update(L1CharEinStat temp, eEinhasadStatType type){
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con			= L1DatabaseFactory.getInstance().getConnection();
			String sql	= "UPDATE character_einhasadstat SET ";
			switch(type){
			case BLESS:
				sql	+=	String.format("bless=%d, bless_efficiency=%d, bless_exp=%d", temp.getBless(), temp.getBless_efficiency(), temp.getBless_exp());
				break;
			case LUCKY:
				sql	+=	String.format("lucky=%d, lucky_item=%d, lucky_adena=%d", temp.getLucky(), temp.getLucky_item(), temp.getLucky_adena());
				break;
			case VITAL:
				sql	+=	String.format("vital=%d, vital_potion=%d, vital_heal=%d", temp.getVital(), temp.getVital_potion(), temp.getVital_heal());
				break;
			case ITEM_SPELL_PROB:
				sql	+=	String.format("itemSpellProb=%d, itemSpellProb_armor=%d, itemSpellProb_weapon=%d", temp.getItemSpellProb(), temp.getItemSpellProb_armor(), temp.getItemSpellProb_weapon());
				break;
			case ABSOLUTE_REGEN:
				sql	+=	String.format("absoluteRegen=%d, absoluteRegen_hp=%d, absoluteRegen_mp=%d", temp.getAbsoluteRegen(), temp.getAbsoluteRegen_hp(), temp.getAbsoluteRegen_mp());
				break;
			case POTION:
				sql	+=	String.format("potion=%d, potion_critical=%d, potion_delay=%d", temp.getPotion(), temp.getPotion_critical(), temp.getPotion_delay());
				break;
			default:
				return;
			}
			sql			+=	" WHERE objid=" + temp.getObjid();
			pstm		=	con.prepareStatement(sql);
			pstm.execute();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 스탯 초기화
	 * @param temp
	 */
	public void resetTemp(L1CharEinStat temp){
		temp.reset();// 스탯정보 초기화
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(RESET_QUERY);
			pstm.setInt(1, temp.getObjid());
			pstm.execute();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public void delete(L1CharEinStat temp){
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(DELETE_QUERY);
			pstm.setInt(1, temp.getObjid());
			if (pstm.executeUpdate() > 0) {
				DATA.remove(temp.getObjid());
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	// 케릭터 삭제시 메모리영역 제거
	public void removeInfo(int objid){
		if (DATA.containsKey(objid)) {
			DATA.remove(objid);
		}
	}
	
	/**
	 * 신규 데이타 생성
	 * @param temp
	 */
	private void create(L1CharEinStat temp){
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(CREATE_QUERY);
			int i=0;
			pstm.setInt(++i, temp.getObjid());
			pstm.setByte(++i, temp.getBless());
			pstm.setByte(++i, temp.getLucky());
			pstm.setByte(++i, temp.getVital());
			pstm.setByte(++i, temp.getItemSpellProb());
			pstm.setByte(++i, temp.getAbsoluteRegen());
			pstm.setByte(++i, temp.getPotion());
			pstm.setInt(++i, temp.getBless_efficiency());
			pstm.setInt(++i, temp.getBless_exp());
			pstm.setInt(++i, temp.getLucky_item());
			pstm.setInt(++i, temp.getLucky_adena());
			pstm.setInt(++i, temp.getVital_potion());
			pstm.setInt(++i, temp.getVital_heal());
			pstm.setInt(++i, temp.getItemSpellProb_armor());
			pstm.setInt(++i, temp.getItemSpellProb_weapon());
			pstm.setInt(++i, temp.getAbsoluteRegen_hp());
			pstm.setInt(++i, temp.getAbsoluteRegen_mp());
			pstm.setInt(++i, temp.getPotion_critical());
			pstm.setInt(++i, temp.getPotion_delay());
			if (pstm.executeUpdate() > 0) {
				DATA.put(temp.getObjid(), temp);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
}

