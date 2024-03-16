package l1j.server.server.model;

import l1j.server.Config;
import l1j.server.GameSystem.freebuffshield.FreeBuffShieldHandler;
import l1j.server.common.data.Gender;
import l1j.server.common.data.eArenaMapKind;
import l1j.server.common.data.ePolymorphAnonymityType;
import l1j.server.server.construct.L1BeginnerQuest;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.FairlyQuestTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.playsupport.S_FinishPlaySupport;
import l1j.server.server.serverpackets.ranking.S_MyRanking;

/**
 * 유저의 설정 관리 클래스
 * @author LinOffice
 */
public class L1CharacterConfig {
	private L1PcInstance owner;
	
	public L1CharacterConfig(L1PcInstance owner) {
		this.owner = owner;
	}
	
	public long window_active_time = -1;
	public int window_noactive_count;
	
	public boolean _dominationTeleportRing, _dominationHeroRing, _dominationPolyRing, _level100PolyRing;
	
	private int all_rank, previous_all_rank, class_rank, previous_class_rank, ranker_rating, class_ranker_rating;// 랭킹
	private S_MyRanking my_rank_ack;
	
	private long craft_batch_time;
	
	private FreeBuffShieldHandler _free_buff_shield;
	
	private int anonymityCnt = 1;
    private String anonymityName;
    private ePolymorphAnonymityType anonimityType;
    private String profileUrl;
	
	public boolean _halpasLoyaltyEnable;
    public boolean _massTeleportState;
    
    public boolean RootMent = true;// 루팅 멘트
    public boolean _enchantGfx = true;// 인첸트 연출
    
    private boolean _isShowWorldChat = true;
    private boolean _isCanWhisper = true;
    private boolean _isShowTradeChat = true;
    private boolean _isGlobalMessege = true;
    private boolean _isIndunInviteOnOff = true;
    
    private boolean _isNotAlignmentPenalty;
    
    private boolean _isFreePVPRegion;
    
    // 플레이 서포트
 	private boolean _playSupport;
 	private int _playSupportType;
 	
 	// 잠재력
 	private int _potentialTargetId, _potentialBonusGrade, _potentialBonusId;
 	
 	private L1ItemInstance _luckyBagOpenResultItem;
 	
 	// 인스턴스 던전
 	public boolean _IndunReady;
 	public boolean _IndunLoginCheck;
 	public boolean _indunAutoMatching;
 	public eArenaMapKind _indunAutoMatchingMapKind;
 	public int _indunAutoMatchingNumber;
 	
 	// 시세 상인 오브젝트
 	private int _findMerchantId;
 	
 	// 보스 레이드
 	private int _bossYN, _bossId;
 	
 	// 수배 설정
 	private int huntCount, huntPrice;
 	private String _reasonToHunt;
 	private int[] beWanted = new int[6];
 	
 	private int _duelLine;// 배틀존
 	
 	// 피어스
    public int[] PiersItemId, PiersEnchant;
    
    private byte[] fairlyInfo;// 페이러 퀘스트
    
    // 바포메트 시스템
    private int _nbapoLevel, _obapoLevel, _bapodmg;
    public int alignAC, alignMR, alignSP, alignAT;
    
    public boolean isShowWorldChat() {
    	return _isShowWorldChat;
    }
    
    public void setShowWorldChat(boolean val) {
    	_isShowWorldChat = val;
    }
    
    public boolean isCanWhisper() {
    	return _isCanWhisper;
    }
    
    public void setCanWhisper(boolean val) {
    	_isCanWhisper = val;
    }
    
    public boolean isShowTradeChat() {
    	return _isShowTradeChat;
    }
    
    public void setShowTradeChat(boolean val) {
    	_isShowTradeChat = val;
    }
    
    public boolean isGlobalMessege() {
    	return _isGlobalMessege;
    }
    
    public void setGlobalMessege(boolean val) {
    	_isGlobalMessege = val;
    }
    
    public void setAnonymityName(){
    	int num = 0;
    	for (L1PcInstance each : L1World.getInstance().getAllPlayers()) {
    		if (each.getType() == owner.getType() && num < each.getConfig().anonymityCnt) {
    			num = each.getConfig().anonymityCnt;
    		}
    	}
    	anonymityName	= String.format("_%04d", ++num);// 익명 설정(4자리: 자릿수만큼 0으로 채움)
    	anonymityCnt	= num;
    }
    
    public String getAnonymityName(){
    	return anonymityName;
    }
    
    public ePolymorphAnonymityType getAnonymityType() {
    	return anonimityType;
    }
    
