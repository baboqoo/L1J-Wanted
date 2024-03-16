package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javolution.util.FastMap;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.bin.CompanionCommonBinLoader;
import l1j.server.common.bin.companion.CompanionT;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.utils.QueryUtil;
import l1j.server.server.utils.SQLUtil;

/**
 * 캐릭터 펫 정보
 * @author LinOffice
 */
public class CharacterCompanionTable {
    private Map<Integer, L1Pet> map;
    private static CharacterCompanionTable _instance = new CharacterCompanionTable();
    
    private Set<String> _defaultNames = new HashSet<String>();
    
    private CharacterCompanionTable() {
        map = new FastMap<Integer, L1Pet>();
        load();
    }
    
    private void load() {
    	Connection con = null;
    	PreparedStatement pstm = null;
    	ResultSet rs = null;
        try {
        	con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT * FROM character_companion");
            rs = pstm.executeQuery();
            while(rs.next()){
                int itemobjid			= rs.getInt("item_objId");
                int objid				= rs.getInt("objid");
                String name				= rs.getString("name");
                int npcId				= rs.getInt("npcId");
                int level				= rs.getInt("level");
                int exp					= rs.getInt("exp");
                int maxHp				= rs.getInt("maxHp");
                int currentHp			= rs.getInt("currentHp");
                int friend_ship_marble	= rs.getInt("friend_ship_marble");
                int friend_ship_guage	= rs.getInt("friend_ship_guage");
                int add_str				= rs.getInt("add_str");
                int add_con				= rs.getInt("add_con");
                int add_int				= rs.getInt("add_int");
                int remain_stats		= rs.getInt("remain_stats");
                int elixir_use_count	= rs.getInt("elixir_use_count");
                boolean dead			= rs.getBoolean("dead");
                boolean oblivion		= rs.getBoolean("oblivion");
                int tier				= rs.getInt("tier");
                byte[] wild				= rs.getBytes("wild");
                int lessExp				= rs.getInt("lessExp");
                Timestamp traningTime	= rs.getTimestamp("traningTime");
                CompanionT.ClassInfoT.ClassT companionT = CompanionCommonBinLoader.getClass(NpcTable.getInstance().getTemplate(npcId).getClassId());
                newPet(itemobjid, objid, name, npcId, level, exp, maxHp, currentHp, friend_ship_marble, friend_ship_guage, 
                		add_str, add_con, add_int,
                		remain_stats, elixir_use_count, dead, oblivion, tier, wild, lessExp, traningTime, companionT);
                _defaultNames.add(name.toLowerCase());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	SQLUtil.close(rs, pstm, con);
        }
    }
    
    public L1Pet NewPetAdd(L1Npc npc, CompanionT.ClassInfoT.ClassT classT, int itemObjId, int petObjNumber) {
    	/*L1Pet pet = newPet(itemObjId, petObjNumber, npc.getDescKr(), npc.getNpcId(), 1, 0, 160, 160, 0, 0, 
    			0, 0, 0, 
    			0, 0, false, false, 1, null, 0, null, classT);*/
        L1Pet pet = newPet(itemObjId, petObjNumber, npc.getDescEn(), npc.getNpcId(), 1, 0, 160, 160, 0, 0, 
    			0, 0, 0, 
    			0, 0, false, false, 1, null, 0, null, classT);                
        byte[] wild = WildData(pet.getWild());
        QueryUtil.execute("INSERT INTO character_companion SET item_objId=?, objid=?, name=?, npcId=?, level=?, exp=?, maxHp=?, currentHp=?, "
        		+ "friend_ship_marble=?, friend_ship_guage=?, "
        		+ "add_str=?, add_con=?, add_int=?, "
        		+ "remain_stats=?, elixir_use_count=?, dead=?, oblivion=?, tier=?, wild=?, lessExp=?, traningTime=?", 
        		new Object[] { pet.getItemObjId(), pet.getObjId(), pet.getName(), pet.getNpcId(), pet.getLevel(), pet.getExp(), pet.getMaxHp(), pet.getCurrentHp(), 
        				pet.get_friend_ship_marble(), pet.get_friend_ship_guage(), 
        				pet.getAddStr(), pet.getAddCon(), pet.getAddInt(),
        				pet.get_remain_stats(), pet.get_elixir_use_count(), pet.isDead(), pet.isOblivion(), pet.getTier(), wild, pet.getLessExp(), pet.getTraningTime() });
        return pet;
    }
    
    public void ChangeName(L1Pet pet) {
        QueryUtil.execute("UPDATE character_companion SET name=? WHERE item_objId=?", 
        		new Object[] { pet.getName(), pet.getItemObjId() });
    }
    
    public void ChangeStat(L1Pet pet) {
        QueryUtil.execute("UPDATE character_companion SET add_str=?, add_con=?, add_int=?, remain_stats=?, elixir_use_count=? WHERE item_objId=?", 
        		new Object[] { pet.getAddStr(), pet.getAddCon(), pet.getAddInt(), pet.get_remain_stats(), pet.get_elixir_use_count(), pet.getItemObjId() });
    }
    
    public void oblivionSave(L1Pet pet) {
        QueryUtil.execute("UPDATE character_companion SET oblivion=? WHERE item_objId=?", 
        		new Object[] { pet.isOblivion(), pet.getItemObjId() });
    }
    
    public void tierSave(L1Pet pet) {
        QueryUtil.execute("UPDATE character_companion SET tier=? WHERE item_objId=?", 
        		new Object[] { pet.getTier(), pet.getItemObjId() });
    }
    
    public void WildSave(L1Pet pet) {
        byte[] wild = WildData(pet.getWild());
        QueryUtil.execute("UPDATE character_companion SET wild=? WHERE item_objId=?", 
        		new Object[] { wild, pet.getItemObjId() });
    }
    
    public void LessExpSave(L1Pet pet) {
        QueryUtil.execute("UPDATE character_companion SET exp=?, lessExp=? WHERE item_objId=?", 
        		new Object[] { pet.getExp(), pet.getLessExp(), pet.getItemObjId() });
    }
    
    public void TraningSave(L1Pet pet) {
        pet.setTraningTime(new Timestamp(System.currentTimeMillis()));
        QueryUtil.execute("UPDATE character_companion SET exp=?, lessExp=?, traningTime=? WHERE item_objId=?", 
        		new Object[] { pet.getExp(), pet.getLessExp(), pet.getTraningTime(), pet.getItemObjId() });
    }
    
    public void saveAll(L1PetInstance companion) {
    	L1Pet pet = companion.getPetInfo();
        pet.setLevel(companion.getLevel());
        pet.setExp((int)companion.getExp());
        pet.setMaxHp(companion.getMaxHp());
        pet.setCurrentHp(companion.getCurrentHp());
        byte[] wild = this.WildData(pet.getWild());
        QueryUtil.execute("UPDATE character_companion SET level=?, exp=?, maxHp=?, currentHp=?, "
        		+ "friend_ship_marble=?, friend_ship_guage=?, "
        		+ "add_str=?, add_con=?, add_int=?, "
        		+ "remain_stats=?, elixir_use_count=?, dead=?, oblivion=?, tier=?, wild=?, lessExp=?, traningTime=? WHERE item_objId=?", 
        		new Object[] { 
        				pet.getLevel(), pet.getExp(), pet.getMaxHp(), pet.getCurrentHp(), 
        				pet.get_friend_ship_marble(), pet.get_friend_ship_guage(), 
        				pet.getAddStr(), pet.getAddCon(), pet.getAddInt(),
        				pet.get_remain_stats(), pet.get_elixir_use_count(), pet.isDead(), pet.isOblivion(), pet.getTier(), wild, pet.getLessExp(), pet.getTraningTime(), pet.getItemObjId() });
    }
    
    private byte[] WildData(Map<Integer, Integer> map) {
        if (map == null || map.isEmpty()) {
        	return null;
        }
        byte[] data = new byte[map.size() << 1];
        int index = 0;
        for (int i : map.keySet()) {
            int j = map.get(i);
            data[index++] = (byte)i;
            data[index++] = (byte)j;
        }
        return data;
    }
    
    private L1Pet newPet(int itemobjid, int objid, String name, int npcId, int level, int exp, 
    		int maxHp, int currentHp, int friend_ship_marble, int friend_ship_guage, 
    		int add_str, int add_con, int add_int,
    		int remain_stats, int elixir_use_count, boolean dead, boolean oblivion, int tier, byte[] wild,
    		int lessExp, Timestamp traningTime, CompanionT.ClassInfoT.ClassT classT) {
    	L1Pet pet = new L1Pet();
        pet.setItemObjId(itemobjid);
        pet.setObjId(objid);
        pet.setName(name);
        pet.setNpcId(npcId);
        pet.setNpc(NpcTable.getInstance().getTemplate(npcId));
        pet.setLevel(level);
        pet.setExp(exp);
        pet.setMaxHp(maxHp);
        pet.setCurrentHp(currentHp);
        pet.set_friend_ship_marble(friend_ship_marble);
        pet.set_friend_ship_guage(friend_ship_guage);
        pet.setAddStr(add_str);
        pet.setAddCon(add_con);
        pet.setAddInt(add_int);
        pet.set_remain_stats(remain_stats);
        pet.set_elixir_use_count(elixir_use_count);
        pet.setDead(dead);
        pet.setOblivion(oblivion);
        pet.setLessExp(lessExp);
        pet.setTraningTime(traningTime);
        pet.setClassT(classT);
        pet.setTier(tier);
        if (wild == null) {
        	pet.newWild();
        } else {
            Map<Integer, Integer> map = new FastMap<Integer, Integer>();
            pet.setWild(map);
            for (int i = 0; i < wild.length; ++i) {
                int skill_id = wild[i];
                int enchant = wild[++i];
                map.put(skill_id, enchant);
            }
        }
        map.put(itemobjid, pet);
        return pet;
    }
    
    public void deletePet(int itemobjid) {
    	L1Pet pet = getTemplate(itemobjid);
    	if (pet == null) {
    		return;
    	}
    	CharacterCompanionBuffTable.deleteBuff(pet.getObjId());
        QueryUtil.execute("DELETE FROM character_companion WHERE item_objId=?", new Object[] { itemobjid });
        map.remove(itemobjid);
    }
    
    public static boolean isNameExists(String nameCaseInsensitive) {
        String nameLower = nameCaseInsensitive.toLowerCase();
        Connection con = null;
        PreparedStatement pstm = null;
        try {
        	con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT item_objId FROM character_companion WHERE LOWER(name)=?");
            pstm.setString(1, nameLower);
            try (           		
            	ResultSet rs = pstm.executeQuery()) {
                if (!rs.next()) {
                	return false;
                }
                if (CharacterCompanionTable.getInstance().isNameDefault(nameLower)) {
                	return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
        } finally {
        	SQLUtil.close(pstm, con);
        }
        return true;
    }
    
    public boolean isNameDefault(String name) {
		return _defaultNames.contains(name.toLowerCase());
	}
    
    public L1Pet getTemplate(int itemObjId) {
        return map.get(itemObjId);
    }
    
    public static CharacterCompanionTable getInstance() {
        return CharacterCompanionTable._instance;
    }
    
}

