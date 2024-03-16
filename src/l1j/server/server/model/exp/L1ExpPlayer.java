package l1j.server.server.model.exp;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import l1j.server.Config;
import l1j.server.IndunSystem.ruun.Ruun;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1Einhasad;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ExpBoostingInfo;
import l1j.server.server.serverpackets.einhasad.S_RestExpInfoNoti;

/**
 * 플레이어 겸험치 처리 핸들러(L1ExpHandler 상속)
 * @author LinOffice
 */
public class L1ExpPlayer extends L1ExpHandler {
	private static final Random random = new Random(System.nanoTime());
	public static long LIMIT_EXP = getLimitExp();
	
	public static int getExpPotion3Value(int level){
		return level >= 89 ? 60 : level == 88 ? 70 : level == 87 ? 80 : level == 86 ? 90 : level >= 84 ? 100 : level >= 82 ? 110 : level >= 80 ? 120 : 130;
	}
	
	public static int getExpPotion6Value(int level){
		return level >= 93 ? 30 : level >= 90 ? 35 : level >= 88 ? 40 : 50;
	}
	
	public static int getTopazExp(){
		return 150;
	}
	
	public static int getDragonPupleExp(int level){
		return level >= 65 ? 23 : level >= 60 ? 33 : level >= 55 ? 43 : level >= 49 ? 53 : 0;
	}
	
	private static final List<Short> NOT_ENABLE_EXP_MAP = Arrays.asList(new Short[] {
			623, 2041, 2047, 2053, 2059
	});
	
	private static final List<Short> BEGINNER_QUEST_EXP_MAP = Arrays.asList(new Short[] {
			1, 2, 7, 8, 9, 10, 11, 12, 12146, 12147, 12148, 12149, 12150, 12152, 12257, 12358
	});
	
	/**
	 * 초급 퀘스트 경험치 배율 계산(퀘스트 누락 방지)
	 * @param level
	 * @return rate
	 */
	private static double calc_beginner_quest_map_exp_rate(int level) {
		double rate = Config.RATE.RATE_XP;
		if (level < 49 && rate > 1.0D) {
			return 1.0D;
		}
		if (level < 52 && rate > 5.0D) {
			return 5.0D;
		}
		if (level < 65 && rate > 8.0D) {
			return 8.0D;
		}
		if (level < 70 && rate > 10.0D) {
			return 10.0D;
		}
		if (level < 75 && rate > 15.0D) {
			return 15.0D;
		}
		return rate;
	}
	
	public L1ExpPlayer(L1PcInstance owner) {
		super(owner);
	}