    public void setAnonymityType(ePolymorphAnonymityType val) {
    	anonimityType = val;
    }
    
    public String getProfileUrl(){
    	return profileUrl;
    }
    
    public void setProfileUrl(){
    	profileUrl = String.format("/img/char/char%d_%s.png", owner.getType(), (owner.getGender() == Gender.MALE ? "m" : "f"));
    }
    
	public boolean isPlaySupport() {
		return _playSupport;
	}
	public void setPlaySupport(boolean val) {
		if (val && owner.getLevel() <= Config.QUEST.BEGINNER_QUEST_LIMIT_LEVEL) {
			owner.getQuest().questProcess(L1BeginnerQuest.PSS);
		}
		_playSupport = val;
	}
	public int getPlaySupportType() {
		return _playSupportType;
	}
	public void setPlaySupportType(int val) {
		_playSupportType = val;
	}
	
	/**
	 * 플레이 서포트를 종료한다.
	 */
	public void finishPlaySupport() {
		if (!_playSupport) {
			return;
		}
		_playSupport = false;
		_playSupportType = 0;
		owner.sendPackets(S_FinishPlaySupport.FINISH);
	}
	
	public boolean isIndunLoginCheck() {
		return _IndunLoginCheck;
	}
	public void setIndunLoginCheck(boolean val) {
		_IndunLoginCheck = val;
	}
	
	public boolean isIndunInviteOnOff() {
    	return _isIndunInviteOnOff;
    }
    
    public void setIndunInviteOnOff(boolean val) {
    	_isIndunInviteOnOff = val;
    }
    
    public int getBossYN() {
    	return _bossYN;
    }
    public void setBossYN(int i) {
    	_bossYN = i;
    }
    
	public void setBossId(int val) {
		_bossId = val;
	}
    public int getBossId() {
    	return _bossId;
    }
	
	public String getReasonToHunt() {
		return _reasonToHunt;
	}
	public void setReasonToHunt(String val) {
		_reasonToHunt = val;
	}

	public int getHuntCount() {
		return huntCount;
	}
	public void setHuntCount(int val) {
		huntCount = val;
	}

	public int getHuntPrice() {
		return huntPrice;
	}
	public void setHuntPrice(int val) {
		huntPrice = val;
	}
	
	public int[] getBeWanted() {
		return beWanted;
	}
	public void setBeWanted(int[] Wanted) {
		beWanted = Wanted;
	}
	public void initBeWanted() {
		owner.ability.addShortDmgup(-beWanted[0]);
		owner.ability.addShortHitup(-beWanted[1]);
		owner.ability.addLongDmgup(-beWanted[2]);
		owner.ability.addLongHitup(-beWanted[3]);
		owner.ability.addSp(-beWanted[4]);
		owner.ability.addDamageReduction(-beWanted[5]);
		for (int i = 0; i < beWanted.length; i++) {
			beWanted[i] = 0;
		}
		owner.sendPackets(new S_OwnCharAttrDef(owner), true);
		owner.sendPackets(new S_OwnCharStatus2(owner), true);
		owner.sendPackets(new S_OwnCharStatus(owner), true);
	}
	public void addBeWanted() {
		owner.ability.addShortDmgup(beWanted[0]);
		owner.ability.addShortHitup(beWanted[1]);
		owner.ability.addLongDmgup(beWanted[2]);
		owner.ability.addLongHitup(beWanted[3]);
		owner.ability.addSp(beWanted[4]);
		owner.ability.addDamageReduction(beWanted[5]);
		owner.sendPackets(new S_OwnCharAttrDef(owner), true);
		owner.sendPackets(new S_OwnCharStatus2(owner), true);
		owner.sendPackets(new S_OwnCharStatus(owner), true);
	}
	
	public int getFindMerchantId() {
		return this._findMerchantId;
	}
	public void setFindMerchantId(int val) {
		this._findMerchantId = val;
	}
	
    public int getDuelLine() {
    	return _duelLine;
    }
    public void setDuelLine(int val) {
    	_duelLine = val;
    }
    
