package l1j.server.server.datatables;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.bin.NpcCommonBinLoader;
import l1j.server.common.bin.npc.CommonNPCInfo;
import l1j.server.server.ActionCodes;
import l1j.server.server.construct.L1Attr;
import l1j.server.server.construct.L1Undead;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.poison.L1PoisonType;
import l1j.server.server.model.sprite.L1Sprite;
import l1j.server.server.model.sprite.SpriteLoader;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class NpcTable {
	private static Logger _log = Logger.getLogger(NpcTable.class.getName());
	private static final String DOPPEL_IMPL_STRING = "L1Doppelganger";
	
	private final boolean _initialized;

	private static NpcTable _instance;

	private final HashMap<Integer, L1Npc> _npcs				= new HashMap<Integer, L1Npc>();
	private static final Map<String, Integer> _familyTypes	= NpcTable.buildFamily();

	public static NpcTable getInstance() {
		if (_instance == null) {
			_instance = new NpcTable();
		}
		return _instance;
	}
	
	public boolean isInitialized() {
		return _initialized;
	}

	private NpcTable() {
		loadNpcData();
		_initialized = true;
	}

	private void loadNpcData() {
		Connection con				= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		
		int move_delay_reduce_rate	= Config.SPEED.NPC_MOVE_DELAY_REDUCE_RATE;
		try {
			con			= L1DatabaseFactory.getInstance().getConnection();
			pstm		= con.prepareStatement("SELECT * FROM npc");
			rs			= pstm.executeQuery();
			L1Npc npc 	= null;
			while(rs.next()){
				npc = new L1Npc();
				int npcId = rs.getInt("npcid");
				npc.setNpcId(npcId);
				npc.setDescKr(rs.getString("desc_kr"));
				npc.setDescEn(rs.getString("desc_en"));
				npc.setDesc(rs.getString("desc_id"));
				npc.setImpl(rs.getString("impl"));
				npc.setSpriteId(rs.getInt("spriteId"));
				npc.setLevel(rs.getInt("lvl"));
				npc.setHp(rs.getInt("hp"));
				npc.setMp(rs.getInt("mp"));
				npc.setAc(rs.getInt("ac"));
				npc.setStr(rs.getByte("str"));
				npc.setCon(rs.getByte("con"));
				npc.setDex(rs.getByte("dex"));
				npc.setWis(rs.getByte("wis"));
				npc.setInt(rs.getByte("intel"));
				npc.setMr(rs.getInt("mr"));
				npc.setExp(rs.getInt("exp"));
				npc.setAlignment(rs.getInt("alignment"));
				npc.setBig(Boolean.valueOf(rs.getString("big")));
				// set weakAttr from enum values NONE FIRE WIND EARTH WATER
				npc.setWeakAttr(L1Attr.fromString(rs.getString("weakAttr")));
				npc.setRanged(rs.getInt("ranged"));
				npc.setTamingable(Boolean.parseBoolean(rs.getString("is_taming")));
				npc.setPassiSpeed(rs.getInt("passispeed"));
				npc.setAtkSpeed(rs.getInt("atkspeed"));
				npc.setAtkMagicSpeed(rs.getInt("atk_magic_speed"));
				npc.setSubMagicSpeed(rs.getInt("sub_magic_speed"));
				
				// sprite action speed detail setting
				L1Sprite sprite = SpriteLoader.get_sprite(npc.getSpriteId());
				if (sprite != null) {
					if (npc.getPassiSpeed() > 0) {
						int move_interval = sprite.getActionSpeed(ActionCodes.ACTION_Walk);
						if (move_interval > 0 && move_interval > npc.getPassiSpeed()) {
							npc.setPassiSpeed(move_interval);
						}
						if (move_delay_reduce_rate > 0 && move_delay_reduce_rate != 100) {
							npc.setPassiSpeed((int)(npc.getPassiSpeed() * 100 / move_delay_reduce_rate));
						}
					}
					if (npc.getAtkSpeed() > 0) {
						int attack_interval = sprite.getActionSpeed(npc.getRanged() >= 10 ? ActionCodes.ACTION_BowAttack : ActionCodes.ACTION_Attack);
						if (attack_interval > 0 && attack_interval > npc.getAtkSpeed()) {
							npc.setAtkSpeed(attack_interval);
						}
					}
					if (npc.getAtkMagicSpeed() > 0) {
						int dir_spell_interval = sprite.getActionSpeed(ActionCodes.ACTION_SkillAttack);
						if (dir_spell_interval > 0 && dir_spell_interval > npc.getAtkMagicSpeed()) {
							npc.setAtkMagicSpeed(dir_spell_interval);
						}
					}
					if (npc.getSubMagicSpeed() > 0) {
						int nodir_spell_interval = sprite.getActionSpeed(ActionCodes.ACTION_SkillBuff);
						if (nodir_spell_interval > 0 && nodir_spell_interval > npc.getSubMagicSpeed()) {
							npc.setSubMagicSpeed(nodir_spell_interval);
						}
					}
				}
				
				npc.setUndead(L1Undead.fromString(rs.getString("undead")));
				npc.setPoisonAtk(L1PoisonType.fromString(rs.getString("poison_atk")));
				npc.setAgro(Boolean.parseBoolean(rs.getString("is_agro")));
				npc.setAgroPoly(Boolean.parseBoolean(rs.getString("is_agro_poly")));
				npc.setAgroInvis(Boolean.parseBoolean(rs.getString("is_agro_invis")));
				Integer family = _familyTypes.get(rs.getString("family"));
				npc.setFamily(family == null ? 0 : family.intValue());
				int agrofamily = rs.getInt("agrofamily");
				npc.setAgroFamily((npc.getFamily() == 0 && agrofamily == 1) ? 0 : agrofamily);
				npc.setAgroGfxId1(rs.getInt("agrogfxid1"));
				npc.setAgroGfxId2(rs.getInt("agrogfxid2"));
				npc.setPicupItem(Boolean.parseBoolean(rs.getString("is_picupitem")));
				npc.setDigestItem(rs.getInt("digestitem"));
				npc.setBraveSpeed(Boolean.parseBoolean(rs.getString("is_bravespeed")));
				npc.setHprInterval(rs.getInt("hprinterval"));
				npc.setHpr(rs.getInt("hpr"));
				npc.setMprInterval(rs.getInt("mprinterval"));
				npc.setMpr(rs.getInt("mpr"));
				npc.setTeleport(Boolean.parseBoolean(rs.getString("is_teleport")));
				npc.setRandomLevel(rs.getInt("randomlevel"));
				npc.setRandomHp(rs.getInt("randomhp"));
				npc.setRandomMp(rs.getInt("randommp"));
				npc.setRandomAc(rs.getInt("randomac"));
				npc.setRandomExp(rs.getInt("randomexp"));
				npc.setRandomAlign(rs.getInt("randomAlign"));
				npc.setDamageReduction(rs.getInt("damage_reduction"));
				npc.setHard(Boolean.parseBoolean(rs.getString("is_hard")));
				npc.setBossMonster(Boolean.parseBoolean(rs.getString("is_bossmonster")));
				npc.setTurnUndead(Boolean.parseBoolean(rs.getString("can_turnundead")));
				npc.setBowSpriteId(rs.getInt("bowSpritetId"));
				npc.setKarma(rs.getInt("karma"));
				npc.setTransformId(rs.getInt("transform_id"));
				npc.setTransformGfxId(rs.getInt("transform_gfxid"));
				npc.setLightSize(rs.getInt("light_size"));
				npc.setAmountFixed(Boolean.parseBoolean(rs.getString("is_amount_fixed")));
				npc.setChangeHead(Boolean.parseBoolean(rs.getString("is_change_head")));
				npc.setDoor(rs.getInt("spawnlist_door"));
				npc.setCountId(rs.getInt("count_map"));
				npc.setCantResurrect(Boolean.parseBoolean(rs.getString("cant_resurrect")));
				npc.setHide(Boolean.parseBoolean(rs.getString("isHide")));
				int classId = rs.getInt("classId");
				
				CommonNPCInfo bin = classId > 0 ? NpcCommonBinLoader.getCommonBinInfo(classId) : NpcCommonBinLoader.getCommonBinInfo(npc.getDesc(), npc.getSpriteId());
				if (bin != null) {
					classId = bin.get_class_id();
					npc.setLevel(bin.get_level());
					if (bin.get_hp() > 0) {
						npc.setHp(bin.get_hp());
					}
					if (bin.get_mp() > 0) {
						npc.setMp(bin.get_mp());
					}
					npc.setAc(bin.get_ac());
					npc.setStr((byte) bin.get_str());
					npc.setCon((byte) bin.get_con());
					npc.setDex((byte) bin.get_dex());
					npc.setWis((byte) bin.get_wis());
					npc.setInt((byte) bin.get_int());
					npc.setMr(bin.get_mr());
					npc.setAlignment(bin.get_alignment());
					npc.setBig(bin.get_big());
					npc.setTurnUndead(bin.get_can_turnundead());
				}
				
				if (classId == 0) {
					classId = getDefaultClassId(npc);
				}
				npc.setClassId(classId);
				npc.setBin(bin);
				_npcs.put(npcId, npc);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private int getDefaultClassId(L1Npc npc){
		if (npc.isTurnUndead()) {
			return 25;// 턴언데드 몬스터 추가될 시 해골 classid(PSS인식)
		}
		if (npc.getImpl().equals(DOPPEL_IMPL_STRING)) {
			return 560;// 도펠겡어 몬스터 추가될 시 도펠겡어 classid(PSS인식)
		}
		return 0;
	}

	public L1Npc getTemplate(int id) {
		return _npcs.get(id);
	}
	
	public ArrayList<L1Npc> getTemplateToClassIdList(int classId) {
		ArrayList<L1Npc> list = new ArrayList<L1Npc>();
		for (L1Npc npc : _npcs.values()) {
			if (npc.getClassId() == classId) {
				list.add(npc);
			}
		}
		return list;
	}
	
	public Collection<L1Npc> getAllTemplate() {
		return _npcs.values();
	}

	public L1NpcInstance newNpcInstance(int id) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, IllegalArgumentException {
		L1Npc npcTemp = getTemplate(id);
		if (npcTemp == null)
			throw new IllegalArgumentException(String.format("NpcTemplate: %d not found", id));
		Constructor<?> con = Class.forName("l1j.server.server.model.Instance." + npcTemp.getImpl() + "Instance").getConstructors()[0];
		return (L1NpcInstance) con.newInstance(new Object[] { npcTemp });
	}

	public static Map<String, Integer> buildFamily() {
		Map<String, Integer> result	= new HashMap<String, Integer>();
		Connection con				= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT DISTINCT(family) AS family FROM npc WHERE NOT TRIM(family) =''");
			rs		= pstm.executeQuery();
			int id	= 1;
			while(rs.next()){
				String family = rs.getString("family");
				result.put(family, id++);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return result;
	}

	public int findNpcIdByName(String name) {
		for (L1Npc npc : _npcs.values()) {
			if (npc.getDescKr().equals(name) || npc.getDescEn().equals(name)) {
				return npc.getNpcId();
			}
		}
		return 0;
	}

	public int findNpcIdByNameWithoutSpace(String name) {
		for (L1Npc npc : _npcs.values()) {
			if (npc.getDescKr().replace(StringUtil.EmptyOneString, StringUtil.EmptyString).equals(name) || 
			    npc.getDescEn().replace(StringUtil.EmptyOneString, StringUtil.EmptyString).equals(name)) {
				return npc.getNpcId();
			}
		}
		return 0;
	}
	
	public static void reload() {
		NpcTable oldInstance = _instance;
		_instance = new NpcTable();
		oldInstance._npcs.clear();
		oldInstance = null;
	}
}