	@Override
	public void addExp(long exp, int align) {
		if (_player == null || _player.getNetConnection() == null || _player.getAccount() == null || _player.isGhost()) {
			return;
		}
		if (align != 0) {
			_player.addAlignment((int)(align * Config.RATE.RATE_ALIGNMENT) * -1);
		}
		
		if (exp <= 0) {
			return;
		}
		if (Config.SERVER.STANDBY_SERVER) {
			return;
		}
		if (_player.getExp() >= LIMIT_EXP) {
			return;
		}
		
		short mapId = _player.getMapId();
		if (NOT_ENABLE_EXP_MAP.contains(mapId)) {
			return;			
		}
		
		int level			= _player.getLevel();
		double expPenalty	= ExpTable.getPenalty(level);
		double bonusExp		= (((double) _player.get_exp_boosting_ratio()) * 0.01D);// 보너스 경험치
		double combo		= 1.0D;
		
		// 루운의 초대장
		if (_player._isRuunPaper && Ruun.isRuunMap(mapId)) {
			bonusExp += 0.1D;
		}
		
		// 보너스 경험치
		if (bonusExp > 0.0D) {
			exp *= bonusExp + 1.0D;
		}
		
		// 부스터
		if ((_player.isFencer() && level < 85 && _player.isPassiveStatus(L1PassiveId.GROWS))
				|| (_player._isElixerBooster > 0 && level >= 85 && level < 90)) {
			exp *= getBooster();
		}
		
		// 그랑카인 시스템
		if (Config.FATIGUE.FATIGUE_ACTIVE) {
			double fatiguePenalty = _player.getFatigue().getExpPenalty();
			if (fatiguePenalty > 0) {
				exp *= fatiguePenalty;
			}
		}
		
		// 아인하사드(경험치 반영은 추가 경험치에 합산되어 있다)
		L1Einhasad ein				= _player.getAccount().getEinhasad();
		int einhasadValue			= ein.getPoint();
		int einhasadDefaultRation	= Config.EIN.REST_EXP_DEFAULT_RATION;
		if (einhasadValue >= einhasadDefaultRation) {
			// 아인하사드 수치가 존재하면 콤보 버프가 발동한다.
			if (_player.getSkill().hasSkillEffect(L1SkillId.COMBO_BUFF)) {
				combo		+= isCombo();
			}
			einhasadConsume(exp, ein, einhasadDefaultRation);
		}
		
		long addExp = 0L;
		
		// 초급 퀘스트 중에는 매끄러운 진행을 위해 배율을 통제한다.
		if (level < 75 && Config.QUEST.BEGINNER_QUEST_ACTIVE && BEGINNER_QUEST_EXP_MAP.contains(mapId)) {
			addExp = (long) (exp * calc_beginner_quest_map_exp_rate(level) * combo * expPenalty);
		} else {
			addExp = (long) (exp * Config.RATE.RATE_XP * combo * expPenalty);
		}
		
		if (addExp < 0) {
			System.out.println(String.format("[L1ExpPlayer] MINUS_EXP_CHECK : VALUE(%d), CHAR_NAME(%s)", addExp, _player.getName()));
			return;
		}
		
		// 패널티에 의해 0이 된다면 1로 고정한다.
		if (addExp == 0) {
			addExp = 1;
		}
		
		// 폭렙 방지
		if (addExp + _player.getExp() > ExpTable.getExpByLevel(level + 1)) {
			addExp = (ExpTable.getExpByLevel(level + 1) - _player.getExp());
		}
		
		// 혈맹 공헌도
		if (_player.getClan() != null 
				&& (!Config.PLEDGE.PLEDGE_CONTRIBUTION_EINHASAD || einhasadValue >= einhasadDefaultRation)) {
			addPledgeContribution(addExp);
		}
		
		_player.addExp(addExp);
	}
	
	/**
	 * 경험치 콤보
	 * @return double
	 */
	double isCombo(){
		if (_player.getComboCount() <= 10) {
			return 0.1D * _player.getComboCount();
		}
		if (_player.getComboCount() > 10 && _player.getComboCount() <= 15) {
			return (0.1D * _player.getComboCount()) + (0.2D * (_player.getComboCount() - 10));
		}
		if (_player.getComboCount() > 15) {
			return 3.0D;
		}
		return 0D;
	}
	
