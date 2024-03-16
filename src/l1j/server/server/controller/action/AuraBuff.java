package l1j.server.server.controller.action;

import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;

/**
 * 아우라 버프 컨트롤러
 * @author LinOffice
 */
public class AuraBuff implements ControllerInterface {
	private static class newInstance {
		public static final AuraBuff INSTANCE = new AuraBuff();
	}
	public static AuraBuff getInstance() {
		return newInstance.INSTANCE;
	}
	private AuraBuff(){}

	@Override
	public void execute() {
	}

	@Override
	public void execute(L1PcInstance pc) {
		if (pc.getNetConnection() == null || pc.getRobotAi() != null || pc.isPrivateShop() || pc.isAutoClanjoin() || pc.noPlayerCK || pc.isPassiveStatus(L1PassiveId.AURA)) {
			return;
		}
		boolean crownSearch = false;
		boolean currentAura	= pc.getSkill().isAuraBuff();
		if (pc.isInParty() && pc.getParty().getList().size() > 1) {
			crownSearch = memberAuraCheck(pc);
		}
		if (crownSearch && !currentAura) {
			ablityAura(pc, true);
		} else if (!crownSearch && currentAura) {
			ablityAura(pc, false);
		}
	}
	
	private boolean memberAuraCheck(L1PcInstance pc){
		for (L1PcInstance member : pc.getParty().getList()) {
			if (member == null || !member.isCrown() || member.isAutoClanjoin() || member.noPlayerCK || member == pc || !member.isPassiveStatus(L1PassiveId.AURA)) {
				continue;
			}
			if (member.getLocation().getTileLineDistance(pc.getLocation()) <= 18) {
				return true;
			}
		}
		return false;
	}
	private void ablityAura(L1PcInstance pc, boolean active) {
		pc.getSkill().setAuraBuff(active);
		pc.getResistance().addMr(active ? 10 : -10);
		pc.getResistance().addHitupAll(active ? 2 : -2);
		pc.getAbility().addAddedInt(active ? (byte) 1 : (byte) -1);
		pc.getAbility().addAddedDex(active ? (byte) 1 : (byte) -1);
		pc.getAbility().addAddedStr(active ? (byte) 1 : (byte) -1);
		pc.sendPackets(new S_OwnCharStatus2(pc), true);
		pc.sendPackets(active ? S_PacketBox.AURA_BUFF_ON : S_PacketBox.AURA_BUFF_OFF);
	}

}

