package l1j.server.server.templates;

import l1j.server.GameSystem.beginnerquest.bean.L1QuestDropItem;
import l1j.server.GameSystem.beginnerquest.bean.L1QuestKillNpc;
import l1j.server.common.bin.npc.CommonNPCInfo;
import l1j.server.server.construct.L1Attr;
import l1j.server.server.construct.L1Undead;
import l1j.server.server.datatables.NpcInfoTable.NpcInfoData;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.poison.L1PoisonType;

public class L1Npc extends L1Object implements Cloneable {
	private static final long serialVersionUID = 1L;

	@Override
	public L1Npc clone() {
		try {
			return (L1Npc) (super.clone());
		} catch (CloneNotSupportedException e) {
			throw (new InternalError(e.getMessage()));
		}
	}

	public L1Npc() {
	}

	private int _npcid;
	public int getNpcId() {
		return _npcid;
	}
	public void setNpcId(int i) {
		_npcid = i;
	}

	private String _descKr;
	public String getDescKr() {
		return _descKr;
	}
	public void setDescKr(String val) {
		_descKr = val;
	}

	private String _descEn;
	public String getDescEn() {
		return _descEn;
	}
	public void setDescEn(String val) {
		_descEn = val;
	}	
	
	private String _desc;
	public String getDesc() {
		return _desc;
	}
	public void setDesc(String val) {
		_desc = val;
	}

	private String _impl;
	public String getImpl() {
		return _impl;
	}
	public void setImpl(String s) {
		_impl = s;
	}

	private int _level;
	public int getLevel() {
		return _level;
	}
	public void setLevel(int i) {
		_level = i;
	}

	private int _hp;
	public int getHp() {
		return _hp;
	}
	public void setHp(int i) {
		_hp = i;
	}

	private int _mp;
	public int getMp() {
		return _mp;
	}
	public void setMp(int i) {
		_mp = i;
	}

	private int _ac;
	public int getAc() {
		return _ac;
	}
	public void setAc(int i) {
		_ac = i;
	}

	private byte _str;
	public byte getStr() {
		return _str;
	}
	public void setStr(byte i) {
		_str = i;
	}

	private byte _con;
	public byte getCon() {
		return _con;
	}
	public void setCon(byte i) {
		_con = i;
	}

	private byte _dex;
	public byte getDex() {
		return _dex;
	}
	public void setDex(byte i) {
		_dex = i;
	}

	private byte _wis;
	public byte getWis() {
		return _wis;
	}
	public void setWis(byte i) {
		_wis = i;
	}

	private byte _int;
	public byte getInt() {
		return _int;
	}
	public void setInt(byte i) {
		_int = i;
	}

	private int _mr;
	public int getMr() {
		return _mr;
	}
	public void setMr(int i) {
		_mr = i;
	}

	private int _exp;
	public int getExp() {
		return _exp;
	}
	public void setExp(int val) {
		_exp = val;
	}

	private int _align;
	public int getAlignment() {
		return _align;
	}
	public void setAlignment(int val) {
		_align = val;
	}

	private boolean _big;
	public boolean isBig() {
		return _big;
	}
	public void setBig(boolean flag) {
		_big = flag;
	}

	private L1Attr _weakAttr;
	public L1Attr getWeakAttr() {
		return _weakAttr;
	}
	public void setWeakAttr(L1Attr i) {
		_weakAttr= i;
	}
	
	private int _ranged;
	public int getRanged() {
		return _ranged;
	}
	public void setRanged(int i) {
		_ranged = i;
	}

	private boolean _tameable;
	public boolean isTamingable() {
		return _tameable;
	}
	public void setTamingable(boolean flag) {
		_tameable = flag;
	}

	private int _passispeed;
	public int getPassiSpeed() {
		return _passispeed;
	}
	public void setPassiSpeed(int i) {
		_passispeed = i;
	}

	private int _atkspeed;
	public int getAtkSpeed() {
		return _atkspeed;
	}
	public void setAtkSpeed(int i) {
		_atkspeed = i;
	}
	
	private int _atkMagicSpeed;
	public int getAtkMagicSpeed() {
		return _atkMagicSpeed;
	}
	public void setAtkMagicSpeed(int atkMagicSpeed) {
		_atkMagicSpeed = atkMagicSpeed;
	}

