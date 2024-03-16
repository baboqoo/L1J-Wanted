package l1j.server.GameSystem.ai.brain;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.controller.action.GameTimeNight;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.L1MoveState;
import l1j.server.server.model.L1SkillStatus;
import l1j.server.server.model.Instance.L1AiUserInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.function.L1HealingPotion;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.sprite.AcceleratorChecker;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Skills;

/**
 * AI 추가 액션 담당 쓰레드
 * @author LinOffice
 */
public class AiBrainSundryThread implements Runnable {
	private static L1Skills fullHeal;
	
	private L1AiUserInstance _ai;
	private ServerBasePacket CURE_EFFECT;
	private ServerBasePacket[] PEARL_PACKET;

	@Override
	public void run() {
		try {
			if (_ai == null || _ai.isDead() || _ai.getAiBrainStatus() == null) {
				return;// 종료 시점
			}
			if (_ai.getTeleport().isTeleport() || _ai.isStop()) {
				GeneralThreadPool.getInstance().schedule(this, 300L);
				return;
			}
			
			int hpPercent = _ai.getCurrentHpPercent();
			if (hpPercent < 20 && teleport()) {// 텔레포트
				GeneralThreadPool.getInstance().schedule(this, 1000L);
				return;
			}
			if (_ai.getSpriteId() == _ai.getClassId() && _ai.doPoly()) {// 변신
				GeneralThreadPool.getInstance().schedule(this, 500L);
				return;
			}
			if (_ai.getDoll() == null && _ai.useDoll()) {// 인형 소환
				GeneralThreadPool.getInstance().schedule(this, 500L);
				return;
			}
			int delay = buff(hpPercent);
			if (delay > 0) {
				GeneralThreadPool.getInstance().schedule(this, delay);
				return;
			}
			delay = potion(hpPercent);
			if (delay > 0) {
				GeneralThreadPool.getInstance().schedule(this, delay);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		GeneralThreadPool.getInstance().schedule(this, 300L);
	}
	
	/**
	 * 버프류 시전
	 * @param hpPercent
	 * @return int
	 */
	int buff(int hpPercent){
		if (_ai.isDesperado() || _ai.isOsiris()) {
			return 0;
		}
		L1SkillStatus status = _ai.getSkill();
		if (status.hasSkillEffect(L1SkillId.SILENCE) && (_ai.isCrown() || _ai.isElf() || _ai.isWizard() || _ai.isDarkelf())) {
			return 0;
		}
		if (_ai.isWizard() && hpPercent < 50 && !status.isSkillDelay(fullHeal.getDelayGroupId())) {
			L1BuffUtil.skillMotionAction(_ai, fullHeal.getSkillId());
			_ai.setBuffMotion(true);
			return _ai.getAcceleratorChecker().getRightInterval(AcceleratorChecker.ACT_TYPE.SPELL_NODIR) + 20;
		}
		for (L1Skills skill : _ai.getAiBuffs()) {
			if (status.hasSkillEffect(skill.getSkillId()) || status.isSkillDelay(skill.getDelayGroupId())) {
				continue;
			}
			L1BuffUtil.skillMotionAction(_ai, skill.getSkillId());
			_ai.setBuffMotion(true);
			return _ai.getAcceleratorChecker().getRightInterval(AcceleratorChecker.ACT_TYPE.SPELL_NODIR) + 20;
		}
		return 0;
	}
	
	/**
	 * 물약류 사용
	 * @param hpPercent
	 * @return int
	 */
	int potion(int hpPercent){
		if (_ai.isPotionPenalty()) {
			return 400;
		}
		if (_ai.getPoison() != null) {// 해독
			cure();
			return 500;
		}
		L1MoveState move = _ai.getMoveState();
		if (move.getMoveSpeed() == 0) {// 1단 가속
			firstSpeed();
			return 500;
		}
		if (move.getBraveSpeed() == 0) {// 2단 가속
			secondSpeed();
			return 500;
		}
		if (move.getDrunken() == 0) {// 3단 가속
			thirdSpeed();
			return 500;
		}
		return hpPotion(hpPercent);// 회복제
	}
	
	/**
	 * 회복 포션 사용
	 * @param hpPercent
	 * @return int
	 */
	int hpPotion(int hpPercent) {
		L1ItemInstance potionItem = hpPercent < 70 ? _ai.getAiHealPotion()[2] : hpPercent < 95 ? _ai.getAiHealPotion()[0] : null;
		if (potionItem == null) {
			return 0;
		}
		L1EtcItem etcItem = (L1EtcItem) potionItem.getItem();
		int delay_id = etcItem.getDelayId();
		if (delay_id != 0 && _ai.hasItemDelay(delay_id)) {
			return 0;
		}
		L1HealingPotion potion = etcItem.getHealingPotion();
		if (potion == null) {
			return 0;
		}
		if (potion.use(_ai, potionItem)) {
			L1ItemDelay.onItemUse(_ai, potionItem);// 아이템 지연 개시
		}
		if (potionItem.getCount() <= 1) {
			potionItem.setCount(100);
		}
		return ((L1EtcItem) potionItem.getItem()).getDelayEffect();
	}
	
	/**
	 * 텔레포트
	 * @return boolean
	 */
	boolean teleport(){
		if (_ai.getMapId() == 54 && GameTimeNight.isNight()) {
			return false;
		}
		return _ai.randomTeleport();
	}
	
	/**
	 * 회복제 사용
	 */
	void cure() {
		if (CURE_EFFECT == null) {
			CURE_EFFECT = new S_Effect(_ai.getId(), 192);
		}
		_ai.broadcastPacket(CURE_EFFECT);
		_ai.curePoison();
	}
	
	/**
	 * 1단 가속 사용
	 */
	void firstSpeed(){
		L1ItemInstance greenPotion = _ai.getAiGreenPotion();
		if (greenPotion != null) {
			greenPotion.clickItem(_ai, null);
			if (greenPotion.getCount() <= 1) {
				greenPotion.setCount(10);
			}
		}
	}
	
	/**
	 * 2단 가속 사용
	 */
	void secondSpeed(){
		L1ItemInstance bravePotion = null;
		switch (_ai.getType()) {
		case 0:case 1:case 6:case 7:case 8:case 9:
			bravePotion = _ai.getAiBravePotion();
			break;
		case 2:
			if (_ai.getElfAttr() == 0) {
				bravePotion = _ai.getAiBravePotion();
			}
			break;
		default:return;
		}
		if (bravePotion == null) {
			return;
		}
		bravePotion.clickItem(_ai, null);
		if (bravePotion.getCount() <= 1) {
			bravePotion.setCount(10);
		}
	}
	
	/**
	 * 3단 가속 사용
	 */
	void thirdSpeed(){
		if (PEARL_PACKET == null) {
			PEARL_PACKET	= new ServerBasePacket[2];
			PEARL_PACKET[0]	= new S_Effect(_ai.getId(), 197);
			PEARL_PACKET[1]	= new S_Liquor(_ai.getId(), 8);
		}
		_ai.broadcastPacket(PEARL_PACKET[0]);
		_ai.getSkill().setSkillEffect(L1SkillId.STATUS_DRAGON_PEARL, 600000);
		_ai.broadcastPacket(PEARL_PACKET[1]);
		_ai.getMoveState().setDrunken(8);
	}
	
	/**
	 * 생성자
	 * @param ai
	 */
	protected AiBrainSundryThread(L1AiUserInstance ai) {
		_ai = ai;
		if (fullHeal == null) {
			fullHeal = SkillsTable.getTemplate(L1SkillId.FULL_HEAL);
		}		
	}
	
	/**
	 * 쓰레드 시작
	 */
	protected void start() {
		GeneralThreadPool.getInstance().execute(this);
	}
}

