package l1j.server.server.model.Instance;

import java.util.Random;

import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.RepeatTask;
import l1j.server.server.construct.L1Doll;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.MagicDollInfoTable.L1DollInfo;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.potential.L1Potential;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.action.S_UseAttackSkill;
import l1j.server.server.serverpackets.alchemy.S_SummonPetNoti;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.object.S_NPCObject;
import l1j.server.server.templates.L1Npc;

public class L1DollInstance extends L1NpcInstance implements L1Doll {
	private static final long serialVersionUID = 1L;
	private static final Random random = new Random(System.nanoTime());
	private L1DollInfo _info;
	private L1Potential _potential;
	private int _dollNpcId;
	private int _itemObjId;
	private DollItemTimer _itemTimer;
	private L1ItemInstance _item;

	// 타겟이 없는 경우의 처리
	@Override
	public boolean noTarget() {
		if (_master == null) {
			deleteMe();
			return true;
		}
		if (_master.isDead()) {
			deleteDoll(false);
			return true;
		}
		if (_master.getMapId() == getMapId()) {
			if (getLocation().getTileLineDistance(_master.getLocation()) > 15) {
				teleport(_master.getX(), _master.getY(), getMoveState() == null ? 0 : getMoveState().getHeading());
			} else if (getLocation().getTileLineDistance(_master.getLocation()) > 2) {
				int dir = moveDirection(_master.getMapId(), _master.getX(), _master.getY());
				if (dir == -1) {
					teleport(_master.getX(), _master.getY(), getMoveState() == null ? 0 : getMoveState().getHeading());
				} else {
					setDirectionMove(dir);
					setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));				 
				}
			}
			return false;
		}
		deleteDoll(false);
		return true;
	}

	// 인형 유지 시간 타이머
	class DollTimer implements Runnable {
		@Override
		public void run() {
			if (_destroyed) {
				return;// 이미 파기되어 있지 않은가 체크
			}
			deleteDoll(true);
		}
	}

	// 보너스 아이템 지급 타이머
	class DollItemTimer extends RepeatTask {
		private int _itemId, _count;
		
		DollItemTimer(int itemId, int count, int time) {
			super(time);
			_itemId = itemId;
			_count	= count;
		}

		@Override
		public void execute() {
			if (_destroyed) { // 이미 파기되어 있지 않은가 체크
				cancel();
				return;
			}

			L1PcInstance pc = (L1PcInstance) _master;
			if (pc == null) {
				cancel();
				return;
			}
			L1ItemInstance item = ItemTable.getInstance().createItem(_itemId);
			item.setCount(_count);
			if (pc.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
				pc.sendPackets(new S_ServerMessage(403, item.getLogNameRef()), true);
			}
		}
	}
	
	private void dollItemTimerStarter(int itemId, int count, int time){
		if (_itemTimer != null) {
			return;
		}
		_itemTimer = new DollItemTimer(itemId, count, time);
		GeneralThreadPool.getInstance().schedule(_itemTimer, time);
	}

	public L1DollInstance(L1Npc template, L1PcInstance master, L1DollInfo info, L1ItemInstance item) {
		super(template);
		setId(IdFactory.getInstance().nextId());
		_dollNpcId	= template.getNpcId();
		_itemObjId	= item.getId();
		_info		= info;
		_potential	= item.getPotential();// 잠재력
		GeneralThreadPool.getInstance().schedule(new DollTimer(), DOLL_TIME);
		setMaster(master);
		setX(master.getX() + random.nextInt(5) - 2);
		setY(master.getY() + random.nextInt(5) - 2);
		setMap(master.getMapId());
		getMoveState().setHeading(5);
		setLightSize(template.getLightSize());
		
		L1World world = L1World.getInstance();
		world.storeObject(this);
		world.addVisibleObject(this);
		for (L1PcInstance pc : world.getRecognizePlayer(this)) {
			onPerceive(pc);
		}
		master.setDoll(this);
		_item = item;
		if (!isAiRunning()) {
			startAI();
		}
		
		master.getInventory().setItemAblity(item);// 일반 옵션
		if (_potential != null) {
			_potential.ablityPotential(master, true);// 잠재력 옵션
		}
		if (_info.isHaste()) {
			master.removeSpeedSkill();
			if (master.getMoveState().getMoveSpeed() != 1) {
				master.getMoveState().setMoveSpeed(1);
				master.sendPackets(new S_SkillHaste(master.getId(), 1, -1), true);
				master.broadcastPacket(new S_SkillHaste(master.getId(), 1, 0), true);
			}
			master.getSkill().setSkillEffect(L1SkillId.STATUS_HASTE, 600 * 3200);
		}
		
		// 보너스 아이템 타이머
		if (_info != null && _info.getBonusItemId() > 0 && _info.getBonusCount() > 0) {
			dollItemTimerStarter(_info.getBonusItemId(), _info.getBonusCount(), _info.getBonusInterval() * 1000);
		}
		master.sendPackets(new S_OwnCharAttrDef(master), true);
		master.sendPackets(new S_SPMR(master), true);
	}

	public void deleteDoll(boolean flag) {
		try {
			if (_master == null) {
				deleteMe();
				return;
			}
			L1PcInstance master = (L1PcInstance) _master;
			master.getInventory().removeItemAblity(_item);// 일반 옵션
			if (_potential != null) {
				_potential.ablityPotential(master, false);// 잠재력 옵션
			}
			if (_info.isHaste()) {
				master.getMoveState().setMoveSpeed(0);
				master.broadcastPacketWithMe(new S_SkillHaste(master.getId(), 0, 0), true);
				master.getSkill().removeSkillEffect(L1SkillId.STATUS_HASTE);
			}
			if (_itemTimer != null) {
				if (_itemTimer != null) {
					_itemTimer.cancel();
				}
				_itemTimer = null;
			}
			master.sendPackets(new S_OwnCharAttrDef(master), true);
			master.sendPackets(new S_SPMR(master), true);
				
			if (master.getDoll() != null) {
				master.setDoll(null);
			}
			if (flag && master != null && !master.getInventory().checkItem(L1ItemId.GEMSTONE, 50)) {// 자동소환
				master.sendPackets(L1ServerMessage.sm337_GEMSTONE);
			}
			
			master.sendPackets(S_SummonPetNoti.ICON_OFF);
			
			//인형사라질때
			master.sendPackets(new S_Effect(getId(), 5936), true);
			master.sendPackets(new S_OwnCharStatus(master), true);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			deleteMe();
			setMaster(null);
		}
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if (_master == null) {
			deleteMe();
			return;
		}
		if (perceivedFrom == null) {
			return;
		}
		perceivedFrom.addKnownObject(this);
		L1PcInstance master = (L1PcInstance) _master;
		if (L1InterServer.isAnonymityInter(this.getMap().getInter()) || master.isGmInvis()) {
			if (perceivedFrom == master) {
				perceivedFrom.sendPackets(new S_NPCObject(this), true);
			}
			return;
		}
		if (master.isInvisble()) {
			if (perceivedFrom == master || (perceivedFrom.isInParty() && perceivedFrom.getParty().isMember(master))
					|| (perceivedFrom.getClanid() != 0 && perceivedFrom.getClanid() == master.getClanid())) {
				perceivedFrom.sendPackets(new S_NPCObject(this), true);
			}
			return;
		}
		perceivedFrom.sendPackets(new S_NPCObject(this), true);
	}

	@Override
	public void onItemUse() {
		;
	}

	@Override
	public void onGetItem(L1ItemInstance item) {
		;
	}

	public int getDollNpcId() {
		return _dollNpcId;
	}
	public void setDollNpcId(int val) {
		_dollNpcId = val;
	}

	public int getItemObjId() {
		return _itemObjId;
	}
	public void setItemObjId(int val) {
		_itemObjId = val;
	}
	
	public L1Potential getPotential(){
		return _potential;
	}
	public void setPotential(L1Potential val){
		_potential = val;
	}

	public int getShortDamageChanceByDoll() {// 근접무기 착용시에만 불려간다.
		if (_info.getDamageChance() <= 0) {
			return 0;
		}
		if (_master instanceof L1PcInstance && random.nextInt(100) + 1 <= _info.getDamageChance()) {
			((L1PcInstance) _master).send_effect(6319);
			return 15;
		}
		return 0;
	}

	// 공격 스킬 발동
	public int attackMagicDamage(L1PcInstance pc, L1Character cha) {
		int effect = _info.getAttackSkillEffectId();
		if (effect == 0) {
			return 0;
		}
		if (random.nextInt(10) + 1 > 1) {
			return 0;
		}
		switch(effect){
		case 11736:// 콜라이트닝
			broadcastPacket(new S_UseAttackSkill(this, cha.getId(), effect, cha.getX(), cha.getY(), ActionCodes.ACTION_SkillAttack, false), true);
			return 30 + random.nextInt(10);
		case 13576:// 헬파이어
			broadcastPacket(new S_EffectLocation(this.getX(), this.getY(), effect), true);
			return 80 + random.nextInt(30);
		case 4022:// 그레그
			L1Attack attack = new L1Attack(pc, cha);
			attack.getDollDrainHP(pc, cha);
			broadcastPacket(new S_UseAttackSkill(this, cha.getId(), effect, cha.getX(), cha.getY(), ActionCodes.ACTION_SkillAttack, false), true);
			attack = null;
			return 0;
		default:
			broadcastPacket(new S_UseAttackSkill(this, cha.getId(), effect, cha.getX(), cha.getY(), ActionCodes.ACTION_SkillAttack, false), true);
			return 20;
		}
	}
	
	@Override
	public boolean checkCondition() {
		return _master == null;
	}

}
