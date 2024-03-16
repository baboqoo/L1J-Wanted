package l1j.server.server.model.Instance;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.GameSystem.ai.AiLoader;
import l1j.server.GameSystem.ai.constuct.AiMent;
import l1j.server.GameSystem.beginnerquest.BeginnerQuestUserTable;
import l1j.server.GameSystem.deathpenalty.DeathPenaltyTable;
import l1j.server.GameSystem.deathpenalty.user.DeathPenaltyUser;
import l1j.server.GameSystem.dungeontimer.L1DungeonTimer;
import l1j.server.GameSystem.einhasadfaith.EinhasadFaithHandler;
import l1j.server.GameSystem.einhasadfaith.EinhasadFaithLoader;
import l1j.server.GameSystem.eventpush.EventPushLoader;
import l1j.server.GameSystem.fatigue.L1Fatigue;
import l1j.server.GameSystem.freebuffshield.FreeBuffShieldHandler;
import l1j.server.GameSystem.freebuffshield.FreeBuffShieldLoader;
import l1j.server.GameSystem.huntingquest.user.HuntingQuestUser;
import l1j.server.GameSystem.huntingquest.user.HuntingQuestUserTable;
import l1j.server.GameSystem.shoplimit.ShopLimitLoader;
import l1j.server.GameSystem.shoplimit.ShopLimitUser;
import l1j.server.GameSystem.tjcoupon.TJCouponLoader;
import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.IndunSystem.indun.IndunList;
import l1j.server.IndunSystem.minigame.L1GamblingObject;
import l1j.server.IndunSystem.occupy.OccupyHandler;
import l1j.server.IndunSystem.occupy.OccupyHandler.OccupyPenaltyTimer;
import l1j.server.IndunSystem.occupy.OccupyManager;
import l1j.server.IndunSystem.occupy.OccupyTeam;
import l1j.server.IndunSystem.occupy.OccupyTeamType;
import l1j.server.IndunSystem.occupy.OccupyType;
import l1j.server.LFCSystem.InstanceEnums.InstStatus;
import l1j.server.LFCSystem.InstanceSpace;
//import l1j.server.QuestSystem.Loader.UserWeekQuestLoader;
import l1j.server.RobotSystem.L1RobotAI;
import l1j.server.common.data.Gender;
import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.Account;
import l1j.server.server.ActionCodes;
import l1j.server.server.GameClient;
import l1j.server.server.GameServerSetting;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.command.executor.L1HpBar;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.construct.L1Status;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.controller.HpMpRegenController;
import l1j.server.server.controller.action.War;
import l1j.server.server.datatables.CharBuffTable;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.CharactersGiftItemTable;
import l1j.server.server.datatables.EquipSetTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.PenaltyItemTable;
import l1j.server.server.datatables.PenaltyItemTable.ProtectItemData;
import l1j.server.server.datatables.PenaltyItemTable.ProtectType;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.PolyTable.WeaponeType;
import l1j.server.server.datatables.RevengeTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1Ability;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1CharacterConfig;
import l1j.server.server.model.L1ChatParty;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1EquipmentSet;
//import l1j.server.server.model.L1DeathMatch;
import l1j.server.server.model.L1EquipmentSlot;
import l1j.server.server.model.L1ExpPotion;
//import l1j.server.server.model.L1HauntedHouse;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Karma;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PcSpeedSync;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.L1Quest;
//import l1j.server.server.model.L1Racing;
import l1j.server.server.model.L1StatReset;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1Trade;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.ReportDeley;
import l1j.server.server.model.classes.L1ClassFeature;
import l1j.server.server.model.exp.L1ExpHandler;
import l1j.server.server.model.exp.L1ExpPlayer;
import l1j.server.server.model.gametime.GameTimeCarrier;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.collection.favor.L1FavorBookInventory;
import l1j.server.server.model.item.collection.favor.loader.L1FavorBookUserLoader;
import l1j.server.server.model.item.collection.time.L1TimeCollectionHandler;
import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionUserLoader;
import l1j.server.server.model.item.repeat.ElfArmorBlessing;
import l1j.server.server.model.item.repeat.HalloweenRegeneration;
import l1j.server.server.model.item.repeat.HpRegeneration32SecondByItem;
import l1j.server.server.model.item.repeat.LindArmorBlessing;
import l1j.server.server.model.item.repeat.MpRegeneration16SecondByItem;
import l1j.server.server.model.item.repeat.MpRegeneration64SecondByItem;
import l1j.server.server.model.item.repeat.PapuArmorBlessing;
import l1j.server.server.model.monitor.L1PcAutoUpdate;
import l1j.server.server.model.monitor.L1PcExpMonitor;
import l1j.server.server.model.monitor.L1PcGhostMonitor;
import l1j.server.server.model.monitor.L1PcHellMonitor;
import l1j.server.server.model.monitor.L1PcInvisDelay;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1PassiveSkillHandler;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.model.skill.action.BurningShot;
import l1j.server.server.model.sprite.AcceleratorChecker;
import l1j.server.server.model.warehouse.ClanWarehouse;
import l1j.server.server.model.warehouse.WarehouseManager;
import l1j.server.server.monitor.Logger.ItemActionType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_Alignment;
import l1j.server.server.serverpackets.S_CastleMaster;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_ExpBoostingInfo;
import l1j.server.server.serverpackets.S_HPMeter;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_InstanceHP;
import l1j.server.server.serverpackets.S_Invis;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_PinkName;
import l1j.server.server.serverpackets.S_Poison;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_SiegeInjuryTimeNoti;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.action.S_DoActionShop;
import l1j.server.server.serverpackets.action.S_Fishing;
import l1j.server.server.serverpackets.einhasad.S_RestExpInfoNoti;
import l1j.server.server.serverpackets.einhasad.S_RestGaugeChargeNoti;
import l1j.server.server.serverpackets.einhasadpoint.S_EinhasadPointPointNoti;
import l1j.server.server.serverpackets.indun.S_IndunChangeRoomStatus;
import l1j.server.server.serverpackets.indun.S_IndunExitRoom;
import l1j.server.server.serverpackets.indun.S_IndunExitRoom.ArenaRoomExitResult;
import l1j.server.server.serverpackets.message.S_BlueMessage;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_MessegeNoti;
import l1j.server.server.serverpackets.message.S_MsgAnnounce;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.object.S_NPCObject;
import l1j.server.server.serverpackets.object.S_PCObject;
import l1j.server.server.serverpackets.party.S_PartyMemberStatus;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeEmblem;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeUserInfo;
import l1j.server.server.serverpackets.pledge.S_Pledge;
import l1j.server.server.serverpackets.pledge.S_PledgeWatch;
import l1j.server.server.serverpackets.polymorph.S_Polymorph;
import l1j.server.server.serverpackets.polymorph.S_PolymorphAnonymity;
import l1j.server.server.serverpackets.polymorph.S_PolymorphAnonymityNoti;
import l1j.server.server.serverpackets.returnedstat.S_ReturnedStatStart;
import l1j.server.server.serverpackets.returnedstat.S_StatusBaseNoti;
import l1j.server.server.serverpackets.returnedstat.S_StatusCarryWeightInfoNoti;
import l1j.server.server.serverpackets.returnedstat.S_StatusRenewalInfo;
import l1j.server.server.serverpackets.spell.S_AvailableSpellNoti;
import l1j.server.server.serverpackets.spell.S_SkillIconGFX;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti.SkillIconNotiType;
import l1j.server.server.serverpackets.system.FREE_BUFF_SHIELD_INFO;
import l1j.server.server.serverpackets.system.S_FreeBuffShieldUpdateNoti;
import l1j.server.server.serverpackets.system.S_PCMasterFavorUpdateNoti;
import l1j.server.server.storage.CharactersItemStorage;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1PrivateShopBuyList;
import l1j.server.server.templates.L1PrivateShopSellList;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.types.Point;
import l1j.server.server.utils.CalcChaStat;
import l1j.server.server.utils.CalcConStat;
import l1j.server.server.utils.CalcDexStat;
import l1j.server.server.utils.CalcIntelStat;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.CalcWisStat;
import l1j.server.server.utils.ColorUtil;
import l1j.server.server.utils.DelayClose;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.StringUtil;
//import manager.Manager;  // MANAGER DISABLED
//import manager.ManagerInfoThread;  // MANAGER DISABLED
//import l1j.server.server.serverpackets.S_SummonPack;
//import l1j.server.server.datatables.MonsterBookTable;

public class L1PcInstance extends L1Character implements L1CharacterInfo {
	private static Logger _log					= Logger.getLogger(L1PcInstance.class.getName());
	private static final long serialVersionUID	= 1L;
	private static final Random random			= new Random(System.nanoTime());
	private static final IntRange EINHASAD_POINT_RANGE = new IntRange(0, Config.EIN.EINHASAD_POINT_LIMIT_CHARGE_VALUE);
	
	public static final int REGENSTATE_NONE		= 4;
	public static final int REGENSTATE_MOVE		= 2;
	public static final int REGENSTATE_ATTACK	= 1;
	
	public boolean _isArmorSetPoly;// 세트 아이템 변신
    public boolean _isRuunPaper;// 루운의 초대장 보유 여부
    public boolean _isShipIn;// 운전중인 배 탑승 여부
    public int _isElixerBooster;
    public OccupyTeamType _occupyTeamType;
    public boolean _occupyPenalyFirst;
    public OccupyPenaltyTimer _occupyPenaltyTimer;
    
    public boolean ingame_login_auth_delay, ingame_login_auth;// 웹서버 인게임 로그인 결정 변수
    public String ingame_init_auth_uid;
    
    // 아우라키아 던전(공격 스킬 변수)
    public long lastArrowOfAurakiaTime;
    public boolean isArrowOfAurakiaSkill, successArrowOfAurakia;
    
    public int resetTimerItemId;
    
    // 마법사 스킬 스택 시스템
    public int _spellStackTargetId, _spellStackTargetCount;
    public long _spellStackTargetTime;
    
    public boolean _isBloodToSoulAuto;
    public boolean _isMagicShield;
    public boolean _isImmunToHarmSaint;
    public boolean _isAnonymityPoly, _isWorldPoly, _isLegendPledgePoly;
    public boolean _isDesperadoAbsolute;
    
    public int _statusScalesEarthDragonValue = 1, _statusScalesWaterDragonValue = 1, _statusScalesFireDragonValue = 1, _statusScalesWindDragonValue = 1;
    
    public int _bloodToSoulCount;
    public int _asuraCount = -1;
    public int _shiningShieldValue;
    public int _statusJudgementDoll = -1;
    public short _divineProtectionHp;
    public short _empireDg;
    public L1PcInstance _tomahawkHuntPc;
    public double immunToHarmValue;
    public boolean _isLancerForm;// 창기사 폼 상태
    public int _statusLancerFormRange = 4;// 창기사 원거리 범위
    public int _vanguardFlag;
    public boolean _halpasLoyaltyCheck;
    public int _halpasLoyaltyValue;
    
    public int _soldierType;// 용병 타입
    public int _partyMark;
    public byte _pixyFeatherCount, _pixyGoldFeatherCount, _arcaDelayCount;
	public boolean warZone, _is_siege_rank_buff;
	
	protected int attackRange;
	public int getAttackRange(){
		return attackRange;
	}
	public void setAttackRange(int value){
		attackRange = value;
	}
	
	public boolean isNotEnablePc(){
		return isPrivateShop() || isFishing() || noPlayerCK || this instanceof L1AiUserInstance || isAutoClanjoin() || isDead() || isGhost();
	}
	
	/**
     * 이동 불가 상태
     * @return boolean
     */
    public boolean isNotEnableMove(){
		return isDead() || isStop() || isDesperado() || isOsiris() || isEternity() || isHold() || isPhantomNotMove() || _teleport.isTeleport();
	}
	
    /**
     * 공격 불가 상태
     * @return boolean
     */
	public boolean isNotEnableAttack(){
		return isDead() || isGhost() || isStop() || isDesperado() || isOsiris() || _teleport.isTeleport() || isPrivateShop() || isInvisDelay();
	}
	
	/**
     * 스킬 사용 불가 상태
     * @return boolean
     */
    public boolean isNotEnableUseSpell(){
		return isDead() || isDesperado() || isOsiris() || _teleport.isTeleport();
	}
    
    public int getLongLocation() {
    	return (getX() << 16) & 0xFFFF0000 | (getY() & 0x0000FFFF);
    }
    
	public int getLongLocationReverse() {
	    return (getY() << 16) & 0xFFFF0000 | (getX() & 0x0000FFFF);
	}
	
	/**
	 * 공성전 지역 조사
	 * @param teleport
	 */
	public void siegeRegionCheck(boolean teleport){
		int castleid = L1CastleLocation.getCastleIdByArea(this);
		if (castleid != 0) {
			if (!warZone || teleport) {
				siegeRegionCheck(true, castleid);
			}
		} else {
			if (warZone) {
				siegeRegionCheck(false, 0);
			}
		}
	}
	
	void siegeRegionCheck(boolean flag, int castleid) {
		if (flag) {
			warZone = true;
			War.getInstance().WarTime_SendPacket(castleid, this);
			for (L1Object obj : getKnownObjects()) {
				if (obj instanceof L1TowerInstance) {
					if (isInvisble() && !isGm()) {
						delInvis();
						break;
					}
				} else if (obj instanceof L1DoorInstance) {
					L1DoorInstance door = (L1DoorInstance) obj;
					if (((door.getDoorId() >= 2001 && door.getDoorId() <= 2003) || (door.getDoorId() >= 2010 && door.getDoorId() <= 2012)
						|| (door.getDoorId() >= 2031 && door.getDoorId() <= 2035) || (door.getDoorId() >= 2041 && door.getDoorId() <= 2042)
						|| (door.getDoorId() >= 2051 && door.getDoorId() <= 2052)) && isInvisble() && !isGm()) {
						delInvis();
						if (getCurrentHp() > 10) {
							receiveDamage(door, 10);
						}
						break;
					}
				}
			}
			if (skillStatus.hasSkillEffect(L1SkillId.PRIME)) {
				sendPackets(new S_SpellBuffNoti(this, L1SkillId.PRIME, true, skillStatus.getSkillEffectTimeSec(L1SkillId.PRIME)), true);
			}
			L1BuffUtil.siege_rank_buff_set(this, true);
		} else {
			warZone = false;
			sendPackets(S_SiegeInjuryTimeNoti.CASTLE_WAR_TIME_NONE);
			if (skillStatus.hasSkillEffect(L1SkillId.BUFF_JUGUN)) {
				skillStatus.removeSkillEffect(L1SkillId.BUFF_JUGUN);
				sendPackets(S_PacketBox.JUGUN_BUFF_OFF);
			}
			if (skillStatus.hasSkillEffect(L1SkillId.PRIME)) {
				sendPackets(new S_SpellBuffNoti(this, L1SkillId.PRIME, true, skillStatus.getSkillEffectTimeSec(L1SkillId.PRIME)), true);
			}
			L1BuffUtil.siege_rank_buff_set(this, false);
		}
	}
	
