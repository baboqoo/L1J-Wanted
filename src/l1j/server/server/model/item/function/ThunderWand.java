package l1j.server.server.model.item.function;

import l1j.server.Config;
import l1j.server.LFCSystem.InstanceEnums.InstStatus;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.sprite.AcceleratorChecker;
import l1j.server.server.model.sprite.AcceleratorChecker.ACT_TYPE;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.action.S_UseAttackSkill;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;

public class ThunderWand extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public ThunderWand(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.isInvisble() || Config.SERVER.STANDBY_SERVER) {
				return;
			}
			int delay_id = ((L1EtcItem) this.getItem()).getDelayId();
			if (delay_id != 0 && pc.hasItemDelay(delay_id) == true) {
				return;// 지연 설정 있어
			}
			int spellsc_objid, spellsc_x, spellsc_y;
			spellsc_objid	= packet.readD();
			spellsc_x		= packet.readH();
			spellsc_y		= packet.readH();
			int itemId		= this.getItemId();
			int effectId	= (itemId == 40006 || itemId == 140006) ? 6598 : 10;
			pc.cancelAbsoluteBarrier();
			L1Object target = L1World.getInstance().findObject(spellsc_objid);
			if (target != null && !doWandAction(pc, target, effectId)) {
				pc.broadcastPacketWithMe(new S_UseAttackSkill(pc, 0, effectId, spellsc_x, spellsc_y, 17), true);
				pc.getSpeedSync().setAttackSyncInterval(pc.getAcceleratorChecker().getRightInterval(ACT_TYPE.WAND) + System.currentTimeMillis() + Config.SPEED.WAND_SPEED_SYNCHRONIZED);// 액션 시간
			}
			pc.getInventory().updateItem(this, L1PcInventory.COL_COUNT);
			L1ItemDelay.onItemUse(pc, this);// 아이템 지연 개시
			pc.getInventory().removeItem(this, 1);
		}
	}
	
	private boolean doWandAction(L1PcInstance user, L1Object target, int effectId) {
		if (user.glanceCheck(15, target.getX(), target.getY(), target instanceof L1DoorInstance) == false || user.getAcceleratorChecker().isAccelerated(AcceleratorChecker.ACT_TYPE.ATTACK, 1000)) {
			return false;// 직선상에 장애물이 있다
		}
		/** LFC **/
		if (user.getInstStatus() == InstStatus.INST_USERSTATUS_LFCINREADY) {
			return false;
		}
		// XXX 적당한 대미지 계산, 요점 수정
		int dmgAdd = target instanceof L1PcInstance ? 60 - (((L1PcInstance) target).getResistance().getMr() / 10) : 10 + (user.getLevel() >> 2);
		int dmg = (dmgAdd >> 1) + CommonUtil.random(5);
		
		if (target instanceof L1Character) {
			L1Character cha = (L1Character) target;
			int reduction = cha.getAbility().getDamageReduction() - user.getAbility().getDamageReductionEgnor();
			if (reduction > 0) {
				dmg -= reduction;
			}
			
			if (cha.getSkill().hasSkillEffect(L1SkillId.COUNTER_MAGIC)) {
				cha.getSkill().removeSkillEffect(L1SkillId.COUNTER_MAGIC);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(S_PacketBox.COUNTER_MAGIC_OFF);
					pc.send_effect(10702);
				} else {
					cha.broadcastPacket(new S_Effect(cha.getId(), 10702), true);
				}
			}
			if (cha.getSkill().hasSkillEffect(L1SkillId.ERASE_MAGIC)) {
				cha.getSkill().removeSkillEffect(L1SkillId.ERASE_MAGIC);
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(S_PacketBox.ERASE_MAGIC_OFF);
				}
			}
		}

		if (target instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) target;
			if (pc.getMapId() != 2006 && pc.getMap().isSafetyZone(pc.getLocation()) || user.checkNonPvP(user, pc)) {// 공격할 수 없는 존
				return false;
			}
			if (user.getRegion() == L1RegionStatus.SAFETY && (pc.getRegion() == L1RegionStatus.NORMAL || pc.getRegion() == L1RegionStatus.COMBAT)) {// 세이프티존에서노멀존x, 세이프티존에서컴뱃존x
				return false;
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER) || pc.isBind()) {
				return false;
			}
			if (dmg > 0) {
				user.broadcastPacketWithMe(new S_UseAttackSkill(user, pc.getId(), effectId, pc.getX(), pc.getY(), 17), true);
				pc.receiveDamage(user, dmg);
			} else {
				user.broadcastPacketWithMe(new S_UseAttackSkill(user, pc.getId(), effectId, pc.getX(), pc.getY(), 17, 0), true);
			}
			L1PinkName.onAction(pc, user);
		} else if (target instanceof L1MonsterInstance) {
			L1MonsterInstance mob = (L1MonsterInstance) target;
			if (mob.getKarma() > 0) {
				return false;
			}
			user.broadcastPacketWithMe(new S_UseAttackSkill(user, mob.getId(), effectId, mob.getX(), mob.getY(), 17), true);
			dmg = Math.max(1, dmg);
			mob.receiveDamage(user, dmg);
		} else if (target instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance) target;
			user.broadcastPacketWithMe(new S_UseAttackSkill(user, npc.getId(), effectId, npc.getX(), npc.getY(), 17), true);
		} else {
			user.broadcastPacketWithMe(new S_UseAttackSkill(user, target.getId(), effectId, target.getX(), target.getY(), 17), true);
		}
		user.getSpeedSync().setAttackSyncInterval(user.getAcceleratorChecker().getRightInterval(ACT_TYPE.WAND) + System.currentTimeMillis() + Config.SPEED.WAND_SPEED_SYNCHRONIZED);// 액션 시간
		return true;
	}
	
}

