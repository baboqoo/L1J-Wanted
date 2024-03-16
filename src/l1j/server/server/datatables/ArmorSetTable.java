package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1ArmorSets;
import l1j.server.server.utils.SQLUtil;

public class ArmorSetTable {
	private static Logger _log = Logger.getLogger(ArmorSetTable.class.getName());

	private static ArmorSetTable _instance;

	private final ArrayList<L1ArmorSets> _armorSetList = new ArrayList<L1ArmorSets>();

	private final HashMap<Integer, L1ArmorSets> _armorsetmap = new HashMap<Integer, L1ArmorSets>();

	public static ArmorSetTable getInstance() {
		if (_instance == null) {
			_instance = new ArmorSetTable();
		}
		return _instance;
	}

	private ArmorSetTable() {
		load();
	}

	private void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM armor_set");
			rs = pstm.executeQuery();
			fillTable(rs);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "error while creating armor_set table", e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private void fillTable(ResultSet rs) throws SQLException {
		L1ArmorSets as = null;
		while (rs.next()) {
			as = new L1ArmorSets();
			as.setId(rs.getInt("id"));
			as.setSets(rs.getString("sets"));
			as.setPolyId(rs.getInt("polyid"));
			as.setMinEnchant(rs.getInt("min_enchant"));
			as.setAc(rs.getInt("ac"));
			as.setHp(rs.getInt("hp"));
			as.setMp(rs.getInt("mp"));
			as.setHpr(rs.getInt("hpr"));
			as.setMpr(rs.getInt("mpr"));
			as.setMr(rs.getInt("mr"));
			as.setStr(rs.getInt("str"));
			as.setDex(rs.getInt("dex"));
			as.setCon(rs.getInt("con"));
			as.setWis(rs.getInt("wis"));
			as.setCha(rs.getInt("cha"));
			as.setIntl(rs.getInt("intl"));
			as.setShortHitup(rs.getInt("shorthitup"));
			as.setShortDmgup(rs.getInt("shortdmgup"));
			as.setShortCritical(rs.getInt("shortCritical"));
			as.setLongHitup(rs.getInt("longhitup"));
			as.setLongDmgup(rs.getInt("longdmgup"));
			as.setLongCritical(rs.getInt("longCritical"));
			as.setSp(rs.getInt("sp"));
			as.setMagicHitup(rs.getInt("magichitup"));
			as.setMagicCritical(rs.getInt("magicCritical"));
			as.setEarth(rs.getInt("earth"));
			as.setFire(rs.getInt("fire"));
			as.setWind(rs.getInt("wind"));
			as.setWater(rs.getInt("water"));
			as.setReduc(rs.getInt("reduction"));
			as.setReducEgnor(rs.getInt("reductionEgnor"));
			as.setMagicReduc(rs.getInt("magicReduction"));
			as.setPVPDamage(rs.getInt("PVPDamage"));
			as.setPVPReduc(rs.getInt("PVPDamageReduction"));
			as.setPVPMagicReduc(rs.getInt("PVPMagicDamageReduction"));
			as.setPVPReducEgnor(rs.getInt("PVPReductionEgnor"));
			as.setPVPMagicReducEgnor(rs.getInt("PVPMagicDamageReductionEgnor"));
			as.setAbnormalStatusDamageReduction(rs.getInt("abnormalStatusDamageReduction"));
			as.setAbnormalStatusPVPDamageReduction(rs.getInt("abnormalStatusPVPDamageReduction"));
			as.setPVPDamagePercent(rs.getInt("PVPDamagePercent"));
			as.setExpBonus(rs.getInt("expBonus"));
			as.setRestExpReduceEfficiency(rs.getInt("rest_exp_reduce_efficiency"));
			as.setDg(rs.getInt("dg"));
			as.setEr(rs.getInt("er"));
			as.setMe(rs.getInt("me"));
			as.setToleranceSkill(rs.getInt("toleranceSkill"));
			as.setToleranceSpirit(rs.getInt("toleranceSpirit"));
			as.setToleranceDragon(rs.getInt("toleranceDragon"));
			as.setToleranceFear(rs.getInt("toleranceFear"));
			as.setToleranceAll(rs.getInt("toleranceAll"));
			as.setHitupSkill(rs.getInt("hitupSkill"));
			as.setHitupSpirit(rs.getInt("hitupSpirit"));
			as.setHitupDragon(rs.getInt("hitupDragon"));
			as.setHitupFear(rs.getInt("hitupFear"));
			as.setHitupAll(rs.getInt("hitupAll"));
			as.setStrangeTimeIncrease(rs.getInt("strangeTimeIncrease"));
			as.setUnderWater(Boolean.valueOf(rs.getString("underWater")));
			_armorsetmap.put(as.getId(), as);
			_armorSetList.add(as);
		}
		SQLUtil.close(rs);
	}

	public L1ArmorSets[] getAllList() {
		return _armorSetList.toArray(new L1ArmorSets[_armorSetList.size()]);
	}

	public L1ArmorSets getArmorSets(int id) {
		if (_armorsetmap.containsKey(id)) {
			return _armorsetmap.get(id);
		}
		return null;
	}
	
	public static void reload(){
		ArmorSetTable old = _instance;
		_instance = new ArmorSetTable();
		old._armorSetList.clear();
		old._armorsetmap.clear();
		old = null;
	}
}