	public boolean isTwoLogin() {// 중복체크
		for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
			if (target.noPlayerCK || target instanceof L1FakePcInstance || target instanceof L1AiUserInstance || target.getRobotAi() != null) {
				continue;
			}
			if (getId() != target.getId() 
					&& (!target.isPrivateShop() && !target.isAutoClanjoin()) 
					&& _netConnection != null && target._netConnection != null 
					&& _netConnection.getAccountName().equalsIgnoreCase(target._netConnection.getAccountName())) {
				return true;
			}
		}
		return false;
	}

	public void finishFishing(){
		_isFishing = false;
		_fishingTime = 0;
		_fishingRod = null;
		broadcastPacketWithMe(new S_CharVisualUpdate(this), true);
	}
	
    public int getNcoin() {
        return _account != null ? _account.getNcoin() : 0;
    }
    public void addNcoin(int coin) {
    	if (_account == null) {
    		return;
    	}
    	_account.addNcoin(coin);
    }
    
    // 콤보시스템
    private int comboCount;
    public int getComboCount() {
    	return comboCount;
    }
    public void setComboCount(int Count) {
    	comboCount = Count;
    }

	private boolean isPCCafe;
	public boolean isPCCafe() {
		return isPCCafe;
	}
	public void setPCCafe(boolean val) {
		isPCCafe = val;
	}

    private Account _account, _exception_account;
    public Account getAccount() {
        return _account == null ? _exception_account : _account;
    }
    public void setAccount(Account val) {
    	_account = _exception_account = val;
    }
    
    // 낚싯대
    public L1ItemInstance _fishingRod;

    private L1ClassFeature _classFeature;
    private L1EquipmentSlot _equipSlot;
    private String _accountName;
    private int _classId;
    private int _type;
    private short _accessLevel;

    private int _baseMaxHp, _baseMaxMp;
    private int _originalMagicHit;
    private int _baseLongDmg, _baseLongHitup;
    private int _baseShortDmg, _baseShortHitup;

    private int _baseMagicHitup;		// 베이스 스탯에 의한 마법 명중
    private int _baseMagicCritical;		// 베이스 스탯에 의한 마법 치명타(%)
    private int _baseMagicDmg; 			// 베이스 스탯에 의한 마법 대미지
    private int _baseMagicDecreaseMp;	// 베이스 스탯에 의한 마법 대미지

    private int _PKcount;
    public int _fishingX, _fishingY;
    private L1Clan _clan = null;
    private int _clanid;
    private String clanname;
    private eBloodPledgeRankType _bloodPledgeRank;
    private int _pledgeJoinDate, _pledgeRankDate;
    
    private Gender _gender;
    private short _hpRegen, _trueHpRegen;
    private short _mpRegen, _trueMpRegen;
    private int _advenHp, _advenMp;
    private int _highLevel;
    
    public boolean isInValakasBoss, isInValakas;// 화룡의 안식처
    private boolean _ghost;
    private boolean _ghostCanTalk = true;
    private boolean _isReserveGhost;
    
    private boolean _isFishing;
    private boolean _isDeathMatch; // 데스매치
    
    private boolean _gm;
    private boolean _monitor;
    private boolean _gmInvis;
    private boolean _isDrink;
    private boolean _isGres;
    private boolean _banned;
    private boolean _gresValid;
    private boolean _tradeOk;
	private boolean _tradeReady;
	private boolean _hpRegen32SecondActiveByItem;
    private boolean _mpRegen64SecondActiveByItem;
    private boolean _mpRegen16SecondActiveByItem;
    private boolean _HalloweenRegenActive;

    public boolean noPlayerCK;

    private int invisDelayCounter;
    private Object _invisTimerMonitor = new Object();

    private int _ghostSaveLocX, _ghostSaveLocY;
    private short _ghostSaveMapId;
    public byte _ghostCount;
    public long ghosttime;

    private ScheduledFuture<?> _ghostFuture;
    private ScheduledFuture<?> _hellFuture;
    private ScheduledFuture<?> _autoUpdateFuture;
    private ScheduledFuture<?> _expMonitorFuture;

    private Timestamp _lastPk;
    private Timestamp _deleteTime;
    private Timestamp _lastLoginTime, _lastLogoutTime;

    private int _carryBonus;
    private int _hasteItemEquipped;
    private int _drunkenItemEquipped;
    private int _fourthItemEquipped;

    private int _tempCharGfxAtDead;
    private int _fightId;
    private byte _chatCount;
    private long _oldChatTimeInMillis;

    private int _elfAttr;
    private long _expRes;

    private int _onlineStatus;
    private int _homeTownId;
    private int _contribution;
    private int _food;
    private int _hellTime;
    private int _partnerId;
    private long _fishingTime;

    private int _currentWeapon;
    private final L1Karma _karma = new L1Karma();
    private final L1PcInventory _inventory;
    private final L1Inventory _tradewindow;

    private L1ItemInstance _weapon;
    private L1Party _party;
    private L1ChatParty _chatParty;

    private int _cookingId, _soupId;
    private int _partyID;
    private int _tradeID;
    private int _tempID;
    
    private int _returnstatus;
	public synchronized int getReturnStatus() {
		return _returnstatus;
	}
	public synchronized void setReturnStatus(int val) {
		_returnstatus = val;
	}
	
	private long _returnstat_exp;
	public synchronized long getReturnStat_exp() {
    	return _returnstat_exp;
    }
    public synchronized void setReturnStat_exp(long i) {
    	_returnstat_exp = i;
    }
	
	private L1StatReset _statReset;
	public void setStatReset(L1StatReset val) {
		_statReset = val;
	}
	public L1StatReset getStatReset() {
		return _statReset;
	}
    
    public int getHighLevel() {
    	return _highLevel;
    }
    public void setHighLevel(int i) {
    	_highLevel = i;
    }
    
    private int _bonusStats;
    public int getBonusStats() {
    	return _bonusStats;
    }
    public void setBonusStats(int i) {
    	_bonusStats = i;
    }

    private int _elixirStats;
    public int getElixirStats() {
    	return _elixirStats;
    }
    public void setElixirStats(int i) {
    	_elixirStats = i;
    }
    
    public void Stat_Reset_Str() {
    	sendPackets(new S_StatusBaseNoti(getAbility()), true);
		sendPackets(new S_StatusCarryWeightInfoNoti(this), true);
		sendPackets(new S_StatusRenewalInfo(this, 1, L1Status.STR), true);
	}

	public void Stat_Reset_Dex() {
		sendPackets(new S_StatusBaseNoti(getAbility()), true);
		resetBaseAc();
		sendPackets(new S_StatusRenewalInfo(this, 1, L1Status.DEX), true);
	}

	public void Stat_Reset_Con() {
		L1Ability ability = getAbility();
		sendPackets(new S_StatusBaseNoti(ability), true);
		ability.setStatusPotionPlus();
		sendPackets(new S_StatusCarryWeightInfoNoti(this), true);
		sendPackets(new S_StatusRenewalInfo(this, 1, L1Status.CON), true);
		sendPackets(new S_HPUpdate(this), true);
	}

	public void Stat_Reset_Int() {
		L1Ability ability = getAbility();
		sendPackets(new S_StatusBaseNoti(ability), true);
		setBaseMagicHitUp(CalcIntelStat.magicHitup(ability.getTotalInt()));
		sendPackets(new S_StatusRenewalInfo(this, 1, L1Status.INT), true);
	}

	public void Stat_Reset_Wis() {
		sendPackets(new S_StatusBaseNoti(getAbility()), true);
		resetBaseMr();
		sendPackets(new S_SPMR(this), true);
		sendPackets(new S_StatusRenewalInfo(this, 1, L1Status.WIS), true);
	}

	public void Stat_Reset_Cha() {
		sendPackets(new S_StatusBaseNoti(getAbility()), true);
		resetBaseSpell();
		getResistance().updateCharismaHitup();
		sendPackets(new S_StatusRenewalInfo(this, 1, L1Status.CHA), true);
	}
	
	public void resetBaseAc() {
		int baseAc = 10 + CalcDexStat.acBonus(getAbility().getTotalDex());
		getAC().setBaseAc(baseAc);
		sendPackets(new S_OwnCharAttrDef(this), true);
	}

    public void resetBaseMr() {
		int newMr = 0;
		newMr += CalcWisStat.mrBonus(_type, getAbility().getTotalWis());
		newMr += getLevel() >> 1;
		resistance.setBaseMr(newMr);		
		sendPackets(new S_SPMR(this), true);
    }
    
    public void resetBaseSpell(){
		L1Ability ability = getAbility();
    	int totalCha = ability.getTotalCha();
    	ability.setSpellCooltimeDecrease(CalcChaStat.decreaseSpellCoolTime(totalCha));
    	ability.setSpellDurationDecrease(CalcChaStat.decreaseCCDuration(totalCha));
    }

    private L1Quest _quest;

    private boolean _hpMpRegenActive;
    private HpMpRegenController _hpMpRegen;
    private int _hpRegen32SecondByItemValue, _mpRegen64SecondByItemValue, _mpRegen16SecondByItemValue;
    private HpRegeneration32SecondByItem _hpRegen32SecondByItem;
    private MpRegeneration64SecondByItem _mpRegen64SecondByItem;
    private MpRegeneration16SecondByItem _mpRegen16SecondByItem;
    private HalloweenRegeneration _HalloweenRegen;
    
    public int getHpRegen32SecondByItemValue() {
    	return _hpRegen32SecondByItemValue;
    }
    public void addHpRegen32SecondByItemValue(int val) {
    	_hpRegen32SecondByItemValue += val;
    	if (_hpRegen32SecondByItemValue > 0) {
    		startHpRegenerationByItem32Second();
    	} else {
    		stopHpRegenerationByItem32Second();
    	}
    }
    
    public int getMpRegen64SecondByItemValue() {
    	return _mpRegen64SecondByItemValue;
    }
    public void addMpRegen64SecondByItemValue(int val) {
    	_mpRegen64SecondByItemValue += val;
    	if (_mpRegen64SecondByItemValue > 0) {
    		startMpRegenerationByItem64Second();
    	} else {
    		stopMpRegenerationByItem64Second();
    	}
    }
    
    public int getMpRegen16SecondByItemValue() {
    	return _mpRegen16SecondByItemValue;
    }
    public void addMpRegen16SecondByItemValue(int val) {
    	_mpRegen16SecondByItemValue += val;
    	if (_mpRegen16SecondByItemValue > 0) {
    		startMpRegenerationByItem16Second();
    	} else {
    		stopMpRegenerationByItem16Second();
    	}
    }

    private static Timer _regenTimer = new Timer(true);

	private boolean _isAutoClanjoin;// 무인가입
    private int _partnersPrivateShopItemCount;

    private long _lastPasswordChangeTime;
    private long _lastQuizChangeTime;
    private long _lastLocalTellTime;
    private boolean _isQuizValidated;

    boolean isExpDrop, isItemDrop;
    
	public int _regenHpMax, _regenHpPoint, _regenMpPoint;
	public int _curHpPoint = 4, _curMpPoint = 4;

	public final ArrayList<L1BookMark> _speedbookmarks;
    public L1BookMark[] getBookMarkArray() {
    	return _bookmarks.toArray(new L1BookMark[_bookmarks.size()]);
    }
    public L1BookMark[] getSpeedBookMarkArray() {
    	return _speedbookmarks.toArray(new L1BookMark[_speedbookmarks.size()]);
    }

    private int _bookMarkCount;
    public void setBookMarkCount(int i) {
    	_bookMarkCount = i;
    }
    public int getBookMarkCount() {
    	return _bookMarkCount;
    }

    /** 혈맹버프 **/
    private boolean _clanBuff;
    public boolean isClanBuff() {
    	return _clanBuff;
    }
    public void setClanBuff(boolean val) {
    	_clanBuff = val;
    }
    
    private int _clanBuffMap;
	public int getClanBuffMap() {
		return _clanBuffMap;
	}
	public void setClanBuffMap(int i) {
		_clanBuffMap = i;
	}
	
    private boolean _magicitem;
    private int _magicitemid;
    public boolean isWorld;
    public boolean isDanteasBuff;
    private long _npcActionTime;

    private final AcceleratorChecker _acceleratorChecker	= new AcceleratorChecker(this);
    private final ArrayList<L1BookMark> _bookmarks;
    private ArrayList<L1PrivateShopSellList> _sellList		= new ArrayList<L1PrivateShopSellList>();
    private ArrayList<L1PrivateShopBuyList> _buyList		= new ArrayList<L1PrivateShopBuyList>();
    private AtomicInteger _pinkNameTime;
    private GameClient _netConnection;
    private final L1PcSpeedSync _speedSync;
    private final L1PassiveSkillHandler _passiveSkill;
    private final L1EquipmentSet _equipSet;
    private final L1Teleport _teleport;
    private final L1ExpHandler _expHandler;
    private final L1CharacterConfig _config;

    // 생성자
    public L1PcInstance() {
        _accessLevel	= 0;
        _currentWeapon	= 0;
        _inventory		= new L1PcInventory(this);
        _tradewindow	= new L1Inventory();
        _bookmarks		= new ArrayList<L1BookMark>();
        _speedbookmarks	= new ArrayList<L1BookMark>();
        _quest			= new L1Quest(this);
        _equipSlot		= new L1EquipmentSlot(this);
        _pinkNameTime	= new AtomicInteger(0);
        _speedSync		= new L1PcSpeedSync();
        _passiveSkill	= new L1PassiveSkillHandler(this);
        _equipSet		= new L1EquipmentSet();
        _teleport		= new L1Teleport(this);
        _expHandler		= new L1ExpPlayer(this);
        _config			= new L1CharacterConfig(this);
    }
    
    public L1PcSpeedSync getSpeedSync() {
    	return _speedSync;
    }
    
    public L1ExpHandler getExpHandler(){
    	return _expHandler;
    }
    
    public L1Teleport getTeleport(){
    	return _teleport;
    }
    
    public L1PassiveSkillHandler getPassiveSkill(){
    	return _passiveSkill;
    }
    public boolean isPassiveStatus(L1PassiveId passive){
    	return passive == null || _passiveSkill == null ? false : _passiveSkill.isStatus(passive);
    }
    
    public L1EquipmentSet getEquipmentSet() {
    	return _equipSet;
    }
    
    private L1DungeonTimer _dungoenTimer;
    public L1DungeonTimer getDungoenTimer(){
    	return _dungoenTimer;
    }
    public void createDungeonTimer(){
    	if (_dungoenTimer != null) {
    		return;
    	}
    	_dungoenTimer = new L1DungeonTimer(this);
    }
    
    private L1Fatigue _fatigue;
    public L1Fatigue getFatigue(){
    	return _fatigue;
    }
    public void createFatigue(int point, Timestamp restTime){
    	if (_fatigue != null) {
    		return;
    	}
    	_fatigue = new L1Fatigue(this, point, restTime);
    }
    
    public L1CharacterConfig getConfig() {
    	return _config;
    }
    
    private DeathPenaltyUser _penalty_exp;
    public DeathPenaltyUser get_penalty_exp() {
    	return _penalty_exp;
    }
    public void set_penalty_exp(DeathPenaltyUser val) {
    	_penalty_exp = val;
    }
    
    private DeathPenaltyUser _penalty_item;
    public DeathPenaltyUser get_penalty_item() {
    	return _penalty_item;
    }
    public void set_penalty_item(DeathPenaltyUser val) {
    	_penalty_item = val;
    }

    public long getLastQuizChangeTime() {
    	return _lastQuizChangeTime;
    }
    public void updateLastQuizChangeTime() {
    	_lastQuizChangeTime = System.currentTimeMillis();
    }
    
    public long getLastPasswordChangeTime() {
    	return _lastPasswordChangeTime;
    }
    public void updateLastPasswordChangeTime() {
    	_lastPasswordChangeTime = System.currentTimeMillis();
    }
    
    public void setQuizValidated() {
    	_isQuizValidated = true;
    }
    public boolean isQuizValidated() {
    	return _isQuizValidated;
    }

    public long getLastLocalTellTime() {
    	return _lastLocalTellTime;
    }
    public void updateLastLocalTellTime() {
    	_lastLocalTellTime = System.currentTimeMillis();
    }

    public int getPinkNameTime() {
    	return _pinkNameTime.get();
    }
    public int DecrementPinkNameTime() {
    	return _pinkNameTime.decrementAndGet();
    }
    public int SetPinkNameTime(int timeValue) {
    	return _pinkNameTime.getAndSet(timeValue);
    }

    public short getHpRegen() {
        return _hpRegen;
    }
    public void addHpRegen(int i) {
    	_trueHpRegen += i;
        _hpRegen = (short) Math.max(0, _trueHpRegen);
    }

    public short getMpRegen() {
        return _mpRegen;
    }
    public void addMpRegen(int i) {
    	_trueMpRegen += i;
        _mpRegen = (short) Math.max(0, _trueMpRegen);
    }
    
    public long getNpcActionTime() {
    	return _npcActionTime;
    }
    public void setNpcActionTime(long flag) {
    	_npcActionTime = flag;
    }

    public boolean isMagicItem() {
    	return _magicitem;
    }
    public void setMagicItem(boolean flag) {
    	_magicitem = flag;
    }

    public int getMagicItemId() {
    	return _magicitemid;
    }
    public void setMagicItemId(int itemid) {
    	_magicitemid = itemid;
    }
    
    private PapuArmorBlessing _papuArmorRegen;
    private boolean _papuArmorActive;
    
    private LindArmorBlessing _lindArmorRegen;
    private boolean _lindArmorActive;
    
    private ElfArmorBlessing _elfArmorRegen;
    private boolean _elfArmorActive;
    
    public void startHpMpRegeneration() {
		if (!_hpMpRegenActive) {
			final long interval = 1000L;
			_hpMpRegen = new HpMpRegenController(this, interval);
			GeneralThreadPool.getInstance().schedule(_hpMpRegen, interval);
			_hpMpRegenActive = true;
		}
	}
    
    public void stopHpMpRegeneration() {
        if (_hpMpRegenActive) {
        	_hpMpRegen.cancel();
        	_hpMpRegen = null;
            _hpMpRegenActive = false;
        }
    }

    public void startPapuBlessing() { // 파푸리온의 갑옷
        if (!_papuArmorActive) {
        	final long interval = 120000L;
        	_papuArmorRegen = new PapuArmorBlessing(this);
        	_papuArmorActive = true;
            _regenTimer.scheduleAtFixedRate(_papuArmorRegen, interval, interval);
        }
    }
    
    public void startLindBlessing() { // 린드비오르의 갑옷
        if (!_lindArmorActive) {
        	final long interval = 40000L;
        	_lindArmorRegen = new LindArmorBlessing(this);
        	_lindArmorActive = true;
            _regenTimer.scheduleAtFixedRate(_lindArmorRegen, interval, interval);
        }
    }
    
    public void startElfArmorBlessing() {// 신성한 요정족 판금 갑옷
        if (!_elfArmorActive) {
        	final long interval = 120000L;
        	_elfArmorRegen = new ElfArmorBlessing(this);
        	_elfArmorActive = true;
        	_regenTimer.scheduleAtFixedRate(_elfArmorRegen, interval, interval);
        }
    }
    
	public void startHpRegenerationByItem32Second() {
		if (_hpRegen32SecondByItemValue <= 0) {
			return;
		}
		if (!_hpRegen32SecondActiveByItem) {
			final long interval = 32000L;
			_hpRegen32SecondByItem = new HpRegeneration32SecondByItem(this, 1608, interval);
			GeneralThreadPool.getInstance().schedule(_hpRegen32SecondByItem, interval);
			_hpRegen32SecondActiveByItem = true;
		}
	}
	
	public void startMpRegenerationByItem64Second() {
		if (_mpRegen64SecondByItemValue <= 0) {
			return;
		}
		if (!_mpRegen64SecondActiveByItem) {
			final long interval = 64000L;
			_mpRegen64SecondByItem = new MpRegeneration64SecondByItem(this, 6321, interval);
			GeneralThreadPool.getInstance().schedule(_mpRegen64SecondByItem, interval);
			_mpRegen64SecondActiveByItem = true;
		}
	}
	
	public void startMpRegenerationByItem16Second() {
		if (_mpRegen16SecondByItemValue <= 0) {
			return;
		}
		if (!_mpRegen16SecondActiveByItem) {
			final long interval = 16000L;
			_mpRegen16SecondByItem = new MpRegeneration16SecondByItem(this, 0, interval);
			GeneralThreadPool.getInstance().schedule(_mpRegen16SecondByItem, interval);
			_mpRegen16SecondActiveByItem = true;
		}
	}

    public void startHalloweenRegeneration() {
        if (!_HalloweenRegenActive) {
        	final long interval = 900000L;
            _HalloweenRegen = new HalloweenRegeneration(this, interval);
            GeneralThreadPool.getInstance().schedule(_HalloweenRegen, interval);
            _HalloweenRegenActive = true;
        }
    }

    public void stopPapuBlessing() {// 파푸리온의 갑옷
        if (_papuArmorActive) {
        	_papuArmorRegen.cancel();
        	_papuArmorRegen = null;
        	_papuArmorActive = false;
        }
    }
    
    public void stopLindBlessing() {// 린드비오르의 갑옷
        if (_lindArmorActive) {
        	_lindArmorRegen.cancel();
        	_lindArmorRegen = null;
        	_lindArmorActive = false;
        }
    }
    
    public void stopElfArmorBlessing() {// 신성한 요정족 판금 갑옷
        if (_elfArmorActive) {
        	_elfArmorRegen.cancel();
        	_elfArmorRegen = null;
        	_elfArmorActive = false;
        }
    }

    public void stopHpRegenerationByItem32Second() {
        if (_hpRegen32SecondActiveByItem) {
        	_hpRegen32SecondByItem.cancel();
        	_hpRegen32SecondByItem = null;
            _hpRegen32SecondActiveByItem = false;
        }
    }

    public void stopMpRegenerationByItem64Second() {
        if (_mpRegen64SecondActiveByItem) {
        	_mpRegen64SecondByItem.cancel();
        	_mpRegen64SecondByItem = null;
            _mpRegen64SecondActiveByItem = false;
        }
    }
    
    public void stopMpRegenerationByItem16Second() {
        if (_mpRegen16SecondActiveByItem) {
        	_mpRegen16SecondByItem.cancel();
        	_mpRegen16SecondByItem = null;
            _mpRegen16SecondActiveByItem = false;
        }
    }

    public void stopHalloweenRegeneration() {
        if (_HalloweenRegenActive) {
            _HalloweenRegen.cancel();
            _HalloweenRegen = null;
            _HalloweenRegenActive = false;
        }
    }

	public void startObjectAutoUpdate() {
		final long interval = 300L;
		removeAllKnownObjects();
		_autoUpdateFuture = GeneralThreadPool.getInstance().scheduleAtFixedRate(new L1PcAutoUpdate(this), 0L, interval);
	}

    public void stopEtcMonitor() {
        if (_autoUpdateFuture != null) {
            _autoUpdateFuture.cancel(true);
            _autoUpdateFuture = null;
        }
        if (_expMonitorFuture != null) {
            _expMonitorFuture.cancel(true);
            _expMonitorFuture = null;
        }
        if (_ghostFuture != null) {
            _ghostFuture.cancel(true);
            _ghostFuture = null;
        }
        if (_hellFuture != null) {
            _hellFuture.cancel(true);
            _hellFuture = null;
        }
    }

    public void stopEquipmentTimer() {
        for (L1ItemInstance item : getInventory().getItems()) {
            if (item == null) {
            	continue;
            }
            if (item.isEquipped() && item.getRemainingTime() > 0) {
            	item.stopEquipmentTimer(this);
            }
        }
    }

    public void onChangeExp() {
        int expLevel = ExpTable.getLevelByExp(getExp());
        int currentLevel = getLevel();
        int gap = expLevel - currentLevel;
        if (gap == 0) {
            sendPackets(new S_OwnCharStatus(this), true);
            int percent = ExpTable.getExpPercentage(currentLevel, getExp());
            if ((currentLevel >= 60 && currentLevel <= 64 && percent >= 10) || (currentLevel >= 65 && percent >= 5)) {
            	setlevelUpBonusBuff(false);
            }
            return;
        }

        if (gap > 0) {
            levelUp(gap);
            if (getLevel() >= 60) {
            	setlevelUpBonusBuff(true);
            }
        } else if (gap < 0) {
            levelDown(gap);
            setlevelUpBonusBuff(false);
        }
    }

    @Override
    public void onPerceive(L1PcInstance perceivedFrom) {
    	if (perceivedFrom == null || this == null) {
			return;
		}

    	perceivedFrom.addKnownObject(this);
    	if (isGmInvis()) {
    		return;
    	}
    	short mapId = getMapId();
    	if (mapId == 5166 || mapId == 5167) {// 회상의 땅, 축복의 땅(불필요한 패킷 처리 방지 맵)
    		return;
    	}
    	
    	boolean is_party_member = isInParty() && getParty().isMember(perceivedFrom);
    	if (isInvisble() 
    			&& !perceivedFrom.isFloatingEye() 
				&& !is_party_member
				&& getClanid() != perceivedFrom.getClanid()) {
    		return;
		}
    	
    	perceivedFrom.sendPackets(new S_PCObject(this), true);
    	
		if (perceivedFrom._config.getAnonymityType() != null) {
			sendPackets(new S_PolymorphAnonymityNoti(perceivedFrom), true);
			sendPackets(new S_PolymorphAnonymity(perceivedFrom._config.getAnonymityType()), true);
		}
		
		if (isPinkName()) {
			perceivedFrom.sendPackets(new S_PinkName(getId(), getPinkNameTime()), true);
		}
		
		if (is_party_member) {
			sendPackets(new S_PartyMemberStatus(perceivedFrom), true);
			perceivedFrom.sendPackets(new S_PartyMemberStatus(this), true);
			if (_partyMark != 0) {
				perceivedFrom.sendPackets(new S_PartyMemberStatus(this.getName(), this._partyMark), true);
			}
		}

		if (isPrivateShop()) {
			perceivedFrom.sendPackets(new S_DoActionShop(getId(), ActionCodes.ACTION_Shop, getShopChat()), true);
		} else if (isFishing()) {
			perceivedFrom.sendPackets(new S_Fishing(getId(), ActionCodes.ACTION_Fishing, _fishingX, _fishingY), true);
		}
		if (isCrown()) {
			L1Clan clan = getClan();
			if (clan != null && getId() == clan.getLeaderId() && clan.getCastleId() != 0) {
				perceivedFrom.sendPackets(new S_CastleMaster(clan.getCastleId(), getId()), true);
			}
		}
	}

	public void broadcastRemoveAllKnownObjects() {
		for (L1Object known : getKnownObjects()) {
			if (known == null) {
				continue;
			}
			sendPackets(new S_RemoveObject(known), true);
		}
	}
	
	// TODO 오브젝트 업데이트
	public void updateObject() {
		updateObject(false);
	}
	
	// TODO 모니터에 의한 오브젝트 업데이트
	public void updateObjectMonitor() {
		updateObject(true);
	}
	
	private Object updateObjectLock = new Object();

	/**
	 * 오브젝트 업데이트
	 * @param monitor 모니터여부
	 */
	public void updateObject(boolean monitor) {
		synchronized (updateObjectLock) {
			try {
				removeOutOfRangeObjects(monitor);
				for (L1Object visible : L1World.getInstance().getVisibleObjects(this, Config.SERVER.PC_RECOGNIZE_RANGE)) {
					// 텔레포트에 의한 업데이트(메인)를 위해 모니터에 의한 업데이트(서브)를 중지한다.(우선 순위 충돌 방지)
					if (monitor && _teleport.isTeleport()) {
						break;
					}
					if (visible == null) {
						continue;
					}
					updateObject(visible);
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}
	
	/**
	 * 오브젝트 업데이트
	 * @param obj 오브젝트
	 */
	private void updateObject(L1Object obj) {
		if (!knownsObject(obj)) {
			obj.onPerceive(this);
		} else {
			if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance mon = (L1MonsterInstance) obj;
				if (mon.getHiddenStatus() != 0 && getLocation().isInScreen(mon.getLocation())) {
					mon.approachPlayer(this);
				}
			}
		}
		
		if (skillStatus.hasSkillEffect(L1SkillId.GMSTATUS_HPBAR) && L1HpBar.isHpBarTarget(obj)) {
			sendPackets(new S_HPMeter((L1Character) obj), true);
		}
	}
	
	/**
	 * 거리 밖에 존재하는 인식하고 있던 오브젝트 제거
	 * @param monitor 모니터 여부
	 */
	private void removeOutOfRangeObjects(boolean monitor) {
		for (L1Object known : getKnownObjects()) {
			// 텔레포트에 의한 업데이트(메인)를 위해 모니터에 의한 업데이트(서브)를 중지한다.(우선 순위 충돌 방지)
			if (monitor && _teleport.isTeleport()) {
				break;
			}
			if (known == null || (known instanceof L1NpcInstance && ((L1NpcInstance) known).getNpcTemplate().isNotification())) {
				continue;
			}
			removeOutOfRangeObjects(known, monitor);
		}
	}
	
	/**
	 * 거리 밖에 존재하는 오브젝트 제거
	 * @param known 인식하고 있던 오브젝트
	 * @param monitor 모니터 여부
	 */
	private void removeOutOfRangeObjects(L1Object known, boolean monitor) {
		if (Config.SERVER.PC_RECOGNIZE_RANGE == -1) {
			if (!getLocation().isInScreen(known.getLocation())) {// 화면내
				removeObjects(known, monitor);
			}
		} else {
			if (getLocation().getTileLineDistance(known.getLocation()) > Config.SERVER.PC_RECOGNIZE_RANGE) {// 거리내
				removeObjects(known, monitor);
			}
		}
	}
	
	/**
	 * 오브젝트 제거
	 * @param obj 오브젝트
	 * @param isRemovePck 패킷 송신여부
	 */
	public void removeObjects(L1Object obj, boolean isRemovePck) {
		removeKnownObject(obj);
		if (isRemovePck){
			sendPackets(new S_RemoveObject(obj));
		}
	}
	
    private void sendVisualEffect() {
        int poisonId = 0;
        if (getParalysis() != null) {
        	poisonId = getParalysis().getEffectId();
        } else if (getPoison() != null) {
        	poisonId = getPoison().getEffectId();
        }
        if (poisonId != 0) {
        	broadcastPacketWithMe(new S_Poison(getId(), poisonId), true);
        }
    }

    public void sendCastleOwner() {
    	if (getClanid() == 0) {
    		return;
    	}
        L1Clan clan = this.getClan();
        if (clan != null && getId() == clan.getLeaderId() && clan.getCastleId() != 0) {
            sendPackets(new S_CastleMaster(clan.getCastleId(), getId()), true);
        }
    }

    public void sendVisualEffectAtLogin() {
        sendVisualEffect();
    }

    public void sendVisualEffectAtTeleport() {
        sendVisualEffect();
    }
    
    public int _db_current_hp, _db_current_mp;

    @Override
    public void setCurrentHp(int i) {
        if (getCurrentHp() == i) {
        	return;
        }
		/** LFC **/
		if ((getInstStatus() == InstStatus.INST_USERSTATUS_LFC) && i > getCurrentHp()) {
			addDamageFromLfc(i-getCurrentHp());
		}
        super.setCurrentHp(i);
        sendPackets(new S_HPUpdate(getCurrentHp(), getMaxHp()), true);
        if (isInParty()) {
        	getParty().update(this);
        }
        if (isWarrior() && isPassiveStatus(L1PassiveId.BERSERK)) {
        	getPassiveSkill().doBerserk();
        }
    }

    @Override
    public void setCurrentMp(int i) {
        if (getCurrentMp() == i) {
        	return;
        }
        if (isGm()) {
        	i = getMaxMp();
        }
        super.setCurrentMp(i);
        sendPackets(new S_MPUpdate(getCurrentMp(), getMaxMp()), true);
        if (isInParty()) {
        	getParty().update(this);
        }
    }

    @Override
    public L1PcInventory getInventory() {
        return _inventory;
    }

    public L1Inventory getTradeWindowInventory() {
        return _tradewindow;
    }

    public boolean isGmInvis() {
    	return _gmInvis;
    }
    public void setGmInvis(boolean flag) {
    	_gmInvis = flag;
    }

    public int getCurrentWeapon() {
    	return _currentWeapon;
    }
    public void setCurrentWeapon(int i) {
    	_currentWeapon = i;
    }

    /**
     * 클래스의 타입 (0:군주, 1:기사, 2:요정, 3:마법사, 4:다크엘프, 5:용기사, 6:환술사, 7:전사, 8:검사, 9:창기사)
     * @return int
     */
    public int getType() {
    	return _type;
    }
    public void setType(int i) {
    	_type = i;
    }

    public short getAccessLevel() {
    	return _accessLevel;
    }
    public void setAccessLevel(short i) {
    	_accessLevel = i;
    }

    public int getClassId() {
    	return _classId;
    }
    
    public L1ClassFeature getClassFeature() {
        return _classFeature;
    }

    public void setClassId(int i) {
        _classId = i;
        _classFeature = L1ClassFeature.newClassFeature(i);
    }

	public void notifyPlayersLogout(List<L1PcInstance> playersArray) {
		S_RemoveObject remove = new S_RemoveObject(this);
		for (L1PcInstance player : playersArray) {
			player.removeKnownObject(this);
			player.sendPackets(remove);
		}
		remove.close();
		remove = null;
	}
    
    /**
     * 캐릭터 종료 업데이트
     */
    void logout_update() {
    	// 아이템 수량 소진 캐시 제거
        try {
            for (L1ItemInstance item : getInventory().getItems()) {
                if (item == null) {
                	continue;
                }
                if (item.getCount() <= 0) {
                	getInventory().deleteItem(item);
                }
            }
        } catch (Exception e) {
            //System.out.println(String.format("[로그아웃] 아이템 수량 소진 캐시 제거 오류 (%s)", getName()));
			System.out.println(String.format("[Logout] Item quantity exhausted cache removal error (%s)", getName()));
            e.printStackTrace();
        }
        
        // 계정 관련 데이터 저장
        Account account = getAccount();
        if (account != null) {
        	try {
        		account.update_logout();
        	} catch (Exception e) {
        		//System.out.println(String.format("[로그아웃] 계정관련 데이터 저장 오류 발생! (%s)", getName()));
				System.out.println(String.format("[Logout] An error occurred while saving account-related data! (%s)", getName()));
        		e.printStackTrace();
    		}
        }
        
    	// 이벤트 푸시
    	try {
    		EventPushLoader.getInstance().store(this);
    	} catch (Exception e) {
    		//System.out.println(String.format("[로그아웃] 이벤트 푸시 저장 오류 발생! (%s)", getName()));
			System.out.println(String.format("[Logout] Event push save error occurred! (%s)", getName()));
    		e.printStackTrace();
		}
    	
    	// 인던 대기실
    	try {
    		if (_netConnection != null && !_netConnection.isInterServer() && IndunList.isIndunInfoPcCheck(this) == true) {
    			int roomnumber = IndunList.getIndunInfoPcCheckRoomNumber(this);
    			IndunInfo info = IndunList.getIndunInfo(roomnumber);
    			if (!info.is_playing && info.getUserInfo(this) != null) {
					if (info.chief_id == getId()) {
        				for (L1PcInstance member : info.getMembers()) {
        					member._config._IndunReady = false;
        					info.setUser(member);
        					member.sendPackets(new S_IndunExitRoom(ArenaRoomExitResult.SUCCESS, info.room_id), true);
        					member.getTeleport().start(33464, 32757, (short) 4, member.getMoveState().getHeading(), true);
        				}
        				IndunList.removeIndunInfo(info.room_id);
        			} else {
        				_config._IndunReady = false;
        				info.setUser(this);
        				S_IndunChangeRoomStatus change = new S_IndunChangeRoomStatus(info);
        				for (L1PcInstance member : info.getMembers()) {
        					member.sendPackets(change);
        				}
        				change.clear();
        				change = null;
        			}
    			}
    		}
    	} catch (Exception e) {
    		//System.out.println(String.format("[로그아웃] 인던정보 초기화 오류 발생 (%s)", getName()));
			System.out.println(String.format("[Logout] Dungeon information initialization error occurred (%s)", getName()));
    		e.printStackTrace();
		}
    	
    	// 낚시 종료
    	try {
			if (isFishing()) {
				finishFishing();
			}
		} catch (Exception e) {
			//System.out.println(String.format("[로그아웃] 낚시종료 오류 발생 (%s)", getName()));
			System.out.println(String.format("[Logout] Fishing end error occurred (%s)", getName()));
			e.printStackTrace();
		}
    	
    	// 혈맹창고 이용 종료
		try {
			if (getClanid() > 0 && getClan() != null) {
				ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(getClan().getClanName());
				if (clanWarehouse.getWarehouseUsingChar() == getId()) {
					clanWarehouse.setWarehouseUsingChar(0, 0);
				}
			}
		} catch (Exception e) {
			//System.out.println(String.format("[로그아웃] 혈맹창고 이용 초기화 에러 발생! (%s)", getName()));
			System.out.println(String.format("[Logout] An initialization error occurred when using the clan warehouse! (%s)", getName()));
			e.printStackTrace();
		}
    	
		// 종료 로그
    	try {
            if (_netConnection != null && !isPrivateShop()) {	
            	//Manager.getInstance().LogLogOutAppend(getName(), _netConnection.getHostname()); // MANAGER DISABLED
                //LoggerInstance.getInstance().addConnection(String.format("종료 캐릭=%s 계정=%s IP=%s", getName(), getAccountName(), _netConnection.getHostname()));
				LoggerInstance.getInstance().addConnection(String.format("End character=%s account=%s IP=%s", getName(), getAccountName(), _netConnection.getHostname()));
             }
        } catch (Exception e) {
        	//System.out.println(String.format("[로그아웃] 로그저장 오류 (%s)", getName()));
			System.out.println(String.format("[Logout] Log saving error (%s)", getName()));
        	e.printStackTrace();
        }
    	
    	// 사망 후 종료
    	try {
            getMap().setPassable(getLocation(), true);
            // 사망하고 있으면(자) 거리에 되돌려, 공복 상태로 한다
            if (isDead()) {
                int[] loc = Getback.GetBack_Location(this);
                setX(loc[0]);
                setY(loc[1]);
                setMap((short) loc[2]);
                setCurrentHp(getLevel());
                setFood(GameServerSetting.MIN_FOOD_VALUE); // 10%
            }
        } catch (Exception e) {
            //System.out.println(String.format("[로그아웃] 사망 후 종료 오류 (%s)", getName()));
			System.out.println(String.format("[Logout] Error exiting after death (%s)", getName()));
            e.printStackTrace();
        }
    	
    	// 거래 중지
        try {
            if (getTradeID() != 0) {
            	new L1Trade().TradeCancel(this);// 트레이드중
            }
        } catch (Exception e) {
            //System.out.println(String.format("[로그아웃] 교환취소 오류 (%s)", getName()));
			System.out.println(String.format("[Logout] Exchange cancellation error (%s)", getName()));
            e.printStackTrace();
        }
        
    	// 결투 종료
        try {
            if (getFightId() != 0) {
                L1PcInstance fightPc = L1World.getInstance().getPlayer(getFightId());
                if (fightPc != null) {
                    fightPc.setFightId(0);
                    fightPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, 0, 0), true);
                }
                setFightId(0);
            }
        } catch (Exception e) {
            //System.out.println(String.format("[로그아웃] 결투 오류 (%s)", getName()));
			System.out.println(String.format("[Logout] Duel error (%s)", getName()));
            e.printStackTrace();
        }
        
    	// 파티 종료
        try {
            if (isInParty()) {
            	getParty().leaveMember(this);// 파티중
            }
            if (isInChatParty()) {
            	getChatParty().leaveMember(this);// 채팅파티중
            }
        } catch (Exception e) {
            //System.out.println(String.format("[로그아웃] 파티탈퇴 오류 (%s)", getName()));
			System.out.println(String.format("[Logout] Error leaving party (%s)", getName()));
            e.printStackTrace();
        }
        
    	// 애완동물을 월드 MAP상으로부터 지운다
        try {
            Object[] petList = getPetList().values().toArray();
            if (petList != null && petList.length > 0) {
            	L1PetInstance pet = null;
                L1SummonInstance summon = null;
                for (Object obj : petList) {
                    if (obj instanceof L1PetInstance) {// 펫
                        pet = (L1PetInstance) obj;
                        pet.getMap().setPassable(pet.getLocation(), true);
                        getPetList().remove(pet.getId());
                        pet.deleteMe();
                    } else if (obj instanceof L1SummonInstance) {// 서먼
                        summon = (L1SummonInstance) obj;
                        summon.deleteLogout();
                    }
                }
            }
        } catch (Exception e) {
            //System.out.println(String.format("[로그아웃] 펫, 서먼 제거 오류 (%s)", getName()));
			System.out.println(String.format("[Logout] Pet, Summon removal error (%s)", getName()));
            e.printStackTrace();
        }
        
    	// 인형 제거
        try {
			if (getDoll() != null) {
				getDoll().deleteDoll(true);
			}
        } catch (Exception e) {
            //System.out.println(String.format("[로그아웃] 인형 제거 오류 (%s)", getName()));
			System.out.println(String.format("[Logout] Error removing doll (%s)", getName()));
            e.printStackTrace();
        }
        
        // 퀘스트 엔피씨 종료
    	try {
            Object[] followerList = getFollowerList().values().toArray();
            if (followerList != null && followerList.length > 0) {
            	L1FollowerInstance follower = null;
                for (Object followerObject : followerList) {
                    if (followerObject == null) {
                    	continue;
                    }
                    follower = (L1FollowerInstance) followerObject;
                    follower.setParalyzed(true);
                    follower.spawn(follower.getNpcTemplate().getNpcId(), follower.getX(), follower.getY(), follower.getMoveState().getHeading(), follower.getMapId());
                    follower.deleteMe();
                }
            }
        } catch (Exception e) {
            //System.out.println(String.format("[로그아웃] L1FollowerInstance 제거 오류 (%s)", getName()));
			System.out.println(String.format("[Logout] Error removing L1FollowerInstance (%s)", getName()));
            e.printStackTrace();
        }
    	
    	// 복수 시스템 저장
    	try {
        	if (!StringUtil.isNullOrEmpty(getRevengeTarget())) {
        		RevengeTable.getInstance().endTargetPursuit(this, getRevengeTarget());
        	}
        	RevengeTable.getInstance().store(this);
        } catch (Exception e) {
            //System.out.println(String.format("[로그아웃] 복수시스템 저장 오류 (%s)", getName()));
			System.out.println(String.format("[Logout] Multiple system save error (%s)", getName()));
            e.printStackTrace();
        }
    	
    	// 퀘스트 저장
    	try {
        	//MonsterBookTable.getInstace().saveMonsterBookList(getId());
			//UserWeekQuestLoader.store(this);
        	BeginnerQuestUserTable.getInstance().update_progress(this);
        	HuntingQuestUserTable.getInstance().store(this);
        } catch (Exception e) {
			//System.out.println(String.format("[로그아웃] 퀘스트 저장 오류 (%s)", getName()));
			System.out.println(String.format("[Logout] Error saving quest (%s)", getName()));
			e.printStackTrace();
		}
    	
    	// 아인하사드 신의 저장
    	try {
        	if (_einhasadFaith != null) {
        		EinhasadFaithLoader.getInstance().upsert(this, _einhasadFaith.getInfos());
        		_einhasadFaith.dispose();
        		_einhasadFaith = null;
        	}
        } catch (Exception e) {
        	//System.out.println(String.format("[로그아웃] 아인하사드의 신의 갱신 오류 (%s)", getName()));
			System.out.println(String.format("[Logout] God of Einhasad update error (%s)", getName()));
        	e.printStackTrace();
        }
    	
    	// DB의 character_buff에 보존한다
        try {
            CharBuffTable.getInstance().storeBuff(this);
            skillStatus.clearSkillEffectTimer();
            skillStatus.clearPartyIconSkillBytes();
            skillStatus.clearSkillDelay();
            skillStatus.clearLearn();
        } catch (Exception e) {
            //System.out.println(String.format("[로그아웃] Buff 저장 오류 (%s)", getName()));
			System.out.println(String.format("[Logout] Error saving Buff (%s)", getName()));
            e.printStackTrace();
        }

        // 상점 구매제한(캐릭터) 저장
        try {
        	ShopLimitLoader.getInstance().storeFromCharacter(this);
        } catch (Exception e) {
        	//System.out.println(String.format("[로그아웃] 상점 구매제한 갱신 오류 (%s)", getName()));
			System.out.println(String.format("[Logout] Error updating store purchase limit (%s)", getName()));
        	e.printStackTrace();
        }
        
        // 성물 저장
    	try {
        	if (_favorBook != null) {
        		L1FavorBookUserLoader.getInstance().merge(this);
        		_favorBook.dispose();
            	_favorBook = null;
        	}
        } catch (Exception e) {
        	//System.out.println(String.format("[로그아웃] 성물 인벤토리 갱신 오류 (%s)", getName()));
			System.out.println(String.format("[Logout] Relic inventory update error (%s)", getName()));
        	e.printStackTrace();
        }
    	
    	// 실렉티스 전시회 저장
    	try {
        	if (_timeCollection != null) {
        		L1TimeCollectionUserLoader.getInstance().merge(this);
            	_timeCollection.dispose();
            	_timeCollection = null;
        	}
        } catch (Exception e) {
        	//System.out.println(String.format("[로그아웃] 셀렉티스 전시회 갱신 오류 (%s)", getName()));
			System.out.println(String.format("[Logout] Error updating Cellectis exhibition (%s)", getName()));
        	e.printStackTrace();
        }
    	
    	// 불멸의 가호(버프) 저장
    	try {
    		FreeBuffShieldHandler handler = _config.get_free_buff_shield();
        	if (handler != null) {
        		FreeBuffShieldLoader.getInstance().upsert(handler);
        		handler.dispose();
        	}
        } catch (Exception e) {
        	//System.out.println(String.format("[로그아웃] FREE_BUFF_SHIELD 갱신 오류 (%s)", getName()));
			System.out.println(String.format("[Logout] FREE_BUFF_SHIELD update error (%s)", getName()));
        	e.printStackTrace();
        }
    	
    	// 던전 시간 저장
    	try {
    		if (_dungoenTimer != null) {
    			_dungoenTimer.upsert();
    			_dungoenTimer.dispose();
    		}
        } catch (Exception e) {
        	//System.out.println(String.format("[로그아웃] 던전 시간 갱신 오류 (%s)", getName()));
			System.out.println(String.format("[Logout] Dungeon time update error (%s)", getName()));
        	e.printStackTrace();
        }
    	
    	stopEtcMonitor();// pc의 모니터를 stop 한다.
        setOnlineStatus(0);// 온라인 상태를 OFF로 해, DB에 캐릭터 정보를 기입한다
        
    	try {
        	if (_netConnection != null) {
        		EquipSetTable.getInstance().updateCurrentSet(getId(), _equipSet.getCurrentSet());
            	if (Config.TJ.TJ_COUPON_ENABLE) {
            		TJCouponLoader.getInstance().save(this);
            	}
        	}
        	if (_clan != null) {
        		_clan.updateClanMemberOnline(this);
        	}
            save();
            saveInventory();
            L1BookMark.WriteBookmark(this);
        } catch (Exception e) {
            //System.out.println(String.format("[로그아웃] 저장 오류 발생 (%s)", getName()));
			System.out.println(String.format("[Logout] Saving error occurred (%s)", getName()));
            e.printStackTrace();
        }
    }

	private boolean _destroyed = false;
	public boolean isDestroyed() {
		return _destroyed;
	}

    public void logout() {
        try {
			synchronized (this) {
				if (_destroyed) {
					return;
				}
				InstanceSpace.getInstance().getBackPc(this);// 앱센터 LFC
				logout_update();
                L1World world = L1World.getInstance();
				world.removeVisibleObject(this);
				world.removeObject(this);
                notifyPlayersLogout(getKnownPlayers());
                notifyPlayersLogout(world.getRecognizePlayer(this));
                _inventory.clearItems();
                WarehouseManager w = WarehouseManager.getInstance();
                w.delPrivateWarehouse(this.getAccountName());
                w.delElfWarehouse(this.getAccountName());
				w.delSpecialWarehouse(this.getName()); // 특수창고
				w.delPackageWarehouse(this.getAccountName());
                removeAllKnownObjects();
                stopHalloweenRegeneration();
                stopHpRegenerationByItem32Second();
                stopMpRegenerationByItem64Second();
                stopMpRegenerationByItem16Second();
                stopHpMpRegeneration();
                stopGameTimeCarrier();
                stopEquipmentTimer();
                setDead(true);
                setNetConnection(null);
                stopPapuBlessing(); // 파푸리온의 갑옷
                stopLindBlessing(); // 린드비오르의 갑옷
                stopElfArmorBlessing();// 신성한 요정족 판금 갑옷
                if (_teleport != null) {
                	_teleport.dispose();
                }
                if (_fatigue != null) {
                	_fatigue.dispose();
                	_fatigue = null;
                }
                for (S_Effect effect : effects.values()) {
                	effect.clear();
                	effect = null;
                }
                effects.clear();
                effects = null;
                _destroyed = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GameClient getNetConnection() {
    	return _netConnection;
    }
    public void setNetConnection(GameClient val) {
    	_netConnection = val;
    }

    public boolean isInParty() {
    	return _party != null;
    }
    public L1Party getParty() {
    	return _party;
    }
    public void setParty(L1Party val) {
    	_party = val;
    }

    public boolean isInChatParty() {
    	return getChatParty() != null;
    }
    public L1ChatParty getChatParty() {
    	return _chatParty;
    }
    public void setChatParty(L1ChatParty cp) {
    	_chatParty = cp;
    }

    public int getPartyID() {
    	return _partyID;
    }
    public void setPartyID(int partyID) {
    	_partyID = partyID;
    }

    public int getTradeID() {
    	return _tradeID;
    }
    public void setTradeID(int tradeID) {
    	_tradeID = tradeID;
    }

    public void setTradeOk(boolean tradeOk) {
    	_tradeOk = tradeOk;
    }
    public boolean getTradeOk() {
    	return _tradeOk;
    }
    
	public void setTradeReady(boolean tradeOk) {
		_tradeReady = tradeOk;
	}
	public boolean getTradeReady() {
		return _tradeReady;
	}

    public int getTempID() {
    	return _tempID;
    }
    public void setTempID(int tempID) {
    	_tempID = tempID;
    }

    public boolean isDrink() {
    	return _isDrink;
    }
    public void setDrink(boolean flag) {
    	_isDrink = flag;
    }

    public boolean isGres() {
    	return _isGres;
    }
    public void setGres(boolean flag) {
    	_isGres = flag;
    }

    private boolean _isPinkName;
    public boolean isPinkName() {
    	return _isPinkName;
    }
    public void setPinkName(boolean flag) {
    	_isPinkName = flag;
    }

    public ArrayList<L1PrivateShopSellList> getSellList() {
    	return _sellList;
    }
    public ArrayList<L1PrivateShopBuyList> getBuyList() {
    	return _buyList;
    }

    private byte[] _shopChat;
    public void setShopChat(byte[] chat) {
    	_shopChat = chat;
    }
    public byte[] getShopChat() {
    	return _shopChat;
    }

    private boolean _isPrivateShop;
    public boolean isPrivateShop() {
    	return _isPrivateShop;
    }
    public void setPrivateShop(boolean flag) {
    	_isPrivateShop = flag;
    }
    
    public int _isAutoClanMentCount;
	public boolean isAutoClanjoin() {
		return _isAutoClanjoin;
	}
	public void setAutoClanjoin(boolean flag) {
		_isAutoClanjoin = flag;
	}

    private int _special_size;
    public int getSpecialWareHouseSize() {
    	return _special_size;
    }
    public void setSpecialWareHouseSize(int special_size) {
    	_special_size = special_size;
    }

    public int getPartnersPrivateShopItemCount() {
    	return _partnersPrivateShopItemCount;
    }
    public void setPartnersPrivateShopItemCount(int i) {
    	_partnersPrivateShopItemCount = i;
    }
    
    private int birthday;// 생일
    public int getBirthDay() {
    	return birthday;
    }
    public void setBirthDay(int t) {
    	birthday = t;
    }

    public int[] DragonPortalLoc;// 드래곤 포탈

    public void sendPackets(ServerBasePacket serverbasepacket, boolean clear) {
        try {
        	sendPackets(serverbasepacket);
            if (clear) {
                serverbasepacket.clear();
                serverbasepacket = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void sendPackets(ServerBasePacket serverbasepacket) {
		if (getNetConnection() == null) {
			return;
		}
		try {
			getNetConnection().sendPacket(serverbasepacket);
		} catch (Exception e) {
		}
	}
	
	public void onAction(L1NpcInstance mon) {
		if (mon == null) {
			return;
		}
		if (getCurrentHp() > 0 && !isDead()) {
			L1Attack attack = new L1Attack(mon, this);
			if (attack.calcHit()) {
				attack.calcDamage();
				attack.addPcPoisonAttack(mon, this);
			}
			attack.action();
			attack.commit();
		}
	}

    @Override
    public void onAction(L1PcInstance attacker) {
        if (attacker == null || _teleport.isTeleport()) {
        	return;
        }
        if (getRegion() == L1RegionStatus.SAFETY || attacker.getRegion() == L1RegionStatus.SAFETY) {
            L1Attack attack_mortion = new L1Attack(attacker, this);
            attack_mortion.action();
            return;
        }
        if (checkNonPvP(this, attacker) == true) {
        	return;
        }

        if (getCurrentHp() > 0 && !isDead()) {
            attacker.delInvis();
            boolean isCounterBarrier = false, isInferno = false, isHalphas = false, isConqueror = false;
            L1Attack attack = new L1Attack(attacker, this);
            if (attack.calcHit()) {
                if (skillStatus.hasSkillEffect(L1SkillId.COUNTER_BARRIER) && attack.isShortDistance()
                		&& new L1Magic(this, attacker).calcProbabilityMagic(L1SkillId.COUNTER_BARRIER)){
                    isCounterBarrier = true;
                } else if (skillStatus.hasSkillEffect(L1SkillId.INFERNO) && attack.isShortDistance()
                		&& new L1Magic(this, attacker).calcProbabilityMagic(L1SkillId.INFERNO)) {
					isInferno = true;
				} else if (skillStatus.hasSkillEffect(L1SkillId.HALPHAS) && attack.isShortDistance()
						&& new L1Magic(this, attacker).calcProbabilityMagic(L1SkillId.HALPHAS)) {
                    isHalphas = true;
                } else if (isPassiveStatus(L1PassiveId.CONQUEROR) && attack.isShortDistance()
                		&& random.nextInt(100) + 1 <= Config.SPELL.CONQUEROR_PROB) {
                	isConqueror = true;
                }
                if (!isCounterBarrier && !isInferno && !isHalphas && !isConqueror) {
                    attacker.setPetTarget(this);
                    attack.calcDamage();
                    attack.addPcPoisonAttack(attacker, this);
                }
            }
            if (isCounterBarrier) {
            	if (attacker.is_reflect_emasculate(isPassiveStatus(L1PassiveId.COUNTER_BARRIER_MASTER) ? L1PassiveId.COUNTER_BARRIER_MASTER : null)) {
    				send_effect(isPassiveStatus(L1PassiveId.COUNTER_BARRIER_VETERAN) ? 17220 : 10710);
            		attack.action();
            	} else {
            		if (isPassiveStatus(L1PassiveId.COUNTER_BARRIER_MASTER)) {
            			attack.actionCounterBarrierMaster();
            			setCurrentHp(getCurrentHp() + Config.SPELL.COUNTER_BARRIER_MASTER_HP);
            		} else if (isPassiveStatus(L1PassiveId.COUNTER_BARRIER_VETERAN)) {
            			attack.actionCounterBarrierBeterang();
            		} else {
            			attack.actionCounterBarrier();
            		}
					attack.commitCounterBarrier();
            	}
            	attack.commit();
            } else if (isInferno) {
            	if (attacker.is_reflect_emasculate(null)) {
            		int rnd = random.nextInt(4) + 1;
            		if (rnd == 1) {
            			send_effect(17561);
            		} else if (rnd == 2) {
             			send_effect(17563);
             		} else if (rnd == 3) {
             			send_effect(17565);
             		} else {
             			send_effect(17567);
             		}
            		attack.action();
            	} else {
            		attack.clacInfernoDamage();
    				attack.actionInferno();
    				attack.commitInferno();
            	}
            	attack.commit();
			} else if (isHalphas) {
				if (attacker.is_reflect_emasculate(null)) {
					send_effect(18410);
            		attack.action();
            	} else {
            		attack.actionHalphas();
                    attack.commitHalphas();
            	}
				attack.commit();
            } else if (isConqueror) {
				if (attacker.is_reflect_emasculate(null)) {
					send_effect(21808);
            		attack.action();
            	} else {
            		attack.actionConcqueror();
                    attack.commitConcqueror();
            	}
				attack.commit();
            } else {
                attack.action();
                attack.commit();
            }
        }
    }
    
    /**
     * 대미지 반사 무시
     * @param defens_passive
     * @return boolean
     */
    public boolean is_reflect_emasculate(L1PassiveId defens_passive) {
    	int attacker_reflect = getAbility().getReflectEmasculate();
    	if (defens_passive != null) {
    		switch (defens_passive) {
        	case COUNTER_BARRIER_MASTER:
        		attacker_reflect -= 5;
        		break;
        	case DEMOLITION:
        		attacker_reflect >>= 1;
        		break;
        	default:
        		break;
        	}
    	}
    	if (attacker_reflect <= 0) {
    		return false;
    	}
		if (random.nextInt(100) + 1 <= attacker_reflect) {
			send_effect(18518);
			return true;
		}
		return false;
    }

    public boolean checkNonPvP(L1PcInstance pc, L1Character target) {
        L1PcInstance targetpc = null;
        if (target instanceof L1PcInstance) {
        	targetpc = (L1PcInstance) target;
        } else if (target instanceof L1PetInstance) {
        	targetpc = (L1PcInstance) ((L1PetInstance) target).getMaster();
        } else if (target instanceof L1SummonInstance) {
        	targetpc = (L1PcInstance) ((L1SummonInstance) target).getMaster();
        }
        if (targetpc == null) {
        	return false;
        }
        if (!Config.ALT.ALT_NONPVP) {
            if (getMap().isCombatZone(getLocation())) {
            	return false;
            }
            for (L1War war : L1World.getInstance().getWarList()) {
                if (war == null) {
                	continue;
                }
                if (pc.getClanid() != 0 && targetpc.getClanid() != 0) {
                    boolean same_war = war.CheckClanInSameWar(pc.getClanName(), targetpc.getClanName());
                    if (same_war == true) {
                    	return false;
                    }
                }
            }
            if (target instanceof L1PcInstance) {
                L1PcInstance targetPc = (L1PcInstance) target;
                if (isInWarAreaAndWarTime(pc, targetPc)) {
                	return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean isInWarAreaAndWarTime(L1PcInstance pc, L1PcInstance target) {
        int castleId = L1CastleLocation.getCastleIdByArea(pc);
        int targetCastleId = L1CastleLocation.getCastleIdByArea(target);
        if (castleId != 0 && targetCastleId != 0 && castleId == targetCastleId && War.getInstance().isNowWar(castleId)) {
            return true;
        }
        return false;
    }

    public void setPetTarget(L1Character target) {
        L1SummonInstance summon = null;
        for (L1NpcInstance pet : getPetList().values()) {
            if (pet == null) {
            	continue;
            }
            if (pet instanceof L1SummonInstance) {
                summon = (L1SummonInstance) pet;
                summon.setMasterTarget(target);
            }
        }
    }
    
    public void invisibleItem(){
		if (skillStatus.hasSkillEffect(L1SkillId.INVISIBILITY)) {
			return;
		}
		skillStatus.removeSkillEffect(L1SkillId.BLIND_HIDING);
		skillStatus.setSkillEffect(L1SkillId.INVISIBILITY, 0);
		invisible();
	}
    
    public void invisible(){
    	invisible(false);
    }
    
    /**
     * 투명 상태 활성화
     * @param blindhiding
     */
    public void invisible(boolean is_blindhiding){
    	S_Invis invis = new S_Invis(getId(), 1);
		sendPackets(invis);
		L1DollInstance doll = getDoll();
		
		if (is_blindhiding) {
			if (isPassiveStatus(L1PassiveId.BLIND_HIDING_ASSASSIN)) {
				getSkill().setBlindHidingAssassin(true);
				addMoveSpeedDelayRate(Config.SPELL.BLIND_HIDING_ASSASSIN_MOVE_SPEED_RATE);
				sendPackets(new S_SpellBuffNoti(this, L1SkillId.BLIND_HIDING_ASSASSIN, true, 3), true);
			}
		}
		
		S_Invis dollInvis = null;
		if (doll != null) {
			// 인형 소유자에게 인비지 상태 알림
			dollInvis = new S_Invis(doll.getId(), 1);
			sendPackets(dollInvis);
		}
		
		// 주위의 캐릭터에게 알림
		S_RemoveObject removePck		= null;
		S_RemoveObject removeDollPck	= null;
		boolean is_party				= isInParty();
		int pledge_id					= getClanid();
		for (L1PcInstance target : L1World.getInstance().getVisiblePlayer(this)) {
			boolean isEquals		= (is_party && getParty().isMember(target)) || (pledge_id != 0 && pledge_id == target.getClanid());
			boolean isFloatingEye	= target.isFloatingEye();
			if (isEquals) {
				// 같은 파티 또는 혈맹원
				target.sendPackets(invis);
				if (doll != null) {
					target.sendPackets(dollInvis);
				}
			} else if (isFloatingEye) {
				// 괴물눈 고기와 눈멀기 물약
				target.sendPackets(invis);
				if (doll != null) {
		    		if (removeDollPck == null) {
		    			removeDollPck = new S_RemoveObject(doll);
		    		}
		    		target.sendPackets(removeDollPck);
		    	}
			} else {
				// 오브젝트 제거
				if (removePck == null) {
					removePck = new S_RemoveObject(this);
				}
				target.sendPackets(removePck);
		    	if (doll != null) {
		    		if (removeDollPck == null) {
		    			removeDollPck = new S_RemoveObject(doll);
		    		}
		    		target.sendPackets(removeDollPck);
		    	}
			}
		}
		
		// 메모리 정리
		invis.clear();
		invis = null;
		if (dollInvis != null) {
			dollInvis.clear();
			dollInvis = null;
		}
		if (removePck != null) {
			removePck.clear();
			removePck = null;
		}
		if (removeDollPck != null) {
			removeDollPck.clear();
			removeDollPck = null;
		}
    }

    /**
     * 투명 상태 제거
     */
	public void delInvis() {
		if (isGmInvis()) {
			return;
		}
		boolean flag = false;
		if (skillStatus.hasSkillEffect(L1SkillId.INVISIBILITY)) {
			skillStatus.killSkillEffectTimer(L1SkillId.INVISIBILITY);
			flag = true;
		}
		if (skillStatus.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
			skillStatus.killSkillEffectTimer(L1SkillId.BLIND_HIDING);
			if (skillStatus.isBlindHidingAssassin()) {
				skillStatus.setBlindHidingAssassin(false);
			}
			if (isPassiveStatus(L1PassiveId.BLIND_HIDING_ASSASSIN)) {
				addMoveSpeedDelayRate(-Config.SPELL.BLIND_HIDING_ASSASSIN_MOVE_SPEED_RATE);
				sendPackets(new S_SpellBuffNoti(this, L1SkillId.BLIND_HIDING_ASSASSIN, false, -1), true);
			}
			flag = true;
		}
		if (!flag) {
			return;
		}
		del_invis_object();
	}

	/**
	 * 블라인드 하이딩 제거
	 */
    public void delBlindHiding() {
    	skillStatus.killSkillEffectTimer(L1SkillId.BLIND_HIDING);
        broadcastPacketWithMe(new S_Invis(getId(), 0), true);
        if (skillStatus.isBlindHidingAssassin()) {
        	skillStatus.setBlindHidingAssassin(false);
		}
        if (isPassiveStatus(L1PassiveId.BLIND_HIDING_ASSASSIN)) {
        	addMoveSpeedDelayRate(-Config.SPELL.BLIND_HIDING_ASSASSIN_MOVE_SPEED_RATE);
        	sendPackets(new S_SpellBuffNoti(this, L1SkillId.BLIND_HIDING_ASSASSIN, false, -1), true);
        }
        del_invis_object();
    }
    
    /**
     * 투명 상태 종료 후 오브젝트 처리
     */
    void del_invis_object() {
    	S_Invis s_invis = new S_Invis(getId(), 0);
		sendPackets(s_invis);
		
		L1DollInstance doll = getDoll();
		S_Invis s_doll_invis = null;
		if (doll != null) {
			s_doll_invis = new S_Invis(doll.getId(), 0);
			sendPackets(s_doll_invis);
		}
		
		// 화면 내 처리
		S_PCObject s_obj_pck		= null;
		S_NPCObject s_doll_obj_pck	= null;
		boolean is_party			= isInParty();
		int pledge_id				= getClanid();
		for (L1PcInstance target : L1World.getInstance().getVisiblePlayer(this)) {
			boolean isEquals		= (is_party && getParty().isMember(target)) || (pledge_id != 0 && pledge_id == target.getClanid());
			boolean isFloatingEye	= target.isFloatingEye();
			if (isEquals) {
				// 같은 파티 또는 혈맹원
				target.sendPackets(s_invis);
				if (doll != null) {
					target.sendPackets(s_doll_invis);
				}
			} else if (isFloatingEye) {
				// 괴물눈 고기와 눈멀기 물약
				target.sendPackets(s_invis);
				if (doll != null) {
					if (s_doll_obj_pck == null) {
						s_doll_obj_pck = new S_NPCObject(doll);
					}
					target.sendPackets(s_doll_obj_pck);
				}
			} else {
				if (s_obj_pck == null) {
					s_obj_pck = new S_PCObject(this);
				}
				target.sendPackets(s_obj_pck);
				if (doll != null) {
					if (s_doll_obj_pck == null) {
						s_doll_obj_pck = new S_NPCObject(doll);
					}
					target.sendPackets(s_doll_obj_pck);
				}
			}
		}
		
		s_invis.clear();
		s_invis = null;
		if (s_obj_pck != null) {
			s_obj_pck.clear();
			s_obj_pck = null;
		}
		if (s_doll_invis != null) {
			s_doll_invis.clear();
			s_doll_invis = null;
		}
		if (s_doll_obj_pck != null) {
			s_doll_obj_pck.clear();
			s_doll_obj_pck = null;
		}
    }
    
    /**
     * 괴물눈 고기와 눈멀기 상태로 화면내 투명 캐릭터 출력
     */
    public void showFloatingEyeToInvis() {
    	boolean is_party	= isInParty();
    	int pledge_id		= getClanid();
    	for (L1PcInstance target : L1World.getInstance().getVisiblePlayer(this)) {
    		if (!target.isInvisble() || (pledge_id != 0 && pledge_id == target.getClanid()) || (is_party && getParty().isMember(target))) {
    			continue;
    		}
			sendPackets(new S_PCObject(target), true);
		}
    }
    
    /**
     * 눈멀기 상태에서  화면내 투명 캐릭터 감춤
     */
    public void hideBlindToInvis() {
    	boolean is_party	= isInParty();
    	int pledge_id		= getClanid();
    	for (L1PcInstance target : L1World.getInstance().getVisiblePlayer(this)) {
    		if (!target.isInvisble() || (pledge_id != 0 && pledge_id == target.getClanid()) || (is_party && getParty().isMember(target))) {
    			continue;
    		}
			sendPackets(new S_RemoveObject(target), true);
			L1DollInstance target_doll = target.getDoll();
			if (target_doll != null) {
	    		sendPackets(new S_RemoveObject(target_doll), true);
	    	}
		}
    }
    
    public boolean isInWarArea() {
        boolean isNowWar = false;
        int castleId = L1CastleLocation.getCastleIdByArea(this);
        if (castleId != 0) {
        	isNowWar = War.getInstance().isNowWar(castleId);
        }
        return isNowWar;
    }
    
    public int soulBarrier(L1Character attacker, int newHp, int damage){
    	boolean barrierArmor = isPassiveStatus(L1PassiveId.SOUL_BARRIER_ARMOR);
    	if (barrierArmor) {
    		int reduc = 4 + (this.getLevel() >= 90 ? ((this.getLevel() - 90) >> 1) + 1 : 0), pvpReduc = (attacker instanceof L1PcInstance) ? 4 + (this.getLevel() >= 90 ? ((this.getLevel() - 90) >> 1) + 1 : 0) : 0;
    		if (reduc > 10) {
    			reduc = 10;
    		}
    		if (pvpReduc > 10) {
    			pvpReduc = 10;
    		}
    		reduc += pvpReduc;// 최종 리덕션
    		newHp += damage > reduc ? reduc : damage;// 리덕션에 따른 대미지 감소 처리
    	}
    	int newMp = newHp > 0 ? getCurrentMp() - newHp : getCurrentMp() + newHp;
		if (newMp > 0) {
			newHp = 10;
		} else {
			newHp = newMp = 0;
		}
		setCurrentMp(newMp);
		send_effect(barrierArmor ? 20467 : 14541);
    	return newHp;
    }
    
    /**
     * 브레이브 유니언에 의환 대미지 분산
     * @param attacker
     * @param damage
     */
    public void receiveDamageFromBraveUnion(L1PcInstance attacker, int damage) {
    	L1Party party	= getParty();
    	if (party == null) {
    		receiveDamage(attacker, damage);
    		return;
    	}
    	
    	// 대미지를 분산시킬 파티원
    	int map			= getMapId();
		Point pt		= getLocation();
		ArrayList<L1PcInstance> result = null;
		for (L1PcInstance member : party.getMembersArray()) {
			if (member == null || member == this || map != member.getMapId()) {
				continue;
			}
			if (member.getCurrentHpPercent() < 30 || pt.getTileLineDistance(member.getLocation()) > 8) {
				continue;
			}
			if (result == null) {
				result = new ArrayList<L1PcInstance>();
			}
			result.add(member);
		}
    	if (result == null || result.isEmpty()) {
    		receiveDamage(attacker, damage);
    		return;
    	}
    	
    	// 대미지 분산
    	int member_size = result.size();
    	double distribute_rate = 0.02D * member_size + (member_size == 7 ? 0.01D : 0D);// 최대 인원 자기자신은 빠진다
    	int distribute_total	= (int)(damage * distribute_rate);// 분배될 총 대미지
    	int distribute_damage	= distribute_total / member_size;// 멤버들에게 전달될 대미지
    	for (L1PcInstance member : result) {
    		member.receiveDamage(attacker, distribute_damage);// 멤버들에게 분배한다.
    		member.send_effect(21814);
    	}
    	receiveDamage(attacker, damage - distribute_total);// 나머지 대미지에 대한 처리
    	result.clear();
    	result = null;
    }
    
    public int halpahArmor(int hp, L1ItemInstance item){
		if (item != null && (item.getLastUsed() == null || (item.getLastUsed() != null && item.getLastUsed().getTime() < System.currentTimeMillis()))) {
			short plus = (short) item.getEnchantLevel();
			_halpasLoyaltyValue = 10 + plus;
			getAbility().addPVPDamage(_halpasLoyaltyValue);
			skillStatus.setSkillEffect(L1SkillId.FAITH_OF_HALPAH_PVP, 12000);
            hp = getMaxHp();
            send_effect(19074);
            sendPackets(new S_ServerMessage(7436), true);
            sendPackets(new S_SpellBuffNoti(this, L1SkillId.FAITH_OF_HALPAH_PVP, true, 12), true);
            if (item.getLastUsed() != null) {
            	item.getLastUsed().setTime(System.currentTimeMillis() + ((22 - plus) * 3600000));
            } else {
            	item.setLastUsed(new Timestamp(System.currentTimeMillis() + ((22 - plus) * 3600000)));
            }
			sendPackets(new S_SpellBuffNoti(false, L1SkillId.FAITH_OF_HALPAH, SkillIconNotiType.NEW), true);
		}
		return hp;
    }

    public void receiveDamage(L1Character attacker, int damage, int attr) {
        if (damage == 0) {
        	return;
        }
        int player_mr = getResistance().getEffectedMrBySkill();
        int rnd = random.nextInt(100) + 1;
        if (player_mr >= rnd) {
        	damage >>= 0x00000001;
        }
        
        receiveDamage(attacker, damage);
    }

    public void receiveManaDamage(L1Character attacker, int mpDamage) {
        if (mpDamage > 0 && !isDead()) {
            delInvis();
            if (attacker instanceof L1PcInstance) {
            	L1PinkName.onAction(this, attacker);
            }
            setCurrentMp(getCurrentMp() - mpDamage);
        }
    }
    
    public void receiveCountingDamage(L1Character attacker, int damage) {
        if (getCurrentHp() > 0 && !isDead()) {
            if (attacker != this && !knownsObject(attacker) && attacker.getMapId() == this.getMapId()) {
            	attacker.onPerceive(this);
            }
            if (damage > 0) {
                delInvis();
                removeSleepSkill();
                pressureCheck(attacker, (double)damage);
            } else if (damage < 0) {
                return;
            }
            L1ItemInstance weapon = this.getInventory().getEquippedWeapon();
            if (weapon != null && (weapon.getItemId() == 145 || weapon.getItemId() == 149)) {
            	damage += damage >> 1;
            }
            int newHp = getCurrentHp() - damage;
            L1ItemInstance armor = this.getInventory().getEquippedArmor();// 착용중인 갑옷
            if (newHp > getMaxHp()) {
            	newHp = getMaxHp();
            }
			if (newHp <= 10) {
				if (isElf() && skillStatus.hasSkillEffect(L1SkillId.SOUL_BARRIER)) {
					newHp = soulBarrier(attacker, newHp, damage);
				}
				if (_config._halpasLoyaltyEnable && armor != null && (armor.getItemId() == 23000 || armor.getItemId() == 23001 || armor.getItemId() == 23002)) {
					newHp = halpahArmor(newHp, armor);
				}
			}
			if (newHp <= 0) {
				if (isGm()) {
					// this.setCurrentHp(getMaxHp());
				} else {
					if (isDeathMatch()) {
						if (getMapId() == 5153) {
							try {
								save();
								beginGhost(getX(), getY(), (short) getMapId(), true);
								sendPackets(new S_ServerMessage(1271), true);
							} catch (Exception e) {
								_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
							}
							return;
						}
					} else {
						death(attacker, false);
					}
			    }
		    }
		    if (newHp > 0) {
		    	setCurrentHp(newHp);
		    }
	    } else if (!isDead()) {
		    System.out.println("■■■■■■■ Found a character whose HP reduction was incorrect. Or blood was 0 from the beginning.");
		    death(attacker, false);
	    }
    }
    
    public void receiveDamage(L1Character attacker, int damage) {
		if (getCurrentHp() > 0 && !isDead()) {
			/** 로봇시스템 */
			if (getRobotAi() != null && damage >= 0 && !(attacker instanceof L1EffectInstance)) {
				getRobotAi().setHate(attacker, damage);
			}
			/** 로봇시스템 */
			if (attacker == null) {
				return;
			}
			if (attacker != this && !knownsObject(attacker) && attacker.getMapId() == getMapId()) {
				attacker.onPerceive(this);
			}
			
			if (damage > 0) {
				delInvis();
				if (attacker instanceof L1PcInstance || attacker instanceof L1DoppelgangerInstance) {
					L1PinkName.onAction(this, attacker);
				} else if (attacker instanceof L1NpcInstance 
						&& ((L1NpcInstance) attacker).getNpcId() == 900519 && skillStatus.hasSkillEffect(L1SkillId.BUFF_ZAKEN_HALPAS)) {
					damage *= 0.8;
				}
				removeSleepSkill();
				pressureCheck(attacker, (double) damage);
			} else if (damage < 0){
				if (attacker instanceof L1PcInstance) {
					L1PinkName.onHelp(this, attacker);
				}
			}
			
			L1ItemInstance weapone = this.getWeapon();
			if (weapone != null && (weapone.getItemId() == 145 || weapone.getItemId() == 149)) {
				damage *= 1.5;
			}
			if (damage > 0 && isWizard() && skillStatus.hasSkillEffect(L1SkillId.DIVINE_PROTECTION) && _divineProtectionHp > 0) {// 디바인 프로텍션
				_divineProtectionHp -= damage;
				if (_divineProtectionHp < 0) {
					_divineProtectionHp = 0;
				}
				damage = 0;
				sendPackets(new S_InstanceHP(_divineProtectionHp, Config.SPELL.DIVINE_PROTECTION_HP_VALUE, true), true);
			}
			int newHp = getCurrentHp() - damage;
			if (newHp <= 10) {
				if (isElf() && skillStatus.hasSkillEffect(L1SkillId.SOUL_BARRIER)) {
					newHp = soulBarrier(attacker, newHp, damage);
				}
				L1ItemInstance armor = this.getInventory().getEquippedArmor();// 착용중인 갑옷
				if (_config._halpasLoyaltyEnable && armor != null && (armor.getItemId() == 23000 || armor.getItemId() == 23001 || armor.getItemId() == 23002)) {
					newHp = halpahArmor(newHp, armor);
				}
			}
			
			if (newHp > getMaxHp()) {
				newHp = getMaxHp();
			}
			if (newHp <= 0) {
				if (isGm()) {
					setCurrentHp(getMaxHp());
				} else {
					if (isDeathMatch()) {
						if (getMapId() == 5153) {
							try {
								save();
								beginGhost(getX(), getY(), (short) getMapId(), true);
								sendPackets(new S_ServerMessage(1271), true);
							} catch (Exception e) {
								_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
							}
							return;
						}
					} else {
						death(attacker, true);
					}
				}
			}
			if (newHp > 0) {
				setCurrentHp(newHp);
			}
		} else if(!isDead()) {
			System.out.println("■■■■■■■ Found a character whose HP reduction was incorrect. Or blood was 0 from the beginning.");
			death(attacker, true);
		}
	}

	public void death(L1Character lastAttacker, boolean deathPenalty) {
		synchronized (this) {
			if (isDead() /*|| hasSkillEffect(L1SkillId.STATUS_TOMAHAWK)*/) {
				return;
			}
			setDead(true);
			setActionStatus(ActionCodes.ACTION_Die);
		}
		if (lastAttacker instanceof L1PcInstance) {
			L1PcInstance _atker = (L1PcInstance) lastAttacker;
            if (Config.ALT.BROADCAST_KILL_LOG && getLevel() >= Config.ALT.BROADCAST_KILL_LOG_LEVEL) {
            	//StringBuilder sb = new StringBuilder().append("\\aH[").append(lastAttacker.getName()).append("]\\aA 님이 \\aG[").append(getName()).append("]\\aA 님을 죽였습니다.");
            	//StringBuilder sb = new StringBuilder().append("\\aH[").append(lastAttacker.getName()).append("]\\aA ").append("has killed").append(" \\aG[").append(getName()).append("]\\aA.").append("");
				StringBuilder sb = new StringBuilder().append("\\aH[").append(lastAttacker.getName()).append("]\\aA ").append(S_SystemMessage.getRefText(1075)).append(" \\aG[").append(getName()).append("]\\aA ").append(S_SystemMessage.getRefText(1076));		
				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(sb.toString()), true);
            	
            	lastAttacker.setKills(lastAttacker.getKills() + 1); // 이긴넘 킬수 +1
                setDeaths(getDeaths() + 1); // 진넘 데스수 +1
                //ManagerInfoThread.PvPCount += 1;
				if (_config.getHuntCount() > 0) {
					lastAttacker.getInventory().storeItem(L1ItemId.ADENA, _config.getHuntPrice());
					_config.setHuntCount(0);
					_config.setHuntPrice(0);
					_config.setReasonToHunt(null);
					_config.initBeWanted();
//AUTO SRM: 					sendPackets(new S_SystemMessage("\\fY수배가 풀려 추가 옵션이 사라졌습니다."), true); // CHECKED OK
					sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1053), true), true);
					sb.setLength(0);
					//sb.append("\\fT[").append(lastAttacker.getName()).append("]  ").append(getName()).append(" 님 현상금 감사합니다.");
					//[Attacker] Thank you for the bounty on Tungusca.
					sb.append("\\fT[").append(lastAttacker.getName()).append("]  ").append(S_SystemMessage.getRefText(1054)).append(getName()).append(S_SystemMessage.getRefText(1070));

					L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(sb.toString()), true);
					try {
						save();
					} catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
			}
            
            // 수호탑 점령전
            if (getNetConnection() != null && L1InterServer.isOccupyInter(getNetConnection().getInter()) && getMapId() == _atker.getMapId() && _occupyTeamType != null && _atker._occupyTeamType != null && _occupyTeamType != _atker._occupyTeamType) {
            	occupyDeath(_atker);
            }
            // %s가 적 %s를 죽엿습니다
            if (isInParty()) {
            	S_ServerMessage message = new S_ServerMessage(3689, getName());
				for (L1PcInstance member : getParty().getMembersArray()) {
					member.sendPackets(message);
				}
				message.clear();
				message = null;
			}
			if (_atker.isInParty()) {
				S_ServerMessage message = new S_ServerMessage(3690, lastAttacker.getName(), getName());
				for (L1PcInstance member : _atker.getParty().getMembersArray()) {
					member.sendPackets(message);
				}
				message.clear();
				message = null;
			} else {
				_atker.sendPackets(new S_ServerMessage(3691, lastAttacker.getName(), getName()), true);
			}
		}
		GeneralThreadPool.getInstance().execute(new Death(lastAttacker, deathPenalty));
	}

	private class Death implements Runnable {
		L1Character _lastAttacker;
		boolean _deathPenalty;

		Death(L1Character cha, boolean deathPenalty) {
			_lastAttacker = cha;
			_deathPenalty = deathPenalty;
			isItemDrop = true;
		}

		@Override
		public void run() {
			// TODO 캐릭터 사망 처리
			if (_teleport.isTeleport()) {
				teleportDelay();
				return;
			}

			L1Character lastAttacker = _lastAttacker;
			_lastAttacker = null;
			setCurrentHp(0);
			setGresValid(false);

			int targetobjid = getId();
			getMap().setPassable(getLocation(), true);

			if (!L1PcInstance.this._isArmorSetPoly) {
				unpoly();
			}

            L1SkillUse l1skilluse = new L1SkillUse(true);
            l1skilluse.handleCommands(L1PcInstance.this, L1SkillId.CANCELLATION, getId(), getX(), getY(), 0, L1SkillUseType.LOGIN);
            l1skilluse = null;
            if (_isBurningShot) {
            	BurningShot.dispose(L1PcInstance.this);
            }

			broadcastPacketWithMe(new S_DoActionGFX(targetobjid, ActionCodes.ACTION_Die), true);
            GeneralThreadPool.getInstance().schedule(new DeathLateDisconnectTimer(L1PcInstance.this), 300000);// 죽음 체크 타이머

            L1RegionStatus region = getRegion();
            if (lastAttacker != L1PcInstance.this) {
                L1PcInstance player = null;
                if (lastAttacker instanceof L1PcInstance) {
                	player = (L1PcInstance) lastAttacker;
                } else if (lastAttacker instanceof L1PetInstance) {
                	player = (L1PcInstance) ((L1PetInstance) lastAttacker).getMaster();
                } else if (lastAttacker instanceof L1SummonInstance) {
                	player = (L1PcInstance) ((L1SummonInstance) lastAttacker).getMaster();
                }
                
                if (player != null && region == L1RegionStatus.COMBAT) {
                    sendPackets(L1ServerMessage.sm3805);// 사망 패널티가 없는 지역: 패널티를 입지 않앗습니다.
                    return;
                }
                boolean sim_ret = simWarResult(lastAttacker);
                if (sim_ret == true) {
                	return;
                }
            }

            if (!getMap().isEnabledDeathPenalty()) {
            	return;
            }
            
            boolean isPVP = lastAttacker instanceof L1PcInstance;
            L1PcInstance attackPc = isPVP ? (L1PcInstance) lastAttacker : null;
            if (attackPc != null && getFightId() == attackPc.getId() && attackPc.getFightId() == getId()) {
            	fighting(attackPc);
            	return;
            }

            // 공성장 경험치하락안되게
            boolean is_siege_region = castleWarResult();
            if (isPVP && !is_siege_region && getLevel() < Config.ALT.BAPHOMET_SYSTEM_LEVEL && (lastAttacker.getLevel() - getLevel()) >= 10) {// 바포메트
            	isExpDrop = false;
            	isItemDrop = false;
            }
            if (is_siege_region == true) {
            	sendPackets(L1ServerMessage.sm3798);// 경험치 손실이 없는 지역 : 경험치가 손실되지 않았습니다.
            	return;
            }
            if (getReturnStat_exp() != 0) {
            	isExpDrop = false;
            	return;
            }
            
            if (region == L1RegionStatus.SAFETY) {// 세이프티존에선 패널티 없게
            	sendPackets(L1ServerMessage.sm3798);// 경험치 손실이 없는 지역 : 경험치가 손실되지 않았습니다.
                return;
            }
            
            if (GameServerSetting.SAFETY_MODE) {// 보호모드
    			sendPackets(L1SystemMessage.SAFTY_MODE_PENALTY_MSG);
    			return;
    		}
            
            if (_deathPenalty && attackPc != null) {
            	attackPc.sendPackets(new S_PacketBox(S_PacketBox.BATTLE_SHOT, getId()), true);// PK멘트
            	if (getAlignment() >= 0 && isPinkName() == false) {
            		alignmentPenalty(attackPc);
                	setFreeBuffShieldPKPenalty((L1PcInstance) lastAttacker);
                } else {
                    setPinkName(false);
                }
            }
            
            // 복수 시스템
            if (isPVP && getLevel() >= 75 && lastAttacker.getLevel() >= 75) {
            	revenge((L1PcInstance) lastAttacker);
            }
            
            if (isPanaltyProtectItem(lastAttacker, isPVP)) {// 보호 아이템
            	return;
            }

			deathExpPenalty();
            setGresValid(true);

            if (getExpRes() == 0) { // 조우의 가호로 경험치 회복 fix
                if (isPVP && getLevel() < Config.ALT.BAPHOMET_SYSTEM_LEVEL && (lastAttacker.getLevel() - getLevel()) >= 10) {
                } else {
                    setExpRes(1);
                }
            }

            // 경비병에게 사망시
            if (lastAttacker instanceof L1GuardInstance) {
                if (getPKcount() > 0) {
                	setPKcount(getPKcount() - 1);
                }
                setLastPk(null);
            }

            // 사망시 아이템 & 마법 드랍
            if (isPVP) {
            	if (Config.PENALTY.PVP_ITEM_DROP) {
            		deathPenaltySetting(isPVP);
            	}
			} else {
				if (Config.PENALTY.PVE_ITEM_DROP) {
					deathPenaltySetting(isPVP);
				}
			}
        }
		
		/**
		 * 텔레포트 중에는 텔레포트 완료후 처리한다.
		 */
		void teleportDelay() {
			sendPackets(S_Paralysis.TELEPORT_UNLOCK);
			_teleport.setTeleport(false);
			GeneralThreadPool.getInstance().schedule(this, 300);
		}
		
		/**
		 * 변신 이미지 초기화
		 */
		void unpoly() {
			int spriteId = 0;
            if (skillStatus.hasSkillEffect(L1SkillId.SHAPE_CHANGE)) {
            	spriteId = getSpriteId();
                setTempCharGfxAtDead(spriteId);
                skillStatus.removeSkillEffect(L1SkillId.SHAPE_CHANGE);
            } else if (skillStatus.hasSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION)) {
            	spriteId = getSpriteId();
                setTempCharGfxAtDead(spriteId);
                skillStatus.removeSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION);
            } else if (skillStatus.hasSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL)) {
            	spriteId = getSpriteId();
                setTempCharGfxAtDead(spriteId);
                skillStatus.removeSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL);
            } else {
                setTempCharGfxAtDead(getClassId());
            }
            
            if (spriteId == 5727 || spriteId == 5730 || spriteId == 5733 || spriteId == 5736) {
            	spriteId = 0;
            }
            if (spriteId != 0) {
            	broadcastPacketWithMe(new S_Polymorph(getId(), spriteId, getCurrentWeapon()), true);
            }
		}
		
		/**
		 * 결투
		 * @param attackPc
		 */
		void fighting(L1PcInstance attackPc) {
			setFightId(0);
        	sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, 0, 0), true);
        	attackPc.setFightId(0);
        	attackPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, 0, 0), true);
		}
		
		/**
		 * 복수 시스템
		 * @param attackPc
		 */
		void revenge(L1PcInstance attackPc) {
        	if (attackPc instanceof L1AiUserInstance) {
        		L1World.getInstance().broadcastPacketToAll(new S_MessegeNoti(7040, attackPc.getName(), getName(), 0), true);// %s (이)가, %s (을)를 제압하였습니다.
        		((L1AiUserInstance) attackPc).send_ment(AiLoader.getInstance().getMent(AiMent.KILL));
        		return;
        	}
        	RevengeTable.getInstance().checkRevenge(L1PcInstance.this, attackPc);
		}
		
		/**
		 * 성향치 패널티
		 * @param attackPc
		 */
		void alignmentPenalty(L1PcInstance attackPc) {
			int beforeAlign = attackPc.getAlignment();
			if (beforeAlign < 30000) {// 만라우풀이 아니다.
        		attackPc.setPKcount(attackPc.getPKcount() + 1);
        		attackPc.setLastPk();
            }
			// 만카오틱이면 추가적 패널티 설정 안함
			if (beforeAlign <= Short.MIN_VALUE) {
    			return;
    		}
			
			// 카오틱 패널티 제외
    		boolean isNotAlignPenalty = (GameServerSetting.OMAN_CRACK > 0 && attackPc.getMapId() == GameServerSetting.OMAN_CRACK)// 균열의 오만의 탑 제외
            		|| (attackPc._config != null && attackPc._config.isNotAlignmentPenalty())// 광역 스펠에 의한 영향 제외
            		|| (Config.ETC.FREE_PVP_REGION_ENABLE && attackPc._config.isFreePVPRegion());// FREE PVP 지역 설정 제외
    		if (isNotAlignPenalty) {
    			return;
    		}
    		
    		int newAlign = beforeAlign - 30000;
            if (newAlign < Short.MIN_VALUE) {
            	newAlign = Short.MIN_VALUE;
            }
            
            attackPc.setAlignment(newAlign);
            attackPc.broadcastPacketWithMe(new S_Alignment(attackPc.getId(), attackPc.getAlignment()), true);
		}
		
		/**
		 * 아이템에 의한 페널티 보호 여부 확인
		 * @param lastAttacker
		 * @param isPVP
		 * @return boolean
		 */
		boolean isPanaltyProtectItem(L1Character lastAttacker, boolean isPVP){
			boolean isInter = getMap().getInter() != null;
			// 인터서버가 아닌경우 불멸의 가호(버프)가 먼저 적용된다.
			if (!isInter && isFreeBuffShieldPenaltyProtect()) {
				return true;
			}
			L1ItemInstance item	= null;
			L1PcInventory inv	= getInventory();
	        for (ProtectItemData protect : PenaltyItemTable.getData()) {
	        	item = inv.findItemId(protect._itemId);
	        	if (item == null) {
	        		continue;
	        	}
	    		if (protect._type == ProtectType.EQUIP && !item.isEquipped()) {
	    			continue;// 착용 여부
	    		}
	    		if (!protect._mapIds.isEmpty() && !protect._mapIds.contains(getMapId())) {
	    			continue;// 위치 여부
	    		}
	    		sendPackets(new S_ServerMessage(protect._msgId), true);// 경험치보호아이템: 경험치를 손실하지 않앗습니다.
	    		if (protect._remove) {
	    			sendPackets(new S_ServerMessage(158, item.getItem().getDesc()), true);// 소멸:%s가 증발 하엿습니다.
	    			inv.consumeItem(protect._itemId, 1);
	    		}
	    		if (protect._dropItemId > 0 && isPVP) {
	    			L1ItemInstance drop = ItemTable.getInstance().createItem(protect._dropItemId); // 드랍 시킬 아이템
	    			L1World.getInstance().getInventory(getX(), getY(), getMapId()).storeItem(drop);
	    			drop.startItemOwnerTimer((L1PcInstance)lastAttacker);// 마지막 어택자에게 소유권
	    		}
	    		if (!protect._itemPanalty) {
	    			if (isPVP) {
	    				if (Config.PENALTY.PVP_ITEM_DROP) {
	    					deathPenaltySetting(isPVP);
	    				}
	    			} else {
	    				if (Config.PENALTY.PVE_ITEM_DROP) {
	    					deathPenaltySetting(isPVP);
	    				}
	    			}
	    		}
	    		if (!protect._expPanalty) {
	    			deathExpPenalty();
	    		}
	    		return true;
	        }
	        // 인터서버인 경우 불멸의 가호(버프)가 나중에 적용된다.
	        if (isInter && isFreeBuffShieldPenaltyProtect()) {
				return true;
			}
	        return false;
		}
		
		/**
		 * PK시 공격자에게 가호(버프) 패널티 부여
		 * @param attacker
		 */
		void setFreeBuffShieldPKPenalty(L1PcInstance attacker) {
			if (!Config.FREEBUFF.FREE_BUFF_SHIELD_ENABLE) {
				return;
			}
			// 공격자가 파티 상태인 경우 화면내 파티원 전원에게 패널티를 부여한다.
			if (attacker.isInParty()) {
				for (L1PcInstance member : L1World.getInstance().getVisiblePartyPlayer(attacker, -1)) {
					if (member == null || member.getNetConnection() == null) {
						continue;
					}
					doFreeBuffShieldPKPenalty(member);
				}
				return;
			}
			doFreeBuffShieldPKPenalty(attacker);
		}
		
		/**
		 * 공격자 또는 공격자의 화면내 파티원에게  PK 가호(버프) 패널티 부여 시작
		 * @param attacker
		 */
		void doFreeBuffShieldPKPenalty(L1PcInstance attacker) {
			// 공격자의 가호(버프) 핸들러
			FreeBuffShieldHandler handler = attacker._config.get_free_buff_shield();
			// 공격자에게 PK패널티를 부여한다.
			handler.start_disable_penalty(Config.FREEBUFF.FAVOR_LOCKED_TIME_SECOND);
			attacker.sendPackets(new S_FreeBuffShieldUpdateNoti(handler), true);// 가호 패널티 알림
			if (!attacker.isPCCafe) {
				return;
			}
			attacker.sendPackets(new S_PCMasterFavorUpdateNoti(handler), true);// PC플레이마스터 알림
		}
		
		/**
		 * 가호(버프)에 의한 패널티 보호 여부
		 * @return boolean
		 */
		boolean isFreeBuffShieldPenaltyProtect() {
			if (!Config.FREEBUFF.FREE_BUFF_SHIELD_ENABLE) {
				return false;
			}
			
			// 본인 또는 파티원이 시야 내에서 정당방위 상태로 변한 경우 가호 임시 중지
			if (L1PcInstance.this.isPinkName()) {
				return false;
			}
			if (L1PcInstance.this.isInParty()) {
				for (L1PcInstance member : L1World.getInstance().getVisiblePartyPlayer(L1PcInstance.this, -1)) {
					if (member == null || member.getNetConnection() == null || member == L1PcInstance.this) {
						continue;
					}
					if (member.isPinkName()) {
						return false;
					}
				}
			}
			
			FreeBuffShieldHandler handler	= _config.get_free_buff_shield();
			
			// 픽시의 불멸의 가호 버프(가호 개수 무제한)
			if (handler.is_pccafe_death_penalty_shield((int)getMapId())) {
				sendPackets(L1ServerMessage.sm9241);// 픽시의 축복으로 죽음을 극복합니다. 사망 경험치와 아이템 드랍 패널티가 없습니다.
				return true;
			}
			
			// 사용할 가호를 우선순위로 조사한다.(이벤트 > 무료 > PC방)
			FREE_BUFF_SHIELD_INFO free_info = handler.getUseFavor();
			if (free_info == null) {
				return false;
			}

			free_info.add_favor_remain_count(-1);// 가호 소모
			sendPackets(new S_FreeBuffShieldUpdateNoti(_config.get_free_buff_shield()), true);// 가호 소모 알림
			switch (free_info.get_favor_type()) {
			case PC_CAFE_SHIELD:
				sendPackets(new S_PCMasterFavorUpdateNoti(handler), true);// PC방 가호 소모 알림
				// PC방에서 사망하여 고급 불멸의 가호(PC)가 소모되었습니다. 사망 경험치와 아이템 드랍 패널티가 없습니다. (남은 수량: %d개)
				sendPackets(new S_ServerMessage(9229, free_info.get_favor_remain_count()), true);
				break;
			default:
				// 사망하여 %s가 소모되었습니다. 사망 경험치와 아이템 드랍 패널티가 없습니다. (남은 수량: %s개)
				sendPackets(new S_ServerMessage(9501, free_info.get_favor_type().toInt(), free_info.get_favor_remain_count()), true);
				break;
			}
			return true;
		}
		
    }
	
	class DeathLateDisconnectTimer implements Runnable {
		private L1PcInstance _pc;
		public DeathLateDisconnectTimer(L1PcInstance pc) {
			_pc = pc;
		}

		@Override
		public void run() {
			try {
				if (!_pc.isDead()) {
					return;
				}
				_pc.sendPackets(S_Disconnect.DISCONNECT);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void deathPenaltySetting(boolean isPVP){
		if (L1CastleLocation.getCastleIdByArea(this) != 0) {
			return;// 공성존에서 드랍안되도록
		}
        if (getRobotAi() != null || isGm() || !isItemDrop || getAlignment() >= Short.MAX_VALUE) {
        	return;
        }
		int lostRate = isPVP ? Config.PENALTY.PVP_ITEM_DROP_PERCENT : Config.PENALTY.PVE_ITEM_DROP_PERCENT;
		if (getAlignment() < 0) {
			lostRate = 100;// 카오상태 100% 설정
		}
		if (random.nextInt(100) + 1 <= lostRate) {
			deathPenaltyCount(isPVP);
		}
	}
	
	private void deathPenaltyCount(boolean isPVP){
		int count = 0;
		int align = getAlignment();
		if (align <= -30000) {
			count = random.nextInt(5);
		} else if (align <= -20000) {
			count = random.nextInt(4);
		} else if (align <= -10000) {
			count = random.nextInt(4);
		} else if (align < 0) {
			count = random.nextInt(3);
		} else if (align <= 10000) {
			count = random.nextInt(2);
		} else if (align <= 20000) {
			count = random.nextInt(2);
		} else if (align <= 30000) {
	    	count = random.nextInt(2);
	    } else if (align <= 32766) {
	    	count = random.nextInt(2);
	    }
		deathPenaltyItem(count, isPVP);
		deathPenaltySkill(count - 1);
	}
	
    private void deathPenaltyItem(int count, boolean isPVP) {
    	if (count <= 0) {
    		return;
    	}
    	L1ItemInstance item = null;
    	L1PcInventory inv = getInventory();
		for (int i = 0; i < count; i++) {
			item = inv.getDeathPenaltyItem(i == 0 && count > 1 ? true : false);
			if (item == null) {
				continue;
			}
			//ManagerInfoThread.PenaltyCount += 1;   // MANAGER DISABLED
			if (Config.PENALTY.ITEM_PENALTY_LOST_DELETE_ACTIVE 
					&& (isPVP || item.getBless() > 3 || item.isSlot())) {// pvp or 봉인 아이템 or 제련석 등록 아이템
				if (Config.PENALTY.ITEM_PENALTY_LOST_REPAIR_ENABLE) {
					DeathPenaltyTable.getInstance().insertItem(this, item);
				}
				inv.removeItem(item, item.isMerge() ? item.getCount() : 1);
	            sendPackets(new S_ServerMessage(158, item.getLogNameRef()), true); // %0%s 증발되어 사라집니다.
				//Manager.getInstance().PenaltyAppend(item.getLogNameRef(), getName(), count, 1); // MANAGER DISABLED
				LoggerInstance.getInstance().addItemAction(ItemActionType.del, this, item, count);
			} else {
				inv.tradeItem(item, item.isMerge() ? item.getCount() : 1, L1World.getInstance().getInventory(getX(), getY(), getMapId()));
	            sendPackets(new S_ServerMessage(638, item.getLogNameRef()), true); // %0%s 잃엇습니다.
				//Manager.getInstance().PenaltyAppend(item.getLogNameRef(), getName(), count, 0); // MANAGER DISABLED
				LoggerInstance.getInstance().addItemAction(ItemActionType.del1, this, item, count);
			}
		}
	}
    
	private void deathPenaltySkill(int count) {
		if (count <= 0 || isIllusionist()) {
			return;
		}
		for (int i = 0; i < count; i++) {
			int lostskilll = -1;
			if (isKnight() || isWarrior()) {
				lostskilll = random.nextInt(8);
			} else if (isElf()) {
				lostskilll = random.nextInt(48);
			} else if (isWizard()) {
				lostskilll = random.nextInt(72);
			} else {
				lostskilll = random.nextInt(16);
			}
			if (lostskilll == -1 || !this.getSkill().isLearnActive(lostskilll, true)) {// 습득 여부 체크
				continue;
			}
			L1Skills skill = SkillsTable.getTemplate(lostskilll);
			if (skill == null) {
				continue;
			}
			sendPackets(new S_ServerMessage(638, skill.getName()), true);// %0%s 잃엇습니다.
			getSkill().spellActiveLost(skill);// DB에서 삭제
			sendPackets(new S_AvailableSpellNoti(skill.getSkillId(), false), true);// 스킬UI에서 삭제
		}
	}

    // 군주 죽으면 혈원 자동 베르
    public boolean castleWarResult() {
        if (getClanid() != 0 && isCrown()) {
            L1Clan clan = this.getClan();
            for (L1War war : L1World.getInstance().getWarList()) {
                if (war == null) {
                	continue;
                }
                int warType = war.GetWarType();
                boolean isInWar = war.CheckClanInWar(getClanName());
                boolean isAttackClan = war.CheckAttackClan(getClanName());
                if (getId() == clan.getLeaderId() && warType == 1 && isInWar && isAttackClan) {
                    String enemyClanName = war.GetEnemyClanName(getClanName());
                    if (enemyClanName != null) {
                        if (war.GetWarType() == 1) {// 공성전일경우
                            L1PcInstance clan_member[] = clan.getOnlineClanMember();//
                            int castle_id = war.GetCastleId();
                            int[] loc = new int[3];
                            loc = L1CastleLocation.getGetBackLoc(castle_id);
                            int locx = loc[0];
                            int locy = loc[1];
                            short mapid = (short) loc[2];
                            for (int k = 0; k < clan_member.length; k++) {
                                if (L1CastleLocation.checkInWarArea(castle_id, clan_member[k])) {
                                	clan_member[k].getTeleport().start(locx, locy, mapid, 5, true);// 기내에있는혈원강제텔레포트
                                }
                            }
                        }
                        war.CeaseWar(getClanName(), enemyClanName); // 종결
                    }
                    break;
                }
            }
        }

        int castleId = 0;
        boolean isNowWar = false;
        castleId = L1CastleLocation.getCastleIdByArea(this);
        if (castleId != 0) {
        	isNowWar = War.getInstance().isNowWar(castleId);
        }
        return isNowWar;
    }

    // 군주 죽으면 혈원 자동 베르 //
    public boolean simWarResult(L1Character lastAttacker) {
        if (getClanid() == 0 || Config.ALT.SIM_WAR_PENALTY) {
        	return false;
        }
        L1PcInstance attacker = null;
        String enemyClanName = null;
        boolean sameWar = false;
        if (lastAttacker instanceof L1PcInstance) {
        	attacker = (L1PcInstance) lastAttacker;
        } else if (lastAttacker instanceof L1PetInstance) {
        	attacker = (L1PcInstance) ((L1PetInstance) lastAttacker).getMaster();
        } else if (lastAttacker instanceof L1SummonInstance) {
        	attacker = (L1PcInstance) ((L1SummonInstance) lastAttacker).getMaster();
        } else {
        	return false;
        }
        L1Clan clan = null;
        for (L1War war : L1World.getInstance().getWarList()) {
            if (war == null) {
            	continue;
            }
            clan = this.getClan();
            int warType = war.GetWarType();
            boolean isInWar = war.CheckClanInWar(getClanName());
            if (attacker != null && attacker.getClanid() != 0) {
            	sameWar = war.CheckClanInSameWar(getClanName(), attacker.getClanName());
            }
            if (getId() == clan.getLeaderId() && warType == 2 && isInWar == true) {
                enemyClanName = war.GetEnemyClanName(getClanName());
                if (enemyClanName != null) {
                	war.CeaseWar(getClanName(), enemyClanName);
                }
            }
            if (warType == 2 && sameWar) {
            	return true;
            }
        }
        return false;
    }

    public void deathExpPenalty() {
        int deathLevel = getLevel();
        if (deathLevel <= 55) {
        	return;// 55레벨 이하 경험치 패널티 없음
        }
        float ratio = DeathPenaltyTable.getLostExpRatio(deathLevel) * 0.01F;
        int resExp = (int) (ExpTable.getNeedExpNextLevel(deathLevel) * ratio);
        if (resExp <= 0) {
        	return;
        }
        addExp(-resExp);
        DeathPenaltyTable.getInstance().insertExp(this, resExp, deathLevel);
    }
    
    private void occupyDeath(L1PcInstance attacker){
		OccupyHandler handler = OccupyManager.getInstance().getHandler(this.getMapId() == L1TownLocation.MAP_WOLRDWAR_HEINE_TOWER ? OccupyType.HEINE : OccupyType.WINDAWOOD);
    	if (handler == null) {
    		return;
    	}
		if (handler.isMainTowerArea(this.getX(), this.getY()) && handler.isMainTowerArea(attacker.getX(), attacker.getY())) {
    		OccupyTeam atkTeam = handler.getTeamInfo(attacker._occupyTeamType);
    		OccupyTeam dieTeam = handler.getTeamInfo(this._occupyTeamType);
        	if (atkTeam != null && dieTeam != null) {
        		atkTeam.addPoint(1);
        		L1ItemInstance consumBadge = getInventory().findItemId(dieTeam.getBadgeId());
        		if (consumBadge != null && consumBadge.getCount() >= 2) {// 2개이상
        			getInventory().removeItem(consumBadge, 1);// 훈장 제거
        			L1ItemInstance item = attacker.getInventory().storeItem(atkTeam.getBadgeId(), 1);
        			//attacker.sendPackets(new S_ServerMessage(403, String.format("%s (1)", item.getDescKr())), true);// 훈장 획득
					attacker.sendPackets(new S_ServerMessage(403, String.format("%s (1)", item.getDesc())), true);// 훈장 획득
        			handler.compareLargeBadge(attacker, item.getCount());
        		}
        	}
    	}
	}
 	
 	// TODO 보너스 경험치
 	private int _exp_boosting_ratio;
 	public int get_exp_boosting_ratio() {
 		if (_netConnection == null) {
 			return 0;
 		}
 		
 		L1ExpPotion potion	= skillStatus.getExpPotion();
 		int add_ratio		= potion.getExpBonusValue();
 		boolean isTopaz		= skillStatus.hasSkillEffect(L1SkillId.DRAGON_TOPAZ);
		if (_account != null && _account.getEinhasad() != null && _account.getEinhasad().getPoint() >= Config.EIN.REST_EXP_DEFAULT_RATION) {
			add_ratio += 300;
			if (skillStatus.hasSkillEffect(L1SkillId.EMERALD_YES)) {
				add_ratio += 54;
			} else if (skillStatus.hasSkillEffect(L1SkillId.DRAGON_PUPLE)) {
				add_ratio += L1ExpPlayer.getDragonPupleExp(getLevel());
			} else if (isTopaz) {
				add_ratio += 150;
			}
			add_ratio += potion.getEinhasadExpBonusValue();
			if (skillStatus.hasSkillEffect(L1SkillId.LEVELUP_BONUS_BUFF)) {
				add_ratio += 30;
			}
			if (_teleport.isGrowBuff()) {// 성장 버프
				add_ratio += 300;
			}
		} else {
			if (isPCCafe || skillStatus.hasSkillEffect(L1SkillId.EINHASAD_FAVOR)) {
				add_ratio += 100;
			}
		}
		if (isTopaz) {
			add_ratio += 50;
		}
		
		if (isPCCafe && skillStatus.hasSkillEffect(L1SkillId.BUFF_PCCAFE_EXP)) {
			add_ratio += 10;
		}
		
		add_ratio += getAbility().getBlessExp();
		if (GameServerSetting.GOLD_BLESS_EXP > 0) {// 황금 성단의 축복
			add_ratio += GameServerSetting.GOLD_BLESS_EXP;
		}
		if ((getMapId() >= 101 && getMapId() <= 111) && getInventory().getCollection().isSasinGraceStatus()) {// 사신의 가호
			add_ratio += 20;
		}
 		return	_exp_boosting_ratio + add_ratio;
 	}
 		
 	public void add_exp_boosting_ratio(int i) {
 		_exp_boosting_ratio += i;
 		sendPackets(new S_ExpBoostingInfo(this), true);
 	}
 
    public L1BookMark getBookMark(String name) {
        L1BookMark element = null;
        int size = _bookmarks.size();
        for (int i = 0; i < size; i++) {
            element = _bookmarks.get(i);
            if (element == null) {
            	continue;
            }
            if (element.getName().equalsIgnoreCase(name)) {
            	return element;
            }
        }
        return null;
    }
	public L1BookMark getBookMark(int mapid, int x, int y) {
		L1BookMark element = null;
		for (L1BookMark bk : this._bookmarks) {
			element = bk;
			if ((element.getMapId() == mapid) && (element.getLocX() == x) && (element.getLocY() == y)) {
				return element;
			}
		}
		return null;
	}

    public L1BookMark getBookMark(int id) {
        L1BookMark element = null;
        int size = _bookmarks.size();
        for (int i = 0; i < size; i++) {
            element = _bookmarks.get(i);
            if (element == null) {
            	continue;
            }
            if (element.getId() == id) {
            	return element;
            }
        }
        return null;
    }
    public ArrayList<L1BookMark> getBookMark(){
    	return _bookmarks;
    }
    public int getBookMarkSize() {
    	return _bookmarks.size();
    }
    public void addBookMark(L1BookMark book) {
    	_bookmarks.add(book);
    }
    public void removeBookMark(L1BookMark book) {
    	_bookmarks.remove(book);
    }
    
    public L1ItemInstance getWeapon() {
    	return _weapon;
    }
    public void setWeapon(L1ItemInstance weapon) {
    	_weapon = weapon;
    }

    public L1Quest getQuest() {
    	return _quest;
    }

    public String getClassNameKr() {
        if (isCrown()) {
        	return L1Class.CROWN.getClassNameKr();
        }
        if (isKnight()) {
        	return L1Class.KNIGHT.getClassNameKr();
        }
        if (isElf()) {
        	return L1Class.ELF.getClassNameKr();
        }
        if (isWizard()) {
        	return L1Class.WIZARD.getClassNameKr();
        }
        if (isDarkelf()) {
        	return L1Class.DARKELF.getClassNameKr();
        }
        if (isDragonknight()) {
        	return L1Class.DRAGONKNIGHT.getClassNameKr();
        }
        if (isIllusionist()) {
        	return L1Class.ILLUSIONIST.getClassNameKr();
        }
    	if (isWarrior()) {
    		return L1Class.WARRIOR.getClassNameKr();
    	}
        if (isFencer()) {
        	return L1Class.FENCER.getClassNameKr();
        }
        if (isLancer()) {
        	return L1Class.LANCER.getClassNameKr();
        }
        return L1Class.NONE.getClassNameKr();
    }

    public String getClassNameEn() {
        if (isCrown()) {
        	return L1Class.CROWN.getClassNameEn();
        }
        if (isKnight()) {
        	return L1Class.KNIGHT.getClassNameEn();
        }
        if (isElf()) {
        	return L1Class.ELF.getClassNameEn();
        }
        if (isWizard()) {
        	return L1Class.WIZARD.getClassNameEn();
        }
        if (isDarkelf()) {
        	return L1Class.DARKELF.getClassNameEn();
        }
        if (isDragonknight()) {
        	return L1Class.DRAGONKNIGHT.getClassNameEn();
        }
        if (isIllusionist()) {
        	return L1Class.ILLUSIONIST.getClassNameEn();
        }
    	if (isWarrior()) {
    		return L1Class.WARRIOR.getClassNameEn();
    	}
        if (isFencer()) {
        	return L1Class.FENCER.getClassNameEn();
        }
        if (isLancer()) {
        	return L1Class.LANCER.getClassNameEn();
        }
        return L1Class.NONE.getClassNameEn();
    }


    public boolean isCrown() {
        return _classId == CLASSID_PRINCE || _classId == CLASSID_PRINCESS;
    }

    public boolean isKnight() {
        return _classId == CLASSID_KNIGHT_MALE || _classId == CLASSID_KNIGHT_FEMALE;
    }

    public boolean isElf() {
        return _classId == CLASSID_ELF_MALE || _classId == CLASSID_ELF_FEMALE;
    }

    public boolean isWizard() {
        return _classId == CLASSID_WIZARD_MALE || _classId == CLASSID_WIZARD_FEMALE;
    }

    public boolean isDarkelf() {
        return _classId == CLASSID_DARK_ELF_MALE || _classId == CLASSID_DARK_ELF_FEMALE;
    }

    public boolean isDragonknight() {
        return _classId == CLASSID_DRAGONKNIGHT_MALE || _classId == CLASSID_DRAGONKNIGHT_FEMALE;
    }

    public boolean isIllusionist() {
        return _classId == CLASSID_ILLUSIONIST_MALE || _classId == CLASSID_ILLUSIONIST_FEMALE;
    }

	public boolean isWarrior() {
		return _classId == CLASSID_WARRIOR_MALE || _classId == CLASSID_WARRIOR_FEMALE;
	}
	
	public boolean isFencer() {
		return _classId == CLASSID_FENCER_MALE || _classId == CLASSID_FENCER_FEMALE;
	}
	
	public boolean isLancer() {
		return _classId == CLASSID_LANCER_MALE || _classId == CLASSID_LANCER_FEMALE;
	}

    public String getAccountName() {
    	return _accountName;
    }
    public void setAccountName(String s) {
    	_accountName = s;
    }

    public int getBaseMaxHp() {
        return _baseMaxHp;
    }
	public void addBaseMaxHp(int i) {
		i += _baseMaxHp;
		switch(getType()){
		case 0:
			if (i >= Config.CHA.PRINCE_MAX_HP) {
				i = (short) Config.CHA.PRINCE_MAX_HP;
			}
			break;
		case 1:
			if (i >= Config.CHA.KNIGHT_MAX_HP) {
				i = (short) Config.CHA.KNIGHT_MAX_HP;
			}
			break;
		case 2:
			if (i >= Config.CHA.ELF_MAX_HP) {
				i = (short) Config.CHA.ELF_MAX_HP;
			}
			break;
		case 3:
			if (i >= Config.CHA.WIZARD_MAX_HP) {
				i = (short) Config.CHA.WIZARD_MAX_HP;
			}
			break;
		case 4:
			if (i >= Config.CHA.DARKELF_MAX_HP) {
				i = (short) Config.CHA.DARKELF_MAX_HP;
			}
			break;
		case 5:
			if (i >= Config.CHA.DRAGONKNIGHT_MAX_HP) {
				i = (short) Config.CHA.DRAGONKNIGHT_MAX_HP;
			}
			break;
		case 6:
			if (i >= Config.CHA.ILLUSIONIST_MAX_HP) {
				i = (short) Config.CHA.ILLUSIONIST_MAX_HP;
			}
			break;
		case 7:
			if (i >= Config.CHA.WARRIOR_MAX_HP) {
				i = (short) Config.CHA.WARRIOR_MAX_HP;
			}
			break;
		case 8:
			if (i >= Config.CHA.FENCER_MAX_HP) {
				i = (short) Config.CHA.FENCER_MAX_HP;
			}
			break;
		case 9:
			if (i >= Config.CHA.LANCER_MAX_HP) {
				i = (short) Config.CHA.LANCER_MAX_HP;
			}
			break;
		default:
			break;
		}
		addMaxHp(i - _baseMaxHp);
		_baseMaxHp = i;
	}
    public int getBaseMaxMp() {
        return _baseMaxMp;
    }
    public void addBaseMaxMp(int i) {
        i += _baseMaxMp;
        i = IntRange.ensure(i, 0, 32767);
        addMaxMp(i - _baseMaxMp);
        _baseMaxMp = i;
    }

    /** 로봇시스템 **/
    private L1RobotAI _robotAi;
    public L1RobotAI getRobotAi() {
    	return _robotAi;
    }
    public void setRobotAi(L1RobotAI ai) {
    	_robotAi = ai;
    }

    public int getOriginalMagicHit() {
    	return _originalMagicHit;
    }

    public int getBaseShortDmg() {
    	return _baseShortDmg;
    }
    public int getBaseLongDmg() {
    	return _baseLongDmg;
    }
    public int getBaseShortHitup() {
    	return _baseShortHitup;
    }
    public int getBaseLongHitup() {
    	return _baseLongHitup;
    }

    public void setBaseMagicHitUp(int i) {
    	_baseMagicHitup = i;
    }
    public int getBaseMagicHitUp() {
    	return _baseMagicHitup;
    }

    public void setBaseMagicCritical(int i) {
    	_baseMagicCritical = i;
    }
    public int getBaseMagicCritical() {
    	return _baseMagicCritical;
    }

    public void setBaseMagicDmg(int i) {
    	_baseMagicDmg = i;
    }
    public int getBaseMagicDmg() {
    	return _baseMagicDmg;
    }

    public void setBaseMagicDecreaseMp(int i) {
    	_baseMagicDecreaseMp = i;
    }
    public int getBaseMagicDecreaseMp() {
    	return _baseMagicDecreaseMp;
    }

    public int getAdvenHp() {
    	return _advenHp;
    }
    public void setAdvenHp(int i) {
    	_advenHp = i;
    }

    public int getAdvenMp() {
    	return _advenMp;
    }
    public void setAdvenMp(int i) {
    	_advenMp = i;
    }
    
    private int _judgementStr;
    public int getJudgementStr() {
    	return _judgementStr;
    }
    public void setJudgementStr(int i) {
    	_judgementStr = i;
    }
    
    private int _primeDmgState;
    public int getPrimeDmgState() {
    	return _primeDmgState;
    }
    public void setPrimeDmgState(int i) {
    	_primeDmgState = i;
    }
    
    private int _primePvPDmgReducState;
    public int getPrimePvPDmgReducState() {
    	return _primePvPDmgReducState;
    }
    public void setPrimePvPDmgReducState(int i) {
    	_primePvPDmgReducState = i;
    }
    
    private int _primeSpState;
    public int getPrimeSpState() {
    	return _primeSpState;
    }
    public void setPrimeSpState(int i) {
    	_primeSpState = i;
    }
    
    private int _primeHpState;
    public int getPrimeHpState() {
    	return _primeHpState;
    }
    public void setPrimeHpState(int i) {
    	_primeHpState = i;
    }
    
    public int _primePrinceState;
    public int getPrimePrinceState() {
    	return _primePrinceState;
    }
    public void setPrimePrinceState(int i) {
    	_primePrinceState = i;
    }
    
    private int _PotentialHp;
    public int getPotentialHp() {
    	return _PotentialHp;
    }
    public void setPotentialHp(int i) {
    	_PotentialHp = i;
    }
    
    private int _potentialMp;
    public int getPotentialMp() {
    	return _potentialMp;
    }
    public void setPotentialMp(int i) {
    	_potentialMp = i;
    }
    
    private int _potentialDg;
    public int getPotentialDg() {
    	return _potentialDg;
    }
    public void setPotentialDg(int i) {
    	_potentialDg = i;
    }
    
    private int _potentialEr;
    public int getPotentialEr() {
    	return _potentialEr;
    }
    public void setPotentialEr(int i) {
    	_potentialEr = i;
    }
    
    private int _potentialMr;
    public int getPotentialMr() {
    	return _potentialMr;
    }
    public void setPotentialMr(int i) {
    	_potentialMr = i;
    }
    
    private int _potentialSp;
    public int getPotentialSp() {
    	return _potentialSp;
    }
    public void setPotentialSp(int i) {
    	_potentialSp = i;
    }
    
    private int _berserkersAC;
    public int getBerserkersAC() {
    	return _berserkersAC;
    }
    public void setBerserkersAC(int i) {
    	_berserkersAC = i;
    }

    public int getElfAttr() {//1:땅, 2:불, 4:물, 8:바람, 3:불+물, 5:불+바람, 9:불+땅, 6:물+바람, 10:물+땅, 12:바람+땅
    	return _elfAttr;
    }
    public void setElfAttr(int i) {
    	_elfAttr = i;
    }

    public long getExpRes() {
    	return _expRes;
    }
    public void setExpRes(long i) {
    	_expRes = i;
    }

    public int getPartnerId() {
    	return _partnerId;
    }
    public void setPartnerId(int i) {
    	_partnerId = i;
    }

    public int getOnlineStatus() {
    	return _onlineStatus;
    }
    public void setOnlineStatus(int i) {
    	_onlineStatus = i;
    }

    public int getHomeTownId() {
    	return _homeTownId;
    }
    public void setHomeTownId(int i) {
    	_homeTownId = i;
    }

    public int getContribution() {
    	return _contribution;
    }
    public void setContribution(int i) {
    	_contribution = i;
    }
    public synchronized void addContribution(int contribution) {
    	_contribution += contribution;
    }

    public int getHellTime() {
    	return _hellTime;
    }
    public void setHellTime(int i) {
    	_hellTime = i;
    }

    public boolean isBanned() {
    	return _banned;
    }
    public void setBanned(boolean flag) {
    	_banned = flag;
    }

    public int getFood() {
    	return _food;
    }
    public void setFood(int i) {
    	_food = i;
    }
    public void addFood(int i) {
        _food += i;
        if (_food >= GameServerSetting.MAX_FOOD_VALUE) {
            _food = GameServerSetting.MAX_FOOD_VALUE;
            if (_survivaltime == null) {
            	_survivaltime = new Timestamp(System.currentTimeMillis());
            }
        } else if (_food < 1) {
        	_food = 1;
        }
    }

    private Timestamp _survivaltime;// 생존의 외침
    public Timestamp getSurvivalTime() {
    	return _survivaltime;
    }
    public void setSurvivalTime(Timestamp ts) {
        if (_food >= GameServerSetting.MAX_FOOD_VALUE) {
        	_survivaltime = ts;
        }
    }

    public L1EquipmentSlot getEquipSlot() {
        return _equipSlot;
    }

    // TODO 캐릭터 로드
    public static L1PcInstance load(String charName) {
        try {
        	return CharacterTable.getInstance().loadCharacter(charName);
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return null;
        }
    }

    public void save() throws Exception {
        if (isGhost() || getFake() || noPlayerCK) {
        	return;
        }
        CharacterTable.getInstance().storeCharacter(this);
		// PSS Time
		if (Config.PSS.PLAY_SUPPORT_TIME_LIMITED) {
			int remainingTime = getSkill().getSkillEffectTimeSec(L1SkillId.STATUS_PSS);
			if (remainingTime > 0)
				getAccount().setPSSTime(remainingTime);
			if (getAccount() != null)
				getAccount().updatePSSTime();
		}
    }

    public void saveInventory() {
		if (noPlayerCK) {
			return;
		}
		ArrayList<L1ItemInstance> updateItemList = null;
        for (L1ItemInstance item : getInventory().getItems()) {
        	if (item == null || item.getRecordingColumns() == 0) {
        		continue;
        	}
    		if (updateItemList == null) {
    			updateItemList = new ArrayList<L1ItemInstance>();
    		}
    		updateItemList.add(item);
        }
        if (updateItemList == null) {
        	return;
        }
        try {
        	// 업데이트 될 아이템을 간추려 batch로 한번에 업데이트한다.
        	CharactersItemStorage.create().updateItemListAll(updateItemList);
		} catch (Exception e) {
			System.out.println(String.format("saveInventory method Exception : CHAR_NAME(%s)", this.getName()));
			e.printStackTrace();
		} finally {
			updateItemList.clear();
	        updateItemList = null;
		}
    }

	public void setRegenState(int state) {
		setStateHp(state);
		setStateMp(state);
	}

	public void setStateHp(int state) {
		if (_curHpPoint < state) {
			return;
		}
		_curHpPoint = state;
	}

	public void setStateMp(int state) {
		if (_curMpPoint < state) {
			return;
		}
		_curMpPoint = state;
	}
	
	public boolean _isWeightNoActionPenalty;
	public int getMaxWeight() {
		L1Ability ability = getAbility();
		int carryBonus = CalcStat.carryBonus(ability.getTotalStr(), ability.getTotalCon()) + getCarryBonus();
		return carryBonus *= Config.RATE.RATE_WEIGHT_LIMIT;
	}

    public boolean isInvisDelay() {
        return invisDelayCounter > 0;
    }
    public void addInvisDelayCounter(int counter) {
        synchronized (_invisTimerMonitor) {
            invisDelayCounter += counter;
        }
    }

    public void beginInvisTimer() {
        final long DELAY_INVIS = 3000L;
        addInvisDelayCounter(1);
        GeneralThreadPool.getInstance().schedule(new L1PcInvisDelay(this), DELAY_INVIS);
    }
    
    // TODO 경험치
    private Object exp_sync = new Object();
    public void addExp(long val) {
    	synchronized (exp_sync) {
    		long exp = getExp() + val;
    		if (exp > ExpTable.MAX_EXP) {
    			exp = ExpTable.MAX_EXP;
            }
    		setExp(exp);
    	}
    }

    public void beginExpMonitor() {
        _expMonitorFuture = GeneralThreadPool.getInstance().scheduleAtFixedRate(new L1PcExpMonitor(this), 0L, 500);
    }
    
    private void leaveLevelOverBeginClan(){
    	try {
            L1Clan clan = getClan();
            L1PcInstance clanMember[] = clan.getOnlineClanMember();
            String player_name = getName();
            String clan_name = getClanName();
            S_ServerMessage leaveMsg = new S_ServerMessage(178, player_name, clan_name);// \f1%0%s %1 혈맹을 탈퇴했습니다.
            for (int i = 0; i < clanMember.length; i++) {
            	clanMember[i].sendPackets(leaveMsg);
            }
            leaveMsg.close();
            leaveMsg = null;
            clearPlayerClanData(clan);
            clan.removeClanMember(player_name);
            broadcastPacketWithMe(new S_PCObject(this), true);
            save();
            saveInventory();
        } catch (Exception e) {
        }
    }

    private void levelUp(int gap) {
        resetLevel();
        _quest.questLevelup(getLevel());

        /** 특정렙 이상 초보혈맹 자동탈퇴 **/
        if (getLevel() >= Config.PLEDGE.BEGINNER_PLEDGE_LEAVE_LEVEL && getClanid() == Config.PLEDGE.BEGINNER_PLEDGE_ID) {
        	leaveLevelOverBeginClan();
        }

        if (getLevel() > 55 && getTitle().contains(Config.SERVER.GAME_SERVER_NAME)) {
            setTitle(StringUtil.EmptyString);
            broadcastPacketWithMe(new S_CharTitle(getId(), StringUtil.EmptyString), true);
        }
  
        /*if (getLevel() == 99 && Config.ALT_REVIVAL_POTION) {
            try {
                L1Item l1item = ItemTable.getInstance().getTemplate(43000);
                if (l1item != null) {
                    getInventory().storeItem(43000, 1);
                    sendPackets(new S_ServerMessage(403, l1item.getName()), true);
                } else {
//AUTO SRM: 					sendPackets(new S_SystemMessage("환생의 물약 입수에 실패했습니다."), true);
					sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(16), true), true);
                }
            } catch (Exception e) {
                _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
//AUTO SRM: 				sendPackets(new S_SystemMessage("환생의 물약 입수에 실패했습니다."), true);
				sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(16), true), true);
            }
        }*/

        for (int i = 0; i < gap; i++) {
        	int randomHp = CalcConStat.levelUpHp(getType(), getBaseMaxHp(), getAbility().getCon());
        	int randomMp = CalcWisStat.levelUpMp(getType(), getBaseMaxMp(), getAbility().getWis());
            addBaseMaxHp(randomHp);
            addBaseMaxMp(randomMp);
        }

        setCurrentHp(getBaseMaxHp());
        setCurrentMp(getBaseMaxMp());
        resetBaseAc();
        resetBaseMr();
        if (getLevel() > getHighLevel() && getReturnStat_exp() == 0) {
        	setHighLevel(getLevel());
        }

        try {
            save();
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        L1Quest quest = getQuest();// 레벨업(퀘스트 선물 외부화) 1~현재레벨까지 반복하면서 검색
        for (int lv = 1; lv <= getLevel(); lv++) {
            CharactersGiftItemTable.Item[] levelItems = CharactersGiftItemTable.getInstance().getItems(lv);
            if (levelItems != null && levelItems.length > 0) {
                int level_quest_step = quest.getStep(lv);
                if (level_quest_step != L1Quest.QUEST_END) {
                	CharactersGiftItemTable.Item levelItem = null;
                    for (int i = 0; i < levelItems.length; i++) {
                    	levelItem = levelItems[i];
                        if (levelItem == null || levelItem.getType() != getType()) {
                        	continue;
                        }
                        getInventory().storeItem(this, levelItem.getItemId(), levelItem.getCount(), levelItem.getEnchant(), levelItem.getBless(), levelItem.getAttrLevel());
                    }
					//sendPackets(new S_SystemMessage(String.format("Level(%d)퀘스트를 완료 하였습니다.", lv)), true);
					sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(89), String.valueOf(lv)), true);
                    getQuest().setEnd(lv);
                }
            }
        }

        if (getLevel() >= 51 && getLevel() - 50 > getBonusStats() && getAbility().getStatsAmount() < 210) {
            int upstat = (getLevel() - 50) - (getBonusStats());
            sendPackets(new S_MessageYN(479, Integer.toString(upstat)), true);
        }
        if (getLevel() >= 15 && getMapId() == 2005) {// 숨겨진 계곡 
            int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_SILVER_KNIGHT);
            _teleport.start(loc[0], loc[1], (short) loc[2], getMoveState().getHeading(), true);
        } else if (getLevel() >= 55 && getMap().isRedKnightZone()) {// 초보존(붉은기사단훈련소)
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_TALKING_ISLAND);
			_teleport.start(loc[0], loc[1], (short) loc[2], getMoveState().getHeading(), true);
		} else if (getLevel() > 69 && (getMapId() >= 1 && getMapId() <= 2)) {// 말하는 섬 던전
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_TALKING_ISLAND);
			_teleport.start(loc[0], loc[1], (short) loc[2], getMoveState().getHeading(), true);
		} else if (getLevel() >= 75 && (getMapId() >= 25 && getMapId() <= 28)) {// 수련 던전
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_TALKING_ISLAND);
			_teleport.start(loc[0], loc[1], (short) loc[2], getMoveState().getHeading(), true);
		}
        
        if (getLevel() >= 80) {// 기사단의 마법인형 80레벨 달성 삭제
        	L1ItemInstance item = getInventory().findItemId(410515);
        	if (item != null) {
        		if (getDoll() != null && getDoll().getItemObjId() == item.getId()) {// 해당 아이템 인형이 사용중이라면 종료
        			getDoll().deleteDoll(true);
        		}
				getInventory().removeItem(item);
        	}
        }
        
        if (getLevel() >= 93) {// 용사의 배지 삭제
        	getInventory().consumeItem(30778);
        }
        
		if (getLevel() == 95) {
			level95Complete();// 95레벨 달성시
		} else if (getLevel() == 100) {
			level100Complete();// 100레벨 달성시
		}
		
        sendPackets(new S_OwnCharStatus(this), true);
    }
    
    private void level95Complete(){
    	if (getQuest().getStep(L1Quest.QUEST_LEVEL95) > 0) {
    		return;
    	}
    	getQuest().setStep(L1Quest.QUEST_LEVEL95, 1);
		L1NpcInstance npc = L1World.getInstance().findNpc(750066);
		if (npc != null) {
			npc.deleteMe();// 기존 동상 제거
		}
//AUTO SRM:         L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, String.format("%s님이 %d레벨을 달성하였습니다. 모두의 축하를 부탁 드립니다.", getName(), getLevel())), true); // CHECKED OK
        L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,String.format("%s " + S_SystemMessage.getRefText(1056) + "%d." + S_SystemMessage.getRefText(1057), getName(), getLevel())), true);