	private int _subMagicSpeed;
	public int getSubMagicSpeed() {
		return _subMagicSpeed;
	}
	public void setSubMagicSpeed(int subMagicSpeed) {
		_subMagicSpeed = subMagicSpeed;
	}

	private int _spriteId;
	public int getSpriteId() {
		return _spriteId;
	}
	public void setSpriteId(int i) {
		_spriteId = i;
	}

	private L1Undead _undead;
	public L1Undead getUndead() {
		return _undead;
	}
	public void setUndead(L1Undead i) {
		_undead = i;
	}

	private L1PoisonType _poisonatk;
	public L1PoisonType getPoisonAtk() {
		return _poisonatk;
	}
	public void setPoisonAtk(L1PoisonType i) {
		_poisonatk = i;
	}
	
	private boolean _agro;
	public boolean isAgro() {
		return _agro;
	}
	public void setAgro(boolean flag) {
		_agro = flag;
	}
	
	private boolean _agro_poly;
	public boolean isAgroPoly() {
		return _agro_poly;
	}
	public void setAgroPoly(boolean flag) {
		_agro_poly = flag;
	}

	private boolean _agro_invis;
	public boolean isAgroInvis() {
		return _agro_invis;
	}
	public void setAgroInvis(boolean flag) {
		_agro_invis = flag;
	}

	private int _family;
	public int getFamily() {
		return _family;
	}
	public void setFamily(int i) {
		_family = i;
	}

	private int _agrofamily;
	public int getAgroFamily() {
		return _agrofamily;
	}
	public void setAgroFamily(int i) {
		_agrofamily = i;
	}

	private int _agrogfxid1;
	public int isAgroGfxId1() {
		return _agrogfxid1;
	}
	public void setAgroGfxId1(int i) {
		_agrogfxid1 = i;
	}

	private int _agrogfxid2;
	public int isAgroGfxId2() {
		return _agrogfxid2;
	}
	public void setAgroGfxId2(int i) {
		_agrogfxid2 = i;
	}

	private boolean _picupitem;
	public boolean isPicupItem() {
		return _picupitem;
	}
	public void setPicupItem(boolean flag) {
		_picupitem = flag;
	}

	private int _digestitem;
	public int getDigestItem() {
		return _digestitem;
	}
	public void setDigestItem(int i) {
		_digestitem = i;
	}

	private boolean _bravespeed;
	public boolean isBraveSpeed() {
		return _bravespeed;
	}
	public void setBraveSpeed(boolean flag) {
		_bravespeed = flag;
	}

	private int _hprinterval;
	public int getHprInterval() {
		return _hprinterval;
	}
	public void setHprInterval(int i) {
		_hprinterval = i;
	}

	private int _hpr;
	public int getHpr() {
		return _hpr;
	}
	public void setHpr(int i) {
		_hpr = i;
	}

	private int _mprinterval;
	public int getMprInterval() {
		return _mprinterval;
	}
	public void setMprInterval(int i) {
		_mprinterval = i;
	}

	private int _mpr;
	public int getMpr() {
		return _mpr;
	}
	public void setMpr(int i) {
		_mpr = i;
	}

	private boolean _teleport;
	public boolean isTeleport() {
		return _teleport;
	}
	public void setTeleport(boolean flag) {
		_teleport = flag;
	}

	private int _randomLevel;
	public int getRandomLevel() {
		return _randomLevel;
	}
	public void setRandomLevel(int val) {
		_randomLevel = val;
	}

	private int _randomHp;
	public int getRandomHp() {
		return _randomHp;
	}
	public void setRandomHp(int val) {
		_randomHp = val;
	}

	private int _randomMp;
	public int getRandomMp() {
		return _randomMp;
	}
	public void setRandomMp(int val) {
		_randomMp = val;
	}

	private int _randomAc;
	public int getRandomAc() {
		return _randomAc;
	}
	public void setRandomAc(int val) {
		_randomAc = val;
	}

	private int _randomExp;
	public int getRandomExp() {
		return _randomExp;
	}
	public void setRandomExp(int val) {
		_randomExp = val;
	}

