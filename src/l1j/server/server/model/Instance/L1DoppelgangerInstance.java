package l1j.server.server.model.Instance;

import java.util.Random;

import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1World;
import l1j.server.server.model.skill.L1SkillDelay;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.model.sprite.AcceleratorChecker;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.object.S_NPCObject;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.StringUtil;

/**
 * 도펠겡어 담당 인스턴스
 * @author LinOffice
 */
public class L1DoppelgangerInstance extends L1MonsterInstance {
	private static final Random random			= new Random(System.nanoTime());
	private static final long serialVersionUID	= 1L;
	private static final int DIS_SPEED_VALUE	= 120;
	
	// 도펠겡어의 변화를 체크할 변수
	private String _oldName = StringUtil.EmptyString;
	private int _oldGfx, _oldWeapon, _oldAttackSpeed, _oldClassId = -1;
	
	// 도펠겡어의 변화를 알릴 변수
	private boolean _trans;
	
	/**
	 * 생성자
	 * 부모 상속 L1MonsterInstance
	 * @param template
	 */
	public L1DoppelgangerInstance(L1Npc template) {
		super(template);
	}
	
	/**
	 * 도펠겡어 defualt 변경
	 * @param targetPc
	 */
	private void transDefault(L1PcInstance targetPc){
		setName(targetPc.getName());
		setDesc(targetPc.getName());
		setTitle(targetPc.getTitle());
		setAlignment(targetPc.getAlignment());
		//setEmblemId(targetPc.getClan() != null ? targetPc.getClan().getEmblemId() : 0);
		_oldName	= targetPc.getName();
		_oldClassId	= targetPc.getType();
		_trans		= true;
	}
	
	/**
	 * 도펠겡어 공격 범위 변경
	 * @param targetPc
	 */
	private void transWeapon(L1PcInstance targetPc){
		int spriteId			= targetPc.getSpriteId();
		L1ItemInstance weapon	= targetPc.getWeapon();
		_dopelRanged = spriteId == targetPc.getClassId() ? 12 : weapon == null ? 1 : targetPc.getAttackRange();
		AcceleratorChecker accel = targetPc.getAcceleratorChecker();
		if (_oldAttackSpeed != accel.getAttackInterval() + DIS_SPEED_VALUE) {
			transSpeed(targetPc, accel);
		}
		_oldWeapon	= targetPc.getCurrentWeapon();
		_trans		= true;
	}
	
	/**
	 * 도펠겡어 외형 변경
	 * @param targetPc
	 */
	private void transGfx(L1PcInstance targetPc){
		setSpriteId(targetPc.getSpriteId());
		AcceleratorChecker accel = targetPc.getAcceleratorChecker();
		if (_oldAttackSpeed != accel.getAttackInterval() + DIS_SPEED_VALUE) {
			transSpeed(targetPc, accel);
		}
		_oldGfx	= targetPc.getSpriteId();
		_trans	= true;
	}
	
	/**
	 * 도펠겡어 세부 스피드 변경
	 * @param targetPc
	 * @param accel
	 */
	private void transSpeed(L1PcInstance targetPc, AcceleratorChecker accel){
		setPassispeed(accel.getMoveInterval() + DIS_SPEED_VALUE);// 이동 속도
		setAtkspeed(accel.getAttackInterval() + DIS_SPEED_VALUE);// 공격 속도
		setAtkMagicspeed(accel.getDirSpellInterval());// 어택 스킬 속도
		setSubMagicspeed(accel.getNodirSpellInterval());// 서브 스킬 속도
		getMoveState().setMoveSpeed(targetPc.getMoveState().getMoveSpeed());// 해이스트
		getMoveState().setBraveSpeed(targetPc.getMoveState().getBraveSpeed());// 2단가속
		_oldAttackSpeed = getAtkspeed();
	}
	
	/**
	 * 도펠겡어 변경
	 * @return boolean
	 */
	private boolean isTrans(){
		if (_target instanceof L1PcInstance == false) {
			return false;
		}
		L1PcInstance targetPc = (L1PcInstance) _target;
		if (!_oldName.equals(targetPc.getName())) {
			transDefault(targetPc);
		}
		if (_oldWeapon != targetPc.getCurrentWeapon()) {
			transWeapon(targetPc);
		}
		if (_oldGfx != targetPc.getSpriteId()) {
			transGfx(targetPc);
		}
		if (_trans){
			finalyTrans();
			return true;
		}
		return false;
	}
	
	S_RemoveObject s_remove;
	
	/**
	 * 도펠겡어 오브젝트 변경 반영
	 */
	private void finalyTrans(){
		_trans = false;
		if (s_remove == null) {
			s_remove = new S_RemoveObject(this);
		}
		S_NPCObject npcPck		= new S_NPCObject(this);
		for (L1PcInstance player : L1World.getInstance().getRecognizePlayer(this)) {
			if (player == null) {
				continue;
			}
			player.sendPackets(s_remove);
			player.sendPackets(npcPck);
		}
		npcPck.clear();
		npcPck = null;
		setSleepTime(1000);// 변경 sleep
	}
	