//AUTO SRM:         L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, String.format("%s님의 %d레벨 달성 기념 버프 동상이 1시간 동안 기란 마을에 세워집니다.", getName(), getLevel())), true); // CHECKED OK
        L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,String.format("%s " + S_SystemMessage.getRefText(1060) + "%d " + S_SystemMessage.getRefText(1058), getName(), getLevel())), true);
        npc = L1SpawnUtil.spawn(33417, 32826, (short) 4, 5, 750066, 0, 3600 * 1000);// 버프 동상 스폰
        if (npc == null) {
        	System.out.println("[L1PcInstance] level95Complete npc not fount! npcId: 750066");
        	return;
        }
		// Commemorative statue of
		// 님의 기념 동상
    	npc.setName(String.format(S_SystemMessage.getRefText(1071) + " %s", getName()));
	    npc.setDesc(npc.getName());
	    int spriteId = 0;
	    boolean isMale = _gender == Gender.MALE;
	    switch(getType()){
	    case 0:spriteId = isMale ? 16349 : 16351;break;
	    case 1:spriteId = isMale ? 16353 : 16355;break;
	    case 2:spriteId = isMale ? 16357 : 16359;break;
	    case 3:spriteId = isMale ? 16361 : 16363;break;
	    case 4:spriteId = isMale ? 16365 : 16367;break;
	    case 5:spriteId = isMale ? 16369 : 16371;break;
	    case 6:spriteId = isMale ? 16373 : 16375;break;
	    case 7:spriteId = isMale ? 16377 : 16379;break;
	    case 8:spriteId = isMale ? 18637 : 18639;break;
	    case 9:spriteId = isMale ? 19696 : 19698;break;
	    }
    	npc.setSpriteId(spriteId);
    }
    
    private void level100Complete(){
    	if (getQuest().getStep(L1Quest.QUEST_LEVEL100) > 0) {
    		return;
    	}
    	getQuest().setStep(L1Quest.QUEST_LEVEL100, 1);
    	
    	// 100레벨 전체 메세지 알림
		Calendar cal	= Calendar.getInstance();
		int year		= cal.get(Calendar.YEAR);
		int month		= cal.get(Calendar.MONTH) + 1;
		int day			= cal.get(Calendar.DAY_OF_MONTH);
		int hour		= cal.get(Calendar.HOUR_OF_DAY);
		int minut		= cal.get(Calendar.MINUTE);
		cal				= null;
		//String message = String.format(
		//		"안녕하세요. 리니지입니다. '%s' 용사님께서 %d년 %d월 %d일 %d시 %d분경 100레벨을 달성 하엿습니다. 100레벨 시대의 문을 여신 %s님께 경의를 표하며 아덴 월드 용사님들 모두 함께 축하해 주시기를 부탁 드립니다.", 
		//		getName(), year, month, day, hour, minut, getName());
		String message = String.format(
			"GREAT news: Hero '%s' has achieved level 100 on %d/%d/%d at %d:%d.\n Let's pay our respects to the godd/goodess %s\n and celebrate together with all Aden World heroes!",
			getName(), year, month, day, hour, minut, getName());	
		L1World world = L1World.getInstance();
		world.broadcastPacketToAll(new S_MsgAnnounce(message, ColorUtil.getWhiteRgbBytes()), true);
		
		// 100레벨 보상 지급
		L1ItemInstance reward = this.getInventory().storeItem(32053, 1);
    	if (reward != null) {
    		//sendPackets(new S_ServerMessage(403, reward.getDescKr()), true);
			sendPackets(new S_ServerMessage(403, reward.getDesc()), true);
    	}
		
		if (!Config.ETC.EVENT_LEVEL_100) {
			return;
		}
		
		// 100레벨 기념 동상 생성
		L1NpcInstance npc = world.findNpc(750068);
		if (npc != null) {
			npc.deleteMe();// 기존 동상 제거
		}
		// npc = L1SpawnUtil.spawn1(33431, 32812, (short) 4, 5, 750068, 0, 86400000, 0);// 버프 동상 스폰
		// npc = L1SpawnUtil.spawn1(33428, 32815, (short) 4, 5, 750068, 0, 86400000, 0);// 버프 동상 스폰
		npc = L1SpawnUtil.spawn1(33445, 32812, (short) 4, 5, 750068, 0, 86400000, 0);// 버프 동상 스폰
        if (npc == null) {
        	System.out.println("[L1PcInstance] level100Complete npc not fount! npcId: 750068");
        	return;
        }
    	//npc.setName(String.format("%s %s^100레벨 버프 동상", this.getClassName(), this.getName()));
		// Level 100 Buff Statue
		npc.setName(String.format("%s %s^" + S_SystemMessage.getRefText(1063), this.getClassNameEn(), this.getName()));
	    npc.setDesc(npc.getName());
	    /*int spriteId = 0;
	    boolean isMale = getGender() == Gender.MALE;
	    switch(getType()){
	    case 0:spriteId = isMale ? 16349 : 16351;break;
	    case 1:spriteId = isMale ? 16353 : 16355;break;
	    case 2:spriteId = isMale ? 16357 : 16359;break;
	    case 3:spriteId = isMale ? 16361 : 16363;break;
	    case 4:spriteId = isMale ? 16365 : 16367;break;
	    case 5:spriteId = isMale ? 16369 : 16371;break;
	    case 6:spriteId = isMale ? 16373 : 16375;break;
	    case 7:spriteId = isMale ? 16377 : 16379;break;
	    case 8:spriteId = isMale ? 18637 : 18639;break;
	    case 9:spriteId = isMale ? 19696 : 19698;break;
	    }
	    npc.setTempCharGfx(spriteId);
    	npc.setGfxId(spriteId);*/
    }

    private void levelDown(int gap) {
        resetLevel();
        for (int i = 0; i > gap; i--) {
        	int randomHp = CalcConStat.levelUpHp(getType(), getBaseMaxHp(), getAbility().getCon());
        	int randomMp = CalcWisStat.levelUpMp(getType(), getBaseMaxMp(), getAbility().getWis());
            addBaseMaxHp(-randomHp);
            addBaseMaxMp(-randomMp);
        }
        resetBaseAc();
        resetBaseMr();

		if (!isGm() && Config.SERVER.LEVEL_DOWN_RANGE != 0) {
			if (getHighLevel() - getLevel() == Config.SERVER.LEVEL_DOWN_RANGE - 1) {
				sendPackets(L1SystemMessage.LEVEL_DOWN_WARRING);
				sendPackets(L1SystemMessage.LEVEL_DOWN_WARRING);
			}
			if (!isGm() && getHighLevel() - getLevel() >= Config.SERVER.LEVEL_DOWN_RANGE) {
				sendPackets(L1ServerMessage.sm64);
				sendPackets(S_Disconnect.DISCONNECT);
				//_log.info(String.format("●●●━━━━━ 레벨 다운 허용범위 초과 ━━━━━●●● : %s", getName()));
				_log.info(String.format("━━━━━ Level down allowed range exceeded ━━━━━ : %s", getName()));
			}
		}

        try {
            save();
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        sendPackets(new S_OwnCharStatus(this), true);
    }

    private GameTimeCarrier _gameTimeCarrier;
    public void beginGameTimeCarrier() {
    	if (_gameTimeCarrier == null) {
    		_gameTimeCarrier = new GameTimeCarrier(this);
    	}
    	_gameTimeCarrier.start();
    }
    public void stopGameTimeCarrier(){
    	if (_gameTimeCarrier == null) {
    		return;
    	}
    	_gameTimeCarrier.stop();
    	_gameTimeCarrier = null;
    }

    public boolean isGhost() {
    	return _ghost;
    }
    private void setGhost(boolean flag) {
    	_ghost = flag;
    }

    public boolean isGhostCanTalk() {
    	return _ghostCanTalk;
    }
    private void setGhostCanTalk(boolean flag) {
    	_ghostCanTalk = flag;
    }

    public boolean isReserveGhost() {
    	return _isReserveGhost;
    }
    private void setReserveGhost(boolean flag) {
    	_isReserveGhost = flag;
    }

    public void beginGhost(int locx, int locy, short mapid, boolean canTalk) {
        beginGhost(locx, locy, mapid, canTalk, 0);
    }

    public void beginGhost(int locx, int locy, short mapid, boolean canTalk, int sec) {
        if (isGhost()) {
        	return;
        }
        if (getDoll() != null) {
        	getDoll().deleteDoll(false);
        }
        _ghostSaveLocX	= getX();
        _ghostSaveLocY	= getY();
        _ghostSaveMapId	= getMapId();
        setGhost(true);
        setGhostCanTalk(canTalk);
        setReserveGhost(false);
        _teleport.start(locx, locy, mapid, 5, canTalk);
        if (sec > 0) {
        	_ghostFuture = GeneralThreadPool.getInstance().schedule(new L1PcGhostMonitor(this), sec * 1000L);
        }
    }

    public void makeEndGhost() {
        setGhost(false);
        setGhostCanTalk(true);
        setReserveGhost(true);
        _teleport.start(_ghostSaveLocX, _ghostSaveLocY, (short) _ghostSaveMapId, 5, false);
    }

    public void DeathMatchEndGhost() {
        setReserveGhost(true);
        _teleport.start(32614, 32735, (short) 4, 5, true);
    }

    public void endGhost() {
    	if (_ghostFuture != null) {
    		_ghostFuture.cancel(false);
    		_ghostFuture = null;
        }
        setGhost(false);
        setGhostCanTalk(true);
        setReserveGhost(false);
    }

    public void beginHell(boolean isFirst) {
        if (getMapId() != 666) {
        	_teleport.start(32701, 32777, (short)666, 5, false);
        }
        if (isFirst) {
        	setHellTime(getPKcount() <= 10 ? 180 : 300 * (getPKcount() - 100) + 300);
            sendPackets(new S_BlueMessage(552, String.valueOf(getPKcount()), String.valueOf(getHellTime() / 60)), true);
        } else {
            sendPackets(new S_BlueMessage(637, String.valueOf(getHellTime())), true);
        }
        if (_hellFuture == null) {
        	_hellFuture = GeneralThreadPool.getInstance().scheduleAtFixedRate(new L1PcHellMonitor(this), 0L, 1000L);
        }
    }

    public void endHell() {
        if (_hellFuture != null) {
            _hellFuture.cancel(false);
            _hellFuture = null;
        }
        int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_ORCISH_FOREST);
        _teleport.start(loc[0], loc[1], (short) loc[2], 5, true);
        try {
            save();
        } catch (Exception ignore) {
        }
    }

    @Override
    public void setPoisonEffect(int effectId) {
        sendPackets(new S_Poison(getId(), effectId), true);
        if (!isGmInvis() && !isGhost() && !isInvisble()) {
        	broadcastPacket(new S_Poison(getId(), effectId), true);
        }
    }

    @Override
    public void healHp(int pt) {
        super.healHp(pt);
        sendPackets(new S_HPUpdate(this), true);
    }

    @Override
    public int getKarma() {
        return _karma.get();
    }
    @Override
    public void setKarma(int i) {
        _karma.set(i);
    }
    public void addKarma(int i) {
        synchronized (_karma) {
            _karma.add(i);
        }
    }
    public int getKarmaLevel() {
    	return _karma.getLevel();
    }
    public int getKarmaPercent() {
    	return _karma.getPercent();
    }

    public Timestamp getLastPk() {
    	return _lastPk;
    }
    public void setLastPk(Timestamp time) {
    	_lastPk = time;
    }
    public void setLastPk() {
        _lastPk = new Timestamp(System.currentTimeMillis());
    }

    public boolean isWanted() {
        if (_lastPk == null) {
        	return false;
        }
        if (System.currentTimeMillis() - _lastPk.getTime() > 86400000) {
            setLastPk(null);
            return false;
        }
        return true;
    }

    public Timestamp getDeleteTime() {
    	return _deleteTime;
    }
    public void setDeleteTime(Timestamp time) {
    	_deleteTime = time;
    }

    public Timestamp getLastLoginTime() {
    	return _lastLoginTime;
    }
    public void setLastLoginTime(Timestamp time) {
    	_lastLoginTime = time;
    }
    
    public Timestamp getLastLogoutTime() {
    	return _lastLogoutTime;
    }
    public void setLastLogoutTime(Timestamp time) {
    	_lastLogoutTime = time;
    }
    
    public boolean _isDragonBless;// 드래곤의 축복
    public boolean _isDragonFavor;// 드래곤의 가호
    public boolean _isDragonFavorPCCafe;// 드래곤의 가호(PC)
    
    /** 아인하사드의 가호 **/
    public Timestamp _einhasdgrace;
	public Timestamp getEinhasadGraceTime() {
		return _einhasdgrace;
	}
	public void setEinhasadGraceTime(Timestamp ts) {
		_einhasdgrace = ts;
	}

    private int _emerald;
    public int getEmerald() {
    	return _emerald;
    }
    public void calEmerald(int i) {
        int calc = _emerald + i;
        _emerald = calc >= 1500 * Config.EIN.REST_EXP_DEFAULT_RATION ? 1500 * Config.EIN.REST_EXP_DEFAULT_RATION : calc;
    }
    public void setEmerald(int i) {
    	_emerald = i;
    }
	 
    private int _rest_exp_reduce_efficiency;
	private int _rest_exp_reduce_efficiency_level_bonus;
	public int get_rest_exp_reduce_efficiency() {
	    int levelUpBuffBonus	= skillStatus.hasSkillEffect(L1SkillId.LEVELUP_BONUS_BUFF) ? 30 : 0;
	    int growBuff			= _teleport.isGrowBuff() ? 20 : 0;
	    return _rest_exp_reduce_efficiency + _rest_exp_reduce_efficiency_level_bonus + getAbility().getBlessEfficiency() + levelUpBuffBonus + growBuff;
	}
	public void set_rest_exp_reduce_efficiency(int i) {
		_rest_exp_reduce_efficiency = i;
	}
    public void add_rest_exp_reduce_efficiency(int i) {
    	_rest_exp_reduce_efficiency += i;
    	if (_account != null) {
    		sendPackets(new S_RestExpInfoNoti(this), true);
    	}
    }
    
    /**
     * 아인하사드 추가(나뭇잎 이팩트 출력)
     * @param value
     */
    public void einGetExcute(int value){
		if (value <= 0) {
			return;
		}
		_account.getEinhasad().addPoint(value * Config.EIN.REST_EXP_DEFAULT_RATION, this);
		sendPackets(new S_RestExpInfoNoti(this), true);
		sendPackets(new S_ExpBoostingInfo(this), true);
		sendPackets(new S_RestGaugeChargeNoti(value), true);
	}

    public int getCarryBonus() {
    	return _carryBonus;
    }
    public void addCarryBonus(int i) {
    	_carryBonus += i;
        sendPackets(new S_StatusCarryWeightInfoNoti(this), true);
        sendPackets(new S_PacketBox(S_PacketBox.WEIGHT, getInventory().getWeightPercent()), true);
    }

    public int getHasteItemEquipped() {
    	return _hasteItemEquipped;
    }
    public void addHasteItemEquipped(int val) {
    	_hasteItemEquipped += val;
    }
    public int getDrunkenItemEquipped() {
    	return _drunkenItemEquipped;
    }
    public void addDrunkenItemEquipped(int val) {
    	_drunkenItemEquipped += val;
    }
    public int getFourthItemEquipped() {
    	return _fourthItemEquipped;
    }
    public void addFourthItemEquipped(int val) {
    	_fourthItemEquipped += val;
    }

    public void resetOriginalMagicHit() {
        int originalInt = this.getAbility().getTotalInt();
        if (isCrown()) {
            if (originalInt == 12 || originalInt == 13) {
            	_originalMagicHit = 1;
            } else if (originalInt >= 14) {
            	_originalMagicHit = 2;
            } else {
            	_originalMagicHit = 0;
            }
        } else if (isKnight()) {
            if (originalInt == 10 || originalInt == 11) {
            	_originalMagicHit = 1;
            } else if (originalInt == 12) {
            	_originalMagicHit = 2;
            } else {
            	_originalMagicHit = 0;
            }
        } else if (isElf()) {
            if (originalInt == 13 || originalInt == 14) {
            	_originalMagicHit = 1;
            } else if (originalInt >= 15) {
            	_originalMagicHit = 2;
            } else {
            	_originalMagicHit = 0;
            }
        } else if (isDarkelf()) {
            if (originalInt == 12 || originalInt == 13) {
            	_originalMagicHit = 1;
            } else if (originalInt >= 14) {
            	_originalMagicHit = 2;
            } else {
            	_originalMagicHit = 0;
            }
        } else if (isWizard()) {
            if (originalInt >= 14) {
            	_originalMagicHit = 1;
            } else {
            	_originalMagicHit = 0;
            }
        } else if (isDragonknight()) {
            if (originalInt == 12 || originalInt == 13) {
            	_originalMagicHit = 2;
            } else if (originalInt == 14 || originalInt == 15) {
            	_originalMagicHit = 3;
            } else if (originalInt >= 16) {
            	_originalMagicHit = 4;
            } else {
            	_originalMagicHit = 0;
            }
        } else if (isIllusionist()) {
            if (originalInt >= 13) {
            	_originalMagicHit = 1;
            } else {
            	_originalMagicHit = 0;
            }
        } else if (isWarrior()) {
            if (originalInt == 12 || originalInt == 13) {
            	_originalMagicHit = 1;
            } else if (originalInt >= 14) {
            	_originalMagicHit = 2;
            } else {
            	_originalMagicHit = 0;
            }
        } else if (isFencer()) {
            if (originalInt == 10 || originalInt == 11) {
            	_originalMagicHit = 1;
            } else if (originalInt >= 12) {
            	_originalMagicHit = 2;
            } else {
            	_originalMagicHit = 0;
            }
        } else if (isLancer()) {
            if (originalInt == 9 || originalInt == 10) {
            	_originalMagicHit = 1;
            } else if (originalInt >= 11) {
            	_originalMagicHit = 2;
            } else {
            	_originalMagicHit = 0;
            }
        }
    }

    public void resetLevel() {
        setLevel(ExpTable.getLevelByExp(getExp()));
		updateLevel();
    }
    
    private static final int LEVEL_REGEN_HP_TABLE[] = new int[] { 
    	30, 25, 20, 16, 14, 12, 11, 10, 9, 3, 2 
    };
	public void updateLevel() {
		try {
			int level		= getLevel();
			int regenLvl	= Math.min(10, level);
			if (30 <= level && (isKnight() || isWarrior() || isFencer() || isLancer())) {
				regenLvl	= 11;
			}
			synchronized (this) {
				// HP회복량
				_regenHpMax = LEVEL_REGEN_HP_TABLE[regenLvl - 1] << 2;
				
				// 축복 소모 효율
			    if (level >= 80 && level <= 85) {
			    	_rest_exp_reduce_efficiency_level_bonus = 5 + (level - 80);
			    } else if (level >= 86 && level <= 90) {
			    	_rest_exp_reduce_efficiency_level_bonus = 10 + ((level - 85) << 1);
			    } else if (level >= 91 && level <= 95) {
			    	_rest_exp_reduce_efficiency_level_bonus = 20 + ((level - 90) * 3);
			    } else {
			    	_rest_exp_reduce_efficiency_level_bonus = 0;
			    }
			}
		} catch (Exception e) {
		}
	}

    public void refresh() {
        CheckChangeExp();
        resetLevel();
        resetBaseMr();
        resetBaseAc();
    }

    public void checkChatInterval() {
        long nowChatTimeInMillis = System.currentTimeMillis();
        if (_chatCount == 0) {
            _chatCount++;
            _oldChatTimeInMillis = nowChatTimeInMillis;
            return;
        }

        long chatInterval = nowChatTimeInMillis - _oldChatTimeInMillis;
        if (chatInterval > 2000) {
            _chatCount = 0;
            _oldChatTimeInMillis = 0;
        } else {
            if (_chatCount >= 3) {
            	skillStatus.setSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED, 120000);
                sendPackets(new S_SkillIconGFX(S_SkillIconGFX.CHAT_ICON, 120), true);
                sendPackets(L1ServerMessage.sm153);
                _chatCount = 0;
                _oldChatTimeInMillis = 0;
            }
            _chatCount++;
        }
    }

    // TODO 경험치 변화
    public void CheckChangeExp() {
        int level = ExpTable.getLevelByExp(getExp());
        int char_level = CharacterTable.getInstance().PcLevelInDB(getId());
        if (char_level == 0) {
        	return;
        }
        int gap = level - char_level;
        if (gap == 0) {
            sendPackets(new S_OwnCharStatus(this), true);
            int percent = ExpTable.getExpPercentage(char_level, getExp());
            if ((char_level >= 60 && char_level <= 64 && percent >= 10) || (char_level >= 65 && percent >= 5)) {
            	setlevelUpBonusBuff(false);
            }
            return;
        }

        // 레벨이 변화했을 경우
        if (gap > 0) {
            levelUp(gap);
            setlevelUpBonusBuff(true);
        } else if (gap < 0) {
            levelDown(gap);
            setlevelUpBonusBuff(false);
        }
    }
    
    private void setlevelUpBonusBuff(boolean on){
    	if (on && getLevel() >= 60) {
    		if (!skillStatus.hasSkillEffect(L1SkillId.LEVELUP_BONUS_BUFF)) {
    			add_exp_boosting_ratio(123);
    		}
    		skillStatus.setSkillEffect(L1SkillId.LEVELUP_BONUS_BUFF, 10800000);
            sendPackets(new S_PacketBox(10800, true, true), true);
    	} else if (!on && skillStatus.hasSkillEffect(L1SkillId.LEVELUP_BONUS_BUFF)) {
    		skillStatus.removeSkillEffect(L1SkillId.LEVELUP_BONUS_BUFF);
    	}
    }

    public void LoadCheckStatus() {
        int totalS = getAbility().getStatsAmount();
        int bonusS = getHighLevel() - 50;
        if (bonusS < 0) {
        	bonusS = 0;
        }
        int calst = totalS - (bonusS + getElixirStats() + 75);
        if (calst > 0 && !isGm()) {
        	returnStat();
        }
    }

    public void CheckStatus() {
        int totalS = ability.getStatsAmount();
        int bonusS = getLevel() - 50;
        if (bonusS < 0) {
        	bonusS = 0;
        }
        int calst = totalS - (bonusS + getElixirStats() + 75);
        if (calst > 0 && !isGm()) {
        	returnStat();
        }
    }
    
    public void returnStat(){
    	L1SkillUse l1skilluse = new L1SkillUse(true);
        l1skilluse.handleCommands(this, L1SkillId.CANCELLATION, getId(), getX(), getY(), 0, L1SkillUseType.LOGIN);
        l1skilluse = null;
        for (L1ItemInstance item : getInventory().getItems()) {
        	if (item != null && item.isEquipped()) {
        		getInventory().setEquipped(item, false, false, false);
        	}
        }
        sendPackets(new S_CharVisualUpdate(this), true);

        setReturnStat_exp(getExp());
        setReturnStatus(1);
        sendPackets(new S_SPMR(this), true);
        sendPackets(new S_OwnCharAttrDef(this), true);
        sendPackets(new S_OwnCharStatus2(this), true);
        sendPackets(new S_ReturnedStatStart(this), true);
        try {
            save();
        } catch (Exception e) {
        	//System.out.println("스탯 초기화 오류 발생! 케릭터: " + getName());
			System.out.println("Stat initialization error occurred! Character: " + getName());
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    public void cancelAbsoluteBarrier() { // 앱솔루트 배리어의 해제
        if (skillStatus.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER)) {
        	skillStatus.killSkillEffectTimer(L1SkillId.ABSOLUTE_BARRIER);
        	startMpRegenerationByItem64Second();
            sendPackets(new S_SpellBuffNoti(this, L1SkillId.ABSOLUTE_BARRIER, false, -1), true);
        }
    }

    public int getPKcount() {
    	return _PKcount;
    }
    public void setPKcount(int i) {
    	_PKcount = i;
    }

    public int getClanid() {
    	return _clanid;
    }
    public void setClanid(int i) {
    	_clanid = i;
    }

    public String getClanName() {
    	return clanname;
    }
    public void setClanName(String s) {
    	clanname = s;
    }

    public L1Clan getClan() {
    	return _clan;
    }
    public void setClan(L1Clan clan) {
    	_clan = clan;
    }

    public eBloodPledgeRankType getBloodPledgeRank() {
    	return _bloodPledgeRank;
    }
    public void setBloodPledgeRank(eBloodPledgeRankType val) {
    	_bloodPledgeRank = val;
    }
    
    public int getPledgeJoinDate() {
    	return _pledgeJoinDate;
    }
    public void setPledgeJoinDate(int val) {
    	_pledgeJoinDate = val;
    }
    
    public int getPledgeRankDate() {
    	return _pledgeRankDate;
    }
    public void setPledgeRankDate(int val) {
    	_pledgeRankDate = val;
    }
    
    private int _clanContribution;
    public int getClanContribution() {
    	return _clanContribution;
    }
    public void setClanContribution(int i) {
    	_clanContribution = i;
    }
    public void addClanContribution(int i){
    	_clanContribution += i;
    	getClan().addContribution(i);
    }
    
    private int _clanWeekContribution;
    public int getClanWeekContribution() {
    	return _clanWeekContribution;
    }
    public void setClanWeekContribution(int i) {
    	_clanWeekContribution = i;
    }
    public void addClanWeekContribution(int i){
    	_clanWeekContribution += i;
    	addClanContribution(i);
    }

    public Gender getGender() {
    	return _gender;
    }
    public void setGender(Gender val) {
    	_gender = val;
    }

    public boolean isGm() {
    	return _gm;
    }
    public void setGm(boolean flag) {
    	_gm = flag;
    }

    public boolean isMonitor() {
    	return _monitor;
    }
    public void setMonitor(boolean flag) {
    	_monitor = flag;
    }
    
    private int _scalesDragon;
    public int getScalesDragon() {
    	return _scalesDragon;
    }
    public void addScalesDragon(int i) {
    	_scalesDragon += i;
    }

    private void setGresValid(boolean valid) {
    	_gresValid = valid;
    }
    public boolean isGresValid() {
    	return _gresValid;
    }

    public long getFishingTime() {
    	return _fishingTime;
    }
    public void setFishingTime(long i) {
    	_fishingTime = i;
    }

    public boolean isFishing() {
    	return _isFishing;
    }
    public void setFishing(boolean flag) {
    	_isFishing = flag;
    }

    public int getCookingId() {
    	return _cookingId;
    }
    public void setCookingId(int i) {
    	_cookingId = i;
    }
    
    public int getSoupId() {
    	return _soupId;
    }
    public void setSoupId(int i) {
    	_soupId = i;
    }
    
    public AcceleratorChecker getAcceleratorChecker() {
    	return _acceleratorChecker;
    }

    /** 패키지상점 **/
    private int cashStep;
    public int getCashStep() {
    	return cashStep;
    }
    public void setCashStep(int cashStep) {
    	this.cashStep = cashStep;
    }

    private int skillTime, skillTime2;
    private long _quiztime, _quiztime2, _quiztime3;
    private int currentSkillCount, currentSkillCount2;

    public long getQuizTime() {
    	return _quiztime;
    }
    public void setQuizTime(long value) {
    	_quiztime = value;
    }
    
    public long getQuizTime2() {
    	return _quiztime2;
    }
    public void setQuizTime2(long value) {
    	_quiztime2 = value;
    }

    public long getQuizTime3() {
    	return _quiztime3;
    }
    public void setQuizTime3(long value) {
    	_quiztime3 = value;
    }
    
    private long teleportTime;
	public long getTeleportTime() {
		return teleportTime;
	}
	public void setTeleportTime(long teleportTime) {
		this.teleportTime = teleportTime;
	}

	private int teleportTime2;
    public int getTeleportTime2() {
    	return teleportTime2;
    }
    public void setTeleportTime2(int Time) {
    	teleportTime2 = Time;
    }

    public int getSkillTime2() {
    	return skillTime2;
    }
    public void setSkillTime2(int Time) {
    	skillTime2 = Time;
    }

    public int getSkillTime() {
    	return skillTime;
    }
    public void setSkillTime(int Time) {
    	skillTime = Time;
    }

    public int getCurrentSkillCount() {
    	return currentSkillCount;
    }
    public void setCurrentSkillCount(int Count) {
    	currentSkillCount = Count;
    }

    public int getCurrentSkillCount2() {
    	return currentSkillCount2;
    }
    public void setCurrentSkillCount2(int Count) {
    	currentSkillCount2 = Count;
    }

    public int getTempCharGfxAtDead() {
    	return _tempCharGfxAtDead;
    }
    public void setTempCharGfxAtDead(int i) {
    	_tempCharGfxAtDead = i;
    }

    public int getFightId() {
    	return _fightId;
    }
    public void setFightId(int i) {
    	_fightId = i;
    }

    public void setDeathMatch(boolean i) {
    	_isDeathMatch = i;
    }
    public boolean isDeathMatch() {
    	return _isDeathMatch;
    }

    /** 코마버프 시작 **/
    private int _deathmatch;
    public int getDeathMatchPiece() {
    	return _deathmatch;
    }
    public void setDeathMatchPiece(int i) {
    	_deathmatch = i;
    }

    private int _petrace;
    public int getPetRacePiece() {
    	return _petrace;
    }
    public void setPetRacePiece(int i) {
    	_petrace = i;
    }

    private int _ultimatebattle;
    public int getUltimateBattlePiece() {
    	return _ultimatebattle;
    }
    public void setUltimateBattlePiece(int i) {
    	_ultimatebattle = i;
    }

    private int _petmatch;
    public int getPetMatchPiece() {
    	return _petmatch;
    }
    public void setPetMatchPiece(int i) {
    	_petmatch = i;
    }

    private int _ghosthouse;
    public int getGhostHousePiece() {
    	return _ghosthouse;
    }
    public void setGhostHousePiece(int i) {
    	_ghosthouse = i;
    }
    /** 코마버프 끝 **/
    
    // 미니게임
    private L1GamblingObject _gambling;
    public L1GamblingObject getGambling() {
    	if (_gambling == null) {
    		_gambling = new L1GamblingObject();
    	}
    	return _gambling;
    }

    private int monsterKill;
    public int getMonsterKill() {
    	return monsterKill;
    }
    public void setMonsterKill(int i) {
    	monsterKill = i;
        sendPackets(new S_OwnCharStatus(this), true);
    }
    public void addMonsterKill(int i) {
    	monsterKill += i;
        sendPackets(new S_OwnCharStatus(this), true);
    }

    // 클랜 매칭 신청,요청 목록 유저가 사용할땐 배열에 혈맹의 이름을 넣고 군주가 사용할땐 배열에 신청자의 이름을 넣는다.
    private ArrayList<String> _cmalist = new ArrayList<String>();
    public void addCMAList(String name) {
        if (_cmalist.contains(name)) {
        	return;
        }
        _cmalist.add(name);
    }
    public void removeCMAList(String name) {
        if (!_cmalist.contains(name)) {
        	return;
        }
        _cmalist.remove(name);
    }
    public ArrayList<String> getCMAList() {
    	return _cmalist;
    }

    private String _clanMemberNotes;
    public String getClanMemberNotes() {
    	return _clanMemberNotes;
    }
    public void setClanMemberNotes(String s) {
    	_clanMemberNotes = s;
    }

    /**
     * 혈맹 탈퇴 OR 추방에 의한 데이터 초기화
     * @param clan
     * @throws Exception
     */
	public void clearPlayerClanData(L1Clan clan) throws Exception {
		try {
			if (this != null && getNetConnection() != null) {
				leave_pleage_object();
			}
			setClan(null);
			setClanid(0);
			setClanName(StringUtil.EmptyString);
			setTitle(StringUtil.EmptyString);
			setClanMemberNotes(StringUtil.EmptyString);
			setClanContribution(0);// 공헌도 초기화
			setClanWeekContribution(0);// 주간 공헌도 초기화
			setBloodPledgeRank(null);
			setPledgeJoinDate(0);
			setPledgeRankDate(0);
			L1BuffUtil.pledge_contribution_buff_all_dispose_from_user(skillStatus);
			if (this != null && getNetConnection() != null) {
				sendPackets(new S_BloodPledgeUserInfo(StringUtil.EmptyString, getBloodPledgeRank(), false), true);
				broadcastPacketWithMe(new S_CharTitle(getId(), StringUtil.EmptyString), true);
				sendPackets(new S_Pledge(this, 0, getBloodPledgeRank()), true);
				broadcastPacketWithMe(new S_BloodPledgeEmblem(getId(), 0), true);
				sendPackets(S_PledgeWatch.ATTENTION_EMPTY);
			}
			save();
		} catch (Exception e) {
			//System.out.println("혈맹탈퇴에러남 : " + getName());
			System.out.println("Error leaving clan: " + getName());
		}
	}
	
	/**
	 * 혈맹 탈퇴 시 화면내 오브젝트 처리
	 */
	public void leave_pleage_object() {
		boolean is_invis			= isInvisble();
		boolean is_party			= isInParty();
		boolean is_floatingEye		= isFloatingEye();
		L1DollInstance doll			= getDoll();
		S_RemoveObject remove		= null;
		S_RemoveObject remove_doll	= null;
		for (L1PcInstance member : L1World.getInstance().getVisibleClanPlayer(this, -1)) {
			if (this == member) {
				continue;
			}
			// 같은 파티원이면 오브젝트 처리를 하지 않는다.
			if (is_party && getParty().isMember(member)) {
				continue;
			}
			
			// 탈퇴 캐릭터가 인비지
			if (is_invis) {
				// 오브젝트를 인식할 수 없는 상태
				if (!member.isFloatingEye()) {
					if (remove == null) {
						remove = new S_RemoveObject(this);
					}
					member.sendPackets(remove);
				}
				// 인형 오브젝트를 인식 할 수 없는 상태
				if (doll != null) {
					if (remove_doll == null) {
						remove_doll = new S_RemoveObject(doll);
					}
					member.sendPackets(remove_doll);
				}
			}
			
			// 남은 혈맹원이 인비지 상태
			if (member.isInvisble()) {
				// 오브젝트를 인식할 수 없는 상태
				if (!is_floatingEye) {
					sendPackets(new S_RemoveObject(member), true);
				}
				// 인형 오브젝트를 인식 할 수 없는 상태
				if (member.getDoll() != null) {
					sendPackets(new S_RemoveObject(member.getDoll()), true);
				}
			}
		}
		if (remove != null) {
			remove.clear();
			remove = null;
		}
		if (remove_doll != null) {
			remove_doll.clear();
			remove_doll = null;
		}
	}
	
	private String sellChat, buyChat;
	public String getSellChat(){
		return sellChat;
	}
	public void setSellchat(String sell){
		sellChat = sell;
	}
	
	public String getBuyChat(){
		return buyChat;
	}
	public void setBuychat(String buy){
		buyChat = buy;
	}
	
	public int sellChatTime, buyChatTime;
	
	private int desperadoAttackerLevel;
	public int getDesperadoAttackerLevel() {
		return desperadoAttackerLevel;
	}
	public void setDesperadoAttackerLevel(int AttackerLevel) {
		desperadoAttackerLevel = AttackerLevel;
	}
	
	boolean _fake;
	public void setFake() {
		_fake = true;
	}
	public boolean getFake() {
		return _fake;
	}
	
	private int _ringSlotLevel, _earringSlotLevel, _badgeSlotLevel, _shoulderSlotLevel;
	
	public int getRingSlotLevel() {
		return _ringSlotLevel;
	}
	public void setRingSlotLevel(int i) {
		_ringSlotLevel = i;
	}
	
	public int getEarringSlotLevel() {
		return _earringSlotLevel;
	}
	public void setEarringSlotLevel(int i) {
		_earringSlotLevel = i;
	}
	
	public int getBadgeSlotLevel() {
		return _badgeSlotLevel;
	}
	public void setBadgeSlotLevel(int i) {
		_badgeSlotLevel = i;
	}
	
	public int getShoulderSlotLevel() {
		return _shoulderSlotLevel;
	}
	public void setShoulderSlotLevel(int i) {
		_shoulderSlotLevel = i;
	}
	
	private int _focusWaveLimit;
	public int getFocusWaveLimit() {
		return _focusWaveLimit;
	}
	public void setFocusWaveLimit(int i) {
		_focusWaveLimit = i;
	}
	public void addFocusWaveLimit(int i) {
		_focusWaveLimit += i;
	}
	
	/** LFC instance space 공간중 어떤 상태에 있는지 여부를 나타냄 **/
	private InstStatus _instStatus = InstStatus.INST_USERSTATUS_NONE;
	public InstStatus getInstStatus(){
		return _instStatus;
	}
	public void setInstStatus(InstStatus status){
		_instStatus = status;
	}

	/** lfc 중 받은 데미지를 누적한다. **/
	private int _dmgLfc;
	public int getDamageFromLfc(){
		return _dmgLfc;
	}
	public void addDamageFromLfc(int i){
		_dmgLfc =+ i;
	}
	public void setDamageFromLfc(int i){
		_dmgLfc = i;
	}
	
    private int _graceavatarstate; //아바타수치
	public void setGraceAvatarState(int state) {
		_graceavatarstate = state;
	}
	public int getGraceAvatarState() {
		return _graceavatarstate;
	}
	
	private int _impactstate; //임팩트수치
	public void setImpactState(int state) {
		_impactstate = state;
	}
	public int getImpactState() {
		return _impactstate;
	}
	
	private Timestamp EMEtime, EMEtime2, PUPLEtime, TOPAZtime;
	
	public Timestamp getEMETime() {
		return EMEtime;
	}
	public void setEMETime(Timestamp t) {
		EMEtime = t;
	}
	
	public Timestamp getEMETime2() {
		return EMEtime2;
	}
	public void setEMETime2(Timestamp t) {
		EMEtime2 = t;
	}

	public Timestamp getPUPLETime() {
		return PUPLEtime;
	}
	public void setPUPLETime(Timestamp t) {
		PUPLEtime = t;
	}

	public Timestamp getTOPAZTime() {
		return TOPAZtime;
	}
	public void setTOPAZTime(Timestamp t) {
		TOPAZtime = t;
	}
	
	private HashMap<Integer, S_Effect> effects = new HashMap<Integer, S_Effect>();
	public S_Effect getEffect(int sprite_id) {
		if (effects.containsKey(sprite_id)) {
			return effects.get(sprite_id);
		}
		S_Effect effect = new S_Effect(getId(), sprite_id);
		effects.put(sprite_id, effect);
		return effect;
	}
	
	public void send_effect(int sprite_id) {
		if (sprite_id <= 0) {
			return;
		}
		broadcastPacketWithMe(getEffect(sprite_id));
	}
	
	public void send_effect(int object_id, int sprite_id) {
		if (sprite_id <= 0) {
			return;
		}
		broadcastPacketWithMe(new S_Effect(object_id, sprite_id), true);
	}
	
	public void send_effect_self(int sprite_id){
		if (sprite_id <= 0) {
			return;
		}
		sendPackets(getEffect(sprite_id));
	}
	
	public void send_effect_self(int object_id, int sprite_id){
		if (sprite_id <= 0) {
			return;
		}
		sendPackets(new S_Effect(object_id, sprite_id), true);
	}
	
	public L1PetInstance getPet() {
        for (L1NpcInstance npc : getPetList().values()) {
            if (npc instanceof L1PetInstance) {
            	return (L1PetInstance)npc;
            }
        }
        return null;
    }
	
	private int einPoint;// 아인하사드 포인트
	public int getEinPoint() {
		return einPoint;
	}
	public void setEinPoint(int val) {
		einPoint = EINHASAD_POINT_RANGE.ensure(val);
	}
	public void addEinPoint(int val) {
		einPoint = EINHASAD_POINT_RANGE.ensure(einPoint + val);
		sendPackets(new S_EinhasadPointPointNoti(einPoint), true);
	}
	
	private int ein_cur_enchant_level;
	public int getEinCurEnchantLevel() {
		return ein_cur_enchant_level;
	}
	public int setEinCurEnchantLevel(int val) {
		return ein_cur_enchant_level = val;
	}
	public int addEinCurEnchantLevel(int val) {
		return ein_cur_enchant_level += val;
	}
	
	private int ein_total_stat;
	public int getEinTotalStat() {
		return ein_total_stat;
	}
	public int setEinTotalStat(int val) {
		return ein_total_stat = val;
	}
	public int addEinTotalStat(int val) {
		return ein_total_stat += val;
	}
	
	private int ein_bonus_card_open_value;
	public int getEinBonusCardOpenValue() {
		return ein_bonus_card_open_value;
	}
	public void setEinBonusCardOpenValue(int val) {
		ein_bonus_card_open_value = val;
	}
    
    private int _coloTeam;
    public int getColoTeam() {
    	return _coloTeam;
    }
    public void setColoTeam(int i) {
    	_coloTeam = i;
    }
    
    // 신고 딜레이
    private ReportDeley _reportdeley;
    public void startReportDeley() {
    	if (_reportdeley == null) {
    		_reportdeley = new ReportDeley(this);
    	}
        _regenTimer.schedule(_reportdeley, 600000L); // 딜레이 시간 10분
    }
    // 신고 추가
    private boolean _isReport = true;
    public void setReport(boolean _isreport) {
    	_isReport = _isreport;
    }
    public boolean isReport() {
    	return _isReport;
    }
    
    private long _lastShellUseTime;
    public long getlastShellUseTime() {
    	return _lastShellUseTime;
    }
    public void updatelastShellUseTime() {
    	_lastShellUseTime = System.currentTimeMillis();
    }
    
	/** 복수시스템 추적 타겟 **/
    private String _revenge_target;   
    public String getRevengeTarget() {
    	return _revenge_target;
    }
    public void setRevengeTarget(String i) {
    	_revenge_target = i;
    }
    
    private HuntingQuestUser _huntingQuest;
    public HuntingQuestUser getHuntingQuest(){
    	return _huntingQuest;
    }
    public void setHuntingQuest(HuntingQuestUser collect){
    	_huntingQuest = collect;
    }
    
    private L1FavorBookInventory _favorBook;
    public L1FavorBookInventory getFavorBook(){
    	return _favorBook;
    }
    public void createFavorBook(){
    	_favorBook = new L1FavorBookInventory(this);
    }
    
    private L1TimeCollectionHandler _timeCollection;
    public L1TimeCollectionHandler getTimeCollection(){
    	return _timeCollection;
    }
    public void createTimeCollection(){
    	_timeCollection = new L1TimeCollectionHandler(this);
    }
    
    private EinhasadFaithHandler _einhasadFaith;
    public EinhasadFaithHandler getEinhasadFaith() {
    	return _einhasadFaith;
    }
    public void createEinhasadFaith() {
    	_einhasadFaith = new EinhasadFaithHandler(this);
    }
	
	public boolean isClassPoly(int spriteId){
		return L1CharacterInfo.CLASS_POLY_LIST.contains(spriteId);
	}
	
	public boolean isRankingPoly(int spriteId){
		return L1CharacterInfo.RANKING_POLY_LIST.contains(spriteId);
	}
	
	public int spearPolyRange(int spriteId){
		boolean isSpear = spearPoly(spriteId);
		if (_isLancerForm && (isSpear || PolyTable.SPEAR_LANCER_POLY_ADD_LIST.contains(spriteId))) {
			return _statusLancerFormRange;
		}
		return isSpear ? 2 : 1;
	}
	
	public boolean spearPoly(int spriteId){// 창변신
		return (isClassPoly(spriteId) || isRankingPoly(spriteId) || PolyTable.isPolyWeapon(WeaponeType.SPEAR, spriteId));
	}
	
    private int _ubScore;
    public int getUbScore() {
    	return _ubScore;
    }
    public void setUbScore(int i) {
    	_ubScore = i;
    }
    
    private boolean _isDragonDungenBoss;
	public void setDragonDungenBoss(boolean flag) {
		_isDragonDungenBoss = flag;
	}
    public boolean isDragonDungenBoss() {
    	return _isDragonDungenBoss;
    }
    
    private boolean _isUltimateBoss;
	public void setUltimateBoss(boolean flag) {
		_isUltimateBoss = flag;
	}
    public boolean isUltimateBoss() {
    	return _isUltimateBoss;
    }
    
    private ShopLimitUser shopLimit;
	public ShopLimitUser getShopLimit(){
		return shopLimit;
	}
	public void setShopLimit(ShopLimitUser limit){
		shopLimit = limit;
	}
    
	public void denals_disconnect(String message) {
		sendPackets(S_Disconnect.DISCONNECT);
		System.out.println(message);
		GeneralThreadPool.getInstance().schedule(new DelayClose(_netConnection), 500L);
	}
}


