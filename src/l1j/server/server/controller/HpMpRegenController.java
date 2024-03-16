package l1j.server.server.controller;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.GameSystem.inn.InnHelper;
import l1j.server.server.RepeatTask;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1AiUserInstance;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.utils.CalcConStat;
import l1j.server.server.utils.CalcWisStat;

/**
 * HP/MP 회복 담당 클래스
 * 각 PC(L1PcInstance)에 할당한다.
 * @author LinOffice
 */
public class HpMpRegenController extends RepeatTask {
	private static Logger _log = Logger.getLogger(HpMpRegenController.class.getName());
	private static final Random random = new Random(System.nanoTime());
	
	private L1PcInstance owner;
	
	/**
	 * 기본 생성자
	 * 부모 클래스 RepeatTask 상속
	 * @param owner
	 * @param interval(반복 주기)
	 */
	public HpMpRegenController(L1PcInstance owner, long interval) {
		super(interval);
		this.owner = owner;
	}
	
	/**
	 * 타이머의 주기적 호출 메소드
	 */
	@Override
	public void execute() {
		try {
			if (!isValidation()) {
				return;
			}
			hpRegen();
			mpRegen();
			if (owner instanceof L1AiUserInstance) {
				return;
			}
			clanBuff();
		}catch(Exception e){
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * 유효성 검사
	 * @return boolean
	 */
	boolean isValidation(){
		if (owner == null || (owner.getNetConnection() == null && !(owner instanceof L1AiUserInstance)) || owner.isDead() || owner.isPrivateShop()) {
			return false;
		}
		return true;
	}
	
	/**
	 * HP회복
	 */
	void hpRegen() {
		try {
			if (owner.getCurrentHp() >= owner.getMaxHp() && !isUnderwater()) {
				return;
			}
			owner._regenHpPoint += owner._curHpPoint;
			owner._curHpPoint = 4;
			if (owner._regenHpMax <= owner._regenHpPoint) {
				owner._regenHpPoint = 0;
				calcHpr();
			}
		} catch (Exception e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	/**
	 * MP회복
	 */
	void mpRegen() {
		try {
			if (owner.getCurrentMp() >= owner.getMaxMp()) {
				return;
			}
			owner._regenMpPoint += owner._curMpPoint;
			owner._curMpPoint = 4;
			if (64 <= owner._regenMpPoint) {
				owner._regenMpPoint = 0;
				calcMpr();
			}
			bloodToSoulMpRegen();
			asuraMpRegen();
		} catch (Exception e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * HP회복량을 조사하여 반영한다.
	 */
	void calcHpr() {
		try {
			int maxBonus = 1, totalCon = owner.getAbility().getTotalCon();
			if (11 < owner.getLevel() && totalCon >= 14) {// CON 보너스
				maxBonus = totalCon - 12;
				if (totalCon >= 25) {
					maxBonus = 14;
				}
				if (totalCon >= 35) {
					maxBonus++;
				}
				if (totalCon >= 45) {
					maxBonus += 3;
				}
				if (totalCon >= 55) {
					maxBonus += 5;
				}
				if (totalCon >= 60) {
					maxBonus += 5;
				}
			}
			int basebonus	= CalcConStat.hpRegen(owner.getAbility().getBaseCon());// 베이스 CON 보너스
			int bonus		= random.nextInt(maxBonus) + 1;
			if (L1HouseLocation.isInHouse(owner.getX(), owner.getY(), owner.getMapId())) {
				bonus += 5;
			}
			if (L1HouseLocation.isRegenLoc(owner, owner.getX(), owner.getY(), owner.getMapId())) {
				bonus += 5;
			}
			boolean isInn	= InnHelper.isInnMap(owner.getMapId());
			if (isInn) {
				bonus += 5;
			}

			boolean inLifeStream = isInLifeStream();
			if (inLifeStream) {// 고대의 공간, 마족의 신전에서는 HPR+3은 없어져?
				bonus += 5;
			}
			
			int resultHpr = bonus + basebonus + owner.getHpRegen();
			if (!isInn && isOverWeight()) {
				resultHpr *= totalCon >= 60 ? 0.84375D : totalCon >= 55 ? 0.675D : totalCon >= 45 ? 0.5D : 0;// 중량의 체크
			}
			if (owner.getFood() < 40 || (owner.getSkill().hasSkillEffect(L1SkillId.BERSERKERS) && owner.getBerserkersAC() != 0)) {
				resultHpr = 0;// 공복과 버서커스의 체크
			}

			int newHp = owner.getCurrentHp() + resultHpr;
			if (newHp < 1) {// HPR 감소 장비에 의해 사망은 하지 않는다
				newHp = 1;
			}
			if (isUnderwater()) {// 수중에서의 감소 처리
				newHp -= 30;
			}
			if (owner.getMap().isDecreaseHp() && !inLifeStream) {// HP가 감소하는 map
				newHp -= 10;
			}
			
			if (newHp < 1) {
				if (owner.isGm()) {
					newHp = 1;
				} else {
					owner.death(null, true);// HP가 0이 되었을 경우는 사망한다.
				}
			}
			if (!owner.isDead()) {
				owner.setCurrentHp(Math.min(newHp, owner.getMaxHp()));// hp반영
			}
		} catch (Exception e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	/**
	 * MP회복량을 조사하여 반영한다.
	 */
	void calcMpr() {
		try {
			int baseMpr = 1, totalWis = owner.getAbility().getTotalWis();
			if (totalWis == 15 || totalWis == 16) {
				baseMpr = 3;
			} else if (totalWis == 17) {
				baseMpr = 4;
			} else if (totalWis >= 18) {
				baseMpr += totalWis - 14;
			}
			if (totalWis >= 25) {
				baseMpr++;
			}
			if (totalWis >= 35) {
				baseMpr++;
			}
			if (totalWis >= 45) {
				baseMpr += 3;
			}
			if (totalWis >= 55) {
				baseMpr += 5;
			}
			if (totalWis >= 60) {
				baseMpr += 5;
			}
			// 베이스 WIS 회복 보너스
			int baseStatMpr = CalcWisStat.mpRegen(owner.getAbility().getBaseWis());
			if (owner.getSkill().hasSkillEffect(L1SkillId.STATUS_BLUE_POTION2)) {
				if (totalWis < 11) {
					totalWis = 11;
				}
				baseMpr += totalWis - 8;
			} else if (owner.getSkill().hasSkillEffect(L1SkillId.STATUS_BLUE_POTION)) {
				if (totalWis < 11) {
					totalWis = 11;
				}
				baseMpr += totalWis - 10;
			}
			if (owner.getSkill().hasSkillEffect(L1SkillId.MEDITATION)) {
				baseMpr += owner.isPassiveStatus(L1PassiveId.MEDITATION_BEYOND) ? (int)(owner.getMaxMp() * 0.02D) + 5 : 5;
			}
			if (L1HouseLocation.isInHouse(owner.getX(), owner.getY(), owner.getMapId())) {
				baseMpr += 10;
			}
			if (L1HouseLocation.isRegenLoc(owner, owner.getX(), owner.getY(), owner.getMapId())) {
				baseMpr += 10;
			}
			boolean isInn = InnHelper.isInnMap(owner.getMapId());
			if (isInn) {
				baseMpr += 10;
			}

			int resultMpr = baseMpr + baseStatMpr + owner.getMpRegen();
			if (!isInn && isOverWeight()) {
				resultMpr *= totalWis >= 60 ? 0.84375D : totalWis >= 55 ? 0.675D : totalWis >= 45 ? 0.5D : 0;
			}
			if (owner.getFood() < 40) {
				resultMpr = 0;
			}
			owner.setCurrentMp(owner.getCurrentMp() + resultMpr);// mp 반영
		} catch (Exception e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 수중 상태 검사
	 * @return boolean
	 */
	boolean isUnderwater() {
		// 워터 부츠 장비시인가, 에바의 축복 상태이면, 수중은 아니면 간주한다.
		if (owner.getAbility().isUnderWater() || owner.getSkill().hasSkillEffect(L1SkillId.STATUS_UNDERWATER_BREATH)) {
			return false;
		}
		return owner.getMap().isUnderwater();
	}

	/**
	 * 무게 검사
	 * @return boolean
	 */
	boolean isOverWeight() {
		// 엑조틱바이탈라이즈, 어디셔널파이어, 각성:파푸리온 상태, 중량 오버이지 않으면 간주한다.
		if (owner.getSkill().hasSkillEffect(L1SkillId.EXOTIC_VITALIZE) 
				|| owner.getSkill().hasSkillEffect(L1SkillId.SCALES_WATER_DRAGON)) {
			return false;
		}
		return (50 <= owner.getInventory().getWeightPercent());
	}

	/**
	 * 혈맹 버프 작동 검사
	 */
	void clanBuff() {
		if (owner.getLevel() > 93) {
			if (owner.isClanBuff()) {
				ablityClanBuff(false);
			}
			return;
		}
		
		L1Clan clan = owner.getClan();
		if (clan == null) {
			if (owner.isClanBuff()) {
				ablityClanBuff(false);
			}
			return;
		}
		if (!owner.isClanBuff() && clan.isClanBuff()) {
			ablityClanBuff(true);
		} else if (owner.isClanBuff() && !clan.isClanBuff()) {
			ablityClanBuff(false);
		}
	}
	
	/**
	 * 혈맹 버프 능력치 설정
	 * @param flag
	 */
	void ablityClanBuff(boolean flag){
		owner.setClanBuff(flag);
		owner.add_exp_boosting_ratio(flag ? 20 : -20);
		owner.sendPackets(flag ? S_PacketBox.CLAN_BUFF_ON : S_PacketBox.CLAN_BUFF_OFF);
	}

	/**
	 * 라이프 스트림 안 포험 여부 조사
	 * @return boolean
	 */
	boolean isInLifeStream() {
		for (L1Object object : owner.getKnownObjects()) {
			if (object instanceof L1EffectInstance == false) {
				continue;
			}
			L1EffectInstance effect = (L1EffectInstance) object;
			if (effect.getNpcId() == 81169 && effect.getLocation().getTileLineDistance(owner.getLocation()) < 4) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 블러드 투 소울 MP회복
	 */
	void bloodToSoulMpRegen() {
		if (!owner._isBloodToSoulAuto || owner.getCurrentMp() >= owner.getMaxMp() || owner.getCurrentHp() <= 25 || !owner.getInventory().checkItem(L1ItemId.ELF_ATTR_STONE, 1)) {
			return;
		}
		if (++owner._bloodToSoulCount >= 5) { // 5초마다 한번씩 작동
			if (!(owner instanceof L1AiUserInstance)) {
				owner.getInventory().consumeItem(L1ItemId.ELF_ATTR_STONE, 1);
			}
			owner.setCurrentHp(owner.getCurrentHp() - 25);
			owner.setCurrentMp(owner.getCurrentMp() + 15);
			owner.send_effect(2178);
			owner._bloodToSoulCount = 0;
		}
	}
	
	/**
	 * 아수라  MP회복
	 */
	void asuraMpRegen() {
		if (!owner.getSkill().hasSkillEffect(L1SkillId.ASURA) || owner._asuraCount < 0) {
			return;
		}
		if (--owner._asuraCount < 10 && owner._asuraCount % 2 == 0) {
			owner.setCurrentMp(owner.getCurrentMp() + 80);// 본섭 2초마다 80씩 총 400획득
			owner.send_effect(2171);
		}
	}
}