	@Override
	public void onTarget() {
		try {
			if (_target == null) {
				return;
			}
			int targetX = _target.getX();
			int targetY = _target.getY();
			_actived = true;
			if (getAtkspeed() == 0 && getPassispeed() == 0) {
				return;
			}
			int distance = getLocation().getTileLineDistance(_target.getLocation());
			if (_target.getMapId() != getMapId() || distance > DISTANCE_RANGE_VALUE) {
				tagertClear();
				return;
			}
			if (isBlind() && distance > 1) {
				tagertClear();
				return;
			}
			if (isTrans()) {
				return;
			}
			if (getAtkspeed() == 0 && getPassispeed() > 0) {
				if (distance > 15) {
					tagertClear();
					return;
				}
				onlyMoveAstar(targetX, targetY);
				return;
			}
			
			if (dopelSkillAction(_target, targetX, targetY)) {// 도펠겡어 스킬시전
				return;
			}
			int range = _dopelRanged != 0 ? _dopelRanged : getNpcTemplate().getRanged();
			if (isAttackPosition(_target, range, _target instanceof L1DoorInstance)
					&& _target.isAttackPosition(this, range, false)) {
				attackTarget();
				return;
			}
			
			// TODO 공격범위가 아니므로 타겟에게 이동 처리
			if (getPassispeed() <= 0) {
				tagertClear();
				return;
			}
			if (isHold()) {
				return;
			}
			onMove(targetX, targetY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 도펠겡어의 스킬 시전 여부를 판별한다.
	 * @param target
	 * @param targetx
	 * @param targety
	 * @return boolean
	 */
	private boolean dopelSkillAction(L1Character target, int targetx, int targety){
		if (getSkill().isSkillDelay(0) || target instanceof L1PcInstance == false) {
			return false;
		}
		int spell_id = -1;
		if (_oldClassId == 0// 군주
				&& _dopelRanged == 1
				&& !target.getSkill().hasSkillEffect(L1SkillId.EMPIRE)
				&& isAttackPosition(target, _dopelRanged, false)
				&& random.nextInt(100) + 1 <= 5) {
			spell_id = L1SkillId.EMPIRE;
		} else if (_oldClassId == 1// 기사
				&& _dopelRanged == 1
				&& !target.getSkill().hasSkillEffect(L1SkillId.SHOCK_STUN)
				&& isAttackPosition(target, _dopelRanged, false)
				&& random.nextInt(100) + 1 <= 5) {
			spell_id = L1SkillId.SHOCK_STUN;
		} else if (_oldClassId == 2// 요정
				&& _dopelRanged >= 12
				&& isAttackPosition(target, _dopelRanged, false)
				&& random.nextInt(100) + 1 <= 7) {
			spell_id = L1SkillId.MOB_TRIPLE_ARROW;
		} else if (_oldClassId == 3// 법사
				&& isAttackPosition(target, 4, false)
				&& random.nextInt(100) + 1 <= 10) {
			spell_id = L1SkillId.SUNBURST;
		} else if (_oldClassId == 5// 용기사
				&& !target.getSkill().hasSkillEffect(L1SkillId.DESTROY)
				&& isAttackPosition(target, 4, false)
				&& random.nextInt(100) + 1 <= 5) {
			spell_id = L1SkillId.DESTROY;
		} else if (_oldClassId == 6// 환술사
				&& !target.getSkill().hasSkillEffect(L1SkillId.PHANTASM)
				&& isAttackPosition(target, 4, false)
				&& random.nextInt(100) + 1 <= 5) {
			spell_id = L1SkillId.PHANTASM;
		} else if (_oldClassId == 7// 전사
				&& isAttackPosition(target, 4, false)
				&& random.nextInt(100) + 1 <= 7) {
			spell_id = L1SkillId.HOWL;
		} else if (_oldClassId == 8// 검사
				&& !target.getSkill().hasSkillEffect(L1SkillId.JUDGEMENT)
				&& isAttackPosition(target, 4, false)
				&& random.nextInt(100) + 1 <= 5) {
			spell_id = L1SkillId.JUDGEMENT;
		} else if (_oldClassId == 9// 창기사
				&& isAttackPosition(target, 4, false)
				&& random.nextInt(100) + 1 <= 5) {
			spell_id = L1SkillId.POS_WAVE;
		}
		if (spell_id == -1) {
			return false;
		}
		L1Skills _skill		= SkillsTable.getTemplate(spell_id);
	    L1SkillUse skillUse	= new L1SkillUse(true);
	    skillUse.handleCommands(null, spell_id, target.getId(), target.getX(), target.getY(), 0, L1SkillUseType.NORMAL, this);
	    skillUse			= null;
	    L1SkillDelay.onSkillUse(this, _skill.getReuseDelay(), 0);
	    setSleepTime(calcSleepTime(getAtkMagicspeed(), MAGIC_SPEED) + 300);// 액션 딜레이
	    return true;
	}
	
	public int getCurrentWeapon(){
		return _oldWeapon;
	}
	
	public int getClassId(){
		return _oldClassId;
	}
}