	/**
	 * 아인하사드 감소 처리
	 */
	void einhasadConsume(long exp, L1Einhasad ein, int einhasadDefaultRation){
		double einBlessExp = 1.0D;
		if (_player.getSkill().hasSkillEffect(L1SkillId.EMERALD_YES)) {
			einBlessExp += 0.54D;
		} else if (_player.getSkill().hasSkillEffect(L1SkillId.DRAGON_PUPLE)) {
			einBlessExp += (double)(getDragonPupleExp(_player.getLevel()) * 0.01D);
		} else if (_player.getSkill().hasSkillEffect(L1SkillId.DRAGON_TOPAZ)) {
			einBlessExp += (double)(getTopazExp() * 0.01D);
		} else if (_player.getSkill().hasSkillEffect(L1SkillId.EXP_POTION6)) {
			einBlessExp += 0.2D;
		} else if (_player.getSkill().hasSkillEffect(L1SkillId.EXP_POTION7)) {
			einBlessExp += 0.3D;
		}
		
		int einDecrease					= (int)((exp * Config.EIN.REST_EXP_DECREASE_RATE) * einBlessExp);
		int rest_exp_reduce_efficiency	= _player.get_rest_exp_reduce_efficiency();// 축복 소모 효율
		if (rest_exp_reduce_efficiency > 0) {
			int defense		= (int)((einDecrease / Config.EIN.REST_EXP_REDUCE_EFFICIENCY_PERCENT) * (rest_exp_reduce_efficiency >= 100 ? 99 : rest_exp_reduce_efficiency));
			einDecrease		-= defense;
		}
		if (einDecrease <= 0) {
			einDecrease		= einhasadDefaultRation >> 2;// 4분의1고정
		}
		ein.addPoint(-einDecrease, _player);// 아인하사드 소모
		if (ein.getPoint() < einhasadDefaultRation) {// 아인하사드 소모 후의 수치
			if (_player.getSkill().hasSkillEffect(L1SkillId.COMBO_BUFF)) {
				_player.getSkill().removeSkillEffect(L1SkillId.COMBO_BUFF);
			}
			if (_player.getSkill().hasSkillEffect(L1SkillId.DRAGON_PUPLE)) {
				_player.getSkill().removeSkillEffect(L1SkillId.DRAGON_PUPLE);
			}
		}
		// 아인하사드 포인트 획득
		if (_player.getEinPoint() < Config.EIN.EINHASAD_POINT_LIMIT_CHARGE_VALUE) {
			int addEinPoint = (int)((einDecrease * 0.0001D) * Config.EIN.EINHASAD_POINT_RATE);
			if (addEinPoint > 0) {
				_player.addEinPoint(addEinPoint);
			}
		} else {
			_player.sendPackets(L1ServerMessage.sm7552);// 아인하사드 포인트를 더 이상  얻을 수 없습니다.
		}
		
		_player.sendPackets(new S_RestExpInfoNoti(_player), true);
		_player.sendPackets(new S_ExpBoostingInfo(_player), true);
	}
	
	/**
	 * 혈맹 공헌도
	 * @param addExp
	 */
	void addPledgeContribution(long addExp){
		int clanContribution = (int)((addExp * 0.01D) * Config.PLEDGE.PLEDGE_CONTRIBUTION_RATE);
		if (clanContribution > 0) {
			_player.addClanWeekContribution(clanContribution);
		}
	}
	
	/**
	 * 경험치 부스트
	 * @return
	 */
	int getBooster(){
		int chance = random.nextInt(100) + 1;
		if (chance <= 6) {
			_player.send_effect_self(18572);
			return 2;
		}
		if (chance <= 9) {
			_player.send_effect_self(18574);
			return 3;
		}
		if (chance == 10) {
			_player.send_effect_self(18576);
			return 5;
		}
		return 1;
	}

	@Override
	public void addExp(long exp) {
		if (_player == null || _player.getNetConnection() == null || Config.SERVER.STANDBY_SERVER || _player.isGhost()) {
			return;
		}
		if (_player.getExp() >= LIMIT_EXP) {
			return;
		}
		int level = _player.getLevel();
		if (level >= Config.CHA.LIMIT_LEVEL) {
			return;
		}
		long addExp = (long) (exp * ExpTable.getPenalty(level));
		if (addExp < 0) {
			System.out.println(String.format("[L1ExpPlayer] MINUS_EXP_CHECK : VALUE(%d), CHAR_NAME(%s)", addExp, _player.getName()));
			return;
		}
		if (addExp == 0) {
			addExp = 1;
		}
		// 폭렙 방지
		if (addExp + _player.getExp() > ExpTable.getExpByLevel(level + 1)) {
			addExp = (ExpTable.getExpByLevel(level + 1) - _player.getExp());
		}
		_player.addExp(addExp);
	}
	
	public static void reloadLimitExp(){
		LIMIT_EXP = getLimitExp();
	}
	
	public static long getLimitExp(){
		long limitExp	= ExpTable.getExpByLevel(Config.CHA.LIMIT_LEVEL);// 제한 레벨까지의 경험치
		long nextExp	= ExpTable.getExpByLevel(Config.CHA.LIMIT_LEVEL + 1);// 다음레벨 까지의 경험치
		long percentExp	= (long)((nextExp - limitExp) * 0.99D);// 제한레벨에서 다음레벨까지의 경험치의 99%
		return limitExp + percentExp;// 제한레벨 99%까지의 경험치
	}

}

