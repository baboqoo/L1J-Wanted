package l1j.server.server.templates;

import java.sql.Timestamp;
import java.util.Map;

import javolution.util.FastMap;
import l1j.server.common.bin.companion.CompanionT;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.utils.IntRange;

public class L1Pet {
    private int itemObjId;
    private int objId;
    private int npcId;
    private L1Npc npc;
    private String name;
    private int level;
    private long exp;
    private int maxHp;
    private int currentHp;
    private int friend_ship_marble;
    private int friend_ship_guage;
    private int add_str;
    private int add_con;
    private int add_int;
    private int remain_stats;
    private int elixir_use_count;
    private boolean dead;
    private boolean isOblivion;
    private int tier;
    private Map<Integer, Integer> wild;
    private boolean is_summoned;
    private int lessExp;
    private Timestamp traningTime;
    private CompanionT.ClassInfoT.ClassT classT;

    public int getItemObjId() {
        return itemObjId; 
    } 
    public void setItemObjId(int i) { 
	    itemObjId = i; 
    }

    public int getObjId() {
        return objId; 
    } 
    public void setObjId(int i) {
	    objId = i; 
    }

    public int getNpcId() {
        return npcId; 
    } 
    public void setNpcId(int i) {
    	npcId = i; 
    }
    
    public L1Npc getNpc() {
    	return npc;
    }
    public void setNpc(L1Npc npc) {
    	this.npc = npc;
    }

    public String getName() {
        return name; 
    } 
    public void setName(String s) { 
    	name = s; 
    }

    public int getLevel() {
        return level; 
    } 
    public void setLevel(int i) { 
    	level = IntRange.ensure(i, 1, 80); 
    } 
    public void addLevel(int i) { 
    	level = IntRange.ensure(level + i, 1, 80); 
    }

    public long getExp() {
        return exp; 
    } 
    public void setExp(long i) { 
    	exp = Math.max(0, i); 
    } 
    public void addExp(long i) { 
    	exp = Math.max(0, exp + i); 
    }

    public int getMaxHp() {
        return maxHp; 
    } 
    public void setMaxHp(int i) { 
    	maxHp = Math.max(1, i); 
    } 
    public void addMaxHp(int i) { 
    	maxHp = IntRange.ensure(maxHp + i, 1, 32767); 
    }

    public int getCurrentHp() {
        return currentHp; 
    } 
    public void setCurrentHp(int i) { 
    	currentHp = IntRange.ensure(i, 0, maxHp); 
    } 
    public void addCurrentHp(int i) { 
    	currentHp = IntRange.ensure(currentHp + i, 0, maxHp); 
    }

    public int get_friend_ship_marble() {
        return friend_ship_marble; 
    } 
    public void set_friend_ship_marble(int i) { 
    	friend_ship_marble = IntRange.ensure(i, 0, 10000000); 
    } 
    public void add_friend_ship_marble(int i) { 
    	friend_ship_marble = IntRange.ensure(friend_ship_marble + i, 0, 10000000); 
    }

    public int get_friend_ship_guage() {
        return friend_ship_guage; 
    } 
    public void set_friend_ship_guage(int i) { 
    	friend_ship_guage = IntRange.ensure(i, 0, 100000); 
    } 
    public void add_friend_ship_guage(int i) { 
    	friend_ship_guage = IntRange.ensure(friend_ship_guage + i, 0, 100000); 
    }
    
    public int getAddStr() {
		return add_str;
	}
	public void setAddStr(int add_str) {
		this.add_str = add_str;
	}
	public void addAddStr(int add_str) {
		this.add_str += add_str;
	}
    
    public int getAddCon() {
		return add_con;
	}
	public void setAddCon(int add_con) {
		this.add_con = add_con;
	}
	public void addAddCon(int add_con) {
		this.add_con += add_con;
	}
    
    public int getAddInt() {
		return add_int;
	}
	public void setAddInt(int add_int) {
		this.add_int = add_int;
	}
	public void addAddInt(int add_int) {
		this.add_int += add_int;
	}
	
	public int get_remain_stats() {
        return remain_stats; 
    } 
    public void set_remain_stats(int i) { 
    	remain_stats = Math.max(0, i); 
    } 
    public void add_remain_stats(int i) { 
    	remain_stats = Math.max(0, remain_stats + i); 
    }

    public int get_elixir_use_count() {
        return elixir_use_count; 
    } 
    public void set_elixir_use_count(int i) { 
    	elixir_use_count = Math.max(0, i); 
    } 
    public void add_elixir_use_count(int i) { 
    	elixir_use_count = Math.max(0, elixir_use_count + i); 
    }

    public boolean isDead() {
        return dead; 
    } 
    public void setDead(boolean f) { 
    	dead = f; 
    }
    
    public boolean isOblivion() {
        return isOblivion; 
    } 
    public void setOblivion(boolean val) { 
    	isOblivion = val; 
    }
    
    public int getTier() {
		return tier;
	}
	public void setTier(int tier) {
		this.tier = tier;
	}
	
	public Map<Integer, Integer> getWild() {
        return wild; 
    } 
    public void setWild(Map<Integer, Integer> map) { 
    	wild = map; 
    }

    public boolean is_summoned() {
        return is_summoned; 
    } 
    public void set_summoned(boolean val) { 
    	is_summoned = val; 
    }

    public int getLessExp() {
        return lessExp; 
    } 
    public void setLessExp(int i) { 
    	lessExp = i; 
    }

    public Timestamp getTraningTime() {
        return traningTime; 
    } 
    public void setTraningTime(Timestamp t) { 
    	traningTime = t;
    }
    
    public CompanionT.ClassInfoT.ClassT getClassT() {
    	return classT;
    }
    public void setClassT(CompanionT.ClassInfoT.ClassT val) {
    	classT = val;
    }
    
    public void newWild() {
        Map<Integer, Integer> map = new FastMap<Integer, Integer>();
        setWild(map);
        settingWild(map, getTier());
    }
    
    public void openWild(L1PetInstance companion) {
        settingWild(companion.getWild(), getTier());
    }
	
	void settingWild(Map<Integer, Integer> map, int tier) {
    	for (CompanionT.ClassInfoT.ClassT.SkillT skillT : classT.get_Skill()) {
    		if (skillT.get_tier() != tier) {
    			continue;
    		}
    		for (int skill_id : skillT.get_skillId()) {
    			map.put(skill_id, 0);
    		}
    	}
    }
}