    public byte[] getFairlyInfo() {
    	if (fairlyInfo == null) {
    		fairlyInfo = new byte[512];
    	}
    	return fairlyInfo;
    }
    public void setFairlyInfo(byte[] info) {
    	fairlyInfo = info;
    }
    //public void 페어리경험치보상(int lv) {
	public void elfExpReward(int lv) {
    	long needExp = ExpTable.getNeedExpNextLevel(lv);
    	long addexp = 0;
        addexp = (long) (needExp * 0.01);
        if (addexp != 0) {
            int level = ExpTable.getLevelByExp(owner.getExp() + addexp);
            if (level > 60) {
//AUTO SRM:             	owner.sendPackets(new S_SystemMessage("더이상 경험치를 획득 할 수 없습니다."), true); // CHECKED OK
            	owner.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1100), true), true);
            } else {
            	owner.addExp(addexp);
            }
        }
    }
    //public void 페어리정보저장(int id) {
	public void saveFairlyInfo(int id) {
    	FairlyQuestTable fairly = FairlyQuestTable.getInstance();
        int count = fairly.fairlycount(owner.getId());
        getFairlyInfo()[id] = 1;
        if (count == 0) {
        	fairly.fairlystore(owner.getId(), fairlyInfo);
        } else {
        	fairly.fairlupdate(owner.getId(), fairlyInfo);
        }
    }
    
    public int getBapodmg() {
    	return _bapodmg;
    }
    public void setBapodmg(int val) {
    	_bapodmg = val;
    }

    public int getNBapoLevel() {
    	return _nbapoLevel;
    }
    public void setNBapoLevel(int val) {
    	_nbapoLevel = val;
    }

    public int getOBapoLevel() {
    	return _obapoLevel;
    }
    public void setOBapoLevel(int val) {
    	_obapoLevel = val;
    }
    
    public int get_potential_target_id() {
 		return _potentialTargetId;
 	}
 	
 	public int get_potential_bonus_grade() {
 		return _potentialBonusGrade;
 	}
 	
 	public int get_potential_bonus_id() {
 		return _potentialBonusId;
 	}
 	
 	public void set_potential_enchant(int target_id, int bonus_grade, int bonus_id) {
		_potentialTargetId		= target_id;
		_potentialBonusGrade	= bonus_grade;
		_potentialBonusId		= bonus_id;
 	}
 	
 	public void reset_potential_enchant() {
		_potentialTargetId = _potentialBonusGrade = _potentialBonusId = 0;
 	}
    
    public FreeBuffShieldHandler get_free_buff_shield() {
    	return _free_buff_shield;
    }
    public void set_free_buff_shield(FreeBuffShieldHandler val) {
    	_free_buff_shield = val;
    }
    
    public boolean isNotAlignmentPenalty() {
    	return _isNotAlignmentPenalty;
    }
    public void setNotAlignmentPenalty(boolean val) {
    	_isNotAlignmentPenalty = val;
    }
    
    public int get_all_rank() {
		return all_rank;
	}
	public void set_all_rank(int val) {
		all_rank = val;
	}
	
	public int get_previous_all_rank() {
		return previous_all_rank;
	}
	public void set_previous_all_rank(int val) {
		previous_all_rank = val;
	}
	
	public int get_class_rank() {
		return class_rank;
	}
	public void set_class_rank(int val) {
		class_rank = val;
	}
	
	public int get_previous_class_rank() {
		return previous_class_rank;
	}
	public void set_previous_class_rank(int val) {
		previous_class_rank = val;
	}
	
	public int get_ranker_rating() {
		return ranker_rating;
	}
	public void set_ranker_rating(int val) {
		ranker_rating = val;
	}
	
	public int get_class_ranker_rating() {
		return class_ranker_rating;
	}
	public void set_class_ranker_rating(int val) {
		class_ranker_rating = val;
	}
	
	public boolean is_rank_poly() {
		return class_rank == 1 || (all_rank >= 1 && all_rank <= 20);
	}
	
	public S_MyRanking get_my_rank_ack() {
		return my_rank_ack;
	}
	public void set_my_rank_ack(S_MyRanking val) {
		my_rank_ack = val;
	}
	public void reset_my_rank_ack() {
		if (my_rank_ack != null) {
			my_rank_ack.clear();
			my_rank_ack = null;
		}
	}
	
	public void init_rank() {
		all_rank = previous_all_rank = class_rank = previous_class_rank = ranker_rating = class_ranker_rating = 0;
		reset_my_rank_ack();
	}
    
	public L1ItemInstance getLuckyBagOpenResultItem() {
		return _luckyBagOpenResultItem;
	}
	public void setLuckyBagOpenResultItem(L1ItemInstance val) {
		_luckyBagOpenResultItem = val;
	}
	
	public boolean isFreePVPRegion() {
		return _isFreePVPRegion;
	}
	public void setFreePVPRegion(boolean val) {
		_isFreePVPRegion = val;
	}
	
	public long get_craft_batch_time() {
		return craft_batch_time;
	}
	public void set_craft_batch_time(long val) {
		craft_batch_time = val;
	}
	
}


