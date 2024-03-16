package l1j.server.server.model.item.function;

import l1j.server.server.ActionCodes;
import l1j.server.server.GameServerSetting;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1AiUserInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.action.S_AttackStatus;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.StringUtil;

public class PolyWand extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public PolyWand(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int spellsc_objid = 0, spellsc_x = 0, spellsc_y = 0;
			spellsc_objid	= packet.readD();
			String polyName	= packet.readS();
			if (pc.getMapId() == 63 || pc.getMapId() == 552 || pc.getMapId() == 555 || pc.getMapId() == 557 || pc.getMapId() == 558 || pc.getMapId() == 779) {
				pc.sendPackets(L1ServerMessage.sm563);// HC4f·배의 묘지 수중에서는 사용 불가
				return;
			}
			if (pc.getSpriteId() == 6034 || pc.getSpriteId() == 6035) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			L1Object target = L1World.getInstance().findObject(spellsc_objid);
			if (spellsc_objid == pc.getId() || target != null) {
				if (target instanceof L1Character == false) {
					return;
				}
				cha = spellsc_objid == pc.getId() ? pc : (L1Character) target;
				poly(pc, cha, polyName);
				pc.cancelAbsoluteBarrier();
				pc.getInventory().updateItem(this, L1PcInventory.COL_COUNT);
				pc.getInventory().removeItem(this, 1);
			} else {
				pc.sendPackets(L1ServerMessage.sm79);
			}
			if (pc.getId() == spellsc_objid || target == null) {
				spellsc_x = pc.getX();
				spellsc_y = pc.getY();
			} else {
				spellsc_x = target.getX();
				spellsc_y = target.getY();
			}
			int heding = pc.targetDirection(spellsc_x, spellsc_y);
			if (pc.getMoveState().getHeading() != heding) {
				pc.getMoveState().setHeading(heding);
			}
			pc.broadcastPacketWithMe(new S_AttackStatus(pc, 0, ActionCodes.ACTION_Wand), true);
		}
	}
	
	private void poly(L1PcInstance attacker, L1Character cha, String polyName) {
		if (cha instanceof L1PcInstance && !(cha instanceof L1AiUserInstance)) {
			L1InterServer inter = ((L1PcInstance) cha).getNetConnection().getInter();
			if(L1InterServer.isNotPolyInter(inter))return;
		}
		boolean isSameClan = false;
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getClanid() != 0 && attacker.getClanid() == pc.getClanid()) {
				isSameClan = true;
			}
		}

		if (attacker.getId() != cha.getId() && !isSameClan) {
			int probability = 3 * (attacker.getLevel() - cha.getLevel()) - cha.getResistance().getEffectedMrBySkill();
			int rnd = CommonUtil.random(100) + 1;
			if (rnd > probability) {
				return;
			}
		}

		int polyId = L1SkillInfo.SHAPE_CHANGE_POLY_ARRAY[CommonUtil.random(L1SkillInfo.SHAPE_CHANGE_POLY_ARRAY.length)];// 단풍막대

		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (attacker.getId() != pc.getId() && (pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE_DOMINATION) || pc.getSkill().hasSkillEffect(L1SkillId.SHAPE_CHANGE_100LEVEL))) {
				pc.send_effect(15846);
			} else if (pc.getInventory().checkEquipped(20281)) {// 변신 조종 반지
				if (polyName.startsWith(L1PolyMorph.MAPLE_STR)) {
					polyName = polyName.replace(L1PolyMorph.MAPLE_STR, StringUtil.EmptyString).trim();
				}
				boolean isRank = pc.getConfig().is_rank_poly();
				if (polyName.startsWith(L1PolyMorph.RANKING_STR) && !isRank) {
					return;
				}
				if (polyName.equalsIgnoreCase(L1PolyMorph.RANKING_CLASS_STR)) {
					if (!isRank) {
						return;
					}
					polyName = L1PolyMorph.RANKING_CLASS_ARRAY[pc.getType()][pc.getGender().toInt()];
				} else if (polyName.equalsIgnoreCase(L1PolyMorph.BASIC_CLASS_STR)) {
					polyName = L1PolyMorph.BASIC_CLASS_ARRAY[pc.getType()][pc.getGender().toInt()];
				}
				
				L1PolyMorph poly = PolyTable.getInstance().getTemplate(polyName);
				if (poly != null || polyName.equals(StringUtil.EmptyString)) {
					if (polyName.equals(StringUtil.EmptyString)) {
						if (pc.getSpriteId() == 6034 || pc.getSpriteId() == 6035) {
						} else {
							pc.removeShapeChange();
						}
					} else if (poly.getMinLevel() <= pc.getLevel() || pc.isGm() || GameServerSetting.POLY_LEVEL_EVENT) {
						L1PolyMorph.handleCommands(pc, polyName);
						pc.cancelAbsoluteBarrier();
						if (pc.isGm()) {
							//pc.sendPackets(new S_SystemMessage(String.format("\\aH변신이름  >> %s 변신번호  >> %d", polyName, pc.getSpriteId())), true);
							pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(16), polyName, String.valueOf(pc.getSpriteId())), true);
						}
					} else {
						pc.sendPackets(L1ServerMessage.sm181);// \f1 그러한 monster에게는 변신할 수 없습니다.
					}
				}
			} else {
				if (pc._isArmorSetPoly) {
					return;
				}
				L1Skills skillTemp = SkillsTable.getTemplate(L1SkillId.SHAPE_CHANGE);
				L1PolyMorph.doPoly(pc, polyId, skillTemp.getBuffDuration(), L1PolyMorph.MORPH_BY_ITEMMAGIC);
				if (attacker.getId() != pc.getId()) {
					pc.sendPackets(new S_ServerMessage(241, attacker.getName()), true); // 누구가 당신을 변신시켰습니다.
				}
			}
		} else if (cha instanceof L1MonsterInstance) {
			L1MonsterInstance mob = (L1MonsterInstance) cha;
			if (mob.getLevel() < 50) {
				int npcId = mob.getNpcTemplate().getNpcId();
				if (npcId != 45338 && npcId != 45370 && npcId != 45456 && npcId != 45464 && npcId != 45473 && npcId != 45488 && npcId != 45497 && npcId != 45516 && npcId != 45529 && npcId != 45458) {
					L1Skills skillTemp = SkillsTable.getTemplate(L1SkillId.SHAPE_CHANGE);
					L1PolyMorph.doPoly(mob, polyId, skillTemp.getBuffDuration(), L1PolyMorph.MORPH_BY_ITEMMAGIC);
				}
			}
		}
	}

}


