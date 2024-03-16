package l1j.server.server.model.item.function;

import l1j.server.server.ActionCodes;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.action.S_AttackStatus;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;

public class BanishWand extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	private static final byte[] HEADING_TABLE_X = { 0, 1, 1, 1, 0, -1, -1, -1 };
	private static final byte[] HEADING_TABLE_Y = { -1, -1, 0, 1, 1, 1, 0, -1 };

	public BanishWand(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1Object target = L1World.getInstance().findObject(packet.readD());
			if (target == null || !(target instanceof L1Character)) {
				return;
			}
			L1Character cha2 = (L1Character) target;
			pc.broadcastPacketWithMe(new S_AttackStatus(pc, 0, ActionCodes.ACTION_Wand), true);
			pc.getInventory().updateItem(this, L1PcInventory.COL_COUNT);
			pc.getInventory().removeItem(this, 1);
			if (cha2.getSkill().hasSkillEffect(L1SkillId.COUNTER_MAGIC)) {
				cha2.getSkill().removeSkillEffect(L1SkillId.COUNTER_MAGIC);
				int castgfx = SkillsTable.getTemplate(L1SkillId.COUNTER_MAGIC).getCastGfx();
				Broadcaster.broadcastPacket(cha2, new S_Effect(cha2.getId(), castgfx), true);
				if (cha2 instanceof L1PcInstance) {
					L1PcInstance pc2 = (L1PcInstance) cha2;
					pc2.sendPackets(new S_Effect(pc2.getId(), castgfx), true);
				}
				return;
			}
			if (target instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) target;
				if (pc != targetPc && pc.getLevel() > targetPc.getLevel() && CommonUtil.random(100) < 80 - (targetPc.getResistance().getEffectedMrBySkill() >> 1)) {
					if (!L1CastleLocation.checkInAllWarArea(targetPc.getX(), targetPc.getY(), targetPc.getMapId()) && !(pc.getMap().isSafetyZone(pc.getLocation()) || targetPc.checkNonPvP(targetPc, pc))) {
						int heading = CommonUtil.random(8);
						heading = checkObject(targetPc.getX(), targetPc.getY(), targetPc.getMapId(), heading);
						if (heading != -1) {
							int tempx = targetPc.getX() + HEADING_TABLE_X[heading];
							int tempy = targetPc.getY() + HEADING_TABLE_Y[heading];
							targetPc.getTeleport().start(tempx, tempy, (short) targetPc.getLocation().getMapId(), targetPc.getMoveState().getHeading(), false);
						}
					}
				}
				if (targetPc.getSkill().hasSkillEffect(L1SkillId.ERASE_MAGIC)) {
					targetPc.getSkill().removeSkillEffect(L1SkillId.ERASE_MAGIC);
				}
			}
		}
	}
	
	private int checkObject(int x, int y, short m, int d) {
		L1Map map = L1WorldMap.getInstance().getMap(m);
		switch (d) {
		case 1:
			if(map.isPassable(x, y, 1))		return 1;
			else if(map.isPassable(x, y, 0))return 0;
			else if(map.isPassable(x, y, 2))return 2;
			break;
		case 2:
			if(map.isPassable(x, y, 2))		return 2;
			else if(map.isPassable(x, y, 1))return 1;
			else if(map.isPassable(x, y, 3))return 3;
			break;
		case 3:
			if(map.isPassable(x, y, 3))		return 3;
			else if(map.isPassable(x, y, 2))return 2;
			else if(map.isPassable(x, y, 4))return 4;
			break;
		case 4:
			if(map.isPassable(x, y, 4))		return 4;
			else if(map.isPassable(x, y, 3))return 3;
			else if(map.isPassable(x, y, 5))return 5;
			break;
		case 5:
			if(map.isPassable(x, y, 5))		return 5;
			else if(map.isPassable(x, y, 4))return 4;
			else if(map.isPassable(x, y, 6))return 6;
			break;
		case 6:
			if(map.isPassable(x, y, 6))		return 6;
			else if(map.isPassable(x, y, 5))return 5;
			else if(map.isPassable(x, y, 7))return 7;
			break;
		case 7:
			if(map.isPassable(x, y, 7))		return 7;
			else if(map.isPassable(x, y, 6))return 6;
			else if(map.isPassable(x, y, 0))return 0;
			break;
		case 0:
			if(map.isPassable(x, y, 0))		return 0;
			else if(map.isPassable(x, y, 7))return 7;
			else if(map.isPassable(x, y, 1))return 1;
			break;
		default:break;
		}
		return -1;
	}
}