	private int _randomAlign;
	public int getRandomAlign() {
		return _randomAlign;
	}
	public void setRandomAlign(int val) {
		_randomAlign = val;
	}

	private int _damagereduction;
	public int getDamageReduction() {
		return _damagereduction;
	}
	public void setDamageReduction(int i) {
		_damagereduction = i;
	}

	private boolean _hard;
	public boolean isHard() {
		return _hard;
	}
	public void setHard(boolean flag) {
		_hard = flag;
	}
	
	private boolean _bossmonster;
	public boolean isBossMonster() {
		return _bossmonster;
	}
	public void setBossMonster(boolean flag) {
		_bossmonster = flag;
	}

	private boolean _turnUndead;
	public boolean isTurnUndead() {
		return _turnUndead;
	}
	public void setTurnUndead(boolean flag) {
		_turnUndead = flag;
	}

	private int bowSpritetId;
	public int getBowSpriteId() {
		return bowSpritetId;
	}
	public void setBowSpriteId(int i) {
		bowSpritetId = i;
	}

	private int _karma;
	public int getKarma() {
		return _karma;
	}
	public void setKarma(int i) {
		_karma = i;
	}

	private int _transformId;
	public int getTransformId() {
		return _transformId;
	}
	public void setTransformId(int transformId) {
		_transformId = transformId;
	}

	private int _transformGfxId;
	public int getTransformGfxId() {
		return _transformGfxId;
	}
	public void setTransformGfxId(int i) {
		_transformGfxId = i;
	}

	private int _lightSize;
	public int getLightSize() {
		return _lightSize;
	}
	public void setLightSize(int lightSize) {
		_lightSize = lightSize;
	}

	private boolean _amountFixed;
	public boolean isAmountFixed() {
		return _amountFixed;
	}
	public void setAmountFixed(boolean fixed) {
		_amountFixed = fixed;
	}

	private boolean _changeHead;
	public boolean isChangeHead() {
		return _changeHead;
	}
	public void setChangeHead(boolean changeHead) {
		_changeHead = changeHead;
	}
	
	private int doorId;
	public void setDoor(int doorId){
		this.doorId = doorId;
	}
	public int getDoor(){
		return doorId;
	}
	
	private int countId;
	public void setCountId(int countId){
		this.countId = countId;
	}
	public int getCountId(){
		return countId;
	}
	
	private boolean _isCantResurrect;
	public boolean isCantResurrect() {
		return _isCantResurrect;
	}
	public void setCantResurrect(boolean isCantResurrect) {
		_isCantResurrect = isCantResurrect;
	}
	
	private int _classId;
	public void setClassId(int classId){
		this._classId = classId;
	}
	public int getClassId(){
		return _classId;
	}
	
	private NpcInfoData info;
	public NpcInfoData getInfo() {
		return info;
	}
	public void setInfo(NpcInfoData info) {
		this.info = info;
	}
	
	private CommonNPCInfo bin;
	public CommonNPCInfo getBin() {
		return bin;
	}
	public void setBin(CommonNPCInfo val) {
		bin = val;
	}
	
	private L1QuestKillNpc questKillNpc;
	public L1QuestKillNpc getQuestKillNpc() {
		return questKillNpc;
	}
	public void setQuestKillNpc(L1QuestKillNpc questKillNpc) {
		this.questKillNpc = questKillNpc;
	}
	
	private L1QuestDropItem questDropNpc;
	public L1QuestDropItem getQuestDropNpc() {
		return questDropNpc;
	}
	public void setQuestDropNpc(L1QuestDropItem questDropNpc) {
		this.questDropNpc = questDropNpc;
	}
	
	private int _bookId = 0;
	public int getBookId(){
		return _bookId;
	}
	public void setBookId(int i){
		_bookId = i;
	}
	
	private boolean _isNotification;
	public boolean isNotification(){
		return _isNotification;
	}
	public void setNotification(boolean flag){
		_isNotification = flag;
	}

	private boolean _chat;
	public boolean isChat(){
		return _chat;
	}
	public void setChat(boolean flag){
		_chat = flag;
	}
	
	private boolean _hide;
	public boolean isHide() {
		return _hide;
	}
	public void setHide(boolean val) {
		_hide = val;
	}
}

